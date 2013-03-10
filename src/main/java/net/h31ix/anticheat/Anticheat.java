/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012-2013 AntiCheat Team | http://gravitydevelopment.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.h31ix.anticheat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import net.h31ix.anticheat.event.*;
import net.h31ix.anticheat.manage.AnticheatManager;
import net.h31ix.anticheat.manage.CheckType;
import net.h31ix.anticheat.manage.User;
import net.h31ix.anticheat.metrics.Metrics;
import net.h31ix.anticheat.metrics.Metrics.Graph;
import net.h31ix.anticheat.update.Updater;
import net.h31ix.anticheat.util.Configuration;
import net.h31ix.anticheat.util.Utilities;
import net.h31ix.anticheat.xray.XRayListener;
import net.h31ix.anticheat.xray.XRayTracker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Anticheat extends JavaPlugin {

    private static AnticheatManager manager;
    private static List<Listener> eventList = new ArrayList<Listener>();
    private static boolean update = false;
    private static Configuration config;
    private static boolean verbose;
    private static Metrics metrics;
    private static final long XRAY_TIME = 1200;

    @Override
    public void onEnable() {
            manager = new AnticheatManager(this, getLogger());
            eventList.add(new PlayerListener());
            eventList.add(new BlockListener());
            eventList.add(new EntityListener());
            eventList.add(new VehicleListener());
            eventList.add(new InventoryListener());
            // Order is important in some cases, don't screw with these unless needed, especially config
            setupConfig();
            // Xray must come before events
            setupXray();
            setupEvents();
            setupCommands();
            setupUpdater();
            setupMetrics();
            restoreLevels();
            // Test for compatibility for 1.4.5-R1.0
            try {
                Material.AIR.isSolid();
            } catch (NoSuchMethodError error) {
                getLogger().severe("You are using an outdated version of CraftBukkit that does not contain the API required for AntiCheat to run properly.");
                getLogger().severe("Please update to CraftBukkit 1.4.5-R1.0 or higher by visiting dl.bukkit.org and hitting the green download button.");
                getLogger().severe("AntiCheat will now be disabled, and will not protect your server until it is updated.");
                getPluginLoader().disablePlugin(this);
                return;
            }
            // End test
            if (verbose) {
                getLogger().log(Level.INFO, "Finished loading.");
            }
    }

    @Override
    public void onDisable() {
            AnticheatManager.close();
            for (User user : manager.getUserManager().getUsers()) {
                config.saveLevel(user.getName(), user.getLevel());
            }
            config.saveLevels();
            getServer().getScheduler().cancelTasks(this);
            cleanup();
    }

    private boolean save(InputStream in, File file) {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    OutputStream out = new FileOutputStream(file);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.close();
                    in.close();
                } catch (Exception e) {
                    getLogger().severe("Unable to save files. Is the directory writable?");
                }
                if (verbose) {
                    getLogger().log(Level.INFO, file.getName() + " created.");
                }
                return true;
            }
            return false;
    }

    private void setupXray() {
            final XRayTracker xtracker = manager.getXRayTracker();
            if (config.logXRay()) {
                eventList.add(new XRayListener());
                if (config.alertXRay()) {
                    getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
                        @Override
                        public void run() {
                            for (Player player : getServer().getOnlinePlayers()) {
                                String name = player.getName();
                                if (!xtracker.hasAlerted(name) && xtracker.sufficientData(name) && xtracker.hasAbnormal(name)) {
                                    List<String> alert = new ArrayList<String>();
                                    alert.add(ChatColor.YELLOW + "[ALERT] " + ChatColor.WHITE + name + ChatColor.YELLOW + " might be using xray.");
                                    alert.add(ChatColor.YELLOW + "[ALERT] Please check their xray stats using " + ChatColor.WHITE + "/anticheat xray " + name + ChatColor.YELLOW + ".");
                                    Utilities.alert(alert);
                                    xtracker.logAlert(name);
                                }
                            }
                        }
                    }, XRAY_TIME, XRAY_TIME);

                    if (verbose) {
                        getLogger().log(Level.INFO, "Scheduled the XRay checker.");
                    }
                }
            }
    }

    private void setupEvents() {
            for (Listener listener : eventList) {
                getServer().getPluginManager().registerEvents(listener, this);
                if (verbose) {
                    getLogger().log(Level.INFO, "Registered events for ".concat(listener.toString().split("@")[0].split(".anticheat.")[1]));
                }
            }
    }

    private void setupCommands() {
            getCommand("anticheat").setExecutor(new CommandHandler());
            if (verbose) {
                getLogger().log(Level.INFO, "Registered commands.");
            }
    }

    private void setupUpdater() {
            if (config.autoUpdate()) {
                if (verbose) {
                    getLogger().log(Level.INFO, "Checking for a new update...");
                }
                Updater updater = new Updater(this, "anticheat", this.getFile(), Updater.UpdateType.DEFAULT, false);
                update = updater.getResult() != Updater.UpdateResult.NO_UPDATE;
                if (verbose) {
                    getLogger().log(Level.INFO, "Update available: " + update);
                }
            }
    }

    private void setupMetrics() {
            try {
                metrics = new Metrics(this);
                final EventListener listener = new EventListener();
                Graph hacksGraph = metrics.createGraph("Hacks blocked");
                for (final CheckType type : CheckType.values()) {
                    hacksGraph.addPlotter(new Metrics.Plotter(CheckType.getName(type)) {
                        @Override
                        public int getValue() {
                            return EventListener.getCheats(type);
                        }
                    });
                    listener.resetCheck(type);
                }
                Graph apiGraph = metrics.createGraph("API Usage");
                apiGraph.addPlotter(new Metrics.Plotter("Checks disabled") {
                    @Override
                    public int getValue() {
                        return manager.getCheckManager().getDisabled();
                    }
                });
                apiGraph.addPlotter(new Metrics.Plotter("Players exempted") {
                    @Override
                    public int getValue() {
                        return manager.getCheckManager().getExempt();
                    }
                });
                metrics.start();
                if (verbose) {
                    getLogger().log(Level.INFO, "Metrics started.");
                }
            } catch (IOException ex) {}
    }

    private void setupConfig() {
            config = manager.getConfiguration();
            checkConfigs();
            config.load();
            verbose = config.verboseStartup();
            if (verbose) {
                getLogger().log(Level.INFO, "Setup the config.");
            }
    }

    private void restoreLevels() {
            for (Player player : getServer().getOnlinePlayers()) {
                String name = player.getName();
                manager.getUserManager().addUser(new User(player.getName(), config.getLevel(name)));
                if (verbose) {
                    getLogger().log(Level.INFO, "Data for " + player.getName() + " re-applied from flatfile");
                }
            }
    }

    public void checkConfigs() {
            save(getResource("config.yml"), new File(getDataFolder() + "/config.yml"));
            save(getResource("lang.yml"), new File(getDataFolder() + "/lang.yml"));
            save(getResource("magic.yml"), new File(getDataFolder() + "/magic.yml"));
            if(save(getResource("events.yml"), new File(getDataFolder() + "/events.yml"))) {
                config.setUpdateEvents();
            }
    }

    public static Anticheat getPlugin() {
            return manager.getPlugin();
    }

    public static AnticheatManager getManager() {
            return manager;
    }

    public static boolean isUpdated() {
            return !update;
    }

    public static String getVersion() {
            return manager.getPlugin().getDescription().getVersion();
    }

    private void cleanup() {
            eventList = null;
            manager = null;
            config = null;
            metrics = null;
    }
}

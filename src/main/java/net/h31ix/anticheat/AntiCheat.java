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

import com.comphenix.protocol.ProtocolLibrary;
import net.h31ix.anticheat.config.Configuration;
import net.h31ix.anticheat.event.*;
import net.h31ix.anticheat.manage.AnticheatManager;
import net.h31ix.anticheat.manage.PacketManager;
import net.h31ix.anticheat.manage.User;
import net.h31ix.anticheat.update.Updater;
import net.h31ix.anticheat.util.Utilities;
import net.h31ix.anticheat.xray.XRayListener;
import net.h31ix.anticheat.xray.XRayTracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class AntiCheat extends JavaPlugin {

    private static AnticheatManager manager;
    private static AntiCheat plugin;
    private static List<Listener> eventList = new ArrayList<Listener>();
    private static boolean update = false;
    private static Configuration config;
    private static boolean verbose;
    private static boolean developer;
    private static final long XRAY_TIME = 1200;
    private static PacketManager packetManager;
    private static boolean protocolLib = false;

    @Override
    public void onEnable() {
        plugin = this;
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
        setupProtocol();
        // Enterprise must come before levels
        setupEnterprise();
        restoreLevels();
        // Check if NoCheatPlus is installed
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            public void run() {
                if (Bukkit.getPluginManager().getPlugin("NoCheatPlus") != null) {
                    getLogger().severe("You are also running NoCheatPlus!");
                    getLogger().severe("NoCheatPlus has been known to conflict with AntiCheat's results and create false cheat detections.");
                    getLogger().severe("Please remove or disable NoCheatPlus to silence this warning.");
                }
            }
        }, 40L);
        // End tests
        verboseLog("Finished loading.");
    }

    @Override
    public void onDisable() {
        verboseLog("Saving user levels...");
        config.getLevels().saveLevelsFromUsers(getManager().getUserManager().getUsers());

        AnticheatManager.close();
        getServer().getScheduler().cancelTasks(this);
        cleanup();
    }

    private void setupProtocol() {
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            protocolLib = true;
            packetManager = new PacketManager(ProtocolLibrary.getProtocolManager(), this, manager);
            verboseLog("Hooked into ProtocolLib");
        }
    }

    private void setupXray() {
        final XRayTracker xtracker = manager.getXRayTracker();
        if (config.getConfig().logXRayStats.getValue()) {
            eventList.add(new XRayListener());
            if (config.getConfig().alertWhenXRayIsFound.getValue()) {
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

                verboseLog("Scheduled the XRay checker.");
            }
        }
    }

    private void setupEvents() {
        for (Listener listener : eventList) {
            getServer().getPluginManager().registerEvents(listener, this);
            verboseLog("Registered events for ".concat(listener.toString().split("@")[0].split(".anticheat.")[1]));
        }
    }

    private void setupCommands() {
        getCommand("anticheat").setExecutor(new CommandHandler());
        verboseLog("Registered commands.");
    }

    private void setupUpdater() {
        if (config.getConfig().autoUpdate.getValue()) {
            verboseLog("Checking for a new update...");
            Updater updater = new Updater(this, "anticheat", this.getFile(), Updater.UpdateType.DEFAULT, false);
            update = updater.getResult() != Updater.UpdateResult.NO_UPDATE;
            verboseLog("Update available: " + update);
        }
    }

    private void setupConfig() {
        config = manager.getConfiguration();
        verbose = config.getConfig().verboseStartup.getValue();
        verboseLog("Setup the config.");
    }

    private void setupEnterprise() {
        if (config.getConfig().enterprise.getValue()) {
            if (config.getEnterprise().loggingEnabled.getValue()) {
                config.getEnterprise().database.cleanEvents();
            }
        }
    }

    private void restoreLevels() {
        for (Player player : getServer().getOnlinePlayers()) {
            String name = player.getName();

            User user = new User(name);
            user.setIsWaitingOnLevelSync(true);
            config.getLevels().loadLevelToUser(user);

            manager.getUserManager().addUser(user);
            verboseLog("Data for " + name + " loaded");
        }
    }

    public static AntiCheat getPlugin() {
        return plugin;
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
    }

    public static boolean developerMode() {
        return developer;
    }

    public static void setDeveloperMode(boolean b) {
        developer = b;
    }

    public static boolean isUsingProtocolLib() {
        return protocolLib;
    }

    public static void debugLog(final String string) {
        Bukkit.getScheduler().runTask(getPlugin(), new Runnable() {
            public void run() {
                if (developer) {
                    manager.log("[DEBUG] " + string);
                }
            }
        });
    }

    public void verboseLog(final String string) {
        if (verbose) {
            getLogger().info(string);
        }
    }
}

/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 AntiCheat Team | http://h31ix.net
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

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.h31ix.anticheat.event.*;
import net.h31ix.anticheat.manage.AnticheatManager;
import net.h31ix.anticheat.manage.CheckType;
import net.h31ix.anticheat.metrics.Metrics;
import net.h31ix.anticheat.metrics.Metrics.Graph;
import net.h31ix.anticheat.util.Configuration;
import net.h31ix.anticheat.util.Utilities;
import net.h31ix.anticheat.xray.XRayListener;
import net.h31ix.anticheat.xray.XRayTracker;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Anticheat extends JavaPlugin
{
    private static AnticheatManager manager;
    private static List<Listener> eventList = new ArrayList<Listener>();
    private static boolean update = false;
    private static final int BYTE_SIZE = 1024;
    private static Logger logger;
    private static Configuration config;
    private static boolean verbose;
    private static String updateFolder;
    private static Metrics metrics;
    private static final long XRAY_TIME = 1200;

    @Override
    public void onDisable()
    {
        AnticheatManager.close();
        Map<String, Integer> map = manager.getPlayerManager().getLevels();
        Iterator<String> set = map.keySet().iterator();
        while (set.hasNext())
        {
            String player = set.next();
            config.saveLevel(player, map.get(player));
        }
        config.saveLevels();
        getServer().getScheduler().cancelAllTasks();
        cleanup();
    }

    @Override
    public void onEnable()
    {
        logger = this.getLogger();
        manager = new AnticheatManager(this);
        config = manager.getConfiguration();
        checkConfig();
        verbose = config.verboseStartup();
        updateFolder = config.updateFolder();
        if (verbose)
        {
            logger.log(Level.INFO, "Setup the config.");
        }
        checkForUpdate();        
        eventList.add(new PlayerListener());
        eventList.add(new BlockListener());
        eventList.add(new EntityListener());
        eventList.add(new VehicleListener());
        final XRayTracker xtracker = manager.getXRayTracker();
        if (config.logXRay())
        {
            eventList.add(new XRayListener());
            if (config.alertXRay())
            {
                getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (Player player : getServer().getOnlinePlayers())
                        {
                            String name = player.getName();
                            if (!xtracker.hasAlerted(name) && xtracker.sufficientData(name) && xtracker.hasAbnormal(name))
                            {
                                String[] alert = new String[2];
                                alert[0] = ChatColor.YELLOW + "[ALERT] " + ChatColor.WHITE + name + ChatColor.YELLOW + " might be using xray.";
                                alert[1] = ChatColor.YELLOW + "[ALERT] Please check their xray stats using " + ChatColor.WHITE + "/anticheat xray " + name + ChatColor.YELLOW + ".";
                                Utilities.alert(alert);
                                xtracker.logAlert(name);
                            }
                        }
                    }
                }, XRAY_TIME, XRAY_TIME);
                if (verbose)
                {
                    logger.log(Level.INFO, "Scheduled the XRay checker.");
                }
            }
        }       
        for (Listener listener : eventList)
        {
            getServer().getPluginManager().registerEvents(listener, this);
            if (verbose)
            {
                logger.log(Level.INFO, "Registered events for ".concat(listener.toString()));
            }
        }
        getCommand("anticheat").setExecutor(new CommandHandler());
        if (verbose)
        {
            logger.log(Level.INFO, "Registered commands.");
        }
        if (update && config.autoUpdate())
        {
            if (verbose)
            {
                logger.log(Level.INFO, "Downloading the new update...");
            }
            File file = new File("plugins/" + updateFolder);
            if (!file.exists())
            {
                try
                {
                    file.mkdir();
                }
                catch (Exception ex)
                {
                }
            }
            try
            {
                saveFile(file.getCanonicalPath() + "/AntiCheat.jar", "http://dl.dropbox.com/u/38228324/AntiCheat.jar");
            }
            catch (IOException ex)
            {
            }
        }
        try
        {
            metrics = new Metrics(this);
            final EventListener listener = new EventListener();
            Graph hacksGraph = metrics.createGraph("Hacks blocked");
            for (final CheckType type : CheckType.values())
            {
                hacksGraph.addPlotter(new Metrics.Plotter(CheckType.getName(type))
                {
                    @Override
                    public int getValue()
                    {
                        return listener.getCheats(type);
                    }
                });
                listener.resetCheck(type);
            }
            Graph apiGraph = metrics.createGraph("API Usage");
            apiGraph.addPlotter(new Metrics.Plotter("Checks disabled")
            {
                @Override
                public int getValue()
                {
                    return manager.getCheckManager().getDisabled();
                }
            });
            apiGraph.addPlotter(new Metrics.Plotter("Players exempted")
            {
                @Override
                public int getValue()
                {
                    return manager.getCheckManager().getExempt();
                }
            });
            metrics.start();
            if (verbose)
            {
                logger.log(Level.INFO, "Metrics started.");
            }
        }
        catch (IOException ex)
        {
        }
        for (Player player : getServer().getOnlinePlayers())
        {
            String name = player.getName();
            manager.getPlayerManager().setLevel(player, config.getLevel(name));
            if (verbose)
            {
                logger.log(Level.INFO, "Data for " + player.getName() + " re-applied from flatfile");
            }
        }
        String v = this.getServer().getVersion().split("-")[6];
        if(v.contains("jnks"))
        {
            v = v.replaceAll("b", "").replaceAll("jnks", "").split(" ")[0];
            int build  = 0;
            try 
            {
                build = Integer.parseInt(v);
            } 
            catch(Exception ex){ }
            if(build != 0 && build < 2287)
            {
                System.out.println("[WARNING] You are using an UNSUPPORTED version of CraftBukkit that does not contain the API required to run AntiCheat.");
                System.out.println("[WARNING] Please update your server to be build #2287 or higher at http://dl.bukkit.org/downloads/craftbukkit/");
                System.out.println("[WARNING] AntiCheat will now disable itself, and your server will not be protected until you update.");
                getServer().getPluginManager().disablePlugin(this);
            }
        }        
        if (verbose)
        {
            logger.log(Level.INFO, "Finished loading.");
        }
    }

    private void saveFile(String file, String url)
    {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try
        {
            in = new BufferedInputStream(new URL(url).openStream());
            fout = new FileOutputStream(file);

            byte[] data = new byte[BYTE_SIZE];
            int count;
            while ((count = in.read(data, 0, BYTE_SIZE)) != -1)
            {
                fout.write(data, 0, count);
            }
        }
        catch (Exception ex)
        {
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
                if (fout != null)
                {
                    fout.close();
                }
            }
            catch (Exception ex)
            {
            }
            if (verbose)
            {
                logger.log(Level.INFO, "AntiCheat update has been downloaded and will be installed on next launch.");
            }
        }
    }

    public void checkConfig()
    {
        if (!new File(getDataFolder() + "/config.yml").exists())
        {
            saveDefaultConfig();
            if (verbose)
            {
                logger.log(Level.INFO, "Config file created.");
            }
        }
        if (!new File(getDataFolder() + "/lang.yml").exists())
        {
            saveResource("lang.yml", false);
            if (verbose)
            {
                logger.log(Level.INFO, "Lang file created.");
            }
        }
    }

    private void checkForUpdate()
    {
        if (verbose)
        {
            logger.log(Level.INFO, "Checking for updates...");
        }
        try
        {
            URL url;
            URLConnection urlConn;
            InputStreamReader inStream = null;
            BufferedReader buff;
            String v = "";
            try
            {
                url = new URL("http://dl.dropbox.com/u/38228324/anticheatVersion.txt");
                urlConn = url.openConnection();
                inStream = new InputStreamReader(urlConn.getInputStream());
            }
            catch (Exception ex)
            {
            }
            buff = new BufferedReader(inStream);
            try
            {
                v = buff.readLine();
                urlConn = null;
                inStream = null;
                buff.close();
                buff = null;
            }
            catch (Exception ex)
            {
            }
            String version = this.getDescription().getVersion().split("-b")[0];
            if (!version.equalsIgnoreCase(v))
            {
                if (version.endsWith("-PRE") || version.endsWith("-DEV"))
                {
                    if (version.replaceAll("-PRE", "").replaceAll("-DEV", "").equalsIgnoreCase(v))
                    {
                        update = true;
                        if (verbose)
                        {
                            logger.log(Level.INFO, "Your dev build has been promoted to release. Downloading the update.");
                        }
                    }
                    else
                    {
                        update = false;
                        if (verbose)
                        {
                            logger.log(Level.INFO, "Dev build detected, so skipping update checking until this version is released.");
                        }
                    }
                }
                else
                {
                    update = true;
                    if (verbose)
                    {
                        logger.log(Level.INFO, "An update was found.");
                    }
                }
            }
            else
            {
                if (verbose)
                {
                    logger.log(Level.INFO, "No update found.");
                }
                update = false;
            }
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, "Updating failed.  Possible connection failure.");
        }
    }

    public static Anticheat getPlugin()
    {
        return manager.getPlugin();
    }

    public static AnticheatManager getManager()
    {
        return manager;
    }

    public static boolean isUpdated()
    {
        return !update;
    }

    public static String getVersion()
    {
        return manager.getPlugin().getDescription().getVersion();
    }

    public Logger getAnticheatLogger()
    {
        return this.getLogger();
    }
    private void cleanup()
    {
        eventList = null;
        manager = null;
        config = null;
        metrics = null;
        logger = null;
    }
}

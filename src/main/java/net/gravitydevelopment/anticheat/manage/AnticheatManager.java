/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012-2014 AntiCheat Team | http://gravitydevelopment.net
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

package net.gravitydevelopment.anticheat.manage;

import net.gravitydevelopment.anticheat.AntiCheat;
import net.gravitydevelopment.anticheat.config.Configuration;
import net.gravitydevelopment.anticheat.util.FileFormatter;
import net.gravitydevelopment.anticheat.xray.XRayTracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredListener;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The internal hub for all managers.
 */

public class AnticheatManager {
    private static AntiCheat plugin = null;
    private static Configuration configuration;
    private static XRayTracker xrayTracker = null;
    private static UserManager userManager = null;
    private static CheckManager checkManager = null;
    private static Backend backend = null;
    private final Logger fileLogger;
    private static Map<String, RegisteredListener[]> eventchains = new ConcurrentHashMap<String, RegisteredListener[]>();
    private static Map<String, Long> eventcache = new ConcurrentHashMap<String, Long>();
    private static List<String> logs = new CopyOnWriteArrayList<String>();
    private static Handler fileHandler;
    private static final int LOG_LEVEL_HIGH = 3;

    public AnticheatManager(AntiCheat instance, Logger logger) {
        plugin = instance;
        // now load all the others!!!!!
        fileLogger = Logger.getLogger("net.gravitydevelopment.anticheat.AntiCheat");
        configuration = new Configuration(plugin);
        xrayTracker = new XRayTracker();
        userManager = new UserManager(configuration);
        checkManager = new CheckManager(this, configuration);
        backend = new Backend(this);
        try {
            File file = new File(plugin.getDataFolder(), "log");
            if (!file.exists()) {
                file.mkdir();
            }
            fileHandler = new FileHandler(plugin.getDataFolder() + "/log/anticheat.log", true);
            fileHandler.setFormatter(new FileFormatter());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        fileLogger.setUseParentHandlers(false);
        fileLogger.addHandler(fileHandler);
    }

    public void log(String message) {
        log(message, 0);
    }

    public void debugLog(String message) {
        Bukkit.getConsoleSender().sendMessage("[AntiCheat] " + ChatColor.GRAY + message);
        logs.add(ChatColor.stripColor(message));
    }

    public void log(String message, int i) {
        if (i != 1 && getConfiguration().getConfig().logToConsole.getValue()) {
            Bukkit.getConsoleSender().sendMessage("[AntiCheat] " + ChatColor.RED + message);
        }
        if (i == 0 && getConfiguration().getConfig().fileLogLevel.getValue() == LOG_LEVEL_HIGH) { // Not an alert, normal log message
            fileLog(message);
        } else if (getConfiguration().getConfig().fileLogLevel.getValue() != 0) { // alert
            fileLog(message);
        }
        logs.add(ChatColor.stripColor(message));
    }

    public void fileLog(String message) {
        fileLogger.info(message);
    }

    public List<String> getLastLogs() {
        List<String> log = new CopyOnWriteArrayList<String>();
        if (logs.size() < 30) {
            return logs;
        }
        for (int i = logs.size() - 1; i >= 0; i--) {
            log.add(logs.get(i));
        }
        logs.clear();
        return log;
    }

    public void addEvent(String e, RegisteredListener[] arr) {
        if (!configuration.getConfig().eventChains.getValue())
            return;
        if (!eventcache.containsKey(e) || eventcache.get(e) > 30000L) {
            eventchains.put(e, arr);
            eventcache.put(e, System.currentTimeMillis());
        }
    }

    public String getEventChainReport() {
        String gen = "";
        if (!configuration.getConfig().eventChains.getValue()) {
            return "Event Chains is disabled by the configuration." + '\n';
        }

        if (eventchains.entrySet().size() == 0) {
            return "No event chains found." + '\n';
        }

        for (Entry<String, RegisteredListener[]> e : eventchains.entrySet()) {
            String toadd = "";
            String ename = e.getKey();

            toadd += ename + ":" + '\n';

            RegisteredListener[] reg = e.getValue();
            for (RegisteredListener plug : reg) {
                String pluginname = plug.getPlugin().getName();
                if (pluginname.equals("AntiCheat"))
                    pluginname = "self";

                toadd += "- " + pluginname + '\n';
            }

            gen += toadd + '\n';
        }

        return gen;
    }

    public AntiCheat getPlugin() {
        return plugin;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public XRayTracker getXRayTracker() {
        return xrayTracker;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public CheckManager getCheckManager() {
        return checkManager;
    }

    public Backend getBackend() {
        return backend;
    }

    public static void close() {
        fileHandler.close();

        if (configuration.getConfig().enterprise.getValue()) {
            configuration.getEnterprise().database.shutdown();
        }
    }
}

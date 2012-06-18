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

package net.h31ix.anticheat.manage;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.util.Configuration;
import net.h31ix.anticheat.util.FileFormatter;
import net.h31ix.anticheat.xray.XRayTracker;
import org.bukkit.ChatColor;

/**
 * <p>
 * The internal hub for all managers.
 */

public class AnticheatManager
{
    private Anticheat plugin = null;
    private Configuration configuration;
    private XRayTracker xrayTracker = null;
    private PlayerManager playerManager = null;
    private CheckManager checkManager = null;
    private Backend backend = null;
    private static Logger LOGGER, FILE_LOGGER;
    private static List<String> logs = new CopyOnWriteArrayList<String>();
    private static Handler fileHandler;
    private static final int LOG_LEVEL_HIGH = 3;

    public AnticheatManager(Anticheat instance)
    {
        plugin = instance;
        // now load all the others!!!!!
        LOGGER = Logger.getLogger("Minecraft");
        FILE_LOGGER = plugin.getAnticheatLogger();
        configuration = new Configuration(this);
        xrayTracker = new XRayTracker();
        playerManager = new PlayerManager(this);
        checkManager = new CheckManager(this);
        backend = new Backend(this);
        try
        {
            File file = new File(plugin.getDataFolder() + "/log");
            if (!file.exists())
            {
                file.mkdir();
            }
            fileHandler = new FileHandler(plugin.getDataFolder() + "/log/anticheat.log", true);
            fileHandler.setFormatter(new FileFormatter());
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        FILE_LOGGER.setUseParentHandlers(false);
        FILE_LOGGER.addHandler(fileHandler);
    }

    public void log(String message)
    {
        if (getConfiguration().logConsole())
        {
            LOGGER.info("[AntiCheat] " + ChatColor.stripColor(message)); //This is temporary. Is something wrong with jline?
        }
        if (getConfiguration().getFileLogLevel() == LOG_LEVEL_HIGH)
        {
            fileLog(ChatColor.stripColor(message));
        }
        logs.add(ChatColor.stripColor(message));
    }

    public void fileLog(String message)
    {
        FILE_LOGGER.info(message);
    }

    public List<String> getLastLogs()
    {
        List<String> log = new CopyOnWriteArrayList<String>();
        if (logs.size() < 30)
            return logs;
        for(int i = 1; i < 31; i++) {
            log.add(logs.get(logs.size() - i));
        }
        logs.clear();
        return log;
    }

    public Anticheat getPlugin()
    {
        return plugin;
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }

    public XRayTracker getXRayTracker()
    {
        return xrayTracker;
    }

    public PlayerManager getPlayerManager()
    {
        return playerManager;
    }

    public CheckManager getCheckManager()
    {
        return checkManager;
    }

    public Backend getBackend()
    {
        return backend;
    }

    public static void close()
    {
        fileHandler.close();
    }
}

/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 H31IX http://h31ix.net
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
import java.io.IOException;
import java.util.logging.*;
import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.Configuration;
import net.h31ix.anticheat.util.ConsoleFormatter;
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
    private static Handler handler;
    private static final int LOG_LEVEL_HIGH = 3;
    
    public AnticheatManager(Anticheat instance)
    {
        plugin = instance;
        // now load all the others!!!!!
        LOGGER = plugin.getAnticheatLogger();
        FILE_LOGGER = LOGGER;
        configuration = new Configuration(this);
        xrayTracker = new XRayTracker();
        playerManager = new PlayerManager(this);
        checkManager = new CheckManager(this);
        backend = new Backend(this);
        try 
        {
            File file = new File(plugin.getDataFolder()+"/log");
            if(!file.exists())
            {
                file.mkdir();
            }
            handler = new FileHandler(plugin.getDataFolder()+"/log/anticheat.log.%u.txt",true);
            handler.setFormatter(new FileFormatter());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        FILE_LOGGER.setUseParentHandlers(false);
        FILE_LOGGER.addHandler(handler);
        LOGGER.setUseParentHandlers(false);
        Handler chandler = new ConsoleHandler();
        chandler.setFormatter(new ConsoleFormatter());
        LOGGER.addHandler(chandler);
    }
    
    public void log(String message)
    {
        if(getConfiguration().logConsole())
        {
            LOGGER.warning(ChatColor.stripColor(message)); //This is temporary. Is something wrong with jline?
        }       
        if(getConfiguration().getFileLogLevel() == LOG_LEVEL_HIGH)
        {
            fileLog(ChatColor.stripColor(message));
        }
    }
    
    public void fileLog(String message)
    {
        FILE_LOGGER.warning(message);
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
}

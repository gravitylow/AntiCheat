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
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.logging.SimpleFormatter;
import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.Configuration;
import net.h31ix.anticheat.xray.XRayTracker;

/**
 * <p>
 * The internal hub for all managers.
 */

public class AnticheatManager 
{
    private Anticheat plugin = null;
    private Configuration CONFIGURATION = null;
    private XRayTracker XRAY_TRACKER = null;
    private PlayerManager PLAYER_MANAGER = null;
    private CheckManager CHECK_MANAGER = null;
    private Backend BACKEND = null;
    private static Logger LOGGER = null;
    private static Logger FILE_LOGGER = null;
    private static Handler HANDLER;
    
    public AnticheatManager(Anticheat instance)
    {
        plugin = instance;
        // now load all the others!!!!!
        CONFIGURATION = new Configuration(this);
        XRAY_TRACKER = new XRayTracker();
        PLAYER_MANAGER = new PlayerManager(this);
        CHECK_MANAGER = new CheckManager(this);
        BACKEND = new Backend(this);
        LOGGER = Logger.getLogger("Minecraft");
        FILE_LOGGER = Logger.getLogger(AnticheatManager.class.getName());
        try 
        {
            File file = new File(plugin.getDataFolder()+"/log");
            if(!file.exists())
            {
                file.mkdir();
            }
            HANDLER = new FileHandler(plugin.getDataFolder()+"/log/anticheat.log.%u.txt",true);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        HANDLER.setFormatter(new SimpleFormatter());
        FILE_LOGGER.setUseParentHandlers(false);
        FILE_LOGGER.addHandler(HANDLER);
    }
    
    public void log(String message)
    {
        if(getConfiguration().logConsole())
        {
            LOGGER.log(Level.WARNING,"[AC] ".concat(message));
        }
        FILE_LOGGER.log(Level.WARNING, message);
    }
    
    public Anticheat getPlugin() 
    {
    	return plugin;
    }
    
    public Configuration getConfiguration() 
    {
    	return CONFIGURATION;
    }
    
    public XRayTracker getXRayTracker() 
    {
    	return XRAY_TRACKER;
    }
    
    public PlayerManager getPlayerManager() 
    {
    	return PLAYER_MANAGER;
    }
    
    public CheckManager getCheckManager()
    {
    	return CHECK_MANAGER;
    }
    
    public Backend getBackend() 
    {
    	return BACKEND;
    }
}

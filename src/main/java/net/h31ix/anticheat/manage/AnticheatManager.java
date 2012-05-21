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

import java.util.logging.Level;
import java.util.logging.Logger;

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
    private Logger LOGGER = null;
	
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
    }
    
    public void log(String message)
    {
       LOGGER.log(Level.WARNING,"[AC] ".concat(message));
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

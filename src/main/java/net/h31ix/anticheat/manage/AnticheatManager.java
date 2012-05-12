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
import net.h31ix.anticheat.Configuration;
import net.h31ix.anticheat.xray.XRayTracker;

public final class AnticheatManager 
{
    private AnticheatManager()
    {
        
    }
    
    public static final Configuration CONFIGURATION = new Configuration();
    public static final XRayTracker XRAY_TRACKER = new XRayTracker();
    public static final PlayerManager PLAYER_MANAGER = new PlayerManager();
    public static final CheckManager CHECK_MANAGER = new CheckManager();
    public static final Backend BACKEND = new Backend();
    private static final Logger ANTICHEAT_LOGGER = Logger.getLogger("Minecraft");
    
    public static void log(String message)
    {
        ANTICHEAT_LOGGER.log(Level.WARNING,"[AC] ".concat(message));
    }
}

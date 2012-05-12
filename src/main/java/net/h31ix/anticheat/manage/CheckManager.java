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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

/**
 * <p>
 * The manager that AntiCheat will check with to see if it should watch certain checks and certain players.
 */

public class CheckManager 
{
    private static List<CheckType> checkIgnoreList = new ArrayList<CheckType>();
    private static Multimap<Player,CheckType> exemptList = ArrayListMultimap.create();
    
    /**
     * Start running a certain check
     *
     * @param  type  Check to start watching for
     */         
    public void activateCheck(CheckType type)
    {
        AnticheatManager.log("The "+type.toString()+" check was activated.");
        checkIgnoreList.remove(type);
    }
    
    /**
     * Stop running a certain check
     *
     * @param  type  Check to stop watching for
     */      
    public void deactivateCheck(CheckType type)
    {
        AnticheatManager.log("The "+type.toString()+" check was deactivated.");
        checkIgnoreList.add(type);
    }
    
    /**
     * Find out if a check is currently being watched for
     *
     * @param  type  Type to check
     * @return true if plugin is watching for this check
     */     
    public boolean isActive(CheckType type)
    {
        return !checkIgnoreList.contains(type);
    }
    
    /**
     * Allow a player to skip a certain check
     *
     * @param  player  Player to stop watching
     * @param  type  Check to stop watching for
     */      
    public void exemptPlayer(Player player, CheckType type)
    {
        AnticheatManager.log(player.getName()+" was exempted from the "+type.toString()+" check.");
        exemptList.put(player,type);
    }
  
    /**
     * Stop allowing a player to skip a certain check
     *
     * @param  player  Player to start watching
     * @param  type  Check to start watching for
     */         
    public void unexemptPlayer(Player player, CheckType type)
    {
        AnticheatManager.log(player.getName()+" was re-added to the "+type.toString()+" check.");
        exemptList.remove(type, type);
    }
    
    /**
     * Find out if a player is currently exempt from a certain check
     *
     * @param  player  Player to check
     * @param  type  Type to check
     * @return true if plugin is ignoring this check on this player
     */     
    public boolean isExempt(Player player, CheckType type)
    {
        return exemptList.containsEntry(player, type);
    }
    
    private boolean hasPermission(Player player, String permission)
    {
        return player.hasPermission(permission);
    }
    
    /**
     * Find out if a check will occur for a player. This checks if they are being tracked, the check is active, the player isn't exempt from the check, and the player doesn't have override permission. 
     *
     * @param  player  Player to check
     * @param  type  Type to check
     * @return true if plugin will check this player, and that all things allow it to happen.
     */      
    public boolean willCheck(Player player, CheckType type)
    {        
        return  isActive(type) 
                && AnticheatManager.CONFIGURATION.checkInWorld(player.getWorld())
                && !isExempt(player, type) 
                && !hasPermission(player, type.getPermission());
    }
}

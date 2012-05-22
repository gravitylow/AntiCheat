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
    public AnticheatManager manager = null;
    private static List<CheckType> checkIgnoreList = new ArrayList<CheckType>();
    private static Multimap<Player,CheckType> exemptList = ArrayListMultimap.create();
    
    public CheckManager(AnticheatManager instance) 
    {
    	manager = instance;
    }
            
    public void activateCheck(CheckType type)
    {
        manager.log("The "+type.toString()+" check was activated.");
        checkIgnoreList.remove(type);
    }
          
    public void deactivateCheck(CheckType type)
    {
        manager.log("The "+type.toString()+" check was deactivated.");
        checkIgnoreList.add(type);
    }
        
    public boolean isActive(CheckType type)
    {
        return !checkIgnoreList.contains(type);
    }
         
    public void exemptPlayer(Player player, CheckType type)
    {
        manager.log(player.getName()+" was exempted from the "+type.toString()+" check.");
        exemptList.put(player,type);
    }
         
    public void unexemptPlayer(Player player, CheckType type)
    {
        manager.log(player.getName()+" was re-added to the "+type.toString()+" check.");
        exemptList.remove(type, type);
    }
       
    public boolean isExempt(Player player, CheckType type)
    {
        return exemptList.containsEntry(player, type);
    }
    
    private boolean hasPermission(Player player, String permission)
    {
        return player.hasPermission(permission);
    }
     
    public boolean willCheck(Player player, CheckType type)
    {        
        return  isActive(type) 
                && manager.getConfiguration().checkInWorld(player.getWorld())
                && !isExempt(player, type) 
                && !hasPermission(player, type.getPermission());
    }
}

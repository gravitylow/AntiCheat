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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * <p>
 * The manager that AntiCheat will check with to see if it should watch certain
 * checks and certain players.
 */

public class CheckManager
{
    private AnticheatManager manager = null;
    private static List<CheckType> checkIgnoreList = new ArrayList<CheckType>();
    private static Map<String, List<CheckType>> exemptList = new HashMap<String, List<CheckType>>();
    private static int disabled = 0;
    private static int exempt = 0;

    public CheckManager(AnticheatManager instance)
    {
        manager = instance;
    }

    public void activateCheck(CheckType type)
    {
        if (isActive(type))
        {
            manager.log("The " + type.toString() + " check was activated.");
            checkIgnoreList.remove(type);
        }
    }

    public void deactivateCheck(CheckType type)
    {
        if (!isActive(type))
        {        
            manager.log("The " + type.toString() + " check was deactivated.");
            checkIgnoreList.add(type);
            disabled++;
        }
    }

    public boolean isActive(CheckType type)
    {
        return !checkIgnoreList.contains(type);
    }

    public void exemptPlayer(Player player, CheckType type)
    {
        if (!isExempt(player, type))
        {
            if(!exemptList.containsKey(player.getName()))
            {
                exemptList.put(player.getName(), new ArrayList<CheckType>());
            }
            manager.log(player.getName() + " was exempted from the " + type.toString() + " check.");
            exemptList.get(player.getName()).add(type);
            exempt++;
        }
    }

    public void unexemptPlayer(Player player, CheckType type)
    {
        if (isExempt(player, type))
        {
            manager.log(player.getName() + " was re-added to the " + type.toString() + " check.");
            exemptList.get(player.getName()).remove(type);
        }
    }

    public boolean isExempt(Player player, CheckType type)
    {
        return exemptList.containsKey(player.getName()) ? exemptList.get(player.getName()).contains(type) : false;
    }
    
    public boolean isOpExempt(Player player)
    {
        return (this.manager.getConfiguration().opExempt() && player.isOp());
    }

    public boolean willCheck(Player player, CheckType type)
    {
        return isActive(type) && manager.getConfiguration().checkInWorld(player.getWorld()) && !isExempt(player, type) && !type.checkPermission(player) && isOnline(player) && !isOpExempt(player);
    }
    
    public boolean isOnline(Player player)
    {
        // Check if the player is on the user list, e.g. is not an NPC
        for(Player p : Bukkit.getOnlinePlayers())
        {
            if(p.getName().equals(player.getName()))
            {
                return true;
            }
        }
        return false;
    }

    public int getExempt()
    {
        int x = exempt;
        exempt = 0;
        return x;
    }

    public int getDisabled()
    {
        int x = disabled;
        disabled = 0;
        return x;
    }
}

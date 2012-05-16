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

package net.h31ix.anticheat.event;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.manage.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class EventListener implements Listener 
{
    private static final Map<CheckType,Integer> usageList = new EnumMap<CheckType,Integer>(CheckType.class);
    private static final Map<String,Integer> decreaseList = new HashMap<String,Integer>();
    private static final CheckManager CHECK_MANAGER = AnticheatManager.CHECK_MANAGER;   
    private static final Backend BACKEND = AnticheatManager.BACKEND;  
    private static final Anticheat PLUGIN = Anticheat.getPlugin();
    private static final PlayerManager PLAYER_MANAGER = AnticheatManager.PLAYER_MANAGER;
    
    public static void log(String message,Player player, CheckType type)
    {
        if(AnticheatManager.CONFIGURATION.logConsole())
        {
            AnticheatManager.log(player.getName()+" "+message);
        }
        PLAYER_MANAGER.increaseLevel(player);
        removeDecrease(player);
        logCheat(type);
    }
    
    private static void logCheat(CheckType type)
    {
        usageList.put(type, getCheats(type)+1);
    }
    
    public static void resetCheck(CheckType type)
    {
        usageList.put(type, 0);
    }
    
    public static int getCheats(CheckType type)
    {
        int x = 0;
        if(usageList.get(type) != null)
        {
            x = usageList.get(type);
        }
        return x;
    }
    
    private static void removeDecrease(Player player)
    {
        int x = 0;
        if(decreaseList.get(player.getName()) != null)
        {
            x = decreaseList.get(player.getName());
            x-=2;
            if(x < 0)
            {
                x = 0;
            }
        }
        decreaseList.put(player.getName(), x);       
    }
    
    public static void decrease(Player player)
    {
        int x = 0;
        if(decreaseList.get(player.getName()) != null)
        {
            x = decreaseList.get(player.getName());
        }
        x+=1;
        decreaseList.put(player.getName(), x);
        if(x >= 10)
        {
            PLAYER_MANAGER.decreaseLevel(player);
            decreaseList.put(player.getName(),0);
        }
    }
    
    public static CheckManager getCheckManager()
    {
        return CHECK_MANAGER;
    }
    
    public static Backend getBackend()
    {
        return BACKEND;
    }  
    
    public static Anticheat getPlugin()
    {
        return PLUGIN;
    }    
}

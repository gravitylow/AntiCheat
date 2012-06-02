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

import java.util.HashMap;
import java.util.Map;
import net.h31ix.anticheat.Configuration;
import net.h31ix.anticheat.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * <p>
 * The manager that AntiCheat will use to monitor player's hack levels and to execute events that the admin has set
 */

public class PlayerManager 
{
    
    private static Map<String,Integer> level = new HashMap<String,Integer>();
    private static Configuration config = null;
    private static final int MED_THRESHOLD = 20;
    private static final int HIGH_THRESHOLD = 50;
    private static final int LEVEL_MAX = 60;
    private static final int LEVEL_BOOST = 5;
    
    public PlayerManager(AnticheatManager instance) {
    	config = instance.getConfiguration();
    }
    
    private static void reactMedium(Player player)
    {
        execute("Medium",player);                           
        String [] alert = new String[2];
        alert[0] = ChatColor.YELLOW+"[ALERT] "+ChatColor.WHITE+player.getName()+ChatColor.YELLOW+" has entered the MEDIUM hack level.";
        alert[1] = ChatColor.YELLOW+"[ALERT] This means they may be using a hacked client or may have a bad connection!";      
        Utilities.alert(alert);
    }
    private static void reactHigh(Player player)
    {
        execute("High",player);
        String [] alert = new String[2];
        alert[0] = ChatColor.RED+"[ALERT] "+ChatColor.WHITE+player.getName()+ChatColor.RED+" has entered the HIGH hack level.";
        alert[1] = ChatColor.RED+"[ALERT] This means they probably are hacking or are lagging out!";
        Utilities.alert(alert);              
    }
    
    public void increaseLevel(Player player)
    {
        final String name = player.getName();
        if(level.get(name) == null || level.get(name) == 0)
        {
            level.put(name,1);
        }
        else
        {
            final int playerLevel = level.get(name);
            level.put(name, playerLevel+1);
            if(playerLevel <= MED_THRESHOLD && playerLevel+1 > MED_THRESHOLD && playerLevel+1 <= HIGH_THRESHOLD)
            {
                reactMedium(player);
            }
            else if(playerLevel <= HIGH_THRESHOLD && playerLevel+1 > HIGH_THRESHOLD)
            {
                reactHigh(player);
                level.put(player.getName(), MED_THRESHOLD+LEVEL_BOOST);
            }
            else if (playerLevel > LEVEL_MAX)
            {
                level.put(player.getName(), MED_THRESHOLD+LEVEL_BOOST);
            }
        }        
    }
    
    public void decreaseLevel(Player player)
    {
        final String name = player.getName();
        if(level.get(name) != null && level.get(name) != 0)
        {
            int playerLevel = level.get(name)-1;
            level.put(name, playerLevel);
        }          
    }
    
    public int getLevel(Player player)
    {
        final String name = player.getName();
        if(level.get(name) != null)
        {
            return level.get(name);
        } 
        else
        {
            return 0;
        }
    }
    
    public void setLevel(Player player, int x)
    {
        if(!(x > LEVEL_MAX && x < 0))
        {
            level.put(player.getName(), x);
        }
    }
    
    public boolean hasLevel(Player player)
    {
        return level.containsKey(player.getName());
    }
    
    public void reset(Player player)
    {
        level.put(player.getName(),0);
        for(CheckType type : CheckType.values())
        {
            type.clearUse(player);
        }
    }   
    
    public Map<String,Integer> getLevels()
    {
        return level;
    }
    
    private static void execute(String level, Player player)
    {
        String result = config.getResult(level);
        if(result.startsWith("COMMAND["))
        {
            String command = result.replaceAll("COMMAND\\[", "").replaceAll("]", "").replaceAll("&player", player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }        
        else if(result.equalsIgnoreCase("KICK"))
        {
            player.kickPlayer(ChatColor.RED+"Kicked by AntiCheat");
            player.getServer().broadcastMessage(ChatColor.RED+"[AntiCheat] "+player.getName()+" was kicked for hacking.");
        }
        else if(result.equalsIgnoreCase("WARN"))
        {
            player.sendMessage(ChatColor.RED+"[AntiCheat] Hacks are not allowed on this server.");
            player.sendMessage(ChatColor.RED+"[AntiCheat] If you continue to use hacks, action will be taken.");
        } 
        else if(result.equalsIgnoreCase("BAN"))
        {
            player.setBanned(true);
            player.kickPlayer(ChatColor.RED+"Banned by AntiCheat");
            player.getServer().broadcastMessage(ChatColor.RED+"[AntiCheat] "+player.getName()+" was banned for hacking.");
        }          
    }   
}

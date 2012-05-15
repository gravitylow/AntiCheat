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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerManager {
    private Map<Player,Integer> level = new HashMap<Player,Integer>();
    private Configuration config = AnticheatManager.CONFIGURATION;
    private static final int MED_THRESHOLD = 20;
    private static final int HIGH_THRESHOLD = 50;
    
    public void reactMedium(Player player)
    {
        execute("Medium",player);                           
        for(Player p : player.getServer().getOnlinePlayers())
        {
            if(p.hasPermission("anticheat.alert") || p.hasPermission("anticheat.admin") || p.isOp())
            {
                p.sendMessage(ChatColor.YELLOW+"[ALERT] "+ChatColor.WHITE+player.getName()+ChatColor.YELLOW+" has entered the MEDIUM hack level.");
                p.sendMessage(ChatColor.YELLOW+"[ALERT] This means they may be using a hacked client or may have a bad connection!");
            }
        }        
    }
    public void reactHigh(Player player)
    {
        execute("High",player);
        for(Player p : player.getServer().getOnlinePlayers())
        {
            if(p.hasPermission("anticheat.alert") || p.hasPermission("anticheat.admin") || p.isOp())
            {
                p.sendMessage(ChatColor.RED+"[ALERT] "+ChatColor.WHITE+player.getName()+ChatColor.RED+" has entered the HIGH hack level.");
                p.sendMessage(ChatColor.RED+"[ALERT] This means they probably are hacking or are lagging out!");
            }
        }       
    }
    
    public void increaseLevel(Player player)
    {
        if(level.get(player) == null || level.get(player) == 0)
        {
            level.put(player,1);
        }
        else
        {
            int playerLevel = level.get(player);
            level.put(player, playerLevel+1);
            if(playerLevel <= MED_THRESHOLD && playerLevel+1 > MED_THRESHOLD && playerLevel+1 <= HIGH_THRESHOLD)
            {
                reactMedium(player);
            }
            else if(playerLevel <= HIGH_THRESHOLD && playerLevel+1 > HIGH_THRESHOLD)
            {
                reactHigh(player);
            }
        }        
    }
    
    public void decreaseLevel(Player player)
    {
        if(level.get(player) != null && level.get(player) != 0)
        {
            int playerLevel = level.get(player)-1;
            level.put(player, playerLevel);
        }          
    }
    
    public int getLevel(Player player)
    {
        if(level.get(player) != null)
        {
            return level.get(player);
        } 
        else
        {
            return 0;
        }
    }
    
    public void reset(Player player)
    {
        level.put(player,0);
    }    
    
    public void execute(String level, Player player)
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

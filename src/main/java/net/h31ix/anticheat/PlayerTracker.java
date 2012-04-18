package net.h31ix.anticheat;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerTracker {
    
    public Map<Player,Integer> level = new HashMap<Player,Integer>();
    
    public PlayerTracker()
    {
        
    }
    
    public void increaseLevel(Player player)
    {
        if(level.get(player) == null || level.get(player) == 0)
        {
            level.put(player,2);
        }
        else
        {
            int playerLevel = level.get(player);
            level.put(player, playerLevel+2);
            if(playerLevel <= 10 && playerLevel+1 > 10 && playerLevel+1 <= 40)
            {
                for(Player p : player.getServer().getOnlinePlayers())
                {
                    if(p.hasPermission("anticheat.admin"))
                    {
                        p.sendMessage(ChatColor.YELLOW+"[ALERT] "+ChatColor.WHITE+player.getName()+ChatColor.YELLOW+" has entered the MEDIUM hack level.");
                        p.sendMessage(ChatColor.YELLOW+"[ALERT] This means they may be using a hacked client or may have a bad connection!");
                    }
                }
            }
            if(playerLevel <= 40 && playerLevel+1 > 40)
            {
                for(Player p : player.getServer().getOnlinePlayers())
                {
                    if(p.hasPermission("anticheat.admin"))
                    {
                        p.sendMessage(ChatColor.RED+"[ALERT] "+ChatColor.WHITE+player.getName()+ChatColor.RED+" has entered the HIGH hack level.");
                        p.sendMessage(ChatColor.RED+"[ALERT] This means they probably are hacking or are lagging out!");
                    }
                }
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
}

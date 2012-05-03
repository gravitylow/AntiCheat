package net.h31ix.anticheat;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerTracker {
    public Map<Player,Integer> level = new HashMap<Player,Integer>();
    Anticheat plugin;
    Configuration config;
    
    public PlayerTracker(Anticheat plugin)
    {
        this.plugin = plugin;
        this.config = plugin.config;
    }
    
    public void increaseLevel(Player player, int i)
    {
        if(level.get(player) == null || level.get(player) == 0)
        {
            level.put(player,i);
        }
        else
        {
            int playerLevel = level.get(player);
            level.put(player, playerLevel+i);
            if(playerLevel <= 10 && playerLevel+i > 10 && playerLevel+1 <= 40)
            {
                execute("Medium",player);                           
                for(Player p : player.getServer().getOnlinePlayers())
                {
                    if(p.hasPermission("anticheat.admin") || p.isOp())
                    {
                        p.sendMessage(ChatColor.YELLOW+"[ALERT] "+ChatColor.WHITE+player.getName()+ChatColor.YELLOW+" has entered the MEDIUM hack level.");
                        p.sendMessage(ChatColor.YELLOW+"[ALERT] This means they may be using a hacked client or may have a bad connection!");
                    }
                }
            }
            if(playerLevel <= 40 && playerLevel+i > 40)
            {
                execute("High",player);
                for(Player p : player.getServer().getOnlinePlayers())
                {
                    if(p.hasPermission("anticheat.admin") || p.isOp())
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
    
    public void reset(Player player)
    {
        level.put(player,0);
    }    
    
    public void execute(String level, Player player)
    {
        String result = config.getResult(level);
        if(result.equalsIgnoreCase("KICK"))
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

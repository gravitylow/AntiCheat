package net.h31ix.anticheat;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatManager {
    Anticheat plugin;
    ChatColor red = ChatColor.RED;
    
    public ChatManager(Anticheat plugin)
    {
        this.plugin = plugin;
    }
    
    private Map<Player,Integer> chatLevel = new HashMap<Player,Integer>();
    private Map<Player,Integer> kicks = new HashMap<Player,Integer>();
    
    public void addChat(final Player player)
    {
        if(chatLevel.get(player) == null || chatLevel.get(player) == 0)
        {
            chatLevel.put(player, 1);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
            {
                public void run() 
                {
                    clear(player);
                }
            },      100L);
        }
        else
        {
            int amount = (int)chatLevel.get(player)+1;
            chatLevel.put(player, amount);
            check(player, amount);
        }
    }
    
    public void clear(Player player)
    {
        chatLevel.put(player, 0);
    }
    
    public void check(Player player, int amount)
    {
        if(amount >= 7)
        {
            player.sendMessage(red+"Please stop flooding the server!");
        }
        if (amount >= 10)
        {
            int kick = 0;
            if(kicks.get(player) == null || kicks.get(player) == 0)
            {
                kick = 1;
                kicks.put(player, 1);
            }
            else
            {
                kick = (int)kicks.get(player)+1;
                kicks.put(player, kick);
            }
            
            if(kicks.get(player) <=3)
            {
                player.kickPlayer(red+"Spamming, kick "+kick+"/3");
                plugin.getServer().broadcastMessage(red+player.getName()+" was kicked for spamming.");
            }
            else
            {
                player.kickPlayer(red+"Banned for spamming.");
                player.setBanned(true);
                plugin.getServer().broadcastMessage(red+player.getName()+" was banned for spamming.");
            }
        }
    }
    
}

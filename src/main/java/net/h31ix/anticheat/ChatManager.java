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
    
    public void addChat(final Player player)
    {
        if(chatLevel.get(player) == null)
        {
            chatLevel.put(player, 1);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
            {
                public void run() 
                {
                    chatLevel.put(player, 0);
                }
            },      600L);
        }
        else
        {
            int amount = (int)chatLevel.get(player)+1;
            chatLevel.put(player, amount);
            check(player, amount);
        }
    }
    
    public void check(Player player, int amount)
    {
        if(amount >= 7)
        {
            player.sendMessage(red+"Please stop flooding the server!");
        }
        else if (amount >= 10)
        {
            player.kickPlayer(red+"Spam");
            plugin.getServer().broadcastMessage(red+player.getName()+"Was kicked for spamming.");
        }
    }
    
}

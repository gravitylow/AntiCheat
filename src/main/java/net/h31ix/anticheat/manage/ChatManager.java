package net.h31ix.anticheat.manage;

import java.util.HashMap;
import java.util.Map;
import net.h31ix.anticheat.Anticheat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatManager {
    private Anticheat plugin;
    private static final ChatColor red = ChatColor.RED;
    private Map<Player,Integer> chatLevel = new HashMap<Player,Integer>();
    private Map<Player,Integer> kicks = new HashMap<Player,Integer>();    
    private static final int CHAT_WARN_LEVEL = 7;
    private static final int CHAT_KICK_LEVEL = 10;
    private static final int CHAT_BAN_LEVEL = 3;
    
    public ChatManager(Anticheat plugin)
    {
        this.plugin = plugin;
    }
    
    public void addChat(final Player player)
    {
        if(chatLevel.get(player) == null || chatLevel.get(player) == 0)
        {
            chatLevel.put(player, 1);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
            {
                @Override
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
        if(amount >= CHAT_WARN_LEVEL)
        {
            player.sendMessage(red+"Please stop flooding the server!");
        }
        if (amount >= CHAT_KICK_LEVEL)
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
            
            if(kicks.get(player) <= CHAT_BAN_LEVEL)
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

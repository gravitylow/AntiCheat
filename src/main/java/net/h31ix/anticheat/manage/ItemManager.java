package net.h31ix.anticheat.manage;

import java.util.HashMap;
import java.util.Map;
import net.h31ix.anticheat.Anticheat;
import org.bukkit.entity.Player;

public class ItemManager {
    public Map<Player,Boolean> drop = new HashMap<Player,Boolean>();
    Anticheat plugin;
    
    public ItemManager(Anticheat plugin)
    {
        this.plugin = plugin;
    }   
    
    public void logDrop(final Player player)
    {
        drop.put(player, true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            public void run() 
            {
                drop.put(player, false);
            }
        },      2L);        
    }
    
    public boolean hasDropped(Player player)
    {
        if(drop.get(player) == null || drop.get(player) == false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}

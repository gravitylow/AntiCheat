package net.h31ix.anticheat.manage;

import java.util.HashMap;
import java.util.Map;
import net.h31ix.anticheat.Anticheat;
import org.bukkit.entity.Player;

public class ExemptManager {
    public Map<Player,Boolean> enter = new HashMap<Player,Boolean>();
    public Map<Player,Boolean> hit = new HashMap<Player,Boolean>();
    Anticheat plugin;
    
    public ExemptManager(Anticheat plugin)
    {
        this.plugin = plugin;
    }   
    
    public void logEnter(final Player player)
    {
        enter.put(player, true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            @Override
            public void run() 
            {
                enter.put(player, false);
            }
        },      10L);        
    }
    
    public boolean isEntering(Player player)
    {
        if(enter.get(player) == null || enter.get(player) == false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public void logHit(final Player player)
    {
        hit.put(player, true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            @Override
            public void run() 
            {
                hit.put(player, false);
            }
        },      15L);        
    }
    
    public boolean isHit(Player player)
    {
        if(hit.get(player) == null || hit.get(player) == false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }    
}

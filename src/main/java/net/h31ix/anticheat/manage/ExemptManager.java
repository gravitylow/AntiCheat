package net.h31ix.anticheat.manage;

import java.util.HashMap;
import java.util.Map;
import net.h31ix.anticheat.Anticheat;
import org.bukkit.entity.Player;

public class ExemptManager {
    private Map<Player,Boolean> enter = new HashMap<Player,Boolean>();
    private Map<Player,Boolean> hit = new HashMap<Player,Boolean>();
    private Anticheat plugin;
    
    public ExemptManager(Anticheat plugin)
    {
        this.plugin = plugin;
    }   
    
    public void logEnter(final Player player)
    {
        //For ignoring players entering vehicles
        enter.put(player, true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            @Override
            public void run() 
            {
                enter.put(player, false);
            }
        },      20L);        
    }
    
    public boolean isEntering(Player player)
    {
        if(enter.get(player) == null)
        {
            return false;
        }
        else
        {
            return enter.get(player);
        }
    }
    
    public void logHit(final Player player, int time)
    {
        hit.put(player, true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            @Override
            public void run() 
            {
                hit.put(player, false);
            }
        },      time);        
    }
    
    public boolean isHit(Player player)
    {
        if(hit.get(player) == null)
        {
            return false;
        }
        else
        {
            return hit.get(player);
        }
    }    
}

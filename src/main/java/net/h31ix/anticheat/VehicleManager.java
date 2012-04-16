package net.h31ix.anticheat;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class VehicleManager {
    public Map<Player,Boolean> enter = new HashMap<Player,Boolean>();
    Anticheat plugin;
    
    public VehicleManager(Anticheat plugin)
    {
        this.plugin = plugin;
    }   
    
    public void logEnter(final Player player)
    {
        enter.put(player, true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            public void run() 
            {
                enter.put(player, false);
            }
        },      10L);        
    }
    
    public boolean isEntering(Player player)
    {
        if(enter.get(player) == null)
        {
            return false;
        }
        if(enter.get(player) == false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}

package net.h31ix.anticheat.manage;

import java.util.HashMap;
import java.util.Map;
import net.h31ix.anticheat.Anticheat;
import org.bukkit.entity.Player;

public class FoodManager {
    private Map<Player,Boolean> start = new HashMap<Player,Boolean>();
    private Anticheat plugin;
    
    public FoodManager(Anticheat plugin)
    {
        this.plugin = plugin;
    }   
    
    public void logStart(final Player player)
    {
        start.put(player, true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            @Override
            public void run() 
            {
                start.put(player, false);
            }
        },      20L);        
    }    
    
    public boolean justStarted(Player player)
    {
        if(start.get(player) == null)
        {
            return false;
        }
        else
        {
            return start.get(player);
        }        
    }
}

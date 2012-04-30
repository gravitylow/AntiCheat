package net.h31ix.anticheat.manage;

import java.util.HashMap;
import java.util.Map;
import net.h31ix.anticheat.Anticheat;
import org.bukkit.entity.Player;

public class BowManager {
    public Map<Player,Boolean> shot = new HashMap<Player,Boolean>();
    public Map<Player,Boolean> wind = new HashMap<Player,Boolean>();
    Anticheat plugin;
    
    public BowManager(Anticheat plugin)
    {
        this.plugin = plugin;
    }   
    
    public void logWindUp(final Player player)
    {
        wind.put(player, true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            @Override
            public void run() 
            {
                wind.put(player, false);
            }
        },      2L);        
    }    
    
    public void logShoot(final Player player)
    {
        shot.put(player, true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            @Override
            public void run() 
            {
                shot.put(player, false);
            }
        },      5L);        
    }
    
    public boolean hasShot(Player player)
    {
        //If the player has shot in the last .5 seconds
        if(shot.get(player) == null || shot.get(player) == false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public boolean justWoundUp(Player player)
    {
        if(wind.get(player) == null || wind.get(player) == false)
        {
            return false;
        }
        else
        {
            return true;
        }        
    }
}

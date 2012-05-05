package net.h31ix.anticheat.manage;

import java.util.HashMap;
import java.util.Map;
import net.h31ix.anticheat.Anticheat;
import org.bukkit.entity.Player;

public class HealthManager {
    private Anticheat plugin;
    private Map<Player,Float> fall = new HashMap<Player,Float>();
    private Map<Player,Boolean> healed = new HashMap<Player,Boolean>();
    private Map<Player,Integer> health = new HashMap<Player,Integer>();
    private Map<Player,Integer> fallvl = new HashMap<Player,Integer>();
    
    public HealthManager(Anticheat plugin)
    {
        this.plugin = plugin;
    }   
    
    public void log(final Player player)
    {
        fall.put(player, player.getFallDistance()); 
        health.put(player, player.getHealth());          
    }
    
    public boolean checkFall(Player player)
    {
        //Sometimes a player's fall distance is 0 when they reach the peak of their jump
        //To combat false positives, the player must have 10 fall distance values of 0 in a row to trigger.
        if(fall.get(player) == 0)
        {
            if(fallvl.get(player) == null)
            {
                fallvl.put(player,1);
            }
            else
            {
                fallvl.put(player,fallvl.get(player)+1);
            }
            
            if(fallvl.get(player) >= 10)
            {
                fallvl.put(player,1);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            fallvl.put(player,0);
            return false;
        }
    }
    
    public void logHeal(final Player player)
    {
        healed.put(player, true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            @Override
            public void run() 
            {
                healed.put(player, false);
            }
        },      35L);         
    }
    
    public boolean justHealed(Player player)
    {
        if(healed.get(player) != null)
        {
            return healed.get(player);
        }
        else
        {
            return false;
        }        
    }
    
    public float getFall(Player player)
    {
        if(fall.get(player) != null)
        {
            return fall.get(player);
        }
        else
        {
            return 0;
        }
    }
    
    public int getHealth(Player player)
    {
        if(health.get(player) != null)
        {
            return health.get(player);
        }
        else
        {
            return 0;
        }        
    }      
}

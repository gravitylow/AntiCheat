package net.h31ix.anticheat.manage;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class HealthManager {
    public Map<Player,Float> fall = new HashMap<Player,Float>();
    public Map<Player,Integer> health = new HashMap<Player,Integer>();
    public Map<Player,Integer> fallvl = new HashMap<Player,Integer>();
    
    public HealthManager()
    {
    }   
    
    public void log(Player player)
    {
        fall.put(player, player.getFallDistance()); 
        health.put(player, player.getHealth());
    }
    
    public boolean checkFall(Player player)
    {
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

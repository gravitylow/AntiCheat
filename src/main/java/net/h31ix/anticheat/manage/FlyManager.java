package net.h31ix.anticheat.manage;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class FlyManager {
    private Map<Player,Integer> yvl = new HashMap<Player,Integer>();
    private static final int FLY_LIMIT = 4;
    
    public FlyManager()
    {
        
    }   
    
    public boolean checkFly(Player player)
    {
        //Sometimes a player's y drop is 0 when they reach the peak of their jump
        //To combat false positives, the player must have 4 y values of 0 in a row to trigger.
        if(yvl.get(player) == null)
        {
            yvl.put(player,1);
        }
        else
        {
            yvl.put(player,yvl.get(player)+1);
        }
        if(yvl.get(player) >= FLY_LIMIT)
        {
            yvl.put(player,1);
            return true;
        }
        else
        {
            return false;
        }
    } 
}


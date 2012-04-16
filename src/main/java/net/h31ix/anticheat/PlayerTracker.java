package net.h31ix.anticheat;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class PlayerTracker {
    
    public Map<Player,Integer> level = new HashMap<Player,Integer>();
    
    public PlayerTracker()
    {
        
    }
    
    public void increaseLevel(Player player)
    {
        if(level.get(player) == null || level.get(player) == 0)
        {
            level.put(player,1);
        }
        else
        {
            int playerLevel = level.get(player)+1;
            level.put(player, playerLevel);
        }        
    }
    
    public void decreaseLevel(Player player)
    {
        if(level.get(player) != null && level.get(player) != 0)
        {
            int playerLevel = level.get(player)-1;
            level.put(player, playerLevel);
        }          
    }
    
    public int getLevel(Player player)
    {
        if(level.get(player) != null)
        {
            return level.get(player);
        } 
        else
        {
            return 0;
        }
    }
}

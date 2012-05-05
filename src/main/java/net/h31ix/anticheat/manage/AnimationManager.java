package net.h31ix.anticheat.manage;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class AnimationManager {
    private Map<Player,Boolean> animations = new HashMap<Player,Boolean>();
    
    public AnimationManager()
    {
        
    }   
    
    public void logAnimation(final Player player)
    {
        animations.put(player, true);       
    }
    
    public boolean swungArm(Player player)
    {
        if(animations.get(player) == null)
        {
            return false;
        }
        else
        {
            return animations.get(player);
        }
    }
    
    public void reset(Player player)
    {
        animations.put(player,false);
    }
}

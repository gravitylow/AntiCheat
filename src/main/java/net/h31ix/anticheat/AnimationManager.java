package net.h31ix.anticheat;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class AnimationManager {
    public Map<Player,Boolean> animations = new HashMap<Player,Boolean>();
    Anticheat plugin;
    
    public AnimationManager(Anticheat plugin)
    {
        this.plugin = plugin;
    }   
    
    public void logAnimation(final Player player)
    {
        animations.put(player, true);       
    }
    
    public boolean swungArm(Player player)
    {
        if(animations.get(player) == null || animations.get(player) == false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public void reset(Player player)
    {
        animations.put(player,false);
    }
}

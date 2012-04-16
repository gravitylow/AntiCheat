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
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            public void run() 
            {
                animations.put(player, false);
            }
        },      10L);        
    }
    
    public boolean swungArm(Player player)
    {
        if(animations.get(player) == null)
        {
            return false;
        }
        if(animations.get(player) == false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}

package net.h31ix.anticheat.manage;

import java.util.HashMap;
import java.util.Map;

import net.h31ix.anticheat.Anticheat;
import org.bukkit.entity.Player;

public class SprintManager {
    private Map<Player,Boolean> sprint = new HashMap<Player,Boolean>();
    private Anticheat plugin;
    
    public SprintManager(Anticheat plugin)
    {
        this.plugin = plugin;
    }   
    
    public void logSprint(final Player player)
    {
        sprint.put(player, true);       
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            @Override
            public void run() 
            {
                sprint.put(player, false);
            }
        },      2L);           
    }
    
    public boolean justSprinted(Player player)
    {
        if(sprint.get(player) == null)
        {
            return false;
        }
        else
        {
            return sprint.get(player);
        }
    }
}

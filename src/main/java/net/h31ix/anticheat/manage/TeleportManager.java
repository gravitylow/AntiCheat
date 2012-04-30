package net.h31ix.anticheat.manage;

import java.util.HashMap;
import java.util.Map;
import net.h31ix.anticheat.Anticheat;
import org.bukkit.entity.Player;

public class TeleportManager {
    public Map<Player,Boolean> teleport = new HashMap<Player,Boolean>();
    Anticheat plugin;
    
    public TeleportManager(Anticheat plugin)
    {
        this.plugin = plugin;
    }   
    
    public void logTeleport(final Player player)
    {
        teleport.put(player, true);     
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            @Override
            public void run() 
            {
                teleport.put(player, false);
            }
        },      40L);         
    }
    
    public boolean didTeleport(Player player)
    {
        if(teleport.get(player) == null || teleport.get(player) == false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}

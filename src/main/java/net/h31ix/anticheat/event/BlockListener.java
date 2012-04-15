package net.h31ix.anticheat.event;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.checks.LengthCheck;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListener implements Listener {
    Anticheat plugin;
    
    public BlockListener(Anticheat plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if(event.getPlayer() != null)
        {
            LengthCheck c = new LengthCheck(event.getBlock().getLocation(),event.getPlayer().getLocation());
            if(c.getXDifference() > 5L)
            {
                event.setCancelled(true);
            }
            if(c.getYDifference() > 4.3)
            {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockBreakEvent event)
    {
        if(event.getPlayer() != null)
        {        
            LengthCheck c = new LengthCheck(event.getBlock().getLocation(),event.getPlayer().getLocation());
            if(c.getXDifference() > 5L)
            {
                event.setCancelled(true);
            }
            if(c.getYDifference() > 4.3)
            {
                event.setCancelled(true);
            }
        }
    }    
    
}

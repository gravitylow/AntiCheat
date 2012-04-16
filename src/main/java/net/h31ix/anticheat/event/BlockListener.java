package net.h31ix.anticheat.event;

import net.h31ix.anticheat.AnimationManager;
import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.checks.LengthCheck;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListener implements Listener {
    Anticheat plugin;
    AnimationManager am;
    
    public BlockListener(Anticheat plugin)
    {
        this.plugin = plugin;
        this.am = plugin.am;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        if(player != null)
        {
            LengthCheck c = new LengthCheck(event.getBlock().getLocation(),event.getPlayer().getLocation());
            if(c.getXDifference() > 6.0D || c.getZDifference() > 6.0D || c.getYDifference() > 6.0D)
            {
                plugin.log(player.getName()+" tried to break a block too far away!");
                event.setCancelled(true);
            }
            else
            {
                if(!am.swungArm(player))
                {
                    plugin.log(player.getName()+" didn't swing their arm on a block break!");
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        if(player != null)
        {        
            LengthCheck c = new LengthCheck(event.getBlock().getLocation(),event.getPlayer().getLocation());
            if(c.getXDifference() > 6.0D || c.getZDifference() > 6.0D || c.getYDifference() > 6.0D)
            {
                plugin.log(player.getName()+" tried to place a block too far away!");
                event.setCancelled(true);
            }
        }
    }    
    
}

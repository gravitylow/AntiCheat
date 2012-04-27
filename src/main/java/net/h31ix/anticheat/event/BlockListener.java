package net.h31ix.anticheat.event;

import net.h31ix.anticheat.manage.AnimationManager;
import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.PlayerTracker;
import net.h31ix.anticheat.checks.EyeCheck;
import net.h31ix.anticheat.checks.LengthCheck;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    Anticheat plugin;
    AnimationManager am;
    PlayerTracker tracker;
    EyeCheck e = new EyeCheck();
    
    public BlockListener(Anticheat plugin)
    {
        this.plugin = plugin;
        this.am = plugin.am;
        this.tracker = plugin.tracker;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(player != null)
        {     
            //Check if an animation was done before this
            if(!am.swungArm(player))
            {
                tracker.increaseLevel(player);
                plugin.log(player.getName()+" didn't swing their arm on a block break!");
                event.setCancelled(true);
            }
            else
            {
                //If so, check the distance from the block. Is it too far away?
                LengthCheck c = new LengthCheck(block.getLocation(),event.getPlayer().getLocation());
                if(c.getXDifference() > 6.0D || c.getZDifference() > 6.0D || c.getYDifference() > 6.0D)
                {
                    plugin.log(player.getName()+" tried to break a block too far away!");
                    tracker.increaseLevel(player);
                    event.setCancelled(true);
                }
                else
                {
                    tracker.decreaseLevel(player);
                }
            }                         
        }
        am.reset(player);
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(player != null)
        {      
            if(block.getType() != Material.LADDER)
            {
                //Check if the player can see the block they are placing
                //This is mostly used for preventing build/autobuild hacks (Logic not yet finished)
                if(!e.canSee(player, block) && !player.getWorld().getBlockAt(player.getLocation()).isLiquid())
                {
                    //TODO: this is causing false alerts. Re-work the vision logic?
                    //tracker.increaseLevel(player);
                    plugin.log(player.getName()+" tried to place a block that they couldn't see!");
                    event.setCancelled(true);                    
                }
            }
            //Is the player too far away?
            LengthCheck c = new LengthCheck(block.getLocation(),event.getPlayer().getLocation());
            if(c.getXDifference() > 6.0D || c.getZDifference() > 6.0D || c.getYDifference() > 6.0D)
            {
                tracker.increaseLevel(player);
                plugin.log(player.getName()+" tried to place a block too far away!");
                event.setCancelled(true);
            }    
            else
            {
                tracker.decreaseLevel(player);
            }                          
        } 
        am.reset(player);
    }
}

package net.h31ix.anticheat.event;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.PlayerTracker;
import net.h31ix.anticheat.checks.*;
import net.h31ix.anticheat.manage.*;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
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
    BlockManager blm;
    
    public BlockListener(Anticheat plugin)
    {
        this.plugin = plugin;
        this.am = plugin.am;
        this.tracker = plugin.tracker;
        this.blm = plugin.blm;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(player != null)
        {     
            if(plugin.check(player))
            {                
                //Check if an animation was done before this
                if(!am.swungArm(player))
                {
                    if(!player.hasPermission("anticheat.noswing"))
                    {
                        tracker.increaseLevel(player,2);
                        plugin.log(player.getName()+" didn't swing their arm on a block break!");
                        event.setCancelled(true);
                    }
                }          
                else
                {
                    if(!player.hasPermission("anticheat.longreach"))
                    {                
                        //If so, check the distance from the block. Is it too far away?
                        LengthCheck c = new LengthCheck(block.getLocation(),event.getPlayer().getLocation());
                        if(c.getXDifference() > 6.0D || c.getZDifference() > 6.0D || c.getYDifference() > 6.0D)
                        {
                            plugin.log(player.getName()+" tried to break a block too far away!");
                            tracker.increaseLevel(player,2);
                            event.setCancelled(true);
                        }
                        else
                        {
                            tracker.decreaseLevel(player);
                        }
                    }
                    if (!player.hasPermission("anticheat.fastbreak") && !player.getInventory().getItemInHand().containsEnchantment(Enchantment.DIG_SPEED))
                    {
                        if(block.getType() != Material.RED_MUSHROOM 
                                && block.getType() != Material.RED_ROSE 
                                && block.getType() != Material.BROWN_MUSHROOM 
                                && block.getType() != Material.YELLOW_FLOWER 
                                && block.getType() != Material.REDSTONE 
                                && block.getType() != Material.REDSTONE_TORCH_OFF 
                                && block.getType() != Material.REDSTONE_TORCH_ON 
                                && block.getType() != Material.REDSTONE_WIRE 
                                && block.getType() != Material.GRASS 
                                && block.getType() != Material.PAINTING 
                                && block.getType() != Material.WHEAT 
                                && block.getType() != Material.SUGAR_CANE 
                                && block.getType() != Material.SUGAR_CANE_BLOCK 
                                && block.getType() != Material.DIODE 
                                && block.getType() != Material.DIODE_BLOCK_OFF 
                                && block.getType() != Material.DIODE_BLOCK_ON
                                && block.getType() != Material.SAPLING
                                && block.getType() != Material.TORCH
                                && block.getType() != Material.SNOW)
                        {
                            if (!blm.justBroke(player))
                            {
                                blm.logBreak(player);
                            }
                            else
                            {
                                plugin.log(player.getName() + " tried to break a block too fast!");
                                tracker.increaseLevel(player, 2);
                                event.setCancelled(true);
                            }
                        }
                    }                    
                }                         
            }
            am.reset(player);
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(player != null)
        {      
            if(plugin.check(player))
            {            
                if(block.getType() != Material.LADDER)
                {
                    //Check if the player can see the block they are placing
                    //This is mostly used for preventing build/autobuild hacks (Logic not yet finished)
                    if(!e.canSee(player, block) && !player.getWorld().getBlockAt(player.getLocation()).isLiquid())
                    {
                        plugin.log(player.getName()+" tried to place a block that they couldn't see!");
                        event.setCancelled(true);                    
                    }
                }
                if(!player.hasPermission("anticheat.longreach"))
                {            
                    //Is the player too far away?
                    LengthCheck c = new LengthCheck(block.getLocation(),event.getPlayer().getLocation());
                    if(c.getXDifference() > 6.0D || c.getZDifference() > 6.0D || c.getYDifference() > 6.0D)
                    {
                        tracker.increaseLevel(player,2);
                        plugin.log(player.getName()+" tried to place a block too far away!");
                        event.setCancelled(true);
                    }    
                    else
                    {
                        tracker.decreaseLevel(player);
                    }
                }
                if (!player.hasPermission("anticheat.fastplace"))
                {
                    if (!blm.justPlaced(player))
                    {
                        blm.logPlace(player);
                    }
                    else
                    {
                        plugin.log(player.getName() + " tried to place a block too fast!");
                        tracker.increaseLevel(player, 2);
                        event.setCancelled(true);
                    }
                }                 
            } 
            am.reset(player);
        }
    }
}

package net.h31ix.anticheat.xray;

import net.h31ix.anticheat.Anticheat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class XRayListener implements Listener {
    XRayTracker tracker;
    
    public XRayListener(Anticheat plugin)
    {
        tracker = plugin.xtracker;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        if(!player.hasPermission("anticheat.xray"))
        {
            Material m = event.getBlock().getType();
            if(m == Material.DIAMOND_ORE)
            {
                tracker.addDiamond(player);
            }
            else if(m == Material.IRON_ORE)
            {
                tracker.addIron(player);
            }
            else if(m == Material.COAL_ORE)
            {
                tracker.addCoal(player);
            }  
            else if(m == Material.GOLD_ORE)
            {
                tracker.addGold(player);
            }   
            else if(m == Material.LAPIS_ORE)
            {
                tracker.addLapis(player);
            } 
            else if(m == Material.REDSTONE_ORE)
            {
                tracker.addRedstone(player);
            }     
            else if(m == Material.GOLD_ORE)
            {
                tracker.addGold(player);
            } 
            else
            {
                tracker.addBlock(player);
            }
            tracker.addTotal(player);
        }
    } 
}

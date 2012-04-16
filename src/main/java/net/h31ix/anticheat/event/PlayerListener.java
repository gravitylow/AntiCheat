package net.h31ix.anticheat.event;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.checks.LengthCheck;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {
    Anticheat plugin;
    
    public PlayerListener(Anticheat plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event)
    {
        plugin.cm.addChat(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event)
    {
        plugin.cm.clear(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        LengthCheck c = new LengthCheck(event.getFrom(), event.getTo());
        double xd = c.getXDifference();
        double zd = c.getZDifference();
        double yd = c.getYDifference();
        Block p1 = player.getLocation().getWorld().getBlockAt(player.getLocation());
        if(p1.isLiquid())
        {
            if(xd > 0.19D || zd > 0.19D)
            {
                if(!player.isSprinting() && !player.isFlying())
                {
                    plugin.log(player.getName()+" is walking too fast in water! XSpeed="+xd+" ZSpeed="+zd);
                    event.setTo(event.getFrom().clone());
                }  
            }
            else
            {
                if(xd > 0.3D || zd > 0.3D)
                {
                    plugin.log(player.getName()+" is flying/sprinting too fast in water! XSpeed="+xd+" ZSpeed="+zd);
                    event.setTo(event.getFrom().clone());
                }
            }                
        }
        if(player.isSneaking())
        {
            if(xd > 0.097D || zd > 0.081D)
            {
                plugin.log(player.getName()+" is sneaking too fast! XSpeed="+xd+" ZSpeed="+zd);
                event.setTo(event.getFrom().clone());
                player.setSneaking(false);
            }
        }
        else if(xd > 0.32D || zd > 0.32D)
        {
            if(!player.isSprinting() && !player.isFlying())
            {
                plugin.log(player.getName()+" is walking too fast! XSpeed="+xd+" ZSpeed="+zd);
                event.setTo(event.getFrom().clone());
            }
            else
            {
                if(xd > 0.62D || zd > 0.62D)
                {
                    plugin.log(player.getName()+" is flying/sprinting too fast! XSpeed="+xd+" ZSpeed="+zd);
                    event.setTo(event.getFrom().clone());
                }
            }
        }
        if(event.getFrom().getY() < event.getTo().getY())
        {
            if(yd > 1D)
            {
                plugin.log(player.getName()+" is ascending too fast! YSpeed="+yd);
                event.setTo(event.getFrom().clone());
            }
        }        
    }
    
}

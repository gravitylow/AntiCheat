package net.h31ix.anticheat.event;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.checks.LengthCheck;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
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
    public void onPlayerMove(PlayerMoveEvent event)
    {
        LengthCheck c = new LengthCheck(event.getFrom(), event.getTo());
        double xd = c.getXDifference();
        double zd = c.getZDifference();
        double yd = c.getYDifference();
        Player player = event.getPlayer();
        if(xd > 0.29D || zd > 0.26D)
        {
            if(!player.isSprinting() && !player.isFlying())
            {
                player.teleport(event.getFrom());
            }
            else
            {
                if(zd > 0.61D)
                {
                    player.teleport(event.getFrom());
                }
                if(zd > 0.61D)
                {
                    player.teleport(event.getFrom());
                }
            }
        }
        if(event.getFrom().getY() < event.getTo().getY())
        {
            if(yd > 0.42D)
            {
                event.getPlayer().teleport(event.getFrom());
            }
        }        
    }
    
}

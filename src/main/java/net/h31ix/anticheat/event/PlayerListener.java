package net.h31ix.anticheat.event;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.checks.LengthCheck;
import org.bukkit.Location;
import org.bukkit.Material;
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
            System.out.println(xd);
            System.out.println(zd);
            if(xd > 0.15D || zd > 0.15D)
            {
                if(!player.isSprinting() && !player.isFlying())
                {
                    event.setTo(event.getFrom().clone());
                }  
            }
            else
            {
                if(zd > 0.3D)
                {
                    event.setTo(event.getFrom().clone());
                }
                if(zd > 0.3D)
                {
                    event.setTo(event.getFrom().clone());
                }
            }                
        }
        if(player.isSneaking())
        {
            if(xd > 0.097D || zd > 0.081D)
            {
                event.setTo(event.getFrom().clone());
                player.setSneaking(false);
            }
        }
        else if(xd > 0.29D || zd > 0.26D)
        {
            if(!player.isSprinting() && !player.isFlying())
            {
                event.setTo(event.getFrom().clone());
            }
            else
            {
                if(xd > 0.61D)
                {
                    event.setTo(event.getFrom().clone());
                }
                if(zd > 0.61D)
                {
                    event.setTo(event.getFrom().clone());
                }
            }
        }
        if(event.getFrom().getY() < event.getTo().getY())
        {
             String dir = null;
             float y = player.getLocation().getYaw();
             if( y < 0 ){y += 360;}
             y %= 360;
             int i = (int)((y+8) / 22.5);
             if(i == 0){dir = "west";}
             else if(i == 1){dir = "west";}
             else if(i == 2){dir = "west";}
             else if(i == 3){dir = "north";}
             else if(i == 4){dir = "north";}
             else if(i == 5){dir = "north";}
             else if(i == 6){dir = "north";}
             else if(i == 7){dir = "east";}
             else if(i == 8){dir = "east";}
             else if(i == 9){dir = "east";}
             else if(i == 10){dir = "east";}
             else if(i == 11){dir = "south";}
             else if(i == 12){dir = "south";}
             else if(i == 13){dir = "south";}
             else if(i == 14){dir = "south";}
             else if(i == 15){dir = "west";}
             else {dir = "west";}
             Location e = null;
             Location x = event.getTo().getBlock().getLocation();
             if (dir.equals("north"))
             {
                 e = new Location(player.getWorld(),x.getX()-1,x.getY(),x.getZ());
             }
             else if (dir.equals("east"))
             {
                 e = new Location(player.getWorld(),x.getX(),x.getY(),x.getZ()-1);
             }
             else if (dir.equals("south"))
             {
                 e = new Location(player.getWorld(),x.getX()+1,x.getY(),x.getZ());
             } 
             else if (dir.equals("west"))
             {
                 e = new Location(player.getWorld(),x.getX(),x.getY(),x.getZ()+1);
             }               
            int id = x.getWorld().getBlockAt(e).getTypeId();
            int id2 = event.getTo().getBlock().getRelative(BlockFace.DOWN).getTypeId();
            if(id != 53 && id != 67 && id != 108 && id != 109 && id != 114 && id != 43 && id != 44 && id2 != 53 && id2 != 67 && id2 != 108 && id2 != 109 && id2 != 114 && id2 != 43 && id2 != 44)
            {            
                if(yd > 0.42D)
                {
                        event.setTo(event.getFrom().clone());
                }
            }
        }        
    }
    
}

package net.h31ix.anticheat.event;

import net.h31ix.anticheat.manage.*;
import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.PlayerTracker;
import net.h31ix.anticheat.checks.*;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener {
    Anticheat plugin;
    AnimationManager am;
    ExemptManager ex;
    PlayerTracker tracker;
    ItemManager im;
    HealthManager hm;
    LoginManager lm;
    FlyManager fm;
    
    public PlayerListener(Anticheat plugin)
    {
        this.plugin = plugin;
        this.am = plugin.am;
        this.ex = plugin.ex;
        this.im = plugin.im;
        this.tracker = plugin.tracker;
        this.hm = plugin.hm;
        this.lm = plugin.lm;
        this.fm = plugin.fm;
    }
    
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        if(!lm.log()) 
        {
            //Make sure a player has not joined in the last .5th of a second
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Please do not flood the server!");
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
       Player player = event.getPlayer();
       if(!player.hasPermission("anticheat.zombe"))
       {
           player.sendMessage("§f §f §1 §0 §2 §4");
           player.sendMessage("§f §f §2 §0 §4 §8");
           player.sendMessage("§f §f §4 §0 §9 §6");            
       }           
    }
    
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        Player player = event.getPlayer();
        if(plugin.check(player))
        {        
            if(!player.hasPermission("anticheat.spamdrop"))
            {
                if(!im.hasDropped(player))
                {
                    //Make sure the player isn't spamming drops
                    //For a normal user this is no big deal, but to hackers it gets rid of most of the items in their inventory
                    im.logDrop(player);
                }
                else
                {
                    plugin.log(player.getName()+" tried to drop blocks too fast!");
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAnimation(PlayerAnimationEvent event)
    {
        if(event.getAnimationType() == PlayerAnimationType.ARM_SWING)
        {
            //Log animations for future checks
            am.logAnimation(event.getPlayer());
        }
    }
    
    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event)
    {
        if(event.getEntered() instanceof Player)
        {
            //Give the player a grace period as they enter a vehicle, as they move very fast.
            ex.logEnter((Player)event.getEntered());
        }
    }    
    
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        if(!player.hasPermission("anticheat.spam"))
        {
            if(!plugin.lagged)
            {        
                //Block command spamming (consider them chats)
                plugin.cm.addChat(event.getPlayer());
            }
        }
    }
    
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if(!player.hasPermission("anticheat.spam"))
        {        
            if(player.getLocation().equals(player.getWorld().getSpawnLocation()))
            {
                //Make sure the player is not a potential spambot
                event.setCancelled(true);
                player.sendMessage("Please move from spawn before speaking.");
            }        
            if(!plugin.lagged)
            {        
                //Block chat spamming
                plugin.cm.addChat(player);
            }
        }
    }
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event)
    {
        //Clear the player's chat level when they disconnect
        plugin.cm.clear(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        if(plugin.check(player))
        {        
            if(!plugin.lagged && !ex.isHit(player))
            {
                //Log the player's health level
                hm.log(player);
                //Get distances for hack checks.
                LengthCheck c = new LengthCheck(event.getFrom(), event.getTo());
                double xd = c.getXDifference();
                double zd = c.getZDifference();
                double yd = c.getYDifference();
                Block p1 = player.getLocation().getWorld().getBlockAt(player.getLocation());
                //Is the player in water?
                if(p1.isLiquid())
                {
                    //Are they using a boat? If so give them a bit more leniency
                    if (player.getVehicle() != null)
                    {
                        if(xd > 2.0D || zd > 2.0D)
                        {
                            tracker.increaseLevel(player);
                            plugin.log(player.getName()+" is using a boat too fast! XSpeed="+xd+" ZSpeed="+zd);
                        }
                    }                
                    else if(xd > 0.19D || zd > 0.19D)
                    {
                        if(!player.hasPermission("anticheat.waterwalk"))
                        {                    
                            //Otherwise check for normal walking speeds, making sure they aren't using 'jesus' hacks
                            if(!player.isSprinting() && !player.isFlying())
                            {
                                tracker.increaseLevel(player);
                                plugin.log(player.getName()+" is walking too fast in water! XSpeed="+xd+" ZSpeed="+zd);
                                event.setTo(event.getFrom().clone());
                            } 
                        }
                    }
                    else
                    {
                        if(!player.hasPermission("anticheat.waterwalk"))
                        {                      
                            //If they are flying/sprinting give them a bit of slack
                            if(xd > 0.3D || zd > 0.3D)
                            {
                                tracker.increaseLevel(player);
                                plugin.log(player.getName()+" is flying/sprinting too fast in water! XSpeed="+xd+" ZSpeed="+zd);
                                event.setTo(event.getFrom().clone());
                            }
                        }
                    }                
                }
                else
                {
                    //Are they in a vehicle?
                    if (player.getVehicle() != null)
                    {
                        //If they are just entering it, skip the check. They are moving way too fast naturally.
                        if(!ex.isEntering(player))
                        {
                            if(xd > 0.6D || zd > 0.6D)
                            {
                                tracker.increaseLevel(player);
                                plugin.log(player.getName()+" is using a vehicle too fast! XSpeed="+xd+" ZSpeed="+zd);
                                event.setTo(event.getFrom().clone());
                            }
                        }
                    }        
                    //Otherwise, are they sneaking?
                    else if(player.isSneaking())
                    {
                        if(!player.hasPermission("anticheat.sneakhack") && !player.isFlying())
                        {                      
                            //Make sure they are at normal sneak speeds. (not using sneak hacks)
                            if(xd > 0.2D || zd > 0.2D)
                            {
                                tracker.increaseLevel(player);
                                plugin.log(player.getName()+" is sneaking too fast! XSpeed="+xd+" ZSpeed="+zd);
                                event.setTo(event.getFrom().clone());
                                //If they are, force them out of it.
                                player.setSneaking(false);
                            }
                        }
                    }
                    //Otherwise set a hardcoded limit to any other traveling
                    else if(xd > 0.4D || zd > 0.4D)
                    {
                        if(!player.hasPermission("anticheat.speedhack"))
                        {                      
                            if(!player.isFlying() && !player.hasPotionEffect(PotionEffectType.SPEED))
                            {
                                if(!player.isSprinting())
                                {
                                    tracker.increaseLevel(player);
                                    plugin.log(player.getName()+" is walking too fast! XSpeed="+xd+" ZSpeed="+zd);
                                    event.setTo(event.getFrom().clone());
                                }              
                                else
                                {
                                    //If they are sprinting or flying give slack
                                    if(xd > 0.7D || zd > 0.7D)
                                    {
                                        tracker.increaseLevel(player);
                                        plugin.log(player.getName()+" is sprinting too fast! XSpeed="+xd+" ZSpeed="+zd);
                                        event.setTo(event.getFrom().clone());
                                    }
                                }
                            }
                        }
                    }
                }
                //If the player is ascending
                if(event.getFrom().getY() < event.getTo().getY())
                {
                    //TODO: This is a little hacky. Any better way to figure this out?
                    //Are they climbing something?
                    if(yd <= 0.11761 && yd >= 0.11759)
                    {
                        if(!player.hasPermission("anticheat.spider"))
                        {                              
                            if(player.getLocation().getBlock().getType() != Material.VINE && player.getLocation().getBlock().getType() != Material.LADDER)
                            {
                                //If it's not climbable, block it.
                                plugin.log(player.getName()+" tried to climb a wall!");
                                tracker.increaseLevel(player);
                                event.setTo(event.getFrom().clone());
                            }
                        }
                    }
                    else if(!player.isFlying() && player.getVehicle() == null)
                    {
                        if(!player.hasPermission("anticheat.flyhack"))
                        {                              
                            //Otherwise check for fast ascension
                            if(yd > 0.5D)
                            {
                                tracker.increaseLevel(player);
                                plugin.log(player.getName()+" is ascending too fast! YSpeed="+yd);
                                event.setTo(event.getFrom().clone());
                            }
                        }
                    }
                } 
                //If they are falling
                else if(event.getFrom().getY() > event.getTo().getY())
                {         
                    //Ignore players in creative or in vehicles, they give a fall distance of 0 naturally.
                    if(player.getGameMode() != GameMode.CREATIVE && player.getVehicle() == null)
                    {
                        if(!player.hasPermission("anticheat.nofall"))
                        {                             
                            hm.log(player);
                            //Log health (for nofall detection)
                            if(hm.checkFall(player))
                            {
                                //If the player is falling but has a 0 fall distance
                                plugin.log(player.getName()+" tried to avoid fall damage!");
                                tracker.increaseLevel(player);
                            }
                        }
                    }
                } 
                //No change in Y
                else
                {
                    if(!player.hasPermission("anticheat.flyhack") && !player.isFlying() && player.getVehicle() == null)
                    {                  
                        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
                        if(!player.isSneaking())
                        {
                            if(!canStand(block) && !canStand(block.getRelative(BlockFace.NORTH)) && !canStand(block.getRelative(BlockFace.EAST)) && !canStand(block.getRelative(BlockFace.SOUTH)) && !canStand(block.getRelative(BlockFace.WEST)))
                            {                            
                                if (fm.checkFly(player))
                                {
                                    event.setTo(event.getFrom().clone());
                                    plugin.log(player.getName()+" tried to fly!");
                                    tracker.increaseLevel(player);
                                }
                                else
                                {
                                    tracker.decreaseLevel(player);
                                }
                            }
                        }
                        else
                        {
                            if(!canStand(block) && !canStand(block.getRelative(BlockFace.NORTH)) && !canStand(block.getRelative(BlockFace.EAST)) && !canStand(block.getRelative(BlockFace.SOUTH)) && !canStand(block.getRelative(BlockFace.WEST)) && !canStand(block.getRelative(BlockFace.NORTH_WEST)) && !canStand(block.getRelative(BlockFace.NORTH_EAST)) && !canStand(block.getRelative(BlockFace.SOUTH_WEST)) && !canStand(block.getRelative(BlockFace.SOUTH_EAST)))
                            {
                                if (fm.checkFly(player))
                                {
                                    event.setTo(event.getFrom().clone());
                                    plugin.log(player.getName()+" tried to fly!");
                                    tracker.increaseLevel(player);
                                }
                                else
                                {
                                    tracker.decreaseLevel(player);
                                }                            
                            }
                        }                    
                    }
                }
            }            
        }
    } 
    
    public boolean canStand(Block block)
    {
        if(block.getRelative(BlockFace.UP).getType() == Material.STEP || block.getRelative(BlockFace.UP).getType() == Material.SOUL_SAND || block.getRelative(BlockFace.UP).getTypeId() == 111)
        {
            return true;
        }        
        if(block.isLiquid())
        {
            return false;
        }
        else if (block.getType() == Material.AIR)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}

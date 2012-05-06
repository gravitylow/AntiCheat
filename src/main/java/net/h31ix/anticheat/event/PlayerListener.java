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
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener {
    private Anticheat plugin;
    private AnimationManager am;
    private ExemptManager ex;
    private PlayerTracker tracker;
    private ItemManager im;
    private HealthManager hm;
    private LoginManager lm;
    private FlyManager fm;
    private BowManager bm;
    private FoodManager fom;
    
    public PlayerListener(Anticheat plugin)
    {
        this.plugin = plugin;
        this.am = plugin.am;
        this.ex = plugin.ex;
        this.im = plugin.im;
        this.tracker = plugin.getPlayerTracker();
        this.hm = plugin.hm;
        this.lm = plugin.lm;
        this.fm = plugin.fm;
        this.bm = plugin.bm;
        this.fom = plugin.fom;
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
       if(!player.hasPermission("anticheat.zombe.fly"))
       {       
           player.sendMessage("§f §f §1 §0 §2 §4");
       }
       if(!player.hasPermission("anticheat.zombe.cheat"))
       {
           player.sendMessage("§f §f §2 §0 §4 §8");
       }
       if(!player.hasPermission("anticheat.zombe.noclip"))
       {
           player.sendMessage("§f §f §4 §0 §9 §6");            
       }           
    }
    
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        Player player = event.getPlayer();
        if(plugin.check(player))
        {        
            if(!player.hasPermission("anticheat.spamdrop") && !im.hasDropped(player))
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
            //Block command spamming (consider them chats)
            plugin.cm.addChat(event.getPlayer());
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
            else
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
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        if(event.getAction() == Action.RIGHT_CLICK_AIR)
        {
            Material m = inv.getItemInHand().getType();
            if(m == Material.BOW)
            {
                bm.logWindUp(player);
            }
            else if(m == Material.COOKED_BEEF || m == Material.COOKED_CHICKEN || m == Material.COOKED_FISH || m == Material.GRILLED_PORK || m == Material.PORK || m == Material.MUSHROOM_SOUP ||  m == Material.RAW_BEEF || m == Material.RAW_CHICKEN || m == Material.RAW_FISH || m == Material.APPLE || m == Material.GOLDEN_APPLE || m == Material.MELON || m == Material.COOKIE || m == Material.SPIDER_EYE || m == Material.ROTTEN_FLESH)
            {
                fom.logStart(player);
            }
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        if(plugin.check(player) && !ex.isHit(player))
        {        
            //Log the player's health level
            hm.log(player);
            //Get distances for hack checks.
            LengthCheck c = new LengthCheck(event.getFrom(), event.getTo());
            double xd = c.getXDifference();
            double zd = c.getZDifference();
            double yd = c.getYDifference();
            String speed = "XSpeed="+xd+" ZSpeed="+zd;
            Block p1 = player.getLocation().getWorld().getBlockAt(player.getLocation());
            //Is the player in water?
            if(p1.isLiquid())
            {
                //Are they using a boat? If so give them a bit more leniency
                if (player.getVehicle() != null)
                {
                    if(xd > 2.0D || zd > 2.0D)
                    {
                        tracker.increaseLevel(player,2);
                        plugin.log(player.getName()+" is using a boat too fast! "+speed);
                    }
                }                
                else if(xd > 0.19D || zd > 0.19D)
                {
                    if(!player.hasPermission("anticheat.waterwalk") && !player.isSprinting() && !player.isFlying() && player.getNearbyEntities(1, 1, 1).isEmpty())
                    {                    
                        tracker.increaseLevel(player,2);
                        plugin.log(player.getName()+" is walking too fast in water! "+speed);
                        event.setTo(event.getFrom().clone());
                    }
                }
                else
                {
                    if(!player.hasPermission("anticheat.waterwalk") && xd > 0.3D || zd > 0.3D)
                    {                      
                        tracker.increaseLevel(player,2);
                        plugin.log(player.getName()+" is flying/sprinting too fast in water! "+speed);
                        event.setTo(event.getFrom().clone());
                    }
                }                
            }
            else
            {
                //Are they in a vehicle?
                if (player.getVehicle() != null)
                {
                    //Nothing to do
                }        
                //Otherwise, are they sneaking?
                else if(player.isSneaking())
                {
                    if(!player.hasPermission("anticheat.sneakhack") && !player.isFlying()) 
                    {       
                        if(xd > 0.2D || zd > 0.2D)
                        {
                            tracker.increaseLevel(player,2);
                            plugin.log(player.getName()+" is sneaking too fast! "+speed);
                            event.setTo(event.getFrom().clone());
                            //If they are, force them out of it.
                            player.setSneaking(false);
                        }
                    }
                }
                //Otherwise set a hardcoded limit to any other traveling
                else if(xd > 0.4D || zd > 0.4D)
                {
                    if(!player.hasPermission("anticheat.speedhack") && !player.isFlying() && !player.hasPotionEffect(PotionEffectType.SPEED))
                    {                      
                        if(!player.isSprinting())
                        {
                            tracker.increaseLevel(player,2);
                            plugin.log(player.getName()+" is walking too fast! "+speed);
                            event.setTo(event.getFrom().clone());
                        }              
                        else
                        {
                            //If they are sprinting or flying give slack
                            if(xd > 0.7D || zd > 0.7D)
                            {
                                tracker.increaseLevel(player,2);
                                plugin.log(player.getName()+" is sprinting too fast! "+speed);
                                event.setTo(event.getFrom().clone());
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
                    if(!player.hasPermission("anticheat.spider") && player.getLocation().getBlock().getType() != Material.VINE && player.getLocation().getBlock().getType() != Material.LADDER)
                    {                              
                        //If it's not climbable, block it.
                        plugin.log(player.getName()+" tried to climb a wall!");
                        tracker.increaseLevel(player,3);
                        event.setTo(event.getFrom().clone());
                    }
                }
                else if(!player.isFlying() && player.getVehicle() == null && !player.hasPermission("anticheat.flyhack") && yd > 0.5D)
                {
                    tracker.increaseLevel(player,2);
                    plugin.log(player.getName()+" is ascending too fast! YSpeed="+yd);
                    event.setTo(event.getFrom().clone());
                }
            } 
            //If they are falling
            else if(event.getFrom().getY() > event.getTo().getY())
            {         
                //Ignore players in creative or in vehicles, they give a fall distance of 0 naturally.
                if(!player.hasPermission("anticheat.nofall") && player.getGameMode() != GameMode.CREATIVE && player.getVehicle() == null)
                {                           
                    hm.log(player);
                    //Log health (for nofall detection)
                    if(hm.checkFall(player))
                    {
                        //If the player is falling but has a 0 fall distance
                        plugin.log(player.getName()+" tried to avoid fall damage!");
                        tracker.increaseLevel(player,2);
                    }
                }
            } 
            //No change in Y
            else
            {
                if(!player.hasPermission("anticheat.flyhack") && !player.hasPermission("anticheat.zombe.fly") && !player.isFlying() && player.getVehicle() == null)
                {                  
                    Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    if(!canStand(block) && !canStand(block.getRelative(BlockFace.NORTH)) && !canStand(block.getRelative(BlockFace.EAST)) && !canStand(block.getRelative(BlockFace.SOUTH)) && !canStand(block.getRelative(BlockFace.WEST)) && !canStand(block.getRelative(BlockFace.NORTH_WEST)) && !canStand(block.getRelative(BlockFace.NORTH_EAST)) && !canStand(block.getRelative(BlockFace.SOUTH_WEST)) && !canStand(block.getRelative(BlockFace.SOUTH_EAST)))
                    {
                        if (fm.checkFly(player))
                        {
                            event.setTo(event.getFrom().clone());
                            plugin.log(player.getName()+" tried to fly!");
                            tracker.increaseLevel(player,2);
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
    
    public boolean canStand(Block block)
    {
        if(block.getRelative(BlockFace.UP).getType() == Material.STEP || block.getRelative(BlockFace.UP).getType() == Material.SOUL_SAND || block.getRelative(BlockFace.UP).getTypeId() == 111)
        {
            return true;
        }        
        else
        {
            return true;
        }
    }
}

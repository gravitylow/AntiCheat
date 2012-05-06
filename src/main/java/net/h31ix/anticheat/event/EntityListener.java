package net.h31ix.anticheat.event;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.PlayerTracker;
import net.h31ix.anticheat.checks.LengthCheck;
import net.h31ix.anticheat.manage.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EntityListener implements Listener {
    private Anticheat plugin;
    private BowManager bm;
    private ExemptManager ex;
    private PlayerTracker tracker;
    private HealthManager hm;
    private FoodManager fom;
    private SprintManager sp;
    private static final double ENTITY_MAX_DISTANCE_XZ = 5.0;
    private static final double ENTITY_MAX_DISTANCE_Y = 4.3;
    
    public EntityListener(Anticheat plugin)
    {
        this.plugin = plugin;
        this.bm = plugin.bm;
        this.tracker = plugin.getPlayerTracker();
        this.ex = plugin.ex;
        this.hm = plugin.hm;
        this.fom = plugin.fom;
        this.sp = plugin.sp;
    }
    
    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event)
    {
        if(event.getEntity() instanceof Player)
        {
            Player player = (Player)event.getEntity();
            if(plugin.check(player))
            {
                if(!player.hasPermission("anticheat.autofire"))
                {            
                    if(!bm.hasShot(player))
                    {
                        tracker.decreaseLevel(player);
                        bm.logShoot((Player)event.getEntity());
                    }
                    else
                    {
                        event.setCancelled(true);
                        tracker.increaseLevel(player,2);
                        plugin.log(player.getName()+" tried to fire a bow too fast!");
                    }
                    if(bm.justWoundUp(player))
                    {
                        event.setCancelled(true);
                        tracker.increaseLevel(player,2);
                        plugin.log(player.getName()+" tried to fire a bow too fast!");                        
                    }
                    else
                    {
                        tracker.decreaseLevel(player);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event)
    {
        if(event.getEntity() instanceof Player)
        {
            Player player = (Player)event.getEntity();
            if(event.getRegainReason() == RegainReason.SATIATED)
            {
                if(!player.hasPermission("anticheat.instantheal"))
                {
                    if(hm.justHealed(player))
                    {
                        event.setCancelled(true);
                        tracker.increaseLevel(player,3);
                        plugin.log(player.getName()+" tried to heal too fast!");
                    }
                    else
                    {
                        hm.logHeal(player);
                        tracker.decreaseLevel(player);
                    }                       
                }
            }
        }
    }
    
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
        if(event.getEntity() instanceof Player)
        {
            Player player = (Player)event.getEntity();
            if(!player.hasPermission("anticheat.instanteat"))
            {
                if(fom.justStarted(player))
                {
                    event.setCancelled(true);
                    tracker.increaseLevel(player,3);
                    plugin.log(player.getName()+" tried to eat too fast!");
                }
                else
                {
                    tracker.decreaseLevel(player);
                }   
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event instanceof EntityDamageByEntityEvent)
        {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;    
            if(e.getDamager() instanceof Player)
            {
                Player player = (Player) e.getDamager();
                if(sp.justSprinted(player))
                {
                    event.setCancelled(true);
                    tracker.increaseLevel(player,2);
                    plugin.log(player.getName()+" tried to sprint and damage too fast!");                      
                }
            }
            if(event.getEntity() instanceof Player)
            {      
                Player player = (Player)event.getEntity();
                if(plugin.check(player))
                {
                    if (e.getDamager() instanceof Player)
                    {         
                        Player p = (Player) e.getDamager();            
                        //Being damaged causes the player to move really fast, usually triggering a warning and
                        //A teleport, so give them a grace period after damaging someone or being hit for recovery.
                        int time = 50;
                        if(p.getInventory().getItemInHand().getEnchantments().containsKey(Enchantment.KNOCKBACK))
                        {
                            //If they were hit using knockback supply double recovery time
                            time = 100;
                        }
                        ex.logHit(p,time);                       
                        if(!p.hasPermission("anticheat.longreach"))
                        {                      
                            //Make sure they are close enough to the entity to hit them
                            LengthCheck lc = new LengthCheck(event.getEntity().getLocation(),p.getLocation());
                            if(lc.getXDifference() > ENTITY_MAX_DISTANCE_XZ || lc.getZDifference() > ENTITY_MAX_DISTANCE_XZ || lc.getYDifference() > ENTITY_MAX_DISTANCE_Y)
                            {
                                tracker.increaseLevel(player,2);
                                plugin.log(player.getName()+" tried to hit an entity too far away!");                                
                                event.setCancelled(true);
                            } 
                        }
                        ex.logHit(player,time);
                    }
                    else
                    {
                        ex.logHit(player,50);
                    }
                }
            }  
        }     
    }     
}

package net.h31ix.anticheat.event;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.PlayerTracker;
import net.h31ix.anticheat.checks.LengthCheck;
import net.h31ix.anticheat.manage.BowManager;
import net.h31ix.anticheat.manage.ExemptManager;
import net.h31ix.anticheat.manage.HealthManager;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityShootBowEvent;

public class EntityListener implements Listener {
    Anticheat plugin;
    BowManager bm;
    ExemptManager ex;
    PlayerTracker tracker;
    HealthManager hm;
    
    public EntityListener(Anticheat plugin)
    {
        this.plugin = plugin;
        bm = plugin.bm;
        tracker = plugin.tracker;
        ex = plugin.ex;
        hm = plugin.hm;
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
                if(!player.hasPermission("anticheat.instaheal"))
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
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event instanceof EntityDamageByEntityEvent)
        {       
            if(event.getEntity() instanceof Player)
            {
                final Player player = (Player)event.getEntity();
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        //Check damage ticks?
                    }
                },      2);                   
            }
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            if (e.getDamager() instanceof Player)
            {         
                Player p = (Player) e.getDamager(); 
                if(plugin.check(p))
                {                
                    if(event.getEntity() instanceof Player)
                    {
                        Player player = (Player)event.getEntity();
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
                            if(lc.getXDifference() > 5.0D || lc.getZDifference() > 5.0D || lc.getYDifference() > 4.3D)
                            {
                                event.setCancelled(true);
                            } 
                        }
                        ex.logHit(player,time);
                    }
                }
            }  
        }     
    }     
}

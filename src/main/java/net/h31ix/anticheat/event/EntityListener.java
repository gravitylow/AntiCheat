package net.h31ix.anticheat.event;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.PlayerTracker;
import net.h31ix.anticheat.checks.EyeCheck;
import net.h31ix.anticheat.checks.LengthCheck;
import net.h31ix.anticheat.manage.BowManager;
import net.h31ix.anticheat.manage.ExemptManager;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class EntityListener implements Listener {
    Anticheat plugin;
    BowManager bm;
    ExemptManager ex;
    PlayerTracker tracker;
    
    public EntityListener(Anticheat plugin)
    {
        this.plugin = plugin;
        bm = plugin.bm;
        tracker = plugin.tracker;
        ex = plugin.ex;
    }
    
    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event)
    {
        if(event.getEntity() instanceof Player)
        {
            Player player = (Player)event.getEntity();
            if(!bm.hasShot(player))
            {
                tracker.decreaseLevel(player);
                bm.logShoot((Player)event.getEntity());
            }
            else
            {
                event.setCancelled(true);
                tracker.increaseLevel(player);
                plugin.log(player.getName()+" tried to fire a bow too fast!");
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event instanceof EntityDamageByEntityEvent)
        {       
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            if (e.getDamager() instanceof Player)
            {         
                Player p = (Player) e.getDamager(); 
                if(event.getEntity() instanceof Player)
                {
                    Player player = (Player)event.getEntity();
                    int time = 50;
                    if(p.getInventory().getItemInHand().getEnchantments().containsKey(Enchantment.KNOCKBACK))
                    {
                        time = 100;
                    }
                    ex.logHit(p,50);
                    LengthCheck lc = new LengthCheck(event.getEntity().getLocation(),p.getLocation());
                    if(lc.getXDifference() > 5.0D || lc.getZDifference() > 5.0D || lc.getYDifference() > 4.3D)
                    {
                        event.setCancelled(true);
                    } 
                    ex.logHit(player,time);
                }
            }
        }     
    }     
}

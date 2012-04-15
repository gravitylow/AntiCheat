package net.h31ix.anticheat.event;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.checks.EyeCheck;
import net.h31ix.anticheat.checks.LengthCheck;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityListener implements Listener {
    Anticheat plugin;
    
    public EntityListener(Anticheat plugin)
    {
        this.plugin = plugin;
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
                LengthCheck lc = new LengthCheck(event.getEntity().getLocation(),p.getLocation());
                if(lc.getXDifference() > 5.0D || lc.getZDifference() > 5.0D || lc.getYDifference() > 4.3D)
                {
                    System.out.println("canc");
                    event.setCancelled(true);
                } 
                EyeCheck ec = new EyeCheck(p,((CraftEntity)event.getEntity()).getHandle());
                double off = ec.getOffset();
                if(off > 0.1D && off != -1) 
                {
                    p.sendMessage("canc");
                    event.setCancelled(true);                    
                }
            }
        }
    }     
}

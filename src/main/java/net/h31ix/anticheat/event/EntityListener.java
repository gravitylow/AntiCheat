/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 H31IX http://h31ix.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.h31ix.anticheat.event;

import net.h31ix.anticheat.manage.Backend;
import net.h31ix.anticheat.manage.CheckManager;
import net.h31ix.anticheat.manage.CheckType;
import net.h31ix.anticheat.manage.Distance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;


public class EntityListener extends EventListener 
{    
    private final Backend backend = getBackend();
    private final CheckManager checkManager = getCheckManager();
    
    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event)
    {
        if(event.getEntity() instanceof Player)
        {
            Player player = (Player)event.getEntity();   
            if(checkManager.willCheck(player, CheckType.FAST_BOW))
            {      
                if(backend.justWoundUp(player))
                {
                    event.setCancelled(true);
                    log("tried to fire a bow too fast.",player,CheckType.FAST_BOW);
                }
                else
                {
                    decrease(player);
                }                
            }
        }
    }
    
    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event)
    {
        if(event.getEntity() instanceof Player && event.getRegainReason() == RegainReason.SATIATED)
        {
            Player player = (Player)event.getEntity();
            if(checkManager.willCheck(player, CheckType.FAST_HEAL)) 
            {
                if(backend.justHealed(player))
                {
                    event.setCancelled(true);
                    log("tried to heal too fast.",player,CheckType.FAST_HEAL);  
                }
                else
                {
                    decrease(player);
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
            if(checkManager.willCheck(player, CheckType.FAST_EAT)) 
            {
                if(backend.justStartedEating(player))
                {
                    event.setCancelled(true);
                    log("tried to eat too fast.",player,CheckType.FAST_EAT); 
                }
                else
                {
                    decrease(player);
                }                 
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        boolean noHack = true;
        if (event instanceof EntityDamageByEntityEvent)
        {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;    
            if(event.getEntity() instanceof Player)
            {      
                Player player = (Player)event.getEntity();
                if (e.getDamager() instanceof Player)
                {         
                    Player p = (Player)e.getDamager();   
                    backend.logDamage(p);
                    backend.logDamage(player);
                    if(checkManager.willCheck(p, CheckType.LONG_REACH))
                    {
                        Distance distance = new Distance(player.getLocation(),p.getLocation());
                        if(backend.checkLongReachDamage(distance.getXDifference(),distance.getYDifference(),distance.getZDifference()))
                        {
                            event.setCancelled(true);
                            log("tried to damage a player too far away from them.",p,CheckType.LONG_REACH); 
                            noHack = false;
                        }                         
                    }                     
                }
                else
                {
                    backend.logDamage(player);
                }
            }
            if(e.getDamager() instanceof Player)
            {
                Player player = (Player) e.getDamager();
                if(checkManager.willCheck(player, CheckType.FORCEFIELD)) 
                {
                    if(backend.justSprinted(player))
                    {
                        event.setCancelled(true);
                        log("tried to sprint & damage too fast.",player,CheckType.FORCEFIELD);  
                        noHack = false;                        
                    }                     
                }
                if(checkManager.willCheck(player, CheckType.NO_SWING)) 
                {
                    if(!backend.justAnimated(player))
                    {
                        event.setCancelled(true);
                        log("tried to damage an entity without swinging their arm.",player,CheckType.NO_SWING);  
                        noHack = false;                        
                    }                     
                }                
                if(noHack)
                {
                    decrease(player);
                }                
            }   
        }     
    }
}
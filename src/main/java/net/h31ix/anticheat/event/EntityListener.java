/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 AntiCheat Team | http://h31ix.net
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

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.manage.Backend;
import net.h31ix.anticheat.manage.CheckManager;
import net.h31ix.anticheat.manage.CheckType;
import net.h31ix.anticheat.util.Configuration;
import net.h31ix.anticheat.util.Distance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public class EntityListener extends EventListener
{
    private final Backend backend = getBackend();
    private final CheckManager checkManager = getCheckManager();
    private final Configuration config = Anticheat.getManager().getConfiguration();

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();
            if (checkManager.willCheck(player, CheckType.FAST_BOW))
            {
                if (backend.checkFastBow(player, event.getForce()))
                {
                    event.setCancelled(!config.silentMode());
                    log("tried to fire a bow too fast.", player, CheckType.FAST_BOW);
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
        if (event.getEntity() instanceof Player && event.getRegainReason() == RegainReason.SATIATED)
        {
            Player player = (Player) event.getEntity();
            if (checkManager.willCheck(player, CheckType.FAST_HEAL))
            {
                if (backend.justHealed(player))
                {
                    event.setCancelled(!config.silentMode());
                    log("tried to heal too fast.", player, CheckType.FAST_HEAL);
                }
                else
                {
                    decrease(player);
                    backend.logHeal(player);
                }
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();
            if(player.getFoodLevel() < event.getFoodLevel()) // Make sure it's them actually gaining a food level
            {
                if (checkManager.willCheck(player, CheckType.FAST_EAT))
                {
                    if (backend.justStartedEating(player))
                    {
                        event.setCancelled(!config.silentMode());
                        log("tried to eat too fast.", player, CheckType.FAST_EAT);
                    }
                    else
                    {
                        decrease(player);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event)
    {
        boolean noHack = true;
        if (event instanceof EntityDamageByEntityEvent)
        {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            if (event.getEntity() instanceof Player)
            {
                Player player = (Player) event.getEntity();
                if (e.getDamager() instanceof Player)
                {
                    Player p = (Player) e.getDamager();
                    backend.logDamage(p, 1);
                    int value = p.getInventory().getItemInHand().containsEnchantment(Enchantment.KNOCKBACK) ? 2 : 1;
                    backend.logDamage(player, value);
                    if (checkManager.willCheck(p, CheckType.LONG_REACH))
                    {
                        Distance distance = new Distance(player.getLocation(), p.getLocation());
                        if (backend.checkLongReachDamage(distance.getXDifference(), distance.getYDifference(), distance.getZDifference()))
                        {
                            event.setCancelled(!config.silentMode());
                            log("tried to damage a player too far away from them.", p, CheckType.LONG_REACH);
                            noHack = false;
                        }
                    }
                }
                else
                {
                    if (e.getDamager() instanceof TNTPrimed || e.getDamager() instanceof Creeper)
                    {
                        backend.logDamage(player, 3);
                    }
                    else
                    {
                        backend.logDamage(player, 1);
                    }
                }
            }
            if (e.getDamager() instanceof Player)
            {
                Player player = (Player) e.getDamager();
                if (checkManager.willCheck(player, CheckType.FORCEFIELD))
                {
                    if (backend.justSprinted(player))
                    {
                        event.setCancelled(!config.silentMode());
                        log("tried to sprint & damage too fast.", player, CheckType.FORCEFIELD);
                        noHack = false;
                    }
                }
                if (checkManager.willCheck(player, CheckType.NO_SWING))
                {
                    if (!backend.justAnimated(player))
                    {
                        event.setCancelled(!config.silentMode());
                        log("tried to damage an entity without swinging their arm.", player, CheckType.NO_SWING);
                        noHack = false;
                    }
                }
                if (checkManager.willCheck(player, CheckType.FORCEFIELD))
                {
                    if(!backend.checkSight(player, e.getEntity()))
                    {
                        event.setCancelled(!config.silentMode());
                        log("tried to damage an entity that they couldn't see.", player, CheckType.FORCEFIELD);
                        noHack = false;                    
                    }
                }
                if (noHack)
                {
                    decrease(player);
                }
            }
        }
    }
}
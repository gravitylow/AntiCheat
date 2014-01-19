/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012-2014 AntiCheat Team | http://gravitydevelopment.net
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

package net.gravitydevelopment.anticheat.event;

import net.gravitydevelopment.anticheat.AntiCheat;
import net.gravitydevelopment.anticheat.check.CheckType;
import net.gravitydevelopment.anticheat.check.CheckResult;
import net.gravitydevelopment.anticheat.util.Distance;
import net.gravitydevelopment.anticheat.util.Utilities;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public class EntityListener extends EventListener {

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (getCheckManager().willCheck(player, CheckType.FAST_BOW)) {
                CheckResult result = getBackend().checkFastBow(player, event.getForce());
                if (result.failed()) {
                    event.setCancelled(!silentMode());
                    log(result.getMessage(), player, CheckType.FAST_BOW);
                } else {
                    decrease(player);
                }
            }
        }

        AntiCheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player && event.getRegainReason() == RegainReason.SATIATED) {
            Player player = (Player) event.getEntity();
            if (getCheckManager().willCheck(player, CheckType.FAST_HEAL)) {
                CheckResult result = getBackend().checkFastHeal(player);
                if (result.failed()) {
                    event.setCancelled(!silentMode());
                    log(result.getMessage(), player, CheckType.FAST_HEAL);
                } else {
                    decrease(player);
                    getBackend().logHeal(player);
                }
            }
        }

        AntiCheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getFoodLevel() < event.getFoodLevel() && getCheckManager().willCheck(player, CheckType.FAST_EAT)) // Make sure it's them actually gaining a food level
            {
                CheckResult result = getBackend().checkFastEat(player);
                if (result.failed()) {
                    event.setCancelled(!silentMode());
                    log(result.getMessage(), player, CheckType.FAST_EAT);
                } else {
                    decrease(player);
                }
            }
        }

        AntiCheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        boolean noHack = true;
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                // Keep players from shooting an arrow at themselves in order to fly
                if (e.getDamager() instanceof Arrow) {
                    Arrow arrow = (Arrow) e.getDamager();
                    if (arrow.getShooter() instanceof Player && event.getEntity() == arrow.getShooter()) {
                        event.setCancelled(true);
                    }
                }
                if (Utilities.hasArmorEnchantment(player, Enchantment.THORNS)) {
                    getBackend().logAnimation(player);
                }
                if (e.getDamager() instanceof Player) {
                    Player p = (Player) e.getDamager();
                    getBackend().logDamage(p, 1);
                    int value = p.getInventory().getItemInHand().containsEnchantment(Enchantment.KNOCKBACK) ? 2 : 1;
                    getBackend().logDamage(player, value);
                    if (getCheckManager().willCheck(p, CheckType.LONG_REACH)) {
                        Distance distance = new Distance(player.getLocation(), p.getLocation());
                        CheckResult result = getBackend().checkLongReachDamage(player, distance.getXDifference(), distance.getYDifference(), distance.getZDifference());
                        if (result.failed()) {
                            event.setCancelled(!silentMode());
                            log(result.getMessage(), p, CheckType.LONG_REACH);
                            noHack = false;
                        }
                    }
                } else {
                    if (e.getDamager() instanceof TNTPrimed || e.getDamager() instanceof Creeper) {
                        getBackend().logDamage(player, 3);
                    } else {
                        getBackend().logDamage(player, 1);
                    }
                }
            }
            if (e.getDamager() instanceof Player) {
                Player player = (Player) e.getDamager();
                getBackend().logDamage(player, 1);
                if (getCheckManager().willCheck(player, CheckType.AUTOTOOL)) {
                    CheckResult result = getBackend().checkAutoTool(player);
                    if (result.failed()) {
                        event.setCancelled(!silentMode());
                        log(result.getMessage(), player, CheckType.AUTOTOOL);
                        noHack = false;
                    }
                }
                if (getCheckManager().willCheck(player, CheckType.FORCEFIELD)) {
                    CheckResult result = getBackend().checkSprintDamage(player);
                    if (result.failed()) {
                        event.setCancelled(!silentMode());
                        log(result.getMessage(), player, CheckType.FORCEFIELD);
                        noHack = false;
                    }
                }
                if (getCheckManager().willCheck(player, CheckType.NO_SWING)) {
                    CheckResult result = getBackend().checkAnimation(player, event.getEntity());
                    if (result.failed()) {
                        event.setCancelled(!silentMode());
                        log(result.getMessage(), player, CheckType.NO_SWING);
                        noHack = false;
                    }
                }
                if (getCheckManager().willCheck(player, CheckType.FORCEFIELD)) {
                    CheckResult result = getBackend().checkSight(player, e.getEntity());
                    if (result.failed()) {
                        event.setCancelled(!silentMode());
                        log(result.getMessage(), player, CheckType.FORCEFIELD);
                        noHack = false;
                    }
                }
                if (noHack) {
                    decrease(player);
                }
            }
        }

        AntiCheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
}

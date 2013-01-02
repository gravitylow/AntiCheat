/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 AntiCheat Team | http://gravitydevelopment.net
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
import net.h31ix.anticheat.manage.User;
import net.h31ix.anticheat.util.Configuration;
import net.h31ix.anticheat.util.Distance;
import net.h31ix.anticheat.util.Permission;
import net.h31ix.anticheat.util.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.PlayerInventory;

public class PlayerListener extends EventListener {
    private final Backend backend = getBackend();
    private final CheckManager checkManager = getCheckManager();
    private final Configuration config = Anticheat.getManager().getConfiguration();
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (checkManager.willCheck(player, CheckType.SPAM) && config.commandSpam()) {
            backend.logChat(player);
            if (backend.checkSpam(player, event.getMessage())) {
                event.setCancelled(!config.silentMode());
                player.sendMessage(ChatColor.RED + "Please do not spam.");
            }
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        if (!event.isFlying()) {
            backend.logEnterExit(event.getPlayer());
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        if (event.getNewGameMode() != GameMode.CREATIVE) {
            backend.logEnterExit(event.getPlayer());
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            
            if (event.getEntity() instanceof Arrow) { return; }
            
            if (checkManager.willCheck(player, CheckType.FAST_PROJECTILE) && backend.checkProjectile(player)) {
                event.setCancelled(!config.silentMode());
                log("tried to fire projectiles too fast.", player, CheckType.FAST_PROJECTILE);
            }
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == TeleportCause.ENDER_PEARL || event.getCause() == TeleportCause.PLUGIN) {
            backend.logTeleport(event.getPlayer());
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler
    public void onPlayerChangeWorlds(PlayerChangedWorldEvent event) {
        backend.logTeleport(event.getPlayer());
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            backend.logToggleSneak(event.getPlayer());
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent event) {
        Player player = event.getPlayer();
        if (checkManager.willCheck(player, CheckType.FLY) && checkManager.willCheck(player, CheckType.ZOMBE_FLY)) // @h31ix: Change if necessary. I'm not sure what perms should go here :3
        {
            if (backend.justVelocity(player) && backend.extendVelocityTime(player)) {
                event.setCancelled(!config.silentMode());
                return; // don't log it lol.
            }
            backend.logVelocity(player);
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (checkManager.willCheck(player, CheckType.SPAM) && config.chatSpam()) {
            backend.logChat(player);
            if (backend.checkSpam(player, event.getMessage())) {
                boolean b = !config.silentMode();
                event.setCancelled(b);
                if (b) {
                    player.sendMessage(ChatColor.RED + config.getLang().getChatWarning());
                }
            }
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        backend.clearChatLevel(event.getPlayer());
        backend.garbageClean(event.getPlayer());
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        backend.garbageClean(event.getPlayer());
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();
        if (checkManager.willCheck(player, CheckType.SPRINT)) {
            if (backend.checkSprintHungry(event)) {
                event.setCancelled(!config.silentMode());
                log("tried to sprint while hungry.", player, CheckType.SPRINT);
            } else {
                decrease(player);
            }
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Material m = inv.getItemInHand().getType();
            if (m == Material.BOW) {
                backend.logBowWindUp(player);
            } else if (Utilities.isFood(m)) {
                backend.logEatingStart(player);
            }
        }
        Block block = event.getClickedBlock();
        
        if (block != null) {
            Distance distance = new Distance(player.getLocation(), block.getLocation());
            backend.checkLongReachBlock(player, distance.getXDifference(), distance.getYDifference(), distance.getZDifference());
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (checkManager.willCheck(player, CheckType.ITEM_SPAM) && backend.checkFastDrop(player)) {
            event.setCancelled(!config.silentMode());
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerEnterBed(PlayerBedEnterEvent event) {
        backend.logEnterExit(event.getPlayer());
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerExitBed(PlayerBedLeaveEvent event) {
        backend.logEnterExit(event.getPlayer());
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler
    public void onPlayerAnimation(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        backend.logAnimation(player);
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String section = "\u00a7";
        if (checkManager.willCheck(player, CheckType.ZOMBE_FLY)) {
            player.sendMessage(section + "f " + section + "f " + section + "1 " + section + "0 " + section + "2 " + section + "4");
        }
        if (checkManager.willCheck(player, CheckType.ZOMBE_CHEAT)) {
            player.sendMessage(section + "f " + section + "f " + section + "2 " + section + "0 " + section + "4 " + section + "8");
        }
        if (checkManager.willCheck(player, CheckType.ZOMBE_NOCLIP)) {
            player.sendMessage(section + "f " + section + "f " + section + "4 " + section + "0 " + section + "9 " + section + "6");
        }
        backend.logJoin(player);
        if (getUserManager().getUser(player.getName()) == null) {
            getUserManager().addUser(new User(player.getName()));
        } else {
            getUserManager().addUserFromFile(player.getName());
        }
        if (player.hasMetadata(Anticheat.SPY_METADATA)) {
            for (Player p : player.getServer().getOnlinePlayers()) {
                if (!Permission.SYSTEM_SPY.get(p)) {
                    p.hidePlayer(player);
                }
            }
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void checkExploit(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        Distance distance = new Distance(from, to);
        double y = distance.getYDifference();
        backend.logAscension(player, from.getY(), to.getY());
        if (checkManager.willCheck(player, CheckType.SPEED) && backend.checkFreeze(player, from.getY(), to.getY())) {
            log("tried to freeze in mid-air.", player, CheckType.SPEED);
            player.kickPlayer("Freezing client");
        }
        if (checkManager.willCheck(player, CheckType.SPRINT)) {
            if (backend.checkSprintStill(player, from, to)) {
                event.setCancelled(!config.silentMode());
                log("tried to sprint while standing still.", player, CheckType.SPRINT);
            }
        }
        if (checkManager.willCheck(player, CheckType.FLY) && !player.isFlying() && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && backend.checkFlight(player, distance)) {
            if (!config.silentMode()) {
                event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
            }
            log("tried to fly.", player, CheckType.FLY);
        }
        if (checkManager.willCheck(player, CheckType.FLY) && !player.isFlying() && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && (backend.checkYAxis(player, distance) || backend.checkAscension(player, from.getY(), to.getY()))) {
            if (!config.silentMode()) {
                event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
            }
            log("tried to fly on y-axis", player, CheckType.FLY);
        }
        if (checkManager.willCheck(player, CheckType.VCLIP) && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && checkManager.willCheck(player, CheckType.FLY) && event.getFrom().getY() > event.getTo().getY()) {
            int result = backend.checkVClip(player, new Distance(event.getFrom(), event.getTo()));
            
            if (result > 0) {
                if (!config.silentMode()) {
                    // You come back up, and you suddenly feel sick from trying to stick yourself through the blocks..
                    Location newloc = new Location(player.getWorld(), event.getFrom().getX(), event.getFrom().getY() + result, event.getFrom().getZ());
                    if (result > 3) {
                        result = 3; // prevents players from using AC as a way to teleport to sky islands.
                    }
                    if (newloc.getBlock().getTypeId() == 0) {
                        event.setTo(newloc);
                    } else {
                        event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                    }
                    player.damage(3);
                }
                log("tried to move down through a block.", player, CheckType.VCLIP);
            }
        }
        if (checkManager.willCheck(player, CheckType.NOFALL) && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && checkManager.willCheck(player, CheckType.FLY) && !Utilities.isClimbableBlock(player.getLocation().getBlock()) && event.getFrom().getY() > event.getTo().getY() && backend.checkNoFall(player, y)) {
            if (!config.silentMode()) {
                event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                player.damage(2); // I added this in here so the player(s) would still receive damage.
            }
            log("tried to avoid fall damage.", player, CheckType.NOFALL);
        }

        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void checkSpeed(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        boolean changed = false;
        
        if (event.getTo() != event.getFrom()) {
            Distance distance = new Distance(from, to);
            double x = distance.getXDifference();
            double y = distance.getYDifference();
            double z = distance.getZDifference();
            if (checkManager.willCheck(player, CheckType.SPEED) && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && checkManager.willCheck(player, CheckType.FLY)) {
                if (event.getFrom().getY() < event.getTo().getY() && backend.checkYSpeed(player, y)) {
                    if (!config.silentMode()) {
                        event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                    }
                    log("tried to ascend too fast.", player, CheckType.SPEED);
                    changed = true;
                }
                if (backend.checkXZSpeed(player, x, z)) {
                    if (!config.silentMode()) {
                        event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                    }
                    log("tried to move too fast.", player, CheckType.SPEED);
                    changed = true;
                }
                if ((event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) && backend.checkTimer(player)) {
                    /*if (!config.silentMode()) {
                        event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                    }
                    log("tried to alter their timer.", player, CheckType.SPEED);
                    changed = true;*/
                }
            }
            if (checkManager.willCheck(player, CheckType.WATER_WALK) && backend.checkWaterWalk(player, x, y, z)) {
                if (!config.silentMode()) {
                    player.teleport(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(player.getLocation().add(0, -1, 0)));
                }
                log("tried to walk on water.", player, CheckType.WATER_WALK);
                changed = true;
            }
            if (checkManager.willCheck(player, CheckType.SNEAK) && backend.checkSneak(player, x, z)) {
                if (!config.silentMode()) {
                    event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                    player.setSneaking(false);
                }
                log("tried to sneak too fast.", player, CheckType.SNEAK);
                changed = true;
            }
            if (checkManager.willCheck(player, CheckType.SPIDER) && backend.checkSpider(player, y)) {
                if (!config.silentMode()) {
                    event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                }
                log("tried to climb a wall.", player, CheckType.SPIDER);
                changed = true;
            }
            
            if (!changed) {
                Anticheat.getManager().getUserManager().getUser(player.getName()).setGoodLocation(event.getFrom());
            }
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
}

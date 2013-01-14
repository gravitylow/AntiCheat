/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012-2013 AntiCheat Team | http://gravitydevelopment.net
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
import net.h31ix.anticheat.util.CheckResult;
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
            CheckResult result = backend.checkSpam(player, event.getMessage());
            if (result.failed()) {
                event.setCancelled(!config.silentMode());
                player.sendMessage(ChatColor.RED + "Please do not spam.");
                log(result.getMessage(), player, CheckType.SPAM);
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
            
            if (checkManager.willCheck(player, CheckType.FAST_PROJECTILE)) {
                CheckResult result = backend.checkProjectile(player);
                if(result.failed()) {
                    event.setCancelled(!config.silentMode());
                    log(result.getMessage(), player, CheckType.FAST_PROJECTILE);                    
                }
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
            CheckResult result = backend.checkSpam(player, event.getMessage());
            if (result.failed()) {
                boolean b = !config.silentMode();
                event.setCancelled(b);
                if (b) {
                    player.sendMessage(ChatColor.RED + config.getLang().getChatWarning());
                }
                log(result.getMessage(), player, CheckType.SPAM);
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
            CheckResult result = backend.checkSprintHungry(event);
            if (result.failed()) {
                event.setCancelled(!config.silentMode());
                log(result.getMessage(), player, CheckType.SPRINT);
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
        if (checkManager.willCheck(player, CheckType.ITEM_SPAM)) {
            CheckResult result = backend.checkFastDrop(player);
            if(result.failed()) {
                event.setCancelled(!config.silentMode());
                log(result.getMessage(), player, CheckType.ITEM_SPAM);
            }
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
        if (checkManager.willCheck(player, CheckType.SPEED)) {
            CheckResult result = backend.checkFreeze(player, from.getY(), to.getY());
            if(result.failed()) {
                log(result.getMessage(), player, CheckType.SPEED);
                if(!config.silentMode()) {
                    player.kickPlayer("Freezing client");
                }
            }
        }
        if (checkManager.willCheck(player, CheckType.SPRINT)) {
            CheckResult result = backend.checkSprintStill(player, from, to);
            if (result.failed()) {
                event.setCancelled(!config.silentMode());
                log(result.getMessage(), player, CheckType.SPRINT);
            }
        }
        if (checkManager.willCheck(player, CheckType.FLY) && !player.isFlying() && checkManager.willCheck(player, CheckType.ZOMBE_FLY)) {
            CheckResult result = backend.checkFlight(player, distance);
            if(result.failed()) {
                if (!config.silentMode()) {
                    event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                }
                log(result.getMessage(), player, CheckType.FLY);                
            }
        }
        if (checkManager.willCheck(player, CheckType.FLY) && !player.isFlying() && checkManager.willCheck(player, CheckType.ZOMBE_FLY)) {
            CheckResult result1 = backend.checkYAxis(player, distance);
            CheckResult result2 = backend.checkAscension(player, from.getY(), to.getY());
            String log = result1.failed() ? result1.getMessage() : result2.failed() ? result2.getMessage() : "";
            if(!log.equals("")) {
                if (!config.silentMode()) {
                    event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                }
                log(log, player, CheckType.FLY);                
            }
        }
        if (checkManager.willCheck(player, CheckType.VCLIP) && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && checkManager.willCheck(player, CheckType.FLY) && event.getFrom().getY() > event.getTo().getY()) {
            CheckResult result = backend.checkVClip(player, new Distance(event.getFrom(), event.getTo()));
            
            if (result.failed()) {
                if (!config.silentMode()) {
                    int data = result.getData() > 3 ? 3 : result.getData();                    
                    Location newloc = new Location(player.getWorld(), event.getFrom().getX(), event.getFrom().getY() + data, event.getFrom().getZ());
                    if (newloc.getBlock().getTypeId() == 0) {
                        event.setTo(newloc);
                    } else {
                        event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                    }
                    player.damage(3);
                }
                log(result.getMessage(), player, CheckType.VCLIP);
            }
        }
        if (checkManager.willCheck(player, CheckType.NOFALL) && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && checkManager.willCheck(player, CheckType.FLY) && !Utilities.isClimbableBlock(player.getLocation().getBlock()) && event.getFrom().getY() > event.getTo().getY()) {
            CheckResult result = backend.checkNoFall(player, y);
            if(result.failed()) {
                if (!config.silentMode()) {
                    event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                    player.damage(1); // I added this in here so the player would still receive damage.
                }
                log(result.getMessage(), player, CheckType.NOFALL);                
            }
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
                if (event.getFrom().getY() < event.getTo().getY()) {
                    CheckResult result = backend.checkYSpeed(player, y);
                    if(result.failed()) {
                        if (!config.silentMode()) {
                            event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                        }
                        log(result.getMessage(), player, CheckType.SPEED);
                        changed = true;
                    }
                }
                CheckResult result = backend.checkXZSpeed(player, x, z);
                if (result.failed()) {
                    if (!config.silentMode()) {
                        event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                    }
                    log(result.getMessage(), player, CheckType.SPEED);
                    changed = true;
                }
                if ((event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ())) {
                    result = backend.checkTimer(player);
                    if(result.failed()) {
                        /*if (!config.silentMode()) {
                            event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                        }
                        log("tried to alter their timer.", player, CheckType.SPEED);
                        changed = true;*/
                    }
                }
            }
            if (checkManager.willCheck(player, CheckType.WATER_WALK)) {
                CheckResult result = backend.checkWaterWalk(player, x, y, z);
                if(result.failed()) {
                    if (!config.silentMode()) {
                        player.teleport(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(player.getLocation().add(0, -1, 0)));
                    }
                    log(result.getMessage(), player, CheckType.WATER_WALK);
                    changed = true;
                }
            }
            if (checkManager.willCheck(player, CheckType.SNEAK)) {
                CheckResult result = backend.checkSneak(player, x, z);
                if(result.failed()) {
                    if (!config.silentMode()) {
                        event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                        player.setSneaking(false);
                    }
                    log(result.getMessage(), player, CheckType.SNEAK);
                    changed = true;                    
                }
            }
            if (checkManager.willCheck(player, CheckType.SPIDER)) {
                CheckResult result = backend.checkSpider(player, y);
                if(result.failed()) {
                    if (!config.silentMode()) {
                        event.setTo(Anticheat.getManager().getUserManager().getUser(player.getName()).getGoodLocation(from.clone()));
                    }
                    log(result.getMessage(), player, CheckType.SPIDER);
                    changed = true;
                }
            }
            
            if (!changed) {
                Anticheat.getManager().getUserManager().getUser(player.getName()).setGoodLocation(event.getFrom());
            }
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
}

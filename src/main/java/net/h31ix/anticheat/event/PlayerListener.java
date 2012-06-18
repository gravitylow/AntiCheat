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
import net.h31ix.anticheat.util.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.*;
import org.bukkit.inventory.PlayerInventory;

public class PlayerListener extends EventListener
{
    private final Backend backend = getBackend();
    private final CheckManager checkManager = getCheckManager();
    private final Configuration config = Anticheat.getManager().getConfiguration();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        if (checkManager.willCheck(player, CheckType.SPAM) && config.commandSpam())
        {
            backend.logChat(player);
            if (backend.checkSpam(player, event.getMessage()))
            {
                event.setCancelled(!config.silentMode());
                player.sendMessage(ChatColor.RED + "Please do not spam.");
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event)
    {
        if (event.getEntity().getShooter() instanceof Player)
        {
            Player player = (Player) event.getEntity().getShooter();
            if (checkManager.willCheck(player, CheckType.FAST_PROJECTILE))
            {
                backend.logProjectile(player, this);
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        if (event.getCause() == TeleportCause.ENDER_PEARL || event.getCause() == TeleportCause.PLUGIN)
        {
            backend.logTeleport(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerChangeWorlds(PlayerChangedWorldEvent event)
    {
        backend.logTeleport(event.getPlayer());
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event)
    {
        if (event.isSneaking())
        {
            backend.logToggleSneak(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent event)
    {
        Player player = event.getPlayer();
        if (checkManager.willCheck(player, CheckType.FLY) && checkManager.willCheck(player, CheckType.ZOMBE_FLY)) //@h31ix: Change if necessary.  I'm not sure what perms should go here :3
        {
            if (backend.justVelocity(player))
            {
                if (backend.extendVelocityTime(player))
                {
                    event.setCancelled(!config.silentMode());
                    return; // don't log it lol.
                }
            }
            backend.logVelocity(player);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(PlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if (checkManager.willCheck(player, CheckType.SPAM) && config.chatSpam())
        {
            backend.logChat(player);
            if (backend.checkSpam(player, event.getMessage()))
            {
                boolean b = !config.silentMode();
                event.setCancelled(b);
                if(b)
                {
                    player.sendMessage(ChatColor.RED + "Please do not spam.");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event)
    {
        backend.clearChatLevel(event.getPlayer());
        backend.garbageClean(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        backend.garbageClean(event.getPlayer());
    }

    @EventHandler
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event)
    {
        Player player = event.getPlayer();
        if (checkManager.willCheck(player, CheckType.SPRINT))
        {
            if (backend.checkSprint(event))
            {
                event.setCancelled(!config.silentMode());
                log("tried to sprint while hungry.", player, CheckType.SPRINT);
            }
            else
            {
                decrease(player);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        Block playerClick = player.getTargetBlock(Utilities.getNonSolid(), 5);
        PlayerInventory inv = player.getInventory();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            Material m = inv.getItemInHand().getType();
            if (m == Material.BOW)
            {
                backend.logBowWindUp(player);
            }
            else if (Utilities.isFood(m))
            {
                backend.logEatingStart(player);
            }
        }

        Block block = event.getClickedBlock();

        if (block != null)
        {
            Distance distance = new Distance(player.getLocation(), block.getLocation());
            backend.checkLongReachBlock(player, distance.getXDifference(), distance.getYDifference(), distance.getZDifference());

            /* Visuals Check */

            if (checkManager.willCheck(player, CheckType.VISUAL) && event.getAction() != Action.PHYSICAL)
            {
                if (backend.checkVisuals(player, block, playerClick))
                {
                    event.setCancelled(!config.silentMode());
                    log("tried to interact with an object that they couldn't see", player, CheckType.VISUAL);
                }
                else
                {
                    backend.logInteraction(player);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        Player player = event.getPlayer();
        if (checkManager.willCheck(player, CheckType.ITEM_SPAM))
        {
            if (backend.justDroppedItem(player))
            {
                event.setCancelled(!config.silentMode());
            }
            else
            {
                backend.logDroppedItem(player);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerEnterBed(PlayerBedEnterEvent event)
    {
        backend.logEnterExit(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerExitBed(PlayerBedLeaveEvent event)
    {
        backend.logEnterExit(event.getPlayer());
    }

    @EventHandler
    public void onPlayerAnimation(PlayerAnimationEvent event)
    {
        Player player = event.getPlayer();
        backend.logAnimation(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        String section = "\u00a7";
        if (checkManager.willCheck(player, CheckType.ZOMBE_FLY))
        {
            player.sendMessage(section + "f " + section + "f " + section + "1 " + section + "0 " + section + "2 " + section + "4");
        }
        if (checkManager.willCheck(player, CheckType.ZOMBE_CHEAT))
        {
            player.sendMessage(section + "f " + section + "f " + section + "2 " + section + "0 " + section + "4 " + section + "8");
        }
        if (checkManager.willCheck(player, CheckType.ZOMBE_NOCLIP))
        {
            player.sendMessage(section + "f " + section + "f " + section + "4 " + section + "0 " + section + "9 " + section + "6");
        }
        backend.logJoin(event.getPlayer());
        if (!getPlayerManager().hasLevel(player))
        {
            getPlayerManager().setLevel(player, getManager().getConfiguration().getLevel(player.getName()));
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void checkExploit(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        Distance distance = new Distance(from, to);
        double x = distance.getXDifference();
        double y = distance.getYDifference();
        double z = distance.getZDifference();
        backend.logAscension(player, from.getY(), to.getY());
        if (checkManager.willCheck(player, CheckType.FLY) && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && backend.checkFlight(player, distance))
        {
            if(!config.silentMode())
            {
                from.setX(from.getX() - 1);
                from.setY(from.getY() - 1);
                from.setZ(from.getZ() - 1);
                event.setTo(from);
                Block lower = player.getWorld().getHighestBlockAt(from);
                if (lower.getLocation().getY() + 2 < player.getLocation().getY())
                {
                    player.teleport(new Location(lower.getWorld(), lower.getLocation().getX(), lower.getLocation().getY() + 2, lower.getLocation().getZ()));
                }
                else
                {
                    player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 1, player.getLocation().getZ()));
                }
            }
            log("tried to fly.", player, CheckType.FLY);
        }
        if (checkManager.willCheck(player, CheckType.FLY) && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && (backend.checkYAxis(player, distance) || backend.checkAscension(player, from.getY(), to.getY())))
        {
            if(!config.silentMode())
            {            
                Block lower = player.getWorld().getHighestBlockAt(player.getLocation());
                if (lower.getLocation().getY() + 2 < player.getLocation().getY())
                {
                    player.teleport(new Location(lower.getWorld(), lower.getLocation().getX(), lower.getLocation().getY() + 2, lower.getLocation().getZ()));
                }
                else
                {
                    player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 1, player.getLocation().getZ()));
                }
            }
            log("tried to fly on y-axis", player, CheckType.FLY);
        }
        if (checkManager.willCheck(player, CheckType.SPEED) && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && checkManager.willCheck(player, CheckType.FLY))
        {
            if (event.getFrom().getY() < event.getTo().getY() && backend.checkYSpeed(player, y))
            {
                if(!config.silentMode())
                {                
                    event.setTo(from);
                }
                log("tried to ascend too fast.", player, CheckType.SPEED);
            }
            if (backend.checkXZSpeed(player, x, z))
            {
                if(!config.silentMode())
                {                
                    event.setTo(from);
                }
                log("tried to move too fast.", player, CheckType.SPEED);
            }
        }
        if (checkManager.willCheck(player, CheckType.NOFALL) && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && checkManager.willCheck(player, CheckType.FLY) && event.getFrom().getY() > event.getTo().getY() && backend.checkNoFall(player, y))
        {
            if(!config.silentMode())
            {            
                event.setTo(from);
            }
            log("tried avoid fall damage.", player, CheckType.NOFALL);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void checkSpeed(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if (event.getTo() != event.getFrom())
        {
            Distance distance = new Distance(from, to);
            double x = distance.getXDifference();
            double y = distance.getYDifference();
            double z = distance.getZDifference();
            if (checkManager.willCheck(player, CheckType.WATER_WALK) && backend.checkWaterWalk(player, x, z))
            {
                if(!config.silentMode())
                {                
                    event.setTo(from);
                }
                log("tried to walk on water.", player, CheckType.WATER_WALK);
            }
            if (checkManager.willCheck(player, CheckType.SNEAK) && backend.checkSneak(player, x, z))
            {
                if(!config.silentMode())
                {                
                    event.setTo(from);
                    player.setSneaking(false);
                }
                log("tried to sneak too fast.", player, CheckType.SNEAK);
            }
            if (checkManager.willCheck(player, CheckType.SPIDER) && backend.checkSpider(player, y))
            {
                if(!config.silentMode())
                {                
                    event.setTo(from);
                }
                log("tried to climb a wall.", player, CheckType.SPIDER);
            }
        }
    }
}
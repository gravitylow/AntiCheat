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
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener extends EventListener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        if (event.getInstaBreak() || Utilities.isInstantBreak(event.getBlock().getType())) {
            getBackend().logInstantBreak(player);
        }
        if (getCheckManager().willCheck(player, CheckType.AUTOTOOL)) {
            CheckResult result = getBackend().checkAutoTool(player);
            if (result.failed()) {
                event.setCancelled(!silentMode());
                log(result.getMessage(), player, CheckType.AUTOTOOL);
            }
        }

        AntiCheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (player != null && getCheckManager().willCheck(player, CheckType.FAST_PLACE)) {
            CheckResult result = getBackend().checkFastPlace(player);
            if (result.failed()) {
                event.setCancelled(!silentMode());
                log(result.getMessage(), player, CheckType.FAST_PLACE);
            } else {
                decrease(player);
                getBackend().logBlockPlace(player);
            }
        }

        AntiCheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        boolean noHack = true;
        if (player != null) {
            CheckResult result;
            if (getCheckManager().willCheck(player, CheckType.FAST_BREAK)) {
                result = getBackend().checkFastBreak(player, block);
                if (result.failed()) {
                    event.setCancelled(!silentMode());
                    log(result.getMessage(), player, CheckType.FAST_BREAK);
                    noHack = false;
                }
            }
            if (getCheckManager().willCheck(player, CheckType.NO_SWING)) {
                result = getBackend().checkSwing(player, block);
                if (result.failed()) {
                    event.setCancelled(!silentMode());
                    log(result.getMessage(), player, CheckType.NO_SWING);
                    noHack = false;
                }
            }
            if (getCheckManager().willCheck(player, CheckType.LONG_REACH)) {
                Distance distance = new Distance(player.getLocation(), block.getLocation());
                result = getBackend().checkLongReachBlock(player, distance.getXDifference(), distance.getYDifference(), distance.getZDifference());
                if (result.failed()) {
                    event.setCancelled(!silentMode());
                    log(result.getMessage(), player, CheckType.LONG_REACH);
                    noHack = false;
                }
            }
        }
        if (noHack) {
            decrease(player);
        }
        getBackend().logBlockBreak(player);
    }
}

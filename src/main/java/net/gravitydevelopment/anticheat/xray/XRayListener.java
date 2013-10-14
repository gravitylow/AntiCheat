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

package net.gravitydevelopment.anticheat.xray;

import net.gravitydevelopment.anticheat.AntiCheat;
import net.gravitydevelopment.anticheat.config.Configuration;
import net.gravitydevelopment.anticheat.manage.CheckManager;
import net.gravitydevelopment.anticheat.manage.CheckType;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class XRayListener implements Listener {
    private XRayTracker tracker = AntiCheat.getManager().getXRayTracker();
    private Configuration config = AntiCheat.getManager().getConfiguration();
    private CheckManager checkManager = AntiCheat.getManager().getCheckManager();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (config.getConfig().logXRayStats.getValue()) {
            Player p = event.getPlayer();
            if (p.getGameMode() == GameMode.CREATIVE && !config.getConfig().trackCreativeXRay.getValue()) {
                return;
            }
            String player = p.getName();
            if (checkManager.willCheck(p, CheckType.XRAY)) {
                Material m = event.getBlock().getType();
                if (m == Material.DIAMOND_ORE) {
                    tracker.addDiamond(player);
                } else if (m == Material.IRON_ORE) {
                    tracker.addIron(player);
                } else if (m == Material.GOLD_ORE) {
                    tracker.addGold(player);
                } else if (m == Material.LAPIS_ORE) {
                    tracker.addLapis(player);
                } else if (m == Material.REDSTONE_ORE || m == Material.GLOWING_REDSTONE_ORE) {
                    tracker.addRedstone(player);
                } else if (m == Material.GOLD_ORE) {
                    tracker.addGold(player);
                } else {
                    tracker.addBlock(player);
                }
                tracker.addTotal(player);
            }
        }
    }
}

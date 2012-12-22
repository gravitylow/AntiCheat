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

package net.h31ix.anticheat.xray;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.manage.CheckManager;
import net.h31ix.anticheat.manage.CheckType;
import net.h31ix.anticheat.util.Configuration;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class XRayListener implements Listener {
    private XRayTracker tracker = Anticheat.getManager().getXRayTracker();
    private Configuration config = Anticheat.getManager().getConfiguration();
    private CheckManager checkManager = Anticheat.getManager().getCheckManager();
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (config.logXRay()) {
            Player p = event.getPlayer();
            if (p.getGameMode() == GameMode.CREATIVE && !config.trackCreativeXRay()) { return; }
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
                } else if (m == Material.REDSTONE_ORE) {
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

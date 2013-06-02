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

package net.h31ix.anticheat.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.util.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * <p>
 * The manager that AntiCheat will check with to see if it should watch certain checks and certain players.
 */

public class CheckManager {
    private AnticheatManager manager = null;
    private Configuration config;
    private static List<CheckType> checkIgnoreList = new ArrayList<CheckType>();
    private static Map<String, List<CheckType>> exemptList = new HashMap<String, List<CheckType>>();
    private static int disabled = 0;
    private static int exempt = 0;

    public CheckManager(AnticheatManager instance) {
        manager = instance;
        config = manager.getConfiguration();
    }

    /**
     * Turn a check on
     *
     * @param type The CheckType to enable
     */
    public void activateCheck(CheckType type) {
        if (isActive(type)) {
            manager.log("The " + type.toString() + " check was activated.");
            checkIgnoreList.remove(type);
        }
    }

    /**
     * Turn a check off
     *
     * @param type The CheckType to disable
     */
    public void deactivateCheck(CheckType type) {
        if (!isActive(type)) {
            manager.log("The " + type.toString() + " check was deactivated.");
            checkIgnoreList.add(type);
            disabled++;
        }
    }

    /**
     * Determine whether a check is enabled
     *
     * @param type The CheckType to check
     * @return true if the check is active
     */
    public boolean isActive(CheckType type) {
        return !checkIgnoreList.contains(type);
    }

    /**
     * Exempt a player from a check
     *
     * @param player The player
     * @param type The check
     */
    public void exemptPlayer(Player player, CheckType type) {
        if (!isExempt(player, type)) {
            if (!exemptList.containsKey(player.getName())) {
                exemptList.put(player.getName(), new ArrayList<CheckType>());
            }
            manager.log(player.getName() + " was exempted from the " + type.toString() + " check.");
            exemptList.get(player.getName()).add(type);
            exempt++;
        }
    }

    /**
     * Unexempt a player from a check
     *
     * @param player The player
     * @param type The check
     */
    public void unexemptPlayer(Player player, CheckType type) {
        if (isExempt(player, type)) {
            manager.log(player.getName() + " was re-added to the " + type.toString() + " check.");
            exemptList.get(player.getName()).remove(type);
        }
    }

    /**
     * Determine whether a player is exempt from a check
     *
     * @param player The player
     * @param type The check
     */
    public boolean isExempt(Player player, CheckType type) {
        return exemptList.containsKey(player.getName()) ? exemptList.get(player.getName()).contains(type) : false;
    }

    /**
     * Determine whether a player is exempt from all checks from op status
     *
     * @param player The player
     */
    public boolean isOpExempt(Player player) {
        return (this.manager.getConfiguration().opExempt() && player.isOp());
    }

    /**
     * Determine whether a player should be checked in their world
     *
     * @param player The player
     * @return true if the player's world is enabled
     */
    public boolean checkInWorld(Player player) {
        return config.checkInWorld(player.getWorld());
    }

    /**
     * Run a quick version of the "willCheck" method, using the other non-check-specific methods beforehand
     *
     * @param player The player to check
     * @param type The check being run
     * @return true if the check should run
     */
    public boolean willCheckQuick(Player player, CheckType type) {
        return
            isActive(type)
            && !isExempt(player, type)
            && !type.checkPermission(player);
    }

    /**
     * Determine whether a check should run on a player
     *
     * @param player The player to check
     * @param type The check being run
     * @return true if the check should run
     */
    public boolean willCheck(Player player, CheckType type) {
        boolean check = isActive(type)
            && config.checkInWorld(player.getWorld())
            && !isExempt(player, type)
            && !type.checkPermission(player)
            && !isOpExempt(player);
        Anticheat.debugLog("Check " + type + (check ? " run " : " not run ") + "on " + player.getName());
        return check;
    }

    /**
     * Determine whether a player is actually online; not an NPC
     *
     * @param player Player to check
     * @return true if the player is a real person
     */
    public boolean isOnline(Player player) {
        // Check if the player is on the user list, e.g. is not an NPC
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equals(player.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the number of exempt players
     *
     * @return exempt players
     */
    public int getExempt() {
        int x = exempt;
        exempt = 0;
        return x;
    }

    /**
     * Get the number of disabled checks
     *
     * @return disabled checks
     */
    public int getDisabled() {
        int x = disabled;
        disabled = 0;
        return x;
    }
}

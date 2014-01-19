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

package net.gravitydevelopment.anticheat.api;

import net.gravitydevelopment.anticheat.AntiCheat;
import net.gravitydevelopment.anticheat.manage.AntiCheatManager;
import net.gravitydevelopment.anticheat.manage.CheckManager;
import net.gravitydevelopment.anticheat.check.CheckType;
import net.gravitydevelopment.anticheat.manage.UserManager;
import net.gravitydevelopment.anticheat.util.Group;
import net.gravitydevelopment.anticheat.xray.XRayTracker;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Developer's interface for all things AntiCheat.
 */

public class AntiCheatAPI {
    private static CheckManager chk = AntiCheat.getManager().getCheckManager();
    private static UserManager umr = AntiCheat.getManager().getUserManager();
    private static XRayTracker xtracker = AntiCheat.getManager().getXRayTracker();

    // CheckManager API

    /**
     * Start running a certain check
     *
     * @param type Check to start watching for
     */
    public static void activateCheck(CheckType type) {
        chk.activateCheck(type, getCallingClass());
    }

    /**
     * Stop running a certain check
     *
     * @param type Check to stop watching for
     */
    public static void deactivateCheck(CheckType type) {
        chk.deactivateCheck(type, getCallingClass());
    }

    /**
     * Find out if a check is currently being watched for
     *
     * @param type Type to check
     * @return true if plugin is watching for this check
     */
    public static boolean isActive(CheckType type) {
        return chk.isActive(type);
    }

    /**
     * Allow a player to skip a certain check
     *
     * @param player Player to stop watching
     * @param type   Check to stop watching for
     */
    public static void exemptPlayer(Player player, CheckType type) {
        chk.exemptPlayer(player, type, getCallingClass());
    }

    /**
     * Stop allowing a player to skip a certain check
     *
     * @param player Player to start watching
     * @param type   Check to start watching for
     */
    public static void unexemptPlayer(Player player, CheckType type) {
        chk.unexemptPlayer(player, type, getCallingClass());
    }

    /**
     * Find out if a player is currently exempt from a certain check
     *
     * @param player Player to check
     * @param type   Type to check
     * @return true if plugin is ignoring this check on this player
     */
    public static boolean isExempt(Player player, CheckType type) {
        return chk.isExempt(player, type);
    }

    /**
     * Find out if a check will occur for a player. This checks if they are being tracked, the check is active, the player isn't exempt from the check, and the player doesn't have override permission.
     *
     * @param player Player to check
     * @param type   Type to check
     * @return true if plugin will check this player, and that all things allow it to happen.
     */
    public boolean willCheck(Player player, CheckType type) {
        return chk.willCheck(player, type);
    }

    // PlayerManager API

    /**
     * Get a player's integer hack level
     *
     * @param player Player to check
     * @return player's hack level
     * @deprecated see {@link #getGroup(org.bukkit.entity.Player)}
     */
    @Deprecated
    public static int getLevel(Player player) {
        return umr.safeGetLevel(player.getName());
    }


    /**
     * Set a player's hack level
     *
     * @param player Player to set
     * @param level  Group to set to
     * @deprecated see {@link #resetPlayer(org.bukkit.entity.Player)}
     */
    @Deprecated
    public static void setLevel(Player player, int level) {
        umr.safeSetLevel(player.getName(), level);
    }

    /**
     * Reset a player's hack level to 0
     *
     * @param player Player to reset
     */
    public static void resetPlayer(Player player) {
        umr.getUser(player.getName()).resetLevel();
    }

    /**
     * Get a user's {@link net.gravitydevelopment.anticheat.util.Group}
     *
     * @param player Player whose group to find
     * @return The player's group
     */
    public static Group getGroup(Player player) {
        return umr.getUser(player.getName()).getGroup();
    }

    /**
     * Get all configured Groups
     *
     * @return List of all groups
     * @see net.gravitydevelopment.anticheat.util.Group
     */
    public static List<Group> getGroups() {
        return getManager().getConfiguration().getGroups().getGroups();
    }

    // XrayTracker API

    /**
     * Find out if a player is detected as using xray hacks (on any ore)
     *
     * @param player Player to check
     * @return true if the player has any xray anomalies.
     */
    public static boolean isXrayer(Player player) {
        String name = player.getName();
        return xtracker.sufficientData(name) && xtracker.hasAbnormal(name);
    }

    // Advanced Users Only API.

    /**
     * Get access to all the other managers, advanced users ONLY
     *
     * @return the AntiCheat Manager
     */
    public static AntiCheatManager getManager() {
        return AntiCheat.getManager();
    }

    private static String getCallingClass() {
        return sun.reflect.Reflection.getCallerClass(2).getName();
    }
}

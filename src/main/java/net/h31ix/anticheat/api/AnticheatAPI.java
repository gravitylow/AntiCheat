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

package net.h31ix.anticheat.api;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.manage.AnticheatManager;
import net.h31ix.anticheat.manage.CheckManager;
import net.h31ix.anticheat.manage.CheckType;
import net.h31ix.anticheat.manage.PlayerManager;
import net.h31ix.anticheat.xray.XRayTracker;
import org.bukkit.entity.Player;

/**
 * <p>
 * Developer's interface for all things AntiCheat.
 */

public class AnticheatAPI
{
    private static CheckManager chk = Anticheat.getManager().getCheckManager();
    private static PlayerManager pmr = Anticheat.getManager().getPlayerManager();
    private static XRayTracker xtracker = Anticheat.getManager().getXRayTracker();

    //CheckManager API

    /**
     * Start running a certain check
     * 
     * @param type
     *            Check to start watching for
     */
    public static void activateCheck(CheckType type)
    {
        chk.activateCheck(type);
    }

    /**
     * Stop running a certain check
     * 
     * @param type
     *            Check to stop watching for
     */
    public static void deactivateCheck(CheckType type)
    {
        chk.deactivateCheck(type);
    }

    /**
     * Find out if a check is currently being watched for
     * 
     * @param type
     *            Type to check
     * @return true if plugin is watching for this check
     */
    public static boolean isActive(CheckType type)
    {
        return chk.isActive(type);
    }

    /**
     * Allow a player to skip a certain check
     * 
     * @param player
     *            Player to stop watching
     * @param type
     *            Check to stop watching for
     */
    public static void exemptPlayer(Player player, CheckType type)
    {
        chk.exemptPlayer(player, type);
    }

    /**
     * Stop allowing a player to skip a certain check
     * 
     * @param player
     *            Player to start watching
     * @param type
     *            Check to start watching for
     */
    public static void unexemptPlayer(Player player, CheckType type)
    {
        chk.unexemptPlayer(player, type);
    }

    /**
     * Find out if a player is currently exempt from a certain check
     * 
     * @param player
     *            Player to check
     * @param type
     *            Type to check
     * @return true if plugin is ignoring this check on this player
     */
    public static boolean isExempt(Player player, CheckType type)
    {
        return chk.isExempt(player, type);
    }

    /**
     * Find out if a check will occur for a player. This checks if they are
     * being tracked, the check is active, the player isn't exempt from the
     * check, and the player doesn't have override permission.
     * 
     * @param player
     *            Player to check
     * @param type
     *            Type to check
     * @return true if plugin will check this player, and that all things allow
     *         it to happen.
     */
    public boolean willCheck(Player player, CheckType type)
    {
        return chk.willCheck(player, type);
    }

    //PlayerManager API

    /**
     * Get a player's integer hack level (0-19 = Low, 20-49 = Medium, 50-60 =
     * High)
     * 
     * @param player
     *            Player to check
     * @return player's hack level
     */
    public static int getLevel(Player player)
    {
        if (!pmr.hasLevel(player))
        {
            return 0;
        }
        return pmr.getLevel(player);
    }

    /**
     * Set a player's hack level (Min = 0, Max = 60)
     * 
     * @param player
     *            Player to set
     * @param level
     *            Level to set to
     */
    public static void setLevel(Player player, int level)
    {
        pmr.setLevel(player, level);
    }

    //XrayTracker API

    /**
     * Find out if a player is detected as using xray hacks (on any ore)
     * 
     * @param player
     *            Player to check
     * @return true if the player has any xray anomalies.
     */
    public static boolean isXrayer(Player player)
    {
        String name = player.getName();
        if (xtracker.sufficientData(name) && xtracker.hasAbnormal(name))
        {
            return true;
        }
        return false;
    }

    //Advanced Users Only API.

    /**
     * Get access to all the other managers, advanced users ONLY
     * 
     * @return the AntiCheat Manager
     */
    public static AnticheatManager getManager()
    {
        return Anticheat.getManager();
    }
}
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

package net.h31ix.anticheat.util;

import org.bukkit.configuration.file.FileConfiguration;

public class Language
{
    private static String[] medAlert = null;
    private static String[] highAlert = null;
    private static String[] warning = null;
    private static String banReason = null;
    private static String banBroadcast = null;
    private static String kickReason = null;
    private static String kickBroadcast = null;

    public Language(FileConfiguration file)
    {
        medAlert = file.getList("alert.medium_alert").toArray(new String[file.getList("alert.medium_alert").size()]);
        highAlert = file.getList("alert.high_alert").toArray(new String[file.getList("alert.high_alert").size()]);
        warning = file.getList("warning.player_warning").toArray(new String[file.getList("warning.player_warning").size()]);
        banReason = file.getString("ban.ban_reason");
        banBroadcast = file.getString("ban.ban_broadcast");
        kickReason = file.getString("kick.kick_reason");
        kickBroadcast = file.getString("kick.kick_broadcast");
    }

    public String[] getMediumAlert()
    {
        return medAlert;
    }

    public String[] getHighAlert()
    {
        return highAlert;
    }

    public String[] getWarning()
    {
        return warning;
    }

    public String getBanReason()
    {
        return banReason;
    }

    public String getBanBroadcast()
    {
        return banBroadcast;
    }

    public String getKickReason()
    {
        return kickReason;
    }

    public String getKickBroadcast()
    {
        return kickBroadcast;
    }
}

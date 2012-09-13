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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
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
    
    private static final ChatColor GOLD = ChatColor.GOLD;
    private static final ChatColor GRAY = ChatColor.GRAY;

    public Language(FileConfiguration file)
    {
        String [] temp = file.getList("alert.medium_alert").toArray(new String[file.getList("alert.medium_alert").size()]);
        medAlert = new String [temp.length+2];
        medAlert[0] = GOLD + "----------------------[" + GRAY + "AntiCheat" + GOLD + "]----------------------";
        for(int i=0;i<temp.length;i++)
        {
            medAlert[i+1] = GRAY + temp[i];
        }
        medAlert[medAlert.length-1] = GOLD + "-----------------------------------------------------";
        temp = file.getList("alert.high_alert").toArray(new String[file.getList("alert.high_alert").size()]);
        highAlert = new String [temp.length+2];
        highAlert[0] = GOLD + "----------------------[" + GRAY + "AntiCheat" + GOLD + "]----------------------";
        for(int i=0;i<temp.length;i++)
        {
            highAlert[i+1] = GRAY + temp[i];
        }
        highAlert[highAlert.length-1] = GOLD + "-----------------------------------------------------";
        
        warning = file.getList("warning.player_warning").toArray(new String[file.getList("warning.player_warning").size()]);
        banReason = file.getString("ban.ban_reason");
        banBroadcast = file.getString("ban.ban_broadcast");
        kickReason = file.getString("kick.kick_reason");
        kickBroadcast = file.getString("kick.kick_broadcast");
    }

    public List<String> getMediumAlert()
    {
        return new ArrayList<String>(Arrays.asList(medAlert));
    }

    public List<String> getHighAlert()
    {
        return new ArrayList<String>(Arrays.asList(highAlert));
    }

    public List<String> getWarning()
    {
        return new ArrayList<String>(Arrays.asList(warning));
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

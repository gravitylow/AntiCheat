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

package net.h31ix.anticheat.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Language {
    private static String[] medAlert = null;
    private static String[] highAlert = null;
    private static String[] warning = null;
    private static String banReason = null;
    private static String banBroadcast = null;
    private static String kickReason = null;
    private static String kickBroadcast = null;
    private static String chatWarning = null;
    private static String chatKickBroadcast = null;
    private static String chatKickReason = null;
    private static String chatBanBroadcast = null;
    private static String chatBanReason = null;
    
    private static final ChatColor GOLD = ChatColor.GOLD;
    private static final ChatColor GRAY = ChatColor.GRAY;
    private FileConfiguration file;
    
    public Language(FileConfiguration file, File f) {
        this.file = file;
        String[] temp = file.getList("alert.medium_alert").toArray(new String[file.getList("alert.medium_alert").size()]);
        medAlert = new String[temp.length + 2];
        medAlert[0] = GOLD + "----------------------[" + GRAY + "AntiCheat" + GOLD + "]----------------------";
        for (int i = 0; i < temp.length; i++) {
            medAlert[i + 1] = GRAY + temp[i];
        }
        medAlert[medAlert.length - 1] = GOLD + "-----------------------------------------------------";
        temp = file.getList("alert.high_alert").toArray(new String[file.getList("alert.high_alert").size()]);
        highAlert = new String[temp.length + 2];
        highAlert[0] = GOLD + "----------------------[" + GRAY + "AntiCheat" + GOLD + "]----------------------";
        for (int i = 0; i < temp.length; i++) {
            highAlert[i + 1] = GRAY + temp[i];
        }
        highAlert[highAlert.length - 1] = GOLD + "-----------------------------------------------------";
        
        warning = file.getList("warning.player_warning").toArray(new String[file.getList("warning.player_warning").size()]);
        banReason = getString("ban.ban_reason", "Banned by AntiCheat");
        banBroadcast = getString("ban.ban_broadcast", "[AntiCheat] &player was banned for hacking.");
        kickReason = getString("kick.kick_reason", "Kicked by AntiCheat");
        kickBroadcast = getString("kick.kick_broadcast", "[AntiCheat] &player was kicked for hacking.");
        chatWarning = getString("chat.warning", "Stop spamming the server or you will be kicked!");
        chatKickReason = getString("chat.kick_reason", "Kicked for spamming");
        chatBanReason = getString("chat.ban_reason", "Banned for spamming");
        chatKickBroadcast = getString("chat.kick_broadcast", "[AntiCheat] &player was kicked for spamming");
        chatBanBroadcast = getString("chat.ban_broadcast", "[AntiCheat] &player was banned for spamming");
        try {
            file.save(f);
        } catch (IOException ex) {
            Logger.getLogger(Language.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<String> getMediumAlert() {
        return new ArrayList<String>(Arrays.asList(medAlert));
    }
    
    public List<String> getHighAlert() {
        return new ArrayList<String>(Arrays.asList(highAlert));
    }
    
    public List<String> getWarning() {
        return new ArrayList<String>(Arrays.asList(warning));
    }
    
    public String getBanReason() {
        return banReason;
    }
    
    public String getBanBroadcast() {
        return banBroadcast;
    }
    
    public String getKickReason() {
        return kickReason;
    }
    
    public String getKickBroadcast() {
        return kickBroadcast;
    }
    
    public String getChatKickReason() {
        return chatKickReason;
    }
    
    public String getChatKickBroadcast() {
        return chatKickBroadcast;
    }
    
    public String getChatBanReason() {
        return chatBanReason;
    }
    
    public String getChatBanBroadcast() {
        return chatBanBroadcast;
    }
    
    public String getChatWarning() {
        return chatWarning;
    }
    
    private String getString(String entry, String d) {
        if (file.getString(entry) == null) {
            file.set(entry, d);
            return d;
        } else {
            return file.getString(entry);
        }
    }
}

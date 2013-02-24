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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Language {
    private static List<String> alert = null;
    private static List<String> warning = null;
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
    private File f;
    
    public Language(FileConfiguration file, File f) {
        this.file = file;
        this.f = f;

        // Begin medium_alert / high_alert -> alert swap
        // We're doing it this way to preserve *most* changes the user might have made to the alert
        List<String> list = file.getStringList("alert.medium_alert");
        if(list != null && list.size() > 0) {
            list.set(0, "&player has just entered the &level hack level.");
            file.set("alert.medium_alert", null);
            file.set("alert.high_alert", null);
            file.set("alert", list);
            save();
        }
        // End swap

        List<String> defAlert = new ArrayList<String>();
        defAlert.add("&player has just entered the &level hack level.");
        defAlert.add("&player's last failed check was: &check.");
        defAlert.add("Type '/anticheat report &player' for more information.");

        List<String> temp = getStringList("alert", defAlert);
        alert = new ArrayList<String>();
        alert.add(GOLD + "----------------------[" + GRAY + "AntiCheat" + GOLD + "]----------------------");
        for (int i = 0; i < temp.size(); i++) {
            alert.add(GRAY + temp.get(i));
        }
        alert.add(GOLD + "-----------------------------------------------------");

        List<String> defWarning = new ArrayList<String>();
        defWarning.add("[AntiCheat] Hacking is not permitted.");
        defWarning.add("[AntiCheat] If you continue to hack, action will be taken.");

        warning = getStringList("warning.player_warning", defWarning);
        banReason = getString("ban.ban_reason", "Banned by AntiCheat");
        banBroadcast = getString("ban.ban_broadcast", "[AntiCheat] &player was banned for hacking.");
        kickReason = getString("kick.kick_reason", "Kicked by AntiCheat");
        kickBroadcast = getString("kick.kick_broadcast", "[AntiCheat] &player was kicked for hacking.");
        chatWarning = getString("chat.warning", "Stop spamming the server or you will be kicked!");
        chatKickReason = getString("chat.kick_reason", "Kicked for spamming");
        chatBanReason = getString("chat.ban_reason", "Banned for spamming");
        chatKickBroadcast = getString("chat.kick_broadcast", "[AntiCheat] &player was kicked for spamming");
        chatBanBroadcast = getString("chat.ban_broadcast", "[AntiCheat] &player was banned for spamming");

        save();
    }

    private void save() {
        try {
            file.save(f);
        } catch (IOException ex) {
            Logger.getLogger(Language.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<String> getAlert() {
        return alert;
    }
    
    public List<String> getWarning() {
        return warning;
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

    private List<String> getStringList(String entry, List<String> d) {
        if (file.getString(entry) == null) {
            file.set(entry, d);
            return d;
        } else {
            return file.getStringList(entry);
        }
    }
}

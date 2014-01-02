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

package net.gravitydevelopment.anticheat.config.files;

import net.gravitydevelopment.anticheat.AntiCheat;
import net.gravitydevelopment.anticheat.config.Configuration;
import net.gravitydevelopment.anticheat.config.ConfigurationFile;

import java.util.List;

public class Lang extends ConfigurationFile {

    public static final String FILENAME = "lang.yml";

    public ConfigValue<List<String>> alert;
    public ConfigValue<List<String>> playerWarning;
    public ConfigValue<String> spamWarning;
    public ConfigValue<String> spamKickReason;
    public ConfigValue<String> spamBanReason;
    public ConfigValue<String> spamKickBroadcast;
    public ConfigValue<String> spamBanBroadcast;
    public ConfigValue<String> banReason;
    public ConfigValue<String> banBroadcast;
    public ConfigValue<String> kickReason;
    public ConfigValue<String> kickBroadcast;

    public Lang(AntiCheat plugin, Configuration config) {
        super(plugin, config, FILENAME);
    }

    @Override
    public void open() {
        alert = new ConfigValue<List<String>>("alert");
        playerWarning = new ConfigValue<List<String>>("warning.player_warning");
        spamWarning = new ConfigValue<String>("chat.warning");
        spamKickReason = new ConfigValue<String>("chat.kick_reason");
        spamBanReason = new ConfigValue<String>("chat.ban_reason");
        spamKickBroadcast = new ConfigValue<String>("chat.kick_broadcast");
        spamBanBroadcast = new ConfigValue<String>("chat.ban_broadcast");
        banReason = new ConfigValue<String>("ban.ban_reason");
        banBroadcast = new ConfigValue<String>("ban.ban_broadcast");
        kickReason = new ConfigValue<String>("kick.kick_reason");
        kickBroadcast = new ConfigValue<String>("kick.kick_broadcast");
    }
}

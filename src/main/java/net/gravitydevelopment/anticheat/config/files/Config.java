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

package net.gravitydevelopment.anticheat.config.files;

import net.gravitydevelopment.anticheat.AntiCheat;
import net.gravitydevelopment.anticheat.config.Configuration;
import net.gravitydevelopment.anticheat.config.ConfigurationFile;

import java.util.List;

public class Config extends ConfigurationFile {

    public static final String FILENAME = "config.yml";

    public ConfigValue<Boolean> logToConsole;
    public ConfigValue<Boolean> logXRayStats;
    public ConfigValue<Boolean> autoUpdate;
    public ConfigValue<Boolean> verboseStartup;
    public ConfigValue<Boolean> alertWhenXRayIsFound;
    public ConfigValue<Boolean> silentMode;
    public ConfigValue<Boolean> exemptOp;
    public ConfigValue<Boolean> trackCreativeXRay;
    public ConfigValue<Boolean> eventChains;
    public ConfigValue<Boolean> enterprise;
    public ConfigValue<Boolean> blockChatSpam;
    public ConfigValue<Boolean> blockCommandSpam;

    public ConfigValue<Integer> fileLogLevel;

    public ConfigValue<String> spamKickAction;
    public ConfigValue<String> spamBanAction;

    public ConfigValue<List<String>> exemptedWorlds;

    public Config(AntiCheat plugin, Configuration config) {
        super(plugin, config, FILENAME);
    }

    @Override
    public void open() {
        logToConsole = new ConfigValue<Boolean>("system.log-to-console");
        logXRayStats = new ConfigValue<Boolean>("xray.check-xray");
        alertWhenXRayIsFound = new ConfigValue<Boolean>("xray.alert");
        autoUpdate = new ConfigValue<Boolean>("system.auto-update");
        verboseStartup = new ConfigValue<Boolean>("system.verbose-startup");
        silentMode = new ConfigValue<Boolean>("system.silent-mode");
        exemptOp = new ConfigValue<Boolean>("system.exempt-op");
        trackCreativeXRay = new ConfigValue<Boolean>("xray.check-creative");
        eventChains = new ConfigValue<Boolean>("system.event-chains");
        enterprise = new ConfigValue<Boolean>("system.enterprise");
        blockChatSpam = new ConfigValue<Boolean>("spam.chat");
        blockCommandSpam = new ConfigValue<Boolean>("spam.command");

        fileLogLevel = new ConfigValue<Integer>("system.file-log-level");

        spamKickAction = new ConfigValue<String>("spam.action-one");
        spamBanAction = new ConfigValue<String>("spam.action-two");

        exemptedWorlds = new ConfigValue<List<String>>("disable-in");
    }
}

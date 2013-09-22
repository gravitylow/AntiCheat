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

package net.h31ix.anticheat.config.files;

import net.h31ix.anticheat.AntiCheat;
import net.h31ix.anticheat.config.Configuration;
import net.h31ix.anticheat.config.ConfigurationFile;
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
        logToConsole = new ConfigValue<Boolean>("System.Log to console");
        logXRayStats = new ConfigValue<Boolean>("XRay.Log xray stats");
        autoUpdate = new ConfigValue<Boolean>("System.Auto update");
        verboseStartup = new ConfigValue<Boolean>("System.Verbose startup");
        alertWhenXRayIsFound = new ConfigValue<Boolean>("XRay.Alert when xray is found");
        silentMode = new ConfigValue<Boolean>("System.Silent mode");
        exemptOp = new ConfigValue<Boolean>("System.Exempt op");
        trackCreativeXRay = new ConfigValue<Boolean>("XRay.Track creative");
        eventChains = new ConfigValue<Boolean>("System.Event Chains");
        enterprise = new ConfigValue<Boolean>("System.Enterprise");
        blockChatSpam = new ConfigValue<Boolean>("Chat.Block chat spam");
        blockCommandSpam = new ConfigValue<Boolean>("Chat.Block command spam");

        fileLogLevel = new ConfigValue<Integer>("System.File log level");

        spamKickAction = new ConfigValue<String>("Chat.Kick Action");
        spamBanAction = new ConfigValue<String>("Chat.Ban Action");

        exemptedWorlds = new ConfigValue<List<String>>("Disable in");
    }
}


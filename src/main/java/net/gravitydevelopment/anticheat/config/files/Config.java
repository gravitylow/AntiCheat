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

public class Config extends ConfigurationFile {

    public static final String FILENAME = "config.yml";

    public ConfigValue<Boolean> logToConsole;
    public ConfigValue<Boolean> autoUpdate;
    public ConfigValue<Boolean> verboseStartup;
    public ConfigValue<Boolean> silentMode;
    public ConfigValue<Boolean> exemptOp;

    public ConfigValue<Boolean> checkXRay;
    public ConfigValue<Boolean> checkCreativeXRay;
    public ConfigValue<Boolean> alertXRay;
    public ConfigValue<Integer> alertXRayInterval;

    public ConfigValue<Boolean> eventChains;
    public ConfigValue<Boolean> enterprise;

    public ConfigValue<Boolean> blockChatSpamSpeed;
    public ConfigValue<Boolean> blockCommandSpamSpeed;
    public ConfigValue<Boolean> blockChatSpamRepetition;
    public ConfigValue<Boolean> blockCommandSpamRepetition;
    public ConfigValue<String> chatSpamActionOne;
    public ConfigValue<String> chatSpamActionTwo;
    public ConfigValue<String> commandSpamActionOne;
    public ConfigValue<String> commandSpamActionTwo;

    public ConfigValue<Integer> fileLogLevel;

    public ConfigValue<List<String>> disabledWorlds;

    public Config(AntiCheat plugin, Configuration config) {
        super(plugin, config, FILENAME);
    }

    @Override
    public void open() {
        logToConsole = new ConfigValue<Boolean>("system.log-to-console");

        checkXRay = new ConfigValue<Boolean>("xray.check-xray");
        alertXRay = new ConfigValue<Boolean>("xray.alert");
        alertXRayInterval = new ConfigValue<Integer>("xray.alert-interval");
        checkCreativeXRay = new ConfigValue<Boolean>("xray.check-creative");

        autoUpdate = new ConfigValue<Boolean>("system.auto-update");
        verboseStartup = new ConfigValue<Boolean>("system.verbose-startup");
        silentMode = new ConfigValue<Boolean>("system.silent-mode");
        exemptOp = new ConfigValue<Boolean>("system.exempt-op");
        eventChains = new ConfigValue<Boolean>("system.event-chains");
        enterprise = new ConfigValue<Boolean>("system.enterprise");

        blockChatSpamSpeed = new ConfigValue<Boolean>("spam.chat.block-speed");
        blockChatSpamRepetition = new ConfigValue<Boolean>("spam.chat.block-repetition");
        chatSpamActionOne = new ConfigValue<String>("spam.chat.action-one");
        chatSpamActionTwo = new ConfigValue<String>("spam.chat.action-two");

        blockCommandSpamSpeed = new ConfigValue<Boolean>("spam.command.block-speed");
        blockCommandSpamRepetition = new ConfigValue<Boolean>("spam.command.block-repetition");
        commandSpamActionOne = new ConfigValue<String>("spam.command.action-one");
        commandSpamActionTwo = new ConfigValue<String>("spam.command.action-two");

        fileLogLevel = new ConfigValue<Integer>("system.file-log-level");

        disabledWorlds = new ConfigValue<List<String>>("disable-in");
    }
}

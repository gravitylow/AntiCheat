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
import net.h31ix.anticheat.manage.CheckType;
import net.h31ix.anticheat.manage.User;
import net.h31ix.anticheat.util.enterprise.Database;

public class Enterprise extends ConfigurationFile {

    public static final String FILENAME = "enterprise.yml";

    public ConfigValue<String> serverName;

    public ConfigValue<Boolean> loggingEnabled;
    public ConfigValue<Integer> loggingLife;
    public ConfigValue<Integer> loggingInterval;

    public ConfigValue<Boolean> usersEnabled;
    public ConfigValue<Integer> usersInterval;

    public Database database;

    public Enterprise(AntiCheat plugin, Configuration config) {
        super(plugin, config, FILENAME);
    }

    @Override
    public void open() {
        serverName = new ConfigValue<String>("server.name");

        loggingEnabled = new ConfigValue<Boolean>("logging.enable");
        loggingLife = new ConfigValue<Integer>("logging.life");
        loggingInterval = new ConfigValue<Integer>("logging.interval");

        usersEnabled = new ConfigValue<Boolean>("users.enable");
        usersInterval = new ConfigValue<Integer>("users.interval");

        ConfigValue<String> databaseType = new ConfigValue<String>("database.type");
        ConfigValue<String> databaseHostname = new ConfigValue<String>("database.hostname");
        ConfigValue<String> databaseUsername = new ConfigValue<String>("database.username");
        ConfigValue<String> databasePassword = new ConfigValue<String>("database.password");
        ConfigValue<String> databasePrefix = new ConfigValue<String>("database.prefix");
        ConfigValue<String> databaseSchema = new ConfigValue<String>("database.database");
        ConfigValue<Integer> databasePort = new ConfigValue<Integer>("database.port");

        // Convert database values to Database
        database = new Database(
            Database.DatabaseType.valueOf(databaseType.getValue()),
            databaseHostname.getValue(),
            databasePort.getValue(),
            databaseUsername.getValue(),
            databasePassword.getValue(),
            databasePrefix.getValue(),
            databaseSchema.getValue(),
            serverName.getValue(),
            loggingInterval.getValue(),
            usersInterval.getValue(),
            loggingLife.getValue()
        );
    }
}

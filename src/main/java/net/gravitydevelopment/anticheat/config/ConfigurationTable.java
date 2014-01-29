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

package net.gravitydevelopment.anticheat.config;

import net.gravitydevelopment.anticheat.AntiCheat;
import net.gravitydevelopment.anticheat.util.enterprise.Database;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigurationTable {

    private final String table;
    private final Configuration config;

    private Database database;
    private String prefix;
    private String serverName;

    public ConfigurationTable(Configuration config, String table) {
        this.config = config;
        this.table = table;

        load();
    }

    public void load() {
        this.database = config.getEnterprise().database;
        this.prefix = database.getPrefix();
        this.serverName = config.getEnterprise().serverName.getValue();

        open();
    }

    public void open() {
        // Nothing to do
    }

    public void reload() {
        // For after sql inserts have been made
        Bukkit.getScheduler().runTask(AntiCheat.getPlugin(), new Runnable() {
            @Override
            public void run() {
                load();
            }
        });
    }

    public Database getDatabase() {
        return database;
    }

    public String getFullTable() {
        return prefix + table;
    }

    public boolean tableExists() {
        try {
            return getConnection().getMetaData().getTables(null, null, getFullTable(), null).next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getServerName() {
        return serverName;
    }

    public Connection getConnection() {
        return database.getConnection();
    }
}

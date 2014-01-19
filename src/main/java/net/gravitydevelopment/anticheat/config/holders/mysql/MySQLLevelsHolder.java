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

package net.gravitydevelopment.anticheat.config.holders.mysql;

import net.gravitydevelopment.anticheat.AntiCheat;
import net.gravitydevelopment.anticheat.config.Configuration;
import net.gravitydevelopment.anticheat.config.ConfigurationTable;
import net.gravitydevelopment.anticheat.config.providers.Levels;
import net.gravitydevelopment.anticheat.util.User;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySQLLevelsHolder extends ConfigurationTable implements Levels {

    private static final String TABLE = "levels";
    private static final String MANUAL_EDIT_TAG = "MANUAL";

    private String sqlSave;
    private String sqlLoad;
    private String sqlUpdate;

    public MySQLLevelsHolder(Configuration config) {
        super(config, TABLE);
    }

    @Override
    public void open() {
        sqlSave = "INSERT INTO " + getFullTable() +
                " (user, level, last_update_server) " +
                "VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "level = IF(last_update = ?, ?, level+?), " +
                "last_update=CURRENT_TIMESTAMP, " +
                "last_update_server=?";

        sqlLoad = "SELECT level, last_update FROM " + getFullTable() + " " +
                "WHERE user = ?";

        sqlUpdate = "SELECT level, last_update FROM " + getFullTable() + " " +
                "WHERE user = ? AND last_update <> ? AND last_update_server = ?";

        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + getFullTable() + "(" +
                "  `user` VARCHAR(45) NOT NULL," +
                "  `level` INT NOT NULL," +
                "  `last_update` TIMESTAMP NOT NULL DEFAULT NOW()," +
                "  `last_update_server` VARCHAR(45) NOT NULL," +
                "  PRIMARY KEY (`user`));";

        try {
            getConnection().prepareStatement(sqlCreate).executeUpdate();
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveLevelFromUser(final User user) {
        Bukkit.getScheduler().runTaskAsynchronously(AntiCheat.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (!user.isWaitingOnLevelSync()) {
                    AntiCheat.debugLog("Saving level from " + user.getName() + ". Value: " + user.getLevel());
                    try {
                        PreparedStatement statement = getConnection().prepareStatement(sqlSave);
                        statement.setString(1, user.getName());
                        statement.setInt(2, user.getLevel());
                        statement.setString(3, getServerName());
                        statement.setTimestamp(4, user.getLevelSyncTimestamp());
                        statement.setInt(5, user.getLevel());
                        statement.setInt(6, user.getLevel());
                        statement.setString(7, getServerName());

                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                AntiCheat.getManager().getUserManager().remove(user);
            }
        });
    }

    @Override
    public void loadLevelToUser(final User user) {
        Bukkit.getScheduler().runTaskAsynchronously(AntiCheat.getPlugin(), new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = getConnection().prepareStatement(sqlLoad);
                    statement.setString(1, user.getName());

                    ResultSet set = statement.executeQuery();

                    boolean has = false;
                    while (set.next()) {
                        has = true;
                        AntiCheat.debugLog("Syncing level to " + user.getName() + ". Value: " + set.getInt("level"));
                        user.setLevel(set.getInt("level"));
                        user.setLevelSyncTimestamp(set.getTimestamp("last_update"));
                    }
                    if (!has) user.setLevel(0);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void saveLevelsFromUsers(List<User> users) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(sqlSave);
            for (User user : users) {
                if (!user.isWaitingOnLevelSync()) {
                    try {
                        statement.setString(1, user.getName());
                        statement.setInt(2, user.getLevel());
                        statement.setString(3, getServerName());
                        statement.setTimestamp(4, user.getLevelSyncTimestamp());
                        statement.setInt(5, user.getLevel());
                        statement.setInt(6, user.getLevel());
                        statement.setString(7, getServerName());

                        statement.addBatch();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            statement.executeBatch();
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateLevelToUser(final User user) {
        Bukkit.getScheduler().runTaskAsynchronously(AntiCheat.getPlugin(), new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = getConnection().prepareStatement(sqlUpdate);
                    statement.setString(1, user.getName());
                    statement.setTimestamp(2, user.getLevelSyncTimestamp());
                    statement.setString(3, MANUAL_EDIT_TAG);

                    ResultSet set = statement.executeQuery();

                    while (set.next()) {
                        AntiCheat.debugLog("Syncing level to " + user.getName() + ". Value: " + set.getInt("level"));
                        user.setLevel(set.getInt("level"));
                        user.setLevelSyncTimestamp(set.getTimestamp("last_update"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

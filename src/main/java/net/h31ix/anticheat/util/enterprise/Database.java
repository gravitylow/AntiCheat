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

package net.h31ix.anticheat.util.enterprise;

import net.h31ix.anticheat.AntiCheat;
import net.h31ix.anticheat.manage.CheckType;
import net.h31ix.anticheat.manage.User;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.sql.*;

public class Database {

    private static final String EVENTS_TABLE = "events";
    private static final String USERS_TABLE = "users";

    private String sqlLogEvent;
    private String sqlSyncTo;
    private String sqlSyncFrom;
    private String sqlCleanEvents;

    public enum DatabaseType {
        MySQL,
    }

    private DatabaseType type;
    private String hostname;
    private int port;
    private String username;
    private String password;
    private String prefix;
    private String schema;
    private String serverName;

    private int eventInterval;
    private int userInterval;

    private int eventLife;

    private Connection connection;

    private PreparedStatement eventBatch;
    private PreparedStatement syncBatch;

    private BukkitTask eventTask;
    private BukkitTask userTask;

    public Database(DatabaseType type, String hostname, int port, String username, String password, String prefix, String schema, String serverName, int eventInterval, int userInterval, int eventLife) {
        this.type = type;
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.prefix = prefix;
        this.schema = schema;
        this.serverName = serverName;

        this.eventInterval = eventInterval;
        this.userInterval = userInterval;

        this.eventLife = eventLife;

        sqlLogEvent = "INSERT INTO " + prefix + EVENTS_TABLE + " (server, user, check_type) VALUES (?, ?, ?)";
        sqlSyncTo = "INSERT INTO " + prefix + USERS_TABLE + " (user, level) VALUES (?, ?) ON DUPLICATE KEY UPDATE level = ?, last_update=CURRENT_TIMESTAMP, last_update_server=?";
        sqlSyncFrom = "SELECT level FROM " + prefix + USERS_TABLE + " WHERE user = ?";
        sqlCleanEvents = "DELETE FROM " + prefix + EVENTS_TABLE + " WHERE time < (CURRENT_TIMESTAMP - INTERVAL ? DAY)";
    }

    public DatabaseType getType() {
        return type;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSchema() {
        return schema;
    }

    public void connect() {
        String url = "jdbc:" + type.toString().toLowerCase() + "://" + hostname + ":" + port + "/" + schema;

        try {
            connection = DriverManager.getConnection(url, username, password);
            eventBatch = connection.prepareStatement(sqlLogEvent);
            syncBatch = connection.prepareStatement(sqlSyncTo);

            connection.setAutoCommit(false);

            eventTask = Bukkit.getScheduler().runTaskTimerAsynchronously(AntiCheat.getPlugin(), new Runnable(){
                @Override
                public void run() {
                    flushEvents();
                }
            }, eventInterval* 60 * 20, eventInterval * 60 * 20);

            userTask = Bukkit.getScheduler().runTaskTimerAsynchronously(AntiCheat.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    flushSyncs();
                }
            }, userInterval * 60 * 20, userInterval * 60 * 20);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //TODO: setup tables
    }

    public void shutdown() {
        eventTask.cancel();
        userTask.cancel();

        flushEvents();
        flushSyncs();

        try {
            eventBatch.close();
            syncBatch.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void logEvent(User user, CheckType checkType) {
        try {
            eventBatch.setString(1, serverName);
            eventBatch.setString(2, user.getName());
            eventBatch.setString(3, checkType.toString());

            eventBatch.addBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void syncTo(User user) {
        if (!user.isWaitingOnLevelSync()) {
            System.out.println("Syncing to "+user.getName()+" value "+user.getLevel());
            try {
                syncBatch.setString(1, user.getName());
                syncBatch.setInt(2, user.getLevel());
                syncBatch.setInt(3, user.getLevel());
                syncBatch.setString(4, serverName);

                syncBatch.addBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void syncFrom(final User user) {
        Bukkit.getScheduler().runTaskAsynchronously(AntiCheat.getPlugin(), new Runnable(){
            @Override
            public void run() {
                try {
                    PreparedStatement statement = connection.prepareStatement(sqlSyncFrom);
                    statement.setString(1, user.getName());

                    ResultSet set = statement.executeQuery();
                    while (set.next()) {
                        System.out.println("Added level for " + user.getName()+":" +set.getInt("level"));
                        user.setLevel(set.getInt("level"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void flushEvents() {
        try {
            eventBatch.executeBatch();
            connection.commit();

            eventBatch = connection.prepareStatement(sqlLogEvent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void flushSyncs() {
        System.out.println("Flushing syncs");
        try {
            syncBatch.executeBatch();
            connection.commit();

            syncBatch = connection.prepareStatement(sqlSyncTo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cleanEvents() {
        Bukkit.getScheduler().runTaskAsynchronously(AntiCheat.getPlugin(), new Runnable(){
            @Override
            public void run() {
                try {
                    PreparedStatement statement = connection.prepareStatement(sqlCleanEvents);
                    statement.setInt(1, eventLife);

                    statement.executeUpdate();

                    connection.commit();
                    AntiCheat.getPlugin().verboseLog("Cleaned " + statement.getUpdateCount() + " old events from the database");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

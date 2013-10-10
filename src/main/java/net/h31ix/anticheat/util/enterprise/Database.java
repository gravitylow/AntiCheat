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

    private static final String SQL_LOG_EVENT = "INSERT INTO ? (server_name, user, check_type) VALUES (?, ?, ?)";
    private static final String SQL_SYNC_FROM = "SELECT level FROM ? WHERE user = ?";
    private static final String SQL_SYNC_TO = "INSERT INTO ? (user, level) VALUES (?, ?) ON DUPLICATE KEY UPDATE level = ?";

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

    private int eventInterval;
    private int userInterval;

    private Connection connection;

    private PreparedStatement eventBatch;
    private PreparedStatement syncBatch;

    private BukkitTask eventTask;
    private BukkitTask userTask;

    public Database(DatabaseType type, String hostname, int port, String username, String password, String prefix, String schema, int eventInterval, int userInterval) {
        this.type = type;
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.prefix = prefix;
        this.schema = schema;

        this.eventInterval = eventInterval;
        this.userInterval = userInterval;
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
        String url = type.toString().toLowerCase() + "://" + hostname + ":" + port + "/" + schema;

        try {
            connection = DriverManager.getConnection(url, username, password);
            eventBatch = connection.prepareStatement(SQL_LOG_EVENT);
            syncBatch = connection.prepareStatement(SQL_SYNC_TO);

            connection.setAutoCommit(false);

            eventTask = Bukkit.getScheduler().runTaskTimerAsynchronously(AntiCheat.getPlugin(), new Runnable(){
                @Override
                public void run() {
                    flushEvents();
                }
            }, eventInterval*60*20, eventInterval*60*20);

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

    public void logEvent(String serverName, User user, CheckType checkType) {
        try {
            eventBatch.setString(1, EVENTS_TABLE);
            eventBatch.setString(2, serverName);
            eventBatch.setString(3, user.getName());
            eventBatch.setString(4, checkType.toString());

            eventBatch.addBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void syncTo(User user) {
        try {
            syncBatch.setString(1, USERS_TABLE);
            syncBatch.setString(2, user.getName());
            syncBatch.setInt(3, user.getLevel());
            syncBatch.setInt(4, user.getLevel());

            syncBatch.addBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void syncFrom(final User user) {
        Bukkit.getScheduler().runTaskAsynchronously(AntiCheat.getPlugin(), new Runnable(){
            @Override
            public void run() {
                try {
                    PreparedStatement statement = connection.prepareStatement(SQL_SYNC_FROM);
                    statement.setString(1, USERS_TABLE);
                    statement.setString(2, user.getName());

                    ResultSet set = statement.executeQuery();
                    while (set.next()) {
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

            eventBatch = connection.prepareStatement(SQL_LOG_EVENT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void flushSyncs() {
        try {
            syncBatch.executeBatch();
            connection.commit();

            syncBatch = connection.prepareStatement(SQL_SYNC_TO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

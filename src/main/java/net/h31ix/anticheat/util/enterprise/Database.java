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

public class Database {

    public enum DatabaseType {
        MYSQL,
    }

    private DatabaseType type;
    private String hostname;
    private int port;
    private String username;
    private String password;
    private String schema;
    private String prefix;

    public Database(DatabaseType type, String hostname, int port, String username, String password, String schema, String prefix) {
        this.type = type;
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.schema = schema;
        this.prefix = prefix;
    }
}

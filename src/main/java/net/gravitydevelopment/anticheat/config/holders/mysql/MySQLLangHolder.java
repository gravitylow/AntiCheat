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
import net.gravitydevelopment.anticheat.config.providers.Lang;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MySQLLangHolder extends ConfigurationTable implements InvocationHandler {

    private static final String TABLE = "lang";

    private HashMap<String, String> stringValues;
    private HashMap<String, List<String>> listValues;

    private FileConfiguration defaults;

    public MySQLLangHolder(Configuration config) {
        super(config, TABLE);
    }

    @Override
    public void open() {
        defaults = YamlConfiguration.loadConfiguration(AntiCheat.getPlugin().getResource("lang.yml"));

        stringValues = new HashMap<String, String>();
        listValues = new HashMap<String, List<String>>();

        String sqlCreate = "CREATE TABLE " + getFullTable() + "(" +
                "  `id` INT NOT NULL AUTO_INCREMENT," +
                "  `key` VARCHAR(45) NOT NULL," +
                "  `value` TEXT," +
                "  PRIMARY KEY (`id`));";

        String sqlPopulate = "INSERT INTO " + getFullTable() +
                " (`key`, `value`) " +
                "SELECT t.*" +
                "FROM (";

        Method[] methods = Lang.class.getMethods();
        for (int i=1;i<=methods.length;i++) {
            sqlPopulate += "(SELECT ? as `key`, ? as `value`)";
            if (i < methods.length) sqlPopulate += " UNION ALL ";
        }
        sqlPopulate += ") t;";

        String sqlLoad = "SELECT * FROM " + getFullTable();

        try {
            PreparedStatement insert = getConnection().prepareStatement(sqlPopulate);
            int strings = 1;
            for (int i=1;i<=methods.length;i++) {
                String key = methods[i-1].getName();
                String value = defaults.getString(key);
                insert.setString(strings, key);
                strings++;
                insert.setString(strings, value);
                strings++;
            }

            if (!tableExists()) {
                getConnection().prepareStatement(sqlCreate).executeUpdate();
                insert.executeUpdate();
                getConnection().commit();
            }

            ResultSet set = getConnection().prepareStatement(sqlLoad).executeQuery();
            while (set.next()) {
                String key = set.getString("key");
                String value = set.getString("value");
                if (value.matches("^\\[.*\\]$")) {
                    listValues.put(key, getList(value));
                } else {
                    stringValues.put(key, value);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        String key = method.getName();

        if (method.getReturnType() == List.class) {
            if (listValues.containsKey(key)) {
                return listValues.get(key);
            } else if (defaults.getString(key) != null) {
                String value = defaults.getString(key);
                try {
                    PreparedStatement s = getConnection().prepareStatement("INSERT INTO " + getFullTable() + " (key, value) VALUES (?, ?)");
                    s.setString(1, key);
                    s.setString(2, value);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                reload();
                return getList(value);
            }
        } else {
            if (stringValues.containsKey(key)) {
                return stringValues.get(key);
            } else if (defaults.getString(key) != null) {
                String value = defaults.getString(key);
                try {
                    PreparedStatement s = getConnection().prepareStatement("INSERT INTO " + getFullTable() + " (key, value) VALUES (?, ?)");
                    s.setString(1, key);
                    s.setString(2, value);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                reload();
                return value;
            }
        }
        AntiCheat.getPlugin().getLogger().severe("The lang value " + key + " couldn't be found.");
        return "Language error. See console for details.";
    }

    private List<String> getList(String value) {
        value = value.substring(1, value.length() - 1);
        ArrayList<String> list = new ArrayList<String>();
        for (String s : value.split(", ")) {
            list.add(s);
        }
        return list;
    }
}

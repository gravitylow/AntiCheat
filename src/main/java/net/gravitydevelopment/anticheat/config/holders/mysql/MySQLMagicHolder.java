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
import net.gravitydevelopment.anticheat.config.providers.Magic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class MySQLMagicHolder extends ConfigurationTable implements InvocationHandler {

    private static final String TABLE = "magic";

    private HashMap<String, Integer> ints;
    private HashMap<String, Double> doubles;

    private FileConfiguration defaults;

    public MySQLMagicHolder(Configuration config) {
        super(config, TABLE);
    }

    @Override
    public void open() {
        defaults = YamlConfiguration.loadConfiguration(AntiCheat.getPlugin().getResource("magic.yml"));

        ints = new HashMap<String, Integer>();
        doubles = new HashMap<String, Double>();

        String sqlCreate = "CREATE TABLE " + getFullTable() + "(" +
                "  `id` INT NOT NULL AUTO_INCREMENT," +
                "  `key` VARCHAR(45) NOT NULL," +
                "  `value_int` INT," +
                "  `value_double` DOUBLE," +
                "  PRIMARY KEY (`id`));";

        String sqlPopulate = "INSERT INTO " + getFullTable() +
                " (`key`, `value_int`, `value_double`) " +
                "SELECT t.*" +
                "FROM (";

        Method[] methods = Magic.class.getMethods();
        for (int i=1;i<=methods.length;i++) {
            String key = methods[i-1].getName();
            Object value = defaults.get(key);
            String type1 = methods[i-1].getReturnType().getSimpleName();
            String type2 = type1.equals("int") ? "double" : "int";
            sqlPopulate += "(SELECT '" + key + "' as `key`, " + (type1.equals("int") ? value : "null") + " as value_" + type1 + ", " + (type1.equals("double") ? value : "null") + " as value_" + type2 + ")";
            if (i < methods.length) sqlPopulate += " UNION ALL ";
        }

        sqlPopulate += ") t;";

        String sqlLoad = "SELECT * FROM " + getFullTable();

        try {
            if (!tableExists()) {
                getConnection().prepareStatement(sqlCreate).executeUpdate();
                getConnection().prepareStatement(sqlPopulate).executeUpdate();
                getConnection().commit();
            }

            ResultSet set = getConnection().prepareStatement(sqlLoad).executeQuery();
            while (set.next()) {
                String key = set.getString("key");

                if (set.getObject("value_int") != null) {
                    ints.put(key, set.getInt("value_int"));
                } else if (set.getObject("value_double") != null) {
                    doubles.put(key, set.getDouble("value_double"));
                } else {
                    AntiCheat.getPlugin().getLogger().severe("The magic value " + key + " loaded from the database did not have a value configured. Using the default value.");
                    for (int i=1;i<=methods.length;i++) {
                        String name = methods[i-1].getName();
                        if (name.equalsIgnoreCase(key)) {
                            String type = methods[i-1].getReturnType().getSimpleName();
                            if (type.equals("int")) {
                                ints.put(key, defaults.getInt(key));
                            } else if (type.equals("double")) {
                                doubles.put(key, defaults.getDouble(key));
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        String key = method.getName();

        if (ints.containsKey(key)) {
            return ints.get(key).intValue();
        } else if (doubles.containsKey(key)) {
            return doubles.get(key).doubleValue();
        } else if (defaults.getString(key) != null ) {
            if (method.getReturnType().getSimpleName().equals("int")) {
                int value = defaults.getInt(key);
                try {
                    getConnection().prepareStatement("INSERT INTO " + getFullTable() + " (key, value_int) VALUES ('" + key + "' , '" + value + "')").executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                reload();
                return value;
            } else if (method.getReturnType().getSimpleName().equals("double")) {
                double value = defaults.getDouble(key);
                try {
                    getConnection().prepareStatement("INSERT INTO " + getFullTable() + " (key, value_double) VALUES ('" + key + "' , '" + value + "')").executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                reload();
                return value;
            }
        }
        AntiCheat.getPlugin().getLogger().severe("The magic value " + key + " couldn't be found.");
        return 0;
    }
}

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
import net.gravitydevelopment.anticheat.config.providers.Rules;
import net.gravitydevelopment.anticheat.util.rule.Rule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLRulesHolder extends ConfigurationTable implements Rules {

    private static final String TABLE = "rules";

    private List<Rule> rules;

    public MySQLRulesHolder(Configuration config) {
        super(config, TABLE);
    }

    @Override
    public void open() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + getFullTable() + "(" +
                "  `id` INT NOT NULL AUTO_INCREMENT," +
                "  `rule` VARCHAR(256) NOT NULL," +
                "  PRIMARY KEY (`id`));";

        String sqlLoad = "SELECT * FROM " + getFullTable();

        try {
            getConnection().prepareStatement(sqlCreate).executeUpdate();
            getConnection().commit();

            this.rules = new ArrayList<Rule>();
            ResultSet set = getConnection().prepareStatement(sqlLoad).executeQuery();
            while (set.next()) {
                String string = set.getString("rule");
                Rule rule = Rule.load(string);
                if (rule != null) {
                    this.rules.add(rule);
                } else {
                    AntiCheat.getPlugin().getLogger().warning("Couldn't load rule '" + string + "' from the database. Improper format used.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Rule> getRules() {
        return rules;
    }
}

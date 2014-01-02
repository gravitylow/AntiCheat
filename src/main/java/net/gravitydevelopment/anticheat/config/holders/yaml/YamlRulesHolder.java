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

package net.gravitydevelopment.anticheat.config.holders.yaml;

import net.gravitydevelopment.anticheat.AntiCheat;
import net.gravitydevelopment.anticheat.config.Configuration;
import net.gravitydevelopment.anticheat.config.ConfigurationFile;
import net.gravitydevelopment.anticheat.config.providers.Rules;
import net.gravitydevelopment.anticheat.util.rule.Rule;

import java.util.ArrayList;
import java.util.List;

public class YamlRulesHolder extends ConfigurationFile implements Rules {

    public static final String FILENAME = "rules.yml";

    private List<Rule> rules;

    public YamlRulesHolder(AntiCheat plugin, Configuration config) {
        super(plugin, config, FILENAME);
    }

    @Override
    public List<Rule> getRules() {
        return rules;
    }

    @Override
    public void open() {
        ConfigValue<List<String>> rules = new ConfigValue<List<String>>("rules");

        // Convert rules list to Rules
        this.rules = new ArrayList<Rule>();
        List<String> tempRules = rules.getValue();
        for (int i = 0; i < tempRules.size(); i++) {
            String string = tempRules.get(i);

            if (string.equals("Check_SPIDER < 0 ? Player.KICK : null")) {
                // Default rule, won't ever run so we shouldn't load it; only used as example
                continue;
            }

            Rule rule = Rule.load(string);
            if (rule != null) {
                this.rules.add(rule);
            } else {
                AntiCheat.getPlugin().getLogger().warning("Couldn't load rule '" + string + "' from config. Improper format used.");
            }
        }
    }
}

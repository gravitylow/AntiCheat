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
import net.h31ix.anticheat.util.Level;
import net.h31ix.anticheat.util.rule.Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Events extends ConfigurationFile {

    public static final String FILENAME = "events.yml";

    public List<Level> levels;
    public List<Rule> rules;

    public int highestLevel;

    public Events(AntiCheat plugin, Configuration config) {
        super(plugin, config, FILENAME);
    }

    @Override
    public void open() {
        ConfigValue<List<String>> levels = new ConfigValue<List<String>>("levels");
        ConfigValue<List<String>> actions = new ConfigValue<List<String>>("actions");
        ConfigValue<List<String>> rules = new ConfigValue<List<String>>("rules");

        // Convert levels list to Levels
        this.levels = new ArrayList<Level>();
        for(String string : levels.getValue()) {
            Level level = Level.load(string);
            if(level == null || level.getValue() < 0) {
                continue;
            }

            this.levels.add(level);
            highestLevel = level.getValue() > highestLevel ? level.getValue() : highestLevel;
        }

        // Convert actions list and add to corresponding Levels
        for(String string : actions.getValue()) {
            String name;
            String action;
            try {
                name = string.split(": ")[0];
                action = string.split(": ")[1];
            } catch (Exception ex) {
                AntiCheat.getPlugin().getLogger().warning("Couldn't load action '"+string+"' from config. Improper format used.");
                break;
            }
            for(Level level : this.levels) {
                if(level.getName().equalsIgnoreCase(name)) {
                    level.addAction(action);
                }
            }
        }

        // Convert rules list to Rules
        this.rules = new ArrayList<Rule>();
        List<String> tempRules = rules.getValue();
        for(int i=0;i<tempRules.size();i++) {
            String string = tempRules.get(i);

            if(string.equals("Check_SPIDER < 0 ? Player.KICK : null")) {
                // Default rule, won't ever run so we shouldn't load it; only used as example
                continue;
            }

            Rule rule = Rule.load(string);
            if(rule != null) {
                this.rules.add(rule);
            } else {
                AntiCheat.getPlugin().getLogger().warning("Couldn't load rule '"+string+"' from config. Improper format used.");
            }
        }

        // Sort levels
        Collections.sort(this.levels, new Comparator<Level>() {
            public int compare(Level l1, Level l2) {
                if (l1.getValue() == l2.getValue()) {
                    return 0;
                } else if (l1.getValue() < l2.getValue()) {
                    return -1;
                }
                return 1;
            }
        });
    }

    public Level getUserLevel(int value) {
        for(int i=0;i<levels.size();i++) {
            if (i == 0 && value < levels.get(i).getValue()) break;
            else if (i == levels.size()-1) return levels.get(i);
            else if (value >= levels.get(i).getValue() && value < levels.get(i+1).getValue()) return levels.get(i);
        }
        return null;
    }
}

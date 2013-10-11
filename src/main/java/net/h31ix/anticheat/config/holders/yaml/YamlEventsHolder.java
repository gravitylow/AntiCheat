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

package net.h31ix.anticheat.config.holders.yaml;

import net.h31ix.anticheat.AntiCheat;
import net.h31ix.anticheat.config.Configuration;
import net.h31ix.anticheat.config.ConfigurationFile;
import net.h31ix.anticheat.config.providers.Events;
import net.h31ix.anticheat.util.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class YamlEventsHolder extends ConfigurationFile implements Events {

    public static final String FILENAME = "events.yml";

    private List<Level> levels;

    private int highestLevel;

    public YamlEventsHolder(AntiCheat plugin, Configuration config) {
        super(plugin, config, FILENAME);
    }

    @Override
    public List<Level> getLevels() {
        return levels;
    }

    @Override
    public int getHighestLevel() {
        return highestLevel;
    }

    @Override
    public void open() {
        ConfigValue<List<String>> levels = new ConfigValue<List<String>>("events");

        // Convert levels list to Levels
        this.levels = new ArrayList<Level>();
        for (String string : levels.getValue()) {
            Level level = Level.load(string);
            if (level == null || level.getValue() < 0) {
                continue;
            }

            this.levels.add(level);
            highestLevel = level.getValue() > highestLevel ? level.getValue() : highestLevel;
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
}

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

public class Levels extends ConfigurationFile {

    public static final String FILENAME = "data/levels.yml";

    public Levels(AntiCheat plugin, Configuration config) {
        super(plugin, config, FILENAME, false);
    }

    public int getLevel(String name) {
        ConfigValue<Integer> level = new ConfigValue<Integer>(name, false);
        if(level.hasValue()) {
            return level.getValue();
        } else {
            return -1;
        }
    }

    public void saveLevel(String name, int level) {
        new ConfigValue<Integer>(name, false).setValue(new Integer(level));
    }
}

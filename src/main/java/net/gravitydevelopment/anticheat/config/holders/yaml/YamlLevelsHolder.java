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
import net.gravitydevelopment.anticheat.config.providers.Levels;
import net.gravitydevelopment.anticheat.util.User;

import java.util.List;

public class YamlLevelsHolder extends ConfigurationFile implements Levels {

    public static final String FILENAME = "data/levels.yml";

    public YamlLevelsHolder(AntiCheat plugin, Configuration config) {
        super(plugin, config, FILENAME, false);
    }

    @Override
    public void loadLevelToUser(User user) {
        user.setLevel(getLevel(user.getName()));
    }

    @Override
    public void saveLevelFromUser(User user) {
        saveLevelFromUser(user, true);
    }

    private void saveLevelFromUser(User user, boolean remove) {
        saveLevel(user.getName(), user.getLevel());
        if (remove) AntiCheat.getManager().getUserManager().remove(user);
    }

    @Override
    public void saveLevelsFromUsers(List<User> users) {
        for (User user : users) {
            saveLevelFromUser(user, false);
        }
    }

    @Override
    public void updateLevelToUser(User user) {
        // This method intentionally left blank.
        return;
    }

    private int getLevel(String name) {
        ConfigValue<Integer> level = new ConfigValue<Integer>(name, false);
        if (level.hasValue()) {
            return level.getValue();
        } else {
            return 0;
        }
    }

    private void saveLevel(String name, int level) {
        new ConfigValue<Integer>(name, false).setValue(new Integer(level));
    }
}

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
import net.gravitydevelopment.anticheat.config.providers.Groups;
import net.gravitydevelopment.anticheat.util.Group;

import java.util.*;

public class YamlGroupsHolder extends ConfigurationFile implements Groups {

    public static final String FILENAME = "groups.yml";

    private List<Group> groups;

    private int highestLevel;

    public YamlGroupsHolder(AntiCheat plugin, Configuration config) {
        super(plugin, config, FILENAME);
    }

    @Override
    public List<Group> getGroups() {
        return groups;
    }

    @Override
    public int getHighestLevel() {
        return highestLevel;
    }

    @Override
    public void open() {
        ConfigValue<List<String>> groups = new ConfigValue<List<String>>("groups");

        // Convert groups list to Levels
        this.groups = new ArrayList<Group>();
        for (String string : groups.getValue()) {
            Group group = Group.load(string);
            if (group == null || group.getLevel() < 0) {
                continue;
            }

            this.groups.add(group);
            highestLevel = group.getLevel() > highestLevel ? group.getLevel() : highestLevel;
        }

        // Sort groups
        Collections.sort(this.groups, new Comparator<Group>() {
            public int compare(Group l1, Group l2) {
                if (l1.getLevel() == l2.getLevel()) {
                    return 0;
                } else if (l1.getLevel() < l2.getLevel()) {
                    return -1;
                }
                return 1;
            }
        });
    }
}

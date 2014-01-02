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

package net.gravitydevelopment.anticheat.config.providers;

import net.gravitydevelopment.anticheat.util.Group;

import java.util.List;

public interface Groups {

    /**
     * Get all groups.
     *
     * @return List of groups.
     */
    public List<Group> getGroups();

    /**
     * Get the highest level value assigned to a group.
     * Used as a maximum cap to rising hack levels.
     *
     * @return Highest group level.
     */
    public int getHighestLevel();
}

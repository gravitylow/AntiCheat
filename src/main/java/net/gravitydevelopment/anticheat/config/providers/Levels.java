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

import net.gravitydevelopment.anticheat.manage.User;

import java.util.List;

public interface Levels {

    /**
     * Checks for a stored level for this user, and loads it to their object if found.
     *
     * @param user User to load.
     */
    public void loadLevelToUser(User user);

    /**
     * Saves the user's level to storage.
     *
     * @param user User to save.
     */
    public void saveLevelFromUser(User user);

    /**
     * Saves a list of user's levels to storage.
     * Used on server shutdown / reload.
     *
     * @param users List of users to save.
     */
    public void saveLevelsFromUsers(List<User> users);

    /**
     * Check to see if the user's level has been manually changed since it was loaded, and update it if needed
     *
     * @param user User to update.
     */
    public void updateLevelToUser(User user);
}

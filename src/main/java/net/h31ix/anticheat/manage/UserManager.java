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

package net.h31ix.anticheat.manage;

import java.util.ArrayList;
import java.util.List;

import net.h31ix.anticheat.AntiCheat;
import net.h31ix.anticheat.config.Configuration;
import net.h31ix.anticheat.util.Level;
import net.h31ix.anticheat.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class UserManager {
    private List<User> users = new ArrayList<User>();
    private static Configuration config;
    private static final ChatColor GRAY = ChatColor.GRAY;
    private static final ChatColor GOLD = ChatColor.GOLD;
    private static final ChatColor YELLOW = ChatColor.YELLOW;
    private static final ChatColor RED = ChatColor.RED;
    private static List<String> alert;

    /**
     * Initialize the user manager with a given configuration
     *
     * @param conf Configuration to use
     */
    public UserManager(Configuration conf) {
        config = conf;
        alert = config.getLang().alert.getValue();
    }

    /**
     * Get a user with the given name
     * @param name Name
     * @return User with name
     */
    public User getUser(String name) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(name)) { return user; }
        }
        // Otherwise try to load them
        return loadUserFromFile(name);
    }

    /**
     * Get all users
     *
     * @return List of users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Add a user to the list
     *
     * @param user User to add
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Remove a user from the list
     *
     * @param user User to remove
     */
    public void remove(User user) {
        config.getLevels().saveLevel(user.getName(), user.getLevel());
        users.remove(user);
    }

    /**
     * Get a user's level, or 0 if the player isn't found
     *
     * @param name Name of the player
     * @return player level
     */
    public int safeGetLevel(String name) {
        User user = getUser(name);
        if (user == null) {
            return 0;
        } else {
            return user.getLevel();
        }
    }

    /**
     * Set a user's level
     *
     * @param name Name of the player
     * @param level Level to set
     */
    public void safeSetLevel(String name, int level) {
        User user = getUser(name);
        if (user != null) {
            user.setLevel(level);
        }
    }

    /**
     * Reset a user
     * @param name Name of the user
     */
    public void safeReset(String name) {
        User user = getUser(name);
        if (user != null) {
            user.resetLevel();
        }
    }

    /**
     * Load a user into the list from flatfile
     *
     * @param name Name of the user
     * @return true if found in the file
     */
    public boolean addUserFromFile(String name) {
        User user = loadUserFromFile(name);
        if (user != null) {
            users.add(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get a user from flatfile
     *
     * @param name Name of the user
     * @return the user if found, null otherwise
     */
    public User loadUserFromFile(String name) {
        int level = config.getLevels().getLevel(name);
        if (level == -1) {
            return null;
        } else {
            return new User(name, level);
        }
    }

    /**
     * Fire an alert
     *
     * @param user The user to alert
     * @param level The user's level
     * @param type The CheckType that triggered the alert
     */
    public void alert(User user, Level level, CheckType type) {
        ArrayList<String> messageArray = new ArrayList<String>();
        for (int i = 0; i < alert.size(); i++) {
            String message = alert.get(i);
            if(!message.equals("")) {
                message = message.replaceAll("&player", GOLD + user.getName() + GRAY);
                message = message.replaceAll("&check", GOLD + CheckType.getName(type) + GRAY);
                message = message.replaceAll("&level", level.getColor() + level.getName() + GRAY);
                messageArray.add(message);
            }
        }
        Utilities.alert(messageArray);
        execute(user, level.getActions(), type);
    }

    /**
     * Execute configuration actions for an alert
     * @param user The user
     * @param actions The list of actions to execute
     * @param type The CheckType that triggered the alert
     */
    public void execute(User user, List<String> actions, CheckType type) {
        execute(user, actions, type, config.getLang().kickReason.getValue(), config.getLang().playerWarning.getValue(), config.getLang().banReason.getValue());
    }

    /**
     * Execute configuration actions for an alert
     *
     * @param user The user
     * @param actions The list of actions to execute
     * @param type The CheckType that triggered the alert
     * @param kickReason The config's kick reason
     * @param warning The config's warning format
     * @param banReason The config's ban reason
     */
    public void execute(final User user, final List<String> actions, final CheckType type, final String kickReason, final List<String> warning, final String banReason) {
        // Execute synchronously for thread safety when called from AsyncPlayerChatEvent
        Bukkit.getScheduler().scheduleSyncDelayedTask(AntiCheat.getPlugin(), new Runnable() {
            @Override
            public void run() {
                final String name = user.getName();
                for(String event : actions) {
                    event = event.replaceAll("&player", name).replaceAll("&world", user.getPlayer().getWorld().getName()).replaceAll("&check", type.name());

                    if (event.startsWith("COMMAND[")) {
                        for(String cmd : Utilities.getCommands(event)) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
                        }
                    } else if (event.equalsIgnoreCase("KICK")) {
                        user.getPlayer().kickPlayer(RED + kickReason);
                        String msg = RED + config.getLang().kickBroadcast.getValue().replaceAll("&player", name);
                        if (!msg.equals("")) {
                            Bukkit.broadcastMessage(msg);
                        }
                    } else if (event.equalsIgnoreCase("WARN")) {
                        List<String> message = warning;
                        for (String string : message) {
                            user.getPlayer().sendMessage(RED + string);
                        }
                    } else if (event.equalsIgnoreCase("BAN")) {
                        user.getPlayer().setBanned(true);
                        user.getPlayer().kickPlayer(RED + banReason);
                        String msg = RED + config.getLang().banBroadcast.getValue().replaceAll("&player", name);
                        if (!msg.equals("")) {
                            Bukkit.broadcastMessage(msg);
                        }
                    } else if (event.equalsIgnoreCase("RESET")) {
                        user.resetLevel();
                    }
                }
            }
        });
    }

}

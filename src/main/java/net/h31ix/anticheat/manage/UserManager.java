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
import net.h31ix.anticheat.util.Configuration;
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
    
    public UserManager(Configuration conf) {
        config = conf;
    }
    
    public User getUser(String name) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(name)) { return user; }
        }
        // Otherwise try to load them
        return loadUserFromFile(name);
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public void addUser(User user) {
        users.add(user);
    }
    
    public void remove(User user) {
        config.saveLevel(user.getName(), user.getLevel());
        users.remove(user);
    }
    
    public int safeGetLevel(String name) {
        User user = getUser(name);
        if (user == null) {
            return 0;
        } else {
            return user.getLevel();
        }
    }
    
    public void safeSetLevel(String name, int level) {
        User user = getUser(name);
        if (user != null) {
            user.setLevel(level);
        }
    }
    
    public void safeReset(String name) {
        User user = getUser(name);
        if (user != null) {
            user.resetLevel();
        }
    }
    
    public boolean addUserFromFile(String name) {
        User user = loadUserFromFile(name);
        if (user != null) {
            users.add(user);
            return true;
        } else {
            return false;
        }
    }
    
    public User loadUserFromFile(String name) {
        int level = config.getLevel(name);
        if (level == -1) {
            return null;
        } else {
            return new User(name, level);
        }
    }
    
    public void alert(User user, Level level, CheckType type) {
        final String name = user.getName();
        List<String> messageArray = config.getLang().getAlert();
        for (int i = 0; i < messageArray.size(); i++) {
            String message = messageArray.get(i);
            message = message.replaceAll("&player", GOLD + name + GRAY);
            message = message.replaceAll("&check", GOLD + CheckType.getName(type) + GRAY);
            message = message.replaceAll("&level", level.getColor() + level.getName() + GRAY);
            messageArray.set(i, message);
        }
        Utilities.alert(messageArray);
        execute(user, level.getActions(), type);
    }
    public void execute(User user, List<String> actions, CheckType type) {
        execute(user, actions, type, config.getLang().getKickReason(), config.getLang().getWarning(), config.getLang().getBanReason());
    }

    public void execute(User user, List<String> actions, CheckType type, String kickReason, List<String> warning, String banReason) {
        final String name = user.getName();
        for(String event : actions) {
            event = event.replaceAll("&player", name).replaceAll("&world", user.getPlayer().getWorld().getName()).replaceAll("&check", type.name());

            if (event.startsWith("COMMAND[")) {
                for(String cmd : Utilities.getCommands(event)) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
            } else if (event.equalsIgnoreCase("KICK")) {
                user.getPlayer().kickPlayer(RED + kickReason);
                String msg = RED + config.getLang().getKickBroadcast().replaceAll("&player", name);
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
                String msg = RED + config.getLang().getBanBroadcast().replaceAll("&player", name);
                if (!msg.equals("")) {
                    Bukkit.broadcastMessage(msg);
                }
            } else if (event.equalsIgnoreCase("RESET")) {
                user.resetLevel();
            }
        }
    }
    
}

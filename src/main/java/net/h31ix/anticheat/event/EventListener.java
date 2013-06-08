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

package net.h31ix.anticheat.event;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.manage.*;
import net.h31ix.anticheat.util.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class EventListener implements Listener {
    private static final Map<CheckType, Integer> USAGE_LIST = new EnumMap<CheckType, Integer>(CheckType.class);
    private static final Map<String, Integer> DECREASE_LIST = new HashMap<String, Integer>();
    private static final CheckManager CHECK_MANAGER = Anticheat.getManager().getCheckManager();
    private static final Backend BACKEND = Anticheat.getManager().getBackend();
    private static final Anticheat PLUGIN = Anticheat.getManager().getPlugin();
    private static final UserManager USER_MANAGER = Anticheat.getManager().getUserManager();
    private static final Configuration CONFIG = Anticheat.getManager().getConfiguration();

    public static void log(String message, Player player, CheckType type) {
        User user = getUserManager().getUser(player.getName());
        logCheat(type, user);
        if (user.increaseLevel(type) && message != null) {
            Anticheat.getManager().log(message);
        }
        removeDecrease(user);
    }

    private static void logCheat(CheckType type, User user) {
        USAGE_LIST.put(type, getCheats(type) + 1);
        // Ignore plugins that are creating NPCs with no names (why the hell)
        if (user != null && user.getName() != null) {
            type.logUse(user);
            if (Anticheat.getManager().getConfiguration().getFileLogLevel() == 2 && type.getUses(user.getName()) % 10 == 0) {
                Anticheat.getManager().fileLog(user.getName() + " has triggered multiple " + type + " checks.");
            }
        }
    }

    public void resetCheck(CheckType type) {
        USAGE_LIST.put(type, 0);
    }

    public static int getCheats(CheckType type) {
        int x = 0;
        if (USAGE_LIST.get(type) != null) {
            x = USAGE_LIST.get(type);
        }
        return x;
    }

    private static void removeDecrease(User user) {
    int x = 0;
    // Ignore plugins that are creating NPCs with no names
    if (user.getName() != null) {
        if (DECREASE_LIST.get(user.getName()) != null) {
            x = DECREASE_LIST.get(user.getName());
            x -= 2;
            if (x < 0) {
                x = 0;
            }
        }
        DECREASE_LIST.put(user.getName(), x);
        }
    }

    public static void decrease(Player player) {
        User user = getUserManager().getUser(player.getName());
        // Ignore plugins that are creating NPCs with no names
        if (user.getName() != null) {
            int x = 0;

            if (DECREASE_LIST.get(user.getName()) != null) {
                x = DECREASE_LIST.get(user.getName());
            }

            x += 1;
            DECREASE_LIST.put(user.getName(), x);

            if (x >= 10) {
                user.decreaseLevel();
                DECREASE_LIST.put(user.getName(), 0);
            }
        }
    }

    public static CheckManager getCheckManager() {
        return CHECK_MANAGER;
    }

    public static AnticheatManager getManager() {
        return Anticheat.getManager();
    }

    public static Backend getBackend() {
        return BACKEND;
    }

    public static UserManager getUserManager() {
        return USER_MANAGER;
    }

    public static Anticheat getPlugin() {
        return PLUGIN;
    }

    public static Configuration getConfig() {
        return CONFIG;
    }

    public static boolean silentMode() {
        return CONFIG.silentMode();
    }
}

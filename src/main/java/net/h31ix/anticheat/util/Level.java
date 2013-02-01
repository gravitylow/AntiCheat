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

package net.h31ix.anticheat.util;

import net.h31ix.anticheat.Anticheat;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Level {

    private String name;
    private int value;
    private ChatColor color = ChatColor.RED;
    private boolean customColor = false;
    private List<String> actions = new ArrayList<String>();

    public Level(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public Level(String name, int value, ChatColor color) {
        this(name, value);
        this.color = color;
        customColor = true;
    }

    public Level(String name, int value, String color) {
        this(name, value);
        ChatColor c = ChatColor.valueOf(color);
        if(c == null) {
            Anticheat.getPlugin().getLogger().warning("Event '"+name+"' was initialized with the color '"+color+"' which is invalid.");
            Anticheat.getPlugin().getLogger().warning("This event will not run properly. See http://jd.bukkit.org/apidocs/org/bukkit/ChatColor.html#enum_constant_summary for a list of valid colors");
        } else {
            this.color = c;
            customColor = true;
        }
    }

    public void addAction(String string) {
        actions.add(string);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public ChatColor getColor() {
        return color;
    }

    public List<String> getActions() {
        return actions;
    }

    public static Level load(String string) {
        try {
            string = string.trim();
            String name = string.split(":")[0];
            int level = Integer.parseInt(string.split(":")[1]);
            if(string.split(":").length == 3) {
                String color = string.split(":")[2];
                return new Level(name, level, color);
            } else {
                return new Level(name, level);
            }
        } catch (Exception ex) {
            Anticheat.getPlugin().getLogger().warning("An event was intialized with an invalid string: '"+string+"'");
            Anticheat.getPlugin().getLogger().warning("The proper format is: 'name: threshold, color' such as 'high: 50, RED'");
            Anticheat.getPlugin().getLogger().warning("This event will NOT run. ("+ex.getMessage()+")");
            return null;
        }
    }

    @Override
    public String toString() {
        return name + " : " + value + (customColor ? " : "+color.name() : "");
    }
}

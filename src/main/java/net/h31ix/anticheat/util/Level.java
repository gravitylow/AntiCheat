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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.h31ix.anticheat.AntiCheat;
import org.bukkit.ChatColor;

public class Level {

    private String name;
    private int value;
    private ChatColor color = ChatColor.RED;
    private List<String> actions = new ArrayList<String>();

    public Level(String name, int value, String color, List<String> actions) {

        this.name = name;
        this.value = value;
        this.actions = actions;

        ChatColor c = ChatColor.valueOf(color);
        if(c == null) {
            AntiCheat.getPlugin().getLogger().warning("Event '"+name+"' was initialized with the color '"+color+"' which is invalid.");
            AntiCheat.getPlugin().getLogger().warning("This event will not run properly. See http://jd.bukkit.org/apidocs/org/bukkit/ChatColor.html#enum_constant_summary for a list of valid colors");
        } else {
            this.color = c;
        }
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
            if(string.split(":").length == 4) {
                string = Utilities.removeWhitespace(string);
                String name = string.split(":")[0];
                int level = Integer.parseInt(string.split(":")[1]);
                String color = string.split(":")[2];
                List<String> actions = Arrays.asList(string.split(":")[3].split(","));
                return new Level(name, level, color, actions);
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            AntiCheat.getPlugin().getLogger().warning("An event was initialized with an invalid string: '"+string+"'");
            AntiCheat.getPlugin().getLogger().warning("The proper format is: 'name : threshold : color : action' such as 'High : 50 : RED : KICK'");
            AntiCheat.getPlugin().getLogger().warning("This event will NOT run. ("+ex.getMessage()+")");
            return null;
        }
    }

    @Override
    public String toString() {
        return name + " : " + value + " : "+ color.name() + " : " + Utilities.listToCommaString(actions);
    }
}

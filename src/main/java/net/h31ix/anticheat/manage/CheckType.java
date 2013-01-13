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

import java.util.HashMap;
import java.util.Map;
import net.h31ix.anticheat.util.Permission;
import org.bukkit.entity.Player;

/**
 * <p>
 * All the types of checks and their corresponding permission nodes.
 */

public enum CheckType {
    ZOMBE_FLY(Permission.CHECK_ZOMBE_FLY),
    ZOMBE_NOCLIP(Permission.CHECK_ZOMBE_NOCLIP),
    ZOMBE_CHEAT(Permission.CHECK_ZOMBE_CHEAT),
    FLY(Permission.CHECK_FLY),
    WATER_WALK(Permission.CHECK_WATERWALK),
    NO_SWING(Permission.CHECK_NOSWING),
    FAST_BREAK(Permission.CHECK_FASTBREAK),
    FAST_PLACE(Permission.CHECK_FASTPLACE),
    SPAM(Permission.CHECK_SPAM),
    SPRINT(Permission.CHECK_SPRINT),
    SNEAK(Permission.CHECK_SNEAK),
    SPEED(Permission.CHECK_SPEED),
    VCLIP(Permission.CHECK_VCLIP),
    SPIDER(Permission.CHECK_SPIDER),
    NOFALL(Permission.CHECK_NOFALL),
    FAST_BOW(Permission.CHECK_FASTBOW),
    FAST_EAT(Permission.CHECK_FASTEAT),
    FAST_HEAL(Permission.CHECK_FASTHEAL),
    FORCEFIELD(Permission.CHECK_FORCEFIELD),
    XRAY(Permission.CHECK_XRAY),
    LONG_REACH(Permission.CHECK_LONGREACH),
    FAST_PROJECTILE(Permission.CHECK_FASTPROJECTILE),
    ITEM_SPAM(Permission.CHECK_ITEMSPAM),
    FAST_INVENTORY(Permission.CHECK_FASTINVENTORY),
    AUTOTOOL(Permission.CHECK_AUTOTOOL);
    
    private final Permission permission;
    private final Map<String, Integer> level = new HashMap<String, Integer>();
    
    private CheckType(Permission perm) {
        this.permission = perm;
    }
    
    public boolean checkPermission(Player player) {
        return permission.get(player);
    }
    
    public void logUse(String name) {
        int amount = level.get(name) == null ? 1 : level.get(name) + 1;
        level.put(name, amount);
    }
    
    public void clearUse(String name) {
        level.put(name, 0);
    }
    
    public int getUses(String name) {
        return level.get(name) != null ? level.get(name) : 0;
    }
    
    public static String getName(CheckType type) {
        char[] chars = type.toString().replaceAll("_", " ").toLowerCase().toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
}

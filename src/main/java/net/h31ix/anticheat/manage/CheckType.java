/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 H31IX http://h31ix.net
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
import org.bukkit.entity.Player;


/**
 * <p>
 * All the types of checks and their corresponding permission nodes.
 */


public enum CheckType 
{
    ZOMBE_FLY("anticheat.zombe.fly"),
    ZOMBE_NOCLIP("anticheat.zombe.noclip"),
    ZOMBE_CHEAT("anticheat.zombe.cheat"),
    FLY("anticheat.fly"),
    WATER_WALK("anticheat.waterwalk"),
    NO_SWING("anticheat.noswing"),
    FAST_BREAK("anticheat.fastbreak"),
    FAST_PLACE("anticheat.fastplace"),
    SPAM("anticheat.spam"),
    SPRINT("anticheat.sprint"),
    SNEAK("anticheat.sneak"),
    SPEED("anticheat.speed"),
    SPIDER("anticheat.spider"),
    NOFALL("anticheat.nofall"),
    FAST_BOW("anticheat.fastbow"),
    FAST_EAT("anticheat.fasteat"),
    FAST_HEAL("anticheat.fastheal"),
    FORCEFIELD("anticheat.forcefield"),
    XRAY("anticheat.xray"),
    LONG_REACH("anticheat.longreach"),
    FAST_PROJECTILE("anticheat.projectile"),
    ITEM_SPAM("anticheat.itemspam");
    
    private final String permission;
    private final Map<String,Integer> level = new HashMap<String,Integer>();
    
    private CheckType(String permission) 
    {
        this.permission = permission;
    }
    
    public String getPermission()
    {
        return this.permission;
    }
    
    public void logUse(Player player)
    {
        String name = player.getName();
        if(level.get(name) == null)
        {
            level.put(name, 1);
        }
        else
        {
            int amount = level.get(name)+1;
            level.put(name, amount);
        }        
    }
    
    public void clearUse(Player player)
    {
        level.put(player.getName(), 0);
    }
    
    public int getUses(Player player)
    {
        int use = 0;
        if(level.get(player.getName()) != null)
        {
            use = level.get(player.getName());
        }
        return use;
    }
    
    public static String getName(CheckType type)
    {
        char[] chars = type.toString().replaceAll("_", " ").toLowerCase().toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);     
        return new String(chars);
    }
}

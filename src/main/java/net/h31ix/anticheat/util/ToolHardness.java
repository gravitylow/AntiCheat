/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 AntiCheat Team | http://gravitydevelopment.net
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

import org.bukkit.Material;

public enum ToolHardness {
    
    // NOTHING(null, 1.5)
    WOOD(0.75D),
    STONE(0.40D),
    IRON(0.25D),
    DIAMOND(0.20D),
    SHEARS(0.55D),
    GOLD(0.15D);
    
    double hardness;
    
    ToolHardness(double hard) {
        hardness = hard;
    }
    
    public static double getToolHardness(Material tool) {
        for (ToolHardness e : ToolHardness.values()) {
            if (tool.name().contains(e.name())) { return e.hardness; }
        }
        
        return 1.50D;
    }
}

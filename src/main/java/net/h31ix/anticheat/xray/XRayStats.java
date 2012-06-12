/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 AntiCheat Team | http://h31ix.net
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

package net.h31ix.anticheat.xray;

import java.util.Map;

public class XRayStats
{
    private static final int DIVISOR = 100;
    private double t = 1;
    private double d = 0;
    private double g = 0;
    private double i = 0;
    private double c = 0;
    private double l = 0;
    private double r = 0;
    private double o = 0;

    public XRayStats(String player, Map<String, Integer> diamond, Map<String, Integer> gold, Map<String, Integer> iron, Map<String, Integer> coal, Map<String, Integer> lapis, Map<String, Integer> redstone, Map<String, Integer> other, Map<String, Integer> total)
    {
        if (total.get(player) != null)
        {
            t = total.get(player);
        }
        if (diamond.get(player) != null)
        {
            d = (diamond.get(player) / t) * DIVISOR;
        }
        if (gold.get(player) != null)
        {
            g = (gold.get(player) / t) * DIVISOR;
        }
        if (iron.get(player) != null)
        {
            i = (iron.get(player) / t) * DIVISOR;
        }
        if (coal.get(player) != null)
        {
            c = (coal.get(player) / t) * DIVISOR;
        }
        if (lapis.get(player) != null)
        {
            l = (lapis.get(player) / t) * DIVISOR;
        }
        if (redstone.get(player) != null)
        {
            r = (redstone.get(player) / t) * DIVISOR;
        }
        if (other.get(player) != null)
        {
            o = (other.get(player) / t) * DIVISOR;
        }
    }

    public double getTotal()
    {
        return t;
    }

    public double getDiamond()
    {
        return d;
    }

    public double getGold()
    {
        return g;
    }

    public double getIron()
    {
        return i;
    }

    public double getCoal()
    {
        return c;
    }

    public double getLapis()
    {
        return l;
    }

    public double getRedstone()
    {
        return r;
    }

    public double getOther()
    {
        return o;
    }
}

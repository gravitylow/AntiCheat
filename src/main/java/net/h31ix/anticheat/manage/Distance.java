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

import org.bukkit.Location;

public class Distance
{
    private final Location l1,l2;
    
    public Distance(Location from, Location to)
    {
        l1 = to;
        l2 = from;
    }
    
    public double fromX() 
    {
    	return l2.getX();
    }
    
    public double fromY() 
    {
    	return l2.getY();
    }
    
    public double fromZ()
    {
    	return l2.getZ();
    }
    
    public double toY() 
    {
    	return l1.getY();
    }

    public double getXDifference()
    {
        return Math.abs(l1.getX()-l2.getX());   
    }

    public double getZDifference()
    {
        return Math.abs(l1.getZ()-l2.getZ());   
    }    

    public double getYDifference()
    {
        return Math.abs(l1.getY()-l2.getY());   
    }
        
}
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

import org.bukkit.Location;

public class SpyState {
    private boolean allowFlight;
    private boolean flying;
    private Location location;
    
    public SpyState(boolean allowFlight, boolean flying, Location location) {
        this.allowFlight = allowFlight;
        this.flying = flying;
        this.location = location;
    }
    
    public boolean getAllowFlight() {
        return this.allowFlight;
    }
    
    public boolean getFlying() {
        return this.flying;
    }
    
    public Location getLocation() {
        return this.location;
    }
}

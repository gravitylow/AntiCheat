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

import net.h31ix.anticheat.Anticheat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class VehicleListener extends EventListener {
    
    @EventHandler(ignoreCancelled = true)
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player) {
            getBackend().logEnterExit((Player) event.getEntered());
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player) {
            getBackend().logEnterExit((Player) event.getExited());
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }
    
    
    @EventHandler(ignoreCancelled = true)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        if (event.getVehicle().getPassenger() != null &&  event.getVehicle().getPassenger() instanceof Player) {
            getBackend().logEnterExit((Player) event.getVehicle().getPassenger());
        }
        
        Anticheat.getManager().addEvent(event.getEventName(), event.getHandlers().getRegisteredListeners());
    }     
}

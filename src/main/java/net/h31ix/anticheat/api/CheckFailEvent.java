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

package net.h31ix.anticheat.api;

import net.h31ix.anticheat.manage.CheckType;
import net.h31ix.anticheat.manage.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CheckFailEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final User user;
    private final CheckType type;

    public CheckFailEvent(User user, CheckType type) {
        this.user = user;
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public CheckType getCheck() {
        return type;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}

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

import net.h31ix.anticheat.AntiCheat;
import net.h31ix.anticheat.config.Configuration;
import net.h31ix.anticheat.util.Level;
import net.h31ix.anticheat.util.Utilities;
import net.h31ix.anticheat.util.rule.Rule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class User {
    private final String name;
    private final int id;
    private int level = 0;
    private Location goodLocation;
    private List<ItemStack> inventorySnapshot = null;
    private Configuration config = AntiCheat.getManager().getConfiguration();
    private int toX, toY, toZ;
    private String [] messages = new String[2];
    private Long [] messageTimes = new Long[2];

    private boolean isWaitingOnLevelSync;

    /**
     * Initialize an AntiCheat user
     *
     * @param name Player's name
     */
    public User(String name) {
        this(name, 0);
    }

    /**
     * Initialize an AntiCheat user with a predefined level
     *
     * @param name Player's name
     * @param level Player's level
     */
    public User(String name, int level) {
        this.name = name;
        this.level = level;

        this.id = getPlayer().isOnline() ? getPlayer().getEntityId() : -1;
    }

    /**
     * Get the player's name
     *
     * @return Player name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the player's entity ID
     *
     * @return Entity id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the Bukkit player
     *
     * @return Player
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(name);
    }

    /**
     * Get the user's level value
     *
     * @return level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Increase the player's level (from failing a check)
     *
     * @param type The check failed
     * @return true if the level was increased
     */
    public boolean increaseLevel(CheckType type) {
        if (getPlayer() != null && getPlayer().isOnline()) {
            if (silentMode() && type.getUses(name) % 4 != 0) {
                // Prevent silent mode from increasing the level way too fast
                return false;
            } else {
                if (level < config.getEvents().highestLevel) {
                    level++;

                    // Check levels
                    for(Level l : getLevels()) {
                        if(l.getValue() == level) {
                            AntiCheat.getManager().getUserManager().alert(this, l, type);
                            if(l.getValue() == config.getEvents().highestLevel) {
                                level = l.getValue() - 10;
                            }
                        }
                    }

                    // Execute rules
                    for(Rule rule : config.getEvents().rules) {
                        rule.check(this, type);
                    }
                    return true;
                }
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Decrease the player's level by one value
     */
    public void decreaseLevel() {
        level = level != 0 ? level - 1 : 0;
    }

    /**
     * Set the users level to a specific value. Must be 0 < level < config-defined highest level
     * @param level level to set
     * @return true if the level was valid and set properly
     */
    public boolean setLevel(int level) {
        isWaitingOnLevelSync = false;
        if (level >= 0) {
            if (level <= config.getEvents().highestLevel) {
                this.level = level;
                return true;
            } else {
                this.level = config.getEvents().highestLevel;
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Reset the user's level to zero and clear all check failures
     */
    public void resetLevel() {
        level = 0;
        for (CheckType type : CheckType.values()) {
            type.clearUse(name);
        }
    }

    /**
     * Get the last known valid location of the player for use in resetting
     *
     * @param location default value to be returned if no known good location exists
     * @return last known valid location
     */
    public Location getGoodLocation(Location location) {
        if (goodLocation == null) {
            return location;
        }

        return goodLocation;
    }

    /**
     * Set the last known valid location of the player
     *
     * @param location location to be set
     * @return true if the location was valid and set properly
     */
    public boolean setGoodLocation(Location location) {
        if (Utilities.cantStandAtExp(location) || (location.getBlock().isLiquid() && !Utilities.isFullyInWater(location))) {
            return false;
        }

        goodLocation = location;
        return true;
    }

    /**
     * Store a copy of the player's inventory, for use in resetting
     *
     * @param is ItemStack list to store
     */
    public void setInventorySnapshot(ItemStack[] is) {
        inventorySnapshot = new ArrayList<ItemStack>();
        for(int i=0;i<is.length;i++) {
            if(is[i] != null) {
                inventorySnapshot.add(is[i].clone());
            }
        }
    }

    /**
     * Remove the current inventory snapshot
     */
    public void removeInventorySnapshot() {
        inventorySnapshot = null;
    }

    /**
     * Restore the player's inventory with the current inventory snapshot
     * @param inventory Player's inventory
     */
    public void restoreInventory(Inventory inventory) {
        if(inventorySnapshot != null) {
            inventory.clear();
            for(ItemStack is : inventorySnapshot) {
                if(is != null) {
                    inventory.addItem(is);
                }
            }
        }
    }

    /**
     * Set the player's to location
     * @param x X value
     * @param y Y value
     * @param z Z value
     */
    public void setTo(double x, double y, double z) {
        toX = (int)x;
        toY = (int)y;
        toZ = (int)z;
    }

    /**
     * Check if a given to value is the same as the stored value
     * @param x X value
     * @param y Y value
     * @param z Z value
     * @return true if coordinates are all equal
     */
    public boolean checkTo(double x, double y, double z) {
        return (int)x == toX && (int)y == toY && (int)z == toZ;
    }

    /**
     * Log a player's chat message
     *
     * @param message Message to log
     */
    public void addMessage(String message) {
        messages[1] = messages[0];
        messages[0] = message;

        messageTimes[1] = messageTimes[0];
        messageTimes[0] = System.currentTimeMillis();
    }

    /**
     * Get a stored chat message
     *
     * @param index Index of the chat in storage
     * @return the message
     */
    public String getMessage(int index) {
        return messages[index];
    }

    /**
     * Get the time a message in storage was sent
     *
     * @param index Index of the chat in storage
     * @return time the message was sent
     */
    public Long getMessageTime(int index) {
        return messageTimes[index];
    }

    /**
     * Clear all stored messages
     */
    public void clearMessages() {
        messages = new String[2];
        messageTimes = new Long[2];
    }

    /**
     * Get the time the very last stored message was sent
     *
     * @return the last message's time
     */
    public Long getLastMessageTime() {
        return getMessageTime(0) == null ? -1 : getMessageTime(0);
    }

    private List<Level> getLevels() {
        return config.getEvents().levels;
    }

    private boolean silentMode() {
        return config.getConfig().silentMode.getValue();
    }

    public void setIsWaitingOnLevelSync(boolean b) {
        this.isWaitingOnLevelSync = b;
    }

    public boolean isWaitingOnLevelSync() {
        return isWaitingOnLevelSync;
    }
}

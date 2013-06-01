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

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.util.Configuration;
import net.h31ix.anticheat.util.Level;
import net.h31ix.anticheat.util.Utilities;
import net.h31ix.anticheat.util.rule.Rule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class User {
    private final String name;
    private int level = 0;
    private Location goodLocation;
    private boolean silentMode;
    private List<ItemStack> inventorySnapshot = null;
    private Configuration config = Anticheat.getManager().getConfiguration();
    private List<Level> levels = new ArrayList<Level>();
    private int toX, toY, toZ;
    private String [] messages = new String[2];
    private Long [] messageTimes = new Long[2];

    public User(String name) {
        this.name = name;
        getConfigInfo();
    }
    
    public User(String name, int level) {
        this.name = name;
        this.level = level;
        getConfigInfo();
    }
    
    private void getConfigInfo() {
        config = Anticheat.getManager().getConfiguration();
        silentMode = config.silentMode();
        levels = config.getLevels();
    }
    
    public String getName() {
        return name;
    }
    
    public Player getPlayer() {
        return Bukkit.getPlayer(name);
    }
    
    public int getLevel() {
        return level;
    }
    
    public boolean increaseLevel(CheckType type) {
        if (getPlayer() != null && getPlayer().isOnline()) {
            if (silentMode && type.getUses(name) % 4 != 0) {
                // Prevent silent mode from increasing the level way too fast
                return false;
            } else {
                level++;

                // Check levels
                for(Level l : levels) {
                    if(l.getValue() == level) {
                        Anticheat.getManager().getUserManager().alert(this, l, type);
                        if(l.getValue() == config.getHighestLevel()) {
                            level = l.getValue() - 10;
                        }
                    }
                }

                // Execute rules
                for(Rule rule : config.getRules()) {
                    rule.check(this, type);
                }
                return true;
            }
        } else {
            return false;
        }
    }
    
    public void decreaseLevel() {
        level = level != 0 ? level - 1 : 0;
    }
    
    public void setLevel(int level) {
        if (level > 0 && level <= config.getHighestLevel()) {
            this.level = level;
        }
    }
    
    public void resetLevel() {
        level = 0;
        for (CheckType type : CheckType.values()) {
            type.clearUse(name);
        }
    }

    public Location getGoodLocation(Location e) {
        if (goodLocation == null) { return e; }
        
        return goodLocation;
    }
    
    public void setGoodLocation(Location e) {
        if (Utilities.cantStandAtExp(e) || (e.getBlock().isLiquid() && !Utilities.isFullyInWater(e))) {
            return;
        }
        
        goodLocation = e;
    }

    public void setSnapshot(ItemStack[] is) {
        inventorySnapshot = new ArrayList<ItemStack>();
        for(int i=0;i<is.length;i++) {
            if(is[i] != null) {
                inventorySnapshot.add(is[i].clone());
            }
        }
    }

    public void removeSnapshot() {
        inventorySnapshot = null;
    }

    public void restore(Inventory inventory) {
        if(inventorySnapshot != null) {
            inventory.clear();
            for(ItemStack is : inventorySnapshot) {
                if(is != null) {
                    inventory.addItem(is);
                }
            }
        }
    }

    public void setTo(double x, double y, double z) {
        toX = (int)x;
        toY = (int)y;
        toZ = (int)z;
    }

    public boolean checkTo(double x, double y, double z) {
        return (int)x == toX && (int)y == toY && (int)z == toZ;
    }

    public void addMessage(String message) {
        messages[1] = messages[0];
        messages[0] = message;

        messageTimes[1] = messageTimes[0];
        messageTimes[0] = System.currentTimeMillis();
    }

    public String getMessage(int index) {
        return messages[index];
    }

    public Long getMessageTime(int index) {
        return messageTimes[index];
    }

    public void clearMessages() {
        messages = new String[2];
        messageTimes = new Long[2];
    }

    public Long getLastMessageTime() {
        return getMessageTime(0) == null ? -1 : getMessageTime(0);
    }
}

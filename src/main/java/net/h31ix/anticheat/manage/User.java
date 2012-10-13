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

package net.h31ix.anticheat.manage;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.util.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class User
{
    private final String name;
    private int level = 0;
    private int high;
    private int med;
    private boolean silentMode;

    public User(String name)
    {
        this.name = name;
        getConfigInfo();
    }

    public User(String name, int level)
    {
        this.name = name;
        this.level = level;
        getConfigInfo();
    }

    private void getConfigInfo()
    {
        Configuration config = Anticheat.getManager().getConfiguration();
        silentMode = config.silentMode();
        med = config.medThreshold();
        high = config.highThreshold();
    }

    public String getName()
    {
        return name;
    }

    public Player getPlayer()
    {
        return Bukkit.getPlayer(name);
    }

    public int getLevel()
    {
        return level;
    }

    public boolean increaseLevel(CheckType type)
    {
        if(getPlayer().isOnline())
        {
            if(silentMode && type.getUses(name) % 4 != 0)
            {
                // Prevent silent mode from increasing the level way too fast
                return false;
            }
            else
            {
                level++;
                if(level == med)
                {
                    Anticheat.getManager().getUserManager().alertMed(this, type);
                }
                else if(level == high)
                {
                    Anticheat.getManager().getUserManager().alertHigh(this, type);
                    // Prevent the player from going over the high limit
                    level = high-10;
                }
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public void decreaseLevel()
    {
        level = level != 0 ? level-1 : 0;
    }

    public void setLevel(int level)
    {
        if(level > 0 && level <= high)
        {
            this.level = level;
        }
    }

    public void resetLevel()
    {
        level = 0;
        for (CheckType type : CheckType.values())
        {
            type.clearUse(name);
        }
    }

}

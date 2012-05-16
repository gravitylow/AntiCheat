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

package net.h31ix.anticheat.event;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.manage.Backend;
import net.h31ix.anticheat.manage.CheckManager;
import net.h31ix.anticheat.manage.CheckType;
import net.h31ix.anticheat.manage.Distance;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener extends EventListener 
{    
    private final Backend backend = getBackend();
    private final CheckManager checkManager = getCheckManager();
    private final Anticheat plugin = getPlugin();
    
    @EventHandler
    public void onBlockDamage(BlockDamageEvent event)
    {
        if(event.getInstaBreak())
        {
            backend.logInstantBreak(event.getPlayer());
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        final Player player = event.getPlayer();
        Block block = event.getBlock();
        if(player != null && player.getGameMode() != GameMode.CREATIVE)
        {     
            if(checkManager.willCheck(player, CheckType.FAST_PLACE))
            {
                if(backend.justPlaced(player))
                {
                    event.setCancelled(true);
                    log("tried to place a block of "+block.getType().name()+" too fast.",player,CheckType.FAST_PLACE);                     
                }   
                else
                {
                    decrease(player);
                    backend.logBlockPlace(player);
                }                
            }            
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        final Player player = event.getPlayer();
        Block block = event.getBlock();
        if(player != null)
        {     
            if(checkManager.willCheck(player, CheckType.NO_SWING))
            {
                if(backend.checkSwing(player))
                {
                    event.setCancelled(true);
                    log("tried to break a block of "+block.getType().name()+" without swinging their arm.",player,CheckType.NO_SWING);  
                }
                else
                {
                    decrease(player);
                }
            }
            if(checkManager.willCheck(player, CheckType.LONG_REACH))
            {
                Distance distance = new Distance(player.getLocation(),block.getLocation());
                if(backend.checkLongReachBlock(distance.getXDifference(),distance.getYDifference(),distance.getZDifference()))
                {
                    event.setCancelled(true);
                    log("tried to break a block of "+block.getType().name()+" that was too far away.",player,CheckType.LONG_REACH);                      
                }
                else
                {
                    decrease(player);
                }                
            }            
            if(checkManager.willCheck(player, CheckType.FAST_BREAK))
            {
                if(backend.checkFastBreak(player,block))
                {
                    event.setCancelled(true);
                    log("tried to break a block of "+block.getType().name()+" too fast.",player,CheckType.FAST_BREAK);                     
                }
                else
                {
                    decrease(player);
                }                
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        backend.checkSwing(player);
                    }
                },      2L);                    
            }
        }
    }
}
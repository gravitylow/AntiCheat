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

import net.h31ix.anticheat.manage.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.PlayerInventory;

public class PlayerListener extends EventListener 
{
    private final Backend backend = getBackend();
    private final CheckManager checkManager = getCheckManager();  
    
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        if(checkManager.willCheck(player, CheckType.SPAM))
        {     
            backend.logChat(player);
        }
    }
    
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if(checkManager.willCheck(player, CheckType.SPAM))
        {     
            backend.logChat(player);
            if(backend.checkSpam(player, event.getMessage()))
            {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED+"Please do not spam.");
            }
        }
    }
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event)
    {
        backend.clearChatLevel(event.getPlayer());
    }   
    
    @EventHandler
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event)
    {
        Player player = event.getPlayer();
        if(checkManager.willCheck(player, CheckType.SPRINT))
        {
            if(backend.checkSprint(event))
            {
                event.setCancelled(true);
                log("tried to sprint while hungry.",player,CheckType.SPRINT);  
            }
            else
            {
                decrease(player);
            }              
        }
    }    
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            Material m = inv.getItemInHand().getType();
            if(m == Material.BOW)
            {
                backend.logBowWindUp(player);
            }
            else if(Utilities.isFood(m))
            {
                backend.logEatingStart(player);
            }
        }
    }    
    
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        Player player = event.getPlayer();
        if(checkManager.willCheck(player, CheckType.ITEM_SPAM))
        {     
            if(backend.justDroppedItem(player))
            {
                event.setCancelled(true);
            }
            else
            {
                backend.logDroppedItem(player);
            }
        }
    }  
    
    @EventHandler 
    public void onPlayerEnterBed(PlayerBedEnterEvent event)
    {
        backend.logEnterExit(event.getPlayer());
    }
    
    @EventHandler 
    public void onPlayerExitBed(PlayerBedLeaveEvent event)
    {
        backend.logEnterExit(event.getPlayer());
    }    
    
    @EventHandler
    public void onPlayerAnimation(PlayerAnimationEvent event)
    {
        Player player = event.getPlayer();
        backend.logAnimation(player);
    }    
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        backend.addNSH(player);
        String section = "\u00a7";
        if(checkManager.willCheck(player, CheckType.ZOMBE_FLY))
        {      
           player.sendMessage(section+"f "+section+"f "+section+"1 "+section+"0 "+section+"2 "+section+"4");
        }
        if(checkManager.willCheck(player, CheckType.ZOMBE_CHEAT))
        {             
           player.sendMessage(section+"f "+section+"f "+section+"2 "+section+"0 "+section+"4 "+section+"8");
        }
        if(checkManager.willCheck(player, CheckType.ZOMBE_NOCLIP))
        {          
           player.sendMessage(section+"f "+section+"f "+section+"4 "+section+"0 "+section+"9 "+section+"6");                                
        }
        backend.logJoin(event.getPlayer());
        if(!getPlayerManager().hasLevel(player))
        {
            getPlayerManager().setLevel(player, getManager().getConfiguration().getLevel(player.getName()));
        }
    } 
    
    @EventHandler(priority = EventPriority.LOW)
    public void checkExploit(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        Distance distance = new Distance(from, to);
        double x = distance.getXDifference();
        double y = distance.getYDifference();
        double z = distance.getZDifference();
        if(checkManager.willCheck(player, CheckType.FLY) && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && backend.checkFlight(player, from.getY(), to.getY()))
        {
            from.setX(from.getX()-1);
            from.setY(from.getY()-1);
            from.setZ(from.getZ()-1);        
            event.setTo(from);
            //Lets really give this flyer a real pushdown.
            Location newLocation = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY()-2, player.getLocation().getZ());
            Block newBlock = newLocation.getBlock();
            if(newBlock.getTypeId() != 0) 
            {
                event.setTo(newLocation);
            } 
            log("tried to fly.",player,CheckType.FLY);        
        }
        if(checkManager.willCheck(player, CheckType.FLY) && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && backend.checkDoomsOfTheYHeckers(player)) {
        	 from.setX(from.getX()-1);
             from.setY(from.getY()-1);
             from.setZ(from.getZ()-1);
             if(from.getBlock().getTypeId() == 0)
            	 event.setTo(from);
             for(int i= 5;i>0;i--) 
             {
                 Location newLocation = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY()-i, player.getLocation().getZ());
                 Block lower = newLocation.getBlock();
                 if(lower.getTypeId() == 0) 
                 {
                     player.teleport(newLocation);
                     break;
                 } 
             }
             //Lets really give this flyer a real pushdown.
             Location newLocation = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY()-2, player.getLocation().getZ());
             Block newBlock = newLocation.getBlock();
             if(newBlock.getTypeId() != 0) 
             {
                 event.setTo(newLocation);
             } 
        	log("tried to fly on y-axis", player, CheckType.FLY); //cause he's poor.
        }
        if(checkManager.willCheck(player, CheckType.SPEED) && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && checkManager.willCheck(player, CheckType.FLY))
        {
            if(event.getFrom().getY() < event.getTo().getY() && backend.checkYSpeed(player, y))
            {
                event.setTo(from);
                log("tried to ascend to fast.",player,CheckType.SPEED);    
            }            
            if(backend.checkXZSpeed(player,x,z))
            {
                event.setTo(from);
                log("tried to move too fast.",player,CheckType.SPEED); 
            }
        }   
        if(checkManager.willCheck(player, CheckType.NOFALL) && checkManager.willCheck(player, CheckType.ZOMBE_FLY) && checkManager.willCheck(player, CheckType.FLY) && event.getFrom().getY() > event.getTo().getY() && backend.checkNoFall(player, y))
        {           
            event.setTo(from);
            log("tried avoid fall damage.",player,CheckType.NOFALL);   
        }
    }
    @EventHandler
    public void checkDirSpeed(PlayerMoveEvent event)
    {    
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();        
        Distance distance = new Distance(from, to);
        double x = distance.getXDifference();
        double y = distance.getYDifference();
        double z = distance.getZDifference();    
        if(checkManager.willCheck(player, CheckType.WATER_WALK) && backend.checkWaterWalk(player,x,z))
        {
            event.setTo(from);
            log("tried to walk on water.",player,CheckType.WATER_WALK);  
        }    
        if(checkManager.willCheck(player, CheckType.SNEAK) && backend.checkSneak(player,x,z))
        {
            event.setTo(from);
            player.setSneaking(false);
            log("tried to sneak too fast.",player,CheckType.SNEAK);    
        }   
        if(checkManager.willCheck(player, CheckType.SPIDER) && backend.checkSpider(player, y))
        {
            event.setTo(from);
            log("tried to climb a wall.",player,CheckType.SPIDER); 
        }  
    }
}
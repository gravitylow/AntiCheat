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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.h31ix.anticheat.Anticheat;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class Backend 
{
    public static final int ENTERED_EXTITED_TIME = 20;
    public static final int INSTANT_BREAK_TIME = 20;
    public static final int JOIN_TIME = 40;
    public static final int DROPPED_ITEM_TIME = 2;
    public static final int DAMAGE_TIME = 50;
    public static final int KNOCKBACK_DAMAGE_TIME = 50;
    
    private static final int CHAT_WARN_LEVEL = 7;
    private static final int CHAT_KICK_LEVEL = 10;
    private static final int CHAT_BAN_LEVEL = 3;   
    
    public static final int FLIGHT_LIMIT = 4;
    public static final int NOFALL_LIMIT = 5;
    
    public static final int SPRINT_FOOD_MIN = 6;
    public static final int EAT_MIN = 20;
    public static final int HEAL_MIN = 35;
    public static final int ANIMATION_MIN = 60;
    public static final int CHAT_MIN = 100;
    public static final int BOW_MIN = 2;
    public static final int SPRINT_MIN = 2;
    public static final int BLOCK_BREAK_MIN = 2;
    public static final long BLOCK_PLACE_MIN = 1/2;
    
    public static final double BLOCK_MAX_DISTANCE = 6.0;
    public static final double ENTITY_MAX_DISTANCE = 5.5;
    
    public static final double LADDER_Y_MAX = 0.11761;
    public static final double LADDER_Y_MIN = 0.11759;
    
    public static final double Y_SPEED_MAX = 0.5;
    public static final double XZ_SPEED_MAX = 0.4;
    public static final double XZ_SPEED_MAX_SPRINT = 0.7;
    public static final double XZ_SPEED_MAX_SNEAK = 0.2;
    public static final double XZ_SPEED_MAX_WATER = 0.19;
    public static final double XZ_SPEED_MAX_WATER_SPRINT = 0.3;
    
    private Anticheat plugin = Anticheat.getPlugin();
    private List<String> droppedItem = new ArrayList<String>();
    private List<String> animated = new ArrayList<String>();
    private List<String> movingExempt = new ArrayList<String>();
    private List<String> brokenBlock = new ArrayList<String>();
    private List<String> placedBlock = new ArrayList<String>();
    private List<String> bowWindUp = new ArrayList<String>();
    private List<String> startEat = new ArrayList<String>();
    private List<String> healed = new ArrayList<String>();
    private List<String> sprinted = new ArrayList<String>();
    private List<String> instantBreakExempt = new ArrayList<String>();
    private Map<String,Integer> flightViolation = new HashMap<String,Integer>();
    private Map<String,Integer> chatLevel = new HashMap<String,Integer>();
    private Map<String,Integer> chatKicks = new HashMap<String,Integer>();         
    private Map<String,Integer> nofallViolation = new HashMap<String,Integer>(); 
    
    public boolean checkLongReachBlock(double x,double y,double z)
    {
        return x >= BLOCK_MAX_DISTANCE || y > BLOCK_MAX_DISTANCE || z > BLOCK_MAX_DISTANCE;
    }
    
    public boolean checkLongReachDamage(double x,double y,double z)
    {
        return x >= ENTITY_MAX_DISTANCE || y > ENTITY_MAX_DISTANCE || z > ENTITY_MAX_DISTANCE;
    }    
    
    public boolean checkSpider(Player player,double y)
    {
        if(y <= LADDER_Y_MAX && y >= LADDER_Y_MIN && player.getLocation().getBlock().getType() != Material.VINE && player.getLocation().getBlock().getType() != Material.LADDER)
        {                           
            return true;
        }
        return false;
    }
    
    public boolean checkYSpeed(Player player,double y)
    {
        if(!player.isFlying() && player.getVehicle() == null && y > Y_SPEED_MAX)
        {
            return true;
        }
        return false;
    }
    
    public boolean checkNoFall(Player player, double y)
    { 
        String name = player.getName();
        if(player.getGameMode() != GameMode.CREATIVE && player.getVehicle() == null && !isMovingExempt(player))
        {                           
            if(player.getFallDistance() == 0)
            {
                if(nofallViolation.get(name) == null)
                {
                    nofallViolation.put(name,1);
                }
                else
                {
                    nofallViolation.put(name,nofallViolation.get(player.getName())+1);
                }

                if(nofallViolation.get(name) >= NOFALL_LIMIT)
                {
                    nofallViolation.put(player.getName(),1);
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                nofallViolation.put(name,0);
                return false;
            }            
        }
        return false;
    }
    
    public boolean checkXZSpeed(Player player,double x,double z)    
    {
        if(!isMovingExempt(player) && player.getVehicle() == null)
        {
            if(!player.isSprinting())
            {                      
                return x > XZ_SPEED_MAX || z > XZ_SPEED_MAX;            
            }
            else 
            {
                return x > XZ_SPEED_MAX_SPRINT || z > XZ_SPEED_MAX_SPRINT;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean checkSneak(Player player,double x,double z)
    {
        if(player.isSneaking())
        {
            return x > XZ_SPEED_MAX_SNEAK || z > XZ_SPEED_MAX_SNEAK;
        }
        else
        {
            return false;
        }
    }
    
    public boolean checkSprint(PlayerToggleSprintEvent event)
    {
        Player player = event.getPlayer();
        if(event.isSprinting())
        {
            return player.getFoodLevel() <= SPRINT_FOOD_MIN && player.getGameMode() != GameMode.CREATIVE;
        }
        else
        {
            return false;
        }
    }
    
    public boolean checkWaterWalk(Player player, double x, double z)
    {
        Block block = player.getLocation().getBlock();
         if(block.isLiquid() && player.getVehicle() == null)
         {
            if(x > XZ_SPEED_MAX_WATER || z > XZ_SPEED_MAX_WATER && !Utilities.sprintFly(player) && player.getNearbyEntities(1, 1, 1).isEmpty())
            {
                return true;
            }
            else if(x > XZ_SPEED_MAX_WATER_SPRINT || z > XZ_SPEED_MAX_WATER_SPRINT)
            {
                return true;
            }                
         }
         return false;
    }
    
    public boolean checkFlight(Player player, double y1, double y2)
    {
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if(y1 == y2 && !isMovingExempt(player) && player.getVehicle() == null && player.getFallDistance() == 0 && !Utilities.isOnLilyPad(player))
        {
            String name = player.getName();
            if(Utilities.cantStandAt(block) && !Utilities.isOnLilyPad(player) && !Utilities.canStand(player.getLocation().getBlock()))
            {
                int violation = 1;
                if(!flightViolation.containsKey(name))
                {
                    flightViolation.put(name, violation);
                }
                else
                {
                    violation = flightViolation.get(name)+1;
                    flightViolation.put(name, violation);
                }
                if(violation >= FLIGHT_LIMIT)
                {
                    flightViolation.put(name, 1);
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean checkSwing(Player player)
    {
        return !justAnimated(player);
    }
    
    public boolean checkFastBreak(Player player, Block block)
    {      
        if(player.getGameMode() != GameMode.CREATIVE && !player.getInventory().getItemInHand().containsEnchantment(Enchantment.DIG_SPEED) && !Utilities.isInstantBreak(block.getType()) && !isInstantBreakExempt(player) && !(player.getInventory().getItemInHand().getType() == Material.SHEARS && block.getType() == Material.LEAVES))
        {
            if (!justBroke(player))
            {
                logBlockBreak(player);
            }
            else
            {
                return true;
            }
        }
        return false;
    }     
    
    public void logBowWindUp(Player player)
    {
        logEvent(bowWindUp,player,BOW_MIN);
    }
    
    public boolean justWoundUp(Player player)
    {
        return bowWindUp.contains(player.getName());             
    }   
    
    public void logEatingStart(Player player)
    {
        logEvent(startEat,player,EAT_MIN);
    }
    
    public boolean justStartedEating(Player player)
    {
        return startEat.contains(player.getName());             
    }   
    
    public void logHeal(Player player)
    {
        logEvent(healed,player,HEAL_MIN);
    }
    
    public boolean justHealed(Player player)
    {
        return healed.contains(player.getName());             
    }     
    
    public void logChat(Player player)
    {
        String name = player.getName();
        if(chatLevel.get(name) == null)
        {
            logEvent(chatLevel,player,1,CHAT_MIN);
        }
        else
        {
            int amount = chatLevel.get(name)+1;
            chatLevel.put(name, amount);
            checkChatLevel(player, amount);  
        }
    }
    
    public void clearChatLevel(Player player)
    {
        chatLevel.remove(player.getName());
    } 
    
    public void logInstantBreak(final Player player)
    {
        logEvent(instantBreakExempt,player,INSTANT_BREAK_TIME);             
    }
    
    public boolean isInstantBreakExempt(Player player)
    {
        return instantBreakExempt.contains(player.getName());             
    }    
    
    public void logSprint(final Player player)
    {
        logEvent(sprinted,player,SPRINT_MIN);             
    }
    
    public boolean justSprinted(Player player)
    {
        return sprinted.contains(player.getName());             
    }     

    public void logBlockBreak(final Player player)
    {
        logEvent(brokenBlock,player,BLOCK_BREAK_MIN);             
    }
    
    public boolean justBroke(Player player)
    {
        return brokenBlock.contains(player.getName());             
    } 
    
    public void logBlockPlace(final Player player)
    {
        logEvent(placedBlock,player,BLOCK_PLACE_MIN);             
    }
    
    public boolean justPlaced(Player player)
    {
        return placedBlock.contains(player.getName());             
    }     
    
    public void logAnimation(final Player player)
    {
        logEvent(animated,player,ANIMATION_MIN);             
    }
    
    public void resetAnimation(final Player player)
    {
        animated.remove(player.getName());           
    }    
    
    public boolean justAnimated(Player player)
    {
        return animated.contains(player.getName());       
    }    
    
    public void logDamage(final Player player)
    {
        int time = DAMAGE_TIME;
        if(player.getInventory().getItemInHand().getEnchantments().containsKey(Enchantment.KNOCKBACK))
        {
            time = KNOCKBACK_DAMAGE_TIME;
        }   
        logEvent(movingExempt,player,time);  
    }
    
    public void logEnterExit(final Player player)
    {
        logEvent(movingExempt,player,ENTERED_EXTITED_TIME);             
    }
    
    public void logJoin(final Player player)
    {
        logEvent(movingExempt,player,JOIN_TIME);             
    }    
    
    public boolean isMovingExempt(Player player)
    {
        return movingExempt.contains(player.getName()) || player.isFlying();       
    }     
    
    public void logDroppedItem(final Player player)
    {
        logEvent(droppedItem,player,DROPPED_ITEM_TIME);  
    }
    
    public boolean justDroppedItem(Player player)
    {
        return droppedItem.contains(player.getName());     
    }
    
    private void logEvent(final List list, final Player player, long time)
    {
        list.add(player.getName());
        Anticheat.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Anticheat.getPlugin(), new Runnable() 
        {
            @Override
            public void run() 
            {
                list.remove(player.getName());
            }
        },      time);            
    }
    private void logEvent(final Map map, final Player player, final Object obj, long time)
    {
        map.put(player,obj);
        Anticheat.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Anticheat.getPlugin(), new Runnable() 
        {
            @Override
            public void run() 
            {
                map.remove(player);
            }
        },      time);            
    } 
    private void checkChatLevel(Player player, int amount)
    {
        if(amount >= CHAT_WARN_LEVEL)
        {
            player.sendMessage(ChatColor.RED+"Please stop flooding the server!");
        }
        if (amount >= CHAT_KICK_LEVEL)
        {
            String name = player.getName();
            int kick = 0;
            if(chatKicks.get(name) == null || chatKicks.get(name) == 0)
            {
                kick = 1;
                chatKicks.put(name, 1);
            }
            else
            {
                kick = (int)chatKicks.get(name)+1;
                chatKicks.put(name, kick);
            }
            
            if(chatKicks.get(name) <= CHAT_BAN_LEVEL)
            {
                player.kickPlayer(ChatColor.RED+"Spamming, kick "+kick+"/3");
                plugin.getServer().broadcastMessage(ChatColor.RED+player.getName()+" was kicked for spamming.");
            }
            else
            {
                player.kickPlayer(ChatColor.RED+"Banned for spamming.");
                player.setBanned(true);
                plugin.getServer().broadcastMessage(ChatColor.RED+player.getName()+" was banned for spamming.");
            }
        }
    }    
}

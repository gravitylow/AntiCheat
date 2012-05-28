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
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class Backend 
{
    public static final int ENTERED_EXTITED_TIME = 20;
    public static final int EXIT_FLY_TIME = 40;
    public static final int INSTANT_BREAK_TIME = 100;
    public static final int JOIN_TIME = 40;
    public static final int DROPPED_ITEM_TIME = 2;
    public static final int DAMAGE_TIME = 50;
    public static final int KNOCKBACK_DAMAGE_TIME = 50;
    
    public static final int FASTBREAK_LIMIT = 3;
    public static final int FASTBREAK_TIMEMAX = 500;
    public static final int FASTBREAK_MAXVIOLATIONS = 2;
    public static final int FASTBREAK_MAXVIOLATIONTIME = 10000;
    public static final int FASTPLACE_LIMIT = 2;
    public static final int FASTPLACE_ZEROLIMIT = 3;
    public static final int FASTPLACE_TIMEMAX = 80;
    public static final int FASTPLACE_MAXVIOLATIONS = 2;
    public static final int FASTPLACE_MAXVIOLATIONTIME = 10000;
    
    private static final int CHAT_WARN_LEVEL = 7;
    private static final int CHAT_KICK_LEVEL = 10;
    private static final int CHAT_BAN_LEVEL = 3;   
    
    public static final int FLIGHT_LIMIT = 4;
    public static final int FLIGHT_MINIMUM = 1;
    public static final int FLIGHT_TIMELIMIT = 5000;
    public static final int Y_MAXVIOLATIONS = 1;
    public static final int Y_MAXVIOTIME = 5000;
    public static final int NOFALL_LIMIT = 5;
    
    public static final int SPRINT_FOOD_MIN = 6;
    public static final int EAT_MIN = 20;
    public static final int HEAL_MIN = 35;
    public static final int ANIMATION_MIN = 60;
    public static final int CHAT_MIN = 100;
    public static final int BOW_MIN = 2;
    public static final int SPRINT_MIN = 2;
    public static final int BLOCK_BREAK_MIN = 1;
    public static final long BLOCK_PLACE_MIN = 1/3;
    
    public static final double BLOCK_MAX_DISTANCE = 6.0;
    public static final double ENTITY_MAX_DISTANCE = 5.5;
    
    public static final double LADDER_Y_MAX = 0.11761;
    public static final double LADDER_Y_MIN = 0.11759;
    
    public static final double Y_SPEED_MAX = 0.5;
    public static final double Y_MAXDIFF = 5;
    public static final double Y_TIME = 1000;
    public static final double XZ_SPEED_MAX = 0.4;
    public static final double XZ_SPEED_MAX_SPRINT = 0.65;
    public static final double XZ_SPEED_MAX_FLY = 0.56;
    public static final double XZ_SPEED_MAX_SNEAK = 0.2;
    public static final double XZ_SPEED_MAX_WATER = 0.19;
    public static final double XZ_SPEED_MAX_WATER_SPRINT = 0.3;
    
    private AnticheatManager micromanage = null;
    private List<String> droppedItem = new ArrayList<String>();
    private List<String> animated = new ArrayList<String>();
    private List<String> movingExempt = new ArrayList<String>();
    private List<String> brokenBlock = new ArrayList<String>();
    private List<String> placedBlock = new ArrayList<String>();
    private List<String> bowWindUp = new ArrayList<String>();
    private List<String> startEat = new ArrayList<String>();
    private List<String> healed = new ArrayList<String>();
    private List<String> sprinted = new ArrayList<String>();
    private List<String> isInWater = new ArrayList<String>();
    private List<String> isInWaterCache = new ArrayList<String>();
    private List<String> instantBreakExempt = new ArrayList<String>();
    private Map<String,String> oldMessage = new HashMap<String,String>();
    private Map<String,String> lastMessage = new HashMap<String,String>();
    private Map<String,Integer> flightViolation = new HashMap<String,Integer>();
    private Map<String,Integer> chatLevel = new HashMap<String,Integer>();
    private Map<String,Integer> chatKicks = new HashMap<String,Integer>();         
    private Map<String,Integer> nofallViolation = new HashMap<String,Integer>(); 
    private Map<String,Integer> fastBreakViolation = new HashMap<String,Integer>();
    private Map<String,Integer> YaxisViolations = new HashMap<String,Integer>();
    private Map<String,Long> YaxisLastVio = new HashMap<String,Long>();
    private Map<String,Double> lastYcoord = new HashMap<String,Double>();
    private Map<String,Long> lastYtime = new HashMap<String,Long>();
    private Map<String,Integer> blocksBroken = new HashMap<String,Integer>();
    private Map<String,Long> lastBlockBroken = new HashMap<String,Long>();
    private Map<String,Integer> fastPlaceViolation = new HashMap<String,Integer>();
    private Map<String,Integer> lastZeroHitPlace = new HashMap<String,Integer>();
    private Map<String,Long> lastBlockPlaced = new HashMap<String,Long>();    
    private Map<String,Long> lastBlockPlaceTime = new HashMap<String,Long>(); 
    private Map<String,Integer> blockPunches = new HashMap<String,Integer>();
    
    public Backend(AnticheatManager instance) 
    {
    	micromanage = instance;
    }
    
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
        if(/*!player.isFlying() &&*/ player.getVehicle() == null && y > Y_SPEED_MAX)
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
        if(!isSpeedExempt(player) && player.getVehicle() == null)
        {
            if(player.isFlying())
            {
                return x > XZ_SPEED_MAX_FLY || z > XZ_SPEED_MAX_FLY; 
            }
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
        if(player.isSneaking() && !player.isFlying())
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
            if(isInWater.contains(player.getName()))
            {
                if(isInWaterCache.contains(player.getName()))
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
                else
                {
                    isInWaterCache.add(player.getName());
                    return false;
                }
            }
            else
            {
                isInWater.add(player.getName());
                return false;
            }

        }
        isInWater.remove(player.getName());  
        isInWaterCache.remove(player.getName());  
        return false;
    }
    
    public boolean checkYAxis(Player player) 
    {
        if(!player.isFlying())
        {
            double y1 = player.getLocation().getY();
            String name = player.getName();
            //Fix Y axis spam.
            if(!lastYcoord.containsKey(name) || !lastYtime.containsKey(name) || !YaxisViolations.containsKey(name) || !YaxisLastVio.containsKey(name))
            {
                lastYcoord.put(name, y1);
                YaxisViolations.put(name, 0);
                YaxisLastVio.put(name, 0L);
                lastYtime.put(name, System.currentTimeMillis());
            } 
            else 
            {
                if(y1 > lastYcoord.get(name) && YaxisViolations.get(name) > Y_MAXVIOLATIONS && (System.currentTimeMillis() - YaxisLastVio.get(name)) < Y_MAXVIOTIME) 
                {
                    Location g = player.getLocation();
                    g.setY(lastYcoord.get(name));
                    player.sendMessage(ChatColor.RED + "[AntiCheat] Fly hacking on the y-axis detected.  Please wait 5 seconds to prevent getting damage.");
                    YaxisViolations.put(name, YaxisViolations.get(name)+1);
                    YaxisLastVio.put(name, System.currentTimeMillis());
                    if(g.getBlock().getTypeId() == 0) 
                    {
                            player.teleport(g);
                    }
                    return true;
                } 
                else 
                {
                    if(YaxisViolations.get(name) > Y_MAXVIOLATIONS && (System.currentTimeMillis() - YaxisLastVio.get(name)) > Y_MAXVIOTIME) 
                    {
                        YaxisViolations.put(name, 0);
                        YaxisLastVio.put(name, 0L);
                    }
                }
                if((y1 - lastYcoord.get(name)) > Y_MAXDIFF && (System.currentTimeMillis() - lastYtime.get(name)) < Y_TIME) 
                {
                    Location g = player.getLocation();
                    g.setY(lastYcoord.get(name));
                    YaxisViolations.put(name, YaxisViolations.get(name)+1);
                    YaxisLastVio.put(name, System.currentTimeMillis());
                    if(g.getBlock().getTypeId() == 0) 
                    {
                            player.teleport(g);
                    }
                    return true;
                }
                else
                {
                    if((y1 - lastYcoord.get(name)) > Y_MAXDIFF + 1 || (System.currentTimeMillis() - lastYtime.get(name)) > Y_TIME) 
                    {
                        lastYtime.put(name, System.currentTimeMillis());
                        lastYcoord.put(name, y1);
                    }
                }
            }
        }
    	//Fix Y axis spam
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
                //Start Fly bypass patch.
                if(flightViolation.containsKey(name) && flightViolation.get(name) > 0) 
                {
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
                }
                //End Fly bypass patch.
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
        String name = player.getName();
        if(!player.getInventory().getItemInHand().containsEnchantment(Enchantment.DIG_SPEED) && !Utilities.isInstantBreak(block.getType()) && !isInstantBreakExempt(player) && !(player.getInventory().getItemInHand().getType() == Material.SHEARS && block.getType() == Material.LEAVES && player.getGameMode() != GameMode.CREATIVE))
        {
            if(blockPunches.get(name) != null)
            {
                int i = blockPunches.get(name);
                if(i < 5)
                {
                    return true;
                }
            }
            if (!fastBreakViolation.containsKey(name))
            {
                fastBreakViolation.put(name, 0);
            } 
            else 
            {
                Long math = System.currentTimeMillis() - lastBlockBroken.get(name);
                if(fastBreakViolation.get(name) > FASTBREAK_MAXVIOLATIONS && math < FASTBREAK_MAXVIOLATIONTIME)
                {
                    lastBlockBroken.put(name, System.currentTimeMillis());
                    player.sendMessage(ChatColor.RED + "[AntiCheat] Fastbreaking detected. Please wait 10 seconds before breaking blocks.");
                    return true;
                } 
                else if(fastBreakViolation.get(name) > 0 && math > FASTBREAK_MAXVIOLATIONTIME)
                {
                    fastBreakViolation.put(name, 0);
                } 
            }
            if (!blocksBroken.containsKey(name) || !lastBlockBroken.containsKey(name))
            {
                if(!lastBlockBroken.containsKey(name))
                {
                    lastBlockBroken.put(name, System.currentTimeMillis());
                }
                blocksBroken.put(name, 0);
            }
            else
            {
                blocksBroken.put(name, blocksBroken.get(name)+1);
                Long math = System.currentTimeMillis() - lastBlockBroken.get(name);
                if(blocksBroken.get(name) > FASTBREAK_LIMIT && math < FASTBREAK_TIMEMAX)
                {
                    blocksBroken.put(name, 0);
                    lastBlockBroken.put(name, System.currentTimeMillis());
                    fastBreakViolation.put(name, fastBreakViolation.get(name)+1);
                    return true;
                }
                else if(blocksBroken.get(name) > FASTBREAK_LIMIT)
                {
                    lastBlockBroken.put(name, System.currentTimeMillis());
                    blocksBroken.put(name, 0);
                }
            }
        }
        return false;
    }
    
    public boolean checkFastPlace(Player player)
    {    
        long time = System.currentTimeMillis();
        String name = player.getName();
        if(!lastBlockPlaceTime.containsKey(name) || !fastPlaceViolation.containsKey(name))
        {
            lastBlockPlaceTime.put(name, Long.parseLong("0"));
            if(!fastPlaceViolation.containsKey(name))
            {
            	fastPlaceViolation.put(name, 0);
            }
        }
        else if(fastPlaceViolation.containsKey(name) && fastPlaceViolation.get(name) > FASTPLACE_MAXVIOLATIONS)
        {
            Long math = System.currentTimeMillis() - lastBlockPlaced.get(name);
            if(lastBlockPlaced.get(name) > 0 && math < FASTPLACE_MAXVIOLATIONTIME)
            {
                lastBlockPlaced.put(name, time);
                player.sendMessage(ChatColor.RED + "[AntiCheat] Fastplacing detected. Please wait 10 seconds before placing blocks.");
                return true;
            } 
            else if (lastBlockPlaced.get(name) > 0 && math > FASTPLACE_MAXVIOLATIONTIME)
            {
                fastPlaceViolation.put(name, 0);
            }
        }
        else if(lastBlockPlaced.containsKey(name))
        {
            long last = lastBlockPlaced.get(name);
            long lastTime = lastBlockPlaceTime.get(name);
            long thisTime = time-last;
            boolean nocheck = thisTime < 1 || lastTime < 1;
            if(!lastZeroHitPlace.containsKey(name))
            {
                lastZeroHitPlace.put(name, 0);
            }
            if(nocheck)
            {
                if(!lastZeroHitPlace.containsKey(name))
                {
                    lastZeroHitPlace.put(name, 1);
                }
                else
                {
                    lastZeroHitPlace.put(name, lastZeroHitPlace.get(name)+1);
                }
            }
            if(thisTime < FASTPLACE_TIMEMAX && lastTime < FASTPLACE_TIMEMAX
            && nocheck && lastZeroHitPlace.get(name) > FASTPLACE_ZEROLIMIT)
            {
                lastBlockPlaceTime.put(name, (time-last));
                lastBlockPlaced.put(name, time);
                fastPlaceViolation.put(name, fastPlaceViolation.get(name)+1);
                return true;
            }
            lastBlockPlaceTime.put(name, (time-last));
        }
        lastBlockPlaced.put(name, time); 
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
    
    public boolean checkSpam(Player player, String msg)
    {
        String name = player.getName();
        if(lastMessage.get(name) == null)
        {
            lastMessage.put(name, msg);
        }  
        else
        {
            if(oldMessage.get(name) != null && lastMessage.get(name).equals(msg) && oldMessage.get(name).equals(msg))
            {
                return true;
            }
            else
            {
                oldMessage.put(name, lastMessage.get(name));
                lastMessage.put(name, msg);
                return false;
            }
        }
        return false;
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
        resetAnimation(player);
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
        increment(player,blockPunches);
    }
    
    public void resetAnimation(final Player player)
    {
        animated.remove(player.getName());  
        blockPunches.put(player.getName(), 0);
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
    
    public void logExitFly(final Player player)
    {
        logEvent(movingExempt,player,EXIT_FLY_TIME);             
    }    
    
    public void logJoin(final Player player)
    {
        logEvent(movingExempt,player,JOIN_TIME);             
    }    
    
    public boolean isMovingExempt(Player player)
    {
        return movingExempt.contains(player.getName()) || player.isFlying();       
    }
    
    public boolean isSpeedExempt(Player player) {
    	return movingExempt.contains(player.getName());
    }
    
    public void logDroppedItem(final Player player)
    {
        logEvent(droppedItem,player,DROPPED_ITEM_TIME);  
    }
    
    public boolean justDroppedItem(Player player)
    {
        return droppedItem.contains(player.getName());     
    }   
    
    @SuppressWarnings("unchecked")
    private void logEvent(@SuppressWarnings("rawtypes") final List list, final Player player, long time)
    {
        list.add(player.getName());
        micromanage.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(micromanage.getPlugin(), new Runnable() 
        {
            @Override
            public void run() 
            {
                list.remove(player.getName());
            }
        },      time);            
    }
    
    @SuppressWarnings("unchecked")
    private void logEvent(@SuppressWarnings("rawtypes") final Map map, final Player player, final Object obj, long time)
    {
        map.put(player,obj);
        micromanage.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(micromanage.getPlugin(), new Runnable() 
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
                micromanage.getPlugin().getServer().broadcastMessage(ChatColor.RED+player.getName()+" was kicked for spamming.");
            }
            else
            {
                player.kickPlayer(ChatColor.RED+"Banned for spamming.");
                player.setBanned(true);
                micromanage.getPlugin().getServer().broadcastMessage(ChatColor.RED+player.getName()+" was banned for spamming.");
            }
        }
    } 
    public void increment(Player player, Map<String,Integer> map)
    { 
        String name = player.getName();
        if(map.get(name) == null)
        {
            map.put(name, 1);
        }
        else
        {
            int amount = map.get(name)+1;
            if(amount < 6)
            {
                map.put(name, amount);
            }
            else
            {
                map.put(name, 5);
            }
        }        
    }
}
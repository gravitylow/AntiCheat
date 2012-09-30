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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.h31ix.anticheat.util.Distance;
import net.h31ix.anticheat.util.Magic;
import net.h31ix.anticheat.util.Utilities;
import net.h31ix.anticheat.util.yaml.CommentedConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffectType;

public class Backend
{
    private List<String> isInWater = new ArrayList<String>();
    private List<String> isInWaterCache = new ArrayList<String>();
    private List<String> isAscending = new ArrayList<String>();
    private Map<String, Integer> ascensionCount = new HashMap<String, Integer>();
    private Map<String, String> oldMessage = new HashMap<String, String>();
    private Map<String, String> lastMessage = new HashMap<String, String>();
    private Map<String, Integer> flightViolation = new HashMap<String, Integer>();
    private Map<String, Integer> chatLevel = new HashMap<String, Integer>();
    private Map<String, Integer> chatKicks = new HashMap<String, Integer>();
    private Map<String, Integer> nofallViolation = new HashMap<String, Integer>();
    private Map<String, Integer> speedViolation = new HashMap<String, Integer>();
    private Map<String, Integer> fastBreakViolation = new HashMap<String, Integer>();
    private Map<String, Integer> yAxisViolations = new HashMap<String, Integer>();
    private Map<String, Long> yAxisLastViolation = new HashMap<String, Long>();
    private Map<String, Double> lastYcoord = new HashMap<String, Double>();
    private Map<String, Long> lastYtime = new HashMap<String, Long>();
    private Map<String, Integer> blocksBroken = new HashMap<String, Integer>();
    private Map<String, Long> lastBlockBroken = new HashMap<String, Long>();
    private Map<String, Integer> fastPlaceViolation = new HashMap<String, Integer>();
    private Map<String, Integer> lastZeroHitPlace = new HashMap<String, Integer>();
    private Map<String, Long> lastBlockPlaced = new HashMap<String, Long>();
    private Map<String, Long> lastBlockPlaceTime = new HashMap<String, Long>();
    private Map<String, Integer> blockPunches = new HashMap<String, Integer>();
    private Map<String, Integer> waterAscensionViolation = new HashMap<String, Integer>();
    private Map<String, Integer> waterSpeedViolation = new HashMap<String, Integer>();
    private Map<String, Integer> projectilesShot = new HashMap<String, Integer>();
    private Map<String, Long> velocitized = new HashMap<String, Long>();
    private Map<String, Integer> velocitytrack = new HashMap<String, Integer>();
    private Map<String, Long> animated = new HashMap<String, Long>();
    private Map<String, Long> startEat = new HashMap<String, Long>();
    private Map<String, Long> lastHeal = new HashMap<String, Long>();
    private Map<String, Long> projectileTime = new HashMap<String, Long>();
    private Map<String, Long> bowWindUp = new HashMap<String, Long>();
    private Map<String, Long> instantBreakExempt = new HashMap<String, Long>();
    private Map<String, Long> sprinted = new HashMap<String, Long>();
    private Map<String, Long> brokenBlock = new HashMap<String, Long>();
    private Map<String, Long> placedBlock = new HashMap<String, Long>();
    private Map<String, Long> movingExempt = new HashMap<String, Long>();
    private Map<String, Long> blockTime = new HashMap<String, Long>();
    private Map<String, Integer> blocksDropped = new HashMap<String, Integer>();
    
    private Magic magic;
    private AnticheatManager micromanage = null;

    public Backend(AnticheatManager instance)
    {
        magic = new Magic(instance.getConfiguration().getMagic(), instance.getConfiguration(), CommentedConfiguration.loadConfiguration(instance.getPlugin().getResource("magic.yml")));
        micromanage = instance;
    }

    public void buildAdvancedInformation(StringBuilder r)
    {
        //r.append("Dropped Items List:" + '\n');
        //for (String l : droppedItem.keySet())
        {
            //r.append(l + '\n');
        }
        r.append("Moving Exempt List:" + '\n');
        for (String l : movingExempt.keySet())
        {
            r.append(l + '\n');
        }
        r.append("Broken Block List:" + '\n');
        for (String l : brokenBlock.keySet())
        {
            r.append(l + '\n');
        }
        r.append("Placed Block List:" + '\n');
        for (String l : placedBlock.keySet())
        {
            r.append(l + '\n');
        }
        r.append("Bow Wind Up List:" + '\n');
        for (String l : bowWindUp.keySet())
        {
            r.append(l + '\n');
        }
        r.append("Start Eating List:" + '\n');
        for (String l : startEat.keySet())
        {
            r.append(l + '\n');
        }
        r.append("Healed List:" + '\n');
        for (String l : lastHeal.keySet())
        {
            r.append(l + '\n');
        }
        r.append("Sprinted List:" + '\n');
        for (String l : sprinted.keySet())
        {
            r.append(l + '\n');
        }
        r.append("Is In Water List:" + '\n');
        for (String l : isInWater)
        {
            r.append(l + '\n');
        }
        r.append("Is In Water (cached) List:" + '\n');
        for (String l : isInWaterCache)
        {
            r.append(l + '\n');
        }
        r.append("Instant Break Exempt List:" + '\n');
        for (String l : instantBreakExempt.keySet())
        {
            r.append(l + '\n');
        }
        r.append("Is Ascending List:" + '\n');
        for (String l : isAscending)
        {
            r.append(l + '\n');
        }
    }

    public void garbageClean(Player player)
    {
        String pN = player.getName();
        User user = micromanage.getUserManager().getUser(pN);
        if(user != null)
        {
            micromanage.getUserManager().remove(user);
        }
        //droppedItem.remove(pN);
        movingExempt.remove(pN);
        brokenBlock.remove(pN);
        placedBlock.remove(pN);
        bowWindUp.remove(pN);
        startEat.remove(pN);
        lastHeal.remove(pN);
        sprinted.remove(pN);
        isInWater.remove(pN);
        isInWaterCache.remove(pN);
        instantBreakExempt.remove(pN);
        isAscending.remove(pN);
        ascensionCount.remove(pN);
        oldMessage.remove(pN);
        lastMessage.remove(pN);
        flightViolation.remove(pN);
        chatLevel.remove(pN);
        chatKicks.remove(pN);
        nofallViolation.remove(pN);
        fastBreakViolation.remove(pN);
        yAxisViolations.remove(pN);
        yAxisLastViolation.remove(pN);
        lastYcoord.remove(pN);
        lastYtime.remove(pN);
        blocksBroken.remove(pN);
        lastBlockBroken.remove(pN);
        fastPlaceViolation.remove(pN);
        lastZeroHitPlace.remove(pN);
        lastBlockPlaced.remove(pN);
        lastBlockPlaceTime.remove(pN);
        blockPunches.remove(pN);
        waterAscensionViolation.remove(pN);
        waterSpeedViolation.remove(pN);
        projectilesShot.remove(pN);
        velocitized.remove(pN);
        velocitytrack.remove(pN);
        animated.remove(pN);
    }
    
    public boolean checkFastBow(Player player, float force)
    {
        int ticks = (int)((((System.currentTimeMillis()-bowWindUp.get(player.getName()))*20)/1000)+3);
        bowWindUp.remove(player.getName());
        float f = (float) ticks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        f = f > 1.0F ? 1.0F : f;   
        return Math.abs(force-f) > magic.BOW_ERROR;
    }    
    
    public boolean checkProjectile(Player player)
    {
        increment(player, projectilesShot, 10);
        if (!projectileTime.containsKey(player.getName()))
        {
            projectileTime.put(player.getName(), System.currentTimeMillis());
            return false;
        }
        else if(projectilesShot.get(player.getName()) == magic.PROJECTILE_CHECK)
        {
            long time = System.currentTimeMillis()-projectileTime.get(player.getName());
            projectileTime.remove(player.getName());
            projectilesShot.remove(player.getName());
            return time < magic.PROJECTILE_TIME_MIN;
        }
        return false;
    } 
    
    public boolean checkFastDrop(Player player)
    {
        increment(player, blocksDropped, 10);
        if (!blockTime.containsKey(player.getName()))
        {
            blockTime.put(player.getName(), System.currentTimeMillis());
            return false;
        }
        else if(blocksDropped.get(player.getName()) == magic.DROP_CHECK)
        {
            long time = System.currentTimeMillis()-blockTime.get(player.getName());
            blockTime.remove(player.getName());
            blocksDropped.remove(player.getName());
            return time < magic.DROP_TIME_MIN;
        }
        return false;        
    }

    public boolean checkLongReachBlock(Player player, double x, double y, double z)
    {
        return (x >= magic.BLOCK_MAX_DISTANCE || y > magic.BLOCK_MAX_DISTANCE || z > magic.BLOCK_MAX_DISTANCE);
    }

    public boolean checkLongReachDamage(double x, double y, double z)
    {
        return x >= magic.ENTITY_MAX_DISTANCE || y > magic.ENTITY_MAX_DISTANCE || z > magic.ENTITY_MAX_DISTANCE;
    }

    public boolean checkSpider(Player player, double y)
    {
        if (y <= magic.LADDER_Y_MAX && y >= magic.LADDER_Y_MIN && !Utilities.isClimbableBlock(player.getLocation().getBlock()))
        {
            return true;
        }
        return false;
    }

    public boolean checkYSpeed(Player player, double y)
    {
        if (!player.isInsideVehicle() && y > magic.Y_SPEED_MAX && !isDoing(player, velocitized, magic.VELOCITY_TIME) && !player.hasPotionEffect(PotionEffectType.JUMP))
        {
            return true;
        }
        return false;
    }

    public boolean checkNoFall(Player player, double y)
    {
        String name = player.getName();
        if (player.getGameMode() != GameMode.CREATIVE && player.getVehicle() == null && !isMovingExempt(player) && !Utilities.isInWater(player))
        {
            if (player.getFallDistance() == 0)
            {
                if (nofallViolation.get(name) == null)
                {
                    nofallViolation.put(name, 1);
                }
                else
                {
                    nofallViolation.put(name, nofallViolation.get(player.getName()) + 1);
                }

                if (nofallViolation.get(name) >= magic.NOFALL_LIMIT)
                {
                    nofallViolation.put(player.getName(), 1);
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                nofallViolation.put(name, 0);
                return false;
            }
        }
        return false;
    }

    public boolean checkXZSpeed(Player player, double x, double z)
    {
        if (!isSpeedExempt(player) && player.getVehicle() == null)
        {
            boolean speed = false;
            if (player.isFlying())
            {              
                speed = x > magic.XZ_SPEED_MAX_FLY || z > magic.XZ_SPEED_MAX_FLY;
            }
            else if (player.hasPotionEffect(PotionEffectType.SPEED))
            {             
                speed = x > magic.XZ_SPEED_MAX_POTION || z > magic.XZ_SPEED_MAX_POTION;
            }
            else if (player.isSprinting())
            {                                 
                speed = x > magic.XZ_SPEED_MAX_SPRINT || z > magic.XZ_SPEED_MAX_SPRINT;
            }
            else
            {
                speed = x > magic.XZ_SPEED_MAX || z > magic.XZ_SPEED_MAX;
            }
            if(speed)
            {
                int num = this.increment(player, speedViolation, magic.SPEED_MAX);
                if(num >= magic.SPEED_MAX)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                speedViolation.put(player.getName(), 0);
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean checkSneak(Player player, double x, double z)
    {
        if (player.isSneaking() && !player.isFlying() && !isMovingExempt(player))
        {
            return x > magic.XZ_SPEED_MAX_SNEAK || z > magic.XZ_SPEED_MAX_SNEAK;
        }
        else
        {
            return false;
        }
    }

    public boolean checkSprint(PlayerToggleSprintEvent event)
    {
        Player player = event.getPlayer();
        if (event.isSprinting())
        {
            return player.getFoodLevel() <= magic.SPRINT_FOOD_MIN && player.getGameMode() != GameMode.CREATIVE;
        }
        else
        {
            return false;
        }
    }

    public boolean checkWaterWalk(Player player, double x, double z)
    {
        Block block = player.getLocation().getBlock();
        if (player.getVehicle() == null && !player.isFlying())
        {
            if (block.isLiquid())
            {
                if (isInWater.contains(player.getName()))
                {
                    if (isInWaterCache.contains(player.getName()))
                    {
                        if (player.getNearbyEntities(1, 1, 1).isEmpty())
                        {
                            boolean b = false;
                            if (!Utilities.sprintFly(player) && x > magic.XZ_SPEED_MAX_WATER || z > magic.XZ_SPEED_MAX_WATER)
                            {
                                b = true;
                            }
                            else if (x > magic.XZ_SPEED_MAX_WATER_SPRINT || z > magic.XZ_SPEED_MAX_WATER_SPRINT)
                            {
                                b = true;
                            }
                            if (b)
                            {
                                if (waterSpeedViolation.containsKey(player.getName()))
                                {
                                    int v = waterSpeedViolation.get(player.getName());
                                    if (v >= magic.WATER_SPEED_VIOLATION_MAX)
                                    {
                                        waterSpeedViolation.put(player.getName(), 0);
                                        return true;
                                    }
                                    else
                                    {
                                        waterSpeedViolation.put(player.getName(), v + 1);
                                    }
                                }
                                else
                                {
                                    waterSpeedViolation.put(player.getName(), 1);
                                }
                            }
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
            else if (block.getRelative(BlockFace.DOWN).isLiquid() && isAscending(player) && Utilities.cantStandAt(block) && Utilities.cantStandAt(block.getRelative(BlockFace.DOWN)))
            {
                if (waterAscensionViolation.containsKey(player.getName()))
                {
                    int v = waterAscensionViolation.get(player.getName());
                    if (v >= magic.WATER_ASCENSION_VIOLATION_MAX)
                    {
                        waterAscensionViolation.put(player.getName(), 0);
                        return true;
                    }
                    else
                    {
                        waterAscensionViolation.put(player.getName(), v + 1);
                    }
                }
                else
                {
                    waterAscensionViolation.put(player.getName(), 1);
                }
            }
            else
            {
                isInWater.remove(player.getName());
                isInWaterCache.remove(player.getName());
            }
        }
        return false;
    }

    // The first check in Anticheat with a integer as a result :O!

    public int checkVClip(Player player, Distance distance)
    {
        double from = Math.round(distance.fromY());
        double to = Math.round(distance.toY());

        boolean bs = false;

        if (player.isInsideVehicle())
        {
            return 0;
        }
        if (from == to || from < to)
        {    
            return 0;
        }
        if (Math.round(distance.getYDifference()) < 2)
        {
            return 0;
        }

        for (int i = 0; i < (Math.round(distance.getYDifference())) + 1; i++)
        {
            Location l = new Location(player.getWorld(), player.getLocation().getX(), to + i, player.getLocation().getZ());
            if (l.getBlock().getTypeId() != 0 && net.minecraft.server.Block.byId[l.getBlock().getTypeId()].material.isSolid() && !l.getBlock().isLiquid())
            {
                bs = true;
            }
            if (bs)
            {
                return (int) from + 3;
            }
        }

        return 0;
    }

    public boolean checkYAxis(Player player, Distance distance)
    {
        if (distance.getYDifference() > 400 || distance.getYDifference() < 0)
        {
            return false;
        }
        if (!isMovingExempt(player) && !Utilities.isClimbableBlock(player.getLocation().getBlock()) && !Utilities.isClimbableBlock(player.getLocation().add(0, -1, 0).getBlock()) && !player.isInsideVehicle() && !Utilities.isInWater(player))
        {
            double y1 = player.getLocation().getY();
            String name = player.getName();
            //Fix Y axis spam.
            if (!lastYcoord.containsKey(name) || !lastYtime.containsKey(name) || !yAxisLastViolation.containsKey(name) || !yAxisLastViolation.containsKey(name))
            {
                lastYcoord.put(name, y1);
                yAxisViolations.put(name, 0);
                yAxisLastViolation.put(name, 0L);
                lastYtime.put(name, System.currentTimeMillis());
            }
            else
            {
                if (y1 > lastYcoord.get(name) && yAxisViolations.get(name) > magic.Y_MAXVIOLATIONS && (System.currentTimeMillis() - yAxisLastViolation.get(name)) < magic.Y_MAXVIOTIME)
                {
                    Location g = player.getLocation();
                    g.setY(lastYcoord.get(name));
                    player.sendMessage(ChatColor.RED + "[AntiCheat] Fly hacking on the y-axis detected.  Please wait 5 seconds to prevent getting damage.");
                    yAxisViolations.put(name, yAxisViolations.get(name) + 1);
                    yAxisLastViolation.put(name, System.currentTimeMillis());
                    if (g.getBlock().getTypeId() == 0)
                    {
                        player.teleport(g);
                    }
                    return true;
                }
                else
                {
                    if (yAxisViolations.get(name) > magic.Y_MAXVIOLATIONS && (System.currentTimeMillis() - yAxisLastViolation.get(name)) > magic.Y_MAXVIOTIME)
                    {
                        yAxisViolations.put(name, 0);
                        yAxisLastViolation.put(name, 0L);
                    }
                }
                if ((y1 - lastYcoord.get(name)) > magic.Y_MAXDIFF && (System.currentTimeMillis() - lastYtime.get(name)) < magic.Y_TIME)
                {
                    Location g = player.getLocation();
                    g.setY(lastYcoord.get(name));
                    yAxisViolations.put(name, yAxisViolations.get(name) + 1);
                    yAxisLastViolation.put(name, System.currentTimeMillis());
                    if (g.getBlock().getTypeId() == 0)
                    {
                        player.teleport(g);
                    }
                    return true;
                }
                else
                {
                    if ((y1 - lastYcoord.get(name)) > magic.Y_MAXDIFF + 1 || (System.currentTimeMillis() - lastYtime.get(name)) > magic.Y_TIME)
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
    
    public boolean checkTimer(Player player)
    {
    /*
        String name = player.getName();
        int step = 1;
        if(steps.containsKey(name))
        {
            step = steps.get(name)+1;
        }
        if(step == 1)
        {
            lastTime.put(name,System.currentTimeMillis());
        }
        increment(player, steps, step);
        if(step >= STEP_CHECK)
        {
            long time = System.currentTimeMillis()-lastTime.get(name);
            steps.put(name, 0);
            if(time < TIME_MIN)
            {
                return true;
            }
        }*/
        return false;
    }
    
    public boolean checkSight(Player player, Entity entity)
    {
        // Check to make sure the entity's head is not surrounded
        Block head = entity.getLocation().add(0, 1, 0).getBlock();
        boolean solid = false;
        if(head.getRelative(BlockFace.NORTH).getTypeId() != 0)
        {
            solid = net.minecraft.server.Block.byId[head.getRelative(BlockFace.NORTH).getTypeId()].material.isSolid();
        }
        if(!solid)
        {
            if(head.getRelative(BlockFace.SOUTH).getTypeId() != 0)
            {
                solid = net.minecraft.server.Block.byId[head.getRelative(BlockFace.SOUTH).getTypeId()].material.isSolid();
            }            
        }
        if(!solid)
        {
            if(head.getRelative(BlockFace.EAST).getTypeId() != 0)
            {
                solid = net.minecraft.server.Block.byId[head.getRelative(BlockFace.EAST).getTypeId()].material.isSolid();
            }            
        }
        if(!solid)
        {
            if(head.getRelative(BlockFace.WEST).getTypeId() != 0)
            {
                solid = net.minecraft.server.Block.byId[head.getRelative(BlockFace.WEST).getTypeId()].material.isSolid();
            }            
        }   
        return solid ? true : player.hasLineOfSight(entity);
    }

    public boolean checkFlight(Player player, Distance distance)
    {
        if (distance.getYDifference() > 400)
        {
            //This was a teleport, so we don't care about it.
            return false;
        }
        String name = player.getName();
        int y1 = (int)distance.fromY();
        int y2 = (int)distance.toY();
        if(!isMovingExempt(player) && !Utilities.isInWater(player) && !Utilities.canStand(player.getLocation().getBlock()) && Utilities.cantStandAt(player.getLocation().add(0, -1, 0).getBlock()) && Utilities.cantStandAt(player.getLocation().add(0, -2, 0).getBlock()))
        {
            Block block = player.getLocation().getBlock();
            //Check if the player is climbing up water. 
            if(block.getRelative(BlockFace.NORTH).isLiquid() || block.getRelative(BlockFace.SOUTH).isLiquid() || block.getRelative(BlockFace.EAST).isLiquid() || block.getRelative(BlockFace.WEST).isLiquid())
            {
                if(y1 < y2)
                {
                    //Even if they are using a hacked client and flying next to water to abuse this, they can't be go past the speed limit so they might as well swim
                    return distance.getYDifference() > magic.WATER_CLIMB_MAX;
                }
            }
            else if(y1 == y2 || y1 < y2)
            {
                //Check if the player is crouching on slabs
                if(player.isSneaking())
                {
                    if(block.getRelative(BlockFace.NORTH).getTypeId() == 43 || block.getRelative(BlockFace.NORTH).getTypeId() == 44 || block.getRelative(BlockFace.SOUTH).getTypeId() == 43 || block.getRelative(BlockFace.SOUTH).getTypeId() == 44 || block.getRelative(BlockFace.EAST).getTypeId() == 43 || block.getRelative(BlockFace.EAST).getTypeId() == 44 || block.getRelative(BlockFace.WEST).getTypeId() == 43 || block.getRelative(BlockFace.WEST).getTypeId() == 44)
                    {
                        return false;
                    }
                }
                int violation = flightViolation.containsKey(name) ? flightViolation.get(name)+1 : 1;
                increment(player, flightViolation, violation);
                if(violation > magic.FLIGHT_LIMIT)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        flightViolation.put(name, 0);
        return false;
    }

    public void logAscension(Player player, double y1, double y2)
    {
        String name = player.getName();
        if (y1 < y2 && !isAscending.contains(name))
        {
            isAscending.add(name);
        }
        else
        {
            isAscending.remove(player.getName());
        }
    }

    public boolean checkAscension(Player player, double y1, double y2)
    {
        int max = magic.ASCENSION_COUNT_MAX;
        if (player.hasPotionEffect(PotionEffectType.JUMP))
        {
            max += 12;
        }
        Block block = player.getLocation().getBlock();
        if (!isMovingExempt(player) && !Utilities.isInWater(player) && !Utilities.isClimbableBlock(player.getLocation().getBlock()) && !player.isInsideVehicle())
        {
            String name = player.getName();
            if (y1 < y2)
            {
                if (!block.getRelative(BlockFace.NORTH).isLiquid() && !block.getRelative(BlockFace.SOUTH).isLiquid() && !block.getRelative(BlockFace.EAST).isLiquid() && !block.getRelative(BlockFace.WEST).isLiquid())
                {
                    increment(player, ascensionCount, max);
                    if (ascensionCount.get(name) >= max)
                    {
                        return true;
                    }
                }
            }
            else
            {
                ascensionCount.put(name, 0);
            }
        }
        return false;
    }

    public boolean checkSwing(Player player, Block block)
    {
        if(!player.getInventory().getItemInHand().containsEnchantment(Enchantment.DIG_SPEED) && !(player.getInventory().getItemInHand().getType() == Material.SHEARS && block.getType() == Material.LEAVES))
        {
            return !justAnimated(player);
        }
        else
        {
            return false;
        }
    }

    public boolean checkFastBreak(Player player, Block block)
    {
        int violations = magic.FASTBREAK_MAXVIOLATIONS;
        if (player.getGameMode() == GameMode.CREATIVE)
        {
            violations = magic.FASTBREAK_MAXVIOLATIONS_CREATIVE;
        }
        String name = player.getName();
        if (!player.getInventory().getItemInHand().containsEnchantment(Enchantment.DIG_SPEED) && !Utilities.isInstantBreak(block.getType()) && !isInstantBreakExempt(player) && !(player.getInventory().getItemInHand().getType() == Material.SHEARS && block.getType() == Material.LEAVES))
        {
            if (blockPunches.get(name) != null && player.getGameMode() != GameMode.CREATIVE)
            {
                int i = blockPunches.get(name);
                if (i < magic.BLOCK_PUNCH_MIN)
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
                if (fastBreakViolation.get(name) > violations && math < magic.FASTBREAK_MAXVIOLATIONTIME)
                {
                    lastBlockBroken.put(name, System.currentTimeMillis());
                    player.sendMessage(ChatColor.RED + "[AntiCheat] Fastbreaking detected. Please wait 10 seconds before breaking blocks.");
                    return true;
                }
                else if (fastBreakViolation.get(name) > 0 && math > magic.FASTBREAK_MAXVIOLATIONTIME)
                {
                    fastBreakViolation.put(name, 0);
                }
            }
            if (!blocksBroken.containsKey(name) || !lastBlockBroken.containsKey(name))
            {
                if (!lastBlockBroken.containsKey(name))
                {
                    lastBlockBroken.put(name, System.currentTimeMillis());
                }
                blocksBroken.put(name, 0);
            }
            else
            {
                blocksBroken.put(name, blocksBroken.get(name) + 1);
                Long math = System.currentTimeMillis() - lastBlockBroken.get(name);
                if (blocksBroken.get(name) > magic.FASTBREAK_LIMIT && math < magic.FASTBREAK_TIMEMAX)
                {
                    blocksBroken.put(name, 0);
                    lastBlockBroken.put(name, System.currentTimeMillis());
                    fastBreakViolation.put(name, fastBreakViolation.get(name) + 1);
                    return true;
                }
                else if (blocksBroken.get(name) > magic.FASTBREAK_LIMIT)
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
        int violations = magic.FASTPLACE_MAXVIOLATIONS;
        if (player.getGameMode() == GameMode.CREATIVE)
        {
            violations = magic.FASTPLACE_MAXVIOLATIONS_CREATIVE;
        }
        long time = System.currentTimeMillis();
        String name = player.getName();
        if (!lastBlockPlaceTime.containsKey(name) || !fastPlaceViolation.containsKey(name))
        {
            lastBlockPlaceTime.put(name, Long.parseLong("0"));
            if (!fastPlaceViolation.containsKey(name))
            {
                fastPlaceViolation.put(name, 0);
            }
        }
        else if (fastPlaceViolation.containsKey(name) && fastPlaceViolation.get(name) > violations)
        {
            Long math = System.currentTimeMillis() - lastBlockPlaced.get(name);
            if (lastBlockPlaced.get(name) > 0 && math < magic.FASTPLACE_MAXVIOLATIONTIME)
            {
                lastBlockPlaced.put(name, time);
                player.sendMessage(ChatColor.RED + "[AntiCheat] Fastplacing detected. Please wait 10 seconds before placing blocks.");
                return true;
            }
            else if (lastBlockPlaced.get(name) > 0 && math > magic.FASTPLACE_MAXVIOLATIONTIME)
            {
                fastPlaceViolation.put(name, 0);
            }
        }
        else if (lastBlockPlaced.containsKey(name))
        {
            long last = lastBlockPlaced.get(name);
            long lastTime = lastBlockPlaceTime.get(name);
            long thisTime = time - last;
            boolean nocheck = thisTime < 1 || lastTime < 1;
            if (!lastZeroHitPlace.containsKey(name))
            {
                lastZeroHitPlace.put(name, 0);
            }
            if (nocheck)
            {
                if (!lastZeroHitPlace.containsKey(name))
                {
                    lastZeroHitPlace.put(name, 1);
                }
                else
                {
                    lastZeroHitPlace.put(name, lastZeroHitPlace.get(name) + 1);
                }
            }
            if (thisTime < magic.FASTPLACE_TIMEMAX && lastTime < magic.FASTPLACE_TIMEMAX && nocheck && lastZeroHitPlace.get(name) > magic.FASTPLACE_ZEROLIMIT)
            {
                lastBlockPlaceTime.put(name, (time - last));
                lastBlockPlaced.put(name, time);
                fastPlaceViolation.put(name, fastPlaceViolation.get(name) + 1);
                return true;
            }
            lastBlockPlaceTime.put(name, (time - last));
        }
        lastBlockPlaced.put(name, time);
        return false;
    }

    public void logBowWindUp(Player player)
    {
        bowWindUp.put(player.getName(), System.currentTimeMillis());
    }

    public void logEatingStart(Player player)
    {
        startEat.put(player.getName(), System.currentTimeMillis());
    }

    public boolean justStartedEating(Player player)
    {
        if(startEat.containsKey(player.getName())) // Otherwise it was modified by a plugin, don't worry about it.
        {
            long l = startEat.get(player.getName());
            startEat.remove(player.getName());
            return (System.currentTimeMillis()-l) < magic.EAT_TIME_MIN;
        }
        return false;
    }

    public void logHeal(Player player)
    {
        lastHeal.put(player.getName(),System.currentTimeMillis());
    }

    public boolean justHealed(Player player)
    {
        if(lastHeal.containsKey(player.getName())) // Otherwise it was modified by a plugin, don't worry about it.
        {
            long l = lastHeal.get(player.getName());
            lastHeal.remove(player.getName());
            return (System.currentTimeMillis()-l) < magic.HEAL_TIME_MIN;         
        }
        return false;
    }

    public void logChat(Player player)
    {
        String name = player.getName();
        if (chatLevel.get(name) == null)
        {
            logEvent(chatLevel, player, 1, magic.CHAT_MIN);
        }
        else
        {
            int amount = chatLevel.get(name) + 1;
            logEvent(chatLevel, player, amount, magic.CHAT_MIN);
            checkChatLevel(player, amount);
        }
    }

    public boolean checkSpam(Player player, String msg)
    {
        String name = player.getName();
        if (lastMessage.get(name) == null)
        {
            logEvent(lastMessage, player, msg, magic.CHAT_REPEAT_MIN);
            //lastMessage.put(name, msg);
        }
        else
        {
            if (oldMessage.get(name) != null && lastMessage.get(name).equals(msg) && oldMessage.get(name).equals(msg))
            {
                return true;
            }
            else
            {
                logEvent(oldMessage, player, lastMessage.get(name), magic.CHAT_REPEAT_MIN);
                logEvent(oldMessage, player, msg, magic.CHAT_REPEAT_MIN);
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
        instantBreakExempt.put(player.getName(), System.currentTimeMillis());
    }

    public boolean isInstantBreakExempt(Player player)
    {
        return isDoing(player, instantBreakExempt, magic.INSTANT_BREAK_TIME);
    }

    public void logSprint(final Player player)
    {
        sprinted.put(player.getName(), System.currentTimeMillis());
    }

    public boolean justSprinted(Player player)
    {
        return isDoing(player, sprinted, magic.SPRINT_MIN);
    }

    public void logBlockBreak(final Player player)
    {
        brokenBlock.put(player.getName(), System.currentTimeMillis());
        resetAnimation(player);
    }

    public boolean justBroke(Player player)
    {
        return isDoing(player, brokenBlock, magic.BLOCK_BREAK_MIN);
    }

    public void logVelocity(final Player player)
    {
        velocitized.put(player.getName(), System.currentTimeMillis());
    }

    public boolean justVelocity(Player player)
    {
        return (velocitized.containsKey(player.getName()) ? (System.currentTimeMillis() - velocitized.get(player.getName())) < magic.VELOCITY_CHECKTIME : false);
    }

    public boolean extendVelocityTime(final Player player)
    {
        if (velocitytrack.containsKey(player.getName()))
        {
            velocitytrack.put(player.getName(), velocitytrack.get(player.getName()) + 1);
            if (velocitytrack.get(player.getName()) > magic.VELOCITY_MAXTIMES)
            {
                velocitized.put(player.getName(), System.currentTimeMillis() + magic.VELOCITY_PREVENT);
                micromanage.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(micromanage.getPlugin(), new Runnable()
                {
                    @Override
                    public void run()
                    {
                        velocitytrack.put(player.getName(), 0);
                    }
                }, magic.VELOCITY_SCHETIME * 20L);
                return true;
            }
        }
        else
        {
            velocitytrack.put(player.getName(), 0);
        }

        return false;
    }

    public void logBlockPlace(final Player player)
    {
        placedBlock.put(player.getName(), System.currentTimeMillis());
    }

    public boolean justPlaced(Player player)
    {
        return isDoing(player, placedBlock, magic.BLOCK_PLACE_MIN);
    }

    public void logAnimation(final Player player)
    {
        animated.put(player.getName(), System.currentTimeMillis());
        increment(player, blockPunches, magic.BLOCK_PUNCH_MIN);
    }

    public void resetAnimation(final Player player)
    {
        animated.remove(player.getName());
        blockPunches.put(player.getName(), 0);
    }

    public boolean justAnimated(Player player)
    {
        String name = player.getName();
        if(animated.containsKey(name))
        {
            long time = System.currentTimeMillis()-animated.get(name);
            animated.remove(player.getName());
            return time < magic.ANIMATION_MIN;
        }
        else
        {
            return false;
        }
    }

    public void logDamage(final Player player, int type)
    {
        long time;
        switch(type)
        {
            case 1:
                time = magic.DAMAGE_TIME;
                break;
            case 2:
                time = magic.KNOCKBACK_DAMAGE_TIME;
                break;
            case 3: 
                time = magic.EXPLOSION_DAMAGE_TIME;
                break;
            default:
                time = magic.DAMAGE_TIME;
                break;
                
        }
        movingExempt.put(player.getName(), System.currentTimeMillis()+time);
        // Only map in which termination time is calculated beforehand.
    }   

    public void logEnterExit(final Player player)
    {
        movingExempt.put(player.getName(), System.currentTimeMillis()+magic.ENTERED_EXITED_TIME);
    }

    public void logToggleSneak(final Player player)
    {
        movingExempt.put(player.getName(), System.currentTimeMillis()+magic.SNEAK_TIME);
    }

    public void logTeleport(final Player player)
    {
        movingExempt.put(player.getName(), System.currentTimeMillis()+magic.TELEPORT_TIME);

        /* Data for fly/speed should be reset */
        nofallViolation.remove(player.getName());
        flightViolation.remove(player.getName());
        yAxisViolations.remove(player.getName());
        yAxisLastViolation.remove(player.getName());
        lastYcoord.remove(player.getName());
        lastYtime.remove(player.getName());
    }

    public void logExitFly(final Player player)
    {
        movingExempt.put(player.getName(), System.currentTimeMillis()+magic.EXIT_FLY_TIME);
    }

    public void logJoin(final Player player)
    {
        movingExempt.put(player.getName(), System.currentTimeMillis()+magic.JOIN_TIME);
    }

    public boolean isMovingExempt(Player player)
    {
        return isDoing(player, movingExempt, -1);
    }

    public boolean isAscending(Player player)
    {
        return isAscending.contains(player.getName());
    }

    public boolean isSpeedExempt(Player player)
    {
        return isMovingExempt(player) || justVelocity(player);
    }

    @SuppressWarnings("unchecked")
    private void logEvent(@SuppressWarnings("rawtypes") final Map map, final Player player, final Object obj, long time)
    {
        
        map.put(player.getName(), obj);
        micromanage.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(micromanage.getPlugin(), new Runnable()
        {
            @Override
            public void run()
            {
                if (map.get(player.getName()) == obj)
                {
                    map.remove(player.getName());
                }
                else
                {
                    //Don't remove this, for some reason it's fixing chat bugs.
                }
            }
        }, time);
    }
    
    private boolean isDoing(Player player, Map<String, Long> map, double max)
    {
        if(map.containsKey(player.getName()))
        {
            if(max != -1)
            {
                if(((System.currentTimeMillis()-map.get(player.getName()))/1000) > max)
                {
                    map.remove(player.getName());
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else
            {
                // Termination time has already been calculated
                if(map.get(player.getName()) < System.currentTimeMillis())
                {
                    map.remove(player.getName());
                    return false;                    
                }
                else
                {
                    return true;
                }
            }
        }
        else
        {
            return false;
        }        
    }

    private void checkChatLevel(Player player, int amount)
    {
        if (amount >= magic.CHAT_WARN_LEVEL)
        {
            player.sendMessage(ChatColor.RED + "Please stop flooding the server!");
        }
        if (amount >= magic.CHAT_KICK_LEVEL)
        {
            String name = player.getName();
            int kick;
            if (chatKicks.get(name) == null || chatKicks.get(name) == 0)
            {
                kick = 1;
                chatKicks.put(name, 1);
            }
            else
            {
                kick = (int) chatKicks.get(name) + 1;
                chatKicks.put(name, kick);
            }

            if (chatKicks.get(name) <= magic.CHAT_BAN_LEVEL)
            {
                player.kickPlayer(ChatColor.RED + "Spamming, kick " + kick + "/"+magic.CHAT_BAN_LEVEL);
                micromanage.getPlugin().getServer().broadcastMessage(ChatColor.RED + player.getName() + " was kicked for spamming.");
            }
            else
            {
                player.kickPlayer(ChatColor.RED + "Banned for spamming.");
                player.setBanned(true);
                micromanage.getPlugin().getServer().broadcastMessage(ChatColor.RED + player.getName() + " was banned for spamming.");
            }
        }
    }

    public int increment(Player player, Map<String, Integer> map, int num)
    {
        String name = player.getName();
        if (map.get(name) == null)
        {
            map.put(name, 1);
            return 1;
        }
        else
        {
            int amount = map.get(name) + 1;
            if (amount < num + 1)
            {
                map.put(name, amount);
                return amount;
            }
            else
            {
                map.put(name, num);
                return num;
            }
        }
    }
}

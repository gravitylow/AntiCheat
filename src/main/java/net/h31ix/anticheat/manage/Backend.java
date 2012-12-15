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
import net.h31ix.anticheat.util.Language;
import net.h31ix.anticheat.util.Magic;
import net.h31ix.anticheat.util.Utilities;
import net.h31ix.anticheat.util.yaml.CommentedConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
    private Map<String, Double> blocksOverFlight = new HashMap<String, Double>();
    private Map<String, Integer> chatLevel = new HashMap<String, Integer>();
    private Map<String, Integer> chatKicks = new HashMap<String, Integer>();
    private Map<String, Integer> nofallViolation = new HashMap<String, Integer>();
    private Map<String, Integer> speedViolation = new HashMap<String, Integer>();
    private Map<String, Integer> fastBreakViolation = new HashMap<String, Integer>();
    private Map<String, Integer> yAxisViolations = new HashMap<String, Integer>();
    private Map<String, Long> yAxisLastViolation = new HashMap<String, Long>();
    private Map<String, Double> lastYcoord = new HashMap<String, Double>();
    private Map<String, Long> lastYtime = new HashMap<String, Long>();
    private Map<String, Integer> fastBreaks = new HashMap<String, Integer>();
    private Map<String, Boolean> blockBreakHolder = new HashMap<String, Boolean>();
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
    private Map<String, Long> lastInventoryTime = new HashMap<String, Long>();
    private Map<String, Long> inventoryTime = new HashMap<String, Long>();
    private Map<String, Integer> inventoryChanges = new HashMap<String, Integer>();

    private Magic magic;
    private AnticheatManager micromanage = null;
    private Language lang = null;

    public Backend(AnticheatManager instance)
    {
        magic = new Magic(instance.getConfiguration().getMagic(), instance.getConfiguration(), CommentedConfiguration.loadConfiguration(instance.getPlugin().getResource("magic.yml")));
        micromanage = instance;
        lang = micromanage.getConfiguration().getLang();
    }

    public void garbageClean(Player player)
    {
        String pN = player.getName();
        User user = micromanage.getUserManager().getUser(pN);
        if (user != null)
        {
            micromanage.getUserManager().remove(user);
        }
        blocksDropped.remove(pN);
        blockTime.remove(pN);
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
        blocksOverFlight.remove(pN);
        chatLevel.remove(pN);
        nofallViolation.remove(pN);
        fastBreakViolation.remove(pN);
        yAxisViolations.remove(pN);
        yAxisLastViolation.remove(pN);
        lastYcoord.remove(pN);
        lastYtime.remove(pN);
        fastBreaks.remove(pN);
        blockBreakHolder.remove(pN);
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
        startEat.remove(pN);
        lastHeal.remove(pN);
        projectileTime.remove(pN);
        bowWindUp.remove(pN);
        instantBreakExempt.remove(pN);
        sprinted.remove(pN);
        brokenBlock.remove(pN);
        placedBlock.remove(pN);
        movingExempt.remove(pN);
        blockTime.remove(pN);
        blocksDropped.remove(pN);
        lastInventoryTime.remove(pN);
        inventoryTime.remove(pN);
        inventoryChanges.remove(pN);
    }

    public boolean checkFastBow(Player player, float force)
    {
        // Ignore magic numbers here, they are minecrafty vanilla stuff.
        int ticks = (int) ((((System.currentTimeMillis() - bowWindUp.get(player.getName())) * 20) / 1000) + 3);
        bowWindUp.remove(player.getName());
        float f = (float) ticks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        f = f > 1.0F ? 1.0F : f;
        return Math.abs(force - f) > magic.BOW_ERROR;
    }

    public boolean checkProjectile(Player player)
    {
        increment(player, projectilesShot, 10);
        if (!projectileTime.containsKey(player.getName()))
        {
            projectileTime.put(player.getName(), System.currentTimeMillis());
            return false;
        }
        else if (projectilesShot.get(player.getName()) == magic.PROJECTILE_CHECK)
        {
            long time = System.currentTimeMillis() - projectileTime.get(player.getName());
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
        else if (blocksDropped.get(player.getName()) == magic.DROP_CHECK)
        {
            long time = System.currentTimeMillis() - blockTime.get(player.getName());
            blockTime.remove(player.getName());
            blocksDropped.remove(player.getName());
            return time < magic.DROP_TIME_MIN;
        }
        return false;
    }

    public boolean checkLongReachBlock(Player player, double x, double y, double z)
    {
        if (isInstantBreakExempt(player))
        {
            return false;
        }
        else
        {
            return (x >= magic.BLOCK_MAX_DISTANCE || y > magic.BLOCK_MAX_DISTANCE || z > magic.BLOCK_MAX_DISTANCE);
        }
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
        if (!player.isInsideVehicle() && !player.isSleeping() && y > magic.Y_SPEED_MAX && !isDoing(player, velocitized, magic.VELOCITY_TIME) && !player.hasPotionEffect(PotionEffectType.JUMP))
        {
            return true;
        }
        return false;
    }

    public boolean checkNoFall(Player player, double y)
    {
        String name = player.getName();
        if (player.getGameMode() != GameMode.CREATIVE && !player.isInsideVehicle() && !player.isSleeping() && !isMovingExempt(player) && !justPlaced(player) && !Utilities.isInWater(player))
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
            if (speed)
            {
                int num = this.increment(player, speedViolation, magic.SPEED_MAX);
                return num >= magic.SPEED_MAX;
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

    public boolean checkWaterWalk(Player player, double x, double y, double z)
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
                            else if (!Utilities.isFullyInWater(player.getLocation()) && Utilities.isHoveringOverWater(player.getLocation(), 1) && y == 0D && !block.getType().equals(Material.WATER_LILY))
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
                        return true;
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
            Block block = new Location(player.getWorld(), player.getLocation().getX(), to + i, player.getLocation().getZ()).getBlock();
            if (block.getTypeId() != 0 && block.getType().isSolid())
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
        if(entity instanceof LivingEntity)
        {
            LivingEntity le = (LivingEntity)entity;
            // Check to make sure the entity's head is not surrounded
            Block head = le.getWorld().getBlockAt((int) le.getLocation().getX(), (int) (le.getLocation().getY() +le.getEyeHeight()), (int) le.getLocation().getZ());
            boolean solid = false;
            //TODO: This sucks. See if it's possible to not have as many false-positives while still retaining most of the check.
            for (int x = -2; x <= 2; x++)
            {
                for (int z = -2; z <= 2; z++)
                {
                    for (int y = -1; y < 2; y++)
                    {
                        if (head.getRelative(x, y, z).getTypeId() != 0)
                        {
                            if (head.getRelative(x, y, z).getType().isSolid())
                            {
                                solid = true;
                                break;
                            }

                        }
                    }
                }

            }
            return solid ? true : player.hasLineOfSight(le);
        }
        return true;
    }

    public boolean checkFlight(Player player, Distance distance)
    {
        if (distance.getYDifference() > 400)
        {
            //This was a teleport, so we don't care about it.
            return false;
        }
        String name = player.getName();
        double y1 = distance.fromY();
        double y2 = distance.toY();
        //Check if the player is climbing up water.
        /*   if (block.getRelative(BlockFace.NORTH).isLiquid() || block.getRelative(BlockFace.SOUTH).isLiquid() || block.getRelative(BlockFace.EAST).isLiquid() || block.getRelative(BlockFace.WEST).isLiquid())
           {
               if (y1 < y2)
               {
                   //Even if they are using a hacked client and flying next to water to abuse this, they can't be go past the speed limit so they might as well swim
                   return distance.getYDifference() > magic.WATER_CLIMB_MAX;
               }
           } */

        if (!isMovingExempt(player) && !Utilities.isHoveringOverWater(player.getLocation(), 1) && Utilities.cantStandAtExp(player.getLocation()))
        {
            /* if (y1 == y2 || y1 < y2)
             {

                 if (y1 == y2)
                 {
                     //Check if the player is on slabs or crouching on stairs
                     if (Utilities.isSlab(block.getRelative(BlockFace.NORTH)) || Utilities.isSlab(block.getRelative(BlockFace.SOUTH)) || Utilities.isSlab(block.getRelative(BlockFace.EAST)) || Utilities.isSlab(block.getRelative(BlockFace.WEST)) || Utilities.isSlab(block.getRelative(BlockFace.NORTH_EAST)) || Utilities.isSlab(block.getRelative(BlockFace.SOUTH_EAST)) || Utilities.isSlab(block.getRelative(BlockFace.SOUTH_WEST)) || Utilities.isSlab(block.getRelative(BlockFace.NORTH_WEST)))
                     {
                         return false;
                     }
                     else if (player.isSneaking() && (Utilities.isStair(block.getRelative(BlockFace.NORTH)) || Utilities.isStair(block.getRelative(BlockFace.SOUTH)) || Utilities.isStair(block.getRelative(BlockFace.EAST)) || Utilities.isStair(block.getRelative(BlockFace.WEST)) || Utilities.isStair(block.getRelative(BlockFace.NORTH_EAST)) || Utilities.isStair(block.getRelative(BlockFace.SOUTH_EAST)) || Utilities.isStair(block.getRelative(BlockFace.SOUTH_WEST)) || Utilities.isStair(block.getRelative(BlockFace.NORTH_WEST))))
                     {
                         return false;
                     }


                 int violation = flightViolation.containsKey(name) ? flightViolation.get(name) + 1 : 1;
                 increment(player, flightViolation, violation);
                 return violation > magic.FLIGHT_LIMIT;
             } */

            if (!blocksOverFlight.containsKey(name))
            {
                blocksOverFlight.put(name, 0D);
            }

            blocksOverFlight.put(name, (blocksOverFlight.get(name) + distance.getXDifference() + distance.getYDifference() + distance.getZDifference()));

            if (y1 > y2)
            {
                blocksOverFlight.put(name, (blocksOverFlight.get(name) - distance.getYDifference()));
            }

            if (blocksOverFlight.get(name) > magic.FLIGHT_BLOCK_LIMIT && (y1 <= y2))
            {
                return true;
            }
        }
        else
        {
            blocksOverFlight.put(name, 0D);
        }

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
        if (!isMovingExempt(player) && !Utilities.isInWater(player) && !justBroke(player) && !Utilities.isClimbableBlock(player.getLocation().getBlock()) && !player.isInsideVehicle())
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
        String name = player.getName();
        if (!isInstantBreakExempt(player))
        {
            if (!player.getInventory().getItemInHand().containsEnchantment(Enchantment.DIG_SPEED) && !(player.getInventory().getItemInHand().getType() == Material.SHEARS && block.getType() == Material.LEAVES))
            {
                if (blockPunches.get(name) != null && player.getGameMode() != GameMode.CREATIVE)
                {
                    int i = blockPunches.get(name);
                    if (i < magic.BLOCK_PUNCH_MIN)
                    {
                        return true;
                    }
                    else
                    {
                        blockPunches.put(name, 0); // it should reset after EACH block break.
                    }
                }
            }
        }
        return !justAnimated(player); //TODO: best way to implement other option?
    }

    public boolean checkFastBreak(Player player, Block block)
    {
        int violations = player.getGameMode() == GameMode.CREATIVE ? magic.FASTBREAK_MAXVIOLATIONS_CREATIVE : magic.FASTBREAK_MAXVIOLATIONS;
        String name = player.getName();
        if (!player.getInventory().getItemInHand().containsEnchantment(Enchantment.DIG_SPEED) && !Utilities.isInstantBreak(block.getType()) && !isInstantBreakExempt(player) && !(player.getInventory().getItemInHand().getType() == Material.SHEARS && block.getType() == Material.LEAVES))
        {
            long expectedTime = Utilities.getTime(player.getInventory().getItemInHand().getType());
            if(!fastBreaks.containsKey(name))
            {
                fastBreaks.put(name, 0);
                lastBlockBroken.put(name, System.currentTimeMillis());
                fastBreakViolation.put(name, 0);
            }
            else
            {
                long realTime = System.currentTimeMillis()-lastBlockBroken.get(name);
                if(realTime < expectedTime)
                {
                    fastBreaks.put(name, fastBreaks.get(name) + 1);
                }
                lastBlockBroken.put(name, System.currentTimeMillis());

                if(fastBreaks.get(name) > violations)
                {
                    return true;
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
        if (startEat.containsKey(player.getName())) // Otherwise it was modified by a plugin, don't worry about it.
        {
            long l = startEat.get(player.getName());
            startEat.remove(player.getName());
            return (System.currentTimeMillis() - l) < magic.EAT_TIME_MIN;
        }
        return false;
    }

    public void logHeal(Player player)
    {
        lastHeal.put(player.getName(), System.currentTimeMillis());
    }

    public boolean justHealed(Player player)
    {
        if (lastHeal.containsKey(player.getName())) // Otherwise it was modified by a plugin, don't worry about it.
        {
            long l = lastHeal.get(player.getName());
            lastHeal.remove(player.getName());
            return (System.currentTimeMillis() - l) < magic.HEAL_TIME_MIN;
        }
        return false;
    }

    public void logChat(final Player player)
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
            if (checkChatLevel(player, amount))
            {
                micromanage.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(micromanage.getPlugin(), new Runnable()
                {
                    @Override
                    public void run()
                    {
                        processChatSpammer(player);
                    }
                });
            }
        }
    }

    public boolean checkSpam(Player player, String msg)
    {
        String name = player.getName();
        if (lastMessage.get(name) == null)
        {
            logEvent(lastMessage, player, msg, magic.CHAT_REPEAT_MIN);
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
                logEvent(lastMessage, player, msg, magic.CHAT_REPEAT_MIN);
                return false;
            }
        }
        return false;
    }

    public boolean checkInventoryClicks(Player player)
    {
        String name = player.getName();
        long time = System.currentTimeMillis();
        if (!inventoryTime.containsKey(name) || !inventoryChanges.containsKey(name))
        {
            inventoryTime.put(name, 0l);
            if (!inventoryChanges.containsKey(name))
            {
                inventoryChanges.put(name, 0);
            }
        }
        else if (inventoryChanges.containsKey(name) && inventoryChanges.get(name) > magic.INVENTORY_MAXVIOLATIONS)
        {
            long diff = System.currentTimeMillis() - lastInventoryTime.get(name);
            if (inventoryTime.get(name) > 0 && diff < magic.INVENTORY_MAXVIOLATIONTIME)
            {
                inventoryTime.put(name, diff);
                player.sendMessage(ChatColor.RED + "[AntiCheat] Inventory hack detected. Please wait 10 seconds before using inventories.");
                return true;
            }
            else if (inventoryTime.get(name) > 0 && diff > magic.INVENTORY_MAXVIOLATIONTIME)
            {
                inventoryChanges.put(name, 0);
            }
        }
        else if (inventoryTime.containsKey(name))
        {
            long last = lastInventoryTime.get(name);
            long thisTime = time - last;
            if (thisTime < magic.INVENTORY_TIMEMAX)
            {
                inventoryChanges.put(name, inventoryChanges.get(name) + 1);
                if (inventoryChanges.get(name) > magic.INVENTORY_MAXVIOLATIONS)
                {
                    inventoryTime.put(name, thisTime);
                    player.sendMessage(ChatColor.RED + "[AntiCheat] Inventory hack detected. Please wait 10 seconds before using inventories.");
                    return true;
                }
            }
            inventoryTime.put(name, thisTime);
        }
        lastInventoryTime.put(name, time);
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

    public boolean isHoveringOverWaterAfterViolation(Player player)
    {
        if (waterSpeedViolation.containsKey(player.getName()))
        {
            if (waterSpeedViolation.get(player.getName()) >= magic.WATER_SPEED_VIOLATION_MAX && Utilities.isHoveringOverWater(player.getLocation()))
            {
                return true;
            }
        }
        return false;
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
        if (animated.containsKey(name))
        {
            long time = System.currentTimeMillis() - animated.get(name);
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
        switch (type)
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
        movingExempt.put(player.getName(), System.currentTimeMillis() + time);
        // Only map in which termination time is calculated beforehand.
    }

    public void logEnterExit(final Player player)
    {
        movingExempt.put(player.getName(), System.currentTimeMillis() + magic.ENTERED_EXITED_TIME);
    }

    public void logToggleSneak(final Player player)
    {
        movingExempt.put(player.getName(), System.currentTimeMillis() + magic.SNEAK_TIME);
    }

    public void logTeleport(final Player player)
    {
        movingExempt.put(player.getName(), System.currentTimeMillis() + magic.TELEPORT_TIME);

        /* Data for fly/speed should be reset */
        nofallViolation.remove(player.getName());
        blocksOverFlight.remove(player.getName());
        yAxisViolations.remove(player.getName());
        yAxisLastViolation.remove(player.getName());
        lastYcoord.remove(player.getName());
        lastYtime.remove(player.getName());
    }

    public void logExitFly(final Player player)
    {
        movingExempt.put(player.getName(), System.currentTimeMillis() + magic.EXIT_FLY_TIME);
    }

    public void logJoin(final Player player)
    {
        movingExempt.put(player.getName(), System.currentTimeMillis() + magic.JOIN_TIME);
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

    private <E> void logEvent(final Map<String, E> map, final Player player, final E obj, long time)
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
        if (map.containsKey(player.getName()))
        {
            if (max != -1)
            {
                if (((System.currentTimeMillis() - map.get(player.getName())) / 1000) > max)
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
                if (map.get(player.getName()) < System.currentTimeMillis())
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

    private boolean checkChatLevel(Player player, int amount)
    {
        return amount >= magic.CHAT_KICK_LEVEL;
    }

    private void processChatSpammer(Player player)
    {
        if (player != null && player.isOnline())
        {
            String name = player.getName();
            int kick;
            if (chatKicks.get(name) == null || chatKicks.get(name) == 0)
            {
                kick = 1;
                chatKicks.put(name, kick);
            }
            else
            {
                kick = chatKicks.get(name) + 1;
                chatKicks.put(name, kick);
            }

            String event = kick <= magic.CHAT_BAN_LEVEL ? micromanage.getConfiguration().chatActionKick() : micromanage.getConfiguration().chatActionBan();
            // Set string variables
            event = event.replaceAll("&player", name).replaceAll("&world", player.getWorld().getName());

            if (event.startsWith("COMMAND["))
            {
                String command = event.replaceAll("COMMAND\\[", "").replaceAll("]", "");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }
            else if (event.equalsIgnoreCase("KICK"))
            {
                String msg = kick <= magic.CHAT_BAN_LEVEL ? ChatColor.RED + lang.getChatKickReason() + " (" + kick + "/" + magic.CHAT_BAN_LEVEL + ")" : ChatColor.RED + lang.getChatBanReason();
                player.kickPlayer(msg);
                msg = kick <= magic.CHAT_BAN_LEVEL ? ChatColor.RED + lang.getChatKickBroadcast() + " (" + kick + "/" + magic.CHAT_BAN_LEVEL + ")" : ChatColor.RED + lang.getChatBanBroadcast();
                msg = msg.replaceAll("&player", name);
                if (!msg.equals(""))
                {
                    Bukkit.broadcastMessage(msg);
                }
            }
            else if (event.equalsIgnoreCase("BAN"))
            {
                player.setBanned(true);
                player.kickPlayer(ChatColor.RED + lang.getChatBanReason());
                String msg = ChatColor.RED + lang.getChatBanBroadcast();
                msg = msg.replaceAll("&player", name);
                if (!msg.equals(""))
                {
                    Bukkit.broadcastMessage(msg);
                }
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

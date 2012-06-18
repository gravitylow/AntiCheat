/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 AntiCheat Team | http://h31ix.net
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
import net.h31ix.anticheat.event.EventListener;
import net.h31ix.anticheat.util.Distance;
import net.h31ix.anticheat.util.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffectType;

public class Backend
{
    private static final int ENTERED_EXTITED_TIME = 20;
    private static final int SNEAK_TIME = 5;
    private static final int TELEPORT_TIME = 50;
    private static final int EXIT_FLY_TIME = 40;
    private static final int INSTANT_BREAK_TIME = 100;
    private static final int JOIN_TIME = 40;
    private static final int DROPPED_ITEM_TIME = 2;
    private static final int DAMAGE_TIME = 50;
    private static final int KNOCKBACK_DAMAGE_TIME = 50;
    private static final int PROJECTILE_TIME = 20;

    private static final int FASTBREAK_LIMIT = 3;
    private static final int FASTBREAK_TIMEMAX = 500;
    private static final int FASTBREAK_MAXVIOLATIONS = 2;
    private static final int FASTBREAK_MAXVIOLATIONS_CREATIVE = 3;
    private static final int FASTBREAK_MAXVIOLATIONTIME = 10000;
    private static final int FASTPLACE_ZEROLIMIT = 3;
    private static final int FASTPLACE_TIMEMAX = 100;
    private static final int FASTPLACE_MAXVIOLATIONS = 2;
    private static final int FASTPLACE_MAXVIOLATIONS_CREATIVE = 3;
    private static final int FASTPLACE_MAXVIOLATIONTIME = 10000;

    private static final double VISUALS_MAXOFFSET = 2;
    private static final long VISUALS_DELAY = 20L;

    private static final int BLOCK_PUNCH_MIN = 5;

    private static final int PROJECTILE_MAX = 8;

    private static final int CHAT_WARN_LEVEL = 7;
    private static final int CHAT_KICK_LEVEL = 10;
    private static final int CHAT_BAN_LEVEL = 3;

    private static final int FLIGHT_LIMIT = 4;
    private static final int Y_MAXVIOLATIONS = 1;
    private static final int Y_MAXVIOTIME = 5000;
    private static final int VELOCITY_TIME = 60;
    private static final long VELOCITY_SCHETIME = 2;
    private static final long VELOCITY_CHECKTIME = 2100;
    private static final long VELOCITY_PREVENT = 5000;
    private static final int VELOCITY_MAXTIMES = 2;
    private static final int NOFALL_LIMIT = 5;

    private static final int ASCENSION_COUNT_MAX = 8;
    private static final int WATER_ASCENSION_VIOLATION_MAX = 13;
    private static final int WATER_SPEED_VIOLATION_MAX = 4;

    private static final int SPRINT_FOOD_MIN = 6;
    private static final int EAT_MIN = 20;
    private static final int HEAL_MIN = 35;
    private static final int ANIMATION_MIN = 60;
    private static final int CHAT_MIN = 100;
    private static final int CHAT_REPEAT_MIN = 260;
    private static final int BOW_MIN = 2;
    private static final int SPRINT_MIN = 2;
    private static final int BLOCK_BREAK_MIN = 1;
    private static final long BLOCK_PLACE_MIN = 1 / 3;

    private static final double BLOCK_MAX_DISTANCE = 6.0;
    private static final double ENTITY_MAX_DISTANCE = 5.5;

    private static final double LADDER_Y_MAX = 0.11761;
    private static final double LADDER_Y_MIN = 0.11759;

    private static final double Y_SPEED_MAX = 0.5;
    private static final double Y_MAXDIFF = 5;
    private static final double Y_TIME = 1000;
    private static final double XZ_SPEED_MAX = 0.4;
    private static final double XZ_SPEED_MAX_SPRINT = 0.65;
    private static final double XZ_SPEED_MAX_FLY = 0.56;
    private static final double XZ_SPEED_MAX_POTION = 0.85;
    private static final double XZ_SPEED_MAX_SNEAK = 0.2;
    private static final double XZ_SPEED_MAX_WATER = 0.19;
    private static final double XZ_SPEED_MAX_WATER_SPRINT = 0.3;

    private static final int FLY_LOOP = 5;

    private AnticheatManager micromanage = null;
    private List<String> droppedItem = new ArrayList<String>();
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
    private List<String> isAscending = new ArrayList<String>();
    private List<String> trackingProjectiles = new ArrayList<String>();
    private List<String> velocitizing = new ArrayList<String>();
    private List<String> interacting = new ArrayList<String>();
    private Map<String, Integer> ascensionCount = new HashMap<String, Integer>();
    private Map<String, String> oldMessage = new HashMap<String, String>();
    private Map<String, String> lastMessage = new HashMap<String, String>();
    private Map<String, Integer> flightViolation = new HashMap<String, Integer>();
    private Map<String, Integer> chatLevel = new HashMap<String, Integer>();
    private Map<String, Integer> chatKicks = new HashMap<String, Integer>();
    private Map<String, Integer> nofallViolation = new HashMap<String, Integer>();
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
    private Map<String, Location> animated = new HashMap<String, Location>();

    public Backend(AnticheatManager instance)
    {
        micromanage = instance;
    }

    public void buildAdvancedInformation(StringBuilder r)
    {
        r.append("Dropped Items List:" + '\n');
        for (String l : droppedItem)
        {
            r.append(l + '\n');
        }
        r.append("Moving Exempt List:" + '\n');
        for (String l : movingExempt)
        {
            r.append(l + '\n');
        }
        r.append("Broken Block List:" + '\n');
        for (String l : brokenBlock)
        {
            r.append(l + '\n');
        }
        r.append("Placed Block List:" + '\n');
        for (String l : placedBlock)
        {
            r.append(l + '\n');
        }
        r.append("Bow Wind Up List:" + '\n');
        for (String l : bowWindUp)
        {
            r.append(l + '\n');
        }
        r.append("Start Eating List:" + '\n');
        for (String l : startEat)
        {
            r.append(l + '\n');
        }
        r.append("Healed List:" + '\n');
        for (String l : healed)
        {
            r.append(l + '\n');
        }
        r.append("Sprinted List:" + '\n');
        for (String l : sprinted)
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
        for (String l : instantBreakExempt)
        {
            r.append(l + '\n');
        }
        r.append("Is Ascending List:" + '\n');
        for (String l : isAscending)
        {
            r.append(l + '\n');
        }
        r.append("Tracking Projectiles List:" + '\n');
        for (String l : trackingProjectiles)
        {
            r.append(l + '\n');
        }
        r.append("Velocitizing List:" + '\n');
        for (String l : velocitizing)
        {
            r.append(l + '\n');
        }
        r.append("Interacting List:" + '\n');
        for (String l : interacting)
        {
            r.append(l + '\n');
        }
    }

    public void garbageClean(Player player)
    {
        String pN = player.getName();
        droppedItem.remove(pN);
        movingExempt.remove(pN);
        brokenBlock.remove(pN);
        placedBlock.remove(pN);
        bowWindUp.remove(pN);
        startEat.remove(pN);
        healed.remove(pN);
        sprinted.remove(pN);
        isInWater.remove(pN);
        isInWaterCache.remove(pN);
        instantBreakExempt.remove(pN);
        isAscending.remove(pN);
        trackingProjectiles.remove(pN);
        velocitizing.remove(pN);
        interacting.remove(pN);
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

    public boolean checkLongReachBlock(Player player, double x, double y, double z)
    {
        return !Utilities.isUsingMcMMOAbility(player) && (x >= BLOCK_MAX_DISTANCE || y > BLOCK_MAX_DISTANCE || z > BLOCK_MAX_DISTANCE);
    }

    public boolean checkLongReachDamage(double x, double y, double z)
    {
        return x >= ENTITY_MAX_DISTANCE || y > ENTITY_MAX_DISTANCE || z > ENTITY_MAX_DISTANCE;
    }

    public boolean checkSpider(Player player, double y)
    {
        if (y <= LADDER_Y_MAX && y >= LADDER_Y_MIN && !Utilities.isOnClimbableBlock(player))
        {
            return true;
        }
        return false;
    }

    public boolean checkYSpeed(Player player, double y)
    {
        if (!player.isInsideVehicle() && y > Y_SPEED_MAX && !isVelocity(player) && !player.hasPotionEffect(PotionEffectType.JUMP))
        {
            return true;
        }
        return false;
    }

    public boolean checkNoFall(Player player, double y)
    {
        String name = player.getName();
        if (player.getGameMode() != GameMode.CREATIVE && player.getVehicle() == null && !isMovingExempt(player))
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

                if (nofallViolation.get(name) >= NOFALL_LIMIT)
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
            if (player.isFlying())
            {
                return x > XZ_SPEED_MAX_FLY || z > XZ_SPEED_MAX_FLY;
            }
            else if (player.hasPotionEffect(PotionEffectType.SPEED))
            {
                return x > XZ_SPEED_MAX_POTION || z > XZ_SPEED_MAX_POTION;
            }
            else if (player.isSprinting())
            {
                return x > XZ_SPEED_MAX_SPRINT || z > XZ_SPEED_MAX_SPRINT;
            }
            else
            {
                return x > XZ_SPEED_MAX || z > XZ_SPEED_MAX;
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
        if (event.isSprinting())
        {
            return player.getFoodLevel() <= SPRINT_FOOD_MIN && player.getGameMode() != GameMode.CREATIVE;
        }
        else
        {
            return false;
        }
    }

    public boolean checkVisuals(Player player, Block targetBlock, Block playerClick)
    {
        if (!Utilities.isInteractable(targetBlock.getType()))
        {
            return false;
        }

        double x = targetBlock.getX() - playerClick.getX() * ((targetBlock.getX() - playerClick.getX() * 1) < 0 ? -1 : 1);
        double y = targetBlock.getY() - playerClick.getY() * ((targetBlock.getY() - playerClick.getY() * 1) < 0 ? -1 : 1);
        double z = targetBlock.getZ() - playerClick.getZ() * ((targetBlock.getZ() - playerClick.getZ() * 1) < 0 ? -1 : 1);

        return x >= VISUALS_MAXOFFSET || y >= VISUALS_MAXOFFSET || z >= VISUALS_MAXOFFSET;
    }

    public boolean checkWaterWalk(Player player, double x, double z)
    {
        Block block = player.getLocation().getBlock();
        if (player.getVehicle() == null && !player.isFlying())
        {
            if (block.isLiquid() && block.getRelative(BlockFace.DOWN).isLiquid())
            {
                if (isInWater.contains(player.getName()))
                {
                    if (isInWaterCache.contains(player.getName()))
                    {
                        if (player.getNearbyEntities(1, 1, 1).isEmpty())
                        {
                            boolean b = false;
                            if (!Utilities.sprintFly(player) && x > XZ_SPEED_MAX_WATER || z > XZ_SPEED_MAX_WATER)
                            {
                                b = true;
                            }
                            else if (x > XZ_SPEED_MAX_WATER_SPRINT || z > XZ_SPEED_MAX_WATER_SPRINT)
                            {
                                b = true;
                            }
                            if (b)
                            {
                                if (waterSpeedViolation.containsKey(player.getName()))
                                {
                                    int v = waterSpeedViolation.get(player.getName());
                                    if (v >= WATER_SPEED_VIOLATION_MAX)
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
                    if (v >= WATER_ASCENSION_VIOLATION_MAX)
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

    public boolean checkYAxis(Player player, Distance distance)
    {
        if (distance.getYDifference() > 400)
        {
            return false;
        }
        if (!isMovingExempt(player) && !Utilities.isOnClimbableBlock(player) && !player.isInsideVehicle() && !Utilities.isInWater(player))
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
                if (y1 > lastYcoord.get(name) && yAxisViolations.get(name) > Y_MAXVIOLATIONS && (System.currentTimeMillis() - yAxisLastViolation.get(name)) < Y_MAXVIOTIME)
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
                    if (yAxisViolations.get(name) > Y_MAXVIOLATIONS && (System.currentTimeMillis() - yAxisLastViolation.get(name)) > Y_MAXVIOTIME)
                    {
                        yAxisViolations.put(name, 0);
                        yAxisLastViolation.put(name, 0L);
                    }
                }
                if ((y1 - lastYcoord.get(name)) > Y_MAXDIFF && (System.currentTimeMillis() - lastYtime.get(name)) < Y_TIME)
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
                    if ((y1 - lastYcoord.get(name)) > Y_MAXDIFF + 1 || (System.currentTimeMillis() - lastYtime.get(name)) > Y_TIME)
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

    public boolean checkFlight(Player player, Distance distance)
    {
        if (distance.getYDifference() > 400)
        {
            //This was a teleport, so we don't care about it.
            return false;
        }
        double y1 = distance.fromY();
        double y2 = distance.toY();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (y1 == y2 && !isMovingExempt(player) && !player.isInsideVehicle() && player.getFallDistance() == 0 && !Utilities.isOnLilyPad(player) && !Utilities.isOnVine(player))
        {
            String name = player.getName();
            if (Utilities.cantStandAt(block) && !Utilities.isOnLilyPad(player) && Utilities.cantStandAt(player.getLocation().getBlock()) && !Utilities.isInWater(player))
            {
                int violation = 1;
                if (!flightViolation.containsKey(name))
                {
                    flightViolation.put(name, violation);
                }
                else
                {
                    violation = flightViolation.get(name) + 1;
                    flightViolation.put(name, violation);
                }
                if (violation >= FLIGHT_LIMIT)
                {
                    flightViolation.put(name, 1);
                    return true;
                }
                //Start Fly bypass patch.
                if (flightViolation.containsKey(name) && flightViolation.get(name) > 0)
                {
                    for (int i = FLY_LOOP; i > 0; i--)
                    {
                        Location newLocation = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - i, player.getLocation().getZ());
                        Block lower = newLocation.getBlock();
                        if (lower.getTypeId() == 0)
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
        int max = ASCENSION_COUNT_MAX;
        if (player.hasPotionEffect(PotionEffectType.JUMP))
        {
            max += 12;
        }
        Block block = player.getLocation().getBlock();
        if (!isMovingExempt(player) && !Utilities.isInWater(player) && !Utilities.isOnClimbableBlock(player) && !player.isInsideVehicle())
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
        return !player.getInventory().getItemInHand().containsEnchantment(Enchantment.DIG_SPEED) && !Utilities.isInstantBreak(block.getType()) && !justAnimated(player) && !Utilities.isUsingMcMMOAbility(player) && !(player.getInventory().getItemInHand().getType() == Material.SHEARS && block.getType() == Material.LEAVES);
    }

    public boolean checkFastBreak(Player player, Block block)
    {
        if (!Utilities.isUsingMcMMOAbility(player))
        {
            int violations = FASTBREAK_MAXVIOLATIONS;
            if (player.getGameMode() == GameMode.CREATIVE)
            {
                violations = FASTBREAK_MAXVIOLATIONS_CREATIVE;
            }
            String name = player.getName();
            if (!player.getInventory().getItemInHand().containsEnchantment(Enchantment.DIG_SPEED) && !Utilities.isInstantBreak(block.getType()) && !isInstantBreakExempt(player) && !(player.getInventory().getItemInHand().getType() == Material.SHEARS && block.getType() == Material.LEAVES))
            {
                if (blockPunches.get(name) != null && player.getGameMode() != GameMode.CREATIVE)
                {
                    int i = blockPunches.get(name);
                    if (i < BLOCK_PUNCH_MIN)
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
                    if (fastBreakViolation.get(name) > violations && math < FASTBREAK_MAXVIOLATIONTIME)
                    {
                        lastBlockBroken.put(name, System.currentTimeMillis());
                        player.sendMessage(ChatColor.RED + "[AntiCheat] Fastbreaking detected. Please wait 10 seconds before breaking blocks.");
                        return true;
                    }
                    else if (fastBreakViolation.get(name) > 0 && math > FASTBREAK_MAXVIOLATIONTIME)
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
                    if (blocksBroken.get(name) > FASTBREAK_LIMIT && math < FASTBREAK_TIMEMAX)
                    {
                        blocksBroken.put(name, 0);
                        lastBlockBroken.put(name, System.currentTimeMillis());
                        fastBreakViolation.put(name, fastBreakViolation.get(name) + 1);
                        return true;
                    }
                    else if (blocksBroken.get(name) > FASTBREAK_LIMIT)
                    {
                        lastBlockBroken.put(name, System.currentTimeMillis());
                        blocksBroken.put(name, 0);
                    }
                }
            }
        }
        return false;
    }

    public boolean checkFastPlace(Player player)
    {
        int violations = FASTPLACE_MAXVIOLATIONS;
        if (player.getGameMode() == GameMode.CREATIVE)
        {
            violations = FASTPLACE_MAXVIOLATIONS_CREATIVE;
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
            if (lastBlockPlaced.get(name) > 0 && math < FASTPLACE_MAXVIOLATIONTIME)
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
            if (thisTime < FASTPLACE_TIMEMAX && lastTime < FASTPLACE_TIMEMAX && nocheck && lastZeroHitPlace.get(name) > FASTPLACE_ZEROLIMIT)
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
        logEvent(bowWindUp, player, BOW_MIN);
    }

    public boolean justWoundUp(Player player)
    {
        return bowWindUp.contains(player.getName());
    }

    public void logEatingStart(Player player)
    {
        logEvent(startEat, player, EAT_MIN);
    }

    public boolean justStartedEating(Player player)
    {
        return startEat.contains(player.getName());
    }

    public void logHeal(Player player)
    {
        logEvent(healed, player, HEAL_MIN);
    }

    public void logInteraction(Player player)
    {
        logEvent(interacting, player, VISUALS_DELAY);
    }

    public boolean isInteracting(Player player)
    {
        return interacting.contains(player.getName());
    }

    public boolean justHealed(Player player)
    {
        return healed.contains(player.getName());
    }

    public void logChat(Player player)
    {
        String name = player.getName();
        if (chatLevel.get(name) == null)
        {
            logEvent(chatLevel, player, 1, CHAT_MIN);
        }
        else
        {
            int amount = chatLevel.get(name) + 1;
            logEvent(chatLevel, player, amount, CHAT_MIN);
            checkChatLevel(player, amount);
        }
    }

    public boolean checkSpam(Player player, String msg)
    {
        String name = player.getName();
        if (lastMessage.get(name) == null)
        {
            logEvent(lastMessage, player, msg, CHAT_REPEAT_MIN);
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
                logEvent(oldMessage, player, lastMessage.get(name), CHAT_REPEAT_MIN);
                //oldMessage.put(name, lastMessage.get(name));
                logEvent(oldMessage, player, msg, CHAT_REPEAT_MIN);
                //lastMessage.put(name, msg);
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
        logEvent(instantBreakExempt, player, INSTANT_BREAK_TIME);
    }

    public boolean isInstantBreakExempt(Player player)
    {
        return instantBreakExempt.contains(player.getName());
    }

    public void logSprint(final Player player)
    {
        logEvent(sprinted, player, SPRINT_MIN);
    }

    public boolean justSprinted(Player player)
    {
        return sprinted.contains(player.getName());
    }

    public void logBlockBreak(final Player player)
    {
        logEvent(brokenBlock, player, BLOCK_BREAK_MIN);
        resetAnimation(player);
    }

    public boolean justBroke(Player player)
    {
        return brokenBlock.contains(player.getName());
    }

    public void logVelocity(final Player player)
    {
        logEvent(velocitizing, player, VELOCITY_TIME);
        velocitized.put(player.getName(), System.currentTimeMillis());
    }

    public boolean justVelocity(Player player)
    {
        return isVelocity(player) || (velocitized.containsKey(player.getName()) ? (System.currentTimeMillis() - velocitized.get(player.getName())) < VELOCITY_CHECKTIME : false);
    }

    public boolean isVelocity(Player player)
    {
        return velocitizing.contains(player.getName());
    }

    public boolean extendVelocityTime(final Player player)
    {
        if (velocitytrack.containsKey(player.getName()))
        {
            velocitytrack.put(player.getName(), velocitytrack.get(player.getName()) + 1);
            if (velocitytrack.get(player.getName()) > VELOCITY_MAXTIMES)
            {
                velocitized.put(player.getName(), System.currentTimeMillis() + VELOCITY_PREVENT);
                micromanage.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(micromanage.getPlugin(), new Runnable()
                {
                    @Override
                    public void run()
                    {
                        velocitytrack.put(player.getName(), 0);
                    }
                }, VELOCITY_SCHETIME * 20L);
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
        logEvent(placedBlock, player, BLOCK_PLACE_MIN);
    }

    public boolean justPlaced(Player player)
    {
        return placedBlock.contains(player.getName());
    }

    public void logAnimation(final Player player)
    {
        logEvent(animated, player, player.getLocation(), ANIMATION_MIN);
        increment(player, blockPunches, BLOCK_PUNCH_MIN);
    }

    public void resetAnimation(final Player player)
    {
        animated.remove(player.getName());
        blockPunches.put(player.getName(), 0);
    }

    public boolean justAnimated(Player player)
    {
        return animated.containsKey(player.getName());
    }

    public void logProjectile(final Player player, final EventListener e)
    {
        increment(player, projectilesShot, PROJECTILE_MAX);
        if (!trackingProjectiles.contains(player.getName()))
        {
            trackingProjectiles.add(player.getName());
            micromanage.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(micromanage.getPlugin(), new Runnable()
            {
                @Override
                public void run()
                {
                    trackingProjectiles.remove(player.getName());
                    if (projectilesShot.get(player.getName()) >= PROJECTILE_MAX)
                    {
                        e.log("tried to fire projectiles too fast", player, CheckType.FAST_PROJECTILE);
                    }
                    projectilesShot.put(player.getName(), 0);
                }
            }, PROJECTILE_TIME);
        }
    }

    public void logDamage(final Player player)
    {
        int time = DAMAGE_TIME;
        if (player.getInventory().getItemInHand().getEnchantments().containsKey(Enchantment.KNOCKBACK))
        {
            time = KNOCKBACK_DAMAGE_TIME;
        }
        logEvent(movingExempt, player, time);
    }

    public void logEnterExit(final Player player)
    {
        logEvent(movingExempt, player, ENTERED_EXTITED_TIME);
    }

    public void logToggleSneak(final Player player)
    {
        logEvent(movingExempt, player, SNEAK_TIME);
    }

    public void logTeleport(final Player player)
    {
        logEvent(movingExempt, player, TELEPORT_TIME);

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
        logEvent(movingExempt, player, EXIT_FLY_TIME);
    }

    public void logJoin(final Player player)
    {
        logEvent(movingExempt, player, JOIN_TIME);
    }

    public boolean isMovingExempt(Player player)
    {
        return movingExempt.contains(player.getName()) || player.isFlying() || isVelocity(player);
    }

    public boolean isAscending(Player player)
    {
        return isAscending.contains(player.getName());
    }

    public boolean isSpeedExempt(Player player)
    {
        return movingExempt.contains(player.getName()) || isVelocity(player);
    }

    public void logDroppedItem(final Player player)
    {
        logEvent(droppedItem, player, DROPPED_ITEM_TIME);
    }

    public boolean justDroppedItem(Player player)
    {
        return droppedItem.contains(player.getName());
    }

    @SuppressWarnings("unchecked")
    private void logEvent(@SuppressWarnings("rawtypes") final List list, final Player player, long time)
    {
        if(!list.contains(player.getName()))
        {
            list.add(player.getName());
        }
        micromanage.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(micromanage.getPlugin(), new Runnable()
        {
            @Override
            public void run()
            {
                list.remove(player.getName());
            }
        }, time);
    }

    @SuppressWarnings("unchecked")
    private void logEvent(@SuppressWarnings("rawtypes") final Map map, final Player player, final Object obj, long time)
    {
        if(!map.containsKey(player.getName()))
        {        
            map.put(player.getName(), obj);
        }
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

    private void checkChatLevel(Player player, int amount)
    {
        if (amount >= CHAT_WARN_LEVEL)
        {
            player.sendMessage(ChatColor.RED + "Please stop flooding the server!");
        }
        if (amount >= CHAT_KICK_LEVEL)
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

            if (chatKicks.get(name) <= CHAT_BAN_LEVEL)
            {
                player.kickPlayer(ChatColor.RED + "Spamming, kick " + kick + "/3");
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

    public void increment(Player player, Map<String, Integer> map, int num)
    {
        String name = player.getName();
        if (map.get(name) == null)
        {
            map.put(name, 1);
        }
        else
        {
            int amount = map.get(name) + 1;
            if (amount < num + 1)
            {
                map.put(name, amount);
            }
            else
            {
                map.put(name, num);
            }
        }
    }
}

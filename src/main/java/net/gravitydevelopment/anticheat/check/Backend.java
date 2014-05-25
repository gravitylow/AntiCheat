/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012-2014 AntiCheat Team | http://gravitydevelopment.net
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

package net.gravitydevelopment.anticheat.check;

import net.gravitydevelopment.anticheat.AntiCheat;
import net.gravitydevelopment.anticheat.config.Configuration;
import net.gravitydevelopment.anticheat.config.providers.Lang;
import net.gravitydevelopment.anticheat.config.providers.Magic;
import net.gravitydevelopment.anticheat.manage.AntiCheatManager;
import net.gravitydevelopment.anticheat.util.User;
import net.gravitydevelopment.anticheat.util.Distance;
import net.gravitydevelopment.anticheat.util.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Backend {
    private List<String> isInWater = new ArrayList<String>();
    private List<String> isInWaterCache = new ArrayList<String>();
    private List<String> isAscending = new ArrayList<String>();
    private Map<String, Integer> interactionCount = new HashMap<String, Integer>();
    private Map<String, Integer> ascensionCount = new HashMap<String, Integer>();
    private Map<String, Double> blocksOverFlight = new HashMap<String, Double>();
    private Map<String, Integer> chatLevel = new HashMap<String, Integer>();
    private Map<String, Integer> commandLevel = new HashMap<String, Integer>();
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
    private Map<String, Integer> inventoryClicks = new HashMap<String, Integer>();
    private Map<String, Material> itemInHand = new HashMap<String, Material>();
    private Map<String, Integer> steps = new HashMap<String, Integer>();
    private Map<String, Long> stepTime = new HashMap<String, Long>();
    private HashSet<Byte> transparent = new HashSet<Byte>();
    private Map<String, Long> lastFallPacket = new HashMap<String, Long>();

    private Magic magic;
    private AntiCheatManager manager = null;
    private Lang lang = null;
    private static final CheckResult PASS = new CheckResult(CheckResult.Result.PASSED);

    public Backend(AntiCheatManager instance) {
        magic = instance.getConfiguration().getMagic();
        manager = instance;
        lang = manager.getConfiguration().getLang();
        transparent.add((byte) -1);
    }

    public void updateConfig(Configuration config) {
        magic = config.getMagic();
        lang = config.getLang();
    }

    public void resetChatLevel(User user) {
        chatLevel.put(user.getName(), 0);
    }

    public void garbageClean(Player player) {
        String pN = player.getName();

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
        blocksOverFlight.remove(pN);
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
        inventoryClicks.remove(pN);
        lastFallPacket.remove(pN);
    }

    public CheckResult checkFreeze(Player player, double from, double to) {
        // TODO: Fix! This is causing false-positives
        /*
         * if((from-to) > 0) { boolean flag = false; if(lastFallPacket.containsKey(player.getName()) && lastFallPacket.get(player.getName()) > 0) { flag = (System.currentTimeMillis()-lastFallPacket.get(player.getName())) > 1000; } lastFallPacket.put(player.getName(), System.currentTimeMillis());
         * return flag; } else { lastFallPacket.put(player.getName(), -1L); }
         */
        return PASS;
    }

    public CheckResult checkFastBow(Player player, float force) {
        // Ignore magic numbers here, they are minecrafty vanilla stuff.
        int ticks = (int) ((((System.currentTimeMillis() - bowWindUp.get(player.getName())) * 20) / 1000) + 3);
        bowWindUp.remove(player.getName());
        float f = (float) ticks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        f = f > 1.0F ? 1.0F : f;
        if (Math.abs(force - f) > magic.BOW_ERROR()) {
            return new CheckResult(CheckResult.Result.FAILED, player.getName() + " fired their bow too fast (actual force=" + force + ", calculated force=" + f + ")");
        } else {
            return PASS;
        }
    }

    public CheckResult checkProjectile(Player player) {
        increment(player, projectilesShot, 10);
        if (!projectileTime.containsKey(player.getName())) {
            projectileTime.put(player.getName(), System.currentTimeMillis());
            return new CheckResult(CheckResult.Result.PASSED);
        } else if (projectilesShot.get(player.getName()) == magic.PROJECTILE_CHECK()) {
            long time = System.currentTimeMillis() - projectileTime.get(player.getName());
            projectileTime.remove(player.getName());
            projectilesShot.remove(player.getName());
            if (time < magic.PROJECTILE_TIME_MIN()) {
                return new CheckResult(CheckResult.Result.FAILED, player.getName() + " wound up a bow too fast (actual time=" + time + ", min time=" + magic.PROJECTILE_TIME_MIN() + ")");
            }
        }
        return PASS;
    }

    public CheckResult checkFastDrop(Player player) {
        increment(player, blocksDropped, 10);
        if (!blockTime.containsKey(player.getName())) {
            blockTime.put(player.getName(), System.currentTimeMillis());
            return new CheckResult(CheckResult.Result.PASSED);
        } else if (blocksDropped.get(player.getName()) == magic.DROP_CHECK()) {
            long time = System.currentTimeMillis() - blockTime.get(player.getName());
            blockTime.remove(player.getName());
            blocksDropped.remove(player.getName());
            if (time < magic.DROP_TIME_MIN()) {
                return new CheckResult(CheckResult.Result.FAILED, player.getName() + " dropped an item too fast (actual time=" + time + ", min time=" + magic.DROP_TIME_MIN() + ")");
            }
        }
        return PASS;
    }

    public CheckResult checkLongReachBlock(Player player, double x, double y, double z) {
        if (isInstantBreakExempt(player)) {
            return new CheckResult(CheckResult.Result.PASSED);
        } else {
            String string = player.getName() + " reached too far for a block";
            double distance =
                    player.getGameMode() == GameMode.CREATIVE ? magic.BLOCK_MAX_DISTANCE_CREATIVE()
                            : player.getLocation().getDirection().getY() > 0.9 ? magic.BLOCK_MAX_DISTANCE_CREATIVE()
                            : magic.BLOCK_MAX_DISTANCE();
            double i = x >= distance ? x : y > distance ? y : z > distance ? z : -1;
            if (i != -1) {
                return new CheckResult(CheckResult.Result.FAILED, string + " (distance=" + i + ", max=" + magic.BLOCK_MAX_DISTANCE() + ")");
            } else {
                return PASS;
            }
        }
    }

    public CheckResult checkLongReachDamage(Player player, double x, double y, double z) {
        String string = player.getName() + " reached too far for an entity";
        double i = x >= magic.ENTITY_MAX_DISTANCE() ? x : y > magic.ENTITY_MAX_DISTANCE() ? y : z > magic.ENTITY_MAX_DISTANCE() ? z : -1;
        if (i != -1) {
            return new CheckResult(CheckResult.Result.FAILED, string + " (distance=" + i + ", max=" + magic.ENTITY_MAX_DISTANCE() + ")");
        } else {
            return PASS;
        }
    }

    public CheckResult checkSpider(Player player, double y) {
        if (y <= magic.LADDER_Y_MAX() && y >= magic.LADDER_Y_MIN() && !Utilities.isClimbableBlock(player.getLocation().getBlock())) {
            return new CheckResult(CheckResult.Result.FAILED, player.getName() + " tried to climb a non-ladder (" + player.getLocation().getBlock().getType() + ")");
        } else {
            return PASS;
        }
    }

    public CheckResult checkYSpeed(Player player, double y) {
        if (!isMovingExempt(player) && !player.isInsideVehicle() && !player.isSleeping() && y > magic.Y_SPEED_MAX() && !isDoing(player, velocitized, magic.VELOCITY_TIME()) && !player.hasPotionEffect(PotionEffectType.JUMP)) {
            return new CheckResult(CheckResult.Result.FAILED, player.getName() + "'s y speed was too high (speed=" + y + ", max=" + magic.Y_SPEED_MAX() + ")");
        } else {
            return PASS;
        }
    }

    public CheckResult checkNoFall(Player player, double y) {
        String name = player.getName();
        if (player.getGameMode() != GameMode.CREATIVE && !player.isInsideVehicle() && !player.isSleeping() && !isMovingExempt(player) && !justPlaced(player) && !Utilities.isInWater(player) && !Utilities.isInWeb(player)) {
            if (player.getFallDistance() == 0) {
                if (nofallViolation.get(name) == null) {
                    nofallViolation.put(name, 1);
                } else {
                    nofallViolation.put(name, nofallViolation.get(player.getName()) + 1);
                }

                int i = nofallViolation.get(name);
                if (i >= magic.NOFALL_LIMIT()) {
                    nofallViolation.put(player.getName(), 1);
                    return new CheckResult(CheckResult.Result.FAILED, player.getName() + " tried to avoid fall damage (fall distance = 0 " + i + " times in a row, max=" + magic.NOFALL_LIMIT() + ")");
                } else {
                    return PASS;
                }
            } else {
                nofallViolation.put(name, 0);
                return PASS;
            }
        }
        return PASS;
    }

    public CheckResult checkXZSpeed(Player player, double x, double z) {
        if (!isSpeedExempt(player) && player.getVehicle() == null) {
            String reason = "";
            double max = magic.XZ_SPEED_MAX();
            if (player.getLocation().getBlock().getType() == Material.SOUL_SAND) {
                if (player.isSprinting()) {
                    reason = "on soulsand while sprinting ";
                    max = magic.XZ_SPEED_MAX_SOULSAND_SPRINT();
                } else if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                    reason = "on soulsand with speed potion ";
                    max = magic.XZ_SPEED_MAX_SOULSAND_POTION();
                } else {
                    reason = "on soulsand ";
                    max = magic.XZ_SPEED_MAX_SOULSAND();
                }
            } else if (player.isFlying()) {
                reason = "while flying ";
                max = magic.XZ_SPEED_MAX_FLY();
            } else if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                if (player.isSprinting()) {
                    reason = "with speed potion while sprinting ";
                    max = magic.XZ_SPEED_MAX_POTION_SPRINT();
                } else {
                    reason = "with speed potion ";
                    max = magic.XZ_SPEED_MAX_POTION();
                }
            } else if (player.isSprinting()) {
                reason = "while sprinting ";
                max = magic.XZ_SPEED_MAX_SPRINT();
            }

            float speed = player.getWalkSpeed();
            max += speed > 0 ? player.getWalkSpeed() - 0.2f : 0;

            if (x > max || z > max) {
                int num = this.increment(player, speedViolation, magic.SPEED_MAX());
                if (num >= magic.SPEED_MAX()) {
                    return new CheckResult(CheckResult.Result.FAILED, player.getName() + "'s speed was too high " + reason + num + " times in a row (max=" + magic.SPEED_MAX() + ", speed=" + (x > z ? x : z) + ", max speed=" + max + ")");
                } else {
                    return PASS;
                }
            } else {
                speedViolation.put(player.getName(), 0);
                return PASS;
            }
        } else {
            return PASS;
        }
    }

    public CheckResult checkSneak(Player player, double x, double z) {
        if (player.isSneaking() && !player.isFlying() && !isMovingExempt(player) && !player.isInsideVehicle()) {
            double i = x > magic.XZ_SPEED_MAX_SNEAK() ? x : z > magic.XZ_SPEED_MAX_SNEAK() ? z : -1;
            if (i != -1) {
                return new CheckResult(CheckResult.Result.FAILED, player.getName() + " was sneaking too fast (speed=" + i + ", max=" + magic.XZ_SPEED_MAX_SNEAK() + ")");
            } else {
                return PASS;
            }
        } else {
            return PASS;
        }
    }

    public CheckResult checkSprintHungry(PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();
        if (event.isSprinting() && player.getGameMode() != GameMode.CREATIVE && player.getFoodLevel() <= magic.SPRINT_FOOD_MIN()) {
            return new CheckResult(CheckResult.Result.FAILED, player.getName() + " sprinted while hungry (food=" + player.getFoodLevel() + ", min=" + magic.SPRINT_FOOD_MIN() + ")");
        } else {
            return PASS;
        }
    }

    public CheckResult checkSprintStill(Player player, Location from, Location to) {
        /*if(!isMovingExempt(player) && player.isSprinting() && from.getX() == to.getX() && from.getZ() == to.getZ()) {
            return new CheckResult(Result.FAILED, player.getName()+" sprinted while standing still (xyz = "+(int)from.getX()+","+(int)from.getY()+","+(int)from.getZ()+")");
        }*/
        return PASS;
    }

    public CheckResult checkWaterWalk(Player player, double x, double y, double z) {
        Block block = player.getLocation().getBlock();

        if (player.getVehicle() == null && !player.isFlying()) {
            if (block.isLiquid()) {
                if (isInWater.contains(player.getName())) {
                    if (isInWaterCache.contains(player.getName())) {
                        if (player.getNearbyEntities(1, 1, 1).isEmpty()) {
                            boolean b;
                            if (!Utilities.sprintFly(player)) {
                                b = x > magic.XZ_SPEED_MAX_WATER() || z > magic.XZ_SPEED_MAX_WATER();
                            } else {
                                b = x > magic.XZ_SPEED_MAX_WATER_SPRINT() || z > magic.XZ_SPEED_MAX_WATER_SPRINT();
                            }
                            if (!b && !Utilities.isFullyInWater(player.getLocation()) && Utilities.isHoveringOverWater(player.getLocation(), 1) && y == 0D && !block.getType().equals(Material.WATER_LILY)) {
                                b = true;
                            }
                            if (b) {
                                if (waterSpeedViolation.containsKey(player.getName())) {
                                    int v = waterSpeedViolation.get(player.getName());
                                    if (v >= magic.WATER_SPEED_VIOLATION_MAX()) {
                                        waterSpeedViolation.put(player.getName(), 0);
                                        return new CheckResult(CheckResult.Result.FAILED, player.getName() + " stood on water " + v + " times (can't stand on " + block.getType() + " or " + block.getRelative(BlockFace.DOWN).getType() + ")");
                                    } else {
                                        waterSpeedViolation.put(player.getName(), v + 1);
                                    }
                                } else {
                                    waterSpeedViolation.put(player.getName(), 1);
                                }
                            }
                        }
                    } else {
                        isInWaterCache.add(player.getName());
                        return PASS;
                    }
                } else {
                    isInWater.add(player.getName());
                    return PASS;
                }
            } else if (block.getRelative(BlockFace.DOWN).isLiquid() && isAscending(player) && Utilities.cantStandAt(block) && Utilities.cantStandAt(block.getRelative(BlockFace.DOWN))) {
                if (waterAscensionViolation.containsKey(player.getName())) {
                    int v = waterAscensionViolation.get(player.getName());
                    if (v >= magic.WATER_ASCENSION_VIOLATION_MAX()) {
                        waterAscensionViolation.put(player.getName(), 0);
                        return new CheckResult(CheckResult.Result.FAILED, player.getName() + " stood on water " + v + " times (can't stand on " + block.getType() + " or " + block.getRelative(BlockFace.DOWN).getType() + ")");
                    } else {
                        waterAscensionViolation.put(player.getName(), v + 1);
                    }
                } else {
                    waterAscensionViolation.put(player.getName(), 1);
                }
            } else {
                isInWater.remove(player.getName());
                isInWaterCache.remove(player.getName());
            }
        }
        return PASS;
    }

    public CheckResult checkVClip(Player player, Distance distance) {
        double from = Math.round(distance.fromY());
        double to = Math.round(distance.toY());

        if (player.isInsideVehicle() || (from == to || from < to) || Math.round(distance.getYDifference()) < 2) {
            return PASS;
        }

        for (int i = 0; i < (Math.round(distance.getYDifference())) + 1; i++) {
            Block block = new Location(player.getWorld(), player.getLocation().getX(), to + i, player.getLocation().getZ()).getBlock();
            if (block.getType() != Material.AIR && block.getType().isSolid()) {
                return new CheckResult(CheckResult.Result.FAILED, player.getName() + " tried to move through a solid block", (int) from + 3);
            }
        }

        return PASS;
    }

    public CheckResult checkYAxis(Player player, Distance distance) {
        if (distance.getYDifference() > magic.TELEPORT_MIN() || distance.getYDifference() < 0) {
            return PASS;
        }
        if (!isMovingExempt(player) && !Utilities.isClimbableBlock(player.getLocation().getBlock()) && !Utilities.isClimbableBlock(player.getLocation().add(0, -1, 0).getBlock()) && !player.isInsideVehicle() && !Utilities.isInWater(player) && !hasJumpPotion(player)) {
            double y1 = player.getLocation().getY();
            String name = player.getName();
            // Fix Y axis spam.
            if (!lastYcoord.containsKey(name) || !lastYtime.containsKey(name) || !yAxisLastViolation.containsKey(name) || !yAxisLastViolation.containsKey(name)) {
                lastYcoord.put(name, y1);
                yAxisViolations.put(name, 0);
                yAxisLastViolation.put(name, 0L);
                lastYtime.put(name, System.currentTimeMillis());
            } else {
                if (y1 > lastYcoord.get(name) && yAxisViolations.get(name) > magic.Y_MAXVIOLATIONS() && (System.currentTimeMillis() - yAxisLastViolation.get(name)) < magic.Y_MAXVIOTIME()) {
                    Location g = player.getLocation();
                    yAxisViolations.put(name, yAxisViolations.get(name) + 1);
                    yAxisLastViolation.put(name, System.currentTimeMillis());
                    if (!silentMode()) {
                        g.setY(lastYcoord.get(name));
                        player.sendMessage(ChatColor.RED + "[AntiCheat] Fly hacking on the y-axis detected.  Please wait 5 seconds to prevent getting damage.");
                        if (g.getBlock().getType() == Material.AIR) {
                            player.teleport(g);
                        }
                    }
                    return new CheckResult(CheckResult.Result.FAILED, player.getName() + " tried to fly on y-axis " + yAxisViolations.get(name) + " times (max =" + magic.Y_MAXVIOLATIONS() + ")");
                } else {
                    if (yAxisViolations.get(name) > magic.Y_MAXVIOLATIONS() && (System.currentTimeMillis() - yAxisLastViolation.get(name)) > magic.Y_MAXVIOTIME()) {
                        yAxisViolations.put(name, 0);
                        yAxisLastViolation.put(name, 0L);
                    }
                }
                long i = System.currentTimeMillis() - lastYtime.get(name);
                double diff = magic.Y_MAXDIFF() + (Utilities.isStair(player.getLocation().add(0, -1, 0).getBlock()) ? 0.5 : 0.0);
                if ((y1 - lastYcoord.get(name)) > diff && i < magic.Y_TIME()) {
                    Location g = player.getLocation();
                    yAxisViolations.put(name, yAxisViolations.get(name) + 1);
                    yAxisLastViolation.put(name, System.currentTimeMillis());
                    if (!silentMode()) {
                        g.setY(lastYcoord.get(name));
                        if (g.getBlock().getType() == Material.AIR) {
                            player.teleport(g);
                        }
                    }
                    return new CheckResult(CheckResult.Result.FAILED, player.getName() + " tried to fly on y-axis in " + i + " ms (min =" + magic.Y_TIME() + ")");
                } else {
                    if ((y1 - lastYcoord.get(name)) > magic.Y_MAXDIFF() + 1 || (System.currentTimeMillis() - lastYtime.get(name)) > magic.Y_TIME()) {
                        lastYtime.put(name, System.currentTimeMillis());
                        lastYcoord.put(name, y1);
                    }
                }
            }
        }
        // Fix Y axis spam
        return PASS;
    }

    public CheckResult checkTimer(Player player) {
        String name = player.getName();
        int step = 1;
        if (steps.containsKey(name)) {
            step = steps.get(name) + 1;
        }
        if (step == 1) {
            stepTime.put(name, System.currentTimeMillis());
        }
        increment(player, steps, step);
        if (step == magic.TIMER_STEP_CHECK()) {
            long time = System.currentTimeMillis() - stepTime.get(name);
            steps.put(name, 0);
            if (time < magic.TIMER_TIMEMIN()) {
                return new CheckResult(CheckResult.Result.FAILED, player.getName() + " tried to alter their timer, took " + step + " steps in " + time + " ms (min = " + magic.TIMER_TIMEMIN() + " ms)");
            }
        }
        return PASS;
    }

    public CheckResult checkSight(Player player, Entity entity) {
        /*if (entity instanceof LivingEntity) {
            LivingEntity le = (LivingEntity) entity;
            // Check to make sure the entity's head is not surrounded
            Block head = le.getWorld().getBlockAt((int) le.getLocation().getX(), (int) (le.getLocation().getY() + le.getEyeHeight()), (int) le.getLocation().getZ());
            boolean solid = false;
            // TODO: This sucks. See if it's possible to not have as many false-positives while still retaining most of the check.
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    for (int y = -1; y < 2; y++) {
                        if (head.getRelative(x, y, z).getTypeId() != 0) {
                            if (head.getRelative(x, y, z).getType().isSolid()) {
                                solid = true;
                                break;
                            }

                        }
                    }
                }

            }
            if (solid) {
                return PASS;
            }
            // TODO: Needs proper testing
            Location mobLocation = le.getEyeLocation();
            for (Block block : player.getLineOfSight(transparent, 5)) {
                if (Math.abs(block.getLocation().getX() - mobLocation.getX()) < 2.3 || Math.abs(block.getLocation().getZ() - mobLocation.getZ()) < 2.3) {
                    return PASS;
                }
            }
            return new CheckResult(Result.FAILED, player.getName()+" tried to damage an entity ("+le.getType()+") out of sight ");
        }*/
        return PASS;
    }

    public CheckResult checkFlight(Player player, Distance distance) {
        if (distance.getYDifference() > magic.TELEPORT_MIN()) {
            // This was a teleport, so we don't care about it.
            return PASS;
        }
        final String name = player.getName();
        final double y1 = distance.fromY();
        final double y2 = distance.toY();
        if (!isMovingExempt(player) && !Utilities.isHoveringOverWater(player.getLocation(), 1) && Utilities.cantStandAtExp(player.getLocation()) && Utilities.blockIsnt(player.getLocation().getBlock().getRelative(BlockFace.DOWN), new Material[]{Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL})) {

            if (!blocksOverFlight.containsKey(name)) {
                blocksOverFlight.put(name, 0D);
            }

            blocksOverFlight.put(name, (blocksOverFlight.get(name) + distance.getXDifference() + distance.getYDifference() + distance.getZDifference()));

            if (y1 > y2) {
                blocksOverFlight.put(name, (blocksOverFlight.get(name) - distance.getYDifference()));
            }

            if (blocksOverFlight.get(name) > magic.FLIGHT_BLOCK_LIMIT() && (y1 <= y2)) {
                return new CheckResult(CheckResult.Result.FAILED, player.getName() + " flew over " + blocksOverFlight.get(name) + " blocks (max=" + magic.FLIGHT_BLOCK_LIMIT() + ")");
            }
        } else {
            blocksOverFlight.put(name, 0D);
        }

        return PASS;
    }

    public void logAscension(Player player, double y1, double y2) {
        String name = player.getName();
        if (y1 < y2 && !isAscending.contains(name)) {
            isAscending.add(name);
        } else {
            isAscending.remove(name);
        }
    }

    public CheckResult checkAscension(Player player, double y1, double y2) {
        int max = magic.ASCENSION_COUNT_MAX();
        String string = "";
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            max += 12;
            string = " with jump potion";
        }
        Block block = player.getLocation().getBlock();
        if (!isMovingExempt(player) && !Utilities.isInWater(player) && !justBroke(player) && !Utilities.isClimbableBlock(player.getLocation().getBlock()) && !player.isInsideVehicle()) {
            String name = player.getName();
            if (y1 < y2) {
                if (!block.getRelative(BlockFace.NORTH).isLiquid() && !block.getRelative(BlockFace.SOUTH).isLiquid() && !block.getRelative(BlockFace.EAST).isLiquid() && !block.getRelative(BlockFace.WEST).isLiquid()) {
                    increment(player, ascensionCount, max);
                    if (ascensionCount.get(name) >= max) {
                        return new CheckResult(CheckResult.Result.FAILED, player.getName() + " ascended " + ascensionCount.get(name) + " times in a row (max = " + max + string + ")");
                    }
                }
            } else {
                ascensionCount.put(name, 0);
            }
        }
        return PASS;
    }

    public CheckResult checkSwing(Player player, Block block) {
        String name = player.getName();
        if (!isInstantBreakExempt(player)) {
            if (!player.getInventory().getItemInHand().containsEnchantment(Enchantment.DIG_SPEED) && !(player.getInventory().getItemInHand().getType() == Material.SHEARS && block.getType() == Material.LEAVES)) {
                if (blockPunches.get(name) != null && player.getGameMode() != GameMode.CREATIVE) {
                    int i = blockPunches.get(name);
                    if (i < magic.BLOCK_PUNCH_MIN()) {
                        return new CheckResult(CheckResult.Result.FAILED, player.getName() + " tried to break a block of " + block.getType() + " after only " + i + " punches (min=" + magic.BLOCK_PUNCH_MIN() + ")");
                    } else {
                        blockPunches.put(name, 0); // it should reset after EACH block break.
                    }
                }
            }
        }
        return PASS;
    }

    public CheckResult checkFastBreak(Player player, Block block) {
        int violations = magic.FASTBREAK_MAXVIOLATIONS();
        long timemax = isInstantBreakExempt(player) ? 0 : Utilities.calcSurvivalFastBreak(player.getInventory().getItemInHand(), block.getType());
        if (player.getGameMode() == GameMode.CREATIVE) {
            violations = magic.FASTBREAK_MAXVIOLATIONS_CREATIVE();
            timemax = magic.FASTBREAK_TIMEMAX_CREATIVE();
        }
        String name = player.getName();
        if (!fastBreakViolation.containsKey(name)) {
            fastBreakViolation.put(name, 0);
        } else {
            Long math = System.currentTimeMillis() - lastBlockBroken.get(name);
            int i = fastBreakViolation.get(name);
            if (i > violations && math < magic.FASTBREAK_MAXVIOLATIONTIME()) {
                lastBlockBroken.put(name, System.currentTimeMillis());
                if (!silentMode()) {
                    player.sendMessage(ChatColor.RED + "[AntiCheat] Fastbreaking detected. Please wait 10 seconds before breaking blocks.");
                }
                return new CheckResult(CheckResult.Result.FAILED, player.getName() + " broke blocks too fast " + i + " times in a row (max=" + violations + ")");
            } else if (fastBreakViolation.get(name) > 0 && math > magic.FASTBREAK_MAXVIOLATIONTIME()) {
                fastBreakViolation.put(name, 0);
            }
        }
        if (!fastBreaks.containsKey(name) || !lastBlockBroken.containsKey(name)) {
            if (!lastBlockBroken.containsKey(name)) {
                lastBlockBroken.put(name, System.currentTimeMillis());
            }
            if (!fastBreaks.containsKey(name)) {
                fastBreaks.put(name, 0);
            }
        } else {
            Long math = System.currentTimeMillis() - lastBlockBroken.get(name);
            if ((math != 0L && timemax != 0L)) {
                if (math < timemax) {
                    if (fastBreakViolation.containsKey(name) && fastBreakViolation.get(name) > 0) {
                        fastBreakViolation.put(name, fastBreakViolation.get(name) + 1);
                    } else {
                        fastBreaks.put(name, fastBreaks.get(name) + 1);
                    }
                    blockBreakHolder.put(name, false);
                }
                if (fastBreaks.get(name) >= magic.FASTBREAK_LIMIT() && math < timemax) {
                    int i = fastBreaks.get(name);
                    fastBreaks.put(name, 0);
                    fastBreakViolation.put(name, fastBreakViolation.get(name) + 1);
                    return new CheckResult(CheckResult.Result.FAILED, player.getName() + " tried to break " + i + " blocks in " + math + " ms (max=" + magic.FASTBREAK_LIMIT() + " in " + timemax + " ms)");
                } else if (fastBreaks.get(name) >= magic.FASTBREAK_LIMIT() || fastBreakViolation.get(name) > 0) {
                    if (!blockBreakHolder.containsKey(name) || !blockBreakHolder.get(name)) {
                        blockBreakHolder.put(name, true);
                    } else {
                        fastBreaks.put(name, fastBreaks.get(name) - 1);
                        if (fastBreakViolation.get(name) > 0) {
                            fastBreakViolation.put(name, fastBreakViolation.get(name) - 1);
                        }
                        blockBreakHolder.put(name, false);
                    }
                }
            }
        }

        lastBlockBroken.put(name, System.currentTimeMillis()); // always keep a log going.
        return PASS;
    }

    public CheckResult checkFastPlace(Player player) {
        int violations = player.getGameMode() == GameMode.CREATIVE ? magic.FASTPLACE_MAXVIOLATIONS_CREATIVE() : magic.FASTPLACE_MAXVIOLATIONS();
        long time = System.currentTimeMillis();
        String name = player.getName();
        if (!lastBlockPlaceTime.containsKey(name) || !fastPlaceViolation.containsKey(name)) {
            lastBlockPlaceTime.put(name, 0L);
            if (!fastPlaceViolation.containsKey(name)) {
                fastPlaceViolation.put(name, 0);
            }
        } else if (fastPlaceViolation.containsKey(name) && fastPlaceViolation.get(name) > violations) {
            AntiCheat.debugLog("Noted that fastPlaceViolation contains key " + name + " with value " + fastPlaceViolation.get(name));
            Long math = System.currentTimeMillis() - lastBlockPlaced.get(name);
            AntiCheat.debugLog("Player lastBlockPlaced value = " + lastBlockPlaced + ", diff=" + math);
            if (lastBlockPlaced.get(name) > 0 && math < magic.FASTPLACE_MAXVIOLATIONTIME()) {
                lastBlockPlaced.put(name, time);
                if (!silentMode()) {
                    player.sendMessage(ChatColor.RED + "[AntiCheat] Fastplacing detected. Please wait 10 seconds before placing blocks.");
                }
                return new CheckResult(CheckResult.Result.FAILED, player.getName() + " placed blocks too fast " + fastBreakViolation.get(name) + " times in a row (max=" + violations + ")");
            } else if (lastBlockPlaced.get(name) > 0 && math > magic.FASTPLACE_MAXVIOLATIONTIME()) {
                AntiCheat.debugLog("Reset facePlaceViolation for " + name);
                fastPlaceViolation.put(name, 0);
            }
        } else if (lastBlockPlaced.containsKey(name)) {
            long last = lastBlockPlaced.get(name);
            long lastTime = lastBlockPlaceTime.get(name);
            long thisTime = time - last;

            if (lastTime != 0 && thisTime < magic.FASTPLACE_TIMEMIN()) {
                lastBlockPlaceTime.put(name, (time - last));
                lastBlockPlaced.put(name, time);
                fastPlaceViolation.put(name, fastPlaceViolation.get(name) + 1);
                return new CheckResult(CheckResult.Result.FAILED, player.getName() + " tried to place a block " + thisTime + " ms after the last one (min=" + magic.FASTPLACE_TIMEMIN() + " ms)");
            }
            lastBlockPlaceTime.put(name, (time - last));
        }
        lastBlockPlaced.put(name, time);
        return PASS;
    }

    public void logBowWindUp(Player player) {
        bowWindUp.put(player.getName(), System.currentTimeMillis());
    }

    public void logEatingStart(Player player) {
        startEat.put(player.getName(), System.currentTimeMillis());
    }

    public void logHeal(Player player) {
        lastHeal.put(player.getName(), System.currentTimeMillis());
    }

    public CheckResult checkChatSpam(Player player, String msg) {
        String name = player.getName();
        User user = manager.getUserManager().getUser(name);
        if (user.getLastMessageTime() != -1) {
            for (int i = 0; i < 2; i++) {
                String m = user.getMessage(i);
                if (m == null) {
                    break;
                }
                Long l = user.getMessageTime(i);

                if (System.currentTimeMillis() - l > magic.CHAT_REPEAT_MIN() * 100) {
                    user.clearMessages();
                    break;
                } else {
                    if (manager.getConfiguration().getConfig().blockChatSpamRepetition.getValue() && m.equalsIgnoreCase(msg) && i == 1) {
                        manager.getLoggingManager().logFineInfo(player.getName() + " spam-repeated \"" + msg + "\"");
                        return new CheckResult(CheckResult.Result.FAILED, lang.SPAM_WARNING());
                    } else if (manager.getConfiguration().getConfig().blockChatSpamSpeed.getValue() && System.currentTimeMillis() - user.getLastCommandTime() < magic.COMMAND_MIN() * 2) {
                        manager.getLoggingManager().logFineInfo(player.getName() + " spammed quickly \"" + msg + "\"");
                        return new CheckResult(CheckResult.Result.FAILED, lang.SPAM_WARNING());
                    }
                }
            }
        }
        user.addMessage(msg);
        return PASS;
    }

    public CheckResult checkCommandSpam(Player player, String cmd) {
        String name = player.getName();
        User user = manager.getUserManager().getUser(name);
        if (user.getLastCommandTime() != -1) {
            for (int i = 0; i < 2; i++) {
                String m = user.getCommand(i);
                if (m == null) {
                    break;
                }
                Long l = user.getCommandTime(i);

                if (System.currentTimeMillis() - l > magic.COMMAND_REPEAT_MIN() * 100) {
                    user.clearCommands();
                    break;
                } else {
                    if (manager.getConfiguration().getConfig().blockCommandSpamRepetition.getValue() && m.equalsIgnoreCase(cmd) && i == 1) {
                        return new CheckResult(CheckResult.Result.FAILED, lang.SPAM_WARNING());
                    } else if (manager.getConfiguration().getConfig().blockCommandSpamSpeed.getValue() && System.currentTimeMillis() - user.getLastCommandTime() < magic.COMMAND_MIN() * 2) {
                        return new CheckResult(CheckResult.Result.FAILED, lang.SPAM_WARNING());
                    }
                }
            }
        }
        user.addCommand(cmd);
        return PASS;
    }

    public CheckResult checkInventoryClicks(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return PASS;
        }
        String name = player.getName();
        int clicks = 1;
        if (inventoryClicks.containsKey(name)) {
            clicks = inventoryClicks.get(name) + 1;
        }
        inventoryClicks.put(name, clicks);
        if (clicks == 1) {
            inventoryTime.put(name, System.currentTimeMillis());
        } else if (clicks == magic.INVENTORY_CHECK()) {
            long time = System.currentTimeMillis() - inventoryTime.get(name);
            inventoryClicks.put(name, 0);
            if (time < magic.INVENTORY_TIMEMIN()) {
                return new CheckResult(CheckResult.Result.FAILED, player.getName() + " clicked inventory slots " + clicks + " times in " + time + " ms (max=" + magic.INVENTORY_CHECK() + " in " + magic.INVENTORY_TIMEMIN() + " ms)");
            }
        }
        return PASS;
    }

    public CheckResult checkAutoTool(Player player) {
        if (itemInHand.containsKey(player.getName()) && itemInHand.get(player.getName()) != player.getItemInHand().getType()) {
            return new CheckResult(CheckResult.Result.FAILED, player.getName() + " switched tools too fast (had " + itemInHand.get(player.getName()) + ", has " + player.getItemInHand().getType() + ")");
        } else {
            return PASS;
        }
    }

    public CheckResult checkSprintDamage(Player player) {
        if (isDoing(player, sprinted, magic.SPRINT_MIN())) {
            return new CheckResult(CheckResult.Result.FAILED, player.getName() + " sprinted and damaged an entity too fast (min sprint=" + magic.SPRINT_MIN() + " ms)");
        } else {
            return PASS;
        }
    }

    public CheckResult checkAnimation(Player player, Entity e) {
        if (!justAnimated(player)) {
            return new CheckResult(CheckResult.Result.FAILED, player.getName() + " didn't animate before damaging a " + e.getType());
        } else {
            return PASS;
        }
    }

    public CheckResult checkFastHeal(Player player) {
        if (lastHeal.containsKey(player.getName())) // Otherwise it was modified by a plugin, don't worry about it.
        {
            long l = lastHeal.get(player.getName());
            lastHeal.remove(player.getName());
            if ((System.currentTimeMillis() - l) < magic.HEAL_TIME_MIN()) {
                return new CheckResult(CheckResult.Result.FAILED, player.getName() + " healed too quickly (time=" + (System.currentTimeMillis() - l) + " ms, min=" + magic.HEAL_TIME_MIN() + " ms)");
            }
        }
        return PASS;
    }

    public CheckResult checkFastEat(Player player) {
        if (startEat.containsKey(player.getName())) // Otherwise it was modified by a plugin, don't worry about it.
        {
            long l = startEat.get(player.getName());
            startEat.remove(player.getName());
            if ((System.currentTimeMillis() - l) < magic.EAT_TIME_MIN()) {
                return new CheckResult(CheckResult.Result.FAILED, player.getName() + " ate too quickly (time=" + (System.currentTimeMillis() - l) + " ms, min=" + magic.EAT_TIME_MIN() + " ms)");
            }
        }
        return PASS;
    }

    public void logAnimation(final Player player) {
        animated.put(player.getName(), System.currentTimeMillis());
        increment(player, blockPunches, magic.BLOCK_PUNCH_MIN());
        itemInHand.put(player.getName(), player.getItemInHand().getType());
        interactionCount.put(player.getName(), 0);
    }

    public void logInstantBreak(final Player player) {
        instantBreakExempt.put(player.getName(), System.currentTimeMillis());
    }

    public boolean isInstantBreakExempt(Player player) {
        return isDoing(player, instantBreakExempt, magic.INSTANT_BREAK_TIME());
    }

    public void logSprint(final Player player) {
        sprinted.put(player.getName(), System.currentTimeMillis());
    }

    public boolean isHoveringOverWaterAfterViolation(Player player) {
        if (waterSpeedViolation.containsKey(player.getName())) {
            if (waterSpeedViolation.get(player.getName()) >= magic.WATER_SPEED_VIOLATION_MAX() && Utilities.isHoveringOverWater(player.getLocation())) {
                return true;
            }
        }
        return false;
    }

    public void logBlockBreak(final Player player) {
        brokenBlock.put(player.getName(), System.currentTimeMillis());
        resetAnimation(player);
    }

    public boolean justBroke(Player player) {
        return isDoing(player, brokenBlock, magic.BLOCK_BREAK_MIN());
    }

    public void logVelocity(final Player player) {
        velocitized.put(player.getName(), System.currentTimeMillis());
    }

    public boolean justVelocity(Player player) {
        return (velocitized.containsKey(player.getName()) ? (System.currentTimeMillis() - velocitized.get(player.getName())) < magic.VELOCITY_CHECKTIME() : false);
    }

    public boolean extendVelocityTime(final Player player) {
        if (velocitytrack.containsKey(player.getName())) {
            velocitytrack.put(player.getName(), velocitytrack.get(player.getName()) + 1);
            if (velocitytrack.get(player.getName()) > magic.VELOCITY_MAXTIMES()) {
                velocitized.put(player.getName(), System.currentTimeMillis() + magic.VELOCITY_PREVENT());
                manager.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(manager.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        velocitytrack.put(player.getName(), 0);
                    }
                }, magic.VELOCITY_SCHETIME() * 20L);
                return true;
            }
        } else {
            velocitytrack.put(player.getName(), 0);
        }

        return false;
    }

    public void logBlockPlace(final Player player) {
        placedBlock.put(player.getName(), System.currentTimeMillis());
    }

    public boolean justPlaced(Player player) {
        return isDoing(player, placedBlock, magic.BLOCK_PLACE_MIN());
    }

    public void resetAnimation(final Player player) {
        animated.remove(player.getName());
        blockPunches.put(player.getName(), 0);
    }

    private boolean justAnimated(Player player) {
        String name = player.getName();
        if (animated.containsKey(name)) {
            long time = System.currentTimeMillis() - animated.get(name);
            int count = interactionCount.get(player.getName()) + 1;
            interactionCount.put(player.getName(), count);

            if (count > magic.ANIMATION_INTERACT_MAX()) {
                animated.remove(player.getName());
                return false;
            }
            return time < magic.ANIMATION_MIN();
        } else {
            return false;
        }
    }

    public void logDamage(final Player player, int type) {
        long time;
        switch (type) {
            case 1:
                time = magic.DAMAGE_TIME();
                break;
            case 2:
                time = magic.KNOCKBACK_DAMAGE_TIME();
                break;
            case 3:
                time = magic.EXPLOSION_DAMAGE_TIME();
                break;
            default:
                time = magic.DAMAGE_TIME();
                break;

        }
        movingExempt.put(player.getName(), System.currentTimeMillis() + time);
        // Only map in which termination time is calculated beforehand.
    }

    public void logEnterExit(final Player player) {
        movingExempt.put(player.getName(), System.currentTimeMillis() + magic.ENTERED_EXITED_TIME());
    }

    public void logToggleSneak(final Player player) {
        movingExempt.put(player.getName(), System.currentTimeMillis() + magic.SNEAK_TIME());
    }

    public void logTeleport(final Player player) {
        movingExempt.put(player.getName(), System.currentTimeMillis() + magic.TELEPORT_TIME());

        /* Data for fly/speed should be reset */
        nofallViolation.remove(player.getName());
        blocksOverFlight.remove(player.getName());
        yAxisViolations.remove(player.getName());
        yAxisLastViolation.remove(player.getName());
        lastYcoord.remove(player.getName());
        lastYtime.remove(player.getName());
    }

    public void logExitFly(final Player player) {
        movingExempt.put(player.getName(), System.currentTimeMillis() + magic.EXIT_FLY_TIME());
    }

    public void logJoin(final Player player) {
        movingExempt.put(player.getName(), System.currentTimeMillis() + magic.JOIN_TIME());
    }

    public boolean isMovingExempt(Player player) {
        return isDoing(player, movingExempt, -1);
    }

    public boolean isAscending(Player player) {
        return isAscending.contains(player.getName());
    }

    public boolean isSpeedExempt(Player player) {
        return isMovingExempt(player) || justVelocity(player);
    }

    private boolean isDoing(Player player, Map<String, Long> map, double max) {
        if (map.containsKey(player.getName())) {
            if (max != -1) {
                if (((System.currentTimeMillis() - map.get(player.getName())) / 1000) > max) {
                    map.remove(player.getName());
                    return false;
                } else {
                    return true;
                }
            } else {
                // Termination time has already been calculated
                if (map.get(player.getName()) < System.currentTimeMillis()) {
                    map.remove(player.getName());
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public boolean hasJumpPotion(Player player) {
        return player.hasPotionEffect(PotionEffectType.JUMP);
    }

    public boolean hasSpeedPotion(Player player) {
        return player.hasPotionEffect(PotionEffectType.SPEED);
    }

    public void processChatSpammer(Player player) {
        User user = manager.getUserManager().getUser(player.getName());
        int level = chatLevel.containsKey(user.getName()) ? chatLevel.get(user.getName()) : 0;
        if (player != null && player.isOnline() && level >= magic.CHAT_ACTION_ONE_LEVEL()) {
            String event = level >= magic.CHAT_ACTION_TWO_LEVEL() ? manager.getConfiguration().getConfig().chatSpamActionTwo.getValue() : manager.getConfiguration().getConfig().chatSpamActionOne.getValue();
            manager.getUserManager().execute(manager.getUserManager().getUser(player.getName()), Utilities.stringToList(event), CheckType.CHAT_SPAM, lang.SPAM_KICK_REASON(), Utilities.stringToList(lang.SPAM_WARNING()), lang.SPAM_BAN_REASON());
        }
        chatLevel.put(user.getName(), level + 1);
    }

    public void processCommandSpammer(Player player) {
        User user = manager.getUserManager().getUser(player.getName());
        int level = commandLevel.containsKey(user.getName()) ? commandLevel.get(user.getName()) : 0;
        if (player != null && player.isOnline() && level >= magic.COMMAND_ACTION_ONE_LEVEL()) {
            String event = level >= magic.COMMAND_ACTION_TWO_LEVEL() ? manager.getConfiguration().getConfig().commandSpamActionTwo.getValue() : manager.getConfiguration().getConfig().commandSpamActionOne.getValue();
            manager.getUserManager().execute(manager.getUserManager().getUser(player.getName()), Utilities.stringToList(event), CheckType.COMMAND_SPAM, lang.SPAM_KICK_REASON(), Utilities.stringToList(lang.SPAM_WARNING()), lang.SPAM_BAN_REASON());
        }
        commandLevel.put(user.getName(), level + 1);
    }

    public int increment(Player player, Map<String, Integer> map, int num) {
        String name = player.getName();
        if (map.get(name) == null) {
            map.put(name, 1);
            return 1;
        } else {
            int amount = map.get(name) + 1;
            if (amount < num + 1) {
                map.put(name, amount);
                return amount;
            } else {
                map.put(name, num);
                return num;
            }
        }
    }

    public boolean silentMode() {
        return manager.getConfiguration().getConfig().silentMode.getValue();
    }
}

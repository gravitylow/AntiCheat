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

package net.h31ix.anticheat.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.h31ix.anticheat.Anticheat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

public final class Utilities {
    private static final List<Material> INSTANT_BREAK = new ArrayList<Material>();
    private static final List<Material> FOOD = new ArrayList<Material>();
    private static final List<Material> INTERACTABLE = new ArrayList<Material>();
    private static final List<Material> AXES = new ArrayList<Material>();
    private static final List<Material> PICKAXES = new ArrayList<Material>();
    private static final List<Material> SHEARS = new ArrayList<Material>();
    private static final List<Material> SHOVELS = new ArrayList<Material>();
    private static final List<Material> ANY = new ArrayList<Material>();
    private static final Map<Material, Material> COMBO = new HashMap<Material, Material>();
    public static final String SPY_METADATA = "ac-spydata";

    /**
     * Send a hack level alert to players and console
     *
     * @param message List of strings to send as the alert
     *
     */
    public static void alert(List<String> message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Permission.SYSTEM_ALERT.get(player)) {
                for (String msg : message) {
                    player.sendMessage(msg);
                }
            }
        }
        if (Anticheat.getManager().getConfiguration().logConsole()) {
            for (String msg : message) {
                Anticheat.getManager().log(msg);
            }
        }
        if (Anticheat.getManager().getConfiguration().getFileLogLevel() != 0) {
            for (String msg : message) {
                Anticheat.getManager().log(msg, 1);
            }
        }
    }

    /**
     * Determine whether a player cannot stand on or around the given block
     *
     * @param block the block to check
     * @return true if the player should be unable to stand here
     */
    public static boolean cantStandAt(Block block) {
        return !canStand(block) && cantStandClose(block) && cantStandFar(block);
    }

    /**
     * Determine whether a player should be unable to stand at a given location
     *
     * @param location the location to check
     * @return true if the player should be unable to stand here
     */
    public static boolean cantStandAtExp(Location location) {
        return cantStandAt(new Location(location.getWorld(), fixXAxis(location.getX()), location.getY() - 0.01D, location.getBlockZ()).getBlock());
    }

    /**
     * Determine whether cannot stand on the block's immediately surroundings (North, East, South, West)
     *
     * @param block the block to check
     * @return true if a player cannot stand in the immediate vicinity
     */
    public static boolean cantStandClose(Block block) {
        return !canStand(block.getRelative(BlockFace.NORTH)) && !canStand(block.getRelative(BlockFace.EAST)) && !canStand(block.getRelative(BlockFace.SOUTH)) && !canStand(block.getRelative(BlockFace.WEST));
    }

    /**
     * Determine whether cannot stand on the block's outer surroundings
     *
     * @param block the block to check
     * @return true if a player cannot stand in areas further away from the block
     */
    public static boolean cantStandFar(Block block) {
        return !canStand(block.getRelative(BlockFace.NORTH_WEST)) && !canStand(block.getRelative(BlockFace.NORTH_EAST)) && !canStand(block.getRelative(BlockFace.SOUTH_WEST)) && !canStand(block.getRelative(BlockFace.SOUTH_EAST));
    }

    /**
     * Determine whether a player can stand on the given block
     *
     * @param block the block to check
     * @return true if the player can stand here
     */
    public static boolean canStand(Block block) {
        return !(block.isLiquid() || block.getType() == Material.AIR);
    }

    /**
     * Determine whether a player is fully submerged in water
     *
     * @param player the player's location
     * @return true if the player is fully in the water
     */
    public static boolean isFullyInWater(Location player) {
        double touchedX = fixXAxis(player.getX());
        
        // Yes, this doesn't make sense, but it's supposed to fix some false positives in water walk.
        // Think of it as 2 negatives = a positive :)
        if (!(new Location(player.getWorld(), touchedX, player.getY(), player.getBlockZ()).getBlock()).isLiquid() && !(new Location(player.getWorld(), touchedX, Math.round(player.getY()), player.getBlockZ()).getBlock()).isLiquid()) {
            return true;
        }
        
        return (new Location(player.getWorld(), touchedX, player.getY(), player.getBlockZ()).getBlock()).isLiquid() && (new Location(player.getWorld(), touchedX, Math.round(player.getY()), player.getBlockZ()).getBlock()).isLiquid();
    }

    /**
     * Fixes a player's X position to determine the block they are on, even if they're on the edge
     * @param x player's x position
     * @return fixed x position
     */
    public static double fixXAxis(double x) {
        /* For Z axis, just use Math.round(xaxis); */
        double touchedX = x;
        double rem = touchedX - Math.round(touchedX) + 0.01D;
        if (rem < 0.30D) {
            touchedX = NumberConversions.floor(x) - 1;
        }
        return touchedX;
    }

    /**
     * Determine if the player is hovering over water with the given limit
     *
     * @param player the player's location
     * @param blocks max blocks to check
     * @return true if the player is hovering over water
     */
    public static boolean isHoveringOverWater(Location player, int blocks) {
        for (int i = player.getBlockY(); i > player.getBlockY() - blocks; i--) {
            Block newloc = (new Location(player.getWorld(), player.getBlockX(), i, player.getBlockZ())).getBlock();
            if (newloc.getTypeId() != 0 && newloc.isLiquid()) {
                return true;
            } else if (newloc.getTypeId() != 0) {
                return false;
            }
        }
        
        return false;
    }

    /**
     * Determine if the player is hovering over water with a hard limit of 25 blocks
     *
     * @param player the player's location
     * @return true if the player is hovering over water
     */
    public static boolean isHoveringOverWater(Location player) {
        return isHoveringOverWater(player, 25);
    }

    /**
     * Determine whether a material will break instantly when hit
     *
     * @param m the material to check
     * @return true if the material is instant break
     */
    public static boolean isInstantBreak(Material m) {
        return INSTANT_BREAK.contains(m);
    }

    /**
     * Determine whether a material is edible
     *
     * @param m the material to check
     * @return true if the material is food
     */
    public static boolean isFood(Material m) {
        return FOOD.contains(m);
    }

    /**
     * Determine whether a block is a slab
     *
     * @param block block to check
     * @return true if slab
     */
    public static boolean isSlab(Block block) {
        int id = block.getTypeId();
        return id == 43 || id == 44 || id == 125 || id == 126;
    }

    /**
     * Determine whether a block is a stair
     *
     * @param block block to check
     * @return true if stair
     */
    public static boolean isStair(Block block) {
        int id = block.getTypeId();
        return id == 53 || id == 67 || id == 108 || id == 109 || id == 114 || id == 128 || id == 134 || id == 135 || id == 136;
    }

    /**
     * Determine whether a player can interact with this material
     *
     * @param m material to check
     * @return true if interactable
     */
    public static boolean isInteractable(Material m) {
        return INTERACTABLE.contains(m);
    }

    /**
     * Determine whether a player is sprinting or flying
     *
     * @param player player to check
     * @return true if sprinting or flying
     */
    public static boolean sprintFly(Player player) {
        return player.isSprinting() || player.isFlying();
    }

    /**
     * Determine whether a player is standing on a lily pad
     *
     * @param player player to check
     * @return true if on lily pad
     */
    public static boolean isOnLilyPad(Player player) {
        Block block = player.getLocation().getBlock();
        Material lily = Material.WATER_LILY;
        // TODO: Can we fix X this?
        return block.getType() == lily || block.getRelative(BlockFace.NORTH).getType() == lily || block.getRelative(BlockFace.SOUTH).getType() == lily || block.getRelative(BlockFace.EAST).getType() == lily || block.getRelative(BlockFace.WEST).getType() == lily;
    }

    /**
     * Determine whether a player is fully submersed in liquid
     *
     * @param player player to check
     * @return true if submersed
     */
    public static boolean isSubmersed(Player player) {
        return player.getLocation().getBlock().isLiquid() && player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid();
    }

    /**
     * Determine whether a player is in water
     *
     * @param player player to check
     * @return true if in water
     */
    public static boolean isInWater(Player player) {
        return player.getLocation().getBlock().isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid();
    }

    /**
     * Determine whether a player is in a web
     *
     * @param player player to check
     * @return true if in web
     */
    public static boolean isInWeb(Player player) {
        return player.getLocation().getBlock().getType() == Material.WEB || player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WEB || player.getLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.WEB;
    }

    /**
     * Determine whether a block is climbable
     *
     * @param block block to check
     * @return true if climbable
     */
    public static boolean isClimbableBlock(Block block) {
        return block.getType() == Material.VINE || block.getType() == Material.LADDER;
    }

    /**
     * Determine whether a player is on a vine (can be free hanging)
     *
     * @param player to check
     * @return true if on vine
     */
    public static boolean isOnVine(Player player) {
        return player.getLocation().getBlock().getType() == Material.VINE;
    }

    /**
     * Determine whether a String can be cast to an Integer
     *
     * @param string text to check
     * @return true if int
     */
    public static boolean isInt(String string) {
        boolean x = false;
        try {
            Integer.parseInt(string);
            x = true;
        } catch (Exception ex) {}
        return x;
    }

    /**
     * Calculate the time in milliseconds that it should take to break the given block with the given tool
     *
     * @param tool tool to check
     * @param block block to check
     * @return time in milliseconds to break
     */
    public static long calcSurvivalFastBreak(ItemStack tool, Material block) {
        if(isInstantBreak(block) || (tool.getType() == Material.SHEARS && block.getId() == Material.LEAVES.getId())) {
            return 0;
        }
        double bhardness = BlockHardness.getBlockHardness(block);
        double thardness = ToolHardness.getToolHardness(tool.getType());
        long enchantlvl = (long) tool.getEnchantmentLevel(Enchantment.DIG_SPEED);
        
        long result = Math.round((bhardness * thardness) * 0.10 * 10000);
        
        result += 150;
        
        if (enchantlvl > 0) {
            result /= enchantlvl * enchantlvl + 1L;
        }
        
        if (result > 25000) {
            result = 25000;
        } else if (result < 0) {
            result = 0;
        }

        if(isQuickCombo(tool, block)) {
            result = result/2;
        }
        
        return result;
    }

    /**
     * Determine whether the given tool is a combination that makes the breaking of this block faster
     *
     * @param tool tool to check
     * @param block block to check
     * @return true if quick combo
     */
    private static boolean isQuickCombo(ItemStack tool, Material block) {
        for(Material t : COMBO.keySet()) {
            if(tool.getType() == t && COMBO.get(t) == block) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine if a block ISN'T one of the specified types
     *
     * @param block block to check
     * @param materials array of possible materials
     * @return true if the block isn't any of the materials
     */
    public static boolean blockIsnt(Block block, Material[] materials) {
        Material type = block.getType();
        for(Material m : materials) {
            if(m == type) {
                return false;
            }
        }
        return false;
    }

    /**
     * Parse a COMMAND[] input to a set of commands to execute
     *
     * @param command input string
     * @return parsed commands
     */
    public static String [] getCommands(String command) {
        return command.replaceAll("COMMAND\\[", "").replaceAll("]", "").split(";");
    }

    /**
     * Remove all whitespace from the given string to ready it for parsing
     *
     * @param string the string to parse
     * @return string with whitespace removed
     */
    public static String removeWhitespace(String string) {
        return string.replaceAll(" ", "");
    }

    /**
     * Determine if a player has the given enchantment on their armor
     *
     * @param player player to check
     * @param e enchantment to check
     * @return true if the armor has this enchantment
     */
    public static boolean hasArmorEnchantment(Player player, Enchantment e) {
        for(ItemStack is : player.getInventory().getArmorContents()) {
            if(is != null && is.containsEnchantment(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create a list with the given string for execution
     *
     * @param string the string to parse
     * @return ArrayList with string
     */
    public static ArrayList<String> stringToList(String string) {
        ArrayList<String> i = new ArrayList<String>();
        i.add(string);
        return i;
    }
    
    static {
        // START INSTANT BREAK MATERIALS
        INSTANT_BREAK.add(Material.RED_MUSHROOM);
        INSTANT_BREAK.add(Material.RED_ROSE);
        INSTANT_BREAK.add(Material.BROWN_MUSHROOM);
        INSTANT_BREAK.add(Material.YELLOW_FLOWER);
        INSTANT_BREAK.add(Material.REDSTONE);
        INSTANT_BREAK.add(Material.REDSTONE_TORCH_OFF);
        INSTANT_BREAK.add(Material.REDSTONE_TORCH_ON);
        INSTANT_BREAK.add(Material.REDSTONE_WIRE);
        INSTANT_BREAK.add(Material.LONG_GRASS);
        INSTANT_BREAK.add(Material.PAINTING);
        INSTANT_BREAK.add(Material.WHEAT);
        INSTANT_BREAK.add(Material.SUGAR_CANE);
        INSTANT_BREAK.add(Material.SUGAR_CANE_BLOCK);
        INSTANT_BREAK.add(Material.DIODE);
        INSTANT_BREAK.add(Material.DIODE_BLOCK_OFF);
        INSTANT_BREAK.add(Material.DIODE_BLOCK_ON);
        INSTANT_BREAK.add(Material.SAPLING);
        INSTANT_BREAK.add(Material.TORCH);
        INSTANT_BREAK.add(Material.CROPS);
        INSTANT_BREAK.add(Material.SNOW);
        INSTANT_BREAK.add(Material.TNT);
        INSTANT_BREAK.add(Material.POTATO);
        INSTANT_BREAK.add(Material.CARROT);
        // END INSTANT BREAK MATERIALS

        // START INTERACTABLE MATERIALS
        INTERACTABLE.add(Material.STONE_BUTTON);
        INTERACTABLE.add(Material.LEVER);
        INTERACTABLE.add(Material.CHEST);
        // END INTERACTABLE MATERIALS

        // START FOOD
        FOOD.add(Material.COOKED_BEEF);
        FOOD.add(Material.COOKED_CHICKEN);
        FOOD.add(Material.COOKED_FISH);
        FOOD.add(Material.GRILLED_PORK);
        FOOD.add(Material.PORK);
        FOOD.add(Material.MUSHROOM_SOUP);
        FOOD.add(Material.RAW_BEEF);
        FOOD.add(Material.RAW_CHICKEN);
        FOOD.add(Material.RAW_FISH);
        FOOD.add(Material.APPLE);
        FOOD.add(Material.GOLDEN_APPLE);
        FOOD.add(Material.MELON);
        FOOD.add(Material.COOKIE);
        FOOD.add(Material.BREAD);
        FOOD.add(Material.SPIDER_EYE);
        FOOD.add(Material.ROTTEN_FLESH);
        FOOD.add(Material.POTATO_ITEM);
        // END FOOD

        // START COMBOS
        COMBO.put(Material.SHEARS, Material.WOOL);
        COMBO.put(Material.IRON_SWORD, Material.WEB);
        COMBO.put(Material.DIAMOND_SWORD, Material.WEB);
        COMBO.put(Material.STONE_SWORD, Material.WEB);
        COMBO.put(Material.WOOD_SWORD, Material.WEB);
        // END COMBOS
    }
}

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

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

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
    
    public static boolean cantStandAt(Block block) {
        return !canStand(block) && cantStandClose(block) && cantStandFar(block);
    }
    
    public static boolean cantStandAtExp(Location block) {
        // This is a experimental check for checking whether you can't stand at or whatever.
        Block nblock = new Location(block.getWorld(), fixXAxis(block.getX()), block.getY() - 0.01D, block.getBlockZ()).getBlock();
        return cantStandAt(nblock);
    }
    
    public static boolean cantStandClose(Block block) {
        return !canStand(block.getRelative(BlockFace.NORTH)) && !canStand(block.getRelative(BlockFace.EAST)) && !canStand(block.getRelative(BlockFace.SOUTH)) && !canStand(block.getRelative(BlockFace.WEST));
    }
    
    public static boolean cantStandFar(Block block) {
        return !canStand(block.getRelative(BlockFace.NORTH_WEST)) && !canStand(block.getRelative(BlockFace.NORTH_EAST)) && !canStand(block.getRelative(BlockFace.SOUTH_WEST)) && !canStand(block.getRelative(BlockFace.SOUTH_EAST));
    }
    
    public static boolean canStand(Block block) {
        return !(block.isLiquid() || block.getType() == Material.AIR);
    }
    
    public static boolean isFullyInWater(Location player) {
        double touchedX = fixXAxis(player.getX());
        
        // Yes, this doesn't make sense, but it's supposed to fix some false positives in water walk.
        // Think of it as 2 negatives = a positive :)
        if (!(new Location(player.getWorld(), touchedX, player.getY(), player.getBlockZ()).getBlock()).isLiquid() && !(new Location(player.getWorld(), touchedX, Math.round(player.getY()), player.getBlockZ()).getBlock()).isLiquid()) { return true; }
        
        return (new Location(player.getWorld(), touchedX, player.getY(), player.getBlockZ()).getBlock()).isLiquid() && (new Location(player.getWorld(), touchedX, Math.round(player.getY()), player.getBlockZ()).getBlock()).isLiquid();
    }
    
    public static double fixXAxis(double x) {
        /* Really really really useful function for those who are on the edges of blocks */
        /* For Z axis, just use Math.round(xaxis); */
        double touchedX = x;
        double rem = touchedX - Math.round(touchedX) + 0.01D;
        if (rem < 0.30D) {
            touchedX = NumberConversions.floor(x) - 1;
        }
        return touchedX;
    }
    
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
    
    public static boolean isHoveringOverWater(Location player) {
        return isHoveringOverWater(player, 25);
    }
    
    public static boolean isInstantBreak(Material m) {
        return INSTANT_BREAK.contains(m);
    }
    
    public static boolean isFood(Material m) {
        return FOOD.contains(m);
    }
    
    public static boolean isSlab(Block block) {
        int id = block.getTypeId();
        return id == 43 || id == 44 || id == 125 || id == 126;
    }
    
    public static boolean isStair(Block block) {
        int id = block.getTypeId();
        return id == 53 || id == 67 || id == 108 || id == 109 || id == 114 || id == 128 || id == 134 || id == 135 || id == 136;
    }
    
    public static boolean isInteractable(Material m) {
        return INTERACTABLE.contains(m);
    }
    
    public static boolean sprintFly(Player player) {
        return player.isSprinting() || player.isFlying();
    }
    
    public static boolean isOnLilyPad(Player player) {
        Block block = player.getLocation().getBlock();
        Material lily = Material.WATER_LILY;
        return block.getType() == lily || block.getRelative(BlockFace.NORTH).getType() == lily || block.getRelative(BlockFace.SOUTH).getType() == lily || block.getRelative(BlockFace.EAST).getType() == lily || block.getRelative(BlockFace.WEST).getType() == lily;
    }
    
    public static boolean isSubmersed(Player player) {
        return player.getLocation().getBlock().isLiquid() && player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid();
    }
    
    public static boolean isInWater(Player player) {
        return player.getLocation().getBlock().isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid();
    }
    
    public static boolean isInWeb(Player player) {
        return player.getLocation().getBlock().getType() == Material.WEB || player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WEB || player.getLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.WEB;
    }
    
    public static boolean isClimbableBlock(Block block) {
        return block.getType() == Material.VINE || block.getType() == Material.LADDER;
    }
    
    public static boolean isOnVine(Player player) {
        return player.getLocation().getBlock().getType() == Material.VINE;
    }
    
    public static boolean isInt(String string) {
        boolean x = false;
        try {
            Integer.parseInt(string);
            x = true;
        } catch (Exception ex) {}
        return x;
    }
    
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

    private static boolean isQuickCombo(ItemStack tool, Material block) {
        for(Material t : COMBO.keySet()) {
            if(tool.getType() == t && COMBO.get(t) == block) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean blockIsnt(Block block, Material[] materials) {
        Material type = block.getType();
        for(Material m : materials) {
            if(m == type) {
                return false;
            }
        }
        return false;
    }    
    
    public static String [] getCommands(String command) {
        return command.replaceAll("COMMAND\\[", "").replaceAll("]", "").split(";");
    }

    public static boolean hasArmorEnchantment(Player player, Enchantment e) {
        for(ItemStack is : player.getInventory().getArmorContents()) {
            if(is != null && is.containsEnchantment(e)) {
                return true;
            }
        }
        return false;
    }
    
    static {
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

        INTERACTABLE.add(Material.STONE_BUTTON);
        INTERACTABLE.add(Material.LEVER);
        INTERACTABLE.add(Material.CHEST);

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

        COMBO.put(Material.SHEARS, Material.WOOL);
        COMBO.put(Material.IRON_SWORD, Material.WEB);
        COMBO.put(Material.DIAMOND_SWORD, Material.WEB);
        COMBO.put(Material.STONE_SWORD, Material.WEB);
        COMBO.put(Material.WOOD_SWORD, Material.WEB);
    }
}

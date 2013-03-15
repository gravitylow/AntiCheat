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

import org.bukkit.Material;

public enum BlockHardness {
    STONE(Material.STONE, 1.50D),
    GRASS(Material.GRASS, 0.60D),
    DIRT(Material.DIRT, 0.50D),
    COBBLESTONE(Material.COBBLESTONE, 2.00D),
    WOOD(Material.WOOD, 2.00D),
    SAPLING(Material.SAPLING, 0.00D),
    WATER(Material.WATER, 100.00D),
    STATIONARY_WATER(Material.STATIONARY_WATER, 100.00D),
    LAVA(Material.LAVA, 0.00D),
    STATIONARY_LAVA(Material.STATIONARY_LAVA, 100.00D),
    SAND(Material.SAND, 0.50D),
    GRAVEL(Material.GRAVEL, 0.60D),
    GOLD_ORE(Material.GOLD_ORE, 3.00D),
    IRON_ORE(Material.IRON_ORE, 3.00D),
    COAL_ORE(Material.COAL_ORE, 3.00D),
    LOG(Material.LOG, 2.00D),
    LEAVES(Material.LEAVES, 0.20D),
    SPONGE(Material.SPONGE, 0.60D),
    GLASS(Material.GLASS, 0.30D),
    LAPIS_ORE(Material.LAPIS_ORE, 3.00D),
    LAPIS_BLOCK(Material.LAPIS_BLOCK, 3.00D),
    DISPENSER(Material.DISPENSER, 3.50D),
    DROPPER(Material.DROPPER, 3.50D),
    SANDSTONE(Material.SANDSTONE, 0.80D),
    NOTE_BLOCK(Material.NOTE_BLOCK, 0.80D),
    BED(Material.BED, 0.20D),
    RAILS(Material.RAILS, 0.70D),
    DETECTOR_RAIL(Material.DETECTOR_RAIL, 0.70D),
    POWERED_RAIL(Material.POWERED_RAIL, 0.70D),
    ACTIVATOR_RAIL(Material.ACTIVATOR_RAIL, 0.70D),
    WEB(Material.WEB, 4.00D),
    LONG_GRASS(Material.LONG_GRASS, 0.00D),
    DEAD_BUSH(Material.DEAD_BUSH, 0.00D),
    WOOL(Material.WOOL, 0.80D),
    YELLOW_FLOWER(Material.YELLOW_FLOWER, 0.00D),
    RED_ROSE(Material.RED_ROSE, 0.00D),
    BROWN_MUSHROOM(Material.BROWN_MUSHROOM, 0.00D),
    RED_MUSHROOM(Material.RED_MUSHROOM, 0.00D),
    GOLD_BLOCK(Material.GOLD_BLOCK, 3.00D),
    IRON_BLOCK(Material.IRON_BLOCK, 5.00D),
    DOUBLE_STEP(Material.DOUBLE_STEP, 2.00D),
    STEP(Material.STEP, 2.00D),
    BRICK(Material.BRICK, 2.00D),
    TNT(Material.TNT, 0.00D),
    BOOKSHELF(Material.BOOKSHELF, 1.50D),
    MOSSY_COBBLESTONE(Material.MOSSY_COBBLESTONE, 2.00D),
    OBSIDIAN(Material.OBSIDIAN, 50.00D),
    TORCH(Material.TORCH, 0.00D),
    FIRE(Material.FIRE, 0.00D),
    MOB_SPAWNER(Material.MOB_SPAWNER, 5.00D),
    CHEST(Material.CHEST, 2.50D),
    TRAPPED_CHEST(Material.TRAPPED_CHEST, 2.50D),
    REDSTONE_WIRE(Material.REDSTONE_WIRE, 0.00D),
    DIAMOND_ORE(Material.DIAMOND_ORE, 3.00D),
    DIAMOND_BLOCK(Material.DIAMOND_BLOCK, 5.00D),
    WORKBENCH(Material.WORKBENCH, 2.50D),
    SOIL(Material.SOIL, 0.60D),
    FURNACE(Material.FURNACE, 3.50D),
    BURNING_FURNACE(Material.BURNING_FURNACE, 3.50D),
    WOODEN_DOOR(Material.WOODEN_DOOR, 3.00D),
    LADDER(Material.LADDER, 0.40D),
    LEVER(Material.LEVER, 0.50D),
    IRON_DOOR_BLOCK(Material.IRON_DOOR_BLOCK, 5.00D),
    HOPPER(Material.HOPPER, 2.00D),
    STONE_PLATE(Material.STONE_PLATE, 0.50D),
    WOOD_PLATE(Material.WOOD_PLATE, 0.50D),
    GOLD_PLATE(Material.GOLD_PLATE, 0.50D),
    IRON_PLATE(Material.IRON_PLATE, 0.50D),
    QUARTZ_BLOCK(Material.QUARTZ_BLOCK, 0.80D),
    QUARTZ_ORE(Material.QUARTZ_ORE, 3.00D),
    QUARTZ_STAIRS(Material.QUARTZ_STAIRS, 0.80D),
    REDSTONE_BLOCK(Material.REDSTONE_BLOCK, 5.00D),
    REDSTONE_ORE(Material.REDSTONE_ORE, 3.00D),
    GLOWING_REDSTONE_ORE(Material.GLOWING_REDSTONE_ORE, 3.00D),
    REDSTONE_TORCH_OFF(Material.REDSTONE_TORCH_OFF, 0.00D),
    REDSTONE_TORCH_ON(Material.REDSTONE_TORCH_ON, 0.00D),
    REDSTONE_COMPARATOR_OFF(Material.REDSTONE_COMPARATOR_OFF, 0.00D),
    REDSTONE_COMPARATOR_ON(Material.REDSTONE_COMPARATOR_ON, 0.00D),
    DAYLIGHT_DETECTOR(Material.DAYLIGHT_DETECTOR, 0.20D),
    STONE_BUTTON(Material.STONE_BUTTON, 0.50D),
    SNOW(Material.SNOW, 0.10D),
    ICE(Material.ICE, 0.50D),
    SNOW_BLOCK(Material.SNOW_BLOCK, 0.20D),
    CACTUS(Material.CACTUS, 0.40D),
    CLAY(Material.CLAY, 0.60D),
    SUGAR_CANE_BLOCK(Material.SUGAR_CANE_BLOCK, 0.00D),
    JUKEBOX(Material.JUKEBOX, 2.00D),
    FENCE(Material.FENCE, 2.00D),
    NETHERRACK(Material.NETHERRACK, 0.40D),
    SOUL_SAND(Material.SOUL_SAND, 0.50D),
    GLOWSTONE(Material.GLOWSTONE, 0.30D),
    PORTAL(Material.PORTAL, -1.00D),
    CAKE_BLOCK(Material.CAKE_BLOCK, 0.50D),
    LOCKED_CHEST(Material.LOCKED_CHEST, 0.00D),
    TRAP_DOOR(Material.TRAP_DOOR, 3.00D),
    MONSTER_EGGS(Material.MONSTER_EGGS, 0.750D),
    SMOOTH_BRICK(Material.SMOOTH_BRICK, 1.50D),
    IRON_FENCE(Material.IRON_FENCE, 5.00D),
    THIN_GLASS(Material.THIN_GLASS, 0.30D),
    PUMPKIN_STEM(Material.PUMPKIN_STEM, 0.00D),
    MELON_STEM(Material.MELON_STEM, 0.00D),
    VINE(Material.VINE, 0.20D),
    FENCE_GATE(Material.FENCE_GATE, 2.00D),
    MYCEL(Material.MYCEL, 0.60D),
    WATER_LILY(Material.WATER_LILY, 0.00D),
    NETHER_BRICK(Material.NETHER_BRICK, 2.00D),
    NETHER_FENCE(Material.NETHER_FENCE, 2.00D),
    ENCHANTMENT_TABLE(Material.ENCHANTMENT_TABLE, 5.00D),
    BREWING_STAND(Material.BREWING_STAND, 0.50D),
    CAULDRON(Material.CAULDRON, 2.00D),
    ENDER_PORTAL(Material.ENDER_PORTAL, -1.00D),
    ENDER_PORTAL_FRAME(Material.ENDER_PORTAL_FRAME, -1.00D),
    DRAGON_EGG(Material.DRAGON_EGG, 3.00D),
    REDSTONE_LAMP_OFF(Material.REDSTONE_LAMP_OFF, 0.30D),
    REDSTONE_LAMP_ON(Material.REDSTONE_LAMP_ON, 0.30D),
    WOOD_DOUBLE_STEP(Material.WOOD_DOUBLE_STEP, 2.00D),
    WOOD_STEP(Material.WOOD_STEP, 2.00D),
    COCOA(Material.COCOA, 0.20D),
    EMERALD_ORE(Material.EMERALD_ORE, 3.00D),
    ENDER_CHEST(Material.ENDER_CHEST, 22.50D),
    EMERALD_BLOCK(Material.EMERALD_BLOCK, 5.00D),
    FLOWER_POT(Material.FLOWER_POT, 0.00D),
    WOOD_BUTTON(Material.WOOD_BUTTON, 0.50D),
    ANVIL(Material.ANVIL, 5.00D),
    HUGE_MUSHROOM_1(Material.HUGE_MUSHROOM_1, 0.20D),
    HUGE_MUSHROOM_2(Material.HUGE_MUSHROOM_2, 0.20D),
    BED_BLOCK(Material.BED_BLOCK, 0.20D),
    WOOD_STAIRS(Material.WOOD_STAIRS, 2.00D),
    COBBLESTONE_STAIRS(Material.COBBLESTONE_STAIRS, 2.00D),
    BRICK_STAIRS(Material.BRICK_STAIRS, 2.00D),
    SMOOTH_STAIRS(Material.SMOOTH_STAIRS, 2.00D),
    NETHER_BRICK_STAIRS(Material.NETHER_BRICK_STAIRS, 2.00D),
    SANDSTONE_STAIRS(Material.SANDSTONE_STAIRS, 2.00D),
    SPRUCE_WOOD_STAIRS(Material.SPRUCE_WOOD_STAIRS, 2.00D),
    BIRCH_WOOD_STAIRS(Material.BIRCH_WOOD_STAIRS, 2.00D),
    JUNGLE_WOOD_STAIRS(Material.JUNGLE_WOOD_STAIRS, 2.00D),
    COMMAND(Material.COMMAND, 0.00D),
    BEACON(Material.BEACON, 3.00D),
    COBBLE_WALL(Material.COBBLE_WALL, 2.00D),
    CROPS(Material.CROPS, 0.00D),
    CARROT(Material.CARROT, 0.00D),
    POTATO(Material.POTATO, 0.00D),
    SKULL(Material.SKULL, 1.00D),
    NETHER_WARTS(Material.NETHER_WARTS, 0.00D),
    SIGN_POST(Material.SIGN_POST, 1.00D),
    WALL_SIGN(Material.WALL_SIGN, 1.00D),
    PUMPKIN(Material.PUMPKIN, 1.00D),
    JACK_O_LANTERN(Material.JACK_O_LANTERN, 1.00D),
    DIODE_BLOCK_ON(Material.DIODE_BLOCK_ON, 0.00D),
    DIODE_BLOCK_OFF(Material.DIODE_BLOCK_OFF, 0.00D),
    MELON_BLOCK(Material.MELON_BLOCK, 1.00D),
    ENDER_STONE(Material.ENDER_STONE, 3.00D),
    TRIPWIRE(Material.TRIPWIRE, 0.00D),
    TRIPWIRE_HOOK(Material.TRIPWIRE_HOOK, 0.00D),
    PISTON_BASE(Material.PISTON_BASE, 0.70D),
    PISTON_EXTENSION(Material.PISTON_EXTENSION, 0.70D),
    PISTON_MOVING_PIECE(Material.PISTON_MOVING_PIECE, 0.70D),
    PISTON_STICKY_BASE(Material.PISTON_STICKY_BASE, 0.70D);
    
    Material mat;
    double hardness;
    
    BlockHardness(Material m, double hard) {
        mat = m;
        hardness = hard;
    }
    
    public static double getBlockHardness(Material m) {
        BlockHardness h = getHardness(m);
        return h == null ? 0D : h.hardness;
    }

    public static boolean hasBlockHardness(Material m) {
        return getHardness(m) != null;
    }

    public static BlockHardness getHardness(Material m) {
        for (BlockHardness e : BlockHardness.values()) {
            if (e.mat.equals(m)) {
                return e;
            }
        }
        return null;
    }
}

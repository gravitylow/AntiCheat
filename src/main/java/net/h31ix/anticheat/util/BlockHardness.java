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
    STONE(1.50D),
    GRASS(0.60D),
    DIRT(0.50D),
    COBBLESTONE(2.00D),
    WOOD(2.00D),
    SAPLING(0.00D),
    WATER(100.00D),
    STATIONARY_WATER(100.00D),
    LAVA(0.00D),
    STATIONARY_LAVA(100.00D),
    SAND(0.50D),
    GRAVEL(0.60D),
    GOLD_ORE(3.00D),
    IRON_ORE(3.00D),
    COAL_ORE(3.00D),
    LOG(2.00D),
    LEAVES(0.20D),
    SPONGE(0.60D),
    GLASS(0.30D),
    LAPIS_ORE(3.00D),
    LAPIS_BLOCK(3.00D),
    DISPENSER(3.50D),
    DROPPER(3.50D),
    SANDSTONE(0.80D),
    NOTE_BLOCK(0.80D),
    BED(0.20D),
    RAILS(0.70D),
    DETECTOR_RAIL(0.70D),
    POWERED_RAIL(0.70D),
    ACTIVATOR_RAIL(0.70D),
    WEB(4.00D),
    LONG_GRASS(0.00D),
    DEAD_BUSH(0.00D),
    WOOL(0.80D),
    YELLOW_FLOWER(0.00D),
    RED_ROSE(0.00D),
    BROWN_MUSHROOM(0.00D),
    RED_MUSHROOM(0.00D),
    GOLD_BLOCK(3.00D),
    IRON_BLOCK(5.00D),
    DOUBLE_STEP(2.00D),
    STEP(2.00D),
    BRICK(2.00D),
    TNT(0.00D),
    BOOKSHELF(1.50D),
    MOSSY_COBBLESTONE(2.00D),
    OBSIDIAN(50.00D),
    TORCH(0.00D),
    FIRE(0.00D),
    MOB_SPAWNER(5.00D),
    CHEST(2.50D),
    TRAPPED_CHEST(2.50D),
    REDSTONE_WIRE(0.00D),
    DIAMOND_ORE(3.00D),
    DIAMOND_BLOCK(5.00D),
    WORKBENCH(2.50D),
    SOIL(0.60D),
    FURNACE(3.50D),
    BURNING_FURNACE(3.50D),
    WOODEN_DOOR(3.00D),
    LADDER(0.40D),
    LEVER(0.50D),
    IRON_DOOR_BLOCK(5.00D),
    HOPPER(2.00D),
    STONE_PLATE(0.50D),
    WOOD_PLATE(0.50D),
    GOLD_PLATE(0.50D),
    IRON_PLATE(0.50D),
    QUARTZ_BLOCK(0.80D),
    QUARTZ_ORE(3.00D),
    QUARTZ_STAIRS(0.80D),
    REDSTONE_BLOCK(5.00D),
    REDSTONE_ORE(3.00D),
    GLOWING_REDSTONE_ORE(3.00D),
    REDSTONE_TORCH_OFF(0.00D),
    REDSTONE_TORCH_ON(0.00D),
    REDSTONE_COMPARATOR_OFF(0.00D),
    REDSTONE_COMPARATOR_ON(0.00D),
    DAYLIGHT_DETECTOR(0.20D),
    STONE_BUTTON(0.50D),
    SNOW(0.10D),
    ICE(0.50D),
    SNOW_BLOCK(0.20D),
    CACTUS(0.40D),
    CLAY(0.60D),
    HARD_CLAY(1.25D),
    STAINED_CLAY(1.25D),
    SUGAR_CANE_BLOCK(0.00D),
    JUKEBOX(2.00D),
    FENCE(2.00D),
    NETHERRACK(0.40D),
    SOUL_SAND(0.50D),
    GLOWSTONE(0.30D),
    PORTAL(-1.00D),
    CAKE_BLOCK(0.50D),
    LOCKED_CHEST(0.00D),
    TRAP_DOOR(3.00D),
    MONSTER_EGGS(0.750D),
    SMOOTH_BRICK(1.50D),
    IRON_FENCE(5.00D),
    THIN_GLASS(0.30D),
    PUMPKIN_STEM(0.00D),
    MELON_STEM(0.00D),
    VINE(0.20D),
    FENCE_GATE(2.00D),
    MYCEL(0.60D),
    WATER_LILY(0.00D),
    NETHER_BRICK(2.00D),
    NETHER_FENCE(2.00D),
    ENCHANTMENT_TABLE(5.00D),
    BREWING_STAND(0.50D),
    CAULDRON(2.00D),
    ENDER_PORTAL(-1.00D),
    ENDER_PORTAL_FRAME(-1.00D),
    DRAGON_EGG(3.00D),
    REDSTONE_LAMP_OFF(0.30D),
    REDSTONE_LAMP_ON(0.30D),
    WOOD_DOUBLE_STEP(2.00D),
    WOOD_STEP(2.00D),
    COCOA(0.20D),
    EMERALD_ORE(3.00D),
    ENDER_CHEST(22.50D),
    EMERALD_BLOCK(5.00D),
    FLOWER_POT(0.00D),
    WOOD_BUTTON(0.50D),
    ANVIL(5.00D),
    HUGE_MUSHROOM_1(0.20D),
    HUGE_MUSHROOM_2(0.20D),
    BED_BLOCK(0.20D),
    WOOD_STAIRS(2.00D),
    COBBLESTONE_STAIRS(2.00D),
    BRICK_STAIRS(2.00D),
    SMOOTH_STAIRS(2.00D),
    NETHER_BRICK_STAIRS(2.00D),
    SANDSTONE_STAIRS(2.00D),
    SPRUCE_WOOD_STAIRS(2.00D),
    BIRCH_WOOD_STAIRS(2.00D),
    JUNGLE_WOOD_STAIRS(2.00D),
    COMMAND(0.00D),
    BEACON(3.00D),
    COBBLE_WALL(2.00D),
    CROPS(0.00D),
    CARROT(0.00D),
    POTATO(0.00D),
    SKULL(1.00D),
    NETHER_WARTS(0.00D),
    SIGN_POST(1.00D),
    WALL_SIGN(1.00D),
    PUMPKIN(1.00D),
    JACK_O_LANTERN(1.00D),
    DIODE_BLOCK_ON(0.00D),
    DIODE_BLOCK_OFF(0.00D),
    MELON_BLOCK(1.00D),
    ENDER_STONE(3.00D),
    TRIPWIRE(0.00D),
    TRIPWIRE_HOOK(0.00D),
    HAY_BLOCK(0.50D),
    CARPET(0.10D),
    COAL_BLOCK(5.00D),
    PISTON_BASE(0.70D),
    PISTON_EXTENSION(0.70D),
    PISTON_MOVING_PIECE(0.70D),
    PISTON_STICKY_BASE(0.70D);
    
    Material mat;
    double hardness;
    
    BlockHardness(double hard) {
        mat = Material.getMaterial(name());
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

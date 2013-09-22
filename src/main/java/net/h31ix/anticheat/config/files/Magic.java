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

package net.h31ix.anticheat.config.files;

import net.h31ix.anticheat.AntiCheat;
import net.h31ix.anticheat.config.Configuration;
import net.h31ix.anticheat.config.ConfigurationFile;

/**
 * Magic number class. Modifications to values in magic.yml will be accepted here.
 * <p>
 * Note that although each value is documented, changing it may have unintended side-effects. For instance, setting something to 0 that the plugin then uses as a dividend will result in an error. <br>
 * Also realize that the smaller a value is, the less you should change it; it's probably small for a reason. The larger a value is, the safer it is to probably change it.
 * <p>
 * <b>How to read the value documentation:</b> <br>
 * First, you will see a description of the value. Then, you will see a type: <br>
 * <b>Type SYSTEM:</b> This is a 'long' value, measured in milliseconds, used to compare past and future events with the current system time. Remember, 1000 ms = 1 second. <br>
 * <b>Type INTEGER:</b> This is a regular number. It's typically used as something trivial, such as how many times X event can occur. <br>
 * <b>Type DOUBLE:</b> This is a number that has a decimal in it. It's typically used to evaluate speed or distance.
 * <p>
 * After the type, you may see a recommendation labeled as '+Leniency'. This means 'In order to add leniency to this value, do X' <br>
 * The suggestion for adding leniency will either be to INCREASE or DECREASE the variable. Doing what it suggests will cause the system to not judge the people being checked so vigorously.
 */

public class Magic extends ConfigurationFile {
    /**
     * Time to exempt a player from moving because of entering/exiting a vehicle; Type=system, +Leniency=Increase.
     */
    public int ENTERED_EXITED_TIME;
    /**
     * Time to exempt a player from moving because of entering/exiting sneak mode; Type=system, +Leniency=Increase.
     */
    public int SNEAK_TIME;
    /**
     * Time to exempt a player from moving because of teleporting; Type=system, +Leniency=Increase.
     */
    public int TELEPORT_TIME;
    /**
     * Time to exempt a player from moving because of exiting fly mode; Type=system, +Leniency=Increase.
     */
    public int EXIT_FLY_TIME;
    /**
     * Time to exempt a player from moving because of joining the server; Type=system, +Leniency=Increase.
     */
    public int JOIN_TIME;
    /**
     * Time to exempt a player from fastbreak check because of using instant break; Type=seconds, +Leniency=Increase.
     */
    public int INSTANT_BREAK_TIME;
    /**
     * Time to exempt a player from moving because of taking damage; Type=system, +Leniency=Increase.
     */
    public long DAMAGE_TIME;
    /**
     * Time to exempt a player from moving because of taking damage with knockback effects; Type=system, +Leniency=Increase.
     */
    public long KNOCKBACK_DAMAGE_TIME;
    /**
     * Time to exempt a player from moving because of taking damage from an explosion; Type=system, +Leniency=Increase.
     */
    public long EXPLOSION_DAMAGE_TIME;
    /**
     * Minimum time it takes to fire X projectiles; Type=system, +Leniency=Decrease.
     */
    public int PROJECTILE_TIME_MIN;
    /**
     * Number of projectiles to wait for before checking how long they took to fire off; Type=integer.
     */
    public int PROJECTILE_CHECK;
    /**
     * Minimum time it takes to drop X items; Type=system, +Leniency=Decrease.
     */
    public int DROP_TIME_MIN;
    /**
     * Number of item drops to wait for before checking how long they took to drop; Type=integer.
     */
    public int DROP_CHECK;
    /**
     * Max number of blocks that can be broken in a given time; Type=integer, +Leniency=Increase.
     */
    public int FASTBREAK_LIMIT;
    /**
     * Time to wait before checking block breaks; Type=system, +Leniency=Increase.
     */
    public int FASTBREAK_TIMEMAX;
    /**
     * Time to wait before checking block breaks in creative mode; Type=system, +Leniency=Increase.
     */
    public int FASTBREAK_TIMEMAX_CREATIVE;
    /**
     * Number of times fastbreak can be observed before taking action; Type=integer, +Leniency=Increase.
     */
    public int FASTBREAK_MAXVIOLATIONS;
    /**
     * Number of times fastbreak can be observed before taking action in creative mode; Type=integer, +Leniency=Increase.
     */
    public int FASTBREAK_MAXVIOLATIONS_CREATIVE;
    /**
     * Time a player is forced to wait after fastbreak has been detected; Type=system, +Leniency=Decrease.
     */
    public int FASTBREAK_MAXVIOLATIONTIME;
    /**
     * Number of blocks that can be broken without being hit before taking action; Type=integer, +Leniency=Increase.
     */
    public int FASTPLACE_ZEROLIMIT;
    /**
     * Minimum time in between block places; Type=system, +Leniency=Decrease.
     */
    public int FASTPLACE_TIMEMIN;
    /**
     * Number of times fastplace can be observed before taking action; Type=integer, +Leniency=Increase.
     */
    public int FASTPLACE_MAXVIOLATIONS;
    /**
     * Number of times fastplace can be observed before taking action in creative mode; Type=integer, +Leniency=Increase.
     */
    public int FASTPLACE_MAXVIOLATIONS_CREATIVE;
    /**
     * Time a player is forced to wait after fastplace has been detected; Type=system, +Leniency=Decrease.
     */
    public int FASTPLACE_MAXVIOLATIONTIME;
    /**
     * Number of times required to punch a block before it breaks; Type=integer, +Leniency=Decrease.
     */
    public int BLOCK_PUNCH_MIN;
    /**
     * Number of chat spam violations before a player is warned; Type=integer, +Leniency=Increase.
     */
    public int CHAT_WARN_LEVEL;
    /**
     * Number of chat spam violations before a player is kicked; Type=integer, +Leniency=Increase.
     */
    public int CHAT_KICK_LEVEL;
    /**
     * Number of kicks for chat violation before a player is banned; Type=integer, +Leniency=Increase.
     */
    public int CHAT_BAN_LEVEL;
    /**
     * Number of times a player can be caught in-flight before action is taken; Type=integer, +Leniency=Increase.
     */
    public int FLIGHT_LIMIT;
    /**
     * How many blocks you can travel before action is taken; Type=integer, +Leniency=Increase.
     */
    public double FLIGHT_BLOCK_LIMIT;
    /**
     * Maximum speed that a player can climb a waterfall; Type=double, +Leniency=Increase.
     */
    public double WATER_CLIMB_MAX;
    /**
     * Number of times a player can fly on the y-axis before action is taken; Type=integer, +Leniency=Increase.
     */
    public int Y_MAXVIOLATIONS;
    /**
     * Time a player is forced to wait after flying on the y-axis; Type=system, +Leniency=Decrease.
     */
    public int Y_MAXVIOTIME;
    /**
     * Number of times a player can fail a velocity check before action is taken; Type=integer, +Leniency=Increase.
     */
    public int VELOCITY_TIME;
    /**
     * Time used to schedule increasing a players velocity count - probably not touching this would be good; Type=integer.
     */
    public long VELOCITY_SCHETIME;
    /**
     * Maximum time a player is considered to have a change in velocity; Type=system, +Leniency=Increase.
     */
    public long VELOCITY_CHECKTIME;
    /**
     * Time to extend a player's velocity count by; Type=system, +Leniency=Increase.
     */
    public long VELOCITY_PREVENT;
    /**
     * Number of times a player can fail the velocity check before action is taken; Type=integer, +Leniency=Increase.
     */
    public int VELOCITY_MAXTIMES;
    /**
     * Number of times a player can fail the nofall check before action is taken; Type=integer, +Leniency=Increase.
     */
    public int NOFALL_LIMIT;
    /**
     * Number of times a player can fail the ascension check before action is taken; Type=integer, +Leniency=Increase.
     */
    public int ASCENSION_COUNT_MAX;
    /**
     * Number of times a player can fail the ascension check while in water before action is taken; Type=integer, +Leniency=Increase.
     */
    public int WATER_ASCENSION_VIOLATION_MAX;
    /**
     * Number of times a player can be caught speeding in water before action is taken; Type=integer, +Leniency=Increase.
     */
    public int WATER_SPEED_VIOLATION_MAX;
    /**
     * Number of times a player can sprint without proper food level before action is taken; Type=integer, +Leniency=Increase.
     */
    public int SPRINT_FOOD_MIN;
    /**
     * Minimum time between animations; Type=system, +Leniency=Increase.
     */
    public int ANIMATION_MIN;
    /**
     * Minimum time between chats; Type=system, +Leniency=Increase.
     */
    public int CHAT_MIN;
    /**
     * Minimum time between repeating yourself in the chat; Type=system, +Leniency=Increase.
     */
    public int CHAT_REPEAT_MIN;
    /**
     * Maximum speed you can travel while sprinting; Type=double, +Leniency=Increase.
     */
    public double SPRINT_MIN;
    /**
     * Minimum time in between block breaks; Type=seconds, +Leniency=Decrease.
     */
    public double BLOCK_BREAK_MIN;
    /**
     * Minimum time in between block placing; Type=seconds, +Leniency=Decrease.
     */
    public double BLOCK_PLACE_MIN;
    /**
     * Minimum time in between healing; Type=system, +Leniency=Decrease.
     */
    public long HEAL_TIME_MIN;
    /**
     * Minimum time in between eating; Type=system, +Leniency=Decrease.
     */
    public long EAT_TIME_MIN;
    /**
     * Maximum percent error between predicted bow force and actual bow force; Type=double, +Leniency=Increase.
     */
    public double BOW_ERROR;
    /**
     * Minimum distance a player can break a block from; Type=double, +Leniency=Increase.
     */
    public double BLOCK_MAX_DISTANCE;
    /**
     * Minimum distance a player can break a block from in creative mode; Type=double, +Leniency=Increase.
     */
    public double BLOCK_MAX_DISTANCE_CREATIVE;
    /**
     * Minimum distance a player can damage an entity from; Type=double, +Leniency=Increase.
     */
    public double ENTITY_MAX_DISTANCE;
    /**
     * Minimum distance a player can damage an entity from in creative mode; Type=double, +Leniency=Increase.
     */
    public double ENTITY_MAX_DISTANCE_CREATIVE;
    /**
     * Maximum speed a player on a ladder can travel; Type=double, +Leniency=Increase.
     */
    public double LADDER_Y_MAX;
    /**
     * Minimum speed a player on a ladder can travel; Type=double, +Leniency=Decrease.
     */
    public double LADDER_Y_MIN;
    /**
     * Maximum speed a player can travel up; Type=double, +Leniency=Increase.
     */
    public double Y_SPEED_MAX;
    /**
     * Maximum difference between the player's last 2 Y values when traveling up; Type=double, +Leniency=Increase.
     */
    public double Y_MAXDIFF;
    /**
     * Time in between Y difference measures; Type=system, +Leniency=Decrease.
     */
    public long Y_TIME;
    /**
     * Maximum speed a player can travel forwards or backwards; Type=double, +Leniency=Increase.
     */
    public double XZ_SPEED_MAX;
    /**
     * Maximum speed a player can travel forwards or backwards while sprinting; Type=double, +Leniency=Increase.
     */
    public double XZ_SPEED_MAX_SPRINT;
    /**
     * Maximum speed a player can travel forwards or backwards while flying; Type=double, +Leniency=Increase.
     */
    public double XZ_SPEED_MAX_FLY;
    /**
     * Maximum speed a player can travel forwards or backwards while under the influence; Type=double, +Leniency=Increase.
     */
    public double XZ_SPEED_MAX_POTION;
    /**
     * Maximum speed a player can travel forwards or backwards while sneaking; Type=double, +Leniency=Increase.
     */
    public double XZ_SPEED_MAX_SNEAK;
    /**
     * Maximum speed a player can travel forwards or backwards in water; Type=double, +Leniency=Increase.
     */
    public double XZ_SPEED_MAX_WATER;
    /**
     * Maximum speed a player can travel forwards or backwards in water while sprinting; Type=double, +Leniency=Increase.
     */
    public double XZ_SPEED_MAX_WATER_SPRINT;
    /**
     * Maximum speed a player can travel forwards or backwards on soul sand; Type=double, +Leniency=Increase.
     */
    public double XZ_SPEED_MAX_SOULSAND;
    /**
     * Maximum speed a player can travel forwards or backwards on soul sand while sprinting; Type=double, +Leniency=Increase.
     */
    public double XZ_SPEED_MAX_SOULSAND_SPRINT;
    /**
     * Maximum speed a player can travel forwards or backwards on soul sand while under the influence; Type=double, +Leniency=Increase.
     */
    public double XZ_SPEED_MAX_SOULSAND_POTION;
    /**
     * Maximum times a player can fail the speed check before action is taken; Type=integer, +Leniency=Increase.
     */
    public int SPEED_MAX;
    /**
     * The number of clicks we should wait before checking if the player has used fast inventory; Type=integer; +Leniency=Increase.
     */
    public int INVENTORY_CHECK;
    /**
     * The minimum time it should have taken for the player to click X times; Type=integer; +Leniency=Increase.
     */
    public long INVENTORY_TIMEMIN;
    /**
     * The number of steps we should wait before checking if the player has used timer cheating; Type=integer; +Leniency=Increase.
     */
    public int TIMER_STEP_CHECK;
    /**
     * The minimum time it should have taken for the player to travel X steps; Type=system; +Leniency=Decrease.
     */
    public long TIMER_TIMEMIN;
    /**
     * Minimum travel distance for move to be considered a teleport and subsequently be ignored; Type=int; +Leniency=Increase.
     */
    public long TELEPORT_MIN;

    private double version;
    
    public Magic(AntiCheat plugin, Configuration config) {
        super(plugin, config, "magic.yml");
    }

    @Override
    public void open() {
        version = new ConfigValue<Double>("VERSION").getValue();

        if(version == 0.2D) {
            // 0.2 -> 0.3 diffs
            // Fix improperly stored double Y_MAXDIFF
            new ConfigValue<Double>("Y_MAXDIFF").setValue(new ConfigValue<Double>("Y_MAXDIFF").getDefaultValue());
            // Update version
            version = new ConfigValue<Double>("VERSION").setValue(0.3D);
            setNeedsReload(true);

        }
        ENTERED_EXITED_TIME = new ConfigValue<Integer>("ENTERED_EXITED_TIME").getValue();
        SNEAK_TIME = new ConfigValue<Integer>("SNEAK_TIME").getValue();
        TELEPORT_TIME = new ConfigValue<Integer>("TELEPORT_TIME").getValue();
        EXIT_FLY_TIME = new ConfigValue<Integer>("EXIT_FLY_TIME").getValue();
        JOIN_TIME = new ConfigValue<Integer>("JOIN_TIME").getValue();
        INSTANT_BREAK_TIME = new ConfigValue<Integer>("INSTANT_BREAK_TIME").getValue();
        DAMAGE_TIME = new ConfigValue<Integer>("DAMAGE_TIME").getValue();
        KNOCKBACK_DAMAGE_TIME = new ConfigValue<Integer>("KNOCKBACK_DAMAGE_TIME").getValue();
        EXPLOSION_DAMAGE_TIME = new ConfigValue<Integer>("EXPLOSION_DAMAGE_TIME").getValue();
        PROJECTILE_TIME_MIN = new ConfigValue<Integer>("PROJECTILE_TIME_MIN").getValue();
        PROJECTILE_CHECK = new ConfigValue<Integer>("PROJECTILE_CHECK").getValue();
        DROP_TIME_MIN = new ConfigValue<Integer>("DROP_TIME_MIN").getValue();
        DROP_CHECK = new ConfigValue<Integer>("DROP_CHECK").getValue();
        FASTBREAK_LIMIT = new ConfigValue<Integer>("FASTBREAK_LIMIT").getValue();
        FASTBREAK_TIMEMAX = new ConfigValue<Integer>("FASTBREAK_TIMEMAX").getValue();
        FASTBREAK_TIMEMAX_CREATIVE = new ConfigValue<Integer>("FASTBREAK_TIMEMAX_CREATIVE").getValue();
        FASTBREAK_MAXVIOLATIONS = new ConfigValue<Integer>("FASTBREAK_MAXVIOLATIONS").getValue();
        FASTBREAK_MAXVIOLATIONS_CREATIVE = new ConfigValue<Integer>("FASTBREAK_MAXVIOLATIONS_CREATIVE").getValue();
        FASTBREAK_MAXVIOLATIONTIME = new ConfigValue<Integer>("FASTBREAK_MAXVIOLATIONTIME").getValue();
        FASTPLACE_ZEROLIMIT = new ConfigValue<Integer>("FASTPLACE_ZEROLIMIT").getValue();
        FASTPLACE_TIMEMIN = new ConfigValue<Integer>("FASTPLACE_TIMEMIN").getValue();
        FASTPLACE_MAXVIOLATIONS = new ConfigValue<Integer>("FASTPLACE_MAXVIOLATIONS").getValue();
        FASTPLACE_MAXVIOLATIONS_CREATIVE = new ConfigValue<Integer>("FASTPLACE_MAXVIOLATIONS_CREATIVE").getValue();
        FASTPLACE_MAXVIOLATIONTIME = new ConfigValue<Integer>("FASTPLACE_MAXVIOLATIONTIME").getValue();
        BLOCK_PUNCH_MIN = new ConfigValue<Integer>("BLOCK_PUNCH_MIN").getValue();
        CHAT_WARN_LEVEL = new ConfigValue<Integer>("CHAT_WARN_LEVEL").getValue();
        CHAT_KICK_LEVEL = new ConfigValue<Integer>("CHAT_KICK_LEVEL").getValue();
        CHAT_BAN_LEVEL = new ConfigValue<Integer>("CHAT_BAN_LEVEL").getValue();
        FLIGHT_LIMIT = new ConfigValue<Integer>("FLIGHT_LIMIT").getValue();
        FLIGHT_BLOCK_LIMIT = new ConfigValue<Double>("FLIGHT_BLOCK_LIMIT").getValue();
        WATER_CLIMB_MAX = new ConfigValue<Double>("WATER_CLIMB_MAX").getValue();
        Y_MAXVIOLATIONS = new ConfigValue<Integer>("Y_MAXVIOLATIONS").getValue();
        Y_MAXVIOTIME = new ConfigValue<Integer>("Y_MAXVIOTIME").getValue();
        VELOCITY_TIME = new ConfigValue<Integer>("VELOCITY_TIME").getValue();
        VELOCITY_SCHETIME = new ConfigValue<Integer>("VELOCITY_SCHETIME").getValue();
        VELOCITY_CHECKTIME = new ConfigValue<Integer>("VELOCITY_CHECKTIME").getValue();
        VELOCITY_PREVENT = new ConfigValue<Integer>("VELOCITY_PREVENT").getValue();
        VELOCITY_MAXTIMES = new ConfigValue<Integer>("VELOCITY_MAXTIMES").getValue();
        NOFALL_LIMIT = new ConfigValue<Integer>("NOFALL_LIMIT").getValue();
        ASCENSION_COUNT_MAX = new ConfigValue<Integer>("ASCENSION_COUNT_MAX").getValue();
        WATER_ASCENSION_VIOLATION_MAX = new ConfigValue<Integer>("WATER_ASCENSION_VIOLATION_MAX").getValue();
        WATER_SPEED_VIOLATION_MAX = new ConfigValue<Integer>("WATER_SPEED_VIOLATION_MAX").getValue();
        SPRINT_FOOD_MIN = new ConfigValue<Integer>("SPRINT_FOOD_MIN").getValue();
        ANIMATION_MIN = new ConfigValue<Integer>("ANIMATION_MIN").getValue();
        CHAT_MIN = new ConfigValue<Integer>("CHAT_MIN").getValue();
        CHAT_REPEAT_MIN = new ConfigValue<Integer>("CHAT_REPEAT_MIN").getValue();
        SPRINT_MIN = new ConfigValue<Double>("SPRINT_MIN").getValue();
        BLOCK_BREAK_MIN = new ConfigValue<Double>("BLOCK_BREAK_MIN").getValue();
        BLOCK_PLACE_MIN = new ConfigValue<Double>("BLOCK_PLACE_MIN").getValue();
        HEAL_TIME_MIN = new ConfigValue<Integer>("HEAL_TIME_MIN").getValue();
        EAT_TIME_MIN = new ConfigValue<Integer>("EAT_TIME_MIN").getValue();
        BOW_ERROR = new ConfigValue<Double>("BOW_ERROR").getValue();
        BLOCK_MAX_DISTANCE = new ConfigValue<Double>("BLOCK_MAX_DISTANCE").getValue();
        BLOCK_MAX_DISTANCE_CREATIVE = new ConfigValue<Double>("BLOCK_MAX_DISTANCE_CREATIVE").getValue();
        ENTITY_MAX_DISTANCE = new ConfigValue<Double>("ENTITY_MAX_DISTANCE").getValue();
        ENTITY_MAX_DISTANCE_CREATIVE = new ConfigValue<Double>("ENTITY_MAX_DISTANCE_CREATIVE").getValue();
        LADDER_Y_MAX = new ConfigValue<Double>("LADDER_Y_MAX").getValue();
        LADDER_Y_MIN = new ConfigValue<Double>("LADDER_Y_MIN").getValue();
        Y_SPEED_MAX = new ConfigValue<Double>("Y_SPEED_MAX").getValue();
        Y_MAXDIFF = new ConfigValue<Double>("Y_MAXDIFF").getValue();
        Y_TIME = new ConfigValue<Integer>("Y_TIME").getValue();
        XZ_SPEED_MAX = new ConfigValue<Double>("XZ_SPEED_MAX").getValue();
        XZ_SPEED_MAX_SPRINT = new ConfigValue<Double>("XZ_SPEED_MAX_SPRINT").getValue();
        XZ_SPEED_MAX_FLY = new ConfigValue<Double>("XZ_SPEED_MAX_FLY").getValue();
        XZ_SPEED_MAX_POTION = new ConfigValue<Double>("XZ_SPEED_MAX_POTION").getValue();
        XZ_SPEED_MAX_SNEAK = new ConfigValue<Double>("XZ_SPEED_MAX_SNEAK").getValue();
        XZ_SPEED_MAX_WATER = new ConfigValue<Double>("XZ_SPEED_MAX_WATER").getValue();
        XZ_SPEED_MAX_SOULSAND = new ConfigValue<Double>("XZ_SPEED_MAX_SOULSAND").getValue();
        XZ_SPEED_MAX_SOULSAND_SPRINT = new ConfigValue<Double>("XZ_SPEED_MAX_SOULSAND_SPRINT").getValue();
        XZ_SPEED_MAX_SOULSAND_POTION = new ConfigValue<Double>("XZ_SPEED_MAX_SOULSAND_POTION").getValue();
        XZ_SPEED_MAX_WATER_SPRINT = new ConfigValue<Double>("XZ_SPEED_MAX_WATER_SPRINT").getValue();
        SPEED_MAX = new ConfigValue<Integer>("SPEED_MAX").getValue();
        INVENTORY_CHECK = new ConfigValue<Integer>("INVENTORY_CHECK").getValue();
        INVENTORY_TIMEMIN = new ConfigValue<Integer>("INVENTORY_TIMEMIN").getValue();
        TIMER_STEP_CHECK = new ConfigValue<Integer>("TIMER_STEP_CHECK").getValue();
        TIMER_TIMEMIN = new ConfigValue<Integer>("TIMER_TIMEMIN").getValue();
        TELEPORT_MIN = new ConfigValue<Integer>("TELEPORT_MIN").getValue();
    }

    public double getVersion() {
        return version;
    }
}

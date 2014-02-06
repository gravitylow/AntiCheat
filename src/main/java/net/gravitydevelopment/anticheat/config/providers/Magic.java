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

package net.gravitydevelopment.anticheat.config.providers;

/**
 * Magic number class. Modifications to values in magic.yml will be accepted here.
 * <p/>
 * Note that although each value is documented, changing it may have unintended side-effects. For instance, setting something to 0 that the plugin then uses as a dividend will result in an error. <br>
 * Also realize that the smaller (more precise) a value is, the less you should change it; it's probably small for a reason. The larger a value is, the safer it is to make larger modifications to it.
 * <p/>
 * <b>How to read the value documentation:</b>
 * <p/>
 * First, you will see a description of the value. Then, you will see a type: <br>
 * <b>Type SYSTEM:</b> This is a millisecond value used to compare past and future events with the current SYSTEM time. Remember, 1000 ms = 1 second. <br>
 * <b>Type INTEGER:</b> This is a regular number. It's typically used as something trivial, such as how many times X event can occur. <br>
 * <b>Type DOUBLE:</b> This is a number that has a decimal in it. It's typically used to evaluate speed or distance.
 * <p/>
 * After the type, you may see a recommendation labeled as 'Leniency'. This means 'In order to add leniency to this value, do X'<br>
 * The suggestion for adding leniency will either be to INCREASE or DECREASE the variable. Doing what it suggests will cause the SYSTEM to not judge the people being checked so vigorously.<br>
 * Some values may not have a leniency recommendation because they are internal numbers used for running checks. Values without these recommendations would be best left alone.
 */

public interface Magic {
    /**
     * Time to exempt a player from moving because of entering/exiting a vehicle; Type=SYSTEM, Leniency=INCREASE.
     */
    public int ENTERED_EXITED_TIME();
    /**
     * Time to exempt a player from moving because of entering/exiting sneak mode; Type=SYSTEM, Leniency=INCREASE.
     */
    public int SNEAK_TIME();
    /**
     * Time to exempt a player from moving because of teleporting; Type=SYSTEM, Leniency=INCREASE.
     */
    public int TELEPORT_TIME();
    /**
     * Time to exempt a player from moving because of exiting fly mode; Type=SYSTEM, Leniency=INCREASE.
     */
    public int EXIT_FLY_TIME();
    /**
     * Time to exempt a player from moving because of joining the server; Type=SYSTEM, Leniency=INCREASE.
     */
    public int JOIN_TIME();
    /**
     * Time to exempt a player from fastbreak check because of using instant break; Type=INTEGER, Leniency=INCREASE.
     */
    public int INSTANT_BREAK_TIME();
    /**
     * Time to exempt a player from moving because of taking damage; Type=SYSTEM, Leniency=INCREASE.
     */
    public int DAMAGE_TIME();
    /**
     * Time to exempt a player from moving because of taking damage with knockback effects; Type=SYSTEM, Leniency=INCREASE.
     */
    public int KNOCKBACK_DAMAGE_TIME();
    /**
     * Time to exempt a player from moving because of taking damage from an explosion; Type=SYSTEM, Leniency=INCREASE.
     */
    public int EXPLOSION_DAMAGE_TIME();
    /**
     * Minimum time it takes to fire X projectiles; Type=SYSTEM, Leniency=DECREASE.
     */
    public int PROJECTILE_TIME_MIN();
    /**
     * Number of projectiles to wait for before checking how long they took to fire off; Type=INTEGER.
     */
    public int PROJECTILE_CHECK();
    /**
     * Minimum time it takes to drop X items; Type=SYSTEM, Leniency=DECREASE.
     */
    public int DROP_TIME_MIN();
    /**
     * Number of item drops to wait for before checking how long they took to drop; Type=INTEGER.
     */
    public int DROP_CHECK();
    /**
     * Max number of blocks that can be broken in a given time; Type=INTEGER, Leniency=INCREASE.
     */
    public int FASTBREAK_LIMIT();
    /**
     * Time to wait before checking block breaks; Type=SYSTEM, Leniency=INCREASE.
     */
    public int FASTBREAK_TIMEMAX();
    /**
     * Time to wait before checking block breaks in creative mode; Type=SYSTEM, Leniency=INCREASE.
     */
    public int FASTBREAK_TIMEMAX_CREATIVE();
    /**
     * Number of times fastbreak can be observed before taking action; Type=INTEGER, Leniency=INCREASE.
     */
    public int FASTBREAK_MAXVIOLATIONS();
    /**
     * Number of times fastbreak can be observed before taking action in creative mode; Type=INTEGER, Leniency=INCREASE.
     */
    public int FASTBREAK_MAXVIOLATIONS_CREATIVE();
    /**
     * Time a player is forced to wait after fastbreak has been detected; Type=SYSTEM, Leniency=DECREASE.
     */
    public int FASTBREAK_MAXVIOLATIONTIME();
    /**
     * Number of blocks that can be broken without being hit before taking action; Type=INTEGER, Leniency=INCREASE.
     */
    public int FASTPLACE_ZEROLIMIT();
    /**
     * Minimum time in between block places; Type=SYSTEM, Leniency=DECREASE.
     */
    public int FASTPLACE_TIMEMIN();
    /**
     * Number of times fastplace can be observed before taking action; Type=INTEGER, Leniency=INCREASE.
     */
    public int FASTPLACE_MAXVIOLATIONS();
    /**
     * Number of times fastplace can be observed before taking action in creative mode; Type=INTEGER, Leniency=INCREASE.
     */
    public int FASTPLACE_MAXVIOLATIONS_CREATIVE();
    /**
     * Time a player is forced to wait after fastplace has been detected; Type=SYSTEM, Leniency=DECREASE.
     */
    public int FASTPLACE_MAXVIOLATIONTIME();
    /**
     * Number of times required to punch a block before it breaks; Type=INTEGER, Leniency=DECREASE.
     */
    public int BLOCK_PUNCH_MIN();
    /**
     * Number of command spam violations before action one is executed; Type=INTEGER, Leniency=INCREASE.
     */
    public int COMMAND_ACTION_ONE_LEVEL();
    /**
     * Number of command spam violations before action two is executed; Type=INTEGER, Leniency=INCREASE.
     */
    public int COMMAND_ACTION_TWO_LEVEL();
    /**
     * Number of chat spam violations before action one is executed; Type=INTEGER, Leniency=INCREASE.
     */
    public int CHAT_ACTION_ONE_LEVEL();
    /**
     * Number of chat spam violations before action two is executed; Type=INTEGER, Leniency=INCREASE.
     */
    public int CHAT_ACTION_TWO_LEVEL();
    /**
     * Number of times a player can be caught in-flight before action is taken; Type=INTEGER, Leniency=INCREASE.
     */
    public int FLIGHT_LIMIT();
    /**
     * How many blocks you can travel before action is taken; Type=INTEGER, Leniency=INCREASE.
     */
    public double FLIGHT_BLOCK_LIMIT();
    /**
     * Maximum speed that a player can climb a waterfall; Type=DOUBLE, Leniency=INCREASE.
     */
    public double WATER_CLIMB_MAX();
    /**
     * Number of times a player can fly on the y-axis before action is taken; Type=INTEGER, Leniency=INCREASE.
     */
    public int Y_MAXVIOLATIONS();
    /**
     * Time a player is forced to wait after flying on the y-axis; Type=SYSTEM, Leniency=DECREASE.
     */
    public int Y_MAXVIOTIME();
    /**
     * Number of times a player can fail a velocity check before action is taken; Type=INTEGER, Leniency=INCREASE.
     */
    public int VELOCITY_TIME();
    /**
     * Time used to schedule increasing a players velocity count - probably not touching this would be good; Type=INTEGER.
     */
    public int VELOCITY_SCHETIME();
    /**
     * Maximum time a player is considered to have a change in velocity; Type=SYSTEM, Leniency=INCREASE.
     */
    public int VELOCITY_CHECKTIME();
    /**
     * Time to extend a player's velocity count by; Type=SYSTEM, Leniency=INCREASE.
     */
    public int VELOCITY_PREVENT();
    /**
     * Number of times a player can fail the velocity check before action is taken; Type=INTEGER, Leniency=INCREASE.
     */
    public int VELOCITY_MAXTIMES();
    /**
     * Number of times a player can fail the nofall check before action is taken; Type=INTEGER, Leniency=INCREASE.
     */
    public int NOFALL_LIMIT();
    /**
     * Number of times a player can fail the ascension check before action is taken; Type=INTEGER, Leniency=INCREASE.
     */
    public int ASCENSION_COUNT_MAX();
    /**
     * Number of times a player can fail the ascension check while in water before action is taken; Type=INTEGER, Leniency=INCREASE.
     */
    public int WATER_ASCENSION_VIOLATION_MAX();
    /**
     * Number of times a player can be caught speeding in water before action is taken; Type=INTEGER, Leniency=INCREASE.
     */
    public int WATER_SPEED_VIOLATION_MAX();
    /**
     * Number of times a player can sprint without proper food level before action is taken; Type=INTEGER, Leniency=INCREASE.
     */
    public int SPRINT_FOOD_MIN();
    /**
     * Maximum times a player can interact between animations; Type=INTEGER, Leniency=INCREASE.
     */
    public int ANIMATION_INTERACT_MAX();
    /**
     * Minimum time between animations; Type=SYSTEM, Leniency=INCREASE.
     */
    public int ANIMATION_MIN();
    /**
     * Minimum time between commands; Type=SYSTEM, Leniency=DECREASE.
     */
    public int COMMAND_MIN();
    /**
     * Minimum time before repeating a command is ignored; Type=SYSTEM, Leniency=DECREASE.
     */
    public int COMMAND_REPEAT_MIN();
    /**
     * Minimum time between chats; Type=SYSTEM, Leniency=INCREASE.
     */
    public int CHAT_MIN();
    /**
     * Time before repeating yourself in chat is ignored; Type=SYSTEM, Leniency=INCREASE.
     */
    public int CHAT_REPEAT_MIN();
    /**
     * Maximum speed you can travel while sprinting; Type=DOUBLE, Leniency=INCREASE.
     */
    public double SPRINT_MIN();
    /**
     * Minimum time in between block breaks; Type=INTEGER, Leniency=DECREASE.
     */
    public double BLOCK_BREAK_MIN();
    /**
     * Minimum time in between block placing; Type=INTEGER, Leniency=DECREASE.
     */
    public double BLOCK_PLACE_MIN();
    /**
     * Minimum time in between healing; Type=SYSTEM, Leniency=DECREASE.
     */
    public int HEAL_TIME_MIN();
    /**
     * Minimum time in between eating; Type=SYSTEM, Leniency=DECREASE.
     */
    public int EAT_TIME_MIN();
    /**
     * Maximum percent error between predicted bow force and actual bow force; Type=DOUBLE, Leniency=INCREASE.
     */
    public double BOW_ERROR();
    /**
     * Minimum distance a player can break a block from; Type=DOUBLE, Leniency=INCREASE.
     */
    public double BLOCK_MAX_DISTANCE();
    /**
     * Minimum distance a player can break a block from in creative mode; Type=DOUBLE, Leniency=INCREASE.
     */
    public double BLOCK_MAX_DISTANCE_CREATIVE();
    /**
     * Minimum distance a player can damage an entity from; Type=DOUBLE, Leniency=INCREASE.
     */
    public double ENTITY_MAX_DISTANCE();
    /**
     * Minimum distance a player can damage an entity from in creative mode; Type=DOUBLE, Leniency=INCREASE.
     */
    public double ENTITY_MAX_DISTANCE_CREATIVE();
    /**
     * Maximum speed a player on a ladder can travel; Type=DOUBLE, Leniency=INCREASE.
     */
    public double LADDER_Y_MAX();
    /**
     * Minimum speed a player on a ladder can travel; Type=DOUBLE, Leniency=DECREASE.
     */
    public double LADDER_Y_MIN();
    /**
     * Maximum speed a player can travel up; Type=DOUBLE, Leniency=INCREASE.
     */
    public double Y_SPEED_MAX();
    /**
     * Maximum difference between the player's last 2 Y values when traveling up; Type=DOUBLE, Leniency=INCREASE.
     */
    public double Y_MAXDIFF();
    /**
     * Time in between Y difference measures; Type=SYSTEM, Leniency=DECREASE.
     */
    public int Y_TIME();
    /**
     * Maximum speed a player can travel forwards or backwards; Type=DOUBLE, Leniency=INCREASE.
     */
    public double XZ_SPEED_MAX();
    /**
     * Maximum speed a player can travel forwards or backwards while sprinting; Type=DOUBLE, Leniency=INCREASE.
     */
    public double XZ_SPEED_MAX_SPRINT();
    /**
     * Maximum speed a player can travel forwards or backwards while flying; Type=DOUBLE, Leniency=INCREASE.
     */
    public double XZ_SPEED_MAX_FLY();
    /**
     * Maximum speed a player can travel forwards or backwards while under the influence; Type=DOUBLE, Leniency=INCREASE.
     */
    public double XZ_SPEED_MAX_POTION();
    /**
     * Maximum speed a player can travel forwards or backwards while sprinting while under the influence; Type=DOUBLE, Leniency=INCREASE.
     */
    public double XZ_SPEED_MAX_POTION_SPRINT();
    /**
     * Maximum speed a player can travel forwards or backwards while sneaking; Type=DOUBLE, Leniency=INCREASE.
     */
    public double XZ_SPEED_MAX_SNEAK();
    /**
     * Maximum speed a player can travel forwards or backwards in water; Type=DOUBLE, Leniency=INCREASE.
     */
    public double XZ_SPEED_MAX_WATER();
    /**
     * Maximum speed a player can travel forwards or backwards in water while sprinting; Type=DOUBLE, Leniency=INCREASE.
     */
    public double XZ_SPEED_MAX_WATER_SPRINT();
    /**
     * Maximum speed a player can travel forwards or backwards on soul sand; Type=DOUBLE, Leniency=INCREASE.
     */
    public double XZ_SPEED_MAX_SOULSAND();
    /**
     * Maximum speed a player can travel forwards or backwards on soul sand while sprinting; Type=DOUBLE, Leniency=INCREASE.
     */
    public double XZ_SPEED_MAX_SOULSAND_SPRINT();
    /**
     * Maximum speed a player can travel forwards or backwards on soul sand while under the influence; Type=DOUBLE, Leniency=INCREASE.
     */
    public double XZ_SPEED_MAX_SOULSAND_POTION();
    /**
     * Maximum times a player can fail the speed check before action is taken; Type=INTEGER, Leniency=INCREASE.
     */
    public int SPEED_MAX();
    /**
     * The number of clicks we should wait before checking if the player has used fast inventory; Type=INTEGER; Leniency=INCREASE.
     */
    public int INVENTORY_CHECK();
    /**
     * The minimum time it should have taken for the player to click X times; Type=INTEGER; Leniency=INCREASE.
     */
    public int INVENTORY_TIMEMIN();
    /**
     * The number of steps we should wait before checking if the player has used timer cheating; Type=INTEGER; Leniency=INCREASE.
     */
    public int TIMER_STEP_CHECK();
    /**
     * The minimum time it should have taken for the player to travel X steps; Type=SYSTEM; Leniency=DECREASE.
     */
    public int TIMER_TIMEMIN();
    /**
     * Minimum travel distance for move to be considered a teleport and subsequently be ignored; Type=INTEGER, Leniency=INCREASE.
     */
    public int TELEPORT_MIN();
}

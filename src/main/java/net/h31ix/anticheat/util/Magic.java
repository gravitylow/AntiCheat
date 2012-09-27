package net.h31ix.anticheat.util;

import net.h31ix.anticheat.Anticheat;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Magic number class. Modifications to values in magic.yml will be accepted here.
 * <p>
 * Note that although each value is documented, changing it may have unintended side-effects. For instance, setting something to 0 that the plugin then uses as a dividend will result in an error.
 * <br>
 * Also realize that the smaller a value is, the less you should change it; it's probably small for a reason. The larger a value is, the safer it is to probably change it. 
 * <p>
 * <b>How to read the value documentation:</b>
 * <br>
 * First, you will see a description of the value. Then, you will see a type:
 * <br>
 * <b>Type SYSTEM:</b> This is a 'long' value, measured in milliseconds, used to compare past and future events with the current system time. Remember, 1000 ms = 1 second.
 * <br>
 * <b>Type INTEGER:</b> This is a regular number. It's typically used as something trivial, such as how many times X event can occur.
 * <br>
 * <b>Type DOUBLE:</b> This is a number that has a decimal in it. It's typically used to evaluate speed or distance.
 * <p>
 * After the type, you may see a recommendation labeled as '+Leniency'. This means 'In order to add leniency to this value, do X'
 * <br>
 * The suggestion for adding leniency will either be to INCREASE or DECREASE the variable. Doing what it suggests will cause the system to not judge the people being checked so vigorously.
 */

public class Magic
{
   /**
     * Time to exempt a player from moving because of entering/exiting a vehicle; Type=system, +Leniency=Increase.
     */    
    public final int ENTERED_EXITED_TIME;
    /**
     * Time to exempt a player from moving because of entering/exiting sneak mode; Type=system, +Leniency=Increase.
     */
    public final int SNEAK_TIME;
    /**
     * Time to exempt a player from moving because of teleporting; Type=system, +Leniency=Increase.
     */
    public final int TELEPORT_TIME;
    /**
     * Time to exempt a player from moving because of exiting fly mode; Type=system, +Leniency=Increase.
     */
    public final int EXIT_FLY_TIME;
    /**
     * Time to exempt a player from moving because of joining the server; Type=system, +Leniency=Increase.
     */   
    public final int JOIN_TIME;    
    /**
     * Time to exempt a player from fastbreak check because of using instant break; Type=seconds, +Leniency=Increase.
     */
    public final int INSTANT_BREAK_TIME;
    /**
     * Time to exempt a player from moving because of taking damage; Type=system, +Leniency=Increase.
     */    
    public final long DAMAGE_TIME;
    /**
     * Time to exempt a player from moving because of taking damage with knockback effects; Type=system, +Leniency=Increase.
     */
    public final long KNOCKBACK_DAMAGE_TIME;
    /**
     * Time to exempt a player from moving because of taking damage from an explosion; Type=system, +Leniency=Increase.
     */    
    public final long EXPLOSION_DAMAGE_TIME;    
    /**
     * Time to wait in between item drops; Type=system, +Leniency=Decrease.
     */
    public final int DROPPED_ITEM_TIME;
    /**
     * Time to wait in between firing a given number projectiles; Type=system, +Leniency=Decrease.
     */    
    public final int PROJECTILE_TIME_MIN;
    /**
     * Number of projectiles to wait for before checking how long they took to fire off; Type=integer.
     */     
    public final int PROJECTILE_CHECK;
    /**
     * Max number of blocks that can be broken in a given time; Type=integer, +Leniency=Increase.
     */     
    public final int FASTBREAK_LIMIT;
    /**
     * Time to wait before checking block breaks; Type=system, +Leniency=Increase.
     */      
    public final int FASTBREAK_TIMEMAX;
    /**
     * Number of times fastbreak can be observed before taking action; Type=integer, +Leniency=Increase.
     */      
    public final int FASTBREAK_MAXVIOLATIONS;
    /**
     * Number of times fastbreak can be observed before taking action in creative mode; Type=integer, +Leniency=Increase.
     */ 
    public final int FASTBREAK_MAXVIOLATIONS_CREATIVE;
    /**
     * Time a player is forced to wait after fastbreak has been detected; Type=system, +Leniency=Decrease.
     */    
    public final int FASTBREAK_MAXVIOLATIONTIME;
    /**
     * Number of blocks that can be broken without being hit before taking action; Type=integer, +Leniency=Increase.
     */     
    public final int FASTPLACE_ZEROLIMIT;
    /**
     * Time to wait before checking block places; Type=system, +Leniency=Increase.
     */ 
    public final int FASTPLACE_TIMEMAX;
    /**
     * Number of times fastplace can be observed before taking action; Type=integer, +Leniency=Increase.
     */ 
    public final int FASTPLACE_MAXVIOLATIONS;
    /**
     * Number of times fastplace can be observed before taking action in creative mode; Type=integer, +Leniency=Increase.
     */ 
    public final int FASTPLACE_MAXVIOLATIONS_CREATIVE;
    /**
     * Time a player is forced to wait after fastplace has been detected; Type=system, +Leniency=Decrease.
     */ 
    public final int FASTPLACE_MAXVIOLATIONTIME;
    /**
     * Number of times required to punch a block before it breaks; Type=integer, +Leniency=Decrease.
     */ 
    public final int BLOCK_PUNCH_MIN;
    /**
     * Number of chat spam violations before a player is warned; Type=integer, +Leniency=Increase.
     */ 
    public final int CHAT_WARN_LEVEL;
    /**
     * Number of chat spam violations before a player is kicked; Type=integer, +Leniency=Increase.
     */
    public final int CHAT_KICK_LEVEL;
    /**
     * Number of kicks for chat violation before a player is banned; Type=integer, +Leniency=Increase.
     */
    public final int CHAT_BAN_LEVEL;
    /**
     * Number of times a player can be caught in-flight before action is taken; Type=integer, +Leniency=Increase.
     */
    public final int FLIGHT_LIMIT;
    /**
     * Maximum speed that a player can climb a waterfall; Type=double, +Leniency=Increase.
     */
    public final double WATER_CLIMB_MAX;
    /**
     * Number of times a player can fly on the y-axis before action is taken; Type=integer, +Leniency=Increase.
     */
    public final int Y_MAXVIOLATIONS;
    /**
     * Time a player is forced to wait after flying on the y-axis; Type=system, +Leniency=Decrease.
     */
    public final int Y_MAXVIOTIME;
    /**
     * Number of times a player can fail a velocity check before action is taken; Type=integer, +Leniency=Increase.
     */
    public final int VELOCITY_TIME;
    /**
     * Time used to schedule increasing a players velocity count - probably not touching this would be good; Type=integer.
     */    
    public final long VELOCITY_SCHETIME;
    /**
     * Maximum time a player is considered to have a change in velocity; Type=system, +Leniency=Increase.
     */
    public final long VELOCITY_CHECKTIME;
    /**
     * Time to extend a player's velocity count by; Type=system, +Leniency=Increase.
     */
    public final long VELOCITY_PREVENT;
    /**
     * Number of times a player can fail the velocity check before action is taken; Type=integer, +Leniency=Increase.
     */
    public final int VELOCITY_MAXTIMES;
    /**
     * Number of times a player can fail the nofall check before action is taken; Type=integer, +Leniency=Increase.
     */
    public final int NOFALL_LIMIT;
    /**
     * Number of times a player can fail the ascension check before action is taken; Type=integer, +Leniency=Increase.
     */
    public final int ASCENSION_COUNT_MAX;
    /**
     * Number of times a player can fail the ascension check while in water before action is taken; Type=integer, +Leniency=Increase.
     */
    public final int WATER_ASCENSION_VIOLATION_MAX;
    /**
     * Number of times a player can be caught speeding in water before action is taken; Type=integer, +Leniency=Increase.
     */
    public final int WATER_SPEED_VIOLATION_MAX;
    /**
     * Number of times a player can sprint without proper food level before action is taken; Type=integer, +Leniency=Increase.
     */
    public final int SPRINT_FOOD_MIN;
    /**
     * Minimum time between animations; Type=system, +Leniency=Increase.
     */
    public final int ANIMATION_MIN;
    /**
     * Minimum time between chats; Type=system, +Leniency=Increase.
     */
    public final int CHAT_MIN;
    /**
     * Minimum time between repeating yourself in the chat; Type=system, +Leniency=Increase.
     */
    public final int CHAT_REPEAT_MIN;
    /**
     * Maximum speed you can travel while sprinting; Type=double, +Leniency=Increase.
     */
    public final double SPRINT_MIN;
    /**
     * Minimum time in between block breaks; Type=seconds, +Leniency=Decrease.
     */
    public final double BLOCK_BREAK_MIN;
    /**
     * Minimum time in between block placing; Type=seconds, +Leniency=Decrease.
     */
    public final double BLOCK_PLACE_MIN;
    /**
     * Minimum time in between healing; Type=system, +Leniency=Decrease.
     */
    public final long HEAL_TIME_MIN;
    /**
     * Minimum time in between eating; Type=system, +Leniency=Decrease.
     */
    public final long EAT_TIME_MIN;
    /**
     * Maximum percent error between predicted bow force and actual bow force; Type=double, +Leniency=Increase.
     */
    public final double BOW_ERROR;
    /**
     * Minimum distance a player can break a block from; Type=double, +Leniency=Increase.
     */
    public final double BLOCK_MAX_DISTANCE;
    /**
     * Minimum distance a player can damage an entity from; Type=double, +Leniency=Increase.
     */
    public final double ENTITY_MAX_DISTANCE;
    /**
     * Maximum speed a player on a ladder can travel; Type=double, +Leniency=Increase.
     */
    public final double LADDER_Y_MAX;
    /**
     * Minimum speed a player on a ladder can travel; Type=double, +Leniency=Decrease.
     */
    public final double LADDER_Y_MIN;
    /**
     * Maximum speed a player can travel up; Type=double, +Leniency=Increase.
     */
    public final double Y_SPEED_MAX;
    /**
     * Maximum difference between the player's last 2 Y values when traveling up; Type=double, +Leniency=Increase.
     */
    public final double Y_MAXDIFF;
    /**
     * Time in between Y difference measures; Type=system, +Leniency=Decrease.
     */
    public final long Y_TIME;
    /**
     * Maximum speed a player can travel forwards or backwards; Type=double, +Leniency=Increase.
     */
    public final double XZ_SPEED_MAX;
    /**
     * Maximum speed a player can travel forwards or backwards while sprinting; Type=double, +Leniency=Increase.
     */
    public final double XZ_SPEED_MAX_SPRINT;
    /**
     * Maximum speed a player can travel forwards or backwards while flying; Type=double, +Leniency=Increase.
     */
    public final double XZ_SPEED_MAX_FLY;
    /**
     * Maximum speed a player can travel forwards or backwards while under the influence; Type=double, +Leniency=Increase.
     */
    public final double XZ_SPEED_MAX_POTION;
    /**
     * Maximum speed a player can travel forwards or backwards while sneaking; Type=double, +Leniency=Increase.
     */
    public final double XZ_SPEED_MAX_SNEAK;
    /**
     * Maximum speed a player can travel forwards or backwards in water; Type=double, +Leniency=Increase.
     */
    public final double XZ_SPEED_MAX_WATER;
    /**
     * Maximum speed a player can travel forwards or backwards in water while sprinting; Type=double, +Leniency=Increase.
     */
    public final double XZ_SPEED_MAX_WATER_SPRINT;
    /**
     * Maximum times a player can fail the speed check before action is taken; Type=integer, +Leniency=Increase.
     */
    public final int SPEED_MAX;     
    
    public Magic(FileConfiguration magic)
    { 
         ENTERED_EXITED_TIME = magic.getInt("ENTERED_EXITED_TIME");
         SNEAK_TIME = magic.getInt("SNEAK_TIME");
         TELEPORT_TIME = magic.getInt("TELEPORT_TIME");
         EXIT_FLY_TIME = magic.getInt("EXIT_FLY_TIME");
         JOIN_TIME = magic.getInt("JOIN_TIME");    
         INSTANT_BREAK_TIME = magic.getInt("INSTANT_BREAK_TIME"); 
         DAMAGE_TIME = magic.getLong("DAMAGE_TIME");
         KNOCKBACK_DAMAGE_TIME = magic.getLong("KNOCKBACK_DAMAGE_TIME");  
         EXPLOSION_DAMAGE_TIME = magic.getLong("EXPLOSION_DAMAGE_TIME");    
         DROPPED_ITEM_TIME = magic.getInt("DROPPED_ITEM_TIME");   
         PROJECTILE_TIME_MIN = magic.getInt("PROJECTILE_TIME_MIN");    
         PROJECTILE_CHECK = magic.getInt("PROJECTILE_CHECK");   
         FASTBREAK_LIMIT = magic.getInt("FASTBREAK_LIMIT");    
         FASTBREAK_TIMEMAX = magic.getInt("FASTBREAK_TIMEMAX");     
         FASTBREAK_MAXVIOLATIONS = magic.getInt("FASTBREAK_MAXVIOLATIONS");
         FASTBREAK_MAXVIOLATIONS_CREATIVE = magic.getInt("FASTBREAK_MAXVIOLATIONS_CREATIVE");   
         FASTBREAK_MAXVIOLATIONTIME = magic.getInt("FASTBREAK_MAXVIOLATIONTIME");     
         FASTPLACE_ZEROLIMIT = magic.getInt("FASTPLACE_ZEROLIMIT");
         FASTPLACE_TIMEMAX = magic.getInt("FASTPLACE_TIMEMAX");
         FASTPLACE_MAXVIOLATIONS = magic.getInt("FASTPLACE_MAXVIOLATIONS");
         FASTPLACE_MAXVIOLATIONS_CREATIVE = magic.getInt("FASTPLACE_MAXVIOLATIONS_CREATIVE");
         FASTPLACE_MAXVIOLATIONTIME = magic.getInt("FASTPLACE_MAXVIOLATIONTIME");
         BLOCK_PUNCH_MIN = magic.getInt("BLOCK_PUNCH_MIN");
         CHAT_WARN_LEVEL = magic.getInt("CHAT_WARN_LEVEL");
         CHAT_KICK_LEVEL = magic.getInt("CHAT_KICK_LEVEL");
         CHAT_BAN_LEVEL = magic.getInt("CHAT_BAN_LEVEL");
         FLIGHT_LIMIT = magic.getInt("FLIGHT_LIMIT");
         WATER_CLIMB_MAX = magic.getDouble("WATER_CLIMB_MAX");
         Y_MAXVIOLATIONS = magic.getInt("Y_MAXVIOLATIONS");
         Y_MAXVIOTIME = magic.getInt("Y_MAXVIOTIME");
         VELOCITY_TIME = magic.getInt("VELOCITY_TIME");   
         VELOCITY_SCHETIME = magic.getLong("VELOCITY_SCHETIME");
         VELOCITY_CHECKTIME = magic.getLong("VELOCITY_CHECKTIME");
         VELOCITY_PREVENT = magic.getLong("VELOCITY_PREVENT");
         VELOCITY_MAXTIMES = magic.getInt("VELOCITY_MAXTIMES");
         NOFALL_LIMIT = magic.getInt("NOFALL_LIMIT");
         ASCENSION_COUNT_MAX = magic.getInt("ASCENSION_COUNT_MAX");
         WATER_ASCENSION_VIOLATION_MAX = magic.getInt("WATER_ASCENSION_VIOLATION_MAX");
         WATER_SPEED_VIOLATION_MAX = magic.getInt("WATER_SPEED_VIOLATION_MAX");
         SPRINT_FOOD_MIN = magic.getInt("SPRINT_FOOD_MIN");
         ANIMATION_MIN = magic.getInt("ANIMATION_MIN");
         CHAT_MIN = magic.getInt("CHAT_MIN");
         CHAT_REPEAT_MIN = magic.getInt("CHAT_REPEAT_MIN");
         SPRINT_MIN = magic.getDouble("SPRINT_MIN");
         BLOCK_BREAK_MIN = magic.getDouble("BLOCK_BREAK_MIN");
         BLOCK_PLACE_MIN = magic.getDouble("BLOCK_PLACE_MIN");
         HEAL_TIME_MIN = magic.getLong("HEAL_TIME_MIN");
         EAT_TIME_MIN = magic.getLong("EAT_TIME_MIN");
         BOW_ERROR = magic.getDouble("BOW_ERROR");
         BLOCK_MAX_DISTANCE = magic.getDouble("BLOCK_MAX_DISTANCE");
         ENTITY_MAX_DISTANCE = magic.getDouble("ENTITY_MAX_DISTANCE");
         LADDER_Y_MAX = magic.getDouble("LADDER_Y_MAX");
         LADDER_Y_MIN = magic.getDouble("LADDER_Y_MIN");
         Y_SPEED_MAX = magic.getDouble("Y_SPEED_MAX");
         Y_MAXDIFF = magic.getDouble("Y_MAXDIFF");
         Y_TIME = magic.getLong("Y_TIME ");
         XZ_SPEED_MAX = magic.getDouble("XZ_SPEED_MAX");
         XZ_SPEED_MAX_SPRINT = magic.getDouble("XZ_SPEED_MAX_SPRINT");
         XZ_SPEED_MAX_FLY = magic.getDouble("XZ_SPEED_MAX_FLY");
         XZ_SPEED_MAX_POTION = magic.getDouble("XZ_SPEED_MAX_POTION");
         XZ_SPEED_MAX_SNEAK = magic.getDouble("XZ_SPEED_MAX_SNEAK");
         XZ_SPEED_MAX_WATER = magic.getDouble("XZ_SPEED_MAX_WATER");
         XZ_SPEED_MAX_WATER_SPRINT = magic.getDouble("XZ_SPEED_MAX_WATER_SPRINT");
         SPEED_MAX = magic.getInt("SPEED_MAX");         
    }
}

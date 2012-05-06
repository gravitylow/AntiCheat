package net.h31ix.anticheat.checks;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.PlayerTracker;
import net.h31ix.anticheat.manage.AnimationManager;
import net.h31ix.anticheat.manage.BlockManager;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class FastbreakCheck implements Runnable 
{
    private Player player;
    private AnimationManager am;
    private BlockBreakEvent event;
    private PlayerTracker tracker;
    private Anticheat plugin;
        
    Thread thread = new Thread(this);

    public FastbreakCheck(Player player, BlockBreakEvent event, Anticheat plugin) 
    {
        this.player = player;
        this.am = plugin.am;
        this.event = event;
        this.tracker = plugin.getPlayerTracker();
        this.plugin = plugin;
    }
        
    public void start() 
    {
        thread.start();
    }     

    @Override
    public void run() 
    {
        if(!am.swungArm(player))
        {
            tracker.increaseLevel(player,2);
            plugin.log(player.getName()+" didn't finish breaking a block!");
            event.setCancelled(true);
        }
    }
}
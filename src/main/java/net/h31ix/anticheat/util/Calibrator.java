package net.h31ix.anticheat.util;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Calibrator implements Listener
{
    private final Player player;
    private int step = 0;
    private static final int TOTAL = 30;
    private long lastLong;
    private int lastInt;
    private int lastDouble;
    private long [] longs = new long[10];
    private double [] doubles = new double[10];
    private int [] ints = new int[10];

    // Value-specific calculations
    private long punchAvg;
    private double blockBreakMax;
    private double blockPlaceMax;

    public Calibrator(Player player)
    {
        this.player = player;
        player.sendMessage(ChatColor.GREEN+"----You have entered calibration mode.----");
        // Wait for listener to be registered and calibrate method called.
    }

    public void calibrate()
    {
        switch(step)
        {
            case 0:
                sendStep();
                player.sendMessage("Spam the left mouse button as fast as possible.");
                break;
            case 2:
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage("Break blocks from as far away as you can possibly get from them.");
                break;
            case 3:
                player.sendMessage("Place blocks from as far away as you can possibly get from them.");
                break;
        }

    }

    private int getLongCount()
    {
        int fill = 0;
        for(long l : longs)
        {
            if(l != 0)
            {
                fill++;
            }
        }
        return fill;
    }

    private int getDoubleCount()
    {
        int fill = 0;
        for(double d : doubles)
        {
            if(d != 0)
            {
                fill++;
            }
        }
        return fill;
    }

    private void clear()
    {
        for(int i =0;i<longs.length;i++)
        {
            longs[i] = 0;
        }
        for(int i =0;i<doubles.length;i++)
        {
            doubles[i] = 0;
        }
    }

    private void sendStep()
    {
        step++;
        player.sendMessage(ChatColor.GREEN+"Step "+step+"/"+TOTAL);
    }

    private void getAvgLong(long avg)
    {
        int count = getLongCount();
        if(count < 10)
        {
            if(lastLong == 0)
            {
                lastLong = System.currentTimeMillis();
            }
            else
            {
                longs[count] = System.currentTimeMillis()-lastLong;
                lastLong = System.currentTimeMillis();
            }
        }
        else
        {
            for(long l : longs)
            {
                avg+=l;
            }
            lastLong = 0;
            clear();
            avg/=10;
            player.sendMessage(ChatColor.AQUA+"Done. Average calculated at "+ChatColor.WHITE+avg);
            sendStep();
            calibrate();
        }
    }

    private void getMaxDouble(double max, double length)
    {
        int count = getDoubleCount();
        System.out.println(count);
        if(count < 10)
        {
            doubles[count] = length;
        }
        else
        {
            for(double d : doubles)
            {
                if(max == 0 || d > max)
                {
                    max = d;
                }
            }
            clear();
            player.sendMessage(ChatColor.AQUA+"Done. Maximum calculated at "+ChatColor.WHITE+max);
            sendStep();
            calibrate();
        }
    }



    // Begin event listeners

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if(step == 1)
        {
            getAvgLong(punchAvg);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if(step == 2)
        {
            double lengthX = Math.abs(event.getBlock().getX()-event.getPlayer().getLocation().getX());
            double lengthZ = Math.abs(event.getBlock().getZ()-event.getPlayer().getLocation().getZ());
            getMaxDouble(blockBreakMax, lengthX >= lengthZ ? lengthX : lengthZ);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if(step == 3)
        {
            double lengthX = Math.abs(event.getBlock().getX()-event.getPlayer().getLocation().getX());
            double lengthZ = Math.abs(event.getBlock().getZ()-event.getPlayer().getLocation().getZ());
            getMaxDouble(blockPlaceMax, lengthX >= lengthZ ? lengthX : lengthZ);
        }
    }
}

// Not production code yet. Still experimental and needs a lot of work.

package net.h31ix.anticheat.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Calibrator implements Listener {
    private final Player player;
    private int step = 0;
    private static final int TOTAL = 30;
    private long lastLong;
    private int lastInt;
    private int lastDouble;
    private long[] longs = new long[10];
    private double[] doubles = new double[10];
    private int[] ints = new int[10];
    private int times = 0;
    
    // Value-specific calculations
    private long projectileTime;
    private long dropItemTime;
    private long punchAvg;
    private double blockBreakMax;
    private double blockPlaceMax;
    
    public Calibrator(Player player) {
        this.player = player;
        player.sendMessage(ChatColor.GREEN + "----You have entered calibration mode.----");
        // Wait for listener to be registered and calibrate method called.
    }
    
    public void calibrate() {
        switch (step) {
            case 0:
                sendStep();
                player.getInventory().setItemInHand(new ItemStack(Material.BOW));
                player.getInventory().addItem(new ItemStack(Material.ARROW, 64));
                player.sendMessage("Fire arrows as fast as possible (don't pull all the way back)");
                break;
            case 2:
                player.getInventory().clear();
                player.getInventory().setItemInHand(new ItemStack(Material.DIRT, 64));
                player.sendMessage("Drop items as fast as possible.");
                /*
                 * case 0: sendStep(); player.sendMessage("Spam the left mouse button as fast as possible."); break; case 2: player.setGameMode(GameMode.CREATIVE); player.sendMessage("Break blocks from as far away as you can possibly get from them."); break; case 3:
                 * player.sendMessage("Place blocks from as far away as you can possibly get from them."); break;
                 */
        }
        
    }
    
    private int getLongCount() {
        int fill = 0;
        for (long l : longs) {
            if (l != 0) {
                fill++;
            }
        }
        return fill;
    }
    
    private int getDoubleCount() {
        int fill = 0;
        for (double d : doubles) {
            if (d != 0) {
                fill++;
            }
        }
        return fill;
    }
    
    private void clear() {
        for (int i = 0; i < longs.length; i++) {
            longs[i] = 0;
        }
        for (int i = 0; i < ints.length; i++) {
            ints[i] = 0;
        }
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = 0;
        }
        lastInt = 0;
        lastDouble = 0;
        lastLong = 0;
        times = 0;
        
    }
    
    private void sendStep() {
        step++;
        player.sendMessage(ChatColor.GREEN + "Step " + step + "/" + TOTAL);
    }
    
    // Find the average time it takes to do a task
    private long getAvgLong() {
        int count = getLongCount();
        if (count < 10) {
            if (lastLong == 0) {
                lastLong = System.currentTimeMillis();
            } else {
                longs[count] = System.currentTimeMillis() - lastLong;
                lastLong = System.currentTimeMillis();
            }
        } else {
            long avg = 0;
            for (long l : longs) {
                avg += l;
            }
            clear();
            avg /= 10;
            player.sendMessage(ChatColor.AQUA + "Done. Average calculated at " + ChatColor.WHITE + avg);
            sendStep();
            calibrate();
            return avg;
        }
        return -1;
    }
    
    // Find the maximum time it takes to do a task
    private double getMaxDouble(double length) {
        int count = getDoubleCount();
        if (count < 10) {
            doubles[count] = length;
        } else {
            double max = 0;
            for (double d : doubles) {
                if (max == 0 || d > max) {
                    max = d;
                }
            }
            clear();
            player.sendMessage(ChatColor.AQUA + "Done. Maximum calculated at " + ChatColor.WHITE + max);
            sendStep();
            calibrate();
            return max;
        }
        return -1;
    }
    
    // Get the absolute time it takes to do something X times
    private long getAbsoluteLong() {
        int count = getLongCount();
        if (count < 10) {
            if (lastLong == 0) {
                lastLong = System.currentTimeMillis();
            } else {
                longs[count] = System.currentTimeMillis();
                lastLong = System.currentTimeMillis();
            }
        } else {
            long val = longs[9] - longs[0];
            clear();
            player.sendMessage(ChatColor.AQUA + "Done. Value calculated at " + ChatColor.WHITE + val);
            sendStep();
            calibrate();
            return val;
        }
        return -1;
    }
    
    
    // Begin event listeners
    
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getPlayer() == player) {
            if (step == 2) {
                long v = getAbsoluteLong();
                if (v != -1) {
                    dropItemTime = v;
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player && ((Player) event.getEntity()) == player) {
            if (step == 1) {
                long v = getAbsoluteLong();
                if (v != -1) {
                    projectileTime = v;
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer() == player) {
            if (step == -1) {
                long v = getAvgLong();
                if (v != -1) {
                    punchAvg = v;
                }
            } else if (step == 1) {
                
            }
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer() == player) {
            if (step == -1) {
                double lengthX = Math.abs(event.getBlock().getX() - event.getPlayer().getLocation().getX());
                double lengthZ = Math.abs(event.getBlock().getZ() - event.getPlayer().getLocation().getZ());
                double v = getMaxDouble(lengthX >= lengthZ ? lengthX : lengthZ);
                if (v != -1) {
                    blockBreakMax = v;
                }
            }
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer() == player) {
            if (step == -1) {
                double lengthX = Math.abs(event.getBlock().getX() - event.getPlayer().getLocation().getX());
                double lengthZ = Math.abs(event.getBlock().getZ() - event.getPlayer().getLocation().getZ());
                double v = getMaxDouble(lengthX >= lengthZ ? lengthX : lengthZ);
                if (v != -1) {
                    blockPlaceMax = v;
                }
            }
        }
    }
}

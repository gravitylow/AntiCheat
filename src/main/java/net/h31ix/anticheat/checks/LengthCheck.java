package net.h31ix.anticheat.checks;

import org.bukkit.Location;

public class LengthCheck {
    private Location l1, l2;

    public LengthCheck(Location l1, Location l2)
    {
        this.l1 = l1;
        this.l2 = l2;
    }
    
    public double getXDifference()
    {
        return Math.abs(l1.getX()-l2.getX());   
    }
    
    public double getZDifference()
    {
        return Math.abs(l1.getZ()-l2.getZ());   
    }    
    
    public double getYDifference()
    {
        return Math.abs(l1.getY()-l2.getY());   
    }    
    
}

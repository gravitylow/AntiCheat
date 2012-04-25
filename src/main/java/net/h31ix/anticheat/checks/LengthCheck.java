package net.h31ix.anticheat.checks;

import org.bukkit.Location;

public class LengthCheck {
    Location l1, l2;
    //Used for checking how far a player travels on an event and how far they are from another location
    public LengthCheck(Location l1, Location l2)
    {
        this.l1 = l1;
        this.l2 = l2;
    }
    
    public double getXDifference()
    {
        double bx = l1.getX();
        double px = l2.getX();
        double xdiff = Math.abs(bx-px);   
        return xdiff;
    }
    
    public double getZDifference()
    {
        double bz = l1.getZ();
        double pz = l2.getZ();
        double zdiff = Math.abs(bz-pz);   
        return zdiff;
    }    
    
    public double getYDifference()
    {
        double by = l1.getY();
        double py = l2.getY();
        double ydiff = Math.abs(by-py);   
        return ydiff;
    }    
    
}

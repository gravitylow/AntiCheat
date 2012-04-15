package net.h31ix.anticheat.checks;

import org.bukkit.Location;

public class LengthCheck {
    Location l1, l2;
    
    public LengthCheck(Location l1, Location l2)
    {
        this.l1 = l1;
        this.l2 = l2;
    }
    
    public double getXDifference()
    {
        double bx = l1.getX();
        double px = l2.getX();
        double xdiff = bx-px;
        if(xdiff < 0)
        {
            xdiff = -xdiff;
        }    
        return xdiff;
    }
    
    public double getZDifference()
    {
        double bz = l1.getZ();
        double pz = l2.getZ();
        double zdiff = bz-pz;
        if(zdiff < 0)
        {
            zdiff = -zdiff;
        }    
        return zdiff;
    }    
    
    public double getYDifference()
    {
        double by = l1.getY();
        double py = l2.getY();
        double ydiff = by-py;
        if(ydiff < 0)
        {
            ydiff = -ydiff;
        }      
        return ydiff;
    }    
    
}

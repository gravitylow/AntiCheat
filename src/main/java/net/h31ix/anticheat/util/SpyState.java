package net.h31ix.anticheat.util;

import org.bukkit.Location;

public class SpyState
{
    private boolean allowFlight;
    private boolean flying;
    private Location location;

    public SpyState(boolean allowFlight, boolean flying, Location location)
    {
        this.allowFlight = allowFlight;
        this.flying = flying;
        this.location = location;
    }

    public boolean getAllowFlight()
    {
        return this.allowFlight;
    }

    public boolean getFlying()
    {
        return this.flying;
    }

    public Location getLocation()
    {
        return this.location;
    }
}

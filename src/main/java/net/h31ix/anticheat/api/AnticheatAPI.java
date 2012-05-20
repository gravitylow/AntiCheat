package net.h31ix.anticheat.api;

import org.bukkit.entity.Player;

import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.manage.AnticheatManager;
import net.h31ix.anticheat.manage.CheckManager;
import net.h31ix.anticheat.manage.CheckType;
import net.h31ix.anticheat.manage.PlayerManager;
import net.h31ix.anticheat.xray.XRayTracker;

public class AnticheatAPI 
{
    private static CheckManager chk = Anticheat.getManager().getCheckManager();
    private static PlayerManager pmr = Anticheat.getManager().getPlayerManager();
    private static XRayTracker xtracker = Anticheat.getManager().getXRayTracker();

    //CheckManager API.

    public static void activateCheck(CheckType type) 
    {
        chk.activateCheck(type);
    }

    public static void deactivateCheck(CheckType type) 
    {
        chk.deactivateCheck(type);
    }

    public static boolean isActive(CheckType type)
    {
        return chk.isActive(type);
    }

    public static void exemptPlayer(Player player, CheckType type)
    {
        chk.exemptPlayer(player, type);
    }

    public static void unexemptPlayer(Player player, CheckType type)
    {
        chk.unexemptPlayer(player, type);
    }

    public static boolean isExempt(Player player, CheckType type)
    {
        return chk.isExempt(player, type);
    }

    //PlayerManager API

    public static int getLevel(Player player)
    {
        if(!pmr.hasLevel(player)) 
        {
            return 0;
        }
        return pmr.getLevel(player);
    }

    public static void setLevel(Player player, int x)
    {
        pmr.setLevel(player, x);
    }

    //XrayTracker API

    public static boolean isXrayer(Player player)
    {
        String name = player.getName();
        if (xtracker.sufficientData(name) && xtracker.hasAbnormal(name)) 
        {
            return true;
        }
        return false;
    }

    //Advanced Users Only API.

    public static AnticheatManager getManager() 
    {
        return Anticheat.getManager();
    }
}
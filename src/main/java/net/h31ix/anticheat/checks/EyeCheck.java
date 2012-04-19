package net.h31ix.anticheat.checks;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.EntityComplex;
import net.minecraft.server.EntityComplexPart;
import net.minecraft.server.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class EyeCheck {
    Player player;
    Entity entity;
    
    public EyeCheck(Player player, Entity entity)
    {
        this.player = player;
        this.entity = entity;
    }
    
    public boolean isLooking()
    {
        //TODO: Fix this mess!
        /**Location startLoc = player.getEyeLocation();
        int direction = (int)((-180)+startLoc.getYaw());
        int radius = 100;
        int degrees = 30;
        List<Entity> entitys = new ArrayList<Entity>();
        int[] startPos = new int[] { (int)startLoc.getX(), (int)startLoc.getZ() };
        int[] endA = new int[] { (int)(radius * Math.cos(direction - (degrees/2))), (int)(radius * Math.sin(direction - (degrees/2))) };
        int[] endB = new int[] { (int)(radius * Math.cos(direction + (degrees/2))), (int)(radius * Math.sin(direction + (degrees/2))) };
       
        Location l = entity.getBukkitEntity().getLocation();
        if (!isPointInCircle(startPos[0], startPos[1], radius, l.getBlockX(), l.getBlockZ())) {
            return false;
        }
        else
        {
            return true;
        }**/
        return true; 
    }
    
        private static int[] getVectorForPoints(int x1, int y1, int x2, int y2) {
            return new int[] { Math.abs(x2-x1), Math.abs(y2-y1) };
        }

        private static boolean isPointInCircle(int cx, int cy, int radius, int px, int py) {
            double dist = (px-cx)^2 + (py-cy)^2;
            return dist < (radius^2);
        }

        private static double getAngleBetweenVectors(int[] vector1, int[] vector2){
            return Math.atan2(vector2[1], vector2[0]) - Math.atan2(vector1[1], vector1[0]);
        }    
}

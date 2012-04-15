/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.h31ix.anticheat.checks;

import net.minecraft.server.EntityComplex;
import net.minecraft.server.EntityComplexPart;
import net.minecraft.server.Entity;
import org.bukkit.Location;
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
    
    public double getOffset()
    {
        //Thanks to Evenprime (NoCheat)
        if(entity instanceof EntityComplex || entity instanceof EntityComplexPart) 
        {
            return -1;
        }
        
        final float width = entity.length > entity.width ? entity.length : entity.width;
        final double height = entity.boundingBox.e - entity.boundingBox.b;
        final double off = directionCheck(player, entity.locX, entity.locY + (height / 2D), entity.locZ, width, height, 75);
        return off;
    }
    
    public final double directionCheck(final Player player, final double targetX, final double targetY, final double targetZ, final double targetWidth, final double targetHeight, final double precision) 
    {
        //Thanks to Evenprime (NoCheat)
        final Location eyes = player.getPlayer().getEyeLocation();
        final double factor = Math.sqrt(Math.pow(eyes.getX() - targetX, 2) + Math.pow(eyes.getY() - targetY, 2) + Math.pow(eyes.getZ() - targetZ, 2));
        final Vector direction = eyes.getDirection();
        final double x = ((double) targetX) - eyes.getX();
        final double y = ((double) targetY) - eyes.getY();
        final double z = ((double) targetZ) - eyes.getZ();
        final double xPrediction = factor * direction.getX();
        final double yPrediction = factor * direction.getY();
        final double zPrediction = factor * direction.getZ();
        double off = 0.0D;
        off += Math.max(Math.abs(x - xPrediction) - (targetWidth / 2 + precision), 0.0D);
        off += Math.max(Math.abs(z - zPrediction) - (targetWidth / 2 + precision), 0.0D);
        off += Math.max(Math.abs(y - yPrediction) - (targetHeight / 2 + precision), 0.0D);
        if(off > 1) {
            off = Math.sqrt(off);
        }
        return off;
    }    
        
        
    
}

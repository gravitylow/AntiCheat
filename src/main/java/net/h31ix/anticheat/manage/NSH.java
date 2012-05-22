package net.h31ix.anticheat.manage;

import net.h31ix.anticheat.api.AnticheatAPI;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetServerHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet202Abilities;

public class NSH extends NetServerHandler
{

    public NSH(MinecraftServer server, NetworkManager manager, EntityPlayer player) 
    {
        super(server, manager, player);
    }
        
    @Override
    public void a(Packet202Abilities packet202abilities) 
    {
        if(this.player.abilities.isFlying)
        {
            if(!packet202abilities.b && this.player.abilities.canFly)
            {
                AnticheatAPI.getManager().getBackend().logExitFly(player.getBukkitEntity().getPlayer());
            }
        }
        this.player.abilities.isFlying = packet202abilities.b && this.player.abilities.canFly;
    }    
}
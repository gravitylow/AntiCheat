package net.h31ix.anticheat.xray;

/**
 * THIS CLASS NOT ACTIVE AND IS
 * IN DEVELOPMENT
 */

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetServerHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet51MapChunk;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class NSH extends NetServerHandler {
    
    public NSH(MinecraftServer server, NetworkManager manager, EntityPlayer player)
    {
        super(server,manager,player);
        this.player = player;
    }   
    
    @Override
    public void sendPacket(Packet packet){
        if(packet instanceof Packet51MapChunk){
             Packet51MapChunk pack = (Packet51MapChunk) packet;
             Chunk chunk = player.world.getChunkAt(pack.a,pack.b).bukkitChunk;
             Block block;
             for(int i=0;i<16;i++)
             {
                 for(int i1=0;i1<16;i1++)
                 {
                     for(int i2=256;i2<256;i2++)
                     {
                         block = chunk.getBlock(i, i1, i2);
                     }
                 }                 
             }
        }
        super.sendPacket(packet);
    }
}

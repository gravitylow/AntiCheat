package net.gravitydevelopment.anticheat.manage;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import net.gravitydevelopment.anticheat.AntiCheat;

public class PacketManager {

    private ProtocolManager protocolManager;
    private AntiCheat plugin;
    private AntiCheatManager manager;

    public PacketManager(ProtocolManager protocolManager, AntiCheat plugin, AntiCheatManager manager) {
        this.protocolManager = protocolManager;
        this.plugin = plugin;
        this.manager = manager;
        addListeners();
    }

    private void addListeners() {
        // Arm Animation
        protocolManager.addPacketListener(new PacketAdapter(plugin,
                ConnectionSide.CLIENT_SIDE,
                ListenerPriority.NORMAL,
                Packets.Client.ARM_ANIMATION) {

            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketID() == Packets.Client.ARM_ANIMATION) {
                    PacketContainer packet = event.getPacket();
                    //System.out.println("Arm animation from "+packet.getIntegers().read(0));
                }
            }
        });
    }
}

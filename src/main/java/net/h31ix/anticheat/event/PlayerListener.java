package net.h31ix.anticheat.event;

import net.h31ix.anticheat.Anticheat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class PlayerListener implements Listener {
    Anticheat plugin;
    
    public PlayerListener(Anticheat plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event)
    {
        plugin.cm.addChat(event.getPlayer());
    }
    
}

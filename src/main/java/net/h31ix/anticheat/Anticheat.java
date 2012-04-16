package net.h31ix.anticheat;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.h31ix.anticheat.event.BlockListener;
import net.h31ix.anticheat.event.EntityListener;
import net.h31ix.anticheat.event.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Anticheat extends JavaPlugin {
    public ChatManager cm = new ChatManager(this);
    private static final Logger l = Logger.getLogger("Minecraft");
    
    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
    }
    
    public void log(String s)
    {
        l.log(Level.WARNING,s);
    }
}


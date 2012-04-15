package net.h31ix.anticheat;

import org.bukkit.plugin.java.JavaPlugin;

public class Anticheat extends JavaPlugin {
    public ChatManager cm = new ChatManager(this);
    
    public void onDisable() {
    }

    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }
}


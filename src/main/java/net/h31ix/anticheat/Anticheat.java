package net.h31ix.anticheat;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.h31ix.anticheat.event.BlockListener;
import net.h31ix.anticheat.event.EntityListener;
import net.h31ix.anticheat.event.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Anticheat extends JavaPlugin {
    public ChatManager cm = new ChatManager(this);
    public AnimationManager am = new AnimationManager(this);
    public VehicleManager vm = new VehicleManager(this);
    private static final Logger l = Logger.getLogger("Minecraft");
    private long lastTime = System.currentTimeMillis();
    private long time = 0;
    public boolean lagged = false;
    
    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() 
        {
            public void run() 
            {
                long l = System.currentTimeMillis();
                time = l-lastTime;
                lastTime = l;
                time = time/10;
                lagged = time > 1500;
            }
        },      200L, 200L);
    }
    
    public void log(String s)
    {
        l.log(Level.WARNING,s);
    }
}


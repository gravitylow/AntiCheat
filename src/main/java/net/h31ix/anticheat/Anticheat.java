package net.h31ix.anticheat;

import net.h31ix.anticheat.manage.ExemptManager;
import net.h31ix.anticheat.manage.ChatManager;
import net.h31ix.anticheat.manage.AnimationManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.h31ix.anticheat.command.CommandManager;
import net.h31ix.anticheat.event.BlockListener;
import net.h31ix.anticheat.event.EntityListener;
import net.h31ix.anticheat.event.PlayerListener;
import net.h31ix.anticheat.manage.BowManager;
import net.h31ix.anticheat.manage.ItemManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Anticheat extends JavaPlugin {
    public ChatManager cm = new ChatManager(this);
    public AnimationManager am = new AnimationManager(this);
    public ExemptManager ex = new ExemptManager(this);
    public ItemManager im = new ItemManager(this);
    public BowManager bm = new BowManager(this);
    public PlayerTracker tracker = new PlayerTracker();
    private static final Logger l = Logger.getLogger("Minecraft");
    private long lastTime = System.currentTimeMillis();
    private long time = 0;
    public boolean lagged = false;
    
    public boolean log = false;
    
    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        
        getCommand("anticheat").setExecutor(new CommandManager(this));
        
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() 
        {
            @Override
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
        if(this.log)
        {
            l.log(Level.WARNING,"[AntiCheat] "+s);
        }
    }
}


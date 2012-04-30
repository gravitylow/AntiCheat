package net.h31ix.anticheat;

import java.io.File;
import java.util.List;
import net.h31ix.anticheat.manage.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.h31ix.anticheat.command.CommandManager;
import net.h31ix.anticheat.event.BlockListener;
import net.h31ix.anticheat.event.EntityListener;
import net.h31ix.anticheat.event.PlayerListener;
import net.h31ix.anticheat.manage.BowManager;
import net.h31ix.anticheat.manage.ItemManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Anticheat extends JavaPlugin {
    //Define and initialize managers, used for various checks and tasks in the listeners.
    public ChatManager cm = new ChatManager(this);
    public AnimationManager am = new AnimationManager(this);
    public ExemptManager ex = new ExemptManager(this);
    public ItemManager im = new ItemManager(this);
    public BowManager bm = new BowManager(this);
    public HealthManager hm = new HealthManager(this);
    public LoginManager lm = new LoginManager(this);
    public FoodManager fom = new FoodManager(this);
    public TeleportManager tp = new TeleportManager(this);
    public FlyManager fm = new FlyManager();
    //End Managers
    
    public Configuration config; 
    public PlayerTracker tracker; //PlayerTracker for monitoring levels
    private static final Logger l = Logger.getLogger("Minecraft");
    private long lastTime = System.currentTimeMillis();
    private long time = 0;
    public boolean lagged = false; 
    public boolean logConsole;
    public FileConfiguration log;
    private List<String> worlds;
    
    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        if(!new File(this.getDataFolder()+"\\config.yml").exists())
        {
            this.saveDefaultConfig();
        }     
        config = new Configuration(this);
        tracker = new PlayerTracker(this);
        logConsole = config.logConsole;
        worlds = config.getWorlds();
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
        l.log(Level.INFO,"--[Thanks for using a DEV build of AntiCheat!]--");
        l.log(Level.INFO,"If you notice any bugs, PLEASE report them on BukkitDev!");
    }
    
    public void log(String s)
    {
        if(this.logConsole)
        {
            l.log(Level.WARNING,"[AntiCheat] "+s);
        }
    }
    
    public boolean check(Player player)
    {
        boolean check = false;
        String pworld = player.getWorld().getName();
        for(String world : worlds)
        {
            if(world.equals(pworld))
            {
                check = true;
                break;
            }
        }
        return check;
    }
    
    //Avoid catching our own teleportations
    public void logTeleport()
    {
        
    }
}


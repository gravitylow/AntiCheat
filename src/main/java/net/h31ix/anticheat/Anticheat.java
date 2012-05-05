package net.h31ix.anticheat;

import java.io.File;
import java.util.List;
import net.h31ix.anticheat.manage.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.h31ix.anticheat.command.CommandManager;
import net.h31ix.anticheat.event.*;
import net.h31ix.anticheat.xray.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Anticheat extends JavaPlugin {
    //Define and initialize managers, used for various checks and tasks in the listeners.
    public ChatManager cm = new ChatManager(this);
    public AnimationManager am = new AnimationManager();
    public ExemptManager ex = new ExemptManager(this);
    public ItemManager im = new ItemManager(this);
    public BowManager bm = new BowManager(this);
    public HealthManager hm = new HealthManager(this);
    public LoginManager lm = new LoginManager(this);
    public FoodManager fom = new FoodManager(this);
    public FlyManager fm = new FlyManager();
    public BlockManager blm = new BlockManager(this);
    //End Managers
    
    private Configuration config; 
    private PlayerTracker tracker; //PlayerTracker for monitoring levels
    private XRayTracker xtracker; //XRay tracker for monitoring ores
    private static final Logger logger = Logger.getLogger("Minecraft");
    private List<String> worlds;
    
    public Configuration getConfiguration()
    {
        return config;
    }
    
    public PlayerTracker getPlayerTracker()
    {
        return tracker;
    }
    
    public XRayTracker getXRayTracker()
    {
        return xtracker;
    }
    
    @Override
    public void onDisable() 
    {
        
    }
    
    @Override
    public void onEnable() {
        if(!new File(this.getDataFolder()+"\\config.yml").exists())
        {
            this.saveDefaultConfig();
        }     
        config = new Configuration();
        tracker = new PlayerTracker(this);
        xtracker = new XRayTracker();
        worlds = config.getWorlds();
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        getServer().getPluginManager().registerEvents(new XRayListener(this), this);
        
        getCommand("anticheat").setExecutor(new CommandManager(this));
        
        logger.log(Level.INFO,"--[Thanks for using a DEV build of AntiCheat!]--");
        logger.log(Level.INFO,"If you notice any bugs, PLEASE report them on BukkitDev!");
    }
    
    public void log(String s)
    {
        if(config.logConsole())
        {
            logger.log(Level.WARNING, (new StringBuilder("[AntiCheat]")).append(s).toString());
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
}


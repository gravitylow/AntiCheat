package net.h31ix.anticheat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration {
    private File configFile = new File("plugins/AntiCheat/config.yml");
    private FileConfiguration config;
    private boolean logConsole;
    private boolean logXRay;
    private List<String> worlds = new ArrayList<String>();
    
    public Configuration()
    {
        load();
    }
    
    private void save()
    {
        try {
            config.save(configFile);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean logConsole()
    {
        return logConsole;
    }
    
    public boolean logXRay()
    {
        return logXRay;
    }
    
    public final void load()
    {
        config = YamlConfiguration.loadConfiguration(configFile);
        logConsole = config.getBoolean("Logging.Log to console");
        if(config.getList("Enable in") == null)
        {
            List<String> w = new ArrayList<String>();
            for(World world : Bukkit.getServer().getWorlds())
            {
                w.add(world.getName());
            }
            config.set("Enable in", w);
            save();
        }
        worlds = (List<String>)(config.getList("Enable in")); 
        if(config.getString("XRay.Log xray stats") == null)
        {
            config.set("XRay.Log xray stats", true);
            save();
        }   
        logXRay = config.getBoolean("XRay.Log xray stats");    
    }
    
    public String getResult(String event)
    {
        return config.getString("Events.Level "+event);
    }
    
    public void setLog(boolean b)
    {
        config.set("Logging.Log to console", b);
        this.logConsole = b;
        try {
            config.save(configFile);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<String> getWorlds()
    {
        return worlds;
    }
}

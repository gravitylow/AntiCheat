package net.h31ix.anticheat;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration {
    private File configFile = new File("plugins/AntiCheat/config.yml");
    private FileConfiguration config;
    private Anticheat plugin;
    public boolean logConsole;
    
    public Configuration(Anticheat plugin)
    {
        this.plugin = plugin;
        config = YamlConfiguration.loadConfiguration(configFile);
        logConsole = config.getBoolean("Logging.Log to console");
    }
    
    public String getResult(String event)
    {
        return config.getString("Events.Level "+event);
    }
    
    public void setLog(boolean b)
    {
        config.set("Logging.Log to console", b);
        this.logConsole = b;
        plugin.logConsole = b;
        try {
            config.save(configFile);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 H31IX http://h31ix.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.h31ix.anticheat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.h31ix.anticheat.manage.AnticheatManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration
{
    private AnticheatManager micromanage = null; //like a boss!
    private File configFile = null;
    private File levelFile = null;
    private File bukkitFile = null;
    private File langFile = null;
    private FileConfiguration config;
    private FileConfiguration level;
    private FileConfiguration lang;
    private boolean logConsole;
    private boolean logXRay;
    private boolean alertXRay;
    private boolean autoUpdate;
    private boolean verboseStartup;
    private boolean chatSpam;
    private boolean commandSpam;
    private int fileLogLevel = 0;
    private String updateFolder;
    private static Language language;
    private List<String> worlds = new ArrayList<String>();

    public Configuration(AnticheatManager instance)
    {
        micromanage = instance;
        configFile = new File(micromanage.getPlugin().getDataFolder() + "/config.yml");
        levelFile = new File(micromanage.getPlugin().getDataFolder() + "/data/level.yml");
        bukkitFile = new File("bukkit.yml");
        langFile = new File(micromanage.getPlugin().getDataFolder() + "/lang.yml");
        load();
    }

    private void save()
    {
        try
        {
            config.save(configFile);
        }
        catch (IOException ex)
        {
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

    public boolean alertXRay()
    {
        return alertXRay;
    }

    public boolean autoUpdate()
    {
        return autoUpdate;
    }

    public boolean verboseStartup()
    {
        return verboseStartup;
    }

    public boolean chatSpam()
    {
        return chatSpam;
    }

    public boolean commandSpam()
    {
        return commandSpam;
    }

    public String updateFolder()
    {
        return updateFolder;
    }

    public int getFileLogLevel()
    {
        return fileLogLevel;
    }

    public Language getLang()
    {
        return language;
    }

    public final void load()
    {
        FileConfiguration bukkit;
        micromanage.getPlugin().checkConfig();
        config = YamlConfiguration.loadConfiguration(configFile);
        level = YamlConfiguration.loadConfiguration(levelFile);
        bukkit = YamlConfiguration.loadConfiguration(bukkitFile);
        lang = YamlConfiguration.loadConfiguration(langFile);
        language = new Language(lang);
        updateFolder = bukkit.getString("settings.update-folder");
        if (config.getString("Logging.Log to console") != null)
        {
            boolean b = config.getBoolean("Logging.Log to console");
            config.set("Logging", null);
            config.set("System.Log to console", b);
            save();
        }
        logConsole = config.getBoolean("System.Log to console");
        if (config.getList("Enable in") == null)
        {
            List<String> w = new ArrayList<String>();
            for (World world : Bukkit.getServer().getWorlds())
            {
                w.add(world.getName());
            }
            config.set("Enable in", w);
            save();
        }
        worlds = config.getStringList("Enable in");
        if (config.getString("XRay.Log xray stats") == null)
        {
            config.set("XRay.Log xray stats", true);
            save();
        }
        logXRay = config.getBoolean("XRay.Log xray stats");
        if (config.getString("System.Auto update") == null)
        {
            config.set("System.Auto update", true);
            save();
        }
        autoUpdate = config.getBoolean("System.Auto update");
        if (config.getString("System.Verbose startup") == null)
        {
            config.set("System.Verbose startup", false);
            save();
        }
        verboseStartup = config.getBoolean("System.Verbose startup");
        if (config.getString("XRay.Alert when xray is found") == null)
        {
            config.set("XRay.Alert when xray is found", false);
            save();
        }
        alertXRay = config.getBoolean("XRay.Alert when xray is found");
        if (config.getString("System.File log level") == null)
        {
            config.set("System.File log level", 1);
            save();
        }
        fileLogLevel = config.getInt("System.File log level");
        if (config.getString("System.Block chat spam") == null)
        {
            config.set("System.Block chat spam", true);
            save();
        }
        chatSpam = config.getBoolean("System.Block chat spam");
        if (config.getString("System.Block command spam") == null)
        {
            config.set("System.Block command spam", true);
            save();
        }
        commandSpam = config.getBoolean("System.Block command spam");
    }

    public String getResult(String event)
    {
        return config.getString("Events.Level " + event);
    }

    public void setLog(boolean b)
    {
        config.set("System.Log to console", b);
        this.logConsole = b;
        try
        {
            config.save(configFile);
        }
        catch (IOException ex)
        {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> getWorlds()
    {
        return worlds;
    }

    public File getConfigFile()
    {
        return configFile;
    }

    public File getLevelFile()
    {
        return levelFile;
    }

    public boolean checkInWorld(World world)
    {
        return worlds.contains(world.getName());
    }

    public int getLevel(String player)
    {
        int x = 0;
        if (level.getString(player) != null)
        {
            x = level.getInt(player);
        }
        return x;
    }

    public void saveLevel(String player, int x)
    {
        level.set(player, x);
    }

    public void saveLevels()
    {
        try
        {
            level.save(levelFile);
        }
        catch (IOException ex)
        {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

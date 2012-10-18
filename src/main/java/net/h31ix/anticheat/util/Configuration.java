/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 AntiCheat Team | http://gravitydevelopment.net
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

package net.h31ix.anticheat.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.h31ix.anticheat.manage.AnticheatManager;
import net.h31ix.anticheat.util.yaml.CommentedConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class Configuration
{
    private AnticheatManager micromanage = null; //like a boss!
    private File configFile = null;
    private File levelFile = null;
    private File langFile = null;
    private File magicFile = null;
    private CommentedConfiguration config;
    private CommentedConfiguration level;
    private CommentedConfiguration lang;
    private CommentedConfiguration magic;
    private boolean logConsole;
    private boolean logXRay;
    private boolean alertXRay;
    private boolean autoUpdate;
    private boolean verboseStartup;
    private boolean chatSpam;
    private boolean commandSpam;
    private boolean silentMode;
    private boolean opExempt;
    private boolean trackCreativeXRay;
    private int fileLogLevel = 0;
    private int medThreshold = 0;
    private int highThreshold = 0;
    private String updateFolder;
    private String eventMed;
    private String eventHigh;
    private String chatActionKick;
    private String chatActionBan;
    private static Language language;
    private List<String> exemptWorlds = new ArrayList<String>();

    public Configuration(AnticheatManager instance)
    {
        micromanage = instance;
        configFile = new File(micromanage.getPlugin().getDataFolder() + "/config.yml");
        levelFile = new File(micromanage.getPlugin().getDataFolder() + "/data/level.yml");
        langFile = new File(micromanage.getPlugin().getDataFolder() + "/lang.yml");
        magicFile = new File(micromanage.getPlugin().getDataFolder() + "/magic.yml");
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

    public boolean silentMode()
    {
        return silentMode;
    }

    public boolean opExempt()
    {
        return opExempt;
    }

    public boolean trackCreativeXRay()
    {
        return trackCreativeXRay;
    }

    public int medThreshold()
    {
        return medThreshold;
    }

    public int highThreshold()
    {
        return highThreshold;
    }

    public String mediumEvent()
    {
        return eventMed;
    }

    public String highEvent()
    {
        return eventHigh;
    }

    public String chatActionKick()
    {
        return chatActionKick;
    }

    public String chatActionBan()
    {
        return chatActionBan;
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
        micromanage.getPlugin().checkConfigs();
        config = CommentedConfiguration.loadConfig(configFile);
        level = CommentedConfiguration.loadConfig(levelFile);
        lang = CommentedConfiguration.loadConfig(langFile);
        magic = CommentedConfiguration.loadConfig(magicFile);
        language = new Language(lang);
        updateFolder = Bukkit.getUpdateFolder();

        // Begin pulling values from config.
        logConsole = getBoolean("System.Log to console", false);
        logXRay = getBoolean ("XRay.Log xray stats", true);
        autoUpdate = getBoolean ("System.Auto update", true);
        verboseStartup = getBoolean ("System.Verbose startup", false);
        alertXRay = getBoolean("XRay.Alert when xray is found", false);
        fileLogLevel = getInt("System.File log level", 1);
        silentMode = getBoolean("System.Silent mode", false);
        medThreshold = getInt("Events.Medium threshold", 20);
        highThreshold = getInt("Events.High threshold", 50);
        opExempt = getBoolean("System.Exempt op", false);
        trackCreativeXRay = getBoolean("XRay.Track creative", true);
        eventMed = getString("Events.Level Medium", "WARN");
        eventMed = getString("Events.Level High", "KICK");
        chatActionKick = getString("Chat.Kick Action", "KICK");
        chatActionBan = getString("Chat.Ban Action", "BAN");
        chatSpam = getBoolean("Chat.Block chat spam", true);
        commandSpam = getBoolean("Chat.Block command spam", false);
        // Cleanup old values
        if(config.getString("System.Block command spam") != null)
        {
            config.set("System.Block command spam", null);
        }
        if(config.getString("System.Block chat spam") != null)
        {
            config.set("System.Block chat spam", null);
        }

        // Worlds
        List<String> list = new ArrayList<String>();
        list.add("example-world");
        list.add("example-world-2");

        if (config.getList("Enable in") != null)
        {
            List<String> list2 = config.getStringList("Enable in");
            List<String> list3 = new ArrayList<String>();
            for(World world : micromanage.getPlugin().getServer().getWorlds())
            {
                if(!list2.contains(world.getName()))
                {
                    list3.add(world.getName());
                }
            }
            if(!list3.isEmpty())
            {
                list = list3;
            }
            config.set("Enable in", null);
        }
        if (config.getList("Disable in") == null)
        {
            config.set("Disable in", list);
        }

        exemptWorlds = config.getStringList("Disable in");
        // End pulling values from config
        save();
    }

    private boolean getBoolean(String entry, boolean d)
    {
        if (config.getString(entry) == null)
        {
            config.set(entry, d);
            return d;
        }
        else
        {
            return config.getBoolean(entry);
        }
    }

    private int getInt(String entry, int d)
    {
        if (config.getString(entry) == null)
        {
            config.set(entry, d);
            return d;
        }
        else
        {
            return config.getInt(entry);
        }
    }

    private String getString(String entry, String d)
    {
        if (config.getString(entry) == null)
        {
            config.set(entry, d);
            return d;
        }
        else
        {
            return config.getString(entry);
        }
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

    public List<String> getWorldsExempt()
    {
        return exemptWorlds;
    }

    public File getConfigFile()
    {
        return configFile;
    }

    public File getLevelFile()
    {
        return levelFile;
    }

    public CommentedConfiguration getMagic()
    {
        return magic;
    }

    public boolean checkInWorld(World world)
    {
        return !exemptWorlds.contains(world.getName());
    }

    public int getLevel(String player)
    {
        int x = -1;
        if (level.getString(player) != null)
        {
            x = level.getInt(player);
        }
        return x;
    }

    public void saveLevel(String user, int x)
    {
        level.set(user, x);
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

    public void saveMagic(CommentedConfiguration newMagic)
    {
        try
        {
            newMagic.save(magicFile);
        }
        catch (IOException ex)
        {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012-2013 AntiCheat Team | http://gravitydevelopment.net
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
import java.util.logging.Logger;
import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.manage.AnticheatManager;
import net.h31ix.anticheat.util.yaml.CommentedConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration {
    private AnticheatManager micromanage = null;
    private File configFile = null;
    private File levelFile = null;
    private File langFile = null;
    private File magicFile = null;
    private File eventsFile = null;
    private CommentedConfiguration config;
    private FileConfiguration level;
    private FileConfiguration magic;
    private FileConfiguration events;
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
    private boolean eventChains;
    private int fileLogLevel = 0;
    private String updateFolder;
    private String chatActionKick;
    private String chatActionBan;
    private static Language language;
    private List<String> exemptWorlds = new ArrayList<String>();
    private List<Level> levels = new ArrayList<Level>();
    private Magic magicInstance;
    private int highestLevel;
    
    public Configuration(AnticheatManager instance) {
        micromanage = instance;
        configFile = new File(micromanage.getPlugin().getDataFolder() + "/config.yml");
        levelFile = new File(micromanage.getPlugin().getDataFolder() + "/data/level.yml");
        langFile = new File(micromanage.getPlugin().getDataFolder() + "/lang.yml");
        magicFile = new File(micromanage.getPlugin().getDataFolder() + "/magic.yml");
        eventsFile = new File(micromanage.getPlugin().getDataFolder() + "/events.yml");
    }
    
    private void save() {
        try {
            config.save(configFile);
            events.save(eventsFile);
            level.save(levelFile);
            magic.save(magicFile);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).severe(ex.getMessage());
        }
    }
    
    public boolean logConsole() {
        return logConsole;
    }
    
    public boolean logXRay() {
        return logXRay;
    }
    
    public boolean alertXRay() {
        return alertXRay;
    }
    
    public boolean autoUpdate() {
        return autoUpdate;
    }
    
    public boolean verboseStartup() {
        return verboseStartup;
    }
    
    public boolean chatSpam() {
        return chatSpam;
    }
    
    public boolean commandSpam() {
        return commandSpam;
    }
    
    public String updateFolder() {
        return updateFolder;
    }
    
    public boolean silentMode() {
        return silentMode;
    }
    
    public boolean opExempt() {
        return opExempt;
    }
    
    public boolean trackCreativeXRay() {
        return trackCreativeXRay;
    }
    
    public boolean eventChains() {
        return eventChains;
    }
    
    public String chatActionKick() {
        return chatActionKick;
    }
    
    public String chatActionBan() {
        return chatActionBan;
    }
    
    public int getFileLogLevel() {
        return fileLogLevel;
    }
    
    public Language getLang() {
        return language;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public int getHighestLevel() {
        return highestLevel = -99;
    }

    public void updateEvents() {
        // Update the old event system to events.yml
        CommentedConfiguration config = CommentedConfiguration.loadConfig(configFile);
        events = YamlConfiguration.loadConfiguration(Anticheat.getPlugin().getResource("events.yml"));
        String medium = config.getString("Events.Level Medium");
        String high = config.getString("Events.Level High");
        int medValue = config.getString("Events.Medium threshold") != null ? config.getInt("Events.Medium threshold") : -1;
        int highValue = config.getString("Events.High threshold") != null ? config.getInt("Events.High threshold") : -1;

        config.set("Events", null);
        Level mediumLevel = new Level("Medium", medValue != -1 ? medValue : 20, ChatColor.YELLOW);
        Level highLevel = new Level("High", highValue != -1 ? highValue : 50, ChatColor.RED);
        Level customLevel = new Level("Custom", -1, ChatColor.GREEN);
        mediumLevel.addAction(medium != null ? medium : "WARN");
        highLevel.addAction(high != null ? high : "KICK");
        customLevel.addAction("COMMAND[ban &player;say hello world]");

        levels.add(mediumLevel);
        levels.add(highLevel);
        levels.add(customLevel);

        writeEvents();
    }

    private void writeEvents() {
        List<String> list = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        for(Level level : levels) {
            list.add(level.toString());
            for(String string : level.getActions()) {
                list2.add(level.getName()+": "+string);
            }
        }
        events.set("levels", list);
        events.set("actions", list2);
        save();
    }

    private void readEvents() {
        levels = new ArrayList<Level>();
        for(String string : events.getStringList("levels")) {
            Level level = Level.load(string);
            levels.add(level);
            highestLevel = level.getValue() > highestLevel ? level.getValue() : highestLevel;
        }
        for(String string : events.getStringList("actions")) {
            String name;
            String action;
            try {
                name = string.split(": ")[0];
                action = string.split(": ")[1];
            } catch (Exception ex) {
                Anticheat.getPlugin().getLogger().warning("Couldn't load action '"+string+"' from config. Improper formatting used.");
                break;
            }
            for(Level level : levels) {
                if(level.getName().equalsIgnoreCase(name)) {
                    level.addAction(action);
                }
            }
        }
    }
    
    public final void load() {
        boolean secondLoad = config != null;
        config = CommentedConfiguration.loadConfig(configFile);
        level = YamlConfiguration.loadConfiguration(levelFile);
        magic = YamlConfiguration.loadConfiguration(magicFile);
        events = YamlConfiguration.loadConfiguration(eventsFile);
        language = new Language(YamlConfiguration.loadConfiguration(langFile), langFile);
        updateFolder = Bukkit.getUpdateFolder();
        
        // Begin pulling values from config.
        logConsole = getBoolean("System.Log to console", false);
        logXRay = getBoolean("XRay.Log xray stats", true);
        autoUpdate = getBoolean("System.Auto update", true);
        verboseStartup = getBoolean("System.Verbose startup", false);
        alertXRay = getBoolean("XRay.Alert when xray is found", false);
        fileLogLevel = getInt("System.File log level", 1);
        silentMode = getBoolean("System.Silent mode", false);
        opExempt = getBoolean("System.Exempt op", false);
        trackCreativeXRay = getBoolean("XRay.Track creative", true);
        eventChains = getBoolean("System.Event Chains", true);
        chatActionKick = getString("Chat.Kick Action", "KICK");
        chatActionBan = getString("Chat.Ban Action", "BAN");
        chatSpam = getBoolean("Chat.Block chat spam", true);
        commandSpam = getBoolean("Chat.Block command spam", false);
        // Cleanup old values
        if (config.getString("System.Block command spam") != null) {
            config.set("System.Block command spam", null);
        }
        if (config.getString("System.Block chat spam") != null) {
            config.set("System.Block chat spam", null);
        }
        
        // Worlds
        List<String> list = new ArrayList<String>();
        list.add("example-world");
        list.add("example-world-2");
        
        if (config.getList("Enable in") != null) {
            List<String> list2 = config.getStringList("Enable in");
            List<String> list3 = new ArrayList<String>();
            for (World world : micromanage.getPlugin().getServer().getWorlds()) {
                if (!list2.contains(world.getName())) {
                    list3.add(world.getName());
                }
            }
            if (!list3.isEmpty()) {
                list = list3;
            }
            config.set("Enable in", null);
        }
        if (config.getList("Disable in") == null) {
            config.set("Disable in", list);
        }
        
        exemptWorlds = config.getStringList("Disable in");

        if(secondLoad) {
            readEvents();
        }
        // End pulling values from config
        save();
        magicInstance = new Magic(getMagic(), this, CommentedConfiguration.loadConfiguration(micromanage.getPlugin().getResource("magic.yml")));
        if(micromanage.getBackend() != null) { // If this is first run, backend may not be setup yet
            micromanage.getBackend().updateMagic(magicInstance);
        }
    }
    
    private boolean getBoolean(String entry, boolean d) {
        if (config.getString(entry) == null) {
            config.set(entry, d);
            return d;
        } else {
            return config.getBoolean(entry);
        }
    }
    
    private int getInt(String entry, int d) {
        if (config.getString(entry) == null) {
            config.set(entry, d);
            return d;
        } else {
            return config.getInt(entry);
        }
    }
    
    private String getString(String entry, String d) {
        if (config.getString(entry) == null) {
            config.set(entry, d);
            return d;
        } else {
            return config.getString(entry);
        }
    }
    
    public void setLog(boolean b) {
        config.set("System.Log to console", b);
        this.logConsole = b;
        try {
            config.save(configFile);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).severe(ex.toString());
        }
    }
    
    public FileConfiguration getMagic() {
        return magic;
    }
    
    public boolean checkInWorld(World world) {
        return !exemptWorlds.contains(world.getName());
    }
    
    public int getLevel(String player) {
        int x = -1;
        if (level.getString(player) != null) {
            x = level.getInt(player);
        }
        return x;
    }
    
    public void saveLevel(String user, int x) {
        level.set(user, x);
    }
    
    public void saveLevels() {
        try {
            level.save(levelFile);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).severe(ex.toString());
        }
    }
    
    public void saveMagic(FileConfiguration newMagic) {
        try {
            newMagic.save(magicFile);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).severe(ex.toString());
        }
    }

    public Magic getMagicInstance() {
        return magicInstance;
    }
}

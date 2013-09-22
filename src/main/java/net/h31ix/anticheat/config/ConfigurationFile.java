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

package net.h31ix.anticheat.config;

import net.h31ix.anticheat.AntiCheat;
import net.h31ix.anticheat.config.yaml.CommentedConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigurationFile {

    private File rawFile;
    private String fileName;

    private CommentedConfiguration configFile;
    private FileConfiguration defaultConfigFile;
    private Configuration config;

    private AntiCheat plugin;

    private boolean saveDefault;

    protected boolean needsReload;

    public ConfigurationFile(AntiCheat plugin, Configuration config, String fileName) {
        this(plugin, config, fileName, true);
    }

    public ConfigurationFile(AntiCheat plugin, Configuration config, String fileName, boolean saveDefault) {
        this(plugin, config, fileName, saveDefault, new File(plugin.getDataFolder(), fileName));
    }

    public ConfigurationFile(AntiCheat plugin, Configuration config, String fileName, boolean saveDefault, File rawFile) {
        // Can't change with user editing
        this.plugin = plugin;
        this.config = config;
        this.fileName = fileName;
        this.rawFile = rawFile;
        this.saveDefault = saveDefault;

        if(saveDefault) {
            defaultConfigFile = YamlConfiguration.loadConfiguration(plugin.getResource(fileName));
        }
        load();
    }

    public void load() {
        // Can change with user editing
        if(!rawFile.exists()) {
            if(saveDefault) {
                plugin.saveResource(fileName, true);
            } else {
                if(!rawFile.getParentFile().exists()) {
                    rawFile.getParentFile().mkdir();
                }
                try {
                    rawFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        configFile = CommentedConfiguration.loadConfiguration(rawFile);

        open();
    }

    public void open() {
        // Nothing to do
    }

    public void close() {
        // Nothing to do
    }

    public void save() {
        close();
        try {
            configFile.save(rawFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        save();
        load();
    }

    public File getRawFile() {
        return rawFile;
    }

    public CommentedConfiguration getConfigFile() {
        return configFile;
    }

    public FileConfiguration getDefaultConfigFile() {
        return defaultConfigFile;
    }

    public Configuration getConfiguration() {
        return config;
    }

    public void setNeedsReload(boolean reload) {
        this.needsReload = reload;
    }

    public boolean needsReload() {
        return needsReload;
    }

    public class ConfigValue<T> {

        private String path;

        private Object value = null;

        public ConfigValue(String path) {
            this(path, true);
        }

        public ConfigValue(String path, boolean loadDefault) {
            this.path = path;

            if(getConfigFile().contains(path)) {
                value = getConfigFile().get(path);
            } else if(loadDefault) {
                getConfigFile().set(path, getDefaultConfigFile().get(path));
                value = getDefaultConfigFile().get(path);
            }
        }

        public String getPath() {
            return path;
        }

        public T getValue() {
            return (T) value;
        }

        public T setValue(T value) {
            getConfigFile().set(path, value);
            return value;
        }

        public T getDefaultValue() {
            return (T) getDefaultConfigFile().get(path);
        }

        public boolean hasValue() {
            return value != null;
        }
    }
}

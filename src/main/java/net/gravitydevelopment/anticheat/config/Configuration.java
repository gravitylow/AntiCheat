/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012-2014 AntiCheat Team | http://gravitydevelopment.net
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

package net.gravitydevelopment.anticheat.config;

import net.gravitydevelopment.anticheat.AntiCheat;
import net.gravitydevelopment.anticheat.config.files.Config;
import net.gravitydevelopment.anticheat.config.files.Enterprise;
import net.gravitydevelopment.anticheat.config.holders.mysql.*;
import net.gravitydevelopment.anticheat.config.holders.yaml.*;
import net.gravitydevelopment.anticheat.config.providers.*;
import net.gravitydevelopment.anticheat.manage.AntiCheatManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

public class Configuration {

    private AntiCheatManager manager;
    private Config config;
    private Enterprise enterprise;

    private Lang lang;
    private Magic magic;
    private Groups groups;
    private Levels levels;
    private Rules rules;

    private ArrayList<ConfigurationFile> flatfiles;
    private ArrayList<ConfigurationTable> dbfiles;

    public Configuration(AntiCheat plugin, AntiCheatManager manager) {
        removeOldFiles();
        this.manager = manager;
        config = new Config(plugin, this);
        plugin.setVerbose(config.verboseStartup.getValue());
        // Now load others
        enterprise = new Enterprise(plugin, this);

        flatfiles = new ArrayList<ConfigurationFile>() {{
            add(config);
            add(enterprise);
        }};

        dbfiles = new ArrayList<ConfigurationTable>();

        // The following values can be configuration from a database, or from flatfile.
        if (config.enterprise.getValue() && enterprise.configGroups.getValue()) {
            groups = new MySQLGroupsHolder(this);
            dbfiles.add((MySQLGroupsHolder) groups);
        } else {
            groups = new YamlGroupsHolder(plugin, this);
            flatfiles.add((YamlGroupsHolder) groups);
        }

        if (config.enterprise.getValue() && enterprise.configRules.getValue()) {
            rules = new MySQLRulesHolder(this);
            dbfiles.add((MySQLRulesHolder) rules);
        } else {
            rules = new YamlRulesHolder(plugin, this);
            flatfiles.add((YamlRulesHolder) rules);
        }

        InvocationHandler handler;
        if (config.enterprise.getValue() && enterprise.configMagic.getValue()) {
            handler = new MySQLMagicHolder(this);
            magic = (Magic) Proxy.newProxyInstance(Magic.class.getClassLoader(),
                    new Class[] { Magic.class },
                    handler);
            dbfiles.add((MySQLMagicHolder) handler);
        } else {
            handler = new YamlMagicHolder(plugin, this);
            magic = (Magic) Proxy.newProxyInstance(Magic.class.getClassLoader(),
                    new Class[] { Magic.class },
                    handler);
            flatfiles.add((YamlMagicHolder) handler);
        }

        if (config.enterprise.getValue() && enterprise.configLang.getValue()) {
            handler = new MySQLLangHolder(this);
            lang = (Lang) Proxy.newProxyInstance(Lang.class.getClassLoader(),
                    new Class[] { Lang.class },
                    handler);
            dbfiles.add((MySQLLangHolder) handler);
        } else {
            handler = new YamlLangHolder(plugin, this);
            lang = (Lang) Proxy.newProxyInstance(Lang.class.getClassLoader(),
                    new Class[] { Lang.class },
                    handler);
            flatfiles.add((YamlLangHolder) handler);
        }

        if (config.enterprise.getValue() && enterprise.syncLevels.getValue()) {
            levels = new MySQLLevelsHolder(this);
            dbfiles.add((MySQLLevelsHolder) levels);
        } else {
            levels = new YamlLevelsHolder(plugin, this);
            flatfiles.add((YamlLevelsHolder) levels);
        }
        // End

        for (ConfigurationFile file : flatfiles) {
            file.save();
            checkReload(file);
        }
    }

    public void load() {
        for (ConfigurationFile file : flatfiles) {
            file.load();
            checkReload(file);
        }
        for (ConfigurationTable table : dbfiles) {
            table.load();
        }
        if (manager.getBackend() != null) {
            manager.getBackend().updateConfig(this);
        }
    }

    private void checkReload(ConfigurationFile file) {
        if (file.needsReload()) {
            file.reload();
            file.setNeedsReload(false);
        }
    }

    private void removeOldFiles() {
        ArrayList<String> removed = new ArrayList<String>();
        File configFile = new File(AntiCheat.getPlugin().getDataFolder(), "config.yml");
        if (configFile.exists() && YamlConfiguration.loadConfiguration(configFile).getString("System.Auto update") != null) {
            configFile.renameTo(new File(AntiCheat.getPlugin().getDataFolder(), "config.old"));
            removed.add("config.yml has been renamed to config.old and replaced with the new config.yml");
        }
        File eventsFile = new File(AntiCheat.getPlugin().getDataFolder(), "events.yml");
        if (eventsFile.exists()) {
            eventsFile.renameTo(new File(AntiCheat.getPlugin().getDataFolder(), "events.old"));
            removed.add("events.yml has been renamed to events.old and replaced with groups.yml and rules.yml");
        }
        if (removed.size() > 0) {
            AntiCheat.getPlugin().getLogger().info("You are upgrading from an old version of AntiCheat. Due to configuration changes, the following files have been modified:");
            for (String s : removed) {
                AntiCheat.getPlugin().getLogger().info(s);
            }
        }
    }

    public Config getConfig() {
        return config;
    }

    public Groups getGroups() {
        return groups;
    }

    public Rules getRules() {
        return rules;
    }

    public Lang getLang() {
        return lang;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public Levels getLevels() {
        return levels;
    }

    public Magic getMagic() {
        return magic;
    }
}

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
import net.gravitydevelopment.anticheat.config.files.Lang;
import net.gravitydevelopment.anticheat.config.files.Magic;
import net.gravitydevelopment.anticheat.config.holders.mysql.MySQLGroupsHolder;
import net.gravitydevelopment.anticheat.config.holders.mysql.MySQLLevelsHolder;
import net.gravitydevelopment.anticheat.config.holders.mysql.MySQLRulesHolder;
import net.gravitydevelopment.anticheat.config.holders.yaml.YamlGroupsHolder;
import net.gravitydevelopment.anticheat.config.holders.yaml.YamlLevelsHolder;
import net.gravitydevelopment.anticheat.config.holders.yaml.YamlRulesHolder;
import net.gravitydevelopment.anticheat.config.providers.Groups;
import net.gravitydevelopment.anticheat.config.providers.Levels;
import net.gravitydevelopment.anticheat.config.providers.Rules;
import net.gravitydevelopment.anticheat.manage.AnticheatManager;

import java.util.ArrayList;

public class Configuration {

    private AnticheatManager manager;
    private Config config;
    private Enterprise enterprise;
    private Lang lang;
    private Magic magic;

    private Groups groups;
    private Levels levels;
    private Rules rules;

    private ArrayList<ConfigurationFile> flatfiles;
    private ArrayList<ConfigurationTable> dbfiles;

    public Configuration(AntiCheat plugin) {
        manager = plugin.getManager();
        config = new Config(plugin, this);
        plugin.setVerbose(config.verboseStartup.getValue());
        // Now load others
        enterprise = new Enterprise(plugin, this);
        lang = new Lang(plugin, this);
        magic = new Magic(plugin, this);

        flatfiles = new ArrayList<ConfigurationFile>() {{
            add(config);
            add(enterprise);
            add(lang);
            add(magic);
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
            manager.getBackend().updateMagic(magic);
        }
    }

    private void checkReload(ConfigurationFile file) {
        if (file.needsReload()) {
            file.reload();
            file.setNeedsReload(false);
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

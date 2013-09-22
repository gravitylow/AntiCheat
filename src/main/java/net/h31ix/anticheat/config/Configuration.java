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
import net.h31ix.anticheat.config.files.*;
import net.h31ix.anticheat.config.files.Magic;

public class Configuration {

    private final AntiCheat plugin;

    private Config config;
    private Events events;
    private Lang lang;
    private Enterprise enterprise;

    private Levels levels;

    private Magic magic;

    public Configuration(AntiCheat plugin) {
        this.plugin = plugin;

        config = new Config(plugin, this);
        events = new Events(plugin, this);
        lang = new Lang(plugin, this);
        enterprise = new Enterprise(plugin, this);

        levels = new Levels(plugin, this);

        magic = new Magic(plugin, this);

        load();
    }

    public void load() {
        for(ConfigurationFile file : new ConfigurationFile[]{config, events, lang, enterprise, levels, magic}) {
            file.load();
            checkReload(file);
        }
    }

    public void save() {
        config.save();
        events.save();
        lang.save();
        enterprise.save();
        levels.save();
        magic.save();
    }

    private void checkReload(ConfigurationFile file) {
        if(file.needsReload()) {
            file.reload();
            file.setNeedsReload(false);
        }
    }

    public void reload() {
        save();
        load();
    }

    public Config getConfig() {
        return config;
    }

    public Events getEvents() {
        return events;
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

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

package net.gravitydevelopment.anticheat.config.holders.yaml;

import net.gravitydevelopment.anticheat.AntiCheat;
import net.gravitydevelopment.anticheat.config.Configuration;
import net.gravitydevelopment.anticheat.config.ConfigurationFile;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class YamlMagicHolder extends ConfigurationFile implements InvocationHandler {

    public static final String FILENAME = "magic.yml";

    public YamlMagicHolder(AntiCheat plugin, Configuration config) {
        super(plugin, config, FILENAME);
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        String key = method.getName();

        if (method.getReturnType().getSimpleName().equals("int")) {
            return new ConfigValue<Integer>(key).getValue();
        } else if (method.getReturnType().getSimpleName().equals("double")) {
            return new ConfigValue<Double>(key).getValue();
        } else {
            AntiCheat.getPlugin().getLogger().severe("The magic value " + key + " couldn't be found.");
            return -1;
        }
    }
}

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

package net.h31ix.anticheat.util.yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

public class CommentedConfiguration extends YamlConfiguration
{
    private Map<String, String> comments = new HashMap<String, String>();

    private final DumperOptions yamlOptions = new DumperOptions();
    private final Representer yamlRepresenter = new YamlRepresenter();
    private final Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions);
    private String lastLine = null;

    public CommentedConfiguration()
    {
        // Val 1 = line comment should be under
        // Val 2 = comment
        comments.put("XRay:", "  # Should AntiCheat use calculations to try and find xrayers?");
        comments.put("Log xray stats:", "  # Should a warning be sent to admins when a person is found that could be xraying?");
        comments.put("Alert when xray is found:", "  # Should players in creative mode be tracked for possible xray usage?");
        comments.put("Chat:", "  # Should AntiCheat block players spamming messages in chat?");
        comments.put("Block chat spam:", "  # Should AntiCheat block players spamming commands in chat?");
        comments.put("Block command spam:", "  # Valid actions = NONE,KICK,BAN,COMMAND[command]");
        comments.put("Ban Action:", "# Events occur when a player's hack level changes. You may configure reactions to these events here.");
        comments.put("# Events occur when a player's hack level changes. You may configure reactions to these events here.", "# Valid events = NONE,WARN,KICK,BAN,COMMAND[command]");
        comments.put("# Valid events = NONE,WARN,KICK,BAN,COMMAND[command]", "# Use commands like so: COMMAND[ban &player 30] or COMMAND[kick &player hacking] or COMMAND[jail &player 10]");
        comments.put("# Use commands like so: COMMAND[ban &player 30] or COMMAND[kick &player hacking] or COMMAND[jail &player 10]", "# &player will be replaced with player's name, and &world will be replaced with the player's world");
        comments.put("Level High:", "  # How many warnings should the player get before entering medium?");
        comments.put("Medium threshold:", "  # How many warnings should the player get before entering high?");
        comments.put("System:", "  # Turning auto-update off is a _BAD_ idea. You will no longer be protected by the latest hacks/cheats if you do so, and will have to update manually.");
        comments.put("# Turning auto-update off is a _BAD_ idea. You will no longer be protected by the latest hacks/cheats if you do so, and will have to update manually.", "  # Should AntiCheat log ALL failed checks to console?");
        comments.put("Log to console:", "  # Should AntiCheat log to files?");
        comments.put("# Should AntiCheat log to files?", "  # 0 = off, 1 = log only when an event takes place, 2 = more detailed logs, 3 = most detailed logs");
        comments.put("File log level:", "  # Should AntiCheat display extra debug information when starting?");
        comments.put("Verbose startup:", "  # If silent mode is on, players will not be stopped when they try to hack, and AntiCheat will do everything possible to keep them unaware of their rising hack level.");
        comments.put("# If silent mode is on, players will not be stopped when they try to hack, and AntiCheat will do everything possible to keep them unaware of their rising hack level.", "  # However, alerts will still be sent to console and to admins online, and events will still take place.");
        comments.put("Silent mode:", "  # Should ops be exempt from all checks?");
    }

    @Override
    public String saveToString() {
        yamlOptions.setIndent(options().indent());
        yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        String dump = yaml.dump(getValues(false));

        if (dump.equals(BLANK_CONFIG)) {
            dump = "";
        } else {
            StringBuilder builder = new StringBuilder();
            String [] lines = dump.split("\n");
            int i =0;

            while(i < lines.length) {
                if(containsLine(lines[i])) {
                    builder.append(lines[i]);
                    builder.append("\n");
                    builder.append(getComment(lines[i]));
                    builder.append("\n");
                    lastLine = getComment(getComment(lines[i]));
                    i++;
                } else if(containsLine(lastLine)) {
                    builder.append(getComment(lastLine));
                    builder.append("\n");
                    lastLine = getComment(lastLine);
                } else {
                    builder.append(lines[i]);
                    builder.append("\n");
                    i++;
                }
            }
            dump = "# AntiCheat configuration file\n# Please report any bugs: http://dev.bukkit.org/server-mods/anticheat/\n"+builder.toString();
        }

        return dump;
    }

    private boolean containsLine(String line)
    {
        if(line != null)
        {
            for(String string : comments.keySet()) {
                if(string.replaceAll(" ", "").split(":")[0].equalsIgnoreCase(line.replaceAll(" ", "").split(":")[0])) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getComment(String line)
    {
        for(String string : comments.keySet()) {
            if(string.replaceAll(" ", "").split(":")[0].equalsIgnoreCase(line.replaceAll(" ", "").split(":")[0])) {
                return comments.get(string);
            }
        }
        return null;
    }

    public static CommentedConfiguration loadConfig(File file) {

        CommentedConfiguration config = new CommentedConfiguration();
        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file , ex);
        }

        return config;
    }
}

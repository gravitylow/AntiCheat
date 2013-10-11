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

package net.h31ix.anticheat.config.yaml;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * An extension of {@link org.bukkit.configuration.file.YamlConfiguration} which preserves comments.
 * Note that this implementation is not synchronized.
 * <br>
 * Some overridden code has been moved to this file from its parent & modified.
 */
public class CommentedConfiguration extends YamlConfiguration {
    private Map<Integer, String> comments = new HashMap<Integer, String>();

    private final DumperOptions yamlOptions = new DumperOptions();
    private final Representer yamlRepresenter = new YamlRepresenter();
    private final Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions);

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        Validate.notNull(contents, "Contents cannot be null");

        Map<?, ?> input;
        try {
            input = (Map<?, ?>) yaml.load(contents);
        } catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
        } catch (ClassCastException e) {
            throw new InvalidConfigurationException("Top level is not a Map.");
        }

        /** - CommentedConfiguration
         String header = parseHeader(contents);
         if (header.length() > 0) {
         options().header(header);
         }
         **/

        if (input != null) {
            convertMapsToSections(input, this);
        }

        // Begin CommentedConfiguration
        int i = 0;
        int blank = 0;
        String[] lines = contents.split("\n");
        while (i < lines.length) {
            if (lines[i].trim().equalsIgnoreCase("")) {
                i++;
                blank++;
                continue;
            }
            if (lines[i].contains(COMMENT_PREFIX)) {
                comments.put(i - blank, lines[i]);
            }
            i++;
        }
        // End CommentedConfiguration
    }

    @Override
    public String saveToString() {
        yamlOptions.setIndent(options().indent());
        yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        // String header = buildHeader(); - CommentedConfiguration
        String dump = yaml.dump(getValues(false));

        if (dump.equals(BLANK_CONFIG)) {
            dump = "";
        }

        // Begin CommentedConfiguration
        StringBuilder builder = new StringBuilder();

        String[] lines = dump.split("\n");
        ArrayDeque<String> queue = new ArrayDeque<String>();
        for (String string : lines) {
            queue.add(string);
        }

        int i = 0;
        while (queue.size() > 0) {
            if (comments.containsKey(i)) {
                builder.append(comments.get(i));

                // Handle subsequent comments
                int b = i;
                while (true) {
                    b++;
                    if (comments.containsKey(b)) {
                        builder.append('\n');
                        builder.append(comments.get(b));
                    } else {
                        break;
                    }
                }
                builder.append('\n');
                i = b;
            }

            builder.append(queue.getFirst());
            builder.append('\n');
            queue.pop();
            i++;
        }
        // End CommentedConfiguration

        return builder.toString();
    }

    /**
     * Creates a new {@link CommentedConfiguration}, loading from the given file.
     * <p/>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be returned.
     *
     * @param file Input file
     * @return Resulting configuration
     * @throws IllegalArgumentException Thrown if file is null
     */
    public static CommentedConfiguration loadConfiguration(File file) {
        Validate.notNull(file, "File cannot be null");

        CommentedConfiguration config = new CommentedConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        }

        return config;
    }

    /**
     * Creates a new {@link CommentedConfiguration}, loading from the given stream.
     * <p/>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be returned.
     *
     * @param stream Input stream
     * @return Resulting configuration
     * @throws IllegalArgumentException Thrown if stream is null
     */
    public static CommentedConfiguration loadConfiguration(InputStream stream) {
        Validate.notNull(stream, "Stream cannot be null");

        CommentedConfiguration config = new CommentedConfiguration();

        try {
            config.load(stream);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        }

        return config;
    }
}

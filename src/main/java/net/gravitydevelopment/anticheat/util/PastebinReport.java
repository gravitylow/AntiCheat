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

package net.gravitydevelopment.anticheat.util;

import net.gravitydevelopment.anticheat.AntiCheat;
import net.gravitydevelopment.anticheat.config.files.Magic;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PastebinReport {
    private static final String DATE = new SimpleDateFormat("yyyy-MM-dd kk:mm Z").format(new Date());
    private static final String API_KEY = "6eeace09c2742f8463b9db9b0c467605";

    private StringBuilder report = new StringBuilder();
    private String url = "";

    public PastebinReport(CommandSender cs) {
        Player player = null;
        if (cs instanceof Player) {
            player = (Player) cs;
        }
        createReport(player);
        try {
            writeReport();
        } catch (IOException e) {
        }
        postReport();
    }

    public PastebinReport(CommandSender cs, Player tp) {
        createReport(tp);
        try {
            writeReport();
        } catch (IOException e) {
        }
        postReport();
    }

    public String getURL() {
        return url;
    }

    private void appendPermissionsTester(Player player) {
        if (player == null) {
            append("No player defined.");
            return;
        }

        for (Permission node : Permission.values()) {
            report.append(player.getName() + ": " + node.toString() + " " + node.get(player));
            if (node.get(player) && !node.whichPermission(player).equals(node.toString())) {
                report.append(" (Applied by " + node.whichPermission(player) + ")");
            }
            report.append('\n');
        }
    }

    private void createReport(Player player) {
        if (Bukkit.getPluginManager().getPlugin("NoCheatPlus") != null) {
            append("------------ WARNING! ------------");
            append("This report was run with NoCheatPlus enabled. Results may be inaccurate." + '\n');
        }
        append("------------ AntiCheat Report - " + DATE + " ------------");
        appendSystemInfo();
        append("------------Last 30 logs------------");
        appendLogs();
        append("------------Permission Tester------------");
        appendPermissionsTester(player);
        append("------------Event Chains------------");
        appendEventHandlers();
        append("------------Magic Diff------------");
        appendMagicDiff();
        append("-----------End Of Report------------");
    }

    private void appendLogs() {
        List<String> logs = AntiCheat.getManager().getLastLogs();
        if (logs.size() == 0) {
            append("No recent logs.");
            return;
        }
        for (String log : logs) {
            append(log);
        }
    }

    private void appendSystemInfo() {
        Runtime runtime = Runtime.getRuntime();
        append("AntiCheat Version: " + AntiCheat.getVersion() + (AntiCheat.isUpdated() ? "" : " (OUTDATED)"));
        append("Server Version: " + Bukkit.getVersion());
        append("Server Implementation: " + Bukkit.getName());
        append("Server ID: " + Bukkit.getServerId());
        append("Java Version: " + System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
        append("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        append("Free Memory: " + runtime.freeMemory() / 1024 / 1024 + "MB");
        append("Max Memory: " + runtime.maxMemory() / 1024 / 1024 + "MB");
        append("Total Memory: " + runtime.totalMemory() / 1024 / 1024 + "MB");
        append("Online Mode: " + String.valueOf(Bukkit.getOnlineMode()).toUpperCase());
        append("Players: " + Bukkit.getOnlinePlayers().length + "/" + Bukkit.getMaxPlayers());
        append("Plugin Count: " + Bukkit.getPluginManager().getPlugins().length);
        append("Plugin Uptime: " + ((System.currentTimeMillis() - AntiCheat.getPlugin().getLoadTime()) / 1000) / 60 + " minutes");
    }

    private void appendMagicDiff() {
        // This is hacky, and I like it
        Magic magic = AntiCheat.getManager().getConfiguration().getMagic();
        FileConfiguration file = magic.getDefaultConfigFile();
        append("Version: " + magic.getVersion());
        boolean changed = false;
        for (Field field : Magic.class.getFields()) {
            Object defaultValue = file.get(field.getName());
            try {
                Field value = magic.getClass().getDeclaredField(field.getName());
                boolean x = false;
                String s1 = value.get(magic).toString();
                String s2 = defaultValue.toString();
                if (!s1.equals(s2) && !s1.equals(s2 + ".0")) {
                    changed = true;
                    append(field.getName() + ": " + s1 + " (Default: " + s2 + ")");
                } else {
                }
            } catch (NoSuchFieldException ex) {

            } catch (IllegalAccessException ex) {

            }
        }
        if (!changed) {
            append("No changes from default.");
        }
    }

    private void appendEventHandlers() {
        report.append(AntiCheat.getManager().getEventChainReport());
    }

    private void writeReport() throws IOException {
        File f = new File(AntiCheat.getPlugin().getDataFolder() + "/report.txt");
        FileWriter r = new FileWriter(f);
        BufferedWriter writer = new BufferedWriter(r);
        writer.write(report.toString());
        writer.close();
    }

    private void postReport() {
        try {
            URL urls = new URL("http://pastebin.com/api/api_post.php");
            HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
            conn.setInstanceFollowRedirects(false);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();

            out.write(("api_option=paste" + "&api_dev_key=" + URLEncoder.encode(API_KEY, "utf-8") + "&api_paste_code=" + URLEncoder.encode(report.toString(), "utf-8") + "&api_paste_private=" + URLEncoder.encode("1", "utf-8") + "&api_paste_name=" + URLEncoder.encode("", "utf-8") + "&api_paste_expire_date=" + URLEncoder.encode("1M", "utf-8") + "&api_paste_format=" + URLEncoder.encode("text", "utf-8") + "&api_user_key=" + URLEncoder.encode("", "utf-8")).getBytes());
            out.flush();
            out.close();

            if (conn.getResponseCode() == 200) {
                InputStream receive = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(receive));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    response.append("\r\n");
                }
                reader.close();

                String result = response.toString().trim();

                if (!result.contains("http://")) {
                    url = "Failed to post.  Check report.txt";
                } else {
                    url = result.trim();
                }
            } else {
                url = "Failed to post.  Check report.txt";
            }
        } catch (Exception e) {
            url = "Failed to post.  Check report.txt";
        }
    }

    private void append(String s) {
        report.append(s + '\n');
    }
}

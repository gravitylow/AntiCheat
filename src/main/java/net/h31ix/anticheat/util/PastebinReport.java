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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.h31ix.anticheat.Anticheat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PastebinReport
{
    private StringBuilder report = new StringBuilder();
    private String url = "";
    private Date date = new Date();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm Z");

    public PastebinReport(CommandSender cs)
    {
        Player player = null;
        try
        {
            player = (Player) cs;
        }
        catch (Exception e)
        {
            cs.sendMessage(ChatColor.RED + "We were unable to detect a player when making this report.  Skipping permstester");
        }

        createReport(player);
        try
        {
            writeReport();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        postReport();
    }

    public String getURL()
    {
        return url;
    }

    private void appendPermissionsTester(Player player)
    {
        if (player == null)
            return;
        
        // alrighty then noobs.
        for(Permission node : Permission.values())
        {
            report.append(player.getName() + ": " + node.toString() + " " + node.get(player) + '\n');
        }
        
    }

    private void createReport(Player player)
    {
        report.append("------------ AntiCheat Report - " + format.format(date) + " ------------" + '\n');
        report.append("Version: " + Anticheat.getVersion() + '\n');
        report.append("CraftBukkit: " + Bukkit.getVersion() + '\n');
        report.append("Plugin Count: " + Bukkit.getPluginManager().getPlugins().length + '\n');
        appendSystemInfo();
        report.append("------------Last 30 logs------------" + '\n');
        for (String log : Anticheat.getManager().getLastLogs())
        {
            report.append("[AntiCheat] " + log + '\n');
        }
        report.append("------------Permission Tester------------" + '\n');
        appendPermissionsTester(player);
        report.append("------------Advanced Information------------" + '\n');
        Anticheat.getManager().getBackend().buildAdvancedInformation(report);
        report.append("-----------End Of Report------------");
    }

    private void appendSystemInfo()
    {
        Runtime runtime = Runtime.getRuntime();
        report.append("Java: " + System.getProperty("java.vendor") + " " + System.getProperty("java.version") + '\n');
        report.append("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + '\n');
        report.append("Free Memory: " + runtime.freeMemory() / 1024 / 1024 + "MB" + '\n');
        report.append("Max Memory: " + runtime.maxMemory() / 1024 / 1024 + "MB" + '\n');
        report.append("Total Memory: " + runtime.totalMemory() / 1024 / 1024 + "MB" + '\n');
        report.append("Server ID + Name: " + Bukkit.getServerId() + " - " + Bukkit.getName() + '\n');
        report.append("Players: " + Bukkit.getOnlinePlayers().length + "/" + Bukkit.getMaxPlayers() + '\n');
    }

    private void writeReport() throws IOException
    {
        File f = new File(Anticheat.getPlugin().getDataFolder() + "/report.txt");
        FileWriter r = new FileWriter(f);
        BufferedWriter writer = new BufferedWriter(r);
        writer.write(report.toString());
        writer.close();
    }

    private void postReport()
    {
        try
        {
            URL urls = new URL("http://pastebin.com/api/api_post.php");
            HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
            conn.setInstanceFollowRedirects(false);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();

            out.write(("api_option=paste" + "&api_dev_key=" + URLEncoder.encode("c0616def494dcb5b7632304f8c52c0f1", "utf-8") + "&api_paste_code=" + URLEncoder.encode(report.toString(), "utf-8") + "&api_paste_private=" + URLEncoder.encode("0", "utf-8") + "&api_paste_name=" + URLEncoder.encode("", "utf-8") + "&api_paste_expire_date=" + URLEncoder.encode("1D", "utf-8") + "&api_paste_format=" + URLEncoder.encode("text", "utf-8") + "&api_user_key=" + URLEncoder.encode("", "utf-8")).getBytes());
            out.flush();
            out.close();

            if (conn.getResponseCode() == 200)
            {
                InputStream receive = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(receive));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = reader.readLine()) != null)
                {
                    response.append(line);
                    response.append("\r\n");
                }
                reader.close();

                String result = response.toString().trim();

                if (!result.contains("http://"))
                {
                    url = "Failed to post.  Check report.txt";
                }
                else
                {
                    url = result.trim();
                }
            }
            else
            {
                url = "Failed to post.  Check report.txt";
            }
        }
        catch (Exception e)
        {
            url = "Failed to post.  Check report.txt";
        }
    }

}

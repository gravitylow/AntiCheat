/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 AntiCheat Team | http://h31ix.net
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

package net.h31ix.anticheat;

import net.h31ix.anticheat.util.Configuration;
import net.h31ix.anticheat.util.PastebinReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.h31ix.anticheat.manage.CheckType;
import net.h31ix.anticheat.util.Permission;
import net.h31ix.anticheat.manage.PlayerManager;
import net.h31ix.anticheat.util.Utilities;
import net.h31ix.anticheat.xray.XRayTracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor
{
    private Configuration config = Anticheat.getManager().getConfiguration();
    private PlayerManager playerManager = Anticheat.getManager().getPlayerManager();
    private XRayTracker xtracker = Anticheat.getManager().getXRayTracker();
    private static final ChatColor RED = ChatColor.RED;
    private static final ChatColor YELLOW = ChatColor.YELLOW;
    private static final ChatColor GREEN = ChatColor.GREEN;
    private static final ChatColor WHITE = ChatColor.WHITE;
    private static final ChatColor GRAY = ChatColor.GRAY;
    private List<String> high = new ArrayList<String>();
    private List<String> med = new ArrayList<String>();
    private List<String> low = new ArrayList<String>();
    private Map<String, Location> spyLocation = new HashMap<String, Location>();
    private static final Server SERVER = Bukkit.getServer();
    private static final int MED_THRESHOLD = 20;
    private static final int HIGH_THRESHOLD = 50;
    private static final String PERMISSIONS_ERROR = RED + "Insufficient Permissions.";

    public void handleLog(CommandSender cs, String[] args)
    {
        if (Permission.SYSTEM_LOG.get(cs))
        {
            if (args[1].equalsIgnoreCase("enable"))
            {
                if (!config.logConsole())
                {
                    config.setLog(true);
                    cs.sendMessage(GREEN + "Console logging enabled.");
                }
                else
                {
                    cs.sendMessage(GREEN + "Console logging is already enabled!");
                }
            }
            else if (args[1].equalsIgnoreCase("disable"))
            {
                if (config.logConsole())
                {
                    config.setLog(false);
                    cs.sendMessage(GREEN + "Console logging disabled.");
                }
                else
                {
                    cs.sendMessage(GREEN + "Console logging is already disabled!");
                }
            }
            else
            {
                cs.sendMessage(RED + "Usage: /anticheat log [enable/disable]");
            }
        }
        else
        {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }

    public void handleDebug(CommandSender cs)
    {
        if (Permission.SYSTEM_REPORT.get(cs))
        {
            PastebinReport report = new PastebinReport();
            cs.sendMessage(GREEN + "Debug information posted to: " + WHITE + report.getURL());
            cs.sendMessage(GREEN + "Please include this link when making bug reports.");
        }
        else
        {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }

    public void handleXRay(CommandSender cs, String[] args)
    {
        if (Permission.SYSTEM_XRAY.get(cs))
        {
            if (config.logXRay())
            {
                List<Player> list = SERVER.matchPlayer(args[1]);
                if (list.size() == 1)
                {
                    Player player = list.get(0);
                    if (xtracker.sufficientData(player.getName()))
                    {
                        xtracker.sendStats(cs, player.getName());
                    }
                    else
                    {
                        cs.sendMessage(RED + "Insufficient data collected from " + WHITE + args[1] + RED + ".");
                        cs.sendMessage(RED + "Please wait until more info is collected before predictions are calculated.");
                    }
                }
                else if (list.size() > 1)
                {
                    cs.sendMessage(RED + "Multiple players found by name: " + WHITE + args[1] + RED + ".");
                }
                else if (xtracker.sufficientData(args[1]))
                {
                    xtracker.sendStats(cs, args[1]);
                }
                else
                {
                    cs.sendMessage(RED + "Insufficient data collected from " + WHITE + args[1] + RED + ".");
                    cs.sendMessage(RED + "Please wait until more info is collected before predictions are calculated.");
                }
            }
            else
            {
                cs.sendMessage(RED + "XRay logging is off in the config.");
            }
        }
        else
        {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }

    public void handleReset(CommandSender cs, String[] args)
    {
        if (Permission.SYSTEM_RESET.get(cs))
        {
            List<Player> list = SERVER.matchPlayer(args[1]);
            if (list.size() == 1)
            {
                Player player = list.get(0);
                if (playerManager.getLevel(player) == 0)
                {
                    cs.sendMessage(player.getName() + RED + " is already in Low Level!");
                }
                else
                {
                    playerManager.reset(player);
                    cs.sendMessage(player.getName() + GREEN + " has been reset to Low Level.");
                }
                xtracker.reset(player.getName());
                cs.sendMessage(player.getName() + GREEN + "'s XRay stats have been reset.");
            }
            else if (list.size() > 1)
            {
                cs.sendMessage(RED + "Multiple players found by name: " + WHITE + args[1] + RED + ".");
            }
            else
            {
                cs.sendMessage(RED + "Player: " + WHITE + args[1] + RED + " not found.");
            }
        }
        else
        {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }

    public void handleSpy(CommandSender cs, String[] args)
    {
        if (cs instanceof Player)
        {
            Player sender = (Player) cs;
            if (Permission.SYSTEM_SPY.get(cs))
            {
                if (!args[1].equalsIgnoreCase("off"))
                {
                    List<Player> list = SERVER.matchPlayer(args[1]);
                    if (list.size() == 1)
                    {
                        Player player = list.get(0);
                        for (Player p : cs.getServer().getOnlinePlayers())
                        {
                            p.hidePlayer(sender);
                        }
                        sender.setAllowFlight(true);
                        sender.setFlying(true);
                        spyLocation.put(sender.getName(), sender.getLocation());
                        sender.teleport(player);
                        sender.sendMessage(GREEN + "You have been teleported to " + player.getName() + " and made invisible.");
                        sender.sendMessage(GREEN + "To stop spying, type " + WHITE + " /AntiCheat spy off");
                    }
                    else if (list.size() > 1)
                    {
                        cs.sendMessage(RED + "Multiple players found by name: " + WHITE + args[1] + RED + ".");
                    }
                    else
                    {
                        cs.sendMessage(RED + "Player: " + WHITE + args[1] + RED + " not found.");
                    }
                }
                else
                {
                    sender.setFlying(false);
                    sender.setAllowFlight(false);
                    if (spyLocation.containsKey(sender.getName()))
                    {
                        sender.teleport(spyLocation.get(sender.getName()));
                        spyLocation.remove(sender.getName());
                    }
                    for (Player p : cs.getServer().getOnlinePlayers())
                    {
                        p.showPlayer(sender);
                    }
                }
            }
            else
            {
                cs.sendMessage(PERMISSIONS_ERROR);
            }
        }
        else
        {
            cs.sendMessage(RED + "Sorry, but you can't spy on a player from the console.");
        }
    }

    public void handleHelp(CommandSender cs)
    {
        if (Permission.SYSTEM_HELP.get(cs))
        {
            String base = "/AntiCheat ";
            cs.sendMessage("----------------------[" + GREEN + "AntiCheat" + WHITE + "]----------------------");
            cs.sendMessage(base + GREEN + "log [Enable/Disable]" + WHITE + " - toggle logging");
            cs.sendMessage(base + GREEN + "report" + WHITE + " - get a detailed cheat report");
            cs.sendMessage(base + GREEN + "report [user]" + WHITE + " - get a player's cheat report");
            cs.sendMessage(base + GREEN + "reload" + WHITE + " - reload AntiCheat configuration");
            cs.sendMessage(base + GREEN + "reset [user]" + WHITE + " - reset user's hack level");
            cs.sendMessage(base + GREEN + "xray [user]" + WHITE + " - check user's xray levels");
            cs.sendMessage(base + GREEN + "spy [user]" + WHITE + " - spy on a user in secret");
            cs.sendMessage(base + GREEN + "help" + WHITE + " - access this page");
            cs.sendMessage(base + GREEN + "debug" + WHITE + " - post debug information");
            cs.sendMessage(base + GREEN + "update" + WHITE + " - check update status");
            cs.sendMessage("-----------------------------------------------------");
        }
        else
        {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }

    public void handleUpdate(CommandSender cs)
    {
        if (Permission.SYSTEM_UPDATE.get(cs))
        {
            cs.sendMessage("Running " + GREEN + "AntiCheat " + WHITE + "v" + GREEN + Anticheat.getVersion());
            cs.sendMessage("-----------------------------------------------------");
            if (!Anticheat.isUpdated())
            {
                cs.sendMessage(GRAY + "There " + GREEN + "IS" + GRAY + " a newer version avaliable.");
                if (config.autoUpdate())
                {
                    cs.sendMessage(GRAY + "It will be installed automatically for you on next launch.");
                }
                else
                {
                    cs.sendMessage(GRAY + "Due to your config settings, we " + RED + "can not" + GRAY + " auto update.");
                    cs.sendMessage(GRAY + "Please visit http://dev.bukkit.org/server-mods/anticheat/");
                }
            }
            else
            {
                cs.sendMessage(GRAY + "AntiCheat is " + GREEN + "UP TO DATE!");
            }
        }
        else
        {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }

    public void handleReport(CommandSender cs)
    {
        if (Permission.SYSTEM_REPORT.get(cs))
        {
            getPlayers();
            if (!low.isEmpty())
            {
                cs.sendMessage(GREEN + "----Level: Low (Not likely hacking)----");
                for (String string : low)
                {
                    cs.sendMessage(GREEN + string);
                }
            }
            if (!med.isEmpty())
            {
                cs.sendMessage(YELLOW + "----Level: Medium (Possibly hacking/lagging)----");
                for (String string : med)
                {
                    cs.sendMessage(YELLOW + string);
                }
            }
            if (!high.isEmpty())
            {
                cs.sendMessage(RED + "----Level: High (Probably hacking or bad connection)----");
                for (String string : high)
                {
                    cs.sendMessage(RED + string);
                }
            }
        }
        else
        {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }

    public void handlePlayerReport(CommandSender cs, String[] args)
    {
        if (Permission.SYSTEM_REPORT.get(cs))
        {
            List<Player> list = SERVER.matchPlayer(args[1]);
            if (list.size() == 1)
            {
                Player player = list.get(0);
                if (args.length == 2)
                {
                    sendPlayerReport(cs, CheckType.values(), player, 1);
                }
                else if (Utilities.isInt(args[2]))
                {
                    int num = Integer.parseInt(args[2]);
                    if (num <= 3 && num > 1)
                    {
                        sendPlayerReport(cs, CheckType.values(), player, num);
                    }
                    else
                    {
                        cs.sendMessage(RED + "Page: " + num + RED + " does not exist.");
                    }
                }
                else
                {
                    cs.sendMessage(RED + "Not a valid page number: " + WHITE + args[2]);
                }
            }
            else if (list.size() > 1)
            {
                cs.sendMessage(RED + "Multiple players found by name: " + WHITE + args[1] + RED + ".");
            }
            else
            {
                cs.sendMessage(RED + "Player: " + WHITE + args[1] + RED + " not found.");
            }
        }
        else
        {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }

    public void sendPlayerReport(CommandSender cs, CheckType[] types, Player player, int page)
    {
        cs.sendMessage("--------------------[" + GREEN + "REPORT[" + page + "/3]" + WHITE + "]---------------------");
        if (page == 1)
        {
            int level = playerManager.getLevel(player);
            String levelString = GREEN + "Low";
            if (level >= 20)
            {
                levelString = YELLOW + "Medium";
            }
            else if (level >= 50)
            {
                levelString = RED + "High";
            }
            cs.sendMessage(GRAY + "Player: " + WHITE + player.getName());
            cs.sendMessage(GRAY + "Level: " + levelString);
            for (int i = 0; i < 6; i++)
            {
                int use = types[i].getUses(player);
                ChatColor color = WHITE;
                if (use >= 20)
                {
                    color = YELLOW;
                }
                else if (use > 50)
                {
                    color = RED;
                }
                cs.sendMessage(GRAY + CheckType.getName(types[i]) + ": " + color + use);
            }
        }
        else if (page == 2)
        {
            for (int i = 6; i < 14; i++)
            {
                int use = types[i].getUses(player);
                ChatColor color = WHITE;
                if (use >= 20)
                {
                    color = YELLOW;
                }
                else if (use > 50)
                {
                    color = RED;
                }
                cs.sendMessage(GRAY + CheckType.getName(types[i]) + ": " + color + use);
            }
        }
        else if (page == 3)
        {
            for (int i = 14; i < 21; i++)
            {
                int use = types[i].getUses(player);
                ChatColor color = WHITE;
                if (use >= 20)
                {
                    color = YELLOW;
                }
                else if (use > 50)
                {
                    color = RED;
                }
                cs.sendMessage(GRAY + CheckType.getName(types[i]) + ": " + color + use);
            }
        }
        cs.sendMessage("-----------------------------------------------------");

    }

    public void handleReload(CommandSender cs)
    {
        if (Permission.SYSTEM_RELOAD.get(cs))
        {
            config.load();
            cs.sendMessage(GREEN + "AntiCheat configuration reloaded.");
        }
        else
        {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args)
    {
        if (args.length == 3)
        {
            if (args[0].equalsIgnoreCase("report"))
            {
                handlePlayerReport(cs, args);
            }
        }
        else if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("log"))
            {
                handleLog(cs, args);
            }
            else if (args[0].equalsIgnoreCase("xray"))
            {
                handleXRay(cs, args);
            }
            else if (args[0].equalsIgnoreCase("reset"))
            {
                handleReset(cs, args);
            }
            else if (args[0].equalsIgnoreCase("spy"))
            {
                handleSpy(cs, args);
            }
            else if (args[0].equalsIgnoreCase("report"))
            {
                handlePlayerReport(cs, args);
            }
            else
            {
                cs.sendMessage(RED + "Unrecognized command.");
            }
        }
        else if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("help"))
            {
                handleHelp(cs);
            }
            else if (args[0].equalsIgnoreCase("debug"))
            {
                handleDebug(cs);
            }
            else if (args[0].equalsIgnoreCase("report"))
            {
                handleReport(cs);
            }
            else if (args[0].equalsIgnoreCase("reload"))
            {
                handleReload(cs);
            }
            else if (args[0].equalsIgnoreCase("update"))
            {
                handleUpdate(cs);
            }
            else
            {
                cs.sendMessage(RED + "Unrecognized command.");
            }
        }
        else
        {
            cs.sendMessage(RED + "Unrecognized command. Try " + ChatColor.WHITE + "/anticheat help");
        }
        return true;
    }

    public void getPlayers()
    {
        high.clear();
        med.clear();
        low.clear();
        for (Player player : SERVER.getOnlinePlayers())
        {
            int level = playerManager.getLevel(player);
            if (level <= MED_THRESHOLD)
            {
                low.add(player.getName());
            }
            else if (level <= HIGH_THRESHOLD)
            {
                med.add(player.getName());
            }
            else
            {
                high.add(player.getName());
            }
        }
    }
}

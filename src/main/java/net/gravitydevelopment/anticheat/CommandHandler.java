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

package net.gravitydevelopment.anticheat;

import net.gravitydevelopment.anticheat.config.Configuration;
import net.gravitydevelopment.anticheat.manage.CheckType;
import net.gravitydevelopment.anticheat.manage.User;
import net.gravitydevelopment.anticheat.manage.UserManager;
import net.gravitydevelopment.anticheat.util.*;
import net.gravitydevelopment.anticheat.xray.XRayTracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor {
    private static final Configuration CONFIG = AntiCheat.getManager().getConfiguration();
    private static final UserManager USER_MANAGER = AntiCheat.getManager().getUserManager();
    private static final XRayTracker XRAY_TRACKER = AntiCheat.getManager().getXRayTracker();
    private static final ChatColor RED = ChatColor.RED;
    private static final ChatColor YELLOW = ChatColor.YELLOW;
    private static final ChatColor GREEN = ChatColor.GREEN;
    private static final ChatColor WHITE = ChatColor.WHITE;
    private static final ChatColor GRAY = ChatColor.GRAY;
    private static final Server SERVER = Bukkit.getServer();
    private static final String PERMISSIONS_ERROR = RED + "Insufficient Permissions.";
    private static final String MENU_END = "-----------------------------------------------------";

    public void handleLog(CommandSender cs, String[] args) {
        if (hasPermission(cs, Permission.SYSTEM_LOG)) {
            if (args[1].equalsIgnoreCase("enable")) {
                if (!CONFIG.getConfig().logToConsole.getValue()) {
                    CONFIG.getConfig().logToConsole.setValue(true);
                    cs.sendMessage(GREEN + "Console logging enabled.");
                    CONFIG.getConfig().reload();
                } else {
                    cs.sendMessage(GREEN + "Console logging is already enabled!");
                }
            } else if (args[1].equalsIgnoreCase("disable")) {
                if (CONFIG.getConfig().logToConsole.getValue()) {
                    CONFIG.getConfig().logToConsole.setValue(false);
                    cs.sendMessage(GREEN + "Console logging disabled.");
                    CONFIG.getConfig().reload();
                } else {
                    cs.sendMessage(GREEN + "Console logging is already disabled!");
                }
            } else {
                cs.sendMessage(RED + "Usage: /anticheat log [enable/disable]");
            }
        }
    }

    public void handleDebug(CommandSender cs) {
        handleDebug(cs, null);
    }

    public void handleDebug(CommandSender cs, Player tp) {
        if (hasPermission(cs, Permission.SYSTEM_REPORT)) {
            cs.sendMessage(GRAY + "Please wait while I collect some data...");
            PastebinReport report = new PastebinReport(cs, tp);
            cs.sendMessage(GREEN + "Debug information posted to: " + WHITE + report.getURL());
            cs.sendMessage(GREEN + "Please include this link when making bug reports.");
        }
    }

    public void handleXRay(CommandSender cs, String[] args) {
        if (hasPermission(cs, Permission.SYSTEM_XRAY)) {
            if (CONFIG.getConfig().checkXRay.getValue()) {
                List<Player> list = SERVER.matchPlayer(args[1]);
                if (list.size() == 1) {
                    Player player = list.get(0);
                    if (XRAY_TRACKER.sufficientData(player.getName())) {
                        XRAY_TRACKER.sendStats(cs, player.getName());
                    } else {
                        cs.sendMessage(RED + "Insufficient data collected from " + WHITE + args[1] + RED + ".");
                        cs.sendMessage(RED + "Please wait until more info is collected before predictions are calculated.");
                    }
                } else if (list.size() > 1) {
                    cs.sendMessage(RED + "Multiple players found by name: " + WHITE + args[1] + RED + ".");
                } else if (XRAY_TRACKER.sufficientData(args[1])) {
                    XRAY_TRACKER.sendStats(cs, args[1]);
                } else {
                    cs.sendMessage(RED + "Insufficient data collected from " + WHITE + args[1] + RED + ".");
                    cs.sendMessage(RED + "Please wait until more info is collected before predictions are calculated.");
                }
            } else {
                cs.sendMessage(RED + "XRay logging is off in the config.");
            }
        }
    }

    public void handleReset(CommandSender cs, String[] args) {
        if (hasPermission(cs, Permission.SYSTEM_RESET)) {
            List<Player> list = SERVER.matchPlayer(args[1]);
            if (list.size() == 1) {
                Player player = list.get(0);
                User user = USER_MANAGER.getUser(player.getName());
                if (user != null) {
                    user.resetLevel();
                    XRAY_TRACKER.reset(player.getName());
                    user.clearMessages();
                    AntiCheat.getManager().getBackend().resetChatLevel(player);
                    cs.sendMessage(player.getName() + GREEN + " has been reset.");
                } else {

                }
            } else if (list.size() > 1) {
                cs.sendMessage(RED + "Multiple players found by name: " + WHITE + args[1] + RED + ".");
            } else {
                cs.sendMessage(RED + "Player: " + WHITE + args[1] + RED + " not found.");
            }
        }
    }

    public void handleSpy(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            if (hasPermission(cs, Permission.SYSTEM_SPY)) {
                Player sender = (Player) cs;
                if (!args[1].equalsIgnoreCase("off")) {
                    List<Player> list = SERVER.matchPlayer(args[1]);
                    if (list.size() == 1) {
                        Player player = list.get(0);
                        for (Player p : cs.getServer().getOnlinePlayers()) {
                            if (!Permission.SYSTEM_SPY.get(p)) {
                                p.hidePlayer(sender);
                            }
                        }
                        if (!sender.hasMetadata(Utilities.SPY_METADATA)) // Maintain ORIGINAL location and other data
                        {
                            SpyState state = new SpyState(sender.getAllowFlight(), sender.isFlying(), sender.getLocation());
                            sender.setMetadata(Utilities.SPY_METADATA, new FixedMetadataValue(AntiCheat.getPlugin(), state));
                        }
                        sender.setAllowFlight(true);
                        sender.setFlying(true);
                        sender.teleport(player);
                        sender.sendMessage(GREEN + "You have been teleported to " + player.getName() + " and made invisible.");
                        sender.sendMessage(GREEN + "To stop spying, type " + WHITE + " /AntiCheat spy off");
                    } else if (list.size() > 1) {
                        cs.sendMessage(RED + "Multiple players found by name: " + WHITE + args[1] + RED + ".");
                    } else {
                        cs.sendMessage(RED + "Player: " + WHITE + args[1] + RED + " not found.");
                    }
                } else {
                    if (sender.hasMetadata(Utilities.SPY_METADATA)) {
                        SpyState state = ((SpyState) sender.getMetadata(Utilities.SPY_METADATA).get(0).value());
                        sender.setAllowFlight(state.getAllowFlight());
                        sender.setFlying(state.getFlying());
                        sender.teleport(state.getLocation());
                        sender.removeMetadata(Utilities.SPY_METADATA, AntiCheat.getPlugin());
                        for (Player p : cs.getServer().getOnlinePlayers()) {
                            p.showPlayer(sender);
                        }
                        sender.sendMessage(GREEN + "Done spying! Brought you back to where you started!");
                    } else {
                        sender.sendMessage(RED + "You were not spying.");
                    }
                }
            }
        } else {
            cs.sendMessage(RED + "Sorry, but you can't spy on a player from the console.");
        }
    }

    public void handleHelp(CommandSender cs) {
        if (hasPermission(cs, Permission.SYSTEM_HELP)) {
            String base = "/AntiCheat ";
            String[] lines = {
                    "log [Enable/Disable]" + WHITE + " - toggle logging",
                    "report [group]" + WHITE + " - show users in groups",
                    "report [user]" + WHITE + " - get a player's cheat report",
                    "reload" + WHITE + " - reload AntiCheat configuration",
                    "reset [user]" + WHITE + " - reset user's hack level",
                    "xray [user]" + WHITE + " - check user's xray levels",
                    "spy [user]" + WHITE + " - spy on a user in secret",
                    "help" + WHITE + " - access this page",
                    "debug" + WHITE + " - post debug information",
                    "update" + WHITE + " - check update status",
            };

            cs.sendMessage("----------------------[" + GREEN + "AntiCheat" + WHITE + "]----------------------");
            for (String s : lines) {
                cs.sendMessage(base + GREEN + s);
            }
        }
    }

    public void handleUpdate(CommandSender cs) {
        if (hasPermission(cs, Permission.SYSTEM_UPDATE)) {
            cs.sendMessage(GRAY + "Running AntiCheat v" + GREEN + AntiCheat.getVersion());
            cs.sendMessage(MENU_END);
            if (!AntiCheat.isUpdated()) {
                cs.sendMessage(GRAY + "There " + GREEN + "IS" + GRAY + " a newer version available.");
                if (CONFIG.getConfig().autoUpdate.getValue()) {
                    cs.sendMessage(GRAY + "It will be installed automatically for you on next launch.");
                } else {
                    cs.sendMessage(GRAY + "Due to your config settings, we " + RED + "can not" + GRAY + " auto update.");
                    cs.sendMessage(GRAY + "Please visit http://dev.bukkit.org/server-mods/anticheat/");
                }
            } else {
                cs.sendMessage(GRAY + "AntiCheat is " + GREEN + "UP TO DATE!");
            }
        }
    }

    public void handleCalibrate(CommandSender cs) {
        if (cs instanceof Player) {
            if (hasPermission(cs, Permission.SYSTEM_CALIBRATE)) {
                Calibrator c = new Calibrator((Player) cs);
                SERVER.getPluginManager().registerEvents(c, AntiCheat.getPlugin());
                c.calibrate();
            }
        }
    }

    public void handleReport(CommandSender cs, String[] args) {
        if (hasPermission(cs, Permission.SYSTEM_REPORT)) {
            if (args.length > 1) {
                String group = args[1];
                int num = getReportPageNum(args);
                if (group.equalsIgnoreCase("low")) {
                    if (num > 0) {
                        sendReport(cs, null, num);
                    } else {
                        cs.sendMessage(RED + "Not a valid page number: " + WHITE + args[2]);
                    }
                } else if (group.equalsIgnoreCase("all")) {
                    cs.sendMessage(GREEN + "Low: " + WHITE + USER_MANAGER.getUsersInGroup(null).size() + " players");
                    for (Group g : CONFIG.getGroups().getGroups()) {
                        int numPlayers = USER_MANAGER.getUsersInGroup(g).size();
                        cs.sendMessage(g.getColor() + g.getName() + WHITE + ": " + numPlayers + " players");
                    }
                    cs.sendMessage("Use /anticheat report [group] for a list of players in each group.");
                } else {
                    boolean sent = false;
                    for (Group g : CONFIG.getGroups().getGroups()) {
                        if (g.getName().equalsIgnoreCase(group)) {
                            sendReport(cs, g, num);
                            sent = true;
                            break;
                        }
                    }
                    if (!sent) {
                        cs.sendMessage(RED + "Not a valid group: " + WHITE + group);
                    }
                }
            }
        }
    }

    public int getReportPageNum(String[] args) {
        if (args.length == 2) {
            return 1;
        } else if (Utilities.isInt(args[2])) {
            return Integer.parseInt(args[2]);
        } else {
            return -1;
        }
    }

    public void sendReport(CommandSender cs, Group group, int page) {
        List<User> users = USER_MANAGER.getUsersInGroup(group);
        ChatColor color = group == null ? GREEN : group.getColor();
        String groupName = group == null ? "Low" : group.getName();
        int pages = (int) Math.ceil(((float) users.size()) / 7);
        if (page <= pages && page > 0) {
            cs.sendMessage("--------------------[" + GREEN + "REPORT[" + page + "/" + pages + "]" + WHITE + "]---------------------");
            cs.sendMessage(GRAY + "Group: " + color + groupName);
            for (int x = 0; x < 7; x++) {
                int index = ((page - 1) * 6) + (x + ((page - 1) * 1));
                if (index < users.size()) {
                    String player = users.get(index).getName();
                    cs.sendMessage(GRAY + player);
                }
            }
            cs.sendMessage(MENU_END);
        } else {
            if (pages == 0) {
                cs.sendMessage("--------------------[" + GREEN + "REPORT[1/1]" + WHITE + "]---------------------");
                cs.sendMessage(GRAY + "Group: " + color + groupName);
                cs.sendMessage(GRAY + "There are no users in this group.");
                cs.sendMessage(MENU_END);
            } else {
                cs.sendMessage(RED + "Page not found. Requested " + WHITE + page + RED + ", Max " + WHITE + pages);
            }
        }
    }

    public void handlePlayerReport(CommandSender cs, String[] args) {
        if (hasPermission(cs, Permission.SYSTEM_REPORT)) {
            User user = AntiCheat.getManager().getUserManager().getUser(args[1]);
            boolean cont = false;
            if (user == null) {
                if ((user = AntiCheat.getManager().getUserManager().getUser(args[1])) == null) {
                    cs.sendMessage(RED + "Player: " + WHITE + args[1] + RED + " not found.");
                } else {
                    cont = true;
                }
            } else {
                cont = true;
            }
            if (cont) {
                List<CheckType> l = new ArrayList<CheckType>();
                for (CheckType type : CheckType.values()) {
                    if (type.getUses(user.getName()) > 0) {
                        l.add(type);
                    }
                }
                if (args.length == 2) {
                    sendPlayerReport(cs, l, user, 1);
                } else if (Utilities.isInt(args[2])) {
                    int num = Integer.parseInt(args[2]);
                    sendPlayerReport(cs, l, user, num);
                } else {
                    cs.sendMessage(RED + "Not a valid page number: " + WHITE + args[2]);
                }
            }
        }
    }

    public void handleDeveloper(CommandSender cs) {
        if (hasPermission(cs, Permission.SYSTEM_DEBUG)) {
            AntiCheat.setDeveloperMode(!AntiCheat.developerMode());
            cs.sendMessage(GREEN + "Developer mode " + (AntiCheat.developerMode() ? "ON" : "OFF"));
        }
    }

    public void sendPlayerReport(CommandSender cs, List<CheckType> types, User user, int page) {
        String name = user.getName();
        int pages = (int) Math.ceil(((float) types.size()) / 6);

        Group group = user.getGroup();
        String groupString = GREEN + "Low";

        if (group != null) {
            groupString = group.getColor() + group.getName();
        }
        groupString += " (" + user.getLevel() + ")";

        if (page <= pages && page > 0) {
            cs.sendMessage("--------------------[" + GREEN + "REPORT[" + page + "/" + pages + "]" + WHITE + "]---------------------");
            cs.sendMessage(GRAY + "Player: " + WHITE + name);
            cs.sendMessage(GRAY + "Group: " + groupString);
            for (int x = 0; x < 6; x++) {
                int index = ((page - 1) * 5) + (x + ((page - 1)));
                if (index < types.size()) {
                    int use = types.get(index).getUses(name);
                    ChatColor color = WHITE;
                    if (use >= 20) {
                        color = YELLOW;
                    } else if (use > 50) {
                        color = RED;
                    }
                    cs.sendMessage(GRAY + CheckType.getName(types.get(index)) + ": " + color + use);
                }
            }
            cs.sendMessage(MENU_END);
        } else {
            if (pages == 0) {
                cs.sendMessage("--------------------[" + GREEN + "REPORT[1/1]" + WHITE + "]---------------------");
                cs.sendMessage(GRAY + "Player: " + WHITE + name);
                cs.sendMessage(GRAY + "Group: " + groupString);
                cs.sendMessage(GRAY + "This user has not failed any checks.");
                cs.sendMessage(MENU_END);
            } else {
                cs.sendMessage(RED + "Page not found. Requested " + WHITE + page + RED + ", Max " + WHITE + pages);
            }
        }
    }

    public void handleReload(CommandSender cs) {
        if (hasPermission(cs, Permission.SYSTEM_RELOAD)) {
            CONFIG.load();
            cs.sendMessage(GREEN + "AntiCheat configuration reloaded.");
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("report")) {
                for (Group group : CONFIG.getGroups().getGroups()) {
                    if (args[1].equalsIgnoreCase(group.getName())) {
                        handleReport(cs, args);
                        return true;
                    }
                }
                handlePlayerReport(cs, args);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("log")) {
                handleLog(cs, args);
            } else if (args[0].equalsIgnoreCase("xray")) {
                handleXRay(cs, args);
            } else if (args[0].equalsIgnoreCase("debug")) {
                handleDebug(cs, Bukkit.getPlayer(args[1]));
            } else if (args[0].equalsIgnoreCase("reset")) {
                handleReset(cs, args);
            } else if (args[0].equalsIgnoreCase("spy")) {
                handleSpy(cs, args);
            } else if (args[0].equalsIgnoreCase("report")) {
                if (args[1].equalsIgnoreCase("low") || args[1].equalsIgnoreCase("medium") || args[1].equalsIgnoreCase("high") || args[1].equalsIgnoreCase("all")) {
                    handleReport(cs, args);
                } else {
                    handlePlayerReport(cs, args);
                }
            } else {
                cs.sendMessage(RED + "Unrecognized command.");
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                handleHelp(cs);
            } else if (args[0].equalsIgnoreCase("debug")) {
                handleDebug(cs);
            } else if (args[0].equalsIgnoreCase("report")) {
                cs.sendMessage(GREEN + "To see a user's report, use " + WHITE + "/anticheat report [user]");
                cs.sendMessage(GREEN + "To see all users in a group, use " + WHITE + "/anticheat report [group]");
            } else if (args[0].equalsIgnoreCase("reload")) {
                handleReload(cs);
            } else if (args[0].equalsIgnoreCase("update")) {
                handleUpdate(cs);
            } else if (args[0].equalsIgnoreCase("developer")) {
                handleDeveloper(cs);
            }
            /**
             * else if (args[0].equalsIgnoreCase("calibrate")) { handleCalibrate(cs); }
             */
            else {
                cs.sendMessage(RED + "Unrecognized command.");
            }
        } else {
            handleHelp(cs); // Handle no args as alias of help
        }
        return true;
    }

    public boolean hasPermission(CommandSender cs, Permission perm) {
        if (perm.get(cs)) {
            return true;
        } else {
            cs.sendMessage(PERMISSIONS_ERROR + " (" + WHITE + perm.toString() + RED + ")");
            return false;
        }
    }
}

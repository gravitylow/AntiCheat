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

package net.h31ix.anticheat;

import java.util.ArrayList;
import java.util.List;
import net.h31ix.anticheat.manage.CheckType;
import net.h31ix.anticheat.manage.User;
import net.h31ix.anticheat.manage.UserManager;
import net.h31ix.anticheat.util.*;
import net.h31ix.anticheat.xray.XRayTracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class CommandHandler implements CommandExecutor {
    private static final Configuration CONFIG = Anticheat.getManager().getConfiguration();
    private static final UserManager USER_MANAGER = Anticheat.getManager().getUserManager();
    private static final XRayTracker XRAY_TRACKER = Anticheat.getManager().getXRayTracker();
    private static final ChatColor RED = ChatColor.RED;
    private static final ChatColor YELLOW = ChatColor.YELLOW;
    private static final ChatColor GREEN = ChatColor.GREEN;
    private static final ChatColor WHITE = ChatColor.WHITE;
    private static final ChatColor GRAY = ChatColor.GRAY;
    private List<String> high = new ArrayList<String>();
    private List<String> med = new ArrayList<String>();
    private List<String> low = new ArrayList<String>();
    private static final Server SERVER = Bukkit.getServer();
    private static final int MED_THRESHOLD = 20;
    private static final int HIGH_THRESHOLD = 50;
    private static final String PERMISSIONS_ERROR = RED + "Insufficient Permissions.";
    private static final String MENU_END = "-----------------------------------------------------";
    
    public void handleLog(CommandSender cs, String[] args) {
        if (Permission.SYSTEM_LOG.get(cs)) {
            if (args[1].equalsIgnoreCase("enable")) {
                if (!CONFIG.logConsole()) {
                    CONFIG.setLog(true);
                    cs.sendMessage(GREEN + "Console logging enabled.");
                } else {
                    cs.sendMessage(GREEN + "Console logging is already enabled!");
                }
            } else if (args[1].equalsIgnoreCase("disable")) {
                if (CONFIG.logConsole()) {
                    CONFIG.setLog(false);
                    cs.sendMessage(GREEN + "Console logging disabled.");
                } else {
                    cs.sendMessage(GREEN + "Console logging is already disabled!");
                }
            } else {
                cs.sendMessage(RED + "Usage: /anticheat log [enable/disable]");
            }
        } else {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }
    
    public void handleDebug(CommandSender cs) {
        if (Permission.SYSTEM_REPORT.get(cs)) {
            PastebinReport report = new PastebinReport(cs);
            cs.sendMessage(GREEN + "Debug information posted to: " + WHITE + report.getURL());
            cs.sendMessage(GREEN + "Please include this link when making bug reports.");
        } else {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }
    
    public void handleDebug(CommandSender cs, Player tp) {
        if (Permission.SYSTEM_REPORT.get(cs)) {
            PastebinReport report = new PastebinReport(cs, tp);
            cs.sendMessage(GREEN + "Debug information posted to: " + WHITE + report.getURL());
            cs.sendMessage(GREEN + "Please include this link when making bug reports.");
        } else {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }
    
    public void handleXRay(CommandSender cs, String[] args) {
        if (Permission.SYSTEM_XRAY.get(cs)) {
            if (CONFIG.logXRay()) {
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
        } else {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }
    
    public void handleReset(CommandSender cs, String[] args) {
        if (Permission.SYSTEM_RESET.get(cs)) {
            List<Player> list = SERVER.matchPlayer(args[1]);
            if (list.size() == 1) {
                Player player = list.get(0);
                if (USER_MANAGER.safeGetLevel(player.getName()) == 0) {
                    cs.sendMessage(player.getName() + RED + " is already in Low Level!");
                } else {
                    USER_MANAGER.safeReset(player.getName());
                    cs.sendMessage(player.getName() + GREEN + " has been reset to Low Level.");
                }
                XRAY_TRACKER.reset(player.getName());
                cs.sendMessage(player.getName() + GREEN + "'s XRay stats have been reset.");
            } else if (list.size() > 1) {
                cs.sendMessage(RED + "Multiple players found by name: " + WHITE + args[1] + RED + ".");
            } else {
                cs.sendMessage(RED + "Player: " + WHITE + args[1] + RED + " not found.");
            }
        } else {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }
    
    public void handleSpy(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            if (Permission.SYSTEM_SPY.get(cs)) {
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
                            sender.setMetadata(Utilities.SPY_METADATA, new FixedMetadataValue(Anticheat.getPlugin(), state));
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
                        sender.removeMetadata(Utilities.SPY_METADATA, Anticheat.getPlugin());
                        for (Player p : cs.getServer().getOnlinePlayers()) {
                            p.showPlayer(sender);
                        }
                        sender.sendMessage(GREEN + "Done spying! Brought you back to where you started!");
                    } else {
                        sender.sendMessage(RED + "You were not spying.");
                    }
                }
            } else {
                cs.sendMessage(PERMISSIONS_ERROR);
            }
        } else {
            cs.sendMessage(RED + "Sorry, but you can't spy on a player from the console.");
        }
    }
    
    public void handleHelp(CommandSender cs) {
        if (Permission.SYSTEM_HELP.get(cs)) {
            String base = "/AntiCheat ";
            cs.sendMessage("----------------------[" + GREEN + "AntiCheat" + WHITE + "]----------------------");
            cs.sendMessage(base + GREEN + "log [Enable/Disable]" + WHITE + " - toggle logging");
            cs.sendMessage(base + GREEN + "report [low/medium/high]" + WHITE + " - show users in groups");
            cs.sendMessage(base + GREEN + "report [user]" + WHITE + " - get a player's cheat report");
            cs.sendMessage(base + GREEN + "reload" + WHITE + " - reload AntiCheat configuration");
            cs.sendMessage(base + GREEN + "reset [user]" + WHITE + " - reset user's hack level");
            cs.sendMessage(base + GREEN + "xray [user]" + WHITE + " - check user's xray levels");
            cs.sendMessage(base + GREEN + "spy [user]" + WHITE + " - spy on a user in secret");
            cs.sendMessage(base + GREEN + "help" + WHITE + " - access this page");
            cs.sendMessage(base + GREEN + "debug" + WHITE + " - post debug information");
            cs.sendMessage(base + GREEN + "update" + WHITE + " - check update status");
            cs.sendMessage(MENU_END);
        } else {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }
    
    public void handleUpdate(CommandSender cs) {
        if (Permission.SYSTEM_UPDATE.get(cs)) {
            cs.sendMessage("Running " + GREEN + "AntiCheat " + WHITE + "v" + GREEN + Anticheat.getVersion());
            cs.sendMessage(MENU_END);
            if (!Anticheat.isUpdated()) {
                cs.sendMessage(GRAY + "There " + GREEN + "IS" + GRAY + " a newer version avaliable.");
                if (CONFIG.autoUpdate()) {
                    cs.sendMessage(GRAY + "It will be installed automatically for you on next launch.");
                } else {
                    cs.sendMessage(GRAY + "Due to your config settings, we " + RED + "can not" + GRAY + " auto update.");
                    cs.sendMessage(GRAY + "Please visit http://dev.bukkit.org/server-mods/anticheat/");
                }
            } else {
                cs.sendMessage(GRAY + "AntiCheat is " + GREEN + "UP TO DATE!");
            }
        } else {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }
    
    public void handleCalibrate(CommandSender cs) {
        if (cs instanceof Player) {
            if (Permission.SYSTEM_CALIBRATE.get(cs)) {
                Calibrator c = new Calibrator((Player) cs);
                SERVER.getPluginManager().registerEvents(c, Anticheat.getPlugin());
                c.calibrate();
            } else {
                cs.sendMessage(PERMISSIONS_ERROR);
            }
        } else {
            cs.sendMessage(RED + "Sorry, but you cannot calibrate from console.");
        }
    }
    
    public void handleReport(CommandSender cs, String[] args) {
        if (Permission.SYSTEM_REPORT.get(cs)) {
            getPlayers();
            if (args.length > 1) {
                String group = args[1];
                if (group.equalsIgnoreCase("low")) {
                    int num = getReportPageNum(args);
                    if (num > 0) {
                        sendReport(cs, low, "Low", GREEN, num);
                    } else {
                        cs.sendMessage(RED + "Not a valid page number: " + WHITE + args[2]);
                    }
                } else if (group.equalsIgnoreCase("medium")) {
                    int num = getReportPageNum(args);
                    if (num > 0) {
                        sendReport(cs, med, "Medium", YELLOW, num);
                    } else {
                        cs.sendMessage(RED + "Not a valid page number: " + WHITE + args[2]);
                    }
                }
                if (group.equalsIgnoreCase("high")) {
                    int num = getReportPageNum(args);
                    if (num > 0) {
                        sendReport(cs, high, "High", RED, num);
                    } else {
                        cs.sendMessage(RED + "Not a valid page number: " + WHITE + args[2]);
                    }
                }
            }
        } else {
            cs.sendMessage(PERMISSIONS_ERROR);
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
    
    public void sendReport(CommandSender cs, List<String> players, String group, ChatColor color, int page) {
        int pages = (int) Math.ceil(((float) players.size()) / 7);
        if (page <= pages && page > 0) {
            cs.sendMessage("--------------------[" + GREEN + "REPORT[" + page + "/" + pages + "]" + WHITE + "]---------------------");
            cs.sendMessage(GRAY + "Group: " + color + group);
            for (int x = 0; x < 7; x++) {
                int index = ((page - 1) * 6) + (x + ((page - 1) * 1));
                if (index < players.size()) {
                    String player = players.get(index);
                    cs.sendMessage(GRAY + player);
                }
            }
            cs.sendMessage(MENU_END);
        } else {
            if (pages == 0) {
                cs.sendMessage("--------------------[" + GREEN + "REPORT[1/1]" + WHITE + "]---------------------");
                cs.sendMessage(GRAY + "Group: " + color + group);
                cs.sendMessage(GRAY + "There are no users in this group.");
                cs.sendMessage(MENU_END);
            } else {
                cs.sendMessage(RED + "Page not found. Requested " + WHITE + page + RED + ", Max " + WHITE + pages);
            }
        }
    }
    
    public void handlePlayerReport(CommandSender cs, String[] args) {
        if (Permission.SYSTEM_REPORT.get(cs)) {
            User user = Anticheat.getManager().getUserManager().getUser(args[1]);
            boolean cont = false;
            if (user == null) {
                if ((user = Anticheat.getManager().getUserManager().loadUserFromFile(args[1])) == null) {
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
        } else {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }
    
    public void sendPlayerReport(CommandSender cs, List<CheckType> types, User user, int page) {
        String name = user.getName();
        int pages = (int) Math.ceil(((float) types.size()) / 6);
        String levelString = ChatColor.GREEN+"Low";
        for(Level level : CONFIG.getLevels()) {
            if(level.getValue() == user.getLevel()) {
                levelString = level.getColor()+level.getName();
            }
        }
        if (page <= pages && page > 0) {
            cs.sendMessage("--------------------[" + GREEN + "REPORT[" + page + "/" + pages + "]" + WHITE + "]---------------------");
            cs.sendMessage(GRAY + "Player: " + WHITE + name);
            cs.sendMessage(GRAY + "Level: " + levelString);
            for (int x = 0; x < 6; x++) {
                int index = ((page - 1) * 5) + (x + ((page - 1) * 1));
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
                cs.sendMessage(GRAY + "Level: " + levelString);
                cs.sendMessage(GRAY + "This user has not failed any checks.");
                cs.sendMessage(MENU_END);
            } else {
                cs.sendMessage(RED + "Page not found. Requested " + WHITE + page + RED + ", Max " + WHITE + pages);
            }
        }
    }
    
    public void handleReload(CommandSender cs) {
        if (Permission.SYSTEM_RELOAD.get(cs)) {
            CONFIG.load();
            cs.sendMessage(GREEN + "AntiCheat configuration reloaded.");
        } else {
            cs.sendMessage(PERMISSIONS_ERROR);
        }
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("report")) {
                for(Level level : CONFIG.getLevels()) {
                    if(args[1].equalsIgnoreCase(level.getName())) {
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
                if (args[1].equalsIgnoreCase("low") || args[1].equalsIgnoreCase("medium") || args[1].equalsIgnoreCase("high")) {
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
                cs.sendMessage(ChatColor.GREEN + "To see the report of a specific user, type their name, like so:");
                cs.sendMessage(ChatColor.WHITE + "/anticheat report [user]");
                cs.sendMessage(ChatColor.GRAY + " - This will allow you to see the checks that user has failed.");
                cs.sendMessage(ChatColor.GREEN + "To see the report of an AntiCheat group, type it's name, like so:");
                cs.sendMessage(ChatColor.WHITE + "/anticheat report [low/medium/high]");
                cs.sendMessage(ChatColor.GRAY + " - This will allow you to see which players are good, and which could be hacking.");
            } else if (args[0].equalsIgnoreCase("reload")) {
                handleReload(cs);
            } else if (args[0].equalsIgnoreCase("update")) {
                handleUpdate(cs);
            }
            /**
             * else if (args[0].equalsIgnoreCase("calibrate")) { handleCalibrate(cs); }
             */
            else {
                cs.sendMessage(RED + "Unrecognized command.");
            }
        } else {
            cs.sendMessage(RED + "Unrecognized command. Try " + ChatColor.WHITE + "/anticheat help");
        }
        return true;
    }
    
    public void getPlayers() {
        high.clear();
        med.clear();
        low.clear();
        for (Player player : SERVER.getOnlinePlayers()) {
            int level = USER_MANAGER.safeGetLevel(player.getName());
            if (level <= MED_THRESHOLD) {
                low.add(player.getName());
            } else if (level <= HIGH_THRESHOLD) {
                med.add(player.getName());
            } else {
                high.add(player.getName());
            }
        }
    }
}

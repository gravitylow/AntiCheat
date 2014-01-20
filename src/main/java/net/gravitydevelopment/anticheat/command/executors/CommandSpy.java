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

package net.gravitydevelopment.anticheat.command.executors;

import net.gravitydevelopment.anticheat.AntiCheat;
import net.gravitydevelopment.anticheat.command.CommandBase;
import net.gravitydevelopment.anticheat.util.Permission;
import net.gravitydevelopment.anticheat.util.SpyState;
import net.gravitydevelopment.anticheat.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

public class CommandSpy extends CommandBase {

    private static final String NAME = "AntiCheat Spying";
    private static final String COMMAND = "spy";
    private static final String USAGE = "anticheat spy [user]";
    private static final Permission PERMISSION = Permission.SYSTEM_SPY;
    private static final String[] HELP = {
            GRAY + "Use: " + AQUA + "/anticheat spy [user]" + GRAY + " to spy on a user",
    };

    public CommandSpy() {
        super(NAME, COMMAND, USAGE, HELP, PERMISSION);
    }

    @Override
    protected void execute(CommandSender cs, String[] args) {
        if (args.length == 1) {
            if (cs instanceof Player) {
                Player sender = (Player) cs;
                if (!args[0].equalsIgnoreCase("off")) {
                    Player player = Bukkit.getPlayer(args[0]);
                    if (player != null) {
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
                        sender.sendMessage(GREEN + "To stop spying, type " + WHITE + " /anti spy off");
                    } else {
                        cs.sendMessage(RED + "Player: " +args[0] + " not found.");
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
                } else {
                    cs.sendMessage(RED + "Sorry, but you can't spy on a player from the console.");
                }
            } else {
                sendHelp(cs);
            }
        }
}

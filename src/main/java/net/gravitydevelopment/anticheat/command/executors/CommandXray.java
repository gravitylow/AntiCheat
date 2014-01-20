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

import net.gravitydevelopment.anticheat.command.CommandBase;
import net.gravitydevelopment.anticheat.util.PastebinReport;
import net.gravitydevelopment.anticheat.util.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandXray extends CommandBase {

    private static final String NAME = "AntiCheat XRAY Stats";
    private static final String COMMAND = "xray";
    private static final String USAGE = "anticheat xray [user]";
    private static final Permission PERMISSION = Permission.SYSTEM_UPDATE;
    private static final String[] HELP = {
            GRAY + "Use: " + AQUA + "/anticheat xray [user]" + GRAY + " to view xray statistics for a user",
    };

    public CommandXray() {
        super(NAME, COMMAND, USAGE, HELP, PERMISSION);
    }

    @Override
    protected void execute(CommandSender cs, String[] args) {
        if (args.length == 1) {
            if (CONFIG.getConfig().checkXRay.getValue()) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player != null) {
                    if (XRAY_TRACKER.sufficientData(player.getName())) {
                        XRAY_TRACKER.sendStats(cs, player.getName());
                    } else {
                        cs.sendMessage(RED + "Insufficient data collected from " + WHITE + args[0] + RED + ".");
                        cs.sendMessage(RED + "Please wait until more info is collected before predictions are calculated.");
                    }
                } else if (XRAY_TRACKER.sufficientData(args[0])) {
                    XRAY_TRACKER.sendStats(cs, args[1]);
                } else {
                    cs.sendMessage(RED + "Insufficient data collected from " + WHITE + args[0] + RED + ".");
                    cs.sendMessage(RED + "Please wait until more info is collected before predictions are calculated.");
                }
            } else {
                cs.sendMessage(RED + "XRay logging is off in the config.");
            }
        } else {
            sendHelp(cs);
        }
    }
}

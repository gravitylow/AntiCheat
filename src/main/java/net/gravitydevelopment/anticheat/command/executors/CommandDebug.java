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

public class CommandDebug extends CommandBase {

    private static final String NAME = "AntiCheat Debug Reporting";
    private static final String COMMAND = "debug";
    private static final String USAGE = "anticheat debug <user>";
    private static final Permission PERMISSION = Permission.SYSTEM_DEBUG;
    private static final String[] HELP = {
            GRAY + "Use: " + AQUA + "/anticheat debug" + GRAY + " to create a debug report",
            GRAY + "Use: " + AQUA + "/anticheat debug <user>" + GRAY + " to create a debug report for a user",
    };

    public CommandDebug() {
        super(NAME, COMMAND, USAGE, HELP, PERMISSION);
    }

    @Override
    protected void execute(CommandSender cs, String[] args) {
        Player player = null;
        if (args.length == 1) {
            player = Bukkit.getPlayer(args[0]);
        }

        cs.sendMessage(GRAY + "Please wait while I collect some data...");
        PastebinReport report = new PastebinReport(cs, player);
        cs.sendMessage(GREEN + "Debug information posted to: " + WHITE + report.getURL());
        cs.sendMessage(GREEN + "Please include this link when making bug reports.");
    }
}

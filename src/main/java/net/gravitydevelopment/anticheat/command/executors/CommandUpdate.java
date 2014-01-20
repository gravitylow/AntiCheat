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
import net.gravitydevelopment.anticheat.util.PastebinReport;
import net.gravitydevelopment.anticheat.util.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUpdate extends CommandBase {

    private static final String NAME = "AntiCheat Update Checking";
    private static final String COMMAND = "update";
    private static final String USAGE = "anticheat update";
    private static final Permission PERMISSION = Permission.SYSTEM_UPDATE;
    private static final String[] HELP = {
            GRAY + "Use: " + AQUA + "/anticheat update" + GRAY + " to view the system's update status",
    };

    public CommandUpdate() {
        super(NAME, COMMAND, USAGE, HELP, PERMISSION);
    }

    @Override
    protected void execute(CommandSender cs, String[] args) {
        cs.sendMessage(GRAY + "Running AntiCheat v" + GREEN + AntiCheat.getVersion());
        if (CONFIG.getConfig().autoUpdate.getValue()) {
            cs.sendMessage(GRAY + "Up to date: " + (AntiCheat.isUpdated() ? GREEN + "YES" : RED + "NO"));
            if (!AntiCheat.isUpdated()) {
                cs.sendMessage(GRAY + "Newest version: " + GREEN + AntiCheat.getUpdateDetails());
                cs.sendMessage(GOLD + "The newest version will be automatically installed on next launch.");
            }
        } else {
            cs.sendMessage(GRAY + "Your config settings have disabled update checking.");
            cs.sendMessage(GRAY + "Please enable this setting or visit http://dev.bukkit.org/bukkit-plugins/anticheat/");
        }
    }
}

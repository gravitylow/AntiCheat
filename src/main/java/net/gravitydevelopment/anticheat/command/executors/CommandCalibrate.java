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
import net.gravitydevelopment.anticheat.util.Calibrator;
import net.gravitydevelopment.anticheat.util.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCalibrate extends CommandBase {

    private static final String NAME = "AntiCheat Calibration";
    private static final String COMMAND = "calibrate";
    private static final String USAGE = "anticheat calibrate";
    private static final Permission PERMISSION = Permission.SYSTEM_CALIBRATE;
    private static final String[] HELP = {
            GRAY + "Use: " + AQUA + "/anticheat calibrate" + GRAY + " to calibrate AntiCheat settings",
    };

    public CommandCalibrate() {
        super(NAME, COMMAND, USAGE, HELP, PERMISSION);
    }

    @Override
    protected void execute(CommandSender cs, String[] args) {
        Calibrator c = new Calibrator((Player) cs);
        SERVER.getPluginManager().registerEvents(c, AntiCheat.getPlugin());
        c.calibrate();
    }
}

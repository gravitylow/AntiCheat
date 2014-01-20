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

import net.gravitydevelopment.anticheat.check.CheckType;
import net.gravitydevelopment.anticheat.command.CommandBase;
import net.gravitydevelopment.anticheat.util.Permission;
import org.bukkit.command.CommandSender;

public class CommandCheck extends CommandBase {

    private static final String NAME = "AntiCheat Check Management";
    private static final String COMMAND = "check";
    private static final String USAGE = "anticheat check [check] [on/off]";
    private static final Permission PERMISSION = Permission.SYSTEM_CHECK;
    private static final String[] HELP = new String[3];

    static {
        HELP[0] = GRAY + "Use: " + AQUA + "/anticheat check [check] on" + GRAY + " to enable a check";
        HELP[1] = GRAY + "Use: " + AQUA + "/anticheat check [check] off" + GRAY + " to disable a check";
        StringBuilder builder = new StringBuilder();
        builder.append(GRAY + "Checks: ");
        for (int i=0;i<CheckType.values().length;i++) {
            builder.append(CheckType.values()[i]);
            if (i<CheckType.values().length-1) {
                builder.append(", ");
            }
        }
        HELP[2] = builder.toString();
    }

    public CommandCheck() {
        super(NAME, COMMAND, USAGE, HELP, PERMISSION);
    }

    @Override
    protected void execute(CommandSender cs, String[] args) {
        if (args.length == 2) {
            for (CheckType type : CheckType.values()) {
                if (type.toString().equalsIgnoreCase(args[0]) || type.toString().replaceAll("_", "").equalsIgnoreCase(args[0])) {
                    boolean value = CHECK_MANAGER.isActive(type);

                    boolean newValue;
                    if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("enable")) {
                        newValue = true;
                    } else if (args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("disable")) {
                        newValue = false;
                    } else {
                        sendHelp(cs);
                        return;
                    }

                    String strValue = (newValue ? " activated" : " deactivated");
                    if (value == newValue) {
                        cs.sendMessage(GREEN + type.toString() + " is already" + strValue + "!");
                    } else {
                        if (newValue) {
                            CHECK_MANAGER.activateCheck(type, cs.getName());
                        } else {
                            CHECK_MANAGER.deactivateCheck(type, cs.getName());
                        }
                        cs.sendMessage(GREEN + type.toString() + strValue + ".");
                    }
                    return;
                }
            }
        }
        sendHelp(cs);
    }
}

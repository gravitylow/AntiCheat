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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FileFormatter extends Formatter
{
    private static final DateFormat FORMAT = new SimpleDateFormat("h:mm:ss");
    private static final String LINE_SEP = System.getProperty("line.separator");

    @Override
    public String format(LogRecord record)
    {
        StringBuilder output = new StringBuilder().append("[").append(record.getLevel()).append('|').append(FORMAT.format(new Date(record.getMillis()))).append("]: ").append(record.getMessage()).append(' ').append(LINE_SEP);
        return output.toString();
    }
}

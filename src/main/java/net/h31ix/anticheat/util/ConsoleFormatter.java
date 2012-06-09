package net.h31ix.anticheat.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
 
public class ConsoleFormatter extends Formatter {

    @Override
    public String format(LogRecord record) 
    {
        StringBuilder output = new StringBuilder().append(record.getMessage()).append(' ');
        return output.toString();		
    }
}
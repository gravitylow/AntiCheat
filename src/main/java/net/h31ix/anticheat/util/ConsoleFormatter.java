package net.h31ix.anticheat.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
 
public class ConsoleFormatter extends Formatter {
	
    private static final DateFormat format = new SimpleDateFormat("h:mm:ss");
    private static final String lineSep = System.getProperty("line.separator");

    @Override
    public String format(LogRecord record) {
        StringBuilder output = new StringBuilder()
                .append(record.getMessage()).append(' ');
        return output.toString();		
    }
 
}
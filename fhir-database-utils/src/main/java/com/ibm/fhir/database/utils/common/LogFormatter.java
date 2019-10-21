/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A slightly nicer formatter for Java util logging output
 * Modified to write out logs to a specified file
 *
 */
public class LogFormatter extends Formatter {

    private static final String ISO_TIME = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * The standard formatter uses a horrible multiline approach. A single line
     * message is far more readable (and easier to post-process). Java 7
     * supports a (completely cryptic) format string, but we cook our own
     * food here...
     */
    @Override
    public String format(LogRecord lr) {
        // Don't make this static...it's not thread-safe
        SimpleDateFormat df = new SimpleDateFormat(ISO_TIME);
    
        // Trim the class name to the last couple of names
        String className = lr.getSourceClassName();
        // Use a max of 30 characters for our className
        if (className.length() > 30) {
            className = className.substring(className.length() - 30);
        }
        
        String thrown = "";
        if (lr.getThrown() != null) {
            thrown = lr.getThrown().getClass().getName();            
        }
    
        StringBuilder result = new StringBuilder();
        result.append(String.format("%1$s %7$08x %4$7s %2$30s %5$s%n", 
                df.format(new Date(lr.getMillis())), 
                className, 
                lr.getLoggerName(), 
                lr.getLevel(), 
                formatMessage(lr), 
                thrown,
                lr.getThreadID()));

        // Add the stack trace if necessary
        if (lr.getThrown() != null) {
            Throwable t = lr.getThrown();
            String msg = t.getMessage();
            if (msg != null) {
                result.append(System.lineSeparator());
                result.append(msg);
            }
            
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            result.append(System.lineSeparator());
            result.append(sw);
        }
        
        return result.toString();
    }

    /**
     * Initialize logging by attaching this formatter to the root logger. If logs don't need to be written out, pass a null filename
     */
    public static void init(String filename) {
        Formatter halosFormatter = new LogFormatter();
        Logger rootLogger = Logger.getLogger("");

        if (filename != null || filename != "") {
            try {
                FileHandler fh = new FileHandler(filename);
                fh.setFormatter(halosFormatter);
                rootLogger.addHandler(fh);
            } catch (SecurityException | IOException e) {
                throw new IllegalStateException("Error while initializing log output file. ", e);
            }
            
            // set up logging
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler h: handlers) {
                h.setFormatter(halosFormatter);
            }
        }
    }
}

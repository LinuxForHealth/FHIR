/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.LogFormatter;
import com.ibm.fhir.task.api.ITaskGroup;

/**
 * Schema Utility
 */
public class SchemaUtil {
    private static final Logger logger = Logger.getLogger(SchemaUtil.class.getName());

    // Random generator for new tenant keys and salts
    private static final SecureRandom random = new SecureRandom();

    private SchemaUtil() {
        // No Operation
    }

    public static String mapToId(ITaskGroup group) {
        return group.getTaskId();
    }

    /**
     * Print the classpath so we can see what on earth is going on with connecting
     * to DB2 using an api key.
     */
    public static void logClasspath() {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("CLASSPATH: ");
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            if (cl instanceof URLClassLoader) {
                URL[] classpath = ((URLClassLoader) cl).getURLs();
                for (URL u : classpath) {
                    logger.fine("  " + u.getFile());
                }
            }
            logger.fine("[java.class.path] -> " + System.getProperty("java.class.path"));
        }
    }

    /**
     * Load the DB2 driver class
     */
    public static void loadDriver(IDatabaseTranslator translator) {
        try {
            Class.forName(translator.getDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Set up the logger using the log.dir system property
     */
    public static void configureLogger() {
        final String logDirectory = System.getProperty("log.dir");
        configureLogger(logDirectory);
    }

    /**
     * Configure the logger to use the given directory.
     * 
     * @param logDir
     */
    public static void configureLogger(final String logDir) {
        String tmpLogDir = ".";

        final String TARGET = "./target/";
        File tgt = new File(TARGET);
        if (tgt.exists()) {
            tmpLogDir = TARGET;
        }

        if (logDir != null && !logDir.isEmpty()) {
            tmpLogDir = logDir;
        }
        File f = new File(tmpLogDir, "fhirschema.log");
        LogFormatter.init(f.getPath());
    }

    /**
     * Generate a random 32 byte value encoded as a Base64 string (44 characters).
     * 
     * @return
     */
    public static String getRandomKey() {
        byte[] buffer = new byte[32];
        random.nextBytes(buffer);
        Encoder enc = Base64.getEncoder();
        return enc.encodeToString(buffer);
    }
}
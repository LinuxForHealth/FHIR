/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class FHIRTermGraphUtil {
    private final static Set<String> RESERVED_WORDS = new HashSet<>(Arrays.asList("key", "vertex", "edge", "element", "property", "label"));

    private FHIRTermGraphUtil() { }

    public static boolean isReservedWord(String s) {
        return RESERVED_WORDS.contains(s.toLowerCase());
    }

    public static void setRootLoggerLevel(Level level) {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(level);
    }
}

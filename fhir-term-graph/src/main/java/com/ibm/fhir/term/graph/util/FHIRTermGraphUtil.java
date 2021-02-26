/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.util;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class FHIRTermGraphUtil {
    private final static Set<String> RESERVED_WORDS = new HashSet<>(Arrays.asList("key", "vertex", "edge", "element", "property", "label"));

    private FHIRTermGraphUtil() { }

    public static boolean isReservedWord(String s) {
        return RESERVED_WORDS.contains(s.toLowerCase());
    }

    public static String normalize(String value) {
        if (value != null) {
            return Normalizer.normalize(value, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        }
        return null;
    }

    public static void setRootLoggerLevel(Level level) {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(level);
    }

    public static String toLabel(String typeName) {
        List<String> tokens = Arrays.asList(typeName.split(" |_|-"));
        String label = tokens.stream()
                .map(token -> token.substring(0, 1).toUpperCase() + token.substring(1))
                .collect(Collectors.joining(""));
        label = label.substring(0, 1).toLowerCase() + label.substring(1);
        return isReservedWord(label) ? label + "_" : label;
    }
}

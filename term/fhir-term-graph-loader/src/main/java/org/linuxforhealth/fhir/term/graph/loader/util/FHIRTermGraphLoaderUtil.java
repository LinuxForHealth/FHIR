/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.graph.loader.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import org.linuxforhealth.fhir.term.graph.FHIRTermGraph;

public class FHIRTermGraphLoaderUtil {
    private final static Set<String> RESERVED_WORDS = new HashSet<>(Arrays.asList("key", "vertex", "edge", "element", "property", "label"));

    private FHIRTermGraphLoaderUtil() { }

    public static boolean isReservedWord(String s) {
        return RESERVED_WORDS.contains(s.toLowerCase());
    }

    public static String toLabel(String typeName) {
        List<String> tokens = Arrays.asList(typeName.split(" - | |_|-"));
        String label = tokens.stream()
                .map(token -> token.substring(0, 1).toUpperCase() + token.substring(1))
                .collect(Collectors.joining(""));
        label = label.substring(0, 1).toLowerCase() + label.substring(1);
        if ("isA".equals(label)) {
            // for consistency between SNOMED-CT and UMLS
            return FHIRTermGraph.IS_A;
        }
        return isReservedWord(label) ? label + "_" : label;
    }

    public static Map<String, String> toMap(CommandLine commandLine) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Option option : commandLine.getOptions()) {
            map.put(option.getOpt(), option.getValue());
        }
        return Collections.unmodifiableMap(map);
    }
}

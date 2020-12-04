/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FHIRTermGraphUtil {
    private final static Set<String> RESERVED_WORDS = new HashSet<>(Arrays.asList("key", "vertex", "edge", "element", "property", "label"));

    private FHIRTermGraphUtil() { }

    public static boolean isReservedWord(String s) {
        return RESERVED_WORDS.contains(s.toLowerCase());
    }
}

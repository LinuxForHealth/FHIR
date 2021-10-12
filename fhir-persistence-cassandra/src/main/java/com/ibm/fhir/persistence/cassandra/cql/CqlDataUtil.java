/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.cql;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility functions for dealing with data read from and written to Cassandra
 */
public class CqlDataUtil {
    private static final Logger logger = Logger.getLogger(CqlDataUtil.class.getName());
    private static final String NAME_PATTERN_RGX = "[a-zA-Z_]\\w*$";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_PATTERN_RGX);
    
    /**
     * Asserts that the given id is safe and will not escape a Cql statement
     * In this case, we can simply assert that it's a valid FHIR identifier
     * string
     * @param id
     */
    public static void safeId(String id) {

        if (id == null || !isValidName(id)) {
            logger.log(Level.SEVERE, "Invalid identifier: " + id);
            throw new IllegalArgumentException("Invalid identifier");
        }
    }

    /**
     * Check that the name is a valid object name for Cassandra.
     * @param name
     * @return
     */
    public static boolean isValidName(String name) {
        Matcher m = NAME_PATTERN.matcher(name);
        return m.matches() && name.length() <= 128;
    }
}
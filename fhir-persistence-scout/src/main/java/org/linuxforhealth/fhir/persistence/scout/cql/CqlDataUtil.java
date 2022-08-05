/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.scout.cql;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility functions for dealing with data read from and written to Cassandra
 */
public class CqlDataUtil {
    private static final Logger logger = Logger.getLogger(CqlDataUtil.class.getName());
    
    /**
     * Asserts that the given id is safe and will not escape a Cql statement
     * In this case, we can simply assert that it's a valid FHIR identifier
     * string
     * @param id
     */
    public static void safeId(String id) {
        // TODO implement the check
        logger.log(Level.SEVERE, "Invalid identifier: " + id);
        throw new IllegalArgumentException("Invalid identifier");
    }
}

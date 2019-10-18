/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.helper;

import java.util.logging.Logger;

import javax.naming.InitialContext;

/**
 * Utility functions used by fhir-server.
 */
public class FHIRServerUtils {
    private static final Logger log = Logger.getLogger(FHIRServerUtils.class.getName());

    /**
     * Retrieves the specified JNDI entry and interprets it as a value of type "T".
     * @param jndiName the name of the JNDI entry to search for
     * @param defaultValue the defaultValue to be returned if the JNDI entry isn't found
     */
    @SuppressWarnings("unchecked")
    public static <T> T getJNDIValue(String jndiName, T defaultValue) {
        T result = defaultValue;
        try {
            InitialContext ctx = new InitialContext();
            T jndiValue = (T) ctx.lookup(jndiName);
            if (jndiValue != null ) {
                result = jndiValue;
            }
        } catch (Throwable t) {
            // Ignore any exceptions while looking up the JNDI entry.
            log.finer("Caught exception while looking up JNDI entry " + jndiName + ": " + t);
        }
        return result;
    }
}

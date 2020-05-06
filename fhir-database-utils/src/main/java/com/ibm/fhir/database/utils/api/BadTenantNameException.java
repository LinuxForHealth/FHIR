/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

/**
 * BadTenantNameException
 */
public class BadTenantNameException extends DataAccessException {
    // All exceptions are serializable
    private static final long serialVersionUID = -3385697603070015558L;

    /**
     * Public constructor
     * @param msg
     */
    public BadTenantNameException(String msg) {
        super(msg);
    }

    /**
     * Public constructor
     * @param msg
     * @param t
     */
    public BadTenantNameException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Public constructor
     * @param t
     */
    public BadTenantNameException(Throwable t) {
        super(t);
    }
}

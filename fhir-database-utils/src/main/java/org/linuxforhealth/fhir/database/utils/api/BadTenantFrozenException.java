/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.api;

/**
 * BadTenantFrozenException
 */
public class BadTenantFrozenException extends DataAccessException {
    // All exceptions are serializable
    private static final long serialVersionUID = -3385697603070015558L;

    /**
     * Public constructor
     * @param msg
     */
    public BadTenantFrozenException(String msg) {
        super(msg);
    }

    /**
     * Public constructor
     * @param msg
     * @param t
     */
    public BadTenantFrozenException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Public constructor
     * @param t
     */
    public BadTenantFrozenException(Throwable t) {
        super(t);
    }
}

/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

/**
 * DatabaseNotReadyException
 */
public class DatabaseNotReadyException extends DataAccessException {
    // All exceptions are serializable
    private static final long serialVersionUID = -3385694603070015558L;

    /**
     * Public constructor
     * @param x
     */
    public DatabaseNotReadyException(Exception x) {
        super(x);
    }

    /**
     * Public constructor
     * @param msg
     * @param t
     */
    public DatabaseNotReadyException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Public constructor
     * @param t
     */
    public DatabaseNotReadyException(Throwable t) {
        super(t);
    }
}
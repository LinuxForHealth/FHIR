/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

/**
 * Translation of a SQLException representing an issue related to database
 * connectivity. This is potentially an ephemeral issue, so the business
 * logic may decide to retry (or return an error telling the client to
 * retry instead of just a generic 500 server error type response).
 *
 */
public class ConnectionException extends DataAccessException {

    // Generated serial number
    private static final long serialVersionUID = 2602426230067692801L;

    /**
     * Public constructor
     * @param t
     */
    public ConnectionException(Throwable t) {
        super(t);
    }
    
    /**
     * Public constructor
     * @param msg
     * @param t
     */
    public ConnectionException(String msg, Throwable t) {
        super(msg, t);
    }

}

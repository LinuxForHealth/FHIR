/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;

import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * Command pattern for performing actions on a JDBC connection
 * (e.g. to configure it for use)
 */
public interface Action {

    /**
     * Perform this action on the given connection
     * @param flavor describes the type of database associated with the given connection
     * @param c the connection to perform the action on
     */
    public void performOn(FHIRDbFlavor flavor, Connection c) throws FHIRPersistenceDBConnectException;
}

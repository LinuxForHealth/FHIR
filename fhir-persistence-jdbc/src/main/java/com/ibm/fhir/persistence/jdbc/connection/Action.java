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
     * @param c
     */
    public void performOn(Connection c) throws FHIRPersistenceDBConnectException;
}

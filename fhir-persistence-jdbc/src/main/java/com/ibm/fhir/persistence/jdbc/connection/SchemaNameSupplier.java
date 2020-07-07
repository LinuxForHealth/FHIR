/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;

import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * Provides the schema name for the current request context
 */
@FunctionalInterface
public interface SchemaNameSupplier {

    /**
     * Get the schema name for the current request context
     * @param c the connection for which we want to obtain the schema name
     * @return the main schema name to use for the given connection
     * @throws FHIRPersistenceDBConnectException
     */
    public String getSchemaForRequestContext(Connection c) throws FHIRPersistenceDBConnectException;
}

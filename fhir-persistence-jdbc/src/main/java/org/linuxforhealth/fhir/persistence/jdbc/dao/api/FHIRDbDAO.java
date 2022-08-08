/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.api;

import java.sql.Connection;

import org.linuxforhealth.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import org.linuxforhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * This is a root interface for child Data Access Object interfaces.
 */
public interface FHIRDbDAO {

    /**
     * Obtains a database connection. Connection is configured and ready to use.
     * If multi-tenant, the tenant session variable will have been set.
     * 
     * @return Connection - A connection to the FHIR database.
     * @throws FHIRPersistenceDBConnectException
     */
    Connection getConnection() throws FHIRPersistenceDBConnectException;

    /**
     * Get the database flavor, which describes the database type and
     * its capabilities (e.g. is it multi-tenant?)
     * @return
     */
    public FHIRDbFlavor getFlavor();
}
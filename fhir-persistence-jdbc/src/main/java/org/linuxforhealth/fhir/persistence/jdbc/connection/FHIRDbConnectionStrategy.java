/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.connection;

import java.sql.Connection;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.FHIRDbDAOImpl;
import org.linuxforhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
/**
 * Abstraction used to obtain JDBC connections. The database being connected
 * is determined by the datasource currently referenced by the {@link FHIRRequestContext}
 * (which is a ThreadLocal thing, and not obvious).
 *
 * @implNote Refactor of the getConnection logic from {@link FHIRDbDAOImpl}. This
 * isolates connection logic from the DAO implementations, promoting separation of
 * concerns, and makes it possible to use different strategies in the future,
 * without having to disrupt the (complex) DAO code again.
 */
public interface FHIRDbConnectionStrategy {

    /**
     * Get a connection to the desired data source identified by the current {@link FHIRRequestContext}
     * @return a {@link Connection}. Never null.
     */
    public Connection getConnection() throws FHIRPersistenceDBConnectException;

    /**
     * Get the flavor of the database we are working with to reveal its capabilities
     * @return the datastore/source flavor from the FHIR configuration
     * @throws FHIRPersistenceDataAccessException if there is an issue with the configuration
     */
    public FHIRDbFlavor getFlavor() throws FHIRPersistenceDataAccessException;

    /**
     * Append the given hint to FHIR search queries
     * @return
     */
    public QueryHints getQueryHints();

    /**
     * Apply any optimizer options configured to improve search query performance
     * See issue-1911 for details.
     * @param c the connection to be configured
     * @param isCompartment true if the query is a compartment-based search
     */
    public void applySearchOptimizerOptions(Connection c, boolean isCompartment);
}
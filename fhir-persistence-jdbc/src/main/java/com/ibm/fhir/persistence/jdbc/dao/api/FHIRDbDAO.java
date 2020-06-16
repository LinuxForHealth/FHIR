/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;

import java.sql.Connection;

import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * This is a root interface for child Data Access Object interfaces.
 */
public interface FHIRDbDAO {
    public static final String FHIRDB_JNDI_NAME_DEFAULT = "jdbc/fhirProxyDataSource";
    public static final String PROPERTY_DB_DRIVER = "dbDriverName";
    public static final String PROPERTY_DB_URL = "dbUrl";
    public static final String PROPERTY_DB2_USER = "user";
    public static final String PROPERTY_DB2_PSWD = "password";

    /**
     * Obtains a database connection. Connection is configured and ready to use. Its
     * schema will be set to the configured FHIR data schema (usually 'FHIRDATA') and
     * if multi-tenant, the tenant property will have been set.
     * 
     * @return Connection - A connection to the FHIR database.
     * @throws FHIRPersistenceDBConnectException
     */
    Connection getConnection() throws FHIRPersistenceDBConnectException;

    /**
     * @return true if this DAO is connected to a DB2 database.
     * @throws Exception
     */
    boolean isDb2Database();
}
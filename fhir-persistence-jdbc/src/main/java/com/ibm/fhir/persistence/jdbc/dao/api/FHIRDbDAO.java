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
     * Acquires and returns a JDBC database connection to the FHIR database.
     * If no DB Properties are available, an attempt is made to acquire the connection via a Datasource obtained via JNDI.
     * If DB Properties are present, those properties are used to build the Connection. 
     * @return Connection - A connection to the FHIR database.
     * @throws FHIRPersistenceDBConnectException
     */
    Connection getConnection() throws FHIRPersistenceDBConnectException;
    
    /**
     * Returns a previously set externally managed DB connection, used by the DAO for all DB activity.
     * @return Connection
     */
    Connection getExternalConnection();
    
    /**
     * Sets an externally managed DB connection, used by the DAO for all DB activity.
     * @param connection
     */
    void setExternalConnection(Connection connection);
    
    /**
     * 
     * @return true if this DAO is connected to a DB2 database.
     * @throws Exception
     */
    boolean isDb2Database() throws Exception;

}

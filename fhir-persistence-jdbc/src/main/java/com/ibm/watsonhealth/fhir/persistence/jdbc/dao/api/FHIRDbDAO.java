/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api;

import java.sql.Connection;

import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * This is a root interface for child Data Access Object interfaces. 
 * @author markd
 *
 */
public interface FHIRDbDAO {

	String FHIRDB_JNDI_NAME = "jdbc/fhirDB";
	String PROPERTY_DB_DRIVER = "dbDriverName";
	String PROPERTY_DB_URL = "dbUrl";
	String PROPERTY_DB2_USER = "user";
	String PROPERTY_DB2_PSWD = "password";

	/**
	 * Acquires and returns a JDBC database connection to the FHIR database.
	 * If no DB Properties are available, an attempt is made to acquire the connection via a Datasource obtained via JNDI.
	 * If DB Properties are present, those properties are used to build the Connection. 
	 * @return Connection - A connection to the FHIR database.
	 * @throws FHIRPersistenceDBConnectException
	 */
	Connection getConnection() throws FHIRPersistenceDBConnectException;

}

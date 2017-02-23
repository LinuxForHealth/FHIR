/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.test.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl.FHIRDbDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

/**
 * This utility class initializes and bootstraps a FHIR Derby database for unit testing. 
 * If an existing database is found in the target path, it is reused. If not, a new database is defined and the appropriate DDL for
 * table and index creation is run using Liquibase.
 * 
 * It's intended that this class be consumed by testNg tests in the fhir-persistence-jdbc project.
 * @author markd
 *
 */
public class DerbyInitializer {
	
	private static final String LIQUIBASE_CHANGE_LOG_PATH = "../fhir-schemaddl/src/test/resources/liquibase/derby/ddl/changelog.xml";
	
	private boolean newDbCreated = false;
	private Properties dbProps;
	
	/**
	 * Main method to facilitate standalone testing of this class.
	 * @param args
	 */
	public static void main(String[] args) {
		DerbyInitializer initializer = new DerbyInitializer();
		try {
			initializer.bootstrapDb();
			
		} 
		catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructs a new DerbyInitializer using default database properties.
	 */
	public DerbyInitializer() {
		super();
		this.dbProps = new Properties();
		this.dbProps.setProperty(FHIRDbDAO.PROPERTY_DB_DRIVER, "org.apache.derby.jdbc.EmbeddedDriver");
		this.dbProps.setProperty(FHIRDbDAO.PROPERTY_DB_URL, "jdbc:derby:target/fhirdb");
	}
	
	/**
	 * Constructs a new DerbyInitializer using the passed database properties.
	 */
	public DerbyInitializer(Properties props) {
		super();
		this.dbProps = props;
	}
	
	/**
	 * Establishes a connection to /target/fhirdb. Creates the database if necessary complete with tables indexes.
	 * @throws FHIRPersistenceDBConnectException
	 * @throws LiquibaseException
	 */
	public void bootstrapDb() throws FHIRPersistenceDBConnectException, LiquibaseException {
		
		Connection connection = this.establishDb();
		if (this.isNewDbCreated()) {
			this.runDDL(connection);
		}
	}
	
	/**
	 * Establishes a connection to a Derby fhirdb, located in the project's /target/fhirdb directory.
	 * If the database already exists, a connection is returned to it. If not, a new Derby fhirdb is created
	 * and populated with the appropriate tables. 
	 * @return Connection - A connection to the project's /target/fhirdb
	 * @throws FHIRPersistenceDBConnectException
	 */
	@SuppressWarnings("rawtypes")
	private Connection establishDb() throws FHIRPersistenceDBConnectException  {
		
		Connection connection = null;
		SQLException sqlEx;
		
		FHIRDbDAO dao = new FHIRDbDAO(this.dbProps);
		
		try {
			connection = dao.getConnection();
		} 
		catch (FHIRPersistenceDBConnectException e) {
			if (e.getCause() != null && e.getCause() instanceof SQLException) {
				sqlEx = (SQLException) e.getCause();
				// XJ004 means database not found
				if("XJ004".equals(sqlEx.getSQLState())) {
					this.dbProps.setProperty(FHIRDbDAO.PROPERTY_DB_URL, "jdbc:derby:target/fhirdb;create=true");
					dao = new FHIRDbDAO(this.dbProps);
					connection = dao.getConnection();
					this.setNewDbCreated(true);
				}
			}
			else {
				throw e;
			}
		}
		return connection;
	}
	
	/**
	 * Uses Liquibase to run the required DDL for a newly created Derby database.
	 * The path to the Liquibase change log is defined by constant LIQUIBASE_CHANGE_LOG_PATH.
	 * @param dbConn
	 * @throws LiquibaseException
	 */
	private void runDDL(Connection dbConn) throws LiquibaseException {
		
		Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dbConn));
		
		Liquibase liquibase = new Liquibase(LIQUIBASE_CHANGE_LOG_PATH, new FileSystemResourceAccessor(), database);
		
		liquibase.update((Contexts)null);
	}

	private boolean isNewDbCreated() {
		return newDbCreated;
	}

	private void setNewDbCreated(boolean newDbCreated) {
		this.newDbCreated = newDbCreated;
	}

}

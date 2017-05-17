/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.FHIRDbDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBCleanupException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This class is a root Data Access Object for managing JDBC access to the FHIR database. It contains common functions for managing connections, closing used 
 * JDBC resources, and running database queries.
 * @author markd
 *
 */
public class FHIRDbDAOBasicImpl<T> implements FHIRDbDAO {
		
	private static final Logger log = Logger.getLogger(FHIRDbDAOBasicImpl.class.getName());
	private static final String CLASSNAME = FHIRDbDAOBasicImpl.class.getName(); 
	
	private String datasourceJndiName = null;
	private Properties dbProps = null;
	private DataSource fhirDb = null;
	private Connection externalConnection = null;
	private static boolean dbDriverLoaded = false;

	/**
	 * Constructs a DAO instance suitable for acquiring DB connections via JNDI from the app server.
	 */
	public FHIRDbDAOBasicImpl() {
		super();
	}
	
	/**
	 * Constructs a DAO instance suitable for acquiring connections based on the passed database type specific properties.
	 * @param dbProperties
	 */
	public FHIRDbDAOBasicImpl(Properties dbProperties) {
		this();
		this.setDbProps(dbProperties);
	}
	
	/**
	 * Constructs a DAO using the passed externally managed database connection.
	 * The connection used by this instance for all DB operations will be the passed connection.
	 * @param Connection - A database connection that will be managed by the caller.
	 */
	public FHIRDbDAOBasicImpl(Connection conn) {
		this();
		this.setExternalConnection(conn);
	}



	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl.FHIRDbDAO#getConnection()
	 */
	@Override
    public Connection getConnection() throws FHIRPersistenceDBConnectException {
        final String METHODNAME = "getConnection";
        log.entering(CLASSNAME, METHODNAME);
        try {
            Connection connection = null;
            String dbDriverName = null;
            String dbUrl;

            if (this.getExternalConnection() != null) {
            	connection = this.getExternalConnection();
            }
            else if (this.getDbProps() == null) {
                try {
                    connection = this.getFhirDatasource().getConnection();
                } catch (Throwable e) {
                    throw new FHIRPersistenceDBConnectException("Failure acquiring Connection for datasource: " + getDataSourceJndiName(), e);
                }
            } else {
                if (!dbDriverLoaded) {
                    try {
                        dbDriverName = this.getDbProps().getProperty(PROPERTY_DB_DRIVER);
                        Class.forName(dbDriverName);
                        dbDriverLoaded = true;
                    } catch (ClassNotFoundException e) {
                        throw new FHIRPersistenceDBConnectException("Failed to load driver: " + dbDriverName, e);
                    }
                }

                dbUrl = this.getDbProps().getProperty(PROPERTY_DB_URL);
                try {
                    connection = DriverManager.getConnection(dbUrl, this.getDbProps());
                } catch (Throwable e) {
                    throw new FHIRPersistenceDBConnectException("Failed to acquire DB connection. dbUrl=" + dbUrl, e);
                }
            }
            return connection;
        } catch (FHIRPersistenceDBConnectException e) {
            throw e;
        } catch (Throwable t) {
            throw new FHIRPersistenceDBConnectException("An unexpected error occurred while connecting to the database.", t);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);

        }
    }
	
	/**
	 * Retrieves the datasource JNDI name to be used from the fhir server configuration.
	 * @return the datasource JNDI name
	 * @throws Exception
	 */
	private String getDataSourceJndiName() throws Exception {
	    if (datasourceJndiName == null) {
	        datasourceJndiName = FHIRConfiguration.getInstance().loadConfiguration()
	                .getStringProperty(FHIRConfiguration.PROPERTY_JDBC_DATASOURCE_JNDINAME, FHIRDbDAO.FHIRDB_JNDI_NAME_DEFAULT);
	        log.fine("Using datasource JNDI name: " + datasourceJndiName);
	    }
	    return datasourceJndiName;
	}

	/**
	 * Looks up and returns a Datasource JDBC object representing the FHIR database via JNDI.
	 * @return
	 * @throws Exception 
	 */
    private DataSource getFhirDatasource() throws Exception {
        final String METHODNAME = "getFhirDb";
        log.entering(CLASSNAME, METHODNAME);
        try {
            InitialContext ctxt;

            if (this.fhirDb == null) {
                try {
                    ctxt = new InitialContext();
                    this.fhirDb = (DataSource) ctxt.lookup(getDataSourceJndiName());
                } catch (Throwable e) {
                    throw new FHIRPersistenceDBConnectException("Failure acquiring Datasource for " + getDataSourceJndiName(), e);
                }
            }
            return this.fhirDb;
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }
	
	/**
	 * Closes the passed PreparedStatement and Connection objects.
	 * @param stmt
	 * @param connection
	 */
	protected void cleanup(PreparedStatement stmt, Connection connection)  {
		final String METHODNAME = "cleanup(PreparedStatement, Connection)";
		log.entering(CLASSNAME, METHODNAME);
		
		FHIRPersistenceDBCleanupException ce;
		
		if (stmt != null) {
			try {
				stmt.close();
			} 
			catch (Throwable e) {
				ce =  new FHIRPersistenceDBCleanupException("Failure closing PreparedStatement.",e);
				log.log(Level.SEVERE, ce.getMessage(), ce);
			}
		}
		if(connection != null && this.getExternalConnection() == null) {
			try {
				connection.close();
			} 
			catch (Throwable e) {
				ce =  new FHIRPersistenceDBCleanupException("Failure closing Connection.",e);
				log.log(Level.SEVERE, ce.getMessage(), ce);
			}
		}
		log.exiting(CLASSNAME, METHODNAME);
	}
	
	/**
	 * Closes the passed ResultSet, PreparedStatement, and Connection objects.
	 * @param resultSet
	 * @param stmt
	 * @param connection
	 */
	protected void cleanup(ResultSet resultSet, PreparedStatement stmt, Connection connection)  {
		final String METHODNAME = "cleanup(PreparedStatement, Connection)";
		log.entering(CLASSNAME, METHODNAME);
		
		FHIRPersistenceDBCleanupException ce;
		
		if (resultSet != null) {
			try {
				resultSet.close();
			} 
			catch (Throwable e) {
				ce =  new FHIRPersistenceDBCleanupException("Failure closing ResultSet.",e);
				log.log(Level.SEVERE, ce.getMessage(), ce);
			}
		}
		this.cleanup(stmt, connection);
		log.exiting(CLASSNAME, METHODNAME);
	}
	
	/**
	 * Creates and executes a PreparedStatement using the passed parameters that returns a collection of FHIR Data Transfer Objects of type T.
	 * @param sql - The SQL template to execute.
	 * @param searchArgs - An array of arguments to be substituted into the SQL template.
	 * @return List<T> - A List of FHIR Data Transfer Objects resulting from the executed query.
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException 
	 */
	protected List<T> runQuery(String sql, Object... searchArgs) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "runQuery";
		log.entering(CLASSNAME, METHODNAME);
		
		List<T> fhirObjects = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String errMsg;
						
		try {
			connection = this.getConnection();
			stmt = connection.prepareStatement(sql);
			// Inject arguments into the prepared stmt.
			for (int i = 0; i <searchArgs.length;  i++) {
				stmt.setObject(i+1, searchArgs[i]);
			}
			resultSet = stmt.executeQuery();
			// Transform the resultSet into a collection of Data Transfer Objects
			fhirObjects = this.createDTOs(resultSet);
			log.fine("Sucessfully retrieved FHIR objects. SQL=" + sql + "  searchArgs=" + searchArgs);
		} 
		catch(FHIRPersistenceException e) {
			throw e;
		}
		catch (Throwable e) {
			errMsg = "Failure retrieving FHIR objects. SQL=" + sql + "  searchArgs=" + searchArgs;
			throw new FHIRPersistenceDataAccessException(errMsg,e);
		} 
		finally {
			this.cleanup(resultSet, stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
		
		return fhirObjects;
	}
	
	/**
	 * Creates and executes a PreparedStatement for the passed sql containing a 'SELECT COUNT...'. 
	 * The count value is extracted from the ResultSet and returned as an int.
	 * @param sql - The SQL SELECT COUNT template to execute.
	 * @param searchArgs - An array of arguments to be substituted into the SQL template.
	 * @return int - The count of results returned by the SQL query.
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException 
	 */
	protected int runCountQuery(String sql, Object... searchArgs) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "runCountQuery";
		log.entering(CLASSNAME, METHODNAME);
		
		int rowCount = 0;
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String errMsg = "Failure retrieving count. SQL=" + sql + "  searchArgs=" + searchArgs;;
						
		try {
			connection = this.getConnection();
			stmt = connection.prepareStatement(sql);
			// Inject arguments into the prepared stmt.
			for (int i = 0; i <searchArgs.length;  i++) {
				stmt.setObject(i+1, searchArgs[i]);
			}
			resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				rowCount = resultSet.getInt(1);
				log.fine("Sucessfully retrieved count. SQL=" + sql + "  searchArgs=" + searchArgs);
			}
			else {
				throw new FHIRPersistenceDataAccessException(errMsg);
			}
		} 
		catch(FHIRPersistenceException e) {
			throw e;
		}
		catch (Throwable e) {
			throw new FHIRPersistenceDataAccessException(errMsg,e);
		} 
		finally {
			this.cleanup(resultSet, stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
		
		return rowCount;
	}
	
	/**
	 * An method for creating a collection of Data Transfer Objects of type T from the contents of the passed ResultSet.
	 * @param resultSet A ResultSet containing FHIR persistent object data.
	 * @return List<T> - A collection of FHIR Data Transfer objects of the same type.
	 * @throws FHIRPersistenceDataAccessException
	 */
	protected List<T> createDTOs(ResultSet resultSet) throws FHIRPersistenceDataAccessException {
		final String METHODNAME = "runQuery";
		log.entering(CLASSNAME, METHODNAME);
		
		T dto;
		List<T> dtoList = new ArrayList<T>();
		
		try {
			while(resultSet.next()) {
				dto = this.createDTO(resultSet);
				if (dto != null) {
					dtoList.add(this.createDTO(resultSet));
				}
			}
		} 
		catch (Throwable e) {
			throw new FHIRPersistenceDataAccessException("Failure creating DTOs.", e);
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
		return dtoList;
	}
	
	/**
	 * A method for creating a Data Transfer Object of type T from the contents of the passed ResultSet.
	 * @param resultSet A ResultSet containing FHIR persistent object data.
	 * @return T - An instance of type T, which is a FHIR Data Transfer Object.
	 * @throws FHIRPersistenceDataAccessException
	 */
	protected T createDTO(ResultSet resultSet) throws FHIRPersistenceDataAccessException {
		// Can be overridden by subclasses that need to return DTOs.
		return null;
	}

	private Properties getDbProps() {
		return dbProps;
	}

	private void setDbProps(Properties dbProps) {
		this.dbProps = dbProps;
	}

	public Connection getExternalConnection() {
		return externalConnection;
	}

	public void setExternalConnection(Connection externalConnection) {
		this.externalConnection = externalConnection;
	}

	@Override
	public boolean isDb2Database() throws FHIRPersistenceDBConnectException, SQLException {
				
		String dbUrl;
		
		dbUrl = this.getConnection().getMetaData().getURL();
		dbUrl = dbUrl.toLowerCase();
		return dbUrl.contains("db2");
	}

}

/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl;

import java.io.ByteArrayInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ResourceNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This Data Access Object extends the "basic" implementation to provide functionality specific to the "normalized"
 * relational schema.
 * @author markd
 *
 */
public class ResourceDAONormalizedImpl extends ResourceDAOBasicImpl implements ResourceNormalizedDAO {
	private static final Logger log = Logger.getLogger(ResourceDAONormalizedImpl.class.getName());
	private static final String CLASSNAME = ResourceDAONormalizedImpl.class.getName(); 
	
	
	private static final String SQL_READ = "SELECT RESOURCE_ID, LOGICAL_RESOURCE_ID, VERSION_ID, LAST_UPDATED, IS_DELETED, DATA FROM %s_RESOURCES R WHERE " +
										   "R.LOGICAL_RESOURCE_ID = ? AND R.VERSION_ID = " +
										   "(SELECT MAX(R2.VERSION_ID) FROM %s_RESOURCES R2 WHERE R2.LOGICAL_RESOURCE_ID = ?)";
	
	private static final String SQL_VERSION_READ = "SELECT RESOURCE_ID, LOGICAL_RESOURCE_ID, VERSION_ID, LAST_UPDATED, IS_DELETED, DATA FROM %s_RESOURCES R WHERE " +
										   		   "R.LOGICAL_RESOURCE_ID = ? AND R.VERSION_ID = ?";
	
	private static final  String SQL_INSERT = "CALL %s_add_resource(?, ?, ?, ?, ?, ?)";
	

	// The following History related SQL strings are used by the history related methods in the superclass.
	private static final String SQL_HISTORY = "SELECT RESOURCE_ID, LOGICAL_RESOURCE_ID, VERSION_ID, LAST_UPDATED, IS_DELETED, DATA FROM %s_RESOURCES R WHERE R.LOGICAL_RESOURCE_ID = ? "
											   + "ORDER BY R.VERSION_ID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
	private static final String SQL_HISTORY_COUNT = "SELECT COUNT(*) FROM %s_RESOURCES R WHERE R.LOGICAL_RESOURCE_ID = ?";
	private static final String SQL_HISTORY_FROM_DATETIME = "SELECT RESOURCE_ID, LOGICAL_RESOURCE_ID, VERSION_ID, LAST_UPDATED, IS_DELETED, DATA FROM %s_RESOURCES R WHERE R.LOGICAL_RESOURCE_ID = ? AND R.LAST_UPDATED >= ?" + 
			 													" ORDER BY R.VERSION_ID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
	private static final String SQL_HISTORY_FROM_DATETIME_COUNT = "SELECT COUNT(*) FROM %s_RESOURCES R WHERE R.LOGICAL_RESOURCE_ID = ? AND R.LAST_UPDATED >= ?";

	
	
	/**
	 * Constructs a DAO instance suitable for acquiring connections from a JDBC Datasource object.
	 */
	public ResourceDAONormalizedImpl() {
		super();
	}
	
	/**
	 * Constructs a DAO instance suitable for acquiring connections based on the passed database type specific properties.
	 * @param dbProperties
	 */
	public ResourceDAONormalizedImpl(Properties dbProperties) {
		super(dbProperties);
	}

	@Override
	public Resource insert(Resource resource)
			throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "insert";
		log.entering(CLASSNAME, METHODNAME);
		
		Connection connection = null;
		CallableStatement stmt = null;
		ResultSet resultSet = null;
		String stmtString = null;
				
		try {
			stmtString = String.format(SQL_INSERT, resource.getClass().getSimpleName());
			connection = this.getConnection();
			stmt = connection.prepareCall(stmtString);
			stmt.setString(1, resource.getLogicalId());
			stmt.setBinaryStream(2, new ByteArrayInputStream(resource.getData()), resource.getData().length);
			stmt.setString(3, resource.isDeleted() ? "Y": "N");
			stmt.setInt(4, 1);
			stmt.setInt(5, 1);
			stmt.registerOutParameter(6, Types.BIGINT);
			
			stmt.execute();
			
			resource.setId(stmt.getLong(6));
			log.fine("Succesfully inserted Resource. id=" + resource.getId());
			
		}
		catch(FHIRPersistenceDBConnectException e) {
			throw e;
		}
		catch(Throwable e) {
			throw new FHIRPersistenceDataAccessException("Failure inserting Resource.", e);
		}
		finally {
			this.cleanup(resultSet, stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
		
		return resource;
		
	}

	@Override
	public Resource read(String logicalId, String resourceType)
			throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "read";
		log.entering(CLASSNAME, METHODNAME);
		
		Resource resource = null;
		List<Resource> resources;
		String stmtString = null;
						
		try {
			stmtString = String.format(SQL_READ, resourceType, resourceType);
			resources = this.runQuery(stmtString, logicalId, logicalId);
			if (!resources.isEmpty()) {
				resource = resources.get(0);
			}
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
		return resource;
	}

	@Override
	public Resource versionRead(String logicalId, String resourceType, int versionId)
			throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "versionRead";
		log.entering(CLASSNAME, METHODNAME);
		
		Resource resource = null;
		List<Resource> resources;
		String stmtString = null;
						
		try {
			stmtString = String.format(SQL_VERSION_READ, resourceType, resourceType);
			resources = this.runQuery(stmtString, logicalId, versionId);
			if (!resources.isEmpty()) {
				resource = resources.get(0);
			}
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
		return resource;
				
	}


	/**
	 * Creates and returns a Resource DTO based on the contents of the passed ResultSet
	 * @param resultSet A ResultSet containing FHIR persistent object data.
	 * @return Resource - A Resource DTO
	 * @throws FHIRPersistenceDataAccessException
	 */
	protected Resource createDTO(ResultSet resultSet) throws FHIRPersistenceDataAccessException {
		final String METHODNAME = "createDTO";
		log.entering(CLASSNAME, METHODNAME);
		
		Resource resource = new Resource();
		
		try {
			resource.setData(resultSet.getBytes("DATA"));
			resource.setId(resultSet.getLong("RESOURCE_ID"));
			resource.setLastUpdated(resultSet.getTimestamp("LAST_UPDATED"));
			resource.setLogicalId(resultSet.getString("LOGICAL_RESOURCE_ID"));
			resource.setVersionId(resultSet.getInt("VERSION_ID"));
			resource.setDeleted(resultSet.getString("IS_DELETED").equals("Y") ? true : false);
		}
		catch (Throwable e) {
			throw new FHIRPersistenceDataAccessException("Failure creating Resource DTO.", e);
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
		
		return resource;
	}

	@Override
	public List<Resource> history(String resourceType, String logicalId, Timestamp fromDateTime, int offset, int maxResults) 
									throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "history";
		log.entering(CLASSNAME, METHODNAME);
		
		List<Resource> resources;
		String stmtString;
				
		try {
			if (fromDateTime != null) {
				stmtString = String.format(SQL_HISTORY_FROM_DATETIME, resourceType);
				resources = this.runQuery(stmtString, logicalId, fromDateTime, offset, maxResults);
			}
			else {
				stmtString = String.format(SQL_HISTORY, resourceType);
				resources = this.runQuery(stmtString, logicalId, offset, maxResults);
			}
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
		return resources;
	}

	@Override
	public int historyCount(String resourceType, String logicalId, Timestamp fromDateTime) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "historyCount";
		log.entering(CLASSNAME, METHODNAME);
		
		int count;
		String stmtString;
				
		try {
			if (fromDateTime != null) {
				stmtString = String.format(SQL_HISTORY_FROM_DATETIME_COUNT, resourceType);
				count = this.runCountQuery(stmtString, logicalId, fromDateTime);
			}
			else {
				stmtString = String.format(SQL_HISTORY_COUNT, resourceType);
				count = this.runCountQuery(stmtString, logicalId);
			}
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
		return count;
	}

}

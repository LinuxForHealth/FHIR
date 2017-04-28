/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl;

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
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.SqlQueryData;

/**
 * This Data Access Object extends the "basic" implementation to provide functionality specific to the "normalized"
 * relational schema.
 * @author markd
 *
 */
public class ResourceDAONormalizedImpl extends ResourceDAOBasicImpl implements ResourceNormalizedDAO {
	private static final Logger log = Logger.getLogger(ResourceDAONormalizedImpl.class.getName());
	private static final String CLASSNAME = ResourceDAONormalizedImpl.class.getName(); 
	
	
	private static final String SQL_READ = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
			 							   "FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
										   "LR.LOGICAL_ID = ? AND R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID";
	
	private static final String SQL_VERSION_READ = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
			   									   "FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
			   									   "LR.LOGICAL_ID = ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID AND R.VERSION_ID = ?";
	
	private static final  String SQL_INSERT = "CALL %s.%s_add_resource(?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_HISTORY = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
			   								  "FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
			   								  "LR.LOGICAL_ID = ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID " +
											  "ORDER BY R.VERSION_ID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
	
	private static final String SQL_HISTORY_COUNT = "SELECT COUNT(R.VERSION_ID) FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE LR.LOGICAL_ID = ? AND " +
			                                        "R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID";
	
	private static final String SQL_HISTORY_FROM_DATETIME = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
				  											"FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
				  											"LR.LOGICAL_ID = ? AND R.LAST_UPDATED >= ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID " +
				  											"ORDER BY R.VERSION_ID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
	
	private static final String SQL_HISTORY_FROM_DATETIME_COUNT = "SELECT COUNT(R.VERSION_ID) FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE LR.LOGICAL_ID = ? AND " +
            													  "AND R.LAST_UPDATED >= ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID";
	
	
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
		String currentSchema;
		String stmtString = null;
				
		try {
			connection = this.getConnection();
			currentSchema = connection.getSchema().trim();
			stmtString = String.format(SQL_INSERT, currentSchema,resource.getResourceType().toLowerCase());
			stmt = connection.prepareCall(stmtString);
			stmt.setString(1, resource.getLogicalId());
			stmt.setBytes(2, resource.getData());
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
			resources = this.runQuery(stmtString, logicalId);
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
			resource.setLogicalId(resultSet.getString("LOGICAL_ID"));
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
				stmtString = String.format(SQL_HISTORY_FROM_DATETIME, resourceType, resourceType);
				resources = this.runQuery(stmtString, logicalId, fromDateTime, offset, maxResults);
			}
			else {
				stmtString = String.format(SQL_HISTORY, resourceType, resourceType);
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
				stmtString = String.format(SQL_HISTORY_FROM_DATETIME_COUNT, resourceType, resourceType);
				count = this.runCountQuery(stmtString, logicalId, fromDateTime);
			}
			else {
				stmtString = String.format(SQL_HISTORY_COUNT, resourceType, resourceType);
				count = this.runCountQuery(stmtString, logicalId);
			}
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
		return count;
	}

	@Override
	public List<Resource> search(SqlQueryData queryData) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "search(SqlQueryData)";
		log.entering(CLASSNAME, METHODNAME);
		
		List<Resource> resources;
		String sqlSelect = queryData.getQueryString();
		Object[] bindVariables = queryData.getBindVariables().toArray();
		
		try {
			resources = this.runQuery(sqlSelect, bindVariables);
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
				
		return resources;
	}

	@Override
	public int searchCount(SqlQueryData queryData) 	throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "searchCount(SqlQueryData)";
		log.entering(CLASSNAME, METHODNAME);
		
		int count;
		String sqlSelectCount = queryData.getQueryString();
		Object[] bindVariables = queryData.getBindVariables().toArray();
				
		try {
			count = this.runCountQuery(sqlSelectCount, bindVariables);
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
		return count;
	}

}

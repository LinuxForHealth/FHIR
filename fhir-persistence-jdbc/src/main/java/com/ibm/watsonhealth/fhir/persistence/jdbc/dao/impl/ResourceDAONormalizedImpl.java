/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl;

import static java.util.Objects.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
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
	
	private static Set<String> resourceTypes = Collections.synchronizedSet(new HashSet<>());
	
	
	private static final String SQL_READ = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
			 							   "FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
										   "LR.LOGICAL_ID = ? AND R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID";
	
	private static final String SQL_VERSION_READ = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
			   									   "FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
			   									   "LR.LOGICAL_ID = ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID AND R.VERSION_ID = ?";
	
	private static final  String SQL_INSERT = "CALL %s.%s_add_resource(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final  String SQL_INSERT_RESOURCE_TYPE = "CALL %s.add_resource_type(?,?)";
	
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
            													  "R.LAST_UPDATED >= ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID";
	
	private FHIRPersistenceContext context;
	
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

	/**
	 * Constructs a DAO using the passed externally managed database connection.
	 * The connection used by this instance for all DB operations will be the passed connection.
	 * @param Connection - A database connection that will be managed by the caller.
	 */
	public ResourceDAONormalizedImpl(Connection managedConnection) {
		super(managedConnection);
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
			if (!resourceTypes.contains(resource.getResourceType().toLowerCase())) {
				this.insertResourceType(resource);
			}
			connection = this.getConnection();
			currentSchema = connection.getSchema().trim();
			stmtString = String.format(SQL_INSERT, currentSchema,resource.getResourceType().toLowerCase());
			stmt = connection.prepareCall(stmtString);
			stmt.setString(1, resource.getLogicalId());
			stmt.setBytes(2, resource.getData());
			stmt.setTimestamp(3, resource.getLastUpdated());
			stmt.setString(4, resource.isDeleted() ? "Y": "N");
			stmt.setString(5, UUID.randomUUID().toString());
			stmt.setString(6, this.getCorrelationId());
			stmt.setString(7, this.getUser());
			stmt.setString(8, this.getCorrelationToken());
			stmt.setString(9, this.getTenantId());
			//TODO REAL values need to be inserted instead of these hard-coded placeholders.
			stmt.setString(10, "reason");
			stmt.setString(11, this.getEventType(resource));
			stmt.setString(12, "site_id");
			stmt.setString(13, "study_id");
			stmt.setString(14, "service_id");
			stmt.setString(15, "patient_id");
			stmt.setInt(16, 1);
			stmt.setInt(17, 1);
			stmt.registerOutParameter(18, Types.BIGINT);
			
			stmt.execute();
			
			resource.setId(stmt.getLong(18));
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

	/**
	 * Inserts the name of the type of the passed Resource into the resource_types table.
	 * This method calls a stored procedure to achieve the insertion.
	 * @param resource A valid FHIR Resource
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException
	 */
	private synchronized void insertResourceType(Resource resource) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "insertResourceType";
		log.entering(CLASSNAME, METHODNAME);
		
		Connection connection = null;
		CallableStatement stmt = null;
		String currentSchema;
		String stmtString = null;
			
		
		try {
			connection = this.getConnection();
			currentSchema = connection.getSchema().trim();
			stmtString = String.format(SQL_INSERT_RESOURCE_TYPE, currentSchema);
			stmt = connection.prepareCall(stmtString);
			stmt.setString(1, resource.getResourceType().toLowerCase());
			stmt.registerOutParameter(2, Types.BIGINT);
			
			stmt.execute();
			resourceTypes.add(resource.getResourceType().toLowerCase());
			log.fine("Succesfully inserted Resource Type: " + resource.getResourceType());
		}
		catch(FHIRPersistenceDBConnectException e) {
			throw e;
		}
		catch(Throwable e) {
			throw new FHIRPersistenceDataAccessException("Failure inserting Resource Type.", e);
		}
		finally {
			this.cleanup(stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
	}

	@Override
	public void setPersistenceContext(FHIRPersistenceContext context) {
		this.context = context;
	}
	
	/**
	 * Retrieves the correlation token from input http headers.
	 * @return
	 */
	private String getCorrelationToken() {
		String trxCorrelationToken = "";
		
		if (nonNull(this.context) && 
			nonNull(this.context.getPersistenceEvent()) && 
			nonNull(this.context.getPersistenceEvent().getHttpHeaders()) &&
			nonNull(this.context.getPersistenceEvent().getHttpHeaders().getHeaderString("IBM-DP-correlationid"))) {
			
			trxCorrelationToken = this.context.getPersistenceEvent().getHttpHeaders().getHeaderString("IBM-DP-correlationid");
		}
		return trxCorrelationToken;
	}
	
	/**
	 * Retrieves the correlation token from input http headers.
	 * @return
	 */
	private String getCorrelationId() {
		String trxCorrelationId = "";
		
		if (nonNull(this.context) && 
			nonNull(this.context.getPersistenceEvent()) && 
			nonNull(this.context.getPersistenceEvent().getTransactionCorrelationId())) {
			
			trxCorrelationId = this.context.getPersistenceEvent().getTransactionCorrelationId();
		}
		return trxCorrelationId;
	}
	
	/**
	 * Retrieves the current user from http headers.
	 * @return
	 */
	private String getUser() {
		String user = "";
		
		if (nonNull(this.context) && 
			nonNull(this.context.getPersistenceEvent()) && 
			nonNull(this.context.getPersistenceEvent().getHttpHeaders()) &&
			nonNull(this.context.getPersistenceEvent().getHttpHeaders().getHeaderString("IBM-APP-User"))) {
			
			user = this.context.getPersistenceEvent().getHttpHeaders().getHeaderString("IBM-APP-User");
		}
		return user;
	}
	
	/**
	 * Determines and returns the event type, based on data contained in the passed Resource.
	 * @param resource
	 * @return
	 */
	private String getEventType(Resource resource) {
		String eventType = "";
		
		if (resource.isDeleted()) {
			eventType = "DELETE";
		}
		else if (resource.getVersionId() == 1) {
			eventType = "CREATE";
		}
		else {
			eventType = "UPDATE";
		}
		return eventType;
	}
	
	/**
	 * Retrieves the current tenant id from Thread-local storage.
	 * @return
	 */
	private String getTenantId() {
		String tenantId = "";
		
		if (nonNull(FHIRRequestContext.get().getTenantId())) {
			tenantId = FHIRRequestContext.get().getTenantId();
		}
		return tenantId;
	}

	

}

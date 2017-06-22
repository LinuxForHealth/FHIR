/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import javax.xml.datatype.XMLGregorianCalendar;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRReplicationContext;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ResourceNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.ResourceTypesCache;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.SqlQueryData;
import com.ibm.watsonhealth.fhir.replication.api.model.ReplicationInfo;

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
	
	private static final  String SQL_INSERT = "CALL %s.%s_add_resource(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String SQL_HISTORY = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
			   								  "FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
			   								  "LR.LOGICAL_ID = ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID " +
											  "ORDER BY R.VERSION_ID DESC ";
	
	private static final String SQL_HISTORY_COUNT = "SELECT COUNT(R.VERSION_ID) FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE LR.LOGICAL_ID = ? AND " +
			                                        "R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID";
	
	private static final String SQL_HISTORY_FROM_DATETIME = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
				  											"FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
				  											"LR.LOGICAL_ID = ? AND R.LAST_UPDATED >= ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID " +
				  											"ORDER BY R.VERSION_ID DESC ";
	
	private static final String SQL_HISTORY_FROM_DATETIME_COUNT = "SELECT COUNT(R.VERSION_ID) FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE LR.LOGICAL_ID = ? AND " +
            													  "R.LAST_UPDATED >= ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID";
	
	private static final String SQL_READ_ALL_RESOURCE_TYPE_NAMES = "SELECT RESOURCE_TYPE_ID, RESOURCE_TYPE FROM RESOURCE_TYPES";
	
	private static final String SQL_READ_RESOURCE_TYPE = "CALL %s.add_resource_type(?, ?)";
	
	private FHIRPersistenceContext context;
	private ReplicationInfo replicationInfo;
	private boolean isRepInfoRequired;
	
	/**
	 * Constructs a DAO instance suitable for acquiring connections from a JDBC Datasource object.
	 */
	public ResourceDAONormalizedImpl() {
		super();
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
		Integer resourceTypeId;
		Timestamp lastUpdated, replicationLastUpdated;
				
		try {
			connection = this.getConnection();
			
			// The Resource Type Id must be populated into the cache by calling the following method.
			// This also causes the store procedure that is called by the cache to persist the resource type and id
			// in the resource_types table, if it does not already exist.
			resourceTypeId = ResourceTypesCache.getResourceTypeId(resource.getResourceType(), this);
			log.fine("resourceType=" + resource.getResourceType() + "  resourceTypeId=" + resourceTypeId);
						
			currentSchema = connection.getSchema().trim();
			stmtString = String.format(SQL_INSERT, currentSchema,resource.getResourceType());
			stmt = connection.prepareCall(stmtString);
			stmt.setString(1, resource.getLogicalId());
			stmt.setBytes(2, resource.getData());
			// If there is a lastUpdated attribute in the ReplicationContext, use it.
			replicationLastUpdated = this.getReplicationLastUpdated();
			lastUpdated = nonNull(replicationLastUpdated) ? replicationLastUpdated : resource.getLastUpdated();
			stmt.setTimestamp(3, lastUpdated);
			stmt.setString(4, resource.isDeleted() ? "Y": "N");
			stmt.setString(5, UUID.randomUUID().toString());
			stmt.setString(6, this.getReplicationInfo(resource.isDeleted()).getTxCorrelationId());
			stmt.setString(7, this.getReplicationInfo(resource.isDeleted()).getChangedBy());
			stmt.setString(8, this.getReplicationInfo(resource.isDeleted()).getCorrelationToken());
			stmt.setString(9, this.getReplicationInfo(resource.isDeleted()).getTenantId());
			stmt.setString(10, this.getReplicationInfo(resource.isDeleted()).getReason());
			stmt.setString(11, this.getReplicationInfo(resource.isDeleted()).getEvent());
			stmt.setString(12, this.getReplicationInfo(resource.isDeleted()).getSiteId());
			stmt.setString(13, this.getReplicationInfo(resource.isDeleted()).getStudyId());
			stmt.setString(14, this.getReplicationInfo(resource.isDeleted()).getServiceId());
			stmt.setString(15, this.getReplicationInfo(resource.isDeleted()).getPatientId());
			stmt.setObject(16, this.getReplicationVersionId(), Types.INTEGER);
			stmt.setInt(17, 1);
			stmt.setInt(18, 1);
			stmt.registerOutParameter(19, Types.BIGINT);
			
			stmt.execute();
			
			resource.setId(stmt.getLong(19));
			log.fine("Succesfully inserted Resource. id=" + resource.getId());
			
		}
		catch(FHIRPersistenceDBConnectException | FHIRPersistenceDataAccessException e) {
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
		String stmtString = null;
				
		try {
			if (fromDateTime != null) {
				stmtString = String.format(SQL_HISTORY_FROM_DATETIME, resourceType, resourceType);
				if (this.isDb2Database()) {
					stmtString = stmtString + DB2_PAGINATION_PARMS;
					resources = this.runQuery(stmtString, logicalId, fromDateTime, maxResults, offset);
				}
				else {
					stmtString = stmtString + DERBY_PAGINATION_PARMS;
					resources = this.runQuery(stmtString, logicalId, fromDateTime, offset, maxResults);
				}
			}
			else {
				stmtString = String.format(SQL_HISTORY, resourceType, resourceType);
				if (this.isDb2Database()) {
					stmtString = stmtString + DB2_PAGINATION_PARMS;
					resources = this.runQuery(stmtString, logicalId, maxResults, offset);
				}
				else {
					stmtString = stmtString + DERBY_PAGINATION_PARMS;
					resources = this.runQuery(stmtString, logicalId, offset, maxResults);
				}
			}
		} 
		catch (SQLException e) {
			throw new FHIRPersistenceDataAccessException("Failure running history query: " + stmtString, e);
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

	@Override
	public void setPersistenceContext(FHIRPersistenceContext context) {
		this.context = context;
	}
	
	private ReplicationInfo getReplicationInfo(boolean isLogicalDelete) throws FHIRPersistenceDataAccessException {
		ReplicationInfo repInfo = null;
		
		// If a ReplicationInfo is found in the persistence event, use it. Otherwise, create a dummy ReplicationInfo.
		if (this.replicationInfo == null) {
			if (nonNull(this.context) &&
				nonNull(this.context.getPersistenceEvent()) && 
				nonNull(this.context.getPersistenceEvent().getProperty(FHIRPersistenceEvent.PROPNAME_REPLICATION_INFO))) {
				repInfo = (ReplicationInfo)this.context.getPersistenceEvent().getProperty(FHIRPersistenceEvent.PROPNAME_REPLICATION_INFO);
			}
			else if (this.isRepInfoRequired && !isLogicalDelete && this.getReplicationContext() == null) {
				throw new FHIRPersistenceDataAccessException("Required ReplicationInfo is null");
			}
			else {
				repInfo = new ReplicationInfo();
			}
			// Ensure that all ReplilcationInfo attributes that are required to be non-null in the fhir_replication_log table 
			// do indeed have non-null values.
			if (isNull(repInfo.getTxCorrelationId())) {
				repInfo.setTxCorrelationId("");
			}
			if (isNull(repInfo.getChangedBy())) {
				repInfo.setChangedBy("");
			}
			if (isNull(repInfo.getCorrelationToken())) {
				repInfo.setCorrelationToken("");
			}
			if (isNull(repInfo.getTenantId())) {
				repInfo.setTenantId("");
			}
			if (isNull(repInfo.getReason())) {
				repInfo.setReason("");
			}
			if (isNull(repInfo.getEvent())) {
				repInfo.setEvent("");
			}
			if (isNull(repInfo.getServiceId())) {
				repInfo.setServiceId("");
			}
			
			this.replicationInfo = repInfo;
		}
		return this.replicationInfo;
	}
	
	private FHIRReplicationContext getReplicationContext() {
		FHIRReplicationContext replicationContext = null;
		
		if (nonNull(this.context) && nonNull(this.context.getPersistenceEvent())) {
				replicationContext = this.context.getPersistenceEvent().getReplicationContext();
		}
		
		return replicationContext;
	}
	
	private Integer getReplicationVersionId() {
		Integer repVersionId = null;
		
		FHIRReplicationContext repContext = this.getReplicationContext();
		if (nonNull(repContext) && nonNull(repContext.getVersionId())) {
			repVersionId = Integer.valueOf(repContext.getVersionId());
		}
		return repVersionId;
	}
	
	private Timestamp getReplicationLastUpdated() {
		Timestamp repLastUpdated = null;
				
		FHIRReplicationContext repContext = this.getReplicationContext();
		if (nonNull(repContext) && nonNull(repContext.getLastUpdated())) {
			XMLGregorianCalendar calendar = FHIRUtilities.parseDateTime(repContext.getLastUpdated(), true);
			repLastUpdated = FHIRUtilities.convertToTimestamp(calendar);
		}
		
		return repLastUpdated;
	}
	
	
	@Override
	public Map<String, Integer> readAllResourceTypeNames()
										 throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		final String METHODNAME = "readAllResourceTypeNames";
		log.entering(CLASSNAME, METHODNAME);
				
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String parameterName;
		int parameterId;
		Map<String, Integer> parameterMap = new HashMap<>();
		String errMsg = "Failure retrieving all Resource type names.";
				
		try {
			connection = this.getConnection();
			stmt = connection.prepareStatement(SQL_READ_ALL_RESOURCE_TYPE_NAMES);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				parameterName = resultSet.getString("RESOURCE_TYPE");
				parameterId = resultSet.getInt("RESOURCE_TYPE_ID");
				parameterMap.put(parameterName, parameterId);
			}
		}
		catch (Throwable e) {
			throw new FHIRPersistenceDataAccessException(errMsg,e);
		}
		finally {
			this.cleanup(stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
				
		return parameterMap;
	}

	
	@Override
	public Integer readResourceTypeId(String resourceType) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException  {
		final String METHODNAME = "readResourceTypeId";
		log.entering(CLASSNAME, METHODNAME);
		
		Connection connection = null;
		CallableStatement stmt = null;
		Integer parameterNameId = null;
		String currentSchema;
		String stmtString;
		String errMsg = "Failure storing Resource type name id: name=" + resourceType;
				
		try {
			connection = this.getConnection();
			currentSchema = connection.getSchema().trim();
			stmtString = String.format(SQL_READ_RESOURCE_TYPE, currentSchema);
			stmt = connection.prepareCall(stmtString); 
			stmt.setString(1, resourceType);
			stmt.registerOutParameter(2, Types.INTEGER);
			stmt.execute();
			parameterNameId = stmt.getInt(2);
		}
		catch(FHIRPersistenceDBConnectException e) {
			throw e;
		}
		catch (Throwable e) {
			throw new FHIRPersistenceDataAccessException(errMsg,e);
		} 
		finally {
			this.cleanup(stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
		return parameterNameId;
	}

	@Override
	public void setRepInfoRequired(boolean isRepInfoRequired) {
		this.isRepInfoRequired = isRepInfoRequired;
		
	}

	@Override
	public boolean isRepInfoRequired() {
		return this.isRepInfoRequired;
	}

	

}

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
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;
import javax.ws.rs.core.Response.Status;
import javax.xml.datatype.XMLGregorianCalendar;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRReplicationContext;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ResourceNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceFKVException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceVersionIdMismatchException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.ResourceTypesCache;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.ResourceTypesCacheUpdater;
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
	
	private static final  String SQL_INSERT = "CALL %s.%s_add_resource(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String SQL_INSERT_WITH_PARAMETERS = "CALL %s.%s_add_resource2(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
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
	
	private static final String SQL_SEARCH_BY_IDS = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
													"FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID AND " + 
													"R.RESOURCE_ID IN ";
	
	private FHIRPersistenceContext context;
	private ReplicationInfo replicationInfo;
	private boolean isRepInfoRequired;
	private Map<String, Integer> newResourceTypeIds = new HashMap<>();
	private boolean runningInTrx = false;
	private ResourceTypesCacheUpdater rtCacheUpdater = null;
	private TransactionSynchronizationRegistry trxSynchRegistry;
	
	/**
	 * Constructs a DAO instance suitable for acquiring connections from a JDBC Datasource object.
	 */
	public ResourceDAONormalizedImpl(TransactionSynchronizationRegistry trxSynchRegistry) {
		super();
		this.runningInTrx = true;
		this.trxSynchRegistry = trxSynchRegistry;
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
		
		List<Resource> resources = null;
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
			log.exiting(CLASSNAME, METHODNAME, Arrays.toString(new Object[] {resources}));
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
		long dbCallStartTime;
		double dbCallDuration;
				
		try {
			connection = this.getConnection();
			stmt = connection.prepareStatement(SQL_READ_ALL_RESOURCE_TYPE_NAMES);
			dbCallStartTime = System.nanoTime();
			resultSet = stmt.executeQuery();
			dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
			if (log.isLoggable(Level.FINE)) {
                log.fine("DB read all resource type complete. executionTime=" + dbCallDuration + "ms");
            }
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
		long dbCallStartTime;
		double dbCallDuration;
				
		try {
			connection = this.getConnection();
			currentSchema = connection.getSchema().trim();
			stmtString = String.format(SQL_READ_RESOURCE_TYPE, currentSchema);
			stmt = connection.prepareCall(stmtString); 
			stmt.setString(1, resourceType);
			stmt.registerOutParameter(2, Types.INTEGER);
			dbCallStartTime = System.nanoTime();
			stmt.execute();
			dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
            if (log.isLoggable(Level.FINE)) {
                log.fine("DB read resource type id complete. executionTime=" + dbCallDuration + "ms");
            }
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

	@Override
	public List<Long> searchForIds(SqlQueryData queryData) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "searchForIds";
		log.entering(CLASSNAME, METHODNAME);
		
		List<Long> resourceIds = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String errMsg;
		long dbCallStartTime;
		double dbCallDuration;
		
		try {
			connection = this.getConnection();
			stmt = connection.prepareStatement(queryData.getQueryString());
			// Inject arguments into the prepared stmt.
			for (int i = 0; i < queryData.getBindVariables().size();  i++) {
				stmt.setObject(i+1, queryData.getBindVariables().get(i));
			}
			dbCallStartTime = System.nanoTime();
			resultSet = stmt.executeQuery();
			dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
            if (log.isLoggable(Level.FINE)) {
                log.fine("DB search for ids complete. executionTime=" + dbCallDuration + "ms");
            }
			while(resultSet.next())	 {
				resourceIds.add(resultSet.getLong(1));
			}
		}
		catch(FHIRPersistenceException e) {
			throw e;
		}
		catch (Throwable e) {
			errMsg = "Failure retrieving FHIR Resource Ids. SqlQueryData=" + queryData;
			throw new FHIRPersistenceDataAccessException(errMsg,e);
		} 
		finally {
			this.cleanup(resultSet, stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
		return resourceIds;
	}

	@Override
	public List<Resource> searchByIds(String resourceType, List<Long> resourceIds) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "searchByIds";
		log.entering(CLASSNAME, METHODNAME);
		
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String errMsg;
		StringBuilder idQuery = new StringBuilder();
		List<Resource> resources = new ArrayList<>();
		String stmtString = null;
		long dbCallStartTime;
		double dbCallDuration;
		
		try {
			stmtString = String.format(this.getSearchByIdsSql(resourceType));
			idQuery.append(stmtString);
			idQuery.append("(");
			for (int i = 0; i < resourceIds.size(); i++) {
				if (i > 0) {
					idQuery.append(",");
				}
				idQuery.append(resourceIds.get(i));
			}
			idQuery.append(")");
						
			connection = this.getConnection();
			stmt = connection.prepareStatement(idQuery.toString());
			dbCallStartTime = System.nanoTime();
			resultSet = stmt.executeQuery();
			dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
            if (log.isLoggable(Level.FINE)) {
                log.fine("DB search by ids complete. executionTime=" + dbCallDuration + "ms");
            }
			resources = this.createDTOs(resultSet);
		}
		catch(FHIRPersistenceException e) {
			throw e;
		}
		catch (Throwable e) {
			errMsg = "Failure retrieving FHIR Resources. SQL=" + idQuery;
			throw new FHIRPersistenceDataAccessException(errMsg,e);
		} 
		finally {
			this.cleanup(resultSet, stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
		return resources;
	}

	protected String getSearchByIdsSql(String resourceType) {
		
		String stmtString;
		stmtString = String.format(SQL_SEARCH_BY_IDS, resourceType, resourceType);
		return stmtString;
	}	
	
	 /**
     * Adds a resource type/ resource id pair to a candidate collection for population into the ResourceTypesCache. 
     * This pair must be present as a row in the FHIR DB RESOURCE_TYPES table.
     * @param resourceType A valid FHIR resource type.
     * @param resourceTypeId The corresponding id for the resource type.
     * @throws FHIRPersistenceException
     */
    @Override
    public void addResourceTypeCacheCandidate(String resourceType, Integer resourceTypeId) throws FHIRPersistenceException {
        final String METHODNAME = "addResourceTypeCacheCandidate";
        log.entering(CLASSNAME, METHODNAME);
        
        if (this.runningInTrx && ResourceTypesCache.isEnabled()) {
            if (this.rtCacheUpdater == null) {
                // Register a new ResourceTypeCacheUpdater for this thread/trx, if one hasn't been already registered.
                this.rtCacheUpdater = new ResourceTypesCacheUpdater(ResourceTypesCache.getCacheNameForTenantDatastore(), this.newResourceTypeIds);
                try {
                    trxSynchRegistry.registerInterposedSynchronization(rtCacheUpdater);
                    log.fine("Registered ResourceTypeCacheUpdater.");
                }
                catch(Throwable e) {
                    throw new FHIRPersistenceException("Failure registering ResourceTypesCacheUpdater", Status.INTERNAL_SERVER_ERROR, e);
                }
            }
            this.newResourceTypeIds.put(resourceType, resourceTypeId);
        }
        
            
        log.exiting(CLASSNAME, METHODNAME);
        
    }
    
    @Override
    public Resource insert(Resource resource, List<Parameter> parameters, ParameterNormalizedDAO parameterDao)
            throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException, FHIRPersistenceVersionIdMismatchException {
            final String METHODNAME = "insert(Resource, List<Parameter>";
            log.entering(CLASSNAME, METHODNAME); 
            
            try {
                if (this.isDb2Database()) {
                    resource = this.insertToDb2(resource, parameters, parameterDao);
                }
                else {
                    resource = this.insertToDerby(resource, parameters, parameterDao);
                }
            }
            catch(SQLException e) {
                throw new FHIRPersistenceDataAccessException("Failure determining database type.",e);
            }
            
            return resource;
    }

    /**
     * Inserts the passed FHIR Resource and associated search parameters to a DB2 FHIR database.
     * This method will call the DB2 stored procedure defined in the SQL_INSERT_WITH_PARAMETERS constant. 
     * Both the Resource and its search parameters are stored at the same time by the stored procedure.
     * @param resource The FHIR Resource to be inserted.
     * @param parameters The Resource's search parameters to be inserted.
     * @param parameterDao
     * @return The Resource DTO
     * @throws FHIRPersistenceDataAccessException
     * @throws FHIRPersistenceDBConnectException
     * @throws FHIRPersistenceVersionIdMismatchException 
     */
    private Resource insertToDb2(Resource resource, List<Parameter> parameters, ParameterNormalizedDAO parameterDao)
                    throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException, FHIRPersistenceVersionIdMismatchException {
        final String METHODNAME = "insertToDb2";
        log.entering(CLASSNAME, METHODNAME);
         
        Connection connection = null;
        CallableStatement stmt = null;
        String currentSchema;
        String stmtString = null;
        Integer resourceTypeId;
        Timestamp lastUpdated, replicationLastUpdated;
        boolean acquiredFromCache;
        long dbCallStartTime;
        double dbCallDuration;
                
        try {
            connection = this.getConnection();
                        
            resourceTypeId = ResourceTypesCache.getResourceTypeId(resource.getResourceType());
            if (resourceTypeId == null) {
                acquiredFromCache = false;
                resourceTypeId = this.readResourceTypeId(resource.getResourceType());
                this.addResourceTypeCacheCandidate(resource.getResourceType(), resourceTypeId);
            }
            else {
                acquiredFromCache = true;
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine("resourceType=" + resource.getResourceType() + "  resourceTypeId=" + resourceTypeId + 
                         "  acquiredFromCache=" + acquiredFromCache + "  tenantDatastoreCacheName=" + ResourceTypesCache.getCacheNameForTenantDatastore());
            }
                        
            currentSchema = connection.getSchema().trim();
            stmtString = String.format(SQL_INSERT_WITH_PARAMETERS, currentSchema,resource.getResourceType());
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
            stmt.setInt(17, resource.getVersionId());
            
            // Transform the passed search parameters into SQL parameter array structures.
            if (parameterDao != null && parameters != null) {
                stmt.setArray(18, parameterDao.transformStringParameters(connection, currentSchema, parameters));
                stmt.setArray(19, parameterDao.transformNumberParameters(connection, currentSchema, parameters));
                stmt.setArray(20, parameterDao.transformDateParameters(connection, currentSchema, parameters));
                stmt.setArray(21, parameterDao.transformLatLongParameters(connection, currentSchema, parameters));
                stmt.setArray(22, parameterDao.transformTokenParameters(connection, currentSchema, parameters));
                stmt.setArray(23, parameterDao.transformQuantityParameters(connection, currentSchema, parameters));
            }
            else {
                stmt.setArray(18, null);
                stmt.setArray(19, null);
                stmt.setArray(20, null);
                stmt.setArray(21, null);
                stmt.setArray(22, null);
                stmt.setArray(23, null);
            }
            stmt.setString(24, this.isRepInfoRequired() ? "Y": "N");
            stmt.registerOutParameter(25, Types.BIGINT);
            
            dbCallStartTime = System.nanoTime();
            stmt.execute();
            dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
            
            resource.setId(stmt.getLong(25));
            if (log.isLoggable(Level.FINE)) {
                log.fine("Successfully inserted Resource. id=" + resource.getId() + " executionTime=" + dbCallDuration + "ms");
            }
        }
        catch(FHIRPersistenceDBConnectException | FHIRPersistenceDataAccessException e) {
            throw e;
        }
        catch(SQLIntegrityConstraintViolationException e) {
            throw new FHIRPersistenceFKVException("Encountered FK violation while inserting Resource.", e);
        }
        catch(SQLException e) {
            if ("99001".equals(e.getSQLState())) {
                throw new FHIRPersistenceVersionIdMismatchException("Encounterd version id mismatch while inserting Resource", e );
            }
            else {
                throw new FHIRPersistenceDataAccessException("SQLException encountered while inserting Resource.", e);
            }
        }
        catch(Throwable e) {
            throw new FHIRPersistenceDataAccessException("Failure inserting Resource.", e);
        }
        finally {
            this.cleanup(stmt, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }
        
        return resource;
    }
    
    /**
     * Inserts the passed FHIR Resource and associated search parameters to a Derby FHIR database.
     * This method will call the Derby stored procedure defined in the SQL_INSERT constant. 
     * The search parameters are stored first by calling the passed parameterDao. Then the Resource is stored
     * by invoking the stored procedure.
     * @param resource The FHIR Resource to be inserted.
     * @param parameters The Resource's search parameters to be inserted.
     * @param parameterDao
     * @return The Resource DTO
     * @throws FHIRPersistenceDataAccessException
     * @throws FHIRPersistenceDBConnectException
     * @throws FHIRPersistenceVersionIdMismatchException 
     */
    private Resource insertToDerby(Resource resource, List<Parameter> parameters, ParameterNormalizedDAO parameterDao) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException, FHIRPersistenceVersionIdMismatchException {
        final String METHODNAME = "insertToDerby";
        log.entering(CLASSNAME, METHODNAME);
        
        Connection connection = null;
        CallableStatement stmt = null;
        ResultSet resultSet = null;
        String currentSchema;
        String stmtString = null;
        Integer resourceTypeId;
        Timestamp lastUpdated, replicationLastUpdated;
        boolean acquiredFromCache;
        long dbCallStartTime;
        double dbCallDuration;
                
        try {
            if (parameterDao != null && parameters != null) {
                parameterDao.insert(parameters);
            }
            
            connection = this.getConnection();
            
            resourceTypeId = ResourceTypesCache.getResourceTypeId(resource.getResourceType());
            if (resourceTypeId == null) {
                acquiredFromCache = false;
                resourceTypeId = this.readResourceTypeId(resource.getResourceType());
                this.addResourceTypeCacheCandidate(resource.getResourceType(), resourceTypeId);
            }
            else {
                acquiredFromCache = true;
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine("resourceType=" + resource.getResourceType() + "  resourceTypeId=" + resourceTypeId + 
                         "  acquiredFromCache=" + acquiredFromCache + "  tenantDatastoreCacheName=" + ResourceTypesCache.getCacheNameForTenantDatastore());
            }
                        
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
            stmt.setInt(17, resource.getVersionId());
            stmt.setInt(18, 1);
            stmt.setInt(19, 1);
            stmt.setString(20, this.isRepInfoRequired() ? "Y": "N");
            stmt.registerOutParameter(21, Types.BIGINT);
            
            dbCallStartTime = System.nanoTime();
            stmt.execute();
            dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
            
            resource.setId(stmt.getLong(21));
            if (log.isLoggable(Level.FINE)) {
                log.fine("Successfully inserted Resource. id=" + resource.getId() + " executionTime=" + dbCallDuration + "ms");
            }
        }
        catch(FHIRPersistenceDBConnectException | FHIRPersistenceDataAccessException e) {
            throw e;
        }
        catch(SQLIntegrityConstraintViolationException e) {
            throw new FHIRPersistenceFKVException("Encountered FK violation while inserting Resource.", e);
        }
        catch(SQLException e) {
            if ("99001".equals(e.getSQLState())) {
                throw new FHIRPersistenceVersionIdMismatchException("Encounterd version id mismatch while inserting Resource", e );
            }
            else {
                throw new FHIRPersistenceDataAccessException("SQLException encountered while inserting Resource.", e);
            }
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

}

/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.END;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.THEN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.UTC;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.WHEN;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceVersionIdMismatchException;
import com.ibm.fhir.persistence.jdbc.JDBCConstants;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import com.ibm.fhir.persistence.jdbc.dto.Resource;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceFKVException;
import com.ibm.fhir.persistence.jdbc.util.ResourceTypesCache;
import com.ibm.fhir.persistence.jdbc.util.ResourceTypesCacheUpdater;
import com.ibm.fhir.persistence.jdbc.util.SqlQueryData;

/**
 * This Data Access Object implements the ResourceDAO interface for creating, updating,
 * and retrieving rows in the IBM FHIR Server resource tables.
 */
public class ResourceDAOImpl extends FHIRDbDAOImpl implements ResourceDAO {
    private static final Logger log = Logger.getLogger(ResourceDAOImpl.class.getName());
    private static final String CLASSNAME = ResourceDAOImpl.class.getName();

    // Read the current version of the resource
    private static final String SQL_READ = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
                                            "FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
                                           "LR.LOGICAL_ID = ? AND R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID";

    // Read a specific version of the resource
    private static final String SQL_VERSION_READ = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
                                                      "FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
                                                      "LR.LOGICAL_ID = ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID AND R.VERSION_ID = ?";

    //                                                                                 0
    //                                                                                 1 2 3 4 5 6 7 8
    // Don't forget that we must account for IN and OUT parameters.
    private static final String SQL_INSERT_WITH_PARAMETERS = "CALL %s.add_any_resource(?,?,?,?,?,?,?,?)";

    // Read version history of the resource identified by its logical-id
    private static final String SQL_HISTORY = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
                                                 "FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
                                                 "LR.LOGICAL_ID = ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID " +
                                              "ORDER BY R.VERSION_ID DESC ";

    // Count the number of versions we have for the resource identified by its logical-id
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

    private static final String SQL_ORDER_BY_IDS = "ORDER BY CASE R.RESOURCE_ID ";

    private static final String DERBY_PAGINATION_PARMS = "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    private static final String DB2_PAGINATION_PARMS = "LIMIT ? OFFSET ?";

    @SuppressWarnings("unused")
    private FHIRPersistenceContext context;

    private Map<String, Integer> newResourceTypeIds = new HashMap<>();
    private boolean runningInTrx = false;
    private ResourceTypesCacheUpdater rtCacheUpdater = null;
    private TransactionSynchronizationRegistry trxSynchRegistry;

    /**
     * Constructs a DAO instance suitable for acquiring connections from a JDBC Datasource object.
     */
    public ResourceDAOImpl(TransactionSynchronizationRegistry trxSynchRegistry) {
        super();
        this.runningInTrx = true;
        this.trxSynchRegistry = trxSynchRegistry;
    }

    /**
     * Constructs a DAO using the passed externally managed database connection.
     * The connection used by this instance for all DB operations will be the passed connection.
     * @param Connection - A database connection that will be managed by the caller.
     */
    public ResourceDAOImpl(Connection managedConnection) {
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
        } finally {
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
        } finally {
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
    @Override
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
        } catch (Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure creating Resource DTO.");
            throw severe(log, fx, e);
        } finally {
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
                } else {
                    stmtString = stmtString + DERBY_PAGINATION_PARMS;
                    resources = this.runQuery(stmtString, logicalId, fromDateTime, offset, maxResults);
                }
            } else {
                stmtString = String.format(SQL_HISTORY, resourceType, resourceType);
                if (this.isDb2Database()) {
                    stmtString = stmtString + DB2_PAGINATION_PARMS;
                    resources = this.runQuery(stmtString, logicalId, maxResults, offset);
                } else {
                    stmtString = stmtString + DERBY_PAGINATION_PARMS;
                    resources = this.runQuery(stmtString, logicalId, offset, maxResults);
                }
            }
        } catch (SQLException e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure running history query");
            String errMsg = "Failure running history query: " + stmtString;
            throw severe(log, fx, errMsg, e);
        } finally {
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
            } else {
                stmtString = String.format(SQL_HISTORY_COUNT, resourceType, resourceType);
                count = this.runCountQuery(stmtString, logicalId);
            }
        } finally {
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
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }

        return resources;
    }

    @Override
    public int searchCount(SqlQueryData queryData)     throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "searchCount(SqlQueryData)";
        log.entering(CLASSNAME, METHODNAME);

        int count;
        String sqlSelectCount = queryData.getQueryString();
        Object[] bindVariables = queryData.getBindVariables().toArray();

        try {
            count = this.runCountQuery(sqlSelectCount, bindVariables);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return count;
    }

    @Override
    public void setPersistenceContext(FHIRPersistenceContext context) {
        this.context = context;
    }

    @Override
    public Map<String, Integer> readAllResourceTypeNames()
                                         throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
        final String METHODNAME = "readAllResourceTypeNames";
        log.entering(CLASSNAME, METHODNAME);

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        Map<String, Integer> result = new HashMap<>();
        long dbCallStartTime;
        double dbCallDuration;

        try {
            connection = this.getConnection();
            stmt = connection.prepareStatement(SQL_READ_ALL_RESOURCE_TYPE_NAMES);
            dbCallStartTime = System.nanoTime();
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                final int resourceTypeId = resultSet.getInt(1);
                final String resourceType = resultSet.getString(2);
                result.put(resourceType, resourceTypeId);
            }

            if (log.isLoggable(Level.FINE)) {
                dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
                log.fine("DB read all resource type complete. executionTime=" + dbCallDuration + "ms");
            }
        } catch (Throwable e) {
            final String errMsg = "Failure retrieving all Resource type names.";
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException(errMsg);
            throw severe(log, fx, e);
        } finally {
            this.cleanup(stmt, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }

        return result;
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
        } catch(FHIRPersistenceDBConnectException e) {
            throw e;
        } catch (Throwable e) {
            final String errMsg = "Failure storing Resource type name id: name=" + resourceType;
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException(errMsg);
            throw severe(log, fx, e);
        } finally {
            this.cleanup(stmt, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }
        return parameterNameId;
    }

    @Override
    public List<Long> searchForIds(SqlQueryData queryData) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "searchForIds";
        log.entering(CLASSNAME, METHODNAME);

        List<Long> resourceIds = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            connection = this.getConnection();
            stmt = connection.prepareStatement(queryData.getQueryString());
            // Inject arguments into the prepared stmt.
            for (int i = 0; i < queryData.getBindVariables().size(); i++) {
                Object object = queryData.getBindVariables().get(i);
                if (object instanceof Timestamp) {
                    stmt.setTimestamp(i+1, (Timestamp) object, JDBCConstants.UTC);
                } else {
                    stmt.setObject(i+1, object);
                }
            }
            dbCallStartTime = System.nanoTime();
            resultSet = stmt.executeQuery();
            dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
            if (log.isLoggable(Level.FINE)) {
                log.fine("DB search for ids complete. " + queryData + "  executionTime=" + dbCallDuration + "ms");
            }
            while(resultSet.next()) {
                resourceIds.add(resultSet.getLong(1));
            }
        } catch(FHIRPersistenceException e) {
            throw e;
        } catch (Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure retrieving FHIR Resource Ids");
            final String errMsg = "Failure retrieving FHIR Resource Ids. SqlQueryData=" + queryData;
            throw severe(log, fx, errMsg, e);
        } finally {
            this.cleanup(resultSet, stmt, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }
        return resourceIds;
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
                } catch(Throwable e) {
                    throw new FHIRPersistenceException("Failure registering ResourceTypesCacheUpdater", e);
                }
            }
            this.newResourceTypeIds.put(resourceType, resourceTypeId);
        }

        log.exiting(CLASSNAME, METHODNAME);

    }
    
    protected  Integer getResourceTypeIdFromCaches(String resourceType) {
    	Integer resourceTypeId;
    	// Get resourceTypeId from ResourceTypesCache first.
        resourceTypeId = ResourceTypesCache.getResourceTypeId(resourceType);
        // If no found, then get resourceTypeId from local newResourceTypeIds in case this id is already in newResourceTypeIds
        // but has not been updated to ResourceTypesCache yet. newResourceTypeIds is updated to ResourceTypesCache only when the
        // current transaction is committed.
        if (resourceTypeId == null) {
            resourceTypeId = this.newResourceTypeIds.get(resourceType);
        }       
        return resourceTypeId;
    }

    @Override
    public Resource insert(Resource resource, List<ExtractedParameterValue> parameters, ParameterDAO parameterDao)
            throws FHIRPersistenceException {
        final String METHODNAME = "insert(Resource, List<ExtractedParameterValue>";
        log.entering(CLASSNAME, METHODNAME);

        Connection connection = null;
        CallableStatement stmt = null;
        String currentSchema;
        String stmtString = null;
        Integer resourceTypeId;
        Timestamp lastUpdated;
        boolean acquiredFromCache;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            connection = this.getConnection();

            resourceTypeId = getResourceTypeIdFromCaches(resource.getResourceType());
            if (resourceTypeId == null) {
                acquiredFromCache = false;
                resourceTypeId = this.readResourceTypeId(resource.getResourceType());
                this.addResourceTypeCacheCandidate(resource.getResourceType(), resourceTypeId);
            } else {
                acquiredFromCache = true;
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine("resourceType=" + resource.getResourceType() + "  resourceTypeId=" + resourceTypeId +
                         "  acquiredFromCache=" + acquiredFromCache + "  tenantDatastoreCacheName=" + ResourceTypesCache.getCacheNameForTenantDatastore());
            }

            // TODO avoid the round-trip and use the configured data schema name
            currentSchema = connection.getSchema().trim();
            stmtString = String.format(SQL_INSERT_WITH_PARAMETERS, currentSchema);
            stmt = connection.prepareCall(stmtString);
            stmt.setString(1, resource.getResourceType());
            stmt.setString(2, resource.getLogicalId());
            stmt.setBytes(3, resource.getData());

            lastUpdated = resource.getLastUpdated();
            stmt.setTimestamp(4, lastUpdated, UTC);
            stmt.setString(5, resource.isDeleted() ? "Y": "N");
            stmt.setString(6, UUID.randomUUID().toString());
            stmt.setInt(7, resource.getVersionId());
            stmt.registerOutParameter(8, Types.BIGINT);

            dbCallStartTime = System.nanoTime();
            stmt.execute();
            dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;

            resource.setId(stmt.getLong(8));

            // Parameter time
            // TODO FHIR_ADMIN schema name needs to come from the configuration/context
            if (parameters != null) {
                try (ParameterVisitorBatchDAO pvd = new ParameterVisitorBatchDAO(connection, "FHIR_ADMIN", resource.getResourceType(), true,
                        resource.getId(), 100, new ParameterNameCacheAdapter(parameterDao), new CodeSystemCacheAdapter(parameterDao))) {
                    for (ExtractedParameterValue p: parameters) {
                        p.accept(pvd);
                    }
                }
            }

            if (log.isLoggable(Level.FINE)) {
                log.fine("Successfully inserted Resource. id=" + resource.getId() + " executionTime=" + dbCallDuration + "ms");
            }
        } catch(FHIRPersistenceDBConnectException | FHIRPersistenceDataAccessException e) {
            throw e;
        } catch(SQLIntegrityConstraintViolationException e) {
            FHIRPersistenceFKVException fx = new FHIRPersistenceFKVException("Encountered FK violation while inserting Resource.");
            throw severe(log, fx, e);
        } catch(SQLException e) {
            if ("99001".equals(e.getSQLState())) {
                // this is just a concurrency update, so there's no need to log the SQLException here
                throw new FHIRPersistenceVersionIdMismatchException("Encountered version id mismatch while inserting Resource");
            } else {
                FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("SQLException encountered while inserting Resource.");
                throw severe(log, fx, e);
            }
        } catch(Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure inserting Resource.");
            throw severe(log, fx, e);
        } finally {
            this.cleanup(stmt, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }

        return resource;
    }


    @Override
    public List<Resource> search(String sqlSelect) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "search";
        log.entering(CLASSNAME, METHODNAME);

        List<Resource> resources;

        try {
            resources = this.runQuery(sqlSelect);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }

        return resources;
    }

    @Override
    public List<Resource> searchByIds(String resourceType, List<Long> resourceIds) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "searchByIds";
        log.entering(CLASSNAME, METHODNAME);

        if (resourceIds.isEmpty()) {
            return Collections.emptyList();
        }

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
            stmtString = getSearchByIdsSql(resourceType);
            idQuery.append(stmtString);
            idQuery.append("(");
            // resourceIds should have a max length of 1000 (the max page size)
            StringBuilder caseStmts = new StringBuilder();
            for (int i = 0; i < resourceIds.size(); i++) {
                if (i > 0) {
                    idQuery.append(",");
                }
                idQuery.append(resourceIds.get(i));

                // build up the caseStmts here so we only need to iterate the list once
                caseStmts.append(WHEN + resourceIds.get(i) + THEN + i);
            }
            idQuery.append(") " + SQL_ORDER_BY_IDS + caseStmts + END);

            connection = this.getConnection();
            stmt = connection.prepareStatement(idQuery.toString());
            dbCallStartTime = System.nanoTime();
            resultSet = stmt.executeQuery();
            dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
            if (log.isLoggable(Level.FINE)) {
                log.fine("DB search by ids complete. SQL=[" + idQuery + "]  executionTime=" + dbCallDuration + "ms");
            }
            resources = this.createDTOs(resultSet);
        } catch(FHIRPersistenceException e) {
            throw e;
        } catch (Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure retrieving FHIR Resources");
            errMsg = "Failure retrieving FHIR Resources. SQL=[" + idQuery + "]";
            throw severe(log, fx, errMsg, e);
        } finally {
            this.cleanup(resultSet, stmt, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }
        return resources;
    }

    protected String getSearchByIdsSql(String resourceType) {
        return String.format(SQL_SEARCH_BY_IDS, resourceType, resourceType);
    }

    @Override
    public int searchCount(String sqlSelectCount) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "searchCount";
        log.entering(CLASSNAME, METHODNAME);

        int count;

        try {
            count = this.runCountQuery(sqlSelectCount);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return count;
    }

    @Override
    public List<String> searchStringValues(SqlQueryData queryData)
            throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "searchSTR_VALUES";
        log.entering(CLASSNAME, METHODNAME);

        String sqlSelect = queryData.getQueryString();
        Object[] bindVariables = queryData.getBindVariables().toArray();
        try {
            return this.runQuery_STR_VALUES(sqlSelect, bindVariables);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }
}
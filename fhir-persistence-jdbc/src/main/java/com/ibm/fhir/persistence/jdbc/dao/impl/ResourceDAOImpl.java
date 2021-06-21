/*
 * (C) Copyright IBM Corp. 2017, 2021
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.database.utils.query.QueryUtil;
import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceVersionIdMismatchException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.JDBCConstants;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import com.ibm.fhir.persistence.jdbc.dto.Resource;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceFKVException;
import com.ibm.fhir.persistence.jdbc.impl.ParameterTransactionDataImpl;
import com.ibm.fhir.persistence.jdbc.util.ResourceTypesCache;
import com.ibm.fhir.persistence.jdbc.util.ResourceTypesCacheUpdater;
import com.ibm.fhir.persistence.jdbc.util.SqlQueryData;
import com.ibm.fhir.persistence.util.InputOutputByteStream;
import com.ibm.fhir.schema.control.FhirSchemaConstants;

/**
 * This Data Access Object implements the ResourceDAO interface for creating, updating,
 * and retrieving rows in the IBM FHIR Server resource tables.
 */
public class ResourceDAOImpl extends FHIRDbDAOImpl implements ResourceDAO {

    private static final Logger log = Logger.getLogger(ResourceDAOImpl.class.getName());
    private static final String CLASSNAME = ResourceDAOImpl.class.getName();

    // Per issue with private memory in db2, we have set this to 1M.
    // Anything larger than 1M is then inserted into the db with an update.
    private static final String LARGE_BLOB = "UPDATE %s_RESOURCES SET DATA = ? WHERE RESOURCE_ID = ?";

    public static final String DEFAULT_VALUE_REINDEX_TSTAMP = "1970-01-01 00:00:00";

    // column indices for all our resource reading queries
    public static final int IDX_RESOURCE_ID = 1;
    public static final int IDX_LOGICAL_RESOURCE_ID = 2;
    public static final int IDX_VERSION_ID = 3;
    public static final int IDX_LAST_UPDATED = 4;
    public static final int IDX_IS_DELETED = 5;
    public static final int IDX_DATA = 6;
    public static final int IDX_LOGICAL_ID = 7;

    // Read the current version of the resource (even if the resource has been deleted)
    private static final String SQL_READ = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
            "FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
            "LR.LOGICAL_ID = ? AND R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID";

    // Read a specific version of the resource
    private static final String SQL_VERSION_READ =
            "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
                    "FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
                    "LR.LOGICAL_ID = ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID AND R.VERSION_ID = ?";

    // @formatter:off
    //                                                                                 0
    //                                                                                 1 2 3 4 5 6 7 8
    // @formatter:on
    // Don't forget that we must account for IN and OUT parameters.
    private static final String SQL_INSERT_WITH_PARAMETERS = "CALL %s.add_any_resource(?,?,?,?,?,?,?,?)";

    // Read version history of the resource identified by its logical-id
    private static final String SQL_HISTORY =
            "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
                    "FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
                    "LR.LOGICAL_ID = ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID " +
                    "ORDER BY R.VERSION_ID DESC ";

    // Count the number of versions we have for the resource identified by its logical-id
    private static final String SQL_HISTORY_COUNT = "SELECT COUNT(R.VERSION_ID) FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE LR.LOGICAL_ID = ? AND " +
            "R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID";

    private static final String SQL_HISTORY_FROM_DATETIME =
            "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
                    "FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE " +
                    "LR.LOGICAL_ID = ? AND R.LAST_UPDATED >= ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID " +
                    "ORDER BY R.VERSION_ID DESC ";

    private static final String SQL_HISTORY_FROM_DATETIME_COUNT =
            "SELECT COUNT(R.VERSION_ID) FROM %s_RESOURCES R, %s_LOGICAL_RESOURCES LR WHERE LR.LOGICAL_ID = ? AND " +
                    "R.LAST_UPDATED >= ? AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID";

    private static final String SQL_READ_ALL_RESOURCE_TYPE_NAMES = "SELECT RESOURCE_TYPE_ID, RESOURCE_TYPE FROM RESOURCE_TYPES";

    private static final String SQL_READ_RESOURCE_TYPE = "CALL %s.add_resource_type(?, ?)";

    private static final String SQL_SEARCH_BY_IDS =
            "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID " +
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
    private final IResourceReferenceDAO resourceReferenceDAO;

    private final FHIRPersistenceJDBCCache cache;

    private final ParameterTransactionDataImpl transactionData;

    /**
     * Constructs a DAO instance suitable for acquiring connections from a JDBC Datasource object.
     *
     * @param c
     * @param schemaName
     * @param flavor
     * @param trxSyncRegistry
     */
    public ResourceDAOImpl(Connection c, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry,
            FHIRPersistenceJDBCCache cache, IResourceReferenceDAO rrd, ParameterTransactionDataImpl ptdi) {
        super(c, schemaName, flavor);
        this.runningInTrx = true;
        this.trxSynchRegistry = trxSynchRegistry;
        this.cache = cache;
        this.resourceReferenceDAO = rrd;
        this.transactionData = ptdi;
    }

    /**
     * Constructs a DAO instance for use outside a managed transaction (JEE) environment
     *
     * @param c
     * @param schemaName
     * @param flavor
     */
    public ResourceDAOImpl(Connection c, String schemaName, FHIRDbFlavor flavor, FHIRPersistenceJDBCCache cache, IResourceReferenceDAO rrd) {
        super(c, schemaName, flavor);
        this.runningInTrx = false;
        this.trxSynchRegistry = null;
        this.cache = cache;
        this.resourceReferenceDAO = rrd;
        this.transactionData = null; // not supported outside JEE
    }

    /**
     * Getter for the IResourceReferenceDAO used by this ResourceDAO implementation
     *
     * @return
     */
    protected IResourceReferenceDAO getResourceReferenceDAO() {
        return this.resourceReferenceDAO;
    }

    /**
     * Get the ParameterTransactionDataImpl held by this.
     *
     * @return the transactionData object. Can be null.
     */
    protected ParameterTransactionDataImpl getTransactionData() {
        return this.transactionData;
    }

    @Override
    public Resource read(String logicalId, String resourceType) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
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
    public Resource versionRead(String logicalId, String resourceType, int versionId) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
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
     *
     * @param resultSet
     *            A ResultSet containing FHIR persistent object data.
     * @return Resource - A Resource DTO
     * @throws FHIRPersistenceDataAccessException
     */
    @Override
    protected Resource createDTO(ResultSet resultSet) throws FHIRPersistenceDataAccessException {
        final String METHODNAME = "createDTO";
        log.entering(CLASSNAME, METHODNAME);

        Resource resource = new Resource();

        try {
            byte[] payloadData = resultSet.getBytes(IDX_DATA);
            if (payloadData != null) {
                resource.setDataStream(new InputOutputByteStream(payloadData, payloadData.length));
            }
            resource.setId(resultSet.getLong(IDX_RESOURCE_ID));
            resource.setLogicalResourceId(resultSet.getLong(IDX_LOGICAL_RESOURCE_ID));
            resource.setLastUpdated(resultSet.getTimestamp(IDX_LAST_UPDATED));
            resource.setLogicalId(resultSet.getString(IDX_LOGICAL_ID));
            resource.setVersionId(resultSet.getInt(IDX_VERSION_ID));
            resource.setDeleted(resultSet.getString(IDX_IS_DELETED).equals("Y") ? true : false);
        } catch (Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure creating Resource DTO.");
            throw severe(log, fx, e);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }

        return resource;
    }

    @Override
    public List<Resource> history(String resourceType, String logicalId, Timestamp fromDateTime, int offset, int maxResults) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "history";
        log.entering(CLASSNAME, METHODNAME);

        List<Resource> resources = null;
        String stmtString = null;

        try {
            if (fromDateTime != null) {
                stmtString = String.format(SQL_HISTORY_FROM_DATETIME, resourceType, resourceType);
                if (isDb2Database()) {
                    stmtString = stmtString + DB2_PAGINATION_PARMS;
                    resources = this.runQuery(stmtString, logicalId, fromDateTime, maxResults, offset);
                } else {
                    stmtString = stmtString + DERBY_PAGINATION_PARMS;
                    resources = this.runQuery(stmtString, logicalId, fromDateTime, offset, maxResults);
                }
            } else {
                stmtString = String.format(SQL_HISTORY, resourceType, resourceType);
                if (isDb2Database()) {
                    stmtString = stmtString + DB2_PAGINATION_PARMS;
                    resources = this.runQuery(stmtString, logicalId, maxResults, offset);
                } else {
                    stmtString = stmtString + DERBY_PAGINATION_PARMS;
                    resources = this.runQuery(stmtString, logicalId, offset, maxResults);
                }
            }
        } finally {
            log.exiting(CLASSNAME, METHODNAME, Arrays.toString(new Object[] { resources }));
        }
        return resources;
    }

    @Override
    public int historyCount(String resourceType, String logicalId, Timestamp fromDateTime)
            throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
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
    public int searchCount(SqlQueryData queryData) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
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

        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        Map<String, Integer> result = new HashMap<>();
        long dbCallStartTime;
        double dbCallDuration;

        try {
            final Connection connection = this.getConnection();
            stmt = connection.prepareStatement(SQL_READ_ALL_RESOURCE_TYPE_NAMES);
            dbCallStartTime = System.nanoTime();
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                final int resourceTypeId = resultSet.getInt(1);
                final String resourceType = resultSet.getString(2);
                result.put(resourceType, resourceTypeId);
            }

            if (log.isLoggable(Level.FINE)) {
                dbCallDuration = (System.nanoTime() - dbCallStartTime) / 1e6;
                log.fine("DB read all resource type complete. executionTime=" + dbCallDuration + "ms");
            }
        } catch (Throwable e) {
            final String errMsg = "Failure retrieving all Resource type names.";
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException(errMsg);
            throw severe(log, fx, e);
        } finally {
            this.cleanup(stmt);
            log.exiting(CLASSNAME, METHODNAME);
        }

        return result;
    }

    @Override
    public Integer readResourceTypeId(String resourceType) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
        final String METHODNAME = "readResourceTypeId";
        log.entering(CLASSNAME, METHODNAME);

        final Connection connection = getConnection(); // do not close
        CallableStatement stmt = null;
        Integer parameterNameId = null;
        String stmtString;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            stmtString = String.format(SQL_READ_RESOURCE_TYPE, getSchemaName());
            stmt = connection.prepareCall(stmtString);
            stmt.setString(1, resourceType);
            stmt.registerOutParameter(2, Types.INTEGER);
            dbCallStartTime = System.nanoTime();
            stmt.execute();
            dbCallDuration = (System.nanoTime() - dbCallStartTime) / 1e6;
            if (log.isLoggable(Level.FINE)) {
                log.fine("DB read resource type id complete. executionTime=" + dbCallDuration + "ms");
            }
            parameterNameId = stmt.getInt(2);
        } catch (Throwable e) {
            final String errMsg = "Failure storing Resource type name id: name=" + resourceType;
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException(errMsg);
            throw severe(log, fx, e);
        } finally {
            this.cleanup(stmt);
            log.exiting(CLASSNAME, METHODNAME);
        }
        return parameterNameId;
    }

    @Override
    public List<Long> searchForIds(SqlQueryData queryData) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "searchForIds";
        log.entering(CLASSNAME, METHODNAME);

        List<Long> resourceIds = new ArrayList<>();
        Connection connection = getConnection(); // do not close
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            stmt = connection.prepareStatement(queryData.getQueryString());
            // Inject arguments into the prepared stmt.
            for (int i = 0; i < queryData.getBindVariables().size(); i++) {
                Object object = queryData.getBindVariables().get(i);
                if (object instanceof Timestamp) {
                    stmt.setTimestamp(i + 1, (Timestamp) object, JDBCConstants.UTC);
                } else {
                    stmt.setObject(i + 1, object);
                }
            }
            dbCallStartTime = System.nanoTime();
            resultSet = stmt.executeQuery();
            dbCallDuration = (System.nanoTime() - dbCallStartTime) / 1e6;
            if (log.isLoggable(Level.FINE)) {
                log.fine("DB search for ids complete. " + queryData + "  executionTime=" + dbCallDuration + "ms");
            }
            while (resultSet.next()) {
                resourceIds.add(resultSet.getLong(1));
            }
        } catch (Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure retrieving FHIR Resource Ids");
            final String errMsg = "Failure retrieving FHIR Resource Ids. SqlQueryData=" + queryData;
            throw severe(log, fx, errMsg, e);
        } finally {
            this.cleanup(resultSet, stmt);
            log.exiting(CLASSNAME, METHODNAME);
        }
        return resourceIds;
    }

    /**
     * Adds a resource type/ resource id pair to a candidate collection for population into the ResourceTypesCache.
     * This pair must be present as a row in the FHIR DB RESOURCE_TYPES table.
     *
     * @param resourceType
     *            A valid FHIR resource type.
     * @param resourceTypeId
     *            The corresponding id for the resource type.
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
                } catch (Throwable e) {
                    throw new FHIRPersistenceException("Failure registering ResourceTypesCacheUpdater", e);
                }
            }
            this.newResourceTypeIds.put(resourceType, resourceTypeId);
        }

        log.exiting(CLASSNAME, METHODNAME);

    }

    protected Integer getResourceTypeIdFromCaches(String resourceType) {
        // Get resourceTypeId from ResourceTypesCache first.
        Integer resourceTypeId = ResourceTypesCache.getResourceTypeId(resourceType);
        // If no found, then get resourceTypeId from local newResourceTypeIds in case this id is already in
        // newResourceTypeIds
        // but has not been updated to ResourceTypesCache yet. newResourceTypeIds is updated to ResourceTypesCache only
        // when the
        // current transaction is committed.
        if (resourceTypeId == null) {
            resourceTypeId = this.newResourceTypeIds.get(resourceType);
        }
        return resourceTypeId;
    }

    @Override
    public Resource insert(Resource resource, List<ExtractedParameterValue> parameters, String parameterHashB64, ParameterDAO parameterDao)
            throws FHIRPersistenceException {
        final String METHODNAME = "insert(Resource, List<ExtractedParameterValue>";
        log.entering(CLASSNAME, METHODNAME);

        final Connection connection = getConnection(); // do not close
        CallableStatement stmt = null;
        String stmtString = null;
        Integer resourceTypeId;
        Timestamp lastUpdated;
        boolean acquiredFromCache;
        long dbCallStartTime = System.nanoTime();

        try {
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

            stmtString = String.format(SQL_INSERT_WITH_PARAMETERS, getSchemaName());
            stmt = connection.prepareCall(stmtString);
            stmt.setString(1, resource.getResourceType());
            stmt.setString(2, resource.getLogicalId());

            // Check for large objects, and branch around it.
            boolean large = FhirSchemaConstants.STORED_PROCEDURE_SIZE_LIMIT < resource.getDataStream().size();
            if (large) {
                // Outside of the normal flow we have a BIG JSON or XML
                stmt.setNull(3, Types.BLOB);
            } else {
                // Normal Flow, we set the data
                stmt.setBinaryStream(3, resource.getDataStream().inputStream());
            }

            lastUpdated = resource.getLastUpdated();
            stmt.setTimestamp(4, lastUpdated, UTC);
            stmt.setString(5, resource.isDeleted() ? "Y": "N");
            stmt.setInt(6, resource.getVersionId());
            stmt.registerOutParameter(7, Types.BIGINT);
            stmt.registerOutParameter(8, Types.BIGINT);

            stmt.execute();
            long latestTime = System.nanoTime();
            double dbCallDuration = (latestTime-dbCallStartTime)/1e6;

            resource.setId(stmt.getLong(7));
            long versionedResourceRowId = stmt.getLong(8);
            if (large) {
                String largeStmtString = String.format(LARGE_BLOB, resource.getResourceType());
                try (PreparedStatement ps = connection.prepareStatement(largeStmtString)) {
                    // Use the long id to update the record in the database with the large object.
                    ps.setBinaryStream(1, resource.getDataStream().inputStream());
                    ps.setLong(2, versionedResourceRowId);
                    long dbCallStartTime2 = System.nanoTime();
                    int numberOfRows = -1;
                    ps.execute();
                    double dbCallDuration2 = (System.nanoTime() - dbCallStartTime2) / 1e6;
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("DB update large blob complete. ROWS=[" + numberOfRows + "] SQL=[" + largeStmtString + "]  executionTime=" + dbCallDuration2
                            + "ms");
                    }
                }
            }

            // Parameter time
            // TODO FHIR_ADMIN schema name needs to come from the configuration/context
            long paramInsertStartTime = latestTime;
            if (parameters != null) {
                JDBCIdentityCache identityCache = new JDBCIdentityCacheImpl(cache, this, parameterDao, getResourceReferenceDAO());
                try (ParameterVisitorBatchDAO pvd = new ParameterVisitorBatchDAO(connection, "FHIR_ADMIN", resource.getResourceType(), true,
                    resource.getId(), 100, identityCache, resourceReferenceDAO, this.transactionData)) {
                    for (ExtractedParameterValue p: parameters) {
                        p.accept(pvd);
                    }
                }
            }

            if (log.isLoggable(Level.FINE)) {
                latestTime = System.nanoTime();
                double totalDuration = (latestTime - dbCallStartTime) / 1e6;
                double paramInsertDuration = (latestTime-paramInsertStartTime)/1e6;
                log.fine("Successfully inserted Resource. id=" + resource.getId() + " total=" + totalDuration + "ms, proc=" + dbCallDuration + "ms, param=" + paramInsertDuration + "ms");
            }
        } catch (FHIRPersistenceDBConnectException |

                FHIRPersistenceDataAccessException e) {
            throw e;
        } catch (SQLIntegrityConstraintViolationException e) {
            FHIRPersistenceFKVException fx = new FHIRPersistenceFKVException("Encountered FK violation while inserting Resource.");
            throw severe(log, fx, e);
        } catch (SQLException e) {
            if ("99001".equals(e.getSQLState())) {
                // this is just a concurrency update, so there's no need to log the SQLException here
                throw new FHIRPersistenceVersionIdMismatchException("Encountered version id mismatch while inserting Resource");
            } else {
                FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("SQLException encountered while inserting Resource.");
                throw severe(log, fx, e);
            }
        } catch (Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure inserting Resource.");
            throw severe(log, fx, e);
        } finally {
            this.cleanup(stmt);
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
    public List<Resource> searchByIds(String resourceType, List<Long> resourceIds)
            throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "searchByIds";
        log.entering(CLASSNAME, METHODNAME);

        if (resourceIds.isEmpty()) {
            return Collections.emptyList();
        }

        final Connection connection = getConnection(); // do not close
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

            stmt = connection.prepareStatement(idQuery.toString());
            dbCallStartTime = System.nanoTime();
            resultSet = stmt.executeQuery();
            dbCallDuration = (System.nanoTime() - dbCallStartTime) / 1e6;
            if (log.isLoggable(Level.FINE)) {
                log.fine("DB search by ids complete. SQL=[" + idQuery + "]  executionTime=" + dbCallDuration + "ms");
            }
            resources = this.createDTOs(resultSet);
        } catch (FHIRPersistenceException e) {
            throw e;
        } catch (Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure retrieving FHIR Resources");
            errMsg = "Failure retrieving FHIR Resources. SQL=[" + idQuery + "]";
            throw severe(log, fx, errMsg, e);
        } finally {
            this.cleanup(resultSet, stmt);
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

    /**
     * Getter for access to the {@link FHIRPersistenceJDBCCache} from subclasses
     *
     * @return
     */
    protected FHIRPersistenceJDBCCache getCache() {
        return this.cache;
    }

    @Override
    public int searchCount(Select countQuery) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        return runCountQuery(countQuery);
   }

    @Override
    public List<Resource> search(Select select) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        return runQuery(select);
    }

    @Override
    public List<Long> searchForIds(Select dataQuery) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "searchForIds";
        log.entering(CLASSNAME, METHODNAME);

        List<Long> resourceIds = new ArrayList<>();
        Connection connection = getConnection(); // do not close
        ResultSet resultSet = null;
        long dbCallStartTime;
        double dbCallDuration;

        // QueryUtil creates a fully bound executable statement
        try (PreparedStatement stmt = QueryUtil.prepareSelect(connection, dataQuery, getTranslator())) {
            dbCallStartTime = System.nanoTime();
            resultSet = stmt.executeQuery();
            dbCallDuration = (System.nanoTime() - dbCallStartTime) / 1e6;
            if (log.isLoggable(Level.FINE)) {
                log.fine("DB search for ids complete. " + dataQuery.toString() + "  executionTime=" + dbCallDuration + "ms");
            }
            while (resultSet.next()) {
                resourceIds.add(resultSet.getLong(1));
            }
        } catch (Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure retrieving FHIR Resource Ids");
            final String errMsg = "Failure retrieving FHIR Resource Ids. SqlQueryData=" + dataQuery.toDebugString();
            throw severe(log, fx, errMsg, e);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return resourceIds;
    }

    /**
     * Delete any current parameters from the whole-system and resource-specific parameter tables
     * for the given resourcetype and logical_resource_id
     * @param conn
     * @param tablePrefix
     * @param v_logical_resource_id
     * @throws SQLException
     */
    protected void deleteFromParameterTables(Connection conn, String tablePrefix, long v_logical_resource_id) throws SQLException {
        deleteFromParameterTable(conn, tablePrefix + "_str_values", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_number_values", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_date_values", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_latlng_values", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_resource_token_refs", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_quantity_values", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_profiles", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_tags", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_security", v_logical_resource_id);

        // delete any system level parameters we have for this resource
        deleteFromParameterTable(conn, "str_values", v_logical_resource_id);
        deleteFromParameterTable(conn, "date_values", v_logical_resource_id);
        deleteFromParameterTable(conn, "resource_token_refs", v_logical_resource_id);
        deleteFromParameterTable(conn, "logical_resource_profiles", v_logical_resource_id);
        deleteFromParameterTable(conn, "logical_resource_tags", v_logical_resource_id);
        deleteFromParameterTable(conn, "logical_resource_security", v_logical_resource_id);
    }

    /**
     * Delete all parameters for the given resourceId from the named parameter value table
     * @param conn
     * @param tableName
     * @param logicalResourceId
     * @throws SQLException
     */
    protected void deleteFromParameterTable(Connection conn, String tableName, long logicalResourceId) throws SQLException {
        final String delStrValues = "DELETE FROM " + tableName + " WHERE logical_resource_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(delStrValues)) {
            // bind parameters
            stmt.setLong(1, logicalResourceId);
            stmt.executeUpdate();
        }
    }
}
/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.postgres;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.database.utils.common.CalendarHelper;
import com.ibm.fhir.persistence.InteractionStatus;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceVersionIdMismatchException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.FHIRDAOConstants;
import com.ibm.fhir.persistence.jdbc.dao.api.FhirRefSequenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.JDBCIdentityCacheImpl;
import com.ibm.fhir.persistence.jdbc.dao.impl.ParameterVisitorBatchDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import com.ibm.fhir.persistence.jdbc.dto.Resource;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceFKVException;
import com.ibm.fhir.persistence.jdbc.impl.ParameterTransactionDataImpl;
import com.ibm.fhir.persistence.jdbc.util.ParameterTableSupport;
import com.ibm.fhir.persistence.jdbc.util.ResourceTypesCache;

/**
 * Data access object for writing FHIR resources to a PostgreSQL database.
 * Like the Derby implementation, this version uses individual SQL statements
 * instead of a stored procedure and is useful for debugging performance and/or
 * concurrency issues. It should not be used in production scenarios because
 * the numerous app-database round-trips slow things down considerably.
 *
 * @implNote This class follows the logic of the stored procedure, but does so
 * using a series of individual JDBC statements.
 */
public class PostgresResourceNoProcDAO extends ResourceDAOImpl {
    private static final String CLASSNAME = PostgresResourceNoProcDAO.class.getSimpleName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    private static final String SQL_READ_RESOURCE_TYPE = "{CALL %s.add_resource_type(?, ?)}";

    // DAO used to obtain sequence values from FHIR_REF_SEQUENCE
    private FhirRefSequenceDAO fhirRefSequenceDAO;

    public PostgresResourceNoProcDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, FHIRPersistenceJDBCCache cache, IResourceReferenceDAO rrd) {
        super(connection, schemaName, flavor, cache, rrd);
    }

    public PostgresResourceNoProcDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry, FHIRPersistenceJDBCCache cache, IResourceReferenceDAO rrd,
        ParameterTransactionDataImpl ptdi) {
        super(connection, schemaName, flavor, trxSynchRegistry, cache, rrd, ptdi);
    }

    @Override
    public Resource insert(Resource resource, List<ExtractedParameterValue> parameters, String parameterHashB64, 
            ParameterDAO parameterDao, Integer ifNoneMatch)
            throws FHIRPersistenceException {
        final String METHODNAME = "insert";
        logger.entering(CLASSNAME, METHODNAME);

        final Connection connection = getConnection(); // do not close
        Integer resourceTypeId;
        Timestamp lastUpdated;
        boolean acquiredFromCache;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            resourceTypeId = getResourceTypeIdFromCaches(resource.getResourceType());
            if (resourceTypeId == null) {
                acquiredFromCache = false;
                resourceTypeId = getOrCreateResourceType(resource.getResourceType(), connection);
                this.addResourceTypeCacheCandidate(resource.getResourceType(), resourceTypeId);
            } else {
                acquiredFromCache = true;
            }

            if (logger.isLoggable(Level.FINE)) {
                logger.fine("resourceType=" + resource.getResourceType() + "  resourceTypeId=" + resourceTypeId +
                         "  acquiredFromCache=" + acquiredFromCache + "  tenantDatastoreCacheName=" + ResourceTypesCache.getCacheNameForTenantDatastore());
            }

            lastUpdated = resource.getLastUpdated();
            dbCallStartTime = System.nanoTime();

            final String sourceKey = UUID.randomUUID().toString();
            
            // to mimic out parameters from the stored procedure
            AtomicInteger outInteractionStatus = new AtomicInteger();
            AtomicInteger outIfNoneMatchVersion  = new AtomicInteger();

            long resourceId = this.storeResource(resource.getResourceType(),
                parameters,
                resource.getLogicalId(),
                resource.getDataStream().inputStream(),
                lastUpdated,
                resource.isDeleted(),
                sourceKey,
                resource.getVersionId(),
                parameterHashB64,
                connection,
                parameterDao,
                ifNoneMatch,
                outInteractionStatus,
                outIfNoneMatchVersion
                );


            dbCallDuration = (System.nanoTime() - dbCallStartTime)/1e6;

            if (outInteractionStatus.get() == 1) {
                resource.setInteractionStatus(InteractionStatus.IF_NONE_MATCH_EXISTED);
                resource.setIfNoneMatchVersion(outIfNoneMatchVersion.get());
            } else {
                resource.setInteractionStatus(InteractionStatus.MODIFIED);
                resource.setId(resourceId);
            }

            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Successfully inserted Resource. id=" + resource.getId() + " executionTime=" + dbCallDuration + "ms");
            }
        } catch(FHIRPersistenceDBConnectException | FHIRPersistenceDataAccessException e) {
            throw e;
        } catch(SQLIntegrityConstraintViolationException e) {
            FHIRPersistenceFKVException fx = new FHIRPersistenceFKVException("Encountered FK violation while inserting Resource.");
            throw severe(logger, fx, e);
        } catch(SQLException e) {
            if (FHIRDAOConstants.SQLSTATE_WRONG_VERSION.equals(e.getSQLState())) {
                // this is just a concurrency update, so there's no need to log the SQLException here
                throw new FHIRPersistenceVersionIdMismatchException("Encountered version id mismatch while inserting Resource");
            } else {
                FHIRPersistenceException fx = new FHIRPersistenceException("SQLException encountered while inserting Resource.");
                throw severe(logger, fx, e);
            }
        } catch(Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure inserting Resource.");
            throw severe(logger, fx, e);
        } finally {
            logger.exiting(CLASSNAME, METHODNAME);
        }

        return resource;

    }

    /**
     * Store the resource in the database, creating a new logical_resource entry if this is
     * the first version of this resource, or creating a new resource entry if this a new
     * version of an existing logical resource. The logic tracks closely the DB2 stored
     * procedure implementation, including locking of the logical_resource and handling
     * concurrency issues using the standard insert-or-update pattern:
     * <pre>
     *   SELECT FOR UPDATE                 -- try and get a write lock
     *   IF NOT FOUND THEN                 -- doesn't exist, so we don't have a lock
     *     INSERT new logical resource     -- create the record - if OK, we own the lock
     *     IF DUPLICATE THEN               -- someone else beat us to the create
     *       SELECT FOR UPDATE             -- so we need to try again for a write lock
     *     ...
     *   ...
     * </pre>
     *
     * This works because we never delete a logical_resource record, and so don't have to deal
     * with concurrency issues caused when deletes are mingled with inserts/updates
     *
     * Note the execution flow aligns very closely with the DB2 stored procedure
     * implementation (fhir-persistence-schema/src/main/resources/add_any_resource.sql)
     * @param tablePrefix
     * @param parameters
     * @param p_logical_id
     * @param p_payload
     * @param p_last_updated
     * @param p_is_deleted
     * @param p_source_key
     * @param p_version
     * @param parameterHashB64
     * @param conn
     * @param parameterDao
     * @return the resource_id for the entry we created
     * @throws Exception
     */
    public long storeResource(String tablePrefix, List<ExtractedParameterValue> parameters, String p_logical_id, 
            InputStream p_payload, Timestamp p_last_updated, boolean p_is_deleted,
            String p_source_key, Integer p_version, String parameterHashB64, Connection conn, 
            ParameterDAO parameterDao, Integer ifNoneMatch, 
            AtomicInteger outInteractionStatus, AtomicInteger outIfNoneMatchVersion) throws Exception {

        final String METHODNAME = "storeResource() for " + tablePrefix + " resource";
        logger.entering(CLASSNAME, METHODNAME);

        final Calendar UTC = CalendarHelper.getCalendarForUTC();
        Long v_logical_resource_id = null;
        Long v_current_resource_id = null;
        Long v_resource_id = null;
        Integer v_resource_type_id = null;
        boolean v_new_resource = false;
        boolean v_not_found = false;
        boolean v_duplicate = false;
        int v_current_version = 0;
        String currentHash = null;

        String v_resource_type = tablePrefix;

        // Map the resource type name to the normalized id value in the database
        v_resource_type_id = getResourceTypeId(v_resource_type, conn);
        if (v_resource_type_id == null) {
            // programming error, as this should've been created earlier
            throw new IllegalStateException("resource type not found: " + v_resource_type);
        }

        // Get a lock at the system-wide logical resource level. Note the PostgreSQL-specific syntax
        final String SELECT_FOR_UPDATE = "SELECT logical_resource_id, parameter_hash FROM logical_resources WHERE resource_type_id = ? AND logical_id = ? FOR NO KEY UPDATE";
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_FOR_UPDATE)) {
            stmt.setInt(1, v_resource_type_id);
            stmt.setString(2, p_logical_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                v_logical_resource_id = rs.getLong(1);
                currentHash = rs.getString(2);
            }
            else {
                v_not_found = true;
                v_logical_resource_id = -1L; // just to be careful
            }
        }

        // Create the logical resource if we don't have it already
        if (v_not_found) {
            // grab the id we want to use for the new logical resource instance
            final String sql2 = "SELECT nextval('fhir_sequence')";
            try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
                ResultSet res = stmt.executeQuery();
                if (res.next()) {
                    v_logical_resource_id = res.getLong(1);
                }
                else {
                    // not going to happen, unless someone butchers the statement being executed
                    throw new IllegalStateException("VALUES failed to return a row: " + sql2);
                }
            }

            // insert the system-wide logical resource record.
            final String sql3 = "INSERT INTO logical_resources (logical_resource_id, resource_type_id, logical_id, reindex_tstamp, parameter_hash) VALUES (?, ?, ?, ?, ?) "
                    + " ON CONFLICT DO NOTHING"
                    + " RETURNING logical_resource_id";
            try (PreparedStatement stmt = conn.prepareStatement(sql3)) {
                // bind parameters
                stmt.setLong(1, v_logical_resource_id);
                stmt.setInt(2, v_resource_type_id);
                stmt.setString(3, p_logical_id);
                stmt.setTimestamp(4, Timestamp.valueOf(DEFAULT_VALUE_REINDEX_TSTAMP), UTC);
                stmt.setString(5, parameterHashB64);
                stmt.execute();

                ResultSet rs = stmt.getResultSet();
                if (rs == null || !rs.next()) {
                    // nothing returned, so there was a conflict
                    v_duplicate = true;
                }
            }

            /**
             * remember that we have a concurrent system...so there is a possibility
             * that another thread snuck in before us and created the logical resource. This
             * is easy to handle, just turn around and read it
             */
            if (v_duplicate) {
                try (PreparedStatement stmt = conn.prepareStatement(SELECT_FOR_UPDATE)) {
                    // bind parameters
                    stmt.setInt(1, v_resource_type_id);
                    stmt.setString(2, p_logical_id);
                    ResultSet res = stmt.executeQuery();
                    if (res.next()) {
                        v_logical_resource_id = res.getLong(1);
                        currentHash = res.getString(2);
                    }
                    else {
                        // Extremely unlikely as we should never delete logical resource records
                        throw new IllegalStateException("Logical resource was deleted: " + tablePrefix + "/" + p_logical_id);
                    }
                }
            }
            else {
                v_new_resource = true;

                // Insert the resource-specific logical resource record. Remember that logical_id is denormalized
                // so it gets stored again here for convenience
                final String INSERT_LR = "INSERT INTO " + tablePrefix + "_logical_resources (logical_resource_id, logical_id, is_deleted, last_updated, version_id) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(INSERT_LR)) {
                    // bind parameters
                    stmt.setLong(1, v_logical_resource_id);
                    stmt.setString(2, p_logical_id);
                    stmt.setString(3, p_is_deleted ? "Y" : "N");
                    stmt.setTimestamp(4, p_last_updated, UTC);
                    stmt.setInt(5, p_version); // initial version
                    stmt.executeUpdate();
                }
            }
        }

        if (!v_new_resource) {
            // existing resource.  We need to know the current version from the
            // resource-specific logical resources table.
            final String sql3 = "SELECT current_resource_id, version_id FROM " + tablePrefix + "_logical_resources WHERE logical_resource_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql3)) {
                stmt.setLong(1, v_logical_resource_id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    v_current_resource_id = rs.getLong(1);
                    v_current_version = rs.getInt(2);
                }
                else {
                    // This database is broken, because we shouldn't have logical_resource records without
                    // corresponding resource-specific logical_resource records.
                    throw new SQLException("Logical_id record '" + p_logical_id + "' missing for resource " + tablePrefix);
                }
            }

            if (checkIfNoneMatch(ifNoneMatch, v_current_version)) {
                // Conditional create-on-update If-None-Match matches the current resource, so skip the update
                logger.fine(() -> "Resource " + v_resource_type + "/" + p_logical_id + " [" + p_version + "] matches [If-None-Match: " + ifNoneMatch + "]");
                outInteractionStatus.set(1);
                outIfNoneMatchVersion.set(v_current_version);
                return -1L;
            } else if (p_version != v_current_version + 1) {
                // Concurrency check:
                //   the version parameter we've been given (which is also embedded in the JSON payload) must be
                //   one greater than the current version, otherwise we've hit a concurrent update race condition
                // mimic the exception we'd see from one of our stored procedures
                throw new SQLException("Concurrent update - mismatch of version in JSON", FHIRDAOConstants.SQLSTATE_WRONG_VERSION);
            }

            // existing resource, so need to delete all its parameters
            if (currentHash == null || currentHash.isEmpty() || !currentHash.equals(parameterHashB64)) {
                ParameterTableSupport.deleteFromParameterTables(conn, tablePrefix, v_logical_resource_id);
            }

            // For schema V0014, now we also need to update the is_deleted and last_updated values
            // in LOGICAL_RESOURCES to support whole-system search
            final String sql4b = "UPDATE logical_resources SET is_deleted = ?, last_updated = ?, parameter_hash = ? WHERE logical_resource_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql4b)) {
                // bind parameters
                stmt.setString(1, p_is_deleted ? "Y" : "N");
                stmt.setTimestamp(2, p_last_updated, UTC);
                stmt.setString(3, parameterHashB64);
                stmt.setLong(4, v_logical_resource_id);
                stmt.executeUpdate();
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("Updated logical_resources: " + v_resource_type + "/" + p_logical_id);
                }
            }
        }

        /**
         * Create the new resource version.
         * Alpha version uses last_updated time from the app-server, so we keep that here
         */
        final String sql2 = "SELECT nextval('fhir_sequence')";
        try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                v_resource_id = res.getLong(1); // Assign result of the above query
            } else {
                // unlikely
                throw new IllegalStateException("no row returned: " + sql2);
            }
        }

        // Finally we get to the big resource data insert
        String sql3 = "INSERT INTO " + tablePrefix + "_resources (resource_id, logical_resource_id, version_id, data, last_updated, is_deleted) "
                + "VALUES (?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql3)) {
            // bind parameters
            stmt.setLong(1, v_resource_id);
            stmt.setLong(2, v_logical_resource_id);
            stmt.setInt(3, p_version);
            stmt.setBinaryStream(4, p_payload);
            stmt.setTimestamp(5, p_last_updated, UTC);
            stmt.setString(6, p_is_deleted ? "Y" : "N");
            stmt.executeUpdate();
        }

        // update the logical resource with the values for the latest version
        String sql4 = "UPDATE " + tablePrefix + "_logical_resources SET current_resource_id = ?, is_deleted = ?, last_updated = ?, version_id = ? WHERE logical_resource_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql4)) {
            // bind parameters
            stmt.setLong(1, v_resource_id);
            stmt.setString(2, p_is_deleted ? "Y" : "N");
            stmt.setTimestamp(3, p_last_updated, UTC);
            stmt.setInt(4, p_version);
            stmt.setLong(5, v_logical_resource_id);
            stmt.executeUpdate();
        }

        // Note we don't get any parameters for the resource soft-delete operation
        if (parameters != null && (currentHash == null || currentHash.isEmpty() || !currentHash.equals(parameterHashB64))) {
            // PostgreSQL doesn't support partitioned multi-tenancy, so we disable it on the DAO:
            JDBCIdentityCache identityCache = new JDBCIdentityCacheImpl(getCache(), this, parameterDao, getResourceReferenceDAO());
            try (ParameterVisitorBatchDAO pvd = new ParameterVisitorBatchDAO(conn, null, tablePrefix, false, v_logical_resource_id, 100,
                identityCache, getResourceReferenceDAO(), getTransactionData())) {
                for (ExtractedParameterValue p: parameters) {
                    p.accept(pvd);
                }
            }
        }

        // Finally, write a record to RESOURCE_CHANGE_LOG which records each event
        // related to resources changes (issue-1955)
        String changeType = p_is_deleted ? "D" : v_new_resource ? "C" :  "U";
        String INSERT_CHANGE_LOG = "INSERT INTO resource_change_log(resource_id, change_tstamp, resource_type_id, logical_resource_id, version_id, change_type)"
                + " VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(INSERT_CHANGE_LOG)) {
            ps.setLong(     1, v_resource_id);
            ps.setTimestamp(2, p_last_updated, UTC);
            ps.setInt(      3, v_resource_type_id);
            ps.setLong(     4, v_logical_resource_id);
            ps.setInt(      5, p_version);
            ps.setString(   6, changeType);
            ps.executeUpdate();
        }

        logger.exiting(CLASSNAME, METHODNAME);
        return v_resource_id;
    }

    /**
     * Read the id for the named type
     * @param resourceTypeName
     * @return the database id, or null if the named record is not found
     * @throws SQLException
     */
    protected Integer getResourceTypeId(String resourceTypeName, Connection conn) throws SQLException {
        Integer result = null;

        final String sql = "SELECT resource_type_id FROM resource_types WHERE resource_type = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resourceTypeName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        }

        return result;
    }

    /**
     * stored-procedure-less implementation for managing the resource_types table
     * @param resourceTypeName
     * @throw SQLException
     */
    public int getOrCreateResourceType(String resourceTypeName, Connection conn) throws SQLException {
        // As the system is concurrent, we have to handle cases where another thread
        // might create the entry after we selected and found nothing
        Integer result = getResourceTypeId(resourceTypeName, conn);

        // Create the resource if we don't have it already (set by the continue handler)
        if (result == null) {
            try {
                result = fhirRefSequenceDAO.nextValue();
                final String INS = "INSERT INTO resource_types (resource_type_id, resource_type) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(INS)) {
                    // bind parameters
                    stmt.setInt(1, result);
                    stmt.setString(2, resourceTypeName);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                throw e;
            }
        }

        return result;
    }


    @Override
    public Integer readResourceTypeId(String resourceType) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException  {
        final String METHODNAME = "readResourceTypeId";
        logger.entering(CLASSNAME, METHODNAME);

        final Connection connection = getConnection(); // do not close
        CallableStatement stmt = null;
        Integer resourceTypeId = null;
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
            dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("DB read resource type id complete. executionTime=" + dbCallDuration + "ms");
            }
            resourceTypeId = stmt.getInt(2);
        } catch (Throwable e) {
            final String errMsg = "Failure storing Resource type name id: name=" + resourceType;
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException(errMsg);
            throw severe(logger, fx, e);
        } finally {
            cleanup(stmt);
            logger.exiting(CLASSNAME, METHODNAME);
        }
        return resourceTypeId;
    }
}

/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.derby;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.database.utils.common.CalendarHelper;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.persistence.InteractionStatus;
import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceVersionIdMismatchException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.FHIRDAOConstants;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.JDBCIdentityCacheImpl;
import com.ibm.fhir.persistence.jdbc.dao.impl.ParameterVisitorBatchDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import com.ibm.fhir.persistence.jdbc.dto.Resource;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceFKVException;
import com.ibm.fhir.persistence.jdbc.impl.ParameterTransactionDataImpl;
import com.ibm.fhir.persistence.jdbc.util.ParameterTableSupport;

/**
 * Data access object for writing FHIR resources to an Apache Derby database.
 *
 * @implNote The original implementation (for DSTU2) used a global temporary table
 * to pass the parameter list into the stored procedure, but this approach
 * exposed some query optimizer issues in DB2 resulting in significant
 * concurrency problems (related to dynamic statistics collection and
 * query compilation). The solution used row type arrays instead, but these
 * aren't supported in Derby, and have since been replaced by a DAO-based
 * batch statements due to issues with dynamic SQL and array types in DB2.
 * <br>
 * So this class follows the logic of the stored procedure, but does so
 * using a series of individual JDBC statements.
 */
public class DerbyResourceDAO extends ResourceDAOImpl {
    private static final Logger logger = Logger.getLogger(DerbyResourceDAO.class.getName());
    private static final String CLASSNAME = DerbyResourceDAO.class.getSimpleName();
    private static final int INTERACTION_STATUS_IF_NONE_MATCH = 1;

    private static final DerbyTranslator translator = new DerbyTranslator();

    public DerbyResourceDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, FHIRPersistenceJDBCCache cache, IResourceReferenceDAO rrd) {
        super(connection, schemaName, flavor, cache, rrd);
    }

    /**
     * Derby is not only used for unit tests, but can also be used to provide persistence
     * for a stand-alone full FHIR server.
     * @param strat the connection strategy
     * @param trxSynchRegistry
     */
    public DerbyResourceDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry, FHIRPersistenceJDBCCache cache, IResourceReferenceDAO rrd, ParameterTransactionDataImpl ptdi) {
        super(connection, schemaName, flavor, trxSynchRegistry, cache, rrd, ptdi);
    }

    @Override
    public Resource insert(Resource resource, List<ExtractedParameterValue> parameters, String parameterHashB64, ParameterDAO parameterDao,
            Integer ifNoneMatch)
            throws FHIRPersistenceException {
        final String METHODNAME = "insert";
        logger.entering(CLASSNAME, METHODNAME);

        final Connection connection = getConnection(); // do not close
        Timestamp lastUpdated;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            // check that the resource type is valid according to the database
            Objects.requireNonNull(getResourceTypeId(resource.getResourceType()));

            lastUpdated = resource.getLastUpdated();
            dbCallStartTime = System.nanoTime();

            final String sourceKey = UUID.randomUUID().toString();

            // to mimic out parameters from the stored procedure
            AtomicInteger outInteractionStatus = new AtomicInteger();
            AtomicInteger outIfNoneMatchVersion  = new AtomicInteger();

            long resourceId = this.storeResource(resource.getResourceType(),
                parameters,
                resource.getLogicalId(),
                resource.getDataStream() != null ? resource.getDataStream().inputStream() : null,
                lastUpdated,
                resource.isDeleted(),
                sourceKey,
                resource.getVersionId(),
                parameterHashB64,
                connection,
                parameterDao,
                ifNoneMatch,
                resource.getResourcePayloadKey(),
                outInteractionStatus,
                outIfNoneMatchVersion
                );

            dbCallDuration = (System.nanoTime() - dbCallStartTime)/1e6;

            if (outInteractionStatus.get() == INTERACTION_STATUS_IF_NONE_MATCH) {
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
                // this is just a concurrent update, so there's no need to log the SQLException here
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
     * implementation (fhir-persistence-schema/src/main/resources/db2/add_any_resource.sql)
     *
     * @param tablePrefix
     * @param parameters
     * @param p_logical_id
     * @param p_payload
     * @param p_last_updated
     * @param p_is_deleted
     * @param p_source_key
     * @param p_version
     * @param p_parameterHashB64
     * @param conn
     * @param parameterDao
     * @param ifNoneMatch 0 for conditional create-on-update behavior; otherwise null
     * @param resourcePayloadKey
     * @param outInteractionStatus
     * @param outIfNoneMatchVersion
     * @return the resource_id for the entry we created
     * @throws Exception
     */
    public long storeResource(String tablePrefix, List<ExtractedParameterValue> parameters,
            String p_logical_id, InputStream p_payload, Timestamp p_last_updated, boolean p_is_deleted,
            String p_source_key, Integer p_version, String p_parameterHashB64, Connection conn,
            ParameterDAO parameterDao, Integer ifNoneMatch, String resourcePayloadKey,
            AtomicInteger outInteractionStatus, AtomicInteger outIfNoneMatchVersion) throws Exception {

        final Calendar UTC = CalendarHelper.getCalendarForUTC();
        final String METHODNAME = "storeResource() for " + tablePrefix + " resource";
        logger.entering(CLASSNAME, METHODNAME);

        Long v_logical_resource_id = null;
        Long v_resource_id = null;
        Integer v_resource_type_id = null;
        boolean v_new_resource = false;
        boolean v_not_found = false;
        boolean v_duplicate = false;
        boolean v_currently_deleted = false;
        int v_current_version;

        // used to bypass param delete/insert if all param values are the same
        String currentParameterHash = null;
        boolean requireParameterUpdate = true;

        String v_resource_type = tablePrefix;

        // Map the resource type name to the normalized id value in the database
        v_resource_type_id = getResourceTypeId(v_resource_type, conn);
        if (v_resource_type_id == null) {
            // programming error, as this should've been created earlier
            throw new IllegalStateException("resource type not found: " + v_resource_type);
        }

        // Get a value for the new resource_id we'll be storing later
        final String sql2 = "VALUES (NEXT VALUE FOR fhir_sequence)";
        try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    v_resource_id = res.getLong(1); //Assign result of the above query
                    res.next();
                }
                else {
                    // unlikely
                    throw new IllegalStateException("no row returned: " + sql2);
                }
            }
        }

        if (logger.isLoggable(Level.FINEST)) {
            // Debug locking...
            DerbyMaster.dumpLockInfo(conn);
        }

        // Get a lock at the system-wide logical resource level. Note the Derby-specific syntax
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("Getting LOGICAL_RESOURCE_IDENT row lock for: " + v_resource_type + "/" + p_logical_id);
        }
        final String SELECT_FOR_UPDATE = "SELECT logical_resource_id"
                + " FROM logical_resource_ident "
                + " WHERE resource_type_id = ? AND logical_id = ?"
                + " FOR UPDATE WITH RS";
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_FOR_UPDATE)) {
            stmt.setInt(1, v_resource_type_id);
            stmt.setString(2, p_logical_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("Resource locked: " + v_resource_type + "/" + p_logical_id);
                }
                v_logical_resource_id = rs.getLong(1);
            }
            else {
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("Resource not found: " + v_resource_type + "/" + p_logical_id);
                }
                v_not_found = true;
                v_logical_resource_id = -1L; // just to be careful
            }
        }

        // Create the logical resource if we don't have it already
        if (v_not_found) {
            // grab the id we want to use for the new logical resource instance
            final String sql3 = "VALUES(NEXT VALUE FOR fhir_sequence)";
            try (PreparedStatement stmt = conn.prepareStatement(sql3)) {
                try (ResultSet res = stmt.executeQuery()) {
                    if (res.next()) {
                        v_logical_resource_id = res.getLong(1);
                    }
                    else {
                        // not going to happen, unless someone butchers the statement being executed
                        throw new IllegalStateException("VALUES failed to return a row: " + sql3);
                    }
                }
            }
            // insert the logical_resource_ident record (which we now do our locking on)
            final String INS_IDENT = "INSERT INTO logical_resource_ident (resource_type_id, logical_id, logical_resource_id) VALUES (?, ?, ?)";
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("Creating new logical_resource_ident row for: " + v_resource_type + "/" + p_logical_id);
            }

            try (PreparedStatement stmt = conn.prepareStatement(INS_IDENT)) {
                // bind parameters
                stmt.setInt(1, v_resource_type_id);
                stmt.setString(2, p_logical_id);
                stmt.setLong(3, v_logical_resource_id);
                stmt.executeUpdate();

                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("Created logical_resource_ident row for: " + v_resource_type + "/" + p_logical_id);
                }
            } catch (SQLException e) {
                if (translator.isDuplicate(e)) {
                    if (logger.isLoggable(Level.FINEST)) {
                        logger.finest("Resource already exists - duplicate: " + v_resource_type + "/" + p_logical_id);
                    }
                    v_duplicate = true;
                }
                else {
                    throw e;
                }
            }

            /**
             * remember that we have a concurrent system...so there is a possibility
             * that another thread snuck in before us and created the logical resource. This
             * is easy to handle, just turn around and read it
             */
            if (v_duplicate) {
                try (PreparedStatement stmt = conn.prepareStatement(SELECT_FOR_UPDATE)) {
                    if (logger.isLoggable(Level.FINEST)) {
                        logger.finest("Locking existing resource: " + v_resource_type + "/" + p_logical_id);
                    }
                    // bind parameters
                    stmt.setInt(1, v_resource_type_id);
                    stmt.setString(2, p_logical_id);
                    try (ResultSet res = stmt.executeQuery()) {
                        if (res.next()) {
                            if (logger.isLoggable(Level.FINEST)) {
                                logger.finest("Resource locked: " + v_resource_type + "/" + p_logical_id);
                            }
                            v_logical_resource_id = res.getLong(1);
                        } else {
                            // Extremely unlikely as we should never delete logical resource records
                            throw new IllegalStateException("Logical resource was deleted: " + tablePrefix + "/" + p_logical_id);
                        }
                    }
                }
            }
        }

        // At this point we have an exclusive lock at the logical resource level, so we
        // no longer have to worry about concurrency issues. Let's see if we have a
        // logical_resources entry:
        final String SELECT_LOGICAL_RESOURCE = ""
                + "SELECT parameter_hash, is_deleted"
                + "  FROM logical_resources "
                + " WHERE logical_resource_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_LOGICAL_RESOURCE)) {
            stmt.setLong(1, v_logical_resource_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("Found logical_resources record for: " + v_resource_type + "/" + p_logical_id);
                }
                currentParameterHash = rs.getString(1);
                v_currently_deleted = "Y".equals(rs.getString(2));
                v_not_found = false;
            }
            else {
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("Logical_resources record not found for: " + v_resource_type + "/" + p_logical_id);
                }
                v_not_found = true;
            }
        }

        if (v_not_found) {
            // insert the system-wide logical resource record
            final String sql4 = "INSERT INTO logical_resources (logical_resource_id, resource_type_id, logical_id, reindex_tstamp, is_deleted, last_updated, parameter_hash) VALUES (?, ?, ?, ?, ?, ?, ?)";
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("Creating new logical_resources row for: " + v_resource_type + "/" + p_logical_id);
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql4)) {
                // bind parameters
                stmt.setLong(1, v_logical_resource_id);
                stmt.setInt(2, v_resource_type_id);
                stmt.setString(3, p_logical_id);
                stmt.setTimestamp(4, Timestamp.valueOf(DEFAULT_VALUE_REINDEX_TSTAMP), UTC);
                stmt.setString(5, p_is_deleted ? "Y" : "N"); // from V0014
                stmt.setTimestamp(6, p_last_updated, UTC);   // from V0014
                stmt.setString(7, p_parameterHashB64);       // from V0015
                stmt.executeUpdate();
                
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("Created logical_resources row for: " + v_resource_type + "/" + p_logical_id);
                }

                v_new_resource = true;
            }

            // Insert the resource-specific logical resource record. Remember that logical_id is denormalized
            // so it gets stored again here for convenience
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("Creating " + tablePrefix + "_logical_resources row: " + v_resource_type + "/" + p_logical_id);
            }
            final String sql5 = "INSERT INTO " + tablePrefix + "_logical_resources (logical_resource_id, logical_id, is_deleted, last_updated, version_id, current_resource_id) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql5)) {
                // bind parameters
                stmt.setLong(1, v_logical_resource_id);
                stmt.setString(2, p_logical_id);
                stmt.setString(3, p_is_deleted ? "Y" : "N");
                stmt.setTimestamp(4, p_last_updated, UTC);
                stmt.setInt(5, p_version); // initial version
                stmt.setLong(6, v_resource_id);
                stmt.executeUpdate();
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("Created " + tablePrefix + "_logical_resources row: " + v_resource_type + "/" + p_logical_id);
                }
            }

        }

        // For existing resources, we need to know the current resource version_id
        if (!v_new_resource) {
            // existing resource.  We need to know the current version from the
            // resource-specific logical resources table.
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("Getting version info from " + tablePrefix + "_logical_resources for: " + v_resource_type + "/" + p_logical_id);
            }
            final String sql3 = "SELECT version_id"
                    + " FROM " + tablePrefix + "_logical_resources"
                    + " WHERE logical_resource_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql3)) {
                stmt.setLong(1, v_logical_resource_id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        v_current_version = rs.getInt(1);
                        if (logger.isLoggable(Level.FINEST)) {
                            logger.finest("Current version: " + v_resource_type + "/" + p_logical_id + " = " + v_current_version);
                        }
                    }
                    else {
                        // This database is broken, because we shouldn't have logical_resource records without
                        // corresponding resource-specific logical_resource records.
                        throw new SQLException("Logical_id record '" + p_logical_id + "' missing for resource " + tablePrefix);
                    }
                }
            }

            // If-None-Match doesn't apply if the current record is deleted
            if (!v_currently_deleted && checkIfNoneMatch(ifNoneMatch, v_current_version)) {
                // Conditional create-on-update If-None-Match matches the current resource, so skip the update
                logger.fine(() -> "Resource " + v_resource_type + "/" + p_logical_id + " [" + p_version + "] matches [If-None-Match: " + ifNoneMatch + "]");
                outInteractionStatus.set(INTERACTION_STATUS_IF_NONE_MATCH);
                outIfNoneMatchVersion.set(v_current_version);
                return -1L;
            } else if (p_version != v_current_version + 1) {
                // Concurrency check:
                //   the version parameter we've been given (which is also embedded in the JSON payload) must be
                //   one greater than the current version, otherwise we've hit a concurrent update race condition
                // mimic the exception we'd see from one of our stored procedures
                logger.warning("Concurrent update of resource: " + v_resource_type + "/" + p_logical_id + " [" + p_version + " != " + (v_current_version+1) + "]");
                throw new SQLException("Concurrent update - mismatch of version in JSON", FHIRDAOConstants.SQLSTATE_WRONG_VERSION);
            } else if (v_currently_deleted && p_is_deleted) {
                // Cannot delete a resource which is currently deleted. This is an extra
                // safety check and primarily used to handle unit tests which may attempt
                // to double-delete a resource. Should not be relevant to the REST layer,
                // unless someone breaks it.
                logger.warning("Cannot delete a resource which is currently deleted: " + v_resource_type + "/" + p_logical_id + " [" + p_version + "]");
                throw new SQLException("Cannot delete a resource which is currently deleted", FHIRDAOConstants.SQLSTATE_CURRENTLY_DELETED);
            }

            // existing resource, so need to delete all its parameters unless they share
            // an identical hash, in which case we can bypass the delete/insert
            requireParameterUpdate = currentParameterHash == null || currentParameterHash.isEmpty() || !currentParameterHash.equals(p_parameterHashB64);
            if (requireParameterUpdate) {
                ParameterTableSupport.deleteFromParameterTables(conn, tablePrefix, v_logical_resource_id);
            }
        }

        // Finally we get to the big resource data insert
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("Creating " + tablePrefix + "_resources row: " + v_resource_type + "/" + p_logical_id);
        }
        String sql3 = "INSERT INTO " + tablePrefix + "_resources (resource_id, logical_resource_id, version_id, data, last_updated, is_deleted, resource_payload_key) "
                + "VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql3)) {
            // bind parameters
            stmt.setLong(1, v_resource_id);
            stmt.setLong(2, v_logical_resource_id);
            stmt.setInt(3, p_version);

            if (p_payload != null) {
                stmt.setBinaryStream(4, p_payload);
            } else {
                // payload offloaded to another data store
                stmt.setNull(4, Types.BLOB);
            }
            stmt.setTimestamp(5, p_last_updated, UTC);
            stmt.setString(6, p_is_deleted ? "Y" : "N");
            setString(stmt, 7, resourcePayloadKey); // can be null
            stmt.executeUpdate();
        }

        if (!v_new_resource) {
            // As this is an existing logical resource, we need to update the xx_logical_resource values to match
            // the values of the current resource. For new resources, these are added by the insert so we don't
            // need to update them here.
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("Updating " + tablePrefix + "_logical_resources: " + v_resource_type + "/" + p_logical_id);
            }
            final String sql4 = "UPDATE " + tablePrefix + "_logical_resources SET current_resource_id = ?, is_deleted = ?, last_updated = ?, version_id = ? WHERE logical_resource_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql4)) {
                // bind parameters
                stmt.setLong(1, v_resource_id);
                stmt.setString(2, p_is_deleted ? "Y" : "N");
                stmt.setTimestamp(3, p_last_updated, UTC);
                stmt.setInt(4, p_version);
                stmt.setLong(5, v_logical_resource_id);
                stmt.executeUpdate();
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("Updated " + tablePrefix + "_logical_resources: " + v_resource_type + "/" + p_logical_id);
                }
            }

            // For schema V0014, now we also need to update the is_deleted and last_updated values
            // in LOGICAL_RESOURCES to support whole-system search
            final String sql4b = "UPDATE logical_resources SET is_deleted = ?, last_updated = ?, parameter_hash = ? WHERE logical_resource_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql4b)) {
                // bind parameters
                stmt.setString(1, p_is_deleted ? "Y" : "N");
                stmt.setTimestamp(2, p_last_updated, UTC);
                stmt.setString(3, p_parameterHashB64);
                stmt.setLong(4, v_logical_resource_id);
                stmt.executeUpdate();
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("Updated logical_resources: " + v_resource_type + "/" + p_logical_id);
                }
            }
        }

        // To keep things simple for the Derby use-case, we just use a visitor to
        // handle inserts of parameters directly in the resource parameter tables.
        // Note we don't get any parameters for the resource soft-delete operation
        if (parameters != null && requireParameterUpdate) {
            // Derby doesn't support partitioned multi-tenancy, so we disable it on the DAO:
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("Storing parameters for: " + v_resource_type + "/" + p_logical_id);
            }
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
        String changeType = p_is_deleted ? "D" : (v_new_resource || v_currently_deleted) ? "C" : "U";
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
        Integer result;

        final String sql1 = "SELECT resource_type_id FROM resource_types WHERE resource_type = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql1)) {
            stmt.setString(1, resourceTypeName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            else {
                result = null;
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
                FhirRefSequenceDAOImpl fhirRefSequenceDAO = new FhirRefSequenceDAOImpl(conn);
                result = fhirRefSequenceDAO.nextValue();

                final String INS = "INSERT INTO resource_types (resource_type_id, resource_type) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(INS)) {
                    // bind parameters
                    stmt.setInt(1, result);
                    stmt.setString(2, resourceTypeName);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                if ("23505".equals(e.getSQLState())) {
                    // another thread snuck in and created the record, so we need to fetch the correct id
                    result = getResourceTypeId(resourceTypeName, conn);
                } else {
                    throw e;
                }
            }
        }

        return result;
    }
}
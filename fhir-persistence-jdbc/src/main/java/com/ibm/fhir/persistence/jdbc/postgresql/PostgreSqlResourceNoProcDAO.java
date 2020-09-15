/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.postgresql;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.UTC;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceVersionIdMismatchException;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.FhirRefSequenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.CodeSystemCacheAdapter;
import com.ibm.fhir.persistence.jdbc.dao.impl.ParameterNameCacheAdapter;
import com.ibm.fhir.persistence.jdbc.dao.impl.ParameterVisitorBatchDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import com.ibm.fhir.persistence.jdbc.dto.Resource;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceFKVException;
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
public class PostgreSqlResourceNoProcDAO extends ResourceDAOImpl {
    private static final Logger logger = Logger.getLogger(PostgreSqlResourceDAO.class.getName());
    private static final String CLASSNAME = PostgreSqlResourceDAO.class.getSimpleName();

    private static final String SQL_READ_RESOURCE_TYPE = "{CALL %s.add_resource_type(?, ?)}";

    // DAO used to obtain sequence values from FHIR_REF_SEQUENCE
    private FhirRefSequenceDAO fhirRefSequenceDAO;

    public PostgreSqlResourceNoProcDAO(Connection connection, String schemaName, FHIRDbFlavor flavor) {
        super(connection, schemaName, flavor);
    }

    public PostgreSqlResourceNoProcDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry) {
        super(connection, schemaName, flavor, trxSynchRegistry);
    }
    
    /**
     * Inserts the passed FHIR Resource and associated search parameters to a Derby or PostgreSql FHIR database.
     * The search parameters are stored first by calling the passed parameterDao. Then the Resource is stored
     * by sql.
     * @param resource The FHIR Resource to be inserted.
     * @param parameters The Resource's search parameters to be inserted.
     * @param parameterDao
     * @return The Resource DTO
     * @throws FHIRPersistenceDataAccessException
     * @throws FHIRPersistenceDBConnectException
     * @throws FHIRPersistenceVersionIdMismatchException
     */
    @Override
    public Resource  insert(Resource resource, List<ExtractedParameterValue> parameters, ParameterDAO parameterDao)
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

            long resourceId = this.storeResource(resource.getResourceType(),
                parameters,
                resource.getLogicalId(),
                resource.getData(),
                lastUpdated,
                resource.isDeleted(),
                sourceKey,
                resource.getVersionId(),
                connection,
                parameterDao
                );


            dbCallDuration = (System.nanoTime() - dbCallStartTime)/1e6;

            resource.setId(resourceId);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Successfully inserted Resource. id=" + resource.getId() + " executionTime=" + dbCallDuration + "ms");
            }
        } catch(FHIRPersistenceDBConnectException | FHIRPersistenceDataAccessException e) {
            throw e;
        } catch(SQLIntegrityConstraintViolationException e) {
            FHIRPersistenceFKVException fx = new FHIRPersistenceFKVException("Encountered FK violation while inserting Resource.");
            throw severe(logger, fx, e);
        } catch(SQLException e) {
            if ("99001".equals(e.getSQLState())) {
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
     *
     * @param tablePrefix
     * @param parameters
     * @param p_logical_id
     * @param p_payload
     * @param p_last_updated
     * @param p_is_deleted
     * @param p_source_key
     * @param p_version
     *
     * @return the resource_id for the entry we created
     * @throws Exception
     */
    public long storeResource(String tablePrefix, List<ExtractedParameterValue> parameters, String p_logical_id, byte[] p_payload, Timestamp p_last_updated, boolean p_is_deleted,
        String p_source_key, Integer p_version, Connection conn, ParameterDAO parameterDao) throws Exception {

        final String METHODNAME = "storeResource() for " + tablePrefix + " resource";
        logger.entering(CLASSNAME, METHODNAME);

        Long v_logical_resource_id = null;
        Long v_current_resource_id = null;
        Long v_resource_id = null;
        Integer v_resource_type_id = null;
        boolean v_new_resource = false;
        boolean v_not_found = false;
        boolean v_duplicate = false;
        int v_version = 0;
        int v_insert_version = 0;

        String v_resource_type = tablePrefix;

        // Map the resource type name to the normalized id value in the database
        v_resource_type_id = getResourceTypeId(v_resource_type, conn);
        if (v_resource_type_id == null) {
            // programming error, as this should've been created earlier
            throw new IllegalStateException("resource type not found: " + v_resource_type);
        }

        // Get a lock at the system-wide logical resource level. Note the PostgreSQL-specific syntax
        final String SELECT_FOR_UPDATE = "SELECT logical_resource_id FROM logical_resources WHERE resource_type_id = ? AND logical_id = ? FOR NO KEY UPDATE";
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_FOR_UPDATE)) {
            stmt.setInt(1, v_resource_type_id);
            stmt.setString(2, p_logical_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                v_logical_resource_id = rs.getLong(1);
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
            final String sql3 = "INSERT INTO logical_resources (logical_resource_id, resource_type_id, logical_id) VALUES (?, ?, ?) "
                    + " ON CONFLICT DO NOTHING"
                    + " RETURNING logical_resource_id";
            try (PreparedStatement stmt = conn.prepareStatement(sql3)) {
                // bind parameters
                stmt.setLong(1, v_logical_resource_id);
                stmt.setInt(2, v_resource_type_id);
                stmt.setString(3, p_logical_id);
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
                final String INSERT_LR = "INSERT INTO " + tablePrefix + "_logical_resources (logical_resource_id, logical_id) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(INSERT_LR)) {
                    // bind parameters
                    stmt.setLong(1, v_logical_resource_id);
                    stmt.setString(2, p_logical_id);
                    stmt.executeUpdate();
                }
            }
        }

        if (!v_new_resource) {
            // existing resource.  We need to know the current version from the
            // resource-specific logical resources table.
            final String sql3 = "SELECT current_resource_id FROM " + tablePrefix + "_logical_resources WHERE logical_resource_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql3)) {
                stmt.setLong(1, v_logical_resource_id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    v_current_resource_id = rs.getLong(1);
                }
                else {
                    // This database is broken, because we shouldn't have logical_resource records without
                    // corresponding resource-specific logical_resource records.
                    throw new SQLException("Logical_id record '" + p_logical_id + "' missing for resource " + tablePrefix);
                }
            }

            // so if we are storing a specific version, do a quick check to make
            // sure that this version doesn't currently exist. This is only done when processing
            // replication messages which might be duplicated. We want the operation to be idempotent,
            // so if the resource already exists, we don't need to do anything else.

            if (p_version != null) {
                final String sqlStmt = "SELECT resource_id FROM " + tablePrefix + "_resources dr WHERE dr.logical_resource_id = ? AND dr.version_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlStmt)) {
                    // bind parameters
                    stmt.setLong(1, v_logical_resource_id);
                    stmt.setLong(2, p_version);
                    ResultSet res = stmt.executeQuery();
                    if (res.next()) {
                        // this version of this resource already exists, so we bail out right away
                        v_resource_id = res.getLong(1);
                        return v_resource_id;
                    }
                }
            }

            // Grab the version value for the current version (identified by v_current_resource_id)
            final String sql4 = "SELECT version_id FROM " + tablePrefix + "_resources WHERE resource_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql4)) {
                stmt.setLong(1, v_current_resource_id);
                ResultSet res = stmt.executeQuery();
                if (res.next()) {
                    v_version = res.getInt(1);
                }
                else {
                    throw new IllegalStateException("current resource not found: "
                            + tablePrefix + "_resources.resource_id=" + v_current_resource_id);
                }
            }

            //If we have been passed a version number, this means that this is a replicated
            //resource, and so we only need to delete parameters if the given version is
            // later than the current version
            if (p_version == null || p_version > v_version) {
                // existing resource, so need to delete all its parameters
                // delete composites first, or else the foreign keys there restrict deletes on referenced tables
                deleteFromParameterTable(conn, tablePrefix + "_composites", v_logical_resource_id);
                deleteFromParameterTable(conn, tablePrefix + "_str_values", v_logical_resource_id);
                deleteFromParameterTable(conn, tablePrefix + "_number_values", v_logical_resource_id);
                deleteFromParameterTable(conn, tablePrefix + "_date_values", v_logical_resource_id);
                deleteFromParameterTable(conn, tablePrefix + "_latlng_values", v_logical_resource_id);
                deleteFromParameterTable(conn, tablePrefix + "_token_values", v_logical_resource_id);
                deleteFromParameterTable(conn, tablePrefix + "_quantity_values", v_logical_resource_id);
            }
        }

        // Persist the data using the given version number if required
        if (p_version != null) {
            v_insert_version = p_version;
        } else {
            // remember we have a write (update) lock on the logical version, so we can safely calculate
            // the next version value here
            v_insert_version = v_version + 1;

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
            stmt.setInt(3, v_insert_version);
            stmt.setBytes(4, p_payload);
            stmt.setTimestamp(5, p_last_updated, UTC);
            stmt.setString(6, p_is_deleted ? "Y" : "N");
            stmt.executeUpdate();
        }

        if (p_version == null || p_version > v_version) {
            //only update the logical resource if the resource we are adding supercedes the
            //current resource
            String sql4 = "UPDATE " + tablePrefix + "_logical_resources SET current_resource_id = ? WHERE logical_resource_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql4)) {
                // bind parameters
                stmt.setLong(1, v_resource_id);
                stmt.setLong(2, v_logical_resource_id);
                stmt.executeUpdate();
            }

            // Note we don't get any parameters for the resource soft-delete operation
            if (parameters != null) {
                // PostgreSQL doesn't support partitioned multi-tenancy, so we disable it on the DAO:
                try (ParameterVisitorBatchDAO pvd = new ParameterVisitorBatchDAO(conn, null, tablePrefix, false, v_logical_resource_id, 100,
                    new ParameterNameCacheAdapter(parameterDao), new CodeSystemCacheAdapter(parameterDao))) {
                    for (ExtractedParameterValue p: parameters) {
                        p.accept(pvd);
                    }
                }
            }
        }
        logger.exiting(CLASSNAME, METHODNAME);
        return v_resource_id;
    }
    
    /**
     * Delete all parameters for the given resourceId from the parameters table
     *
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

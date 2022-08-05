/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao;


import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.MetricHandle;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.CalendarHelper;
import com.ibm.fhir.database.utils.common.PreparedStatementHelper;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceIndexRecord;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import com.ibm.fhir.persistence.jdbc.dto.Resource;
import com.ibm.fhir.persistence.jdbc.impl.ParameterTransactionDataImpl;
import com.ibm.fhir.persistence.jdbc.util.FHIRPersistenceJDBCMetric;
import com.ibm.fhir.persistence.jdbc.util.ParameterTableSupport;

/**
 * DAO used to contain the logic required to reindex a given resource
 */
public class ReindexResourceDAO extends ResourceDAOImpl {
    private static final Logger logger = Logger.getLogger(ReindexResourceDAO.class.getName());
    private static final String CLASSNAME = ReindexResourceDAO.class.getSimpleName();
    private static final SecureRandom RANDOM = new SecureRandom();

    // The translator specific to the database type we're working with
    private final IDatabaseTranslator translator;

    private final ParameterDAO parameterDao;

    private static final String PICK_SINGLE_LOGICAL_RESOURCE = ""
            + "  SELECT lr.logical_resource_id, lr.resource_type_id, lr.logical_id, lr.reindex_txid, lr.parameter_hash "
            + "    FROM logical_resources lr "
            + "   WHERE lr.logical_resource_id = ? "
            + "     AND lr.is_deleted = 'N' "
            + "     AND lr.reindex_tstamp < ? "
            ;

    private static final String PICK_SINGLE_RESOURCE = ""
            + "  SELECT lr.logical_resource_id, lr.resource_type_id, lr.logical_id, lr.reindex_txid, lr.parameter_hash "
            + "    FROM logical_resources lr "
            + "   WHERE lr.resource_type_id = ? "
            + "     AND lr.logical_id = ? "
            + "     AND lr.is_deleted = 'N' "
            + "     AND lr.reindex_tstamp < ? "
            ;

    private static final String PICK_SINGLE_RESOURCE_TYPE = ""
            + "  SELECT lr.logical_resource_id, lr.resource_type_id, lr.logical_id, lr.reindex_txid, lr.parameter_hash "
            + "    FROM logical_resources lr "
            + "   WHERE lr.resource_type_id = ? "
            + "     AND lr.is_deleted = 'N' "
            + "     AND lr.reindex_tstamp < ? "
            + "OFFSET ? ROWS FETCH FIRST 1 ROWS ONLY "
            ;

    private static final String PICK_ANY_RESOURCE = ""
            + "  SELECT lr.logical_resource_id, lr.resource_type_id, lr.logical_id, lr.reindex_txid, lr.parameter_hash "
            + "    FROM logical_resources lr "
            + "    JOIN resource_types rt "
            + "      ON rt.resource_type_id = lr.resource_type_id "
            + "   WHERE lr.is_deleted = 'N' "
            + "     AND lr.reindex_tstamp < ? "
            + "     AND rt.retired = 'N' "
            + "OFFSET ? ROWS FETCH FIRST 1 ROWS ONLY "
            ;

    // As of V0027, we serialize resource updates using LOGICAL_RESOURCE_IDENT
    private static final String LOCK_LOGICAL_RESOURCE = ""
            + "  SELECT 1 "
            + "    FROM logical_resource_ident "
            + "   WHERE resource_type_id = ? "
            + "     AND logical_id = ? "
            ;

    /**
     * Public constructor
     * @param connection
     * @param translator
     * @param parameterDao
     * @param schemaName
     * @param flavor
     * @param cache
     * @param rrd
     */
    public ReindexResourceDAO(Connection connection, IDatabaseTranslator translator, ParameterDAO parameterDao, String schemaName, FHIRDbFlavor flavor, FHIRPersistenceJDBCCache cache) {
        super(connection, schemaName, flavor, cache);
        this.translator = translator;
        this.parameterDao = parameterDao;
    }

    /**
     * Public constructor
     * @param connection
     * @param translator
     * @param parameterDao
     * @param schemaName
     * @param flavor
     * @param trxSynchRegistry
     * @param cache
     * @param rrd
     */
    public ReindexResourceDAO(Connection connection, IDatabaseTranslator translator, ParameterDAO parameterDao, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry, FHIRPersistenceJDBCCache cache, ParameterTransactionDataImpl ptdi) {
        super(connection, schemaName, flavor, trxSynchRegistry, cache, ptdi);
        this.translator = translator;
        this.parameterDao = parameterDao;

    }

    /**
     * Getter for the translator currently held by this DAO
     * @return
     */
    @Override
    protected IDatabaseTranslator getTranslator() {
        return this.translator;
    }

    /**
     * Pick a specific resource to process by logicalResourceId (primary key).
     * Since the logicalResourceId is specified, we avoid updating the record as the caller of $reindex operation
     * is passing in an explicit list of resources, so no need to lock for the purpose of picking a random resource.
     * This can improve performance (especially with PostgreSQL by avoiding the generation of tombstones).
     * @param reindexTstamp only get resource with a reindex_tstamp less than this
     * @param logicalResourceId the logical resource ID (primary key) of a specific resource
     * @return the resource record, or null when the resource is not found
     * @throws Exception
     */
    protected ResourceIndexRecord getResource(Instant reindexTstamp, Long logicalResourceId) throws Exception {
        ResourceIndexRecord result = null;

        // no need to close
        Connection connection = getConnection();
        IDatabaseTranslator translator = getTranslator();

        final String select = PICK_SINGLE_LOGICAL_RESOURCE;

        try (PreparedStatement stmt = connection.prepareStatement(select)) {
            if (logicalResourceId != null) {
                // specific resource by logical resource ID (primary key)
                stmt.setLong(1, logicalResourceId);
                stmt.setTimestamp(2, Timestamp.from(reindexTstamp), CalendarHelper.getCalendarForUTC());
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = new ResourceIndexRecord(rs.getLong(1), rs.getInt(2), rs.getString(3), rs.getLong(4), rs.getString(5));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, select, x);
            throw translator.translate(x);
        }

        if (result != null) {
            // We've picked the resource we want to process, now make sure we have a lock on it
            // before we make any changes
            result = lockLogicalResource(result);
        }

        return result;
    }

    /**
     * Lock the logical resource for update
     * @param rir
     * @throws Exception
     * @return the rir parameter, or null if the resource record was erased before it could be locked
     */
    protected ResourceIndexRecord lockLogicalResource(ResourceIndexRecord rir) throws Exception {
        if (logger.isLoggable(Level.FINE)) {
            // note resourceType is not set in the ResourceIndexRecord yet so we need to use resourceTypeId
            logger.fine("Locking (select for update): " + rir.getResourceTypeId() + "/" + rir.getLogicalId() 
                        + " logical_resource_id = [" + rir.getLogicalResourceId() + "]");
        }

        // no need to close
        Connection connection = getConnection();
        IDatabaseTranslator translator = getTranslator();

        // Build the SELECT ... FOR UPDATE statement
        final String select = translator.addForUpdate(LOCK_LOGICAL_RESOURCE);

        try (PreparedStatement stmt = connection.prepareStatement(select)) {
            PreparedStatementHelper psh = new PreparedStatementHelper(stmt);
            psh.setInt(rir.getResourceTypeId());
            psh.setString(rir.getLogicalId());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                logger.warning("logical_resource_ident record no longer exists (erased?): " + rir.getResourceTypeId() + "/" + rir.getLogicalId() 
                    + " logical_resource_id = [" + rir.getLogicalResourceId() + "]");
                rir = null; // prevents further processing now that this resource has disappeared
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, select, x);
            throw translator.translate(x);
        }
        return rir;
    }

    /**
     * Pick the next resource to process, then also lock it. Specializations for different databases may use
     * different techniques to optimize locking/concurrency control.
     * @param random used to generate a random number
     * @param reindexTstamp only get resource with a reindex_tstamp less than this
     * @param resourceTypeId the resource type ID of a specific resource type, or null
     * @param logicalId the resource ID of a specific resource, or null
     * @return the resource record, or null when there is nothing left to do
     * @throws Exception
     */
    protected ResourceIndexRecord getNextResource(SecureRandom random, Instant reindexTstamp, Integer resourceTypeId, String logicalId) throws Exception {
        ResourceIndexRecord result = null;

        // no need to close
        Connection connection = getConnection();
        IDatabaseTranslator translator = getTranslator();

        // Derby can only do select for update with simple queries, so we need to select first,
        // then try and lock, but we also have to try and cover the race condition which can
        // occur here, using an optimistic locking pattern
        final String select;

        if (resourceTypeId != null && logicalId != null) {
            // Just pick the requested resource
            select = PICK_SINGLE_RESOURCE;
        } else if (resourceTypeId != null) {
            // Limit to the given resource type
            select = PICK_SINGLE_RESOURCE_TYPE;
        } else if (resourceTypeId == null && logicalId == null) {
            select = PICK_ANY_RESOURCE;
        } else {
            // programming error
            throw new IllegalArgumentException("logicalId specified without a resourceType");
        }

        // Randomly pick an offset, but if we get no rows, reduce the range
        // until we hit 0. This will ensure we get the last few rows more
        // quickly
        int offsetRange = 1024;
        do {
            // random offset in [0, offsetRange)
            int offset = random.nextInt(offsetRange);
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("Executing the following reindex statement with offset " + offset + ":\n" + select);
            }
            try (PreparedStatement stmt = connection.prepareStatement(select)) {
                if (resourceTypeId != null && logicalId != null) {
                    stmt.setInt(1, resourceTypeId);
                    stmt.setString(2, logicalId);
                    stmt.setTimestamp(3, Timestamp.from(reindexTstamp), CalendarHelper.getCalendarForUTC());
                } else if (resourceTypeId != null) {
                    stmt.setInt(1, resourceTypeId);
                    stmt.setTimestamp(2, Timestamp.from(reindexTstamp), CalendarHelper.getCalendarForUTC());
                    stmt.setInt(3, offset);
                } else {
                    stmt.setTimestamp(1, Timestamp.from(reindexTstamp), CalendarHelper.getCalendarForUTC());
                    stmt.setInt(2, offset);
                }

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    result = new ResourceIndexRecord(rs.getLong(1), rs.getInt(2), rs.getString(3), rs.getLong(4), rs.getString(5));
                }
            } catch (SQLException x) {
                logger.log(Level.SEVERE, select, x);
                throw translator.translate(x);
            }

            if (result != null) {
                // We have picked a resource...now try and lock it. If this fails, we need
                // to pick another resource to cover the race condition.
                final String UPDATE = ""
                        + " UPDATE logical_resources  "
                        + "    SET reindex_tstamp = ?, "
                        + "        reindex_txid = ? "
                        + "  WHERE logical_resource_id = ? "
                        + "    AND reindex_txid = ? "; // make sure we have the txid we selected above

                try (PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
                    stmt.setTimestamp(1, Timestamp.from(reindexTstamp), CalendarHelper.getCalendarForUTC());
                    stmt.setLong(2, result.getTransactionId() + 1L);
                    stmt.setLong(3, result.getLogicalResourceId());
                    stmt.setLong(4, result.getTransactionId());
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected == 0) {
                        // reindex_txid is not the same as when we selected, meaning that this update
                        // was blocked and the resource was processed by another thread. Forget this
                        // record and try again
                        result = null;
                    }
                } catch (SQLException x) {
                    logger.log(Level.SEVERE, UPDATE, x);
                    throw translator.translate(x);
                }
            } else {
                // Offset beyond that last available row, so we need to shrink the range
                // The loop will exit when we get a resource, or no resource was found when
                // the offsetRange was 1 (producing a guaranteed offset = 0)
                offsetRange /= 2;
            }
        } while (offsetRange > 0 && result == null);

        if (result != null) {
            // Since V0027 we lock resources using LOGICAL_RESOURCE_IDENT. There is a small chance of
            // deadlock here if reindex is processing the same resources which are also being updated
            // as part of ingestion. This is because ingestion locks LOGICAL_RESOURCE_IDENT before
            // updating LOGICAL_RESOURCES, whereas here we updated LOGICAL_RESOURCES before locking
            // LOGICAL_RESOURCE_IDENT. Eliminating this would require a significant rework. The
            // recommendation going forward is to always use the client-driven process, because it
            // is more efficient.
            result = lockLogicalResource(result);
        }

        return result;
    }

    /**
     * Get the resource record we want to reindex. This might take a few attempts, because
     * there could be hundreds of threads all trying to do the same thing, and we may see
     * collisions which will cause the FOR UPDATE to block, then return no rows.
     * @param reindexTstamp only get resource with an index_tstamp less than this
     * @param logicalResourceId logical resource ID (primary key) of resource to reindex, or null
     * @param resourceTypeId the resource type ID of a specific resource type, or null;
     * this parameter is ignored if the logicalResourceId parameter value is non-null
     * @param logicalId the resource ID of a specific resource, or null;
     * this parameter is ignored if the logicalResourceId parameter value is non-null
     * @return the resource record, or null when there is nothing left to do
     * @throws Exception
     */
    public ResourceIndexRecord getResourceToReindex(Instant reindexTstamp, Long logicalResourceId, Integer resourceTypeId, String logicalId) throws Exception {
        ResourceIndexRecord result = null;

        // no need to close
        Connection connection = getConnection();

        // Get a resource which needs to be reindexed
        if (logicalResourceId != null) {
            result = getResource(reindexTstamp, logicalResourceId);
        } else {
            result = getNextResource(RANDOM, reindexTstamp, resourceTypeId, logicalId);
        }

        if (result != null) {

            // grab the resource type name while we're here. We do this as a separate query
            // because it would otherwise complicate the select for update above, which is very
            // sensitive performance-wise. Although this is another database round-trip, it shouldn't
            // impact concurrency which is the main issue in driving reindex throughput
            final String SELECT_RESOURCE_TYPE = ""
                    + "SELECT rt.resource_type "
                    + "  FROM resource_types rt, "
                    + "       logical_resources lr "
                    + " WHERE rt.resource_type_id = lr.resource_type_id "
                    + "   AND lr.logical_resource_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(SELECT_RESOURCE_TYPE)) {
                stmt.setLong(1, result.getLogicalResourceId());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    result.setResourceType(rs.getString(1));
                } else if (logicalResourceId != null) {
                    // When logicalResourceId is specified, the resource is not locked, so it could disappear
                    logger.fine("Logical resource no longer exists: logical_resource_id=" + result.getLogicalResourceId());
                    result = null;
                } else {
                    // Can't really happen, because the resource is selected for update, so it can't disappear
                    logger.severe("Logical resource no longer exists: logical_resource_id=" + result.getLogicalResourceId());
                    throw new IllegalStateException("resource not found");
                }
            } catch (SQLException x) {
                logger.log(Level.SEVERE, SELECT_RESOURCE_TYPE, x);
                throw translator.translate(x);
            }
        }

        return result;
    }

    /**
     * Reindex the resource by deleting existing parameters and replacing them with those passed in.
     * @param tablePrefix the table prefix
     * @param parameters A collection of search parameters to be persisted along with the passed Resource
     * @param parameterHashB64 the Base64 encoded SHA-256 hash of parameters
     * @param logicalId the logical id
     * @param logicalResourceId the logical resource id
     * @throws Exception
     */
    public void updateParameters(String tablePrefix, List<ExtractedParameterValue> parameters, String parameterHashB64, String logicalId, long logicalResourceId) throws Exception {

        final String METHODNAME = "updateParameters() for " + tablePrefix + "/" + logicalId;
        logger.entering(CLASSNAME, METHODNAME);

        try (MetricHandle m = FHIRRequestContext.get().getMetricHandle(FHIRPersistenceJDBCMetric.M_JDBC_DELETE_RESOURCE_PARAMETERS.name())) {
            deleteResourceParameters(tablePrefix, logicalResourceId);
        }

        // Update the parameter hash in the LOGICAL_RESOURCES table
        updateParameterHash(getConnection(), logicalResourceId, parameterHashB64);

        logger.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Delete all the parameter values for the given resourceType/logicalResourceId
     * @param resourceTypeName
     * @param logicalResourceId
     * @throws SQLException
     */
    protected void deleteResourceParameters(String resourceTypeName, long logicalResourceId) throws SQLException {
        ParameterTableSupport.deleteFromParameterTables(getConnection(), resourceTypeName, logicalResourceId);        
    }

    /**
     * Updates the parameter hash in the LOGICAL_RESOURCES table.
     * @param conn the connection
     * @param logicalResourceId the logical resource ID
     * @param parameterHashB64 the Base64 encoded SHA-256 hash of parameters
     * @throws SQLException
     */
    protected void updateParameterHash(Connection conn, long logicalResourceId, String parameterHashB64) throws SQLException {
        final String SQL = "UPDATE logical_resources SET parameter_hash = ? WHERE logical_resource_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
            // bind parameters
            stmt.setString(1, parameterHashB64);
            stmt.setLong(2, logicalResourceId);
            long dbCallStartTime = System.nanoTime();
            stmt.executeUpdate();
            double dbCallDuration = (System.nanoTime() - dbCallStartTime) / 1e6;
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("Update parameter_hash '" + parameterHashB64 + "' for logicalResourceId '" + logicalResourceId + "' [took " + dbCallDuration + " ms]");
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, SQL, x);
            throw translator.translate(x);
        }
    }

    @Override
    public Resource insert(Resource resource, List<ExtractedParameterValue> parameters, String parameterHashB64, ParameterDAO parameterDao, Integer ifNoneMatch)
        throws FHIRPersistenceException {
        // NOP because for reindex we only insert the parameters - the current resource is not changed
        return resource;
    }
}

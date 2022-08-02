/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.postgres;

import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.CalendarHelper;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.ReindexResourceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceIndexRecord;
import com.ibm.fhir.persistence.jdbc.impl.ParameterTransactionDataImpl;

/**
 * PostgreSQL specialization of the DAO used to assist the reindex custom operation
 */
public class PostgresReindexResourceDAO extends ReindexResourceDAO {
    private static final Logger logger = Logger.getLogger(PostgresReindexResourceDAO.class.getName());

    private static final String PICK_SINGLE_RESOURCE = ""
            + "   UPDATE logical_resources "
            + "      SET reindex_tstamp = ?,"
            + "          reindex_txid = COALESCE(reindex_txid + 1, 1) "
            + "    WHERE logical_resource_id = ( "
            + "       SELECT lr.logical_resource_id "
            + "         FROM logical_resources lr "
            + "        WHERE lr.resource_type_id = ? "
            + "          AND lr.logical_id = ? "
            + "          AND lr.is_deleted = 'N' "
            + "          AND lr.reindex_tstamp < ? "
            + "     ORDER BY lr.reindex_tstamp  "
            + "   FOR UPDATE SKIP LOCKED LIMIT 1) "
            + "RETURNING logical_resource_id, resource_type_id, logical_id, reindex_txid, parameter_hash "
            ;

    private static final String PICK_SINGLE_RESOURCE_TYPE = ""
            + "   UPDATE logical_resources "
            + "      SET reindex_tstamp = ?, "
            + "          reindex_txid = COALESCE(reindex_txid + 1, 1) "
            + "    WHERE logical_resource_id = ( "
            + "       SELECT lr.logical_resource_id "
            + "         FROM logical_resources lr "
            + "        WHERE lr.resource_type_id = ? "
            + "          AND lr.is_deleted = 'N' "
            + "          AND lr.reindex_tstamp < ? "
            + "     ORDER BY lr.reindex_tstamp  "
            + "   FOR UPDATE SKIP LOCKED LIMIT 1) "
            + "RETURNING logical_resource_id, resource_type_id, logical_id, reindex_txid, parameter_hash "
            ;

    private static final String PICK_ANY_RESOURCE = ""
            + "   UPDATE logical_resources "
            + "      SET reindex_tstamp = ?,"
            + "          reindex_txid = COALESCE(reindex_txid + 1, 1) "
            + "    WHERE logical_resource_id = ( "
            + "       SELECT lr.logical_resource_id "
            + "         FROM logical_resources lr "
            + "         JOIN resource_types rt "
            + "           ON rt.resource_type_id = lr.resource_type_id"
            + "        WHERE lr.is_deleted = 'N' "
            + "          AND lr.reindex_tstamp < ? "
            + "          AND rt.retired = 'N' "
            + "     ORDER BY lr.reindex_tstamp  "
            + "   FOR UPDATE SKIP LOCKED LIMIT 1) "
            + "RETURNING logical_resource_id, resource_type_id, logical_id, reindex_txid, parameter_hash "
            ;

    // Delete all the resource parameter values associated with the given resource
    private static final String SQL_DELETE_RESOURCE_PARAMETERS = "{CALL %s.delete_resource_parameters(?,?,?)}";

    /**
     * Public constructor
     * @param connection
     * @param translator
     * @param parameterDao
     * @param schemaName
     * @param flavor
     * @param cache
     */
    public PostgresReindexResourceDAO(Connection connection, IDatabaseTranslator translator, ParameterDAO parameterDao, String schemaName, FHIRDbFlavor flavor, FHIRPersistenceJDBCCache cache) {
        super(connection, translator, parameterDao, schemaName, flavor, cache);
    }

    /**
     * Public constructor for use in a JEE context
     * @param connection
     * @param translator
     * @param parameterDao
     * @param schemaName
     * @param flavor
     * @param trxSynchRegistry
     * @param cache
     */
    public PostgresReindexResourceDAO(Connection connection, IDatabaseTranslator translator, ParameterDAO parameterDao, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry, FHIRPersistenceJDBCCache cache,
        ParameterTransactionDataImpl ptdi) {
        super(connection, translator, parameterDao, schemaName, flavor, trxSynchRegistry, cache, ptdi);
    }

    @Override
    public ResourceIndexRecord getNextResource(SecureRandom random, Instant reindexTstamp, Integer resourceTypeId, String logicalId) throws Exception {
        ResourceIndexRecord result = null;

        // no need to close
        Connection connection = getConnection();
        IDatabaseTranslator translator = getTranslator();

        // For Postgres, we can leverage SKIP LOCKED which is specifically designed for this
        // sort of thing, where we want to pull work from a "queue" without being blocked
        // by existing locks. The ORDER BY is included to persuade[force] Postgres to always
        // use the index instead of switching to a full tablescan when the distribution stats
        // confuse the optimizer.
        final String update;
        if (resourceTypeId != null && logicalId != null) {
            // Limit to one resource
            update = PICK_SINGLE_RESOURCE;
        } else if (resourceTypeId != null) {
            // Limit to one type of resource
            update = PICK_SINGLE_RESOURCE_TYPE;
        } else if (resourceTypeId == null && logicalId == null) {
            // Pick the next resource needing to be reindexed regardless of type
            update = PICK_ANY_RESOURCE;
        } else {
            // programming error
            throw new IllegalArgumentException("logicalId specified without a resourceType");
        }

        final Calendar UTC = CalendarHelper.getCalendarForUTC();
        try (PreparedStatement stmt = connection.prepareStatement(update)) {
            if (resourceTypeId != null && logicalId != null) {
                // specific resource
                stmt.setTimestamp(1, Timestamp.from(reindexTstamp), UTC);
                stmt.setInt(2, resourceTypeId);
                stmt.setString(3, logicalId);
                stmt.setTimestamp(4, Timestamp.from(reindexTstamp), UTC);
            } else if (resourceTypeId != null) {
                // limit to resource type
                stmt.setTimestamp(1, Timestamp.from(reindexTstamp), UTC);
                stmt.setInt(2, resourceTypeId);
                stmt.setTimestamp(3, Timestamp.from(reindexTstamp), UTC);
            } else {
                // any resource type
                stmt.setTimestamp(1, Timestamp.from(reindexTstamp), UTC);
                stmt.setTimestamp(2, Timestamp.from(reindexTstamp), UTC);
            }

            // This is an UPDATE statement...
            stmt.execute();
            
            // ...with a RETURNING clause
            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {
                result = new ResourceIndexRecord(rs.getLong(1), rs.getInt(2), rs.getString(3), rs.getLong(4), rs.getString(5));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, update, x);
            throw translator.translate(x);
        }

        if (result != null) {
            // Since V0027 we lock resources using LOGICAL_RESOURCE_IDENT. There is a small chance of
            // deadlock here if reindex is processing the same resources which are also being updated
            // as part of ingestion. This is because ingestion locks LOGICAL_RESOURCE_IDENT before
            // updating LOGICAL_RESOURCES, whereas here we update LOGICAL_RESOURCES before locking
            // LOGICAL_RESOURCE_IDENT. Eliminating this would require a significant rework. The
            // recommendation going forward is to always use the client-driven process. It
            // is more efficient and does not have this deadlock issue.
            result = lockLogicalResource(result);
        }

        return result;
    }

    @Override
    protected void deleteResourceParameters(String resourceTypeName, long logicalResourceId) throws SQLException {
        // for PostgreSQL we can use the stored procedure/function to delete all the parameters. This
        // uses the same delete function that is used during initial ingestion
        final Connection connection = getConnection(); // do not close
        final String stmtString = String.format(SQL_DELETE_RESOURCE_PARAMETERS, getSchemaName());

        try (CallableStatement stmt = connection.prepareCall(stmtString)) {
            int arg = 1;
            stmt.setString(arg++, resourceTypeName);
            stmt.setLong(arg++, logicalResourceId);
            stmt.registerOutParameter(arg, Types.BIGINT);
            stmt.execute();
        }
    }
}
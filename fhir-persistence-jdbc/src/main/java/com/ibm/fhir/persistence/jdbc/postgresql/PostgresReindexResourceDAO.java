/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.postgresql;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceIndexRecord;
import com.ibm.fhir.persistence.jdbc.derby.ReindexResourceDAO;
import com.ibm.fhir.persistence.jdbc.impl.ParameterTransactionDataImpl;

/**
 * Derby specialization of the DAO used to assist the reindex custom operation
 */
public class PostgresReindexResourceDAO extends ReindexResourceDAO {
    private static final Logger logger = Logger.getLogger(PostgresReindexResourceDAO.class.getName());

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
    public PostgresReindexResourceDAO(Connection connection, IDatabaseTranslator translator, ParameterDAO parameterDao, String schemaName, FHIRDbFlavor flavor, FHIRPersistenceJDBCCache cache, IResourceReferenceDAO rrd) {
        super(connection, translator, parameterDao, schemaName, flavor, cache, rrd);
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
     * @param rrd
     */
    public PostgresReindexResourceDAO(Connection connection, IDatabaseTranslator translator, ParameterDAO parameterDao, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry, FHIRPersistenceJDBCCache cache, IResourceReferenceDAO rrd, 
        ParameterTransactionDataImpl ptdi) {
        super(connection, translator, parameterDao, schemaName, flavor, trxSynchRegistry, cache, rrd, ptdi);
    }
    
    @Override
    public ResourceIndexRecord getNextResource(SecureRandom random, Instant reindexTstamp) throws Exception {
        ResourceIndexRecord result = null;
        
        // no need to close
        Connection connection = getConnection();
        IDatabaseTranslator translator = getTranslator();

        // For Postgres, we can leverage SKIP LOCKED which is specifically designed for this
        // sort of thing, where we want to pull work from a "queue" without being blocked
        // by existing locks. The ORDER BY is included to persuade[force] Postgres to always
        // use the index instead of switching to a full tablescan when the distribution stats
        // confuse the optimizer.
        final String UPDATE = ""
            + "   UPDATE logical_resources "
            + "      SET reindex_tstamp = ?,"
            + "          reindex_txid = COALESCE(reindex_txid + 1, 1) "
            + "    WHERE logical_resource_id = ( "
            + "       SELECT lr.logical_resource_id "
            + "         FROM logical_resources lr "
            + "        WHERE lr.reindex_tstamp < ? "
            + "     ORDER BY lr.reindex_tstamp DESC "
            + "   FOR UPDATE SKIP LOCKED LIMIT 1) "
            + "RETURNING logical_resource_id, resource_type_id, logical_id, reindex_txid "
            ;
        
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
            stmt.setTimestamp(1, Timestamp.from(reindexTstamp));
            stmt.setTimestamp(2, Timestamp.from(reindexTstamp));
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {
                result = new ResourceIndexRecord(rs.getLong(1), rs.getInt(2), rs.getString(3), rs.getLong(4));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, UPDATE, x);
            throw translator.translate(x);
        }
        
        return result;
    }

}
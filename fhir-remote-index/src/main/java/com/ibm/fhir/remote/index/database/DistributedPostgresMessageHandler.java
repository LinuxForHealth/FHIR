/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.remote.index.api.IdentityCache;

/**
 * For the DISTRIBUTED schema variant used on databases such as Citus, we
 * can't use IDENTITY columns. Instead we have to use values generated
 * by a sequence, which means a slightly different INSERT statement
 * in certain cases
 */
public class DistributedPostgresMessageHandler extends PlainMessageHandler {
    private static final Logger logger = Logger.getLogger(DistributedPostgresMessageHandler.class.getName());

    /**
     * Public constructor
     * @param connection
     * @param schemaName
     * @param cache
     * @param maxReadyTimeMs
     */
    public DistributedPostgresMessageHandler(Connection connection, String schemaName, IdentityCache cache, long maxReadyTimeMs) {
        super(new PostgresTranslator(), connection, schemaName, cache, maxReadyTimeMs);
    }

    @Override
    protected String onConflict() {
        return "ON CONFLICT DO NOTHING";
    }

    @Override
    protected void addMissingCommonTokenValues(List<CommonTokenValue> missing) throws FHIRPersistenceException {

        // Need to use our own sequence number because distributed databases don't
        // like generated identity columns
        final String nextVal = translator.nextValue(schemaName, "fhir_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO common_token_values (code_system_id, token_value, common_token_value_id) ");
        insert.append(" OVERRIDING SYSTEM VALUE "); // we want to use our sequence number
        insert.append("     VALUES (?,?,");
        insert.append(nextVal); // next sequence value
        insert.append(") ");
        insert.append(onConflict());

        try (PreparedStatement ps = connection.prepareStatement(insert.toString())) {
            int count = 0;
            for (CommonTokenValue ctv: missing) {
                ps.setInt(1, ctv.getCodeSystemValue().getCodeSystemId());
                ps.setString(2, ctv.getTokenValue());
                ps.addBatch();
                if (++count == this.maxCommonTokenValuesPerStatement) {
                    // not too many statements in a single batch
                    ps.executeBatch();
                    count = 0;
                }
            }
            if (count > 0) {
                // final batch
                ps.executeBatch();
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "failed: " + insert.toString(), x);
            throw new FHIRPersistenceException("failed inserting new common token values");
        }
    }

    @Override
    protected void addMissingCommonCanonicalValues(List<CommonCanonicalValue> missing) throws FHIRPersistenceException {

        // Need to use our own sequence number because distributed databases don't
        // like generated identity columns
        final String nextVal = translator.nextValue(schemaName, "fhir_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO common_canonical_values (url, canonical_id) ");
        insert.append(" OVERRIDING SYSTEM VALUE "); // we want to use our sequence number
        insert.append("     VALUES (?,");
        insert.append(nextVal); // next sequence value
        insert.append(") ");
        insert.append(onConflict());

        final String DML = insert.toString();
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("addMissingCanonicalIds: " + DML);
        }
        try (PreparedStatement ps = connection.prepareStatement(DML)) {
            int count = 0;
            for (CommonCanonicalValue ctv: missing) {
                logger.finest(() -> "Adding canonical value [" + ctv.toString() + "]");
                ps.setString(1, ctv.getUrl());
                ps.addBatch();
                if (++count == this.maxCommonCanonicalValuesPerStatement) {
                    // not too many statements in a single batch
                    ps.executeBatch();
                    count = 0;
                }
            }
            if (count > 0) {
                // final batch
                ps.executeBatch();
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "failed: " + insert.toString(), x);
            throw new FHIRPersistenceException("failed inserting new common canonical values");
        }
    }
}
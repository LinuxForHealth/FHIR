/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.database.utils.schema;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Stream;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;

/**
 * Access layer for WHOLE_SCHEMA_VERSION data
 */
public class SchemaVersionsManager {
    private final IDatabaseTranslator translator;
    private final IConnectionProvider connectionProvider;
    private final ITransactionProvider transactionProvider;
    private final String schemaName;
    
    // The latest version of the schema according to this code
    private final int latestCodeVersion;

    /**
     * Public constructor
     * @param translator
     * @param connectionPool
     * @param transactionProvider
     * @param schemaName
     * @param latestCodeVersion
     */
    public SchemaVersionsManager(IDatabaseTranslator translator, IConnectionProvider connectionPool, 
            ITransactionProvider transactionProvider, String schemaName, int latestCodeVersion) {
        this.translator = translator;
        this.connectionProvider = connectionPool;
        this.transactionProvider = transactionProvider;
        this.schemaName = schemaName;
        this.latestCodeVersion = latestCodeVersion;
    }

    /**
     * Get the installed version for the schema
     * @return the schema version, or -1 if no schema build has completed yet
     */
    public int getVersionForSchema() {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try (Connection c = connectionProvider.getConnection()) {
                GetSchemaVersion cmd = new GetSchemaVersion(this.schemaName);
                return cmd.run(translator, c);
            } catch (SQLException x) {
                throw translator.translate(x);
            }
        }
    }
    
    /**
     * Record the version information for the given schemaName in the
     * WHOLESCHEMA_VERSION table, creating a new record if required
     * @param schemaName
     * @param version
     */
    public void updateSchemaVersionId(int versionId) {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try (Connection c = connectionProvider.getConnection()) {
                final UpdateSchemaVersion cmd;
                switch (this.translator.getType()) {
                case POSTGRESQL:
                    cmd = new UpdateSchemaVersionPostgresql(schemaName, versionId);
                    break;
                default:
                    cmd = new UpdateSchemaVersion(schemaName, versionId);
                }
                cmd.run(translator, c);
            } catch (SQLException x) {
                throw translator.translate(x);
            }
        }
    }


    /**
     * Find the latest FhirSchemaVersion and use it to update the WHOLE_SCHEMA_VERSION
     * table
     */
    public void updateSchemaVersion() {
        updateSchemaVersionId(this.latestCodeVersion);
    }

    /**
     * Returns true if the current schema version recorded in WHOLE_SCHEMA_VERSION is the
     * latest FhirSchemaVersion
     * @return
     */
    public boolean isLatestSchema() {
        return getVersionForSchema() == latestCodeVersion;
    }
}
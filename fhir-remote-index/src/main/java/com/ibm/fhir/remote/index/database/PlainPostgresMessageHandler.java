/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.sql.Connection;

import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.remote.index.api.IdentityCache;

/**
 * PostgreSQL variant of the remote index message handler
 */
public class PlainPostgresMessageHandler extends PlainMessageHandler {

    /**
     * @param connection
     * @param schemaName
     * @param cache
     * @param maxReadyTimeMs
     */
    public PlainPostgresMessageHandler(Connection connection, String schemaName, IdentityCache cache, long maxReadyTimeMs) {
        super(new PostgresTranslator(), connection, schemaName, cache, maxReadyTimeMs);
    }

    @Override
    protected String onConflict() {
        return "ON CONFLICT DO NOTHING";
    }

}

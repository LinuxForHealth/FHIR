/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.database;

import java.sql.Connection;

import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.persistence.params.api.IParameterIdentityCache;

/**
 * PostgreSQL variant of the remote index message handler
 */
public class PlainPostgresParamValueProcessor extends PlainParamValueProcessor {

    /**
     * Public constructor
     * 
     * @param connection
     * @param schemaName
     * @param cache
     */
    public PlainPostgresParamValueProcessor(Connection connection, String schemaName, IParameterIdentityCache cache) {
        super(new PostgresTranslator(), connection, schemaName, cache);
    }

    @Override
    protected String onConflict() {
        return "ON CONFLICT DO NOTHING";
    }

}

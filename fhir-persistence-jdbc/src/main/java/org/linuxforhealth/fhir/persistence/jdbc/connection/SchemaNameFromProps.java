/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.util.Properties;

import org.linuxforhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * Returns a constant schemaName. Useful for unit test configurations
 */
public class SchemaNameFromProps implements SchemaNameSupplier { 

    // The constant schema name we supply
    private final String schemaName;
 
    /**
     * Public constructor
     * @param configProps should contain the "schemaName" property
     */
    public SchemaNameFromProps(Properties configProps) {
        // get the schema name from the configuration properties
        this.schemaName = configProps.getProperty("schemaName", null);
    }

    /**
     * Public constructor
     * @param schemaName constant
     */
    public SchemaNameFromProps(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public String getSchemaForRequestContext(Connection connection) throws FHIRPersistenceDBConnectException {
        return this.schemaName;
    }
}

/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.connection;

import org.linuxforhealth.fhir.database.utils.api.SchemaType;
import org.linuxforhealth.fhir.database.utils.model.DbType;

/**
 * Describes the capability of the underlying database and the schema it has
 * been configured with.
 */
public class FHIRDbFlavorImpl implements FHIRDbFlavor {
    
    // basic type of the database (DB2, Derby etc)
    private final DbType type;
    
    private final SchemaType schemaType;

    /**
     * Public constructor
     * @param type
     * @param schemaType
     */
    public FHIRDbFlavorImpl(DbType type, SchemaType schemaType) {
        this.type = type;
        this.schemaType = schemaType;
    }

    @Override
    public DbType getType() {
        return this.type;
    }

    @Override
    public boolean isFamilyPostgreSQL() {
        return this.type == DbType.POSTGRESQL || this.type == DbType.CITUS;
    }

    @Override
    public SchemaType getSchemaType() {
        return this.schemaType;
    }
}
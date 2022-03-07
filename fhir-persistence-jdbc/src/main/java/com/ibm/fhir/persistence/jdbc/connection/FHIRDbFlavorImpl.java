/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import com.ibm.fhir.database.utils.model.DbType;

/**
 * Describes the capability of the underlying database and the schema it has
 * been configured with.
 */
public class FHIRDbFlavorImpl implements FHIRDbFlavor {
    
    // does the database schema support multi-tenancy
    private final boolean multitenant;

    // basic type of the database (DB2, Derby etc)
    private final DbType type;
    
    public FHIRDbFlavorImpl(DbType type, boolean multitenant) {
        this.type = type;
        this.multitenant = multitenant;
    }
    
    @Override
    public boolean isMultitenant() {
        return this.multitenant;
    }

    @Override
    public DbType getType() {
        return this.type;
    }

    @Override
    public boolean isFamilyPostgreSQL() {
        return this.type == DbType.POSTGRESQL || this.type == DbType.CITUS;
    }
}

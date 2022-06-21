/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import com.ibm.fhir.database.utils.api.SchemaType;
import com.ibm.fhir.database.utils.model.DbType;

/**
 * Capabilities supported by the different flavors of database we connect to.
 * The flavor is a combination of the database type (e.g. DB2/Derby etc) and
 * the capabilities of the installed schema
 */
public interface FHIRDbFlavor {

    /**
     * Does the database support multi-tenancy?
     * @return
     */
    public boolean isMultitenant();

    /**
     * What type of schema is this
     * @return
     */
    public SchemaType getSchemaType();

    /**
     * What type of database is this?
     * @return
     */
    public DbType getType();

    /**
     * Is the dbType from the PostgreSQL family?
     * @return
     */
    public boolean isFamilyPostgreSQL();
}

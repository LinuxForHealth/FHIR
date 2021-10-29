/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.time.Instant;

import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.version.SchemaConstants;

/**
 * PostgreSQL variant of GetLease to avoid duplicate PK exception which would
 * cause the transaction to fail. To prevent this, the insert needs
 * ON CONFLICT DO NOTHING
 */
public class GetLeasePostgresql extends GetLease {
    
    /**
     * Public constructor
     * @param adminSchema
     * @param schemaName
     * @param host
     * @param leaseId
     * @param leaseUntil
     */
    public GetLeasePostgresql(String adminSchema, String schemaName, String host, String leaseId, Instant leaseUntil) {
        super(adminSchema, schemaName, host, leaseId, leaseUntil);
    }

    @Override
    protected String getInsertSQL(final String adminSchema) {
        // In PostgreSQL, we need to avoid the duplicate PK exception because it
        // kills the transaction
        final String CONTROL = DataDefinitionUtil.getQualifiedName(adminSchema, SchemaConstants.CONTROL);
        final String INS = "INSERT INTO " + CONTROL + " ("
                + " schema_name, lease_owner_host, lease_owner_uuid, lease_until) "
                + " VALUES (?, ?, ?, ?)"
                + " ON CONFLICT DO NOTHING";
        return INS;
    }
}
/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.model.type.code.FHIRResourceType;

/**
 * Populates the Resource Types Table
 */
public class Db2PopulateResourceTypes implements IDatabaseStatement {
    private final String adminSchemaName;
    private final String schemaName;
    private final int tenantId;

    public Db2PopulateResourceTypes(String adminSchemaName, String schemaName, int tenantId) {
        this.adminSchemaName = adminSchemaName;
        this.schemaName      = schemaName;
        this.tenantId        = tenantId;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        try (Statement s = c.createStatement()) {
            s.execute("SET " + adminSchemaName + ".SV_TENANT_ID = " + tenantId);
            // Create tables for each resource type

            for (FHIRResourceType.ValueSet rt : FHIRResourceType.ValueSet.values()) {
                String resourceType = rt.value();
                String callableStr = "CALL " + schemaName + " .add_resource_type(?, ?)";
                try (CallableStatement cStmt = c.prepareCall(callableStr);) {
                    cStmt.setString(1, resourceType);
                    cStmt.registerOutParameter(2, Types.INTEGER);
                    cStmt.execute();
                }
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }

    }

}
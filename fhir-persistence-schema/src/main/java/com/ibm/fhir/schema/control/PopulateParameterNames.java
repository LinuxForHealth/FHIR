/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Populates the Parameters Names Table
 *
 * @implNote This class supports the multi-tenant schema and the single tenant.
 */
public class PopulateParameterNames implements IDatabaseStatement {

    private final String adminSchemaName;
    private final String schemaName;
    private final Integer tenantId;

    public PopulateParameterNames(String adminSchemaName, String schemaName, Integer tenantId) {
        this.adminSchemaName = adminSchemaName;
        this.schemaName = schemaName;
        this.tenantId = tenantId;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String nextRefVal = translator.nextValue(schemaName, "fhir_ref_sequence");
        
        // For Db2 multi-tenancy, we need to set up the SV_TENANT_ID in order for the row-based-access-control
        // to work
        final String stmtVariable = String.format("SET %s.SV_TENANT_ID = %d", adminSchemaName, tenantId);
        final String stmtResourceTypeInsert;
        final String PARAMETER_NAMES = String.format("SELECT PARAMETER_NAME_ID, PARAMETER_NAME FROM %s.parameter_names", schemaName);
        if (tenantId != null) {
            stmtResourceTypeInsert =
                    String.format("INSERT INTO %s.parameter_names (MT_ID, PARAMETER_NAME_ID, PARAMETER_NAME) "
                            + "VALUES (%s.sv_tenant_id, %s, ?)", schemaName, adminSchemaName, nextRefVal);
        } else {
            stmtResourceTypeInsert =
                    String.format("INSERT INTO %s.parameter_names (PARAMETER_NAME_ID, PARAMETER_NAME) VALUES (%s, ?)", schemaName, nextRefVal);
        }

        // Configure RBAC if we're Db2 multi-tenant
        if (tenantId != null) {
            try (Statement s = c.createStatement();) {
                s.execute(stmtVariable);
            } catch (SQLException x) {
                throw translator.translate(x);
            }
        }
        
        // Grab a set containing all the current parameter names so we can skip them
        Set<String> currentParameterNames = new HashSet<>();
        try (PreparedStatement ps = c.prepareStatement(PARAMETER_NAMES)) {
            ResultSet rset = ps.executeQuery();
            while (rset.next()) {
                currentParameterNames.add(rset.getString(1));
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
        
        // Now we can insert parameter names not found in the current set
        try (PreparedStatement batch = c.prepareStatement(stmtResourceTypeInsert)) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(PopulateParameterNames.class.getClassLoader().getResourceAsStream("parameter_names.properties"), StandardCharsets.UTF_8))) {
                final int batchSize = 100;
                int processed = 0;
                String line;
                while ((line = br.readLine()) != null) {
                    final String parameterName = line.trim();
                    if (!currentParameterNames.contains(parameterName)) {
                        batch.setString(1, parameterName);
                        batch.addBatch();
                        if (++processed == batchSize) {
                            batch.executeBatch();
                            processed = 0;
                        }
                    }
                }
                
                // wrap up by sending the final batch
                if (processed > 0) {
                    batch.executeBatch();
                }
            } catch (IOException e) {
                // Wrap and propagate
                throw new IllegalArgumentException(e);
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}
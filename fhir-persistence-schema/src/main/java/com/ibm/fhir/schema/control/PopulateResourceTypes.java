/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.model.type.code.FHIRResourceType;

/**
 * Populates the Resource Types Table
 */
public class PopulateResourceTypes implements IDatabaseStatement {
    private static final Logger LOGGER = Logger.getLogger(PopulateResourceTypes.class.getName());
    private final String adminSchemaName;
    private final String schemaName;
    private final int tenantId;

    public PopulateResourceTypes(String adminSchemaName, String schemaName, int tenantId) {
        this.adminSchemaName = adminSchemaName;
        this.schemaName      = schemaName;
        this.tenantId        = tenantId;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String stmtVariable = String.format("SET %s.SV_TENANT_ID = %d", adminSchemaName, tenantId);
        final String stmtResourceTypeInsert =
                String.format(
                        "INSERT INTO %s.resource_types (mt_id, resource_type_id, resource_type) " +
                                "VALUES %s.sv_tenant_id, ?, ?);",
                        schemaName, adminSchemaName);
        try (Statement s = c.createStatement(); PreparedStatement batch = c.prepareStatement(stmtResourceTypeInsert)) {
            s.execute(stmtVariable);
            try (InputStream fis =
                    PopulateResourceTypes.class.getClassLoader().getResourceAsStream("resource_types.properties")) {
                Properties props = new Properties();
                props.load(fis);

                for (Entry<Object, Object> valueEntry : props.entrySet()) {
                    Integer curVal = Integer.parseInt((String) valueEntry.getValue());
                    String resource = (String) valueEntry.getKey();
                    batch.setString(1, resource);
                    batch.setLong(2, curVal);
                }

                // Check Error Codes. 
                int[] codes = batch.executeBatch();
                int errorCodes = 0;
                for (int code : codes) {
                    if (code != 0) {
                        errorCodes++;
                    }
                }
                if (errorCodes > 0) {
                    LOGGER.severe("at least one of the Resource Types are not populated [" + errorCodes + "]");
                    c.rollback();
                }
            } catch (IOException e) {
                // Wrap and Send downstream 
                throw new IllegalArgumentException(e);
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    /**
     * This method is very intentional to verify the resource_types.properties on EVERY build.
     * The mapping is intentionally managed, as these KEYS are inserted and used.
     */
    public static void verify() {
        Properties props = new Properties();
        boolean found = false;
        try (InputStream fis =
                PopulateResourceTypes.class.getClassLoader().getResourceAsStream("resource_types.properties")) {
            props.load(fis);

            Set<String> resources = new HashSet<>();
            for (FHIRResourceType.ValueSet rt : FHIRResourceType.ValueSet.values()) {
                resources.add(rt.value());
            }

            // Find the Highest Value to start from:
            Integer highestValue = 0;
            Map<String, Integer> valueMap = new HashMap<>();
            for (Entry<Object, Object> valueEntry : props.entrySet()) {
                Integer curVal = Integer.parseInt((String) valueEntry.getValue());
                String resource = (String) valueEntry.getKey();
                valueMap.put(resource, curVal);
                if (highestValue < curVal) {
                    highestValue = curVal;
                }
                resources.remove(resource);
            }

            // Check to see if something is missing
            for (String resource : resources) {
                LOGGER.info(resource + "=" + highestValue++);
                found = true;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("File access issue for resource_types");
        }

        if (found) {
            throw new IllegalArgumentException("Resources are missing from resource_types");
        }

    }
}
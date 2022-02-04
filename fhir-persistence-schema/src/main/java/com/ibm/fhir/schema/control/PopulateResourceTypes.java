/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.core.FHIRVersionParam;
import com.ibm.fhir.core.util.ResourceTypeHelper;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Populates the Resource Types Table
 *
 * @implNote This class supports the multi-tenant schema and the single tenant.
 */
public class PopulateResourceTypes implements IDatabaseStatement {

    private static final Logger LOGGER = Logger.getLogger(PopulateResourceTypes.class.getName());
    private final String adminSchemaName;
    private final String schemaName;
    private final Integer tenantId;

    private static final String Y = "Y";

    public PopulateResourceTypes(String adminSchemaName, String schemaName, Integer tenantId) {
        this.adminSchemaName = adminSchemaName;
        this.schemaName = schemaName;
        this.tenantId = tenantId;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String stmtVariable = String.format("SET %s.SV_TENANT_ID = %d", adminSchemaName, tenantId);
        final String stmtResourceTypeInsert;
        final String RESOURCE_TYPES = String.format("SELECT resource_type_id, resource_type, retired FROM %s.resource_types", schemaName);
        if (tenantId != null) {
            stmtResourceTypeInsert = String.format("INSERT INTO %s.resource_types (mt_id, resource_type_id, resource_type) "
                    + "VALUES (%s.sv_tenant_id, ?, ?)", schemaName, adminSchemaName);
        } else {
            stmtResourceTypeInsert = String.format("INSERT INTO %s.resource_types (resource_type_id, resource_type) "
                    + "VALUES (?, ?)", schemaName);
        }
        final String stmtResourceTypeUpdate = String.format("UPDATE %s.resource_types SET retired = 'Y' WHERE resource_type = ?", schemaName);

        // Only if it's multitenant is tenantId not null.
        if (tenantId != null) {
            try (Statement s = c.createStatement();) {
                s.execute(stmtVariable);
            } catch (SQLException e) {
                throw translator.translate(e);
            }
        }

        Map<String, Integer> values = new HashMap<>();
        List<String> previouslyRetiredTypes = new ArrayList<>();
        try (PreparedStatement list = c.prepareStatement(RESOURCE_TYPES)) {
            list.execute();
            ResultSet rset = list.getResultSet();
            while (rset.next()) {
                Integer id = rset.getInt(1);
                String type = rset.getString(2);
                values.put(type, id);

                String retired = rset.getString(3);
                if (Y.equals(retired)) {
                    previouslyRetiredTypes.add(type);
                }
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }

        try (PreparedStatement batch = c.prepareStatement(stmtResourceTypeInsert)) {
            insertResourceTypes(values, batch);
        } catch (SQLException x) {
            SQLException exception = x.getNextException();
            for (int i = 0; exception != null; i++)
            while (exception != null) {
                LOGGER.log(Level.SEVERE, "Exception " + i, exception);
                exception = exception.getNextException();
            }
            throw translator.translate(x);
        }

        try (PreparedStatement batch = c.prepareStatement(stmtResourceTypeUpdate)) {
            updateResourceTypes(previouslyRetiredTypes, batch);
        } catch (SQLException x) {
            SQLException exception = x.getNextException();
            for (int i = 0; exception != null; i++)
            while (exception != null) {
                LOGGER.log(Level.SEVERE, "Exception " + i, exception);
                exception = exception.getNextException();
            }
            throw translator.translate(x);
        }
    }

    private void insertResourceTypes(Map<String, Integer> values, PreparedStatement batch) throws SQLException {
        try (InputStream fis =
                PopulateResourceTypes.class.getClassLoader().getResourceAsStream("resource_types.properties")) {
            Properties props = new Properties();
            props.load(fis);

            int numToProcess = 0;
            for (Entry<Object, Object> valueEntry : props.entrySet()) {
                Integer curVal = Integer.parseInt((String) valueEntry.getValue());
                String resource = (String) valueEntry.getKey();

                if (!values.containsKey(resource)) {
                    batch.setInt(1, curVal);
                    batch.setString(2, resource);
                    batch.addBatch();
                    numToProcess++;
                }
            }

            // Only execute with num to process
            if (numToProcess > 0) {
                // Check Error Codes.
                int[] codes = batch.executeBatch();
                int errorCodes = 0;
                for (int code : codes) {
                    if (code < 0) {
                        errorCodes++;
                    }
                }
                if (errorCodes > 0) {
                    String msg = "at least one of the Resource Types are not populated [" + errorCodes + "]";
                    LOGGER.severe(msg);
                    throw new IllegalArgumentException(msg);
                }
            }
        } catch (IOException e) {
            // Wrap and Send downstream
            throw new IllegalArgumentException(e);
        }
    }

    private void updateResourceTypes(List<String> alreadyRetiredTypes, PreparedStatement batch) throws SQLException {
        int numToProcess = 0;
        for (String removedType : ResourceTypeHelper.getRemovedResourceTypes(FHIRVersionParam.VERSION_43)) {
            if (!alreadyRetiredTypes.contains(removedType)) {
                batch.setString(1, removedType);
                batch.addBatch();
                numToProcess++;
            }
        }

        // Only execute with num to process
        if (numToProcess > 0) {
            // Check Error Codes.
            int[] codes = batch.executeBatch();
            int errorCodes = 0;
            for (int code : codes) {
                if (code < 0) {
                    errorCodes++;
                }
            }
            if (errorCodes > 0) {
                String msg = "at least one of the Resource Types was not updated [" + errorCodes + "]";
                LOGGER.severe(msg);
                throw new IllegalArgumentException(msg);
            }
        }
    }
}
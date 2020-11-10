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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.registry.FHIRRegistry;

/**
 * Populates the Parameters Names Table
 *
 * @implNote This class supports the multi-tenant schema and the single tenant.
 */
public class PopulateParameterNames implements IDatabaseStatement {

    private static final Logger LOGGER = Logger.getLogger(PopulateParameterNames.class.getName());
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
        final String stmtVariable = String.format("SET %s.SV_TENANT_ID = %d", adminSchemaName, tenantId);
        final String stmtResourceTypeInsert;
        final String PARAMETER_NAMES = String.format("SELECT PARAMETER_NAME_ID, PARAMETER_NAME FROM %s.parameter_names", schemaName);
        if (tenantId != null) {
            stmtResourceTypeInsert =
                    String.format("INSERT INTO %s.parameter_names (MT_ID, PARAMETER_NAME_ID, PARAMETER_NAME) "
                            + "VALUES (%s.sv_tenant_id, ?, ?)", schemaName, adminSchemaName);
        } else {
            stmtResourceTypeInsert =
                    String.format("INSERT INTO %s.parameter_names (PARAMETER_NAME_ID, PARAMETER_NAME) " + "VALUES (?, ?)", schemaName);
        }

        Map<String, Integer> values = new HashMap<>();
        try (PreparedStatement list = c.prepareStatement(PARAMETER_NAMES)) {
            list.execute();
            ResultSet rset = list.getResultSet();
            while (rset.next()) {
                Integer id = rset.getInt(1);
                String type = rset.getString(1);
                values.put(type, id);
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }

        try (PreparedStatement batch = c.prepareStatement(stmtResourceTypeInsert)) {
            // Only if it's multitenant is tenantId not null.
            if (tenantId != null) {
                try (Statement s = c.createStatement();) {
                    s.execute(stmtVariable);
                }
            }

            try (InputStream fis =
                    PopulateParameterNames.class.getClassLoader().getResourceAsStream("parameter_names.properties")) {
                Properties props = new Properties();
                props.load(fis);

                int numToProcess = 0;
                for (Entry<Object, Object> valueEntry : props.entrySet()) {
                    Integer curVal = Integer.parseInt((String) valueEntry.getValue());
                    String code = (String) valueEntry.getKey();

                    if (values.containsKey(code)) {
                        batch.setLong(1, curVal);
                        batch.setString(2, code);
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
                        String msg = "at least one of the Paramater Name/Codes are not populated [" + errorCodes + "]";
                        LOGGER.severe(msg);
                        throw new IllegalArgumentException(msg);
                    }
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
     * This method is very intentional to verify the parameter_names.properties on EVERY build.
     * The mapping is intentionally managed, as these KEYS are inserted and used.
     */
    public static void verify() {
        Properties props = new Properties();
        boolean found = false;
        try (InputStream fis =
                PopulateParameterNames.class.getClassLoader().getResourceAsStream("parameter_names.properties")) {
            props.load(fis);

            Set<String> codes = new HashSet<>();
            for (SearchParamType.ValueSet spt : SearchParamType.ValueSet.values()) {
                Collection<SearchParameter> searchParametersForResourceType =
                        FHIRRegistry.getInstance().getSearchParameters(spt.value());
                for (SearchParameter searchParameter : searchParametersForResourceType) {
                    codes.add(searchParameter.getCode().getValue());
                }
            }

            // Sort Codes based on where system prefix '_' comes first.
            List<String> codesList = new ArrayList<>(codes);
            Collections.sort(codesList);

            // Find the Highest Value to start from:
            Integer highestValue = 1001;
            Map<String, Integer> valueMap = new HashMap<>();
            for (Entry<Object, Object> valueEntry : props.entrySet()) {
                Integer curVal = Integer.parseInt((String) valueEntry.getValue());
                String resource = (String) valueEntry.getKey();
                valueMap.put(resource, curVal);
                if (highestValue < curVal) {
                    highestValue = curVal;
                }
                codes.remove(resource);
            }

            // Check to see if something is missing
            for (String code : codes.stream().sorted().collect(Collectors.toList())) {
                LOGGER.info(code + "=" + highestValue++);
                found = true;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("File access issue for parameter_names");
        }

        if (found) {
            throw new IllegalArgumentException("Parameter Name/Code are missing from parameter_names");
        }
    }
}
/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Check to see if we have any data in LOGICAL_RESOURCE_IDENT. If it is empty,
 * we assume we need to perform a migration
 */
public class GetLogicalResourceNeedsV0027Migration implements IDatabaseSupplier<Boolean> {

    // The FHIR data schema
    private final String schemaName;

    /**
     * Public constructor
     * 
     * @param schemaName
     */
    public GetLogicalResourceNeedsV0027Migration(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result = true;
        final String tableName = DataDefinitionUtil.getQualifiedName(schemaName, "LOGICAL_RESOURCE_IDENT");
        final String SQL = "SELECT 1 FROM " + tableName + " " + translator.limit("1");

        try (Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(SQL);
            if (rs.next()) {
                // logical_resource_ident already contains data, so no need to migrate
                result = false;
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }

        return result;
    }
}
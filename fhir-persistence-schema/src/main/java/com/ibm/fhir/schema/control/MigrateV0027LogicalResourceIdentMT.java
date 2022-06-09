/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Populate LOGICAL_RESOURCE_IDENT with records from LOGICAL_RESOURCES.
 * Variant for use with the Db2 multitenant schema.
 */
public class MigrateV0027LogicalResourceIdentMT implements IDatabaseStatement {

    // The FHIR data schema
    private final String schemaName;

    /**
     * Public constructor
     * @param schemaName
     */
    public MigrateV0027LogicalResourceIdentMT(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String logicalResources = DataDefinitionUtil.getQualifiedName(schemaName, "LOGICAL_RESOURCES");
        final String logicalResourceIdent = DataDefinitionUtil.getQualifiedName(schemaName, "LOGICAL_RESOURCE_IDENT");
        final String DML = ""
                + "INSERT INTO " + logicalResourceIdent +"(mt_id, resource_type_id, logical_id, logical_resource_id) "
                + "     SELECT mt_id, resource_type_id, logical_id, logical_resource_id "
                + "       FROM " + logicalResources;

        try (PreparedStatement ps = c.prepareStatement(DML)) {
                ps.executeUpdate();
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}
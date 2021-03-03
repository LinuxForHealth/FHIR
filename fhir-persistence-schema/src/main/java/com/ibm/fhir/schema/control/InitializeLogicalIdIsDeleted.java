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
 * Set the current value for xxx_LOGICAL_RESOURCES.IS_DELETED. Called as
 * part of the schema migration step for schema version V0010 and V0011.
 * Note that for this to work for the multi-tenant (Db2) schema,
 * the SV_TENANT_ID needs to be set first.
 */
public class InitializeLogicalIdIsDeleted implements IDatabaseStatement {
    private final String schemaName;
    private final String resourceTypeName;

    /**
     * Public constructor
     *
     * @param schemaName
     * @param tableName
     */
    public InitializeLogicalIdIsDeleted(String schemaName, String resourceTypeName) {
        this.schemaName = schemaName;
        this.resourceTypeName = resourceTypeName;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {

        final String lrTable = DataDefinitionUtil.getQualifiedName(schemaName, resourceTypeName + "_LOGICAL_RESOURCES");
        final String rTable = DataDefinitionUtil.getQualifiedName(schemaName, resourceTypeName + "_RESOURCES");

        // Correlated update to grab the IS_DELETED and LAST_UPDATED values from xxx_RESOURCES and use them to
        // set xxx_LOGICAL_RESOURCES.IS_DELETED and LAST_UPDATED for the current_resource_id.
        final String DML = "UPDATE " + lrTable + " lr "
                + " SET (is_deleted, last_updated) = (SELECT r.is_deleted, r.last_updated FROM " + rTable + " r WHERE r.resource_id = lr.current_resource_id)"
                ;

        try (PreparedStatement ps = c.prepareStatement(DML)) {
            ps.executeUpdate();
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}
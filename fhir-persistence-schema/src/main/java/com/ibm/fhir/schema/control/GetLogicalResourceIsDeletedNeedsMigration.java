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
 * Checks the value of IS_DELETED from the first row found in xxx_LOGICAL_RESOURCES.
 * If this value is "X", it means that the table needs to be migrated. If the table
 * is empty, then obviously there's no need for migration
 */
public class GetLogicalResourceIsDeletedNeedsMigration implements IDatabaseSupplier<Boolean> {
    private final String schemaName;
    private final String resourceTypeName;

    public GetLogicalResourceIsDeletedNeedsMigration(String schemaName, String resourceTypeName) {
        this.schemaName = schemaName;
        this.resourceTypeName = resourceTypeName;
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result = false;
        final String tableName = DataDefinitionUtil.getQualifiedName(schemaName, resourceTypeName + "_LOGICAL_RESOURCES");
        final String SQL = "SELECT is_deleted "
                + "  FROM " + tableName + " "
                + translator.limit("1");

        try (Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(SQL);
            if (rs.next() && "X".equals(rs.getString(1))) {
                result = true;
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }

        return result;
    }
}
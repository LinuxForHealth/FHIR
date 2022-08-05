/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.schema.control;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseSupplier;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Checks the value of IS_DELETED is "X" for any row found in LOGICAL_RESOURCES
 * the given resource type. If found, it means that the table needs to be
 * migrated. If not found, then obviously there's no need for migration.
 */
public class GetLogicalResourceNeedsV0014Migration implements IDatabaseSupplier<Boolean> {

    // The FHIR data schema
    private final String schemaName;

    // The database RESOURCE_TYPES.RESOURCE_TYPE_ID value for the subset we want to look for
    private final int resourceTypeId;

    public GetLogicalResourceNeedsV0014Migration(String schemaName, int resourceTypeId) {
        this.schemaName = schemaName;
        this.resourceTypeId = resourceTypeId;
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        // The LOGICAL_RESOURCES table has an index {RESOURCE_TYPE_ID, LOGICAL_ID}
        // which we can leverage here to filter by resource type without needing
        // to join to the XX_LOGICAL_RESOURCES table.
        Boolean result = false;
        final String tableName = DataDefinitionUtil.getQualifiedName(schemaName, "LOGICAL_RESOURCES");
        final String SQL = "SELECT is_deleted "
                + "  FROM " + tableName
                + " WHERE resource_type_id = " + this.resourceTypeId
                + "  AND is_deleted = 'X' " + translator.limit("1");

        try (Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(SQL);
            if (rs.next()) {
                result = true;
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }

        return result;
    }
}
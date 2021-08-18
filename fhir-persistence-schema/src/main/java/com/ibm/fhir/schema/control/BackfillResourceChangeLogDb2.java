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
 * Backfill the RESOURCE_CHANGE_LOG table using all the existing _RESOURCE
 * records for the given resourceType. For multi-tenant schemas, the
 * table definition looks like this:
 *
                                 Data type                     Column
Column name                     schema    Data type name      Length     Scale Nulls
------------------------------- --------- ------------------- ---------- ----- ------
MT_ID                           SYSIBM    INTEGER                      4     0 No
RESOURCE_ID                     SYSIBM    BIGINT                       8     0 No
RESOURCE_TYPE_ID                SYSIBM    INTEGER                      4     0 No
LOGICAL_RESOURCE_ID             SYSIBM    BIGINT                       8     0 No
CHANGE_TSTAMP                   SYSIBM    TIMESTAMP                   10     6 No
VERSION_ID                      SYSIBM    INTEGER                      4     0 No
CHANGE_TYPE                     SYSIBM    CHARACTER                    1     0 No

 * This variant is used for the Db2 multi-tenant schema and must be invoked on
 * a per-tenant basis (the FHIR_ADMIN.SV_TENANT_ID value must be set prior to invoking
 * run on this class)
 */
public class BackfillResourceChangeLogDb2 implements IDatabaseStatement {
    private final String schemaName;
    private final String resourceType;

    /**
     * Public constructor
     *
     * @param schemaName
     * @param resourceType
     */
    public BackfillResourceChangeLogDb2(String schemaName, String resourceType) {
        this.schemaName = schemaName;
        this.resourceType = resourceType;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {

        final String rclTable = DataDefinitionUtil.getQualifiedName(schemaName, "RESOURCE_CHANGE_LOG");
        final String rTable = DataDefinitionUtil.getQualifiedName(schemaName, resourceType + "_RESOURCES");
        final String rtTable = DataDefinitionUtil.getQualifiedName(schemaName, "RESOURCE_TYPES");
        final String DML = "INSERT INTO " + rclTable
                + "(mt_id, resource_id, resource_type_id, logical_resource_id, change_tstamp, version_id, change_type) "
                + "   SELECT r.mt_id, r.resource_id, rt.resource_type_id, r.logical_resource_id, r.last_updated, r.version_id, "
                + "          CASE WHEN r.is_deleted = 'Y' THEN 'D' WHEN r.version_id > 1 THEN 'U' ELSE 'C' END "
                + "     FROM " +  rTable + " AS r, "
                +                rtTable + " AS rt "
                + "    WHERE rt.resource_type = ? "
                + " ORDER BY r.resource_id";

        try (PreparedStatement ps = c.prepareStatement(DML)) {
            ps.setString(1, resourceType);
            ps.executeUpdate();
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}
/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.tenant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Db2-only DAO to delete a tenant key record
 */
public class DeleteTenantKeyDAO implements IDatabaseStatement {
    private final String schemaName;
    private final int tenantId;
    private final String tenantKey;
    private int count = 0;

    /**
     * Public constructor
     *
     * @param schemaName
     * @param tenantId
     * @param tenantKey
     */
    public DeleteTenantKeyDAO(String schemaName, int tenantId, String tenantKey) {
        DataDefinitionUtil.assertValidName(schemaName);
        this.schemaName = schemaName;
        this.tenantId = tenantId;
        this.tenantKey = tenantKey;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String tableName = DataDefinitionUtil.getQualifiedName(schemaName, "TENANT_KEYS");
        final String SQL;
        if (tenantKey == null) {
            SQL = "DELETE FROM " + tableName + "  WHERE mt_id = ?";
        } else {
            SQL = "DELETE FROM " + tableName + "  WHERE mt_id = ?"
                    + " AND tenant_hash = sysibm.hash(tenant_salt || ?, 2)";
        }

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setInt(1, tenantId);

            if (tenantKey != null) {
                ps.setString(2, tenantKey);
            }
            count = ps.executeUpdate();
        } catch (SQLException x) {
            // Translate the exception into something a little more meaningful
            // for this database type and application
            throw translator.translate(x);
        }
    }

    /**
     * @return total number of keys removed
     */
    public int getCount() {
        return count;
    }
}

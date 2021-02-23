/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Sets the FHIR_ADMIN.SV_TENANT_ID variable which is used for row-based permissions
 * in the Db2 multi-tenant schema. Because we are running as admin, we can set this
 * variable directly and do not need to use the stored procedure (which would required
 * the tenant key).
 */
public class SetTenantIdDb2 implements IDatabaseStatement {
    private final String adminSchemaName;
    private final int tenantId;

    /**
     * Public constructor
     *
     * @param schemaName
     * @param resourceType
     */
    public SetTenantIdDb2(String adminSchemaName, int tenantId) {
        this.adminSchemaName = adminSchemaName;
        this.tenantId = tenantId;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String variableName = DataDefinitionUtil.getQualifiedName(adminSchemaName, "SV_TENANT_ID");
        final String SQL = "SET " + variableName + " = " + this.tenantId;
        try (Statement s = c.createStatement()) {
            s.executeUpdate(SQL);
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}
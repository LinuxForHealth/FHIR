/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * DAO command to call the set_tenant fhir admin procedure which will set the
 * fhir_admin.sv_tenant_id variable but only if the correct tenantKey is
 * provided.
 */
public class Db2SetTenantVariable implements IDatabaseStatement {
    // The schema where the set_tenant procedure resides
    private final String schemaName;
    private final String tenantName;
    private final String tenantKey;

    /**
     * Public constructor
     * @param schemaName
     * @param tenantName
     */
    public Db2SetTenantVariable(String schemaName, String tenantName, String tenantKey) {
        DataDefinitionUtil.assertValidName(schemaName);
        this.schemaName = schemaName;
        this.tenantName = tenantName;
        this.tenantKey = tenantKey;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String call = "CALL " + schemaName + ".set_tenant(?, ?)"; 
        
        try (CallableStatement cs = c.prepareCall(call)) {
            cs.setString(1, tenantName);
            cs.setString(2, tenantKey);
            cs.executeUpdate();
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}

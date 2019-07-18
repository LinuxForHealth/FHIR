/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.tenant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ibm.watsonhealth.database.utils.api.IDatabaseStatement;
import com.ibm.watsonhealth.database.utils.api.IDatabaseTranslator;
import com.ibm.watsonhealth.database.utils.api.TenantStatus;
import com.ibm.watsonhealth.database.utils.common.DataDefinitionUtil;

/**
 * DAO to create a free tenant slot (to align with a new partition)
 * @author rarnold
 */
public class AddTenantDAO implements IDatabaseStatement {
    private final String schemaName;
    private final int tenantId;
    private final String tenantName;

    /**
     * Public constructor
     * @param schemaName
     * @param tenantId
     * @param tenantName
     * @param tenantKey
     * @param tenantSalt
     */
    public AddTenantDAO(String schemaName, int tenantId, String tenantName) {
        DataDefinitionUtil.assertValidName(schemaName);
        this.schemaName = schemaName;
        this.tenantId = tenantId;
        this.tenantName = tenantName;
    }
    
    
    /**
     * Execute the encapsulated query against the database and stream the result data to the
     * configured target
     * @param c
     */
    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String tableName = DataDefinitionUtil.getQualifiedName(schemaName, "TENANTS");
        final String SQL = ""
                + "   INSERT INTO " + tableName + "(tenant_id, tenant_status, tenant_name)"
                + "        VALUES (?, ?, ?)"
                ;

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setInt(1, tenantId);
            ps.setString(2, TenantStatus.PROVISIONING.name());
            ps.setString(3, tenantName);
            ps.executeUpdate();
        }
        catch (SQLException x) {
            // Translate the exception into something a little more meaningful
            // for this database type and application
            throw translator.translate(x);
        }
    }
}

/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.tenant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.watsonhealth.database.utils.api.IDatabaseSupplier;
import com.ibm.watsonhealth.database.utils.api.IDatabaseTranslator;
import com.ibm.watsonhealth.database.utils.api.TenantStatus;
import com.ibm.watsonhealth.database.utils.common.DataDefinitionUtil;
import com.ibm.watsonhealth.database.utils.model.Tenant;

/**
 * DAO to create a free tenant slot (to align with a new partition)
 * @author rarnold
 */
public class GetTenantDAO implements IDatabaseSupplier<Tenant> {
    private final String schemaName;
    private final String tenantName;
    
    /**
     * Get partition information for all tables in the tableSchema, using
     * the catalogSchema as the schema containing the DATAPARTITIONS system table
     * @param translator
     * @param catalogSchema
     * @param tableSchema
     */
    public GetTenantDAO(String schemaName, String tenantName) {
        DataDefinitionUtil.assertValidName(schemaName);
        this.schemaName = schemaName;
        this.tenantName = tenantName;
    }
    
    
    /**
     * Execute the encapsulated query against the database and stream the result data to the
     * configured target
     * @param c
     */
    @Override
    public Tenant run(IDatabaseTranslator translator, Connection c) {
        final String tableName = DataDefinitionUtil.getQualifiedName(schemaName, "TENANTS");
        final String SQL = ""
                + "   SELECT mt_id, tenant_status "
                + "     FROM " + tableName
                + "    WHERE tenant_name = ?";

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setString(1, this.tenantName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Tenant result = new Tenant();
                result.setTenantName(tenantName);
                result.setTenantId(rs.getInt(1));
                result.setTenantStatus(TenantStatus.valueOf(rs.getString(2)));
                return result;
            }
            else {
                return null;
            }
        }
        catch (SQLException x) {
            // Translate the exception into something a little more meaningful
            // for this database type and application
            throw translator.translate(x);
        }
    }
}

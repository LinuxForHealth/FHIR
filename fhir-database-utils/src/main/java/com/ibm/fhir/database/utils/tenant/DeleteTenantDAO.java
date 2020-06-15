/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.tenant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.TenantStatus;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Delete the tenant meta-data after it has been dropped. Deletes
 * data in TENANT_KEYS and TENANTS.
 */
public class DeleteTenantDAO implements IDatabaseStatement {
    // the FHIR admin schema name where the TENANTS and TENANT_KEYS tables live
    private final String schemaName;
    
    // The id of the tenant to delete
    private final int tenantId;
    
    /**
     * Get partition information for all tables in the tableSchema, using
     * the catalogSchema as the schema containing the DATAPARTITIONS system table
     * 
     * @param schemaName the name of the admin schema hosting the tenants and tenant_keys tables
     * @param tenantId the id of the tenant to delete
     */
    public DeleteTenantDAO(String schemaName, int tenantId) {
        DataDefinitionUtil.assertValidName(schemaName);
        this.schemaName = schemaName;
        this.tenantId = tenantId;
    }
    
    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        deleteFromTenantKeys(translator, c);
        deleteFromTenants(translator, c);
    }
    
    protected void deleteFromTenantKeys(IDatabaseTranslator translator, Connection c) {
        final String tableName = DataDefinitionUtil.getQualifiedName(schemaName, "TENANT_KEYS");
        final String DML = ""
                + "   DELETE FROM " + tableName
                + "    WHERE mt_id = ? "
                ;

        try (PreparedStatement ps = c.prepareStatement(DML)) {
            ps.setInt(1, tenantId);
            ps.executeUpdate();
        } catch (SQLException x) {
            // Translate the exception into something a little more meaningful
            // for this database type and application
            throw translator.translate(x);
        }
        
    }

    protected void deleteFromTenants(IDatabaseTranslator translator, Connection c) {
        final String tableName = DataDefinitionUtil.getQualifiedName(schemaName, "TENANTS");
        final String DML = ""
                + "   DELETE FROM " + tableName
                + "    WHERE mt_id = ? "
                + "      AND tenant_status = ?"
                ;

        try (PreparedStatement ps = c.prepareStatement(DML)) {
            ps.setInt(1, tenantId);
            ps.setString(2, TenantStatus.DROPPED.name());
            int rows = ps.executeUpdate();
            
            if (rows < 1) {
                throw new DataAccessException("Invalid tenant_id or tenant not DROPPED");
            }
        } catch (SQLException x) {
            // Translate the exception into something a little more meaningful
            // for this database type and application
            throw translator.translate(x);
        }
        
    }

}

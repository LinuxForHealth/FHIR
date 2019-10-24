/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.tenant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.TenantStatus;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * DAO to create a free tenant slot (to align with a new partition)
 */
public class UpdateTenantStatusDAO implements IDatabaseStatement {
    private final String schemaName;
    private final int tenantId;
    private final TenantStatus tenantStatus;
    
    /**
     * Get partition information for all tables in the tableSchema, using
     * the catalogSchema as the schema containing the DATAPARTITIONS system table
     * 
     * @param schemaName
     * @param tenantId
     * @param tenantStatus
     */
    public UpdateTenantStatusDAO(String schemaName, int tenantId, TenantStatus tenantStatus) {
        DataDefinitionUtil.assertValidName(schemaName);
        this.schemaName = schemaName;
        this.tenantId = tenantId;
        this.tenantStatus = tenantStatus;
    }
    
    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        /*
         * Execute the encapsulated query against the database and stream the result data to the
         * configured target
         */
        final String tableName = DataDefinitionUtil.getQualifiedName(schemaName, "TENANTS");
        final String DML = ""
                + "   UPDATE " + tableName
                + "      SET tenant_status = ? "
                + "    WHERE mt_id = ? "
                ;

        try (PreparedStatement ps = c.prepareStatement(DML)) {
            ps.setString(1, tenantStatus.name());
            ps.setInt(2, tenantId);
            ps.executeUpdate();
        }
        catch (SQLException x) {
            // Translate the exception into something a little more meaningful
            // for this database type and application
            throw translator.translate(x);
        }
    }
}

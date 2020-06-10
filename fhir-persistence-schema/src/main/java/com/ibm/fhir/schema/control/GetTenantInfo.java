/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.TenantStatus;

/**
 * Fetch the {@link TenantInfo} for the tenantName. Returns a null
 * TenantInfo if the tenant does not exist
 */
public class GetTenantInfo implements IDatabaseSupplier<TenantInfo> {
    private final String adminSchema;
    private final String tenantName;

    public GetTenantInfo(String adminSchema, String tenantName) {
        this.adminSchema = adminSchema;
        this.tenantName = tenantName;
    }

    @Override
    public TenantInfo run(IDatabaseTranslator translator, Connection c) {
        TenantInfo result;

        // We take a look at the catalog to find which schema has partitions
        // for each tenant. This would, of course, be easier if the schema
        // had been stored as an attribute of the tenant in the first place.
        final String SQL = ""
                + "SELECT "
                + "       t.mt_id, t.tenant_name, "
                + "       t.tenant_status, "
                + "       dp.tabschema "
                + "  FROM " + adminSchema + ".TENANTS AS t "
                + "LEFT OUTER JOIN syscat.datapartitions dp "
                + "             ON (dp.tabname = 'LOGICAL_RESOURCES' "
                + "            AND dp.datapartitionname = CONCAT('TENANT', t.mt_id))"
                + " WHERE t.tenant_name = ?";
        

        try (PreparedStatement s = c.prepareStatement(SQL)) {
            s.setString(1, tenantName);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                result = new TenantInfo();
                result.setTenantId(rs.getInt(1));
                result.setTenantName(rs.getString(2));
                result.setTenantStatus(TenantStatus.valueOf(rs.getString(3)));
                result.setTenantSchema(rs.getString(4));
            }
            else {
                result = null;
            }
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
        
        return result;
    }

}

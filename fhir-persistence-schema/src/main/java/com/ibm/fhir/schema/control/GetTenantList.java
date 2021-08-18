/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.TenantStatus;

/**
 * Fetch the list of tenants currently configured
 */
public class GetTenantList implements IDatabaseSupplier<List<TenantInfo>> {
    private final String adminSchema;

    /**
     * Public constructor
     * @param adminSchema
     */
    public GetTenantList(String adminSchema) {
        this.adminSchema = adminSchema;
    }

    @Override
    public List<TenantInfo> run(IDatabaseTranslator translator, Connection c) {
        List<TenantInfo> result = new ArrayList<>();

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
                + " ORDER BY t.mt_id";


        try (Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(SQL);
            while (rs.next()) {
                TenantInfo dto = new TenantInfo();
                dto.setTenantId(rs.getInt(1));
                dto.setTenantName(rs.getString(2));
                dto.setTenantStatus(TenantStatus.valueOf(rs.getString(3)));
                // DB2 pads SYSCAT.DATAPARTITIONS.TABSCHEMA with spaces if less than 8 characters
                String tenantSchema = rs.getString(4);
                if (tenantSchema != null) {
                    dto.setTenantSchema(tenantSchema.trim());
                }
                result.add(dto);
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }

        return result;
    }

}

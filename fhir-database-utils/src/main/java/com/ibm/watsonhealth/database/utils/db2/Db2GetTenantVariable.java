/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.db2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.watsonhealth.database.utils.api.IDatabaseSupplier;
import com.ibm.watsonhealth.database.utils.api.IDatabaseTranslator;
import com.ibm.watsonhealth.database.utils.common.DataDefinitionUtil;

/**
 * DAO to fetch the current value of FHIRADMIN.SV_TENANT_ID. Useful for
 * debugging - the value isn't useful outside of the database.
 * @author rarnold
 *
 */
public class Db2GetTenantVariable implements IDatabaseSupplier<Integer> {
    // The schema where the set_tenant procedure resides
    private final String schemaName;
    
    /**
     * Public constructor
     * @param schemaName
     * @param tenantName
     */
    public Db2GetTenantVariable(String schemaName) {
        DataDefinitionUtil.assertValidName(schemaName);
        this.schemaName = schemaName;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseStatement#run(com.ibm.watsonhealth.database.utils.api.IDatabaseTranslator, java.sql.Connection)
     */
    @Override
    public Integer run(IDatabaseTranslator translator, Connection c) {
        final String fqn = DataDefinitionUtil.getQualifiedName(schemaName, "SV_TENANT_ID");
        final String SQL = "VALUES " + fqn; 
        
        try (Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(SQL);
            if (rs.next()) {
                int result = rs.getInt(1);
                if (rs.wasNull()) {
                    return null;
                }
                else {
                    return result;
                }
            }
            else {
                // can't really happen, of course
                throw new IllegalStateException("no rows returned!");
            }
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}

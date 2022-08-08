/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.schema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseSupplier;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;
import org.linuxforhealth.fhir.database.utils.version.SchemaConstants;

/**
 * Clear the lease if it is currently held by the given instance
 */
public class CancelLease implements IDatabaseSupplier<Boolean> {
    private final String adminSchema;
    
    // The FHIR data schema for which we want to obtain the lease
    private final String schemaName;

    // An identifier unique to this instance of the schema update tool
    private final String leaseId;
    
    /**
     * Public constructor
     * @param adminSchema
     * @param schemaName
     * @param leaseId
     */
    public CancelLease(String adminSchema, String schemaName, String leaseId) {
        this.adminSchema = adminSchema;
        this.schemaName = schemaName;
        this.leaseId = leaseId;
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result = Boolean.FALSE;

        // Remove the lease if we owned it
        final String CONTROL = DataDefinitionUtil.getQualifiedName(adminSchema, SchemaConstants.CONTROL);
        final String DEL = "DELETE FROM " + CONTROL
                + " WHERE schema_name = ? "
                + "   AND lease_owner_uuid = ?";
        
        try (PreparedStatement ps = c.prepareStatement(DEL)) {
            ps.setString(1, this.schemaName);
            ps.setString(2, leaseId);
            
            int rowsAffected = ps.executeUpdate();
            if (1 == rowsAffected) {
                result = Boolean.TRUE;
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
       
        return result;
    }
}
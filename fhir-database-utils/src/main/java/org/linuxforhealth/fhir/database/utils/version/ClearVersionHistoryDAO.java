/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.version;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Clear all version history records for a particular schema
 * (used when dropping a schema).
 */
public class ClearVersionHistoryDAO implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(ClearVersionHistoryDAO.class.getName());
    
    // The admin schema holding the history table
    private final String adminSchemaName;

    // The schema we wish to clear
    private final String schemaName;

    /**
     * Public constructor
     * 
     * @param adminSchemaName
     * @param schemaName
     */
    public ClearVersionHistoryDAO(String adminSchemaName, String schemaName) {
        this.adminSchemaName = adminSchemaName;
        this.schemaName = schemaName;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String tbl = DataDefinitionUtil.getQualifiedName(adminSchemaName, SchemaConstants.VERSION_HISTORY);
        final String dml = "DELETE FROM " + tbl + " WHERE " + SchemaConstants.SCHEMA_NAME + " = ?";
        try (PreparedStatement ps = c.prepareStatement(dml)) {
            ps.setString(1, schemaName);
            ps.executeUpdate();
        }
        catch (SQLException x) {
            logger.log(Level.SEVERE, dml, x);
            throw translator.translate(x);
        }
    }
}

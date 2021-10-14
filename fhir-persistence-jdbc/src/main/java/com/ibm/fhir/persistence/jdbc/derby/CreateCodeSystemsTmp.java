/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.derby;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.schema.control.FhirSchemaConstants;

/**
 * Create the CODE_SYSTEMS_TMP table
 */
public class CreateCodeSystemsTmp implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(CreateCommonTokenValuesTmp.class.getName());
    
    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        
        if (!isExists(c)) {
            final String ddl = ""
                    + "DECLARE GLOBAL TEMPORARY TABLE SESSION.code_systems_tmp ("
                    + "  code_system_name VARCHAR(" + FhirSchemaConstants.MAX_TOKEN_VALUE_BYTES + "), "
                    + "  code_system_id BIGINT"
                    + ") NOT LOGGED";
            
            try (Statement s = c.createStatement()) {
                s.executeUpdate(ddl);
            } catch (SQLException x) {
                logger.log(Level.SEVERE, ddl, x);
                throw translator.translate(x);
            }
        }
    }
    
    /**
     * Does the table currently exist
     * @param c
     * @return
     */
    private boolean isExists(Connection c) {
        boolean result = false;
        
        final String sql = "SELECT 1 FROM SESSION.code_systems_tmp WHERE 1=0";
        try (Statement s = c.createStatement()) {
            s.executeQuery(sql);
            result = true;
        } catch (SQLException x) {
            // NOP
        }
        
        return result;
    }

}
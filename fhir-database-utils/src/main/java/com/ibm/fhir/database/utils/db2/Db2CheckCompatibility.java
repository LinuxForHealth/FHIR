/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.version.SchemaConstants;

/**
 * Checks Compatibility with the DB2 Implementation that is used. 
 */
public class Db2CheckCompatibility implements IDatabaseStatement {
    private final String adminSchema;

    /**
     * Public constructor
     * @param adminSchema
     */
    public Db2CheckCompatibility(String adminSchema) {
        DataDefinitionUtil.assertValidName(adminSchema);
        this.adminSchema = adminSchema;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        // a suitable SQL statement with a TIMESTAMP column
        final String tbl = DataDefinitionUtil.getQualifiedName(adminSchema, "VERSION_HISTORY");
        final String SQL = "SELECT * FROM " + tbl + " WHERE " + SchemaConstants.APPLIED + " < ?";
                
        // We are optimistic and assume that the tablespace doesn't yet exist. 
        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            // JDBC-4.2 supports direct use of java.time (THANK YOU!)
            // Unfortunately as of Data Server Driver Package v11.1.3.3,
            // this still doesn't work for DB2
            ps.setObject(1, Timestamp.from(Instant.now()), Types.TIMESTAMP);
            ps.executeQuery();
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}
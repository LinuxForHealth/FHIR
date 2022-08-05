/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.version;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.model.InsertStatement;

/**
 * Add the {type, name, version} record to the database. Idempotent,
 * so if it already exists, it's a NOP.
 */
public class AddVersionDAO implements IDatabaseStatement {
    // The admin schema holding the history table
    private final String adminSchemaName;

    // The schema, type and name of the object we want to manage
    private final String schemaName;
    private final String type;
    private final String name;
    private final int version;

    public AddVersionDAO(String adminSchemaName, String schemaName, String type, String name, int version) {
        this.adminSchemaName = adminSchemaName;
        this.schemaName = schemaName;
        this.type = type;
        this.name = name;
        this.version = version;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        String currentTimeStamp;
        if (translator.getDriverClassName().contains("postgresql")) {
            currentTimeStamp =  "CURRENT_TIMESTAMP";
        } else {
            currentTimeStamp = "CURRENT TIMESTAMP";
        }
        InsertStatement.Builder insBuilder = InsertStatement.builder(adminSchemaName, SchemaConstants.VERSION_HISTORY)
                .addColumn(SchemaConstants.SCHEMA_NAME)
                .addColumn(SchemaConstants.OBJECT_TYPE)
                .addColumn(SchemaConstants.OBJECT_NAME)
                .addColumn(SchemaConstants.VERSION)
                .addColumn(SchemaConstants.APPLIED, currentTimeStamp);

        final InsertStatement ins = insBuilder.build();

        try (PreparedStatement ps = c.prepareStatement(ins.toString())) {
            ps.setString(1, schemaName);
            ps.setString(2, type);
            ps.setString(3, name);
            ps.setInt(4, version);
            ps.executeUpdate();
        }
        catch (SQLException x) {
            // suppress any complaints about duplicates because we want this to
            // be idempotent
            if (!translator.isDuplicate(x)) {
                throw translator.translate(x);
            }
        }
    }

}

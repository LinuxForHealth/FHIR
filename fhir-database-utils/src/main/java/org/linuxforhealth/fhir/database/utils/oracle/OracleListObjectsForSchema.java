/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.database.utils.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseSupplier;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;
import org.linuxforhealth.fhir.database.utils.common.SchemaInfoObject;
import org.linuxforhealth.fhir.database.utils.common.SchemaInfoObject.Type;

/**
 * Fetch a list of objects that exist in a given schema
 */
public class OracleListObjectsForSchema implements IDatabaseSupplier<List<SchemaInfoObject>> {
    private final String schemaName;
    public OracleListObjectsForSchema(String schemaName) {
        this.schemaName = DataDefinitionUtil.assertValidName(schemaName);
    }

    @Override
    public List<SchemaInfoObject> run(IDatabaseTranslator translator, Connection c) {
        List<SchemaInfoObject> result = new ArrayList<>();
        final String sql = ""
                + "SELECT object_name, object_type "
                + "  FROM all_objects "
                + " WHERE owner = ? "
                + "   AND object_name NOT LIKE 'ISEQ$$%'" // ignore this
                ;
        
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, schemaName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final String objectName = rs.getString(1);
                final String objectType = rs.getString(2);
                if (objectType != null) {
                    switch (objectType) {
                    case "TABLE":
                        result.add(new SchemaInfoObject(Type.TABLE, objectName));
                        break;
                    case "VIEW":
                        result.add(new SchemaInfoObject(Type.VIEW, rs.getString(1)));
                        break;
                    case "PROCEDURE":
                        result.add(new SchemaInfoObject(Type.PROCEDURE, rs.getString(1)));
                        break;
                    case "INDEX":
                        result.add(new SchemaInfoObject(Type.INDEX, rs.getString(1)));
                        break;
                    case "SEQUENCE":
                        result.add(new SchemaInfoObject(Type.SEQUENCE, rs.getString(1)));
                        break;
                    case "LOB":
                        // NOP
                        break;
                    default:
                        throw new IllegalStateException("Unexpected object found in schema: type: '" + objectType + "' name: '" + objectName + "'");
                    }
                }
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
        
        return result;
    };
    
}

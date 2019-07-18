/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.version;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.ibm.watsonhealth.database.utils.api.IDatabaseSupplier;
import com.ibm.watsonhealth.database.utils.api.IDatabaseTranslator;
import com.ibm.watsonhealth.database.utils.common.DataDefinitionUtil;

/**
 * Get the latest version by object type and name. This is important, because
 * we deploy the schema in parallel, and so certain objects might end up with
 * different versions at different times (in case of failures)
 * 
 * The Map<String,Integer> returned from {@link #run(IDatabaseTranslator, Connection)}
 * uses a compound string of type:name for the key e.g. "Table:PATIENT_RESOURCES".
 * 
 * @author rarnold
 *
 */
public class GetLatestVersionDAO implements IDatabaseSupplier<Map<String,Integer>> {
    private final String schemaName;

    /**
     * Public constructor
     * @param schemaName
     */
    public GetLatestVersionDAO(String schemaName) {
        this.schemaName = schemaName;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseSupplier#run(com.ibm.watsonhealth.database.utils.api.IDatabaseTranslator, java.sql.Connection)
     */
    @Override
    public Map<String,Integer> run(IDatabaseTranslator translator, Connection c) {
        Map<String,Integer> result = new HashMap<>();
        final String tbl = DataDefinitionUtil.getQualifiedName(schemaName, SchemaConstants.VERSION_HISTORY);
        final String sql = "SELECT " + SchemaConstants.OBJECT_TYPE 
                + ", " + SchemaConstants.OBJECT_NAME 
                + ", max(" + SchemaConstants.VERSION + ") FROM " + tbl
                + " GROUP BY " + SchemaConstants.OBJECT_TYPE 
                + ", " + SchemaConstants.OBJECT_NAME;
        
        try (Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                String type = rs.getString(1);
                String name = rs.getString(2);
                String typeName = type + ":" + name; 
                int version = rs.getInt(3);
                result.put(typeName, version);
            }
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
        
        return result;
    }

}

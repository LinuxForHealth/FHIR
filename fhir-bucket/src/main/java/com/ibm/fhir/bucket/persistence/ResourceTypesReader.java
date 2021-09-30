/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * DAO to encapsulate all the SQL/DML used to retrieve and persist data
 * in the schema
 */
public class ResourceTypesReader implements IDatabaseSupplier<List<ResourceTypeRec>> {
    private static final Logger logger = Logger.getLogger(ResourceTypesReader.class.getName());

    private final String schemaName;

    /**
     * Public constructor
     * @param schemaName
     */
    public ResourceTypesReader(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public List<ResourceTypeRec> run(IDatabaseTranslator translator, Connection c) {
        List<ResourceTypeRec> result = new ArrayList<>();

        final String tableName = DataDefinitionUtil.getQualifiedName(schemaName, "resource_types");
        final String SQL = "SELECT resource_type_id, resource_type FROM " + tableName;

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int resourceTypeId = rs.getInt(1);
                String resourceType = rs.getString(2);
                result.add(new ResourceTypeRec(resourceTypeId, resourceType));
            }
        } catch (SQLException x) {
            // log this, but don't propagate values in the exception
            logger.log(Level.SEVERE, "Error reading resource types");
            throw translator.translate(x);
        }

        return result;
    }
}
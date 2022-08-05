/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;

/**
 * DAO to encapsulate all the SQL/DML used to retrieve and persist data
 * in the schema.
 * Supports only: PostgreSQL
 */
public class MergeResourceTypesPostgres implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(RegisterLoaderInstance.class.getName());

    private final String schemaName;
    // The list of resource types we want to add
    private final List<String> resourceTypes;

    /**
     * Public constructor
     * @param schemaName
     * @param resourceTypes the list of resource types to merge into the RESOURCE_TYPES
     * table
     */
    public MergeResourceTypesPostgres(String schemaName, Collection<String> resourceTypes) {
        this.schemaName = schemaName;
        // copy the list for safety
        this.resourceTypes = new ArrayList<String>(resourceTypes);
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {

        // UPSERT PostgreSQL style
        final String tableName = DataDefinitionUtil.getQualifiedName(schemaName, "resource_types");
        final String dml = ""
                + " INSERT INTO " + tableName + "(resource_type) "
                + "      VALUES (?) "
                + " ON CONFLICT (resource_type) DO NOTHING";

        try (PreparedStatement ps = c.prepareStatement(dml)) {
            for (String resourceType: resourceTypes) {
                ps.setString(1, resourceType);
                ps.executeUpdate();
            }
        } catch (SQLException x) {
            // log this, but don't propagate values in the exception
            logger.log(Level.SEVERE, "Error adding resource types: " + dml + ";");
            throw translator.translate(x);
        }
    }
}
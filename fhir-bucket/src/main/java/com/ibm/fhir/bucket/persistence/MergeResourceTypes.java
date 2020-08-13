/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * DAO to encapsulate all the SQL/DML used to retrieve and persist data
 * in the schema
 */
public class MergeResourceTypes implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(RegisterLoaderInstance.class.getName());

    // The list of resource types we want to add
    private final List<String> resourceTypes;
    
    /**
     * Public constructor
     * @param resourceType
     */
    public MergeResourceTypes(Collection<String> resourceTypes) {
        // copy the list for safety
        this.resourceTypes = new ArrayList<String>(resourceTypes);
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        
        // Support for PostgreSQL as well as Derby/Db2
        final String dual = translator.dualTableName();
        final String source = dual == null ? "(SELECT 1)" : dual;

        // Use a bulk merge approach to insert resource types not previously
        // loaded
        final String merge = "MERGE INTO resource_types tgt "
                + " USING " + source + " src "
                + "    ON tgt.resource_type = ? "
                + " WHEN NOT MATCHED THEN INSERT (resource_type) VALUES (?)";
        
        try (PreparedStatement ps = c.prepareStatement(merge)) {
            // Assume the list is small enough to process in one batch
            for (String resourceType: resourceTypes) {
                ps.setString(1, resourceType);
                ps.setString(2, resourceType);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException x) {
            // log this, but don't propagate values in the exception
            logger.log(Level.SEVERE, "Error adding resource types: " + merge + ";");
            throw translator.translate(x);
        }
    }
}
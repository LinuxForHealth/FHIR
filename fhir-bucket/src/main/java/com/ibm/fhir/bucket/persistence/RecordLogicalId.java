/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.bucket.api.FileType;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * DAO to encapsulate all the SQL/DML used to retrieve and persist data
 * in the schema
 */
public class RecordLogicalId implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(RegisterLoaderInstance.class.getName());

    // FK describing the type of the resource
    private final int resourceTypeId;

    // The newly assigned logical id of the resource
    private final String logicalId;

    // The bundle file from which the resource originated
    private final long resourceBundleId;

    // the line number of the resource (in an NDJSON file)
    private final int lineNumber;

    /**
     * Public constructor
     * @param bucketId
     * @param objectName
     */
    public RecordLogicalId(int resourceTypeId, String logicalId, long resourceBundleId, int lineNumber) {
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.resourceBundleId = resourceBundleId;
        this.lineNumber = lineNumber;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {

        final String INS = "INSERT INTO logical_resources (resource_type_id, logical_id, resource_bundle_id, line_number) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = c.prepareStatement(INS)) {
            ps.setLong(1, resourceTypeId);
            ps.setString(2, logicalId);
            ps.setLong(3, resourceBundleId);
            ps.setInt(4, lineNumber);
            ps.executeUpdate();
        } catch (SQLException x) {
            if (translator.isDuplicate(x)) {
                // This resource has already been recorded, so we'll just warn in case something
                // is going wrong
                logger.warning("Duplicate resource logical id: " + resourceTypeId + "/" + logicalId 
                    + " from " + resourceBundleId + "#" + lineNumber);
            } else {
                // log this, but don't propagate values in the exception
                logger.log(Level.SEVERE, "Error registering logical resource: " + INS + "; "
                    + resourceTypeId + ", " + logicalId + ", " + resourceBundleId + ", " + lineNumber);
                throw translator.translate(x);
            }
        }
    }
}
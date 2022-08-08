/*
 * (C) Copyright IBM Corp. 2020
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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.bucket.api.ResourceIdValue;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;

/**
 * DAO to encapsulate all the SQL/DML used to retrieve and persist data
 * in the schema
 */
public class RecordLogicalIdList implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(RegisterLoaderInstance.class.getName());

    // the id of the resource_bundle_loads associated with the list of ids we're inserting
    private final long resourceBundleLoadId;
    
    // The list of resource types we want to add
    private final List<ResourceIdValue> values;

    // Map to look up resource ids from resource type names
    private final Map<String,Integer> resourceTypeMap;
    
    // the line number of the file (probably 0, as it's likely to be a JSON bundle, not an NDJSON)
    private final int lineNumber;

    // How many values per batch execute
    private final int batchSize;
        
    /**
     * Public constructor
     * @param resourceType
     */
    public RecordLogicalIdList(long resourceBundleLoadId, int lineNumber, Collection<ResourceIdValue> values, Map<String,Integer> resourceTypeMap,
        int batchSize) {
        if (batchSize < 1) {
            throw new IllegalArgumentException("batchSize must be >= 1, not " + batchSize);
        }
        
        this.resourceBundleLoadId = resourceBundleLoadId;
        this.lineNumber = lineNumber;
        this.values = new ArrayList<>(values);
        this.resourceTypeMap = resourceTypeMap;
        this.batchSize = batchSize;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        
        // Use a batch to insert the freshly minted logical ids in one go.
        // Bundles can be large (O(1000) resources), so we periodically execute
        // the batch as we go
        final String currentTimestamp = translator.currentTimestampString();
        final String INS = 
                "INSERT INTO logical_resources ("
                + "          resource_type_id, logical_id, resource_bundle_load_id, line_number, response_time_ms, created_tstamp) "
                + "   VALUES (?, ?, ?, ?, NULL, " + currentTimestamp + ")";

        int batchCount = 0;
        try (PreparedStatement ps = c.prepareStatement(INS)) {
            for (ResourceIdValue rv: values) {
                Integer resourceTypeId = resourceTypeMap.get(rv.getResourceType());
                if (resourceTypeId == null) {
                    throw new IllegalArgumentException("Invalid resource type: " + rv.getResourceType());
                }

                // Note that for bundles we don't include a response time for each created logical
                // response because it doesn't make sense
                ps.setInt(1, resourceTypeId);
                ps.setString(2, rv.getLogicalId());
                ps.setLong(3, resourceBundleLoadId);
                ps.setInt(4, lineNumber);
                ps.addBatch();
                
                if (++batchCount == this.batchSize) {
                    ps.executeBatch();
                    batchCount = 0;
                }
            }
            
            if (batchCount > 0) {
                // final batch
                ps.executeBatch();
            }
        } catch (SQLException x) {
            // log this, but don't propagate values in the exception
            logger.log(Level.SEVERE, "Error adding resource types: " + INS + ";");
            throw translator.translate(x);
        }
    }
}
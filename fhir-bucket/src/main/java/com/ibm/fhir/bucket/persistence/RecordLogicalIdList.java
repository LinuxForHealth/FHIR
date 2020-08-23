/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.bucket.api.ResourceIdValue;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * DAO to encapsulate all the SQL/DML used to retrieve and persist data
 * in the schema
 */
public class RecordLogicalIdList implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(RegisterLoaderInstance.class.getName());

    // The list of resource types we want to add
    private final List<ResourceIdValue> values;
    
    private final Map<String,Integer> resourceTypeMap;
    
    private final long loaderInstanceId;
    
    private final long resourceBundleId;
    
    private final int lineNumber;
    
    private final int batchSize;
        
    /**
     * Public constructor
     * @param resourceType
     */
    public RecordLogicalIdList(long loaderInstanceId, long resourceBundleId, int lineNumber, Collection<ResourceIdValue> resourceTypes, Map<String,Integer> resourceTypeMap,
        int batchSize) {
        this.loaderInstanceId = loaderInstanceId;
        this.resourceBundleId = resourceBundleId;
        this.lineNumber = lineNumber;
        this.values = new ArrayList<>(resourceTypes);
        this.resourceTypeMap = resourceTypeMap;
        this.batchSize = batchSize;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        
        // Use a batch to insert the freshly minted logical ids in one go.
        // Bundles can be large (O(1000) resources), so we periodically execute
        // the batch as we go
        final String INS = 
                "INSERT INTO logical_resources ("
                + "          resource_type_id, logical_id, resource_bundle_id, line_number, loader_instance_id, response_time_ms, created_tstamp) "
                + "   VALUES (?, ?, ?, ?, ?, NULL, CURRENT TIMESTAMP)";

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
                ps.setLong(3, resourceBundleId);
                ps.setInt(4, lineNumber);
                ps.setLong(5, loaderInstanceId);
                ps.addBatch();
                
                if (++batchCount == this.batchSize) {
                    ps.executeBatch();
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
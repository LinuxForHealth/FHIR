/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.schema.size;

import java.util.HashMap;
import java.util.Map;

/**
 * Schema objects related to this resource type
 */
public class FHIRDbResourceSize {

    // The resource tables associated with this resource
    private final Map<String, FHIRDbTableSize> resourceTableSizes = new HashMap<>();
    
    // Table sizes for search parameter tables for this resource
    private final Map<String, FHIRDbTableSize> paramTableSizes = new HashMap<>();

    // The total table size accumulated for this resource type
    private long totalTableSize;

    // The total size accumulated for indexes associated with this resource type
    private long totalIndexSize;

    private long totalRowEstimate;
    
    // Estimate from xx_logical_resources
    private long logicalResourceRowEstimate;

    // Estimate from xx_resources
    private long resourceRowEstimate;

    /**
     * Accumulate the size of the named table in the model
     * @param tableName
     * @param isParamTable
     * @param size
     */
    public void accumulateTableSize(String tableName, boolean isParamTable, long size, long rowEstimate) {
        if (isParamTable) {
            FHIRDbTableSize tableSize = paramTableSizes.computeIfAbsent(tableName, k -> new FHIRDbTableSize());            
            tableSize.accumulateTableSize(size);
            tableSize.accumulateRowEstimate(rowEstimate);
        } else {
            FHIRDbTableSize tableSize = resourceTableSizes.computeIfAbsent(tableName, k -> new FHIRDbTableSize());
            tableSize.accumulateTableSize(size);
            tableSize.accumulateRowEstimate(rowEstimate);
        }
        this.totalTableSize += size;
        this.totalRowEstimate += rowEstimate;
        if (!isParamTable) {
            final String utn = tableName.toUpperCase();
            if (utn.endsWith("_LOGICAL_RESOURCES")) {
                this.logicalResourceRowEstimate = rowEstimate;
            } else if (utn.endsWith("_RESOURCES")) {
                this.resourceRowEstimate = rowEstimate;
            }
        }
    }

    /**
     * Accumulate the size of the named index in the model
     * @param tableName
     * @param isParamTable
     * @param indexName
     * @param size
     */
    public void accumulateIndexSize(String tableName, boolean isParamTable, String indexName, long size) {
        if (isParamTable) {
            FHIRDbTableSize tableSize = paramTableSizes.computeIfAbsent(tableName, k -> new FHIRDbTableSize());            
            tableSize.accumulateIndexSize(indexName, size);
        } else {
            FHIRDbTableSize tableSize = resourceTableSizes.computeIfAbsent(tableName, k -> new FHIRDbTableSize());
            tableSize.accumulateIndexSize(indexName, size);
        }
        this.totalIndexSize += size;
    }

    /**
     * Traverse this resource element of the FHIRDbSizeModel
     * @param visitor
     * @param resourceType
     * @param totalTableSize
     * @param totalIndexSize
     */
    public void accept(FHIRDbSizeModelVisitor visitor, String resourceType, long totalTableSize, long totalIndexSize) {
        visitor.resource(resourceType, logicalResourceRowEstimate, resourceRowEstimate, totalTableSize, totalIndexSize, this.totalRowEstimate, this.totalTableSize, this.totalIndexSize);

        // Visit the non-parameter tables
        for (Map.Entry<String, FHIRDbTableSize> entry: resourceTableSizes.entrySet()) {
            final FHIRDbTableSize ts = entry.getValue();
            visitor.table(resourceType, entry.getKey(), false, ts.getRowEstimate(), ts.getTableSize(), ts.getTotalIndexSize());

            // Visit the table object to see its index info
            ts.accept(visitor, resourceType, entry.getKey(), totalTableSize, totalIndexSize);
        }

        // Visit the parameter tables
        for (Map.Entry<String, FHIRDbTableSize> entry: paramTableSizes.entrySet()) {
            final FHIRDbTableSize ts = entry.getValue();
            visitor.table(resourceType, entry.getKey(), true, ts.getRowEstimate(), ts.getTableSize(), ts.getTotalIndexSize());

            // Visit the table object to see its index info
            ts.accept(visitor, resourceType, entry.getKey(), totalTableSize, totalIndexSize);
        }
    }

    /**
     * Get the logicalResourceRowEstimate
     * @return
     */
    public long getLogicalResourceRowEstimate() {
        return this.logicalResourceRowEstimate;
    }

    /**
     * Get the resourceRowEstimate
     * @return
     */
    public long getResourceRowEstimate() {
        return this.resourceRowEstimate;
    }
}
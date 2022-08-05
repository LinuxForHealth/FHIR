/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.schema.size;

import java.util.HashMap;
import java.util.Map;

/**
 * A model used to collect the reported size of various elements in the
 * FHIR data schema. The model allows reports to be generated showing
 * usage group by various dimensions and with different output formats
 */
public class FHIRDbSizeModel {

    private final String schemaName;

    // Map of sizes for each resource type
    private final Map<String, FHIRDbResourceSize> resourceSizeMap = new HashMap<>();

    // The total size accumulated by the model
    private long totalTableSize;

    // The total size of all indexes in the model
    private long totalIndexSize;

    /**
     * Public constructor
     * @param schemaName
     */
    public FHIRDbSizeModel(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * Getter for the schema name
     * @return
     */
    public String getSchemaName() {
        return this.schemaName;
    }
    /**
     * Accumulate the resource size for the given table
     * @param resourceType
     * @param tableName
     * @param isParamTable
     * @param size
     */
    public void accumulateTableSize(String resourceType, String tableName, boolean isParamTable, long size, long rowEstimate) {
        FHIRDbResourceSize resourceSize = resourceSizeMap.computeIfAbsent(resourceType, k -> new FHIRDbResourceSize());
        resourceSize.accumulateTableSize(tableName, isParamTable, size, rowEstimate);
        this.totalTableSize += size;
    }

    /**
     * Accumulate the resource size for the given table index
     * @param resourceType
     * @param tableName
     * @param isParamTable
     * @param indexName
     * @param size
     */
    public void accumulateIndexSize(String resourceType, String tableName, boolean isParamTable, String indexName, long size) {
        FHIRDbResourceSize resourceSize = resourceSizeMap.computeIfAbsent(resourceType, k -> new FHIRDbResourceSize());
        resourceSize.accumulateIndexSize(tableName, isParamTable, indexName, size);
        this.totalIndexSize += size;
    }

    /**
     * Get the total size of the database
     * @return
     */
    public long getTotalSize() {
        return this.totalTableSize + this.totalIndexSize;
    }

    /**
     * Get the total size of all tables, excluding indexes
     * @return
     */
    public long getTotalTableSize() {
        return this.totalTableSize;
    }
    
    /**
     * Get the total size of all indexes
     * @return
     */
    public long getTotalIndexSize() {
        return this.totalIndexSize;
    }

    /**
     * Traverse the elements of this model using the given visitor
     * @param visitor
     */
    public void accept(FHIRDbSizeModelVisitor visitor) {
        visitor.start();
        for (Map.Entry<String, FHIRDbResourceSize> entry: this.resourceSizeMap.entrySet()) {
            entry.getValue().accept(visitor, entry.getKey(), this.totalTableSize, this.totalIndexSize);
        }
    }
}
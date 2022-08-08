/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.schema.size;

import java.util.HashMap;
import java.util.Map;

/**
 * Size data for a FHIR schema table object and its indexes. This object
 * allows the sizes to be accumulated allowing it to be used to summarize
 * data grouped by other dimensions.
 */
public class FHIRDbTableSize {

    // Map containing the size of indexes associated with this table
    private final Map<String, Long> indexSizeMap = new HashMap<>();

    // The size of the table alone
    private long tableSize;

    // The total size of indexes so we don't have to go back and sum it up
    private long totalIndexSize;

    // The estimate of the number of rows held in this table
    private long rowEstimate;

    /**
     * Accumulate the table size
     * @param tableSize
     */
    public void accumulateTableSize(long tableSize) {
        this.tableSize += tableSize;
    }

    /**
     * Accumulate the row estimate value
     * @param rowEstimate
     */
    public void accumulateRowEstimate(long rowEstimate) {
        this.rowEstimate += rowEstimate;
    }

    /**
     * Add the size of the named index to the model
     * @param indexName
     * @param size
     */
    public void accumulateIndexSize(String indexName, long size) {
        // keep track of the total size for convenience
        this.totalIndexSize += size;
        indexSizeMap.put(indexName, size);
    }

    /**
     * Get the rowEstimate value
     * @return
     */
    public long getRowEstimate() {
        return this.rowEstimate;
    }

    /**
     * Get the table size in bytes
     * @return
     */
    public long getTableSize() {
        return this.tableSize;
    }

    /**
     * Get the total index size in bytes
     * @return
     */
    public long getTotalIndexSize() {
        return this.totalIndexSize;
    }

    /**
     * Visits each of the indexes associated with this table
     * @param visitor
     * @param resourceType
     * @param tableName
     * @param totalTableSize
     * @param totalIndexSize
     */
    public void accept(FHIRDbSizeModelVisitor visitor, String resourceType, String tableName, long totalTableSize, long totalIndexSize) {

        for (Map.Entry<String, Long> entry: this.indexSizeMap.entrySet()) {
            visitor.index(resourceType, tableName, entry.getKey(), entry.getValue());
        }
    }
}
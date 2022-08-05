/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.api;

import java.util.Date;
import java.util.List;

/**
 * Catalog Access to the backend adminstrative functions
 */
public interface ICatalogAccess {

    /**
     * Get the list of partitions for this table
     * @param schema
     * @param table
     * @return
     */
    List<PartitionInfo> getPartitionList(String schema, String table);

    /**
     * Add a new monthly partition to the given table
     * @param schema
     * @param table
     * @param lowValue
     */
    void addMonthPartition(String schema, String table, Date lowValue);
    
    /**
     * Add a new daily partition to the given table
     * @param schema
     * @param table
     * @param lowValue
     */
    void addDayPartition(String schema, String table, Date lowValue);
    
    /**
     * Drop (detach) the partition identified by schema, table and pi
     * @param schema
     * @param table
     * @param pi
     */
    void dropPartition(String schema, String table, PartitionInfo pi);

    /**
     * Drop any tables which have been created as part of the dropPartition
     * process
     * @param schema
     * @param table
     * @param partMaintBatchSize
     */
    void dropDetachedPartitions(String schema, String table, int partMaintBatchSize);
    
    /**
     * Used for commit after batch operations
     */
    void commitBatch();
}

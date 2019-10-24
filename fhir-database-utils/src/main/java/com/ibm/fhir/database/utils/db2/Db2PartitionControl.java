/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.util.Date;
import java.util.List;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.ICatalogAccess;
import com.ibm.fhir.database.utils.api.PartitionInfo;
import com.ibm.fhir.database.utils.api.PartitionUnit;
import com.ibm.fhir.database.utils.common.DateMath;

/**
 * Business logic to add new partitions to the given table
 */
public class Db2PartitionControl {

    // Access to the database catalog for partitioning
    private ICatalogAccess catalog;
    
    // So we can have a constant notion of today
    private Date today;
    
    /**
     * Public constructor
     * @param ica
     * @param today
     */
    public Db2PartitionControl(ICatalogAccess ica, Date today) {
        this.catalog = ica;
        this.today = today;
    }
        
    /**
     * Make sure the named table has at least N future partitions
     *
     * @param schema
     * @param table
     * @param future
     * @param pu
     * @param unitsToKeep
     * @param partMaintBatchSize
     */
    public void pushAhead(final String schema, final String table, int future, PartitionUnit pu, int unitsToKeep, int partMaintBatchSize) {

        // First we need to find the current state of play
        List<PartitionInfo> partitions = catalog.getPartitionList(schema, table);
        if (partitions.isEmpty()) {
            throw new DataAccessException("Table is not partitioned: " + schema + "." + table);
        }
        
        // The last end date is exclusive, so it becomes the anchor for new partitions
        // as we add them
        Date nextStartDate = getHighValueDate(partitions.get(partitions.size()-1));
        
        // We want to make sure we have at least N partitions in the future with respect
        // to today's date
        Date lastEndDate;
        Date currentStart;
        int count = 0;
        
        switch (pu) {
        case DAYS:    
            currentStart = DateMath.truncateToDay(today);
            //don't bother to create partitions that will not be kept 
            if (unitsToKeep > 1) {            
                nextStartDate =   DateMath.max(nextStartDate,  DateMath.addDays(currentStart, -1 * unitsToKeep));         
            }

            lastEndDate = DateMath.addDays(currentStart, future);
            while (nextStartDate.before(lastEndDate)) {
                catalog.addDayPartition(schema, table, nextStartDate);
                // This is for in case too many partitions being added.
                if (++count == partMaintBatchSize) {
                    catalog.commitBatch();
                    count = 0;
                }
                // Advance the start date of the next partition by one period
                nextStartDate = DateMath.addDays(nextStartDate, 1);
            }
            break;
        case MONTHS:            
            // Partition on monthly boundaries to keep things easier
            currentStart = DateMath.truncateToMonth(today);
            //don't bother to create partitions that will not be kept
            if (unitsToKeep > 1) {            
                nextStartDate = DateMath.max(nextStartDate, DateMath.addMonths(currentStart, -1 * unitsToKeep));
            }
            
            lastEndDate = DateMath.addMonths(currentStart, future);
            while (nextStartDate.before(lastEndDate)) {
                catalog.addMonthPartition(schema, table, nextStartDate);        
                // This is for in case too many partitions being added.
                if (++count == partMaintBatchSize) {
                    catalog.commitBatch();
                    count = 0;
                }
                // Advance the start date of the next partition by one period
                nextStartDate = DateMath.addMonths(nextStartDate, 1);
            }
            break;
        }
    }


    /**
     * Get the highValue as a Date from the PartitionInfo
     * @param pi
     * @return
     */
    protected Date getHighValueDate(PartitionInfo pi) {
        return DateMath.parse(pi.getHighValue());
    }

    /**
     * Get the lowValue as a Date from the PartitionInfo
     * @param pi
     * @return
     */
    protected Date getLowValueDate(PartitionInfo pi) {
        return DateMath.parse(pi.getLowValue());
    }

    /**
     * Drop old partitions we no longer need. In DB2 this process is a little more work
     * than it ought to be. You can't simply drop the partition; instead you have to
     * detach it into a standalone table, and then an asynchronous process makes that
     * standalone table available at some later point in time. So we can't drop the
     * table right away, we have to wait for it to become available 
     * 
     * @param schema
     * @param table
     * @param unitsToKeep
     * @param pu
     * @param partMaintBatchSize
     */
    public void rollOff(final String schema, final String table, int unitsToKeep, PartitionUnit pu, int partMaintBatchSize) {
        int count = 0;
        // Always need at least one unit to keep
        if (unitsToKeep < 1) {
            return;
        }
        
        List<PartitionInfo> partitions = catalog.getPartitionList(schema, table);
        if (partitions.isEmpty()) {
            throw new DataAccessException("Table is not partitioned: " + schema + "." + table);
        }

        catalog.dropDetachedPartitions(schema, table, partMaintBatchSize);
        
        // We want to make sure we have at least N partitions in the future with respect
        // to today's date
        Date firstStartDate;
        Date currentStart;
        switch (pu) {
        case DAYS:
            currentStart = DateMath.truncateToDay(today);
            firstStartDate = DateMath.addDays(currentStart, -1 * unitsToKeep);

            // Drop all the leading partitions whose end date falls before the
            // firstStartDate threshold
            for (PartitionInfo pi: partitions) {
                Date lowValue = getLowValueDate(pi);
                if (lowValue.before(firstStartDate)) {
                    catalog.dropPartition(schema, table, pi);
                    if (++count == partMaintBatchSize) {
                        break;
                    }
                        
                }
            }
            break;
        case MONTHS:
            // Partition on monthly boundaries to keep things easier
            currentStart = DateMath.truncateToMonth(today);
            firstStartDate = DateMath.addMonths(currentStart, -1 * unitsToKeep);

            // Drop all the leading partitions whose end date falls before the
            // firstStartDate threshold
            for (PartitionInfo pi: partitions) {
                Date lowValue = getLowValueDate(pi);
                if (lowValue.before(firstStartDate)) {
                    catalog.dropPartition(schema, table, pi);
                    if (++count == partMaintBatchSize) {
                        break;
                    }
                }
            }
            break;
        }
    }
}

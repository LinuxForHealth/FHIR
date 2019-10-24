/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

/**
 * Partitioning configuration information for a table
 */
public class PartitionDef {
    private final String partitionColumn;
    private final int lower;
    private final int upper;

    /**
     * Public constructor
     * @param partitionColumn
     * @param lower
     * @param upper
     */
    public PartitionDef(String partitionColumn, int lower, int upper) {
        this.partitionColumn = partitionColumn;
        this.lower = lower;
        this.upper = upper;
    }

    public String getPartitionColumn() {
        return partitionColumn;
    }

    public int getLower() {
        return lower;
    }

    public int getUpper() {
        return upper;
    }
}

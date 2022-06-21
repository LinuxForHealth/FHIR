/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.database.utils.api;

/**
 * The type of distribution to use for a table
 */
public enum DistributionType {
    NONE,        // table will not be distributed at all
    REFERENCE,   // table will be replicated
    DISTRIBUTED  // table will be sharded by a known column
}

/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.cassandra.test;

import static org.testng.Assert.assertEquals;

import java.util.UUID;

import org.testng.annotations.Test;

import com.ibm.fhir.persistence.cassandra.payload.FHIRPayloadHashPartitionStrategy;
import com.ibm.fhir.persistence.cassandra.payload.FHIRPayloadPersistenceCassandraImpl;

/**
 * Unit test for the Cassandra partitioning strategy
 */
public class PartitionStrategyTest {

    @Test
    public void testHash() {
        // If someone changes this value, make sure they understand the consequence
        assertEquals(FHIRPayloadPersistenceCassandraImpl.PARTITION_HASH_BASE64_DIGITS, 4, 
            "Changing the PARTITION_HASH_BASE64_DIGITS length will break existing databases.");

        // The hash must be deterministic, so we can check the value we know
        // we should be getting
        FHIRPayloadHashPartitionStrategy strat = new FHIRPayloadHashPartitionStrategy(FHIRPayloadPersistenceCassandraImpl.PARTITION_HASH_BASE64_DIGITS);
        String partitionValue = strat.getPartitionName("Patient", "123");
        assertEquals(partitionValue.length(), 4);
        assertEquals(partitionValue, "X3W8");

        partitionValue = strat.getPartitionName("Observation", "23456");
        assertEquals(partitionValue.length(), 4);
        assertEquals(partitionValue, "z2JS");

        // Make sure the reset is working by checking the first hash again
        partitionValue = strat.getPartitionName("Patient", "123");
        assertEquals(partitionValue.length(), 4);
        assertEquals(partitionValue, "X3W8");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBadInitLower() {
        new FHIRPayloadHashPartitionStrategy(0);        
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBadInitUpper() {
        // 43 is the max value
        new FHIRPayloadHashPartitionStrategy(44);        
    }
    
    @Test
    public void testDeterminism() {
        FHIRPayloadHashPartitionStrategy strat = new FHIRPayloadHashPartitionStrategy(FHIRPayloadPersistenceCassandraImpl.PARTITION_HASH_BASE64_DIGITS);
        for (int i=0; i<1000; i++) {
            final String value1 = UUID.randomUUID().toString();
            final String value2 = UUID.randomUUID().toString();
            final String h1 = strat.getPartitionName(value1, value2);
            final String h2 = strat.getPartitionName(value1, value2);
            assertEquals(h1, h2);
        }
    }
}
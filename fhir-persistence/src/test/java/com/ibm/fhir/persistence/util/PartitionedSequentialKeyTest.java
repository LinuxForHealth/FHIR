
/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * Unit test for PartitionedSequentialKey
 */
public class PartitionedSequentialKeyTest {
    
    private void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException x) {
            // NOP
        }
    }

    @Test
    public void testKeyGeneration() {

        // As much as I hate putting sleeps into code, in this instance it is
        // required in order to generate different keys
        PartitionedSequentialKey k1 = PartitionedSequentialKey.generateKey("Patient", "A", 1);
        safeSleep(5);
        PartitionedSequentialKey k2 = PartitionedSequentialKey.generateKey("Patient", "B", 1);
        
        assertTrue(k2.getTstamp() > k1.getTstamp());
        
        // Note...timestamps being different does not imply that the partition MUST be different,
        // so we can't test for that here.
    }
    
    @Test
    public void testPartitioning() {

        // Look for two keys with the same timestamp and make sure they share
        // the same partition
        PartitionedSequentialKey k1 = PartitionedSequentialKey.generateKey("Patient", "A", 1);
        for (int i=0; i<100; i++) {
            PartitionedSequentialKey k2 = PartitionedSequentialKey.generateKey("Patient", "A", 1);
            if (k1.getTstamp() == k2.getTstamp()) {
                assertEquals(k1.getPartition(), k2.getPartition());
                
                // If we get here we've got the match we wanted, so we're all done.
                return;
            }
            k1 = k2;
        }
        
        // if we get here it means we either didn't get two matching timestamps (odd).
        assertTrue(false);
    }
}
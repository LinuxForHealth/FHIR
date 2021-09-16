/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.persistence.jdbc.util.TimestampPrefixedUUID;

/**
 * Unit test for {@link TimestampPrefixedUUID}
 */
public class TimestampPrefixedUUIDTest {

    @Test
    public void testCollation() {
        TimestampPrefixedUUID provider = new TimestampPrefixedUUID();

        // Sample this 10 times, to be sure. That's 100ms, which isn't too disruptive
        for (int i=0; i<10; i++) {
            // We need a time gap between the two strings
            String s1 = provider.createNewIdentityValue();

            // Ensure the generated id is always valid
            ValidationSupport.checkId(s1);

            try {
                // It's only 10ms, which is sufficient. We don't want to make it any longer
                // for the sake of slowing down the build.
                Thread.sleep(10);
            } catch (InterruptedException x) {
                // NOP. Not gonna happen
            }

            String s2 = provider.createNewIdentityValue();

            // Ensure the generated id is always valid
            ValidationSupport.checkId(s2);

            assertTrue(s1.compareTo(s2) < 0); // s1 < s2
        }
    }
}

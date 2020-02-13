/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util.test;

import static org.testng.Assert.assertFalse;

import org.testng.annotations.Test;

import com.ibm.fhir.model.util.FHIRUtil;


public class RandomKeyGeneratorTest {

    @Test
    public void testGetRandomKey() {
        String output = FHIRUtil.getRandomKey("AES");
        assertFalse(output.isEmpty());
        output = FHIRUtil.getRandomKey("FANCY_KEYS");
        assertFalse(output.isEmpty());
    }

}

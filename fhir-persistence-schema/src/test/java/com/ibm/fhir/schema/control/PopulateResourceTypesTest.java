/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import org.testng.annotations.Test;

/**
 * Test to verify the resource_types.properties
 */
public class PopulateResourceTypesTest {
    @Test
    public void testVerify() {
        PopulateResourceTypes.verify();
        assert true;
    }
}
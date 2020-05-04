/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import org.testng.annotations.Test;

/**
 * Test to verify the parameter_names.properties
 */
public class PopulateResourceTypesTest {
    @Test
    public void testVerify() {
        PopulateParameterNames.verify();
        assert true;
    }
}
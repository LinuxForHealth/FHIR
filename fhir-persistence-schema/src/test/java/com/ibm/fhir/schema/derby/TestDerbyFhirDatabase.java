/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.derby;

import org.testng.annotations.Test;

import com.ibm.fhir.schema.derby.DerbyFhirDatabase;

/**
 * Unit test for the DerbyFhirDatabase utility

 *
 */
public class TestDerbyFhirDatabase {

    @Test
    public void testFhirSchema() throws Exception {
        try (DerbyFhirDatabase db = new DerbyFhirDatabase()) {
            System.out.println("FHIR database create successfully.");
        }
    }

}

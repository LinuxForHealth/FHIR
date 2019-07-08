/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.schema.derby;

import org.testng.annotations.Test;

/**
 * Unit test for the DerbyFhirDatabase utility
 * @author rarnold
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

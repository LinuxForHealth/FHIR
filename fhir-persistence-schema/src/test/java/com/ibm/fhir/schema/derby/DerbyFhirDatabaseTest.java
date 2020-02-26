/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.derby;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import com.ibm.fhir.schema.derby.DerbyFhirDatabase;

/**
 * Unit test for the DerbyFhirDatabase utility
 */
public class DerbyFhirDatabaseTest {
    private static final String DB_NAME = "target/derby/fhirDB";
    @Test
    public void testFhirSchema() throws Exception {
        try (DerbyFhirDatabase db = new DerbyFhirDatabase(DB_NAME)) {
            System.out.println("FHIR database create successfully.");

            // No Op on these
            db.commitTransaction();
            db.rollbackTransaction();
            db.describe("DUMMY", null, "DUMMY");

            // Return useful things. 
            assertNotNull(db.getConnection());
            assertNotNull(db.getTranslator());
        } catch (Exception e) {
            fail("Failed to create the database", e);
        }
    }
}

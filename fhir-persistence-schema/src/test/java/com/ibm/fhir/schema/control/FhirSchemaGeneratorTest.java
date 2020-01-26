/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.model.SessionVariableDef;

/**
 * Increases the test coverage for FHIR Schema Generator
 */
public class FhirSchemaGeneratorTest {
    private static boolean result = false;
    
    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCheckValidSessionVariableNull() {
        String adminSchemaName = "adminSchemaName";
        String schemaName = "schemaName";
        FhirSchemaGenerator schemaGen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        schemaGen.checkValidSessionVariable(null);
    }

    @Test
    public void testCheckValidSessionVariable() {
        String adminSchemaName = "adminSchemaName";
        String schemaName = "schemaName";
        String variableName = "variableName";
        int version = 1;
        SessionVariableDef sessionVar = new SessionVariableDef(schemaName, variableName, version);
        FhirSchemaGenerator schemaGen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        schemaGen.checkValidSessionVariable(sessionVar);
        assert true;
    }

    @Test
    public void testReadResourceTemplate() {
        String adminSchemaName = "adminSchemaName";
        String schemaName = "schemaName";

        String resourceType = "PatientFun";
        FhirSchemaGenerator schemaGen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        String output = schemaGen.readResourceTemplate(resourceType);
        assertNotNull(output);
        assertFalse(output.isEmpty());
    }

    @Test
    public void testAapplyResourceTypes() {
        String adminSchemaName = "adminSchemaName";
        String schemaName = "schemaName";

        FhirSchemaGenerator schemaGen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        schemaGen.applyResourceTypes(c -> {
            result = true;
        });
        assertTrue(result);
    }
}
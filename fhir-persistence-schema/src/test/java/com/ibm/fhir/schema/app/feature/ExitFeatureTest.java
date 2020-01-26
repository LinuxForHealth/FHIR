/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.feature;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Tests the Exit Feature under the known/unknown conditions.
 */
public class ExitFeatureTest {
    @Test
    public void testExitFeature() {
        ExitFeature exitFeature = new ExitFeature();
        exitFeature.logStatusMessage();
        assertEquals(exitFeature.getExitStatus(), 0);

        exitFeature.setExitStatus(ExitFeature.EXIT_BAD_ARGS);
        exitFeature.logStatusMessage();
        assertEquals(exitFeature.getExitStatus(), 1);

        exitFeature.setExitStatus(ExitFeature.EXIT_RUNTIME_ERROR);
        exitFeature.logStatusMessage();
        assertEquals(exitFeature.getExitStatus(), 2);

        exitFeature.setExitStatus(ExitFeature.EXIT_VALIDATION_FAILED);
        exitFeature.logStatusMessage();
        assertEquals(exitFeature.getExitStatus(), 3);

        int DUMMY_EXIT_CODE = -1000;
        exitFeature.setExitStatus(DUMMY_EXIT_CODE);
        exitFeature.logStatusMessage();
        assertEquals(exitFeature.getExitStatus(), -1000);
    }
}
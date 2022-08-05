/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.config.preflight.impl;

import java.util.Arrays;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.operation.bulkdata.OperationConstants.ExportType;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.Input;

/**
 * Tests for FILE Preflight
 */
public class FilePreflightTest {
    @Test(expectedExceptions = { FHIROperationException.class })
    public void testPreflightWithBadDirectory() throws FHIROperationException {
        FilePreflight preflight = new FilePreflight("source", "outcome",
                Arrays.asList(new Input("Patient", "1-2-3-4-5")), ExportType.SYSTEM, "application/ndjson+fhir");
        preflight.checkFile("/completely-random");
    }
}

/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.config.preflight.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.util.CommonUtil;

/**
 * Preflight is a health check prior to executing the calls on BulkData.
 */
public class FilePreflight extends NopPreflight {

    private CommonUtil util = new CommonUtil();

    public FilePreflight(String source, String outcome, List<Input> inputs) {
        super(source, outcome, inputs);
    }

    @Override
    public void preflight() throws FHIROperationException {
        super.preflight();
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        String base = adapter.getBaseFileLocation(getSource());
        checkFile(base);
        if (adapter.shouldSourceCollectOperationOutcomes(getSource())) {
            checkFile(adapter.getBaseFileLocation(getOutcome()));
        }
    }

    private void checkFile(String base) throws FHIROperationException {
        if (base != null) {
            Path p = Paths.get(base);
            boolean accessible = Files.isReadable(p);
            if (getInputs() == null) {
                // This is an export
                accessible = Files.isWritable(p);
            } else {
                // This is an import
                for (Input input : getInputs()) {
                    File f = new File(input.getUrl());
                    // We want to append the input path on the base, we don't want to allow everything.
                    Path p1 = Paths.get(base, f.getAbsolutePath());
                    accessible = Files.isReadable(p1);
                    if (!accessible) {
                        // Skip out of the for loop
                        return;
                    }
                }
            }
            if (!accessible) {
                throw util.buildExceptionWithIssue("The location for the bulkdata operation is not found", IssueType.NOT_FOUND);
            }
        } else {
            throw util.buildExceptionWithIssue("No File Base Configured for FHIR bulkdata operation", IssueType.INVALID);
        }
    }
}
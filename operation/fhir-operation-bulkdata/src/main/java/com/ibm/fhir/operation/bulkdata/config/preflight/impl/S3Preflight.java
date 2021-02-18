/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.config.preflight.impl;

import java.util.List;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.operation.bulkdata.model.type.Input;

/**
 *
 */
public class S3Preflight extends NopPreflight {

    public S3Preflight(String source, String outcome, List<Input> inputs) {
        super(source,outcome, inputs);
    }

    @Override
    public void preflight() throws FHIROperationException {
        super.preflight();
    }

    @Override
    public void healthcheck() throws FHIROperationException {
        super.healthcheck();

    }
}
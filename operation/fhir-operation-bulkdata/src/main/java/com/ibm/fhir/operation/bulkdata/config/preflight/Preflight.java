/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.config.preflight;

import com.ibm.fhir.exception.FHIROperationException;

/**
 * Preflight is designed to sanity check a request
 * prior to executing the bulkdata request.
 */
public interface Preflight {
    /**
     * The preflight execution checks access to the Source,Outcome.
     *
     * @throws FHIROperationException
     */
    public void preflight() throws FHIROperationException;
}
/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.erase.impl;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Parameters;

/**
 * Erase specifies a common set of methods to erase a resource.
 */
public interface Erase {
    /**
     * checks if the user calling the erase operation is authorized.
     * @throws FHIROperationException
     */
    void authorize() throws FHIROperationException;

    /**
     * checks if the tenant has enabled the Erase operaiton.
     * @throws FHIROperationException the Erase operation is not enabled
     */
    void enabled() throws FHIROperationException;

    /**
     * verifies the HttpMethod is supported
     *
     * @throws FHIROperationException
     */
    void verifyMethod() throws FHIROperationException;

    /**
     * verifies the incoming parameters are correct
     * @throws FHIROperationException
     */
    void verifyParameters() throws FHIROperationException;

    /**
     * Checks to see if the resource exists
     * @throws FHIROperationException
     */
    void exists() throws FHIROperationException;

    /**
     * Erases a Resource and integrates with the audit framework.
     *
     * @implNote each implementation is expected implement audit.
     *
     * @return
     * @throws FHIROperationException
     */
    Parameters erase() throws FHIROperationException;
}
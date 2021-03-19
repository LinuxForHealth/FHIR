/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.erase.impl;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Parameters;

/**
 *
 * @implNote each method is expected to be responsible for auditing.
 */
public interface Erase {

    public void checkAllowedMethod(Object method) throws FHIROperationException;

    public void authorize(Object securityContext, Object jwt) throws FHIROperationException;

    public void checkPatientId() throws FHIROperationException;

    public void exists() throws FHIROperationException;

    public Parameters erase() throws FHIROperationException;
}
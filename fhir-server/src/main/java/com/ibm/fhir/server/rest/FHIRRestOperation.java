/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;

/**
 * Defines an operation the REST layer wants to perform against the
 * persistence layer
 */
public interface FHIRRestOperation {
    FHIRRestOperationResponse accept(FHIRRestOperationVisitor visitor) throws Exception;
}

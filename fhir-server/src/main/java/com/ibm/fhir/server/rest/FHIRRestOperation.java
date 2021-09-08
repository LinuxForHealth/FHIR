/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;


/**
 * Defines an operation the REST layer wants to perform against the
 * persistence layer
 */
public interface FHIRRestOperation {
    <T> T accept(FHIRRestOperationVisitor<T> visitor) throws Exception;
}

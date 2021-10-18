/*
 * (C) Copyright IBM Corp. 2016,2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.payload.FHIRPayloadPersistence;

/**
 * This interface defines a factory which serves up instances of FHIRPersistence implementations.
 */
public interface FHIRPersistenceFactory {

    /**
     * Returns an instance of a concrete implementation of the FHIRPersistence interface.
     * @throws FHIRPersistenceException
     */
    FHIRPersistence getInstance() throws FHIRPersistenceException;

    /**
     * Returns an instance of a concrete implementation of the FHIRPayloadPersistence interface
     * which may be used by FHIRPersistence implementations to handle storage and retrieval
     * of FHIR resource payloads.
     * @return the concrete implementation
     * @throws FHIRPersistenceException
     */
    default FHIRPayloadPersistence getPayloadPersistence() throws FHIRPersistenceException {
        return null;
    }
}
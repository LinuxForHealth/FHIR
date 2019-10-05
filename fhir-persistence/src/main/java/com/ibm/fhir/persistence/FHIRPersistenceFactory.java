/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This interface defines a factory which serves up instances of FHIRPersistence implementations.
 */
public interface FHIRPersistenceFactory {
    
    /**
     * Returns an instance of a concrete implementation of the FHIRPersistence interface.
     * @throws FHIRPersistenceException
     */
    FHIRPersistence getInstance() throws FHIRPersistenceException;
}

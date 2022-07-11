/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.jdbc.connection;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Supports callbacks from {@link FHIRUserTransactionAdapter} implementations
 */
public interface IFHIRTransactionAdapterCallback {

    /**
     * Callback invoked just before the current transaction commits
     * @throws FHIRPersistenceException
     */
    void beforeCommit() throws FHIRPersistenceException;
}

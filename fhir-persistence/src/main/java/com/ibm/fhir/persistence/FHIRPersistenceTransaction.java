/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This interface represents a transaction within the FHIR persistence layer.
 */
public interface FHIRPersistenceTransaction {
    
    /**
     * Returns true iff an active transaction exists within the current thread's context.
     */
    boolean isActive() throws FHIRPersistenceException;
    
    /**
     * Begin a new transaction on the current thread.
     * @throws Exception
     */
    void begin() throws FHIRPersistenceException;
    
    /**
     * Commit the current thread's transaction.
     * @throws Exception
     */
    void commit() throws FHIRPersistenceException;
    
    /**
     * Roll back the current thread's transaction.
     * @throws Exception
     */
    void rollback() throws FHIRPersistenceException;
    
    /**
     * Modify the transaction associated with the current thread such that the only possible outcome of the transaction 
     * is to roll back the transaction. 
     * @throws FHIRPersistenceException
     */
    void setRollbackOnly() throws FHIRPersistenceException;
}

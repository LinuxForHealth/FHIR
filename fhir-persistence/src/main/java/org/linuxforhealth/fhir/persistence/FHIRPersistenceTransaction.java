/*
 * (C) Copyright IBM Corp. 2016,2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This interface represents a transaction within the FHIR persistence layer.
 * @implNote General pattern for usage:
 *           FHIRPersistenceTransaction tx = persistence.getTransaction();
 *           tx.begin();
 *           try {
 *               doStuff(persistence);
 *           } catch (Throwable t) {
 *               tx.setRollbackOnly();
 *               throw t;
 *           } finally {
 *               // will do a commit, or rollback if tx.setRollbackOnly() has been set
 *               tx.end();
 *           }
 */
public interface FHIRPersistenceTransaction {

    /**
     * Does the underlying implementation actually support transactions? A persistence
     * layer must always return a FHIRPersistenceTransaction even if it doesn't support
     * transactions. This reduces boilerplate code by avoiding the need to check for null
     * every time.
     * @return
     */
    default boolean isTransactional() { return true; }

    /**
     * Begin a new transaction on the current thread if a transaction is not started yet.
     * @throws Exception
     */
    void begin() throws FHIRPersistenceException;

    /**
     * End the current thread's transaction. If setRollbackOnly has been called,
     * then roll back the transaction instead.
     * This call only affects the current transaction if this object actually
     * started the transaction (i.e. is the outermost instance of a transaction).
     * @throws Exception
     */
    void end() throws FHIRPersistenceException;

    /**
     * Modify the transaction associated with the current thread such that the only possible outcome of the transaction
     * is to roll back the transaction.
     * @throws FHIRPersistenceException
     */
    void setRollbackOnly() throws FHIRPersistenceException;
    
    /**
     * Determine if the transaction status is currently active
     * @return true if transaction begin has been called
     * @throws FHIRPersistenceException
     */
    boolean hasBegun() throws FHIRPersistenceException;
}
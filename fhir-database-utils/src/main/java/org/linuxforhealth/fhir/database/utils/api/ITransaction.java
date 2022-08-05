/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.api;

/**
 * Simple service interface which allows us to hide how transactions are managed
 * for the current thread. In this framework, we're only dealing with a single
 * database, and one connection per thread, so the implementation does not need
 * to be complicated
 * 
 * Implemented as {@link AutoCloseable} so the transaction will end when close
 * is closed, making it nice and neat to use with try-with-resource.
 */
public interface ITransaction extends AutoCloseable {

    /**
     * Mark the transaction as failed, so that we we end it, we'll rollback
     */
    public void setRollbackOnly();

    /**
     * Override the {@link AutoCloseable#close()} method so that
     * we can refine the exception
     */
    @Override
    public void close() throws DataAccessException;
}

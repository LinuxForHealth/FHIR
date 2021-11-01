/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.connection;

import jakarta.transaction.Synchronization;
import jakarta.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.persistence.jdbc.connection.FHIRUserTransactionAdapter;


/**
 * Mock implementation of {@link TransactionSynchronizationRegistry} for testing the {@link FHIRUserTransactionAdapter}
 */
public class MockTransactionSynchronizationRegistry implements TransactionSynchronizationRegistry {

    @Override
    public Object getTransactionKey() {
        return null;
    }

    @Override
    public void putResource(Object key, Object value) {
    }

    @Override
    public Object getResource(Object key) {
        return null;
    }

    @Override
    public void registerInterposedSynchronization(Synchronization sync) {
    }

    @Override
    public int getTransactionStatus() {
        return 0;
    }

    @Override
    public void setRollbackOnly() {
    }

    @Override
    public boolean getRollbackOnly() {
        return false;
    }
}
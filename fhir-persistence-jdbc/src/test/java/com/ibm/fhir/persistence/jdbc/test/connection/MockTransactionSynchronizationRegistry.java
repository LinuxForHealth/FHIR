/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.connection;

import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.persistence.jdbc.connection.FHIRUserTransactionAdapter;


/**
 * Mock implementation of {@link TransactionSynchronizationRegistry} for testing the {@link FHIRUserTransactionAdapter}
 */
public class MockTransactionSynchronizationRegistry implements TransactionSynchronizationRegistry {

    @Override
    public Object getTransactionKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void putResource(Object key, Object value) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object getResource(Object key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void registerInterposedSynchronization(Synchronization sync) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getTransactionStatus() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setRollbackOnly() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean getRollbackOnly() {
        // TODO Auto-generated method stub
        return false;
    }

}

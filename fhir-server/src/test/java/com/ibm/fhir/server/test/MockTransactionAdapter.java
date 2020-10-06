/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import com.ibm.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

public class MockTransactionAdapter implements FHIRPersistenceTransaction {
    public MockTransactionAdapter() {
    }

    @Override
    public void begin() throws FHIRPersistenceException {
    }

    @Override
    public void end() throws FHIRPersistenceException {
    }

    @Override
    public void setRollbackOnly() throws FHIRPersistenceException {
    }

    @Override
    public void commit() throws FHIRPersistenceException {
    }

    @Override
    public void rollback() throws FHIRPersistenceException {
    }
}

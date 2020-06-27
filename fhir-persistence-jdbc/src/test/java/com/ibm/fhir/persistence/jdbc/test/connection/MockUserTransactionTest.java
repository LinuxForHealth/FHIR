/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.connection;

import static org.testng.Assert.assertEquals;

import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.testng.annotations.Test;

/**
 * Test our mock for {@link UserTransaction} to make sure it does what we expect
 * so that we can rely on it for the {@link FHIRUserTransactionAdapterTest}
 */
public class MockUserTransactionTest {

    @Test(groups = {"jdbc"})
    public void test() throws Exception {
        MockUserTransaction tx = new MockUserTransaction();
        assertEquals(tx.getStatus(), Status.STATUS_NO_TRANSACTION);
        tx.begin();
        assertEquals(tx.getStatus(), Status.STATUS_ACTIVE);
        tx.commit();
        assertEquals(tx.getStatus(), Status.STATUS_NO_TRANSACTION);

        // try another transaction, but roll it back
        tx.begin();
        assertEquals(tx.getStatus(), Status.STATUS_ACTIVE);
        tx.setRollbackOnly();
        assertEquals(tx.getStatus(), Status.STATUS_MARKED_ROLLBACK);
        tx.rollback();
        assertEquals(tx.getStatus(), Status.STATUS_NO_TRANSACTION);
        
        // straight rollback
        tx.begin();
        assertEquals(tx.getStatus(), Status.STATUS_ACTIVE);
        tx.rollback();
        assertEquals(tx.getStatus(), Status.STATUS_NO_TRANSACTION);
    }

    
    @Test(groups = {"jdbc"}, expectedExceptions = RollbackException.class)
    public void rollbackOnlyCommit() throws Exception {
        MockUserTransaction tx = new MockUserTransaction();
        tx.begin();
        tx.setRollbackOnly();
        tx.commit();
    }

    @Test(groups = {"jdbc"}, expectedExceptions = IllegalStateException.class)
    public void commitNoTransaction() throws Exception {
        MockUserTransaction tx = new MockUserTransaction();
        tx.commit();
    }

    @Test(groups = {"jdbc"}, expectedExceptions = IllegalStateException.class)
    public void rollbackNoTransaction() throws Exception {
        MockUserTransaction tx = new MockUserTransaction();
        tx.rollback();
    }

}

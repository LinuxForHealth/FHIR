/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test.connection;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * Pretend to be a {@link UserTransaction} with modifiable behavior to
 * support different test scenarios
 */
public class MockUserTransaction implements UserTransaction {
    private int status = Status.STATUS_NO_TRANSACTION;
    private int transactionTimeout = 0;

    @Override
    public void begin() throws NotSupportedException, SystemException {
        if (this.status != Status.STATUS_NO_TRANSACTION) {
            throw new IllegalStateException("transaction already active");
        }
        this.status = Status.STATUS_ACTIVE;
    }

    @Override
    public void commit()
        throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {

        switch (this.status) {
        case Status.STATUS_ACTIVE:
            this.status = Status.STATUS_NO_TRANSACTION;
            break;
        case Status.STATUS_MARKED_ROLLBACK:
            throw new RollbackException("transaction rolled back");
        default:
            throw new IllegalStateException("transaction not active");
        }

    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        switch (this.status) {
        case Status.STATUS_ACTIVE:
        case Status.STATUS_MARKED_ROLLBACK:
            this.status = Status.STATUS_NO_TRANSACTION;
            break;
        default:
            throw new IllegalStateException("transaction not active");
        }
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        if (this.status == Status.STATUS_NO_TRANSACTION) {
            throw new IllegalStateException("transaction not active");
        }
        this.status = Status.STATUS_MARKED_ROLLBACK;
    }

    @Override
    public int getStatus() throws SystemException {
        return this.status;
    }

    @Override
    public void setTransactionTimeout(int seconds) throws SystemException {
        this.transactionTimeout = seconds;
    }
    
    /**
     * Getter for transactionTimeout to support unit tests
     * @return
     */
    public int getTransactionTimeout() {
        return this.transactionTimeout;
    }
}
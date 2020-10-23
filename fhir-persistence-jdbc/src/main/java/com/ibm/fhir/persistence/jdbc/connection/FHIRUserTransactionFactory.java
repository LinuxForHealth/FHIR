/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import com.ibm.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;

/**
 * Factory implementation for creating new instances of FHIRTransactionImpl
 * referencing a given {@link UserTransaction} object
 */
public class FHIRUserTransactionFactory implements FHIRTransactionFactory {
    // The UserTransaction object used to create the objects we manufacture
    private final UserTransaction userTransaction;
    
    private final TransactionSynchronizationRegistry syncRegistry;
    
    private final FHIRPersistenceJDBCCache cache;
    
    private final String transactionDataKey;

    /**
     * Public constructor
     * @param tx
     */
    public FHIRUserTransactionFactory(UserTransaction tx, TransactionSynchronizationRegistry syncReg, FHIRPersistenceJDBCCache cache, String transactionDataKey) {
        this.userTransaction = tx;
        this.syncRegistry = syncReg;
        this.cache = cache;
        this.transactionDataKey = transactionDataKey;
    }
    
    @Override
    public FHIRPersistenceTransaction create() {
        return new FHIRUserTransactionAdapter(userTransaction, syncRegistry, cache, transactionDataKey);
    }
}

/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.util.function.Consumer;

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
    
    private final Consumer<Boolean> afterTransactionHandler;

    /**
     * Public constructor
     * 
     * @param tx
     * @param syncReg
     * @param cache
     * @param transactionDataKey
     * @param afterTransactionHandler
     */
    public FHIRUserTransactionFactory(UserTransaction tx, TransactionSynchronizationRegistry syncReg, FHIRPersistenceJDBCCache cache, String transactionDataKey,
            Consumer<Boolean> afterTransactionHandler) {
        this.userTransaction = tx;
        this.syncRegistry = syncReg;
        this.cache = cache;
        this.transactionDataKey = transactionDataKey;
        this.afterTransactionHandler = afterTransactionHandler;
    }
    
    @Override
    public FHIRPersistenceTransaction create() {
        return new FHIRUserTransactionAdapter(userTransaction, syncRegistry, cache, transactionDataKey, afterTransactionHandler);
    }
}

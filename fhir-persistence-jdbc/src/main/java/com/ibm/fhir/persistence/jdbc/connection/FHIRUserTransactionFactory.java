/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import javax.transaction.UserTransaction;

import com.ibm.fhir.persistence.FHIRPersistenceTransaction;

/**
 * Factory implementation for creating new instances of FHIRTransactionImpl
 * referencing a given {@link UserTransaction} object
 */
public class FHIRUserTransactionFactory implements FHIRTransactionFactory {
    // The UserTransaction object used to create the objects we manufacture
    private final UserTransaction userTransaction;

    /**
     * Public constructor
     * @param tx
     */
    public FHIRUserTransactionFactory(UserTransaction tx) {
        this.userTransaction = tx;
    }
    
    @Override
    public FHIRPersistenceTransaction create() {
        return new FHIRUserTransactionAdapter(userTransaction);
    }
}

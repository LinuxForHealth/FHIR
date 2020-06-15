/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.transaction;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;

/**
 * Simple Transaction Wrapper and provider
 */
public class SimpleTransactionProvider implements ITransactionProvider {
    private final IConnectionProvider connectionProvider;

    /**
     * Public constructor
     * @param cp
     */
    public SimpleTransactionProvider(IConnectionProvider cp) {
        this.connectionProvider = cp;
    }

    @Override
    public ITransaction getTransaction() {
        // Start a transaction, connected to the connectionProvider we've
        // been configured with
        return TransactionFactory.openTransaction(this.connectionProvider);
    }
}
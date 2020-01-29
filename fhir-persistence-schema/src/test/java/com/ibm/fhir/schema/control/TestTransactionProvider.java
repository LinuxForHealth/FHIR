/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;

/**
 * A transaction provider stub useful for unit tests where we don't actually
 * need real transactions
 */
public class TestTransactionProvider implements ITransactionProvider {
    @Override
    public ITransaction getTransaction() {
        return new ITransaction() {
            @Override
            public void setRollbackOnly() {
                // No Operation
            }

            @Override
            public void close() throws DataAccessException {
                // No Operation
            }
        };
    }
}
/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.schema.control;

import org.linuxforhealth.fhir.database.utils.api.DataAccessException;
import org.linuxforhealth.fhir.database.utils.api.ITransaction;
import org.linuxforhealth.fhir.database.utils.api.ITransactionProvider;

/**
 * A transaction provider stub useful for unit tests where we don't actually
 * need real transactions
 */
public class TransactionProviderTest implements ITransactionProvider {

    @Override
    public ITransaction getTransaction() {
        return new ITransaction() {

            @Override
            public void setRollbackOnly() {
                // TODO Auto-generated method stub

            }

            @Override
            public void close() throws DataAccessException {
                // TODO Auto-generated method stub

            }
        };
    }

}

/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.schema.control;

import com.ibm.watsonhealth.database.utils.api.DataAccessException;
import com.ibm.watsonhealth.database.utils.api.ITransaction;
import com.ibm.watsonhealth.database.utils.api.ITransactionProvider;

/**
 * A transaction provider stub useful for unit tests where we don't actually
 * need real transactions
 * @author rarnold
 *
 */
public class TestTransactionProvider implements ITransactionProvider {

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.ITransactionProvider#getTransaction()
     */
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

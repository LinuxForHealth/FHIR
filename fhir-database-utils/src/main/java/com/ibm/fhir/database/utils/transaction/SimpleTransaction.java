/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.transaction;

import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.ITransaction;


/**
 * Simple implementation of a transaction service, taking cues from JEE
 * but without the overhead. No distributed transaction support.
 */
public final class SimpleTransaction implements ITransaction {
    
    // Flag to indicate if we should commit or rollback this transaction
    private boolean rollbackOnly;

    // Provides connections in a thread-aware manner
    private final IConnectionProvider connectionProvider;
    
    /**
     * Protected constructor. Should only be instantiated by the TransactionFactory
     * @param cp
     */
    protected SimpleTransaction(IConnectionProvider cp) {
        this.connectionProvider = cp;
    }
        
    /* (non-Javadoc)
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws DataAccessException {
        try {
            if (this.rollbackOnly) {
                connectionProvider.rollbackTransaction();
            }
            else {
                connectionProvider.commitTransaction();
            }
        }
        catch (SQLException x) {
            // We don't want to pollute business layers calling this method
            // with JDBC dependencies, so we wrap the SQLException
            throw new DataAccessException(x);
        }
        finally {
            TransactionFactory.clearTransaction();
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.api.ITransaction#setRollbackOnly()
     */
    @Override
    public void setRollbackOnly() {
        // trap door. Once set, can't be unset.
        this.rollbackOnly = true;
    }

}

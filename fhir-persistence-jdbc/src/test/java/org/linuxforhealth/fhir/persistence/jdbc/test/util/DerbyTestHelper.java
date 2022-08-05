/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test.util;

import java.util.Properties;

import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.ITransaction;
import org.linuxforhealth.fhir.database.utils.api.ITransactionProvider;
import org.linuxforhealth.fhir.database.utils.pool.PoolConnectionProvider;
import org.linuxforhealth.fhir.database.utils.transaction.SimpleTransactionProvider;
import org.linuxforhealth.fhir.model.test.TestUtil;

/**
 * A convenience class pulling together the objects needed to run unit-tests with transactions
 */
public class DerbyTestHelper {

    private final Properties testProps;

    // A connection provider configured for connections to our Derby test database
    private IConnectionProvider derbyConnectionProvider;
    
    // The connection pool to manage connections used within transactions provided by the transactionProvider
    private PoolConnectionProvider connectionPool;
    
    // A way to manage simple transactions in conjunction with a PoolConnectionProvider
    private ITransactionProvider transactionProvider;

    public DerbyTestHelper(int poolSize) throws Exception {
        this.testProps = TestUtil.readTestProperties("test.jdbc.properties");

        // set up the database, initializing it if necessary
        DerbyInitializer di = new DerbyInitializer(testProps);
        this.derbyConnectionProvider = di.getConnectionProvider(false);
        connectionPool = new PoolConnectionProvider(derbyConnectionProvider, poolSize);
        transactionProvider = new SimpleTransactionProvider(connectionPool);
    }
    
    /**
     * Get a transaction object from the transaction provider. Caller is responsible
     * for closing the transaction. Tip...use try-with-resource.
     * @return
     */
    public ITransaction transaction() {
        return transactionProvider.getTransaction();
    }

    /**
     * Get the connection provider (pool) used to manage connections for the test
     * Derby database.
     * @return
     */
    public IConnectionProvider getConnectionProvider() {
        return this.connectionPool;
    }
}

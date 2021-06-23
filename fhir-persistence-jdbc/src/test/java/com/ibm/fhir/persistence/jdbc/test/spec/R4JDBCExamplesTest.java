/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.testng.annotations.Test;

import com.ibm.fhir.config.DefaultFHIRConfigProvider;
import com.ibm.fhir.config.FHIRConfigProvider;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.examples.Index;
import com.ibm.fhir.model.spec.test.R4ExamplesDriver;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.cache.CommonTokenValuesCacheImpl;
import com.ibm.fhir.persistence.jdbc.cache.FHIRPersistenceJDBCCacheImpl;
import com.ibm.fhir.persistence.jdbc.cache.IdNameCache;
import com.ibm.fhir.persistence.jdbc.cache.NameIdCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.fhir.persistence.test.common.AbstractPersistenceTest;
import com.ibm.fhir.validation.test.ValidationProcessor;

public class R4JDBCExamplesTest extends AbstractPersistenceTest {

    private Properties properties;

    // provides connections to a bootstrapped Derby database
    private IConnectionProvider derbyConnectionProvider;

    /**
     * Public constructor
     * @throws Exception
     */
    public R4JDBCExamplesTest() throws Exception {
        this.properties = TestUtil.readTestProperties("test.jdbc.properties");
    }

    @Test(groups = { "jdbc-seed" }, singleThreaded = true, priority = -1)
    public void perform() throws Exception {

        // Use connection pool and transaction provider to make sure the resource operations
        // of each resource are committed after the processing is finished, and because this
        // testng test process the samples one by one, so set the connection pool size to 1.
        PoolConnectionProvider connectionPool = new PoolConnectionProvider(derbyConnectionProvider, 1);
        ITransactionProvider transactionProvider = new SimpleTransactionProvider(connectionPool);
        FHIRConfigProvider configProvider = new DefaultFHIRConfigProvider();
        ICommonTokenValuesCache rrc = new CommonTokenValuesCacheImpl(100, 100, 100);
        FHIRPersistenceJDBCCache cache = new FHIRPersistenceJDBCCacheImpl(new NameIdCache<Integer>(), new IdNameCache<Integer>(), new NameIdCache<Integer>(), rrc);

        List<ITestResourceOperation> operations = new ArrayList<>();
        operations.add(new CreateOperation());
        operations.add(new ReadOperation());
        operations.add(new UpdateOperation());
        operations.add(new UpdateOperation());
        operations.add(new ReadOperation());
        operations.add(new VReadOperation());
        operations.add(new HistoryOperation(3));
        operations.add(new DeleteOperation());
        operations.add(new DeleteOperation());
        operations.add(new HistoryOperation(4));
        R4JDBCExamplesProcessor processor = new R4JDBCExamplesProcessor(
                operations,
                this.properties,
                connectionPool,
                null,
                null,
                transactionProvider,
                configProvider,
                cache);

        // The driver will iterate over all the examples in the index, parse
        // the resource and call the processor.
        R4ExamplesDriver driver = new R4ExamplesDriver();
        driver.setProcessor(processor);
        driver.setValidator(new ValidationProcessor());
        String index = System.getProperty(this.getClass().getName() + ".index", Index.MINIMAL_JSON.name());
        driver.processIndex(Index.valueOf(index));
    }

    /**
     * Create a new {@link FHIRPersistenceContext} for the test
     * @return
     */
    protected FHIRPersistenceContext createPersistenceContext() {
        try {
            return getDefaultPersistenceContext();
        }
        catch (Exception x) {
            // because we're used as a lambda supplier, need to avoid a checked exception
            throw new IllegalStateException(x);
        }
    }

    /**
     * Create a new FHIRPersistenceContext configure with a FHIRHistoryContext
     * @return
     */
    protected FHIRPersistenceContext createHistoryPersistenceContext() {
        try {
            FHIRHistoryContext fhc = FHIRPersistenceContextFactory.createHistoryContext();
            return getPersistenceContextForHistory(fhc);
        }
        catch (Exception x) {
            // because we're used as a lambda supplier, need to avoid a checked exception
            throw new IllegalStateException(x);
        }
    }

    @Override
    public FHIRPersistence getPersistenceImpl() throws Exception {
        // Return null to make sure the transaction operations in @BeforeMethod will be skipped
        return null;
    }

    @Override
    public void bootstrapDatabase() throws Exception {
        // The Derby database instance used for the persistence tests. This is
        // an on-disk (not memory) instance due to the amount of data in the
        // tests and limited resources on build nodes.
        DerbyInitializer di = new DerbyInitializer(properties);
        derbyConnectionProvider = di.getConnectionProvider(false); // no need to reset
    }
}
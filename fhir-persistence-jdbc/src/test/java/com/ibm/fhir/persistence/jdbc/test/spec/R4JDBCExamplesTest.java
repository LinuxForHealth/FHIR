/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

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
import com.ibm.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.fhir.persistence.test.common.AbstractPersistenceTest;
import com.ibm.fhir.schema.derby.DerbyFhirDatabase;
import com.ibm.fhir.validation.test.ValidationProcessor;

public class R4JDBCExamplesTest extends AbstractPersistenceTest {
    // The Derby database instance used for the persistence tests
    private DerbyFhirDatabase database;

    private Properties properties;

    public R4JDBCExamplesTest() throws Exception {
        this.properties = TestUtil.readTestProperties("test.jdbc.properties");
    }

    @Test(groups = { "jdbc-seed" }, singleThreaded = true, priority = -1)
    public void perform() throws Exception {
        if(this.database == null) {
            System.out.println("Bootstrapping database:");
            this.database = new DerbyFhirDatabase(DerbyInitializer.DB_NAME);
        }
        
        // Use connection pool and transaction provider to make sure the resource operations
        // of each resource are committed after the processing is finished, and because this
        // testng test process the samples one by one, so set the connection pool size to 1.
        PoolConnectionProvider connectionPool = new PoolConnectionProvider(database, 1);
        ITransactionProvider transactionProvider = new SimpleTransactionProvider(connectionPool);
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
                transactionProvider);

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
        // Create the derby database
        this.database = new DerbyInitializer().bootstrapDb();
    }

    @AfterClass
    public void shutdown() throws Exception {
        System.out.println("Shutting down database");
        if (this.database != null) {
            this.database.close();
        }
    }
}
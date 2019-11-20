/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.model.spec.test.R4ExamplesDriver;
import com.ibm.fhir.model.spec.test.R4ExamplesDriver.TestType;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.test.common.AbstractPersistenceTest;
import com.ibm.fhir.schema.derby.DerbyFhirDatabase;

public class R4JDBCExamplesTest extends AbstractPersistenceTest {

    // The Derby database instance used for the persistence tests
    private DerbyFhirDatabase database;

    private Properties properties;

    public R4JDBCExamplesTest() throws Exception {
        this.properties = TestUtil.readTestProperties("test.jdbc.properties");
    }

    @BeforeSuite
    public void bootstrapAndLoad() {
        System.out.println("Bootstrapping database:");


        System.out.println("Processing examples:");
    }

    @Test(groups = { "jdbc-seed" })
    public void perform() throws Exception {
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

        // Overriding the JDBC ALL to Minimal.
        // Unless the profile tells us differently
        String testType = System.getProperty("com.ibm.fhir.persistence.jdbc.test.spec.R4JDBCExamplesTest.testType", TestType.MINIMAL.toString());
        System.setProperty("com.ibm.fhir.model.spec.test.R4ExamplesDriver.testType", testType);

        // The driver will iterate over all the JSON examples in the R4 specification, parse
        // the resource and call the processor.
        R4ExamplesDriver driver = new R4ExamplesDriver();
        driver.setProcessor(processor);
        driver.processAllExamples();
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
        this.database = new DerbyFhirDatabase();
    }

    @AfterClass
    public void shutdown() throws Exception {
        System.out.println("Shutting down database");
        if (this.database != null) {
            this.database.close();
        }
    }
}

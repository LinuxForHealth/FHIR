/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.schema.derby;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyConnectionProvider;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.thread.ThreadHandler;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.database.utils.version.CreateControl;
import com.ibm.fhir.schema.app.LeaseManager;
import com.ibm.fhir.schema.app.LeaseManagerConfig;

/**
 * Unit test for the LeaseManager with Derby
 */
public class DerbyLeaseTest {
    private static final String TARGET_DIR = "target/derby/";
    private static final String ADMIN_SCHEMA_NAME = "FHIR_ADMIN";
    private static final String SCHEMA_NAME = "FHIRDATA";
    
    @Test
    public void test() throws Exception {
        String dbPath = TARGET_DIR + "fhirdb";
        DerbyMaster.dropDatabase(dbPath);
        try (DerbyMaster db = new DerbyMaster(dbPath)) {

            // Create the CONTROL table
            db.runWithAdapter(adapter -> CreateControl.createTableIfNeeded(ADMIN_SCHEMA_NAME, adapter));

            IConnectionProvider cp = new DerbyConnectionProvider(db, null);
            PoolConnectionProvider connectionPool = new PoolConnectionProvider(cp, 10);
            ITransactionProvider transactionProvider = new SimpleTransactionProvider(connectionPool);
            IDatabaseAdapter adapter = new DerbyAdapter(connectionPool);
            
            // Set up the lease manager
            LeaseManagerConfig config1 = LeaseManagerConfig.builder().withHost("lm1").withLeaseTimeSeconds(4).build();
            LeaseManager lm1 = new LeaseManager(adapter.getTranslator(), connectionPool, transactionProvider, ADMIN_SCHEMA_NAME, SCHEMA_NAME, config1);
            boolean gotLease = lm1.waitForLease(1);
            assertTrue(gotLease);
            lm1.signalHeartbeat();
            ThreadHandler.safeSleep(ThreadHandler.SECOND);
            assertTrue(lm1.hasLease());
            
            // Sleep for so long our lease expires
            ThreadHandler.safeSleep(ThreadHandler.FIVE_SECONDS);
            assertFalse(lm1.hasLease());
            
            // finally cancel our lease
            lm1.cancelLease();
            assertFalse(lm1.hasLease());
            lm1.shutdown();
            
            // Now we should be able to obtain the lease using a different LeaseManager
            LeaseManagerConfig config2 = LeaseManagerConfig.builder().withHost("lm2").withLeaseTimeSeconds(64).build();
            LeaseManager lm2 = new LeaseManager(adapter.getTranslator(), connectionPool, transactionProvider, ADMIN_SCHEMA_NAME, SCHEMA_NAME, config2);
            boolean gotLease2 = lm2.waitForLease(1);
            assertTrue(gotLease2);
            
            // But another lease manager now should fail because the lease is currently
            // held by lm2
            LeaseManagerConfig config3 = LeaseManagerConfig.builder().withHost("lm3").withLeaseTimeSeconds(64).build();
            LeaseManager lm3 = new LeaseManager(adapter.getTranslator(), connectionPool, transactionProvider, ADMIN_SCHEMA_NAME, SCHEMA_NAME, config3);
            boolean gotLease3 = lm3.waitForLease(1);
            assertFalse(gotLease3);

            assertTrue(lm2.cancelLease());
            lm2.shutdown();
            
            assertFalse(lm3.cancelLease());
            lm3.shutdown();
        }
    }
}
/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test;

import com.ibm.fhir.config.FHIRConfigProvider;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.jdbc.test.util.PersistenceTestSupport;
import com.ibm.fhir.persistence.test.common.AbstractChangesTest;
import com.ibm.fhir.search.util.SearchHelper;

/**
 * JDBC test implementation of the change records functions provided by the persistence layer
 */
public class JDBCChangesTest extends AbstractChangesTest {

    // Container to hide the instantiation of the persistence impl used for tests
    private PersistenceTestSupport testSupport;

    @Override
    public void bootstrapDatabase() throws Exception {
        testSupport = new PersistenceTestSupport();
    }

    @Override
    public FHIRPersistence getPersistenceImpl(FHIRConfigProvider configProvider, SearchHelper searchHelper) throws Exception {
        return testSupport.getPersistenceImpl(configProvider, searchHelper);
    }

    @Override
    protected void shutdownPools() throws Exception {
        if (testSupport != null) {
            testSupport.shutdown();
        }
    }

    @Override
    protected void debugLocks() {
        testSupport.debugLocks();
    }
}
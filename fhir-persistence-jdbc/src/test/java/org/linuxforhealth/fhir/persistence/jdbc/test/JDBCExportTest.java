/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test;

import org.linuxforhealth.fhir.config.FHIRConfigProvider;
import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.jdbc.test.util.PersistenceTestSupport;
import org.linuxforhealth.fhir.persistence.test.common.AbstractExportTest;
import org.linuxforhealth.fhir.search.util.SearchHelper;

/**
 * JDBC test implementation of the high speed export functions provided by the persistence layer
 */
public class JDBCExportTest extends AbstractExportTest {

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
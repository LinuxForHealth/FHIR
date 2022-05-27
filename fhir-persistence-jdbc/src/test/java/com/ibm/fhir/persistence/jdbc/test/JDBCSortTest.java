/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test;

import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.jdbc.test.util.PersistenceTestSupport;
import com.ibm.fhir.persistence.test.common.AbstractSortTest;


public class JDBCSortTest extends AbstractSortTest {

    // Container to hide the instantiation of the persistence impl used for tests
    private PersistenceTestSupport testSupport;

    @Override
    public void bootstrapDatabase() throws Exception {
        testSupport = new PersistenceTestSupport();
    }

    @Override
    public FHIRPersistence getPersistenceImpl() throws Exception {
        return testSupport.getPersistenceImpl();
    }

    @Override
    protected void shutdownPools() throws Exception {
        if (testSupport != null) {
            testSupport.shutdown();
        }
    }
}
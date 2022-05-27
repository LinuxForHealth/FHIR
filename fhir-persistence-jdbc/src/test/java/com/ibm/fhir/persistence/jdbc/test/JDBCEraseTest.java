/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.cache.CommonTokenValuesCacheImpl;
import com.ibm.fhir.persistence.jdbc.cache.FHIRPersistenceJDBCCacheImpl;
import com.ibm.fhir.persistence.jdbc.cache.IdNameCache;
import com.ibm.fhir.persistence.jdbc.cache.NameIdCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import com.ibm.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.fhir.persistence.jdbc.test.util.PersistenceTestSupport;
import com.ibm.fhir.persistence.test.common.AbstractEraseTest;

/**
 * JDBC test implementation of the Erase DAO provided by the persistence layer
 */
public class JDBCEraseTest extends AbstractEraseTest {

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

    @Override
    protected void debugLocks() {
        testSupport.debugLocks();
    }
}
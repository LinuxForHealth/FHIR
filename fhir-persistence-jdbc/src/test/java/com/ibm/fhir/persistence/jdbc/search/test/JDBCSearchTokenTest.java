/*
 * (C) Copyright IBM Corp. 2018, 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.search.test;

import java.util.Properties;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceCache;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceCacheImpl;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCCacheImpl;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import com.ibm.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.fhir.persistence.search.test.AbstractSearchTokenTest;


public class JDBCSearchTokenTest extends AbstractSearchTokenTest {

    private Properties testProps;

    private PoolConnectionProvider connectionPool;
    
    private FHIRPersistenceJDBCCache cache;

    public JDBCSearchTokenTest() throws Exception {
        this.testProps = TestUtil.readTestProperties("test.jdbc.properties");
    }

    @Override
    public void bootstrapDatabase() throws Exception {
        DerbyInitializer derbyInit;
        String dbDriverName = this.testProps.getProperty("dbDriverName");
        if (dbDriverName != null && dbDriverName.contains("derby")) {
            derbyInit = new DerbyInitializer(this.testProps);
            IConnectionProvider cp = derbyInit.getConnectionProvider(false);
            this.connectionPool = new PoolConnectionProvider(cp, 1);
            IResourceReferenceCache rrc = new ResourceReferenceCacheImpl(100, 100);
            cache = new FHIRPersistenceJDBCCacheImpl(rrc);
        }
    }

    @Override
    public FHIRPersistence getPersistenceImpl() throws Exception {
        if (this.connectionPool == null) {
            throw new IllegalStateException("Database not bootstrapped");
        }
        return new FHIRPersistenceJDBCImpl(this.testProps, this.connectionPool, this.cache);
    }

    @Override
    protected void shutdownPools() throws Exception {
        // Mark the pool as no longer in use. This allows the pool to check for
        // lingering open connections/transactions.
        if (this.connectionPool != null) {
            this.connectionPool.close();
        }
    }


    /*
     * Currently, documented in our conformance statement. We do not support
     * modifiers on chained parameters.
     * https://ibm.github.io/FHIR/Conformance#search-modifiers
     * Refer to https://github.com/IBM/FHIR/issues/473 to track the issue.
     */
    @Override
    @Test(expectedExceptions = FHIRPersistenceNotSupportedException.class)
    public void testSearchToken_boolean_chained_missing() throws Exception {
        super.testSearchToken_boolean_chained_missing();
    }
    @Override
    @Test(expectedExceptions = FHIRPersistenceNotSupportedException.class)
    public void testSearchToken_code_chained_missing() throws Exception {
        super.testSearchToken_code_chained_missing();
    }
    @Override
    @Test(expectedExceptions = FHIRPersistenceNotSupportedException.class)
    public void testSearchToken_CodeableConcept_chained_missing() throws Exception {
        super.testSearchToken_CodeableConcept_chained_missing();
    }
}

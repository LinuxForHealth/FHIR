/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.jdbc.search.test;

import java.util.Properties;

import org.testng.annotations.BeforeClass;

import com.ibm.watson.health.fhir.config.FHIRRequestContext;
import com.ibm.watson.health.fhir.exception.FHIRException;
import com.ibm.watson.health.fhir.persistence.FHIRPersistence;
import com.ibm.watson.health.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCNormalizedImpl;
import com.ibm.watson.health.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.watson.health.fhir.persistence.search.test.AbstractSearchTokenTest;


public class JDBCNormSearchTokenTest extends AbstractSearchTokenTest {
    
    private Properties testProps;
    
    public JDBCNormSearchTokenTest() throws Exception {
        this.testProps = readTestProperties("test.normalized.properties");
    }

    @BeforeClass
    public void setTenant() throws FHIRException {
        FHIRRequestContext.get().setTenantId("token");
    }

    @Override
    public void bootstrapDatabase() throws Exception {
        DerbyInitializer derbyInit;
        String dbDriverName = this.testProps.getProperty("dbDriverName");
        if (dbDriverName != null && dbDriverName.contains("derby")) {
            derbyInit = new DerbyInitializer(this.testProps);
            derbyInit.bootstrapDb();
        }
    }
    
    @Override
    public FHIRPersistence getPersistenceImpl() throws Exception {
        return new FHIRPersistenceJDBCNormalizedImpl(this.testProps);
    }
}

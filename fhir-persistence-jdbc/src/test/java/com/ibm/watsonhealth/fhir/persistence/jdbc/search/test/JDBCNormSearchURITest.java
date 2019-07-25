/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.search.test;

import java.util.Properties;

import org.testng.annotations.BeforeClass;

import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.exception.FHIRException;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCNormalizedImpl;
import com.ibm.watsonhealth.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.watsonhealth.fhir.persistence.search.test.AbstractSearchURITest;


public class JDBCNormSearchURITest extends AbstractSearchURITest {
    
    private Properties testProps;
    
    public JDBCNormSearchURITest() throws Exception {
        this.testProps = readTestProperties("test.normalized.properties");
    }

    @BeforeClass
    public void setTenant() throws FHIRException {
        FHIRRequestContext.get().setTenantId("uri");
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

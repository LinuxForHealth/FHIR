/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.search.test;

import java.util.Properties;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.exception.FHIRException;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCNormalizedImpl;
import com.ibm.watsonhealth.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.watsonhealth.fhir.persistence.search.test.AbstractSearchDateTest;


public class JDBCNormSearchDateTest extends AbstractSearchDateTest {
    
    private Properties testProps;
    
    public JDBCNormSearchDateTest() throws Exception {
        this.testProps = readTestProperties("test.normalized.properties");
    }
    
    @BeforeClass
    public void setTenant() throws FHIRException {
        FHIRRequestContext.get().setTenantId("date");
    }
    
    @Override
    public void bootstrapDatabase() throws Exception {
        DerbyInitializer derbyInit;
        String dbDriverName = this.testProps.getProperty("dbDriverName");
        if (dbDriverName != null && dbDriverName.contains("derby")) {
            derbyInit = new DerbyInitializer(this.testProps);
            derbyInit.bootstrapDb(false);
        }
    }
    
    @Override
    public FHIRPersistence getPersistenceImpl() throws Exception {
        return new FHIRPersistenceJDBCNormalizedImpl(this.testProps);
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.search.test.AbstractSearchDateTest#testCreateBasicResource()
     */
    @Override
    @Test
    public void testCreateBasicResource() throws Exception {
        super.testCreateBasicResource();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.search.test.AbstractSearchDateTest#testSearchDate_date()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchDate_date() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchDate_date();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.search.test.AbstractSearchDateTest#testSearchDate_date()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchDate_dateTime() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchDate_dateTime();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.search.test.AbstractSearchDateTest#testSearchDate_Period()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchDate_Period() throws Exception {
        super.testSearchDate_Period();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.search.test.AbstractSearchDateTest#testSearchDate_Period_NoStart()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchDate_Period_NoStart() throws Exception {
        super.testSearchDate_Period_NoStart();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.search.test.AbstractSearchDateTest#testSearchDate_Period_NoEnd()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchDate_Period_NoEnd() throws Exception {
        super.testSearchDate_Period_NoEnd();
    }
}

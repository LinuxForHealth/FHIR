/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.jdbc.search.test;

import java.util.Properties;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.config.FHIRRequestContext;
import com.ibm.watson.health.fhir.exception.FHIRException;
import com.ibm.watson.health.fhir.persistence.FHIRPersistence;
import com.ibm.watson.health.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watson.health.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCNormalizedImpl;
import com.ibm.watson.health.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.watson.health.fhir.persistence.search.test.AbstractSearchNumberTest;
import com.ibm.watson.health.fhir.search.exception.FHIRSearchException;


public class JDBCNormSearchNumberTest extends AbstractSearchNumberTest {
    
    private Properties testProps;
    
    public JDBCNormSearchNumberTest() throws Exception {
        this.testProps = readTestProperties("test.normalized.properties");
    }
    
    @BeforeClass
    public void setTenant() throws FHIRException {
        FHIRRequestContext.get().setTenantId("number");
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
    
    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.persistence.search.test.AbstractSearchNumberTest#testCreateBasicResource()
     */
    @Override
    @Test
    public void testCreateBasicResource() throws Exception {
        // TODO Auto-generated method stub
        super.testCreateBasicResource();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.persistence.search.test.AbstractSearchNumberTest#testSearchNumber_integer()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchNumber_integer() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchNumber_integer();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.persistence.search.test.AbstractSearchNumberTest#testSearchNumber_integer_ap()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" }, expectedExceptions = { FHIRPersistenceException.class })
    public void testSearchNumber_integer_sa() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchNumber_integer_sa();
    }
    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.persistence.search.test.AbstractSearchNumberTest#testSearchNumber_integer_ap()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" }, expectedExceptions = { FHIRPersistenceException.class })
    public void testSearchNumber_integer_eb() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchNumber_integer_eb();
    }
    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.persistence.search.test.AbstractSearchNumberTest#testSearchNumber_integer_ap()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" }, expectedExceptions = { FHIRSearchException.class })
    public void testSearchNumber_integer_invalidPrefix() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchNumber_integer_invalidPrefix();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.persistence.search.test.AbstractSearchNumberTest#testSearchNumber_integer_chained()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchNumber_integer_chained() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchNumber_integer_chained();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.persistence.search.test.AbstractSearchNumberTest#testSearchNumber_integer_missing()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchNumber_integer_missing() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchNumber_integer_missing();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.persistence.search.test.AbstractSearchNumberTest#testSearchNumber_decimal()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchNumber_decimal() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchNumber_decimal();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.persistence.search.test.AbstractSearchNumberTest#testSearchNumber_decimal_invalidPrefix()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" }, expectedExceptions = { FHIRSearchException.class })
    public void testSearchNumber_decimal_invalidPrefix() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchNumber_decimal_invalidPrefix();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.persistence.search.test.AbstractSearchNumberTest#testSearchNumber_decimal_chained()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchNumber_decimal_chained() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchNumber_decimal_chained();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.persistence.search.test.AbstractSearchNumberTest#testSearchNumber_decimal_missing()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchNumber_decimal_missing() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchNumber_decimal_missing();
    }
}

/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.search.test;

import java.util.Properties;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCNormalizedImpl;
import com.ibm.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.fhir.persistence.search.test.AbstractSearchQuantityTest;


public class JDBCNormSearchQuantityTest extends AbstractSearchQuantityTest {
    
    private Properties testProps;
    
    public JDBCNormSearchQuantityTest() throws Exception {
        this.testProps = readTestProperties("test.normalized.properties");
    }

    @BeforeClass
    public void setTenant() throws FHIRException {
        FHIRRequestContext.get().setTenantId("quantity");
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
     * @see com.ibm.fhir.persistence.search.test.AbstractSearchQuantityTest#testSearchQuantity_Quantity()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchQuantity_Quantity() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchQuantity_Quantity();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.search.test.AbstractSearchQuantityTest#testSearchQuantity_Quantity_withPrefixes()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchQuantity_Quantity_withPrefixes() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchQuantity_Quantity_withPrefixes();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.search.test.AbstractSearchQuantityTest#testCreateBasicResource()
     */
    @Override
    @Test
    public void testCreateBasicResource() throws Exception {
        // TODO Auto-generated method stub
        super.testCreateBasicResource();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.search.test.AbstractSearchQuantityTest#testSearchQuantity_Range()
     */
    @Override
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchQuantity_Range() throws Exception {
        // TODO Auto-generated method stub
        super.testSearchQuantity_Range();
    }
}

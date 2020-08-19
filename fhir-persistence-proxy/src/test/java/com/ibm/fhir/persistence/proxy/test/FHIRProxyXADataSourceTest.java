/*
 * (C) Copyright IBM Corp. 2017,2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.proxy.test;

import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.sql.SQLException;

import javax.sql.XADataSource;

import org.apache.derby.jdbc.EmbeddedXADataSource;
import org.apache.derby.jdbc.ClientXADataSource;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.db2.jcc.DB2XADataSource;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.persistence.proxy.FHIRProxyXADataSource;

/**
 * This class contains unit tests for the FHIRProxyXADataSource class.
 */
public class FHIRProxyXADataSourceTest {

    
    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
    }
    
    @BeforeMethod
    @AfterMethod
    public void clearThreadLocal() {
        FHIRRequestContext.remove();
    }
    
    @Test(expectedExceptions = { SQLException.class })
    public void testNoDatasources() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("noprops", "notused"));
        FHIRProxyXADataSource proxyDS = new FHIRProxyXADataSource();
        proxyDS.getDelegate();
    }
    
    @Test(expectedExceptions = { SQLException.class })
    public void testBadDatastoreId() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1", "baddatastoreid"));
        FHIRProxyXADataSource proxyDS = new FHIRProxyXADataSource();
        proxyDS.getDelegate();
    }
    
    @Test(expectedExceptions = { SQLException.class })
    public void testDerby_BadDatasourceProperty() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1", "derby_BadProp"));
        FHIRProxyXADataSource proxyDS = new FHIRProxyXADataSource();
        proxyDS.getDelegate();
    }
    
    @Test(expectedExceptions = { SQLException.class })
    public void testDB2_BadDatasourceProperty() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1", "db2_BadProp"));
        FHIRProxyXADataSource proxyDS = new FHIRProxyXADataSource();
        proxyDS.getDelegate();
    }

    @Test
    public void testDerby_1() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1", "derby_1"));
        
        FHIRProxyXADataSource proxyDS = new FHIRProxyXADataSource();
        XADataSource xaDS = proxyDS.getDelegate();
        assertNotNull(xaDS);
        assertTrue(xaDS instanceof EmbeddedXADataSource);
        EmbeddedXADataSource derbyDS = (EmbeddedXADataSource) xaDS;
        assertEquals("myDerbyDatabase1", derbyDS.getDatabaseName());
    }
    
    @Test
    public void testDerby_2() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1", "derby_2"));
        
        FHIRProxyXADataSource proxyDS = new FHIRProxyXADataSource();
        XADataSource xaDS = proxyDS.getDelegate();
        assertNotNull(xaDS);
        assertTrue(xaDS instanceof EmbeddedXADataSource);
        EmbeddedXADataSource derbyDS = (EmbeddedXADataSource) xaDS;
        assertEquals("myDerbyDatabase2", derbyDS.getDatabaseName());
        assertEquals("create", derbyDS.getCreateDatabase());
    }
    
    @Test
    public void testDerby_3() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1", "derby_3"));
        
        FHIRProxyXADataSource proxyDS = new FHIRProxyXADataSource();
        XADataSource xaDS = proxyDS.getDelegate();
        assertNotNull(xaDS);
        assertTrue(xaDS instanceof EmbeddedXADataSource);
        EmbeddedXADataSource derbyDS = (EmbeddedXADataSource) xaDS;
        assertEquals("myDerbyDatabase3", derbyDS.getDatabaseName());
        assertEquals("create", derbyDS.getCreateDatabase());
        assertEquals("dbuser", derbyDS.getUser());
        assertEquals("change-password", derbyDS.getPassword());
        assertEquals("myDataSource", derbyDS.getDataSourceName());
        assertEquals("shutdown", derbyDS.getShutdownDatabase());
        assertEquals(1000, derbyDS.getLoginTimeout());
        assertEquals("DataSource description", derbyDS.getDescription());
        assertEquals("prop1=value1", derbyDS.getConnectionAttributes());
        assertEquals(false, derbyDS.getAttributesAsPassword());
    }
    
    @Test
    public void testDerby_4() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant2", "derby_4"));
        
        FHIRProxyXADataSource proxyDS = new FHIRProxyXADataSource();
        XADataSource xaDS = proxyDS.getDelegate();
        assertNotNull(xaDS);
        assertTrue(xaDS instanceof ClientXADataSource);
        ClientXADataSource derbyDS = (ClientXADataSource) xaDS;
        assertEquals("myDerbyDatabase4", derbyDS.getDatabaseName());
        assertEquals("create", derbyDS.getCreateDatabase());
        assertEquals("dbuser", derbyDS.getUser());
        assertEquals("change-password", derbyDS.getPassword());
        assertEquals("x.x.x.x", derbyDS.getServerName());
        assertEquals(1527, derbyDS.getPortNumber());
    }
    
    @Test
    public void testDerby_default_default() throws Exception {
        // We can only request databases in the default tenant by explicitly
        // specifying the default tenant. Fallback is no longer supported (issue 639)
        FHIRRequestContext.set(new FHIRRequestContext("default", "default_derby"));
        
        FHIRProxyXADataSource proxyDS = new FHIRProxyXADataSource();
        XADataSource xaDS = proxyDS.getDelegate();
        assertNotNull(xaDS);
        assertTrue(xaDS instanceof EmbeddedXADataSource);
        EmbeddedXADataSource derbyDS = (EmbeddedXADataSource) xaDS;
        assertEquals("myDefaultDerbyDatabase", derbyDS.getDatabaseName());
        assertEquals("create", derbyDS.getCreateDatabase());
    }
    
    @Test(expectedExceptions = java.sql.SQLException.class)
    public void testDerby_default() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1", "default_derby"));
        
        // As of issue 639, we no longer allow tenant datasource lookups
        // to fall back to the default tenant. So this lookup
        // is expected to fail
        FHIRProxyXADataSource proxyDS = new FHIRProxyXADataSource();
        XADataSource xaDS = proxyDS.getDelegate();
        assertNull(xaDS);
    }

    @Test
    public void testDB2_1() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1", "db2_1"));
        
        FHIRProxyXADataSource proxyDS = new FHIRProxyXADataSource();
        XADataSource xaDS= proxyDS.getDelegate();
        assertNotNull(xaDS);
        assertTrue(xaDS instanceof DB2XADataSource);
        DB2XADataSource derbyDS = (DB2XADataSource) xaDS;
        assertEquals("MYDB", derbyDS.getDatabaseName());
        assertEquals("mydbserver", derbyDS.getServerName());
        assertEquals(50000, derbyDS.getPortNumber());
    }
    
    @Test
    public void testDB2_2() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1", "db2_2"));
        
        FHIRProxyXADataSource proxyDS = new FHIRProxyXADataSource();
        XADataSource xaDS= proxyDS.getDelegate();
        assertNotNull(xaDS);
        assertTrue(xaDS instanceof DB2XADataSource);
        DB2XADataSource derbyDS = (DB2XADataSource) xaDS;
        assertEquals("FHIRDB", derbyDS.getDatabaseName());
        assertEquals("localhost", derbyDS.getServerName());
        assertEquals(50001, derbyDS.getPortNumber());
        assertEquals("MyDB2DataSource", derbyDS.getDataSourceName());
        assertEquals("My DB2 DataSource Description", derbyDS.getDescription());
        assertEquals("FHIR1", derbyDS.getCurrentSchema());
        assertEquals("db2inst1", derbyDS.getUser());
        assertEquals(true, derbyDS.getSslConnection());
        assertEquals("mytruststore", derbyDS.getSslTrustStoreLocation());
        assertEquals("change-password", derbyDS.getSslTrustStorePassword());
        assertEquals("mysslcert", derbyDS.getSslCertLocation());
        assertEquals(1000, derbyDS.getLoginTimeout());
        assertEquals(1001, derbyDS.getCommandTimeout());
        assertEquals(1002, derbyDS.getMemberConnectTimeout());
        assertEquals(1003, derbyDS.getConnectionTimeout());
        assertEquals(1004, derbyDS.getBlockingReadConnectionTimeout());
        assertEquals("GMT", derbyDS.getSessionTimeZone());
    }
}

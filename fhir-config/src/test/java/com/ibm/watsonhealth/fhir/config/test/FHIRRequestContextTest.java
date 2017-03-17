/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.config.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;

public class FHIRRequestContextTest {
    
    /**
     * This is a class we'll use for running some test code in a separate thread.
     */
    public static class ThreadTest implements Runnable {
        private String tenantId;
        private String dsId;
        private boolean testPassed;
        
        public ThreadTest(String t, String d) {
            this.tenantId = t;
            this.dsId = d;
            this.testPassed = false;
        }
        
        public boolean getTestPassed() {
            return testPassed;
        }
        
        public void run() {
            // If we've been given a tenant id to test with, then explicitly
            // set the request context on this thread using that id.
            // Otherwise, do not set it and simply rely on the ThreadLocal's initial value.
            if (tenantId != null) {
                FHIRRequestContext.set(new FHIRRequestContext(tenantId, dsId));
            }
            
            String expectedTenantId = (tenantId != null ? tenantId : "default");
            
            // Retrieve the request context and validate.
            FHIRRequestContext ctxt = FHIRRequestContext.get();
            assertNotNull(ctxt);
            assertEquals(expectedTenantId, ctxt.getTenantId());
            assertEquals(dsId, ctxt.getDataStoreId());
            testPassed = true;
        }
    }
    
    @Test
    public void testDefault1() throws Exception {
        FHIRRequestContext ctxt = FHIRRequestContext.get();
        assertNotNull(ctxt);
        assertEquals("default", ctxt.getTenantId());
        assertNull(ctxt.getDataStoreId());
    }
    
    @Test
    public void testDefault2() throws Exception {
        ThreadTest test = new ThreadTest(null, null);
        Thread t = new Thread(test);
        t.start();
        t.join(1000);
        assertTrue(test.getTestPassed());
    }

    @Test
    public void testNonDefault1() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1", "dsid1"));
        
        FHIRRequestContext ctxt = FHIRRequestContext.get();
        assertNotNull(ctxt);
        assertEquals("tenant1", ctxt.getTenantId());
        assertEquals("dsid1", ctxt.getDataStoreId());
    }
    
    @Test
    public void testNonDefault2() throws Exception {
        ThreadTest test = new ThreadTest("tenant1", "dsid1");
        Thread t = new Thread(test);
        t.start();
        t.join(1000);
        assertTrue(test.getTestPassed());
    }
}

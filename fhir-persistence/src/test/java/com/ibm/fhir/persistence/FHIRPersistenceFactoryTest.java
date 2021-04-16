/*
 * (C) Copyright IBM Corp. 2021, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.search.SearchConstants;

public class FHIRPersistenceFactoryTest {
    @BeforeClass
    public void setUpBeforeClass() {
        FHIRConfiguration.setConfigHome("target/test-classes");
    }
    
    @BeforeMethod
    @AfterMethod
    public void clearThreadLocal() {
        FHIRRequestContext.remove();
    }
    
    @Test
    public void testCreateWithDefaultPageSize() throws Exception {
        runCreateTest("default", FHIRConstants.FHIR_PAGE_SIZE_DEFAULT);
    }
    
    @Test
    public void testCreateWithUserConfiguredPageSize() throws Exception {
        runCreateTest("pagesize-valid", 500);
    }
    
    @Test
    public void testCreateWithUserConfiguredPageSizeBeyondMaxium() throws Exception {
        runCreateTest("pagesize-invalid", SearchConstants.MAX_PAGE_SIZE);
    }

    private void runCreateTest(String tenantId, int expectedPageSize) throws FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext(tenantId));        
        FHIRHistoryContext ctx = FHIRPersistenceContextFactory.createHistoryContext();
        assertEquals (ctx.getPageSize(), expectedPageSize);
    }
}

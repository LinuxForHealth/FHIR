/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.context;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.exception.FHIRException;

public class FHIRSearchContextFactoryTest {
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
        runCreateTest("default", FHIRConstants.FHIR_PAGE_SIZE_DEFAULT, FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX, FHIRConstants.FHIR_PAGE_INCLUDE_COUNT_DEFAULT_MAX);
    }

    @Test
    public void testCreateWithUserConfiguredPageSize() throws Exception {
        runCreateTest("tenant1", 500, FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX, FHIRConstants.FHIR_PAGE_INCLUDE_COUNT_DEFAULT_MAX);
    }

    @Test
    public void testCreateWithUserConfiguredPageSizeBeyondDefaultMaxium() throws Exception {
        runCreateTest("tenant2", FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX, FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX, FHIRConstants.FHIR_PAGE_INCLUDE_COUNT_DEFAULT_MAX);
    }

    @Test
    public void testCreateWithUserConfiguredPageSizeBeyondMaxium() throws Exception {
        runCreateTest("tenant4", 2000, 2000, 1500);
    }

    private void runCreateTest(String tenantId, int expectedPageSize, int expectedMaxPageSize, int expectedMaxPageIncludeCount) throws FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext(tenantId));
        FHIRSearchContext ctx = FHIRSearchContextFactory.createSearchContext();
        assertEquals(ctx.getPageSize(), expectedPageSize);
        assertEquals(ctx.getMaxPageSize(), expectedMaxPageSize);
        assertEquals(ctx.getMaxPageIncludeCount(), expectedMaxPageIncludeCount);
    }
}

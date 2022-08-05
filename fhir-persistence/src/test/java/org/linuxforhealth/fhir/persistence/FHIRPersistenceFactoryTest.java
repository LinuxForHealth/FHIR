/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.core.FHIRConstants;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.persistence.context.FHIRHistoryContext;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContextFactory;

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
        runCreateTest("default", FHIRConstants.FHIR_PAGE_SIZE_DEFAULT, FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX, FHIRConstants.FHIR_PAGE_INCLUDE_COUNT_DEFAULT_MAX);
    }

    @Test
    public void testCreateWithUserConfiguredPageSize() throws Exception {
        runCreateTest("pagesize-valid", 500, FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX, FHIRConstants.FHIR_PAGE_INCLUDE_COUNT_DEFAULT_MAX);
    }

    @Test
    public void testCreateWithUserConfiguredPageSizeBeyondMaxium() throws Exception {
        runCreateTest("pagesize-invalid", 4000, 4000, 2500);
    }

    private void runCreateTest(String tenantId, int expectedPageSize, int expectedMaxPageSize, int expectedMaxPageIncludeCount) throws FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext(tenantId));
        FHIRHistoryContext ctx = FHIRPersistenceContextFactory.createHistoryContext();
        assertEquals(ctx.getPageSize(), expectedPageSize);
        assertEquals(ctx.getMaxPageSize(), expectedMaxPageSize);
        assertEquals(ctx.getMaxPageIncludeCount(), expectedMaxPageIncludeCount);
    }
}

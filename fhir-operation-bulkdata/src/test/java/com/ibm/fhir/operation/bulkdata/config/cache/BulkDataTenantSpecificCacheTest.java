/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config.cache;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.operation.bulkdata.config.BulkDataConfigUtil;
import com.ibm.fhir.operation.bulkdata.processor.BulkDataFactory;
import com.ibm.fhir.operation.bulkdata.processor.ExportImportBulkData;
import com.ibm.fhir.operation.bulkdata.processor.impl.DummyImportExportImpl;

/**
 * Test BulkData Tenant Specific Cache
 */
public class BulkDataTenantSpecificCacheTest {
    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes/testdata");
    }

    @Test
    public void testBulkDataTenantSpecificCacheTenant1File() throws Exception {
        BulkDataTenantSpecificCache cache = new BulkDataTenantSpecificCache();
        String fileName = cache.getCacheEntryFilename("tenant1");
        assertEquals(fileName, "target/test-classes/testdata/config/tenant1/bulkdata.json");
    }

    @Test
    public void testBulkDataTenantSpecificCacheDefaultFile() throws Exception {
        BulkDataTenantSpecificCache cache = new BulkDataTenantSpecificCache();
        String fileName = cache.getCacheEntryFilename("default");
        assertEquals(fileName, "target/test-classes/testdata/config/default/bulkdata.json");
    }

    @Test
    public void testBulkDataTenantSpecificCacheTenant1() throws Exception {
        BulkDataTenantSpecificCache cache = new BulkDataTenantSpecificCache();
        String fileName = cache.getCacheEntryFilename("tenant1");
        assertEquals(fileName, "target/test-classes/testdata/config/tenant1/bulkdata.json");
        Map<String, String> props = cache.getCachedObjectForTenant("tenant1");
        assertNull(props);
    }

    @Test
    public void testBulkDataTenantSpecificCacheTenant2() throws Exception {
        BulkDataTenantSpecificCache cache = new BulkDataTenantSpecificCache();
        String fileName = cache.getCacheEntryFilename("tenant2");
        assertEquals(fileName, "target/test-classes/testdata/config/tenant2/bulkdata.json");
        Map<String, String> props = cache.getCachedObjectForTenant("tenant2");
        assertNotNull(props);
    }

    @Test
    public void testBulkDataTenantSpecificCacheTenant3() throws Exception {
        BulkDataTenantSpecificCache cache = new BulkDataTenantSpecificCache();
        String fileName = cache.getCacheEntryFilename("tenant3");
        assertEquals(fileName, "target/test-classes/testdata/config/tenant3/bulkdata.json");
        Map<String, String> props = cache.getCachedObjectForTenant("tenant3");
        assertNotNull(props);
    }

    @Test
    public void testBulkDataTenantSpecificCacheDefault() throws Exception {
        BulkDataTenantSpecificCache cache = new BulkDataTenantSpecificCache();
        String fileName = cache.getCacheEntryFilename("default");
        assertEquals(fileName, "target/test-classes/testdata/config/default/bulkdata.json");
        Map<String, String> props = cache.getCachedObjectForTenant("default");
        assertEquals(props.size(), 18);
    }

    @Test
    public void testBulkDataTenant() {
        BulkDataTenantSpecificCache cache = new BulkDataTenantSpecificCache();
        ExportImportBulkData eibd = BulkDataFactory.getTenantInstance(cache);
        assertNotNull(eibd);
    }

    @Test
    public void testBulkDataTenantDummy() throws FHIRException {
        BulkDataTenantSpecificCache cache = new BulkDataTenantSpecificCache();
        FHIRRequestContext.set(new FHIRRequestContext("dummy"));
        ExportImportBulkData eibd = BulkDataFactory.getTenantInstance(cache);
        assertNotNull(eibd);
        assertTrue(eibd instanceof DummyImportExportImpl);
    }

    @Test
    public void testBulkDataTenantSpecificCacheDefaultNotFound() throws Exception {
        BulkDataTenantSpecificCache cache = new BulkDataTenantSpecificCache();
        String fileName = cache.getCacheEntryFilename("french");
        assertEquals(fileName, "target/test-classes/testdata/config/french/bulkdata.json");
        Map<String,String> props = cache.getCachedObjectForTenant("french");
        assertNull(props);
    }

    @Test
    public void testBulkDataConfigUtilInstance() {
        assertNotNull(BulkDataConfigUtil.getInstance());
    }

    @Test
    public void testBulkDataConfigUtilPopulateConfiguration() {
        Map<String, String> props = BulkDataConfigUtil.populateConfiguration(new File("BAD_FILE"));
        assertNotNull(props);
        assertTrue(props.isEmpty());
    }
}
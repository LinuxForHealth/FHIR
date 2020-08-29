/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.config.test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;

public class FHIRConfigHelperTest {

    String[] array1 = { "token1", "token2" };
    List<String> expectedList1 = Arrays.asList(array1);

    String[] array2 = { "token3", "token4" };
    List<String> expectedList2 = Arrays.asList(array2);

    // String used to write out json config file on the fly.
    String tenant3ConfigTemplate =
            "{ \"fhirServer\": { \"property1\": \"__A__\", \"property2\": \"__B__\" }}";

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
    }

    @BeforeMethod
    @AfterMethod
    public void clearThreadLocal() {
        FHIRRequestContext.remove();
    }

    @Test
    public void testDefaultConfig1() throws Exception {
        String tenant = FHIRConfigHelper.getStringProperty("collection/tenant", null);
        assertNotNull(tenant);
        assertEquals("default", tenant);

        List<String> l;
        String s;
        Boolean b;
        Integer i;
        Double d;
        s = FHIRConfigHelper.getStringProperty("collection/groupA/stringProp1", null);
        assertNotNull(s);
        assertEquals("defaultValue1", s);

        s = FHIRConfigHelper.getStringProperty("collection/groupA/stringProp2", null);
        assertNotNull(s);
        assertEquals("defaultValue2", s);

        b = FHIRConfigHelper.getBooleanProperty("collection/groupB/boolProp1", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        b = FHIRConfigHelper.getBooleanProperty("collection/groupB/boolProp2", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        l = FHIRConfigHelper.getStringListProperty("collection/groupB/stringList1");
        assertNotNull(l);
        assertEquals(expectedList1, l);

        s = FHIRConfigHelper.getStringProperty("collection/groupB/boolProp1", null);
        assertNotNull(s);
        assertEquals("false", s);

        i = FHIRConfigHelper.getIntProperty("collection/groupC/intProp1", null);
        assertNotNull(i);
        assertEquals(12345, i.intValue());

        i = FHIRConfigHelper.getIntProperty("collection/groupC/intProp2", null);
        assertNotNull(i);
        assertEquals(12345, i.intValue());

        d = FHIRConfigHelper.getDoubleProperty("collection/groupC/doubleProp2", null);
        assertNotNull(d);
        assertEquals(12345.001, d.doubleValue());
    }

    @Test
    public void testTenant1() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));

        String tenant = FHIRConfigHelper.getStringProperty("collection/tenant", null);
        assertNotNull(tenant);
        assertEquals("tenant1", tenant);

        List<String> l;
        String s;
        Boolean b;
        s = FHIRConfigHelper.getStringProperty("collection/groupA/stringProp1", null);
        assertNotNull(s);
        assertEquals("tenant1Value1", s);

        s = FHIRConfigHelper.getStringProperty("collection/groupA/stringProp2", null);
        assertNotNull(s);
        assertEquals("defaultValue2", s);

        b = FHIRConfigHelper.getBooleanProperty("collection/groupB/boolProp1", null);
        assertNotNull(b);
        assertEquals(Boolean.TRUE, b);

        b = FHIRConfigHelper.getBooleanProperty("collection/groupB/boolProp2", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        l = FHIRConfigHelper.getStringListProperty("collection/groupB/stringList1");
        assertNotNull(l);
        assertEquals(expectedList2, l);
    }

    @Test
    public void testTenant2() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant2"));

        String tenant = FHIRConfigHelper.getStringProperty("collection/tenant", null);
        assertNotNull(tenant);
        assertEquals("tenant2", tenant);

        List<String> l;
        String s;
        Boolean b;
        s = FHIRConfigHelper.getStringProperty("collection/groupA/stringProp1", null);
        assertNotNull(s);
        assertEquals("defaultValue1", s);

        s = FHIRConfigHelper.getStringProperty("collection/groupA/stringProp2", null);
        assertNotNull(s);
        assertEquals("tenant2Value2", s);

        b = FHIRConfigHelper.getBooleanProperty("collection/groupB/boolProp1", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        b = FHIRConfigHelper.getBooleanProperty("collection/groupB/boolProp2", null);
        assertNotNull(b);
        assertEquals(Boolean.TRUE, b);

        l = FHIRConfigHelper.getStringListProperty("collection/groupB/stringList1");
        assertNotNull(l);
        assertEquals(0, l.size());
    }

    @Test
    public void testTenant3() throws Exception {
        // Create initial version of tenant3's config file.
        String fname = "target/test-classes/config/tenant3/fhir-server-config.json";
        PrintWriter pw = new PrintWriter(fname);
        String jsonString =
                tenant3ConfigTemplate.replaceAll("__A__", "property1Value1").replaceAll("__B__", "property2Value1");

        pw.println(jsonString);
        pw.close();

        // Set our thread local to "tenant3"
        FHIRRequestContext.set(new FHIRRequestContext("tenant3"));

        // Load tenant3's config and check the initial property values.
        String s = FHIRConfigHelper.getStringProperty("fhirServer/property1", null);
        assertNotNull(s);
        assertEquals("property1Value1", s);

        s = FHIRConfigHelper.getStringProperty("fhirServer/property2", null);
        assertNotNull(s);
        assertEquals("property2Value1", s);

        // Sleep for just a bit to make sure the updated config file has a newer timestamp.
        Thread.sleep(1000);

        // Next, make changes to the config file on disk and re-check the values.
        pw         = new PrintWriter(fname);
        jsonString =
                tenant3ConfigTemplate.replaceAll("__A__", "property1Value2").replaceAll("__B__", "property2Value2");
        pw.println(jsonString);
        pw.close();

        s = FHIRConfigHelper.getStringProperty("fhirServer/property1", null);
        assertNotNull(s);
        assertEquals("property1Value2", s);

        s = FHIRConfigHelper.getStringProperty("fhirServer/property2", null);
        assertNotNull(s);
        assertEquals("property2Value2", s);
    }

    @Test
    public void testTenant4() throws Exception {
        // "tenant4" does not have a fhir-server-config.json in place, so we SHOULD
        // end up retrieving the property values from the "default" tenant's config file.
        FHIRRequestContext.set(new FHIRRequestContext("tenant4"));

        String tenant = FHIRConfigHelper.getStringProperty("collection/tenant", null);
        assertNotNull(tenant);
        assertEquals("default", tenant);

        List<String> l;
        String s;
        Boolean b;
        s = FHIRConfigHelper.getStringProperty("collection/groupA/stringProp1", null);
        assertNotNull(s);
        assertEquals("defaultValue1", s);

        s = FHIRConfigHelper.getStringProperty("collection/groupA/stringProp2", null);
        assertNotNull(s);
        assertEquals("defaultValue2", s);

        b = FHIRConfigHelper.getBooleanProperty("collection/groupB/boolProp1", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        b = FHIRConfigHelper.getBooleanProperty("collection/groupB/boolProp2", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        l = FHIRConfigHelper.getStringListProperty("collection/groupB/stringList1");
        assertNotNull(l);
        assertEquals(expectedList1, l);

        Double d = FHIRConfigHelper.getDoubleProperty("collection/groupC/doubleProp1", 1.0);
        assertNotNull(d);
        assertEquals(12345.001, d);

        d = FHIRConfigHelper.getDoubleProperty("collection/groupC/doubleProp2", 1.0);
        assertNotNull(d);
        assertEquals(12345.001, d);

        d = FHIRConfigHelper.getDoubleProperty("collection/groupC/doubleProp3", 1.0);
        assertNotNull(d);
        assertEquals(1.0, d);
        
        assertNotNull(FHIRConfiguration.getInstance().loadConfiguration().toString());
        assertFalse(FHIRConfiguration.getInstance().loadConfiguration().toString().isEmpty());
    }

    @Test
    public void testTenant5() throws Exception {
        // "tenant5" contains property groups from default, but includes only new properties
        // within those group.   Make sure we can retrieve those, plus the property values within
        // those groups that exist only in the default config.
        FHIRRequestContext.set(new FHIRRequestContext("tenant5"));

        String tenant = FHIRConfigHelper.getStringProperty("collection/tenant", null);
        assertNotNull(tenant);
        assertEquals("tenant5", tenant);

        List<String> l;
        String s;
        Boolean b;
        s = FHIRConfigHelper.getStringProperty("collection/groupA/newProp1", null);
        assertNotNull(s);
        assertEquals("newValue1", s);

        s = FHIRConfigHelper.getStringProperty("collection/groupA/stringProp1", null);
        assertNotNull(s);
        assertEquals("defaultValue1", s);

        s = FHIRConfigHelper.getStringProperty("collection/groupA/stringProp2", null);
        assertNotNull(s);
        assertEquals("defaultValue2", s);

        b = FHIRConfigHelper.getBooleanProperty("collection/groupB/boolProp1", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        b = FHIRConfigHelper.getBooleanProperty("collection/groupB/boolProp2", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        b = FHIRConfigHelper.getBooleanProperty("collection/groupB/newBoolProp1", null);
        assertNotNull(b);
        assertEquals(Boolean.TRUE, b);

        l = FHIRConfigHelper.getStringListProperty("collection/groupB/stringList1");
        assertNotNull(l);
        assertEquals(expectedList1, l);
    }

    @Test
    public void testGetConfiguredTenants() {
        List<String> tenants = FHIRConfiguration.getInstance().getConfiguredTenants();
        assertNotNull(tenants);
        assertEquals(5, tenants.size());
        assertTrue(tenants.contains("default"));
        assertTrue(tenants.contains("tenant1"));
        assertTrue(tenants.contains("tenant2"));
        assertTrue(tenants.contains("tenant3"));
        assertTrue(tenants.contains("tenant5"));
    }

    /**
     * Make sure we can can find the default definitions
     */
    @Test
    public void testDefaultDatasourceLookup() throws Exception {
        // Without a tenant set in the request context, we should be able to reach
        // the default datastore configuration
        final String datastoreId = "default";
        String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + datastoreId;
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
        assertNotNull(dsPG);
        String type = dsPG.getStringProperty("type");
        assertEquals("db2", type);
    }

    /**
     * Make sure we can can find the datastore "not_default" defined for tenant1
     */
    @Test
    public void testDatasourceLookup() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));
        final String datastoreId = "not_default";
        String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + datastoreId;
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
        assertNotNull(dsPG);
        String type = dsPG.getStringProperty("type");
        assertEquals("derby", type);
    }

    /**
     * Make sure that for datasource lookups, we don't fall back
     * to using "default" datasources for tenant-specific requests.
     * A tenant must only be able to access data-sources it has
     * explicitly configured. See issue 639.
     */
    @Test
    public void testDatasourceNoFallback() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));
        final String datastoreId = "default";
        String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + datastoreId;
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
        assertNull(dsPG);
    }
}

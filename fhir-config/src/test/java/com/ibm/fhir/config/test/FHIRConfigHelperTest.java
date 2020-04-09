/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.config.test;

import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;

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

    @Test
    public void testDefaultConfig1() throws Exception {
        String tenant = "default";

        List<String> l;
        String s;
        Boolean b;
        Integer i;
        Double d;
        s = FHIRConfigHelper.getStringProperty(tenant, "collection/groupA/stringProp1", null);
        assertNotNull(s);
        assertEquals("defaultValue1", s);

        s = FHIRConfigHelper.getStringProperty(tenant, "collection/groupA/stringProp2", null);
        assertNotNull(s);
        assertEquals("defaultValue2", s);

        b = FHIRConfigHelper.getBooleanProperty(tenant, "collection/groupB/boolProp1", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        b = FHIRConfigHelper.getBooleanProperty(tenant, "collection/groupB/boolProp2", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        l = FHIRConfigHelper.getStringListProperty(tenant, "collection/groupB/stringList1");
        assertNotNull(l);
        assertEquals(expectedList1, l);

        s = FHIRConfigHelper.getStringProperty(tenant, "collection/groupB/boolProp1", null);
        assertNotNull(s);
        assertEquals("false", s);

        i = FHIRConfigHelper.getIntProperty(tenant, "collection/groupC/intProp1", null);
        assertNotNull(i);
        assertEquals(12345, i.intValue());

        i = FHIRConfigHelper.getIntProperty(tenant, "collection/groupC/intProp2", null);
        assertNotNull(i);
        assertEquals(12345, i.intValue());

        d = FHIRConfigHelper.getDoubleProperty(tenant, "collection/groupC/doubleProp2", null);
        assertNotNull(d);
        assertEquals(12345.001, d.doubleValue());
    }

    @Test
    public void testTenant1() throws Exception {
        String tenant = "tenant1";

        List<String> l;
        String s;
        Boolean b;
        s = FHIRConfigHelper.getStringProperty(tenant, "collection/groupA/stringProp1", null);
        assertNotNull(s);
        assertEquals("tenant1Value1", s);

        s = FHIRConfigHelper.getStringProperty(tenant, "collection/groupA/stringProp2", null);
        assertNotNull(s);
        assertEquals("defaultValue2", s);

        b = FHIRConfigHelper.getBooleanProperty(tenant, "collection/groupB/boolProp1", null);
        assertNotNull(b);
        assertEquals(Boolean.TRUE, b);

        b = FHIRConfigHelper.getBooleanProperty(tenant, "collection/groupB/boolProp2", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        l = FHIRConfigHelper.getStringListProperty(tenant, "collection/groupB/stringList1");
        assertNotNull(l);
        assertEquals(expectedList2, l);
    }

    @Test
    public void testTenant2() throws Exception {
        String tenant = "tenant2";

        List<String> l;
        String s;
        Boolean b;
        s = FHIRConfigHelper.getStringProperty(tenant, "collection/groupA/stringProp1", null);
        assertNotNull(s);
        assertEquals("defaultValue1", s);

        s = FHIRConfigHelper.getStringProperty(tenant, "collection/groupA/stringProp2", null);
        assertNotNull(s);
        assertEquals("tenant2Value2", s);

        b = FHIRConfigHelper.getBooleanProperty(tenant, "collection/groupB/boolProp1", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        b = FHIRConfigHelper.getBooleanProperty(tenant, "collection/groupB/boolProp2", null);
        assertNotNull(b);
        assertEquals(Boolean.TRUE, b);

        l = FHIRConfigHelper.getStringListProperty(tenant, "collection/groupB/stringList1");
        assertNotNull(l);
        assertEquals(0, l.size());
    }

    @Test
    public void testTenant3() throws Exception {
        String tenant = "tenant3";

        // Create initial version of tenant3's config file.
        String fname = "target/test-classes/config/tenant3/fhir-server-config.json";
        PrintWriter pw = new PrintWriter(fname);
        String jsonString =
                tenant3ConfigTemplate.replaceAll("__A__", "property1Value1").replaceAll("__B__", "property2Value1");

        pw.println(jsonString);
        pw.close();

        // Load tenant3's config and check the initial property values.
        String s = FHIRConfigHelper.getStringProperty(tenant, "fhirServer/property1", null);
        assertNotNull(s);
        assertEquals("property1Value1", s);

        s = FHIRConfigHelper.getStringProperty(tenant, "fhirServer/property2", null);
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

        s = FHIRConfigHelper.getStringProperty(tenant, "fhirServer/property1", null);
        assertNotNull(s);
        assertEquals("property1Value2", s);

        s = FHIRConfigHelper.getStringProperty(tenant, "fhirServer/property2", null);
        assertNotNull(s);
        assertEquals("property2Value2", s);
    }

    @Test
    public void testTenant4() throws Exception {
        String tenant = "tenant4";

        // "tenant4" does not have a fhir-server-config.json in place, so we SHOULD
        // end up retrieving the property values from the "default" tenant's config file.

        List<String> l;
        String s;
        Boolean b;
        s = FHIRConfigHelper.getStringProperty(tenant, "collection/groupA/stringProp1", null);
        assertNotNull(s);
        assertEquals("defaultValue1", s);

        s = FHIRConfigHelper.getStringProperty(tenant, "collection/groupA/stringProp2", null);
        assertNotNull(s);
        assertEquals("defaultValue2", s);

        b = FHIRConfigHelper.getBooleanProperty(tenant, "collection/groupB/boolProp1", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        b = FHIRConfigHelper.getBooleanProperty(tenant, "collection/groupB/boolProp2", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        l = FHIRConfigHelper.getStringListProperty(tenant, "collection/groupB/stringList1");
        assertNotNull(l);
        assertEquals(expectedList1, l);

        Double d = FHIRConfigHelper.getDoubleProperty(tenant, "collection/groupC/doubleProp1", 1.0);
        assertNotNull(d);
        assertEquals(12345.001, d);

        d = FHIRConfigHelper.getDoubleProperty(tenant, "collection/groupC/doubleProp2", 1.0);
        assertNotNull(d);
        assertEquals(12345.001, d);

        d = FHIRConfigHelper.getDoubleProperty(tenant, "collection/groupC/doubleProp3", 1.0);
        assertNotNull(d);
        assertEquals(1.0, d);

        assertNotNull(FHIRConfiguration.getInstance().loadConfiguration().toString());
        assertFalse(FHIRConfiguration.getInstance().loadConfiguration().toString().isEmpty());
    }

    @Test
    public void testTenant5() throws Exception {
        String tenant = "tenant5";

        // "tenant5" contains property groups from default, but includes only new properties
        // within those group.   Make sure we can retrieve those, plus the property values within
        // those groups that exist only in the default config.

        List<String> l;
        String s;
        Boolean b;
        s = FHIRConfigHelper.getStringProperty(tenant, "collection/groupA/newProp1", null);
        assertNotNull(s);
        assertEquals("newValue1", s);

        s = FHIRConfigHelper.getStringProperty(tenant, "collection/groupA/stringProp1", null);
        assertNotNull(s);
        assertEquals("defaultValue1", s);

        s = FHIRConfigHelper.getStringProperty(tenant, "collection/groupA/stringProp2", null);
        assertNotNull(s);
        assertEquals("defaultValue2", s);

        b = FHIRConfigHelper.getBooleanProperty(tenant, "collection/groupB/boolProp1", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        b = FHIRConfigHelper.getBooleanProperty(tenant, "collection/groupB/boolProp2", null);
        assertNotNull(b);
        assertEquals(Boolean.FALSE, b);

        b = FHIRConfigHelper.getBooleanProperty(tenant, "collection/groupB/newBoolProp1", null);
        assertNotNull(b);
        assertEquals(Boolean.TRUE, b);

        l = FHIRConfigHelper.getStringListProperty(tenant, "collection/groupB/stringList1");
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
}

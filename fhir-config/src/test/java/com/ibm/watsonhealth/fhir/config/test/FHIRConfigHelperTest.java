/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.config.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;

public class FHIRConfigHelperTest {
    
    String[] array1 = { "token1", "token2"};
    List<String> expectedList1 = Arrays.asList(array1);

    String[] array2 = { "token3", "token4"};
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
    }
    
    @Test
    public void testDefaultConfig2() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("default"));
        
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
        String jsonString = tenant3ConfigTemplate.replaceAll("__A__", "property1Value1").replaceAll("__B__", "property2Value1");
        
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
        pw = new PrintWriter(fname);
        jsonString = tenant3ConfigTemplate.replaceAll("__A__", "property1Value2").replaceAll("__B__", "property2Value2");
        pw.println(jsonString);
        pw.close();
        
        s = FHIRConfigHelper.getStringProperty("fhirServer/property1", null);
        assertNotNull(s);
        assertEquals("property1Value2", s);
        
        s = FHIRConfigHelper.getStringProperty("fhirServer/property2", null);
        assertNotNull(s);
        assertEquals("property2Value2", s);
    }
}

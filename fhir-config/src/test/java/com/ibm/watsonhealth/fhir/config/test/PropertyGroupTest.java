/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.config.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.fail;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.config.PropertyGroup;
import com.ibm.watsonhealth.fhir.config.PropertyGroup.PropertyEntry;

public class PropertyGroupTest {
    private JsonObject jsonObj1 = null;
    private JsonObject jsonObj2 = null;

    @BeforeClass
    public void setup() {

        // Build a JSON object for testing.
        jsonObj1 = Json.createObjectBuilder()
                .add("level1", Json.createObjectBuilder()
                    .add("level2", Json.createObjectBuilder()
                        .add("level3", Json.createObjectBuilder()
                            .add("stringProp", "stringValue")
                            .add("intProp", 123)
                            .add("booleanProp", true))))
                .build();

        // Simulate a snippet of the fhir-server config.
        jsonObj2 = Json.createObjectBuilder()
                .add("fhir-server", Json.createObjectBuilder()
                    .add("server-core", Json.createObjectBuilder()
                        .add("truststoreLocation", "XYZ")
                        .add("truststorePassword", "password"))
                    .add("notifications", Json.createObjectBuilder()
                        .add("common", Json.createObjectBuilder()
                            .add("includeResourceTypes", Json.createArrayBuilder()
                                .add("Patient")
                                .add("Observation")))
                        .add("kafka", Json.createObjectBuilder()
                            .add("enabled", true)
                            .add("connectionProperties", Json.createObjectBuilder()
                                .add("groupId", "group1")
                                .add("bootstrap.servers", "localhost:1234")
CODE_REMOVED
                            )
                        )
                    .add("object-array", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                            .add("url", "http://localhost")
                            .add("type", "insecure"))
                        .add(Json.createObjectBuilder()
                            .add("url", "https://localhost")
                            .add("type", "secure"))))
                .build();

    }

    @Test
    public void testGetPropertyGroup() throws Exception {
        PropertyGroup pg = new PropertyGroup(jsonObj1);

        PropertyGroup level3 = pg.getPropertyGroup("level1/level2/level3");
        assertNotNull(level3);
        PropertyGroup result = level3.getPropertyGroup("level3");
        assertNull(result);
        String value = level3.getStringProperty("stringProp");
        assertNotNull(value);
        assertEquals("stringValue", value);
    }

    @Test
    public void testStringProperty() throws Exception {
        PropertyGroup pg = new PropertyGroup(jsonObj1);
        String value = pg.getStringProperty("level1/level2/level3/stringProp");
        assertNotNull(value);
        assertEquals("stringValue", value);
    }

    @Test
    public void testIntProperty() {
        PropertyGroup pg = new PropertyGroup(jsonObj1);
        Integer value = pg.getIntProperty("level1/level2/level3/intProp");
        assertNotNull(value);
        assertEquals(123, value.intValue());
    }

    @Test
    public void testBooleanProperty() {
        PropertyGroup pg = new PropertyGroup(jsonObj1);
        Boolean value = pg.getBooleanProperty("level1/level2/level3/booleanProp");
        assertNotNull(value);
        assertEquals(Boolean.TRUE, value);
    }

    @Test
    public void testArrayProperty() throws Exception {
        PropertyGroup pg = new PropertyGroup(jsonObj2);
        Object[] array = pg.getArrayProperty("fhir-server/notifications/common/includeResourceTypes");
        assertNotNull(array);
        assertEquals(2, array.length);
        assertEquals("Patient", (String) array[0]);
        assertEquals("Observation", (String) array[1]);
    }

    @Test
    public void testObjectArrayProperty() throws Exception {
        PropertyGroup pg = new PropertyGroup(jsonObj2);
        Object[] array = pg.getArrayProperty("fhir-server/object-array/");
        assertNotNull(array);
        assertEquals(2, array.length);
        if (!(array[0] instanceof PropertyGroup)) {
            fail("array element 0 not a PropertyGroup!");
        }
        if (!(array[1] instanceof PropertyGroup)) {
            fail("array element 1 not a PropertyGroup!");
        }
        
        // Check the first element.
        PropertyGroup pg0 = (PropertyGroup) array[0];
        String url = pg0.getStringProperty("url");
        assertNotNull(url);
        assertEquals("http://localhost", url);
        String type = pg0.getStringProperty("type");
        assertNotNull(type);
        assertEquals("insecure", type);
        
        // Check the second element.
        PropertyGroup pg1 = (PropertyGroup) array[1];
        url = pg1.getStringProperty("url");
        assertNotNull(url);
        assertEquals("https://localhost", url);
        type = pg1.getStringProperty("type");
        assertNotNull(type);
        assertEquals("secure", type);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testStringPropertyException() throws Exception {
        PropertyGroup pg = new PropertyGroup(jsonObj1);
        pg.getStringProperty("level1/level2/level3/intProp");
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testIntPropertyException() {
        PropertyGroup pg = new PropertyGroup(jsonObj1);
        pg.getIntProperty("level1/level2/level3/stringProp");
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBooleanPropertyException() {
        PropertyGroup pg = new PropertyGroup(jsonObj1);
        pg.getBooleanProperty("level1/level2/level3/intProp");
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testArrayPropertyException() throws Exception {
        PropertyGroup pg = new PropertyGroup(jsonObj2);
        pg.getArrayProperty("fhir-server/notifications/kafka");
    }
    
    @Test
    public void testGetProperties() throws Exception {
        PropertyGroup pg = new PropertyGroup(jsonObj2);
        PropertyGroup connectionProps = pg.getPropertyGroup("fhir-server/notifications/kafka/connectionProperties");
        assertNotNull(connectionProps);
        
        List<PropertyEntry> properties = connectionProps.getProperties();
        assertNotNull(properties);
        assertEquals(3, properties.size());
        
        PropertyEntry propEntry = properties.get(0);
        assertNotNull(propEntry);
        assertEquals("groupId", propEntry.getName());
        assertEquals("group1", (String) propEntry.getValue());
        
        propEntry = properties.get(1);
        assertNotNull(propEntry);
        assertEquals("bootstrap.servers", propEntry.getName());
        assertEquals("localhost:1234", (String) propEntry.getValue());
        
        propEntry = properties.get(2);
        assertNotNull(propEntry);
        assertEquals("password", propEntry.getName());
        assertEquals("password", (String) propEntry.getValue());
    }
}

/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.config.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.PropertyGroup;
import org.linuxforhealth.fhir.config.PropertyGroup.PropertyEntry;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

public class PropertyGroupTest {
    private static final JsonBuilderFactory BUILDER_FACTORY = Json.createBuilderFactory(null);
    private JsonObject jsonObj1 = null;
    private JsonObject jsonObj2 = null;

    private boolean DEBUG = true;

    @BeforeClass
    public void setup() {
        // Build a JSON object for testing.
        jsonObj1 =
                BUILDER_FACTORY.createObjectBuilder()
                        .add("level1", BUILDER_FACTORY.createObjectBuilder()
                                .add("level2", BUILDER_FACTORY.createObjectBuilder()
                                        .add("level3", BUILDER_FACTORY.createObjectBuilder()
                                                .add("stringProp", "stringValue")
                                                .add("intProp", 123)
                                                .add("booleanProp", true)
                                                .add("booleanProp-2", "true"))))
                        .build();

        // Simulate a snippet of the fhir-server config.
        jsonObj2 =
                BUILDER_FACTORY.createObjectBuilder()
                        .add("fhir-server", BUILDER_FACTORY.createObjectBuilder()
                                .add("server-core", BUILDER_FACTORY.createObjectBuilder()
                                        .add("truststoreLocation", "XYZ")
                                        .add("truststorePassword", "change-password"))
                                .add("notifications", BUILDER_FACTORY.createObjectBuilder()
                                        .add("common", BUILDER_FACTORY.createObjectBuilder()
                                                .add("includeResourceTypes", BUILDER_FACTORY.createArrayBuilder()
                                                        .add("Patient")
                                                        .add("Observation")))
                                        .add("kafka", BUILDER_FACTORY.createObjectBuilder()
                                                .add("enabled", true)
                                                .add("connectionProperties", BUILDER_FACTORY.createObjectBuilder()
                                                        .add("groupId", "group1")
                                                        .add("bootstrap.servers", "localhost:1234")
                                                        .add("change-password", "change-password"))))
                                .add("object-array", BUILDER_FACTORY.createArrayBuilder()
                                        .add(BUILDER_FACTORY.createObjectBuilder()
                                                .add("url", "http://localhost")
                                                .add("type", "insecure"))
                                        .add(BUILDER_FACTORY.createObjectBuilder()
                                                .add("url", "https://localhost")
                                                .add("type", "secure")))
                                .add("int-array", BUILDER_FACTORY.createArrayBuilder()
                                        .add(1)
                                        .add(2)
                                        .add(3))
                                .add("null-value", JsonValue.NULL)
                                )
                        .build();

        if (DEBUG) {
            System.out.println("\njsonObj1 contents:\n" + jsonObj1.toString());
            System.out.println("\njsonObj2 contents:\n" + jsonObj2.toString());
        }
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

        value = pg.getBooleanProperty("level1/level2/level3/booleanProp-2");
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
    public void testStringListProperty() throws Exception {
        PropertyGroup pg = new PropertyGroup(jsonObj2);
        List<String> strings = pg.getStringListProperty("fhir-server/notifications/common/includeResourceTypes");
        assertNotNull(strings);
        assertEquals(2, strings.size());
        assertEquals("Patient", strings.get(0));
        assertEquals("Observation", strings.get(1));

        pg      = new PropertyGroup(jsonObj2);
        strings = pg.getStringListProperty("fhir-server/int-array");
        assertNotNull(strings);
        assertEquals(3, strings.size());
        assertEquals("1", strings.get(0));
        assertEquals("2", strings.get(1));
        assertEquals("3", strings.get(2));
    }

    @Test
    public void testObjectArrayProperty() throws Exception {
        PropertyGroup pg = new PropertyGroup(jsonObj2);
        Object[] array = pg.getArrayProperty("fhir-server/object-array");
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

    @Test
    public void testNullProperty() throws Exception {
        PropertyGroup pg = new PropertyGroup(jsonObj2);
        assertNull(pg.getJsonValue("fhir-server/null-value"));
        assertNull(pg.getStringProperty("fhir-server/null-value"));
        assertNull(pg.getBooleanProperty("fhir-server/null-value"));
        assertNull(pg.getIntProperty("fhir-server/null-value"));
        assertNull(pg.getDoubleProperty("fhir-server/null-value"));
        assertNull(pg.getStringListProperty("fhir-server/null-value"));
    }

    @Test
    public void testNonExistentProperty() throws Exception {
        PropertyGroup pg = new PropertyGroup(jsonObj2);
        assertNull(pg.getJsonValue("fhir-server/bogus"));
        assertNull(pg.getStringProperty("fhir-server/bogus"));
        assertNull(pg.getBooleanProperty("fhir-server/bogus"));
        assertNull(pg.getIntProperty("fhir-server/bogus"));
        assertNull(pg.getDoubleProperty("fhir-server/bogus"));
        assertNull(pg.getStringListProperty("fhir-server/bogus"));
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
    public void testGetProperties1() throws Exception {
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
        assertEquals("change-password", propEntry.getName());
        assertEquals("change-password", (String) propEntry.getValue());
    }

    @Test
    public void testGetProperties2() throws Exception {
        PropertyGroup rootPG = new PropertyGroup(jsonObj2);
        PropertyGroup fhirServerPG = rootPG.getPropertyGroup("fhir-server");
        assertNotNull(fhirServerPG);

        List<PropertyEntry> properties = fhirServerPG.getProperties();
        assertNotNull(properties);
        assertEquals(4, properties.size());

        PropertyEntry propEntry = properties.get(0);
        assertNotNull(propEntry);
        assertEquals("server-core", propEntry.getName());
        assertTrue(propEntry.getValue() instanceof PropertyGroup);

        propEntry = properties.get(1);
        assertNotNull(propEntry);
        assertEquals("notifications", propEntry.getName());
        assertTrue(propEntry.getValue() instanceof PropertyGroup);

        propEntry = properties.get(2);
        assertNotNull(propEntry);
        assertEquals("object-array", propEntry.getName());
        assertTrue(propEntry.getValue() instanceof List);
        for (Object obj : (List<?>) propEntry.getValue()) {
            assertNotNull(obj);
            assertTrue(obj instanceof PropertyGroup);
        }

        propEntry = properties.get(3);
        assertNotNull(propEntry);
        assertEquals("int-array", propEntry.getName());
        assertTrue(propEntry.getValue() instanceof List);
        for (Object obj : (List<?>) propEntry.getValue()) {
            assertNotNull(obj);
            assertTrue(obj instanceof Integer);
        }
    }

    @Test
    public void testGetProperties3() throws Exception {
        PropertyGroup rootPG = new PropertyGroup(jsonObj2);
        PropertyGroup pg = rootPG.getPropertyGroup("fhir-server/notifications/common");
        assertNotNull(pg);

        List<PropertyEntry> properties = pg.getProperties();
        assertNotNull(properties);
        assertEquals(1, properties.size());

        PropertyEntry propEntry = properties.get(0);
        assertNotNull(propEntry);
        assertEquals("includeResourceTypes", propEntry.getName());
        assertTrue(propEntry.getValue() instanceof List);
        assertEquals(2, ((List<?>) propEntry.getValue()).size());
        for (Object obj : (List<?>) propEntry.getValue()) {
            assertNotNull(obj);
            assertTrue(obj instanceof String);
        }
    }

    @Test
    public void testGetPropertiesWithNull() throws Exception {
        PropertyGroup rootPG = new PropertyGroup(jsonObj2);
        PropertyGroup pg = rootPG.getPropertyGroup("fhir-server/notifications/common");
        assertNotNull(pg);

        List<PropertyEntry> properties = pg.getProperties();
        assertNotNull(properties);
        assertEquals(1, properties.size());

        PropertyEntry propEntry = properties.get(0);
        assertNotNull(propEntry);
        assertEquals("includeResourceTypes", propEntry.getName());
        assertTrue(propEntry.getValue() instanceof List);
        assertEquals(2, ((List<?>) propEntry.getValue()).size());
        for (Object obj : (List<?>) propEntry.getValue()) {
            assertNotNull(obj);
            assertTrue(obj instanceof String);
        }
    }
}

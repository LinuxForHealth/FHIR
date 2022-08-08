/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.config.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.mock.MockPropertyGroup;

public class MockPropertyGroupTest {
    private static final JsonBuilderFactory BUILDER_FACTORY = Json.createBuilderFactory(null);
    private JsonObject jsonObj1 = null;

    @BeforeClass
    public void setup() {

        // Build a JSON object for testing.
        jsonObj1 = BUILDER_FACTORY.createObjectBuilder()
                .add("level1", BUILDER_FACTORY.createObjectBuilder()
                    .add("level2", BUILDER_FACTORY.createObjectBuilder()
                        .add("level3", BUILDER_FACTORY.createObjectBuilder()
                            .add("stringProp", "stringValue")
                            .add("intProp", 123)
                            .add("booleanProp", true))))
                .build();

    }

    @Test
    public void testGetPropertyGroup() throws Exception {
        MockPropertyGroup pg = new MockPropertyGroup(jsonObj1);

        MockPropertyGroup level3 = pg.getPropertyGroup("level1/level2/level3");
        assertNotNull(level3);
        MockPropertyGroup result = level3.getPropertyGroup("level3");
        assertNull(result);
        String value = level3.getStringProperty("stringProp");
        assertNotNull(value);
        assertEquals("stringValue", value);
    }

    @Test
    public void testStringProperty() throws Exception {
        MockPropertyGroup pg = new MockPropertyGroup(jsonObj1);
        String value = pg.getStringProperty("level1/level2/level3/stringProp");
        assertNotNull(value);
        assertEquals("stringValue", value);
    }
    
    @Test
    public void testSetStringProperty1() throws Exception {
        MockPropertyGroup pg = new MockPropertyGroup(jsonObj1);
        pg.setProperty("newField", "newValue");
        String value = pg.getStringProperty("newField");
        assertNotNull(value);
        assertEquals("newValue", value);
    }
    
    @Test
    public void testSetStringProperty2() throws Exception {
        MockPropertyGroup pg = new MockPropertyGroup(jsonObj1);
        pg.setProperty("level1/level2/level3/stringProp", "newValue");
        String value = pg.getStringProperty("level1/level2/level3/stringProp");
        assertNotNull(value);
        assertEquals("newValue", value);
    }
    
    @Test
    public void testSetBooleanProperty() throws Exception {
        MockPropertyGroup pg = new MockPropertyGroup(jsonObj1);
        pg.setProperty("level1/level2/enabled", Boolean.TRUE);
        Boolean value = pg.getBooleanProperty("level1/level2/enabled");
        assertNotNull(value);
        assertEquals(Boolean.TRUE, value);
        
        pg.setProperty("level1/level2/enabled", Boolean.FALSE);
        value = pg.getBooleanProperty("level1/level2/enabled");
        assertNotNull(value);
        assertEquals(Boolean.FALSE, value);
    }
    
    @Test
    public void testSetIntegerProperty() throws Exception {
        MockPropertyGroup pg = new MockPropertyGroup(jsonObj1);
        pg.setProperty("level1/level2/level3/intProp", Integer.valueOf(12345));
        Integer value = pg.getIntProperty("level1/level2/level3/intProp");
        assertNotNull(value);
        assertEquals(Integer.valueOf(12345), value);
        
        pg.setProperty("level1/level2/newIntProp", Integer.valueOf(382636));
        value = pg.getIntProperty("level1/level2/newIntProp");
        assertNotNull(value);
        assertEquals(Integer.valueOf(382636), value);
    }
}

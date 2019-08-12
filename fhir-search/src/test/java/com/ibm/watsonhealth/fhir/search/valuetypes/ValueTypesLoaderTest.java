/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.valuetypes;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.resource.Basic;
import com.ibm.watsonhealth.fhir.model.resource.Observation;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.valuetypes.impl.ValueTypesR4Impl;

/**
 * Loads the ValueTypes and Tests with specific jsons.
 * 
 * @author pbastide
 *
 */
public class ValueTypesLoaderTest {

    protected static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    @Test
    public void testConvertToClassesValid() throws IOException {
        try (InputStream stream = ValueTypesR4Impl.class.getResourceAsStream("/testdata/valuetypes-loader/targetClasses-valid.json");
                JsonReader jsonReader = JSON_READER_FACTORY.createReader(stream)) {
            JsonObject jsonObject = jsonReader.readObject();
            JsonArray arr = jsonObject.getJsonArray("targetClasses");
            ValueTypesR4Impl impl = new ValueTypesR4Impl();
            Set<Class<?>> clzs = impl.convertToClasses(arr);

            assertEquals(clzs.size(), 2);
            assertTrue(clzs.contains(Observation.class));
            assertTrue(clzs.contains(Basic.class));
        }
    }

    @Test
    public void testConvertToClassesInvalid() throws IOException {
        try (InputStream stream = ValueTypesR4Impl.class.getResourceAsStream("/testdata/valuetypes-loader/targetClasses-invalid.json");
                JsonReader jsonReader = JSON_READER_FACTORY.createReader(stream)) {
            JsonObject jsonObject = jsonReader.readObject();
            JsonArray arr = jsonObject.getJsonArray("targetClasses");
            ValueTypesR4Impl impl = new ValueTypesR4Impl();
            Set<Class<?>> clzs = impl.convertToClasses(arr);

            assertEquals(clzs.size(), 0);
        }
    }

    @Test
    public void testConvertToClassesTypesAndResources() throws IOException {
        try (InputStream stream = ValueTypesR4Impl.class.getResourceAsStream("/testdata/valuetypes-loader/targetClasses-valid-type.json");
                JsonReader jsonReader = JSON_READER_FACTORY.createReader(stream)) {
            JsonObject jsonObject = jsonReader.readObject();
            JsonArray arr = jsonObject.getJsonArray("targetClasses");
            ValueTypesR4Impl impl = new ValueTypesR4Impl();
            Set<Class<?>> clzs = impl.convertToClasses(arr);
            assertEquals(clzs.size(), 3);
        }
    }

    @Test
    public void testLoadNullStream() {
        ValueTypesR4Impl impl = new ValueTypesR4Impl();
        Map<String, Set<Class<?>>> tmp = impl.load(null);
        assertNotNull(tmp);
        assertEquals(tmp.size(), 0);

    }

    @Test
    public void testEmptyValid() throws IOException {
        String file = "empty-valid.json";
        try (InputStream stream = ValueTypesR4Impl.class.getResourceAsStream("/testdata/valuetypes-loader/" + file);) {
            ValueTypesR4Impl impl = new ValueTypesR4Impl();
            Map<String, Set<Class<?>>> tmp = impl.load(stream);
            assertTrue(tmp.isEmpty());
        }
    }

    @Test
    public void testValidEmptyTargetClasses() throws IOException {
        String file = "empty-target-valid.json";
        try (InputStream stream = ValueTypesR4Impl.class.getResourceAsStream("/testdata/valuetypes-loader/" + file);) {
            ValueTypesR4Impl impl = new ValueTypesR4Impl();
            Map<String, Set<Class<?>>> tmp = impl.load(stream);
            assertTrue(!tmp.isEmpty());
            assertTrue(tmp.entrySet().iterator().next().getValue().isEmpty());
        }
    }

    @Test
    public void testValidTargetClasses() throws IOException {
        String file = "valid-with-target.json";
        try (InputStream stream = ValueTypesR4Impl.class.getResourceAsStream("/testdata/valuetypes-loader/" + file);) {
            ValueTypesR4Impl impl = new ValueTypesR4Impl();
            Map<String, Set<Class<?>>> tmp = impl.load(stream);
            assertTrue(!tmp.isEmpty());

            assertTrue(!tmp.entrySet().iterator().next().getValue().isEmpty());
        }
    }

    @Test
    public void testHash() {
        assertEquals(ValueTypesR4Impl.hash(Observation.class, "test"), "Observation.test");
        assertEquals(ValueTypesR4Impl.hash("Observation", "test"), "Observation.test");
    }

    @Test
    public void testInitWithValidName() throws FHIRSearchException {
        ValueTypesR4Impl impl = new ValueTypesR4Impl();

        Set<Class<?>> vals = impl.getValueTypes(Observation.class, "combo-code");
        assertNotNull(vals);
        assertFalse(vals.isEmpty());

    }

    @Test
    public void testInitWithInvalidName() throws FHIRSearchException {
        ValueTypesR4Impl impl = new ValueTypesR4Impl();

        Set<Class<?>> vals = impl.getValueTypes(Observation.class, "combo-code-no-way-this-exists");
        assertNotNull(vals);
        assertTrue(vals.isEmpty());

    }

    @Test
    public void testNullConditionsForGetValueTypes() throws FHIRSearchException {
        ValueTypesR4Impl impl = new ValueTypesR4Impl();

        Set<Class<?>> vals = impl.getValueTypes(null, "combo-code-no-way-this-exists");
        assertNotNull(vals);
        assertTrue(vals.isEmpty());

        vals = impl.getValueTypes(Observation.class, null);
        assertNotNull(vals);
        assertTrue(vals.isEmpty());
    }

}

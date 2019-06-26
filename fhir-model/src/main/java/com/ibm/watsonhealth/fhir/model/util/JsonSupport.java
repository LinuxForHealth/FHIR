/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

public final class JsonSupport {
    private static final JsonObject JSON_SUPPORT = readJsonSupportObject();
    private static final Map<String, Set<String>> ELEMENT_NAME_MAP = buildElementNameMap(JSON_SUPPORT);
    private static final Map<String, Set<String>> REQUIRED_ELEMENT_NAME_MAP = buildRequiredElementNameMap(JSON_SUPPORT);
    
    private JsonSupport() { }
    
    public static void init() {
        // allows us to initialize this class during startup
    }

    private static JsonObject readJsonSupportObject() {
        try (JsonReader reader = Json.createReader(JsonSupport.class.getClassLoader().getResourceAsStream("json-support.json"))) {
            return reader.readObject();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static Map<String, Set<String>> buildElementNameMap(JsonObject jsonSupport) {
        Map<String, Set<String>> elementNameMap = new LinkedHashMap<>();
        for (String key : jsonSupport.keySet()) {
            JsonObject jsonObject = jsonSupport.get(key).asJsonObject();
            Set<String> elementNames = new LinkedHashSet<>();
            for (JsonValue jsonValue : jsonObject.getJsonArray("elementNames")) {
                JsonString jsonString = (JsonString) jsonValue;
                elementNames.add(jsonString.getString());
            }
            elementNameMap.put(key, Collections.unmodifiableSet(elementNames));
        }
        return Collections.unmodifiableMap(elementNameMap);
    }
    
    private static Map<String, Set<String>> buildRequiredElementNameMap(JsonObject jsonSupport) {
        Map<String, Set<String>> requiredElementNameMap = new LinkedHashMap<>();
        for (String key : jsonSupport.keySet()) {
            JsonObject jsonObject = jsonSupport.get(key).asJsonObject();
            Set<String> requiredElementNames = new LinkedHashSet<>();
            for (JsonValue jsonValue : jsonObject.getJsonArray("requiredElementNames")) {
                JsonString jsonString = (JsonString) jsonValue;
                requiredElementNames.add(jsonString.getString());
            }
            requiredElementNameMap.put(key, Collections.unmodifiableSet(requiredElementNames));
        }
        return Collections.unmodifiableMap(requiredElementNameMap);
    }
    
    public static Set<String> getElementNames(String typeName) {
        return ELEMENT_NAME_MAP.get(typeName);
    }
    
    public static Set<String> getRequiredElementNames(String typeName) {
        return REQUIRED_ELEMENT_NAME_MAP.get(typeName);
    }
    
    public static JsonArray getJsonArray(JsonObject jsonObject, String key) {
        return getJsonValue(jsonObject, key, JsonArray.class);
    }

    public static JsonValue getJsonValue(JsonArray jsonArray, int index) {
        if (jsonArray != null) {
            if (index >= 0 && index < jsonArray.size()) {
                return jsonArray.get(index);
            } else {
                throw new IllegalArgumentException("Could not find element at index: " + index);
            }
        }
        return null;
    }
    
    public static <T extends JsonValue> T getJsonValue(JsonObject jsonObject, String key, Class<T> expectedType) {
        JsonValue jsonValue = jsonObject.get(key);
        if (jsonValue != null && !expectedType.isInstance(jsonValue)) {
            throw new IllegalArgumentException("Expected: " + expectedType.getSimpleName() + " but found: " + jsonValue.getValueType());
        }
        return expectedType.cast(jsonValue);
    }
}

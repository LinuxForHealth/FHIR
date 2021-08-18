/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.config.mock;

import java.io.ByteArrayInputStream;

import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;

import com.google.gson.Gson;
import com.ibm.fhir.config.PropertyGroup;

public class MockPropertyGroup extends PropertyGroup {
    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    public MockPropertyGroup(JsonObject jsonObj) {
        super(jsonObj);
    }
    
    @Override
    public MockPropertyGroup getPropertyGroup(String propertyName) {
        MockPropertyGroup result = null;
        PropertyGroup pg = super.getPropertyGroup(propertyName);
        if (pg != null) {
            result = new MockPropertyGroup(pg.getJsonObj());
        }
        return result;
    }
    
    /**
     * Sets the specified property to the specified value.
     * The value must be an instance of String, Boolean, or Number.
     * @param propertyName a hierarchical property name (e.g. "level1/level2/myProperty")
     * @param value the value to set
     */
    public void setProperty(String propertyName, Object value) {
        
        // Retrieve a mutable version of the underlying JsonObject.
        com.google.gson.JsonObject gsonObj = toGson(getJsonObj());
        
        // Parse the hierarchical property name into the individual path elements
        String[] pathElements = getPathElements(propertyName);
        
        // Next, navigate from the root JsonObject to the one containing the property.
        com.google.gson.JsonObject subGroup = getGsonSubGroup(gsonObj, pathElements);
        
        // Finally, set the property in the resulting subGroup based on the type.
        if (value instanceof String) {
            subGroup.addProperty(pathElements[pathElements.length-1], (String) value);
        } else if (value instanceof Boolean) {
            subGroup.addProperty(pathElements[pathElements.length-1], (Boolean) value);
        } else if (value instanceof Number) {
            subGroup.addProperty(pathElements[pathElements.length-1], (Number) value);
        } else {
            throw new IllegalArgumentException("Unsupported property value type: " + value.getClass().getName());
        }
        
        // Convert back to a JsonObject and stuff it back in the super class.
        setJsonObj(toJson(gsonObj));
    }

    /**
     * Retrieves the JsonObject that contains the property associated with the specified path elements.
     * If a certain property subgroup doesn't exist, then it is created.
     * 
     * @param gsonObj the root JsonObject to start the search in
     * @param pathElements the various elements that make up the hierarchical property name.
     * @return a JsonObject that will contain the specified property
     */
    protected com.google.gson.JsonObject getGsonSubGroup(com.google.gson.JsonObject gsonObj, String[] pathElements) {
        if (pathElements != null) {
            com.google.gson.JsonObject cursor = gsonObj;
            int limit = pathElements.length - 1;
            for (int i = 0; i < limit; i++) {
                com.google.gson.JsonObject prevCursor = cursor;
                cursor = cursor.getAsJsonObject(pathElements[i]);
                if (cursor == null) {
                    cursor = new com.google.gson.JsonObject();
                    prevCursor.add(pathElements[i], cursor);
                }
            }
            return cursor;
        }

        return null;
    }


    protected JsonObject toJson(com.google.gson.JsonObject gsonObj) throws JsonException{
        // Serialize the Gson object.
        String gsonString = gsonObj.toString();
        JsonObject jsonObj = null;
        // De-serialize the string into a JsonObject.
        ByteArrayInputStream bais = new ByteArrayInputStream(gsonString.getBytes());
        try (JsonReader reader = JSON_READER_FACTORY.createReader(bais)) {
            jsonObj = reader.readObject();
        }
        return jsonObj;
    }

    protected com.google.gson.JsonObject toGson(JsonObject jsonObj) {
        // Serialize the Json object.
        String jsonString = jsonObj.toString();
        
        // De-serialize the string into a "Gson" object.
        Gson gson = new Gson();
        com.google.gson.JsonElement jsonElement = gson.fromJson(jsonString, com.google.gson.JsonElement.class);
        com.google.gson.JsonObject gsonObj = jsonElement.getAsJsonObject();
        return gsonObj;
    }
}

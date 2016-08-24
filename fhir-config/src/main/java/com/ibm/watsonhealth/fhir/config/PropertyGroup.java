/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;

/**
 * This class represents a collection of properties - a property group. This could be the entire set of properties
 * resulting from loading the configuration, or it could be just a sub-structure within the overall config hierarchy, as
 * a property group can contain other property groups. Internally, there is a JsonObject which holds the actual group of
 * properties and this class provides a high-level API for accessing properties in a hierarchical manner.
 * 
 * @author padams
 *
 */
public class PropertyGroup {

    /**
     * This constant represents the separator character used within a hierarchical property name. 
     * Example: 
     * <xmp>
     *     fhir-server/server-core/truststoreLocation 
     * </xmp>
     */
    public static final String PATH_ELEMENT_SEPARATOR = "/";

    // This is the JsonObject which holds the property group
    private JsonObject jsonObj;

    public PropertyGroup(JsonObject jsonObj) {
        this.jsonObj = jsonObj;
    }

    /**
     * Returns a PropertyGroup associated with the specified property.
     * 
     * @param propertyName
     *            a hierarchical property name (e.g. "level1/level2/level3") that refers to a property group.
     * @return a PropertyGroup that holds the sub-structure associated with the specified property.
     */
    public PropertyGroup getPropertyGroup(String propertyName) {
        PropertyGroup result = null;
        JsonValue jsonValue = getJsonValue(propertyName);
        if (jsonValue != null) {
            if (jsonValue instanceof JsonObject) {
                result = new PropertyGroup((JsonObject)jsonValue);
            } else {
                throw new IllegalArgumentException("Property '" + propertyName + "' must be of type object (JsonObject)");
            }
        }
        return result;
    }

    /**
     * Returns the value of the specified String property or null if it wasn't found.
     * If the value is encoded, then it will be decoded.
     * @param propertyName the name of the property to retrieved
     * @throws Exception 
     */
    public String getStringProperty(String propertyName) throws Exception {
        return getStringProperty(propertyName, null);
    }

    /**
     * Returns the value of the specified String property.  If not found, then
     * 'defaultValue' is returned instead.
     * If the value is encoded, then it will be decoded.
     * 
     * @param propertyName the name of the property to retrieve
     * @throws Exception 
     */
    public String getStringProperty(String propertyName, String defaultValue) throws Exception {
        String result = defaultValue;
        JsonValue jsonValue = getJsonValue(propertyName);
        if (jsonValue != null) {
            if (jsonValue instanceof JsonString) {
                result = FHIRUtilities.decode(((JsonString) jsonValue).getString());
            } else {
                throw new IllegalArgumentException("Property '" + propertyName + "' must be of type String");
            }
        }
        return result;
    }
    
    /**
     * This is a convenience function that will retrieve an array property, then convert it
     * to a list of Strings by calling toString() on each array element.
     * @param propertyName the name of the property to retrive
     * @return a List<String> containing the elements from the JSON array property.
     * @throws Exception
     */
    public List<String> getStringListProperty(String propertyName) throws Exception {
        Object[] array = getArrayProperty(propertyName);
        List<String> strings = new ArrayList<>();
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                strings.add((String) array[i].toString());
            }
        }
        return strings;
    }


    /**
     * Returns the value of the specified int property or null if it wasn't found.
     * @param propertyName the name of the property to retrieve
     */
    public Integer getIntProperty(String propertyName) {
        return getIntProperty(propertyName, null);
    }

    /**
     * Returns the value of the specified int property.  If not found, then
     * 'defaultValue' is returned instead.
     * @param propertyName the name of the property to retrieve
     */
    public Integer getIntProperty(String propertyName, Integer defaultValue) {
        Integer result = defaultValue;
        JsonValue jsonValue = getJsonValue(propertyName);
        if (jsonValue != null) {
            if (jsonValue instanceof JsonNumber) {
                result = Integer.valueOf(((JsonNumber) jsonValue).intValue());
            } else {
                throw new IllegalArgumentException("Property '" + propertyName + "' must be of type int");
            }
        } 
        return result;
    }

    /**
     * Returns the value of the specified double property or null if it wasn't found.
     * @param propertyName the name of the property to retrieve
     */
    public Double getDoubleProperty(String propertyName) {
        return getDoubleProperty(propertyName, null);
    }

    /**
     * Returns the value of the specified double property.  If not found, then
     * 'defaultValue' is returned instead.
     * @param propertyName the name of the property to retrieve
     */
    public Double getDoubleProperty(String propertyName, Double defaultValue) {
        Double result = defaultValue;
        JsonValue jsonValue = getJsonValue(propertyName);
        if (jsonValue != null) {
            if (jsonValue instanceof JsonNumber) {
                result = Double.valueOf(((JsonNumber) jsonValue).doubleValue());
            } else {
                throw new IllegalArgumentException("Property '" + propertyName + "' must be of type double");
            }
        } 
        return result;
    }
    
    /**
     * Returns the value of the specified boolean property or null if it wasn't found.
     * @param propertyName the name of the property to retrieve
     */
    public Boolean getBooleanProperty(String propertyName) {
        return getBooleanProperty(propertyName, Boolean.FALSE);
    }

    /**
     * Returns the value of the specified boolean property.  If not found, then
     * 'defaultValue' is returned instead.
     * @param propertyName the name of the property to retrieve
     */
    public Boolean getBooleanProperty(String propertyName, Boolean defaultValue) {
        Boolean result = defaultValue;
        JsonValue jsonValue = getJsonValue(propertyName);
        if (jsonValue != null) {
            // If the value is stored in the JSON object as a boolean, then
            // construct the result from that.
            if (jsonValue == JsonValue.TRUE || jsonValue == JsonValue.FALSE) {
                result = Boolean.valueOf(jsonValue == JsonValue.TRUE);
            }

            // Otherwise, if the value was actually a string (i.e. "true" or "false"),
            // then construct the boolean result from the string.
            else if (jsonValue instanceof JsonString) {
                result = Boolean.valueOf(((JsonString) jsonValue).getString());
            } else {
                throw new IllegalArgumentException("Property '" + propertyName + "' must be of type boolean or String");
            }
        }
        return result;
    }
    
    /**
     * Returns the value (as an array of Object) of the specified array property.
     * Each element of the returned array will be an instance of Boolean, Integer, Double, String
     * or PropertyGroup, depending on the value type associated with the property within the 
     * underlying JsonObject.
     * @param propertyName the name of the property to retrieve
     * @return
     * @throws Exception 
     */
    public Object[] getArrayProperty(String propertyName) throws Exception {
        Object[] result = null;
        JsonValue jsonValue = getJsonValue(propertyName);
        if (jsonValue != null) {
            if (jsonValue instanceof JsonArray) {
                JsonArray jsonArray = (JsonArray) jsonValue;
                result = new Object[jsonArray.size()];
                for (int i = 0; i < jsonArray.size(); i++) {
                    result[i] = convertJsonValue(jsonArray.get(i));
                }
            } else {
                throw new IllegalArgumentException("Property '" + propertyName + "' must be an array");
            }
            
        }
        return result;
    }
    
    /**
     * Returns the properties contained in the PropertyGroup in the form of a list of
     * PropertyEntry instances.   If no properties exist, then an empty list will be returned.
     * @throws Exception 
     */
    public List<PropertyEntry> getProperties() throws Exception {
        List<PropertyEntry> results = new ArrayList<>();
        for (Map.Entry<String, JsonValue> entry : jsonObj.entrySet()) {
            results.add(new PropertyEntry(entry.getKey(), convertJsonValue(entry.getValue())));
        }
        return results;
    }
    
    /**
     * Returns the String representation of the PropertyGroup instance.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PropertyGroup[");
        sb.append(jsonObj != null ? jsonObj.toString() : "<empty>");
        sb.append("]");
        return sb.toString();
    }

    /**
     * converts the specified JsonValue into the appropriate java.lang.* type.
     * @param jsonValue the JsonValue instance to be converted
     * @return an instance of Boolean, Integer, String, or PropertyGroup
     * @throws Exception 
     */
    private Object convertJsonValue(JsonValue jsonValue) throws Exception {
        Object result = null;
        switch (jsonValue.getValueType()) {
        case FALSE:
            result = Boolean.FALSE;
            break;
        case NUMBER:
            JsonNumber jsonNumber = (JsonNumber) jsonValue;
            if (jsonNumber.isIntegral()) {
                result = Integer.valueOf(jsonNumber.intValue());
            } else {
                result = Double.valueOf(jsonNumber.doubleValue());
            }
            break;
        case OBJECT:
            result = new PropertyGroup((JsonObject) jsonValue);
            break;
        case STRING:
            result = FHIRUtilities.decode(((JsonString)jsonValue).getString());
            break;
        case TRUE:
            result = Boolean.TRUE;
            break;
        default:
            throw new IllegalStateException("Unexpected JSON value type: " + jsonValue.getValueType().name());
        }
        return result;
    }

    /**
     * Finds the specified property and returns it as a generic JsonValue.
     * 
     * @param propertyName
     *            the possibly hierarchical property name.
     */
    private JsonValue getJsonValue(String propertyName) {
        String[] pathElements = getPathElements(propertyName);
        JsonObject subGroup = getPropertySubGroup(pathElements);
        JsonValue result = null;
        if (subGroup != null) {
            result = subGroup.get(pathElements[pathElements.length - 1]);
        }
        return result;
    }

    /**
     * Splits a potentially hierarchical property name into the individual path elements
     * 
     * @param propertyName
     *            a hierarchical property name (e.g. "level1/level2/myProperty"
     * @return
     */
    private String[] getPathElements(String propertyName) {
        return propertyName.split(PATH_ELEMENT_SEPARATOR);
    }

    /**
     * This function will find the JSON "sub object" rooted at "this.jsonObj" that is associated with the specified
     * hierarchical property name. 
     * <p>For example, consider the following JSON structure: 
     * <xmp>
     * { 
     *     "level1":{ 
     *         "level2":{
     *             "myProperty":"myValue" 
     *         } 
     *     } 
     * } 
     * </xmp> 
     * If this function was invoked with a property name of "level1/level2/myProperty", 
     * then the result will be the JsonObject associated with the "level2" field within the JSON
     * structure above.
     * 
     * @param pathElements
     *            an array of path elements that make up the hierarchical property name (e.g. {"level1", "level2",
     *            "myProperty"})
     * @return the JsonObject sub-structure that contains the specified property.
     */
    private JsonObject getPropertySubGroup(String[] pathElements) {
        if (pathElements != null) {
            JsonObject cursor = this.jsonObj;
            int limit = pathElements.length - 1;
            for (int i = 0; i < limit; i++) {
                cursor = cursor.getJsonObject(pathElements[i]);
                if (cursor == null) {
                    break;
                }
            }
            return cursor;
        }

        return null;
    }
    
    /**
     * This class represents a single property contained within a PropertyGroup.
     */
    public static class PropertyEntry {
        private String name;
        private Object value;
        
        public PropertyEntry(String name, Object value) {
            this.name = name;
            this.value = value;
        }
        
        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }
}

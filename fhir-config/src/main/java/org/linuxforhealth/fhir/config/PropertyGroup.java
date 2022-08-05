/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.linuxforhealth.fhir.core.FHIRUtilities;

import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

/**
 * This class represents a collection of properties - a property group. This could be the entire set of properties
 * resulting from loading the configuration, or it could be just a sub-structure within the overall config hierarchy, as
 * a property group can contain other property groups. Internally, there is a JsonObject which holds the actual group of
 * properties and this class provides a high-level API for accessing properties in a hierarchical manner.
 */
public class PropertyGroup {

    /**
     * This constant represents the separator character used within a hierarchical property name.
     * Example:
     * <code>fhir-server/server-core/truststoreLocation</code>
     */
    public static final String PATH_ELEMENT_SEPARATOR = "/";

    // This is the JsonObject which holds the property group
    protected JsonObject jsonObj;

    public PropertyGroup(JsonObject jsonObj) {
        this.jsonObj = jsonObj;
    }

    public JsonObject getJsonObj() {
        return jsonObj;
    }

    protected void setJsonObj(JsonObject jsonObj) {
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
     *
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
     *
     * @param propertyName the name of the property to retrieve
     * @return a List<String> containing the elements from the JSON array property; possibly null
     */
    public List<String> getStringListProperty(String propertyName) {
        Object[] array = getArrayProperty(propertyName);
        List<String> strings = null;
        if (array != null) {
            strings = new ArrayList<String>();
            for (int i = 0; i < array.length; i++) {
                strings.add(array[i].toString());
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
        return getBooleanProperty(propertyName, null);
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
     *
     * @param propertyName the name of the property to retrieve
     * @return an array of values from the specified array property or null if the property doesn't exist
     */
    public Object[] getArrayProperty(String propertyName) {
        Object[] result = null;
        JsonValue jsonValue = getJsonValue(propertyName);
        if (jsonValue != null) {
            if (jsonValue instanceof JsonArray) {
                result = convertJsonArray((JsonArray) jsonValue);
            } else {
                throw new IllegalArgumentException("Property '" + propertyName + "' must be an array");
            }
        }
        return result;
    }

    /**
     * Returns the properties contained in the PropertyGroup in the form of a list of
     * PropertyEntry instances. If no properties exist, then an empty list will be returned.
     * Properties with a value of null will be omitted from the list.
     */
    public List<PropertyEntry> getProperties() {
        List<PropertyEntry> results = new ArrayList<>();
        for (Map.Entry<String, JsonValue> entry : jsonObj.entrySet()) {
            Object jsonValue = convertJsonValue(entry.getValue());
            if (jsonValue != null) {
                results.add(new PropertyEntry(entry.getKey(), jsonValue));
            }
        }
        return results;
    }

    /**
     * Returns the String representation of the PropertyGroup instance.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PropertyGroup[");
        sb.append(jsonObj != null ? jsonObj.toString() : "<empty>");
        sb.append("]");
        return sb.toString();
    }

    /**
     * Converts the specified JsonValue into the appropriate java.lang.* type.
     * @param jsonValue the JsonValue instance to be converted
     * @return either null or an instance of Boolean, Integer, String, PropertyGroup, or List<Object>
     */
    public static Object convertJsonValue(JsonValue jsonValue) {
        Object result = null;
        switch (jsonValue.getValueType()) {
        case ARRAY:
            Object[] objArray = convertJsonArray((JsonArray)jsonValue);
            result = Arrays.asList(objArray);
            break;
        case OBJECT:
            result = new PropertyGroup((JsonObject) jsonValue);
            break;
        case STRING:
            result = FHIRUtilities.decode(((JsonString) jsonValue).getString());
            break;
        case NUMBER:
            JsonNumber jsonNumber = (JsonNumber) jsonValue;
            if (jsonNumber.isIntegral()) {
                result = Integer.valueOf(jsonNumber.intValue());
            } else {
                result = Double.valueOf(jsonNumber.doubleValue());
            }
            break;
        case TRUE:
            result = Boolean.TRUE;
            break;
        case FALSE:
            result = Boolean.FALSE;
            break;
        case NULL:
            break;
        default:
            throw new IllegalStateException("Unexpected JSON value type: " + jsonValue.getValueType().name());
        }
        return result;
    }

    /**
     * Converts the specified JsonArray into an Object[]
     * @param jsonArray the JsonArray to be converted
     * @return an Object[] containing the converted values found in the JsonArray
     */
    private static Object[] convertJsonArray(JsonArray jsonArray) {
        Object[] result = new Object[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            result[i] = convertJsonValue(jsonArray.get(i));
        }
        return result;
    }

    /**
     * Finds the specified property and returns it as a generic JsonValue.
     *
     * @param propertyName
     *            the possibly hierarchical property name.
     * @return the property value as a JsonValue or null if the property is either missing or has a null value
     */
    public JsonValue getJsonValue(String propertyName) {
        String[] pathElements = getPathElements(propertyName);
        JsonObject subGroup = getPropertySubGroup(pathElements);
        JsonValue result = null;
        if (subGroup != null) {
            result = subGroup.get(pathElements[pathElements.length - 1]);
        }
        return result == JsonValue.NULL ? null : result;
    }

    /**
     * Splits a potentially hierarchical property name into the individual path elements
     *
     * @param propertyName
     *            a hierarchical property name (e.g. "level1/level2/myProperty"
     * @return
     */
    protected String[] getPathElements(String propertyName) {
        return propertyName.split(PATH_ELEMENT_SEPARATOR);
    }

    /**
     * This function will find the JSON "sub object" rooted at "this.jsonObj" that is associated with the specified
     * hierarchical property name.
     * <p>For example, consider the following JSON structure:
     * <pre>
     * {
     *     "level1":{
     *         "level2":{
     *             "myProperty":"myValue"
     *         }
     *     }
     * }
     * </pre>
     * If this function was invoked with a property name of "level1/level2/myProperty",
     * then the result will be the JsonObject associated with the "level2" field within the JSON
     * structure above.
     *
     * @param pathElements
     *            an array of path elements that make up the hierarchical property name (e.g. {"level1", "level2",
     *            "myProperty"})
     * @return the JsonObject sub-structure that contains the specified property.
     */
    protected JsonObject getPropertySubGroup(String[] pathElements) {
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

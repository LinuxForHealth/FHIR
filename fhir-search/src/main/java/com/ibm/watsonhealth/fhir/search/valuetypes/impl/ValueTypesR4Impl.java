/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.valuetypes.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.ibm.watsonhealth.fhir.model.type.Count;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.search.SearchConstants.Type;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.parameters.Parameter;
import com.ibm.watsonhealth.fhir.search.valuetypes.IValueTypes;

/**
 * ValueTypes R4 Impl
 * 
 * @author pbastide
 *
 */
public class ValueTypesR4Impl implements IValueTypes {

    private static final String CLASSNAME = ValueTypesR4Impl.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static final String DEFAULT_FILE = "/valuetypes-default.json";

    private static final String BASE_RESOURCE = "Resource";

    protected static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    private static Map<String, Set<Class<?>>> defaultValueTypeMap = new HashMap<>();

    public static final String EXCEPTION = "Unable to determine search parameter values for query parameter '%s' and resource type '%s' ";

    public static final String CONVERT_EXCEPTION = "unable to convert to class while setting value types for %s";

    // ---------------------------------------------------------------------------------------------------
    @Override
    public boolean isDateRangeSearch(Class<?> resourceType, Parameter queryParm) throws FHIRSearchException {
        return getValueTypes(resourceType, queryParm.getName()).contains(Period.class);
    }

    @Override
    public boolean isDateSearch(Class<?> resourceType, Parameter queryParm) throws FHIRSearchException {
        // Date Search does not support Date and Partial DateTime in a Range Search.

        Set<Class<?>> valueTypes = getValueTypes(resourceType, queryParm.getName());
        return Type.TOKEN.compareTo(queryParm.getType()) == 0 || valueTypes.contains(com.ibm.watsonhealth.fhir.model.type.Date.class) || valueTypes.contains(DateTime.class)
                || valueTypes.contains(Instant.class);
    }

    @Override
    public boolean isRangeSearch(Class<?> resourceType, Parameter queryParm) throws FHIRSearchException {
        // Range Search does not handle decimal searches like a range search
        return getValueTypes(resourceType, queryParm.getName()).contains(Range.class);
    }

    @Override
    public boolean isIntegerSearch(Class<?> resourceType, Parameter queryParm) throws FHIRSearchException {
        try {
            Set<Class<?>> valueTypes = getValueTypes(resourceType, queryParm.getName());
            return (valueTypes.contains(com.ibm.watsonhealth.fhir.model.type.Integer.class) || valueTypes.contains(UnsignedInt.class)
                    || valueTypes.contains(PositiveInt.class) || valueTypes.contains(Count.class));
        } catch (Exception e) {
            throw new FHIRSearchException(String.format(EXCEPTION, queryParm.getName(), resourceType.getSimpleName()), e);
        }
    }

    // ---------------------------------------------------------------------------------------------------
    // Main Processing method:

    @Override
    public Set<Class<?>> getValueTypes(Class<?> resourceType, String name) throws FHIRSearchException {
        // Removed extends Resource

        // The name used in the lookup is from SearchParameter->name
        Set<Class<?>> valueTypes = new HashSet<>();
        if (resourceType != null && name != null) {
            if (defaultValueTypeMap.containsKey(hash(resourceType, name))) {
                valueTypes.addAll(defaultValueTypeMap.get(hash(resourceType, name)));
            }

            // In the case where no responses are found, check and addAll from Resource
            if (valueTypes.isEmpty() && defaultValueTypeMap.containsKey(hash(BASE_RESOURCE, name))) {
                valueTypes.addAll(defaultValueTypeMap.get(hash(BASE_RESOURCE, name)));
            }

        }

        return valueTypes;
    }

    /**
     * load default settings
     */
    @Override
    public void init() {
        try (InputStream stream = ValueTypesR4Impl.class.getResourceAsStream(DEFAULT_FILE)) {
            defaultValueTypeMap.putAll(load(stream));
            log.info("Finished loading the default value types json");
        } catch (IOException e) {
            log.warning("Unable to load the default " + DEFAULT_FILE);
        }
    }

    /**
     * loads the setting provided in the inputstream.
     * 
     * @param in
     * @return
     */
    public Map<String, Set<Class<?>>> load(InputStream in) {
        Map<String, Set<Class<?>>> tmp = new HashMap<>();
        /*
         * Format <br/> <code>{ "value-types": "default", "mappings": [{ "resourceType": "", "name": "",
         * "targetClasses": [] }] }</code>
         */

        if (in != null) {
            try (JsonReader jsonReader = JSON_READER_FACTORY.createReader(in)) {
                JsonObject jsonObject = jsonReader.readObject();
                JsonArray arr = jsonObject.get("mappings").asJsonArray();

                Iterator<JsonValue> iter = arr.iterator();
                while (iter.hasNext()) {
                    JsonObject resourceTypeMapping = iter.next().asJsonObject();
                    String resourceType = resourceTypeMapping.getString("resourceType");
                    String name = resourceTypeMapping.getString("name");
                    JsonArray classesArray = resourceTypeMapping.getJsonArray("targetClasses");
                    tmp.put(hash(resourceType, name), convertToClasses(classesArray));

                }

            }
        }

        return tmp;
    }

    /**
     * converts to FHIR R4 ResourceType classes.
     * 
     * @param classesArray
     * @return
     */
    public Set<Class<?>> convertToClasses(JsonArray classesArray) {
        Set<Class<?>> clzs = new HashSet<>();
        Iterator<JsonValue> iter = classesArray.iterator();
        while (iter.hasNext()) {
            JsonValue v = iter.next();
            String type = ((JsonString) v).getString();
            boolean found = processClass(clzs, "com.ibm.watsonhealth.fhir.model.resource", type);
            if (!found) {
                // Checks the other possibility
                found = processClass(clzs, "com.ibm.watsonhealth.fhir.model.type", type);
            }

            // Logs out if error.
            if (!found) {
                log.warning(String.format(CONVERT_EXCEPTION, type));
            }
        }

        return clzs;
    }

    /*
     * finds the class, and adds if valid.
     * @param clzs
     * @param pkg
     * @param clzVal
     * @return indicates found class
     */
    public static boolean processClass(Set<Class<?>> clzs, String pkg, String clzVal) {
        boolean result = false;
        try {
            Class<?> clz = Class.forName(pkg + "." + clzVal);
            clzs.add(clz);
            result = true;
        } catch (java.lang.IllegalArgumentException | ClassNotFoundException iae) {
            // no op
        }
        return result;

    }

    /**
     * hash for resource type classes.
     * 
     * @param resourceType
     * @param name
     * @return
     */
    public static final String hash(Class<?> resourceType, String name) {
        return hash(resourceType.getSimpleName(), name);
    }

    /**
     * hash is a contract that generates an expected output used in tools to generate the properties file, and to
     * facilitate the fast lookup.
     * 
     * @param resourceType
     * @param name
     * @return
     */
    public static final String hash(String resourceType, String name) {
        return resourceType + "." + name;
    }

}

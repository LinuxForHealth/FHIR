/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.valuetypes.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.type.Count;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.valuetypes.IValueTypes;
import com.ibm.fhir.search.valuetypes.cache.TenantSpecificValueTypesCache;

/**
 * ValueTypes R4 Impl
 */
public class ValueTypesR4Impl implements IValueTypes {

    private static final String CLASSNAME = ValueTypesR4Impl.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static final String DEFAULT_FILE = "/valuetypes-default.json";

    private static final String BASE_RESOURCE = "Resource";

    protected static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    private static Map<String, Set<Class<?>>> defaultValueTypeMap = new HashMap<>();

    private static TenantSpecificValueTypesCache cache = new TenantSpecificValueTypesCache();

    public static final String EXCEPTION =
            "Unable to determine search parameter values for query parameter '%s' and resource type '%s' ";

    public static final String CONVERT_EXCEPTION = "unable to convert to class while setting value types for %s";

    public ValueTypesR4Impl() {
        try (InputStream stream = ValueTypesR4Impl.class.getResourceAsStream(DEFAULT_FILE)) {
            defaultValueTypeMap.putAll(load(stream));
            log.info("Finished loading the default value types json");
        } catch (IOException e) {
            log.log(Level.WARNING, "Unable to load the default [" + DEFAULT_FILE + "]", e);
        }
    }

    @Override
    public boolean isDateRangeSearch(Class<?> resourceType, QueryParameter queryParm) throws FHIRSearchException {
        return getValueTypes(resourceType, queryParm.getCode()).contains(Period.class);
    }

    @Override
    public boolean isDateSearch(Class<?> resourceType, QueryParameter queryParm) throws FHIRSearchException {
        // Date Search does not support Date and Partial DateTime in a Range Search.

        Set<Class<?>> valueTypes = getValueTypes(resourceType, queryParm.getCode());
        return Type.TOKEN.compareTo(queryParm.getType()) == 0 || valueTypes.contains(com.ibm.fhir.model.type.Date.class)
                || valueTypes.contains(DateTime.class) || valueTypes.contains(Instant.class);
    }

    @Override
    public boolean isRangeSearch(Class<?> resourceType, QueryParameter queryParm) throws FHIRSearchException {
        // Range Search does not handle decimal searches like a range search
        return getValueTypes(resourceType, queryParm.getCode()).contains(Range.class);
    }

    @Override
    public boolean isIntegerSearch(Class<?> resourceType, QueryParameter queryParm) throws FHIRSearchException {
        try {
            Set<Class<?>> valueTypes = getValueTypes(resourceType, queryParm.getCode());
            return (valueTypes.contains(com.ibm.fhir.model.type.Integer.class) || valueTypes.contains(UnsignedInt.class)
                    || valueTypes.contains(PositiveInt.class) || valueTypes.contains(Count.class));
        } catch (Exception e) {
            throw new FHIRSearchException(String.format(EXCEPTION, queryParm.getCode(), resourceType.getSimpleName()),
                    e);
        }
    }

    // ---------------------------------------------------------------------------------------------------
    // Main Processing method:

    @Override
    public Set<Class<?>> getValueTypes(Class<?> resourceType, String code) throws FHIRSearchException {
        // Removed extends Resource

        // The name used in the lookup is from SearchParameter->name
        Set<Class<?>> valueTypes = new HashSet<>();
        if (resourceType != null && code != null) {

            addingValueTypesForTenantToListByResourceAndCode(valueTypes, resourceType, code);

            // Only add if there is nothing.
            if (valueTypes.isEmpty()) {
                if (defaultValueTypeMap.containsKey(hash(resourceType, code))) {
                    valueTypes.addAll(defaultValueTypeMap.get(hash(resourceType, code)));
                }

                // In the case where no responses are found, check and addAll from Resource
                if (valueTypes.isEmpty() && defaultValueTypeMap.containsKey(hash(BASE_RESOURCE, code))) {
                    valueTypes.addAll(defaultValueTypeMap.get(hash(BASE_RESOURCE, code)));
                }
            }

        }

        return valueTypes;
    }

    /**
     * Adds value types for tenants to a list by resource and code.
     * 
     * @param valueTypesList
     */
    public void addingValueTypesForTenantToListByResourceAndCode(Set<Class<?>> valueTypesList, Class<?> resourceType,
            String code) {
        // Check the Tenant Specific Cache
        String tenantId = FHIRRequestContext.get().getTenantId();
        if (tenantId != null) {

            try {

                Map<String, Set<Class<?>>> tenantValueTypes = cache.getCachedObjectForTenant(tenantId);
                if (tenantValueTypes != null) {
                    Set<Class<?>> valueTypes = tenantValueTypes.get(hash(resourceType, code));
                    if (valueTypes != null) {
                        valueTypesList.addAll(valueTypes);
                    } else if (log.isLoggable(Level.FINER)) {
                        log.log(Level.FINER, "Assuming search parameter '" + code + "' on resource type '"
                                + resourceType.getSimpleName() + "' is not an extension-search-parameter [tenantId="
                                + tenantId + "]");
                    }
                } else if (log.isLoggable(Level.FINE)) {
                    log.log(Level.FINE,
                            "Unable to find extension-search-parameters-valuetypes for tenantId '" + tenantId + "'");
                }

            } catch (Exception e) {
                // If there is an exception here, it's most likely in the file.
                log.log(Level.INFO, "Error loading the value types for parameter '" + code + "' on resource type '"
                        + resourceType.getSimpleName() + "' [tenantId=" + tenantId + "]", e);
            }
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
         * Format <br/> <pre> { "value-types": "default", "mappings": [{ "resourceType":
         * "", "name": "", "targetClasses": [] }] } </pre>
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

    @Override
    public Map<String, Set<Class<?>>> loadFromFile(File f) {
        Map<String, Set<Class<?>>> results = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(f);) {
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Loading the value types json [" + f + "]");
            }

            results = load(fis);

        } catch (Exception e) {
            log.log(Level.WARNING, "Unable to load the file [" + f + "]", e);
            results = Collections.emptyMap();
        }
        return results;
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
            boolean found = processClass(clzs, "com.ibm.fhir.model.resource", type);
            if (!found) {
                // Checks the other possibility
                found = processClass(clzs, "com.ibm.fhir.model.type", type);
            }
            if (!found) {
                // Checks the other possibility
                found = processClass(clzs, "com.ibm.fhir.model.type.code", type);
            }

            if (!found && "Location.Position".compareTo(type) == 0) {
                found = true;
                clzs.add(Location.Position.class);
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
     * 
     * @param clzs
     * 
     * @param pkg
     * 
     * @param clzVal
     * 
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
     * @param code
     * @return
     */
    public static final String hash(Class<?> resourceType, String code) {
        return hash(resourceType.getSimpleName(), code);
    }

    /**
     * hash is a contract that generates an expected output used in tools to
     * generate the properties file, and to facilitate the fast lookup.
     * 
     * @param resourceType
     * @param code
     * @return
     */
    public static final String hash(String resourceType, String code) {
        return resourceType + "." + code;
    }

}

/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.schema.app.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ibm.fhir.model.util.ModelSupport;

/**
 * Support functions for the FHIR DB schema
 */
public class SchemaSupport {
    // A set of all the FHIR (non-abstract) resource type names converted to upper case
    private static final Map<String,String> ALL_RESOURCE_TYPES = ModelSupport.getResourceTypes(false).stream()
            .map(t -> ModelSupport.getTypeName(t))
            .collect(Collectors.toMap(rt -> rt.toUpperCase(), rt -> rt.toString()));
    private static final Set<String> WHOLE_SYSTEM_TABLES = new HashSet<>(Arrays.asList(
            "LOGICAL_RESOURCES", "RESOURCES", "STR_VALUES", "DATE_VALUES", "RESOURCE_TOKEN_REFS",
            "LOGICAL_RESOURCE_IDENT",
            "RESOURCE_CHANGE_LOG", "COMMON_TOKEN_VALUES", "COMMON_CANONICAL_VALUES",
            "CODE_SYSTEMS",
            "LOGICAL_RESOURCE_PROFILES", "LOGICAL_RESOURCE_TAGS", "LOGICAL_RESOURCE_SECURITY"));

    private static final Set<String> IGNORE_TABLES = new HashSet<>(Arrays.asList(
            "WHOLE_SCHEMA_VERSION", "RESOURCE_TYPES", "PARAMETER_NAMES", 
            "LOGICAL_RESOURCE_COMPARTMENTS", "ERASED_RESOURCES"));

    /**
     * Public constructor
     */
    public SchemaSupport() {
        // NOP
    }

    /**
     * Obtain the resource type associated with the given table
     * @param tableName
     * @return the associated resource type or null if the table is known but not associated with a resource
     */
    public String getResourceTypeFromTableName(String tableName) {
        final String resourceType;

        String utn = tableName.toUpperCase();
        if (WHOLE_SYSTEM_TABLES.contains(utn)) {
            resourceType = "Resource";
        } else if (IGNORE_TABLES.contains(utn)) {
            // we know the table, but want to ignore it
            resourceType = null;
        } else {
            // try to determine the resource type from the prefix
            int idx = tableName.indexOf('_');
            if (idx > 0) {
                String sub = tableName.substring(0, idx);
                resourceType = ALL_RESOURCE_TYPES.get(sub.toUpperCase()); // may be null
            } else {
                throw new IllegalArgumentException("Not a recognized data schema table name: " + tableName);
            }
        }
        return resourceType;
    }

    /**
     * Is the named table one of the search parameter tables
     * @param tableName
     * @return
     */
    public boolean isParamTable(String tableName) {
        final String utn = tableName.toUpperCase();
        return utn.equals("STR_VALUES")
                || utn.equals("DATE_VALUES")
                || utn.equals("RESOURCE_TOKEN_REFS")
                || utn.equals("LOGICAL_RESOURCE_PROFILES")
                || utn.equals("LOGICAL_RESOURCE_TAGS")
                || utn.equals("LOGICAL_RESOURCE_SECURITY")
                || utn.endsWith("_STR_VALUES")
                || utn.endsWith("_DATE_VALUES")
                || utn.endsWith("_NUMBER_VALUES")
                || utn.endsWith("_QUANTITY_VALUES")
                || utn.endsWith("_LATLNG_VALUES")
                || utn.endsWith("_RESOURCE_TOKEN_REFS")
                || utn.endsWith("_TAGS")
                || utn.endsWith("_PROFILES")
                || utn.endsWith("_SECURITY")
                || utn.endsWith("_CURRENT_REFS")
                ;
    }

    /**
     * Convert the parameter table name to a string value describing its
     * type. For example:
     * <pre>
     *      PATIENT_STR_VALUES: STRING
     *      PATIENT_DATE_VALUES: DATE
     *      PATIENT_RESOURCE_TOKEN_REFS: TOKEN
     *      etc.
     * </pre>
     * @param tableName
     * @return
     */
    public String getParamTableType(String tableName) {
        final String utn = tableName.toUpperCase();
        if (utn.equals("STR_VALUES")) {
            return "SYSTEM_STRING";
        } else if (utn.equals("DATE_VALUES")) {
            return "SYSTEM_DATE";
        } else if (utn.equals("RESOURCE_TOKEN_REFS")) {
            return "SYSTEM_TOKEN";
        } else if (utn.equals("LOGICAL_RESOURCE_PROFILES")) {
            return "SYSTEM_PROFILE";
        } else if (utn.equals("LOGICAL_RESOURCE_TAGS")) {
            return "SYSTEM_TAG";
        } else if (utn.equals("LOGICAL_RESOURCE_SECURITY")) {
            return "SYSTEM_SECURITY";
        } else if (utn.endsWith("_STR_VALUES")) {
            return "STRING";
        } else if (utn.endsWith("_DATE_VALUES")) {
            return "DATE";
        } else if (utn.endsWith("_NUMBER_VALUES")) {
            return "NUMBER";
        } else if (utn.endsWith("_QUANTITY_VALUES")) {
            return "QUANTITY";
        } else if (utn.endsWith("_LATLNG_VALUES")) {
            return "LOCATION";
        } else if (utn.endsWith("_RESOURCE_TOKEN_REFS")) {
            return "TOKEN";
        } else if (utn.endsWith("_TAGS")) {
            return "TAG";
        } else if (utn.endsWith("_PROFILES")) {
            return "PROFILE";
        } else if (utn.endsWith("_SECURITY")) {
            return "SECURITY";
        } else if (utn.endsWith("_CURRENT_REFS")) {
            return "LIST";
        } else {
            throw new IllegalArgumentException("Not a parameter table: " + tableName);
        }
    }
}

/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.config;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.config.PropertyGroup.PropertyEntry;
import org.linuxforhealth.fhir.core.FHIRVersionParam;

import jakarta.json.JsonValue;

/**
 * This class contains a set of static helper methods related to configuration parameters.
 * The functions in this class will try to first retrieve a config property from the current
 * tenant's configuration, then (if not found) look in the "default" configuration.
 */
public class FHIRConfigHelper {
    private static final Logger log = Logger.getLogger(FHIRConfigHelper.class.getName());

    //Constants
    public static final String SEARCH_PROPERTY_TYPE_INCLUDE = "_include";
    public static final String SEARCH_PROPERTY_TYPE_REVINCLUDE = "_revinclude";
    public static final String RESOURCE_RESOURCE = "Resource";

    public static String getStringProperty(String propertyName, String defaultValue) {
        return getTypedProperty(String.class, propertyName, defaultValue);
    }

    public static Boolean getBooleanProperty(String propertyName, Boolean defaultValue) {
        return getTypedProperty(Boolean.class, propertyName, defaultValue);
    }

    public static Integer getIntProperty(String propertyName, Integer defaultValue) {
        return getTypedProperty(Integer.class, propertyName, defaultValue);
    }

    public static Double getDoubleProperty(String propertyName, Double defaultValue) {
        return getTypedProperty(Double.class, propertyName, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public static List<String> getStringListProperty(String propertyName) {
        return getTypedProperty(List.class, propertyName, null);
    }

    public static PropertyGroup getPropertyGroup(String propertyName) {
        return getTypedProperty(PropertyGroup.class, propertyName, null);
    }

    /**
     * This function retrieves the specified property as a generic JsonValue. First we try to retrieve the property from
     * the current tenant's config, and then if not found we'll also look in the "default" config.
     *
     * @param propertyName
     *            the hierarchical name of the property to be retrieved (e.g. "level1/level2/prop1")
     * @return a JsonValue representing the property's value or null if it wasn't found in either config
     */
    private static JsonValue getPropertyFromTenantOrDefault(String propertyName) {
        JsonValue result = null;

        PropertyGroup pg = null;
        String tenantId = FHIRRequestContext.get().getTenantId();

        // First, try to retrieve the configuration (property group) associated with the
        // current thread's tenant-id.
        try {
            pg = FHIRConfiguration.getInstance().loadConfigurationForTenant(tenantId);
            if (pg != null) {
                result = pg.getJsonValue(propertyName);
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Error loading configuration for tenant-id '" + tenantId + "': " + e.getMessage());
        }

        // If we didn't find the property in the tenant-specific config, then
        // let's try to find it in the default config.
        if (result == null && !FHIRConfiguration.DEFAULT_TENANT_ID.equals(tenantId)) {
            try {
                if (propertyName.startsWith(FHIRConfiguration.PROPERTY_DATASOURCES)
                        || propertyName.startsWith(FHIRConfiguration.PROPERTY_PERSISTENCE_PAYLOAD)) {
                    // Issue #639 and #3416. Prevent datasource/payload lookups from falling back to
                    // the default configuration which breaks tenant isolation.
                    result = null;
                } else {
                    // Non-datasource property, which we allow to fall back to default
                    pg = FHIRConfiguration.getInstance().loadConfiguration();
                    if (pg != null) {
                        result = pg.getJsonValue(propertyName);
                    }
                }
            } catch (Exception e) {
                log.log(Level.WARNING, "Error loading default configuration: " + e.getMessage());
            }
        }

        return result;
    }

    /**
     * This generic function will perform the work of retrieving a property from either the tenant-specific
     * config or the default config, and then converting the resulting value to the appropriate type.
     *
     * @param propertyName
     *            the name of the property to retrieve
     * @param defaultValue
     *            the default value to return in the event that the property is not found
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <T> T getTypedProperty(Class<T> expectedDataType, String propertyName, T defaultValue) {
        T result = null;

        // Find the property as a generic JsonValue from either the current tenant's config or the default config.
        JsonValue jsonValue = getPropertyFromTenantOrDefault(propertyName);

        // If found, then convert the value to the expected type.
        if (jsonValue != null) {
            Object obj = null;
            try {
                obj = PropertyGroup.convertJsonValue(jsonValue);
                if (obj != null) {
                    // If the property was of the expected type, then just do the assignment.
                    // Otherwise, we'll try to do some simple conversions (e.g. String --> Boolean).
                    if (expectedDataType.isAssignableFrom(obj.getClass())) {
                        result = (T) obj;
                    } else {
                        if (obj instanceof String) {
                            if (Boolean.class.equals(expectedDataType)) {
                                result = (T) Boolean.valueOf((String) obj);
                            } else if (Integer.class.equals(expectedDataType)) {
                                result = (T) Integer.valueOf((String) obj);
                            } else if (Double.class.equals(expectedDataType)) {
                                result = (T) Double.valueOf((String) obj);
                            } else {
                                throw new RuntimeException("Expected property " + propertyName + " to be of type "
                                        + expectedDataType.getName() + ", but was of type " + obj.getClass().getName());
                            }
                        } else if (obj instanceof Boolean) {
                            if (String.class.equals(expectedDataType)) {
                                result = (T) ((Boolean)obj).toString();
                            } else {
                                throw new RuntimeException("Expected property " + propertyName + " to be of type "
                                        + expectedDataType.getName() + ", but was of type " + obj.getClass().getName());
                            }
                        } else {
                            throw new RuntimeException("Expected property " + propertyName + " to be of type "
                                    + expectedDataType.getName() + ", but was of type " + obj.getClass().getName());
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Unexpected error converting property '" + propertyName + "' to native type.", e);
            }
        }

        return (result != null ? result : defaultValue);
    }

    /**
     * Get the set of supported resource types for the latest supported FHIRVersion
     * and the tenantId in the FHIRRequestContext
     * @return an immutable non-null set of resource type names that isn't null
     * @throws IllegalStateException if there is an unexpected issue while processing the config
     */
    public static Set<String> getSupportedResourceTypes() {
        return getSupportedResourceTypes(FHIRVersionParam.VERSION_43);
    }

    /**
     * Get the set of supported resource types for the passed fhirVersion
     * and the tenantId in the FHIRRequestContext
     * @param fhirVersion
     * @return an immutable non-null set of resource type names for the passed fhirVersion
     * @throws IllegalStateException if there is an unexpected issue while processing the config
     */
    public static Set<String> getSupportedResourceTypes(FHIRVersionParam fhirVersion) {
        PropertyGroup resourcesGroup = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_RESOURCES);
        ResourcesConfigAdapter configAdapter = new ResourcesConfigAdapter(resourcesGroup, fhirVersion);
        return configAdapter.getSupportedResourceTypes();
    }

    /**
     * Retrieves the search property restrictions.
     *
     * @param resourceType the resource type
     * @param propertyType the property type, either _include or _revinclude
     * @return list of allowed values for the search property, or null if no restrictions
     * @throws Exception
     *             an exception
     */
    public static List<String> getSearchPropertyRestrictions(String resourceType, String propertyType) throws Exception {
        String propertyField = null;
        if (SEARCH_PROPERTY_TYPE_INCLUDE.equals(propertyType)) {
            propertyField = FHIRConfiguration.PROPERTY_FIELD_RESOURCES_SEARCH_INCLUDES;
        }
        else if (SEARCH_PROPERTY_TYPE_REVINCLUDE.equals(propertyType)) {
            propertyField = FHIRConfiguration.PROPERTY_FIELD_RESOURCES_SEARCH_REV_INCLUDES;
        }

        // Retrieve the "resources" config property group.
        if (propertyField != null) {
            PropertyGroup rsrcsGroup = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_RESOURCES);
            if (rsrcsGroup != null) {
                List<PropertyEntry> rsrcsEntries = rsrcsGroup.getProperties();
                if (rsrcsEntries != null && !rsrcsEntries.isEmpty()) {

                    // Try to find search property for matching resource type
                    for (PropertyEntry rsrcsEntry : rsrcsEntries) {
                        if (resourceType.equals(rsrcsEntry.getName())) {
                            PropertyGroup resourceTypeGroup = (PropertyGroup) rsrcsEntry.getValue();
                            if (resourceTypeGroup != null) {
                                return resourceTypeGroup.getStringListProperty(propertyField);
                            }
                        }
                    }

                    // Otherwise, try to find search property for "Resource" resource type
                    for (PropertyEntry rsrcsEntry : rsrcsEntries) {

                        // Check if matching resource type
                        if (RESOURCE_RESOURCE.equals(rsrcsEntry.getName())) {
                            PropertyGroup resourceTypeGroup = (PropertyGroup) rsrcsEntry.getValue();
                            if (resourceTypeGroup != null) {
                                return resourceTypeGroup.getStringListProperty(propertyField);
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}

/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.config;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains a set of static helper methods related to
 * configuration parameters.
 */
public class FHIRConfigHelper {
    private static final Logger log = Logger.getLogger(FHIRConfigHelper.class.getName());

    
    /**
     * Retrieves the specified property from the configuration associated with
     * the current thread's tenant-id.   If not found there, we'll try to retrieve
     * the same property from the "default" configuration.
     * @param propertyName the name of the property to retrieve
     * @param defaultValue the default value to be returned if the property isn't found
     * @return
     */
    public static String getStringProperty(String propertyName, String defaultValue) {
        String result = null;
        
        PropertyGroup pg = null;
        String tenantId = FHIRRequestContext.get().getTenantId();
        
        // First, try to retrieve the configuration (property group) associated with the 
        // current thread's tenant-id.
        try {
            pg = FHIRConfiguration.getInstance().loadConfigurationForTenant(tenantId);
            if (pg != null) {
                result = pg.getStringProperty(propertyName);
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Error loading configuration for tenant-id '" + tenantId + "': " + e.getMessage());
        }
        
        // If we didn't find the property in the tenant-specific config, then 
        // let's try to find it in the default config.
        if (result == null && !tenantId.equals(FHIRConfiguration.DEFAULT_TENANT_ID)) {
            try {
                pg = FHIRConfiguration.getInstance().loadConfiguration();
                if (pg != null) {
                    result = pg.getStringProperty(propertyName, null);
                }
            } catch (Exception e) {
                log.log(Level.WARNING, "Error loading default configuration: " + e.getMessage());
            }
        }
        
        return (result != null ? result : defaultValue);
    }
    
    public static Boolean getBooleanProperty(String propertyName, Boolean defaultValue) {
        Boolean result = null;
        
        PropertyGroup pg = null;
        String tenantId = FHIRRequestContext.get().getTenantId();
        
        // First, try to retrieve the configuration (property group) associated with the 
        // current thread's tenant-id.
        try {
            pg = FHIRConfiguration.getInstance().loadConfigurationForTenant(tenantId);
            if (pg != null) {
                result = pg.getBooleanProperty(propertyName);
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Error loading configuration for tenant-id '" + tenantId + "': " + e.getMessage());
        }
        
        // If we didn't find the property in the tenant-specific config, then 
        // let's try to find it in the default config.
        if (result == null && !tenantId.equals(FHIRConfiguration.DEFAULT_TENANT_ID)) {
            try {
                pg = FHIRConfiguration.getInstance().loadConfiguration();
                if (pg != null) {
                    result = pg.getBooleanProperty(propertyName, null);
                }
            } catch (Exception e) {
                log.log(Level.WARNING, "Error loading default configuration: " + e.getMessage());
            }
        }
        
        return (result != null ? result : defaultValue);
    }
    
    public static List<String> getStringListProperty(String propertyName) {
        List<String> result = null;
        
        PropertyGroup pg = null;
        String tenantId = FHIRRequestContext.get().getTenantId();
        
        // First, try to retrieve the configuration (property group) associated with the 
        // current thread's tenant-id.
        try {
            pg = FHIRConfiguration.getInstance().loadConfigurationForTenant(tenantId);
            if (pg != null) {
                result = pg.getStringListProperty(propertyName);
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Error loading configuration for tenant-id '" + tenantId + "': " + e.getMessage());
        }
        
        // If we didn't find the property in the tenant-specific config, then 
        // let's try to find it in the default config.
        if (result == null && !tenantId.equals(FHIRConfiguration.DEFAULT_TENANT_ID)) {
            try {
                pg = FHIRConfiguration.getInstance().loadConfiguration();
                if (pg != null) {
                    result = pg.getStringListProperty(propertyName);
                }
            } catch (Exception e) {
                log.log(Level.WARNING, "Error loading default configuration: " + e.getMessage());
            }
        }
        
        return result;
    }
    
    public static PropertyGroup getPropertyGroup(String propertyName) {
        PropertyGroup result = null;
        
        PropertyGroup pg = null;
        String tenantId = FHIRRequestContext.get().getTenantId();
        
        // First, try to retrieve the configuration (property group) associated with the 
        // current thread's tenant-id.
        try {
            pg = FHIRConfiguration.getInstance().loadConfigurationForTenant(tenantId);
            if (pg != null) {
                result = pg.getPropertyGroup(propertyName);
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Error loading configuration for tenant-id '" + tenantId + "': " + e.getMessage());
        }
        
        // If we didn't find the property in the tenant-specific config, then 
        // let's try to find it in the default config.
        if (result == null && !tenantId.equals(FHIRConfiguration.DEFAULT_TENANT_ID)) {
            try {
                pg = FHIRConfiguration.getInstance().loadConfiguration();
                if (pg != null) {
                    result = pg.getPropertyGroup(propertyName);
                }
            } catch (Exception e) {
                log.log(Level.WARNING, "Error loading default configuration: " + e.getMessage());
            }
        }
        
        return result;
    }
}

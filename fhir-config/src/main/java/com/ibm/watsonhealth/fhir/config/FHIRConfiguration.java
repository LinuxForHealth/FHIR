/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.core.CachedObjectHolder;

public class FHIRConfiguration {
    private static final Logger log = Logger.getLogger(FHIRConfiguration.class.getName());

    /**
     * This class serves up a singleton instance of ConfigurationService containing the FHIR Server's configuration.
     */
    public static final String CONFIG_LOCATION = "config";
    public static final String CONFIG_FILE_BASENAME = "fhir-server-config.json";
    public static final String DEFAULT_TENANT_ID = "default";

    // Configuration properties used by various FHIR Server components.
    public static final String PROPERTY_VIRTUAL_RESOURCES_ENABLED = "fhirServer/virtualResources/enabled";
    public static final String PROPERTY_ALLOWABLE_VIRTUAL_RESOURCE_TYPES = "fhirServer/virtualResources/allowableResourceTypes";
    public static final String PROPERTY_USER_DEFINED_SCHEMATRON_ENABLED = "fhirServer/core/userDefinedSchematronEnabled";
    public static final String PROPERTY_TRUSTSTORE_LOCATION = "fhirServer/core/truststoreLocation";
    public static final String PROPERTY_TRUSTSTORE_PASSWORD = "fhirServer/core/truststorePassword";
    public static final String PROPERTY_OAUTH_REGURL = "fhirServer/oauth/regUrl";
    public static final String PROPERTY_OAUTH_AUTHURL = "fhirServer/oauth/authUrl";
    public static final String PROPERTY_OAUTH_TOKENURL = "fhirServer/oauth/tokenUrl";
    public static final String PROPERTY_AUDIT_LOGPATH = "fhirServer/audit/logPath";
    public static final String PROPERTY_AUDIT_TENANTID = "fhirServer/audit/tenantId";
    public static final String PROPERTY_AUDIT_LOG_MAXSIZE = "fhirServer/audit/logMaxSize";
    public static final String PROPERTY_ENCRYPTION = "fhirServer/encryption";
    public static final String PROPERTY_UPDATE_CREATE_ENABLED = "fhirServer/persistence/common/updateCreateEnabled";
    public static final String PROPERTY_USE_UUIDS = "fhirServer/persistence/jpa/useUUIDs";
    public static final String PROPERTY_SCHEMA_NAME = "fhirServer/persistence/jpa/schemaName";
    public static final String PROPERTY_NOTIFICATION_RESOURCE_TYPES = "fhirServer/notifications/common/includeResourceTypes";
    public static final String PROPERTY_WEBSOCKET_ENABLED = "fhirServer/notifications/websocket/enabled";
    public static final String PROPERTY_KAFKA_ENABLED = "fhirServer/notifications/kafka/enabled";
    public static final String PROPERTY_KAFKA_TOPICNAME = "fhirServer/notifications/kafka/topicName";
    public static final String PROPERTY_KAFKA_CONNECTIONPROPS = "fhirServer/notifications/kafka/connectionProperties";
    public static final String PROPERTY_PERSISTENCE_FACTORY = "fhirServer/persistence/factoryClassname";
    public static final String PROPERTY_CLOUDANT_URL = "fhirServer/persistence/cloudant/url";
    public static final String PROPERTY_CLOUDANT_USERNAME = "fhirServer/persistence/cloudant/username";
    public static final String PROPERTY_CLOUDANT_PWD = "fhirServer/persistence/cloudant/pwd";
    public static final String PROPERTY_CLOUDANT_DBNAME = "fhirServer/persistence/cloudant/dbName";
    public static final String PROPERTY_WHCLSF_ROUTER = "fhirServer/persistence/whclsfRouter";
    public static final String PROPERTY_JDBC_BOOTSTRAP_DB = "fhirServer/persistence/jdbc/bootstrapDb";
    public static final String PROPERTY_JDBC_SCHEMA_TYPE = "fhirServer/persistence/jdbc/schemaType";
    public static final String PROPERTY_TENANT_ID_HEADER_NAME = "fhirServer/core/tenantIdHeaderName";
    
    public static final String DEFAULT_TENANT_ID_HEADER_NAME = "X-FHIR-TENANT-ID";

    public static final String FHIR_SERVER_DEFAULT_CONFIG = "config/default/fhir-server-config.json";
    
    // Optional "home directory" for config files.  Defaults to current directory.
    private static String configHome = "";

    private static FHIRConfiguration _instance = new FHIRConfiguration();

    public static FHIRConfiguration getInstance() {
        return _instance;
    }

    /**
     * This Map contains the cache of PropertyGroupHolder objects keyed by tenant-id.
     */
    private Map<String, CachedObjectHolder<PropertyGroup>> configCache = new HashMap<>();
    
    private String getConfigFileName(String tenantId) {
        return configHome + CONFIG_LOCATION + File.separator + tenantId + File.separator + CONFIG_FILE_BASENAME;
    }
    
    /**
     * This method is used to configure an explicit top-level directory where FHIR Server configuration
     * information is expected to reside.
     * For example, by calling this method with value "/mydir", then we'd expect
     * to find config files whose names are of the form:  "/mydir/config/<tenant-id>/fhir-server-config.json".
     * The default location for config files is the current working directory (i.e. "" - the empty string).
     * @param s the new config home directory name 
     */
    public static void setConfigHome(String s) {
        if (s == null) {
            s = "";
        }
        if (!s.isEmpty() && !s.endsWith("/")) {
            s += "/";
        }
        
        configHome = s;
    }
    
    /**
     * Returns the "home" directory for FHIR Server configuration information (this directory will contain
     * the "config" directory, etc.).   
     * The default value of this property is "" which is interpretted to mean the current working directory
     * (which for a running FHIR Server will be $WLP_HOME/wlp/usr/servers/fhir-server).
     */
    public static String getConfigHome() {
        return configHome;
    }

    /**
     * Retrieves the FHIR Server configuration and returns it as a PropertyGroup.
     * 
     * @throws FileNotFoundException
     */
    public PropertyGroup loadConfiguration() throws Exception {
        return loadConfigurationForTenant(DEFAULT_TENANT_ID);
    }

    /**
     * Loads the configuration for the specified tenant id.
     * 
     * @param tenantId
     *            a shortname representing the tenant whose configuration will be loaded
     * @return the top-level property group representing this tenant's configuration
     * @throws Exception
     */
    public PropertyGroup loadConfigurationForTenant(String tenantId) throws Exception {
        log.entering(this.getClass().getName(), "loadConfigurationForTenant", tenantId);

        try {
            synchronized (configCache) {

                // Check to see if this tenant already has a configuration in the cache.
                CachedObjectHolder<PropertyGroup> pgHolder = configCache.get(tenantId);

                // Next, check to see if the property group is stale.
                // If so, just throw it away and re-load below.
                if (pgHolder != null && pgHolder.isStale()) {
                    log.finer("Cached configuration for tenant-id '" + tenantId + "' is stale, discarding...");
                    configCache.remove(tenantId);
                    pgHolder = null;
                }

                // If we have no "current" configuration for this tenant in the cache,
                // then load it and add it to cache.
                if (pgHolder == null) {
                    String fileName = getConfigFileName(tenantId);
                    File f = new File(fileName);
                    if (f.exists()) {
                        PropertyGroup pg = ConfigurationService.loadConfiguration(fileName);
                        pgHolder = new CachedObjectHolder<PropertyGroup>(fileName, pg);
                        configCache.put(tenantId, pgHolder);
                        log.fine("Loaded configuration for tenant-id '" + tenantId + "' and added it to the cache.");
                    } else {
                        log.fine("Tenant-specific configuration file for tenant '" + tenantId + "' not present.");
                    }
                }

                return (pgHolder != null ? pgHolder.getCachedObject() : null);
            }
        } finally {
            log.exiting(this.getClass().getName(), "loadConfigurationForTenant");
        }
    }

    /**
     * Clears the entire cache of configuration objects. This can be used perhaps during testing when you need to clear
     * and re-load the configuration.
     */
    public void clearConfiguration() {
        synchronized (configCache) {
            configCache.clear();
        }
    }
}

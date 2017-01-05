/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.config;

import java.io.FileNotFoundException;

public class FHIRConfiguration {

    /**
     * This class serves up a singleton instance of ConfigurationService containing
     * the FHIR Server's configuration.
     */
    public static final String FHIR_SERVER_DEFAULT_CONFIG = "fhir-server-config.json";
    
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

    /**
     * This is our single-instance config object.
     */
    private static PropertyGroup fhirConfig = null;
    
    /**
     * Retrieves the FHIR Server configuration and returns it as a PropertyGroup.
     * @throws FileNotFoundException
     */
    public static synchronized PropertyGroup loadConfiguration() throws Exception {
        if (fhirConfig == null) {
            fhirConfig = ConfigurationService.loadConfiguration(FHIR_SERVER_DEFAULT_CONFIG);
        }
        return fhirConfig;
    }
    
    /**
     * Clears the single-instance configuration object.
     * This can be used perhaps during testing when you need to clear and re-load the configuration.
     */
    public static synchronized void clearConfiguration() {
        fhirConfig = null;
    }
}

/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.blob;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.config.PropertyGroup;

/**
 * Provides a facade on top of the fhir-server-config PropertyGroup structure
 * to simplify access to configuration elements we need for connecting to
 * the Azure Blob API
 */
public class BlobPropertyGroupAdapter {
    private static final Logger logger = Logger.getLogger(BlobPropertyGroupAdapter.class.getName());

    // The property name for describing the user-specific connection details including credentials
    public static final String PROP_CONNECTION_STRING = "connectionString";
    
    // The property name for the Azure Blob container name to use for the tenant
    public static final String PROP_CONTAINER_NAME = "containerName";

    // The property name for the Azure Blob command timeout in seconds
    public static final String PROP_TIMEOUT_SECS = "timeoutSecs";

    // The property name for the optional Azure Blob service version
    public static final String PROP_SERVICE_VERSION = "serviceVersion";
    
    // The property group we are wrapping
    private final PropertyGroup propertyGroup;
    
    public BlobPropertyGroupAdapter(PropertyGroup pg) {
        this.propertyGroup = pg;
    }
    
    /**
     * Get the configured value for the Azure Blob connectionString
     * @return
     */
    public String getConnectionString() {
        try {
            return propertyGroup.getStringProperty(PROP_CONNECTION_STRING);
        } catch (Exception x) {
            logger.log(Level.SEVERE, PROP_CONNECTION_STRING, x);
            throw new IllegalArgumentException("Property group not configured " + PROP_CONNECTION_STRING);
        }
    }

    /**
     * Get the configured value for the keyspace to use for the tenant.
     * @return
     */
    public String getContainerName() {
        try {
            return propertyGroup.getStringProperty(PROP_CONTAINER_NAME);
        } catch (Exception x) {
            logger.log(Level.SEVERE, PROP_CONTAINER_NAME, x);
            throw new IllegalArgumentException("Property group not configured " + PROP_CONTAINER_NAME);
        }
    }

    /**
     * Get the configured value for the Azure Blob service version
     * @return
     */
    public String getServiceVersion() {
        try {
            return propertyGroup.getStringProperty(PROP_SERVICE_VERSION, null);
        } catch (Exception x) {
            logger.log(Level.SEVERE, PROP_SERVICE_VERSION, x);
            throw new IllegalArgumentException("Property group misconfigured " + PROP_SERVICE_VERSION);
        }
    }

    /**
     * Get the {@link Duration} representing the configured timeout
     * @return
     */
    public Duration getTimeout() {
        try {
            int timeoutSeconds = propertyGroup.getIntProperty(PROP_TIMEOUT_SECS, 120);
            return Duration.ofSeconds(timeoutSeconds);
        } catch (Exception x) {
            logger.log(Level.SEVERE, PROP_TIMEOUT_SECS, x);
            throw new IllegalArgumentException("Bad property " + PROP_TIMEOUT_SECS);
        }
    }
}
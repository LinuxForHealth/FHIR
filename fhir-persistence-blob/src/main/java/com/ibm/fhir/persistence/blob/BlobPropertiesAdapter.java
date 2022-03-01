/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob;

import java.util.Properties;

/**
 * Adapter to read Azure Blob configuration information from a {@link Properties} object
 */
public class BlobPropertiesAdapter {
    private static final String PROP_CONNECTION_STRING = "connectionString";
    private static final String PROP_CONTAINER_NAME = "containerName";

    // The Properties being adapted
    private final Properties properties;
    
    public BlobPropertiesAdapter(Properties properties) {
        this.properties = properties;
    }

    /**
     * Get the connection string property
     * @return
     */
    public String getConnectionString() {
        return properties.getProperty(PROP_CONNECTION_STRING);
    }

    /**
     * Get the container name property
     * @return
     */
    public String getContainerName() {
        return properties.getProperty(PROP_CONTAINER_NAME);
    }
}
/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cos.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.PropertyGroup;

/**
 * Provides a facade on top of the fhir-server-config PropertyGroup structure
 * to simplify access to configuration elements we need for connecting to
 * COS
 */
public class CosPropertyGroupAdapter {
    private static final Logger logger = Logger.getLogger(CosPropertyGroupAdapter.class.getName());

    // Property key constants
    public static final String PROP_CREDENTIAL_IBM = "credentialIBM";
    public static final String PROP_BUCKET_NAME = "bucketName";
    public static final String PROP_API_KEY = "apiKey";
    public static final String PROP_SRV_INST_ID = "srvInstId";
    public static final String PROP_LOCATION = "location";
    public static final String PROP_ENDPOINT_URL = "endpoint";
    public static final String PROP_REQUEST_TIMEOUT = "requestTimeout";
    public static final String PROP_SOCKET_TIMEOUT = "socketTimeout";

    // The property group we are wrapping
    private final PropertyGroup propertyGroup;

    public CosPropertyGroupAdapter(PropertyGroup pg) {
        this.propertyGroup = pg;
    }

    /**
     * Helper function to read a String value property
     * @param key
     * @return
     * @throws IllegalArgumentException
     */
    private String getStringProp(String key) throws IllegalArgumentException {
        try {
            return propertyGroup.getStringProperty(key);
        } catch (Exception x) {
            logger.log(Level.SEVERE, key, x);
            throw new IllegalArgumentException("property not configured: " + key);
        }
    }

    /**
     * Return the bucket to use for this tenant
     * @return
     */
    public String getBucketName() {
        return getStringProp(PROP_BUCKET_NAME);
    }

    /**
     * Does the service use IBM auth? (i.e. COS)
     * @return
     */
    public boolean isCredentialIBM() {
        try {
            return propertyGroup.getBooleanProperty(PROP_CREDENTIAL_IBM);
        } catch (Exception x) {
            logger.log(Level.SEVERE, PROP_CREDENTIAL_IBM, x);
            throw new IllegalArgumentException("property not configured: " + PROP_CREDENTIAL_IBM);
        }

    }

    /**
     * @return
     */
    public String getApiKey() {
        return getStringProp(PROP_API_KEY);
    }

    /**
     * @return
     */
    public String getSrvInstId() {
        return getStringProp(PROP_SRV_INST_ID);
    }

    /**
     * @return
     */
    public int getRequestTimeout() {
        try {
            return propertyGroup.getIntProperty(PROP_REQUEST_TIMEOUT, 30000);
        } catch (Exception x) {
            logger.log(Level.SEVERE, PROP_REQUEST_TIMEOUT, x);
            throw new IllegalArgumentException("property not configured: " + PROP_REQUEST_TIMEOUT);
        }
    }

    /**
     * @return
     */
    public String getEndpointUrl() {
        return getStringProp(PROP_ENDPOINT_URL);
    }

    /**
     * @return
     */
    public String getLocation() {
        return getStringProp(PROP_LOCATION);
    }

    /**
     * @return
     */
    public int getSocketTimeout() {
        try {
            return propertyGroup.getIntProperty(PROP_SOCKET_TIMEOUT, 30000);
        } catch (Exception x) {
            logger.log(Level.SEVERE, PROP_SOCKET_TIMEOUT, x);
            throw new IllegalArgumentException("property not configured: " + PROP_SOCKET_TIMEOUT);
        }
    }
}

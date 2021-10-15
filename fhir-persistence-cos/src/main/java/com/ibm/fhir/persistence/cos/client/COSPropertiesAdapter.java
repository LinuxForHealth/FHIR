/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cos.client;

import java.util.Properties;

/**
 * Adapter to support reading of COS properties from a {@link Properties} instance
 */
public class COSPropertiesAdapter implements COSConfigAdapter {

    // The properties we are adapting
    private final Properties properties;
    
    public COSPropertiesAdapter(Properties properties) {
        // intentionally do not copy. We act as a facade, so want
        // to track any changes to our properties delegate
        this.properties = properties;
    }

    @Override
    public String getApiKey() {
        return properties.getProperty(COSPropertyConstants.COS_API_KEY);
    }
    @Override
    public String getSrvInstId() {
        return properties.getProperty(COSPropertyConstants.COS_SRVINSTID);
    }

    @Override
    public String getEndpointUrl() {
        return properties.getProperty(COSPropertyConstants.COS_ENDPOINT_URL);
    }

    @Override
    public String getLocation() {
        return properties.getProperty(COSPropertyConstants.COS_LOCATION);
    }

    @Override
    public String getBucketName() {
        return properties.getProperty(COSPropertyConstants.COS_BUCKET_NAME);
    }

    @Override
    public boolean isCredentialIBM() {
        return "Y".equalsIgnoreCase(properties.getProperty(COSPropertyConstants.COS_CREDENTIAL_IBM));
    }

    @Override
    public int getRequestTimeout() {
        String val = properties.getProperty(COSPropertyConstants.COS_REQUEST_TIMEOUT, "60000");
        return Integer.parseInt(val);
    }

    @Override
    public int getSocketTimeout() {
        String val = properties.getProperty(COSPropertyConstants.COS_SOCKET_TIMEOUT, "60000");
        return Integer.parseInt(val);
    }

    @Override
    public int getMaxKeys() {
        String val = properties.getProperty(COSPropertyConstants.COS_MAX_KEYS);
        if (val != null) {
            return Integer.parseInt(val);
        } else {
            return defaultMaxKeys();
        }
    }
}
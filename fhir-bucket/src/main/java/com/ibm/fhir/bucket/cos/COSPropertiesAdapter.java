/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.cos;

import java.util.Properties;

/**
 * Adapter to support reading of COS properties from a {@link Properties} instance
 */
public class COSPropertiesAdapter {

    // The properties we are adapting
    private final Properties properties;
    
    public COSPropertiesAdapter(Properties properties) {
        // intentionally do not copy. We act as a facade, so want
        // to track any changes to our properties delegate
        this.properties = properties;
    }

    /**
     * Get the API key property
     * @return
     */
    public String getApiKey() {
        return properties.getProperty(COSConstants.COS_API_KEY);
    }

    /**
     * Get the srvinstid property
     * @return
     */
    public String getSrvInstId() {
        return properties.getProperty(COSConstants.COS_SRVINSTID);
    }

    /**
     * Get the endpoint property
     * @return
     */
    public String getEndpointUrl() {
        return properties.getProperty(COSConstants.COS_ENDPOINT_URL);
    }

    /**
     * Get the location property
     * @return
     */
    public String getLocation() {
        return properties.getProperty(COSConstants.COS_LOCATION);
    }

    /**
     * Get the bucket name property
     * @return
     */
    public String getBucketName() {
        return properties.getProperty(COSConstants.COS_BUCKET_NAME);
    }

    /**
     * Get the credential_ibm property
     * @return
     */
    public boolean isCredentialIBM() {
        return "Y".equalsIgnoreCase(properties.getProperty(COSConstants.COS_CREDENTIAL_IBM));
    }

    /**
     * COS request timeout in milliseconds
     * @return
     */
    public int getRequestTimeout() {
        String val = properties.getProperty(COSConstants.COS_REQUEST_TIMEOUT, "60000");
        return Integer.parseInt(val);
    }

    /**
     * COS socket timeout in milliseconds
     * @return
     */
    public int getSocketTimeout() {
        String val = properties.getProperty(COSConstants.COS_SOCKET_TIMEOUT, "60000");
        return Integer.parseInt(val);
    }

    /**
     * Max keys per list objects request
     * @return
     */
    public int getMaxKeys() {
        String val = properties.getProperty(COSConstants.COS_MAX_KEYS, "1000");
        return Integer.parseInt(val);
    }
}

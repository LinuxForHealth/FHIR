/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.cos.client;

/**
 * Common interface for our COS payload configuration adapters
 */
public interface COSConfigAdapter {

    /**
     * Return the explicit bucket name to use for this tenant
     * @return
     */
    String getBucketName();

    /**
     * Does the service use IBM auth? (i.e. COS)
     * @return
     */
    boolean isCredentialIBM();

    /**
     * Get the API Key value
     * @return
     */
    String getApiKey();

    /**
     * Get the COS srvInstid value
     * @return
     */
    String getSrvInstId();

    /**
     * Get the COS request timeout value
     * @return
     */
    int getRequestTimeout();

    /**
     * Get the COS endpoint URL value
     * @return
     */
    String getEndpointUrl();

    /**
     * Get the COS location value
     * @return
     */
    String getLocation();

    /**
     * Get the COS connection socket timeout value
     * @return
     */
    int getSocketTimeout();
    
    /**
     * Get the maximum number of keys value
     * @return
     */
    int getMaxKeys();
    
    /**
     * Get the default value for maxKeys for use when the property is not configured
     * @return
     */
    default int defaultMaxKeys() { return 1000; }

    /**
     * Is the payload stored compressed
     * @return
     */
    boolean isCompress();
}
/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob;


/**
 *
 */
public class BlobPayloadSupport {

    /**
     * Get the path string (blob key) for the given resource parameters
     * @param resourceTypeId
     * @param logicalId
     * @param version
     * @param resourcePayloadKey
     * @return
     */
    public static String getPayloadPath(int resourceTypeId, String logicalId, int version, String resourcePayloadKey) {
        final StringBuilder blobNameBuilder = new StringBuilder();
        blobNameBuilder.append(resourceTypeId);
        blobNameBuilder.append("/");
        blobNameBuilder.append(logicalId);
        blobNameBuilder.append("/");
        blobNameBuilder.append(version);
        blobNameBuilder.append("/");
        blobNameBuilder.append(resourcePayloadKey);
        return blobNameBuilder.toString();
    }
}

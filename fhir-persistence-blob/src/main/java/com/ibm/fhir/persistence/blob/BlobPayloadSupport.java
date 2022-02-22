/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob;

import com.ibm.fhir.persistence.jdbc.dao.api.ResourceRecord;

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

    /**
     * Construct a {@link ResourceRecord} from the path name used
     * to store the payload blob
     * @param path
     * @return
     */
    public static ResourceRecord buildResourceRecordFromPath(String path) {
        String[] elements = path.split("/");
        if (elements.length == 4) {
            final int resourceTypeId = Integer.parseInt(elements[0]);
            final String logicalId = elements[1];
            final int version = Integer.parseInt(elements[2]);
            final String resourcePayloadKey = elements[3];
            return new ResourceRecord(resourceTypeId, logicalId, version, resourcePayloadKey);
        } else {
            throw new IllegalArgumentException("invalid path for payload blob: " + path);
        }
    }
}

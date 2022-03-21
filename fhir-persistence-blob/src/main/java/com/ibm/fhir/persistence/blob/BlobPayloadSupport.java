/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob;

import com.ibm.fhir.persistence.jdbc.dao.api.ResourceRecord;

/**
 * Utility methods supporting the creation and parsing of blob paths
 * used for payload offload
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
        blobNameBuilder.append(encodeLogicalId(logicalId));
        blobNameBuilder.append("/");
        blobNameBuilder.append(version);
        blobNameBuilder.append("/");
        blobNameBuilder.append(resourcePayloadKey);
        return blobNameBuilder.toString();
    }

    /**
     * The FHIR id value is defined as
     *   [A-Za-z0-9\-\.]{1,64}
     * However, Azure Blob paths have specific requirements around the use of '.'
     * so we simply encode it to '*' which is valid for blob paths, but will not
     * be found in a FHIR logical id. See https://www.ietf.org/rfc/rfc2396.txt
     * @param logicalId
     * @return
     */
    public static String encodeLogicalId(String logicalId) {
        return logicalId.replace('.', '*');
    }

    /**
     * Reverse of {@link #encodeLogicalId(String)}.
     * @param encodedLogicalId
     * @return
     */
    public static String decodeLogicalId(String encodedLogicalId) {
        return encodedLogicalId.replace('*', '.');
    }

    /**
     * Construct a {@link ResourceRecord} from the path name used
     * to store the payload blob
     * @param path
     * @return a ResourceRecord built from the 4 path elements, null if less than
     *     4 elements are included
     * @throws IllegalArgumentException if path contains more than 4 elements
     * @throws NumberFormatException if an integer element in the path is not an integer
     */
    public static ResourceRecord buildResourceRecordFromPath(String path) {
        String[] elements = path.split("/");
        if (elements.length == 4) {
            final int resourceTypeId = Integer.parseInt(elements[0]);
            final String logicalId = decodeLogicalId(elements[1]);
            final int version = Integer.parseInt(elements[2]);
            final String resourcePayloadKey = elements[3];
            return new ResourceRecord(resourceTypeId, logicalId, version, resourcePayloadKey, path);
        } else if (elements.length < 4) {
            // partial, which probably means this is just a path directory returned
            // when scanning an Azure Blob container.
            return null;
        } else {
            throw new IllegalArgumentException("invalid payload offload path");
        }
    }
}

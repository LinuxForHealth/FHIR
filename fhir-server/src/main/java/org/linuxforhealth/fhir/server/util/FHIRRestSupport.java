/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.server.util;

import java.net.URI;

import org.linuxforhealth.fhir.model.resource.Resource;

/**
 * Collection of support and utility methods related to the FHIR REST API.
 */
public class FHIRRestSupport {
    public static String getEtagValue(Resource resource) {
        return "W/\"" + resource.getMeta().getVersionId().getValue() + "\"";
    }

    /**
     * Create an ETag header value using the given resource versionId
     * @param versionId
     * @return
     */
    public static String getEtagValue(int versionId) {
        return "W/\"" + versionId + "\"";
    }

    /**
     * Get the ETag value by extracting the version from the locationURI
     * @param locationURI
     * @return
     */
    public static String getEtagValueFromLocation(URI locationURI) {
        String locn = locationURI.toString();
        int idx = locn.lastIndexOf('/');
        if (idx >= 0) {
            return "W/\"" + locn.substring(idx+1) + "\"";
        } else {
            return null;
        }
    }
}
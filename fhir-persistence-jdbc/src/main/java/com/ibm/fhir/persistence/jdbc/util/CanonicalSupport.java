/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceProfileRec;

/**
 * Support class for processing query parameter data extracted from resources.
 */
public class CanonicalSupport {

    /**
     * Process the search parameter value data to generate a {@link ResourceProfileRec} DTO.
     * @param parameterNameId
     * @param resourceType
     * @param resourceTypeId
     * @param logicalResourceId
     * @param paramValue
     * @param systemLevel
     * @return
     * @throws FHIRPersistenceException
     */
    public static ResourceProfileRec makeResourceProfileRec(int parameterNameId, String resourceType, long resourceTypeId, long logicalResourceId,
        String paramValue, boolean systemLevel) throws FHIRPersistenceException {

        // Parse the parameter value to extract the URI|VERSION#FRAGMENT pieces
        String uri = paramValue;
        String version = null;
        String fragment = null;
        int vindex = paramValue.indexOf('|');
        int findex = paramValue.indexOf('#');
        if (vindex == 0 || findex == 0 || vindex > findex && findex > -1) {
            throw new FHIRPersistenceException("Invalid profile URI");
        }

        // Extract version if given
        if (vindex > 0) {
            if (findex > -1) {
                version = paramValue.substring(vindex+1, findex); // everything after the | but before the #
            } else {
                version = paramValue.substring(vindex+1); // everything after the |
            }
            if (version.isEmpty()) {
                version = null;
            }
            uri = paramValue.substring(0, vindex); // everything before the |
        }

        // Extract fragment if given
        if (findex > 0) {
            fragment = paramValue.substring(findex+1);
            if (fragment.isEmpty()) {
                fragment = null;
            }

            if (vindex < 0) {
                // fragment but no version
                uri = paramValue.substring(0, findex); // everything before the #
            }
        }

        return new ResourceProfileRec(parameterNameId, resourceType, resourceTypeId, logicalResourceId, uri, version, fragment, systemLevel);
    }
}
/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.util;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.ResourceProfileRec;

/**
 * Support class for processing query parameter data extracted from resources.
 */
public class CanonicalSupport {

    /**
     * Process the search parameter value data to generate a {@link ResourceProfileRec} DTO.
     * @param parameterName
     * @param resourceType
     * @param resourceTypeId
     * @param logicalResourceId
     * @param paramValue
     * @param systemLevel
     * @return
     * @throws FHIRPersistenceException
     */
    public static ResourceProfileRec makeResourceProfileRec(String parameterName, String resourceType, long resourceTypeId, long logicalResourceId,
        String paramValue, boolean systemLevel) throws FHIRPersistenceException {

        CanonicalValue canonicalValue = createCanonicalValueFrom(paramValue);

        return new ResourceProfileRec(parameterName, resourceType, resourceTypeId, logicalResourceId,
            canonicalValue.getUri(), canonicalValue.getVersion(), canonicalValue.getFragment(), systemLevel);
    }
    
    /**
     * Process the canonical value data.
     * @param canonicalValue
     * @return
     */
    public static CanonicalValue createCanonicalValueFrom(String canonicalValue) {
        try {
            return parseCanonicalValue(canonicalValue);
        } catch (FHIRPersistenceException e) {
            // Not a valid version/fragment format - just return the input string as uri
            return new CanonicalValue(canonicalValue, null, null);
        }
    }
    
    /**
     * Parse the canonical value.
     * @param canonicalValue
     * @return
     */
    private static CanonicalValue parseCanonicalValue(String canonicalValue) throws FHIRPersistenceException {
        String uri = canonicalValue;
        String version = null;
        String fragment = null;
        
        // Parse the canonical value to extract the URI|VERSION#FRAGMENT pieces
        if (canonicalValue != null) {
            int vindex = canonicalValue.indexOf('|');
            int findex = canonicalValue.indexOf('#');
            if (vindex == 0 || findex == 0 || vindex > findex && findex > -1) {
                throw new FHIRPersistenceException("Invalid canonical URI");
            }
            
            // Extract version if given
            if (vindex > 0) {
                if (findex > -1) {
                    version = canonicalValue.substring(vindex+1, findex); // everything after the | but before the #
                } else {
                    version = canonicalValue.substring(vindex+1); // everything after the |
                }
                if (version.isEmpty()) {
                    version = null;
                }
                uri = canonicalValue.substring(0, vindex); // everything before the |
            }

            // Extract fragment if given
            if (findex > 0) {
                fragment = canonicalValue.substring(findex+1);
                if (fragment.isEmpty()) {
                    fragment = null;
                }

                if (vindex < 0) {
                    // fragment but no version
                    uri = canonicalValue.substring(0, findex); // everything before the #
                }
            }
        }
        
        return new CanonicalValue(uri, version, fragment);
    }
}
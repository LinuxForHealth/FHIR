/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Utility methods supporting the processing of profile search
 * parameters which are stored using common_canonical_values
 */
public class CanonicalSupport {

    /**
     * Split the given string value to extract the profile url, version
     * and fragment parts if they exist
     * @param stringValue
     * @return
     */
    public static ProfileParameter createProfileParameter(String name, String stringValue) {
        ProfileParameter result;
        try {
            result = parseCanonicalValue(stringValue);
        } catch (FHIRPersistenceException e) {
            // Not a valid version/fragment format - just use the input string as the whole uri
            result = new ProfileParameter();
            result.setUrl(stringValue);
        }
        result.setName(name);
        return result;
    }

    /**
     * Parse the canonical value.
     * @param canonicalValue
     * @return
     */
    private static ProfileParameter parseCanonicalValue(String canonicalValue) throws FHIRPersistenceException {
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
        
        ProfileParameter result = new ProfileParameter();
        result.setUrl(uri);
        result.setVersion(version);
        result.setFragment(fragment);
        return result;
    }
}

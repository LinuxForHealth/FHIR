/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;

import com.ibm.fhir.model.type.Reference;

public enum ReferenceType {
    
    NO_REFERENCE_VALUE,
    CONTAINED,
    ABSOLUTE_FHIR_URL,
    RELATIVE_FHIR_URL,
    ABSOLUTE_OTHER_URL,
    ABSOLUTE_OID,
    ABSOLUTE_UUID,
    OTHER,
    INVALID;
    
    public static ReferenceType of(Reference ref) {
        if (ref == null || ref.getReference() == null || ref.getReference().getValue() == null) {
            return ReferenceType.NO_REFERENCE_VALUE;
        }
        String referenceUriString = ref.getReference().getValue();
        if (referenceUriString.startsWith("#")) {
            return CONTAINED;
        }
        
        URI referenceUri;
        try {
            referenceUri = new URI(referenceUriString);
        } catch (URISyntaxException e) {
            return INVALID;
        }
        
        Matcher restUrlMatcher = FHIRUtil.REFERENCE_PATTERN.matcher(referenceUriString);
        boolean isFhirUrl = restUrlMatcher.matches();
        
        if (isFhirUrl) {
            if (referenceUri.isAbsolute()) {
                return ABSOLUTE_FHIR_URL;
            } else {
                return RELATIVE_FHIR_URL;
            }
        } else if (referenceUriString.startsWith("http://") || referenceUriString.startsWith("https://")){
            return ABSOLUTE_OTHER_URL;
        } else if (referenceUriString.startsWith("urn:oid:")){
            return ABSOLUTE_OID;
        } else if (referenceUriString.startsWith("urn:uuid:")) {
            return ABSOLUTE_UUID;
        } else {
            return OTHER;
        }
    }
}

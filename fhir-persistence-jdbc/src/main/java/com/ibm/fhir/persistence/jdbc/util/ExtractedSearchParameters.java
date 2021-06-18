/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;


import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;

/**
 * Contains a list of extracted search parameters and a Base64-encoded SHA-256 hash.
 */
public class ExtractedSearchParameters {

    private List<ExtractedParameterValue> parameters = new ArrayList<>();
    private String hashB64 = null;

    /**
     * Gets the parameters.
     * @return the parameters
     */
    public List<ExtractedParameterValue> getParameters() {
        return parameters;
    }

    /**
     * Generates the Base64-encoded SHA-256 hash of the parameters.
     * @param the parameter hash utility to use for generating the hash
     */
    public void generateHash(ParameterHashUtil parameterHashUtil) {
        hashB64 = parameterHashUtil.getParametersHash(parameters);
    }

    /**
     * Gets the already-generated Base64-encoded SHA-256 hash of the parameters.
     * @return the Base64 encoded SHA-256 hash
     */
    public String getHash() {
        return hashB64;
    }
}

/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;


import java.util.Collections;
import java.util.List;

import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;

/**
 * Contains a list of extracted search parameters and a Base64-encoded hash.
 */
public class ExtractedSearchParameters {

    private final List<ExtractedParameterValue> parameters;
    private final String parameterHashB64;

    public ExtractedSearchParameters(List<ExtractedParameterValue> parameters, String parameterHashB64) {
        this.parameters = Collections.unmodifiableList(parameters);
        this.parameterHashB64 = parameterHashB64;
    }

    /**
     * Gets the parameters.
     * @return the parameters
     */
    public List<ExtractedParameterValue> getParameters() {
        return parameters;
    }

    /**
     * Gets the Base64-encoded hash of the parameters.
     * @return the Base64-encoded hash
     */
    public String getParameterHashB64() {
        return parameterHashB64;
    }
}

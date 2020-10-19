/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.search.reference.value;

import java.util.List;

import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * Parameter Value handles the various use cases for the implementations and use-cases.
 */
public interface ParameterValueHandler {

    /**
     * process parameters beyond the original valueString.
     * If the valueString is included or matches the current type/case, it is not added.
     *
     * @param incomingUrl
     * @param targets
     * @param parameterValues
     * @param valueString
     * @param values
     */
    public void processParameter(
        String incomingUrl,
        List<String> targets,
        List<QueryParameterValue> parameterValues,
        String valueString,
        List<String> values);
}
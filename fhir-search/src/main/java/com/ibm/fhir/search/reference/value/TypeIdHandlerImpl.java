/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.search.reference.value;

import java.util.List;

import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * The supported cases are:
 * Patient/1
 * -> http://server/Patient/1
 */
public class TypeIdHandlerImpl implements ParameterValueHandler {
    @Override
    public void processParameter(String incoming, List<String> targets, List<QueryParameterValue> parameterValues, String valueString, List<String> values) {
        if (!valueString.contains(":") && valueString.contains("/")) {
            String requestUriString = incoming.split("\\?")[0];
            String tmpValueUrl = requestUriString + "/" + valueString;
            if (!values.contains(tmpValueUrl)) {
                QueryParameterValue parameterValue = new QueryParameterValue();
                parameterValue.setValueString(tmpValueUrl);
                parameterValue.setHidden(true);
                parameterValues.add(parameterValue);
                values.add(tmpValueUrl);
            }
        }
    }
}
/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.search.reference.value;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * The supported cases are:
 * Patient/1
 * -> http://server/Patient/1
 * -> 1 (only when there is one target)
 *
 * vread
 * Patient/1/_history/1
 * -> http://server/Patient/1/_history/1
 * -> 1 (only when there is one target)
 */
public class TypeIdHandlerImpl implements ParameterValueHandler {

    private static final String REGEX = "^([A-z][a-z]{2,36}/[A-Za-z0-9\\-\\.]{1,64})(/_history/[A-Za-z0-9\\-\\.]{1,64})?$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public void processParameter(String incoming, List<String> targets, List<QueryParameterValue> parameterValues, String valueString, List<String> values) {
        Matcher matcher = PATTERN.matcher(valueString);
        if (matcher.find()) {
            String requestUriString = incoming.split("\\?")[0];
            String tmpValueUrl = requestUriString + "/" + valueString;
            if (!values.contains(tmpValueUrl)) {
                QueryParameterValue parameterValue = new QueryParameterValue();
                parameterValue.setValueString(tmpValueUrl);
                parameterValue.setHidden(true);
                parameterValues.add(parameterValue);
                values.add(tmpValueUrl);
            }

            if (targets.size() == 1) {
                tmpValueUrl = valueString.substring(valueString.indexOf('/') + 1);
                QueryParameterValue parameterValue = new QueryParameterValue();
                parameterValue.setValueString(tmpValueUrl);
                parameterValue.setHidden(true);
                parameterValues.add(parameterValue);
                values.add(tmpValueUrl);
            }
        }
    }
}

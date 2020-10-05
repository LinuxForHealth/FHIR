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
 * Converts the Single ID to multiple possible cases:
 * 1
 * -> <targets>/1
 * -> http://server/<targets>/1
 *
 * @implNote we don't worry about canonical case, as there are so many iterations of that case.
 */
public class IdHandlerImpl implements ParameterValueHandler {

    private static final String REGEX = "^[A-Za-z0-9\\-\\.]{1,64}$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public void processParameter(final String incoming,
        List<String> targets,
        List<QueryParameterValue> parameterValues,
        String valueString,
        List<String> values) {
        // Checking to see if it's actually a ID
        Matcher matcher = PATTERN.matcher(valueString);
        if (matcher.find()) {
            // Inferred Target from the SearchParameter
            for (String target : targets) {
                String tmpValue = target + "/" + valueString;
                if (!values.contains(tmpValue)) {
                    QueryParameterValue parameterValue = new QueryParameterValue();
                    parameterValue.setValueString(tmpValue);
                    parameterValue.setHidden(true);
                    parameterValues.add(parameterValue);
                    values.add(tmpValue);
                }

                String tmpValueUrl = incoming + "/" + target + "/" + valueString;
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
}
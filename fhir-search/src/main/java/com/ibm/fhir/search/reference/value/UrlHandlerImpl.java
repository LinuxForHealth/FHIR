/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.search.reference.value;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * The supported cases are:
 * http://server/Patient/1
 * -> http://server/Patient/1
 * -> Patient/1
 * -> 1 (only when there is one target)
 */
public class UrlHandlerImpl implements ParameterValueHandler {

    private static final String REGEX = "/([A-z][a-z]{2,36}/[A-Za-z0-9\\-\\.]{1,64})(/_history/[A-Za-z0-9\\-\\.]{1,64})?$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public void processParameter(String incoming, List<String> targets, List<QueryParameterValue> parameterValues, String valueString, List<String> values) {
        try {
            URL aURL = new URL(valueString);
            String path = aURL.getPath();

            Matcher matcher = PATTERN.matcher(path);
            if (matcher.find()) {
                // For instance, Patient/1
                String typeId;
                if (matcher.group(2) == null) {
                    typeId = matcher.group(1);
                } else {
                    typeId = matcher.group(1) + matcher.group(2);
                }

                if (!values.contains(typeId)) {
                    QueryParameterValue parameterValue = new QueryParameterValue();
                    parameterValue.setValueString(typeId);
                    parameterValue.setHidden(true);
                    parameterValues.add(parameterValue);
                    values.add(typeId);
                }

                // For instance, 1
                int lastIndex = typeId.indexOf('/');
                String id = typeId.substring(lastIndex + 1);

                // Only if there is one possible target do we strip down to 1
                // otherwise problematic and inaccurate results may be returned
                if (!values.contains(id) && targets.size() == 1) {
                    QueryParameterValue parameterValue = new QueryParameterValue();
                    parameterValue.setValueString(id);
                    parameterValue.setHidden(true);
                    parameterValues.add(parameterValue);
                    values.add(id);
                }
            }
        } catch (MalformedURLException e) {
            // We're not a URL, skip processing.
        }
    }
}

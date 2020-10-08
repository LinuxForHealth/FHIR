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
 * -> 1
 */
public class UrlHandlerImpl implements ParameterValueHandler {

    private static final String REGEX = "/([A-z][a-z]{2,36}/[A-Za-z0-9\\-\\.]{1,64})$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public void processParameter(String incoming, List<String> targets, List<QueryParameterValue> parameterValues, String valueString, List<String> values) {
        try {
            URL aURL = new URL(valueString);
            String path = aURL.getPath();

            Matcher matcher = PATTERN.matcher(path);
            if (matcher.find()) {
                // For instance, Patient/1
                String typeId = matcher.group().substring(1);
                if (!values.contains(typeId)) {
                    QueryParameterValue parameterValue = new QueryParameterValue();
                    parameterValue.setValueString(typeId);
                    parameterValue.setHidden(true);
                    parameterValues.add(parameterValue);
                    values.add(typeId);
                }
                System.out.println(values);

                // For instance, 1
                int lastIndex = typeId.lastIndexOf('/');
                String id = typeId.substring(lastIndex + 1);
                if (!values.contains(id)) {
                    QueryParameterValue parameterValue = new QueryParameterValue();
                    parameterValue.setValueString(id);
                    parameterValue.setHidden(true);
                    parameterValues.add(parameterValue);
                    values.add(id);
                }
                System.out.println(values);
            }
        } catch (MalformedURLException e) {
            // We're not a URL, skip processing.
        }
    }
}
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
 * http://server/Patient/1
 * -> http://server/Patient/1
 * -> Patient/1
 * -> 1
 *
 * @implNote the code does not yet support versions or fragments
 */
public class CanonicalUrlHandlerImpl implements ParameterValueHandler {

    private static final UrlHandlerImpl handler = new UrlHandlerImpl();

    @Override
    public void processParameter(String incoming,
        List<String> targets,
        List<QueryParameterValue> parameterValues,
        String valueString,
        List<String> values) {

        // At some point we'll want to care about versions of references.
        // split on the pound sign then.
        int idxOfPipe = valueString.indexOf('|');
        if (idxOfPipe > 0) {
            String tmp = valueString.substring(0, idxOfPipe);
            System.out.println(tmp);
            handler.processParameter(incoming, targets, parameterValues, tmp, values);
        }
    }
}
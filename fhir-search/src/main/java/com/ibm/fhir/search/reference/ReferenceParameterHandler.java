/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.search.reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.reference.value.CanonicalUrlHandlerImpl;
import com.ibm.fhir.search.reference.value.IdHandlerImpl;
import com.ibm.fhir.search.reference.value.ParameterValueHandler;
import com.ibm.fhir.search.reference.value.TypeIdHandlerImpl;
import com.ibm.fhir.search.reference.value.UrlHandlerImpl;

/**
 * Reference Parameter Handler controls the access to the underlying implementation of the handler
 *
 * <li><b>reference</b></li>
 * <li>[parameter]=[url]</li>
 * <li>[parameter]=[url|version] - canonical url</li>
 * <li>[parameter]=[type]/[id]</li>
 * <li>[parameter]=[id]</li>
 */
public class ReferenceParameterHandler {

    //@formatter:off
    private static final List<ParameterValueHandler> handlers = Arrays.asList(
            new CanonicalUrlHandlerImpl(),
            new IdHandlerImpl(),
            new TypeIdHandlerImpl(),
            new UrlHandlerImpl()
        );
    //@formatter:on

    private ReferenceParameterHandler() {
        // No Operation
    }

    /**
     * generates References Parameter Values.
     *
     * @param parameter
     * @param parameterValues
     * @param valueString
     * @param values
     * @param modifierResourceTypeName
     */
    public static void generateReferenceParameterValues(SearchParameter parameter, List<QueryParameterValue> parameterValues, String valueString,
        String[] values, String modifierResourceTypeName) {
        // The incomingUrl is taken from the Request Context, else we inline a valid incomingUrl.
        String incomingUrl = FHIRRequestContext.get().getOriginalRequestUri();
        String requestUriString = incomingUrl != null ? incomingUrl.split("\\?")[0] : "/v4/";
        int idx = requestUriString.lastIndexOf('/');

        String tmp = requestUriString.substring(0, idx);
        final String output;
        if (tmp.endsWith("v4")) {
            output = tmp;
        } else {
            // _search or resource type
            int idxSlash = tmp.lastIndexOf('/');
            output = tmp.substring(0, idxSlash);
        }

        // Only if name is not null do we override the target.
        List<String> targets;
        if (modifierResourceTypeName != null) {
            targets = Arrays.asList(modifierResourceTypeName);
        } else {
            // We know that the parameter.getTargets is never null, but it could be empty.
            targets = parameter.getTarget().stream().map(target -> target.getValue()).collect(Collectors.toList());
        }

        // Process through each of the Handlers, the intent here is to have stateless processing for each case.
        List<String> valuesList = new ArrayList<>();
        valuesList.addAll(Arrays.asList(values));
        handlers.stream().forEach(handler -> {
            handler.processParameter(output, targets, parameterValues, valueString, valuesList);
        });
    }
}
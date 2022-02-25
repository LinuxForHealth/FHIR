/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.compartment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Information about a single compartment type.
 */
public class CompartmentCache {

    /**
     * A map from the includable resourceType codes (resourceType name) to their inclusion criteria params
     */
    private Map<java.lang.String, List<java.lang.String>> codeAndParams = new HashMap<>();

    /**
     * constructor
     */
    public CompartmentCache() {
        super();
    }

    /**
     * add the code and parameters to the given compartment cache.
     *
     * @param inclusionResourceCode the name of the resource type that can be within the target compartment type
     * @param params the inclusion criteria used to determine whether a resource of type {@code inclusionResourceCode}
     *      is in a target compartment
     */
    public void add(java.lang.String inclusionResourceCode, List<com.ibm.fhir.model.type.String> params) {
        if (params != null) {
            // Fast Conversion to java.lang.String
            List<String> paramsAsStrings = params.stream().map(param -> param.getValue()).collect(Collectors.toList());
            codeAndParams.put(inclusionResourceCode, paramsAsStrings);
        }
    }

    /**
     * gets the resource types (codes) in the compartment
     *
     * @return
     */
    public List<String> getResourceTypesInCompartment() {
        return Collections.unmodifiableList(new ArrayList<String>(codeAndParams.keySet()));
    }

    /**
     * get parameters by resource type in the compartment cache.
     *
     * @param resourceType
     * @return
     */
    public List<String> getParametersByResourceTypeInCompartment(String resourceType) {
        List<String> results;
        if (resourceType != null && codeAndParams.containsKey(resourceType)) {
            results = Collections.unmodifiableList(codeAndParams.get(resourceType));
        } else {
            results = Collections.unmodifiableList(Collections.emptyList());
        }
        return results;
    }
}

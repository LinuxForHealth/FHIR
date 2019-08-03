/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.compartment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Compartment Cache is a localized class to cache the compartment information and provide helper methods to add to the
 * cache.
 * 
 * 
 * @author pbastide
 *
 */
public class CompartmentCache {

    private Map<java.lang.String, List<java.lang.String>> codeAndParams = new HashMap<>();

    /**
     *
     */
    public CompartmentCache() {
        super();
    }

    /**
     * add the code and parameters to the given compartment cache.
     * 
     * @param code
     * @param params
     */
    public void add(java.lang.String inclusionCode, List<com.ibm.watsonhealth.fhir.model.type.String> params) {
        if (params != null) {
            // Fast Conversion to java.lang.String
            List<String> paramsAsStrings = params.stream().map(param -> param.getValue()).collect(Collectors.toList());
            codeAndParams.put(inclusionCode, paramsAsStrings);
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

/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.compartment;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Information about a single compartment type.
 */
public class CompartmentCache {

    /**
     * A map from the includable resourceType codes (resourceType name) to their inclusion criteria params
     */
    private Map<java.lang.String, Set<java.lang.String>> codeAndParams = new HashMap<>();

    /**
     * constructor
     */
    public CompartmentCache() {
        super();
    }

    /**
     * Add the code and parameters to the given compartment cache.
     *
     * @param inclusionResourceCode the name of the resource type that can be within the target compartment type
     * @param params the inclusion criteria used to determine whether a resource of type {@code inclusionResourceCode}
     *      is in a target compartment
     */
    public void add(java.lang.String inclusionResourceCode, List<org.linuxforhealth.fhir.model.type.String> params) {
        if (params != null) {
            // Fast Conversion to java.lang.String
            Set<String> paramsAsStrings = params.stream().map(param -> param.getValue()).collect(Collectors.toSet());
            codeAndParams.put(inclusionResourceCode, paramsAsStrings);
        }
    }

    /**
     * Get the resource types (codes) that can be in a compartment of this type.
     *
     * @return
     */
    public Set<String> getResourceTypesInCompartment() {
        return Collections.unmodifiableSet(codeAndParams.keySet());
    }

    /**
     * Get parameters by resource type in the compartment cache.
     *
     * @param resourceType
     * @return
     */
    public Set<String> getParametersByResourceTypeInCompartment(String resourceType) {
        Set<String> results;
        if (resourceType != null && codeAndParams.containsKey(resourceType)) {
            results = Collections.unmodifiableSet(codeAndParams.get(resourceType));
        } else {
            results = Collections.emptySet();
        }
        return results;
    }
}

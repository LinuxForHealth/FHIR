/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.compartment;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Information about a specific resource type and which compartments it can be within.
 */
public class ResourceCompartmentCache {

    /**
     * Map from parameter name to a set of compartment names
     */
    private Map<String, Set<java.lang.String>> paramCompartmentMap = new HashMap<>();

    /**
     * constructor
     */
    public ResourceCompartmentCache() {
        super();
    }

    /**
     * Add the parameters which point to the given compartment. Note that the
     * same parameter can be used to point to more than compartment, e.g. for
     * CareTeam, the participant parameter may refer to a Patient or a RelatedPerson.
     * In the schema, we therefore have to store (unique) values for this parameter
     * as both patient_compartment and relatedperson_compartment token references.
     * @param params a list of model parameter names
     * @param compartmentName the compartment associated with these parameters
     */
    public void add(List<com.ibm.fhir.model.type.String> params, java.lang.String compartmentName) {
        if (params != null) {
            // Fast Conversion to java.lang.String
            List<String> paramsAsStrings = params.stream().map(param -> param.getValue()).collect(Collectors.toList());

            for (java.lang.String paramName: paramsAsStrings) {
                Set<java.lang.String> compartmentNames = paramCompartmentMap.get(paramName);
                if (compartmentNames == null) {
                    compartmentNames = new HashSet<>();
                    paramCompartmentMap.put(paramName, compartmentNames);
                }
                compartmentNames.add(compartmentName);
            }
        }
    }

    /**
     * Getter for the set of parameters referencing compartments
     * @return
     */
    public Map<String, Set<java.lang.String>> getCompartmentReferenceParams() {
        return Collections.unmodifiableMap(this.paramCompartmentMap);
    }
}

/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.search.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.core.ResourceType;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.model.type.code.SearchParamType.Value;
import com.ibm.fhir.search.util.SearchHelper;

public class IncludesUtil {
    private static final Logger LOG = java.util.logging.Logger.getLogger(IncludesUtil.class.getName());

    /**
     * Computes the list of supported include parameter values that target resource types from the passed set of resourceTypesToInclude.
     *
     * @param baseResourceType
     * @param resourceTypesToInclude
     * @param searchHelper
     * @return The list of values to use for the _include search parameter.
     * @throws Exception
     */
    public static List<String> computeIncludesParamValues(String baseResourceType, Set<String> resourceTypesToInclude, SearchHelper searchHelper)
            throws Exception {
        List<String> result = new ArrayList<>();

        List<String> allowedIncludesList = FHIRConfigHelper.getSearchPropertyRestrictions(baseResourceType, FHIRConfigHelper.SEARCH_PROPERTY_TYPE_INCLUDE);
        Set<String> allowedIncludes = (allowedIncludesList == null) ? null : new HashSet<>(allowedIncludesList);

        Map<ResourceType, List<String>> codesByType = IncludesUtil.getSearchCodesByType(baseResourceType, searchHelper);

        for (Map.Entry<ResourceType, List<String>> entry : codesByType.entrySet()) {
            if (resourceTypesToInclude.contains(entry.getKey().value())) {
                result.addAll(computeIncludes(baseResourceType, entry.getKey(), allowedIncludes, entry.getValue()));
            }
        }

        return result;
    }

    /**
     * @param compartmentMemberType
     * @param searchHelper
     * @return a map from resource target type to the list of reference search parameter codes that can target that type
     * @throws Exception
     */
    static Map<ResourceType, List<String>> getSearchCodesByType(String compartmentMemberType, SearchHelper searchHelper) throws Exception {
        Map<ResourceType, List<String>> codesByType = new HashMap<>();

        Map<String, SearchParameter> allSearchParameters = searchHelper.getSearchParameters(compartmentMemberType);
        for (Map.Entry<String, SearchParameter> entry : allSearchParameters.entrySet()) {
            SearchParameter sp = entry.getValue();
            if (sp.getType().getValueAsEnum() != Value.REFERENCE) {
                continue;
            }

            String expression = sp.getExpression().getValue();
            for (ResourceTypeCode target : sp.getTarget()) {
                // The search parameter target types come from the possible types of the target reference
                // and don't take into account any "is X" filters from within the expression,
                // so we use this heuristic to avoid adding the code when expression filtering applies
                // and does not include a filter clause for this particular target type.
                if (expression.contains(".is(") || expression.contains(" is ")) {
                    if (!expression.contains(".is(" + target.getValue()) && !expression.contains(" is " + target.getValue())) {
                        continue;
                    }
                }

                codesByType.computeIfAbsent(target.getValueAsEnum(), rt -> new ArrayList<>())
                        .add(entry.getKey());
            }
        }
        return codesByType;
    }


    /**
     * @param compartmentMemberType the resource type that is the source of the includes
     * @param subResourceType the target resource type of the includes; null
     * @param allowedIncludes the set of include parameter values to allow
     * @param codes the search parameter codes to include on
     * @return A list of parameter values to use for the _include query parameter.
     * @throws Exception
     */
    private static List<String> computeIncludes(String compartmentMemberType, ResourceType subResourceType,
            Set<String> allowedIncludes, List<String> codes) throws Exception {
        List<String> includes = new ArrayList<>();

        for (String code : codes) {
            // Need to make sure the search parameter has not been excluded
            String parameterName = compartmentMemberType + ":" + code + ":" + subResourceType.value();
            String simplifiedParameterName = compartmentMemberType + ":" + code;

            boolean isAllowed = allowedIncludes == null ||
                    allowedIncludes.contains(parameterName) ||
                    allowedIncludes.contains(simplifiedParameterName);

            if (isAllowed) {
                includes.add(parameterName);
            } else if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Filtering out searchParameter include because it is not supported by the server config: " + parameterName);
            }
        }

        return includes;
    }
}

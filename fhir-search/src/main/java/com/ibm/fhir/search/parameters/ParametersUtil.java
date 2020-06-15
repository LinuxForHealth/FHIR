/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.parameters;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.search.SearchConstants;

/**
 * Refactored the PopulateSearchParameterMap code, and marked class as final so there are no 'children' and inheritance
 * which overwrites the behaviors of the buildInSearchParameters.
 *
 * <br>
 * Call {@link #init()} before using the class in order to avoid a slight performance hit on first use.
 */
public final class ParametersUtil {

    private static final String CLASSNAME = ParametersUtil.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    public static final String FHIR_PATH_BUNDLE_ENTRY = "entry.resource";

    // RESOURCE ONLY to fast lookup.
    private static final List<String> RESOURCE_ONLY = Arrays.asList("Binary", "Bundle", "Parameters", SearchConstants.DOMAIN_RESOURCE_RESOURCE);

    public static final String FHIR_DEFAULT_SEARCH_PARAMETERS_FILE = "search-parameters.json";
    public static final String FROM_STEAM = "from_stream";

    // Exceptions:
    public static final String ERROR_EXCEPTION = "Error condition reading the FHIR Bundle of Search Parameters -> %s ";
    public static final String BUILTIN_ERROR_EXCEPTION = String.format(ERROR_EXCEPTION, FHIR_DEFAULT_SEARCH_PARAMETERS_FILE);
    public static final String STREAM_ERROR_EXCEPTION = String.format(ERROR_EXCEPTION, FROM_STEAM);
    private static final String NO_MATCH_ON_NAME_CODE = "The code and name of the search parameter does not match [%s] [%s]";

    // Logging:
    public static final String LOG_PARAMETERS = "Parameter is loaded -> %s";
    public static final String MISSING_EXPRESSION_WARNING = "Skipping parameter '%s' with missing expression";
    public static final String LOG_HEADER = "BASE:RESOURCE_NAME:SearchParameter";
    public static final String LOG_SIZE = "Size: %s";
    private static final String LOG_OUTPUT = "%s|%s|%s";

    private static final String LEFT = "[";
    private static final String RIGHT = "]";
    private static final String COMMA = ",";
    private static final String EQUALS = "=";

    private static final Map<String, ParametersMap> builtInSearchParameters = loadBuiltIns();

    private static final String MISSING_EXPRESSION = "/NONE/";

    private ParametersUtil() {
        // No Operation
    }

    /**
     * Loads the class in the classloader to initialize static members.
     * Call this before using the class in order to avoid a slight performance hit on first use.
     */
    public static void init() {
        // No Operation
    }

    /**
     * Loads the built-in search parameters and constructs .
     *
     * @return a map of ParametersMaps, keyed by resourceType
     */
    private static Map<String, ParametersMap> loadBuiltIns() {
        try {
            return buildSearchParametersMap();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Unexpected error while loading built-in search parameters", e);
        }
        return Collections.emptyMap();
    }

    private static Map<String, ParametersMap> buildSearchParametersMap() {
        Map<String, ParametersMap> typeToParamMap = new HashMap<>();

        for (SearchParameter parameter : getSearchParameters()) {
            // Conditional Logging intentionally avoids forming of the String.
            if (log.isLoggable(Level.FINE)) {
                log.fine(String.format(LOG_PARAMETERS, parameter.getCode().getValue()));
            }

            if (parameter.getExpression() == null || !parameter.getExpression().hasValue()) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine(String.format(MISSING_EXPRESSION, parameter.getCode().getValue()));
                }
            } else {
                /*
                 * In R4, SearchParameter changes from a single Base resource to an array.
                 * As Base is an array, there are going be potential collisions in the map.
                 */
                List<ResourceType> types = parameter.getBase();
                for (ResourceType type : types) {
                    String base = type.getValue();

                    ParametersMap map = typeToParamMap.get(base);
                    if (map == null) {
                        map = new ParametersMap();
                        typeToParamMap.put(base, map);
                    }

                    // check and warn if the parameter name and code do not agree.
                    String code = parameter.getCode().getValue();
                    String name = parameter.getName().getValue();
                    checkAndWarnForIssueWithCodeAndName(code, name);

                    // add the map entry with keys for both the code and the url
                    map.insert(code, parameter.getUrl().getValue(), parameter);
                }
            }
        }

        // Return an unmodifiable copy, lest there be side effects.
        return Collections.unmodifiableMap(assignInheritedToAll(typeToParamMap));
    }

    private static List<SearchParameter> getSearchParameters() {
        List<SearchParameter> searchParameters = new ArrayList<>(2048);
        for (SearchParamType.ValueSet searchParamType : SearchParamType.ValueSet.values()) {
            searchParameters.addAll(FHIRRegistry.getInstance().getSearchParameters(searchParamType.value()));
        }
        return searchParameters;
    }

    /**
     * Builds a Map of ParameterMaps from the passed Bundle.
     *
     * @param bundle a Bundle of type Collection with entries of type SearchParameter
     * @return a Map of ParameterMaps, keyed by resourceType
     * @throws ClassCastException if the Bundle contains entries of any type other than SearchParameter
     */
    public static Map<String, ParametersMap> buildSearchParametersMapFromBundle(Bundle bundle) {
        Map<String, ParametersMap> typeToParamMap = new HashMap<>();

        for (Bundle.Entry entry : bundle.getEntry()) {
            SearchParameter parameter = entry.getResource().as(SearchParameter.class);

            // Conditional Logging intentionally avoids forming of the String.
            if (log.isLoggable(Level.FINE)) {
                log.fine(String.format(LOG_PARAMETERS, parameter.getCode().getValue()));
            }

            if (parameter.getExpression() == null || !parameter.getExpression().hasValue()) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine(String.format(MISSING_EXPRESSION, parameter.getCode().getValue()));
                }
            } else {
                /*
                 * In R4, SearchParameter changes from a single Base resource to an array.
                 * As Base is an array, there are going be potential collisions in the map.
                 */
                List<ResourceType> types = parameter.getBase();
                for (ResourceType type : types) {
                    String base = type.getValue();

                    ParametersMap map = typeToParamMap.get(base);
                    if (map == null) {
                        map = new ParametersMap();
                        typeToParamMap.put(base, map);
                    }

                    // check and warn if the parameter name and code do not agree.
                    String code = parameter.getCode().getValue();
                    String name = parameter.getName().getValue();
                    checkAndWarnForIssueWithCodeAndName(code, name);

                    // add the map entry with keys for both the code and the url
                    map.insert(code, parameter.getUrl().getValue(), parameter);
                }
            }
        }

        // Return an unmodifiable copy, lest there be side effects.
        return Collections.unmodifiableMap(assignInheritedToAll(typeToParamMap));
    }

    /**
     * checks and warns if name and code are not equivalent.
     *
     * @param code
     * @param name
     */
    static void checkAndWarnForIssueWithCodeAndName(String code, String name) {
        // Name A natural language name identifying the search parameter. This name should be usable as an identifier
        // for the module by machine processing applications such as code generation.
        // @see https://www.hl7.org/fhir/searchparameter-definitions.html#SearchParameter.name

        // Code is the code used in the URL or the parameter name in a parameters resource for this search parameter.
        // @see https://www.hl7.org/fhir/searchparameter-definitions.html#SearchParameter.code

        if (code != null && name != null && !code.equals(name) && log.isLoggable(Level.FINE)) {
            // Note, this is conditionally output, while the code assist complains it is not.
            log.fine(String.format(NO_MATCH_ON_NAME_CODE, code, name));
        }
    }

    /**
     * @return a map of maps
     *         The outer map is keyed by resource type.
     *         The inner map is keyed by both SearchParameter.code and SearchParameter.url
     */
    public static Map<String, ParametersMap> getBuiltInSearchParametersMap() {
        return builtInSearchParameters;
    }

    /*
     * One of the inherited resource is `Resource`, there may be others, so encapsulating the assignment to the cache.
     * @param searchParameterMap
     * @return
     */
    private static Map<String, ParametersMap> assignInheritedToAll(Map<String, ParametersMap> searchParameterMap) {

        // Hierarchy of Resources drives the search parameters assigned in the map.
        // Resource > DomainResource > Instance (e.g. Claim or CarePlan).
        // As such all Resources receive, some receive DomainResource, and individual instances remain untouched.

        ParametersMap resourceMap = searchParameterMap.get(SearchConstants.RESOURCE_RESOURCE);
        ParametersMap domainResourceMap = searchParameterMap.get(SearchConstants.DOMAIN_RESOURCE_RESOURCE);

        for (Entry<String, ParametersMap> entry : searchParameterMap.entrySet()) {
            // Checks the edge case where there are no RESOURCE found
            if (resourceMap != null && !SearchConstants.RESOURCE_RESOURCE.equals(entry.getKey())) {
                // Take the resourceMap and add to this tree.
                entry.getValue().insertAll(resourceMap);
            }

            // Checks the edge case where there are no DOMAIN RESOURCE found
            // We're now dealing with DomainResource
            if (domainResourceMap != null && !RESOURCE_ONLY.contains(entry.getKey())) {
                entry.getValue().insertAll(domainResourceMap);
            }
        }

        return searchParameterMap;
    }

    /**
     * convenience method to print the output of the Search Parameters.
     *
     * @param out
     */
    public static void print(PrintStream out) {
        print(out, builtInSearchParameters);
    }

    /*
     * used when locally debugging.
     * @param out
     * @param searchParamsMap
     */
    private static void print(PrintStream out, Map<String, ParametersMap> searchParamsMap) {
        Set<String> keys = searchParamsMap.keySet();
        out.println(SearchConstants.LOG_BOUNDARY);
        out.println(LOG_HEADER);
        out.println(String.format(LOG_SIZE, keys.size()));
        for (String base : keys) {
            ParametersMap tmp = searchParamsMap.get(base);
            for(SearchParameter param : tmp.values()) {
                String expression = MISSING_EXPRESSION;
                if (param.getExpression() != null) {
                    expression = param.getExpression().getValue();
                }
                out.println(String.format(LOG_OUTPUT, base, param.getCode().getValue(), expression));
            }
            out.println(SearchConstants.LOG_BOUNDARY);
        }
        out.println(SearchConstants.LOG_BOUNDARY);
    }

    /**
     * outputs the search parameter.
     *
     * @param parameter
     * @param out
     */
    public static void printSearchParameter(SearchParameter parameter, PrintStream out) {
        String code = parameter.getCode().getValue();
        List<ResourceType> types = parameter.getBase();

        StringBuilder builder = new StringBuilder();
        builder.append(code);
        builder.append(EQUALS);
        builder.append(LEFT);
        for (ResourceType type : types) {
            builder.append(type.getValue());
            builder.append(COMMA);
        }
        builder.append(RIGHT);
        out.println(builder.toString());
    }
}

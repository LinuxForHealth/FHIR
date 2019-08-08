/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.parameters;

import static com.ibm.watsonhealth.fhir.model.type.String.string;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.watsonhealth.fhir.exception.FHIRException;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.parser.FHIRParser;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTree;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watsonhealth.fhir.model.resource.Bundle;
import com.ibm.watsonhealth.fhir.model.resource.SearchParameter;
import com.ibm.watsonhealth.fhir.model.type.ResourceType;
import com.ibm.watsonhealth.fhir.search.SearchConstants;

/**
 * ParametersUtil
 * 
 * Refactored the PopulateSearchParameterMap code, and marked class as final so there are no 'children' and inheritance
 * which overwrites the behaviors of the buildInSearchParameters.
 * 
 * @author pbastide@us.ibm.com
 * @author markd
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
    public static final String LOG_HEADER = "BASE:RESOURCE_NAME:SearchParameter";
    public static final String LOG_SIZE = "Size: %s";
    private static final String LOG_OUTPUT = "%s|%s|%s";

    private static final String LEFT = "[";
    private static final String RIGHT = "]";
    private static final String COMMA = ",";
    private static final String EQUALS = "=";

    // Unsupported Operations in FHIR Path
    public static final String OPERATION_RESOLVE = "resolve()";
    public static final String OPERATION_CONTAINS = ".contains";
    private static final List<String> UNSUPPORTED_OPERATIONS = Arrays.asList(OPERATION_CONTAINS);

    private static final String REPLACE_RESOLVE = "(\\.where\\([\\s]{0,}resolve\\(\\)[\\s]{0,}[is]{0,2}[\\s]{0,}[\\w]{0,}\\))";

    private static final String CONTAINS_EXPR = "\\.contains\\.";
    private static final String CONTAINS_EXPR_REPLACE = ".`contains`.";
    private static final String CONTAINS_END_EXPR = ".contains";
    private static final String CONTAINS_END_EXPR_REPLACE = ".`contains`";

    private static final Map<String, Map<String, SearchParameter>> builtInSearchParameters = loadBuiltIn();

    private static final String INVALID_EXPRESSION = "/NONE/";

    private ParametersUtil() {
        // No Operation
    }

    /**
     * loads the static version of the class to populate the map.
     */
    public static void init() {
        // No Operation
    }

    /*
     * wraps the error thrown, catches it, and enables a user to operate.
     * @return
     */
    private static Map<String, Map<String, SearchParameter>> loadBuiltIn() {
        Map<String, Map<String, SearchParameter>> result = null;
        try {
            result = populateSearchParameterMapFromResource(FHIR_DEFAULT_SEARCH_PARAMETERS_FILE);
        } catch (IOException e) {
            // This Exception is HIGHLY improbable.
            log.warning(BUILTIN_ERROR_EXCEPTION);
        }
        return result;
    }

    /**
     * This is a convenience function that simply returns the built-in (spec-defined) SearchParameters as a map keyed by
     * resource type.
     */
    public static Map<String, Map<String, SearchParameter>> getBuiltInSearchParameterMap() {
        return builtInSearchParameters;
    }

    /**
     * Returns a Map containing the SearchParameters loaded from the specified classpath resource.
     *
     * @param resourceName
     *            the name of a resource available on the current classpath from which the SearchParameters are to be
     *            loaded
     * @return the Map containing the SearchParameters
     * @throws IOException
     */
    public static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromResource(String resourceName) throws IOException {
        // Capture Exception here and wrap and log out an issue with Search
        try (InputStream stream = ParametersUtil.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (stream == null) {
                throw new FileNotFoundException(String.format(ERROR_EXCEPTION, resourceName));
            }
            return populateSearchParameterMapFromStream(stream);
        }
    }

    /**
     * Returns a Map containing the SearchParameters loaded from the specified File object.
     *
     * @param file
     *            a File object which represents the file from which the SearchParameters are to be loaded
     * @return the Map containing the SearchParameters
     * 
     * @throws IOException
     */
    public static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromFile(File file) throws IOException {
        try (FileInputStream stream = new FileInputStream(file)) {
            return populateSearchParameterMapFromStream(stream);
        }
    }

    /**
     * populates the search parameter map and defaults to json
     * 
     * @param stream
     * @return
     */
    public static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromStream(InputStream stream) {
        return populateSearchParameterMapFromStreamByFormat(stream, Format.JSON);
    }

    /**
     * populates the search parameter map and is XML
     * 
     * @param stream
     * @return
     */
    public static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromStreamXML(InputStream stream) {
        return populateSearchParameterMapFromStreamByFormat(stream, Format.XML);
    }

    /*
     * Loads SearchParameters using the specified InputStream and returns a Map containing them.
     * @param stream the InputStream from which to load the SearchParameters
     * @param Format
     * @return
     */
    private static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromStreamByFormat(InputStream stream, Format format) {
        // Format is never null, as this method is private and hidden.
        // In order to maintain this contract, and avoid the check, keep private.

        Map<String, Map<String, SearchParameter>> searchParameterMap = new HashMap<>();

        // The code block is isolating the effects of exceptions by capturing the exceptions here
        // and returning an empty searchParameterMap, and continuing operation.
        // The failure is logged out.
        try {
            // The code is agnostic to format.
            Bundle bundle = FHIRParser.parser(format).parse(stream);

            FHIRPathTree tree = FHIRPathTree.tree(bundle);
            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator(tree);
            Collection<FHIRPathNode> result = evaluator.evaluate(FHIR_PATH_BUNDLE_ENTRY, tree.getRoot());

            for (SearchParameter parameter : result.stream().map(node -> node.asResourceNode().resource().as(SearchParameter.class)).collect(Collectors.toList())) {

                // Conditional Logging intentionally avoids forming of the String.
                if (log.isLoggable(Level.FINE)) {
                    log.fine(String.format(LOG_PARAMETERS, parameter.getCode().getValue()));
                }

                // Cleanse Unsupported Expressions
                parameter = removeUnsupportedExpressions(parameter);

                // Don't allow if Parameter is null
                if (parameter != null) {

                    /*
                     * In R4, SearchParameter changes from a single Base resource to an array In prior releases, the
                     * code transformed VirtualResources to Basic. As Base is an array, there are going to result in
                     * potential collisions in the map. transformParameter and transformPath
                     */
                    List<ResourceType> types = parameter.getBase();
                    for (ResourceType type : types) {
                        String base = type.getValue();

                        // Logic seems poor, refactor.
                        Map<String, SearchParameter> map = searchParameterMap.get(base);
                        if (map == null) {
                            // Changed to LinkedHashMap for performance reasons.
                            map = new LinkedHashMap<>();
                            searchParameterMap.put(base, map);
                        }

                        // Issue 202: check and warns if the parameter and code do not agree.
                        // Switch to using code
                        String code = parameter.getCode().getValue();
                        String name = parameter.getName().getValue();
                        checkAndWarnForIssueWithCodeAndName(code, name);
                        map.put(code, parameter);
                    }
                }

            }
        } catch (FHIRException fe) {
            // This exception is highly unlikely, but still possible.
            log.warning(String.format(ERROR_EXCEPTION, FROM_STEAM));
        }

        // Must be unmodifiable, lest there be side effects.
        return Collections.unmodifiableMap(assignInheritedToAll(searchParameterMap));
    }

    /**
     * checks and warns if name and code are not equivalent.
     * 
     * @param code
     * @param name
     */
    public static void checkAndWarnForIssueWithCodeAndName(String code, String name) {
        // Name A natural language name identifying the search parameter. This name should be usable as an identifier
        // for the module by machine processing applications such as code generation.
        // @see https://www.hl7.org/fhir/searchparameter-definitions.html#SearchParameter.name

        // Code is the code used in the URL or the parameter name in a parameters resource for this search parameter.
        // @see https://www.hl7.org/fhir/searchparameter-definitions.html#SearchParameter.code

        if (code != null && name != null && code.compareTo(name) != 0 && log.isLoggable(Level.WARNING)) {

            // Note, this is conditionally output, while the code assist complains it is not.
            log.warning(String.format(NO_MATCH_ON_NAME_CODE, code, name));

        }

    }

    /**
     * removes unsupported expressions from Search.
     * 
     * @param parameter
     * @return
     */
    public static SearchParameter removeUnsupportedExpressions(SearchParameter parameter) {
        SearchParameter revisedParameter;

        // Only add the ones that have expressions.
        if (parameter == null) {
            revisedParameter = null;
        } else if (parameter.getBase().contains(ResourceType.DOMAIN_RESOURCE)) {
            // Domain Resources are a special case where the expression is unknown.
            // We want this to pass through no matter what.
            revisedParameter = parameter;
        } else if (parameter.getExpression() == null) {
            revisedParameter = null;
        } else {

            // Issue 206: FHIRPath -> resolve() is an unsupported value.
            // process over the entire expression string
            // and execute for every string for the unsupported types.
            // Right now, unsupported is <code>resolve()</code>

            boolean expressionChanged = false;
            String expressions = parameter.getExpression().getValue();
            for (String operation : UNSUPPORTED_OPERATIONS) {

                if (expressions.contains(operation)) {
                    expressionChanged = true;
                    expressions = processResolve(expressions);
                    expressions = processContains(expressions);
                }

            }

            if (expressions.trim().isEmpty()) {
                // We've removed all expressions as they are unsupported.
                revisedParameter = null;
            } else if (expressionChanged) {
                // If revised, send back
                revisedParameter = parameter.toBuilder().expression(string(expressions)).build();
            } else {
                // Don't revise, send it back.
                revisedParameter = parameter;
            }

        }

        return revisedParameter;
    }

    /**
     * processes, and conditionally updates the resultingExpressions as a side effect. This side effect approach is
     * designed to limit the cognitive complexity, and was not the first choice of inline comparisions.
     * 
     * @param expressions
     */
    public static String processResolve(String expressions) {
        String result = expressions;
        if (UNSUPPORTED_OPERATIONS.contains(OPERATION_RESOLVE) && expressions.contains(OPERATION_RESOLVE)) {
            result = expressions.replaceAll(REPLACE_RESOLVE, SearchConstants.EMPTY_QUERY_STRING).trim();
        }
        return result;
    }

    /**
     * processes the issues with `ValueSet.expansion.contains.code` when the value is unescaped.
     * 
     * @param expressions
     */
    public static String processContains(String expression) {
        String result = expression;
        if (UNSUPPORTED_OPERATIONS.contains(OPERATION_CONTAINS)) {

            result = expression.replaceAll(CONTAINS_EXPR, CONTAINS_EXPR_REPLACE);
            
            if(result.endsWith(CONTAINS_END_EXPR)) {
                result = result.replace(CONTAINS_END_EXPR,  CONTAINS_END_EXPR_REPLACE);
            }
            
            // Logging out details
            if (log.isLoggable(Level.FINER)) {
                log.finer("Not good... Expression is invalid [" + expression + "] [" + result + "]");
            }

        }
        return result;
    }

    /*
     * One of the inherited resource is `Resource`, there may be others, so encapsulating the assignment to the cache.
     * @param searchParameterMap
     * @return
     */
    private static Map<String, Map<String, SearchParameter>> assignInheritedToAll(Map<String, Map<String, SearchParameter>> searchParameterMap) {

        // Hierarchy of Resources drives the search parameters assigned in the map.
        // Resource > DomainResource > Instance (e.g. Claim or CarePlan).
        // As such all Resources receive, some receive DomainResource, and individual instances remain untouched.

        Map<String, SearchParameter> resourceMap = searchParameterMap.get(SearchConstants.RESOURCE_RESOURCE);
        Map<String, SearchParameter> domainResourceMap = searchParameterMap.get(SearchConstants.DOMAIN_RESOURCE_RESOURCE);

        // Checks the edge case where there are no RESOURCE found
        for (Entry<String, Map<String, SearchParameter>> entry : searchParameterMap.entrySet()) {

            if (resourceMap != null && entry.getKey().compareTo(SearchConstants.RESOURCE_RESOURCE) != 0) {
                // Great, now we want to take the resourceMap and add to this tree.
                entry.getValue().putAll(resourceMap);
            }

            // Checks the edge case where there are no DOMAIN RESOURCE found
            // We're now dealing with DomainResource
            if (domainResourceMap != null && !RESOURCE_ONLY.contains(entry.getKey())) {
                entry.getValue().putAll(domainResourceMap);
            }

        }

        return searchParameterMap;
    }

    /**
     * gets the resource or an empty list
     * 
     * @param resourceType
     * @return
     */
    public static Map<String, SearchParameter> getBuiltInSearchParameterMapByResourceType(String resourceType) {
        Map<String, SearchParameter> result = getBuiltInSearchParameterMap().get(resourceType);
        if (result == null) {
            result = Collections.emptyMap();
        }
        return Collections.unmodifiableMap(result);
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
    private static void print(PrintStream out, Map<String, Map<String, SearchParameter>> searchParamsMap) {
        Set<String> keys = searchParamsMap.keySet();
        out.println(SearchConstants.LOG_BOUNDARY);
        out.println(LOG_HEADER);
        out.println(String.format(LOG_SIZE, keys.size()));
        for (String base : keys) {
            Map<String, SearchParameter> tmp = searchParamsMap.get(base);
            for (Entry<String, SearchParameter> entry : tmp.entrySet()) {
                String expressions = INVALID_EXPRESSION;
                if (entry.getValue().getExpression() != null) {
                    expressions = entry.getValue().getExpression().getValue();
                }
                out.println(String.format(LOG_OUTPUT, base, entry.getKey(), expressions));
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

        // Issue 202: Changed to code
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

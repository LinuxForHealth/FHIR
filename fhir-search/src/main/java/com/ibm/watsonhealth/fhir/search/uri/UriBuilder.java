/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.uri;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ibm.watsonhealth.fhir.search.SearchConstants;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.parameters.InclusionParameter;
import com.ibm.watsonhealth.fhir.search.parameters.Parameter;
import com.ibm.watsonhealth.fhir.search.parameters.ParameterValue;
import com.ibm.watsonhealth.fhir.search.parameters.SortParameter;

/**
 * 
 * Build the self link from the search parameters actually used by the server
 * 
 * @see https://hl7.org/fhir/r4/search.html#conformance
 * 
 * @author pbastide
 *
 */
public class UriBuilder {

    private StringBuilder queryString = new StringBuilder();
    private FHIRSearchContext context;
    private String requestUriString;

    private UriBuilder() {
        // Forces the use of the builder pattern.
    }

    /**
     * generates a single instance of the
     * 
     * @return
     */
    public static UriBuilder builder() {
        return new UriBuilder();
    }

    /**
     * adds the context inline.
     * 
     * @param context
     * @return
     */
    public UriBuilder context(FHIRSearchContext context) {
        this.context = context;
        return this;
    }

    /**
     * adds the request uri inline
     * 
     * @param requestUriString
     * @return
     */
    public UriBuilder requestUri(String requestUriString) {
        this.requestUriString = requestUriString;
        return this;
    }

    /**
     * outputs the searchSelfUri based
     * 
     * @return
     * @throws URISyntaxException
     */
    public String toSearchSelfUri() throws URISyntaxException {

        URI requestUri = new URI(requestUriString);

        // Always include page size at the beginning, even if it wasn't in the request
        queryString.append(SearchConstants.COUNT);
        queryString.append(SearchConstants.EQUALS_CHAR);
        queryString.append(context.getPageSize());
        queryString.append(SearchConstants.AND_CHAR);

        // the URLs is converted from a for loop to a functional paradigm. The functional
        // paradigm is consistent with JOINING multiple strings
        Function<Parameter, String> serializeSearchParmToQueryString = p -> serializeSearchParmToQueryString(p);
        queryString.append(context.getSearchParameters().stream().map(serializeSearchParmToQueryString).collect(Collectors.joining(SearchConstants.AND_CHAR_STR)));

        appendElementsParameter();
        appendInclusionParameters();
        appendRevInclusionParameters();
        appendSortParameters();

        // Always include page number at the end, even if it wasn't in the request
        queryString.append(SearchConstants.AND_CHAR);
        queryString.append(SearchConstants.PAGE);
        queryString.append(SearchConstants.EQUALS_CHAR);
        queryString.append(context.getPageNumber());

        URI selfUri = new URI(requestUri.getScheme(), requestUri.getAuthority(), requestUri.getPath(), queryString.toString(), null);

        return selfUri.toString();

    }

    private void appendSortParameters() {
        for (SortParameter param : context.getSortParameters()) {
            queryString.append(SearchConstants.AND_CHAR);
            queryString.append(SearchConstants.SORT);
            queryString.append(SearchConstants.COLON_DELIMITER);
            queryString.append(param.getDirection().value());
            queryString.append(SearchConstants.EQUALS_CHAR);
            queryString.append(param.getName());
        }
    }

    private void appendInclusionParameters() {
        for (InclusionParameter param : context.getIncludeParameters()) {
            queryString.append(SearchConstants.AND_CHAR);
            queryString.append(SearchConstants.INCLUDE);
            queryString.append(SearchConstants.EQUALS_CHAR);
            appendInclusionParamValue(param);
        }
    }

    private void appendRevInclusionParameters() {
        for (InclusionParameter param : context.getRevIncludeParameters()) {
            queryString.append(SearchConstants.AND_CHAR);
            queryString.append(SearchConstants.REVINCLUDE);
            queryString.append(SearchConstants.EQUALS_CHAR);
            appendInclusionParamValue(param);
        }
    }

    private void appendInclusionParamValue(InclusionParameter param) {
        queryString.append(param.getJoinResourceType());
        queryString.append(SearchConstants.COLON_DELIMITER);
        queryString.append(param.getSearchParameter());
        if (param.getSearchParameterTargetType() != null && !param.getSearchParameterTargetType().isEmpty()) {
            queryString.append(SearchConstants.COLON_DELIMITER);
            queryString.append(param.getSearchParameterTargetType());
        }
    }

    private void appendElementsParameter() {
        // Unlike the other search parameters, "_elements" is collapsed into a single parameter (no AND vs. OR
        // distinction)
        if (context.getElementsParameters() != null && !context.getElementsParameters().isEmpty()) {
            queryString.append(SearchConstants.AND_CHAR);
            queryString.append(SearchConstants.ELEMENTS);
            queryString.append(SearchConstants.EQUALS_CHAR);
            String delim = SearchConstants.EMPTY_QUERY_STRING;
            for (String param : context.getElementsParameters()) {
                queryString.append(delim + param);
                delim = SearchConstants.JOIN_STR;
            }
        }
    }

    /*
     * Performs the reverse of parseQueryParameters, serializing a single parameter as a search parameter for a URI
     * @param param
     * @return
     */
    private String serializeSearchParmToQueryString(Parameter param) {
        if (param.isInclusionCriteria()) {
            // Inclusion criteria come from compartment searches which appear as part of the path
            // and so there is nothing to add to the query string.
            return SearchConstants.EMPTY_QUERY_STRING;
        }

        StringBuilder returnString = new StringBuilder(SearchConstants.AND_CHAR);
        if (param.isChained()) {
            appendChainedParm(param, returnString);

            // Param is never null at this point, and isChained is known to be true.
            boolean process = true;
            while (process) {
                param = param.getNextParameter();
                process = param != null && param.isChained();
                if (param != null) {
                    // A guard is added as the original code 'could' result in an NPE.
                    returnString.append(param.getName());
                }

            }
        }

        appendNormalParameter(param, returnString);

        return returnString.toString();
    }

    /*
     * creates a normal parameter and string.
     * @param param
     * @param returnString
     */
    private void appendNormalParameter(Parameter param, StringBuilder returnString) {
        if (param != null) {
            returnString.append(param.getName());
            SearchConstants.Modifier modifier = param.getModifier();
            if (modifier != null) {
                if (SearchConstants.Modifier.TYPE == modifier) {
                    returnString.append(SearchConstants.COLON_DELIMITER);
                    returnString.append(param.getModifierResourceTypeName());
                } else {
                    returnString.append(SearchConstants.COLON_DELIMITER);
                    returnString.append(modifier.value());
                }
            }
            returnString.append(SearchConstants.EQUALS_CHAR);
            returnString.append(param.getValues().stream().map(ParameterValue::toString).collect(Collectors.joining(SearchConstants.JOIN_STR)));
        }
    }

    /*
     * creates a chained parameter
     * @param param
     * @param returnString
     */
    private void appendChainedParm(Parameter param, StringBuilder returnString) {
        returnString.append(param.getName());
        if (param.getModifierResourceTypeName() != null && !param.getModifierResourceTypeName().isEmpty()) {
            returnString.append(SearchConstants.COLON_DELIMITER);
            returnString.append(param.getModifierResourceTypeName());
        }
    }

}

/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.uri;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.parameters.InclusionParameter;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.parameters.SortParameter;
import com.ibm.fhir.search.sort.Sort.Direction;

/**
 * Build the self link from the search parameters actually used by the server
 * <a href="https://hl7.org/fhir/r4/search.html#conformance">FHIR Specification:
 * URI Pattern</a>
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
        String hostAndPath = requestUriString.contains("?") ?
                requestUriString.substring(0, requestUriString.indexOf("?")) :
                requestUriString;
        URI requestUri = new URI(hostAndPath);

        // Always include page size at the beginning, even if it wasn't in the request
        queryString.append(SearchConstants.COUNT);
        queryString.append(SearchConstants.EQUALS_CHAR);
        queryString.append(context.getPageSize());

        appendSearchParameters();
        appendElementsParameter();
        appendInclusionParameters();
        appendRevInclusionParameters();
        appendSortParameters();
        appendSummaryParameter();
        appendTotalParameter();
        appendResourceTypesParameter();

        // Always include page number at the end, even if it wasn't in the request
        queryString.append(SearchConstants.AND_CHAR);
        queryString.append(SearchConstants.PAGE);
        queryString.append(SearchConstants.EQUALS_CHAR);
        queryString.append(context.getPageNumber());

        URI selfUri = new URI(requestUri.getScheme(), requestUri.getAuthority(), requestUri.getPath(),
                queryString.toString(), null);

        return selfUri.toString();
    }

    /**
     * Appends the search parameters to the query string, if joining them together results in a non-empty string.
     */
    private void appendSearchParameters() {
        if (!context.getSearchParameters().isEmpty()) {
            Predicate<String> stringIsEmpty = String::isEmpty;
            String searchParameters = context.getSearchParameters().stream()
                    .map(p -> serializeSearchParmToQueryString(p))
                    .filter(stringIsEmpty.negate())
                    .collect(Collectors.joining(SearchConstants.AND_CHAR_STR));
            if (!searchParameters.isEmpty()) {
                queryString.append(SearchConstants.AND_CHAR);
                queryString.append(searchParameters);
            }
        }
    }

    private void appendResourceTypesParameter() {
        if (context.getSearchResourceTypes() != null && !context.getSearchResourceTypes().isEmpty()) {
            queryString.append(SearchConstants.AND_CHAR);
            queryString.append(SearchConstants.RESOURCE_TYPE);
            queryString.append(SearchConstants.EQUALS_CHAR);
            String delim = SearchConstants.EMPTY_QUERY_STRING;
            for (String param : context.getSearchResourceTypes()) {
                queryString.append(delim).append(param);
                delim = SearchConstants.JOIN_STR;
            }
        }

    }

    /*
     * In R4, the sort parameters are JOINED together into one key-value
     * URL Parameter.
     */
    private void appendSortParameters() {

        //
        if (!context.getSortParameters().isEmpty()) {
            queryString.append(SearchConstants.AND_CHAR);
            queryString.append(SearchConstants.SORT);
            queryString.append(SearchConstants.EQUALS_CHAR);

            for (SortParameter param : context.getSortParameters()) {
                if (Direction.DECREASING.compareTo(param.getDirection()) == 0) {
                    queryString.append('-');
                }
                queryString.append(param.getCode());
                queryString.append(',');
            }
            queryString.deleteCharAt(queryString.length() - 1);
        }
    }

    private void appendInclusionParameters() {
        for (InclusionParameter param : context.getIncludeParameters()) {
            queryString.append(SearchConstants.AND_CHAR);
            queryString.append(SearchConstants.INCLUDE);
            if (param.getModifier() != null) {
                queryString.append(SearchConstants.COLON_DELIMITER).append(param.getModifier().value());
            }
            queryString.append(SearchConstants.EQUALS_CHAR);
            appendInclusionParamValue(param);
        }
    }

    private void appendSummaryParameter() {
        if (context.getSummaryParameter() != null) {
            queryString.append(SearchConstants.AND_CHAR);
            queryString.append(SearchConstants.SUMMARY);
            queryString.append(SearchConstants.EQUALS_CHAR);
            queryString.append(context.getSummaryParameter().value());
        }
    }

    private void appendTotalParameter() {
        if (context.getTotalParameter() != null) {
            queryString.append(SearchConstants.AND_CHAR);
            queryString.append(SearchConstants.TOTAL);
            queryString.append(SearchConstants.EQUALS_CHAR);
            queryString.append(context.getTotalParameter().value());
        }
    }

    private void appendRevInclusionParameters() {
        for (InclusionParameter param : context.getRevIncludeParameters()) {
            queryString.append(SearchConstants.AND_CHAR);
            queryString.append(SearchConstants.REVINCLUDE);
            if (param.getModifier() != null) {
                queryString.append(SearchConstants.COLON_DELIMITER).append(param.getModifier().value());
            }
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
     * Performs the reverse of parseQueryParameters, serializing a single parameter
     * as a search parameter for a URI
     *
     * @param param
     *
     * @return
     */
    private String serializeSearchParmToQueryString(QueryParameter param) {
        if (param.isInclusionCriteria()) {
            // Inclusion criteria come from compartment searches which appear as part of the path
            // and so there is nothing to add to the query string.
            return SearchConstants.EMPTY_QUERY_STRING;
        }

        StringBuilder returnString = new StringBuilder(SearchConstants.AND_CHAR);
        if (param.isReverseChained()) {
            appendReverseChainedParm(param, returnString);
        } else if (param.isChained()) {
            appendChainedParm(param, returnString);

            // Param is never null at this point, and isChained is known to be true.
            boolean process = true;
            while (process) {
                param   = param.getNextParameter();
                process = param != null && param.isChained();
                // A guard is added as the original code 'could' result in an NPE.
                if (param != null) {
                    returnString.append(SearchConstants.CHAINED_PARAMETER_CHARACTER);
                    if (param.isChained()) {
                        appendChainedParm(param, returnString);
                    } else {
                        appendNormalParameter(param, returnString);
                    }
                }
            }
        } else {
            appendNormalParameter(param, returnString);
        }

        return returnString.toString();
    }

    /**
     * creates a normal parameter and string.
     *
     * @param param
     *
     * @param returnString
     */
    private void appendNormalParameter(QueryParameter param, StringBuilder returnString) {
        if (param != null) {
            returnString.append(param.getCode());
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
            // If it's an intermediate value, specifically for references filter them out.
            returnString.append(param.getValues().stream().filter(qpv -> !qpv.isHidden()).map(QueryParameterValue::toString)
                    .collect(Collectors.joining(SearchConstants.JOIN_STR)));
        }
    }

    /*
     * creates a chained parameter
     *
     * @param param
     *
     * @param returnString
     */
    private void appendChainedParm(QueryParameter param, StringBuilder returnString) {
        returnString.append(param.getCode());
        if (param.getModifierResourceTypeName() != null && !param.getModifierResourceTypeName().isEmpty()) {
            returnString.append(SearchConstants.COLON_DELIMITER);
            returnString.append(param.getModifierResourceTypeName());
        }
    }

    /*
     * creates a reverse chained parameter
     *
     * @param param
     *
     * @param returnString
     */
    private void appendReverseChainedParm(QueryParameter param, StringBuilder returnString) {
        // Build initial string: "_has:<reference-resource-type>:<refernece-search-parm>:
        returnString.append(SearchConstants.HAS)
                    .append(SearchConstants.COLON_DELIMITER)
                    .append(param.getModifierResourceTypeName())
                    .append(SearchConstants.COLON_DELIMITER)
                    .append(param.getCode())
                    .append(SearchConstants.COLON_DELIMITER);

        // Loop through chained parms to build chained strings
        for (QueryParameter revChainParam : param.getChain()) {
            if (revChainParam.isReverseChained()) {
                returnString.append(SearchConstants.HAS)
                            .append(SearchConstants.COLON_DELIMITER)
                            .append(revChainParam.getModifierResourceTypeName())
                            .append(SearchConstants.COLON_DELIMITER)
                            .append(revChainParam.getCode())
                            .append(SearchConstants.COLON_DELIMITER);
            } else if (revChainParam.isChained()) {
                appendChainedParm(revChainParam, returnString);
                returnString.append(SearchConstants.CHAINED_PARAMETER_CHARACTER);
            } else {
                appendNormalParameter(revChainParam, returnString);
            }
        }
    }

}

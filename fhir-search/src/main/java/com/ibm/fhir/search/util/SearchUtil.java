/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.util;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.config.PropertyGroup.PropertyEntry;
import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.SearchParameter.Component;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.model.util.JsonSupport;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.SummaryValueSet;
import com.ibm.fhir.search.compartment.CompartmentUtil;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.context.FHIRSearchContextFactory;
import com.ibm.fhir.search.date.DateTimeHandler;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.exception.SearchExceptionUtil;
import com.ibm.fhir.search.parameters.InclusionParameter;
import com.ibm.fhir.search.parameters.ParametersMap;
import com.ibm.fhir.search.parameters.ParametersUtil;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.parameters.cache.TenantSpecificSearchParameterCache;
import com.ibm.fhir.search.reference.ReferenceParameterHandler;
import com.ibm.fhir.search.sort.Sort;
import com.ibm.fhir.search.uri.UriBuilder;

/**
 * Search Utility<br>
 * This class uses FHIRPath Expressions (and currently does not support XPath)
 * and uses init to activate the Parameters/Compartments/ValueTypes components.
 */
public class SearchUtil {
    private static final String CLASSNAME = SearchUtil.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    // Logging Strings
    private static final String EXTRACT_PARAMETERS_LOGGING = "extractParameterValues: [%s] [%s]";
    private static final String NO_TENANT_SP_MAP_LOGGING =
            "No tenant-specific search parameters found for tenant '%s'; trying %s ";
    private static final String UNSUPPORTED_EXCEPTION =
            "Search Parameter includes an unsupported operation or bad expression : [%s] [%s] [%s]";

    // Exception Strings
    private static final String MODIFIER_NOT_ALLOWED_WITH_CHAINED_EXCEPTION = "Modifier: '%s' not allowed on chained parameter";
    private static final String TYPE_NOT_ALLOWED_WITH_CHAINED_PARAMETER_EXCEPTION =
            "Type: '%s' not allowed on chained parameter";
    private static final String SEARCH_PARAMETER_MODIFIER_NAME =
            "Search parameter: '%s' must have resource type name modifier";
    private static final String INVALID_TARGET_TYPE_EXCEPTION = "Invalid target type for the Inclusion Parameter.";
    private static final String UNSUPPORTED_EXPR_NULL =
            "An empty expression is found or the parameter type is unsupported [%s][%s]";

    private static final String MODIFIYERRESOURCETYPE_NOT_ALLOWED_FOR_RESOURCETYPE =
            "Modifier resource type [%s] is not allowed for search parameter [%s] of resource type [%s].";

    private static final String DIFFERENT_MODIFIYERRESOURCETYPES_FOUND_FOR_RESOURCETYPES =
            "Different Modifier resource types are found for search parameter [%s] of the to-be-searched resource types.";

    // The functionality is split into a new class.
    private static final Sort sort = new Sort();

    /*
     * This is our in-memory cache of SearchParameter objects. The cache is
     * organized at the top level by tenant-id,
     * with the built-in (FHIR spec-defined) SearchParameters stored under the
     * "built-in" pseudo-tenant-id.
     * SearchParameters contained in the default tenant's
     * extension-search-parameters.xml file are stored under the
     * "default" tenant-id, and other tenants' SearchParameters (defined in their
     * tenant-specific
     * extension-search-parameters.xml files) will be stored under their respective
     * tenant-ids as well. The objects
     * stored in our cache are of type CachedObjectHolder, with each one containing
     * a Map<String, Map<String,
     * SearchParameter>>. This map is keyed by resource type (simple name, e.g.
     * "Patient"). Each object stored in this
     * map contains the SearchParameters for that resource type, keyed by
     * SearchParameter name (e.g. "_lastUpdated").
     * When getSearchParameter(resourceType, name) is called, we'll need to first
     * search in the current tenant's map,
     * then if not found, look in the "built-in" tenant's map. Also, when
     * getSearchParameters(resourceType) is called,
     * we'll need to return a List that contains SearchParameters from the current
     * tenant's map (if present) plus those
     * contained in the "built-in" tenant's map as well.
     */
    private static TenantSpecificSearchParameterCache searchParameterCache = new TenantSpecificSearchParameterCache();

    private SearchUtil() {
        // No Operation
        // Hides the Initialization
    }

    /**
     * Initializes the various services related to Search and pre-caches.
     * <br>
     * Loads the class in the classloader to initialize static members. Call this
     * before using the class in order to
     * avoid a slight performance hit on first use.
     */
    public static void init() {
        // Inherently the searchParameterCache is loaded.

        // Loads the Compartments
        CompartmentUtil.init();

        // Loads the Parameters into a map
        ParametersUtil.init();
    }

    /**
     * Retrieves user-defined SearchParameters associated with the specified
     * resource type and current tenant id.
     *
     * @param resourceType
     *                     the resource type for which user-defined SearchParameters
     *                     will be returned
     * @return a list of user-defined SearchParameters associated with the specified
     *         resource type
     * @throws Exception
     */
    protected static List<SearchParameter> getUserDefinedSearchParameters(String resourceType) throws Exception {
        List<SearchParameter> result = new ArrayList<>();
        String tenantId = FHIRRequestContext.get().getTenantId();
        Map<String, ParametersMap> spMapTenant = getTenantOrDefaultSPMap(tenantId);

        if (spMapTenant != null) {
            ParametersMap spMapResourceType = spMapTenant.get(resourceType);
            if (spMapResourceType != null && !spMapResourceType.isEmpty()) {
                result.addAll(spMapResourceType.values());
            }

            // Add the Mapping to All Resource Types
            spMapResourceType = spMapTenant.get(SearchConstants.RESOURCE_RESOURCE);
            if (spMapResourceType != null && !spMapResourceType.isEmpty()) {
                result.addAll(spMapResourceType.values());
            }
        }
        return result;
    }

    /**
     * Returns a filtered list of built-in SearchParameters associated with the
     * specified resource type and those
     * associated with the "Resource" resource type.
     *
     * @param resourceType
     *                     the resource type
     * @return a filtered list of SearchParameters
     * @throws Exception
     */
    protected static List<SearchParameter> getFilteredBuiltinSearchParameters(String resourceType) throws Exception {
        List<SearchParameter> result = new ArrayList<>();

        Map<String, ParametersMap> spBuiltin = ParametersUtil.getBuiltInSearchParametersMap();

        // Retrieve the current tenant's search parameter filtering rules.
        Map<String, List<String>> filterRules = getFilterRules();

        // Retrieve the SPs associated with the specified resource type and filter per the filter rules.
        ParametersMap spMap = spBuiltin.get(resourceType);
        if (spMap != null && !spMap.isEmpty()) {
            result.addAll(filterSearchParameters(filterRules, resourceType, spMap.values()));
        }

        if (!SearchConstants.RESOURCE_RESOURCE.equals(resourceType)) {
            // Retrieve the SPs associated with the "Resource" resource type and filter per the filter rules.
            spMap = spBuiltin.get(SearchConstants.RESOURCE_RESOURCE);
            if (spMap != null && !spMap.isEmpty()) {
                Collection<SearchParameter> superParams =
                        filterSearchParameters(filterRules, SearchConstants.RESOURCE_RESOURCE, spMap.values());
                List<String> resultCodes = result.stream()
                        .map(sp -> sp.getCode().getValue())
                        .collect(Collectors.toList());

                for (SearchParameter sp : superParams) {
                    String spCode = sp.getCode().getValue();
                    if (resultCodes.contains(spCode)) {
                        SearchParameter existingSP = result.get(resultCodes.indexOf(spCode));
                        if (sp.getExpression() != null && !sp.getExpression().equals(existingSP.getExpression())) {
                            log.warning("Code '" + sp.getCode().getValue() + "' is defined for " +
                                    SearchConstants.RESOURCE_RESOURCE + " and " + resourceType +
                                    " with differing expressions; using " + resourceType);
                        }
                    } else {
                        result.add(sp);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Filters the specified input list of SearchParameters according to the filter
     * rules and input resource type. The
     * filter rules are contained in a Map<String, List<String>> that is keyed by
     * resource type. The value of each Map
     * entry is a list of search parameter names that should be included in our
     * filtered result.
     *
     * @param filterRules
     *                                   a Map containing filter rules
     * @param resourceType
     *                                   the resource type associated with each of
     *                                   the unfiltered SearchParameters
     * @param unfilteredSearchParameters
     *                                   the unfiltered Collection of
     *                                   SearchParameter objects
     * @return a filtered Collection of SearchParameters
     */
    private static Collection<SearchParameter> filterSearchParameters(Map<String, List<String>> filterRules,
            String resourceType, Collection<SearchParameter> unfilteredSearchParameters) {
        List<SearchParameter> results = new ArrayList<>();

        // First, retrieve the filter rule (list of SP names to be included) for the specified resource type.
        // We know that the SearchParameters in the unfiltered list are all associated with this resource type,
        // so we can use this same "name list" for each Search Parameter in the unfiltered list.
        List<String> includedSPs = filterRules.get(resourceType);

        if (includedSPs == null) {
            // If the specified resource type wasn't found in the Map then retrieve the wildcard entry if present.
            includedSPs = filterRules.get(SearchConstants.WILDCARD_FILTER);
        }

        // If we found a non-empty list of search parameter names to filter on,
        // then do the filtering. Otherwise, we're just going to return an empty list.
        if (includedSPs != null && !includedSPs.isEmpty()) {
            // If "*" is contained in the included SP names, then we can just return the unfiltered list
            // now, since everything in the list will be included anyway.
            if (includedSPs.contains(SearchConstants.WILDCARD_FILTER)) {
                return unfilteredSearchParameters;
            }

            // Otherwise, we'll walk through the unfiltered list and select the ones to be
            // included in our result.
            else {
                for (SearchParameter sp : unfilteredSearchParameters) {

                    String name = sp.getCode().getValue();
                    if (includedSPs.contains(name)) {
                        results.add(sp);
                    }

                }
            }
        }

        return results;
    }

    /**
     * Retrieves the search parameter filtering rules for the current tenant.
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private static Map<String, List<String>> getFilterRules() throws Exception {
        Map<String, List<String>> result = new HashMap<>();

        // Retrieve the "searchParameterFilter" config property group.
        PropertyGroup spFilter = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_SEARCH_PARAMETER_FILTER);
        List<PropertyEntry> ruleEntries = null;
        if (spFilter != null) {
            ruleEntries = spFilter.getProperties();
        }

        // If we have a non-empty set of filter rules, then walk through them and populate our map.
        if (ruleEntries != null && !ruleEntries.isEmpty()) {
            for (PropertyEntry ruleEntry : ruleEntries) {
                String resourceType = ruleEntry.getName();

                // Make sure the value is a List<String>.
                if (ruleEntry.getValue() instanceof List<?>) {
                    for (Object listMember : (List<?>) ruleEntry.getValue()) {
                        if (!(listMember instanceof String)) {
                            throw SearchExceptionUtil.buildNewIllegalStateException();
                        }
                    }

                    // Add the rule entry to our map, keyed by resource type.
                    List<String> stringList = (List<String>) ruleEntry.getValue();
                    result.put(resourceType, stringList);
                } else {
                    throw SearchExceptionUtil.buildNewIllegalStateException();
                }
            }
        } else {
            // The current tenant doesn't have any filter rules defined, so
            // we'll just fabricate one that includes all search parameters:
            // <pre>{ "*": ["*"] }</pre>
            List<String> list = new ArrayList<>();
            list.add(SearchConstants.WILDCARD);
            result.put(SearchConstants.WILDCARD, list);
        }
        return result;
    }

    /**
     * Returns the SearchParameter map (keyed by resource type) for the specified
     * tenant-id, or null if there are no SearchParameters for the tenant.
     *
     * @param tenantId
     *                 the tenant-id whose SearchParameters should be returned.
     * @throws FileNotFoundException
     */
    private static Map<String, ParametersMap> getTenantOrDefaultSPMap(String tenantId) throws Exception {
        if (log.isLoggable(Level.FINEST)) {
            log.entering(CLASSNAME, "getTenantSPMap", new Object[] { tenantId });
        }
        try {
            Map<String, ParametersMap> cachedObjectForTenant =
                    searchParameterCache.getCachedObjectForTenant(tenantId);

            if (cachedObjectForTenant == null) {

                // Output logging detail.
                if (log.isLoggable(Level.FINER)) {
                    log.finer(String.format(NO_TENANT_SP_MAP_LOGGING, tenantId, FHIRConfiguration.DEFAULT_TENANT_ID));
                }

                cachedObjectForTenant =
                        searchParameterCache.getCachedObjectForTenant(FHIRConfiguration.DEFAULT_TENANT_ID);
            }

            return cachedObjectForTenant;
        } finally {
            if (log.isLoggable(Level.FINEST)) {
                log.exiting(CLASSNAME, "getTenantSPMap");
            }
        }
    }

    /**
     * @param resourceType
     * @param code
     * @return the SearchParameter for type {@code resourceType} with code {@code code} or null if it doesn't exist
     * @throws Exception
     */
    public static SearchParameter getSearchParameter(Class<?> resourceType, String name) throws Exception {
        return getSearchParameter(resourceType.getSimpleName(), name);
    }

    /**
     * @param resourceType
     * @param code
     * @return the SearchParameter for type {@code resourceType} with code {@code code} or null if it doesn't exist
     * @throws Exception
     */
    public static SearchParameter getSearchParameter(String resourceType, String code) throws Exception {
        String tenantId = FHIRRequestContext.get().getTenantId();

        // First try to find the search parameter within the specified tenant's map.
        SearchParameter result = getSearchParameterByCodeIfPresent(getTenantOrDefaultSPMap(tenantId), resourceType, code);

        // If we didn't find it within the tenant's map, then look within the built-in map.
        if (result == null) {
            result = getSearchParameterByCodeIfPresent(ParametersUtil.getBuiltInSearchParametersMap(), resourceType, code);

            // If we found it within the built-in search parameters, apply our filtering rules.
            if (result != null) {

                ResourceType rt = result.getBase().get(0).as(ResourceType.class);
                Collection<SearchParameter> filteredResult =
                        filterSearchParameters(getFilterRules(), rt.getValue(), Collections.singleton(result));

                // If our filtered result is non-empty, then just return the first (and only) item.
                result = (filteredResult.isEmpty() ? null : filteredResult.iterator().next());
            }
        }
        return result;
    }

    /**
     * @param spMaps
     * @param resourceType
     * @param code
     * @return the SearchParameter for type {@code resourceType} with code {@code code} or null if it doesn't exist
     */
    private static SearchParameter getSearchParameterByCodeIfPresent(Map<String, ParametersMap> spMaps, String resourceType, String code) {
        SearchParameter result = null;

        if (spMaps != null && !spMaps.isEmpty()) {
            ParametersMap parametersMap = spMaps.get(resourceType);
            if (parametersMap != null && !parametersMap.isEmpty()) {
                result = parametersMap.lookupByCode(code);
            }

            if (result == null) {
                parametersMap = spMaps.get(SearchConstants.RESOURCE_RESOURCE);
                if (parametersMap != null && !parametersMap.isEmpty()) {
                    result = parametersMap.lookupByCode(code);
                }
            }
        }

        return result;
    }

    /**
     * @param resourceType
     * @param uri
     * @return the SearchParameter for type {@code resourceType} with url {@code uri} or null if it doesn't exist
     * @throws Exception
     */
    public static SearchParameter getSearchParameter(Class<?> resourceType, Canonical uri) throws Exception {
        return getSearchParameter(resourceType.getSimpleName(), uri);
    }

    /**
     * @param resourceType
     * @param uri
     * @return the SearchParameter for type {@code resourceType} with url {@code uri} or null if it doesn't exist
     * @throws Exception
     */
    public static SearchParameter getSearchParameter(String resourceType, Canonical uri) throws Exception {
        String tenantId = FHIRRequestContext.get().getTenantId();

        // First try to find the search parameter within the specified tenant's map.
        SearchParameter result = getSearchParameterByUrlIfPresent(getTenantOrDefaultSPMap(tenantId), resourceType, uri);

        // If we didn't find it within the tenant's map, then look within the built-in map.
        if (result == null) {
            result = getSearchParameterByUrlIfPresent(ParametersUtil.getBuiltInSearchParametersMap(), resourceType, uri);

            // If we found it within the built-in search parameters, apply our filtering rules.
            if (result != null) {
                ResourceType rt = result.getBase().get(0);
                Collection<SearchParameter> filteredResult =
                        filterSearchParameters(getFilterRules(), rt.getValue(), Collections.singleton(result));

                // If our filtered result is non-empty, then just return the first (and only) item.
                result = (filteredResult.isEmpty() ? null : filteredResult.iterator().next());
            }
        }
        return result;
    }

    /**
     * @param spMaps
     * @param resourceType
     * @param uri
     * @return the SearchParameter for type {@code resourceType} with url {@code uri} or null if it doesn't exist
     */
    private static SearchParameter getSearchParameterByUrlIfPresent(Map<String, ParametersMap> spMaps, String resourceType, Canonical uri) {
        SearchParameter result = null;

        if (spMaps != null && !spMaps.isEmpty()) {
            ParametersMap parametersMap = spMaps.get(resourceType);
            if (parametersMap != null && !parametersMap.isEmpty()) {
                result = parametersMap.lookupByUrl(uri.getValue());
            }

            if (result == null) {
                parametersMap = spMaps.get(SearchConstants.RESOURCE_RESOURCE);
                if (parametersMap != null && !parametersMap.isEmpty()) {
                    result = parametersMap.lookupByUrl(uri.getValue());
                }
            }
        }

        return result;
    }

    /**
     * Perform wildcard processing for inclusion search parameters by getting all valid search parameters for the
     * specified join resource type. Search parameters must have a type of 'reference'.
     * <p>
     * If inclusion keyword is ' _include' and a target resource type is specified, search parameter must contain
     * a matching type.
     * <p>
     * If inclusion keyword is '_revinclude', search parameter must have a target resource type matching the resource
     * type being searched.
     *
     * @param resourceType
     *            the resource type being searched for
     * @param joinResourceType
     *            the resource type for which inclusion search parameters will be returned
     * @param searchParameterTargetType
     *            the target resource type for included resources
     * @param inclusionKeyword
     *            the inclusion type, either _include or _revinclude
     * @return
     *         the inclusion SearchParameters for type {@code resourceType} or empty map if none exist
     * @throws Exception
     */
    private static Map<String, SearchParameter> getInclusionWildcardSearchParameters(String resourceType, String joinResourceType,
        String searchParameterTargetType, String inclusionKeyword) throws Exception {
        Map<String, SearchParameter> inclusionSearchParameters = new HashMap<>();

        for (SearchParameter searchParameter : getApplicableSearchParameters(joinResourceType)) {
            if (SearchParamType.REFERENCE.equals(searchParameter.getType()) &&
                    ((SearchConstants.INCLUDE.equals(inclusionKeyword)
                            && (searchParameterTargetType == null || isValidTargetType(searchParameterTargetType, searchParameter))) ||
                    (SearchConstants.REVINCLUDE.equals(inclusionKeyword) && isValidTargetType(resourceType, searchParameter)))) {
                // Valid search parameter of type reference - add to map
                inclusionSearchParameters.put(searchParameter.getCode().getValue(), searchParameter);
            } else if (inclusionSearchParameters.containsKey(searchParameter.getCode().getValue())) {
                // Invalid duplicate search parameter found for valid search parameter already in map. Log invalid search parameter and ignore.
                log.fine("Invalid duplicate search parameter '" + searchParameter.getCode().getValue() +
                    "' found in wildcard inclusion processing. Invalid search parameter ignored.");
            }
        }

        return inclusionSearchParameters;
    }

    /**
     * skips the empty extracted search parameters
     *
     * @param resource
     * @return
     * @throws Exception
     */
    public static Map<SearchParameter, List<FHIRPathNode>> extractParameterValues(Resource resource) throws Exception {
        // Skip Empty is automatically true in this call.
        return extractParameterValues(resource, true);
    }

    /**
     * extract parameter values.
     *
     * @param resource
     * @param skipEmpty
     * @return
     * @throws Exception
     */
    public static Map<SearchParameter, List<FHIRPathNode>> extractParameterValues(Resource resource, boolean skipEmpty)
            throws Exception {

        Map<SearchParameter, List<FHIRPathNode>> result = new LinkedHashMap<>();

        // Get the Parameters for the class.
        Class<?> resourceType = resource.getClass();

        // Create one time.
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(resource);

        List<SearchParameter> parameters = getApplicableSearchParameters(resourceType.getSimpleName());

        for (SearchParameter parameter : parameters) {

            com.ibm.fhir.model.type.String expression = parameter.getExpression();

            // Outputs the Expression and the Name of the SearchParameter
            if (log.isLoggable(Level.FINEST)) {
                String loggedValue = "EMPTY";
                if (expression != null) {
                    loggedValue = expression.getValue();
                }

                log.finest(String.format(EXTRACT_PARAMETERS_LOGGING, parameter.getCode().getValue(), loggedValue));
            }

            // Process the Expression
            if (expression == null) {
                if (log.isLoggable(Level.FINER)) {
                    log.finer(String.format(UNSUPPORTED_EXPR_NULL, parameter.getType(), parameter.getCode().getValue()));
                }
                continue;
            }
            try {
                Collection<FHIRPathNode> tmpResults = evaluator.evaluate(evaluationContext, expression.getValue());

                if (log.isLoggable(Level.FINEST)) {
                    log.finest("Expression [" + expression.getValue() + "] parameter-code ["
                            + parameter.getCode().getValue() + "] Size -[" + tmpResults.size() + "]");
                }

                // Adds only if !skipEmpty || tmpResults is not empty
                if (!tmpResults.isEmpty() || !skipEmpty) {
                    result.put(parameter, new ArrayList<>(tmpResults));
                }

            } catch (java.lang.UnsupportedOperationException | FHIRPathException uoe) {
                // switched to using code instead of name
                log.warning(String.format(UNSUPPORTED_EXCEPTION, parameter.getCode().getValue(),
                        expression.getValue(), uoe.getMessage()));
            }
        }

        return result;
    }

    public static FHIRSearchContext parseQueryParameters(Class<?> resourceType,
            Map<String, List<String>> queryParameters)
            throws Exception {
        return parseQueryParameters(resourceType, queryParameters, false);
    }

    public static FHIRSearchContext parseQueryParameters(Class<?> resourceType,
            Map<String, List<String>> queryParameters, boolean lenient)
            throws Exception {

        FHIRSearchContext context = FHIRSearchContextFactory.createSearchContext();
        context.setLenient(lenient);
        List<QueryParameter> parameters = new ArrayList<>();

        // Make sure _sort is not present with _include and/or _revinclude.
        // TODO: do we really need to forbid this?
        if (queryParameters.containsKey(SearchConstants.SORT) &&
                (queryParameters.containsKey(SearchConstants.INCLUDE)
                        || queryParameters.containsKey(SearchConstants.REVINCLUDE))) {
            throw SearchExceptionUtil.buildNewInvalidSearchException(
                    "_sort search result parameter not supported with _include or _revinclude.");
        }
        HashSet<String> resourceTypes = new HashSet<>();
        if (Resource.class.equals(resourceType)) {
            // Because _include and _revinclude searches all require certain resource type modifier in
            // search parameter, so we just don't support it.
            if (queryParameters.containsKey(SearchConstants.INCLUDE)
                        || queryParameters.containsKey(SearchConstants.REVINCLUDE)) {
                throw SearchExceptionUtil.buildNewInvalidSearchException(
                        "system search not supported with _include or _revinclude.");
            }

            if (queryParameters.containsKey(SearchConstants.RESOURCE_TYPE)) {
                for (String resTypes : queryParameters.get(SearchConstants.RESOURCE_TYPE)) {
                    List<String> tmpResourceTypes = Arrays.asList(resTypes.split("\\s*,\\s*"));
                    for (String resType : tmpResourceTypes) {
                        if (ModelSupport.isResourceType(resType)) {
                            resourceTypes.add(resType);
                        } else {
                            String msg = "_type search parameter has invalid resource type:" + resType;
                            if (lenient) {
                                // TODO add this to the list of supplemental warnings?
                                log.log(Level.FINE, msg);
                                continue;
                            } else {
                                throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                            }
                        }
                    }
                }
            }
        }

        queryParameters.remove(SearchConstants.RESOURCE_TYPE);

        Boolean isMultiResTypeSearch = Resource.class.equals(resourceType) && !resourceTypes.isEmpty();

        if (isMultiResTypeSearch) {
            context.setSearchResourceTypes(new ArrayList<>(resourceTypes));
        }

        for (Entry<String, List<String>> entry : queryParameters.entrySet()) {
            String name = entry.getKey();
            try {
                List<String> params = entry.getValue();

                if (isSearchResultParameter(name)) {
                    parseSearchResultParameter(resourceType, context, name, params, lenient);
                    // _include and _revinclude parameters cannot be mixed with _summary=text
                    // TODO: this will fire on each search result parameter; maybe move this above to where we handle _sort + _include/_revinclude?
                    if (context.getSummaryParameter() != null
                            && context.getSummaryParameter().equals(SummaryValueSet.TEXT)) {
                        context.getIncludeParameters().clear();
                        context.getRevIncludeParameters().clear();
                    }
                } else if (isGeneralParameter(name) ) {
                    // we'll handle it somewhere else, so just ignore it here
                } else if (isChainedParameter(name)) {
                    List<String> chainedParemeters = params;
                    for (String chainedParameterString : chainedParemeters) {
                        QueryParameter chainedParameter;
                        if (isMultiResTypeSearch) {
                            chainedParameter = parseChainedParameter(resourceTypes, name, chainedParameterString);
                        } else {
                            chainedParameter = parseChainedParameter(resourceType, name, chainedParameterString);
                        }
                        parameters.add(chainedParameter);
                    }
                } else {
                    // Parse name into parameter name and modifier (if present).
                    String parameterCode = name;
                    String mod = null;
                    if (parameterCode.contains(":")) {
                        mod           = parameterCode.substring(parameterCode.indexOf(":") + 1);
                        parameterCode = parameterCode.substring(0, parameterCode.indexOf(":"));
                    }

                    SearchParameter searchParameter = null;
                    if (isMultiResTypeSearch) {
                      // Find the SearchParameter that will apply to all the resource types.
                      for (String resType: resourceTypes) {
                          // Get the search parameter from our filtered set of applicable SPs for this resource type.
                          searchParameter = getSearchParameter(resType, parameterCode);
                          if (searchParameter == null) {
                              String msg =
                                      "Search parameter '" + parameterCode + "' for resource type '"
                                              + resType + "' was not found.";
                              throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                          }
                      }
                    } else {
                        // Get the search parameter from our filtered set of applicable SPs for this resource type.
                        searchParameter = getSearchParameter(resourceType.getSimpleName(), parameterCode);
                        if (searchParameter == null) {
                            String msg =
                                    "Search parameter '" + parameterCode + "' for resource type '"
                                            + resourceType.getSimpleName() + "' was not found.";
                            throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                        }
                    }

                    // Get the type of parameter so that we can use it to parse the value.
                    Type type = Type.fromValue(searchParameter.getType().getValue());

                    // Process the modifier
                    Modifier modifier = null;
                    String modifierResourceTypeName = null;
                    if (mod != null) {
                        if (ModelSupport.isResourceType(mod)) {
                            modifier                 = Modifier.TYPE;
                            modifierResourceTypeName = mod;
                        } else {
                            try {
                                modifier = Modifier.fromValue(mod);
                            } catch (IllegalArgumentException e) {
                                String msg = "Undefined Modifier: " + mod;
                                throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                            }
                        }

                        if (modifier != null && !isAllowed(type, modifier)) {
                            String msg =
                                    "Unsupported type/modifier combination: " + type.value() + "/" + modifier.value();
                            throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                        }
                    }

                    for (String queryParameterValueString : queryParameters.get(name)) {
                        QueryParameter parameter = new QueryParameter(type, parameterCode, modifier, modifierResourceTypeName);
                        List<QueryParameterValue> queryParameterValues =
                                processQueryParameterValueString(resourceType, searchParameter, modifier, queryParameterValueString);
                        parameter.getValues().addAll(queryParameterValues);
                        parameters.add(parameter);
                    }
                } // end else
            } catch (FHIRSearchException se) {
                // There's a number of places that throw within this try block. In all cases we want the same behavior:
                // If we're in lenient mode and there was an issue parsing the query parameter then log and move on to the next one.
                String msg =
                        "Error while parsing search parameter '" + name + "' for resource type "
                                + resourceType.getSimpleName();
                if (lenient) {
                    // TODO add this to the list of supplemental warnings?
                    log.log(Level.FINE, msg, se);
                } else {
                    throw se;
                }
            } catch (Exception e) {
                throw SearchExceptionUtil.buildNewParseParameterException(name, e);
            }
        } // end for

        context.setSearchParameters(parameters);
        return context;
    }

    /**
     * Common logic from handling a single queryParameterValueString based on its type
     */
    private static List<QueryParameterValue> processQueryParameterValueString(Class<?> resourceType, SearchParameter searchParameter, Modifier modifier,
        String queryParameterValueString) throws FHIRSearchException, Exception {
        String parameterCode = searchParameter.getCode().getValue();
        Type type = Type.fromValue(searchParameter.getType().getValue());
        List<QueryParameterValue> queryParameterValues;
        if (Modifier.MISSING.equals(modifier)) {
            // FHIR search considers booleans a special case of token for some reason...
            queryParameterValues = parseQueryParameterValuesString(searchParameter, Type.TOKEN, queryParameterValueString, null);
        } else {
            if (Type.COMPOSITE == type) {
                List<Component> components = searchParameter.getComponent();
                List<Type> compTypes = new ArrayList<>(components.size());
                for (Component component : components) {
                    if (component.getDefinition() == null || !component.getDefinition().hasValue()) {
                        throw new IllegalStateException(String.format("Composite search parameter '%s' is "
                                + "missing one or more component definition", searchParameter.getName()));
                    }
                    SearchParameter referencedParam = getSearchParameter(resourceType, component.getDefinition());
                    compTypes.add(Type.fromValue(referencedParam.getType().getValue()));
                }
                queryParameterValues = parseCompositeQueryParameterValuesString(searchParameter, parameterCode, compTypes, queryParameterValueString);
            } else {
                queryParameterValues = parseQueryParameterValuesString(searchParameter, type, queryParameterValueString, null);
            }
        }
        return queryParameterValues;
    }

    private static List<QueryParameterValue> parseCompositeQueryParameterValuesString(SearchParameter searchParameter, String compositeParamCode,
        List<Type> compTypes, String queryParameterValuesString) throws FHIRSearchException {
        List<QueryParameterValue> parameterValues = new ArrayList<>();

        // BACKSLASH_NEGATIVE_LOOKBEHIND prevents it from splitting on ',' that are preceded by a '\'
        for (String v : queryParameterValuesString.split(SearchConstants.BACKSLASH_NEGATIVE_LOOKBEHIND + ",")) {
            String[] componentValueStrings = v.split(SearchConstants.BACKSLASH_NEGATIVE_LOOKBEHIND + "\\$");
            if (compTypes.size() != componentValueStrings.length) {
                throw new FHIRSearchException(String.format("Expected %d components but found %d in composite query value '%s'",
                    compTypes.size(), componentValueStrings.length, v));
            }
            QueryParameterValue parameterValue = new QueryParameterValue();
            for (int i = 0; i < compTypes.size(); i++) {
                List<QueryParameterValue> values = parseQueryParameterValuesString(searchParameter, compTypes.get(i), componentValueStrings[i], null);
                if (values.isEmpty()) {
                    throw new FHIRSearchException("Component values cannot be empty");
                } else if (values.size() > 1) {
                    throw new IllegalStateException("A single component can only have a single value");
                } else {
                    // exactly one
                    QueryParameter parameter = new QueryParameter(compTypes.get(i), compositeParamCode, null, null, values);
                    parameterValue.addComponent(parameter);
                }
            }

            parameterValues.add(parameterValue);
        }
        return parameterValues;
    }

    private static List<QueryParameterValue> parseQueryParameterValuesString(SearchParameter searchParameter, Type type,
        String queryParameterValuesString, String modifierResourceTypeName) throws FHIRSearchException {
        List<QueryParameterValue> parameterValues = new ArrayList<>();

        // BACKSLASH_NEGATIVE_LOOKBEHIND means it won't split on ',' that are preceded by a '\'
        String[] vals = queryParameterValuesString.split(SearchConstants.BACKSLASH_NEGATIVE_LOOKBEHIND + ",");
        for (String v : vals) {
            QueryParameterValue parameterValue = new QueryParameterValue();
            SearchConstants.Prefix prefix = null;
            switch (type) {
            case DATE: {
                // date
                // [parameter]=[prefix][value]
                prefix = getPrefix(v);
                if (prefix != null) {
                    v = v.substring(2);
                    parameterValue.setPrefix(prefix);
                }
                // Dispatches the population and treatment of the DateTime values to the handler.
                DateTimeHandler.parse(prefix, parameterValue,v);
                break;
            }
            case NUMBER: {
                // number
                // [parameter]=[prefix][value]
                prefix = getPrefix(v);
                if (prefix != null) {
                    v = v.substring(2);
                    parameterValue.setPrefix(prefix);
                }
                parameterValue.setValueNumber(new BigDecimal(v));
                break;
            }
            case REFERENCE: {
                // reference
                // [parameter]=[url]
                // [parameter]=[url|version] - canonical url
                // [parameter]=[type]/[id]
                // [parameter]=[id]
                String valueString = unescapeSearchParm(v);
                parameterValue.setValueString(valueString);
                ReferenceParameterHandler.generateReferenceParameterValues(searchParameter, parameterValues, valueString, vals, modifierResourceTypeName);
                break;
            }
            case QUANTITY: {
                // quantity
                // [parameter]=[prefix][number]|[system]|[code]
                prefix = getPrefix(v);
                if (prefix != null) {
                    v = v.substring(2);
                    parameterValue.setPrefix(prefix);
                }
                String[] parts = v.split(SearchConstants.BACKSLASH_NEGATIVE_LOOKBEHIND + "\\|");
                String number = parts[0];
                parameterValue.setValueNumber(new BigDecimal(number));

                if (parts.length > 1) {
                    String system = parts[1]; // could be empty string
                    parameterValue.setValueSystem(unescapeSearchParm(system));
                }
                if (parts.length > 2) {
                    String code = parts[2];
                    parameterValue.setValueCode(unescapeSearchParm(code));
                }
                break;
            }
            case STRING: {
                // string
                // [parameter]=[value]
                parameterValue.setValueString(unescapeSearchParm(v));
                break;
            }
            case TOKEN: {
                // token
                // [parameter]=[system]|[code]
                /*
                 * TODO: start enforcing this:
                 * "For token parameters on elements of type ContactPoint, uri, or boolean,
                 * the presence of the pipe symbol SHALL NOT be used - only the
                 * [parameter]=[code] form is allowed
                 */
                String[] parts = v.split(SearchConstants.BACKSLASH_NEGATIVE_LOOKBEHIND + "\\|");
                if (parts.length == 2) {
                    parameterValue.setValueSystem(unescapeSearchParm(parts[0]));
                    parameterValue.setValueCode(unescapeSearchParm(parts[1]));
                } else {
                    parameterValue.setValueCode(unescapeSearchParm(v));
                }
                break;
            }
            case URI: {
                // [parameter]=[value]
                parameterValue.setValueString(unescapeSearchParm(v));
                break;
            }
            case SPECIAL: {
                // Just in case any instance of SPECIAL supports prefix.
                prefix = getPrefix(v);
                if (prefix != null) {
                    v = v.substring(2);
                    parameterValue.setPrefix(prefix);
                }

                // One specific instance of SPECIAL is 'near'
                //[parameter]=[latitude]|[longitude]|[distance]|[units]
                // As there may be more in the future, we're leaving the parameter as a String
                // so the custom downstream logic can treat appropriately.
                parameterValue.setValueString(unescapeSearchParm(v));
                break;
            }
            default:
                break;
            }
            parameterValues.add(parameterValue);
        }
        return parameterValues;
    }

    /**
     * Un-escape search parameter values that were encoding based on FHIR escaping rules
     *
     * @param escapedString
     * @return unescapedString
     * @throws FHIRSearchException
     * @see https://www.hl7.org/fhir/r4/search.html#escaping
     */
    private static String unescapeSearchParm(String escapedString) throws FHIRSearchException {
        String unescapedString = escapedString.replace("\\$", "$").replace("\\|", "|").replace("\\,", ",");

        long numberOfSlashes = unescapedString.chars().filter(ch -> ch == '\\').count();

        // If there's an odd number of backslahses at this point, then the request was invalid
        if (numberOfSlashes % 2 == 1) {
            throw SearchExceptionUtil.buildNewInvalidSearchException(
                    "Bare '\\' characters are not allowed in search parameter values and must be escaped via '\\'.");
        }
        return unescapedString.replace("\\\\", "\\");
    }

    /**
     * @param type
     * @param modifier
     * @return
     */
    protected static boolean isAllowed(Type type, Modifier modifier) {
        return SearchConstants.RESOURCE_TYPE_MODIFIER_MAP.get(type).contains(modifier);
    }

    /**
     * Returns a list of SearchParameters that consist of those associated with the
     * "Resource" base resource type, as
     * well as those associated with the specified resource type.
     */
    public static List<SearchParameter> getApplicableSearchParameters(String resourceType) throws Exception {
        List<SearchParameter> result = getFilteredBuiltinSearchParameters(resourceType);
        result.addAll(getUserDefinedSearchParameters(resourceType));
        return result;
    }

    public static FHIRSearchContext parseQueryParameters(String compartmentName, String compartmentLogicalId,
            Class<?> resourceType,
            Map<String, List<String>> queryParameters, String queryString) throws Exception {
        return parseQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParameters, true);
    }

    /**
     * @param lenient
     *                Whether to ignore unknown or unsupported parameter
     * @return
     * @throws Exception
     */
    public static FHIRSearchContext parseQueryParameters(String compartmentName, String compartmentLogicalId,
            Class<?> resourceType, Map<String, List<String>> queryParameters, boolean lenient) throws Exception {
        List<QueryParameter> parameters = new ArrayList<>();
        QueryParameter parameter;
        QueryParameterValue value;
        QueryParameter rootParameter = null;

        if (compartmentName != null && compartmentLogicalId != null) {
            // The inclusion criteria are represented as a chain of parameters, each with a value of the
            // compartmentLogicalId.
            // The query parsers will OR these parameters to achieve the compartment search.
            List<String> inclusionCriteria =
                    CompartmentUtil.getCompartmentResourceTypeInclusionCriteria(compartmentName,
                            resourceType.getSimpleName());
            for (String criteria : inclusionCriteria) {
                parameter = new QueryParameter(Type.REFERENCE, criteria, null, null, true);
                value     = new QueryParameterValue();
                value.setValueString(compartmentName + "/" + compartmentLogicalId);
                parameter.getValues().add(value);
                if (rootParameter == null) {
                    rootParameter = parameter;
                } else {
                    if (rootParameter.getChain().isEmpty()) {
                        rootParameter.setNextParameter(parameter);
                    } else {
                        rootParameter.getChain().getLast().setNextParameter(parameter);
                    }
                }
            }
            parameters.add(rootParameter);
        }

        FHIRSearchContext context = parseQueryParameters(resourceType, queryParameters, lenient);
        context.getSearchParameters().addAll(parameters);
        return context;
    }

    private static SearchConstants.Prefix getPrefix(String s) throws FHIRSearchException {

        SearchConstants.Prefix returnPrefix = null;

        for (SearchConstants.Prefix prefix : SearchConstants.Prefix.values()) {
            if (s.startsWith(prefix.value())) {
                returnPrefix = prefix;
                break;
            }
        }

        return returnPrefix;
    }

    public static boolean isSearchResultParameter(String name) {
        return SearchConstants.SEARCH_RESULT_PARAMETER_NAMES.contains(name);
    }

    public static boolean isGeneralParameter(String name) {
        return FHIRConstants.GENERAL_PARAMETER_NAMES.contains(name);
    }

    private static void parseSearchResultParameter(Class<?> resourceType, FHIRSearchContext context, String name,
            List<String> values, boolean lenient) throws FHIRSearchException {
        String resourceTypeName = resourceType.getSimpleName();
        try {
            String first = values.get(0);
            // pageSize and pageNumber validation occurs in the persistence layer
            if (SearchConstants.COUNT.equals(name)) {
                int pageSize = Integer.parseInt(first);

                if (pageSize < 0) {
                    throw new IllegalArgumentException("pageSize must be greater than or equal to zero");
                } else if (pageSize == 0) {
                    // if _count has the value 0, this shall be treated the same as _summary=count
                    // https://www.hl7.org/fhir/r4/search.html#count
                    context.setSummaryParameter(SummaryValueSet.COUNT);
                } else {
                    // If the user specified a value > max, then use the max.
                    if (pageSize > SearchConstants.MAX_PAGE_SIZE) {
                        pageSize = SearchConstants.MAX_PAGE_SIZE;
                    }
                    context.setPageSize(pageSize);
                }
            } else if (SearchConstants.PAGE.equals(name)) {
                int pageNumber = Integer.parseInt(first);
                context.setPageNumber(pageNumber);
            } else if (SearchConstants.SORT.equals(name)) {
                // in R4, we only look for _sort
                sort.parseSortParameter(resourceTypeName, context, values, lenient);
            } else if (name.startsWith(SearchConstants.INCLUDE) || name.startsWith(SearchConstants.REVINCLUDE)) {
                parseInclusionParameter(resourceType, context, name, values, lenient);
            } else if (SearchConstants.ELEMENTS.equals(name)) {
                parseElementsParameter(resourceType, context, values, lenient);
            } else if (SearchConstants.SUMMARY.equals(name) && first != null) {
                context.setSummaryParameter(SummaryValueSet.from(first));
            }
        } catch (FHIRSearchException se) {
            throw se;
        } catch (Exception e) {
            throw SearchExceptionUtil.buildNewParseParameterException(name, e);
        }
    }


    public static boolean isChainedParameter(String name) {
        return name.contains(SearchConstants.CHAINED_PARAMETER_CHARACTER);
    }

    private static QueryParameter parseChainedParameter(HashSet<String> resourceTypes, String name, String valuesString)
            throws Exception {
        QueryParameter rootParameter = null;
        Class<?> resourceType = null;

        // declared here so we can remember the values from the last component in the chain after looping
        SearchParameter searchParameter = null;
        Modifier modifier = null;
        try {
            List<String> components = Arrays.asList(name.split("\\."));
            int lastIndex = components.size() - 1;
            int currentIndex = 0;

            Type type = null;

            for (String component : components) {
                modifier = null;
                String modifierResourceTypeName = null;
                String parameterName = component;

                // Optimization opportunity
                // substring + indexOf and contains execute similar operations
                // collapsing the branching logic is ideal
                int loc = parameterName.indexOf(SearchConstants.COLON_DELIMITER);
                if (loc > 0) {
                    // QueryParameter modifier exists
                    String mod = parameterName.substring(loc + 1);
                    if (ModelSupport.isResourceType(mod)) {
                        modifier                 = Modifier.TYPE;
                        modifierResourceTypeName = mod;
                    } else {
                        modifier = Modifier.fromValue(mod);
                    }

                    if (modifier != null && !Modifier.TYPE.equals(modifier)
                            && currentIndex < lastIndex) {
                        throw SearchExceptionUtil.buildNewInvalidSearchException(
                                String.format(MODIFIER_NOT_ALLOWED_WITH_CHAINED_EXCEPTION, modifier));
                    }
                    parameterName = parameterName.substring(0, parameterName.indexOf(":"));
                } else {
                    modifier = null;
                }

                HashSet<String> modifierResourceTypeName4ResourceTypes = new HashSet<>();
                if (resourceType != null) {
                    searchParameter = getSearchParameter(resourceType, parameterName);
                    type = Type.fromValue(searchParameter.getType().getValue());
                } else {
                    for (String resTypeName: resourceTypes) {
                        searchParameter = getSearchParameter(ModelSupport.getResourceType(resTypeName), parameterName);
                        type = Type.fromValue(searchParameter.getType().getValue());

                        if (!Type.REFERENCE.equals(type) && currentIndex < lastIndex) {
                            throw SearchExceptionUtil.buildNewInvalidSearchException(
                                    String.format(TYPE_NOT_ALLOWED_WITH_CHAINED_PARAMETER_EXCEPTION, type));
                        }

                        List<ResourceType> targets = searchParameter.getTarget();
                        if (modifierResourceTypeName != null && !targets.contains(ResourceType.of(modifierResourceTypeName))) {
                            throw SearchExceptionUtil.buildNewInvalidSearchException(
                                    String.format(MODIFIYERRESOURCETYPE_NOT_ALLOWED_FOR_RESOURCETYPE, modifierResourceTypeName,
                                            parameterName, resTypeName));
                        }

                        if (modifierResourceTypeName == null && targets.size() > 1 && currentIndex < lastIndex) {
                            throw SearchExceptionUtil.buildNewInvalidSearchException(
                                    String.format(SEARCH_PARAMETER_MODIFIER_NAME, parameterName));
                        }

                        if (modifierResourceTypeName == null && currentIndex < lastIndex) {
                            modifier                 = Modifier.TYPE;
                            modifierResourceTypeName4ResourceTypes.add(targets.get(0).getValue());
                        }
                    }
                }


                if (modifierResourceTypeName4ResourceTypes.size() > 1) {
                    String.format(DIFFERENT_MODIFIYERRESOURCETYPES_FOUND_FOR_RESOURCETYPES, parameterName);
                } else if (modifierResourceTypeName4ResourceTypes.size() == 1) {
                    modifierResourceTypeName = modifierResourceTypeName4ResourceTypes.iterator().next();
                }

                QueryParameter parameter = new QueryParameter(type, parameterName, modifier, modifierResourceTypeName);
                if (rootParameter == null) {
                    rootParameter = parameter;
                } else {
                    if (rootParameter.getChain().isEmpty()) {
                        rootParameter.setNextParameter(parameter);
                    } else {
                        rootParameter.getChain().getLast().setNextParameter(parameter);
                    }
                }

                // moves the movement of the chain.
                // Non standard resource support?
                if (currentIndex < lastIndex) {
                    // FHIRUtil.getResourceType(modifierResourceTypeName)
                    resourceType = ModelSupport.getResourceType(modifierResourceTypeName);
                }

                currentIndex++;
            } // end for loop

            List<QueryParameterValue> valueList = processQueryParameterValueString(resourceType, searchParameter, modifier, valuesString);
            rootParameter.getChain().getLast().getValues().addAll(valueList);
        } catch (FHIRSearchException e) {
            throw e;
        } catch (Exception e) {
            throw SearchExceptionUtil.buildNewChainedParameterException(name, e);
        }

        return rootParameter;
    }

    private static QueryParameter parseChainedParameter(Class<?> resourceType, String name, String valuesString)
            throws Exception {

        QueryParameter rootParameter = null;

        try {
            List<String> components = Arrays.asList(name.split("\\."));
            int lastIndex = components.size() - 1;
            int currentIndex = 0;

            Type type = null;

            // declared here so we can remember the values from the last component in the chain after looping
            SearchParameter searchParameter = null;
            Modifier modifier = null;
            for (String component : components) {
                String modifierResourceTypeName = null;
                String parameterName = component;

                // Optimization opportunity
                // substring + indexOf and contains execute similar operations
                // collapsing the branching logic is ideal
                int loc = parameterName.indexOf(SearchConstants.COLON_DELIMITER);
                if (loc > 0) {
                    // QueryParameter modifier exists
                    String mod = parameterName.substring(loc + 1);
                    if (ModelSupport.isResourceType(mod)) {
                        modifier                 = Modifier.TYPE;
                        modifierResourceTypeName = mod;
                    } else {
                        modifier = Modifier.fromValue(mod);
                    }

                    if (modifier != null && !Modifier.TYPE.equals(modifier)
                            && currentIndex < lastIndex) {
                        throw SearchExceptionUtil.buildNewInvalidSearchException(
                                String.format(MODIFIER_NOT_ALLOWED_WITH_CHAINED_EXCEPTION, modifier));
                    }
                    parameterName = parameterName.substring(0, parameterName.indexOf(":"));
                } else {
                    modifier = null;
                }

                searchParameter = getSearchParameter(resourceType, parameterName);
                type = Type.fromValue(searchParameter.getType().getValue());

                if (!Type.REFERENCE.equals(type) && currentIndex < lastIndex) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException(
                            String.format(TYPE_NOT_ALLOWED_WITH_CHAINED_PARAMETER_EXCEPTION, type));
                }

                List<ResourceType> targets = searchParameter.getTarget();
                // Check if the modifier resource type is invalid.
                if (modifierResourceTypeName != null && !targets.contains(ResourceType.of(modifierResourceTypeName))) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException(
                            String.format(MODIFIYERRESOURCETYPE_NOT_ALLOWED_FOR_RESOURCETYPE, modifierResourceTypeName,
                                    parameterName, resourceType.getSimpleName()));
                }

                if (modifierResourceTypeName == null && targets.size() > 1 && currentIndex < lastIndex) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException(
                            String.format(SEARCH_PARAMETER_MODIFIER_NAME, parameterName));
                }

                if (modifierResourceTypeName == null && currentIndex < lastIndex) {
                    modifierResourceTypeName = targets.get(0).getValue();
                    modifier                 = Modifier.TYPE;
                }

                QueryParameter parameter = new QueryParameter(type, parameterName, modifier, modifierResourceTypeName);
                if (rootParameter == null) {
                    rootParameter = parameter;
                } else {
                    if (rootParameter.getChain().isEmpty()) {
                        rootParameter.setNextParameter(parameter);
                    } else {
                        rootParameter.getChain().getLast().setNextParameter(parameter);
                    }
                }

                // moves the movement of the chain.
                // Non standard resource support?
                if (currentIndex < lastIndex) {
                    // FHIRUtil.getResourceType(modifierResourceTypeName)
                    resourceType = ModelSupport.getResourceType(modifierResourceTypeName);
                }

                currentIndex++;
            } // end for loop

            List<QueryParameterValue> valueList = processQueryParameterValueString(resourceType, searchParameter, modifier, valuesString);
            rootParameter.getChain().getLast().getValues().addAll(valueList);
        } catch (FHIRSearchException e) {
            throw e;
        } catch (Exception e) {
            throw SearchExceptionUtil.buildNewChainedParameterException(name, e);
        }

        return rootParameter;
    }

    /**
     * Transforms the passed QueryParameter representing chained inclusion criteria, into
     * an actual chain of QueryParameter objects. This method consumes QueryParameters
     * with names of this form:
     * <pre>
     * "{attribute1}.{attribute2}:{resourceType}"
     * </pre>
     * For specific examples of chained inclusion criteria, see the FHIR spec for the
     * <a href="https://www.hl7.org/fhir/compartment-patient.html">Patient compartment</a>
     *
     * @param inclusionCriteriaParm
     * @return QueryParameter - The root of a parameter chain for chained inclusion
     *         criteria.
     */
    public static QueryParameter parseChainedInclusionCriteria(QueryParameter inclusionCriteriaParm) {
        QueryParameter rootParameter = null;
        QueryParameter chainedInclusionCriteria = null;
        String[] qualifiedInclusionCriteria;
        String[] parmNames = inclusionCriteriaParm.getCode().split("\\.");
        String resourceType = inclusionCriteriaParm.getCode().split(SearchConstants.COLON_DELIMITER_STR)[1];
        for (int i = 0; i < parmNames.length; i++) {

            // indexOf(char) is faster than contains str
            if (parmNames[i].indexOf(SearchConstants.COLON_DELIMITER) != -1) {
                qualifiedInclusionCriteria = parmNames[i].split(SearchConstants.COLON_DELIMITER_STR);
                chainedInclusionCriteria   =
                        new QueryParameter(Type.REFERENCE, qualifiedInclusionCriteria[0], null, resourceType,
                                inclusionCriteriaParm.getValues());
            } else {
                chainedInclusionCriteria =
                        new QueryParameter(Type.REFERENCE, parmNames[i], null, resourceType);
            }
            if (rootParameter == null) {
                rootParameter = chainedInclusionCriteria;
            } else if (rootParameter.getNextParameter() == null) {
                rootParameter.setNextParameter(chainedInclusionCriteria);
            } else {
                rootParameter.getChain().getLast().setNextParameter(chainedInclusionCriteria);
            }
        }
        return rootParameter;
    }

    /**
     * Normalizes a string to be used as a search parameter value. All accents and
     * diacritics are removed. And then the
     * string is transformed to lower case.
     *
     * @param value
     * @return
     */
    public static String normalizeForSearch(String value) {

        String normalizedValue = null;
        if (value != null) {
            normalizedValue = Normalizer.normalize(value, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            normalizedValue = normalizedValue.toLowerCase();
        }

        return normalizedValue;
    }

    /**
     * Parses _include and _revinclude search parameters contained in the query string, and produces
     * InclusionParameter objects to represent those parameters. The InclusionParameter objects are included
     * in the appropriate collections encapsulated in the passed FHIRSearchContext.
     *
     * @param resourceType
     *     the search resource type
     * @param context
     *     the search context
     * @param inclusionKeyword
     *     the type of inclusion, either '_include' or '_revinclude'
     * @param inclusionValues
     *     the inclusion values, each containing joinResourceType, searchParameterName,
     *     and optionally searchParameterTargetType, colon-delimited
     * @param lenient
     *     the validation level
     * @throws Exception
     */
    private static void parseInclusionParameter(Class<?> resourceType, FHIRSearchContext context, String inclusionKeyword, List<String> inclusionValues,
        boolean lenient) throws Exception {

        String[] inclusionValueParts;
        String joinResourceType;
        String searchParameterName;
        String searchParameterTargetType;

        SearchParameter searchParm;
        InclusionParameter newInclusionParm;
        List<InclusionParameter> newInclusionParms;

        for (String inclusionValue : inclusionValues) {

            // Parse value into 3 parts: joinResourceType, searchParameterName, searchParameterTargetType
            inclusionValueParts = inclusionValue.split(":");
            if (inclusionValueParts.length < 2) {
                throw SearchExceptionUtil.buildNewInvalidSearchException("A value for _include or _revinclude must have at least 2 parts separated by a colon.");
            }
            joinResourceType = inclusionValueParts[0];
            searchParameterName = inclusionValueParts[1];
            searchParameterTargetType = inclusionValueParts.length == 3 ? inclusionValueParts[2] : null;

            // For _include parameter, join resource type must match resource type being searched
            if (SearchConstants.INCLUDE.equals(inclusionKeyword) && !joinResourceType.equals(resourceType.getSimpleName())) {
                throw SearchExceptionUtil.buildNewInvalidSearchException(
                        "The join resource type must match the resource type being searched.");
            }

            // For _revinclude parameter, target resource type, if specified, must match resource type being searched
            if (SearchConstants.REVINCLUDE.equals(inclusionKeyword) && searchParameterTargetType != null
                    && !searchParameterTargetType.equals(resourceType.getSimpleName())) {
                throw SearchExceptionUtil.buildNewInvalidSearchException("The search parameter target type must match the resource type being searched.");
            }

            // Ensure that the Inclusion Parameter being parsed is a valid search parameter of type 'reference'.
            Map<String, SearchParameter> searchParametersMap;
            if (SearchConstants.WILDCARD.equals(searchParameterName)) {
                searchParametersMap = getInclusionWildcardSearchParameters(resourceType.getSimpleName(), joinResourceType, searchParameterTargetType, inclusionKeyword);
                if (searchParametersMap.isEmpty()) {
                    log.fine("No valid inclusion parameters found for wildcard search.");
                }
            } else {
                searchParm = getSearchParameter(joinResourceType, searchParameterName);
                if (searchParm == null) {
                    String msg = "Undefined Inclusion Parameter: " + inclusionValue;
                    if (lenient) {
                        log.fine(msg);
                        continue;
                    } else {
                        throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                    }
                }
                if (!SearchParamType.REFERENCE.equals(searchParm.getType())) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException("Inclusion Parameter must be of type 'reference'. "
                            + "The passed Inclusion Parameter is of type: '" + searchParm.getType().getValue() + "'");
                }
                searchParametersMap = Collections.singletonMap(searchParameterName, searchParm);
            }
            for (Map.Entry<String, SearchParameter> entry : searchParametersMap.entrySet()) {
                if (inclusionKeyword.equals(SearchConstants.INCLUDE)) {
                    newInclusionParms =
                            buildIncludeParameter(resourceType, joinResourceType, entry.getValue(), entry.getKey(), searchParameterTargetType);
                    context.getIncludeParameters().addAll(newInclusionParms);
                } else {
                    newInclusionParm =
                            buildRevIncludeParameter(resourceType, joinResourceType, entry.getValue(), entry.getKey(), searchParameterTargetType);
                    context.getRevIncludeParameters().add(newInclusionParm);
                }
            }
        }
    }

    /**
     * Builds and returns a collection of InclusionParameter objects representing
     * occurrences of the _include search result parameter in the query string.
     *
     * @throws FHIRSearchException
     */
    private static List<InclusionParameter> buildIncludeParameter(Class<?> resourceType, String joinResourceType, SearchParameter searchParm,
        String searchParameterName, String searchParameterTargetType) throws FHIRSearchException {

        List<InclusionParameter> includeParms = new ArrayList<>();

        // If no searchParameterTargetType was specified, create an InclusionParameter instance for each of the search
        // parameter's defined target types.
        if (searchParameterTargetType == null) {
            for (Code targetType : searchParm.getTarget()) {
                searchParameterTargetType = targetType.getValue();
                includeParms
                        .add(new InclusionParameter(joinResourceType, searchParameterName, searchParameterTargetType));
            }
        }
        // Validate the specified target type is correct.
        else {
            if (!isValidTargetType(searchParameterTargetType, searchParm)) {
                throw SearchExceptionUtil.buildNewInvalidSearchException(INVALID_TARGET_TYPE_EXCEPTION);
            }
            includeParms.add(new InclusionParameter(joinResourceType, searchParameterName, searchParameterTargetType));
        }
        return includeParms;
    }

    /**
     * Builds and returns a collection of InclusionParameter objects representing
     * occurrences of the _revinclude search result parameter in the query string.
     *
     * @throws FHIRSearchException
     */
    private static InclusionParameter buildRevIncludeParameter(Class<?> resourceType, String joinResourceType, SearchParameter searchParm,
        String searchParameterName, String searchParameterTargetType) throws FHIRSearchException {

        // Verify that the search parameter target type is correct
        if (searchParameterTargetType == null) {
            searchParameterTargetType = resourceType.getSimpleName();
        }
        if (!isValidTargetType(searchParameterTargetType, searchParm)) {
            throw SearchExceptionUtil.buildNewInvalidSearchException(INVALID_TARGET_TYPE_EXCEPTION);
        }
        return new InclusionParameter(joinResourceType, searchParameterName, searchParameterTargetType);

    }

    /**
     * Verifies that the passed searchParameterTargetType is a valid target type for
     * the passed searchParm
     *
     * @param searchParameterTargetType
     * @param searchParm
     * @return
     */
    private static boolean isValidTargetType(String searchParameterTargetType, SearchParameter searchParm) {

        boolean validTargetType = false;

        for (Code targetType : searchParm.getTarget()) {
            if (targetType.getValue().equals(searchParameterTargetType)) {
                validTargetType = true;
                break;
            }
        }
        return validTargetType;
    }

    /**
     * Parses _elements search result parameter contained in the query string, and
     * produces element String objects to
     * represent the values for _elements. Those Strings are included in the
     * elementsParameters collection contained in
     * the passed FHIRSearchContext.
     *
     * @param lenient
     *                Whether to ignore unknown or unsupported elements
     * @throws Exception
     */
    private static void parseElementsParameter(Class<?> resourceType, FHIRSearchContext context,
            List<String> elementLists, boolean lenient) throws Exception {

        Set<String> resourceFieldNames = JsonSupport.getElementNames(resourceType);

        for (String elements : elementLists) {

            // For other parameters, we pass the comma-separated list of values to the PL
            // but for elements, we need to process that here
            for (String elementName : elements.split(SearchConstants.BACKSLASH_NEGATIVE_LOOKBEHIND + ",")) {
                if (elementName.startsWith("_")) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException("Invalid element name: " + elementName);
                }
                if (!resourceFieldNames.contains(elementName)) {
                    if (lenient) {
                        // TODO add this to the list of supplemental warnings?
                        log.fine("Skipping unknown element name: " + elementName);
                        continue;
                    } else {
                        throw SearchExceptionUtil
                                .buildNewInvalidSearchException("Unknown element name: " + elementName);
                    }
                }
                context.addElementsParameter(elementName);
            }
        }
    }

    /**
     * Build the self link from the search parameters actually used by the server
     *
     * @throws URISyntaxException
     * @see https://hl7.org/fhir/r4/search.html#conformance
     */
    public static String buildSearchSelfUri(String requestUriString, FHIRSearchContext context)
            throws URISyntaxException {
        /*
         * the bulk of this method was refactored into UriBuilder.java the signature is
         * maintained here for backwards compatibility, and as a simple helper function.
         */
        return UriBuilder.builder().context(context).requestUri(requestUriString).toSearchSelfUri();
    }

    /**
     * Return only the "text" element, the 'id' element, the 'meta' element, and
     * only top-level mandatory elements.
     * The id, meta and the top-level mandatory elements will be added by the
     * ElementFilter automatically.
     *
     * @param resourceType
     * @return
     */
    public static Set<String> getSummaryTextElementNames(Class<?> resourceType) {
        // Align with other getSummaryxxx functions, we may need the input resourceType in the future
        Set<String> summaryTextList = new HashSet<>();
        summaryTextList.add("text");
        return Collections.unmodifiableSet(summaryTextList);
    }
}

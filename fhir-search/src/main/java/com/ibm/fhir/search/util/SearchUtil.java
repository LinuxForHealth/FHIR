/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.util;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.model.type.code.SearchComparator;
import com.ibm.fhir.model.type.code.SearchModifierCode;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.model.util.JsonSupport;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.SummaryValueSet;
import com.ibm.fhir.search.TotalValueSet;
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
import com.ibm.fhir.search.reference.value.CompartmentReference;
import com.ibm.fhir.search.sort.Sort;
import com.ibm.fhir.search.uri.UriBuilder;
import com.ibm.fhir.search.util.ReferenceValue.ReferenceType;

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
    private static final String SEARCH_PARAMETER_NOT_FOUND = "Search parameter '%s' for resource type '%s' was not found.";
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
    private static final String INCORRECT_NUMBER_OF_COMPONENTS_FOR_REVERSE_CHAIN_SEARCH =
            "An incorrect number of components were specified for '_has' (reverse chain) search.";
    private static final String INVALID_RESOURCE_TYPE_FOR_REVERSE_CHAIN_SEARCH =
            "Resource type '%s' is not valid for '_has' (reverse chain) search.";
    private static final String PARAMETER_TYPE_NOT_REFERENCE_FOR_REVERSE_CHAIN_SEARCH =
            "Search parameter '%s' is not of type reference for '_has' (reverse chain) search.";
    private static final String TARGET_TYPE_OF_REFERENCE_PARAMETER_NOT_VALID_FOR_REVERSE_CHAIN_SEARCH =
            "Search parameter '%s' target types do not include expected type '%s' for '_has' (reverse chain) search.";
    private static final String LOGICAL_ID_VALUE_NOT_ALLOWED_FOR_REFERENCE_SEARCH =
            "Search parameter '%s' with value '%s' must have resource type name modifier.";

    // Other Constants
    private static final String SEARCH_PARAM_COMBINATION_ANY = "*";
    private static final String SEARCH_PARAM_COMBINATION_DELIMITER = "\\+";
    private static final String SEARCH_PROPERTY_TYPE_INCLUDE = "_include";
    private static final String SEARCH_PROPERTY_TYPE_REVINCLUDE = "_revinclude";
    private static final String HAS_DELIMITER = SearchConstants.COLON_DELIMITER_STR + SearchConstants.HAS + SearchConstants.COLON_DELIMITER_STR;

    // compartment parameter reference which can be ignore
    private static final String COMPARTMENT_PARM_DEF = "{def}";

    private static final String IBM_COMPOSITE_PREFIX = "ibm_composite_";

    // The functionality is split into a new class.
    private static final Sort sort = new Sort();

    /*
     * This is our in-memory cache of SearchParameter objects. The cache is organized at the top level by tenant-id,
     * with the built-in (FHIR spec-defined) SearchParameters stored under the "built-in" pseudo-tenant-id.
     * SearchParameters contained in the default tenant's extension-search-parameters.json file are stored under the
     * "default" tenant-id, and other tenants' SearchParameters (defined in their tenant-specific
     * extension-search-parameters.json files) will be stored under their respective tenant-ids as well. The objects
     * stored in our cache are of type CachedObjectHolder, with each one containing a Map<String, Map<String,
     * SearchParameter>>. This map is keyed by resource type (simple name, e.g. "Patient"). Each object stored in this
     * map contains the SearchParameters for that resource type, keyed by SearchParameter name (e.g. "_lastUpdated").
     * When getSearchParameter(resourceType, name) is called, we'll need to first search in the current tenant's map,
     * then if not found, look in the "built-in" tenant's map. Also, when getSearchParameters(resourceType) is called,
     * we'll need to return a List that contains SearchParameters from the current tenant's map (if present) plus those
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
     *     the resource type for which user-defined SearchParameters will be returned
     * @return a list of user-defined SearchParameters associated with the specified resource type
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
     * specified resource type and those associated with the "Resource" resource type.
     *
     * @param resourceType
     *     the resource type
     * @return a filtered list of SearchParameters
     * @throws Exception
     */
    protected static List<SearchParameter> getFilteredBuiltinSearchParameters(String resourceType) throws Exception {
        List<SearchParameter> result = new ArrayList<>();

        Map<String, ParametersMap> spBuiltin = ParametersUtil.getBuiltInSearchParametersMap();

        // Retrieve the current tenant's search parameter filtering rules.
        Map<String, Map<String, String>> filterRules = getFilterRules();

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
                Map<String, SearchParameter> resultCodes = result.stream()
                        .collect(Collectors.toMap(sp -> sp.getCode().getValue(), sp -> sp));

                for (SearchParameter sp : superParams) {
                    String spCode = sp.getCode().getValue();
                    if (resultCodes.containsKey(spCode)) {
                        SearchParameter existingSP = resultCodes.get(spCode);
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
     * Filters the specified input list of SearchParameters according to the filter rules and input resource type.
     * The filter rules are contained in a Map<String, List<String>> that is keyed by resource type.
     * The value of each Map entry is a list of search parameter names that should be included in our filtered result.
     *
     * @param filterRules
     *     a Map containing filter rules
     * @param resourceType
     *     the resource type associated with each of the unfiltered SearchParameters
     * @param unfilteredSearchParameters
     *     the unfiltered Collection of SearchParameter objects
     * @return a filtered Collection of SearchParameters
     * @implSpec This method guarantees that each search parameter returned in the Collection will have a unique code.
     */
    private static Collection<SearchParameter> filterSearchParameters(Map<String, Map<String, String>> filterRules,
            String resourceType, Collection<SearchParameter> unfilteredSearchParameters) {
        Map<String, SearchParameter> results = new HashMap<>();

        // First, retrieve the filter rule (list of SP urls to be included) for the specified resource type.
        // We know that the SearchParameters in the unfiltered list are all associated with this resource type,
        // so we can use this same "url list" for each Search Parameter in the unfiltered list.
        Map<String, String> includedSPs = filterRules.get(resourceType);

        if (includedSPs == null) {
            // If the specified resource type wasn't found in the Map then retrieve the wildcard entry if present.
            includedSPs = filterRules.get(SearchConstants.WILDCARD_FILTER);
        }

        // If we found a non-empty list of search parameters to filter on, then do the filtering.
        if (includedSPs != null && !includedSPs.isEmpty()) {
            boolean includeAll = includedSPs.containsKey(SearchConstants.WILDCARD_FILTER);

            // Walk through the unfiltered list and select the ones to be included in our result.
            for (SearchParameter sp : unfilteredSearchParameters) {
                String code = sp.getCode().getValue();
                String url = sp.getUrl().getValue();

                if (includedSPs.containsKey(code)) {
                    String configuredUrl = includedSPs.get(code);
                    if (configuredUrl != null && configuredUrl.equals(url)) {
                        results.put(code, sp);
                    } else if (log.isLoggable(Level.FINE)) {
                        log.fine("Skipping search parameter with id='" + sp.getId() + "'. "
                                + "Tenant configuration for resource='" + resourceType + "' code='" + code + "' "
                                + "does not match url '" + url + "'");
                    }
                } else if (includeAll) {
                    // If "*" is contained in the included SP urls, then include the search parameter
                    // if it doesn't conflict with any of the previously added parameters
                    if (!results.containsKey(code)) {
                        results.put(code, sp);
                    } else {
                        log.warning("Skipping search parameter with id='" + sp.getId() + "'. "
                                + "Found multiple search parameters for code '" + code + "' on resource type '" + resourceType + "';"
                                + " use search parameter filtering to disambiguate.");
                    }
                }
            }
        }

        return results.values();
    }

    /**
     * Retrieves the search parameter filtering rules for the current tenant.
     *
     * @return a map of resource types to allowed search parameters;
     *          the first map is keyed by resource type ('*' for all resource types)
     *          and the second map is keyed by search parameter code ('*':'*' for all applicable built-in parameters).
     * @throws Exception an exception
     */
    private static Map<String, Map<String, String>> getFilterRules() throws Exception {
        Map<String, Map<String, String>> result = new HashMap<>();
        boolean supportOmittedRsrcTypes = true;

        // Retrieve the "resources" config property group.
        PropertyGroup rsrcsGroup = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_RESOURCES);
        if (rsrcsGroup != null) {
            List<PropertyEntry> rsrcsEntries = rsrcsGroup.getProperties();
            if (rsrcsEntries != null && !rsrcsEntries.isEmpty()) {
                for (PropertyEntry rsrcsEntry : rsrcsEntries) {

                    // Check special property for including omitted resource types
                    if (FHIRConfiguration.PROPERTY_FIELD_RESOURCES_OPEN.equals(rsrcsEntry.getName())) {
                        if (rsrcsEntry.getValue() instanceof Boolean) {
                            supportOmittedRsrcTypes = (Boolean) rsrcsEntry.getValue();
                        } else {
                            throw SearchExceptionUtil.buildNewIllegalStateException();
                        }
                    }
                    else {
                        String resourceType = rsrcsEntry.getName();
                        PropertyGroup resourceTypeGroup = (PropertyGroup) rsrcsEntry.getValue();
                        if (resourceTypeGroup != null) {
                            Map<String, String> searchParameterUrls = new HashMap<>();

                            // Get search parameters
                            PropertyGroup spGroup = resourceTypeGroup.getPropertyGroup(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_SEARCH_PARAMETERS);
                            if (spGroup != null) {
                                List<PropertyEntry> spEntries = spGroup.getProperties();
                                if (spEntries != null && !spEntries.isEmpty()) {
                                    for (PropertyEntry spEntry : spEntries) {
                                        searchParameterUrls.put(spEntry.getName(), (String) spEntry.getValue());
                                    }
                                }
                            } else {
                                searchParameterUrls.put(SearchConstants.WILDCARD, SearchConstants.WILDCARD);
                            }
                            result.put(resourceType, searchParameterUrls);
                        }
                    }
                }
            }
        }

        if (supportOmittedRsrcTypes) {
            // All other resource types include all search parameters
            result.put(SearchConstants.WILDCARD, Collections.singletonMap(SearchConstants.WILDCARD, SearchConstants.WILDCARD));
        }

        return result;
    }

    /**
     * Returns the SearchParameter map (keyed by resource type) for the specified
     * tenant-id, or null if there are no SearchParameters for the tenant.
     *
     * @param tenantId
     *     the tenant-id whose SearchParameters should be returned.
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
    public static SearchParameter getSearchParameter(Class<?> resourceType, String code) throws Exception {
        return getSearchParameter(resourceType.getSimpleName(), code);
    }

    /**
     * @param resourceType
     * @param code
     * @return the SearchParameter for type {@code resourceType} with code {@code code} or null if it doesn't exist
     * @throws Exception
     */
    public static SearchParameter getSearchParameter(String resourceType, String code) throws Exception {
        SearchParameter result = null;

        Map<String, Map<String, String>> filterRules = getFilterRules();
        Map<String, String> targetResourceFilterRules = filterRules.get(resourceType);
        Map<String, String> parentResourceFilterRules = filterRules.get(SearchConstants.WILDCARD);

        String tenantId = FHIRRequestContext.get().getTenantId();
        Map<String, ParametersMap> tenantSpMap = getTenantOrDefaultSPMap(tenantId);

        if (targetResourceFilterRules != null && targetResourceFilterRules.containsKey(code)) {
            Canonical uri = Canonical.of(targetResourceFilterRules.get(code));
            result = getSearchParameterByUrlFromTenantOrBuiltIn(resourceType, code, tenantSpMap, uri);
        } else if (parentResourceFilterRules != null && parentResourceFilterRules.containsKey(code)) {
            Canonical uri = Canonical.of(parentResourceFilterRules.get(code));
            result = getSearchParameterByUrlFromTenantOrBuiltIn(resourceType, code, tenantSpMap, uri);
        } else if (targetResourceFilterRules == null || targetResourceFilterRules.containsKey(SearchConstants.WILDCARD)) {
            Set<SearchParameter> params = getSearchParametersByCodeFromTenantOrBuiltIn(resourceType, code, tenantSpMap);

            if (params != null && !params.isEmpty()) {
                result = params.iterator().next();
                if (params.size() > 1) {
                    log.warning("Found multiple resource-specific search parameters for code '" + code + "' on resource type " + resourceType + ";"
                            + " use search parameter filtering to disambiguate. Using '" + result.getUrl().getValue() + "'.");
                }
            }
        } else if (parentResourceFilterRules == null || parentResourceFilterRules.containsKey(SearchConstants.WILDCARD)) {
            Set<SearchParameter> params = getSearchParametersByCodeFromTenantOrBuiltIn(SearchConstants.RESOURCE_RESOURCE, code, tenantSpMap);

            if (params != null && !params.isEmpty()) {
                result = params.iterator().next();
                if (params.size() > 1) {
                    log.warning("Found multiple cross-resource search parameters for code '" + code + "';"
                            + " use search parameter filtering to disambiguate. Using '" + result.getUrl().getValue() + "'.");
                }
            }
        }

        if (result == null && log.isLoggable(Level.FINE)) {
            log.fine("SearchParameter with code '" + code + "' on resource type " + resourceType + " was not found.");
        }

        return result;
    }

    /**
     * This private method does <em>not</em> apply filtering because it is expected to be used directly from processing the filter.
     */
    private static SearchParameter getSearchParameterByUrlFromTenantOrBuiltIn(String resourceType, String code, Map<String, ParametersMap> tenantSpMap,
            Canonical uri) {
        // First try to find the search parameter within the specified tenant's map.
        SearchParameter result = getSearchParameterByUrlIfPresent(tenantSpMap, resourceType, uri);

        // If we didn't find it within the tenant's map, then look within the built-in map.
        if (result == null) {
            result = getSearchParameterByUrlIfPresent(ParametersUtil.getBuiltInSearchParametersMap(), resourceType, uri);
        }

        if (result == null) {
            log.warning("Configured search parameter with url '" + uri.getValue() + "' for code '" + code +
                "' on resource type '" + resourceType + "' could not be found." );
        }
        return result;
    }

    /**
     * This private method does <em>not</em> apply filtering because it is expected to be used directly from processing the filter.
     */
    private static Set<SearchParameter> getSearchParametersByCodeFromTenantOrBuiltIn(String resourceType, String code, Map<String, ParametersMap> tenantSpMap) {
        // First try to find the search parameters within the specified tenant's map.
        Set<SearchParameter> params = getSearchParametersByCodeIfPresent(tenantSpMap, resourceType, code);

        // If we didn't find any within the tenant's map, then look within the built-in map.
        if (params == null || params.isEmpty()) {
            params = getSearchParametersByCodeIfPresent(ParametersUtil.getBuiltInSearchParametersMap(), resourceType, code);
        }
        return params;
    }

    /**
     * This private method does <em>not</em> apply filtering because it is expected to be used directly from processing the filter.
     *
     * @param spMaps
     * @param resourceType
     * @param code
     * @return the SearchParameter for type {@code resourceType} with code {@code code} or null if it doesn't exist
     */
    private static Set<SearchParameter> getSearchParametersByCodeIfPresent(Map<String, ParametersMap> spMaps, String resourceType, String code) {
        Set<SearchParameter> result = null;

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

                // Check if this search parameter applies to the base Resource type
                ResourceType rt = result.getBase().get(0).as(ResourceType.class);
                if (SearchConstants.RESOURCE_RESOURCE.equals(rt.getValue())) {
                    resourceType = rt.getValue();
                }
                Collection<SearchParameter> filteredResult =
                        filterSearchParameters(getFilterRules(), resourceType, Collections.singleton(result));

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
     *     the resource type being searched for
     * @param joinResourceType
     *     the resource type for which inclusion search parameters will be returned
     * @param searchParameterTargetType
     *     the target resource type for included resources
     * @param inclusionKeyword
     *     the inclusion type, either _include or _revinclude
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
        HashSet<String> resourceTypes = new LinkedHashSet<>();

        // Check for duplicate parameters that are supposed to be specified at most once
        for (Entry<String, List<String>> entry : queryParameters.entrySet()) {
            String name = entry.getKey();
            if (isSearchSingletonParameter(name) && entry.getValue().size() > 1) {
                manageException("Search parameter '" + name + "' is specified multiple times", lenient);
            }
        }

        // Check for unsupported uses of _include/_revinclude
        if (queryParameters.containsKey(SearchConstants.INCLUDE)
                || queryParameters.containsKey(SearchConstants.REVINCLUDE)) {
            // Make sure _sort is not present with _include and/or _revinclude.
            // TODO: do we really need to forbid this?
            if (queryParameters.containsKey(SearchConstants.SORT)) {
                throw SearchExceptionUtil.buildNewInvalidSearchException(
                        "_sort search result parameter not supported with _include or _revinclude.");
            }
            // Make sure _total is not present with _include and/or _revinclude.
            // TODO: do we really need to forbid this?
            if (queryParameters.containsKey(SearchConstants.TOTAL)) {
                throw SearchExceptionUtil.buildNewInvalidSearchException(
                        "_total search result parameter not supported with _include or _revinclude.");
            }
            // Because _include and _revinclude searches all require certain resource type modifier in
            // search parameter, so we just don't support it.
            if (Resource.class.equals(resourceType)) {
                throw SearchExceptionUtil.buildNewInvalidSearchException(
                        "system search not supported with _include or _revinclude.");
            }
        }

        // Check for unsupported uses of _type
        if (queryParameters.containsKey(SearchConstants.RESOURCE_TYPE)) {
            if (Resource.class.equals(resourceType)) {
                // Only first value is used, which matches behavior of other parameters that are supposed to be specified at most once
                String resTypes = queryParameters.get(SearchConstants.RESOURCE_TYPE).get(0);
                List<String> tmpResourceTypes = Arrays.asList(resTypes.split("\\s*,\\s*"));
                for (String resType : tmpResourceTypes) {
                    if (ModelSupport.isConcreteResourceType(resType)) {
                        resourceTypes.add(resType);
                    } else {
                        manageException("_type search parameter has invalid resource type: " + resType, lenient);
                        continue;
                    }
                }
            }
            else {
                manageException("_type search parameter is only supported with system search", lenient);
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
                } else if (isReverseChainedParameter(name)) {
                    if (isMultiResTypeSearch) {
                        // _has search requires specific resource type modifier in
                        // search parameter, so we don't currently support system search.
                        throw SearchExceptionUtil.buildNewInvalidSearchException("system search not supported with _has.");
                    }
                    for (String reverseChainedParameterValueString : params) {
                        parameters.add(parseReverseChainedParameter(resourceType, name, reverseChainedParameterValueString));
                    }
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
                          throwSearchParameterExceptionIfNull(searchParameter, parameterCode, resType);
                      }
                    } else {
                        // Get the search parameter from our filtered set of applicable SPs for this resource type.
                        searchParameter = getSearchParameter(resourceType.getSimpleName(), parameterCode);
                        throwSearchParameterExceptionIfNull(searchParameter, parameterCode, resourceType.getSimpleName());
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

                    // Build list of processed query parameters
                    List<QueryParameter> curParameterList = new ArrayList<>();
                    for (String paramValueString : params) {
                        QueryParameter parameter = new QueryParameter(type, parameterCode, modifier, modifierResourceTypeName);
                        List<QueryParameterValue> queryParameterValues =
                                processQueryParameterValueString(resourceType, searchParameter, modifier, parameter.getModifierResourceTypeName(), paramValueString);
                        parameter.getValues().addAll(queryParameterValues);
                        curParameterList.add(parameter);
                        parameters.add(parameter);
                    }

                    // Check search restrictions based on the SearchParameter
                    checkSearchParameterRestrictions(parameterCode, searchParameter, curParameterList);

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

        try {
            // Check for valid search parameter combinations
            checkSearchParameterCombinations(resourceType, parameters);

        } catch (FHIRSearchException se) {
            throw se;
        } catch (Exception e) {
            throw SearchExceptionUtil.buildNewParseParametersException(e);
        }

        context.setSearchParameters(parameters);
        return context;
    }

    /**
     * Checks the query parameters (with the same parameter code) against any search restrictions specified
     * in the SearchParameter resource for that parameter code.
     *
     * @param parameterCode
     *     the parameter code
     * @param searchParameter
     *     the SearchParameter resource
     * @param queryParameters
     *     the query parameters to check
     * @throws FHIRSearchException
     *     if a search restriction is found that is not followed
     */
    private static void checkSearchParameterRestrictions(String parameterCode, SearchParameter searchParameter, List<QueryParameter> queryParameters)
        throws FHIRSearchException {

        boolean allowMultipleAnd =
                searchParameter.getMultipleAnd() == null || !searchParameter.getMultipleAnd().hasValue() || searchParameter.getMultipleAnd().getValue();
        boolean allowMultipleOr =
                searchParameter.getMultipleOr() == null || !searchParameter.getMultipleOr().hasValue() || searchParameter.getMultipleOr().getValue();
        List<SearchComparator> comparators = searchParameter.getComparator();
        List<SearchModifierCode> modifiers = searchParameter.getModifier();

        // Check multipleAnd
        if (!allowMultipleAnd && queryParameters.size() > 1) {
            String msg =
                    "Search parameter '" + parameterCode + "' does not allow multiple parameters";
            throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
        }

        for (QueryParameter queryParameter : queryParameters) {

            // Check multipleOr
            if (!allowMultipleOr && queryParameter.getValues().size() > 1) {
                String msg =
                        "Search parameter '" + parameterCode + "' does not allow multiple values";
                throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
            }

            // Check modifier
            Modifier modifier = queryParameter.getModifier();
            if (modifier != null && modifiers != null && !modifiers.isEmpty()) {
                // Special handling of "type" modifier
                if (modifier == Modifier.TYPE) {
                    if (!modifiers.contains(SearchModifierCode.TYPE)) {
                        String msg =
                                "Search parameter '" + parameterCode + "' does not allow modifier '" + queryParameter.getModifierResourceTypeName() + "'";
                        throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                    }
                } else {
                    if (!modifiers.contains(SearchModifierCode.of(modifier.value()))) {
                        String msg =
                                "Search parameter '" + parameterCode + "' does not allow modifier '" + modifier.value() + "'";
                        throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                    }
                }
            }

            // Check comparator
            for (QueryParameterValue queryParameterValue : queryParameter.getValues()) {
                Prefix prefix = queryParameterValue.getPrefix();
                if (prefix != null && comparators != null && !comparators.isEmpty()) {
                    // Check if prefix is found in list of valid comparators as an enum,
                    // since the SearchComparators in the SearchParameter may contain extensions
                    boolean foundMatch = false;
                    SearchComparator prefixAsComparator = SearchComparator.of(prefix.value());
                    for (SearchComparator comparator : comparators) {
                        if (comparator.getValueAsEnumConstant() == prefixAsComparator.getValueAsEnumConstant()) {
                            foundMatch = true;
                            break;
                        }
                    }
                    if (!foundMatch) {
                        String msg =
                                "Search parameter '" + parameterCode + "' does not allow comparator '" + prefix.value() + "'";
                        throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                    }
                }
            }
        }
    }

    /**
     * Checks that the combination of search parameters is valid.
     *
     * @param resourceType
     *            the resource type
     * @param parameters
     *            the query parameters to check
     * @throws Exception
     *             an exception
     */
    private static void checkSearchParameterCombinations(Class<?> resourceType, List<QueryParameter> parameters)
        throws Exception {

        List<Set<String>> validCombinations = getSearchParameterCombinations(resourceType.getSimpleName());
        if (validCombinations != null) {
            Set<String> searchParameterCodes = parameters.stream().map(qp -> qp.getCode()).collect(Collectors.toSet());

            // Check that search parameter codes are a valid combinations
            if (!validCombinations.contains(searchParameterCodes)) {
                String msg;
                if (searchParameterCodes.isEmpty()) {
                    msg = "A valid search parameter combination is required";
                } else {
                    msg = "Search parameter combination is not valid";
                }
                throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
            }
        }
    }

    /**
     * Retrieves the search parameter combinations.
     *
     * @param resourceType
     *     the resource type
     * @return list of allowed search parameter combinations, or null if any search parameter combination is allowed
     * @throws Exception
     */
    private static List<Set<String>> getSearchParameterCombinations(String resourceType) throws Exception {

        List<Set<String>> spCombinations = null;

        // Retrieve the "resources" config property group.
        PropertyGroup rsrcsGroup = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_RESOURCES);
        if (rsrcsGroup != null) {
            List<PropertyEntry> rsrcsEntries = rsrcsGroup.getProperties();
            if (rsrcsEntries != null && !rsrcsEntries.isEmpty()) {
                List<String> combinations = null;

                // Try to find search parameter combinations property for matching resource type
                for (PropertyEntry rsrcsEntry : rsrcsEntries) {
                    if (resourceType.equals(rsrcsEntry.getName())) {
                        PropertyGroup resourceTypeGroup = (PropertyGroup) rsrcsEntry.getValue();
                        if (resourceTypeGroup != null) {
                            combinations = resourceTypeGroup.getStringListProperty(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_SEARCH_PARAMETER_COMBINATIONS);
                            break;
                        }
                    }
                }

                // Otherwise, try to find search parameter combinations property for "Resource" resource type
                if (combinations == null) {
                    for (PropertyEntry rsrcsEntry : rsrcsEntries) {

                        // Check if matching resource type
                        if (SearchConstants.RESOURCE_RESOURCE.equals(rsrcsEntry.getName())) {
                            PropertyGroup resourceTypeGroup = (PropertyGroup) rsrcsEntry.getValue();
                            if (resourceTypeGroup != null) {
                                combinations =
                                        resourceTypeGroup.getStringListProperty(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_SEARCH_PARAMETER_COMBINATIONS);
                                break;
                            }
                        }
                    }
                }

                // Convert the delimited combinations to a list of sets
                if (combinations != null) {
                    spCombinations = new ArrayList<>();
                    for (String combination : combinations) {
                        combination = combination.trim();
                        Set<String> combinationSet = new HashSet<>();
                        if (!combination.isEmpty()) {
                            // If any search parameter combination is allowed, return null
                            if (SEARCH_PARAM_COMBINATION_ANY.equals(combination)) {
                                return null;
                            }
                            for (String spString : combination.split(SEARCH_PARAM_COMBINATION_DELIMITER)) {
                                spString = spString.trim();
                                if (spString.isEmpty()) {
                                    throw SearchExceptionUtil.buildNewIllegalStateException();
                                }
                                combinationSet.add(spString);
                            }
                        }
                        spCombinations.add(combinationSet);
                    }
                }
            }
        }

        return spCombinations;
    }

    /**
     * Common logic from handling a single queryParameterValueString based on its type
     */
    private static List<QueryParameterValue> processQueryParameterValueString(Class<?> resourceType, SearchParameter searchParameter, Modifier modifier,
        String modifierResourceTypeName, String queryParameterValueString) throws FHIRSearchException, Exception {
        String parameterCode = searchParameter.getCode().getValue();
        Type type = Type.fromValue(searchParameter.getType().getValue());
        List<QueryParameterValue> queryParameterValues;
        if (Type.COMPOSITE == type) {
            List<Component> components = searchParameter.getComponent();

            // Generate parallel lists of type and code (parameter name) representing each
            // component parameter of the composite
            List<Type> compTypes = new ArrayList<>(components.size());
            List<String> compCodes = new ArrayList<>(components.size());
            for (Component component : components) {
                if (component.getDefinition() == null || !component.getDefinition().hasValue()) {
                    throw new IllegalStateException(String.format("Composite search parameter '%s' is "
                            + "missing one or more component definition", searchParameter.getName()));
                }
                SearchParameter referencedParam = getSearchParameter(resourceType, component.getDefinition());
                compTypes.add(Type.fromValue(referencedParam.getType().getValue()));
                compCodes.add(referencedParam.getCode().getValue());
            }

            if (Modifier.MISSING.equals(modifier)) {
                queryParameterValues = parseQueryParameterValuesString(searchParameter, Type.TOKEN, modifier, modifierResourceTypeName, queryParameterValueString);
                // Still need to populate the components for the query builder to properly build the SQL
                for (QueryParameterValue queryParameterValue : queryParameterValues) {
                    for (int i=0; i<compTypes.size(); i++) {
                        Type componentType = compTypes.get(i);
                        final String compositeSubParamCode = compCodes.get(i);
                        final String compositeParamCode = SearchUtil.makeCompositeSubCode(parameterCode, compositeSubParamCode);
                        queryParameterValue.addComponent(new QueryParameter(componentType, compositeParamCode, null, null));
                    }
                }
            } else {
                queryParameterValues = parseCompositeQueryParameterValuesString(searchParameter, parameterCode, compTypes, compCodes, queryParameterValueString);
            }
        } else if (Modifier.MISSING.equals(modifier)) {
            // FHIR search considers booleans a special case of token for some reason...
            queryParameterValues = parseQueryParameterValuesString(searchParameter, Type.TOKEN, modifier, modifierResourceTypeName, queryParameterValueString);
        } else {
            queryParameterValues = parseQueryParameterValuesString(searchParameter, type, modifier, modifierResourceTypeName, queryParameterValueString);
        }
        return queryParameterValues;
    }

    private static List<QueryParameterValue> parseCompositeQueryParameterValuesString(SearchParameter searchParameter, final String compositeParamCode,
            List<Type> compTypes, List<String> compCodes, String queryParameterValuesString) throws FHIRSearchException {
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
                List<QueryParameterValue> values = parseQueryParameterValuesString(searchParameter, compTypes.get(i), null, null, componentValueStrings[i]);
                if (values.isEmpty()) {
                    throw new FHIRSearchException("Component values cannot be empty");
                } else if (values.size() > 1) {
                    throw new IllegalStateException("A single component can only have a single value");
                } else {
                    // exactly one. Override the parameter code (parameter_name) so that it uniquely
                    // referenced the correct sub-parameter for this composite
                    final String compositeSubParamCode = compCodes.get(i);
                    final String compositeParamName = SearchUtil.makeCompositeSubCode(compositeParamCode, compositeSubParamCode);
                    QueryParameter parameter = new QueryParameter(compTypes.get(i), compositeParamName, null, null, values);
                    parameterValue.addComponent(parameter);
                }
            }

            parameterValues.add(parameterValue);
        }
        return parameterValues;
    }

    private static List<QueryParameterValue> parseQueryParameterValuesString(SearchParameter searchParameter, Type type,
        Modifier modifier, String modifierResourceTypeName, String queryParameterValuesString) throws FHIRSearchException {
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
                // [parameter]=[literal] - literal reference
                // [parameter]=[type]/[id] - relative local reference
                // [parameter]=[base]/[type]/[id] - absolute local reference
                // [parameter]=[id] - relativel local reference
                // [parameter]=[literal|version#fragment] - canonical url - currently not supported
                String valueString = unescapeSearchParm(v);
                valueString = extractReferenceValue(valueString);
                parameterValue.setValueString(valueString);
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
                    // Optimization for search parameters that always reference the same system, added under #1929
                    if (!Modifier.MISSING.equals(modifier)) {
                        try {
                            String implicitSystem = searchParameter.getExtension().stream()
                                    .filter(e -> SearchConstants.IMPLICIT_SYSTEM_EXT_URL.equals(e.getUrl()) && e.getValue() != null)
                                    .findFirst()
                                    .map(e -> e.getValue().as(Uri.class).getValue())
                                    .orElse(null);
                            if (implicitSystem != null) {
                                parameterValue.setValueSystem(implicitSystem);
                            }
                        } catch (ClassCastException e) {
                            log.log(Level.INFO, "Found " + SearchConstants.IMPLICIT_SYSTEM_EXT_URL + " extension with unexpected value type", e);
                        }
                    }
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
     * Convert the string to a reference value useable by the persistence
     * layer. This simply involves removing the URL prefix if it matches
     * the originalUri in the request context
     * @param valueString
     * @return
     */
    public static String extractReferenceValue(String valueString) throws FHIRSearchException {
        // Search values formed as "system|code" like  "https://example.com/codesystem|foo" are
        // code searches not references, so no extra processing required
        if (valueString == null || valueString.contains("|")) {
            return valueString;
        }

        // Remove the baseUrl if it prefixes the value
        final String baseUrl = ReferenceUtil.getBaseUrl(null);

        if (valueString.startsWith(baseUrl)) {
            valueString = valueString.substring(baseUrl.length());
        }
        return valueString;
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

    /**
     * Parse query parameters for read and vread.
     * @param resourceType the resource type
     * @param queryParameters the query parameters
     * @param interaction read or vread
     * @param lenient true if lenient, false if strict
     * @return the FHIR search context
     * @throws Exception an exception
     */
    public static FHIRSearchContext parseReadQueryParameters(Class<?> resourceType,
        Map<String, List<String>> queryParameters, String interaction, boolean lenient) throws Exception {
        String resourceTypeName = resourceType.getSimpleName();

        // Read and vRead only allow general search parameters
        List<String> nonGeneralParams = queryParameters.keySet().stream().filter(k -> !FHIRConstants.GENERAL_PARAMETER_NAMES.contains(k)).collect(Collectors.toList());
        for (String nonGeneralParam : nonGeneralParams) {
            FHIRSearchException se = SearchExceptionUtil.buildNewInvalidSearchException("Search parameter '" + nonGeneralParam
                + "' is not supported by " + interaction + ".");
            if (!lenient) {
                throw se;
            }
            log.log(Level.FINE, "Error while parsing search parameter '" + nonGeneralParam + "' for resource type " + resourceTypeName, se);
        }

        return parseQueryParameters(null, null, resourceType, queryParameters, lenient);
    }


    public static FHIRSearchContext parseQueryParameters(String compartmentName, String compartmentLogicalId,
            Class<?> resourceType,
            Map<String, List<String>> queryParameters, String queryString) throws Exception {
        return parseQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParameters, true);
    }

    /**
     * Check the configuration to see if the flag enabling the compartment search
     * optimization. Defaults to false so the behavior won't change unless it
     * is explicitly enabled in fhir-server-config. This is important, because
     * existing data must be reindexed (see $reindex custom operation) to
     * generate values for the ibm-internal compartment relationship params.
     * @return
     */
    public static boolean useStoredCompartmentParam() {
        return FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_USE_STORED_COMPARTMENT_PARAM, false);
    }

    /**
     * @param lenient
     *                Whether to ignore unknown or unsupported parameter
     * @return
     * @throws Exception
     */
    public static FHIRSearchContext parseQueryParameters(String compartmentName, String compartmentLogicalId,
            Class<?> resourceType, Map<String, List<String>> queryParameters, boolean lenient) throws Exception {

        QueryParameter rootParameter = null;

        if (compartmentName != null && compartmentLogicalId != null) {
            // The inclusion criteria are represented as a chain of parameters, each with a value of the
            // compartmentLogicalId.
            // The query parsers will OR these parameters to achieve the compartment search.
            List<String> inclusionCriteria;

            if (useStoredCompartmentParam()) {
                // issue #1708. When enabled, use the ibm-internal-... compartment parameter. This
                // results in faster queries because only a single parameter is used to represent the
                // compartment membership.
                inclusionCriteria = Collections.singletonList(CompartmentUtil.makeCompartmentParamName(compartmentName));
            } else {
                // pre #1708 behavior, which is the default
                inclusionCriteria =
                        CompartmentUtil.getCompartmentResourceTypeInclusionCriteria(compartmentName,
                                resourceType.getSimpleName());
            }

            for (String criteria : inclusionCriteria) {
                QueryParameter parameter  = new QueryParameter(Type.REFERENCE, criteria, null, null, true);
                QueryParameterValue value = new QueryParameterValue();
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
        }

        FHIRSearchContext context = parseQueryParameters(resourceType, queryParameters, lenient);

        // Add the inclusion criteria search parameters to the front of the search parameter list
        if (rootParameter != null) {
            context.getSearchParameters().add(0, rootParameter);
        }

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

    public static boolean isSearchSingletonParameter(String name) {
        return SearchConstants.SEARCH_SINGLETON_PARAMETER_NAMES.contains(name);
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
            } else if (SearchConstants.SORT.equals(name) && first != null) {
                // in R4, we only look for _sort
                // Only first value is used, which matches behavior of other parameters that are supposed to be specified at most once
                sort.parseSortParameter(resourceTypeName, context, first, lenient);
            } else if (name.startsWith(SearchConstants.INCLUDE) || name.startsWith(SearchConstants.REVINCLUDE)) {
                parseInclusionParameter(resourceType, context, name, values, lenient);
            } else if (SearchConstants.ELEMENTS.equals(name) && first != null) {
                // Only first value is used, which matches behavior of other parameters that are supposed to be specified at most once
                parseElementsParameter(resourceType, context, first, lenient);
            } else if (SearchConstants.SUMMARY.equals(name) && first != null) {
                context.setSummaryParameter(SummaryValueSet.from(first));
            } else if (SearchConstants.TOTAL.equals(name) && first != null) {
                context.setTotalParameter(TotalValueSet.from(first));
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

    public static boolean isReverseChainedParameter(String code) {
        return code.startsWith(SearchConstants.HAS);
    }

    private static QueryParameter parseChainedParameter(HashSet<String> resourceTypes, String name, String valuesString)
            throws Exception {
        QueryParameter rootParameter = null;
        Class<?> resourceType = null;

        // declared here so we can remember the values from the last component in the chain after looping
        SearchParameter searchParameter = null;
        Modifier modifier = null;
        boolean checkForLogicalId = false;
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

                Set<String> modifierResourceTypeNameForResourceTypes = new HashSet<>();
                for (String resTypeName: resourceTypes) {
                    searchParameter = getSearchParameter(ModelSupport.getResourceType(resTypeName), parameterName);
                    throwSearchParameterExceptionIfNull(searchParameter, parameterName, resTypeName);

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

                    if (modifierResourceTypeName == null && targets.size() > 1) {
                        if (currentIndex < lastIndex) {
                            throw SearchExceptionUtil.buildNewInvalidSearchException(
                                String.format(SEARCH_PARAMETER_MODIFIER_NAME, parameterName));
                        } else if (Type.REFERENCE.equals(type)) {
                            checkForLogicalId = true;
                        }
                    }

                    if (modifierResourceTypeName == null && currentIndex < lastIndex) {
                        modifier                 = Modifier.TYPE;
                        modifierResourceTypeNameForResourceTypes.add(targets.get(0).getValue());
                    }
                }

                if (modifierResourceTypeNameForResourceTypes.size() > 1) {
                    String.format(DIFFERENT_MODIFIYERRESOURCETYPES_FOUND_FOR_RESOURCETYPES, parameterName);
                } else if (modifierResourceTypeNameForResourceTypes.size() == 1) {
                    modifierResourceTypeName = modifierResourceTypeNameForResourceTypes.iterator().next();
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
                    resourceTypes.clear();
                    resourceTypes.add(modifierResourceTypeName);
                }

                currentIndex++;
            } // end for loop

            List<QueryParameterValue> valueList =
                    processQueryParameterValueString(resourceType, searchParameter, modifier, rootParameter.getModifierResourceTypeName(), valuesString);

            if (checkForLogicalId) {
                // For last search parameter, if type REFERENCE and not scoped to single target resource type, check if
                // value is logical ID only. If so, throw an exception.
                checkQueryParameterValuesForLogicalIdOnly(rootParameter.getChain().getLast().getCode(), valueList);
            }

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
            boolean checkForLogicalId = false;
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
                                String.format(MODIFIER_NOT_ALLOWED_WITH_CHAINED_EXCEPTION, modifier.value()));
                    }
                    parameterName = parameterName.substring(0, parameterName.indexOf(":"));
                } else {
                    modifier = null;
                }

                searchParameter = getSearchParameter(resourceType, parameterName);
                throwSearchParameterExceptionIfNull(searchParameter, parameterName, resourceType.getSimpleName());

                type = Type.fromValue(searchParameter.getType().getValue());
                if (!Type.REFERENCE.equals(type) && currentIndex < lastIndex) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException(
                            String.format(TYPE_NOT_ALLOWED_WITH_CHAINED_PARAMETER_EXCEPTION, type.value()));
                }

                List<ResourceType> targets = searchParameter.getTarget();
                // Check if the modifier resource type is invalid.
                if (modifierResourceTypeName != null && !targets.contains(ResourceType.of(modifierResourceTypeName))) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException(
                            String.format(MODIFIYERRESOURCETYPE_NOT_ALLOWED_FOR_RESOURCETYPE, modifierResourceTypeName,
                                    parameterName, resourceType.getSimpleName()));
                }

                if (modifierResourceTypeName == null && targets.size() > 1) {
                    if (currentIndex < lastIndex) {
                        throw SearchExceptionUtil.buildNewInvalidSearchException(
                            String.format(SEARCH_PARAMETER_MODIFIER_NAME, parameterName));
                    } else if (Type.REFERENCE.equals(type)) {
                        checkForLogicalId = true;
                    }
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

            List<QueryParameterValue> valueList =
                    processQueryParameterValueString(resourceType, searchParameter, modifier, rootParameter.getModifierResourceTypeName(), valuesString);

            if (checkForLogicalId) {
                // For last search parameter, if type REFERENCE and not scoped to single target resource type, check if
                // value is logical ID only. If so, throw an exception.
                checkQueryParameterValuesForLogicalIdOnly(rootParameter.getChain().getLast().getCode(), valueList);
            }

            rootParameter.getChain().getLast().getValues().addAll(valueList);
        } catch (FHIRSearchException e) {
            throw e;
        } catch (Exception e) {
            throw SearchExceptionUtil.buildNewChainedParameterException(name, e);
        }

        return rootParameter;
    }

    /**
     * Transforms the passed string representing reverse chain search criteria, into
     * an actual chain of QueryParameter objects. This method consumes strings of this form:
     * @formatter:off
     * <pre>
     *      +-------------------------------------------------------------------+
     *      |                                                                   |
     *      V                                                                   |
     * >>---+--- "_has:{referenced-by-resource-type}:{reference-parameter}:" ---+--- "{search-parameter}" ---><
     * </pre>
     * @formatter:on
     * See the FHIR specification for details:
     * <a href="https://www.hl7.org/fhir/search.html#has</a>
     *
     * @param resourceType
     *     Search type.
     * @param reverseChainParameterString
     *     Reverse chain search parameter string.
     * @param valuesString
     *     String containing the final search value.
     * @return QueryParameter
     *     The root of a parameter chain for the reverse chain criteria.
     */
    private static QueryParameter parseReverseChainedParameter(Class<?> resourceType, String reverseChainParameterString, String valuesString) throws Exception {

        QueryParameter rootParameter = null;

        try {
            // Strip leading '_has:' and then split by ':_has:'
            List<String> components = Arrays.asList(reverseChainParameterString.replaceFirst(HAS_DELIMITER.substring(1), "").split(HAS_DELIMITER));

            if (components.size() == 0) {
                throw SearchExceptionUtil.buildNewInvalidSearchException(INCORRECT_NUMBER_OF_COMPONENTS_FOR_REVERSE_CHAIN_SEARCH);
            }

            int currentIndex = 0;
            int lastIndex = components.size() - 1;

            for (String component : components) {
                // Split into subcomponents by colon delimiter
                List<String> subcomponents = Arrays.asList(component.split(SearchConstants.COLON_DELIMITER_STR, 3));

                // Validate correct number of subcomponents
                if ((currentIndex < lastIndex && subcomponents.size() != 2) ||
                        (currentIndex == lastIndex && subcomponents.size() != 3)) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException(INCORRECT_NUMBER_OF_COMPONENTS_FOR_REVERSE_CHAIN_SEARCH);
                }

                // Validate referenced-by resource type
                String referencedByResourceTypeName = subcomponents.get(0);
                Class<? extends Resource> referencedByResourceType = ModelSupport.getResourceType(referencedByResourceTypeName);
                if (referencedByResourceType == null) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException(
                        String.format(INVALID_RESOURCE_TYPE_FOR_REVERSE_CHAIN_SEARCH, referencedByResourceTypeName));
                }

                // Validate reference search parameter
                String referenceSearchParameterName = subcomponents.get(1);
                SearchParameter referenceSearchParameter = getSearchParameter(referencedByResourceType, referenceSearchParameterName);
                throwSearchParameterExceptionIfNull(referenceSearchParameter, referenceSearchParameterName, referencedByResourceTypeName);
                if (!Type.REFERENCE.equals(Type.fromValue(referenceSearchParameter.getType().getValue()))) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException(
                        String.format(PARAMETER_TYPE_NOT_REFERENCE_FOR_REVERSE_CHAIN_SEARCH, referenceSearchParameterName));
                }

                // Validate resource type is one of the reference search parameter target resource types
                if (!referenceSearchParameter.getTarget().contains(ResourceType.of(resourceType.getSimpleName()))) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException(
                        String.format(TARGET_TYPE_OF_REFERENCE_PARAMETER_NOT_VALID_FOR_REVERSE_CHAIN_SEARCH,
                            referenceSearchParameterName, resourceType.getSimpleName()));
                }

                // Create new QueryParameter
                QueryParameter parameter = new QueryParameter(Type.REFERENCE, referenceSearchParameterName, Modifier.TYPE, referencedByResourceTypeName, false, true);
                if (rootParameter == null) {
                    rootParameter = parameter;
                } else if (rootParameter.getChain().isEmpty()) {
                    rootParameter.setNextParameter(parameter);
                } else {
                    rootParameter.getChain().getLast().setNextParameter(parameter);
                }

                if (currentIndex == lastIndex) {
                    // Add last search parameter
                    String parameterName = subcomponents.get(2);
                    if (isChainedParameter(parameterName)) {
                        QueryParameter lastParameter = parseChainedParameter(referencedByResourceType, parameterName, valuesString);
                        if (rootParameter.getChain().isEmpty()) {
                            rootParameter.setNextParameter(lastParameter);
                        } else {
                            rootParameter.getChain().getLast().setNextParameter(lastParameter);
                        }
                    } else {
                        String modifierName = null;
                        Modifier modifier = null;
                        String modifierResourceTypeName = null;

                        // Check if modifier is specified
                        int index = parameterName.indexOf(":");
                        if (index != -1) {
                            modifierName = parameterName.substring(index + 1);
                            parameterName = parameterName.substring(0, index);
                        }

                        SearchParameter searchParameter = getSearchParameter(referencedByResourceType, parameterName);
                        throwSearchParameterExceptionIfNull(searchParameter, parameterName, referencedByResourceTypeName);
                        Type type = Type.fromValue(searchParameter.getType().getValue());

                        if (modifierName != null) {
                            if (ModelSupport.isResourceType(modifierName)) {
                                modifier = Modifier.TYPE;
                                modifierResourceTypeName = modifierName;
                            } else {
                                try {
                                    modifier = Modifier.fromValue(modifierName);
                                } catch (IllegalArgumentException e) {
                                    String msg = "Undefined Modifier: '" + URLEncoder.encode(modifierName, "UTF-8") + "'";
                                    throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                                }
                            }
                            if (!isAllowed(type, modifier)) {
                                String msg = "Unsupported type/modifier combination: '" + type.value() + "'/'" + modifier.value() + "'";
                                throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                            }
                        }

                        // Process value string
                        List<QueryParameterValue> valueList = processQueryParameterValueString(referencedByResourceType, searchParameter,
                            modifier, modifierResourceTypeName, valuesString);

                        if (Type.REFERENCE == type && searchParameter.getTarget().size() > 1 && modifierResourceTypeName == null) {
                            // For last search parameter, if type REFERENCE and not scoped to single target resource type, check if
                            // value is logical ID only. If so, throw an exception.
                            checkQueryParameterValuesForLogicalIdOnly(parameterName, valueList);
                        }

                        QueryParameter lastParameter = new QueryParameter(type, parameterName, modifier, modifierResourceTypeName, valueList);
                        if (rootParameter.getChain().isEmpty()) {
                            rootParameter.setNextParameter(lastParameter);
                        } else {
                            rootParameter.getChain().getLast().setNextParameter(lastParameter);
                        }

                        // Check search restrictions based on the SearchParameter
                        checkSearchParameterRestrictions(parameterName, searchParameter, Collections.singletonList(lastParameter));
                    }
                } else {
                    // Get ready for next parameter in chain
                    resourceType = referencedByResourceType;
                    currentIndex++;
                }
            }
        } catch (FHIRSearchException e) {
            throw e;
        } catch (Exception e) {
            throw SearchExceptionUtil.buildNewReverseChainedParameterException(SearchConstants.HAS, e);
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
                        new QueryParameter(Type.REFERENCE, qualifiedInclusionCriteria[0], null,
                                resourceType, inclusionCriteriaParm.getValues());
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
        String resourceTypeAndParameterName;
        String searchParameterTargetType;

        SearchParameter searchParm;
        InclusionParameter newInclusionParm;
        List<InclusionParameter> newInclusionParms = null;

        List<String> allowedIncludes = getSearchPropertyRestrictions(resourceType.getSimpleName(), SEARCH_PROPERTY_TYPE_INCLUDE);
        List<String> allowedRevIncludes = getSearchPropertyRestrictions(resourceType.getSimpleName(), SEARCH_PROPERTY_TYPE_REVINCLUDE);

        for (String inclusionValue : inclusionValues) {

            // Parse value into 3 parts: joinResourceType, searchParameterName, searchParameterTargetType
            inclusionValueParts = inclusionValue.split(":");
            if (inclusionValueParts.length < 2) {
                manageException("A value for _include or _revinclude must have at least 2 parts separated by a colon.", lenient);
                continue;
            }
            joinResourceType = inclusionValueParts[0];
            searchParameterName = inclusionValueParts[1];
            resourceTypeAndParameterName = joinResourceType + ":" + searchParameterName;
            searchParameterTargetType = inclusionValueParts.length == 3 ? inclusionValueParts[2] : null;

            if (SearchConstants.INCLUDE.equals(inclusionKeyword)) {

                // For _include parameter, join resource type must match resource type being searched
                if (!joinResourceType.equals(resourceType.getSimpleName())) {
                    manageException("The join resource type must match the resource type being searched.", lenient);
                    continue;
                }

                // Check allowed _include values
                if (allowedIncludes != null && !allowedIncludes.contains(inclusionValue) && !allowedIncludes.contains(resourceTypeAndParameterName)) {
                    manageException("'" + inclusionValue + "' is not a valid _include parameter value for resource type '"
                            + resourceType.getSimpleName() + "'", lenient);
                    continue;
                }
            }

            if (SearchConstants.REVINCLUDE.equals(inclusionKeyword)) {

                // For _revinclude parameter, join resource type must be valid resource type
                if (!ModelSupport.isResourceType(joinResourceType)) {
                    manageException("'" + joinResourceType + "' is not a valid resource type.", lenient);
                    continue;
                }

                // For _revinclude parameter, target resource type, if specified, must match resource type being searched
                if (searchParameterTargetType != null && !searchParameterTargetType.equals(resourceType.getSimpleName())) {
                    manageException("The search parameter target type must match the resource type being searched.", lenient);
                    continue;
                }

                // Check allowed _revinclude values
                if (allowedRevIncludes != null && !allowedRevIncludes.contains(inclusionValue) && !allowedRevIncludes.contains(resourceTypeAndParameterName)) {
                    manageException("'" + inclusionValue + "' is not a valid _revinclude parameter value for resource type '"
                            + resourceType.getSimpleName() + "'", lenient);
                    continue;
                }
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
                    manageException("Undefined Inclusion Parameter: " + inclusionValue, lenient);
                    continue;
                }
                if (!SearchParamType.REFERENCE.equals(searchParm.getType())) {
                    manageException("Inclusion Parameter must be of type 'reference'. The passed Inclusion Parameter is of type '"
                            + searchParm.getType().getValue() + "': " + inclusionValue, lenient);
                    continue;
                }
                searchParametersMap = Collections.singletonMap(searchParameterName, searchParm);
            }
            try {
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
            } catch (FHIRSearchException e) {
                if (lenient) {
                    log.fine(e.getMessage());
                    continue;
                } else {
                    throw e;
                }
            }
        }
    }

    /**
     * Retrieves the search property restrictions.
     *
     * @param resourceType the resource type
     * @param propertyType the property type
     * @return list of allowed values for the search property, or null if no restrictions
     * @throws Exception
     *             an exception
     */
    private static List<String> getSearchPropertyRestrictions(String resourceType, String propertyType) throws Exception {
        String propertyField = null;
        if (SEARCH_PROPERTY_TYPE_INCLUDE.equals(propertyType)) {
            propertyField = FHIRConfiguration.PROPERTY_FIELD_RESOURCES_SEARCH_INCLUDES;
        }
        else if (SEARCH_PROPERTY_TYPE_REVINCLUDE.equals(propertyType)) {
            propertyField = FHIRConfiguration.PROPERTY_FIELD_RESOURCES_SEARCH_REV_INCLUDES;
        }

        // Retrieve the "resources" config property group.
        if (propertyField != null) {
            PropertyGroup rsrcsGroup = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_RESOURCES);
            if (rsrcsGroup != null) {
                List<PropertyEntry> rsrcsEntries = rsrcsGroup.getProperties();
                if (rsrcsEntries != null && !rsrcsEntries.isEmpty()) {

                    // Try to find search property for matching resource type
                    for (PropertyEntry rsrcsEntry : rsrcsEntries) {
                        if (resourceType.equals(rsrcsEntry.getName())) {
                            PropertyGroup resourceTypeGroup = (PropertyGroup) rsrcsEntry.getValue();
                            if (resourceTypeGroup != null) {
                                return resourceTypeGroup.getStringListProperty(propertyField);
                            }
                        }
                    }

                    // Otherwise, try to find search property for "Resource" resource type
                    for (PropertyEntry rsrcsEntry : rsrcsEntries) {

                        // Check if matching resource type
                        if (SearchConstants.RESOURCE_RESOURCE.equals(rsrcsEntry.getName())) {
                            PropertyGroup resourceTypeGroup = (PropertyGroup) rsrcsEntry.getValue();
                            if (resourceTypeGroup != null) {
                                return resourceTypeGroup.getStringListProperty(propertyField);
                            }
                        }
                    }
                }
            }
        }

        return null;
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
            String elements, boolean lenient) throws Exception {

        Set<String> resourceFieldNames = JsonSupport.getElementNames(resourceType);

        // For other parameters, we pass the comma-separated list of values to the PL
        // but for elements, we need to process that here
        for (String elementName : elements.split(SearchConstants.BACKSLASH_NEGATIVE_LOOKBEHIND + ",")) {
            if (elementName.startsWith("_")) {
                throw SearchExceptionUtil.buildNewInvalidSearchException("Invalid element name: " + elementName);
            }
            if (!resourceFieldNames.contains(elementName)) {
                manageException("Unknown element name: " + elementName, lenient);
                continue;
            }
            context.addElementsParameter(elementName);
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

    /**
     * Either throw a FHIRSearchException or log the error message, depending on if
     * we are in strict or lenient mode.
     *
     * @param message
     *     The error message.
     * @param lenient
     *     A flag indicating lenient or strict mode.
     * @throws FHIRSearchException
     */
    private static void manageException(String message, boolean lenient) throws FHIRSearchException {
        if (lenient) {
            log.fine(message);
        } else {
            throw SearchExceptionUtil.buildNewInvalidSearchException(message);
        }
    }

    /**
     * Extracts the parameter values defining compartment membership.
     * @param fhirResource
     * @param compartmentRefParams a map of parameter names to a set of compartment names (resource types)
     * @return a map of compartment name to a set of unique compartment reference values
     */
    public static Map<String, Set<CompartmentReference>> extractCompartmentParameterValues(Resource fhirResource,
            Map<String, Set<java.lang.String>> compartmentRefParams) throws FHIRSearchException {
        final Map<String, Set<CompartmentReference>> result = new HashMap<>();
        final String resourceType = fhirResource.getClass().getSimpleName();

        // TODO, probably should use a Bundle.Entry value here if we are processing a bundle
        final String baseUrl = ReferenceUtil.getBaseUrl(null);

        try {
            EvaluationContext resourceContext = new FHIRPathEvaluator.EvaluationContext(fhirResource);

            // Extract any references we find matching parameters representing compartment membership.
            // For example CareTeam.participant can be used to refer to a Patient or RelatedPerson resource:
            // "participant": { "reference": "Patient/abc123" }
            // "participant": { "reference": "RelatedPerson/abc456" }
            for (Map.Entry<String, Set<String>> paramEntry : compartmentRefParams.entrySet()) {
                final String searchParm = paramEntry.getKey();

                // Ignore {def} which is used in the compartment definition where
                // no other search parm is given (e.g. Encounter->Encounter).
                if (!COMPARTMENT_PARM_DEF.equals(searchParm)) {
                    SearchParameter sp = SearchUtil.getSearchParameter(resourceType, searchParm);
                    if (sp != null && sp.getExpression() != null) {
                        String expression = sp.getExpression().getValue();

                        if (log.isLoggable(Level.FINE)) {
                            log.fine("searchParam = [" + resourceType + "] '" + searchParm + "'; expression = '" + expression + "'");
                        }
                        Collection<FHIRPathNode> nodes = FHIRPathEvaluator.evaluator().evaluate(resourceContext, expression);

                        if (log.isLoggable(Level.FINEST)) {
                            log.finest("Expression [" + expression + "], parameter-code ["
                                    + searchParm + "], size [" + nodes.size() + "]");
                        }

                        for (FHIRPathNode node : nodes) {
                            Reference reference = node.asElementNode().element().as(Reference.class);
                            ReferenceValue rv = ReferenceUtil.createReferenceValueFrom(reference, baseUrl);
                            if (rv.getType() != ReferenceType.DISPLAY_ONLY && rv.getType() != ReferenceType.INVALID) {

                                // Check that the target resource type of the reference matches one of the
                                // target resource types in the compartment definition.
                                final String compartmentName = rv.getTargetResourceType();
                                if (paramEntry.getValue().contains(compartmentName)) {
                                    // Add this reference to the set of references we're collecting for each compartment
                                    CompartmentReference cref = new CompartmentReference(searchParm, compartmentName, rv.getValue());
                                    Set<CompartmentReference> references = result.computeIfAbsent(compartmentName, k -> new HashSet<>());
                                    references.add(cref);
                                }
                            }
                        }
                    } else if (!useStoredCompartmentParam()) {
                       log.warning("Compartment parameter not found: [" + resourceType + "] '" + searchParm + "'. This will stop compartment searches from working correctly.");
                    }
                }
            }
        } catch (Exception e) {
            final String msg = "Unexpected exception extracting compartment references "
                    + " for resource type '" + resourceType + "'";
            log.log(Level.WARNING, msg, e);
            throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
        }
        return result;
    }

    /**
     * Throw an exception if the specified search parameter is null.
     *
     * @param searchParameter the search parameter to check
     * @param parameterCode   the search parameter code
     * @param resourceType    the resource type the search parameter was specified for
     * @throws FHIRSearchException
     */
    private static void throwSearchParameterExceptionIfNull(SearchParameter searchParameter, String parameterCode, String resourceType) throws FHIRSearchException {
        if (searchParameter == null) {
            throw SearchExceptionUtil.buildNewInvalidSearchException(String.format(SEARCH_PARAMETER_NOT_FOUND, parameterCode, resourceType));
        }
    }

    /**
     * Throw an exception if a logical ID-only value is found in the list of values.
     *
     * @param parameterCode the search parameter code
     * @param values        the list of parameter values to check
     * @throws FHIRSearchException
     */
    private static void checkQueryParameterValuesForLogicalIdOnly(String parameterCode, List<QueryParameterValue> values) throws FHIRSearchException {
        for (QueryParameterValue value : values) {
            ReferenceValue refVal = ReferenceUtil.createReferenceValueFrom(value.getValueString(), null, ReferenceUtil.getBaseUrl(null));
            if (refVal.getType() == ReferenceType.LITERAL_RELATIVE && refVal.getTargetResourceType() == null) {
                throw SearchExceptionUtil.buildNewInvalidSearchException(
                    String.format(LOGICAL_ID_VALUE_NOT_ALLOWED_FOR_REFERENCE_SEARCH, parameterCode, value.getValueString()));
            }
        }
    }

    /**
     * Build a parameter name (code) which can be used to uniquely represent the stored
     * composite sub-parameter (the values get added to the parameter_names table). We use
     * a prefix just to avoid any (albeit already remote) possibility of collision. Also
     * makes these more visible as something added by the implementation code, not from
     * a configuration file.
     * @param compositeCode
     * @param subParameterCode
     * @return
     */
    public static String makeCompositeSubCode(String compositeCode, String subParameterCode) {
        final StringBuilder result = new StringBuilder();
        result.append(IBM_COMPOSITE_PREFIX);
        result.append(compositeCode);
        result.append("_");
        result.append(subParameterCode);
        return result.toString();
    }
}

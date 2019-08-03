/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.util;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.config.PropertyGroup;
import com.ibm.watsonhealth.fhir.config.PropertyGroup.PropertyEntry;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTree;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watsonhealth.fhir.model.path.exception.FHIRPathException;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.resource.SearchParameter;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.ResourceType;
import com.ibm.watsonhealth.fhir.model.type.SearchParamType;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.JsonSupport;
import com.ibm.watsonhealth.fhir.search.SearchConstants;
import com.ibm.watsonhealth.fhir.search.compartment.CompartmentUtil;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContextFactory;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.exception.SearchExceptionUtil;
import com.ibm.watsonhealth.fhir.search.parameters.InclusionParameter;
import com.ibm.watsonhealth.fhir.search.parameters.Parameter;
import com.ibm.watsonhealth.fhir.search.parameters.ParameterValue;
import com.ibm.watsonhealth.fhir.search.parameters.ParametersUtil;
import com.ibm.watsonhealth.fhir.search.parameters.SortParameter;
import com.ibm.watsonhealth.fhir.search.parameters.cache.TenantSpecificSearchParameterCache;
import com.ibm.watsonhealth.fhir.search.uri.UriBuilder;
import com.ibm.watsonhealth.fhir.search.valuetypes.ValueTypesFactory;

/**
 * Search Utility<br/>
 * 
 * This class uses FHIRPath Expressions (and currently does not support XPath) and uses init to activate the
 * Parameters/Compartments/ValueTypes components.
 * 
 * @author pbastide@us.ibm.com
 *
 */
public class SearchUtil {

    private static final String CLASSNAME = SearchUtil.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    // Logging Strings
    private static final String EXTRACT_PARAMETERS_LOGGING = "extractParameterValues: [%s] [%s]";
    private static final String NO_TENANT_SP_MAP_LOGGING = "No tenant-specific search parameters found for tenant '%s'; trying %s ";
    private static final String UNSUPPORTED_EXCEPTION = "Search Parameter includes an unsupported operation or bad expression : %s %s %s";

    // Exception Strings
    private static final String INVALID_SORT_SEARCH_EXCEPTION = "Sort parameters cannot be processed with null queryString.";
    private static final String UNSUPPORTED_SEARCH_PARAMETERS_EXCEPTION = " %s is not supported as a sort parameter on a system-level search.";
    private static final String MODIFIER_NOT_ALLOWED_WITH_CHAINED_EXCEPTION = "Modifier: '%s' not allowed on chained parameter";
    private static final String TYPE_NOT_ALLOWED_WITH_CHAINED_PARAMETER_EXCEPTION = "Type: '%s' not allowed on chained parameter";
    private static final String SEARCH_PARAMETER_MODIFIER_NAME = "Search parameter: '%s' must have resource type name modifier";
    private static final String INCLUSION_PARAMETERS_NULL_STRING_EXCEPTION = "Inclusion parameters cannot be processed with null queryString.";
    private static final String INVALID_TARGET_TYPE_EXCEPTION = "Invalid target type for the Inclusion Parameter.";

    /*
     * This is our in-memory cache of SearchParameter objects. The cache is organized at the top level by tenant-id,
     * with the built-in (FHIR spec-defined) SearchParameters stored under the "built-in" pseudo-tenant-id.
     * SearchParameters contained in the default tenant's extension-search-parameters.xml file are stored under the
     * "default" tenant-id, and other tenants' SearchParameters (defined in their tenant-specific
     * extension-search-parameters.xml files) will be stored under their respective tenant-ids as well. The objects
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
     * Initializes the various services related to Search and precaches.
     */
    public static void initServletContext() {
        // Inherenetly the searchParameterCache is loaded.

        // Loads the Compartments
        CompartmentUtil.init();

        // Loads the Parameters into a map
        ParametersUtil.init();

        // Loads the ValueTypesUtil
        ValueTypesFactory.init();
    }

    /**
     * Returns the list of search parameters for the specified resource type and the current tenant.
     *
     * @param resourceType
     *            a Class representing the resource type associated with the search parameters to be returned.
     * @throws Exception
     */
    public static List<SearchParameter> getSearchParameters(Class<?> resourceType) throws Exception {
        return getSearchParameters(resourceType.getSimpleName());
    }

    /**
     * This function will return a list of all SearchParameters associated with the specified resource type and the
     * current tenant-id. The result will include both built-in and tenant-specific SearchParameters for the specified
     * resource type.
     *
     * @param resourceType
     *            the resource type associated with the search parameters to be returned
     * @return the list of built-in and tenant-specific search parameters associated with the specified resource type
     * @throws Exception
     */
    public static List<SearchParameter> getSearchParameters(String resourceType) throws Exception {

        List<SearchParameter> result = new ArrayList<>();

        try {
            String tenantId = FHIRRequestContext.get().getTenantId();
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Retrieving SearchParameters for tenant-id '" + tenantId + "' and resource type '" + resourceType + "'.");
            }

            // First retrieve built-in search parameters for this resource type and add them to the result.
            // We'll filter these built-in search parameters to include only the ones
            // specified by the tenant's filtering (inclusion) rules.
            // The contract always returns a valid Map (it just might be empty).
            Map<String, SearchParameter> spMapResourceType = ParametersUtil.getBuiltInSearchParameterMapByResourceType(resourceType);
            if (!spMapResourceType.isEmpty()) {
                // Retrieve the current tenant's search parameter filtering rules.
                Map<String, List<String>> filterRules = getFilterRules();

                // Add only the "included" search parameters for this resource type to our result list.
                result.addAll(filterSearchParameters(filterRules, resourceType, spMapResourceType.values()));

            }

            // Next, retrieve the specified tenant's search parameters for this resource type and add those
            // to the result as well.
            result.addAll(getUserDefinedSearchParameters(resourceType));

        } finally {
            // No Operation
        }

        return result;
    }

    /**
     * Retrieves user-defined SearchParameters associated with the specified resource type and current tenant id.
     *
     * @param resourceType
     *            the resource type for which user-defined SearchParameters will be returned
     * @return a list of user-defined SearchParameters associated with the specified resource type
     * @throws Exception
     */
    protected static List<SearchParameter> getUserDefinedSearchParameters(String resourceType) throws Exception {
        List<SearchParameter> result = new ArrayList<>();
        String tenantId = FHIRRequestContext.get().getTenantId();
        Map<String, Map<String, SearchParameter>> spMapTenant = getTenantOrDefaultSPMap(tenantId);

        if (spMapTenant != null) {
            Map<String, SearchParameter> spMapResourceType = spMapTenant.get(resourceType);
            if (spMapResourceType != null && !spMapResourceType.isEmpty()) {
                result.addAll(spMapResourceType.values());
            }
        }
        return result;
    }

    /**
     * Returns a filtered list of built-in SearchParameters associated with the specified resource type and those
     * associated with the "Resource" resource type.
     *
     * @param resourceType
     *            the resource type
     * @return a filtered list of SearchParameters
     * @throws Exception
     */
    protected static List<SearchParameter> getFilteredBuiltinSearchParameters(String resourceType) throws Exception {
        List<SearchParameter> result = new ArrayList<>();

        Map<String, Map<String, SearchParameter>> spBuiltin = ParametersUtil.getBuiltInSearchParameterMap();

        // Retrieve the current tenant's search parameter filtering rules.
        Map<String, List<String>> filterRules = getFilterRules();

        // Retrieve the SPs associated with the specified resource type and filter per the filter rules.
        Map<String, SearchParameter> spMap = spBuiltin.get(resourceType);
        if (spMap != null && !spMap.isEmpty()) {
            result.addAll(filterSearchParameters(filterRules, resourceType, spMap.values()));
        }

        // TODO: Retrieve the SPs associated with the "Resource" resource type and filter per the filter rules.
        // I'm not sure this is needed.
        spMap = spBuiltin.get(SearchConstants.RESOURCE_RESOURCE);
        if (spMap != null && !spMap.isEmpty()) {
            result.addAll(filterSearchParameters(filterRules, resourceType, spMap.values()));
        }

        return result;
    }

    /**
     * Filters the specified input list of SearchParameters according to the filter rules and input resource type. The
     * filter rules are contained in a Map<String, List<String>> that is keyed by resource type. The value of each Map
     * entry is a list of search parameter names that should be included in our filtered result.
     *
     * @param filterRules
     *            a Map containing filter rules
     * @param resourceType
     *            the resource type associated with each of the unfiltered SearchParameters
     * @param unfilteredSearchParameters
     *            the unfiltered Collection of SearchParameter objects
     * @return a filtered Collection of SearchParameters
     */
    private static Collection<SearchParameter> filterSearchParameters(Map<String, List<String>> filterRules, String resourceType,
        Collection<SearchParameter> unfilteredSearchParameters) {
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

                    String name = sp.getName().getValue();
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
            // <code>{ "*": ["*"] }</code>
            List<String> list = new ArrayList<>();
            list.add(SearchConstants.WILDCARD);
            result.put(SearchConstants.WILDCARD, list);
        }
        return result;
    }

    /**
     * Returns the SearchParameter map (keyed by resource type) for the specified tenant-id. If , or null if there are
     * no SearchParameters for the tenant.
     *
     * @param tenantId
     *            the tenant-id whose SearchParameters should be returned.
     * 
     * @throws FileNotFoundException
     */
    private static Map<String, Map<String, SearchParameter>> getTenantOrDefaultSPMap(String tenantId) throws Exception {
        if (log.isLoggable(Level.FINEST)) {
            log.entering(CLASSNAME, "getTenantSPMap", new Object[] { tenantId });
        }
        try {
            Map<String, Map<String, SearchParameter>> cachedObjectForTenant = searchParameterCache.getCachedObjectForTenant(tenantId);

            if (cachedObjectForTenant == null) {

                // Output logging detail.
                if (log.isLoggable(Level.FINER)) {
                    log.finer(String.format(NO_TENANT_SP_MAP_LOGGING, tenantId, FHIRConfiguration.DEFAULT_TENANT_ID));
                }

                cachedObjectForTenant = searchParameterCache.getCachedObjectForTenant(FHIRConfiguration.DEFAULT_TENANT_ID);
            }

            return cachedObjectForTenant;
        } finally {
            if (log.isLoggable(Level.FINEST)) {
                log.exiting(CLASSNAME, "getTenantSPMap");
            }
        }
    }

    public static SearchParameter getSearchParameter(Class<?> resourceType, String name) throws Exception {
        return getSearchParameter(resourceType.getSimpleName(), name);
    }

    /**
     * 
     * 
     * @param resourceType
     * @param name
     * @return
     * @throws Exception
     */
    public static SearchParameter getSearchParameter(String resourceType, String name) throws Exception {
        String tenantId = FHIRRequestContext.get().getTenantId();

        // First try to find the search parameter within the specified tenant's map.
        SearchParameter result = getSearchParameterInternal(getTenantOrDefaultSPMap(tenantId), resourceType, name, false);

        // If we didn't find it within the tenant's map, then look within the built-in map.
        if (result == null) {
            result = getSearchParameterInternal(ParametersUtil.getBuiltInSearchParameterMap(), resourceType, name, true);

            // If we found it within the built-in search parameters, apply our filtering rules.
            if (result != null) {

                ResourceType rt = result.getBase().get(0).as(ResourceType.class);
                Collection<SearchParameter> filteredResult = filterSearchParameters(getFilterRules(), rt.getValue(), Collections.singleton(result));

                // If our filtered result is non-empty, then just return the first (and only) item.
                result = (filteredResult.isEmpty() ? null : filteredResult.iterator().next());
            }
        }
        return result;
    }

    /**
     * Given a map of SearchParameters for a particular tenant, return the one associated with the specified resource
     * type and name.
     *
     * @param tenantMap
     *            the map in which to look for the desired SearchParameter
     * @param resourceType
     *            the resource type associated with the desired SearchParameter
     * @param name
     *            the name of the desired SearchParameter
     * @return
     */
    private static SearchParameter getSearchParameterInternal(Map<String, Map<String, SearchParameter>> tenantMap, String resourceType, String name,
        boolean searchSuperType) {
        SearchParameter result = null;
        if (tenantMap != null) {
            Map<String, SearchParameter> resourceTypeMap = tenantMap.get(resourceType);
            if (resourceTypeMap != null) {
                result = resourceTypeMap.get(name);
            }

            // If requested, search for the SP associated with the "Resource" super type
            // if we didn't find the SP above.
            if (result == null && searchSuperType) {
                resourceTypeMap = tenantMap.get(SearchConstants.RESOURCE_RESOURCE);
                if (resourceTypeMap != null) {
                    result = resourceTypeMap.get(name);
                }
            }
        }

        return result;
    }

    /**
     * skips the empty extracted search parameters
     * 
     * @param <T>
     * @param resource
     * @return
     * @throws Exception
     */
    public static <T> Map<SearchParameter, List<FHIRPathNode>> extractParameterValues(T resource) throws Exception {
        // Skip Empty is automatically true in this call.
        return extractParameterValues(resource, true);
    }

    /**
     * extract parameter values.
     * 
     * @param <T>
     * @param resource
     * @param skipEmpty
     * @return
     * @throws Exception
     */
    public static <T> Map<SearchParameter, List<FHIRPathNode>> extractParameterValues(T resource, boolean skipEmpty) throws Exception {

        Map<SearchParameter, List<FHIRPathNode>> result = new LinkedHashMap<>();

        // Get the Parameters for the class.
        Class<?> resourceType = resource.getClass();

        // Create one time.
        FHIRPathTree tree = FHIRPathTree.tree((Resource) resource);
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator(tree);

        List<SearchParameter> parameters = getApplicableSearchParameters(resourceType.getSimpleName());

        for (SearchParameter parameter : parameters) {
            com.ibm.watsonhealth.fhir.model.type.String expression = parameter.getExpression();

            // Outputs the Expression and the Name of the SearchParameter
            if (log.isLoggable(Level.FINEST)) {
                log.finest(String.format(EXTRACT_PARAMETERS_LOGGING, parameter.getName().getValue(), expression.getValue()));
            }

            // Process the Expression
            // Disable the processing of composite types
            // <code>"type" : "composite",</code>
            // Alternatives, which are possible, on loading the map, filter out at that time.
            SearchParamType type = parameter.getType();
            if (expression != null && !SearchParamType.COMPOSITE.equals(type)) {

                try {
                    Collection<FHIRPathNode> tmpResults = evaluator.evaluate(expression.getValue());

                    // Adds only if !skipEmpty || collect is not empty
                    if (!tmpResults.isEmpty() || !skipEmpty) {
                        result.put(parameter, new ArrayList<>(tmpResults));
                    }

                } catch (java.lang.UnsupportedOperationException | FHIRPathException uoe) {
                    log.warning(String.format(UNSUPPORTED_EXCEPTION, parameter.getName().getValue(), expression.getValue(), uoe.getMessage()));
                }
            }

        }

        return result;
    }

    public static FHIRSearchContext parseQueryParameters(Class<?> resourceType, Map<String, List<String>> queryParameters, String queryString)
        throws Exception {
        return parseQueryParameters(resourceType, queryParameters, queryString, true);
    }

    public static FHIRSearchContext parseQueryParameters(Class<?> resourceType, Map<String, List<String>> queryParameters, String queryString, boolean lenient)
        throws Exception {
        FHIRSearchContext context = FHIRSearchContextFactory.createSearchContext();
        List<Parameter> parameters = new ArrayList<>();

        // Retrieve the SearchParameters that will apply to this resource type (including those for Resource.class).
        Map<String, SearchParameter> applicableSPs = getApplicableSearchParametersMap(resourceType.getSimpleName());

        /*
         * keySet is used here as the parameter name is the only part that is used to process iteratively in the inner
         * loops.
         */
        for (Entry<String, List<String>> entry : queryParameters.entrySet()) {

            String name = entry.getKey();
            try {

                List<String> params = entry.getValue();
                if (SearchConstants.FORMAT.equals(name)) {
                    continue;
                }

                if (isSearchResultParameter(name)) {
                    parseSearchResultParameter(resourceType, context, name, params, queryString, lenient);
                } else if (isChainedParameter(name)) {
                    List<String> chainedParemeters = params;
                    for (String chainedParameterString : chainedParemeters) {
                        Parameter chainedParameter = parseChainedParameter(resourceType, name, chainedParameterString);
                        parameters.add(chainedParameter);
                    }
                } else {
                    // Parse name into parameter name.

                    String parameterName = name;
                    SearchConstants.Modifier modifier = null;
                    String modifierResourceTypeName = null;
                    if (parameterName.contains(":")) {
                        String mod = parameterName.substring(parameterName.indexOf(":") + 1);
                        if (FHIRUtil.isStandardResourceType(mod)) {
                            modifier = SearchConstants.Modifier.TYPE;
                            modifierResourceTypeName = mod;
                        } else {
                            try {
                                modifier = SearchConstants.Modifier.fromValue(mod);
                                if (!SearchConstants.Modifier.isSupported(modifier)) {
                                    String msg = "Modifier not allowed: " + mod;
                                    throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                                }
                            } catch (IllegalArgumentException e) {
                                String msg = "Undefined Modifier: " + mod;
                                throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                            }
                        }
                        parameterName = parameterName.substring(0, parameterName.indexOf(":"));
                    }

                    // Get the search parameter from our filtered set of applicable SPs for this resource type.
                    SearchParameter searchParameter = applicableSPs.get(parameterName);
                    if (searchParameter == null) {
                        String msg = "Search parameter '" + parameterName + "' for resource type '" + resourceType.getSimpleName() + "' was not found.";
                        if (lenient) {
                            log.fine(msg);
                            continue;
                        } else {
                            throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                        }
                    }

                    // Get the type of parameter so that we can use it to parse the value.
                    SearchConstants.Type type = SearchConstants.Type.fromValue(searchParameter.getType().getValue());

                    if (modifier != null && !isAllowed(type, modifier)) {
                        String msg = "Unsupported type/modifier combination: " + type.value() + "/" + modifier.value();
                        throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                    }

                    for (String queryParameterValueString : queryParameters.get(name)) {
                        Parameter parameter = new Parameter(type, parameterName, modifier, modifierResourceTypeName);
                        List<ParameterValue> queryParameterValues;
                        if (SearchConstants.Modifier.MISSING.equals(modifier)) {
                            // FHIR search considers booleans a special case of token for some reason...
                            queryParameterValues = parseQueryParameterValuesString(SearchConstants.Type.TOKEN, queryParameterValueString);
                        } else {
                            queryParameterValues = parseQueryParameterValuesString(type, queryParameterValueString);
                        }
                        parameter.getValues().addAll(queryParameterValues);
                        parameters.add(parameter);
                    }
                }

            } catch (Exception e) {
                throw SearchExceptionUtil.buildNewParseParameterException(name, e);
            }

        }

        context.setSearchParameters(parameters);
        Collections.sort(context.getSortParameters());

        return context;
    }

    private static List<ParameterValue> parseQueryParameterValuesString(SearchConstants.Type type, String queryParameterValuesString)
        throws FHIRSearchException {
        List<ParameterValue> parameterValues = new ArrayList<>();

        for (String v : queryParameterValuesString.split(SearchConstants.BACKSLASH_NEGATIVE_LOOKBEHIND + ",")) {
            ParameterValue parameterValue = new ParameterValue();
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

                DateTime.Builder builder = DateTime.builder();
                builder.value(v);

                parameterValue.setValueDate(builder.build());
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
                // [parameter]=[type]/[id]
                // [parameter]=[id]
                parameterValue.setValueString(unescapeSearchParm(v));
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
            throw SearchExceptionUtil.buildNewInvalidSearchException("Bare '\\' characters are not allowed in search parameter values and must be escaped via '\\'.");
        }
        return unescapedString.replace("\\\\", "\\");
    }

    /**
     * 
     * @param type
     * @param modifier
     * @return
     */
    protected static boolean isAllowed(SearchConstants.Type type, SearchConstants.Modifier modifier) {
        return SearchConstants.RESOURCE_TYPE_MODIFIER_MAP.get(type).contains(modifier);
    }

    /**
     * Retrieves the applicable search parameters for the specified resource type, then builds a map from it, keyed by
     * search parameter name for quick access.
     */
    public static Map<String, SearchParameter> getApplicableSearchParametersMap(String resourceType) throws Exception {
        Map<String, SearchParameter> result = new HashMap<>();
        List<SearchParameter> list = getApplicableSearchParameters(resourceType);
        for (SearchParameter sp : list) {
            result.put(sp.getName().getValue(), sp);
        }
        return result;
    }

    /**
     * Returns a list of SearchParameters that consist of those associated with the "Resource" base resource type, as
     * well as those associated with the specified resource type.
     */
    public static List<SearchParameter> getApplicableSearchParameters(String resourceType) throws Exception {
        List<SearchParameter> result = getFilteredBuiltinSearchParameters(resourceType);
        result.addAll(getUserDefinedSearchParameters(resourceType));
        return result;
    }

    public static FHIRSearchContext parseQueryParameters(String compartmentName, String compartmentLogicalId, Class<?> resourceType,
        Map<String, List<String>> queryParameters, String queryString) throws Exception {
        return parseQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParameters, queryString, true);
    }

    /**
     * @param lenient
     *            Whether to ignore unknown or unsupported parameter
     * @return
     * @throws Exception
     */
    public static FHIRSearchContext parseQueryParameters(String compartmentName, String compartmentLogicalId, Class<?> resourceType,
        Map<String, List<String>> queryParameters, String queryString, boolean lenient) throws Exception {
        List<Parameter> parameters = new ArrayList<>();
        Parameter parameter;
        ParameterValue value;
        Parameter rootParameter = null;

        if (compartmentName != null && compartmentLogicalId != null) {
            // The inclusion criteria are represented as a chain of parameters, each with a value of the
            // compartmentLogicalId.
            // The query parsers will OR these parameters to achieve the compartment search.
            List<String> inclusionCriteria = CompartmentUtil.getCompartmentResourceTypeInclusionCriteria(compartmentName, resourceType.getSimpleName());
            for (String criteria : inclusionCriteria) {
                parameter = new Parameter(SearchConstants.Type.REFERENCE, criteria, null, null, true);
                value = new ParameterValue();
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

        FHIRSearchContext context = parseQueryParameters(resourceType, queryParameters, queryString, lenient);
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

    private static boolean isSearchResultParameter(String name) {
        return SearchConstants.SEARCH_RESULT_PARAMETER_NAMES.contains(name);
    }

    private static void parseSearchResultParameter(Class<?> resourceType, FHIRSearchContext context, String name, List<String> values, String queryString,
        boolean lenient) throws FHIRSearchException {
        try {
            String first = values.get(0);
            if (SearchConstants.COUNT.equals(name)) {
                int pageSize = Integer.parseInt(first);

                // If the user specified a value > max, then use the max.
                if (pageSize > SearchConstants.MAX_PAGE_SIZE) {
                    pageSize = SearchConstants.MAX_PAGE_SIZE;
                }
                context.setPageSize(pageSize);
            } else if (SearchConstants.PAGE.equals(name)) {
                int pageNumber = Integer.parseInt(first);
                context.setPageNumber(pageNumber);
            } else if (name.startsWith(SearchConstants.SORT)) {
                parseSortParameter(resourceType, context, name, values, queryString, lenient);
            } else if (name.startsWith(SearchConstants.INCLUDE) || name.startsWith(SearchConstants.REVINCLUDE)) {
                parseInclusionParameter(resourceType, context, name, values, queryString, lenient);
            } else if (SearchConstants.ELEMENTS.equals(name)) {
                parseElementsParameter(resourceType, context, values, lenient);
            }
        } catch (Exception e) {
            throw SearchExceptionUtil.buildNewParseException(name, e);
        }
    }

    private static void parseSortParameter(Class<?> resourceType, FHIRSearchContext context, String sortKeyword, List<String> sortParmNames, String queryString,
        boolean lenient) throws Exception {

        SearchConstants.SortDirection sortDirection;
        String sortDirectionString;
        SearchConstants.Type sortParmType;
        SearchParameter sortParmProxy;
        SortParameter sortParm;
        int qualifierDelimiterPosition;
        int queryStringIndex;
        String sortSubstring;

        if (queryString == null) {
            throw SearchExceptionUtil.buildNewInvalidSearchException(INVALID_SORT_SEARCH_EXCEPTION);
        }
        // Extract the sort direction, i.e. 'asc' or 'desc'
        qualifierDelimiterPosition = sortKeyword.indexOf(SearchConstants.COLON_DELIMITER);
        if (qualifierDelimiterPosition > -1) {
            sortDirectionString = sortKeyword.substring(qualifierDelimiterPosition + 1);
            sortDirection = SearchConstants.SortDirection.fromValue(sortDirectionString);
        } else {
            sortDirection = SearchConstants.SortDirection.ASCENDING;
        }

        for (String sortParmName : sortParmNames) {
            // Determine the position of the sort parameter in the query string.
            // This allows the sort parameter itself to be sorted later.
            sortSubstring = sortKeyword + SearchConstants.EQUALS_CHAR + sortParmName;
            queryStringIndex = queryString.indexOf(sortSubstring);
            if (queryStringIndex < 0) {
                String msg = "Sort parameter not found in query string: " + sortSubstring;
                throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
            }
            // Per the FHIR spec, the _sort parameter value is a search parameter. We need to determine what
            // type of search parameter.
            sortParmProxy = getSearchParameter(resourceType, sortParmName);
            if (sortParmProxy == null) {
                String msg = "Undefined sort parameter. resourceType=" + resourceType + " sortParmName=" + sortParmName;
                if (lenient) {
                    log.fine(msg);
                    continue;
                } else {
                    throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                }
            }
            sortParmType = SearchConstants.Type.fromValue(sortParmProxy.getType().getValue());
            sortParm = new SortParameter(sortParmName, sortParmType, sortDirection, queryStringIndex);
            if (resourceType.equals(Resource.class) && !SearchConstants.SYSTEM_LEVEL_SORT_PARAMETER_NAMES.contains(sortParm.getName())) {
                throw SearchExceptionUtil.buildNewInvalidSearchException(String.format(UNSUPPORTED_SEARCH_PARAMETERS_EXCEPTION, sortParm.getName()));
            }
            context.getSortParameters().add(sortParm);
        }
    }

    private static boolean isChainedParameter(String name) {
        return name.contains(SearchConstants.CHAINED_PARAMETER_CHARACTER);
    }

    private static Parameter parseChainedParameter(Class<?> resourceType, String name, String valuesString) throws Exception {

        Parameter rootParameter = null;

        try {
            List<String> components = Arrays.asList(name.split("\\."));
            int lastIndex = components.size() - 1;
            int currentIndex = 0;

            SearchConstants.Type type = null;

            for (String component : components) {
                SearchConstants.Modifier modifier = null;
                String modifierResourceTypeName = null;
                String parameterName = component;

                // Optimization opportunity
                // substring + indexOf and contains execute similar operations
                // collapsing the branching logic is ideal
                int loc = parameterName.indexOf(SearchConstants.COLON_DELIMITER);
                if (loc > 0) {
                    // Parameter exists
                    String mod = parameterName.substring(loc + 1);
                    if (FHIRUtil.isStandardResourceType(mod)) {
                        modifier = SearchConstants.Modifier.TYPE;
                        modifierResourceTypeName = mod;
                    } else {
                        modifier = SearchConstants.Modifier.fromValue(mod);
                    }

                    if (modifier != null && !SearchConstants.Modifier.TYPE.equals(modifier) && currentIndex < lastIndex) {
                        throw SearchExceptionUtil.buildNewInvalidSearchException(String.format(MODIFIER_NOT_ALLOWED_WITH_CHAINED_EXCEPTION, modifier));
                    }
                    parameterName = parameterName.substring(0, parameterName.indexOf(":"));
                }

                SearchParameter searchParameter = getSearchParameter(resourceType, parameterName);
                type = SearchConstants.Type.fromValue(searchParameter.getType().getValue());

                if (!SearchConstants.Type.REFERENCE.equals(type) && currentIndex < lastIndex) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException(String.format(TYPE_NOT_ALLOWED_WITH_CHAINED_PARAMETER_EXCEPTION, type));
                }

                List<ResourceType> targets = searchParameter.getTarget();
                if (modifierResourceTypeName == null && targets.size() > 1 && currentIndex < lastIndex) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException(String.format(SEARCH_PARAMETER_MODIFIER_NAME, parameterName));
                }

                if (modifierResourceTypeName == null && currentIndex < lastIndex) {
                    modifierResourceTypeName = targets.get(0).getValue();
                    modifier = SearchConstants.Modifier.TYPE;
                }

                Parameter parameter = new Parameter(type, parameterName, modifier, modifierResourceTypeName);
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
                    resourceType = FHIRUtil.getResourceType(modifierResourceTypeName);
                }

                currentIndex++;
            } // end for loop

            List<ParameterValue> valueList = parseQueryParameterValuesString(type, valuesString);
            rootParameter.getChain().getLast().getValues().addAll(valueList);
        } catch (FHIRSearchException e) {
            throw e;
        } catch (Exception e) {
            throw SearchExceptionUtil.buildNewChainedParameterException(name, e);
        }

        return rootParameter;
    }

    /**
     * Transforms the passed Parameter representing chained inclusion criteria, into an actual chain of Parameter
     * objects. This method consumes Parameters with names of this form: "{attribute1}.{attribute2}:{resourceType}" For
     * specific examples of chained inclusion criteria, see the FHIR spec for the Patient compartment here:
     *
     * @see https://www.hl7.org/fhir/compartment-patient.html
     * @param inclusionCriteriaParm
     * @return Parameter - The root of a parameter chain for chained inclusion criteria.
     */
    public static Parameter parseChainedInclusionCriteria(Parameter inclusionCriteriaParm) {

        Parameter rootParameter = null;
        Parameter chainedInclusionCriteria = null;
        String[] qualifiedInclusionCriteria;
        String[] parmNames = inclusionCriteriaParm.getName().split("\\.");
        String resourceType = inclusionCriteriaParm.getName().split(SearchConstants.COLON_DELIMITER_STR)[1];
        for (int i = 0; i < parmNames.length; i++) {

            // indexOf(char) is faster than contains str
            if (parmNames[i].indexOf(SearchConstants.COLON_DELIMITER) != -1) {
                qualifiedInclusionCriteria = parmNames[i].split(SearchConstants.COLON_DELIMITER_STR);
                chainedInclusionCriteria =
                        new Parameter(SearchConstants.Type.REFERENCE, qualifiedInclusionCriteria[0], null, resourceType, inclusionCriteriaParm.getValues());
            } else {
                chainedInclusionCriteria = new Parameter(SearchConstants.Type.REFERENCE, parmNames[i], null, resourceType);
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
     * Normalizes a string to be used as a search parameter value. All accents and diacritics are removed. And then the
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
     * Parses _include and _revinclude search result parameters contained in the query string, and produces
     * InclusionParameter objects to represent those parameters. The InclusionParameter objects are included in the
     * appropriate collections encapsulated in the passed FHIRSearchContext.
     * 
     * @throws Exception
     */
    private static void parseInclusionParameter(Class<?> resourceType, FHIRSearchContext context, String inclusionKeyword, List<String> inclusionValues,
        String queryString, boolean lenient) throws Exception {

        String[] inclusionValueParts;
        String joinResourceType;
        String searchParameterName;
        String searchParameterTargetType;

        SearchParameter searchParm;
        InclusionParameter newInclusionParm;
        List<InclusionParameter> newInclusionParms;

        if (queryString == null) {
            throw SearchExceptionUtil.buildNewInvalidSearchException(INCLUSION_PARAMETERS_NULL_STRING_EXCEPTION);
        }

        // Make sure _sort is not present with _include and/or _revinclude.
        if (queryString.contains(SearchConstants.SORT)) {
            throw SearchExceptionUtil.buildNewInvalidSearchException("_sort search result parameter not supported with _include or _revinclude.");
        }

        for (String inclusionValue : inclusionValues) {

            // Parse value into 3 parts: joinResourceType, searchParameterName, searchParameterTargetType
            inclusionValueParts = inclusionValue.split(":");
            if (inclusionValueParts.length < 2) {
                throw SearchExceptionUtil.buildNewInvalidSearchException("A value for _include or _revinclude must have at least 2 parts separated by a colon.");
            }
            joinResourceType = inclusionValueParts[0];
            searchParameterName = inclusionValueParts[1];
            searchParameterTargetType = inclusionValueParts.length == 3 ? inclusionValueParts[2] : null;

            // Ensure that the Inclusion Parameter being parsed is a valid search parameter of type 'reference'.
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
            if (!searchParm.getType().getValue().equals("reference")) {
                throw SearchExceptionUtil.buildNewInvalidSearchException("Inclusion Parameter must be of type 'reference'. "
                        + "The passed Inclusion Parameter is of type: " + searchParm.getType().getValue());
            }

            if (inclusionKeyword.equals(SearchConstants.INCLUDE)) {
                newInclusionParms = buildIncludeParameter(resourceType, joinResourceType, searchParm, searchParameterName, searchParameterTargetType);
                context.getIncludeParameters().addAll(newInclusionParms);
            } else {
                newInclusionParm = buildRevIncludeParameter(resourceType, joinResourceType, searchParm, searchParameterName, searchParameterTargetType);
                context.getRevIncludeParameters().add(newInclusionParm);
            }
        }
    }

    /**
     * Builds and returns a collection of InclusionParameter objects representing occurrences the _include search result
     * parameter in the query string.
     * 
     * @throws FHIRSearchException
     */
    private static List<InclusionParameter> buildIncludeParameter(Class<?> resourceType, String joinResourceType, SearchParameter searchParm,
        String searchParameterName, String searchParameterTargetType) throws FHIRSearchException {

        List<InclusionParameter> includeParms = new ArrayList<>();

        if (!joinResourceType.equals(resourceType.getSimpleName())) {
            throw SearchExceptionUtil.buildNewInvalidSearchException("The join resource type must match the resource type being searched.");
        }

        // If no searchParameterTargetType was specified, create an InclusionParameter instance for each of the search
        // parameter's
        // defined target types.
        if (searchParameterTargetType == null) {
            for (Code targetType : searchParm.getTarget()) {
                searchParameterTargetType = targetType.getValue();
                includeParms.add(new InclusionParameter(joinResourceType, searchParameterName, searchParameterTargetType));
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
     * Builds and returns a collection of InclusionParameter objects representing occurrences the _revinclude search
     * result parameter in the query string.
     * 
     * @throws FHIRSearchException
     */
    private static InclusionParameter buildRevIncludeParameter(Class<?> resourceType, String joinResourceType, SearchParameter searchParm,
        String searchParameterName, String searchParameterTargetType) throws FHIRSearchException {

        // If a target type is specified, it must refer back to the resourceType being searched.
        if (searchParameterTargetType != null) {
            if (!searchParameterTargetType.equals(resourceType.getSimpleName())) {
                throw SearchExceptionUtil.buildNewInvalidSearchException("The search parameter target type must match the resource type being searched.");
            }
        } else {
            searchParameterTargetType = resourceType.getSimpleName();
        }

        // Verify that the search parameter target type is correct
        if (!isValidTargetType(searchParameterTargetType, searchParm)) {
            throw SearchExceptionUtil.buildNewInvalidSearchException(INVALID_TARGET_TYPE_EXCEPTION);
        }
        return new InclusionParameter(joinResourceType, searchParameterName, searchParameterTargetType);

    }

    /**
     * Verifies that the passed searchParameterTargetType is a valid target type for the passed searchParm
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
     * Parses _elements search result parameter contained in the query string, and produces element String objects to
     * represent the values for _elements. Those Strings are included in the elementsParameters collection contained in
     * the passed FHIRSearchContext.
     * 
     * @param lenient
     *            Whether to ignore unknown or unsupported elements
     * @throws Exception
     */
    private static void parseElementsParameter(Class<?> resourceType, FHIRSearchContext context, List<String> elementLists, boolean lenient) throws Exception {

        Set<String> resourceFieldNames = JsonSupport.getElementNames(resourceType.getSimpleName());

        for (String elements : elementLists) {

            // For other parameters, we pass the comma-separated list of values to the PL
            // but for elements, we need to process that here
            for (String elementName : elements.split(SearchConstants.BACKSLASH_NEGATIVE_LOOKBEHIND + ",")) {
                if (elementName.startsWith("_")) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException("Invalid element name: " + elementName);
                }
                if (!resourceFieldNames.contains(elementName)) {
                    if (lenient) {
                        log.fine("Skipping unknown element name: " + elementName);
                        continue;
                    } else {
                        throw SearchExceptionUtil.buildNewInvalidSearchException("Unknown element name: " + elementName);
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
    public static String buildSearchSelfUri(String requestUriString, FHIRSearchContext context) throws URISyntaxException {
        /*
         * the bulk of this method was refactored into UriBuilder.java the signature is maintained here for backwards
         * compatability, and as a simple helper function.
         */
        return UriBuilder.builder().context(context).requestUri(requestUriString).toSearchSelfUri();
    }

}

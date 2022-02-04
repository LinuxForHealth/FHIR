/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.util;

import static com.ibm.fhir.model.util.ModelSupport.FHIR_STRING;

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

import org.owasp.encoder.Encode;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.Interaction;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.config.PropertyGroup.PropertyEntry;
import com.ibm.fhir.config.ResourcesConfigAdapter;
import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.core.FHIRVersionParam;
import com.ibm.fhir.core.util.ResourceTypeHelper;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.SearchParameter.Component;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.model.type.code.SearchComparator;
import com.ibm.fhir.model.type.code.SearchModifierCode;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.model.util.FHIRUtil;
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
import com.ibm.fhir.search.reference.value.CompartmentReference;
import com.ibm.fhir.search.sort.Sort;
import com.ibm.fhir.search.uri.UriBuilder;
import com.ibm.fhir.search.util.ReferenceValue.ReferenceType;
import com.ibm.fhir.term.util.CodeSystemSupport;
import com.ibm.fhir.term.util.ValueSetSupport;

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
    private static final String HAS_DELIMITER = SearchConstants.COLON_DELIMITER_STR + SearchConstants.HAS + SearchConstants.COLON_DELIMITER_STR;

    // compartment parameter reference which can be ignore
    private static final String COMPARTMENT_PARM_DEF = "{def}";

    private static final String IBM_COMPOSITE_PREFIX = "ibm_composite_";

    // The functionality is split into a new class.
    private static final Sort sort = new Sort();

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

        ParametersUtil.init();
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
        if (code == null) {
            return null;
        }
        String tenantId = FHIRRequestContext.get().getTenantId();
        Map<String, ParametersMap> paramsByResourceType = ParametersUtil.getTenantSPs(tenantId);

        // First try the passed resourceType, then fall back to the Resource resourceType (for whole system params)
        for (String type : new String[]{resourceType, FHIRConfigHelper.RESOURCE_RESOURCE}) {
            ParametersMap parametersMap = paramsByResourceType.get(type);
            if (parametersMap != null) {
                SearchParameter searchParam = parametersMap.lookupByCode(code);
                if (searchParam != null) {
                    return searchParam;
                }
            }
        }

        return null;
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
     * @return the SearchParameter for type {@code resourceType} with canonical url {@code uri} or null if it doesn't exist
     * @throws Exception
     */
    public static SearchParameter getSearchParameter(String resourceType, Canonical uri) throws Exception {
        if (uri == null || uri.getValue() == null) {
            return null;
        }
        String tenantId = FHIRRequestContext.get().getTenantId();
        Map<String, ParametersMap> paramsByResourceType = ParametersUtil.getTenantSPs(tenantId);

        // First try the passed resourceType, then fall back to the Resource resourceType (for whole system params)
        for (String type : new String[]{resourceType, FHIRConfigHelper.RESOURCE_RESOURCE}) {
            ParametersMap parametersMap = paramsByResourceType.get(type);
            if (parametersMap != null) {
                SearchParameter searchParam = parametersMap.lookupByCanonical(uri.getValue());
                if (searchParam != null) {
                    return searchParam;
                }
            }
        }

        return null;
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
     * @param modifier
     *     the modifier specified (may be null)
     * @return
     *         the inclusion SearchParameters for type {@code resourceType} or empty map if none exist
     * @throws Exception
     */
    private static Map<String, SearchParameter> getInclusionWildcardSearchParameters(String resourceType, String joinResourceType,
        String searchParameterTargetType, String inclusionKeyword, Modifier modifier) throws Exception {
        Map<String, SearchParameter> inclusionSearchParameters = new HashMap<>();

        for (Entry<String, SearchParameter> searchParameterEntry : getSearchParameters(joinResourceType).entrySet()) {
            String code = searchParameterEntry.getKey();
            SearchParameter searchParameter = searchParameterEntry.getValue();

            if (SearchParamType.REFERENCE.equals(searchParameter.getType()) &&
                    ((SearchConstants.INCLUDE.equals(inclusionKeyword)
                            && (searchParameterTargetType == null || isValidTargetType(searchParameterTargetType, searchParameter))) ||
                     (SearchConstants.REVINCLUDE.equals(inclusionKeyword)
                            && ((!Modifier.ITERATE.equals(modifier) && isValidTargetType(resourceType, searchParameter))
                                || (Modifier.ITERATE.equals(modifier) && (searchParameterTargetType == null
                                    || isValidTargetType(searchParameterTargetType, searchParameter))))))) {
                // Valid search parameter of type reference - add to map
                inclusionSearchParameters.put(code, searchParameter);
            } else if (inclusionSearchParameters.containsKey(code)) {
                // Invalid duplicate search parameter found for valid search parameter already in map. Log invalid
                // search parameter and ignore.
                log.fine("Invalid duplicate search parameter '" + code +
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

        Map<String, SearchParameter> parameters = getSearchParameters(resourceType.getSimpleName());

        for (Entry<String, SearchParameter> parameterEntry : parameters.entrySet()) {
            String code = parameterEntry.getKey();
            SearchParameter parameter = parameterEntry.getValue();

            com.ibm.fhir.model.type.String expression = parameter.getExpression();

            // Outputs the Expression and the Name of the SearchParameter
            if (log.isLoggable(Level.FINEST)) {
                String loggedValue = "EMPTY";
                if (expression != null) {
                    loggedValue = expression.getValue();
                }

                log.finest(String.format(EXTRACT_PARAMETERS_LOGGING, code, loggedValue));
            }

            // Process the Expression
            if (expression == null) {
                if (log.isLoggable(Level.FINER)) {
                    log.finer(String.format(UNSUPPORTED_EXPR_NULL, parameter.getType(), code));
                }
                continue;
            }
            try {
                Collection<FHIRPathNode> tmpResults = evaluator.evaluate(evaluationContext, expression.getValue());

                if (log.isLoggable(Level.FINEST)) {
                    log.finest("Expression [" + expression.getValue() + "] parameter-code ["
                            + code + "] Size -[" + tmpResults.size() + "]");
                }

                // Adds only if !skipEmpty || tmpResults is not empty
                if (!tmpResults.isEmpty() || !skipEmpty) {
                    result.put(parameter, new ArrayList<>(tmpResults));
                }

            } catch (java.lang.UnsupportedOperationException | FHIRPathException uoe) {
                // switched to using code instead of name
                log.warning(String.format(UNSUPPORTED_EXCEPTION, code,
                        expression.getValue(), uoe.getMessage()));
            }
        }

        return result;
    }

    public static FHIRSearchContext parseQueryParameters(Class<?> resourceType,
            Map<String, List<String>> queryParameters) throws Exception {
        return parseQueryParameters(resourceType, queryParameters, false, true, FHIRVersionParam.VERSION_43);
    }

    public static FHIRSearchContext parseQueryParameters(Class<?> resourceType,
            Map<String, List<String>> queryParameters, boolean lenient, boolean includeResource) throws Exception {
        return parseQueryParameters(resourceType, queryParameters, lenient, includeResource, FHIRVersionParam.VERSION_43);
    }

    /**
     * Parse the passed query parameters into a FHIRSeachContext according to the given options
     *
     * @param resourceType
     * @param queryParameters
     * @param lenient
     * @param includeResource
     * @param fhirVersion
     * @return
     * @throws Exception
     */
    public static FHIRSearchContext parseQueryParameters(Class<?> resourceType, Map<String, List<String>> queryParameters,
            boolean lenient, boolean includeResource, FHIRVersionParam fhirVersion) throws Exception {

        FHIRSearchContext context = FHIRSearchContextFactory.createSearchContext();
        context.setLenient(lenient);
        context.setIncludeResourceData(includeResource);
        List<QueryParameter> parameters = new ArrayList<>();

        // Check for duplicate parameters that are supposed to be specified at most once
        for (Entry<String, List<String>> entry : queryParameters.entrySet()) {
            String name = entry.getKey();
            if (isSearchSingletonParameter(name) && entry.getValue().size() > 1) {
                manageException("Search parameter '" + name + "' is specified multiple times", IssueType.INVALID, context, false);
            }
        }

        // _include and _revinclude searches requires specific resource type modifier in
        // search parameter, so we don't support system search with them.
        if (containsInclusionParameter(queryParameters.keySet()) && Resource.class.equals(resourceType)) {
            throw SearchExceptionUtil.buildNewInvalidSearchException("system search not supported with _include or _revinclude.");
        }

        // _type parameter is only supported for whole system searches
        if (queryParameters.containsKey(SearchConstants.RESOURCE_TYPE) && !Resource.class.equals(resourceType)) {
            manageException("_type parameter is only supported for whole-system search", IssueType.NOT_SUPPORTED, context, false);
        }

        boolean isSystemSearch = Resource.class.equals(resourceType);
        Set<String> resourceTypes = new LinkedHashSet<>();
        if (isSystemSearch) {
            if (queryParameters.containsKey(SearchConstants.RESOURCE_TYPE)) {
                // if the _type parameter was supplied
                // Only first value is used, which matches behavior of other parameters that are supposed to be specified at most once
                String resTypes = queryParameters.get(SearchConstants.RESOURCE_TYPE).get(0);
                for (String resType : resTypes.split("\\s*,\\s*")) {
                    if (!ModelSupport.isConcreteResourceType(resType)) {
                        manageException("_type parameter has invalid resource type: " + Encode.forHtml(resType),
                                IssueType.INVALID, context, false);
                    } else {
                        if (!isSearchEnabled(resType)) {
                            manageException("search interaction is not supported for _type parameter value: " + Encode.forHtml(resType),
                                    IssueType.NOT_SUPPORTED, context, false);
                        } else if (!ResourceTypeHelper.isCompatible(resType, fhirVersion, FHIRVersionParam.VERSION_43)) {
                            String msg = "fhirVersion " + fhirVersion.value() + " interaction for _type parameter value: '" + resType +
                                    "' is not supported";
                            manageException(msg, IssueType.NOT_SUPPORTED, context, false);
                        } else {
                            resourceTypes.add(resType);
                        }
                    }
                }
            }

            if (resourceTypes.isEmpty()) {
                Boolean implicitTypeScoping = FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_WHOLE_SYSTEM_TYPE_SCOPING, true);
                if (implicitTypeScoping) {
                    PropertyGroup rsrcsGroup = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_RESOURCES);
                    ResourcesConfigAdapter configAdapter = new ResourcesConfigAdapter(rsrcsGroup, fhirVersion);
                    resourceTypes.addAll(configAdapter.getSupportedResourceTypes(Interaction.SEARCH));
                }
            }
        }

        queryParameters.remove(SearchConstants.RESOURCE_TYPE);

        boolean hasResourceTypeFilter = isSystemSearch && !resourceTypes.isEmpty();

        if (hasResourceTypeFilter) {
            context.setSearchResourceTypes(new ArrayList<>(resourceTypes));
        }

        for (Entry<String, List<String>> entry : queryParameters.entrySet()) {
            String name = entry.getKey();
            try {
                List<String> params = entry.getValue();

                if (isSearchResultParameter(name)) {
                    parseSearchResultParameter(resourceType, context, name, params);
                } else if (isGeneralParameter(name) ) {
                    // we'll handle it somewhere else, so just ignore it here
                } else if (isReverseChainedParameter(name)) {
                    if (hasResourceTypeFilter) {
                        // _has search requires specific resource type modifier in
                        // search parameter, so we don't currently support system search.
                        throw SearchExceptionUtil.buildNewInvalidSearchException("system search not supported with _has.");
                    }
                    for (String reverseChainedParameterValueString : params) {
                        parameters.add(parseReverseChainedParameter(resourceType, name, reverseChainedParameterValueString, context));
                    }
                } else if (isChainedParameter(name)) {
                    List<String> chainedParemeters = params;
                    for (String chainedParameterString : chainedParemeters) {
                        QueryParameter chainedParameter;
                        if (hasResourceTypeFilter) {
                            chainedParameter = parseChainedParameter(resourceTypes, name, chainedParameterString, context);
                        } else {
                            chainedParameter = parseChainedParameter(resourceType, name, chainedParameterString, context);
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
                    if (hasResourceTypeFilter) {
                      // Find the SearchParameter that will apply to all the resource types.
                      for (String resType: resourceTypes) {
                          // Get the search parameter from our filtered set of applicable SPs for this resource type.
                          searchParameter = getSearchParameter(resType, parameterCode);
                          throwSearchParameterExceptionIfNull(searchParameter, parameterCode, resType, context);
                      }
                    } else {
                        // Get the search parameter from our filtered set of applicable SPs for this resource type.
                        searchParameter = getSearchParameter(resourceType.getSimpleName(), parameterCode);
                        throwSearchParameterExceptionIfNull(searchParameter, parameterCode, resourceType.getSimpleName(), context);
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
                        QueryParameter parameter;
                        if (Modifier.OF_TYPE.equals(modifier)) {
                            // Internally treat search with :of-type modifier as composite search
                            parameter = new QueryParameter(Type.COMPOSITE, parameterCode + SearchConstants.OF_TYPE_MODIFIER_SUFFIX, null, null);
                        } else if (Type.REFERENCE.equals(type) && isCanonicalSearchParm(resourceType, searchParameter.getExpression().getValue())) {
                            // Internally treat canonical search parameters as type URI
                            parameter = new QueryParameter(Type.URI, parameterCode, modifier, modifierResourceTypeName, false, false, true);
                        } else {
                            parameter = new QueryParameter(type, parameterCode, modifier, modifierResourceTypeName);
                        }
                        List<QueryParameterValue> queryParameterValues = processQueryParameterValueString(resourceType, searchParameter,
                            modifier, modifierResourceTypeName, paramValueString, parameter.isCanonical());
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
                if (lenient) {
                    String msg = "Search parameter '" + name + "' for resource type '" + resourceType.getSimpleName() + "' ignored";
                    log.log(Level.FINE, msg, se);
                    context.addOutcomeIssue(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.WARNING, IssueType.INCOMPLETE, msg));
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

            // Check include resource type mismatches for :iterate parameters
            if (!context.getIncludeParameters().isEmpty() || !context.getRevIncludeParameters().isEmpty()) {
                // _include and _revinclude parameters cannot be mixed with _summary=text
                if (SummaryValueSet.TEXT.equals(context.getSummaryParameter())) {
                    manageException("_include and _revinclude are not supported with '_summary=text'", IssueType.NOT_SUPPORTED, context, false);
                    manageException("_include and _revinclude parameters are ignored", IssueType.INCOMPLETE, context, false);
                    context.getIncludeParameters().clear();
                    context.getRevIncludeParameters().clear();
                } else {
                    checkInclusionIterateParameters(resourceType.getSimpleName(), context, lenient);
                }
            }
        } catch (FHIRSearchException se) {
            throw se;
        } catch (Exception e) {
            throw SearchExceptionUtil.buildNewParseParametersException(e);
        }

        context.setSearchParameters(parameters);
        return context;
    }

    private static boolean isSearchEnabled(String resourceType) throws FHIROperationException {
        boolean resourceValid = true;
        List<String> interactions = null;

        // Retrieve the interaction configuration
        try {
            StringBuilder defaultInteractionsConfigPath = new StringBuilder(FHIRConfiguration.PROPERTY_RESOURCES).append("/Resource/")
                    .append(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_INTERACTIONS);
            StringBuilder resourceSpecificInteractionsConfigPath = new StringBuilder(FHIRConfiguration.PROPERTY_RESOURCES).append("/")
                    .append(resourceType).append("/").append(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_INTERACTIONS);

            // Get the 'interactions' property
            List<String> resourceSpecificInteractions = FHIRConfigHelper.getStringListProperty(resourceSpecificInteractionsConfigPath.toString());
            if (resourceSpecificInteractions != null) {
                interactions = resourceSpecificInteractions;
            } else {
                // Check the 'open' property, and if that's false, check if resource was specified
                if (!FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_RESOURCES + "/" + FHIRConfiguration.PROPERTY_FIELD_RESOURCES_OPEN, true)) {
                    PropertyGroup resourceGroup = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_RESOURCES + "/" + resourceType);
                    if (resourceGroup == null) {
                        resourceValid = false;
                    }
                }
                if (resourceValid) {
                    // Get the 'Resource' interaction property
                    List<String> defaultInteractions = FHIRConfigHelper.getStringListProperty(defaultInteractionsConfigPath.toString());
                    if (defaultInteractions != null) {
                        interactions = defaultInteractions;
                    }
                }
            }

            if (log.isLoggable(Level.FINE)) {
                log.fine("Allowed interactions: " + interactions);
            }
        } catch (Exception e) {
            String msg = "Error retrieving interactions configuration.";
            throw new FHIROperationException(msg).withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.EXCEPTION));
        }

        // Perform validation of specified interaction against specified resourceType
        if (interactions != null && !interactions.contains("search")) {
            return false;
        }
        return resourceValid;
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
                        if (comparator.getValueAsEnum() == prefixAsComparator.getValueAsEnum()) {
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
                        if (FHIRConfigHelper.RESOURCE_RESOURCE.equals(rsrcsEntry.getName())) {
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
            String modifierResourceTypeName, String queryParameterValueString, boolean isCanonical) throws FHIRSearchException, Exception {
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
                queryParameterValues = parseQueryParameterValuesString(searchParameter, Type.TOKEN, modifier, modifierResourceTypeName,
                    queryParameterValueString, isCanonical);
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
            queryParameterValues = parseQueryParameterValuesString(searchParameter, Type.TOKEN, modifier, modifierResourceTypeName, queryParameterValueString,
                isCanonical);
        } else {
            queryParameterValues = parseQueryParameterValuesString(searchParameter, type, modifier, modifierResourceTypeName, queryParameterValueString,
                isCanonical);
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
                List<QueryParameterValue> values = parseQueryParameterValuesString(searchParameter, compTypes.get(i), null, null,
                    componentValueStrings[i], false);
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
            Modifier modifier, String modifierResourceTypeName, String queryParameterValuesString,
            boolean isCanonical) throws FHIRSearchException {
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
                // [parameter]=[literal|version#fragment] - canonical url
                // [parameter]:identifier=[system|code] - token search of identifier field
                if (Modifier.IDENTIFIER.equals(modifier)) {
                    String[] parts = v.split(SearchConstants.BACKSLASH_NEGATIVE_LOOKBEHIND + "\\|");
                    if (parts.length == 2) {
                        parameterValue.setValueSystem(unescapeSearchParm(parts[0]));
                        parameterValue.setValueCode(unescapeSearchParm(parts[1]));
                    } else {
                        parameterValue.setValueCode(unescapeSearchParm(v));
                    }
                } else if (isCanonical) {
                    parameterValue.setValueString(unescapeSearchParm(v));
                } else {
                    String valueString = unescapeSearchParm(v);
                    valueString = extractReferenceValue(valueString);
                    parameterValue.setValueString(valueString);
                }
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
                // [parameter]:of-type=[system|code|value]
                // [parameter]:text=code
                /*
                 * TODO: start enforcing this:
                 * "For token parameters on elements of type ContactPoint, uri, or boolean,
                 * the presence of the pipe symbol SHALL NOT be used - only the
                 * [parameter]=[code] form is allowed
                 */
                String[] parts = v.split(SearchConstants.BACKSLASH_NEGATIVE_LOOKBEHIND + "\\|");
                if (Modifier.OF_TYPE.equals(modifier)) {
                    // Convert :of-type into a composite search parameter
                    final String ofTypeParmName = searchParameter.getCode().getValue() + SearchConstants.OF_TYPE_MODIFIER_SUFFIX;
                    parameterValue.setOfTypeModifier(true);
                    if (parts.length < 2) {
                        String msg = "Search parameter '" + searchParameter.getCode().getValue() + "' with modifier ':" + modifier.value() +
                                "' requires at least a code and value";
                        throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                    } else if (parts.length < 4) {
                        QueryParameterValue typeParameterValue = new QueryParameterValue();
                        if (parts.length == 3) {
                            typeParameterValue.setValueSystem(unescapeSearchParm(parts[0]));
                        }
                        typeParameterValue.setValueCode(unescapeSearchParm(parts[parts.length - 2]));
                        QueryParameter typeParameter = new QueryParameter(Type.TOKEN, SearchUtil.makeCompositeSubCode(ofTypeParmName,
                            SearchConstants.OF_TYPE_MODIFIER_COMPONENT_TYPE), null, null, Collections.singletonList(typeParameterValue));
                        parameterValue.addComponent(typeParameter);

                        QueryParameterValue valueParameterValue = new QueryParameterValue();
                        valueParameterValue.setValueCode(unescapeSearchParm(parts[parts.length - 1]));
                        QueryParameter valueParameter = new QueryParameter(Type.TOKEN, SearchUtil.makeCompositeSubCode(ofTypeParmName,
                            SearchConstants.OF_TYPE_MODIFIER_COMPONENT_VALUE), null, null, Collections.singletonList(valueParameterValue));
                        parameterValue.addComponent(valueParameter);
                    } else {
                        QueryParameterValue valueParameterValue = new QueryParameterValue();
                        valueParameterValue.setValueCode(unescapeSearchParm(v));
                        QueryParameter valueParameter = new QueryParameter(Type.TOKEN, SearchUtil.makeCompositeSubCode(ofTypeParmName,
                            SearchConstants.OF_TYPE_MODIFIER_COMPONENT_VALUE), null, null, Collections.singletonList(valueParameterValue));
                        parameterValue.addComponent(valueParameter);
                    }
                } else if (Modifier.IN.equals(modifier) || Modifier.NOT_IN.equals(modifier)) {
                    // Validate that the parameter value is a ValueSet URL that points to a registered ValueSet that is
                    // expandable.
                    ValueSet valueSet = ValueSetSupport.getValueSet(v);
                    if (valueSet == null) {
                        String msg = "ValueSet '" + v + "' specified for search parameter '" + searchParameter.getCode().getValue() +
                                "' with modifier ':" + modifier.value() + "' could not be found";
                        throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                    }
                    if (!ValueSetSupport.isExpandable(valueSet)) {
                        String msg = "ValueSet '" + v + "' specified for search parameter '" + searchParameter.getCode().getValue() +
                                "' with modifier ':" + modifier.value() + "' is not expandable";
                        throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                    }
                    parameterValue.setValueCode(unescapeSearchParm(v));
                } else if (Modifier.ABOVE.equals(modifier) || Modifier.BELOW.equals(modifier)) {
                    // Validate that the parameter value is a system+code
                    if (parts.length != 2) {
                        String msg = "Search parameter '" + searchParameter.getCode().getValue() + "' with modifier ':" + modifier.value() +
                                "' requires a system and code";
                        throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                    }
                    // Validate that the system value is a URL that points to a registered CodeSystem.
                    CodeSystem codeSystem = CodeSystemSupport.getCodeSystem(parts[0]);
                    if (codeSystem == null) {
                        String msg = "CodeSystem '" + parts[0] + "' specified for search parameter '" + searchParameter.getCode().getValue() +
                                "' with modifier ':" + modifier.value() + "' could not be found";
                        throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                    }
                    // Validate that the code exists in the code system
                    Code code = Code.builder().value(parts[1]).build();
                    Concept concept = CodeSystemSupport.findConcept(codeSystem, code);
                    if (concept == null) {
                        String msg = "Code '" + parts[1] + "' specified for search parameter '" + searchParameter.getCode().getValue() +
                                "' with modifier ':" + modifier.value() + "' does not exist in CodeSystem '" + parts[0] + "'";
                        throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
                    }
                    parameterValue.setValueSystem(unescapeSearchParm(parts[0]));
                    parameterValue.setValueCode(unescapeSearchParm(parts[1]));
                } else if (Modifier.TEXT.equals(modifier)) {
                    parameterValue.setValueCode(unescapeSearchParm(v));
                } else if (parts.length == 2) {
                    // This will be the empty string if the search is like `param=|code`
                    parameterValue.setValueSystem(unescapeSearchParm(parts[0]));
                    parameterValue.setValueCode(unescapeSearchParm(parts[1]));
                } else if (parts.length == 1 && v.endsWith("|") && v.indexOf("|") == v.length()-1) {
                    // Only a system was specified (uri followed by a single '|')
                    parameterValue.setValueSystem(unescapeSearchParm(parts[0]));
                } else {
                    // Treat as a single code.
                    // Optimization for search parameters that always reference the same system, added under #1929
                    if (!Modifier.MISSING.equals(modifier)) {
                        try {
                            String implicitSystem = findImplicitSystem(searchParameter.getExtension());
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
     * Look up the http://ibm.com/fhir/extension/implicit-system extension in
     * the given list of Extensions
     * @param extensions
     * @return the implicit system value, or null if not found
     */
    public static String findImplicitSystem(List<Extension> extensions) {
        return extensions.stream()
                .filter(e -> SearchConstants.IMPLICIT_SYSTEM_EXT_URL.equals(e.getUrl()) && e.getValue() != null)
                .findFirst()
                .map(e -> e.getValue().as(Uri.class).getValue())
                .orElse(null);
    }

    /**
     * Convert the string to a reference value usable by the persistence
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
     * Returns a map of code to SearchParameter that consist of those associated with the
     * "Resource" base resource type, as well as those associated with the specified resource type.
     *
     * @return the applicable search parameters for the current request context, indexed by code; never null
     */
    public static Map<String, SearchParameter> getSearchParameters(String resourceType) throws Exception {
        Map<String, SearchParameter> result = new LinkedHashMap<>();

        String tenantId = FHIRRequestContext.get().getTenantId();
        Map<String, ParametersMap> paramsByResourceType = ParametersUtil.getTenantSPs(tenantId);

        for (String type : new String[]{FHIRConfigHelper.RESOURCE_RESOURCE, resourceType}) {
            ParametersMap parametersMap = paramsByResourceType.get(type);
            if (parametersMap != null) {
                for (Entry<String, SearchParameter> entry : parametersMap.codeEntries()) {
                    String code = entry.getKey();
                    if (log.isLoggable(Level.FINE) && result.containsKey(code)) {
                        log.fine("Code '" + code + "' is defined for both " + FHIRConfigHelper.RESOURCE_RESOURCE
                            + " and " + resourceType + "; using " + resourceType);
                    }
                    result.put(code, entry.getValue());
                }
            }
        }

        return result;
    }

    /**
     * Parse query parameters for read and vread.
     * @param resourceType the resource type
     * @param queryParameters the query parameters
     * @param interaction read or vread
     * @param lenient true if lenient, false if strict
     * @param fhirVersion
     * @return the FHIR search context
     * @throws Exception an exception
     */
    public static FHIRSearchContext parseReadQueryParameters(Class<?> resourceType,
            Map<String, List<String>> queryParameters, String interaction, boolean lenient, FHIRVersionParam fhirVersion) throws Exception {
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

        return parseCompartmentQueryParameters(null, null, resourceType, queryParameters, lenient, true, fhirVersion);
    }


    public static FHIRSearchContext parseCompartmentQueryParameters(String compartmentName, String compartmentLogicalId,
            Class<?> resourceType, Map<String, List<String>> queryParameters) throws Exception {
        return parseCompartmentQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParameters, true, true, FHIRVersionParam.VERSION_43);
    }

    public static FHIRSearchContext parseCompartmentQueryParameters(String compartmentName, String compartmentLogicalId,
            Class<?> resourceType, Map<String, List<String>> queryParameters, FHIRVersionParam fhirVersion) throws Exception {
        return parseCompartmentQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParameters, true, true, fhirVersion);
    }

    /**
     * @param compartmentName
     * @param compartmentLogicalId
     * @param resourceType
     * @param queryParameters
     * @param lenient
     *                Whether to ignore unknown or unsupported parameter
     * @param includeResource
     *                Whether to include the resource from the result (return handling prefer != minimal)
     * @param fhirVersion
     * @return
     * @throws Exception
     */
    public static FHIRSearchContext parseCompartmentQueryParameters(String compartmentName, String compartmentLogicalId,
            Class<?> resourceType, Map<String, List<String>> queryParameters, boolean lenient, boolean includeResource,
            FHIRVersionParam fhirVersion) throws Exception {

        Set<String> compartmentLogicalIds = Collections.singleton(compartmentLogicalId);
        QueryParameter inclusionCriteria = buildInclusionCriteria(compartmentName, compartmentLogicalIds, resourceType.getSimpleName());
        FHIRSearchContext context = parseQueryParameters(resourceType, queryParameters, lenient, includeResource, fhirVersion);

        // Add the inclusion criteria to the front of the search parameter list
        if (inclusionCriteria != null) {
            context.getSearchParameters().add(0, inclusionCriteria);
        }

        return context;
    }

    /**
     * Build a query parameter to encapsulate the inclusion criteria for a compartment query
     *
     * @param compartmentName
     * @param compartmentLogicalIds
     * @param resourceType
     * @return
     * @throws FHIRSearchException
     */
    public static QueryParameter buildInclusionCriteria(String compartmentName, Set<String> compartmentLogicalIds, String resourceType)
            throws FHIRSearchException {
        QueryParameter rootParameter = null;

        if (compartmentName != null && compartmentLogicalIds != null && !compartmentLogicalIds.isEmpty()) {
            // The inclusion criteria are represented as a chain of parameters, each with a value of the
            // compartmentLogicalId.
            // The query parsers will OR these parameters to achieve the compartment search.
            List<String> inclusionCriteria;

            if (useStoredCompartmentParam()) {
                // issue #1708. When enabled, use the ibm-internal-... compartment parameter. This
                // results in faster queries because only a single parameter is used to represent the
                // compartment membership.
                CompartmentUtil.checkValidCompartmentAndResource(compartmentName, resourceType);
                inclusionCriteria = Collections.singletonList(CompartmentUtil.makeCompartmentParamName(compartmentName));
            } else {
                // pre #1708 behavior
                inclusionCriteria = CompartmentUtil.getCompartmentResourceTypeInclusionCriteria(compartmentName, resourceType);
            }

            for (String criteria : inclusionCriteria) {
                QueryParameter parameter  = new QueryParameter(Type.REFERENCE, criteria, null, null, true);
                for (String compartmentLogicalId : compartmentLogicalIds) {
                    QueryParameterValue value = new QueryParameterValue();
                    value.setValueString(compartmentName + "/" + compartmentLogicalId);
                    parameter.getValues().add(value);
                }

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
        return rootParameter;
    }

    /**
     * Check the configuration to see if the compartment search optimization is enabled.
     * The config property defaults to true and may be removed in a near-future release.
     * @return
     */
    public static boolean useStoredCompartmentParam() {
        return FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_USE_STORED_COMPARTMENT_PARAM, true);
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

    /**
     * Determine if the parameter is a search result parameter.
     *
     * @param name - the parameter name
     * @return true if the parameter is a search result parameter, false otherwise
     */
    public static boolean isSearchResultParameter(String name) {
        return (SearchConstants.SEARCH_RESULT_PARAMETER_NAMES.contains(name) ||
                name.startsWith(SearchConstants.INCLUDE + SearchConstants.COLON_DELIMITER_STR) ||
                name.startsWith(SearchConstants.REVINCLUDE + SearchConstants.COLON_DELIMITER_STR));
    }

    public static boolean isSearchSingletonParameter(String name) {
        return SearchConstants.SEARCH_SINGLETON_PARAMETER_NAMES.contains(name);
    }

    public static boolean isGeneralParameter(String name) {
        return FHIRConstants.GENERAL_PARAMETER_NAMES.contains(name);
    }

    private static void parseSearchResultParameter(Class<?> resourceType, FHIRSearchContext context, String name,
            List<String> values) throws FHIRSearchException {
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
                    if (pageSize > context.getMaxPageSize()) {
                        pageSize = context.getMaxPageSize();
                    }
                    context.setPageSize(pageSize);
                }
            } else if (SearchConstants.PAGE.equals(name)) {
                int pageNumber = Integer.parseInt(first);
                context.setPageNumber(pageNumber);
            } else if (SearchConstants.SORT.equals(name) && first != null) {
                // in R4, we only look for _sort
                // Only first value is used, which matches behavior of other parameters that are supposed to be specified at most once
                sort.parseSortParameter(resourceTypeName, context, first);
            } else if (name.startsWith(SearchConstants.INCLUDE) || name.startsWith(SearchConstants.REVINCLUDE)) {
                parseInclusionParameter(resourceType, context, name, values);
            } else if (SearchConstants.ELEMENTS.equals(name) && first != null) {
                // Only first value is used, which matches behavior of other parameters that are supposed to be specified at most once
                parseElementsParameter(resourceType, context, first);
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

    private static QueryParameter parseChainedParameter(Set<String> resourceTypes, String name, String valuesString, FHIRSearchContext context)
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
                    throwSearchParameterExceptionIfNull(searchParameter, parameterName, resTypeName, context);

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

                boolean isCanonical = Type.REFERENCE.equals(type) &&
                        isCanonicalSearchParm(resourceType, searchParameter.getExpression().getValue());
                QueryParameter parameter = new QueryParameter(isCanonical && (currentIndex == lastIndex) ? Type.URI : type,
                        parameterName, modifier, modifierResourceTypeName, false, false, isCanonical);
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

            List<QueryParameterValue> valueList = processQueryParameterValueString(resourceType, searchParameter, modifier,
                rootParameter.getModifierResourceTypeName(), valuesString, rootParameter.getChain().getLast().isCanonical());

            if (checkForLogicalId) {
                // For last search parameter, if type REFERENCE and not scoped to single target resource type, check if
                // value is logical ID only. If so, throw an exception.
                checkQueryParameterValuesForLogicalIdOnly(rootParameter.getChain().getLast().getCode(), valueList, context);
            }

            rootParameter.getChain().getLast().getValues().addAll(valueList);
        } catch (FHIRSearchException e) {
            throw e;
        } catch (Exception e) {
            throw SearchExceptionUtil.buildNewChainedParameterException(name, e);
        }

        return rootParameter;
    }

    private static QueryParameter parseChainedParameter(Class<?> resourceType, String name, String valuesString, FHIRSearchContext context)
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
                throwSearchParameterExceptionIfNull(searchParameter, parameterName, resourceType.getSimpleName(), context);

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

                boolean isCanonical = Type.REFERENCE.equals(type) &&
                        isCanonicalSearchParm(resourceType, searchParameter.getExpression().getValue());
                QueryParameter parameter = new QueryParameter(isCanonical && (currentIndex == lastIndex) ? Type.URI : type,
                        parameterName, modifier, modifierResourceTypeName, false, false, isCanonical);
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

            List<QueryParameterValue> valueList = processQueryParameterValueString(resourceType, searchParameter, modifier,
                rootParameter.getModifierResourceTypeName(), valuesString, rootParameter.getChain().getLast().isCanonical());

            if (checkForLogicalId) {
                // For last search parameter, if type REFERENCE and not scoped to single target resource type, check if
                // value is logical ID only. If so, throw an exception.
                checkQueryParameterValuesForLogicalIdOnly(rootParameter.getChain().getLast().getCode(), valueList, context);
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
     * <a href="https://www.hl7.org/fhir/search.html#has"</a>
     *
     * @param resourceType
     *     Search type.
     * @param reverseChainParameterString
     *     Reverse chain search parameter string.
     * @param valuesString
     *     String containing the final search value.
     * @param context
     *     Search context.
     * @return QueryParameter
     *     The root of a parameter chain for the reverse chain criteria.
     */
    private static QueryParameter parseReverseChainedParameter(Class<?> resourceType, String reverseChainParameterString, String valuesString, FHIRSearchContext context) throws Exception {

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
                throwSearchParameterExceptionIfNull(referenceSearchParameter, referenceSearchParameterName, referencedByResourceTypeName, context);
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
                boolean isCanonical = isCanonicalSearchParm(resourceType, referenceSearchParameter.getExpression().getValue());
                QueryParameter parameter = new QueryParameter(Type.REFERENCE, referenceSearchParameterName,
                        Modifier.TYPE, referencedByResourceTypeName, false, true, isCanonical);
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
                        QueryParameter lastParameter = parseChainedParameter(referencedByResourceType, parameterName, valuesString, context);
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
                        throwSearchParameterExceptionIfNull(searchParameter, parameterName, referencedByResourceTypeName, context);
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

                        isCanonical = Type.REFERENCE.equals(type) &&
                                isCanonicalSearchParm(resourceType, searchParameter.getExpression().getValue());
                        QueryParameter lastParameter = new QueryParameter(isCanonical ? Type.URI : type, parameterName, modifier,
                                modifierResourceTypeName, false, false, isCanonical);

                        // Process value string
                        List<QueryParameterValue> valueList = processQueryParameterValueString(referencedByResourceType, searchParameter,
                            modifier, modifierResourceTypeName, valuesString, isCanonical);

                        if (Type.REFERENCE == type && searchParameter.getTarget().size() > 1 && modifierResourceTypeName == null) {
                            // For last search parameter, if type REFERENCE and not scoped to single target resource type, check if
                            // value is logical ID only. If so, throw an exception.
                            checkQueryParameterValuesForLogicalIdOnly(parameterName, valueList, context);
                        }

                        lastParameter.getValues().addAll(valueList);
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
     * diacritics are removed. Consecutive whitespace characters are replaced with
     * a single space. And then the string is transformed to lower case.
     *
     * @param value the string to normalize
     * @return the normalized string
     */
    public static String normalizeForSearch(String value) {

        String normalizedValue = null;
        if (value != null) {
            normalizedValue = Normalizer.normalize(value, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            normalizedValue = normalizedValue.replaceAll("\\s+", " ").toLowerCase();
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
     * @throws Exception
     */
    private static void parseInclusionParameter(Class<?> resourceType, FHIRSearchContext context, String inclusionKeyword, List<String> inclusionValues) throws Exception {

        String[] inclusionValueParts;
        String joinResourceType;
        String searchParameterName;
        String resourceTypeAndParameterName;
        String searchParameterTargetType;

        List<String> allowedIncludes = FHIRConfigHelper.getSearchPropertyRestrictions(resourceType.getSimpleName(), FHIRConfigHelper.SEARCH_PROPERTY_TYPE_INCLUDE);
        List<String> allowedRevIncludes = FHIRConfigHelper.getSearchPropertyRestrictions(resourceType.getSimpleName(), FHIRConfigHelper.SEARCH_PROPERTY_TYPE_REVINCLUDE);

        // Parse inclusionKeyword into parameter name and modifier (if present).
        Modifier modifier = null;
        int index = inclusionKeyword.indexOf(SearchConstants.COLON_DELIMITER_STR);
        if (index != -1) {
            String modifierString = inclusionKeyword.substring(index + 1);
            String parameterName = inclusionKeyword.substring(0, index);
            try {
                modifier = Modifier.fromValue(modifierString);
            } catch (IllegalArgumentException e) {}
            if (!Modifier.ITERATE.equals(modifier)) {
                // Invalid modifier
                manageException("Modifier ':" + modifierString + "' is not valid for " + parameterName, IssueType.INVALID, context, true);
                return;
            }
            inclusionKeyword = parameterName;
        }

        for (String inclusionValue : inclusionValues) {

            try {
                // Parse value into 3 parts: joinResourceType, searchParameterName, searchParameterTargetType
                inclusionValueParts = inclusionValue.split(":");
                if (inclusionValueParts.length < 2) {
                    manageException("A value for _include or _revinclude must have at least 2 parts separated by a colon.", IssueType.INVALID, context, true);
                    continue;
                }
                joinResourceType = inclusionValueParts[0];
                searchParameterName = inclusionValueParts[1];
                resourceTypeAndParameterName = joinResourceType + ":" + searchParameterName;
                searchParameterTargetType = inclusionValueParts.length == 3 ? inclusionValueParts[2] : null;

                if (SearchConstants.INCLUDE.equals(inclusionKeyword)) {

                    // For _include parameter, join resource type must match resource type being searched.
                    // For :iterate, we'll check after all include/revinclude parameters have been parsed.
                    if (!Modifier.ITERATE.equals(modifier) && !joinResourceType.equals(resourceType.getSimpleName())) {
                        manageException("The join resource type must match the resource type being searched.", IssueType.INVALID, context, true);
                        continue;
                    }

                    // Check allowed _include values
                    if (allowedIncludes != null && !allowedIncludes.contains(inclusionValue) && !allowedIncludes.contains(resourceTypeAndParameterName)) {
                        manageException("'" + inclusionValue + "' is not a valid _include parameter value for resource type '"
                                + resourceType.getSimpleName() + "'", IssueType.INVALID, context, true);
                        continue;
                    }
                }

                if (SearchConstants.REVINCLUDE.equals(inclusionKeyword)) {

                    // For _revinclude parameter, join resource type must be valid resource type
                    if (!ModelSupport.isResourceType(joinResourceType)) {
                        manageException("'" + joinResourceType + "' is not a valid resource type.", IssueType.INVALID, context, true);
                        continue;
                    }

                    // For _revinclude parameter, target resource type, if specified, must match resource type being searched.
                    // For :iterate, we'll check after all include/revinclude parameters have been parsed.
                    if (!Modifier.ITERATE.equals(modifier) && searchParameterTargetType != null && !searchParameterTargetType.equals(resourceType.getSimpleName())) {
                        manageException("The search parameter target type must match the resource type being searched.", IssueType.INVALID, context, true);
                        continue;
                    }

                    // Check allowed _revinclude values
                    if (allowedRevIncludes != null && !allowedRevIncludes.contains(inclusionValue) && !allowedRevIncludes.contains(resourceTypeAndParameterName)) {
                        manageException("'" + inclusionValue + "' is not a valid _revinclude parameter value for resource type '"
                                + resourceType.getSimpleName() + "'", IssueType.INVALID, context, true);
                        continue;
                    }
                }

                // Ensure that the Inclusion Parameter being parsed is a valid search parameter of type 'reference'.
                Map<String, SearchParameter> searchParametersMap;
                if (SearchConstants.WILDCARD.equals(searchParameterName)) {
                    searchParametersMap = getInclusionWildcardSearchParameters(resourceType.getSimpleName(), joinResourceType,
                        searchParameterTargetType, inclusionKeyword, modifier);
                    if (searchParametersMap.isEmpty()) {
                        log.fine("No valid inclusion parameters found for wildcard search.");
                    }
                } else {
                    SearchParameter searchParm = getSearchParameter(joinResourceType, searchParameterName);
                    if (searchParm == null) {
                        manageException("Undefined Inclusion Parameter: " + inclusionValue, IssueType.INVALID, context, true);
                        continue;
                    }
                    if (!SearchParamType.REFERENCE.equals(searchParm.getType())) {
                        manageException("Inclusion Parameter must be of type 'reference'. The passed Inclusion Parameter is of type '"
                                + searchParm.getType().getValue() + "': " + inclusionValue, IssueType.INVALID, context, true);
                        continue;
                    }
                    if (searchParameterTargetType != null && !isValidTargetType(searchParameterTargetType, searchParm)) {
                        manageException("Search parameter target type '" + searchParameterTargetType +
                            "' is not valid for inclusion search parameter '" + searchParm.getCode() + "'", IssueType.INVALID, context, true);
                        continue;
                    }
                    searchParametersMap = Collections.singletonMap(searchParameterName, searchParm);
                }

                List<InclusionParameter> inclusionParameters = new ArrayList<>();
                for (SearchParameter searchParameter : searchParametersMap.values()) {
                    inclusionParameters.addAll(buildInclusionParameters(resourceType, joinResourceType,
                        searchParameter, searchParameterTargetType, modifier, inclusionKeyword, context));
                }
                if (SearchConstants.INCLUDE.equals(inclusionKeyword)) {
                    context.getIncludeParameters().addAll(inclusionParameters);
                } else {
                    context.getRevIncludeParameters().addAll(inclusionParameters);
                }
            } catch (FHIRSearchException se) {
                // There's a number of places that throw within this try block. In all cases we want the same behavior:
                // If we're in lenient mode and there was an issue parsing the inclusion parameter then log and move on to the next one.
                if (context.isLenient()) {
                    String msg = "Inclusion parameter '" + inclusionKeyword + "' with value '" + inclusionValue + "' ignored";
                    log.log(Level.FINE, msg, se);
                    context.addOutcomeIssue(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.WARNING, IssueType.INVALID, msg));
                } else {
                    throw se;
                }
            }
        }
    }

    /**
     * Builds and returns a collection of InclusionParameter objects representing
     * occurrences of the _include or _revinclude search result parameter in the query string.
     *
     * @param resourceType the primary search parameter resource type
     * @param joinResourceType the inclusion parameter join resource type
     * @param searchParm the inclusion search parameter
     * @param searchParameterTargetType the inclusion parameter target resource type
     * @param modifier the inclusion keyword modifier
     * @param inclusionKeyword the inclusion keyword (_include or _revinclude)
     * @param FHIRSearchContext the context
     * @return a list of InclusionParameter objects
     * @throws FHIRSearchException
     */
    private static List<InclusionParameter> buildInclusionParameters(Class<?> resourceType, String joinResourceType,
        SearchParameter searchParm, String searchParameterTargetType, Modifier modifier, String inclusionKeyword,
        FHIRSearchContext context) throws FHIRSearchException {

        List<InclusionParameter> inclusionParameters = new ArrayList<>();
        String searchParameterCode = searchParm.getCode().getValue();
        boolean isCanonical = isCanonicalSearchParm(resourceType, searchParm.getExpression().getValue());

        // If no searchParameterTargetType was specified, and if an :iterate parameter, create
        // an InclusionParameter instance for each of the search parameter's defined target types.
        if (searchParameterTargetType == null) {
            // If no searchParameterTargetType was specified, determine what to set
            if (SearchConstants.INCLUDE.equals(inclusionKeyword) || Modifier.ITERATE.equals(modifier)) {
                boolean userSpecified = (SearchConstants.INCLUDE.equals(inclusionKeyword) || searchParm.getTarget().size() > 1) ? false : true;
                // Create an InclusionParameter instance for each of the search parameter's defined target types
                for (Code targetType : searchParm.getTarget()) {
                    inclusionParameters.add(new InclusionParameter(
                        joinResourceType, searchParameterCode, targetType.getValue(), modifier, userSpecified, isCanonical));
                }
            } else {
                // This is a _revinclude without :iterate. If the primary search resource type is a valid target
                // type for the search parameter, create a single InclusionParameter instance with
                // searchParameterTargetType set to the primary search resource type.
                if (isValidTargetType(resourceType.getSimpleName(), searchParm)) {
                    inclusionParameters.add(new InclusionParameter(
                        joinResourceType, searchParameterCode, resourceType.getSimpleName(), modifier, false, isCanonical));
                } else {
                    manageException(INVALID_TARGET_TYPE_EXCEPTION, IssueType.INVALID, context, true);
                }
            }
        } else {
            // Create a single InclusionParameter instance using the specified searchParameterTargetType
            inclusionParameters.add(new InclusionParameter(
                joinResourceType, searchParameterCode, searchParameterTargetType, modifier, true, isCanonical));
        }
        return inclusionParameters;
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
     * @param resourceType the resource type
     * @param context the search context
     * @param elements the elements
     * @throws Exception
     */
    private static void parseElementsParameter(Class<?> resourceType, FHIRSearchContext context,
            String elements) throws Exception {

        Set<String> resourceFieldNames = JsonSupport.getElementNames(resourceType);

        // For other parameters, we pass the comma-separated list of values to the PL
        // but for elements, we need to process that here
        for (String elementName : elements.split(SearchConstants.BACKSLASH_NEGATIVE_LOOKBEHIND + ",")) {
            try {
                if (elementName.startsWith("_")) {
                    throw SearchExceptionUtil.buildNewInvalidSearchException("Invalid element name: " + elementName);
                }
                if (!resourceFieldNames.contains(elementName)) {
                    manageException("Unknown element name: " + elementName, IssueType.INVALID, context, true);
                    continue;
                }
                context.addElementsParameter(elementName);
            } catch (FHIRSearchException se) {
                // If we're in lenient mode and there was an issue parsing the element then log and move on to the next one.
                if (context.isLenient()) {
                    String msg = "Element name '" + elementName + "' for resource type '" + resourceType.getSimpleName() + "' ignored";
                    log.log(Level.FINE, msg, se);
                    context.addOutcomeIssue(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.WARNING, IssueType.INCOMPLETE, msg));
                } else {
                    throw se;
                }
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

    /**
     * Either throw a FHIRSearchException or log the error message, depending on if
     * we are in strict or lenient mode.
     *
     * @param message
     *     The error message.
     * @param issueType
     *     The issue type.
     * @param context
     *     The search context.
     * @param alwaysThrow
     *     Determine whether exception should be thrown even if in lenient mode.
     * @throws FHIRSearchException
     */
    private static void manageException(String message, IssueType issueType, FHIRSearchContext context, boolean alwaysThrow) throws FHIRSearchException {
        if (context.isLenient()) {
            log.fine(message);
            context.addOutcomeIssue(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.WARNING, issueType, message));
        }
        if (!context.isLenient() || alwaysThrow) {
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
                            String compartmentName = null;
                            String compartmentId = null;

                            Element element = node.asElementNode().element();
                            if (element.is(Reference.class)) {
                                Reference reference = element.as(Reference.class);
                                ReferenceValue rv = ReferenceUtil.createReferenceValueFrom(reference, baseUrl);
                                if (rv.getType() == ReferenceType.DISPLAY_ONLY || rv.getType() == ReferenceType.INVALID) {
                                    if (log.isLoggable(Level.FINE)) {
                                        log.fine("Skipping reference of type " + rv.getType());
                                    }
                                    continue;
                                }
                                compartmentName = rv.getTargetResourceType();
                                compartmentId = rv.getValue();

                                // Check that the target resource type of the reference matches one of the
                                // target resource types in the compartment definition.
                                if (!paramEntry.getValue().contains(compartmentName)) {
                                    if (log.isLoggable(Level.FINE)) {
                                        String refVal = (reference.getReference() == null) ? null : reference.getReference().getValue();
                                        log.fine("Skipping reference with value " + refVal + ";"
                                                + " target resource type does not match any of the allowed compartment types: " + paramEntry);
                                    }
                                    continue;
                                }
                            } else if (element.is(FHIR_STRING)) {
                                if (paramEntry.getValue().size() != 1) {
                                    log.warning("CompartmentDefinition inclusion criteria must be of type Reference unless they have 1 and only 1 resource target");
                                    continue;
                                }
                                compartmentName = paramEntry.getValue().iterator().next();
                                compartmentId = element.as(FHIR_STRING).getValue();
                            }

                            // Add this reference to the set of references we're collecting for each compartment
                            CompartmentReference cref = new CompartmentReference(searchParm, compartmentName, compartmentId);
                            Set<CompartmentReference> references = result.computeIfAbsent(compartmentName, k -> new HashSet<>());
                            references.add(cref);
                        }
                    } else if (!useStoredCompartmentParam()) {
                       log.warning("Compartment parameter not found: [" + resourceType + "] '" + searchParm + "'. "
                               + "This will stop compartment searches from working correctly.");
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
     * @param context         the context
     * @throws FHIRSearchException
     */
    private static void throwSearchParameterExceptionIfNull(SearchParameter searchParameter, String parameterCode, String resourceType, FHIRSearchContext context) throws FHIRSearchException {
        if (searchParameter == null) {
            String msg = String.format(SEARCH_PARAMETER_NOT_FOUND, parameterCode, resourceType);
            if (context.isLenient()) {
                log.fine(msg);
                context.addOutcomeIssue(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.WARNING, IssueType.INVALID, msg));
            }
            throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
        }
    }

    /**
     * Throw an exception if a logical ID-only value is found in the list of values.
     *
     * @param parameterCode the search parameter code
     * @param values        the list of parameter values to check
     * @param context       the context
     * @throws FHIRSearchException
     */
    private static void checkQueryParameterValuesForLogicalIdOnly(String parameterCode, List<QueryParameterValue> values, FHIRSearchContext context) throws FHIRSearchException {
        for (QueryParameterValue value : values) {
            ReferenceValue refVal = ReferenceUtil.createReferenceValueFrom(value.getValueString(), null, ReferenceUtil.getBaseUrl(null));
            if (refVal.getType() == ReferenceType.LITERAL_RELATIVE && refVal.getTargetResourceType() == null) {
                String msg = String.format(LOGICAL_ID_VALUE_NOT_ALLOWED_FOR_REFERENCE_SEARCH, parameterCode, value.getValueString());
                if (context.isLenient()) {
                    log.fine(msg);
                    context.addOutcomeIssue(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.WARNING, IssueType.INVALID, msg));
                }
                throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
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

    /**
     * Check if the list of search parameters contains either _include or _revinclude.
     *
     * @param searchParameterCodes the set of search parameters to check
     * @return true if either _include or _revinclude found, false otherwise
     */
    public static boolean containsInclusionParameter(Set<String> searchParameterCodes) {
        for (String searchParameterCode : searchParameterCodes) {
            if (SearchConstants.INCLUDE.equals(searchParameterCode) ||
                    searchParameterCode.startsWith(SearchConstants.INCLUDE + SearchConstants.COLON_DELIMITER_STR) ||
                    SearchConstants.REVINCLUDE.equals(searchParameterCode) ||
                    searchParameterCode.startsWith(SearchConstants.REVINCLUDE + SearchConstants.COLON_DELIMITER_STR)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if _include or _revinclude parameters with the :iterate modifier reference invalid resource types.
     * If in strict mode, throw a FHIRSearchException. If in lenient mode, log the invalid parameters and
     * remove those parameters from the search context.
     *
     * @param resourceType the search resource type
     * @param context the search context
     * @param lenient flag indicating lenient or strict mode
     * @throws FHIRSearchException
     */
    @SuppressWarnings("unused")
    public static void checkInclusionIterateParameters(String resourceType, FHIRSearchContext context, boolean lenient) throws FHIRSearchException {
        // Get the valid target types. If max number of iterations allowed is just 1, then that includes
        // base resource type and types in non-iterative _include and _revinclude parameters. If max number
        // of iterations allowed is more than 1, then that also includes types in iterative _include and
        // _revinclude parameters.
        Set<String> validJoinResourceTypesForInclude = new HashSet<>();
        Set<String> validTargetResourceTypesForRevInclude = new HashSet<>();
        validJoinResourceTypesForInclude.add(resourceType);
        validTargetResourceTypesForRevInclude.add(resourceType);
        context.getIncludeParameters().stream().filter(p -> !p.isIterate()).forEach(p -> {
            validJoinResourceTypesForInclude.add(p.getSearchParameterTargetType());
            validTargetResourceTypesForRevInclude.add(p.getSearchParameterTargetType());
        });
        context.getRevIncludeParameters().stream().filter(p -> !p.isIterate()).forEach(p -> {
            validJoinResourceTypesForInclude.add(p.getJoinResourceType());
            validTargetResourceTypesForRevInclude.add(p.getJoinResourceType());
        });
        // If we make maximum number of iterations configurable, we would need
        // to read the config here and replace MAX_INCLUSION_ITERATIONS with that value.
        if (SearchConstants.MAX_INCLUSION_ITERATIONS > 1) {
            context.getIncludeParameters().stream().filter(p -> p.isIterate()).forEach(p -> {
                validJoinResourceTypesForInclude.add(p.getSearchParameterTargetType());
                validTargetResourceTypesForRevInclude.add(p.getSearchParameterTargetType());
            });
            context.getRevIncludeParameters().stream().filter(p -> p.isIterate()).forEach(p -> {
                validJoinResourceTypesForInclude.add(p.getJoinResourceType());
                validTargetResourceTypesForRevInclude.add(p.getJoinResourceType());
            });
        }

        // Get the :iterate inclusion parameters that reference invalid resource types.
        // If in lenient mode, or if multiple added because no target type was specified,
        // log and remove. If in strict mode, or if only one valid target type, throw an exception.
        List<InclusionParameter> invalidIncludeParameters = new ArrayList<>();
        List<InclusionParameter> invalidRevIncludeParameters = new ArrayList<>();
        try {
            // _include parameters
            context.getIncludeParameters().stream().filter(p -> p.isIterate()).forEach(p -> {
                if (!validJoinResourceTypesForInclude.contains(p.getJoinResourceType())) {
                    String msg = "The join resource type '" + p.getJoinResourceType() +
                            "' for _include parameter '" + p.getSearchParameter() +
                            "' does not match a resource type being searched.";
                    if (lenient) {
                        log.fine(msg);
                    } else {
                        throw new RuntimeException(msg);
                    }
                    invalidIncludeParameters.add(p);
                }
            });
            // _revinclude parameters
            context.getRevIncludeParameters().stream().filter(p -> p.isIterate()).forEach(p -> {
                if (!validTargetResourceTypesForRevInclude.contains(p.getSearchParameterTargetType())) {
                    String msg = "The search parameter target type '" + p.getSearchParameterTargetType() +
                            "' for _revinclude parameter '" + p.getSearchParameter() +
                            "' does not match a resource type being searched.";
                    if (lenient || !p.isUserSpecifiedTargetType()) {
                        log.fine(msg);
                    } else {
                        throw new RuntimeException(msg);
                    }
                    invalidRevIncludeParameters.add(p);
                }
            });
        } catch (RuntimeException e) {
            throw SearchExceptionUtil.buildNewInvalidSearchException(e.getMessage());
        }

        // Remove invalid inclusion parameters
        context.getIncludeParameters().removeAll(invalidIncludeParameters);
        context.getRevIncludeParameters().removeAll(invalidRevIncludeParameters);
    }

    /**
     * Inspect the searchContext to see if the parameters define a compartment-based
     * search. This is useful to know because it allows an implementation to
     * enable optimizations specific to compartment-based searches.
     * @param searchContext
     * @return
     */
    public static boolean isCompartmentSearch(FHIRSearchContext searchContext) {
        boolean result = false;

        // The compartment query parameter should be the first, but we check
        // the whole list just to be sure
        for (QueryParameter qp: searchContext.getSearchParameters()) {
            if (qp.isInclusionCriteria()) {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * Determine if a search parameter expression references an element of type Canonical.
     * @param resourceType
     * @param expression
     * @return
     */
    private static boolean isCanonicalSearchParm(Class<?> resourceType, String expression) {
        // Parse the expression to determine element type it refers to.
        // First split by union operator.
        for (String subExpression : expression.split("\\|")) {
            Class<?> dataType = resourceType;

            // Strip any enclosing parens
            subExpression = subExpression.trim();
            if (subExpression.startsWith("(") && subExpression.endsWith(")")) {
                subExpression = subExpression.substring(1, subExpression.length()-1);
            }

            // Strip out '.where()' functions, handling nested parentheses
            subExpression = subExpression
                    .replaceAll("\\.where(?=\\()(?:(?=.*?\\((?!.*?\\1)(.*\\)(?!.*\\2).*))(?=.*?\\)(?!.*?\\2)(.*)).)+?.*?(?=\\1)[^(]*(?=\\2$)", "");

            // Split into components
            for (String component : subExpression.split("\\.")) {
                component = component.trim();
                if (dataType == null || !component.equals(dataType.getSimpleName())) {
                    if (component.contains(" as ")) {
                        dataType = ModelSupport.getDataType(component.substring(component.indexOf(" as ") + 4));
                    } else if (component.startsWith("as(")) {
                        dataType = ModelSupport.getDataType(component.substring(3, component.indexOf(")")).trim());
                    } else if (ModelSupport.isResourceType(component)) {
                        dataType = ModelSupport.getResourceType(component);
                    } else {
                        dataType = ModelSupport.getElementType(dataType, component);
                    }
                }
            }
            if (Canonical.class.getSimpleName().equals(dataType.getSimpleName())) {
                return true;
            }
        }
        return false;
    }
}

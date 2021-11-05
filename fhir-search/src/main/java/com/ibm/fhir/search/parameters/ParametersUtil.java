/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.parameters;

import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.config.PropertyGroup.PropertyEntry;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.exception.SearchExceptionUtil;

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

    public static final String FHIR_DEFAULT_SEARCH_PARAMETERS_FILE = "search-parameters.json";
    public static final String FROM_STEAM = "from_stream";

    private static final Set<String> ALL_RESOURCE_TYPES = ModelSupport.getResourceTypes(true).stream()
            .map(t -> ModelSupport.getTypeName(t))
            .collect(Collectors.toSet());

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

    private static final Map<String, Map<String, ParametersMap>> searchParameters = buildSearchParametersMap();

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
     * Loads search parameters from the registry and applies filtering to construct
     * a per-tenant mapping from resource type to ParametersMap.
     *
     * @return a tenant-aware map of maps from resourceType to ParametersMaps
     */
    private static Map<String, Map<String, ParametersMap>> buildSearchParametersMap() {
        HashMap<String, Map<String, ParametersMap>> result = new HashMap<>();

        FHIRConfiguration config = FHIRConfiguration.getInstance();

        String originalTenantId = FHIRRequestContext.get().getTenantId();
        List<String> configuredTenants = config.getConfiguredTenants();
        if (configuredTenants.isEmpty()) {
            try {
                result.put(originalTenantId, computeTenantSPs(null));
            } catch (Exception e) {
                log.log(Level.SEVERE, "Error while computing search parameters for missing config "
                        + "and default tenant " + originalTenantId, e);
            }
        } else {
            for (String tenant : config.getConfiguredTenants()) {
                try {
                    // Ensure we get the extension search parameters for the proper tenant
                    FHIRRequestContext.get().setTenantId(tenant);
                    if (log.isLoggable(Level.FINE)) {
                        log.fine(("Computing search parameters for tenant " + tenant));
                    }

                    PropertyGroup root = config.loadConfigurationForTenant(tenant);
                    PropertyGroup rsrcsGroup = root == null ? null : root.getPropertyGroup(FHIRConfiguration.PROPERTY_RESOURCES);
                    result.put(tenant, computeTenantSPs(rsrcsGroup));
                } catch (Exception e) {
                    log.log(Level.SEVERE, "Error while computing search parameters for tenant " + tenant, e);
                }
            }
            try {
                // Restore the original tenant id
                FHIRRequestContext.get().setTenantId(originalTenantId);
            } catch (FHIRException e) {
                log.log(Level.SEVERE, "Unexpected error while restoring the FHIRRequestContext tenant id to " + originalTenantId, e);
            }
        }

        return Collections.unmodifiableMap(result);
    }

    private static Map<String, ParametersMap> computeTenantSPs(PropertyGroup rsrcsGroup) throws Exception {
        Map<String, ParametersMap> paramMapsByType = new HashMap<>();
        Set<String> resourceTypesWithWildcardParams = new HashSet<>();
        Map<String, Set<String>> configuredCodes = new HashMap<>();
        boolean supportOmittedRsrcTypes = true;

        if (rsrcsGroup != null) {
            List<PropertyEntry> rsrcsEntries = rsrcsGroup.getProperties();
            if (rsrcsEntries != null && !rsrcsEntries.isEmpty()) {
                for (PropertyEntry rsrcsEntry : rsrcsEntries) {
                    ParametersMap paramMap = new ParametersMap();

                    // Check special property for including omitted resource types
                    if (FHIRConfiguration.PROPERTY_FIELD_RESOURCES_OPEN.equals(rsrcsEntry.getName())) {
                        if (rsrcsEntry.getValue() instanceof Boolean) {
                            supportOmittedRsrcTypes = (Boolean) rsrcsEntry.getValue();
                        } else {
                            throw SearchExceptionUtil.buildNewIllegalStateException();
                        }
                    } else {
                        String resourceType = rsrcsEntry.getName();
                        PropertyGroup resourceTypeGroup = (PropertyGroup) rsrcsEntry.getValue();
                        if (resourceTypeGroup != null) {
                            // Get search parameters
                            PropertyGroup spGroup = resourceTypeGroup.getPropertyGroup(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_SEARCH_PARAMETERS);
                            if (spGroup != null) {
                                List<PropertyEntry> spEntries = spGroup.getProperties();
                                if (spEntries != null && !spEntries.isEmpty()) {
                                    for (PropertyEntry spEntry : spEntries) {
                                        String code = spEntry.getName();
                                        if (SearchConstants.WILDCARD.equals(code)) {
                                            resourceTypesWithWildcardParams.add(resourceType);
                                        } else if (spEntry.getValue() instanceof String) {
                                            SearchParameter sp = FHIRRegistry.getInstance()
                                                    .getResource((String)spEntry.getValue(), SearchParameter.class);
                                            if (sp != null) {
                                                if (sp.getExpression() == null || !sp.getExpression().hasValue()) {
                                                    if (log.isLoggable(Level.FINE)) {
                                                        log.fine(String.format(MISSING_EXPRESSION_WARNING, sp.getCode().getValue()));
                                                    }
                                                } else {
                                                    paramMap.insert(code, sp);
                                                    configuredCodes.computeIfAbsent(resourceType, k -> new HashSet<>())
                                                            .add(code);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                resourceTypesWithWildcardParams.add(resourceType);
                            }
                        }
                        paramMapsByType.put(resourceType, paramMap);
                    }
                }
            }
        }

        if (supportOmittedRsrcTypes) {
            // All other resource types include all search parameters
            for (String resourceType : ALL_RESOURCE_TYPES) {
                if (!paramMapsByType.containsKey(resourceType)) {
                    resourceTypesWithWildcardParams.add(resourceType);
                }
            }
        }

        for (SearchParameter sp : getAllSearchParameters()) {
            for (ResourceType resourceType : sp.getBase()) {
                if (resourceTypesWithWildcardParams.contains(resourceType.getValue()) && sp.getCode().hasValue()) {
                    if (sp.getExpression() == null || !sp.getExpression().hasValue()) {
                        if (log.isLoggable(Level.FINE)) {
                            log.fine(String.format(MISSING_EXPRESSION_WARNING, sp.getCode().getValue()));
                        }
                    } else {
                        ParametersMap paramMap = paramMapsByType.get(resourceType.getValue());
                        if (paramMap == null) {
                            paramMap = new ParametersMap();
                            paramMapsByType.put(resourceType.getValue(), paramMap);
                        }
                        Set<String> configuredCodesForType = configuredCodes.get(resourceType.getValue());
                        if (configuredCodesForType != null && configuredCodesForType.contains(sp.getCode().getValue())) {
                            if (log.isLoggable(Level.FINE)) {
                                String url = sp.getUrl().getValue();
                                String version = (sp.getVersion() == null) ? null : sp.getVersion().getValue();
                                String canonical = (version == null) ? url : url + "|" + version;
                                log.fine("Skipping search parameter '" + canonical + "' because code '" +
                                        sp.getCode() + "' is already configured.");
                            }
                        } else {
                            paramMap.insert(sp.getCode().getValue(), sp);
                        }
                    }
                }
            }
        }

        return Collections.unmodifiableMap(paramMapsByType);
    }

    public static Map<String, ParametersMap> getTenantSPs(String tenant) {
        return searchParameters.get(tenant);
    }

    public static Set<SearchParameter> getAllSearchParameters() {
        Set<SearchParameter> searchParameters = new LinkedHashSet<>(2048);
        for (SearchParamType.Value searchParamType : SearchParamType.Value.values()) {

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
                    map.insert(code, parameter);
                }
            }
        }

        // Return an unmodifiable copy, lest there be side effects.
        return Collections.unmodifiableMap(typeToParamMap);
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
     * convenience method to print the output of the Search Parameters.
     *
     * @param out
     */
    public static void print(PrintStream out) {
        for (String tenant : searchParameters.keySet()) {
            out.println(SearchConstants.LOG_BOUNDARY);
            out.println("- TENANT " + tenant);
            print(out, searchParameters.get(tenant));
        }
    }

    /**
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

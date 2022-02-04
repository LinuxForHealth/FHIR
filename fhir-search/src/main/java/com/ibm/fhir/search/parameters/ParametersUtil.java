/*
 * (C) Copyright IBM Corp. 2019, 2021
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
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
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
    public static final String LOG_HEADER = "BASE:RESOURCE_NAME:SearchParameter";
    public static final String LOG_SIZE = "Size: %s";
    private static final String LOG_OUTPUT = "%s|%s|%s";

    private static final String LEFT = "[";
    private static final String RIGHT = "]";
    private static final String COMMA = ",";
    private static final String EQUALS = "=";

    private static final Map<String, Map<String, ParametersMap>> searchParameters = buildSearchParameterMaps();

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
     * @return a tenant-keyed map of maps from resourceType to ParametersMap
     */
    private static Map<String, Map<String, ParametersMap>> buildSearchParameterMaps() {
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
            for (String tenant : configuredTenants) {
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

        // The set of resourceTypes with implicit or explicit wildcard (* = *) entries.
        // This is the set we'll need to revisit after processing all the configured codes.
        Set<String> resourceTypesWithWildcardParams = new HashSet<>();

        // The set of explicitly-configured codes for each resource type.
        Map<String, Set<String>> configuredCodes = new HashMap<>();

        boolean supportOmittedRsrcTypes = true;

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
                    } else {
                        String resourceType = rsrcsEntry.getName();
                        ParametersMap paramMap = getExplicitParams(resourceTypesWithWildcardParams, rsrcsEntry);
                        configuredCodes.put(resourceType, paramMap.getCodes());
                        paramMapsByType.put(resourceType, paramMap);
                    }
                }
            }
        }

        if (supportOmittedRsrcTypes) {
            // All other resource types include all search parameters (the default)
            for (String resourceType : ALL_RESOURCE_TYPES) {
                if (!paramMapsByType.containsKey(resourceType)) {
                    resourceTypesWithWildcardParams.add(resourceType);
                }
            }
        }

        addWildcardParams(paramMapsByType, resourceTypesWithWildcardParams, configuredCodes);

        return Collections.unmodifiableMap(paramMapsByType);
    }

    private static ParametersMap getExplicitParams(Set<String> resourceTypesWithWildcardParams,
            PropertyEntry rsrcsEntry) throws Exception {
        ParametersMap paramMap = new ParametersMap();
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
                                paramMap.insert(code, sp);
                            } else {
                                log.warning("Search parameter '" + code + "' with the configured url '" + spEntry.getValue() + "' for resourceType '" + resourceType + "' could not be found.");
                            }
                        }
                    }
                }
            } else {
                resourceTypesWithWildcardParams.add(resourceType);
            }
        }

        return paramMap;
    }

    private static void addWildcardParams(Map<String, ParametersMap> paramMapsByType,
            Set<String> resourceTypesWithWildcardParams, Map<String, Set<String>> configuredCodes) {

        for (SearchParameter sp : getAllSearchParameters()) {
            // For each resource type this search parameter applies to
            for (ResourceTypeCode resourceType : sp.getBase()) {

                // If this resource type includes a wildcard entry (i.e. includes codes that weren't explicitly configured)
                if (resourceTypesWithWildcardParams.contains(resourceType.getValue()) && sp.getCode().hasValue()) {
                    ParametersMap paramMap = paramMapsByType.get(resourceType.getValue());
                    if (paramMap == null) {
                        paramMap = new ParametersMap();
                        paramMapsByType.put(resourceType.getValue(), paramMap);
                    }

                    // Only add it if the code wasn't explicitly configured in fhir-server-config
                    Set<String> configuredCodesForType = configuredCodes.get(resourceType.getValue());
                    if (configuredCodesForType != null && configuredCodesForType.contains(sp.getCode().getValue())) {
                        if (log.isLoggable(Level.FINE)) {
                            String canonical = getCanonicalUrl(sp);
                            log.fine("Skipping search parameter '" + canonical + "' because code '" +
                                    sp.getCode().getValue() + "' is already configured.");
                        }
                    } else {
                        paramMap.insert(sp.getCode().getValue(), sp);
                    }
                }

            }
        }
    }

    /**
     * Construct the canonical URL from the SearchParameter's url and version
     *
     * @return the url of this search parameter with a version postfix (if version is non-null)
     */
    public static String getCanonicalUrl(SearchParameter sp) {
        String url = sp.getUrl().getValue();
        String version = (sp.getVersion() == null) ? null : sp.getVersion().getValue();
        return (version == null) ? url : url + "|" + version;
    }

    /**
     * Get the applicable SearchParameter objects for a particular tenant.
     *
     * @param tenantId
     * @return a set of ParametersMaps, organized by resource type; never null
     */
    public static Map<String, ParametersMap> getTenantSPs(String tenantId) {
        if (searchParameters.containsKey(tenantId)) {
            return searchParameters.get(tenantId);
        } else {
            log.warning("No search parameter configuration was loaded for tenant " + tenantId);
            return new HashMap<>();
        }
    }

    /**
     * Get all search parameters of all types from the registry.
     *
     * @return
     */
    public static Set<SearchParameter> getAllSearchParameters() {
        Set<SearchParameter> searchParameters = new LinkedHashSet<>(2048);
        for (SearchParamType.Value searchParamType : SearchParamType.Value.values()) {
            searchParameters.addAll(FHIRRegistry.getInstance().getSearchParameters(searchParamType.value()));
        }
        return searchParameters;
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
        List<ResourceTypeCode> types = parameter.getBase();

        StringBuilder builder = new StringBuilder();
        builder.append(code);
        builder.append(EQUALS);
        builder.append(LEFT);
        for (ResourceTypeCode type : types) {
            builder.append(type.getValue());
            builder.append(COMMA);
        }
        builder.append(RIGHT);
        out.println(builder.toString());
    }
}

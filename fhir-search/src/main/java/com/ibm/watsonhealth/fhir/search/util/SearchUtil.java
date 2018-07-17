/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.util;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.getResourceType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.xml.bind.Binder;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.config.PropertyGroup;
import com.ibm.watsonhealth.fhir.config.PropertyGroup.PropertyEntry;
import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleEntry;
import com.ibm.watsonhealth.fhir.model.Code;
import com.ibm.watsonhealth.fhir.model.IssueTypeList;
import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.SearchParameter;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;
import com.ibm.watsonhealth.fhir.search.InclusionParameter;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.Parameter.Modifier;
import com.ibm.watsonhealth.fhir.search.Parameter.Type;
import com.ibm.watsonhealth.fhir.search.ParameterValue;
import com.ibm.watsonhealth.fhir.search.ParameterValue.Prefix;
import com.ibm.watsonhealth.fhir.search.SortParameter;
import com.ibm.watsonhealth.fhir.search.SortParameter.SortDirection;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContextFactory;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;

public class SearchUtil {
    private static final String CLASSNAME = SearchUtil.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    // This constant represents the maximum _count parameter value.
    // If the user specifies a value greater than this, we'll just use this value instead.
    // In the future, we might want to make this value configurable.
    private static final int MAX_PAGE_SIZE = 1000;

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
    private static TenantSpecificSearchParameterCache searchParameterCache = null;
    private static Map<String, Map<String, SearchParameter>> builtInSearchParameters = null;
    private static Map<Type, List<Modifier>> resourceTypeModifierMap = null;

    private static Map<String, Map<String, List<String>>> compartmentMap = null;

    static {
        System.setProperty("com.sun.org.apache.xml.internal.dtm.DTMManager", "com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault");
        initializeCompartmentMap();
        initializeSearchParameterMap();
        initializedResourceTypeModifierMap();
    }

    private static final ThreadLocal<XPathFactory> threadLocalXPathFactory = new ThreadLocal<XPathFactory>() {
        @Override
        protected XPathFactory initialValue() {
            return XPathFactory.newInstance();
        }
    };

    private static final List<String> SEARCH_RESULT_PARAMETER_NAMES = Arrays.asList("_sort", "_sort:asc", "_sort:desc", "_count", "_page", "_include", "_revinclude", "_elements");
    private static final List<String> SYSTEM_LEVEL_SORT_PARAMETER_NAMES = Arrays.asList("_id", "_lastUpdated");

    private static void initializeCompartmentMap() {
        try {
            compartmentMap = buildCompartmentMap();
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    private SearchUtil() {
    }

    private static void initializeSearchParameterMap() {
        try {
            searchParameterCache = new TenantSpecificSearchParameterCache();
            builtInSearchParameters = populateSearchParameterMapFromResource("search-parameters.xml");
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    private static void initializedResourceTypeModifierMap() {

        resourceTypeModifierMap = new HashMap<Type, List<Modifier>>();
        resourceTypeModifierMap.put(Type.STRING, Arrays.asList(Modifier.EXACT, Modifier.CONTAINS));
        resourceTypeModifierMap.put(Type.REFERENCE, Arrays.asList(Modifier.TYPE));
        resourceTypeModifierMap.put(Type.URI, Arrays.asList(Modifier.BELOW));
        resourceTypeModifierMap.put(Type.TOKEN, Arrays.asList(Modifier.BELOW, Modifier.NOT));

    }

    /**
     * Builds an in-memory model of the Compartment map defined in compartments.json, for supporting compartment based
     * FHIR searches.
     *
     * @return Map<String, Map<String, List<String>>>
     * @throws IOException
     */
    private static Map<String, Map<String, List<String>>> buildCompartmentMap() throws IOException {

        Map<String, Map<String, List<String>>> compartmentMap = new HashMap<String, Map<String, List<String>>>();
        String jsonCompartmentString;
        JsonObject jsonCompartmentDataRoot;
        JsonArray jsonCompartments;
        JsonObject jsonCompartment;
        JsonObject jsonResource;
        JsonArray jsonCompartmentResources;
        JsonArray jsonInclusionCriteria;
        String jsonInclusionCriterion;
        String compartmentName;
        String resourceName;
        List<String> criteriaList;

        // Read the json file containing the compartment data and turn it into a JsonObject.
        try (InputStream stream = SearchUtil.class.getClassLoader().getResourceAsStream("compartments.json");
                InputStreamReader isr = new InputStreamReader(stream);
                BufferedReader reader = new BufferedReader(isr)) {
            jsonCompartmentString = reader.lines().collect(Collectors.joining(System.getProperty("line.separator")));
        }

        JsonReaderFactory factory = Json.createReaderFactory(null);

        try (StringReader stringReader = new StringReader(jsonCompartmentString); JsonReader jsonReader = factory.createReader(stringReader);) {
            jsonCompartmentDataRoot = jsonReader.readObject();
        }

        // Iterate over the comparments, resources, and includsion criteria to populate
        // the compartment map.
        jsonCompartments = jsonCompartmentDataRoot.getJsonArray("compartments");
        for (int i = 0; i < jsonCompartments.size(); i++) {
            jsonCompartment = (JsonObject) jsonCompartments.get(i);
            compartmentName = jsonCompartment.getString("name");
            compartmentMap.put(compartmentName, new TreeMap<String, List<String>>());
            jsonCompartmentResources = jsonCompartment.getJsonArray("resources");
            for (int j = 0; j < jsonCompartmentResources.size(); j++) {
                jsonResource = (JsonObject) jsonCompartmentResources.get(j);
                resourceName = jsonResource.getString("name");
                jsonInclusionCriteria = jsonResource.getJsonArray("inclusionCriteria");
                criteriaList = new ArrayList<>();
                for (int k = 0; k < jsonInclusionCriteria.size(); k++) {
                    jsonInclusionCriterion = jsonInclusionCriteria.getString(k);
                    criteriaList.add(jsonInclusionCriterion);
                }
                compartmentMap.get(compartmentName).put(resourceName, criteriaList);
            }
        }

        return compartmentMap;
    }

    public static List<String> getCompartmentResourceTypes(String compartment) throws FHIRSearchException {
        if (compartmentMap.get(compartment) == null) {
            throw buildInvalidSearchException("Invalid compartment: " + compartment);
        }
        return Collections.unmodifiableList(new ArrayList<String>(compartmentMap.get(compartment).keySet()));
    }

    private static FHIRSearchException buildInvalidSearchException(String msg) throws FHIRSearchException {
        OperationOutcomeIssue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, IssueTypeList.INVALID);
        return new FHIRSearchException(msg).withIssue(ooi);
    }

    public static List<String> getCompartmentResourceTypeInclusionCriteria(String compartment, String resourceType) throws FHIRSearchException {
        if (compartmentMap.get(compartment) == null) {
            String msg = "Invalid compartment: " + compartment;
            throw buildInvalidSearchException(msg);
        }
        if (compartmentMap.get(compartment).get(resourceType) == null) {
            String msg = "Invalid resource type: " + resourceType + " for compartment: " + compartment;
            throw buildInvalidSearchException(msg);
        }
        return Collections.unmodifiableList(compartmentMap.get(compartment).get(resourceType));
    }

    public static void init() {
        // allows us to initialize this class during startup
    }

    private static XPath createXPath() {
        XPath xp = threadLocalXPathFactory.get().newXPath();
        xp.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                if ("f".equals(prefix)) {
                    return "http://hl7.org/fhir";
                }
                return null;
            }

            @Override
            public String getPrefix(String namespaceURI) {
                if ("http://hl7.org/fhir".equals(namespaceURI)) {
                    return "f";
                }
                return null;
            }

            @Override
            public Iterator<String> getPrefixes(String namespaceURI) {
                return null;
            }
        });
        return xp;
    }

    /**
     * Returns a Map containing the SearchParameters loaded from the specified File object.
     *
     * @param file
     *            a File object which represents the file from which the SearchParameters are to be loaded
     * @return the Map containing the SearchParameters
     * @throws JAXBException
     * @throws IOException
     */
    public static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromFile(File file) throws JAXBException, IOException {
        try (FileInputStream stream = new FileInputStream(file)) {
            return populateSearchParameterMapFromStream(stream);
        }
    }

    /**
     * Returns a Map containing the SearchParameters loaded from the specified classpath resource.
     *
     * @param resourceName
     *            the name of a resource available on the current classpath from which the SearchParameters are to be
     *            loaded
     * @return the Map containing the SearchParameters
     * @throws JAXBException
     */
    private static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromResource(String resourceName) throws Exception {
        try (InputStream stream = SearchUtil.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (stream == null) {
                throw new FileNotFoundException("Search parameter configuration file '" + resourceName + "' not found on classpath.");
            }
            return populateSearchParameterMapFromStream(stream);
        }
    }

    /**
     * Loads SearchParameters using the specified InputStream and returns a Map containing them.
     *
     * @param stream
     *            the InputStream from which to load the SearchParameters
     * @return
     * @throws JAXBException
     */
    private static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromStream(InputStream stream) throws JAXBException {
        Map<String, Map<String, SearchParameter>> searchParameterMap = new HashMap<>();
        Bundle bundle = FHIRUtil.read(Bundle.class, Format.XML, stream);
        for (BundleEntry entry : bundle.getEntry()) {
            ResourceContainer container = entry.getResource();
            SearchParameter parameter = container.getSearchParameter();
            if (parameter != null && parameter.getXpath() != null) {
                if (!FHIRUtil.isStandardResourceType(parameter.getBase().getValue())) {
                    transformParameter(parameter);
                }
                String base = parameter.getBase().getValue();
                Map<String, SearchParameter> map = searchParameterMap.get(base);
                if (map == null) {
                    map = new TreeMap<String, SearchParameter>();
                    searchParameterMap.put(base, map);
                }
                String name = parameter.getName().getValue();
                map.put(name, parameter);
            }
        }

        return searchParameterMap;
    }

    private static void transformParameter(SearchParameter parameter) {
        parameter.getBase().setValue("Basic");
        String path = parameter.getXpath().getValue();
        parameter.getXpath().setValue(transformPath(path));
    }

    private static String transformPath(String path) {
        StringBuffer result = new StringBuffer();
        result.append("f:Basic/");
        path = path.substring(path.indexOf("/") + 1);
        for (String component : path.split("/f:")) {
            String fieldName = component.replace("f:", "");
            result.append("f:extension[@url='http://ibm.com/watsonhealth/fhir/extension/" + fieldName + "']");
            result.append("//");
        }
        int index = result.lastIndexOf("//");
        if (index != -1) {
            result.replace(index, index + 2, "");
        }
        return result.toString();
    }

    /**
     * Returns the list of search parameters for the specified resource type and the current tenant.
     *
     * @param resourceType
     *            a Class representing the resource type associated with the search parameters to be returned.
     * @throws Exception
     */
    public static List<SearchParameter> getSearchParameters(Class<? extends Resource> resourceType) throws Exception {
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
        // log.entering(CLASSNAME, "getSearchParameters(String)");
        try {
            String tenantId = FHIRRequestContext.get().getTenantId();
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Retrieving SearchParameters for tenant-id '" + tenantId + "' and resource type '" + resourceType + "'.");
            }
            List<SearchParameter> result = new ArrayList<>();

            // First retrieve built-in search parameters for this resource type and add them to the result.
            // We'll filter these built-in search parameters to include only the ones
            // specified by the tenant's filtering (inclusion) rules.
            Map<String, Map<String, SearchParameter>> spBuiltin = getBuiltInSearchParameterMap();
            Map<String, SearchParameter> spMapResourceType = spBuiltin.get(resourceType);
            if (spMapResourceType != null && !spMapResourceType.isEmpty()) {
                // Retrieve the current tenant's search parameter filtering rules.
                Map<String, List<String>> filterRules = getFilterRules();

                // Add only the "included" search parameters for this resource type to our result list.
                result.addAll(filterSearchParameters(filterRules, resourceType, spMapResourceType.values()));
            }

            // Next, retrieve the specified tenant's search parameters for this resource type and add those
            // to the result as well.
            result.addAll(getUserDefinedSearchParameters(resourceType));

            return result;
        } finally {
            // log.exiting(CLASSNAME, "getSearchparameters(String)");
        }
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
        Map<String, Map<String, SearchParameter>> spMapTenant = getTenantSPMap(tenantId);
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

        Map<String, Map<String, SearchParameter>> spBuiltin = getBuiltInSearchParameterMap();

        // Retrieve the current tenant's search parameter filtering rules.
        Map<String, List<String>> filterRules = getFilterRules();

        // Retrieve the SPs associated with the specified resource type and filter per the filter rules.
        Map<String, SearchParameter> spMap = spBuiltin.get(resourceType);
        if (spMap != null && !spMap.isEmpty()) {
            result.addAll(filterSearchParameters(filterRules, resourceType, spMap.values()));
        }

        // Retrieve the SPs associated with the "Resource" resource type and filter per the filter rules.
        spMap = spBuiltin.get("Resource");
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
            includedSPs = filterRules.get("*");
        }

        // If we found a non-empty list of search parameter names to filter on,
        // then do the filtering. Otherwise, we're just going to return an empty list.
        if (includedSPs != null && !includedSPs.isEmpty()) {
            // If "*" is contained in the included SP names, then we can just return the unfiltered list
            // now, since everything in the list will be included anyway.
            if (includedSPs.contains("*")) {
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
                            throw new IllegalStateException("SearchParameter filter property values must be an array of String.");
                        }
                    }

                    // Add the rule entry to our map, keyed by resource type.
                    List<String> stringList = (List<String>) ruleEntry.getValue();
                    result.put(resourceType, stringList);
                } else {
                    throw new IllegalStateException("SearchParameter filter property values must be an array of String.");
                }
            }
        } else {
            // The current tenant doesn't have any filter rules defined, so
            // we'll just fabricate one that includes all search parameters:
            // { "*": ["*"] }
            List<String> list = new ArrayList<>();
            list.add("*");
            result.put("*", list);
        }
        return result;
    }

    /**
     * This is a convenience function that simply returns the built-in (spec-defined) SearchParameters as a map keyed by
     * resource type.
     */
    private static Map<String, Map<String, SearchParameter>> getBuiltInSearchParameterMap() {
        return builtInSearchParameters;
    }

    /**
     * Returns the SearchParameter map (keyed by resource type) for the specified tenant-id, or null if there are no
     * SearchParameters for the tenant.
     *
     * @param tenantId
     *            the tenant-id whose SearchParameters should be returned.
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    private static Map<String, Map<String, SearchParameter>> getTenantSPMap(String tenantId) throws Exception {
        if (log.isLoggable(Level.FINEST)) {
            log.entering(CLASSNAME, "getTenantSPMap", new Object[] {
                    tenantId
            });
        }
        try {
            return searchParameterCache.getCachedObjectForTenant(tenantId);
        } finally {
            if (log.isLoggable(Level.FINEST)) {
                log.exiting(CLASSNAME, "getTenantSPMap");
            }
        }
    }

    public static SearchParameter getSearchParameter(Class<? extends Resource> resourceType, String name) throws Exception {
        return getSearchParameter(resourceType.getSimpleName(), name);
    }

    public static SearchParameter getSearchParameter(String resourceType, String name) throws Exception {
        String tenantId = FHIRRequestContext.get().getTenantId();

        // First try to find the search parameter within the specified tenant's map.
        SearchParameter result = getSearchParameterInternal(getTenantSPMap(tenantId), resourceType, name, false);

        // If we didn't find it within the tenant's map, then look within the built-in map.
        if (result == null) {
            result = getSearchParameterInternal(getBuiltInSearchParameterMap(), resourceType, name, true);

            // If we found it within the built-in search parameters, apply our filtering rules.
            if (result != null) {
                Collection<SearchParameter> filteredResult =
                        filterSearchParameters(getFilterRules(), result.getBase().getValue(), Collections.singleton(result));

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
                resourceTypeMap = tenantMap.get("Resource");
                if (resourceTypeMap != null) {
                    result = resourceTypeMap.get(name);
                }
            }
        }

        return result;
    }

    public static Set<Class<?>> getValueTypes(Class<? extends Resource> resourceType, String name) throws Exception {
        Set<Class<?>> valueTypes = new HashSet<Class<?>>();
        SearchParameter searchParameter = getSearchParameter(resourceType, name);
        if (searchParameter != null) {
            if (searchParameter.getXpath() != null) {
                String xpath = searchParameter.getXpath().getValue();
                for (String path : xpath.split("\\|")) {
                    path = path.trim();
                    Class<?> valueType = getValueType(resourceType, path);
                    if (valueType != null) {
                        valueTypes.add(valueType);
                    }
                }
            }
        }
        return valueTypes;
    }

    private static Class<?> getValueType(Class<? extends Resource> resourceType, String path) {
        Class<?> currentClass = resourceType;
        path = path.substring(path.indexOf("/") + 1);
        for (String component : path.split("/f:")) {
            if (component.contains("[")) {
                component = component.substring(0, component.indexOf("["));
            }
            String fieldName = component.replace("f:", "");
            if ("class".equals(fieldName)) {
                fieldName = "clazz";
            } else if ("package".equals(fieldName)) {
                fieldName = "_package";
            } else if ("abstract".equals(fieldName)) {
                fieldName = "_abstract";
            }
            Field field = getField(currentClass, fieldName);
            if (field != null) {
                Class<?> fieldType = field.getType();
                java.lang.reflect.Type genericType = field.getGenericType();
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    fieldType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                }
                currentClass = fieldType;
            } else {
                return null;
            }
        }
        return currentClass;
    }

    private static Field getField(Class<?> startingClass, String name) {
        Class<?> currentClass = startingClass;
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
            }
            currentClass = currentClass.getSuperclass();
        }
        return null;
    }

    public static <T extends Resource> Map<SearchParameter, List<Object>> extractParameterValues(T resource) throws Exception {
        Map<SearchParameter, List<Object>> result = new TreeMap<SearchParameter, List<Object>>(new Comparator<SearchParameter>() {
            @Override
            public int compare(SearchParameter first, SearchParameter second) {
                return first.getName().getValue().compareTo(second.getName().getValue());
            }
        });

        Class<? extends Resource> resourceType = resource.getClass();

        Binder<Node> binder = FHIRUtil.createBinder(resource);
        Document document = binder.getXMLNode(resource).getOwnerDocument();

        List<SearchParameter> parameters = getApplicableSearchParameters(resourceType.getSimpleName());

        for (SearchParameter parameter : parameters) {
            if (parameter.getXpath() != null) {
                String xpath = parameter.getXpath().getValue();
                if (xpath.startsWith("f:Resource")) {
                    xpath = xpath.replaceFirst("f:Resource", "f:" + resource.getClass().getSimpleName());
                }
                XPathExpression expr = createXPath().compile(xpath);
                NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
                if (nodeList.getLength() > 0) {
                    List<Object> values = result.get(parameter);
                    if (values == null) {
                        values = new ArrayList<Object>();
                        result.put(parameter, values);
                    }
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        values.add(binder.getJAXBNode(nodeList.item(i)));
                    }
                }
            }
        }

        return result;
    }

    public static FHIRSearchContext parseQueryParameters(Class<? extends Resource> resourceType, Map<String, List<String>> queryParameters, String queryString)
        throws Exception {
        FHIRSearchContext context = FHIRSearchContextFactory.createSearchContext();
        List<Parameter> parameters = new ArrayList<Parameter>();

        // Retrieve the SearchParameters that will apply to this resource type (including those for Resource.class).
        Map<String, SearchParameter> applicableSPs = getApplicableSearchParametersMap(resourceType.getSimpleName());

        for (String name : queryParameters.keySet()) {
            try {
                if (queryParameterShouldBeExcluded(name) || isSearchResultParameter(name)) {
                    if (isSearchResultParameter(name)) {
                        parseSearchResultParameter(resourceType, context, name, queryParameters.get(name), queryString);
                    }
                    continue;
                }

                if (isChainedParameter(name)) {
                    List<String> chainedParemeters = queryParameters.get(name);
                    for (String chainedParameterString : chainedParemeters) {
                        Parameter chainedParameter = parseChainedParameter(resourceType, name, chainedParameterString);
                        parameters.add(chainedParameter);
                    }
                    continue;
                }

                // parse name into parameter name
                String parameterName = name;
                Modifier modifier = null;
                String modifierResourceTypeName = null;
                if (parameterName.contains(":")) {
                    String mod = parameterName.substring(parameterName.indexOf(":") + 1);
                    if (FHIRUtil.isStandardResourceType(mod)) {
                        modifier = Modifier.TYPE;
                        modifierResourceTypeName = mod;
                    } else {
                        try {
                            modifier = Modifier.fromValue(mod);
                            if (!Modifier.isSupported(modifier)) {
                                String msg = "Modifier not allowed: " + mod;
                                throw buildInvalidSearchException(msg);
                            }
                        } catch (IllegalArgumentException e) {
                            String msg = "Undefined Modifier: " + mod;
                            throw buildInvalidSearchException(msg);
                        }
                    }
                    parameterName = parameterName.substring(0, parameterName.indexOf(":"));
                }

                // Get the search parameter from our filtered set of applicable SPs for this resource type.
                SearchParameter searchParameter = applicableSPs.get(parameterName);
                if (searchParameter == null) {
                    String msg = "Search parameter '" + parameterName + "' for resource type '" + resourceType.getSimpleName()
                    + "' was not found.";
                    throw buildInvalidSearchException(msg);
                }

                // get the type of parameter so that we can use it to parse the value
                Type type = Type.fromValue(searchParameter.getType().getValue());

                if (modifier != null && !isAllowed(type, modifier)) {
                    String msg = "Unsupported type/modifier combination: " + type.value() + "/" + modifier.value();
                    throw buildInvalidSearchException(msg);
                }

                for (String queryParameterValueString : queryParameters.get(name)) {
                    Parameter parameter = new Parameter(type, parameterName, modifier, modifierResourceTypeName);
                    List<ParameterValue> queryParameterValues = parseQueryParameterValuesString(type, queryParameterValueString);
                    parameter.getValues().addAll(queryParameterValues);
                    parameters.add(parameter);
                }

            } catch (FHIRSearchException e) {
                throw e;
            } catch (Exception e) {
                String msg = "An error occurred while parsing search parameter '" + name + "'.";
                throw new FHIRSearchException(msg, e);
            }
        }

        context.setSearchParameters(parameters);
        Collections.sort(context.getSortParameters());

        return context;
    }

    private static List<ParameterValue> parseQueryParameterValuesString(Type type, String queryParameterValuesString) throws FHIRSearchException {
        List<ParameterValue> parameterValues = new ArrayList<>();
        for (String v : queryParameterValuesString.split(",")) {
            ParameterValue parameterValue = new ParameterValue();
            Prefix prefix = null;
            switch (type) {
            case DATE: {
                // date
                // [parameter]=[prefix][value]
                prefix = getPrefix(v);
                if (prefix != null) {
                    v = v.substring(2);
                    parameterValue.setPrefix(prefix);
                }
                parameterValue.setValueDate(FHIRUtilities.parseDateTime(v, false));
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
                parameterValue.setValueNumber(Double.parseDouble(v));
                break;
            }
            case REFERENCE: {
                // reference
                // [parameter]=[url]
                // [parameter]=[type]/[id]
                // [parameter]=[id]
                parameterValue.setValueString(v);
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
                String[] parts = v.split("\\|");
                String number = parts[0];
                parameterValue.setValueNumber(Double.parseDouble(number));
                String system = parts[1]; // could be empty string
                parameterValue.setValueSystem(system);
                String code = parts[2];
                parameterValue.setValueCode(code);
                break;
            }
            case STRING: {
                // string
                // [parameter]=[value]
                parameterValue.setValueString(v);
                break;
            }
            case TOKEN: {
                // token
                // [parameter]=[system]|[code]
                String[] parts = v.split("\\|");
                if (parts.length == 2) {
                    parameterValue.setValueSystem(parts[0]);
                    parameterValue.setValueCode(parts[1]);
                } else {
                    parameterValue.setValueCode(v);
                }
                break;
            }
            case URI: {
                // [parameter]=[value]
                parameterValue.setValueString(v);
                break;
            }
            default:
                break;
            }
            parameterValues.add(parameterValue);
        }
        return parameterValues;
    }

    private static boolean isAllowed(Type type, Modifier modifier) {

        return resourceTypeModifierMap.get(type).contains(modifier);
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

    public static FHIRSearchContext parseQueryParameters(String compartmentName, String compartmentLogicalId, Class<? extends Resource> resourceType,
        Map<String, List<String>> queryParameters, String queryString) throws Exception {
        List<Parameter> parameters = new ArrayList<Parameter>();
        Parameter parameter;
        ParameterValue value;
        Parameter rootParameter = null;

        if (compartmentName != null && compartmentLogicalId != null) {
            // The inclusion criteria are represented as a chain of parameters, each with a value of the
            // compartmentLogicalId.
            // The query parsers will OR these parameters to achieve the compartment search.
            List<String> inclusionCriteria = getCompartmentResourceTypeInclusionCriteria(compartmentName, resourceType.getSimpleName());
            for (String criteria : inclusionCriteria) {
                parameter = new Parameter(Type.REFERENCE, criteria, null, null, true);
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

        FHIRSearchContext context = parseQueryParameters(resourceType, queryParameters, queryString);
        context.getSearchParameters().addAll(parameters);

        return context;
    }

    private static Prefix getPrefix(String s) throws FHIRSearchException {

        Prefix returnPrefix = null;

        for (Prefix prefix : Prefix.values()) {
            if (s.startsWith(prefix.value())) {
                returnPrefix = prefix;
                break;
            }
        }
        if (returnPrefix != null && !Prefix.isSupported(returnPrefix)) {
            String msg = "Prefix not allowed: " + returnPrefix;
            throw buildInvalidSearchException(msg);
        }
        return returnPrefix;
    }

    private static boolean queryParameterShouldBeExcluded(String name) {
        if ("_format".equals(name) || name.startsWith("x-whc-lsf-")) {
            return true;
        }

        return false;
    }

    private static boolean isSearchResultParameter(String name) {
        return SEARCH_RESULT_PARAMETER_NAMES.contains(name);
    }

    private static void parseSearchResultParameter(Class<? extends Resource> resourceType, FHIRSearchContext context, String name, List<String> values,
        String queryString) throws FHIRSearchException {
        try {
            String first = values.get(0);
            if ("_count".equals(name)) {
                int pageSize = Integer.parseInt(first);

                // If the user specified a value > max, then use the max.
                if (pageSize > MAX_PAGE_SIZE) {
                    pageSize = MAX_PAGE_SIZE;
                }
                context.setPageSize(pageSize);
            } else if ("_page".equals(name)) {
                int pageNumber = Integer.parseInt(first);
                context.setPageNumber(pageNumber);
            } else if (name.startsWith("_sort")) {
                parseSortParameter(resourceType, context, name, values, queryString);
            } else if (name.startsWith("_include") || name.startsWith("_revinclude")) {
                parseInclusionParameter(resourceType, context, name, values, queryString);
            } else if ("_elements".equals(name)) {
            	parseElementsParameter(resourceType, context, values);
            }
        } catch (FHIRSearchException e) {
            throw e;
        } catch (Exception e) {
            String msg = "Unable to parse search result parameter named: '" + name + "'";
            throw new FHIRSearchException(msg, e);
        }
    }

    private static void parseSortParameter(Class<? extends Resource> resourceType, FHIRSearchContext context, String sortKeyword, List<String> sortParmNames,
        String queryString) throws Exception {

        SortDirection sortDirection;
        String sortDirectionString;
        Type sortParmType;
        SearchParameter sortParmProxy;
        SortParameter sortParm;
        int qualifierDelimiterPosition;
        int queryStringIndex;
        String sortSubstring;

        if (queryString == null) {
            String msg = "Sort parameters cannot be processed with null queryString.";
            throw buildInvalidSearchException(msg);
        }
        // Extract the sort direction, i.e. 'asc' or 'desc'
        qualifierDelimiterPosition = sortKeyword.indexOf(":");
        if (qualifierDelimiterPosition > -1) {
            sortDirectionString = sortKeyword.substring(qualifierDelimiterPosition + 1);
            sortDirection = SortDirection.fromValue(sortDirectionString);
        } else {
            sortDirection = SortDirection.ASCENDING;
        }

        for (String sortParmName : sortParmNames) {
            // Determine the position of the sort parameter in the query string.
            // This allows the sort parameter itself to be sorted later.
            sortSubstring = sortKeyword + "=" + sortParmName;
            queryStringIndex = queryString.indexOf(sortSubstring);
            if (queryStringIndex < 0) {
                String msg = "Sort parameter not found in query string: " + sortSubstring;
                throw buildInvalidSearchException(msg);
            }
            // Per the FHIR spec, the _sort parameter value is a search parameter. We need to determine what
            // type of search parameter.
            sortParmProxy = getSearchParameter(resourceType, sortParmName);
            if (sortParmProxy == null) {
                String msg = "Undefined sort parameter. resourceType=" + resourceType + " sortParmName=" + sortParmName;
                throw buildInvalidSearchException(msg);
            }
            sortParmType = Type.fromValue(sortParmProxy.getType().getValue());
            sortParm = new SortParameter(sortParmName, sortParmType, sortDirection, queryStringIndex);
            if (resourceType.equals(Resource.class) && ! SYSTEM_LEVEL_SORT_PARAMETER_NAMES.contains(sortParm.getName())) {
                String msg = sortParm.getName() + " is not supported as a sort parameter on a system-level search.";
                throw buildInvalidSearchException(msg);
            }
            context.getSortParameters().add(sortParm);
        }
    }

    private static boolean isChainedParameter(String name) {
        return name.contains(".");
    }

    private static Parameter parseChainedParameter(Class<? extends Resource> resourceType, String name, String valuesString) throws Exception {

        Parameter rootParameter = null;

        try {
            List<String> components = Arrays.asList(name.split("\\."));
            int lastIndex = components.size() - 1;
            int currentIndex = 0;

            Type type = null;

            for (String component : components) {
                Modifier modifier = null;
                String modifierResourceTypeName = null;
                String parameterName = component;

                if (parameterName.contains(":")) {
                    String mod = parameterName.substring(parameterName.indexOf(":") + 1);
                    if (FHIRUtil.isStandardResourceType(mod)) {
                        modifier = Modifier.TYPE;
                        modifierResourceTypeName = mod;
                    } else {
                        modifier = Modifier.fromValue(mod);
                    }
                    if (modifier != null && !Modifier.TYPE.equals(modifier) && currentIndex < lastIndex) {
                        String msg = "Modifier: '" + modifier + "' not allowed on chained parameter";
                        throw buildInvalidSearchException(msg);
                    }
                    parameterName = parameterName.substring(0, parameterName.indexOf(":"));
                }

                SearchParameter searchParameter = getSearchParameter(resourceType, parameterName);
                type = Type.fromValue(searchParameter.getType().getValue());

                if (!Type.REFERENCE.equals(type) && currentIndex < lastIndex) {
                    String msg = "Type: '" + type + "' not allowed on chained parameter";
                    throw buildInvalidSearchException(msg);
                }

                List<Code> targets = searchParameter.getTarget();
                if (modifierResourceTypeName == null && targets.size() > 1 && currentIndex < lastIndex) {
                    String msg = "Search parameter: '" + parameterName + "' must have resource type name modifier";
                    throw buildInvalidSearchException(msg);
                }

                if (modifierResourceTypeName == null && currentIndex < lastIndex) {
                    modifierResourceTypeName = targets.get(0).getValue();
                    modifier = Modifier.TYPE;
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

                if (currentIndex < lastIndex) {
                    resourceType = getResourceType(modifierResourceTypeName);
                }

                currentIndex++;
            } // end for loop

            List<ParameterValue> valueList = parseQueryParameterValuesString(type, valuesString);
            rootParameter.getChain().getLast().getValues().addAll(valueList);
        } catch (FHIRSearchException e) {
            throw e;
        } catch (Exception e) {
            String msg = "Unable to parse chained parameter: '" + name + "'";
            throw new FHIRSearchException(msg, e);
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
        String resourceType = inclusionCriteriaParm.getName().split(":")[1];
        for (int i = 0; i < parmNames.length; i++) {
            if (parmNames[i].contains(":")) {
                qualifiedInclusionCriteria = parmNames[i].split(":");
                chainedInclusionCriteria = new Parameter(Type.REFERENCE, qualifiedInclusionCriteria[0], null, resourceType, inclusionCriteriaParm.getValues());
            } else {
                chainedInclusionCriteria = new Parameter(Type.REFERENCE, parmNames[i], null, resourceType);
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
     * Parses _include and _revinclude search result parameters contained in the query string, and produces InclusionParameter objects to represent those
     * parameters. The InclusionParameter objects are included in the appropriate collections encapsulated in the passed FHIRSearchContext.
     * @throws Exception
     */
    private static void parseInclusionParameter(Class<? extends Resource> resourceType, FHIRSearchContext context, String inclusionKeyword, List<String> inclusionValues,
            String queryString) throws Exception {
        
        String[] inclusionValueParts;
        String joinResourceType, searchParameterName, searchParameterTargetType;
        SearchParameter searchParm;
        InclusionParameter newInclusionParm;
        List<InclusionParameter> newInclusionParms;
        
        if (queryString == null) {
            throw buildInvalidSearchException("Inclusion parameters cannot be processed with null queryString.");
        }
        
        // Make sure _sort is not present with _include and/or _revinclude. 
        if (queryString.contains("_sort")) {
            throw buildInvalidSearchException("_sort search result parameter not supported with _include or _revinclude.");
        }
                
        for (String inclusionValue : inclusionValues) {
            
            // Parse value into 3 parts: joinResourceType, searchParameterName, searchParameterTargetType
            inclusionValueParts = inclusionValue.split(":");
            if (inclusionValueParts.length < 2) {
                throw buildInvalidSearchException("A value for _include or _revinclude must have at least 2 parts separated by a colon.");
            }
            joinResourceType = inclusionValueParts[0];
            searchParameterName = inclusionValueParts[1];
            searchParameterTargetType = inclusionValueParts.length == 3 ? inclusionValueParts[2] : null;
            
            // Ensure that the Inclusion Parameter being parsed is a valid search parameter of type 'reference'.
            searchParm = getSearchParameter(joinResourceType, searchParameterName);
            if (searchParm == null) {
                throw buildInvalidSearchException("Undefined Inclusion Parameter: " + inclusionValue);
            }
            if (!searchParm.getType().getValue().equals("reference")) {
                throw buildInvalidSearchException("Inclusion Parameter must be of type 'reference'. " +
                                              "The passed Inclusion Parameter is of type: " + searchParm.getType().getValue());
            }
            
            if (inclusionKeyword.equals("_include")) {
                newInclusionParms = buildIncludeParameter(resourceType, joinResourceType, searchParm, searchParameterName, searchParameterTargetType);
                context.getIncludeParameters().addAll(newInclusionParms);
            }
            else {
                newInclusionParm = buildRevIncludeParameter(resourceType, joinResourceType, searchParm, searchParameterName, searchParameterTargetType);
                context.getRevIncludeParameters().add(newInclusionParm);
            }
        }
    }
    
    /**
     * Builds and returns a collection of InclusionParameter objects representing occurrences the _include search result parameter in the query string. 
     *  
     * @throws FHIRSearchException
     */
    private static List<InclusionParameter> buildIncludeParameter(Class<? extends Resource> resourceType, String joinResourceType, SearchParameter searchParm,
                                        String searchParameterName, String searchParameterTargetType) throws FHIRSearchException {
        
        List<InclusionParameter> includeParms = new ArrayList<>();
        
        if (!joinResourceType.equals(resourceType.getSimpleName())) {
            throw buildInvalidSearchException("The join resource type must match the resource type being searched.");
        }
        
        // If no searchParameterTargetType was specified, create an InclusionParameter instance for each of the search parameter's 
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
                throw buildInvalidSearchException("Invalid target type for the Inclusion Parameter.");
            }
            includeParms.add(new InclusionParameter(joinResourceType, searchParameterName, searchParameterTargetType));
        }
        return includeParms;
    }
    
    /**
     * Builds and returns a collection of InclusionParameter objects representing occurrences the _revinclude search result parameter in the query string. 
     *  
     * @throws FHIRSearchException
     */
    private static InclusionParameter buildRevIncludeParameter(Class<? extends Resource> resourceType, String joinResourceType, SearchParameter searchParm,
            String searchParameterName, String searchParameterTargetType) throws FHIRSearchException {
        
        // If a target type is specified, it must refer back to the resourceType being searched.
        if (searchParameterTargetType != null) {
            if (!searchParameterTargetType.equals(resourceType.getSimpleName())) {
                throw buildInvalidSearchException("The search parameter target type must match the resource type being searched.");
            }
        }
        else {
            searchParameterTargetType = resourceType.getSimpleName();
        }
        
        // Verify that the search parameter target type is correct
        if (!isValidTargetType(searchParameterTargetType, searchParm)) {
            throw buildInvalidSearchException("Invalid target type for the Inclusion Parameter.");
        }
        return new InclusionParameter(joinResourceType, searchParameterName, searchParameterTargetType);
        
    }
    
    /**
     * Verifies that the passed searchParameterTargetType is a valid target type for the passed searchParm
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
     * Parses _elements search result parameter contained in the query string, and produces element String objects to represent the
     * values for _elements. Those Strings are included in the elementsParameters collection contained in the passed FHIRSearchContext.
     * @throws Exception
     */
    private static void parseElementsParameter(Class<? extends Resource> resourceType, FHIRSearchContext context, List<String> elementsList) throws Exception {
    	
    	Set<String> resourceFieldNames = FHIRUtil.getFieldNames(resourceType.getSimpleName());
    	    	
    	for (String elementName: elementsList) {
    		if (elementName.startsWith("_") || !resourceFieldNames.contains(elementName)) {
    			throw buildInvalidSearchException("Invalid element name: " + elementName);
    		}
    		context.getElementsParameters().add(elementName);
    	}
    }
}

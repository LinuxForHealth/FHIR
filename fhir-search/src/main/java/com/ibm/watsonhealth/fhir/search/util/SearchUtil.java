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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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

import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.core.CachedObjectHolder;
import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleEntry;
import com.ibm.watsonhealth.fhir.model.Code;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.SearchParameter;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;
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
    
    private static final String BUILTIN_TENANT_ID = "built-in";
    private static final String SP_FILE_BASENAME = "extension-search-parameters.xml";
    
    /*
     * This is our in-memory cache of SearchParameter objects.  The cache is organized at the top level
     * by tenant-id, with the built-in (FHIR spec-defined) SearchParameters stored under the "built-in"
     * pseudo-tenant-id.  SearchParameters contained in the default tenant's 
     * extension-search-parameters.xml file are stored under the "default" tenant-id, and other tenants'
     * SearchParameters (defined in their tenant-specific extension-search-parameters.xml files) will be
     * stored under their respective tenant-ids as well.
     * 
     * The objects stored in our cache are of type CachedObjectHolder, with each one containing a
     * Map<String, Map<String, SearchParameter>>.  This map is keyed by resource type (simple name, 
     * e.g. "Patient").  Each object stored in this map contains the SearchParameters for that resource type, keyed by
     * SearchParameter name (e.g. "_lastUpdated").
     * 
     * When getSearchParameter(resourceType, name) is called, we'll need to first search in the current
     * tenant's map, then if not found, look in the "built-in" tenant's map.
     * Also, when getSearchParameters(resourceType) is called, we'll need to return a List that contains 
     * SearchParameters from the current tenant's map (if present) plus those contained in the "built-in"
     * tenant's map as well.
     */
    private static Map<String, CachedObjectHolder<Map<String, Map<String, SearchParameter>>>> 
        searchParameterCache = null;
    
    static {
        System.setProperty("com.sun.org.apache.xml.internal.dtm.DTMManager", "com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault");
        initializeSearchParameterMap();
    }

    private static final ThreadLocal<XPathFactory> threadLocalXPathFactory = new ThreadLocal<XPathFactory>() {
        @Override
        protected XPathFactory initialValue() {
            return XPathFactory.newInstance();
        }
    };    
    
    private static final List<String> SEARCH_RESULT_PARAMETER_NAMES =
            Arrays.asList("_sort", "_sort:asc", "_sort:desc", "_count", "_page");
    
    private static final Map<String, Map<String, List<String>>> compartmentMap = buildCompartmentMap();
    
    private SearchUtil() {
    }
    
    
    private static void initializeSearchParameterMap() {
        try {
            // Create our Search Parameter cache and then load up the spec-defined (builtin) 
            // SearchParameters and cache them under the "built-in" pseudo tenant.
            searchParameterCache = new HashMap<>();
            Map<String, Map<String, SearchParameter>> builtInSearchParameters = populateSearchParameterMapFromResource("search-parameters.xml");
            CachedObjectHolder<Map<String, Map<String, SearchParameter>>> cachedObject = new CachedObjectHolder<>(builtInSearchParameters);
            searchParameterCache.put(BUILTIN_TENANT_ID, cachedObject);
        } catch (Throwable t) {
            throw new Error(t);
        }

    }

    /**
     * Builds an in-memory model of the Compartment map defined in compartments.json, for supporting compartment based
     * FHIR searches.
     * @return Map<String, Map<String, List<String>>>
     */
    private static Map<String, Map<String, List<String>>> buildCompartmentMap() {
    	
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
		InputStream stream = SearchUtil.class.getClassLoader().getResourceAsStream("compartments.json");
        jsonCompartmentString = new BufferedReader(new InputStreamReader(stream))
				        		.lines().collect(Collectors.joining(System.getProperty("line.separator")));
		JsonReaderFactory factory = Json.createReaderFactory(null);
    	JsonReader jsonReader = factory.createReader(new StringReader(jsonCompartmentString));
    	jsonCompartmentDataRoot = jsonReader.readObject();
    	
    	// Iterate over the comparments, resources, and includsion criteria to populate 
    	// the compartment map.
    	jsonCompartments = jsonCompartmentDataRoot.getJsonArray("compartments");
    	for (int i = 0; i < jsonCompartments.size(); i++) {
    		jsonCompartment = (JsonObject) jsonCompartments.get(i);
    		compartmentName = jsonCompartment.getString("name");
    		compartmentMap.put(compartmentName, new TreeMap<String, List<String>>());
    		jsonCompartmentResources = jsonCompartment.getJsonArray("resources");
    		for (int j = 0; j < jsonCompartmentResources.size(); j++) {
    			jsonResource = (JsonObject)jsonCompartmentResources.get(j);
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
    		throw new FHIRSearchException("Invalid compartment: " + compartment);
    	}
        return Collections.unmodifiableList(new ArrayList<String>(compartmentMap.get(compartment).keySet()));
    }

    public static List<String> getCompartmentResourceTypeInclusionCriteria(String compartment, String resourceType) throws FHIRSearchException {
    	if (compartmentMap.get(compartment) == null) {
    		throw new FHIRSearchException("Invalid compartment: " + compartment);
    	}
    	if (compartmentMap.get(compartment).get(resourceType) == null) {
    		throw new FHIRSearchException("Invalid resource type: " + resourceType + " for compartment: " + compartment);
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

//    private static Map<String, Map<String, SearchParameter>> buildSearchParameterMap() {
//        Map<String, Map<String, SearchParameter>> searchParameterMap = new HashMap<String, Map<String, SearchParameter>>();
//        try {
//            populateSearchParameterMap(searchParameterMap, "search-parameters.xml");
//        } catch (JAXBException e) {
//            throw new Error(e);
//        }
//        try {
//            populateSearchParameterMap(searchParameterMap, "extension-search-parameters.xml");
//        } catch (Exception e) {
//            log.fine("Unable to load extension search parameters from location: ${server.config.dir}/config/extension-search-parameters.xml");
//            log.fine(e.getMessage());
//        }
//        return searchParameterMap;
//    }
    
    /**
     * Returns a Map containing the SearchParameters loaded from the specified File object.
     * @param file a File object which represents the file from which the SearchParameters are to be loaded
     * @return the Map containing the SearchParameters
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    private static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromFile(File file) throws JAXBException, FileNotFoundException {
        FileInputStream stream = new FileInputStream(file);
        return populateSearchParameterMapFromStream(stream);
    }
    
    
    /**
     * Returns a Map containing the SearchParameters loaded from the specified classpath resource.
     * @param resourceName the name of a resource available on the current classpath from which 
     * the SearchParameters are to be loaded
     * @return the Map containing the SearchParameters
     * @throws JAXBException
     */
    private static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromResource(String resourceName) throws JAXBException {
        InputStream stream = SearchUtil.class.getClassLoader().getResourceAsStream(resourceName);
        return populateSearchParameterMapFromStream(stream);
    }

    /**
     * Loads SearchParameters using the specified InputStream and returns a Map containing them.
     * @param stream the InputStream from which to load the SearchParameters
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
     * @param resourceType a Class representing the resource type associated with the search parameters to be returned.
     * @throws Exception
     */
    public static List<SearchParameter> getSearchParameters(Class<? extends Resource> resourceType) throws Exception {
        return getSearchParameters(resourceType.getSimpleName());
    }

    /**
     * Returns the list of search parameters for the specified resource type and the current tenant.
     * @param resourceType the resource type associated with the search parameters to be returned.
     * @return
     * @throws Exception
     */
    public static List<SearchParameter> getSearchParameters(String resourceType) throws Exception {
        return getSearchParameters(FHIRRequestContext.get().getTenantId(), resourceType);
    }
    
    /**
     * This function will return a list of all SearchParameters associated with the
     * specified tenant and resource type.
     * The result will include both built-in and tenant-specific SearchParameters for the specified 
     * resource type.
     * @param tenantId the tenant id whose search parameters should be returned
     * @param resourceType the resource type associated with the search parameters to be returned
     * @return the list of built-in and tenant-specific search parameters associated with the 
     * specified resource type
     * @throws Exception
     */
    public static List<SearchParameter> getSearchParameters(String tenantId, String resourceType) throws Exception {
        List<SearchParameter> result = new ArrayList<>();
        
        // First retrieve built-in search parameters for this resource type and add them to the result
        Map<String, Map<String, SearchParameter>> spMapTenant = getBuiltInSearchParameterMap();
        Map<String, SearchParameter> spMapResourceType = spMapTenant.get(resourceType);
        if (spMapResourceType != null && !spMapResourceType.isEmpty()) {
            result.addAll(spMapResourceType.values());
        }
        
        // Next, retrieve the specified tenant's search parameters for this resource type and add those
        // to the result as well.
        spMapTenant = getTenantSPMap(tenantId);
        if (spMapTenant != null) {
            spMapResourceType = spMapTenant.get(resourceType);
            if (spMapResourceType != null && !spMapResourceType.isEmpty()) {
                result.addAll(spMapResourceType.values());
            }
        }
        
        return result;
    }
    
    /**
     * This is a convenience function that simply returns the built-in (spec-defined)
     * SearchParameters as a map keyed by resource type.
     */
    private static Map<String, Map<String, SearchParameter>> getBuiltInSearchParameterMap() {
        CachedObjectHolder<Map<String, Map<String, SearchParameter>>> mapHolder = 
                searchParameterCache.get(BUILTIN_TENANT_ID);
        if (mapHolder == null) {
            throw new Error("Internal error: cannot retrieve built-in search parameters.");
        }
        return mapHolder.getCachedObject();
    }
    
    /**
     * Returns the SearchParameter map (keyed by resource type) for the specified tenant-id,
     * or null if there are no SearchParameters for the tenant.
     * 
     * @param tenantId the tenant-id whose SearchParameters should be returned.
     * @throws JAXBException 
     * @throws FileNotFoundException 
     */
    private static Map<String, Map<String, SearchParameter>> getTenantSPMap(String tenantId) throws FileNotFoundException, JAXBException {
        log.entering(CLASSNAME, "getTenantSPMap");
        try {
            CachedObjectHolder<Map<String, Map<String, SearchParameter>>> tenantMapHolder = null;
            synchronized (searchParameterCache) {
                tenantMapHolder = searchParameterCache.get(tenantId);

                // If we found the tenant map, then let's determine if it is stale and should be discarded.
                if (tenantMapHolder != null) {
                    log.finer("Cached search parameter map for tenant-id '" + tenantId + "' is stale, discarding...");
                    searchParameterCache.remove(tenantId);
                    tenantMapHolder = null;
                }

                // If we have no "current" search parameter map for this tenant in the cache,
                // then load it and add it to our cache.
                if (tenantMapHolder == null) {
                    String fileName = getSPFileName(tenantId);
                    File f = new File(fileName);
                    if (f.exists()) {
                        Map<String, Map<String, SearchParameter>> spMap = populateSearchParameterMapFromFile(f);

                        tenantMapHolder = new CachedObjectHolder<Map<String, Map<String, SearchParameter>>>(fileName, spMap);
                        searchParameterCache.put(tenantId, tenantMapHolder);
                        log.fine("Loaded search parameter map for tenant-id '" + tenantId + "' and added it to the cache.");
                    }
                }
            }
            
            return (tenantMapHolder != null ? tenantMapHolder.getCachedObject() : null);
        } finally {
            log.exiting(CLASSNAME, "getTenantSPMap");
        }
    }

    /**
     * Returns the full (relative or absolute depending on result of getConfigHome()) filename where
     * we'd expect to find the specified tenant's search parameter definitions.
     * @param tenantId the tenant-id associated with the tenant whose search parameters we want to load.
     */
    private static String getSPFileName(String tenantId) {
        return FHIRConfiguration.getConfigHome() + FHIRConfiguration.CONFIG_LOCATION + File.separator + tenantId + File.separator + SP_FILE_BASENAME;
    }


    public static SearchParameter getSearchParameter(Class<? extends Resource> resourceType, String name) throws Exception {
        return getSearchParameter(FHIRRequestContext.get().getTenantId(), resourceType.getSimpleName(), name);
    }
    
    public static SearchParameter getSearchParameter(String tenantId, String resourceType, String name) throws Exception {
        // First try to find the search parameter within the specified tenant's map.
        SearchParameter result = getSearchParameterInternal(getTenantSPMap(tenantId), resourceType, name);
        
        // If we didn't find it within the tenant's map, then look within the built-in map.
        if (result == null) {
            result = getSearchParameterInternal(getBuiltInSearchParameterMap(), resourceType, name);
        }
        return result;
    }
    
    /**
     * Given a Map of SearchParameters
     * @param tenantMap
     * @param resourceType
     * @param name
     * @return
     */
    private static SearchParameter getSearchParameterInternal(Map<String, Map<String, SearchParameter>> tenantMap, String resourceType, String name) {
        SearchParameter result = null;
        if (tenantMap != null) {
            Map<String, SearchParameter> resourceTypeMap = tenantMap.get(resourceType);
            if (resourceTypeMap != null) {
                result = resourceTypeMap.get(name);
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

        List<SearchParameter> parameters = new ArrayList<SearchParameter>(getSearchParameters(Resource.class));
        parameters.addAll(getSearchParameters(resourceType));

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
        throws FHIRSearchException {
        FHIRSearchContext context = FHIRSearchContextFactory.createSearchContext();
        List<Parameter> parameters = new ArrayList<Parameter>();

        for (String name : queryParameters.keySet()) {
            try {
                if (isFormatParameter(name) || isSearchResultParameter(name)) {
                    if (isSearchResultParameter(name)) {
                        parseSearchResultParameter(resourceType, context, name, queryParameters.get(name), queryString);
                    }
                    continue;
                }
                
                if (isChainedParameter(name)) {
                    Parameter chainedParameter = parseChainedParameter(resourceType, name, queryParameters.get(name));
                    parameters.add(chainedParameter);
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
                        modifier = Modifier.fromValue(mod);
                    }
                    parameterName = parameterName.substring(0, parameterName.indexOf(":"));
                }

                // get the definition for this search parameter based on resource type and parameter name
                SearchParameter searchParameter =
                        parameterName.startsWith("_") ? getSearchParameter(Resource.class, parameterName) : getSearchParameter(resourceType, parameterName);
                if (searchParameter == null) {
                    throw new FHIRSearchException("Search parameter '" + parameterName + "' for resource type '" + resourceType.getSimpleName()
                            + "' was not found.");
                }

                // get the type of parameter so that we can use it to parse the value
                Type type = Type.fromValue(searchParameter.getType().getValue());

                // parse values
                for (String value : queryParameters.get(name)) {
                    Parameter parameter = new Parameter(type, parameterName, modifier, modifierResourceTypeName);
                    parameters.add(parameter);
                    for (String v : value.split(",")) {
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
                        parameter.getValues().add(parameterValue);
                    }
                }
            } catch (FHIRSearchException e) {
                throw e;
            } catch (Exception e) {
                log.fine("Unable to parse query parameter named: " + name);
                throw new FHIRSearchException("Unable to parse query parameter named: '" + name + "'", e);
            }
        }

        context.setSearchParameters(parameters);
        Collections.sort(context.getSortParameters());
         
        return context;
    }
    
    public static FHIRSearchContext parseQueryParameters(String compartmentName, String compartmentLogicalId, 
    								Class<? extends Resource> resourceType, Map<String, List<String>> queryParameters, String queryString)
    								throws FHIRSearchException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        Parameter parameter;
        ParameterValue value;
        Parameter rootParameter = null;
        
        if (compartmentName != null && compartmentLogicalId != null) { 
	        // The inclusion criteria are represented as a chain of parameters, each with a value of the compartmentLogicalId.
	        // The query parsers will OR these parameters to achieve the compartment search.
	        List<String> inclusionCriteria = getCompartmentResourceTypeInclusionCriteria(compartmentName, resourceType.getSimpleName());
	        for (String criteria : inclusionCriteria) {
	        	parameter = new Parameter(Type.REFERENCE, criteria, null, null, true);
	        	value = new ParameterValue();
	        	value.setValueString(compartmentName + "/" + compartmentLogicalId);
	        	parameter.getValues().add(value);
	            if (rootParameter == null) {
	            	rootParameter = parameter;
	            }
	            else {
	            	if (rootParameter.getChain().isEmpty()) {
	            		rootParameter.setNextParameter(parameter);
	            	}
	            	else {
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

    private static Prefix getPrefix(String s) {
        for (Prefix prefix : Prefix.values()) {
            if (s.startsWith(prefix.value())) {
                return prefix;
            }
        }
        return null;
    }

    private static boolean isFormatParameter(String name) {
        return "_format".equals(name);
    }

    private static boolean isSearchResultParameter(String name) {
        return SEARCH_RESULT_PARAMETER_NAMES.contains(name);
    }

    private static void parseSearchResultParameter(Class<? extends Resource> resourceType, FHIRSearchContext context, 
			String name, List<String> values, String queryString) throws FHIRSearchException {
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
			}
		} catch (FHIRSearchException e) {
			throw e;
		} catch (Exception e) {
			throw new FHIRSearchException("Unable to parse search result parameter named: '" + name + "'", e);
		}
    }
    
    private static void parseSortParameter(Class<? extends Resource> resourceType, FHIRSearchContext context, 
			String sortKeyword, List<String> sortParmNames, String queryString) throws Exception   {

		SortDirection sortDirection;
		String sortDirectionString;
		Type sortParmType;
		SearchParameter sortParmProxy;
		SortParameter sortParm;
		int qualifierDelimiterPosition;
		int queryStringIndex;
		String sortSubstring;
				
		if (queryString == null) {
			throw new FHIRSearchException("Sort parameters cannot be processed with null queryString.");
		}
		// Extract the sort direction, i.e. 'asc' or 'desc'
		qualifierDelimiterPosition = sortKeyword.indexOf(":");
		if (qualifierDelimiterPosition > -1) {
			sortDirectionString = sortKeyword.substring(qualifierDelimiterPosition+1);
			sortDirection = SortDirection.fromValue(sortDirectionString);
		}
		else {
			sortDirection = SortDirection.ASCENDING;
		}
		
		for (String sortParmName : sortParmNames) {
			// Determine the position of the sort parameter in the query string.
			// This allows the sort parameter itself to be sorted later.
			sortSubstring = sortKeyword + "=" + sortParmName;
			queryStringIndex = queryString.indexOf(sortSubstring);
			if (queryStringIndex < 0) {
				throw new FHIRSearchException("Sort parameter not found in query string: " + sortSubstring);
			}
			// Per the FHIR spec, the _sort parameter value is a search parameter. We need to determine what
			// type of search parameter.
			sortParmProxy = getSearchParameter(resourceType, sortParmName);
			if (sortParmProxy == null) {
				throw new FHIRSearchException("Undefined sort parameter. resourceType=" + resourceType + " sortParmName=" + sortParmName);
			}
			sortParmType = Type.fromValue(sortParmProxy.getType().getValue());
			sortParm = new SortParameter(sortParmName, sortParmType, sortDirection, queryStringIndex);
			context.getSortParameters().add(sortParm);
		}
}

   
    private static boolean isChainedParameter(String name) {
        return name.contains(".");
    }
    
    private static Parameter parseChainedParameter(Class<? extends Resource> resourceType, String name, List<String> values) throws FHIRSearchException {
        
    	Parameter rootParameter = null;
        
        try {
            List<String> components = Arrays.asList(name.split("\\."));
            int lastIndex = components.size() - 1;
            int currentIndex = 0;
            
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
                        throw new FHIRSearchException("Modifier: '" + modifier + "' not allowed on chained parameter");
                    }
                    parameterName = parameterName.substring(0, parameterName.indexOf(":"));
                }
                
                SearchParameter searchParameter = getSearchParameter(resourceType, parameterName);
                Type type = Type.fromValue(searchParameter.getType().getValue());
                
                if (!Type.REFERENCE.equals(type) && currentIndex < lastIndex) {
                    throw new FHIRSearchException("Type: '" + type + "' not allowed on chained parameter");
                }
                
                List<Code> targets = searchParameter.getTarget();
                if (modifierResourceTypeName == null && targets.size() > 1 && currentIndex < lastIndex) {
                    throw new FHIRSearchException("Search parameter: '" + parameterName + "' must have resource type name modifier");
                }
                                
                if (modifierResourceTypeName == null && currentIndex < lastIndex) {
                    modifierResourceTypeName = targets.get(0).getValue();
                    modifier = Modifier.TYPE;
                }
                
                Parameter parameter = new Parameter(type, parameterName, modifier, modifierResourceTypeName);
                if (rootParameter == null) {
                	rootParameter = parameter;
                }
                else {
                	if (rootParameter.getChain().isEmpty()) {
                		rootParameter.setNextParameter(parameter);
                	}
                	else {
                		rootParameter.getChain().getLast().setNextParameter(parameter);
                	}
                }
                                
                if (currentIndex < lastIndex) {
                    resourceType = getResourceType(modifierResourceTypeName);
                }
                
                currentIndex++;
            }
            
            ParameterValue value = new ParameterValue();
            value.setValueString(values.get(0));
            rootParameter.getChain().getLast().getValues().add(value);
        } catch (FHIRSearchException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIRSearchException("Unable to parse chained parameter: '" + name + "'", e);
        }
        
        return rootParameter;
    }
    
    /**
     * Transforms the passed Parameter representing chained inclusion criteria, into an actual chain of Parameter objects.
     * This method consumes Parameters with names of this form: "{attribute1}.{attribute2}:{resourceType}"
     * For specific examples of chained inclusion criteria, see the FHIR spec for the Patient compartment here:
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
			}
			else {
				chainedInclusionCriteria = new Parameter(Type.REFERENCE, parmNames[i], null, resourceType);
			}
			if (rootParameter == null) {
				rootParameter = chainedInclusionCriteria;
			}
			else if (rootParameter.getNextParameter() == null) {
				rootParameter.setNextParameter(chainedInclusionCriteria);
			}
			else {
				rootParameter.getChain().getLast().setNextParameter(chainedInclusionCriteria);
			}
		}
		return rootParameter;
    }
}

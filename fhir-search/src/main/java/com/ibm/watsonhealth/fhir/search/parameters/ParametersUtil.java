/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.parameters;

import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.eval;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ibm.watsonhealth.fhir.exception.FHIRException;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTree;
import com.ibm.watsonhealth.fhir.model.resource.Bundle;
import com.ibm.watsonhealth.fhir.model.resource.SearchParameter;
import com.ibm.watsonhealth.fhir.model.type.ResourceType;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;

/**
 * ParametersUtil
 * 
 * History<br/>
 * 1 - Refactored the PopulateSearchParameterMap code.
 * 
 * @author pbastide@us.ibm.com
 * @author markd
 *
 */
public class ParametersUtil {

    private static final String CLASSNAME = ParametersUtil.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    public static final String FHIR_PATH_BUNDLE_ENTRY = "entry.resource";

    private static Map<String, Map<String, SearchParameter>> builtInSearchParameters = new HashMap<>();

    private ParametersUtil() {
        // No Operation
    }

    /**
     * This is a convenience function that simply returns the built-in (spec-defined) SearchParameters as a map keyed by
     * resource type.
     */
    public static Map<String, Map<String, SearchParameter>> getBuiltInSearchParameterMap() {
        if (builtInSearchParameters.isEmpty()) {
            try {
                builtInSearchParameters = populateSearchParameterMapFromResource("search-parameters.json");
            } catch (IOException e) {
                log.warning("Error condition reading the FHIR Bundle of Search Parameters" + " into the SearchParameter Map " + e.getClass().getSimpleName());
            }
        }
        return builtInSearchParameters;
    }

    /**
     * Returns a Map containing the SearchParameters loaded from the specified classpath resource.
     *
     * @param resourceName
     *            the name of a resource available on the current classpath from which the SearchParameters are to be
     *            loaded
     * @return the Map containing the SearchParameters
     * @throws IOException
     * @throws JAXBException
     */
    public static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromResource(String resourceName) throws IOException {
        // Capture Exception here and wrap and log out an issue with Search
        try (InputStream stream = ParametersUtil.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (stream == null) {
                throw new FileNotFoundException("Search parameter configuration file '" + resourceName + "' not found on classpath.");
            }
            return populateSearchParameterMapFromStream(stream);
        }
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
    public static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromFile(File file) throws IOException {
        try (FileInputStream stream = new FileInputStream(file)) {
            return populateSearchParameterMapFromStream(stream);
        }
    }

    /**
     * populates the search parameter map and defaults to json
     * 
     * @param stream
     * @return
     */
    public static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromStream(InputStream stream) {
        return populateSearchParameterMapFromStreamByFormat(stream, Format.JSON);
    }

    /**
     * populates the search parameter map and is XML
     * 
     * @param stream
     * @return
     */
    public static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromStreamXML(InputStream stream) {
        return populateSearchParameterMapFromStreamByFormat(stream, Format.XML);
    }

    /*
     * Loads SearchParameters using the specified InputStream and returns a Map containing them.
     * @param stream the InputStream from which to load the SearchParameters
     * @param Format
     * @return
     */
    private static Map<String, Map<String, SearchParameter>> populateSearchParameterMapFromStreamByFormat(InputStream stream, Format format) {
        // Format is never null, as this method is private and hidden.
        // In order to maintain this contract, and avoid the check, keep private.

        Map<String, Map<String, SearchParameter>> searchParameterMap = new HashMap<>();

        // The code block is isolating the effects of exceptions by capturing the exceptions here
        // and returning an empty searchParameterMap, and continuing operation.
        // The failure is logged out.
        try {
            // The code is agnostic to format.
            Bundle bundle = FHIRUtil.read(Bundle.class, format, new InputStreamReader(stream));

            FHIRPathTree tree = FHIRPathTree.tree(bundle);
            Collection<FHIRPathNode> result = eval(FHIR_PATH_BUNDLE_ENTRY, tree.getRoot());

            for (SearchParameter parameter : result.stream().map(node -> node.asResourceNode().resource().as(SearchParameter.class)).collect(Collectors.toList())) {
                
                log.fine("Parameter is loaded -> " + parameter.getName().getValue());
                // Only add the ones that have expressions.
                if (parameter.getExpression() != null) {
                    /*
                     * In R4, SearchParameter changes from a single Base resource to an array In prior releases, the
                     * code transformed VirtualResources to Basic. As Base is an array, there are going to result in
                     * potential collisions in the map. transformParameter and transformPath
                     */
                    List<ResourceType> types = parameter.getBase();
                    for (ResourceType type : types) {
                        String base = type.getValue();

                        // Logic seems poor, refactor.
                        Map<String, SearchParameter> map = searchParameterMap.get(base);
                        if (map == null) {
                            map = new TreeMap<>();
                            searchParameterMap.put(base, map);
                        }
                        String name = parameter.getName().getValue();
                        map.put(name, parameter);
                    }
                }

            }
        } catch (FHIRException fe) {
            // This exception is highly unlikely, but still possible.
            log.warning("Error condition reading the FHIR Bundle of Search Parameters" + " into the SearchParameter Map " + fe.getClass().getSimpleName());
        }

        // Must be unmodifiable, lest there be side effects.
        return Collections.unmodifiableMap(searchParameterMap);
    }

    /**
     * gets the resource or an empty list
     * @param resourceType
     * @return
     */
    public static Map<String, SearchParameter> getBuiltInSearchParameterMapByResourceType(String resourceType) {
        Map<String,SearchParameter> result = getBuiltInSearchParameterMap().get(resourceType);
        if(result == null) {
            result = Collections.emptyMap();
        }
        return Collections.unmodifiableMap(result);
    }

    /**
     * convenience method to print the output of the Search Parameters.
     * 
     * @param out
     */
    public static void print(PrintStream out) {

        Set<String> keys = builtInSearchParameters.keySet();
        out.println("---------------------------------------------------------");
        out.println("BASE:RESOURCE_NAME:SearchParameter");
        out.println("Size: " + keys.size());
        for (String base : keys) {
            Map<String, SearchParameter> tmp = builtInSearchParameters.get(base);
            for (Entry<String, SearchParameter> entry : tmp.entrySet()) {
                out.println(base + "|" + entry.getKey() + "|" + entry.getValue().getExpression().getValue());
            }
            out.println("---");
        }
        out.println("---------------------------------------------------------");
    }

}

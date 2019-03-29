/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 * This class encapsulates the filtering logic necessary to support the FHIR
 * _elements search result option.
 * 
 * @see https://www.hl7.org/fhir/DSTU2/search.html#elements
 * @author markd
 *
 */
public class ElementFilter {

    private static final JsonBuilderFactory builderFactory = Json.createBuilderFactory(null);
    private static final List<String> REQUIRED_ELEMENTS = Arrays.asList("resourceType", "id", "meta");
    private List<String> includeElements = new ArrayList<>();

    /**
     * Constructs an ElementFilter containing only the required elements.
     */
    public ElementFilter(String resourceTypeName) {
        includeElements.addAll(REQUIRED_ELEMENTS);
        includeElements.addAll(FHIRUtil.getRequiredFieldNames(resourceTypeName));
    }

    /**
     * Constructs an ElementFilter contining the required elements and the passed
     * elements.
     * @param resourceType 
     * 
     * @param elements
     *            A List of element names to be used in addition to the required
     *            elements.
     */
    public ElementFilter(String resourceTypeName, List<String> elements) {
        this(resourceTypeName);
        addElements(elements);
    }

    /**
     * Adds the passed elements to the collection of element names to be used for
     * filtering
     * 
     * @param newElements
     */
    public void addElements(List<String> newElements) {
        for (String element : newElements) {
            includeElements.add(element);
            // add a "_elementName" entry in case that element is a primitive
            // see https://www.hl7.org/fhir/DSTU2/json.html#primitive for more info
            includeElements.add("_" + element);
        }
    }

    /**
     * Creates and returns a new JsonObject based on the passed JsonObject, but only
     * containing the elements whose names are contained in this instance's
     * includeElements collection.
     * 
     * @param jsonObject
     *            - The json object to be filtered.
     * @return
     */
    public JsonObject apply(JsonObject jsonObject) {

        JsonValue value;
        JsonObjectBuilder builder = builderFactory.createObjectBuilder();

        // JsonObject is a Map<String, JsonValue>
        for (String key : jsonObject.keySet()) {
            if (this.includeElements.contains(key)) {
                value = jsonObject.get(key);
                builder.add(key, value);
            }
        }
        return builder.build();
    }

}

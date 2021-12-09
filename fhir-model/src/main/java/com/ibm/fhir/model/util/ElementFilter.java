/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class ElementFilter implements Function<JsonObject, JsonObject> {
    private static final JsonBuilderFactory BUILDER_FACTORY = Json.createBuilderFactory(null);
    private static final List<String> REQUIRED_ELEMENTS = Arrays.asList("resourceType", "id", "meta");
    
    private Set<String> includeElements = new HashSet<>();

    public ElementFilter(Class<?> resourceType) {
        includeElements.addAll(REQUIRED_ELEMENTS);
        includeElements.addAll(JsonSupport.getRequiredElementNames(resourceType));
    }

    public ElementFilter(Class<?> resourceType, Collection<String> elements) {
        this(resourceType);
        includeElements.addAll(elements);
    }

    public void addElements(Collection<String> elements) {
        includeElements.addAll(elements);
    }

    @Override
    public JsonObject apply(JsonObject jsonObject) {
        JsonObjectBuilder builder = BUILDER_FACTORY.createObjectBuilder();
        jsonObject.entrySet().stream().filter(e -> includeElements.contains(e.getKey()))
            .forEach(e -> builder.add(e.getKey(), e.getValue()));
        return builder.build();
    }
}

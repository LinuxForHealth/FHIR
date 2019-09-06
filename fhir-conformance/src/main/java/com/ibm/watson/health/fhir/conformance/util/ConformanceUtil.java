/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.conformance.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.watson.health.fhir.conformance.ConformanceResource;
import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.resource.CapabilityStatement;
import com.ibm.watson.health.fhir.model.resource.CodeSystem;
import com.ibm.watson.health.fhir.model.resource.ConceptMap;
import com.ibm.watson.health.fhir.model.resource.OperationDefinition;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.resource.SearchParameter;
import com.ibm.watson.health.fhir.model.resource.StructureDefinition;
import com.ibm.watson.health.fhir.model.resource.ValueSet;

public final class ConformanceUtil {
    private static final Logger log = Logger.getLogger(ConformanceUtil.class.getName());

    private static final List<Class<?>> CONFORMANCE_RESOURCE_TYPES = Arrays.asList(
        CapabilityStatement.class, 
        CodeSystem.class, 
        ConceptMap.class, 
        OperationDefinition.class, 
        SearchParameter.class, 
        StructureDefinition.class, 
        ValueSet.class);
    
    private ConformanceUtil() { }

    public static Collection<ConformanceResource> getConformanceResources(Format format, ClassLoader loader) {
        List<ConformanceResource> conformanceResources = new ArrayList<>();
        for (String entry : readIndex(loader)) {
            String[] tokens = entry.split(",");
            conformanceResources.add(new ConformanceResource(tokens[0], tokens[1], format, loader));
        }
        return Collections.unmodifiableList(conformanceResources);
    }

    public static String getUrl(Resource resource) {
        if (resource.is(CapabilityStatement.class)) {
            return resource.as(CapabilityStatement.class).getUrl().getValue();
        }
        if (resource.is(CodeSystem.class)) {
            return resource.as(CodeSystem.class).getUrl().getValue();
        }
        if (resource.is(ConceptMap.class)) {
            return resource.as(ConceptMap.class).getUrl().getValue();
        }
        if (resource.is(OperationDefinition.class)) {
            return resource.as(OperationDefinition.class).getUrl().getValue();
        }
        if (resource.is(SearchParameter.class)) {
            return resource.as(SearchParameter.class).getUrl().getValue();
        }
        if (resource.is(StructureDefinition.class)) {
            return resource.as(StructureDefinition.class).getUrl().getValue();
        }
        if (resource.is(ValueSet.class)) {
            return resource.as(ValueSet.class).getUrl().getValue();
        }
        return null;
    }
    
    public static boolean isConformanceResource(Resource resource) {
        return isConformanceResourceType(resource.getClass());
    }
    
    public static boolean isConformanceResourceType(Class<?> resourceType) {
        return CONFORMANCE_RESOURCE_TYPES.contains(resourceType);
    }

    public static Resource loadResource(String name, Format format, ClassLoader loader) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream(name), StandardCharsets.UTF_8))) {
            return FHIRParser.parser(format).parse(reader);
        } catch (Exception e) {
            log.warning("Unable to load: " + name + " due to the following exception: " + e);
        }
        return null;
    }

    public static List<String> readIndex(ClassLoader loader) {
        return new BufferedReader(new InputStreamReader(loader.getResourceAsStream("index"), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.toList());
    }
}
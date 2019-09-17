/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.registry.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.resource.ActivityDefinition;
import com.ibm.watson.health.fhir.model.resource.CapabilityStatement;
import com.ibm.watson.health.fhir.model.resource.ChargeItemDefinition;
import com.ibm.watson.health.fhir.model.resource.CodeSystem;
import com.ibm.watson.health.fhir.model.resource.CompartmentDefinition;
import com.ibm.watson.health.fhir.model.resource.ConceptMap;
import com.ibm.watson.health.fhir.model.resource.EffectEvidenceSynthesis;
import com.ibm.watson.health.fhir.model.resource.EventDefinition;
import com.ibm.watson.health.fhir.model.resource.Evidence;
import com.ibm.watson.health.fhir.model.resource.EvidenceVariable;
import com.ibm.watson.health.fhir.model.resource.ExampleScenario;
import com.ibm.watson.health.fhir.model.resource.GraphDefinition;
import com.ibm.watson.health.fhir.model.resource.ImplementationGuide;
import com.ibm.watson.health.fhir.model.resource.Library;
import com.ibm.watson.health.fhir.model.resource.Measure;
import com.ibm.watson.health.fhir.model.resource.MessageDefinition;
import com.ibm.watson.health.fhir.model.resource.NamingSystem;
import com.ibm.watson.health.fhir.model.resource.OperationDefinition;
import com.ibm.watson.health.fhir.model.resource.PlanDefinition;
import com.ibm.watson.health.fhir.model.resource.Questionnaire;
import com.ibm.watson.health.fhir.model.resource.ResearchDefinition;
import com.ibm.watson.health.fhir.model.resource.ResearchElementDefinition;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.resource.RiskEvidenceSynthesis;
import com.ibm.watson.health.fhir.model.resource.SearchParameter;
import com.ibm.watson.health.fhir.model.resource.StructureDefinition;
import com.ibm.watson.health.fhir.model.resource.StructureMap;
import com.ibm.watson.health.fhir.model.resource.TerminologyCapabilities;
import com.ibm.watson.health.fhir.model.resource.TestScript;
import com.ibm.watson.health.fhir.model.resource.ValueSet;
import com.ibm.watson.health.fhir.registry.resource.FHIRRegistryResource;

public final class FHIRRegistryUtil {
    private static final Logger log = Logger.getLogger(FHIRRegistryUtil.class.getName());
    
    private static final Set<Class<?>> DEFINITIONAL_RESOURCE_TYPES = new HashSet<>(Arrays.asList(
        ActivityDefinition.class, 
        CapabilityStatement.class, 
        ChargeItemDefinition.class, 
        CodeSystem.class, 
        CompartmentDefinition.class, 
        ConceptMap.class, 
        EffectEvidenceSynthesis.class, 
        EventDefinition.class, 
        Evidence.class, 
        EvidenceVariable.class, 
        ExampleScenario.class, 
        GraphDefinition.class, 
        ImplementationGuide.class, 
        Library.class, 
        Measure.class, 
        MessageDefinition.class, 
        NamingSystem.class, 
        OperationDefinition.class, 
        PlanDefinition.class, 
        Questionnaire.class, 
        ResearchDefinition.class, 
        ResearchElementDefinition.class, 
        RiskEvidenceSynthesis.class, 
        SearchParameter.class, 
        StructureDefinition.class, 
        StructureMap.class, 
        TerminologyCapabilities.class, 
        TestScript.class, 
        ValueSet.class));
    
    private FHIRRegistryUtil() { }
    
    public static String getUrl(Resource resource) {
        DefinitionalResourceVisitor visitor = new DefinitionalResourceVisitor();
        resource.accept(visitor);
        return visitor.getUrl();
    }
    
    public static String getVersion(Resource resource) {
        DefinitionalResourceVisitor visitor = new DefinitionalResourceVisitor();
        resource.accept(visitor);
        return visitor.getVersion();
    }
    
    public static boolean isDefinitionalResource(Resource resource) {
        return isDefinitionalResourceType(resource.getClass());
    }
    
    private static boolean isDefinitionalResourceType(Class<?> resourceType) {
        return DEFINITIONAL_RESOURCE_TYPES.contains(resourceType);
    }

    public static Resource loadResource(String name, Format format, ClassLoader loader) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream(name), StandardCharsets.UTF_8))) {
            return FHIRParser.parser(format).parse(reader);
        } catch (Exception e) {
            log.warning("Unable to load resource: " + name + " due to the following exception: " + e.getClass().getName() + " with message: " + e.getMessage());
        }
        return null;
    }
    
    public static Collection<FHIRRegistryResource> getResources(Format format, ClassLoader loader, String index) {
        List<FHIRRegistryResource> resources = new ArrayList<>();
        for (String entry : readIndex(loader, index)) {
            String[] tokens = entry.split(",");
            if (tokens.length != 3) {
                log.warning("Bad index entry: " + entry);
                continue;
            }
            resources.add(new FHIRRegistryResource(tokens[0], tokens[1], tokens[2], format, loader));
        }
        return Collections.unmodifiableList(resources);
    }
    
    public static List<String> readIndex(ClassLoader loader, String index) {
        return new BufferedReader(new InputStreamReader(loader.getResourceAsStream(index), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.toList());
    }
}
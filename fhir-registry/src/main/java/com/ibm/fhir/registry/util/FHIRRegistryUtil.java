/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.ActivityDefinition;
import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.model.resource.ChargeItemDefinition;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CompartmentDefinition;
import com.ibm.fhir.model.resource.ConceptMap;
import com.ibm.fhir.model.resource.EffectEvidenceSynthesis;
import com.ibm.fhir.model.resource.EventDefinition;
import com.ibm.fhir.model.resource.Evidence;
import com.ibm.fhir.model.resource.EvidenceVariable;
import com.ibm.fhir.model.resource.ExampleScenario;
import com.ibm.fhir.model.resource.GraphDefinition;
import com.ibm.fhir.model.resource.ImplementationGuide;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.Measure;
import com.ibm.fhir.model.resource.MessageDefinition;
import com.ibm.fhir.model.resource.NamingSystem;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.PlanDefinition;
import com.ibm.fhir.model.resource.Questionnaire;
import com.ibm.fhir.model.resource.ResearchDefinition;
import com.ibm.fhir.model.resource.ResearchElementDefinition;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.RiskEvidenceSynthesis;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.resource.StructureMap;
import com.ibm.fhir.model.resource.TerminologyCapabilities;
import com.ibm.fhir.model.resource.TestScript;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.resource.FHIRRegistryResource.Version;
import com.ibm.fhir.registry.util.Index.Entry;

public final class FHIRRegistryUtil {
    private static final Logger log = Logger.getLogger(FHIRRegistryUtil.class.getName());

    private static final Set<Class<? extends Resource>> DEFINITIONAL_RESOURCE_TYPES = new HashSet<>(Arrays.asList(
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

    /**
     * Throw an {@link IllegalArgumentException} if the resource type is not a definitional resource type per:
     * <a href="http://hl7.org/fhir/definition.html">http://hl7.org/fhir/definition.html</a>
     *
     * @param resourceType
     *     the resourceType
     */
    public static void requireDefinitionalResourceType(Class<? extends Resource> resourceType) {
        if (!isDefinitionalResourceType(resourceType)) {
            throw new IllegalArgumentException(resourceType.getSimpleName() + " is not a definitional resource type");
        }
    }

    /**
     * Indicates whether the resource type is a definitional resource type per:
     * <a href="http://hl7.org/fhir/definition.html">http://hl7.org/fhir/definition.html</a>
     *
     * @param resourceType
     *     the resource type
     * @return
     *     true if the resource type is a definitional resource, false otherwise
     */
    public static boolean isDefinitionalResourceType(Class<? extends Resource> resourceType) {
        return DEFINITIONAL_RESOURCE_TYPES.contains(resourceType);
    }

    public static Resource loadResource(String path) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(FHIRRegistryUtil.class.getClassLoader().getResourceAsStream(path), StandardCharsets.UTF_8))) {
            return FHIRParser.parser(Format.JSON).parse(reader);
        } catch (Exception e) {
            log.log(Level.WARNING, "Unable to load resource: " + path, e);
        }
        return null;
    }

    public static Collection<FHIRRegistryResource> getRegistryResources(String packageId) {
        List<FHIRRegistryResource> resources = new ArrayList<>();
        String packageDirectory = packageId.replace(".", "/") + "/package";
        for (Entry entry : readIndex(packageDirectory + "/.index.json")) {
            resources.add(new PackageRegistryResource(
                ModelSupport.getResourceType(entry.getResourceType()),
                entry.getId(),
                entry.getUrl(),
                (entry.getVersion() != null) ? Version.from(entry.getVersion()) : Version.NO_VERSION,
                entry.getKind(),
                entry.getType(),
                packageDirectory + "/" + entry.getFileName()));
        }
        return Collections.unmodifiableList(resources);
    }

    public static List<Entry> readIndex(String indexPath) {
        log.info("Loading index: " + indexPath);
        try (InputStream in = FHIRRegistryUtil.class.getClassLoader().getResourceAsStream(indexPath)) {
            Index index = new Index();
            index.load(in);
            return index.getEntries();
        } catch (Exception e) {
            log.log(Level.WARNING, "Unexpected error while loading index '" + indexPath + "'", e);
        }
        return Collections.emptyList();
    }
}
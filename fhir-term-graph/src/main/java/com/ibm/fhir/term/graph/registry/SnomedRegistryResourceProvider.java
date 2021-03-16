/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.registry;

import static com.ibm.fhir.core.util.LRUCache.createLRUCache;
import static com.ibm.fhir.model.type.String.string;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.resource.ValueSet.Compose;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include.Filter;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Xhtml;
import com.ibm.fhir.model.type.code.FilterOperator;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;
import com.ibm.fhir.term.registry.resource.FHIRTermRegistryResource;
import com.ibm.fhir.term.service.FHIRTermService;
import com.ibm.fhir.term.spi.ValidationOutcome;

public class SnomedRegistryResourceProvider implements FHIRRegistryResourceProvider {
    private static final Logger log = Logger.getLogger(SnomedRegistryResourceProvider.class.getName());

    private static final String SNOMED_URL = "http://snomed.info/sct";
    private static final String SNOMED_IMPLICIT_VALUE_SET_PREFIX = SNOMED_URL + "?fhir_vs";
    private static final String SNOMED_COPYRIGHT = "This value set includes content from SNOMED CT, which is copyright © 2002+ International Health Terminology Standards Development Organisation (SNOMED International), and distributed by agreement between SNOMED International and HL7. Implementer use of SNOMED CT is not covered by this agreement";

    public static final CodeSystem SNOMED_CODE_SYSTEM = loadCodeSystem();
    private static final FHIRRegistryResource SNOMED_CODE_SYSTEM_REGISTRY_RESOURCE = FHIRTermRegistryResource.from(SNOMED_CODE_SYSTEM);
    private static final FHIRRegistryResource SNOMED_ALL_CONCEPTS_IMPLICIT_VALUE_SET_REGISTRY_RESOURCE = FHIRTermRegistryResource.from(buildAllConceptsImplicitValueSet());
    private static final Map<String, FHIRRegistryResource> SNOMED_SUBSUMED_BY_IMPLICIT_VALUE_SET_REGISTRY_RESOURCE_CACHE = createLRUCache(128);

    @Override
    public FHIRRegistryResource getRegistryResource(Class<? extends Resource> resourceType, String url, String version) {
        if (url == null) {
            return null;
        }
        if (ValueSet.class.equals(resourceType) && url.startsWith(SNOMED_IMPLICIT_VALUE_SET_PREFIX)) {
            if (SNOMED_IMPLICIT_VALUE_SET_PREFIX.equals(url)) {
                return SNOMED_ALL_CONCEPTS_IMPLICIT_VALUE_SET_REGISTRY_RESOURCE;
            } else {
                String[] tokens = url.split("=");
                if (tokens.length == 2) {
                    String sctid = tokens[1];
                    ValidationOutcome outcome = FHIRTermService.getInstance().validateCode(SNOMED_CODE_SYSTEM, null, Code.of(sctid), null);
                    if (outcome == null || (Boolean.FALSE.equals(outcome.getResult()))) {
                        log.log(Level.WARNING, "Code: " + sctid + " is invalid or SNOMED CT is not supported");
                        return null;
                    }
                    return SNOMED_SUBSUMED_BY_IMPLICIT_VALUE_SET_REGISTRY_RESOURCE_CACHE.computeIfAbsent(sctid, k -> FHIRTermRegistryResource.from(buildSubsumedByImplicitValueSet(sctid)));
                }
            }
        } else if (CodeSystem.class.equals(resourceType) && SNOMED_URL.equals(url)) {
            return SNOMED_CODE_SYSTEM_REGISTRY_RESOURCE;
        }
        return null;
    }

    @Override
    public Collection<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType) {
        return Collections.emptyList();
    }

    @Override
    public Collection<FHIRRegistryResource> getRegistryResources() {
        return Collections.emptyList();
    }

    @Override
    public Collection<FHIRRegistryResource> getProfileResources(String type) {
        return Collections.emptyList();
    }

    @Override
    public Collection<FHIRRegistryResource> getSearchParameterResources(String type) {
        return Collections.emptyList();
    }

    private static ValueSet buildAllConceptsImplicitValueSet() {
        return ValueSet.builder()
                .text(Narrative.builder()
                    .status(NarrativeStatus.GENERATED)
                    .div(Xhtml.from("All SNOMED CT concepts"))
                    .build())
                .url(Uri.of(SNOMED_IMPLICIT_VALUE_SET_PREFIX))
                .name(string("All SNOMED CT concepts"))
                .description(Markdown.of("All SNOMED CT concepts"))
                .copyright(Markdown.of(SNOMED_COPYRIGHT))
                .status(PublicationStatus.ACTIVE)
                .compose(Compose.builder()
                    .include(Include.builder()
                        .system(Uri.of(SNOMED_URL))
                        .build())
                    .build())
                .build();
    }

    private ValueSet buildSubsumedByImplicitValueSet(String sctid) {
        return ValueSet.builder()
                .text(Narrative.builder()
                    .status(NarrativeStatus.GENERATED)
                    .div(Xhtml.from("All SNOMED CT concepts subsumed by " + sctid))
                    .build())
                .url(Uri.of(SNOMED_IMPLICIT_VALUE_SET_PREFIX + "=" + sctid))
                .name(string("SNOMED CT Concept " + sctid + " and descendants"))
                .description(Markdown.of("All SNOMED CT concepts for " + sctid))
                .copyright(Markdown.of(SNOMED_COPYRIGHT))
                .status(PublicationStatus.ACTIVE)
                .compose(Compose.builder()
                    .include(Include.builder()
                        .system(Uri.of(SNOMED_URL))
                        .filter(Filter.builder()
                            .property(Code.of("concept"))
                            .op(FilterOperator.IS_A)
                            .value(string(sctid))
                            .build())
                        .build())
                    .build())
                .build();
    }

    private static CodeSystem loadCodeSystem() {
        try (InputStream in = SnomedRegistryResourceProvider.class.getClassLoader().getResourceAsStream("snomed/codesystem-snomedct.json")) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}

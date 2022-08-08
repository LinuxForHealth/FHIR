/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.term;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.linuxforhealth.fhir.model.util.ModelSupport.FHIR_STRING;
import static org.linuxforhealth.fhir.server.spi.operation.FHIROperationUtil.getOutputParameters;

import java.time.ZoneOffset;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.CodeSystem.Concept;
import org.linuxforhealth.fhir.model.resource.ConceptMap;
import org.linuxforhealth.fhir.model.resource.ConceptMap.Group;
import org.linuxforhealth.fhir.model.resource.ConceptMap.Group.Element;
import org.linuxforhealth.fhir.model.resource.ConceptMap.Group.Element.Target;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.ConceptMapEquivalence;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;
import org.linuxforhealth.fhir.term.service.exception.FHIRTermServiceException;

/**
 * An experimental implementation of the ConceptMap closure operation that does not support versioning or playback
 */
public class ClosureOperation extends AbstractTermOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/ConceptMap-closure", OperationDefinition.class);
    }

    @Override
    protected Parameters doInvoke(
            FHIROperationContext operationContext,
            Class<? extends Resource> resourceType,
            String logicalId,
            String versionId,
            Parameters parameters,
            FHIRResourceHelpers resourceHelper,
            SearchHelper searchHelper) throws FHIROperationException {
        try {
            String name = getName(parameters);
            Set<Coding> codingSet = getCodingSet(parameters);
            if (codingSet.stream().anyMatch(coding -> coding.getSystem() == null || coding.getCode() == null)) {
                throw buildExceptionWithIssue("Parameter(s) named 'concept' must have both a system and a code present", IssueType.INVALID);
            }
            Map<Coding, Set<Concept>> result = service.closure(codingSet);
            if (result.isEmpty()) {
                throw buildExceptionWithIssue("Closure cannot be computed for the provided input parameters", IssueType.NOT_SUPPORTED);
            }
            for (Coding coding : result.keySet()) {
                Set<Concept> concepts = result.get(coding);
                if (concepts.isEmpty()) {
                    throw buildExceptionWithIssue(String.format("Closure cannot be computed for concept '%s' from system '%s'", coding.getCode().getValue(), coding.getSystem().getValue()), IssueType.NOT_SUPPORTED);
                }
            }
            ConceptMap conceptMap = buildConceptMap(name, result);
            return getOutputParameters(conceptMap);
        } catch (FHIROperationException e) {
            throw e;
        } catch (FHIRTermServiceException e) {
            throw new FHIROperationException(e.getMessage(), e.getCause()).withIssue(e.getIssues());
        } catch (UnsupportedOperationException e) {
            throw buildExceptionWithIssue(e.getMessage(), IssueType.NOT_SUPPORTED, e);
        } catch (Exception e) {
            throw new FHIROperationException("An error occurred during the ConceptMap closure operation", e);
        }
    }

    private String getName(Parameters parameters) {
        return getParameter(parameters, "name").getValue().as(FHIR_STRING).getValue();
    }

    private Set<Coding> getCodingSet(Parameters parameters) {
        return getParameters(parameters, "concept").stream()
                .map(parameter -> parameter.getValue().as(Coding.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private ConceptMap buildConceptMap(String name, Map<Coding, Set<Concept>> result) {
        return ConceptMap.builder()
                .id(UUID.randomUUID().toString())
                .version(string("1"))
                .name(string("Updates for Closure Table " + name))
                .status(PublicationStatus.ACTIVE)
                .experimental(Boolean.TRUE)
                .date(DateTime.now(ZoneOffset.UTC))
                .group(getSystemSet(result.keySet()).stream()
                    .map(system -> Group.builder()
                        .source(Uri.of(system))
                        .target(Uri.of(system))
                        .element(getEntrySet(result, system).stream()
                            .map(entry -> Element.builder()
                                .code(entry.getKey().getCode())
                                .display(entry.getKey().getDisplay())
                                .target(entry.getValue().stream()
                                    .map(concept -> Target.builder()
                                        .code(concept.getCode())
                                        .equivalence(entry.getKey().getCode().equals(concept.getCode()) ?
                                                ConceptMapEquivalence.EQUAL : ConceptMapEquivalence.SPECIALIZES)
                                        .display(concept.getDisplay())
                                        .build())
                                    .collect(Collectors.toList()))
                                .build())
                            .collect(Collectors.toList()))
                        .build())
                    .collect(Collectors.toList()))
                .build();
    }

    private Set<String> getSystemSet(Set<Coding> codingSet) {
        return codingSet.stream()
                .map(coding -> coding.getSystem().getValue())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<Entry<Coding, Set<Concept>>> getEntrySet(Map<Coding, Set<Concept>> result, String system) {
        return result.entrySet().stream()
                .filter(entry -> entry.getKey().getSystem().getValue().equals(system))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
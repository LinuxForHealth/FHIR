/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.ConceptMap;
import com.ibm.fhir.model.resource.ConceptMap.Group.Element.Target;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.registry.FHIRRegistry;

public final class ConceptMapSupport {
    private ConceptMapSupport() { }

    public static List<Target> translate(ConceptMap conceptMap, Coding coding) {
        return conceptMap.getGroup().stream()
                .filter(group -> group.getSource() != null)
                .filter(group -> group.getSource().equals(coding.getSystem()))
                .filter(group -> group.getSourceVersion() == null ||
                    coding.getVersion() == null ||
                    group.getSourceVersion().equals(coding.getVersion()))
                .map(group -> group.getElement())
                .flatMap(List::stream)
                .filter(element -> element.getCode() != null)
                .filter(element -> element.getCode().equals(coding.getCode()))
                .map(element -> element.getTarget())
                .flatMap(List::stream)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    public static ConceptMap getConceptMap(String url) {
        return FHIRRegistry.getInstance().getResource(url, ConceptMap.class);
    }
}

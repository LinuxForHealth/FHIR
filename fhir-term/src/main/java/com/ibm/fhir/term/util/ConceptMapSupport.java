/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.util;

import static com.ibm.fhir.model.type.String.string;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.ConceptMap;
import com.ibm.fhir.model.resource.ConceptMap.Group;
import com.ibm.fhir.model.resource.ConceptMap.Group.Element;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.term.spi.TranslationOutcome;
import com.ibm.fhir.term.spi.TranslationOutcome.Match;

public final class ConceptMapSupport {
    private ConceptMapSupport() { }

    public static TranslationOutcome translate(ConceptMap conceptMap, Coding coding) {
        List<Match> match = conceptMap.getGroup().stream()
                .filter(group -> group.getSource() != null)
                .filter(group -> group.getSource().equals(coding.getSystem()))
                .filter(group -> group.getSourceVersion() == null ||
                    coding.getVersion() == null ||
                    group.getSourceVersion().equals(coding.getVersion()))
                .map(group -> group.toBuilder()
                    .element(group.getElement().stream()
                        .filter(element -> element.getCode() != null)
                        .filter(element -> element.getCode().equals(coding.getCode()))
                        .collect(Collectors.toList()))
                    .build())
                .filter(group -> !group.getElement().isEmpty())
                .map(group -> match(getSource(conceptMap), group))
                .flatMap(List::stream)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        return TranslationOutcome.builder()
                .result(!match.isEmpty() ? Boolean.TRUE : Boolean.FALSE)
                .message(match.isEmpty() ? string("no matches") : null)
                .match(match)
                .build();
    }

    private static List<Match> match(Uri source, Group group) {
        return group.getElement().stream()
                .map(element -> match(source, group.getTarget(), group.getTargetVersion(), element))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static List<Match> match(Uri source, Uri system, String version, Element element) {
        return element.getTarget().stream()
                .map(target -> Match.builder()
                    .equivalence(target.getEquivalence())
                    .concept(Coding.builder()
                        .system(system)
                        .version(version)
                        .code(target.getCode())
                        .display(target.getDisplay())
                        .build())
                    .source(source)
                    .build())
                .collect(Collectors.toList());
    }

    private static Uri getSource(ConceptMap conceptMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(conceptMap.getUrl().getValue());
        if (conceptMap.getVersion() != null) {
            sb.append("|").append(conceptMap.getVersion().getValue());
        }
        return Uri.of(sb.toString());
    }

    public static ConceptMap getConceptMap(java.lang.String url) {
        return FHIRRegistry.getInstance().getResource(url, ConceptMap.class);
    }
}

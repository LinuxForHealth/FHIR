/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.service.test;

import static org.linuxforhealth.fhir.term.util.CodeSystemSupport.isCaseSensitive;
import static org.linuxforhealth.fhir.term.util.CodeSystemSupport.normalize;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.CodeSystem;
import org.linuxforhealth.fhir.model.resource.CodeSystem.Concept;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.term.util.CodeSystemSupport;

public class CodeSystemSupportTest {
    @Test
    public void testGetAncestorsAndSelf1() {
        CodeSystem codeSystem = CodeSystemSupport.getCodeSystem("http://example.com/fhir/CodeSystem/cs5|1.0.0");
        Set<String> actual = CodeSystemSupport.getAncestorsAndSelf(codeSystem, Code.of("r"));
        Set<String> expected = new HashSet<>(Arrays.asList("r", "p", "m"));
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetAncestorsAndSelf2() {
        // hierarchy meaning is not defined for this code system
        CodeSystem codeSystem = CodeSystemSupport.getCodeSystem("http://terminology.hl7.org/CodeSystem/condition-clinical");
        Set<String> actual = CodeSystemSupport.getAncestorsAndSelf(codeSystem, Code.of("relapse"));
        Set<String> expected = new HashSet<>(Arrays.asList("relapse", "active"));
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetDescendantsAndSelf1() {
        CodeSystem codeSystem = CodeSystemSupport.getCodeSystem("http://example.com/fhir/CodeSystem/cs5|1.0.0");
        Set<String> actual = CodeSystemSupport.getDescendantsAndSelf(codeSystem, Code.of("m"));
        Set<String> expected = new HashSet<>(Arrays.asList("m", "p", "q", "r"));
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetDescendantsAndSelf2() {
        // hierarchy meaning is not defined for this code system
        CodeSystem codeSystem = CodeSystemSupport.getCodeSystem("http://terminology.hl7.org/CodeSystem/condition-clinical");
        Set<String> actual = CodeSystemSupport.getDescendantsAndSelf(codeSystem, Code.of("active"));
        Set<String> expected = new HashSet<>(Arrays.asList("active", "recurrence", "relapse"));
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithCodeValueFunction() {
        CodeSystem codeSystem = CodeSystemSupport.getCodeSystem("http://example.com/fhir/CodeSystem/test");

        Function<Concept, String> function = CodeSystemSupport.getCodeValueFunction(codeSystem);
        Set<String> actual = CodeSystemSupport.getConcepts(codeSystem, function);

        Set<Concept> concepts = CodeSystemSupport.getConcepts(codeSystem);
        Set<String> expected = concepts.stream()
                .map(concept -> isCaseSensitive(codeSystem) ?
                        concept.getCode().getValue() :
                        normalize(concept.getCode().getValue()))
                .collect(Collectors.toSet());

        Assert.assertEquals(actual, expected);
    }
}

/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.term.util.ValueSetSupport.getContains;
import static com.ibm.fhir.term.util.ValueSetSupport.getValueSet;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.ConceptSubsumptionOutcome;
import com.ibm.fhir.term.service.FHIRTermService;
import com.ibm.fhir.term.util.CodeSystemSupport;

public class FHIRTermServiceTest {
    @Test
    public void testExpand1() throws Exception {
        ValueSet expanded = FHIRTermService.getInstance().expand(getValueSet("http://ibm.com/fhir/ValueSet/vs1|1.0.0"));

        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());

        Assert.assertEquals(actual, Arrays.asList("a", "b", "c"));
    }

    @Test
    public void testExpand2() throws Exception {
        ValueSet expanded = FHIRTermService.getInstance().expand(getValueSet("http://ibm.com/fhir/ValueSet/vs2|1.0.0"));

        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());

        Assert.assertEquals(actual, Arrays.asList("a", "b", "c", "d", "e"));
    }

    @Test
    public void testExpand3() throws Exception {
        ValueSet expanded = FHIRTermService.getInstance().expand(getValueSet("http://ibm.com/fhir/ValueSet/vs3|1.0.0"));

        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());

        Assert.assertEquals(actual, Arrays.asList("g", "x", "h", "i"));
    }

    @Test
    public void testExpand4() throws Exception {
        ValueSet expanded = FHIRTermService.getInstance().expand(getValueSet("http://ibm.com/fhir/ValueSet/vs4|1.0.0"));

        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());

        Assert.assertEquals(actual, Arrays.asList("j", "l", "a", "b", "d", "m", "p", "q", "s", "o", "t", "u"));
    }

    @Test
    public void testExpand5() throws Exception {
        ValueSet expanded = FHIRTermService.getInstance().expand(getValueSet("http://ibm.com/fhir/ValueSet/vs5|1.0.0"));

        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());

        Assert.assertEquals(actual, Arrays.asList("m", "p", "q", "s", "o", "t", "u"));
    }
    
    @Test
    public void testLookup() throws Exception {
        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("t"))
                .build();

        Concept concept = FHIRTermService.getInstance().lookup(coding);

        assertNotNull(concept);
        assertEquals(concept.getCode().getValue(), "t");
        assertTrue(CodeSystemSupport.hasConceptProperty(concept, Code.of("property1")));
    }

    @Test
    public void testSubsumes1() throws Exception {
        Coding codingA = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("t"))
                .build();

        Coding codingB = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("t"))
                .build();

        ConceptSubsumptionOutcome outcome = FHIRTermService.getInstance().subsumes(codingA, codingB);

        assertEquals(outcome, ConceptSubsumptionOutcome.EQUIVALENT);
    }

    @Test
    public void testSubsumes2() throws Exception {
        Coding codingA = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("t"))
                .build();

        Coding codingB = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("u"))
                .build();

        ConceptSubsumptionOutcome outcome = FHIRTermService.getInstance().subsumes(codingA, codingB);

        assertEquals(outcome, ConceptSubsumptionOutcome.SUBSUMES);
    }

    @Test
    public void testSubsumes3() throws Exception {
        Coding codingA = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("u"))
                .build();

        Coding codingB = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("t"))
                .build();

        ConceptSubsumptionOutcome outcome = FHIRTermService.getInstance().subsumes(codingA, codingB);

        assertEquals(outcome, ConceptSubsumptionOutcome.SUBSUMED_BY);
    }

    @Test
    public void testSubsumes4() throws Exception {
        Coding codingA = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("o"))
                .build();

        Coding codingB = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("t"))
                .build();

        ConceptSubsumptionOutcome outcome = FHIRTermService.getInstance().subsumes(codingA, codingB);

        assertEquals(outcome, ConceptSubsumptionOutcome.NOT_SUBSUMED);
    }

    @Test
    public void testClosure() throws Exception {
        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("m"))
                .build();

        Set<Concept> closure = FHIRTermService.getInstance().closure(coding);

        List<String> actual = closure.stream()
                .map(contains -> contains.getCode().getValue())
                .collect(Collectors.toList());

        Assert.assertEquals(actual, Arrays.asList("m", "p", "q", "r"));
    }
}

/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.util.FHIRUtil.STRING_DATA_ABSENT_REASON_UNKNOWN;
import static com.ibm.fhir.term.util.CodeSystemSupport.getCodeSystem;
import static com.ibm.fhir.term.util.ConceptMapSupport.getConceptMap;
import static com.ibm.fhir.term.util.ValueSetSupport.getContains;
import static com.ibm.fhir.term.util.ValueSetSupport.getValueSet;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.ConceptMap;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.ConceptMapEquivalence;
import com.ibm.fhir.model.type.code.ConceptSubsumptionOutcome;
import com.ibm.fhir.term.service.FHIRTermService;
import com.ibm.fhir.term.service.LookupOutcome;
import com.ibm.fhir.term.service.TranslationOutcome;
import com.ibm.fhir.term.service.ValidationOutcome;
import com.ibm.fhir.term.service.TranslationOutcome.Match;

public class FHIRTermServiceTest {
    @Test
    public void testExpand1() throws Exception {
        ValueSet expanded = FHIRTermService.getInstance().expand(getValueSet("http://ibm.com/fhir/ValueSet/vs1|1.0.0"));

        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());

        assertEquals(actual, Arrays.asList("a", "b", "c"));
    }

    @Test
    public void testExpand2() throws Exception {
        ValueSet expanded = FHIRTermService.getInstance().expand(getValueSet("http://ibm.com/fhir/ValueSet/vs2|1.0.0"));

        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());

        assertEquals(actual, Arrays.asList("a", "b", "c", "d", "e"));
    }

    @Test
    public void testExpand3() throws Exception {
        ValueSet expanded = FHIRTermService.getInstance().expand(getValueSet("http://ibm.com/fhir/ValueSet/vs3|1.0.0"));

        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());

        assertEquals(actual, Arrays.asList("g", "x", "h", "i"));
    }

    @Test
    public void testExpand4() throws Exception {
        ValueSet expanded = FHIRTermService.getInstance().expand(getValueSet("http://ibm.com/fhir/ValueSet/vs4|1.0.0"));

        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());

        assertEquals(actual, Arrays.asList("j", "l", "a", "b", "d", "m", "p", "q", "s", "o", "t", "u"));
    }

    @Test
    public void testExpand5() throws Exception {
        ValueSet expanded = FHIRTermService.getInstance().expand(getValueSet("http://ibm.com/fhir/ValueSet/vs5|1.0.0"));

        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());

        assertEquals(actual, Arrays.asList("m", "p", "q", "s", "o", "t", "u"));
    }

    @Test
    public void testLookup() throws Exception {
        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("t"))
                .build();

        LookupOutcome outcome = FHIRTermService.getInstance().lookup(coding);

        assertNotNull(outcome);
        assertTrue(outcome.getProperty().stream().anyMatch(property -> "property1".equals(property.getCode().getValue())));
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
    public void testClosure1() throws Exception {
        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("m"))
                .build();

        Set<Concept> closure = FHIRTermService.getInstance().closure(coding);

        List<String> actual = closure.stream()
                .map(contains -> contains.getCode().getValue())
                .collect(Collectors.toList());

        assertEquals(actual, Arrays.asList("m", "p", "q", "r"));
    }

    @Test
    public void testClosure2() throws Exception {
        Coding coding1 = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("m"))
                .build();

        Coding coding2 = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("n"))
                .build();

        Map<Coding, Set<Concept>> closure = FHIRTermService.getInstance().closure(new HashSet<>(Arrays.asList(coding1, coding2)));

        Set<String> actual1 = closure.get(coding1).stream()
                .map(contains -> contains.getCode().getValue())
                .collect(Collectors.toSet());

        Set<String> actual2 = closure.get(coding2).stream()
                .map(contains -> contains.getCode().getValue())
                .collect(Collectors.toSet());

        assertEquals(actual1, new HashSet<>(Arrays.asList("m", "p", "q", "r")));
        assertEquals(actual2, new HashSet<>(Arrays.asList("n", "s")));
    }

    @Test
    public void testValidateCode1() throws Exception {
        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("m"))
                .build();

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.TRUE)
                .display(STRING_DATA_ABSENT_REASON_UNKNOWN)
                .build();

        CodeSystem codeSystem = getCodeSystem("http://ibm.com/fhir/CodeSystem/cs5");

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(codeSystem, coding);

        assertEquals(actual, expected);
    }

    @Test
    public void testValidateCode2() throws Exception {
        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("x"))
                .build();

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.FALSE)
                .message(string("Code 'x' is invalid"))
                .build();

        CodeSystem codeSystem = getCodeSystem("http://ibm.com/fhir/CodeSystem/cs5");

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(codeSystem, coding);

        assertEquals(actual, expected);
    }

    @Test
    public void testValidateCode3() throws Exception {
        ValueSet valueSet = getValueSet("http://ibm.com/fhir/ValueSet/vs5|1.0.0");

        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("m"))
                .build();

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.TRUE)
                .display(STRING_DATA_ABSENT_REASON_UNKNOWN)
                .build();

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(valueSet, coding);

        assertEquals(actual, expected);
    }

    @Test
    public void testValidateCode4() throws Exception {
        ValueSet valueSet = getValueSet("http://ibm.com/fhir/ValueSet/vs5|1.0.0");

        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("x"))
                .build();

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.FALSE)
                .message(string("Code 'x' is invalid"))
                .build();

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(valueSet, coding);

        assertEquals(actual, expected);
    }

    @Test
    public void testValidateCode5() throws Exception {
        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs1"))
                .version(string("1.0.0"))
                .code(Code.of("a"))
                .display(string("Concept a"))
                .build();

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.TRUE)
                .display(string("Concept a"))
                .build();

        CodeSystem codeSystem = getCodeSystem("http://ibm.com/fhir/CodeSystem/cs1");

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(codeSystem, coding);

        assertEquals(actual, expected);
    }

    @Test
    public void testValidateCode6() throws Exception {
        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs1"))
                .version(string("1.0.0"))
                .code(Code.of("a"))
                .display(string("concept a"))
                .build();

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.FALSE)
                .message(string("The display 'concept a' is incorrect for code 'a' from code system 'http://ibm.com/fhir/CodeSystem/cs1'"))
                .display(string("Concept a"))
                .build();

        CodeSystem codeSystem = getCodeSystem("http://ibm.com/fhir/CodeSystem/cs1");

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(codeSystem, coding);

        assertEquals(actual, expected);
    }

    @Test
    public void testValidateCode7() throws Exception {
        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs2"))
                .version(string("1.0.0"))
                .code(Code.of("d"))
                .display(string("concept d"))
                .build();

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.TRUE)
                .display(string("Concept d"))
                .build();

        CodeSystem codeSystem = getCodeSystem("http://ibm.com/fhir/CodeSystem/cs2");

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(codeSystem, coding);

        assertEquals(actual, expected);
    }

    @Test
    public void testValidateCode8() throws Exception {
        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs2"))
                .version(string("1.0.0"))
                .code(Code.of("d"))
                .display(string("CONCEPT D"))
                .build();

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.TRUE)
                .display(string("Concept d"))
                .build();

        CodeSystem codeSystem = getCodeSystem("http://ibm.com/fhir/CodeSystem/cs2");

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(codeSystem, coding);

        assertEquals(actual, expected);
    }

    @Test
    public void testValidateCode9() throws Exception {
        ValueSet valueSet = getValueSet("http://ibm.com/fhir/ValueSet/vs1|1.0.0");

        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs1"))
                .version(string("1.0.0"))
                .code(Code.of("a"))
                .display(string("Concept a"))
                .build();

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.TRUE)
                .display(string("Concept a"))
                .build();

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(valueSet, coding);

        assertEquals(actual, expected);
    }

    @Test
    public void testValidateCode10() throws Exception {
        ValueSet valueSet = getValueSet("http://ibm.com/fhir/ValueSet/vs1|1.0.0");

        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs1"))
                .version(string("1.0.0"))
                .code(Code.of("a"))
                .display(string("CONCEPT A"))
                .build();

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.FALSE)
                .message(string("The display 'CONCEPT A' is incorrect for code 'a' from code system 'http://ibm.com/fhir/CodeSystem/cs1'"))
                .display(string("Concept a"))
                .build();

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(valueSet, coding);

        System.out.println(actual.toParameters());

        assertEquals(actual, expected);
    }

    @Test
    public void testValidateCode11() throws Exception {
        ValueSet valueSet = getValueSet("http://ibm.com/fhir/ValueSet/vs1|1.0.0");

        Coding coding = Coding.builder()
                .code(Code.of("a"))
                .display(string("CONCEPT A"))
                .build();

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.FALSE)
                .message(string("Code 'a' is invalid"))
                .build();

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(valueSet, coding);

        assertEquals(actual, expected);
    }

    @Test
    public void testValidateCode12() throws Exception {
        ValueSet valueSet = getValueSet("http://ibm.com/fhir/ValueSet/vs1|1.0.0");

        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs1"))
                .display(string("CONCEPT A"))
                .build();

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.FALSE)
                .build();

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(valueSet, coding);

        assertEquals(actual, expected);
    }

    @Test
    public void testValidateCode13() throws Exception {
        ValueSet valueSet = getValueSet("http://ibm.com/fhir/ValueSet/vs1|1.0.0");

        Code code = Code.of("a");

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.TRUE)
                .build();

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(valueSet, code);

        assertEquals(actual, expected);
    }

    @Test
    public void testValidateCode14() throws Exception {
        ValueSet valueSet = getValueSet("http://ibm.com/fhir/ValueSet/vs1|1.0.0");

        Code code = Code.of("x");

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.FALSE)
                .message(string("Code 'x' is invalid"))
                .build();

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(valueSet, code);

        assertEquals(actual, expected);
    }

    @Test
    public void testValidateCode15() throws Exception {
        ValueSet valueSet = getValueSet("http://ibm.com/fhir/ValueSet/vs1|1.0.0");

        Code code = Code.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build();

        ValidationOutcome expected = ValidationOutcome.builder()
                .result(Boolean.FALSE)
                .message(string("Code 'null' is invalid"))
                .build();

        ValidationOutcome actual = FHIRTermService.getInstance().validateCode(valueSet, code);

        assertEquals(actual, expected);
    }

    @Test
    public void testTranslate1() throws Exception {
        ConceptMap conceptMap = getConceptMap("http://ibm.com/fhir/ConceptMap/snomed-ucum");

        Coding coding = Coding.builder()
                .system(Uri.of("http://snomed.info/sct"))
                .code(Code.of("258672001"))
                .build();

        TranslationOutcome expected = TranslationOutcome.builder()
                .result(Boolean.TRUE)
                .match(Collections.singletonList(Match.builder()
                    .equivalence(ConceptMapEquivalence.EQUIVALENT)
                    .concept(Coding.builder()
                        .system(Uri.of("http://unitsofmeasure.org"))
                        .version(string("2015"))
                        .code(Code.of("cm"))
                        .build())
                    .build()))
                .build();

        TranslationOutcome outcome = FHIRTermService.getInstance().translate(conceptMap, coding);

        assertEquals(outcome, expected);
    }

    @Test
    public void testTranslate2() throws Exception {
        ConceptMap conceptMap = getConceptMap("http://ibm.com/fhir/ConceptMap/snomed-ucum");

        Coding coding = Coding.builder()
                .system(Uri.of("http://snomed.info/sct"))
                .code(Code.of("258773002"))
                .build();

        TranslationOutcome expected = TranslationOutcome.builder()
                .result(Boolean.TRUE)
                .match(Collections.singletonList(Match.builder()
                    .equivalence(ConceptMapEquivalence.EQUIVALENT)
                    .concept(Coding.builder()
                        .system(Uri.of("http://unitsofmeasure.org"))
                        .version(string("2015"))
                        .code(Code.of("mL"))
                        .build())
                    .build()))
                .build();

        TranslationOutcome outcome = FHIRTermService.getInstance().translate(conceptMap, coding);

        assertEquals(outcome, expected);
    }
}

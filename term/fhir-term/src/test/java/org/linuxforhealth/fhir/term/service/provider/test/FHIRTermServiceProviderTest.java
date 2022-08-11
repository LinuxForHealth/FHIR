/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.service.provider.test;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.CodeSystem;
import org.linuxforhealth.fhir.model.resource.CodeSystem.Concept;
import org.linuxforhealth.fhir.model.resource.ValueSet.Compose.Include.Filter;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.code.FilterOperator;
import org.linuxforhealth.fhir.term.spi.FHIRTermServiceProvider;
import org.linuxforhealth.fhir.term.util.CodeSystemSupport;

public abstract class FHIRTermServiceProviderTest {
    protected CodeSystem codeSystem;
    protected FHIRTermServiceProvider provider;

    @BeforeClass
    public void beforeClass() throws Exception {
        codeSystem = CodeSystemSupport.getCodeSystem("http://example.com/fhir/CodeSystem/test");
        provider = createProvider();
    }

    public abstract FHIRTermServiceProvider createProvider() throws Exception;

    @Test
    public void testGetConcept() {
        Concept expected = CodeSystemSupport.findConcept(codeSystem, Code.of("a")).toBuilder()
                .concept(Collections.emptyList())
                .build();
        Assert.assertEquals(provider.getConcept(codeSystem, Code.of("a")), expected);
        Assert.assertNull(provider.getConcept(codeSystem, Code.of("zzz")));
    }

    @Test
    public void testGetConceptCaseSensitive() {
        CodeSystem caseSensitiveCodeSystem = codeSystem.toBuilder().caseSensitive(Boolean.TRUE).build();
        Assert.assertNotNull(provider.getConcept(caseSensitiveCodeSystem, Code.of("a")));
        Assert.assertNull(provider.getConcept(caseSensitiveCodeSystem, Code.of("A")));
    }

    @Test
    public void testGetConceptCaseInsensitive() {
        CodeSystem caseInsensitiveCodeSystem = codeSystem.toBuilder().caseSensitive(Boolean.FALSE).build();
        Assert.assertNotNull(provider.getConcept(caseInsensitiveCodeSystem, Code.of("a")));
        Assert.assertNotNull(provider.getConcept(caseInsensitiveCodeSystem, Code.of("A")));
    }

    @Test
    public void testGetConcepts() {
        Set<Concept> actual = new LinkedHashSet<>();
        for (Concept concept : provider.getConcepts(codeSystem)) {
            actual.add(provider.getConcept(codeSystem, concept.getCode()));
        }

        Set<Concept> expected = new LinkedHashSet<>();
        for (Concept concept : CodeSystemSupport.getConcepts(codeSystem)) {
            expected.add(concept.toBuilder()
                .concept(Collections.emptyList())
                .build());
        }

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithDescendantOfFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("concept"))
                .op(FilterOperator.DESCENDENT_OF)
                .value(string("d"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("q", "r", "s");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithParentEqualsFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("parent"))
                .op(FilterOperator.EQUALS)
                .value(string("d"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("q");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithChildEqualsFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("child"))
                .op(FilterOperator.EQUALS)
                .value(string("q"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("d");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithMultipleChildEqualsFilter() {
        Set<Concept> concepts = provider.getConcepts(codeSystem, Arrays.asList(
            Filter.builder()
                .property(Code.of("child"))
                .op(FilterOperator.EQUALS)
                .value(string("g"))
                .build(),
            Filter.builder()
                .property(Code.of("child"))
                .op(FilterOperator.EQUALS)
                .value(string("h"))
                .build()));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        // intersection
        List<String> expected = Arrays.asList("a");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithMultipleParentEqualsFilter() {
        Set<Concept> concepts = provider.getConcepts(codeSystem, Arrays.asList(
            Filter.builder()
                .property(Code.of("parent"))
                .op(FilterOperator.EQUALS)
                .value(string("a"))
                .build(),
            Filter.builder()
                .property(Code.of("parent"))
                .op(FilterOperator.EQUALS)
                .value(string("a"))
                .build()));

        Set<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toSet());

        // intersection
        Set<String> expected = new HashSet<>(Arrays.asList("g", "h", "i"));

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithBooleanPropertyEqualsFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("booleanProperty"))
                .op(FilterOperator.EQUALS)
                .value(string("true"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("o");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithCodePropertyEqualsFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("codeProperty"))
                .op(FilterOperator.EQUALS)
                .value(string("codeValue"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("y");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithDateTimePropertyEqualsFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("dateTimeProperty"))
                .op(FilterOperator.EQUALS)
                .value(string("2021-01-01T00:00:00.000Z"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("k");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithDecimalPropertyEqualsFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("decimalProperty"))
                .op(FilterOperator.EQUALS)
                .value(string("-101.01"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("n");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithIntegerPropertyEqualsFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("integerProperty"))
                .op(FilterOperator.EQUALS)
                .value(string("5"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("s");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithStringPropertyEqualsFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("stringProperty"))
                .op(FilterOperator.EQUALS)
                .value(string("stringValue"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("a");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithPropertyExistsFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("stringProperty"))
                .op(FilterOperator.EXISTS)
                .value(string("true"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("a");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithPropertyNotExistsFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("stringProperty"))
                .op(FilterOperator.EXISTS)
                .value(string("false"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        Set<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toSet());

        Set<String> expected = new HashSet<>(Arrays.asList("b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithGeneralizesFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("concept"))
                .op(FilterOperator.GENERALIZES)
                .value(string("s"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        Set<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toSet());

        Set<String> expected = new HashSet<>(Arrays.asList("s", "r", "q", "d"));

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithConceptInFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("concept"))
                .op(FilterOperator.IN)
                .value(string("a,b,c"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("a", "b", "c");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithBooleanPropertyInFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("booleanProperty"))
                .op(FilterOperator.IN)
                .value(string("true,false"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("o");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithCodePropertyInFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("codeProperty"))
                .op(FilterOperator.IN)
                .value(string("a,b,c,codeValue"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("y");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithDateTimePropertyInFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("dateTimeProperty"))
                .op(FilterOperator.IN)
                .value(string("2019-01-01T00:00:00.000Z,2020-01-01T00:00:00.000Z,2021-01-01T00:00:00.000Z"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("k");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithDecimalPropertyInFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("decimalProperty"))
                .op(FilterOperator.IN)
                .value(string("100.0,101.11,-101.11,-101.01"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("n");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithIntegerPropertyInFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("integerProperty"))
                .op(FilterOperator.IN)
                .value(string("1,2,3,4,5"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("s");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithStringPropertyInFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("stringProperty"))
                .op(FilterOperator.IN)
                .value(string("a,b,c,stringValue"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("a");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithIsAFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("concept"))
                .op(FilterOperator.IS_A)
                .value(string("d"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("d", "q", "r", "s");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithMulitpleIsAFilters() {
        Set<Concept> concepts = provider.getConcepts(codeSystem, Arrays.asList(
            Filter.builder()
                .property(Code.of("concept"))
                .op(FilterOperator.IS_A)
                .value(string("d"))
                .build(),
            Filter.builder()
                .property(Code.of("concept"))
                .op(FilterOperator.IS_A)
                .value(string("r"))
                .build()));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("r", "s");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithIsAFilterAndIsNotAFilter() {
        Set<Concept> concepts = provider.getConcepts(codeSystem, Arrays.asList(
            Filter.builder()
                .property(Code.of("concept"))
                .op(FilterOperator.IS_A)
                .value(string("d"))
                .build(),
            Filter.builder()
                .property(Code.of("concept"))
                .op(FilterOperator.IS_NOT_A)
                .value(string("r"))
                .build()));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("d", "q");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithIsAFilterAndDateTimePropertyEqualsFilter() {
        Set<Concept> concepts = provider.getConcepts(codeSystem, Arrays.asList(
            Filter.builder()
                .property(Code.of("concept"))
                .op(FilterOperator.IS_A)
                .value(string("a"))
                .build(),
            Filter.builder()
                .property(Code.of("dateTimeProperty"))
                .op(FilterOperator.EQUALS)
                .value(string("2021-01-01T00:00:00.000Z"))
                .build()));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("k");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithIsAFilterAndNotExistsFilter() {
        Set<Concept> concepts = provider.getConcepts(codeSystem, Arrays.asList(
            Filter.builder()
                .property(Code.of("concept"))
                .op(FilterOperator.IS_A)
                .value(string("a"))
                .build(),
            Filter.builder()
                .property(Code.of("stringProperty"))
                .op(FilterOperator.EXISTS)
                .value(string("false"))
                .build()));

        Set<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toSet());

        Set<String> expected = new HashSet<>(Arrays.asList("g", "h", "i", "j", "k"));

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithIsAFilterAndConceptInFilter() {
        Set<Concept> concepts = provider.getConcepts(codeSystem, Arrays.asList(
            Filter.builder()
                .property(Code.of("concept"))
                .op(FilterOperator.IS_A)
                .value(string("d"))
                .build(),
            Filter.builder()
                .property(Code.of("concept"))
                .op(FilterOperator.IN)
                .value(string("q,s"))
                .build()));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("q", "s");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithIsNotAFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("concept"))
                .op(FilterOperator.IS_NOT_A)
                .value(string("d"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        Set<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toSet());

        Set<String> expected = new HashSet<>(Arrays.asList("a", "b", "c", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "t", "u", "v", "w", "x", "y", "z"));

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithStringPropertyNotInFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("stringProperty"))
                .op(FilterOperator.NOT_IN)
                .value(string("a,b,c"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("a");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithCodePropertRegexFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("codeProperty"))
                .op(FilterOperator.REGEX)
                .value(string(".*code.*"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("y");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetConceptsWithStringPropertyRegexFilter() {
        Filter filter = Filter.builder()
                .property(Code.of("stringProperty"))
                .op(FilterOperator.REGEX)
                .value(string(".*str.*"))
                .build();

        Set<Concept> concepts = provider.getConcepts(codeSystem, Collections.singletonList(filter));

        List<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList("a");

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testIsSupported() {
        Assert.assertTrue(provider.isSupported(codeSystem));
    }

    @Test
    public void testSubsumes() {
        Assert.assertTrue(provider.subsumes(codeSystem, Code.of("d"), Code.of("s")));
        Assert.assertFalse(provider.subsumes(codeSystem, Code.of("a"), Code.of("b")));
    }

    @Test
    public void testHasConcept() {
        Assert.assertTrue(provider.hasConcept(codeSystem, Code.of("a")));
        Assert.assertFalse(provider.hasConcept(codeSystem, Code.of("zzz")));
    }

    @Test
    public void testHasConcepts() {
        Assert.assertTrue(provider.hasConcepts(codeSystem, new HashSet<>(Arrays.asList(Code.of("a"), Code.of("b")))));
        Assert.assertFalse(provider.hasConcepts(codeSystem, new HashSet<>(Arrays.asList(Code.of("a"), Code.of("zzz")))));
    }

    @Test
    public void testClosure1() {
        Set<Concept> concepts = provider.closure(codeSystem, Code.of("d"));

        Set<String> actual = concepts.stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toSet());

        Set<String> expected = new HashSet<>(Arrays.asList("d", "q", "r", "s"));

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testClosure2() {
        Code code1 = Code.of("c");
        Code code2 = Code.of("d");

        Map<Code, Set<Concept>> closureMap = provider.closure(codeSystem, new HashSet<>(Arrays.asList(code1, code2)));

        Set<String> actual1 = closureMap.get(code1).stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toSet());

        Set<String> expected1 = new HashSet<>(Arrays.asList("c", "o", "p"));

        Set<String> actual2 = closureMap.get(code2).stream()
                .map(concept -> concept.getCode().getValue())
                .collect(Collectors.toSet());

        Set<String> expected2 = new HashSet<>(Arrays.asList("d", "q", "r", "s"));

        Assert.assertEquals(actual1, expected1);
        Assert.assertEquals(actual2, expected2);
    }
}

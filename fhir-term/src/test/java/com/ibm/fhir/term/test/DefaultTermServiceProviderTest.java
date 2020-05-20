/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.ConceptSubsumptionOutcome;
import com.ibm.fhir.term.service.provider.DefaultTermServiceProvider;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;
import com.ibm.fhir.term.util.CodeSystemSupport;

public class DefaultTermServiceProviderTest {
    private static final FHIRTermServiceProvider provider = new DefaultTermServiceProvider();

    @Test
    public void testLookup() throws Exception {
        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs5"))
                .version(string("1.0.0"))
                .code(Code.of("t"))
                .build();

        Concept concept = provider.lookup(coding);

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

        ConceptSubsumptionOutcome outcome = provider.subsumes(codingA, codingB);

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

        ConceptSubsumptionOutcome outcome = provider.subsumes(codingA, codingB);

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

        ConceptSubsumptionOutcome outcome = provider.subsumes(codingA, codingB);

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

        ConceptSubsumptionOutcome outcome = provider.subsumes(codingA, codingB);

        assertEquals(outcome, ConceptSubsumptionOutcome.NOT_SUBSUMED);
    }
}

/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.test;

import static org.testng.Assert.assertEquals;

import java.util.Collection;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.CodeSystem;
import org.linuxforhealth.fhir.model.resource.CodeSystem.Concept;
import org.linuxforhealth.fhir.model.resource.ExplanationOfBenefit;
import org.linuxforhealth.fhir.model.resource.ExplanationOfBenefit.Insurance;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.CodeSystemContentMode;
import org.linuxforhealth.fhir.model.type.code.ExplanationOfBenefitStatus;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;
import org.linuxforhealth.fhir.model.type.code.RemittanceOutcome;
import org.linuxforhealth.fhir.model.type.code.Use;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;

public class RepeatFunctionTest {
    /**
     * This test shows that repeat works for multiple inputs and properly collects
     * each child named "concept" in a recursive fashion
     */
    @Test
    public void testRepeatFunction_CodeSystem() throws Exception {
        CodeSystem codeSystem = CodeSystem.builder()
                .status(PublicationStatus.DRAFT)
                .content(CodeSystemContentMode.EXAMPLE)
                .concept(Concept.builder()
                        .code(Code.of("a"))
                        .concept(Concept.builder()
                                .code(Code.of("b"))
                                .concept(Concept.builder()
                                        .code(Code.of("c"))
                                        .build())
                                .build())
                        .build())
                .concept(Concept.builder()
                        .code(Code.of("x"))
                        .concept(Concept.builder()
                                .code(Code.of("y"))
                                .concept(Concept.builder()
                                        .code(Code.of("z"))
                                        .build())
                                .build())
                        .build())
                .build();
        Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator()
                .evaluate(codeSystem, "repeat(concept).code");
        assertEquals(result.size(), 6);
    }

    /**
     * This test shows that {@code repeat(elementName)} behaves differently from {@code descendants().select(elementName)}
     */
    @Test
    public void testRepeatFunction_EoB() throws Exception {
        // I chose EoB because I knew it had a lot of elements named "type" at various levels in the structure
        ExplanationOfBenefit eob = ExplanationOfBenefit.builder()
                .status(ExplanationOfBenefitStatus.DRAFT)
                .type(CodeableConcept.builder().text("test").build())
                .use(Use.CLAIM)
                .patient(Reference.builder().type(Uri.of("Patient")).build())
                .created(DateTime.now())
                .insurer(Reference.builder().type(Uri.of("Organization")).build())
                .provider(Reference.builder().type(Uri.of("PractitionerRole")).build())
                .outcome(RemittanceOutcome.PARTIAL)
                .insurance(Insurance.builder()
                        .focal(false)
                        .coverage(Reference.builder().display("test").build())
                        .build())
                .build();
        Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator().evaluate(eob, "ExplanationOfBenefit.repeat(type)");
        assertEquals(result.size(), 1);
    }
}

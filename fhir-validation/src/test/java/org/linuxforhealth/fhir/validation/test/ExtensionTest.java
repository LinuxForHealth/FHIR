/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.validation.test;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static org.testng.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.profile.ConstraintGenerator;
import org.linuxforhealth.fhir.registry.FHIRRegistry;

public class ExtensionTest {
    @Test
    public void testExtension() throws Exception {
        StructureDefinition structureDefinition = FHIRRegistry.getInstance().getResource("http://example.com/fhir/StructureDefinition/test-extension", StructureDefinition.class);
        ConstraintGenerator generator = new ConstraintGenerator(structureDefinition);

        List<Constraint> constraints = generator.generate();
        constraints.forEach(System.out::println);
        assertEquals(constraints.size(), 2);
        assertEquals(constraints.get(1).expression(), "value.where(isTypeEqual(CodeableConcept)).exists() and value.where(isTypeEqual(CodeableConcept)).all(memberOf('http://example.com/fhir/ValueSet/test-value-set', 'required'))");

        Extension extension = Extension.builder()
                .url("http://example.com/fhir/pdm/StructureDefinition/test-extension")
                .value(string("test"))
                .build();

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> result = evaluator.evaluate(extension, "value.where(isTypeEqual(CodeableConcept)).exists() and value.where(isTypeEqual(CodeableConcept)).all(memberOf('http://example.com/fhir/ValueSet/test-value-set', 'required'))");

        System.out.println("result: " + result);

        assertEquals(result, SINGLETON_FALSE);
    }
}

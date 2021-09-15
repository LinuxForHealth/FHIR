/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static org.testng.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.profile.ConstraintGenerator;
import com.ibm.fhir.registry.FHIRRegistry;

public class ExtensionTest {
    @Test
    public void testExtension() throws Exception {
        StructureDefinition structureDefinition = FHIRRegistry.getInstance().getResource("http://ibm.com/fhir/StructureDefinition/test-extension", StructureDefinition.class);
        ConstraintGenerator generator = new ConstraintGenerator(structureDefinition);

        List<Constraint> constraints = generator.generate();
        constraints.forEach(System.out::println);
        assertEquals(constraints.size(), 2);
        assertEquals(constraints.get(1).expression(), "value.where(is(CodeableConcept)).exists() and value.where(is(CodeableConcept)).all(memberOf('http://ibm.com/fhir/ValueSet/test-value-set', 'required'))");

        Extension extension = Extension.builder()
                .url("http://ibm.com/fhir/pdm/StructureDefinition/test-extension")
                .value(string("test"))
                .build();

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> result = evaluator.evaluate(extension, "value.where(is(CodeableConcept)).exists() and value.where(is(CodeableConcept)).all(memberOf('http://ibm.com/fhir/ValueSet/test-value-set', 'required'))");

        System.out.println("result: " + result);

        assertEquals(result, SINGLETON_FALSE);
    }
}

/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.path.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.time.ZoneOffset;
import java.util.Collection;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.ObservationStatus;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;

public class FHIRPathAsTest {
    @Test
    void testAsOperation() throws Exception {
        Patient patient = Patient.builder()
                                 .deceased(Boolean.TRUE)
                                 .build();

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(patient, "Patient.deceased as dateTime");

        assertEquals(result.size(), 0, "Number of selected nodes");
    }

    @Test
    void testAsOperationSystemValue() throws Exception {
        // Testing 'as'
        Patient patient = Patient.builder()
                .deceased(DateTime.now(ZoneOffset.UTC))
                .build();

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(patient, "Patient.deceased as System.DateTime");

        assertEquals(result.size(), 1, "Number of selected nodes");
    }

    @Test
    void testAsFunction() throws Exception {
        Condition condition = Condition.builder()
                                       .subject(Reference.builder().display(string("dummy reference")).build())
                                       .onset(DateTime.now())
                                       .build();

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(condition, "Condition.onset.as(Age) | Condition.onset.as(Range)");

        assertEquals(result.size(), 0, "Number of selected nodes");
    }

    @Test
    void testResolveAsOperation() throws Exception {
        Patient patient = Patient.builder()
                                 .generalPractitioner(Reference.builder().reference(string("http://example.com/dummyReference")).build())
                                 .build();

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(patient, "Patient.generalPractitioner.resolve() as Basic");

        assertEquals(result.size(), 1, "Number of selected nodes");
    }

    @Test
    void testArrayAsOperation() throws Exception {
        Observation.Component component = Observation.Component.builder()
                .code(CodeableConcept.builder().text(string("value")).build())
                .value(Quantity.builder().value(Decimal.of(1)).build())
                .build();

        Observation obs = Observation.builder()
                .status(ObservationStatus.AMENDED)
                .code(CodeableConcept.builder().text(string("value")).build())
                .component(component)
                .component(component)
                .build();

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(obs, "Observation.component.value as Quantity");

        assertEquals(result.size(), 2, "Number of selected nodes");
    }
}

/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.path.test;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.time.ZoneOffset;
import java.util.Collection;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.Condition;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.type.Age;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Decimal;
import org.linuxforhealth.fhir.model.type.Distance;
import org.linuxforhealth.fhir.model.type.Duration;
import org.linuxforhealth.fhir.model.type.MoneyQuantity;
import org.linuxforhealth.fhir.model.type.Quantity;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.SimpleQuantity;
import org.linuxforhealth.fhir.model.type.code.ObservationStatus;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;

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
    
    @Test
    void testAsTypeEqualsFunction() throws Exception {
        
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        
        Duration duration = Duration.builder().code(Code.of("123")).build();
        Collection<FHIRPathNode> result = evaluator.evaluate(duration, "asTypeEqual(Quantity)");
        assertEquals(result.size(), 0, "Number of selected nodes");
        
        Age age = Age.builder().code(Code.of("123")).build();
        result = evaluator.evaluate(age, "asTypeEqual(Quantity)");
        assertEquals(result.size(), 0, "Number of selected nodes");
        
        Distance distance = Distance.builder().code(Code.of("123")).build();
        result = evaluator.evaluate(distance, "asTypeEqual(Quantity)");
        assertEquals(result.size(), 0, "Number of selected nodes");
        
        MoneyQuantity moneyQuantity = MoneyQuantity.builder().code(Code.of("123")).build();
        result = evaluator.evaluate(moneyQuantity, "asTypeEqual(Quantity)");
        assertEquals(result.size(), 0, "Number of selected nodes");
        
        SimpleQuantity simpleQuantity = SimpleQuantity.builder().code(Code.of("123")).build();
        result = evaluator.evaluate(simpleQuantity, "asTypeEqual(Quantity)");
        assertEquals(result.size(), 0, "Number of selected nodes");
        
    }
    
    @Test
    void testAsDurationFunction() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        
        Quantity quantity = Duration.builder().code(Code.of("123")).build();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "asTypeEqual(Duration)");
        assertEquals(result.size(), 1, "Number of selected nodes");
    }
    
    @Test
    void testAsQuantityFunction() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        
        Duration duration = Duration.builder().code(Code.of("123")).build();
        Collection<FHIRPathNode> result = evaluator.evaluate(duration, "as(Quantity)");
        assertEquals(result.size(), 1, "Number of selected nodes");
        
        Quantity quantityElement = Quantity.builder().code(Code.of("123")).build();
        result = evaluator.evaluate(quantityElement, "as(Duration)");
        assertEquals(result.size(), 0, "Number of selected nodes");
    }
    
    @Test
    void testAsAgeFunction() throws Exception {
        
        Quantity quantity = Age.builder().code(Code.of("20")).build();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "asTypeEqual(Age)");
        assertEquals(result.size(), 1, "Number of selected nodes");
    }
    
    @Test
    void testAsDistanceFunction() throws Exception {
        
        Quantity quantity = Distance.builder().code(Code.of("100")).build();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "asTypeEqual(Distance)");
        assertEquals(result.size(), 1, "Number of selected nodes");
    }
    
    @Test
    void testAsMoneyQuantityFunction() throws Exception {
        
        Quantity quantity = MoneyQuantity.builder().code(Code.of("100")).build();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "asTypeEqual(MoneyQuantity)");
        assertEquals(result.size(), 1, "Number of selected nodes");
    }
    
    @Test
    void testAsSimpleQuantityFunction() throws Exception {
        
        Quantity quantity = SimpleQuantity.builder().code(Code.of("100")).build();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "asTypeEqual(SimpleQuantity)");
        assertEquals(result.size(), 1, "Number of selected nodes");
    }
    
    @Test
    void testQuantityASimpleQuantityType() throws Exception {
        
        Quantity quantity = Quantity.builder().code(Code.of("100")).build();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "asTypeEqual(SimpleQuantity)");
        assertEquals(result.size(), 0, "Number of selected nodes");
    }
}

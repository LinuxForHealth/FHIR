/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.path.test;

import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getSingleton;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import java.util.Collection;

import org.linuxforhealth.fhir.model.type.Age;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Distance;
import org.linuxforhealth.fhir.model.type.Duration;
import org.linuxforhealth.fhir.model.type.MoneyQuantity;
import org.linuxforhealth.fhir.model.type.Quantity;
import org.linuxforhealth.fhir.model.type.SimpleQuantity;
import org.linuxforhealth.fhir.path.FHIRPathBooleanValue;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.testng.annotations.Test;

public class FHIRPathIsTest {
    @Test
    void testIsDurationFunction() throws Exception {
        
        Quantity quantity = Duration.builder().code(Code.of("123")).build();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "is(Duration)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testIsAgeFunction() throws Exception {
        
        Quantity quantity = Age.builder().code(Code.of("20")).build();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "is(Age)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testIsDistanceFunction() throws Exception {
        
        Quantity quantity = Distance.builder().code(Code.of("100")).build();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "is(Distance)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testIsMoneyQuantityFunction() throws Exception {
        
        Quantity quantity = MoneyQuantity.builder().code(Code.of("100")).build();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "is(MoneyQuantity)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testIsSimpleQuantityFunction() throws Exception {
        
        Quantity quantity = SimpleQuantity.builder().code(Code.of("100")).build();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "is(SimpleQuantity)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testIsQuantityASimpleQuantityType() throws Exception {
        
        Quantity quantity = Quantity.builder().code(Code.of("100")).build();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "is(SimpleQuantity)");
        assertFalse(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }

}

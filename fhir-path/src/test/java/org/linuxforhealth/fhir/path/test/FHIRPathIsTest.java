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
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Distance;
import org.linuxforhealth.fhir.model.type.Duration;
import org.linuxforhealth.fhir.model.type.Id;
import org.linuxforhealth.fhir.model.type.Markdown;
import org.linuxforhealth.fhir.model.type.MoneyQuantity;
import org.linuxforhealth.fhir.model.type.Oid;
import org.linuxforhealth.fhir.model.type.PositiveInt;
import org.linuxforhealth.fhir.model.type.Quantity;
import org.linuxforhealth.fhir.model.type.SimpleQuantity;
import org.linuxforhealth.fhir.model.type.UnsignedInt;
import org.linuxforhealth.fhir.model.type.Url;
import org.linuxforhealth.fhir.model.type.Uuid;
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
    
    @Test
    void testIsFunctionForQuantitySubTypes() throws Exception {
        
        Quantity quantity = Duration.builder().code(Code.of("123")).build();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
       
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "is(Duration)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        result = evaluator.evaluate(quantity, "is(Age)");
        assertFalse(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        result = evaluator.evaluate(quantity, "is(Distance)");
        assertFalse(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        result = evaluator.evaluate(quantity, "is(Distance)");
        assertFalse(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        result = evaluator.evaluate(quantity, "is(MoneyQuantity)");
        assertFalse(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        result = evaluator.evaluate(quantity, "is(SimpleQuantity)");
        assertFalse(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        quantity = Quantity.builder().code(Code.of("123")).build();
        
        result = evaluator.evaluate(quantity, "is(Duration)");
        assertFalse(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        Duration duration = Duration.builder().code(Code.of("123")).build();
        result = evaluator.evaluate(duration, "is(Quantity)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testCodeIsStringFunction() throws Exception {
        Code code = Code.of("100");
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(code, "is(string)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testIdIsStringFunction() throws Exception {
        Id id = Id.of("1");
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(id, "is(string)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testMarkdownIsStringFunction() throws Exception {
        Markdown markdown = Markdown.of("markdown");
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(markdown, "is(string)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testCanonicalIsURIFunction() throws Exception {
        Canonical canonical = Canonical.of("http://docs.org");
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(canonical, "is(uri)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testOidIsURIFunction() throws Exception {
        Oid oid = Oid.of("urn:oid:2.16.840.1.113883.4.642.3.850");
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(oid, "is(uri)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testUrlIsURIFunction() throws Exception {
        Url url = Url.of("http://docs.org");
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(url, "is(uri)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testUuidIsURIFunction() throws Exception {
        Uuid uuid = Uuid.of("urn:uuid:efa2beb1-e310-4668-afb5-19c0a4b87a31");
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(uuid, "is(uri)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testPositiveIntIsintegerFunction() throws Exception {
        PositiveInt positiveInt = PositiveInt.of("40");
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(positiveInt, "is(integer)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testUnsignedIntIsintegerFunction() throws Exception {
        UnsignedInt unsignedInt = UnsignedInt.of("40");
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(unsignedInt, "is(unsignedInt)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
    }
    
    @Test
    void testIsTypeEqualsFunction() throws Exception {
        
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        
        Duration duration = Duration.builder().code(Code.of("123")).build();
        Collection<FHIRPathNode> result = evaluator.evaluate(duration, "isTypeEqual(Quantity)");
        assertFalse(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        result = evaluator.evaluate(duration, "isTypeEqual(Duration)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        Age age = Age.builder().code(Code.of("123")).build();
        result = evaluator.evaluate(age, "isTypeEqual(Quantity)");
        assertFalse(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        result = evaluator.evaluate(age, "isTypeEqual(Age)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        
        Distance distance = Distance.builder().code(Code.of("123")).build();
        result = evaluator.evaluate(distance, "isTypeEqual(Quantity)");
        assertFalse(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        result = evaluator.evaluate(distance, "isTypeEqual(Distance)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        MoneyQuantity moneyQuantity = MoneyQuantity.builder().code(Code.of("123")).build();
        result = evaluator.evaluate(moneyQuantity, "isTypeEqual(Quantity)");
        assertFalse(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        result = evaluator.evaluate(moneyQuantity, "isTypeEqual(MoneyQuantity)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        
        SimpleQuantity simpleQuantity = SimpleQuantity.builder().code(Code.of("123")).build();
        result = evaluator.evaluate(simpleQuantity, "isTypeEqual(Quantity)");
        assertFalse(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
        result = evaluator.evaluate(simpleQuantity, "isTypeEqual(SimpleQuantity)");
        assertTrue(getSingleton(result, FHIRPathBooleanValue.class)._boolean());
        
    }
 
}

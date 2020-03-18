/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.test;

import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.*;
import static com.ibm.fhir.model.type.String.string;

import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;

public class MemberOfFunctionTest {            
    @Test
    public void testMemberOfFunction1() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> result = evaluator.evaluate(Code.of("a"), "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')");
        
        Assert.assertEquals(result, SINGLETON_TRUE);
    }
    
    @Test
    public void testMemberOfFunction2() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> result = evaluator.evaluate(Code.of("x"), "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')");
        
        Assert.assertEquals(result, SINGLETON_FALSE);
    }
    
    @Test
    public void testMemberOfFunction3() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        
        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs1"))
                .version(string("1.0.0"))
                .code(Code.of("a"))
                .build();

        Collection<FHIRPathNode> result = evaluator.evaluate(coding, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')");
        
        Assert.assertEquals(result, SINGLETON_TRUE);
    }
    
    @Test
    public void testMemberOfFunction4() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        
        Coding coding = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs1"))
                .version(string("2.0.0"))
                .code(Code.of("a"))
                .build();

        Collection<FHIRPathNode> result = evaluator.evaluate(coding, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')");
        
        Assert.assertEquals(result, SINGLETON_FALSE);
    }
    
    @Test
    public void testMemberOfFunction5() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        
        CodeableConcept codeableConcept = CodeableConcept.builder()
            .coding(Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs1"))
                .version(string("1.0.0"))
                .code(Code.of("a"))
                .build())
            .build();

        Collection<FHIRPathNode> result = evaluator.evaluate(codeableConcept, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')");
        
        Assert.assertEquals(result, SINGLETON_TRUE);
    }
    
    @Test
    public void testMemberOfFunction6() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        
        CodeableConcept codeableConcept = CodeableConcept.builder()
            .coding(Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs1"))
                .version(string("2.0.0"))
                .code(Code.of("a"))
                .build())
            .build();

        Collection<FHIRPathNode> result = evaluator.evaluate(codeableConcept, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')");
        
        Assert.assertEquals(result, SINGLETON_FALSE);
    }
    
    @Test
    public void testMemberOfFunction7() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> result = evaluator.evaluate(Code.of("a"), "$this in 'http://ibm.com/fhir/ValueSet/vs1'");
        
        Assert.assertEquals(result, SINGLETON_TRUE);
    }
}
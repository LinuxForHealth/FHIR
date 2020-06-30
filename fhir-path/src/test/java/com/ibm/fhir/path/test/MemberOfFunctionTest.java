/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;

import java.util.Collection;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathTree;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

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

    @Test
    public void testMemberOfFunction8() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        EvaluationContext evaluationContext = new EvaluationContext(Code.of("x"));
        Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1', 'extensible')");

        Assert.assertEquals(evaluationContext.getIssues().size(), 2);
        Issue issue = evaluationContext.getIssues().get(0);
        Assert.assertEquals(issue.getSeverity(), IssueSeverity.WARNING);
        Assert.assertEquals(issue.getCode(), IssueType.CODE_INVALID);
        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction9() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        EvaluationContext evaluationContext = new EvaluationContext(Code.of("x"));
        Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1', 'preferred')");

        Assert.assertEquals(evaluationContext.getIssues().size(), 2);
        Issue issue = evaluationContext.getIssues().get(0);
        Assert.assertEquals(issue.getSeverity(), IssueSeverity.WARNING);
        Assert.assertEquals(issue.getCode(), IssueType.CODE_INVALID);
        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction10() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> result = evaluator.evaluate(string("a"), "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')");

        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction11() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> result = evaluator.evaluate(string("x"), "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')");

        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction12() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> result = evaluator.evaluate(Uri.of("a"), "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')");

        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction13() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> result = evaluator.evaluate(Uri.of("x"), "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')");

        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction14() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Quantity quantity = Quantity.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs1"))
                .code(Code.of("a"))
                .build();

        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')");

        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction15() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Quantity quantity = Quantity.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs1"))
                .code(Code.of("x"))
                .build();

        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')");

        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction16() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Quantity quantity = Quantity.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs1"))
                .code(Code.of("a"))
                .build();

        EvaluationContext evaluationContext = new EvaluationContext(quantity);
        FHIRPathTree tree = evaluationContext.getTree();
        Collection<FHIRPathNode> initialContext = getChildren(tree.getRoot(), "code");

        Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')", initialContext);

        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction17() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Quantity quantity = Quantity.builder()
                .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs1"))
                .code(Code.of("x"))
                .build();

        EvaluationContext evaluationContext = new EvaluationContext(quantity);
        FHIRPathTree tree = evaluationContext.getTree();
        Collection<FHIRPathNode> initialContext = getChildren(tree.getRoot(), "code");

        Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')", initialContext);

        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    private Collection<FHIRPathNode> getChildren(FHIRPathNode node, String name) {
        return node.children().stream()
                .filter(child -> name.equals(child.name()))
                .collect(Collectors.toList());
    }
}
/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.test;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;

import java.util.Collection;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.Condition;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Quantity;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.FHIRPathTree;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.path.function.MemberOfFunction;

public class MemberOfFunctionTest {
    private static final String ENGLISH_US = "en-US";
    private static final String UNITS_PER_LITER = "U/L";

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

    @Test(enabled = false)
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
        Assert.assertEquals(issue.getSeverity(), IssueSeverity.INFORMATION);
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
        Assert.assertEquals(issue.getSeverity(), IssueSeverity.INFORMATION);
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

    @Test
    public void testMemberOfFunction18() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(Code.of(ENGLISH_US), "$this.memberOf('" + MemberOfFunction.ALL_LANG_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction19() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(Code.of("invalidLanguageCode"), "$this.memberOf('" + MemberOfFunction.ALL_LANG_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction20() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Coding coding = Coding.builder()
                .system(Uri.of(ValidationSupport.BCP_47_URN))
                .code(Code.of(ENGLISH_US))
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(coding, "$this.memberOf('" + MemberOfFunction.ALL_LANG_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction21() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Coding coding = Coding.builder()
                .system(Uri.of("urn:invalid"))
                .code(Code.of(ENGLISH_US))
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(coding, "$this.memberOf('" + MemberOfFunction.ALL_LANG_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction22() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Coding coding = Coding.builder()
                .system(Uri.of(ValidationSupport.BCP_47_URN))
                .code(Code.of("invalidLanguageCode"))
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(coding, "$this.memberOf('" + MemberOfFunction.ALL_LANG_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction23() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        CodeableConcept codeableConcept = CodeableConcept.builder()
                .coding(Coding.builder()
                    .system(Uri.of(ValidationSupport.BCP_47_URN))
                    .code(Code.of(ENGLISH_US))
                    .build())
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(codeableConcept, "$this.memberOf('" + MemberOfFunction.ALL_LANG_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction24() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        CodeableConcept codeableConcept = CodeableConcept.builder()
                .coding(Coding.builder()
                    .system(Uri.of("urn:invalid"))
                    .code(Code.of(ENGLISH_US))
                    .build())
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(codeableConcept, "$this.memberOf('" + MemberOfFunction.ALL_LANG_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction25() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        CodeableConcept codeableConcept = CodeableConcept.builder()
                .coding(Coding.builder()
                    .system(Uri.of(ValidationSupport.BCP_47_URN))
                    .code(Code.of("invalidLanguageCode"))
                    .build())
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(codeableConcept, "$this.memberOf('" + MemberOfFunction.ALL_LANG_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction26() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Quantity quantity = Quantity.builder()
                .system(Uri.of(ValidationSupport.BCP_47_URN))
                .code(Code.of(ENGLISH_US))
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "$this.memberOf('" + MemberOfFunction.ALL_LANG_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction27() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Quantity quantity = Quantity.builder()
                .system(Uri.of("urn:invalid"))
                .code(Code.of(ENGLISH_US))
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "$this.memberOf('" + MemberOfFunction.ALL_LANG_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction28() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Quantity quantity = Quantity.builder()
                .system(Uri.of(ValidationSupport.BCP_47_URN))
                .code(Code.of("invalidLanguageCode"))
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "$this.memberOf('" + MemberOfFunction.ALL_LANG_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction29() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(org.linuxforhealth.fhir.model.type.String.of(ENGLISH_US), "$this.memberOf('" + MemberOfFunction.ALL_LANG_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction30() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(org.linuxforhealth.fhir.model.type.String.of("invalidLanguageCode"), "$this.memberOf('" + MemberOfFunction.ALL_LANG_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction31() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(Code.of(UNITS_PER_LITER), "$this.memberOf('" + MemberOfFunction.UCUM_UNITS_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction32() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(Code.of("invalid ucum code"), "$this.memberOf('" + MemberOfFunction.UCUM_UNITS_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction33() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Coding coding = Coding.builder()
                .system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL))
                .code(Code.of(UNITS_PER_LITER))
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(coding, "$this.memberOf('" + MemberOfFunction.UCUM_UNITS_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction34() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Coding coding = Coding.builder()
                .system(Uri.of("invalid"))
                .code(Code.of(UNITS_PER_LITER))
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(coding, "$this.memberOf('" + MemberOfFunction.UCUM_UNITS_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction35() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Coding coding = Coding.builder()
                .system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL))
                .code(Code.of("invalid ucum code"))
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(coding, "$this.memberOf('" + MemberOfFunction.UCUM_UNITS_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction36() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        CodeableConcept codeableConcept = CodeableConcept.builder()
                .coding(Coding.builder()
                    .system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL))
                    .code(Code.of(UNITS_PER_LITER))
                    .build())
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(codeableConcept, "$this.memberOf('" + MemberOfFunction.UCUM_UNITS_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction37() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        CodeableConcept codeableConcept = CodeableConcept.builder()
                .coding(Coding.builder()
                    .system(Uri.of("invalid"))
                    .code(Code.of(UNITS_PER_LITER))
                    .build())
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(codeableConcept, "$this.memberOf('" + MemberOfFunction.UCUM_UNITS_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction38() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        CodeableConcept codeableConcept = CodeableConcept.builder()
                .coding(Coding.builder()
                    .system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL))
                    .code(Code.of("invalid ucum code"))
                    .build())
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(codeableConcept, "$this.memberOf('" + MemberOfFunction.UCUM_UNITS_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction39() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Quantity quantity = Quantity.builder()
                .system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL))
                .code(Code.of(UNITS_PER_LITER))
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "$this.memberOf('" + MemberOfFunction.UCUM_UNITS_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction40() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Quantity quantity = Quantity.builder()
                .system(Uri.of("invalid"))
                .code(Code.of(UNITS_PER_LITER))
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "$this.memberOf('" + MemberOfFunction.UCUM_UNITS_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction41() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Quantity quantity = Quantity.builder()
                .system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL))
                .code(Code.of("invalid ucum code"))
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "$this.memberOf('" + MemberOfFunction.UCUM_UNITS_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction42() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(org.linuxforhealth.fhir.model.type.String.of(UNITS_PER_LITER), "$this.memberOf('" + MemberOfFunction.UCUM_UNITS_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction43() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(org.linuxforhealth.fhir.model.type.String.of("invalid ucum code"), "$this.memberOf('" + MemberOfFunction.UCUM_UNITS_VALUE_SET_URL + "')");
        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction44() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> result = evaluator.evaluate(Code.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(),
            "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1', 'required')");

        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction45() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> result = evaluator.evaluate(Code.builder().value("x").extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(),
            "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1', 'required')");

        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction46() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> result = evaluator.evaluate(Code.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(),
            "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1')");

        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction47() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> result = evaluator.evaluate(Uri.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(),
            "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1', 'required')");

        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction48() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> result = evaluator.evaluate(Uri.builder().value("x").extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(),
            "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1', 'required')");

        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction49() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        org.linuxforhealth.fhir.model.type.String unknownString = org.linuxforhealth.fhir.model.type.String.builder()
            .extension(Extension.builder()
                .url("http://hl7.org/fhir/StructureDefinition/data-absent-reason")
                .value(Code.of("unknown"))
                .build())
            .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(unknownString, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1', 'required')");

        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction50() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        org.linuxforhealth.fhir.model.type.String unknownStringWithVal = org.linuxforhealth.fhir.model.type.String.builder()
                .value("x")
                .extension(Extension.builder()
                    .url("http://hl7.org/fhir/StructureDefinition/data-absent-reason")
                    .value(Code.of("unknown"))
                    .build())
                .build();
        Collection<FHIRPathNode> result = evaluator.evaluate(unknownStringWithVal,
            "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1', 'required')");

        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction51() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Coding coding = Coding.builder()
                .extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build())
                .build();

        Collection<FHIRPathNode> result = evaluator.evaluate(coding, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1', 'required')");

        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction52() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Coding coding = Coding.builder()
                .extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build())
                .code(Code.of("a"))
                .build();

        Collection<FHIRPathNode> result = evaluator.evaluate(coding, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1', 'required')");

        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction53() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Quantity quantity = Quantity.builder()
                .extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build())
                .build();

        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1', 'required')");

        Assert.assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testMemberOfFunction54() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Quantity quantity = Quantity.builder()
                .extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build())
                .code(Code.of("a"))
                .build();

        Collection<FHIRPathNode> result = evaluator.evaluate(quantity, "$this.memberOf('http://ibm.com/fhir/ValueSet/vs1', 'required')");

        Assert.assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testMemberOfFunction55() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        // we use condition because its a resource that has a repeating
        // CodeableConcept element with no required binding: Condition.category
        Condition condition = Condition.builder()
            .subject(Reference.builder()
                .reference("Patient/test")
                .build())
            .category(CodeableConcept.builder()
                .coding(Coding.builder()
                    .code(Code.of("unknown"))
                    .build())
                .build())
            .category(CodeableConcept.builder()
                .coding(Coding.builder()
                    .system(Uri.of("http://ibm.com/fhir/CodeSystem/cs1"))
                    .version("1.0.0")
                    .code(Code.of("a"))
                    .build())
                .build())
            .build();

        Collection<FHIRPathNode> result = evaluator.evaluate(condition, "Condition.category.where(memberOf('http://ibm.com/fhir/ValueSet/vs1')).exists()");

        Assert.assertEquals(result, SINGLETON_TRUE);
    }


    private Collection<FHIRPathNode> getChildren(FHIRPathNode node, String name) {
        return node.children().stream()
                .filter(child -> name.equals(child.name()))
                .collect(Collectors.toList());
    }
}
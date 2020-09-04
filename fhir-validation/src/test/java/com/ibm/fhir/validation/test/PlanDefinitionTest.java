/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.test;

import static org.testng.Assert.fail;

import java.io.InputStream;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.PlanDefinition;
import com.ibm.fhir.model.resource.PlanDefinition.Action;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Xhtml;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.validation.FHIRValidator;

/**
 * Tests FHIR spec validation of PlanDefinition.
 */
public class PlanDefinitionTest {
    public static void main(String[] args) throws Exception {
        try (InputStream in = PlanDefinitionTest.class.getClassLoader().getResourceAsStream("JSON/plandefinition.json")) {
            PlanDefinition planDefinition = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(planDefinition);
            for (Issue issue : issues) {
                System.out.println("severity: " + issue.getSeverity().getValue() + ", details: " + issue.getDetails().getText().getValue() + ", expression: " + issue.getExpression().get(0).getValue());
            }
            Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator().evaluate(planDefinition, "%resource.descendants().as(canonical)");
            System.out.println("result: " + result);
        }
    }
    
    @Test
    public void testValid() {
        PlanDefinition pd = buildTestPlanDefinition();
        checkForIssuesWithValidation(pd, 0);
    }

    @Test
    public void testValidWithActionSubjectReference() {
        PlanDefinition pd = buildTestPlanDefinition();
        pd = pd.toBuilder().action(Action.builder().subject(Reference.builder().reference(String.of("Group/test-1234")).build()).build()).build();
        checkForIssuesWithValidation(pd, 0);
    }

    @Test
    public void testValidWithActionSubjectCodeableConcept() {
        PlanDefinition pd = buildTestPlanDefinition();
        pd = pd.toBuilder().action(Action.builder().subject(CodeableConcept.builder().coding(Coding.builder().system(Uri.of("http://hl7.org/fhir/resource-types")).code(Code.of("Patient")).build()).build()).build()).build();
        checkForIssuesWithValidation(pd, 0);
    }

    @Test
    public void testWarningWithActionSubjectCodeableConcept() {
        PlanDefinition pd = buildTestPlanDefinition();
        pd = pd.toBuilder().action(Action.builder().subject(CodeableConcept.builder().coding(Coding.builder().system(Uri.of("http://hl7.org/fhir/resource-types")).code(Code.of("Invalid")).build()).build()).build()).build();
        checkForIssuesWithValidation(pd, 1);
    }

    /**
     * Builds a valid PlanDefinition.
     * 
     * @return a valid PlanDefinition
     */
    public static PlanDefinition buildTestPlanDefinition() {
        Meta meta = Meta.builder().versionId(Id.of("1")).lastUpdated(Instant.now(ZoneOffset.UTC)).build();
        return PlanDefinition.builder().meta(meta).status(PublicationStatus.ACTIVE).text(Narrative.builder().div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">loaded from the datastore</div>")).status(NarrativeStatus.GENERATED).build()).build();
    }

    /**
     * Checks for validation issues.
     * 
     * @param resource
     *            the resource
     * @param numWarningsExpected
     *            number of expected validation warnings
     */
    public static void checkForIssuesWithValidation(Resource resource, int numWarningsExpected) {

        List<Issue> issues = Collections.emptyList();
        try {
            issues = FHIRValidator.validator().validate(resource);
        } catch (Exception e) {
            fail("Unable to validate the resource");
        }

        if (!issues.isEmpty()) {
            System.out.println("Printing Issue with Validation");
            int nonWarning = 0;
            int allOtherIssues = 0;
            for (Issue issue : issues) {
                if (IssueSeverity.ERROR.getValue().compareTo(issue.getSeverity().getValue()) == 0
                        || IssueSeverity.FATAL.getValue().compareTo(issue.getSeverity().getValue()) == 0) {
                    nonWarning++;
                } else {
                    allOtherIssues++;
                }
                System.out.println("level: " + issue.getSeverity().getValue() + ", details: "
                        + issue.getDetails().getText().getValue() + ", expression: "
                        + issue.getExpression().get(0).getValue());
            }

            System.out.println("count = [" + issues.size() + "]");

            if (nonWarning > 0) {
                fail("Fail on Errors " + nonWarning);
            }

            if (numWarningsExpected != allOtherIssues) {
                fail("Fail on Warnings " + allOtherIssues);
            }
        } else {
            System.out.println("Passed with no issues in validation");
        }
    }
    
}

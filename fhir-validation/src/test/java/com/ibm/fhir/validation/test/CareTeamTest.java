/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.test;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.CareTeam;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.validation.FHIRValidator;

public class CareTeamTest {
    public static void main(String[] args) throws Exception {
        try (InputStream in = CareTeamTest.class.getClassLoader().getResourceAsStream("JSON/careteam.json")) {
            CareTeam careTeam = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(careTeam);
            for (Issue issue : issues) {
                System.out.println("severity: " + issue.getSeverity().getValue() + ", details: " + issue.getDetails().getText().getValue() + ", expression: " + issue.getExpression().get(0).getValue());
            }

            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
            EvaluationContext evaluationContext = new EvaluationContext(careTeam);

            Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "CareTeam.participant");
            for (FHIRPathNode node : result) {
                result = evaluator.evaluate(evaluationContext, "member.resolve() is Practitioner", node);
                System.out.println("result: " + result);
            }
        }
    }
}

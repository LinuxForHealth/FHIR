/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.validation.test;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.CareTeam;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.validation.FHIRValidator;

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

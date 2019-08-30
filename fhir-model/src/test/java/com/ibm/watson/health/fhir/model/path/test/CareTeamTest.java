/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.path.test;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.path.FHIRPathNode;
import com.ibm.watson.health.fhir.model.path.FHIRPathTree;
import com.ibm.watson.health.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watson.health.fhir.model.resource.CareTeam;
import com.ibm.watson.health.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watson.health.fhir.model.validation.FHIRValidator;

public class CareTeamTest {
    public static void main(String[] args) throws Exception {
        try (InputStream in = CareTeamTest.class.getClassLoader().getResourceAsStream("JSON/careteam.json")) {  
            CareTeam careTeam = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator(careTeam).validate();
            for (Issue issue : issues) {
                System.out.println("severity: " + issue.getSeverity().getValue() + ", details: " + issue.getDetails().getText().getValue() + ", expression: " + issue.getExpression().get(0).getValue());
            }
            
            FHIRPathEvaluator.DEBUG = true;
            
            FHIRPathTree tree = FHIRPathTree.tree(careTeam);
            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator(tree);
            
            Collection<FHIRPathNode> result = evaluator.evaluate("CareTeam.participant", tree.getRoot());
            for (FHIRPathNode node : result) {
                result = evaluator.evaluate("member.resolve() is Practitioner", node);
                System.out.println("result: " + result);
            }
        }
    }
}

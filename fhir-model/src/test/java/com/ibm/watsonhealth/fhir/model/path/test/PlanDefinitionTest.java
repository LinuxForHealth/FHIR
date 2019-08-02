/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.test;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.parser.FHIRParser;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTree;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watsonhealth.fhir.model.resource.PlanDefinition;
import com.ibm.watsonhealth.fhir.model.validation.FHIRValidator;

public class PlanDefinitionTest {
    public static void main(String[] args) throws Exception {
        try (InputStream in = PlanDefinitionTest.class.getClassLoader().getResourceAsStream("JSON/plandefinition.json")) {  
            PlanDefinition planDefinition = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator(planDefinition).validate();
            for (Issue issue : issues) {
                System.out.println("severity: " + issue.getSeverity().getValue() + ", details: " + issue.getDetails().getText().getValue() + ", expression: " + issue.getExpression().get(0).getValue());
            }
            FHIRPathEvaluator.DEBUG = true;
            FHIRPathTree tree = FHIRPathTree.tree(planDefinition);
            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator(tree);
            Collection<FHIRPathNode> result = evaluator.evaluate("%resource.descendants().as(canonical)", tree.getRoot());
            System.out.println("result: " + result);
        }
    }
}

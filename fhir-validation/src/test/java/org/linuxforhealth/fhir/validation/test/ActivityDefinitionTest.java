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
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.ActivityDefinition;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.validation.FHIRValidator;

public class ActivityDefinitionTest {
    public static void main(String[] args) throws Exception {
        try (InputStream in = ActivityDefinitionTest.class.getClassLoader().getResourceAsStream("JSON/activitydefinition.json")) {
            ActivityDefinition activityDefinition = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(activityDefinition);
            for (Issue issue : issues) {
                System.out.println("severity: " + issue.getSeverity().getValue() + ", details: " + issue.getDetails().getText().getValue() + ", expression: " + issue.getExpression().get(0).getValue());
            }
            Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator().evaluate(activityDefinition, "contained.id");
            System.out.println("result: " + result);
            FHIRGenerator.generator(Format.XML).generate(activityDefinition, System.out);
        }
    }
}

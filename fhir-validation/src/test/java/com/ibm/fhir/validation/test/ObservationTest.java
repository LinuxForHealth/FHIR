/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.test;

import java.io.InputStream;
import java.util.List;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.validation.FHIRValidator;

public class ObservationTest {
    public static void main(String[] args) throws Exception {
        try (InputStream in = ObservationTest.class.getClassLoader().getResourceAsStream("JSON/observation.json")) {
            Observation observation = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(observation);
            for (Issue issue : issues) {
                System.out.println("severity: " + issue.getSeverity().getValue() + ", details: " + issue.getDetails().getText().getValue() + ", expression: " + issue.getExpression().get(0).getValue());
            }
        }
    }
}

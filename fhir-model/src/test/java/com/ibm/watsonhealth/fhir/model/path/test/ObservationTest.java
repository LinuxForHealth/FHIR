/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.test;

import java.io.InputStream;
import java.util.List;

import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.parser.FHIRParser;
import com.ibm.watsonhealth.fhir.model.resource.Observation;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watsonhealth.fhir.model.validation.FHIRValidator;

public class ObservationTest {
    public static void main(String[] args) throws Exception {
        try (InputStream in = ObservationTest.class.getClassLoader().getResourceAsStream("JSON/observation.json")) {
            Observation observation = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator(observation).validate();
            for (Issue issue : issues) {
                System.out.println("severity: " + issue.getSeverity().getValue() + ", diagnostics: " + issue.getDiagnostics().getValue() + ", expression: " + issue.getExpression().get(0).getValue());
            }
        }
    }
}

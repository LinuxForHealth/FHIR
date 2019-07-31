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
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watsonhealth.fhir.model.resource.StructureDefinition;
import com.ibm.watsonhealth.fhir.model.validation.FHIRValidator;

public class StructureDefinitionTest {
    public static void main(String[] args) throws Exception {
        try (InputStream in = StructureDefinitionTest.class.getClassLoader().getResourceAsStream("JSON/StructureDefinition-1.json")) {
            StructureDefinition structureDefinition = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator(structureDefinition).validate();
            for (Issue issue : issues) {
                System.out.println("severity: " + issue.getSeverity().getValue() + ", diagnostics: " + issue.getDiagnostics().getValue() + ", location: " + issue.getLocation().get(0).getValue());
            }
        }
    }
}

/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;

import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

/**
 * Tests the Davinici $member-match
 */
public class DavinciHealthRecordExchangeMemberMatchOperationTest extends ProfilesTestBase {
    public static final String EXPRESSION_OPERATION = "rest.resource.operation.name";

    @BeforeClass
    public void checkOperationExistsOnServer() throws Exception {
            CapabilityStatement conf = retrieveConformanceStatement();
            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
            EvaluationContext evaluationContext = new EvaluationContext(conf);
            // All the possible required operations
            Collection<FHIRPathNode> tmpResults = evaluator.evaluate(evaluationContext, EXPRESSION_OPERATION);
            Collection<String> listOfOperations = tmpResults.stream().map(x -> x.getValue().asStringValue().string()).collect(Collectors.toList());

            if (!listOfOperations.contains("member-match")) {
                throw new SkipException("member match not found");
            }
    }

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList(
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-coverage|0.2.0",
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-organization|0.2.0",
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-out|0.2.0",
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-in|0.2.0",
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-practitioner|0.2.0",
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-practitionerrole|0.2.0",
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-provenance|0.2.0",
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-task-data-request|0.2.0");
    }

    @Override
    public void setCheck(Boolean check) {
        // NOP
    }


}

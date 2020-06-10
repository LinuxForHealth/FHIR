/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.CarePlan;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.validation.FHIRValidator;

public class USCoreCarePlanTest {
    @Test
    public void testUSCoreCarePlan() throws Exception {
        InputStream in = USCoreCarePlanTest.class.getClassLoader().getResourceAsStream("JSON/us-core-careplan.json");
        CarePlan carePlan = FHIRParser.parser(Format.JSON).parse(in);
        List<Issue> issues = FHIRValidator.validator().validate(carePlan);
        issues.forEach(System.out::println);
        assertEquals(issues.size(), 1);
        assertEquals(issues.get(0).getSeverity(), IssueSeverity.INFORMATION);
        assertEquals(issues.get(0).getCode(), IssueType.INFORMATIONAL);
        assertTrue(issues.get(0).getDetails().getText().getValue().startsWith("Resource type could not be inferred from reference"));
        assertEquals(issues.get(0).getExpression().get(0).getValue(), "CarePlan.subject");
    }
}

/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.FamilyMemberHistory;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.profile.ConstraintGenerator;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.validation.FHIRValidator;

public class FamilyMemberHistoryTest {
    @Test
    public void testFamilyMemberHistory() throws Exception {
        StructureDefinition profile = ProfileSupport.getProfile("http://ibm.com/fhir/StructureDefinition/my-family-member-history|0.1.0");
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();

        constraints.forEach(System.out::println);

        assertEquals(constraints.size(), 4);
        assertEquals(constraints.get(3).expression(), "condition.count() >= 1 and condition.all(code.exists() and code.all(memberOf('http://ibm.com/fhir/ValueSet/condition-value-set', 'extensible')) and (outcome.exists() implies (outcome.exists() and outcome.all(memberOf('http://ibm.com/fhir/ValueSet/outcome-value-set', 'extensible')))))");

        InputStream in = FamilyMemberHistoryTest.class.getClassLoader().getResourceAsStream("JSON/familymemberhistory.json");
        FamilyMemberHistory familyMemberHistory = FHIRParser.parser(Format.JSON).parse(in);
        List<Issue> issues = FHIRValidator.validator().validate(familyMemberHistory);

        issues.forEach(System.out::println);

        assertEquals(issues.size(), 1);
        assertTrue(issues.get(0).getDetails().getText().getValue().startsWith("generated-my-family-member-history-4"));
    }
}

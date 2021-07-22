/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.constraint.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.validation.util.FHIRValidationUtil.countWarnings;
import static com.ibm.fhir.validation.util.FHIRValidationUtil.hasErrors;
import static com.ibm.fhir.validation.util.FHIRValidationUtil.hasWarnings;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.validation.FHIRValidator;

public class PatientConstraintValidatorTest {
    @Test
    public void testPatientConstraintProvider() {
        List<Constraint> constraints = ModelSupport.getConstraints(Patient.class);
        Set<String> ids = constraints.stream()
            .map(constraint -> constraint.id())
            .collect(Collectors.toSet());
        assertTrue(ids.contains("patient-name-1"));
    }

    @Test
    public void testPatientConstraintValidator1() throws Exception {
        Patient patient = Patient.builder()
            .active(Boolean.TRUE)
            .birthDate(Date.of("1970-01-01"))
            .name(HumanName.builder()
                .given(string("John"))
                .family(string("Doe"))
                .build())
            .build();
        List<Issue> issues = FHIRValidator.validator().validate(patient);
        assertFalse(hasErrors(issues));
        assertTrue(hasWarnings(issues));
        assertEquals(countWarnings(issues), 1);
    }

    @Test
    public void testPatientConstraintValidator2() throws Exception {
        Patient patient = Patient.builder()
            .active(Boolean.TRUE)
            .birthDate(Date.of("1970-01-01"))
            .name(HumanName.builder()
                .given(string("John"))
                .build())
            .build();
        List<Issue> issues = FHIRValidator.validator().validate(patient);
        assertTrue(hasWarnings(issues));
        assertEquals(countWarnings(issues), 2);
    }
}

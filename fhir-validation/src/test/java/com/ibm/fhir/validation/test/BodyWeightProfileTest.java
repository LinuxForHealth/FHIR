/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.validation.util.ProfileSupport.getConstraints;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.ObservationStatus;
import com.ibm.fhir.validation.FHIRValidator;

public class BodyWeightProfileTest {
    private static final String BODY_WEIGHT_PROFILE_URL = "http://hl7.org/fhir/StructureDefinition/bodyweight";

    @Test
    public static void testBodyWeightProfile() throws Exception {
        List<Constraint> constraints = getConstraints(BODY_WEIGHT_PROFILE_URL, Observation.class);

        Assert.assertEquals(constraints.size(), 3);
        Assert.assertTrue(constraints.stream().filter(constraint -> constraint.id().equals("vs-1")).count() == 1);
        Assert.assertTrue(constraints.stream().filter(constraint -> constraint.id().equals("vs-2")).count() == 1);
        Assert.assertTrue(constraints.stream().filter(constraint -> constraint.id().equals("vs-3")).count() == 1);

        Observation bodyWeight = Observation.builder()
            .meta(Meta.builder()
                .profile(Canonical.of(BODY_WEIGHT_PROFILE_URL))
                .build())
            .status(ObservationStatus.FINAL)
            .effective(DateTime.builder()
                .value("2019")
                .build())
            .category(CodeableConcept.builder()
                .coding(Coding.builder()
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/observation-category"))
                    .code(Code.of("vital-signs"))
                    .build())
                .build())
            .code(CodeableConcept.builder()
                .coding(Coding.builder()
                    .system(Uri.of("http://loinc.org"))
                    .code(Code.of("29463-7"))
                    .build())
                .build())
            .value(Quantity.builder()
                .value(Decimal.of(200))
                .system(Uri.of("http://unitsofmeasure.org"))
                .code(Code.of("[lb_av]"))
                .unit(string("lbs"))
                .build())
            .build();
        
        bodyWeight = bodyWeight.toBuilder()
                .value(bodyWeight.getValue().as(Quantity.class).toBuilder()
                    .value(Decimal.of(210))
                    .build())
                .build();

        List<Issue> issues = FHIRValidator.validator().validate(bodyWeight);
        Assert.assertEquals(issues.size(), 2);
        Assert.assertTrue(issues.stream().filter(issue -> issue.getDetails().getText().getValue().startsWith("dom-6")).count() == 1);
        Assert.assertTrue(issues.stream().filter(issue -> issue.getDetails().getText().getValue().startsWith("vs-1")).count() == 1);
        
        bodyWeight = bodyWeight.toBuilder()
                .meta(null)
                .build();
        
        issues = FHIRValidator.validator().validate(bodyWeight);
        Assert.assertEquals(issues.size(), 1);
        Assert.assertTrue(issues.stream().filter(issue -> issue.getDetails().getText().getValue().startsWith("dom-6")).count() == 1);
        
        issues = FHIRValidator.validator().validate(bodyWeight, BODY_WEIGHT_PROFILE_URL);
        Assert.assertEquals(issues.size(), 2);
        Assert.assertTrue(issues.stream().filter(issue -> issue.getDetails().getText().getValue().startsWith("dom-6")).count() == 1);
        Assert.assertTrue(issues.stream().filter(issue -> issue.getDetails().getText().getValue().startsWith("vs-1")).count() == 1);
    }
}

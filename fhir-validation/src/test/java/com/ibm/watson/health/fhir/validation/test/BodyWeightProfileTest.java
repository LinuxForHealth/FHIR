/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.validation.test;

import static com.ibm.watson.health.fhir.model.type.String.string;
import static com.ibm.watson.health.fhir.validation.util.ProfileSupport.getConstraints;

import com.ibm.watson.health.fhir.model.annotation.Constraint;
import com.ibm.watson.health.fhir.model.resource.Observation;
import com.ibm.watson.health.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watson.health.fhir.model.type.Canonical;
import com.ibm.watson.health.fhir.model.type.Code;
import com.ibm.watson.health.fhir.model.type.CodeableConcept;
import com.ibm.watson.health.fhir.model.type.Coding;
import com.ibm.watson.health.fhir.model.type.DateTime;
import com.ibm.watson.health.fhir.model.type.Decimal;
import com.ibm.watson.health.fhir.model.type.Meta;
import com.ibm.watson.health.fhir.model.type.ObservationStatus;
import com.ibm.watson.health.fhir.model.type.Quantity;
import com.ibm.watson.health.fhir.model.type.Uri;
import com.ibm.watson.health.fhir.validation.FHIRValidator;

public class BodyWeightProfileTest {
    private static final String BODY_WEIGHT_PROFILE_URL = "http://hl7.org/fhir/StructureDefinition/bodyweight";

    public static void main(String[] args) throws Exception {
        for (Constraint constraint : getConstraints(BODY_WEIGHT_PROFILE_URL)) {
            System.out.println(constraint);
        }
                
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
        
        System.out.println(bodyWeight);

        for (Issue issue : FHIRValidator.validator().validate(bodyWeight)) {
            System.out.println(issue);
        }
    }
}
/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.test;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.Basic;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.ValueSet;
import org.linuxforhealth.fhir.model.resource.ValueSet.Expansion;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.HumanName;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;

public class ModelBuilderTest {
    @Test
    public void testPrimitiveSetterEquivalence() {
        Patient p1 = Patient.builder()
                .meta(Meta.builder()
                    .lastUpdated(ZonedDateTime.of(2021, 8, 19, 00, 59, 59, 0, ZoneOffset.of("-05:00"))) // Instant
                    .build())
                .extension(Extension.builder()
                    .url("test")
                    .value("string")
                    .build())
                .contained(ValueSet.builder()
                    .status(PublicationStatus.DRAFT)
                    .expansion(Expansion.builder()
                        .timestamp(DateTime.of("2021-08-19T00:59:59-05:00"))
                        .total(0)                                                                       // Integer
                        .build())
                    .build())
                .active(false)                                                                          // Boolean
                .birthDate(LocalDate.of(1984, 9, 4))                                                    // Date
                .multipleBirth(1)
                .name(HumanName.builder()
                    .given("Lee")                                                                       // String
                    .build())
                .build();

        Patient p2 = Patient.builder()
                .meta(Meta.builder()
                    .lastUpdated(org.linuxforhealth.fhir.model.type.Instant.of("2021-08-19T00:59:59-05:00"))       // Instant
                    .build())
                .extension(Extension.builder()
                    .url("test")
                    .value(string("string"))
                    .build())
                .contained(ValueSet.builder()
                    .status(PublicationStatus.DRAFT)
                    .expansion(Expansion.builder()
                        .timestamp(DateTime.of("2021-08-19T00:59:59-05:00"))
                        .total(org.linuxforhealth.fhir.model.type.Integer.of(0))                                   // Integer
                        .build())
                    .build())
                .active(org.linuxforhealth.fhir.model.type.Boolean.FALSE)                                          // Boolean
                .birthDate(org.linuxforhealth.fhir.model.type.Date.of("1984-09-04"))                               // Date
                .multipleBirth(org.linuxforhealth.fhir.model.type.Integer.of(1))
                .name(HumanName.builder()
                    .given(string("Lee"))                                                               // String
                    .build())
                .build();

        assertEquals(p1, p2);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testVarArgWithNull() {
        Basic.builder()
            .code(CodeableConcept.builder()
                .coding((Coding)null)
                .build())
            .build();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testCollectionWithNull() {
        Basic.builder()
            .code(CodeableConcept.builder()
                .coding((Coding)null)
                .build())
            .build();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNullCollection() {
        Basic.builder()
            .code(CodeableConcept.builder()
                .coding((Collection<Coding>)null)
                .build())
            .build();
    }
}

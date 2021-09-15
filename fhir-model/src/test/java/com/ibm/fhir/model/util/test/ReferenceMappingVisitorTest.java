/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util.test;

import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertNotSame;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.UUID;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.util.ReferenceMappingVisitor;

public class ReferenceMappingVisitorTest {
    public boolean DEBUG = false;

    private Patient patient;
    private HashMap<java.lang.String, java.lang.String> localRefMap;

    @BeforeClass
    public void setUp() throws Exception {
        java.lang.String id = UUID.randomUUID().toString();

        Meta meta = Meta.builder().versionId(Id.of("1"))
                        .lastUpdated(Instant.now(ZoneOffset.UTC))
                        .build();

        String given = String.builder().value("John")
                        .extension(Extension.builder()
                                .url("http://www.ibm.com/someExtension")
                                .value(String.of("value and extension"))
                                .build())
                        .build();

        String otherGiven = String.builder()
                        .extension(Extension.builder()
                                .url("http://www.ibm.com/someExtension")
                                .value(String.of("extension only"))
                                .build())
                        .build();

        HumanName name = HumanName.builder()
                        .id("someId")
                        .given(given)
                        .given(otherGiven)
                        .given(String.of("value no extension"))
                        .family(String.of("Doe"))
                        .build();

        java.lang.String uUID = UUID.randomUUID().toString();

        localRefMap = new HashMap<java.lang.String, java.lang.String>();
        localRefMap.put("urn:uuid:" + uUID, "urn:romote:" + uUID);

        Reference providerRef = Reference.builder()
                        .reference(String.of("urn:uuid:" + uUID))
                        .build();

        patient = Patient.builder()
                        .id(id)
                        .active(Boolean.TRUE)
                        .multipleBirth(Integer.of(2))
                        .meta(meta)
                        .name(name)
                        .birthDate(Date.of(LocalDate.now()))
                        .generalPractitioner(providerRef)
                        .build();
    }

    @Test(enabled = true)
    public void testUpdateReferences() throws FHIRGeneratorException {
        ReferenceMappingVisitor<Patient> visitor = new ReferenceMappingVisitor<Patient>(localRefMap);
        patient.accept(visitor);
        Patient result = visitor.getResult();

        assertNotSame(result, patient);

        StringWriter writer1 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(patient, writer1);
        StringWriter writer2 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(result, writer2);
        assertNotEquals(writer2.toString(), writer1.toString());

        if (DEBUG) {
            System.out.println(writer1.toString());
            System.out.println(writer2.toString());
        }

        assertNotEquals(result, patient);
    }
}
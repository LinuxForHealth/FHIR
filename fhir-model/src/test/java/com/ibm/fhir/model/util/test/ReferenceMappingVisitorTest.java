/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

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

    private Patient basePatient;

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

        basePatient = Patient.builder()
                        .id(id)
                        .active(Boolean.TRUE)
                        .multipleBirth(Integer.of(2))
                        .meta(meta)
                        .name(name)
                        .birthDate(Date.of(LocalDate.now()))
                        .build();
    }

    @Test
    public void testUpdateReferences() throws FHIRGeneratorException {
        java.lang.String uUID = UUID.randomUUID().toString();
        HashMap<java.lang.String, java.lang.String> localRefMap = new HashMap<java.lang.String, java.lang.String>();
        localRefMap.put("urn:uuid:" + uUID, "urn:remote:" + uUID);

        Reference providerRef = Reference.builder()
                .reference(String.of("urn:uuid:" + uUID))
                .build();

        Patient patient = basePatient.toBuilder().generalPractitioner(providerRef).build();
        ReferenceMappingVisitor<Patient> visitor = new ReferenceMappingVisitor<Patient>(localRefMap, null);
        patient.accept(visitor);
        Patient result = visitor.getResult();

        StringWriter writer1 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(patient, writer1);
        StringWriter writer2 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(result, writer2);

        if (DEBUG) {
            System.out.println(writer1.toString());
            System.out.println(writer2.toString());
        }

        assertNotEquals(writer2.toString(), writer1.toString());
        assertEquals(result.getGeneralPractitioner().get(0).getReference().getValue(), "urn:remote:" + uUID);
    }

    @Test
    public void testUpdateReferencesWithAbsoluteFullUrlAndRelativeReferenceMatch() throws FHIRGeneratorException {
        HashMap<java.lang.String, java.lang.String> localRefMap = new HashMap<java.lang.String, java.lang.String>();
        localRefMap.put("https://test.com/fhir-server/api/v4/Practitioner/test", "Practitioner/1");

        Reference providerRef = Reference.builder()
                .reference(String.of("Practitioner/test"))
                .build();

        Patient patient = basePatient.toBuilder().generalPractitioner(providerRef).build();
        ReferenceMappingVisitor<Patient> visitor = new ReferenceMappingVisitor<Patient>(localRefMap,
                "https://test.com/fhir-server/api/v4/Patient/test");
        patient.accept(visitor);
        Patient result = visitor.getResult();

        StringWriter writer1 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(patient, writer1);
        StringWriter writer2 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(result, writer2);

        if (DEBUG) {
            System.out.println(writer1.toString());
            System.out.println(writer2.toString());
        }

        assertNotEquals(writer2.toString(), writer1.toString());
        assertEquals(result.getGeneralPractitioner().get(0).getReference().getValue(), "Practitioner/1");
    }

    @Test
    public void testUpdateReferencesWithRelativeFullUrlAndAbsoluteReferenceMatch() throws FHIRGeneratorException {
        HashMap<java.lang.String, java.lang.String> localRefMap = new HashMap<java.lang.String, java.lang.String>();
        localRefMap.put("https://test.com/fhir-server/api/v4/Practitioner/test", "Practitioner/1");

        Reference providerRef = Reference.builder()
                .reference(String.of("https://test.com/fhir-server/api/v4/Practitioner/test"))
                .build();

        Patient patient = basePatient.toBuilder().generalPractitioner(providerRef).build();
        ReferenceMappingVisitor<Patient> visitor = new ReferenceMappingVisitor<Patient>(localRefMap, "Patient/test");
        patient.accept(visitor);
        Patient result = visitor.getResult();

        StringWriter writer1 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(patient, writer1);
        StringWriter writer2 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(result, writer2);

        if (DEBUG) {
            System.out.println(writer1.toString());
            System.out.println(writer2.toString());
        }

        assertNotEquals(writer2.toString(), writer1.toString());
        assertEquals(result.getGeneralPractitioner().get(0).getReference().getValue(), "Practitioner/1");
    }

    @Test
    public void testUpdateReferencesWithRelativeFullUrlAndRelativeReferenceMatch() throws FHIRGeneratorException {
        HashMap<java.lang.String, java.lang.String> localRefMap = new HashMap<java.lang.String, java.lang.String>();
        localRefMap.put("Practitioner/test", "Practitioner/1");

        Reference providerRef = Reference.builder()
                .reference(String.of("Practitioner/test"))
                .build();

        Patient patient = basePatient.toBuilder().generalPractitioner(providerRef).build();
        ReferenceMappingVisitor<Patient> visitor = new ReferenceMappingVisitor<Patient>(localRefMap, "Patient/test");
        patient.accept(visitor);
        Patient result = visitor.getResult();

        StringWriter writer1 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(patient, writer1);
        StringWriter writer2 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(result, writer2);

        if (DEBUG) {
            System.out.println(writer1.toString());
            System.out.println(writer2.toString());
        }

        assertNotEquals(writer2.toString(), writer1.toString());
        assertEquals(result.getGeneralPractitioner().get(0).getReference().getValue(), "Practitioner/1");
    }

    @Test
    public void testUpdateReferencesWithNonhttpFullUrlAndReferenceMatch() throws FHIRGeneratorException {
        HashMap<java.lang.String, java.lang.String> localRefMap = new HashMap<java.lang.String, java.lang.String>();
        localRefMap.put("resource:1", "Practitioner/1");

        Reference providerRef = Reference.builder()
                .reference(String.of("resource:1"))
                .build();

        Patient patient = basePatient.toBuilder().generalPractitioner(providerRef).build();
        ReferenceMappingVisitor<Patient> visitor = new ReferenceMappingVisitor<Patient>(localRefMap,
                "resource:2");
        patient.accept(visitor);
        Patient result = visitor.getResult();

        StringWriter writer1 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(patient, writer1);
        StringWriter writer2 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(result, writer2);

        if (DEBUG) {
            System.out.println(writer1.toString());
            System.out.println(writer2.toString());
        }

        assertNotEquals(writer2.toString(), writer1.toString());
        assertEquals(result.getGeneralPractitioner().get(0).getReference().getValue(), "Practitioner/1");
    }

    @Test
    public void testUpdateReferencesWithAbsoluteFullUrlAndAbsoluteReferenceNoMatch() throws FHIRGeneratorException {
        HashMap<java.lang.String, java.lang.String> localRefMap = new HashMap<java.lang.String, java.lang.String>();
        localRefMap.put("https://test.com/fhir-server/api/v4/Practitioner/test", "Practitioner/1");

        Reference providerRef = Reference.builder()
                .reference(String.of("https://test2.com/fhir-server/api/v4/Practitioner/test"))
                .build();

        Patient patient = basePatient.toBuilder().generalPractitioner(providerRef).build();
        ReferenceMappingVisitor<Patient> visitor = new ReferenceMappingVisitor<Patient>(localRefMap,
                "https://test.com/fhir-server/api/v4/Patient/test");
        patient.accept(visitor);
        Patient result = visitor.getResult();

        StringWriter writer1 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(patient, writer1);
        StringWriter writer2 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(result, writer2);

        if (DEBUG) {
            System.out.println(writer1.toString());
            System.out.println(writer2.toString());
        }

        assertEquals(writer2.toString(), writer1.toString());
    }

    @Test
    public void testUpdateReferencesWithAbsoluteFullUrlAndFragmentReferenceNoMatch() throws FHIRGeneratorException {
        HashMap<java.lang.String, java.lang.String> localRefMap = new HashMap<java.lang.String, java.lang.String>();
        localRefMap.put("https://test.com/fhir-server/api/v4/#Practitioner", "Practitioner/1");

        Reference providerRef = Reference.builder()
                .reference(String.of("#Practitioner"))
                .build();

        Patient patient = basePatient.toBuilder().generalPractitioner(providerRef).build();
        ReferenceMappingVisitor<Patient> visitor = new ReferenceMappingVisitor<Patient>(localRefMap,
                "https://test.com/fhir-server/api/v4/Patient/test");
        patient.accept(visitor);
        Patient result = visitor.getResult();

        StringWriter writer1 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(patient, writer1);
        StringWriter writer2 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(result, writer2);

        if (DEBUG) {
            System.out.println(writer1.toString());
            System.out.println(writer2.toString());
        }

        assertEquals(writer2.toString(), writer1.toString());
    }

    @Test
    public void testUpdateReferencesWithNonRestfulAbsoluteFullUrlAndRelativeReferenceNoMatch() throws FHIRGeneratorException {
        HashMap<java.lang.String, java.lang.String> localRefMap = new HashMap<java.lang.String, java.lang.String>();
        localRefMap.put("https://test.com/fhir-server/api/v4/Practitioner/test", "Practitioner/1");

        Reference providerRef = Reference.builder()
                .reference(String.of("Practitioner/test"))
                .build();

        Patient patient = basePatient.toBuilder().generalPractitioner(providerRef).build();
        ReferenceMappingVisitor<Patient> visitor = new ReferenceMappingVisitor<Patient>(localRefMap,
                "https://test.com/fhir-server/api/v4/NotValidRestfulRegex/test");
        patient.accept(visitor);
        Patient result = visitor.getResult();

        StringWriter writer1 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(patient, writer1);
        StringWriter writer2 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(result, writer2);

        if (DEBUG) {
            System.out.println(writer1.toString());
            System.out.println(writer2.toString());
        }

        assertEquals(writer2.toString(), writer1.toString());
    }

    @Test
    public void testUpdateReferencesWithNonhttpFullUrlAndMatchingReferenceNoMatch() throws FHIRGeneratorException {
        HashMap<java.lang.String, java.lang.String> localRefMap = new HashMap<java.lang.String, java.lang.String>();
        localRefMap.put("resource:123", "Practitioner/1");

        Reference providerRef = Reference.builder()
                .reference(String.of("resource:xyz"))
                .build();

        Patient patient = basePatient.toBuilder().generalPractitioner(providerRef).build();
        ReferenceMappingVisitor<Patient> visitor = new ReferenceMappingVisitor<Patient>(localRefMap,
                "resource:2");
        patient.accept(visitor);
        Patient result = visitor.getResult();

        StringWriter writer1 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(patient, writer1);
        StringWriter writer2 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(result, writer2);

        if (DEBUG) {
            System.out.println(writer1.toString());
            System.out.println(writer2.toString());
        }

        assertEquals(writer2.toString(), writer1.toString());
    }
}
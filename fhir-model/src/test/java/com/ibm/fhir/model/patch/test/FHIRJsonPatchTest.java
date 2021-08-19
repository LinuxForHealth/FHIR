/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.patch.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Xhtml.xhtml;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.parser.FHIRJsonParser;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.code.NarrativeStatus;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class FHIRJsonPatchTest {
    @BeforeClass
    public void setUp() {
    }

    @Test
    public void testAddOperation() throws FHIRGeneratorException, FHIRPatchException {
        Patient patient = buildPatient();

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        List<HumanName> name = new ArrayList<>(patient.getName());
        patientBuilder.name(Collections.singletonList(
            name.get(0).toBuilder()
                .given(string("Jack"))
                .build()));
        Patient updatedPatient = patientBuilder.build();

        // create a Patch and apply it to the original patient
        FHIRPatch patch = FHIRPatch.patch(Json.createPatchBuilder()
            .add("/name/0/given/1", "Jack")
            .build());
        patient = patch.apply(patient);

        Assert.assertEquals(patient, updatedPatient);
    }

    @Test
    public void testRemoveOperation() throws FHIRPatchException, FHIRGeneratorException {
        Patient patient = buildPatient();

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active((Boolean)null);
        Patient updatedPatient = patientBuilder.build();

        // create a Patch and apply it to the original patient
        FHIRPatch patch = FHIRPatch.patch(Json.createPatchBuilder()
            .remove("/active")
            .build());
        patient = patch.apply(patient);

        Assert.assertEquals(patient, updatedPatient);
    }

    @Test
    public void testReplaceOperation() throws FHIRPatchException, FHIRGeneratorException {
        Patient patient = buildPatient();

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active(Boolean.FALSE);
        Patient updatedPatient = patientBuilder.build();

        // create a Patch and apply it to the original patient
        FHIRPatch patch = FHIRPatch.patch(Json.createPatchBuilder()
            .replace("/active", false)
            .build());
        patient = patch.apply(patient);

        Assert.assertEquals(patient, updatedPatient);
    }

    @Test
    public void testCopyOperation() throws FHIRPatchException, FHIRGeneratorException {
        Patient patient = buildPatient();

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        List<HumanName> name = new ArrayList<>(patient.getName());
        patientBuilder.name(Collections.singletonList(
            name.get(0).toBuilder()
                .family(patient.getName().get(0).getGiven().get(0))
                .build()));
        Patient updatedPatient = patientBuilder.build();

        // create a Patch and apply it to the original patient
        FHIRPatch patch = FHIRPatch.patch(Json.createPatchBuilder()
            .copy("/name/0/family", "/name/0/given/0")
            .build());
        patient = patch.apply(patient);

        Assert.assertEquals(patient, updatedPatient);
    }

    @Test
    public void testMoveOperation() throws FHIRGeneratorException, FHIRPatchException {
        Patient patient = buildPatient();

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        List<HumanName> name = new ArrayList<>(patient.getName());
        patientBuilder.name(Collections.singletonList(
            name.get(0).toBuilder()
                .family(patient.getName().get(0).getGiven().get(0))
                .given(Collections.emptyList())
                .build()));
        Patient updatedPatient = patientBuilder.build();

        // create a Patch and apply it to the original patient
        FHIRPatch patch = FHIRPatch.patch(Json.createPatchBuilder()
            .move("/name/0/family", "/name/0/given/0")
            .build());
        patient = patch.apply(patient);

        Assert.assertEquals(patient, updatedPatient);
    }

    public void print(Resource resource) throws FHIRGeneratorException {
        FHIRGenerator.generator(Format.JSON, true).generate(resource, nonClosingOutputStream(System.out));
    }

    public <T extends Resource> T toResource(JsonObject jsonObject) throws FHIRParserException {
        return FHIRParser.parser(Format.JSON).as(FHIRJsonParser.class).parse(jsonObject);
    }

    public OutputStream nonClosingOutputStream(OutputStream out) {
        return new FilterOutputStream(out) {
            @Override
            public void close() {
                // do nothing
            }
        };
    }

    private Patient buildPatient() {
        java.lang.String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";

        String id = UUID.randomUUID().toString();

        Meta meta = Meta.builder()
                .versionId(Id.of("1"))
                .lastUpdated(Instant.now(ZoneOffset.UTC))
                .build();

        HumanName name = HumanName.builder()
                .given(string("John"))
                .family(string("Doe"))
                .build();

        Narrative text = Narrative.builder()
                .status(NarrativeStatus.GENERATED)
                .div(xhtml(div))
                .build();

        return Patient.builder()
                .id(id)
                .meta(meta)
                .text(text)
                .active(Boolean.TRUE)
                .name(name)
                .birthDate(Date.of("1980-01-01"))
                .build();
    }
}

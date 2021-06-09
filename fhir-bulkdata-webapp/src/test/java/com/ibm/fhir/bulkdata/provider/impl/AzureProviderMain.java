/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl;

import static com.ibm.fhir.model.type.String.string;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.UUID;

import com.ibm.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Xhtml;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.model.type.code.ObservationStatus;

public class AzureProviderMain {

    public static void main(String[] args) throws Exception {
        AzureProviderMain main = new AzureProviderMain();
        AzureProvider provider = new AzureProvider("dummy");

        main.test1(args[0]);
        //main.test2(args[0], provider);
    }

    public void test2(String arg0, AzureProvider provider) throws FHIRException {
        ExportTransientUserData chunkData = ExportTransientUserData.Builder.builder().build();
        ByteArrayOutputStream os = chunkData.getBufferStream();

        String cosBucketPathPrefix =  "DEMO0.956558326916514";
        provider.registerOverride(arg0, "test", chunkData, cosBucketPathPrefix, "Patient", 0);
        provider.readResources(0, cosBucketPathPrefix + "/Patient_1.ndjson");
        provider.readResources(0, cosBucketPathPrefix + "/Patient_1.ndjson");
        provider.readResources(0, cosBucketPathPrefix + "/Patient_1.ndjson");
        System.out.println(provider.getResources().size());
    }

    public void test1(String arg0) throws Exception {
        AzureProviderMain main = new AzureProviderMain();
        Patient patient = main.buildTestPatient();
        Observation observation = main.buildObservation(patient.getId());

        ExportTransientUserData chunkData = ExportTransientUserData.Builder.builder().build();
        ByteArrayOutputStream os = chunkData.getBufferStream();

        FHIRGenerator.generator(Format.JSON).generate(patient, os);
        os.write("\r\n".getBytes());
        FHIRGenerator.generator(Format.JSON).generate(observation, os);
        os.write("\r\n".getBytes());

        String cosBucketPathPrefix = ("DEMO" + Math.random());

        AzureProvider provider = new AzureProvider("dummy");
        provider.registerOverride(arg0, "test", chunkData, cosBucketPathPrefix, "Patient", 0);
        provider.createSource();
        provider.listBlobsForContainer();
        provider.writeResources("application/ndjson", Collections.emptyList());
        provider.listBlobsForContainer();
        System.out.println(provider.getSize(cosBucketPathPrefix + "/null_1.ndjson"));

        os = chunkData.getBufferStream();
        FHIRGenerator.generator(Format.JSON).generate(patient, os);
        os.write("\r\n".getBytes());
        FHIRGenerator.generator(Format.JSON).generate(observation, os);
        os.write("\r\n".getBytes());
        provider.writeResources("application/ndjson", Collections.emptyList());
        provider.listBlobsForContainer();
        System.out.println(provider.getSize(cosBucketPathPrefix + "/null_1.ndjson"));

        os = chunkData.getBufferStream();
        FHIRGenerator.generator(Format.JSON).generate(patient, os);
        os.write("\r\n".getBytes());
        FHIRGenerator.generator(Format.JSON).generate(observation, os);
        os.write("\r\n".getBytes());
        provider.writeResources("application/ndjson", Collections.emptyList());
        provider.listBlobsForContainer();
        System.out.println(provider.getSize(cosBucketPathPrefix + "/null_1.ndjson"));

        provider.readResources(0, cosBucketPathPrefix + "/Patient_1.ndjson");
        provider.readResources(0, cosBucketPathPrefix + "/Patient_1.ndjson");
        provider.readResources(0, cosBucketPathPrefix + "/Patient_1.ndjson");
        provider.readResources(0, cosBucketPathPrefix + "/Patient_1.ndjson");
        System.out.println(provider.getResources().size());

    }

    private Patient buildTestPatient() {
        String id = UUID.randomUUID().toString();
        Meta meta =
                Meta.builder()
                    .versionId(Id.of("1"))
                    .lastUpdated(Instant.now(ZoneOffset.UTC))
                    .build();

        com.ibm.fhir.model.type.String given =
                com.ibm.fhir.model.type.String.builder()
                .value("John")
                .build();

        HumanName name =
                HumanName.builder()
                    .id("someId")
                    .given(given)
                    .family(string("Doe")).build();

        java.lang.String uUID = UUID.randomUUID().toString();

        Reference providerRef =
                Reference.builder().reference(string("urn:uuid:" + uUID)).build();

        return Patient.builder().id(id)
                .active(com.ibm.fhir.model.type.Boolean.TRUE)
                .multipleBirth(com.ibm.fhir.model.type.Integer.of(2))
                .meta(meta).name(name).birthDate(Date.of(LocalDate.now().minus(30,ChronoUnit.YEARS)))
                .gender(AdministrativeGender.FEMALE)
                .generalPractitioner(providerRef).text(
                    Narrative.builder()
                        .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">loaded from the datastore</div>"))
                        .status(NarrativeStatus.GENERATED).build())
                .build();
    }

    public Observation buildObservation(String patientId) {
        CodeableConcept code = CodeableConcept.builder().coding(
            Coding.builder()
                .code(Code.of("82810-3"))
                .system(Uri.of("http://loinc.org"))
                .display(string("Pregnancy status"))
                .build())
            .text(string("Pregnancy status"))
            .build();

        Narrative text = Narrative.builder()
                .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">loaded from the datastore</div>"))
                .status(NarrativeStatus.GENERATED).build();

        CodeableConcept value = CodeableConcept.builder().coding(
            Coding.builder()
                .code(Code.of("LA15173-0"))
                .system(Uri.of("http://loinc.org"))
                .display(string("Pregnant"))
                .build())
            .text(string("Pregnant"))
            .build();

        Observation observation = Observation.builder()
                .text(text)
                .status(ObservationStatus.FINAL)
                .code(code)
                .subject(Reference.builder().reference(string("Patient/" + patientId)).build())
                .value(value)
                .effective(DateTime.of(LocalDate.of(2020, 6, 20)))
             .build();
        return observation;
    }
}

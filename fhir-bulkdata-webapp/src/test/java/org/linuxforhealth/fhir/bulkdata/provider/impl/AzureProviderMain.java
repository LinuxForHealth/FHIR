/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.provider.impl;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.bulkdata.dto.ReadResultDTO;
import org.linuxforhealth.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import org.linuxforhealth.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Date;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.HumanName;
import org.linuxforhealth.fhir.model.type.Id;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.Xhtml;
import org.linuxforhealth.fhir.model.type.code.AdministrativeGender;
import org.linuxforhealth.fhir.model.type.code.NarrativeStatus;
import org.linuxforhealth.fhir.model.type.code.ObservationStatus;

public class AzureProviderMain {

    public static void main(String[] args) throws Exception {
        AzureProviderMain main = new AzureProviderMain();
        AzureProvider provider = new AzureProvider("dummy");

        main.test5(args[0], provider, args[1]);
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

    public void test2(String arg0, AzureProvider provider) throws FHIRException {
        ExportTransientUserData chunkData = ExportTransientUserData.Builder.builder().build();

        String cosBucketPathPrefix =  "DEMO0.956558326916514";
        provider.registerOverride(arg0, "test", chunkData, cosBucketPathPrefix, "Patient", 0);
        provider.readResources(0, cosBucketPathPrefix + "/Patient_1.ndjson");
        provider.readResources(0, cosBucketPathPrefix + "/Patient_1.ndjson");
        provider.readResources(0, cosBucketPathPrefix + "/Patient_1.ndjson");
        System.out.println(provider.getResources().size());
    }

    public void test3(String arg0, AzureProvider provider, String comparePath) throws FHIRException, IOException {
        ImportTransientUserData chunkData = ImportTransientUserData.Builder.builder().build();

        String path =  "r4_AllergyIntolerance.ndjson";
        provider.registerOverride(arg0, "fhirtest", null, path, "Patient", 0);
        provider.registerTransient(chunkData);
        int total = 0;
        long current = 0;
        int iteration = 0;
        long size = provider.getSize(path);
        List<String> ids = new ArrayList<>();
        while (current < size) {
            System.out.println(iteration++ + " current [" + current + "] target [" + size + "]");
            provider.readResources(current, path);
            List<Resource> resources = provider.getResources();
            System.out.println("total resources on iteration [" + iteration + "] total [" + resources.size() + "]");
            current = chunkData.getCurrentBytes();

            total += resources.size();
            ids.addAll(provider.getResources().stream().map(r -> r.getId()).collect(Collectors.toList()));
            chunkData.getBufferStreamForImport().reset();
            provider.getResources().clear();
            System.out.println("--- L >  " + total);
            System.out.println("--- F >  " + provider.getNumberOfParseFailures());
        }
        System.out.println("Final: " + total);
        System.out.println("RESOURCES: " + provider.getNumberOfLoaded());
        System.out.println("FAILURES: " + provider.getNumberOfParseFailures());

        int idx = 1;
        for (String line : Files.readAllLines(Paths.get(comparePath))){
            Resource r = FHIRParser.parser(Format.JSON).parse(new ByteArrayInputStream(line.getBytes()));
            if (!ids.contains(r.getId())) {
                System.out.println(idx + " " + r.getId());
            }
            idx++;
        }
    }

    /**
     * drives write to Azure test using an arraystream
     * @param arg0
     * @param provider
     * @param path
     * @throws Exception
     */
    public void test4(String arg0, AzureProvider provider, String path) throws Exception {

        ExportTransientUserData chunkData = ExportTransientUserData.Builder.builder().build();

        String cosBucketPathPrefix =  "DEMO0." + System.currentTimeMillis();
        provider.registerOverride(arg0, "fhirtest", chunkData, cosBucketPathPrefix, "Patient", 0);

        ReadResultDTO dto = new ReadResultDTO();
        for (String line : Files.readAllLines(Paths.get(path))){
            chunkData.getBufferStream().write(line.getBytes());
            chunkData.getBufferStream().write("\r\n".getBytes());
            Resource r = FHIRParser.parser(Format.JSON).parse(new ByteArrayInputStream(line.getBytes()));
            dto.addResource(r);
        }
        System.out.println("Started the upload");
        provider.writeResources("application/ndjson", Arrays.asList(dto));
    }

    /**
     * drives write to Azure test using an InputStream
     * @param arg0
     * @param provider
     * @param path
     * @throws Exception
     */
    public void test5(String arg0, AzureProvider provider, String path) throws Exception {

        ExportTransientUserData chunkData = ExportTransientUserData.Builder.builder().build();

        String cosBucketPathPrefix =  "DEMO0." + System.currentTimeMillis();
        provider.registerOverride(arg0, "fhirtest", chunkData, cosBucketPathPrefix, "Patient", 0);

        StringBuilder builder = new StringBuilder();
        for (String line : Files.readAllLines(Paths.get(path))){
            builder.append(line);
        }
        System.out.println("Started the upload");
        provider.writeDirectly(cosBucketPathPrefix,
            new ByteArrayInputStream(builder.toString().getBytes()),
            builder.toString().getBytes().length);
    }

    private Patient buildTestPatient() {
        String id = UUID.randomUUID().toString();
        Meta meta =
                Meta.builder()
                    .versionId(Id.of("1"))
                    .lastUpdated(Instant.now(ZoneOffset.UTC))
                    .build();

        org.linuxforhealth.fhir.model.type.String given =
                org.linuxforhealth.fhir.model.type.String.builder()
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
                .active(org.linuxforhealth.fhir.model.type.Boolean.TRUE)
                .multipleBirth(org.linuxforhealth.fhir.model.type.Integer.of(2))
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

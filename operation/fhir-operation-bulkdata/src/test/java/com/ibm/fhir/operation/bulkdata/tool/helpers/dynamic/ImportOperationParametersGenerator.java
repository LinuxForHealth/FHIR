/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.tool.helpers.dynamic;

import static com.ibm.fhir.model.type.String.string;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;

/**
 *
 */
public class ImportOperationParametersGenerator {

    public ImportOperationParametersGenerator() {
        // No Operation
    }

    public Parameters generateParameters(String inputFormat, String inputSource, String type, String url,
            String storageType)
            throws FHIRGeneratorException, IOException {
        List<Parameter> parameters = new ArrayList<>();

        // Required: inputFormat
        parameters.add(Parameter.builder().name(string("inputFormat")).value(string(inputFormat)).build());

        // Required: inputSource - where it came from.
        parameters.add(Parameter.builder().name(string("inputSource")).value(Uri.uri(inputSource)).build());

        // Required: Input Values
        Parameter part1 = Parameter.builder().name(string("type")).value(string(type)).build();
        Parameter part2 = Parameter.builder().name(string("url")).value(Url.of(url)).build();

        // Required: Input
        Parameter inputParameter =
                Parameter.builder().name(string("input")).part(part1, part2)
                        .build();
        parameters.add(inputParameter);

        inputParameter =
                Parameter.builder().name(string("input")).part(part1, part2)
                        .build();
        parameters.add(inputParameter);

        // Optional: Storage Detail
        Parameter part5 =
                Parameter.builder().name(string("contentEncoding")).value(string("gzip")).build();
        Parameter part6 =
                Parameter.builder().name(string("contentEncoding")).value(string("lzw")).build();

        Parameter storageDetailParameter =
                Parameter.builder().name(string("storageDetail"))
                        .value(string("https"))
                        .part(part5, part6)
                        .build();
        parameters.add(storageDetailParameter);

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        try (StringWriter writer = new StringWriter();) {
            FHIRGenerator.generator(Format.JSON, true).generate(ps, writer);
            System.out.println(writer.toString());
        }
        return ps;
    }

    public Parameters generateParametersForSmartOnFhir() throws FHIRGeneratorException, IOException {
        List<Parameter> parameters = new ArrayList<>();

        // Required: inputFormat
        parameters
                .add(Parameter.builder().name(string("inputFormat")).value(string("application/fhir+ndjson")).build());

        // Required: inputSource - where it came from.
        parameters.add(Parameter.builder().name(string("inputSource"))
                .value(Uri.uri("https://other-server.example.org")).build());

        // Required: Input Values
        Parameter part1 = Parameter.builder().name(string("type")).value(string("Patient")).build();
        Parameter part2 =
                Parameter.builder().name(string("url"))
                        .value(Url.of("https://client.example.org/patient_file_2.ndjson?sig=RHIX5Xcg0Mq2rqI3OlWT"))
                        .build();

        // Required: Input
        Parameter inputParameter =
                Parameter.builder().name(string("input")).part(part1, part2)
                        .build();
        parameters.add(inputParameter);

        // Input #2
        part1          = Parameter.builder().name(string("type")).value(string("Observation")).build();
        part2          =
                Parameter.builder().name(string("url"))
                        .value(Url.of("https://client.example.org/obseration_file_19.ndjson?sig=RHIX5Xcg0Mq2rqI3OlWT"))
                        .build();

        inputParameter =
                Parameter.builder().name(string("input")).part(part1, part2)
                        .build();
        parameters.add(inputParameter);

        // Optional: Storage Detail
        Parameter storageDetailParameter =
                Parameter.builder().name(string("storageDetail"))
                        .value(string("https"))
                        .build();
        parameters.add(storageDetailParameter);

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        try (StringWriter writer = new StringWriter();) {
            FHIRGenerator.generator(Format.JSON, true).generate(ps, writer);
            System.out.println(writer.toString());
        }
        return ps;
    }

    public static void main(String[] args) throws FHIRGeneratorException, IOException {
        ImportOperationParametersGenerator generator = new ImportOperationParametersGenerator();
        String inputFormat = "inputFormat";
        String inputSource = "inputSource";
        String type = "Patient";
        String url = "url";
        String storageType = "storageType";
        generator.generateParameters(inputFormat, inputSource, type, url, storageType);
        System.out.println();
        generator.generateParametersForSmartOnFhir();
    }
}

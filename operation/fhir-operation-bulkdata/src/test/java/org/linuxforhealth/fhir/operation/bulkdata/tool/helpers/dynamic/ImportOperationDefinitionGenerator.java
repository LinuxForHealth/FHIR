/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.tool.helpers.dynamic;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.generator.exception.FHIRGeneratorException;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.OperationDefinition.Parameter;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.ContactDetail;
import org.linuxforhealth.fhir.model.type.ContactPoint;
import org.linuxforhealth.fhir.model.type.Markdown;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.Xhtml;
import org.linuxforhealth.fhir.model.type.code.ContactPointSystem;
import org.linuxforhealth.fhir.model.type.code.FHIRAllTypes;
import org.linuxforhealth.fhir.model.type.code.NarrativeStatus;
import org.linuxforhealth.fhir.model.type.code.OperationKind;
import org.linuxforhealth.fhir.model.type.code.OperationParameterUse;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;

/**
 * ImportOperationDefGeneratorMain is used to generate the OperationDefinition for the BulkData Import Operation.
 */
public class ImportOperationDefinitionGenerator {

    public static void main(String[] args) {
        ContactDetail contact =
                ContactDetail.builder()
                        .id("link-to-definition")
                        .name(string("link-to-definition"))
                        .telecom(ContactPoint.builder()
                                .system(ContactPointSystem.URL)
                                .value(string("https://github.com/smart-on-fhir/bulk-import/blob/master/import.md"))
                                .build())
                        .build();

        OperationDefinition.Builder builder = OperationDefinition.builder();
        builder.id("import")
                .text(Narrative.builder()
                        .div(Xhtml.of(
                                "<div xmlns=\"http://www.w3.org/1999/xhtml\"><h2>BulkDataImport</h2><p>OPERATION: BulkDataImport</p></div>"))
                        .status(NarrativeStatus.GENERATED).build())
                .url(Uri.of("http://hl7.org/fhir/OperationDefinition/BulkData-import"))
                .version(string("1.0.0"))
                .kind(OperationKind.OPERATION)
                .name(string("BulkDataImport"))
                .title(string("FHIR Bulk Data Import (Flat FHIR) - System Level Import"))
                .status(PublicationStatus.DRAFT)
                .publisher(string("IBM FHIR Server Team"))
                .description(Markdown.of("Pre ballot version of Bulk Data Import using the Parameters Object"))
                .code(Code.of("import"))
                .system(org.linuxforhealth.fhir.model.type.Boolean.TRUE)
                .type(org.linuxforhealth.fhir.model.type.Boolean.FALSE)
                .instance(org.linuxforhealth.fhir.model.type.Boolean.FALSE)
                .contact(contact);

        List<Parameter> parameters = new ArrayList<>();

        OperationDefinition.Parameter.Builder opBuilder = OperationDefinition.Parameter.builder();
        opBuilder.name(Code.code("inputFormat"));
        opBuilder.use(OperationParameterUse.IN);
        opBuilder.min(org.linuxforhealth.fhir.model.type.Integer.of(1));
        opBuilder.max(string("1"));
        opBuilder.documentation(string(
                "The format of the imported content. Servers SHALL support Newline Delimited JSON with a format type of application/fhir+ndjson but MAY choose to support additional input formats."));
        opBuilder.type(FHIRAllTypes.STRING);
        parameters.add(opBuilder.build());

        opBuilder = OperationDefinition.Parameter.builder();
        opBuilder.name(Code.code("inputSource"));
        opBuilder.use(OperationParameterUse.IN);
        opBuilder.min(org.linuxforhealth.fhir.model.type.Integer.of(1));
        opBuilder.max(string("1"));
        opBuilder.documentation(string(
                "URI for tracking this set of imported data throughout its lifecycle. MAY be used to specify a FHIR endpoint that can by the importing system when matching references to previously imported data."));
        opBuilder.type(FHIRAllTypes.URI);
        parameters.add(opBuilder.build());

        opBuilder = OperationDefinition.Parameter.builder();
        opBuilder.name(Code.code("input"));
        opBuilder.use(OperationParameterUse.IN);
        opBuilder.min(org.linuxforhealth.fhir.model.type.Integer.of(1));
        opBuilder.max(string("*"));
        opBuilder.documentation(string("a set of parameters with input types, urls as extensions"));

        OperationDefinition.Parameter part1 =
                OperationDefinition.Parameter.builder()
                        .name(Code.code("type"))
                        .min(org.linuxforhealth.fhir.model.type.Integer.of(1))
                        .max(string("1"))
                        .documentation(string("FHIR resource type"))
                        .type(FHIRAllTypes.STRING)
                        .use(OperationParameterUse.IN)
                        .build();

        OperationDefinition.Parameter part2 =
                OperationDefinition.Parameter.builder()
                        .name(Code.code("url"))
                        .min(org.linuxforhealth.fhir.model.type.Integer.of(1))
                        .max(string("1"))
                        .documentation(string(
                                "Path to bulk data file of the type reflected in inputFormat containing FHIR resources"))
                        .type(FHIRAllTypes.URL)
                        .use(OperationParameterUse.IN)
                        .build();

        opBuilder.part(part1, part2);
        parameters.add(opBuilder.build());

        opBuilder = OperationDefinition.Parameter.builder();
        opBuilder.name(Code.code("storageDetail"));
        opBuilder.use(OperationParameterUse.IN);
        opBuilder.type(FHIRAllTypes.STRING);
        opBuilder.min(org.linuxforhealth.fhir.model.type.Integer.of(1));
        opBuilder.max(string("1"));
        opBuilder.documentation(string("The type of storage input"));

        OperationDefinition.Parameter part4 =
                OperationDefinition.Parameter.builder()
                        .name(Code.code("contentEncoding"))
                        .min(org.linuxforhealth.fhir.model.type.Integer.of(0))
                        .max(string("*"))
                        .documentation(string("Content Encoding for the specific type of storage"))
                        .type(FHIRAllTypes.STRING)
                        .use(OperationParameterUse.IN)
                        .build();

        OperationDefinition.Parameter part3 =
                OperationDefinition.Parameter.builder()
                        .name(Code.code("type"))
                        .min(org.linuxforhealth.fhir.model.type.Integer.of(1))
                        .max(string("1"))
                        .documentation(string(
                                "Parameters depending on type:"))
                        .type(FHIRAllTypes.STRING)
                        .use(OperationParameterUse.IN)
                        .part(part4)
                        .build();

        opBuilder.part(part3);
        parameters.add(opBuilder.build());

        builder.parameter(parameters);

        try (StringWriter writer = new StringWriter();) {
            FHIRGenerator.generator(Format.JSON, true).generate(builder.build(), System.out);
            System.out.println(writer.toString());
        } catch (FHIRGeneratorException e) {

            e.printStackTrace();
            fail("unable to generate the fhir resource to JSON");

        } catch (IOException e1) {
            e1.printStackTrace();
            fail("unable to generate the fhir resource to JSON (io problem) ");
        }
    }
}

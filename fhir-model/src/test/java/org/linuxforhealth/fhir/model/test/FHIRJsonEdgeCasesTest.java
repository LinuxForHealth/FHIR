/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.test;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.stream.Collectors;

import org.skyscreamer.jsonassert.JSONAssert;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Patient;

public class FHIRJsonEdgeCasesTest {
    public static void main(String[] args) throws Exception {
        File file = new File("./src/test/resources/JSON/json-edge-cases.json");
        String expected = Files.readAllLines(file.toPath()).stream().collect(Collectors.joining(System.lineSeparator()));
        Patient patient = FHIRParser.parser(Format.JSON).parse(new StringReader(expected));
        StringWriter writer = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(patient, writer);
        String actual = writer.toString();
        JSONAssert.assertEquals(expected, actual, true);
    }
}

package org.linuxforhealth.fhir.operation.convert;
import static org.testng.Assert.assertEquals;

import java.io.StringReader;
import java.io.StringWriter;

/*
 * (C) Copyright Merative 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Extension;

public class ConversionTest {
    // https://github.com/LinuxForHealth/FHIR/issues/3577
    @Test(enabled = false)
    void testStringCompare() throws Exception {
        Resource original = Patient.builder()
                .extension(Extension.builder()
                        .url("test")
                        .value("test\none")
                        .build())
                .build();

        StringWriter sw = new StringWriter();
        FHIRGenerator.generator(Format.XML).generate(original, sw);

        StringReader sr = new StringReader(sw.toString());
        Resource roundtripped = FHIRParser.parser(Format.XML).parse(sr);

        assertEquals(roundtripped, original);
    }
}

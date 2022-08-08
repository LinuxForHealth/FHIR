/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.test;

import java.io.InputStream;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Observation;

public class ObservationReadWriteTest {
    public static void main(String[] args) throws Exception {
        try (InputStream in = ObservationReadWriteTest.class.getClassLoader().getResourceAsStream("JSON/observation-example-f001-glucose.json")) {
            Observation observation = FHIRParser.parser(Format.JSON).parse(in);
            FHIRGenerator.generator(Format.JSON, true).generate(observation, System.out);
        }
    }
}

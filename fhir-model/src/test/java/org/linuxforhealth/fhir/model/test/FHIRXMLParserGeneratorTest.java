/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.test;

import static org.linuxforhealth.fhir.model.type.Xhtml.xhtml;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.parser.FHIRXMLParser;
import org.linuxforhealth.fhir.model.resource.ActivityDefinition;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Date;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.HumanName;
import org.linuxforhealth.fhir.model.type.Id;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.model.type.Integer;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.String;
import org.linuxforhealth.fhir.model.type.code.NarrativeStatus;

public class FHIRXMLParserGeneratorTest {
    public static void main(java.lang.String[] args) throws Exception {
        java.lang.String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";
        
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
        
        Narrative text = Narrative.builder().status(NarrativeStatus.GENERATED).div(xhtml(div)).build();
        
        Patient patient = Patient.builder()
                .id(id)
                .text(text)
                .active(Boolean.TRUE)
                .multipleBirth(Integer.of(2))
                .meta(meta)
                .name(name)
                .birthDate(Date.of(LocalDate.now()))
                .build();
    
        StringWriter writer = new StringWriter();
        FHIRGenerator.generator(Format.XML, true).generate(patient, writer);
        java.lang.String result = writer.toString();
        System.out.println(result);
        
        StringReader reader = new StringReader(result);
        FHIRXMLParser.DEBUG = true;
        patient = FHIRParser.parser(Format.XML).parse(reader);
        
        FHIRGenerator.generator(Format.XML, true).generate(patient, System.out);
        
        InputStream in = FHIRXMLParserGeneratorTest.class.getClassLoader().getResourceAsStream("JSON/activitydefinition.json");
        ActivityDefinition activityDefinition = FHIRParser.parser(Format.JSON).parse(in);
        
        writer = new StringWriter();
        FHIRGenerator.generator(Format.XML, true).generate(activityDefinition, writer);
        result = writer.toString();
        
        reader = new StringReader(result);
        activityDefinition = FHIRParser.parser(Format.XML).parse(reader);
        FHIRGenerator.generator(Format.XML, true).generate(activityDefinition, System.out);
    }
}

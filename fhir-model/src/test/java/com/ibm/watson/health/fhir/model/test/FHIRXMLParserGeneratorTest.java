/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.test;

import static com.ibm.watson.health.fhir.model.type.Xhtml.xhtml;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.generator.FHIRGenerator;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.parser.FHIRXMLParser;
import com.ibm.watson.health.fhir.model.resource.ActivityDefinition;
import com.ibm.watson.health.fhir.model.resource.Patient;
import com.ibm.watson.health.fhir.model.type.Boolean;
import com.ibm.watson.health.fhir.model.type.Date;
import com.ibm.watson.health.fhir.model.type.Extension;
import com.ibm.watson.health.fhir.model.type.HumanName;
import com.ibm.watson.health.fhir.model.type.Id;
import com.ibm.watson.health.fhir.model.type.Instant;
import com.ibm.watson.health.fhir.model.type.Integer;
import com.ibm.watson.health.fhir.model.type.Meta;
import com.ibm.watson.health.fhir.model.type.Narrative;
import com.ibm.watson.health.fhir.model.type.NarrativeStatus;
import com.ibm.watson.health.fhir.model.type.String;

public class FHIRXMLParserGeneratorTest {
    public static void main(java.lang.String[] args) throws Exception {
        java.lang.String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";
        
        Id id = Id.builder().value(UUID.randomUUID().toString())
                .extension(Extension.builder()
                    .url("http://www.ibm.com/someExtension")
                    .value(String.of("Hello, World!"))
                    .build())
                .build();
        
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

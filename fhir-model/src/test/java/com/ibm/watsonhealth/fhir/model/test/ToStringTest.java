/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.test;

import static com.ibm.watsonhealth.fhir.model.type.Xhtml.xhtml;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

import com.ibm.watsonhealth.fhir.model.FHIRModel;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.NarrativeStatus;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.visitor.PathAwareVisitorAdapter;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

public class ToStringTest {
    public static void main(java.lang.String[] args) {
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
                .id("someOtherId")
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
        
        Narrative text = Narrative.builder()
                .status(NarrativeStatus.GENERATED)
                .div(xhtml(div))
                .build();
                
        Patient patient = Patient.builder()
                .id(id)
                .text(text)
                .active(Boolean.TRUE)
                .multipleBirth(Integer.of(2))
                .meta(meta)
                .name(name)
                .birthDate(Date.of(LocalDate.now()))
                .build();
        
        FHIRModel.setToStringFormat(Format.XML);
        
        Visitor visitor = new PathAwareVisitorAdapter() {
            @Override
            protected void doVisitStart(java.lang.String elementName, int elementIndex, Element element) {
                System.out.println("path: " + getPath());
                System.out.println(element);
            }

            @Override
            protected void doVisitStart(java.lang.String elementName, int elementIndex, Resource resource) {
                System.out.println("path: " + getPath());
                System.out.println(resource);
            }
        };
        
        patient.accept(visitor);
        
        FHIRModel.setToStringFormat(Format.JSON);
        
        patient.accept(visitor);
        
        FHIRModel.setToStringPrettyPrinting(true);
        
        patient.accept(visitor);
        
        FHIRModel.setToStringFormat(Format.XML);
        FHIRModel.setToStringPrettyPrinting(false);
        
        patient.accept(visitor);
        
        FHIRModel.setToStringPrettyPrinting(true);
        FHIRModel.setToStringIndentAmount(4);
        
        patient.accept(visitor);
    }
}

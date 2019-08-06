/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.test;

import java.io.FilterOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.generator.FHIRGenerator;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.validation.FHIRValidator;
import com.ibm.watsonhealth.fhir.model.visitor.PathAwareAbstractVisitor;

public class FHIRValidatorTest {
    public static void main(java.lang.String[] args) throws Exception {
        Id id = Id.builder().value(UUID.randomUUID().toString())
                .extension(Extension.builder()
                    .url("http://www.ibm.com/someExtension")
                    .value(String.of("Hello, World!"))
                    .build())
                .build();
        
        Meta meta = Meta.builder().versionId(Id.of("1"))
                .lastUpdated(Instant.now(true))
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
                
        Patient patient = Patient.builder()
                .id(id)
                .active(Boolean.TRUE)
                .multipleBirth(Integer.of(2))
                .meta(meta)
                .name(name)
                .birthDate(Date.of(LocalDate.now()))
                .build();
        
        FilterOutputStream out = new FilterOutputStream(System.out) {
            public void close() {
                // do nothing
            }
        };
        
        FHIRGenerator.generator(Format.JSON, true).generate(patient, out);
        
        System.out.println("");
        
        PathAwareAbstractVisitor.DEBUG = true;
        FHIRValidator.DEBUG = true;
        List<Issue> issues = FHIRValidator.validator(patient).validate();
        
        if (!issues.isEmpty()) {
            System.out.println("Issue(s) found:");
            for (Issue issue : issues) {
                System.out.println("    severity: " + issue.getSeverity().getValue() + ", type: " + issue.getCode().getValue() + ", details: " + issue.getDetails().getText().getValue() + ", expression: " + issue.getExpression().get(0).getValue());
            }
        }
    }
}

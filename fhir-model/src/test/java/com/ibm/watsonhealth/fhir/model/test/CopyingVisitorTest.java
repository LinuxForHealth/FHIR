/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotSame;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.generator.FHIRGenerator;
import com.ibm.watsonhealth.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.visitor.DeepCopyingVisitor;
import com.ibm.watsonhealth.fhir.model.visitor.ReferenceAdaptingVisitor;

public class CopyingVisitorTest {
    public static void main(java.lang.String[] args) throws Exception {
        Id id = Id.builder().value(UUID.randomUUID().toString())
                .extension(Extension.builder("http://www.ibm.com/someExtension")
                    .value(String.of("Hello, World!"))
                    .build())
                .build();
        
        Meta meta = Meta.builder().versionId(Id.of("1"))
                .lastUpdated(Instant.now(true))
                .build();
        
        String given = String.builder().value("John")
                .extension(Extension.builder("http://www.ibm.com/someExtension")
                    .value(String.of("value and extension"))
                    .build())
                .build();
        
        String otherGiven = String.builder()
                .extension(Extension.builder("http://www.ibm.com/someExtension")
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
        
        Reference providerRef = Reference.builder()
                .reference(String.of("urn:uuid:" + UUID.randomUUID()))
                .build();
                
        Patient patient = Patient.builder()
                .id(id)
                .active(Boolean.TRUE)
                .multipleBirth(Integer.of(2))
                .meta(meta)
                .name(name)
                .birthDate(Date.of(LocalDate.now()))
                .generalPractitioner(providerRef)
                .build();
        
        testDeepCopy(patient);
        testUpdateReferences(patient);
    }

    static void testDeepCopy(Resource resource) throws FHIRGeneratorException {
        DeepCopyingVisitor<Resource> visitor = new DeepCopyingVisitor<Resource>();
        resource.accept(visitor);
        Resource result = visitor.getResult();
        
        assertNotSame(result, resource);

        StringWriter writer1 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(resource, writer1);
        StringWriter writer2 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(result, writer2);
        assertEquals(writer2.toString(), writer1.toString());
        
        assertEquals(result, resource);
    }
    
    static void testUpdateReferences(Patient patient) throws FHIRGeneratorException {
        ReferenceAdaptingVisitor<Patient> visitor = new ReferenceAdaptingVisitor<Patient>();
        patient.accept(visitor);
        Patient result = visitor.getResult();
        
        assertNotSame(result, patient);

        StringWriter writer1 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(patient, writer1);
        StringWriter writer2 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(result, writer2);
        assertNotEquals(writer2.toString(), writer1.toString());
        
        System.out.println(writer1.toString());
        System.out.println(writer2.toString());
        
        assertNotEquals(result, patient);
    }
}

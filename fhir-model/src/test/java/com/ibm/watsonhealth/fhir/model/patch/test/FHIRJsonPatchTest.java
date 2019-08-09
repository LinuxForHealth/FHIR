/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.patch.test;

import static com.ibm.watsonhealth.fhir.model.type.String.string;

import java.io.FilterOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.generator.FHIRGenerator;
import com.ibm.watsonhealth.fhir.model.patch.FHIRJsonPatch;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.NarrativeStatus;

public class FHIRJsonPatchTest {
    private static final Map<String, Object> map = Collections.synchronizedMap(new WeakHashMap<>());
    
    public static void main(java.lang.String[] args) throws Exception {
        java.lang.String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";
        
        Id id = Id.builder()
                .value(UUID.randomUUID().toString())
                .build();
        
        Meta meta = Meta.builder()
                .versionId(Id.of("1"))
                .lastUpdated(Instant.now(ZoneOffset.UTC))
                .build();
        
        HumanName name = HumanName.builder()
                .given(string("John"))
                .family(string("Doe"))
                .build();
        
        Narrative text = Narrative.builder()
                .status(NarrativeStatus.GENERATED)
                .div(div)
                .build();
        
        Patient patient = Patient.builder()
                .id(id)
                .meta(meta)
                .text(text)
                .active(Boolean.TRUE)
                .name(name)
                .birthDate(Date.of("1980-01-01"))
                .build();
    
        StringWriter writer = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(patient, writer);
        
        System.out.println("");
        
        JsonArray array = Json.createPatchBuilder()
                .replace("/meta/versionId", "2")
                .replace("/meta/lastUpdated", java.time.Instant.now().toString())
                .replace("/name/0/given/0", "Jack")
                .add("/name/0/given/-", "Joe")
                .build()
                .toJsonArray();
        
        System.out.println("Patch: ");
        JsonWriterFactory factory = Json.createWriterFactory(Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true));
        factory.createWriter(new FilterOutputStream(System.out) {
            @Override
            public void close() {
                // do nothing
            }
        }).writeArray(array);
        
        System.out.println("Resource before patch: ");
        System.out.println(writer.toString());
                
        JsonObject object = Json.createReader(new StringReader(writer.toString())).readObject();
                
        FHIRJsonPatch patch = new FHIRJsonPatch(array);
        
        patient = patch.apply(object);
        
        System.out.println("Resource after patch: ");
        FHIRGenerator.generator(Format.JSON, true).generate(patient, System.out);
    }
}

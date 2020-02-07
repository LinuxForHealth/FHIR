/*
 * (C) Copyright IBM Corp. 2019, 2020
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.patch.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Time;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.path.patch.FHIRPathPatch;

public class FHIRPathPatchBuilderTest {
    @Test
    private void patchBuilderTest() throws Exception {
        Patient patient = Patient.builder().id("test").build();
        
        Patient patchedPatient = addViaPatch(patient);
        patient = addViaBuilder(patient);
        assertEquals(patchedPatient, patient);
        
        patchedPatient = deleteViaPatch(patient);
        patient = deleteViaBuilder(patient);
        assertEquals(patchedPatient, patient);
        
        patchedPatient = insertViaPatch(patient);
        patient = insertViaBuilder(patient);
        assertEquals(patchedPatient, patient);
        
        patchedPatient = moveViaPatch(patient);
        patient = moveViaBuilder(patient);
        assertEquals(patchedPatient, patient);
        
        patchedPatient = replaceViaPatch(patient);
        patient = replaceViaBuilder(patient);
        assertEquals(patchedPatient, patient);
    }

    private Patient addViaBuilder(Patient patient) {
        return patient.toBuilder()
                .identifier(Identifier.builder()
                    .system(Uri.of("mySystem"))
                    .value(string("it-me"))
                    .build())
                .name(HumanName.builder()
                               .given(string("First"))
                               .family(string("Last"))
                               .extension(Extension.builder()
                                   .url("myExtension")
                                   .extension(Extension.builder()
                                       .url("lunchTime")
                                       .value(Time.of("12:00:00"))
                                       .build())
                                   .build())
                               .build(),
                      HumanName.builder().family(string("Last")).build())
                .active(com.ibm.fhir.model.type.Boolean.TRUE)
                .build();
    }

    private Patient addViaPatch(Patient patient) throws FHIRPatchException {
        return FHIRPathPatch.builder()
                .add("Patient", "identifier", Identifier.builder().system(Uri.of("mySystem")).build())
                .add("Patient.identifier", "value", string("it-me"))
                .add("Patient", "name", HumanName.builder().family(string("Last")).build())
                .add("Patient", "name", HumanName.builder().family(string("Last")).build())
                .add("Patient.name[0]", "given", string("First"))
                .add("Patient.name[0]", "extension", Extension.builder().url("myExtension").build())
                .add("Patient.name[0].extension", "extension", Extension.builder().url("lunchTime").build())
                .add("Patient.name[0].extension.extension", "value", Time.of("12:00:00"))
                .add("Patient.identifier", "value", string("it-me"))
                .add("Patient", "active", com.ibm.fhir.model.type.Boolean.TRUE)
                .build()
                .apply(patient);
    }
    
    private Patient deleteViaBuilder(Patient patient) {
        return patient.toBuilder()
                .identifier(Collections.emptySet())
                .active(null)
                .build();
    }

    private Patient deleteViaPatch(Patient patient) throws FHIRPatchException {
        return FHIRPathPatch.builder()
                .delete("Patient.active")
                .delete("Patient.identifier")
                .build()
                .apply(patient);
    }

    private Patient insertViaBuilder(Patient patient) {
        List<HumanName> names = new ArrayList<>(patient.getName());
        names.add(2, HumanName.builder().family(string("Inserted")).build());
        
        List<Extension> name1extensions = new ArrayList<>(names.get(0).getExtension());
        name1extensions.add(0, Extension.builder().url("inserted").value(string("value")).build());
        names.set(0, names.get(0).toBuilder().extension(name1extensions).build());
        
        return patient.toBuilder()
                .name(names)
                .build();
    }

    private Patient insertViaPatch(Patient patient) throws FHIRPatchException {
        return FHIRPathPatch.builder()
                .insert("Patient.name", HumanName.builder().family(string("Inserted")).build(), 2)
                .insert("Patient.name[0].extension", 
                        Extension.builder().url("inserted").value(string("value")).build(), 0)
                .build()
                .apply(patient);
    }

    private Patient moveViaBuilder(Patient patient) {
        List<HumanName> names = new ArrayList<>(patient.getName());
        HumanName toMove = names.remove(0);
        names.add(2, toMove);
        return patient.toBuilder().name(names).build();
    }

    private Patient moveViaPatch(Patient patient) throws FHIRPatchException {
        return FHIRPathPatch.builder()
                .move("Patient.name", 0, 2)
                .build()
                .apply(patient);
    }

    private Patient replaceViaBuilder(Patient patient) {
        List<HumanName> names = new ArrayList<>(patient.getName());
        names.set(0, patient.getName().get(0).toBuilder().family(string("UpdatedLast")).build());
        return patient.toBuilder().name(names).build();
    }

    private Patient replaceViaPatch(Patient patient) throws FHIRPatchException {
        return FHIRPathPatch.builder()
                .replace("Patient.name[0].family", string("UpdatedLast"))
                .build()
                .apply(patient);
    }
}

/*
 * (C) Copyright IBM Corp. 2019, 2021
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
import com.ibm.fhir.model.resource.Patient.Communication;
import com.ibm.fhir.model.resource.Patient.Contact;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Time;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AccountStatus;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.DataAbsentReason;
import com.ibm.fhir.path.patch.FHIRPathPatch;

public class FHIRPathPatchBuilderTest {
    @Test
    private void patchBuilderTestIncremental() throws Exception {
        Patient patient = Patient.builder().id("test").build();

        Patient patchedPatient = buildAdd().apply(patient);
        patient = addViaBuilder(patient);
        assertEquals(patchedPatient, patient);

        patchedPatient = buildDelete().apply(patient);
        patient = deleteViaBuilder(patient);
        assertEquals(patchedPatient, patient);

        patchedPatient = buildInsert().apply(patient);
        patient = insertViaBuilder(patient);
        assertEquals(patchedPatient, patient);

        patchedPatient = buildMove().apply(patient);
        patient = moveViaBuilder(patient);
        assertEquals(patchedPatient, patient);

        patchedPatient = buildReplace().apply(patient);
        patient = replaceViaBuilder(patient);
        assertEquals(patchedPatient, patient);
    }

    @Test(expectedExceptions = FHIRPatchException.class)
    private void patchBuilderTestBadAddList() throws Exception {
        Patient patient = Patient.builder().id("test").build();
        Patient modifiedPatient = FHIRPathPatch.builder()
                .add("Patient", "communication", Contact.builder().name(HumanName.builder().family(string("Last")).build()).build())
                .build()
                .apply(patient);
        System.out.println(modifiedPatient);
    }

    @Test(expectedExceptions = FHIRPatchException.class)
    private void patchBuilderTestBadAddSingle() throws Exception {
        Patient patient = Patient.builder().id("test").build();
        Patient modifiedPatient = FHIRPathPatch.builder()
                .add("Patient", "birthDate", DateTime.now())
                .build()
                .apply(patient);
        System.out.println(modifiedPatient);
    }

    @Test(expectedExceptions = FHIRPatchException.class)
    private void patchBuilderTestBadAddSingleCode() throws Exception {
        Patient patient = Patient.builder().id("test").build();
        Patient modifiedPatient = FHIRPathPatch.builder()
                .add("Patient", "active", AccountStatus.UNKNOWN)
                .build()
                .apply(patient);
        System.out.println(modifiedPatient);
    }

    @Test(expectedExceptions = FHIRPatchException.class)
    private void patchBuilderTestBadAddDeepCode() throws Exception {
        Patient patient = Patient.builder().id("test").build();
        Patient modifiedPatient = FHIRPathPatch.builder()
                .add("Patient", "contact", Contact.builder()
                    .name(HumanName.builder().family(string("Last")).build())
                    .build())
                .add("Patient.contact", "gender", DataAbsentReason.MASKED)
                .build()
                .apply(patient);
        System.out.println(modifiedPatient);
    }

    @Test(expectedExceptions = FHIRPatchException.class)
    private void patchBuilderTestBadInsert() throws Exception {
        Patient patient = Patient.builder().id("test").build();
        Patient modifiedPatient = FHIRPathPatch.builder()
                .add("Patient", "communication", Communication.builder()
                        .language(CodeableConcept.builder()
                                .coding(Coding.builder()
                                        .system(Uri.of("urn:ietf:bcp:47"))
                                        .code(Code.of("en"))
                                        .build())
                                .build())
                        .build())
                .insert("Patient.communication", Contact.builder().name(HumanName.builder().family(string("Last")).build()).build(), 1)
                .build()
                .apply(patient);
        System.out.println(modifiedPatient);
    }

    @Test(expectedExceptions = FHIRPatchException.class)
    private void patchBuilderTestBadReplaceList() throws Exception {
        Patient patient = Patient.builder().id("test").build();
        Patient modifiedPatient = FHIRPathPatch.builder()
                .add("Patient", "communication", Communication.builder()
                        .language(CodeableConcept.builder()
                                .coding(Coding.builder()
                                        .system(Uri.of("urn:ietf:bcp:47"))
                                        .code(Code.of("en"))
                                        .build())
                                .build())
                        .build())
                .replace("Patient.communication[0]", Contact.builder().name(HumanName.builder().family(string("Last")).build()).build())
                .build()
                .apply(patient);
        System.out.println(modifiedPatient);
    }

    @Test(expectedExceptions = FHIRPatchException.class)
    private void patchBuilderTestBadReplaceSingle() throws Exception {
        Patient patient = Patient.builder().id("test").build();
        FHIRPathPatch.builder()
                .add("Patient", "birthDate", Date.of("2021"))
                .replace("Patient.birthDate", DateTime.now())
                .build()
                .apply(patient);
    }

    @Test(expectedExceptions = FHIRPatchException.class)
    private void patchBuilderTestBadReplaceSingleCode() throws Exception {
        Patient patient = Patient.builder().id("test").build();
        FHIRPathPatch.builder()
                .add("Patient", "active", com.ibm.fhir.model.type.Boolean.TRUE)
                .replace("Patient.active", Code.of("false"))
                .build()
                .apply(patient);
    }

    @Test
    private void patchBuilderTestAll() throws Exception {
        Patient patient = Patient.builder().id("test").build();

        FHIRPathPatch allPatch = FHIRPathPatch.builder()
            .from(buildAdd())
            .from(buildDelete())
            .from(buildInsert())
            .from(buildMove())
            .from(buildReplace())
            .build();

        Patient patchedPatient = allPatch.apply(patient);

        patient = addViaBuilder(patient);
        patient = deleteViaBuilder(patient);
        patient = insertViaBuilder(patient);
        patient = moveViaBuilder(patient);
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
                .contact(Contact.builder()
                                .name(HumanName.builder().family(string("Last")).build())
                                .gender(AdministrativeGender.FEMALE)
                                .build())
                .active(com.ibm.fhir.model.type.Boolean.TRUE)
                .build();
    }

    private FHIRPathPatch buildAdd() throws FHIRPatchException {
        return FHIRPathPatch.builder()
                .add("Patient", "identifier", Identifier.builder().system(Uri.of("mySystem")).build())
                .add("Patient.identifier", "value", string("it-me"))
                .add("Patient", "name", HumanName.builder().family(string("Last")).build())
                .add("Patient", "name", HumanName.builder().family(string("Last")).build())
                .add("Patient", "contact", Contact.builder().name(HumanName.builder().family(string("Last")).build()).build())
                .add("Patient.contact", "gender", AdministrativeGender.FEMALE)
                .add("Patient.name[0]", "given", string("First"))
                .add("Patient.name[0]", "extension", Extension.builder().url("myExtension").build())
                .add("Patient.name[0].extension", "extension", Extension.builder().url("lunchTime").build())
                .add("Patient.name[0].extension.extension", "value", Time.of("12:00:00"))
                .add("Patient", "active", com.ibm.fhir.model.type.Boolean.TRUE)
                .build();
    }

    private Patient deleteViaBuilder(Patient patient) {
        List<HumanName> names = new ArrayList<>(patient.getName());
        names.add(0, names.remove(0).toBuilder()
                .given(Collections.emptySet())
                .build());
        return patient.toBuilder()
                .identifier(Collections.emptySet())
                .active((Boolean)null)
                .name(names)
                .build();
    }

    private FHIRPathPatch buildDelete() throws FHIRPatchException {
        return FHIRPathPatch.builder()
                .delete("Patient.active")
                .delete("Patient.identifier")
                .delete("Patient.name[0].given")
                .build();
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

    private FHIRPathPatch buildInsert() throws FHIRPatchException {
        return FHIRPathPatch.builder()
                .insert("Patient.name", HumanName.builder().family(string("Inserted")).build(), 2)
                .insert("Patient.name[0].extension",
                        Extension.builder().url("inserted").value(string("value")).build(), 0)
                .build();
    }

    private Patient moveViaBuilder(Patient patient) {
        List<HumanName> names = new ArrayList<>(patient.getName());
        HumanName toMove = names.remove(0);
        names.add(2, toMove);
        return patient.toBuilder().name(names).build();
    }

    private FHIRPathPatch buildMove() throws FHIRPatchException {
        return FHIRPathPatch.builder()
                .move("Patient.name", 0, 2)
                .build();
    }

    private Patient replaceViaBuilder(Patient patient) {
        List<HumanName> names = new ArrayList<>(patient.getName());
        names.set(0, patient.getName().get(0).toBuilder().family(string("UpdatedLast")).build());
        return patient.toBuilder().name(names).build();
    }

    private FHIRPathPatch buildReplace() throws FHIRPatchException {
        return FHIRPathPatch.builder()
                .replace("Patient.name[0].family", string("UpdatedLast"))
                .build();
    }
}

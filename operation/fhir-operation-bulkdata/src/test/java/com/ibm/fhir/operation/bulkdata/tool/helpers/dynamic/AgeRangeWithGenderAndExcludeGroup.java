/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.tool.helpers.dynamic;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Xhtml.xhtml;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.resource.Group.Characteristic;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Age;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Xhtml;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.GroupType;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.model.type.code.ObservationStatus;
import com.ibm.fhir.model.type.code.QuantityComparator;

/**
 * Shows a simple age included in a Dynamic Group.
 */
public class AgeRangeWithGenderAndExcludeGroup extends GroupExample {
    @Override
    public String filename() {
        return "age-range-with-gender-and-exclude";
    }

    @Override
    public Group group() {
        java.lang.String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";

        java.lang.String id = UUID.randomUUID().toString();

        Meta meta = Meta.builder()
                .versionId(Id.of("1"))
                .lastUpdated(Instant.now(ZoneOffset.UTC))
                .build();

        Narrative text = Narrative.builder()
                .status(NarrativeStatus.GENERATED)
                .div(xhtml(div))
                .build();

        com.ibm.fhir.model.type.Boolean active = com.ibm.fhir.model.type.Boolean.of(true);

        com.ibm.fhir.model.type.Boolean actual = com.ibm.fhir.model.type.Boolean.of(false);

        Collection<Characteristic> characteristics = new ArrayList<>();
        characteristics.add(generateLowerBoundBirthdateCharacteristic());
        characteristics.add(generateUpperBoundBirthdateCharacteristic());
        characteristics.add(generateGenderCharacteristic());
        characteristics.add(generateExcludeCharacteristic());

        Group group = Group.builder()
                .id(id)
                .meta(meta)
                .text(text)
                .active(active)
                .type(GroupType.PERSON)
                .actual(actual)
                .name(string(filename()))
                .characteristic(characteristics)
                .build();
        return group;
    }

    private Characteristic generateGenderCharacteristic() {
        CodeableConcept code = CodeableConcept.builder()
                .coding(Coding.builder().code(Code.of("AdministrativeGender"))
                    .system(Uri.of("http://hl7.org/fhir/administrative-gender"))
                    .build())
            .text(string("Gender"))
            .build();

        CodeableConcept value = CodeableConcept.builder().coding(Coding.builder().code(Code.of("Female"))
            .system(Uri.of("http://hl7.org/fhir/administrative-gender")).build())
            .text(string("female"))
            .build();

        Characteristic characteristic = Characteristic.builder()
            .code(code)
            .value(value)
            .exclude(com.ibm.fhir.model.type.Boolean.FALSE)
            .build();
        return characteristic;
    }

    private Characteristic generateExcludeCharacteristic() {
        CodeableConcept code = CodeableConcept.builder()
                .coding(Coding.builder().code(Code.of("82810-3"))
                    .system(Uri.of("http://loinc.org"))
                    .build())
            .text(string("Pregnancy status"))
            .build();

        CodeableConcept value = CodeableConcept.builder().coding(Coding.builder().code(Code.of("LA15173-0"))
            .system(Uri.of("http://loinc.org")).build())
            .text(string("Pregnant"))
            .build();

        Characteristic characteristic = Characteristic.builder()
            .code(code)
            .value(value)
            .exclude(com.ibm.fhir.model.type.Boolean.TRUE)
            .build();
        return characteristic;
    }

    private Characteristic generateLowerBoundBirthdateCharacteristic() {
        CodeableConcept code = CodeableConcept.builder().coding(Coding.builder().code(Code.of("29553-5"))
            .system(Uri.of("http://loinc.org")).build())
            .text(string("Age calculated"))
            .build();

        Age value = Age.builder()
                .system(Uri.of("http://unitsofmeasure.org"))
                .code(Code.of("a"))
                .unit(string("years"))
                .value(Decimal.of("13"))
                .comparator(QuantityComparator.GREATER_OR_EQUALS)
                .build();

        Characteristic characteristic = Characteristic.builder()
            .code(code)
            .value(value)
            .exclude(com.ibm.fhir.model.type.Boolean.FALSE)
            .build();
        return characteristic;
    }

    private Characteristic generateUpperBoundBirthdateCharacteristic() {
        CodeableConcept code = CodeableConcept.builder().coding(Coding.builder().code(Code.of("29553-5"))
            .system(Uri.of("http://loinc.org")).build())
            .text(string("Age calculated"))
            .build();

        Age value = Age.builder()
                .system(Uri.of("http://unitsofmeasure.org"))
                .code(Code.of("a"))
                .unit(string("years"))
                .value(Decimal.of("56"))
                .comparator(QuantityComparator.LESS_OR_EQUALS)
                .build();

        Characteristic characteristic = Characteristic.builder()
            .code(code)
            .value(value)
            .exclude(com.ibm.fhir.model.type.Boolean.FALSE)
            .build();
        return characteristic;
    }

    @Override
    public Bundle sampleData() {
        Bundle.Entry entry = Bundle.Entry.builder()
                .resource(buildTestPatient())
                .request(Request.builder().method(HTTPVerb.POST)
                    .url(Uri.of("Patient")).build())
                .build();
        Bundle.Entry entryObservation = Bundle.Entry.builder()
                .resource(buildObservation("1-2-3-4"))
                .request(Request.builder().method(HTTPVerb.POST)
                    .url(Uri.of("Observation")).build())
                .build();
        return Bundle.builder().type(BundleType.TRANSACTION).entry(entry, entryObservation).build();
    }

    private Patient buildTestPatient() {
        String id = UUID.randomUUID().toString();

        Meta meta =
                Meta.builder()
                    .versionId(Id.of("1"))
                    .lastUpdated(Instant.now(ZoneOffset.UTC))
                    .build();

        com.ibm.fhir.model.type.String given =
                com.ibm.fhir.model.type.String.builder()
                .value("John")
                .build();

        HumanName name =
                HumanName.builder()
                    .id("someId")
                    .given(given)
                    .family(string("Doe")).build();

        java.lang.String uUID = UUID.randomUUID().toString();

        Reference providerRef =
                Reference.builder().reference(string("urn:uuid:" + uUID)).build();

        return Patient.builder().id(id)
                .active(com.ibm.fhir.model.type.Boolean.TRUE)
                .multipleBirth(com.ibm.fhir.model.type.Integer.of(2))
                .meta(meta).name(name).birthDate(Date.of(LocalDate.now().minus(30,ChronoUnit.YEARS)))
                .gender(AdministrativeGender.FEMALE)
                .generalPractitioner(providerRef).text(
                    Narrative.builder()
                        .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">loaded from the datastore</div>"))
                        .status(NarrativeStatus.GENERATED).build())
                .build();
    }

    public Observation buildObservation(String patientId) {
        CodeableConcept code = CodeableConcept.builder().coding(
            Coding.builder()
                .code(Code.of("82810-3"))
                .system(Uri.of("http://loinc.org"))
                .display(string("Pregnancy status"))
                .build())
            .text(string("Pregnancy status"))
            .build();

        Narrative text = Narrative.builder()
                .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">loaded from the datastore</div>"))
                .status(NarrativeStatus.GENERATED).build();

        CodeableConcept value = CodeableConcept.builder().coding(
            Coding.builder()
                .code(Code.of("LA15173-0"))
                .system(Uri.of("http://loinc.org"))
                .display(string("Pregnant"))
                .build())
            .text(string("Pregnant"))
            .build();

        Observation observation = Observation.builder()
                .text(text)
                .status(ObservationStatus.FINAL)
                .code(code)
                .subject(Reference.builder().reference(string("Patient/" + patientId)).build())
                .value(value)
                .effective(DateTime.of(LocalDate.of(2020, 6, 20)))
             .build();
        return observation;
    }
}
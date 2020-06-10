package com.ibm.fhir.operation.bulkdata.util.group.examples;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Xhtml.xhtml;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.resource.Group.Characteristic;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.GroupType;
import com.ibm.fhir.model.type.code.NarrativeStatus;

/*
 * @see README.md for Group details.
 */
public class AgeRangeExample extends GroupExample {
    @Override
    public String typeName() {
        return "AgeRangeExample";
    }

    @Override
    public List<Group> groups() {
        List<Group> groups = new ArrayList<>();
        // code: birthdate
        // range: >= 13 to < 56

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
        // Blood Pressure
        characteristics.add(generateFirstCharacteristic());
        // Age
        characteristics.add(generateSecondCharacteristic());



        Group group = Group.builder()
                .id(id)
                .meta(meta)
                .active(active)
                .type(GroupType.PERSON)
                .actual(actual)
                .name(string("age-example-with-bloodpressure"))
                .characteristic(characteristics)
                .build();

        groups.add(group);

        // 0 compliant and non-compliant

        // #1 Population
        // Inclusion:
        // - Gender: female
        // - Age: >= 13 to < 56
        // Exclusion:
        // - Pregnancy in last 12 months

        // #2 Population
        // Inclusion:
        // - Gender: female
        // - Age: >= 13 to < 56
        // - Encounter:
            // -ICD9    V72.31  Routine gynecological examination
            // in last 36 mo look back
        // Exclusion:
        // - Pregnancy in last 12 months

        // nested group

        return groups;
    }

    private Characteristic generateFirstCharacteristic() {
        CodeableConcept code = CodeableConcept.builder().coding(Coding.builder().code(Code.of("55284-4"))
            .system(Uri.of("http://loinc.org")).build())
            .text(string("Blood pressure systolic & diastolic"))
            .extension(Extension.builder()
                .url("http://www.ibm.com/search/code")
                .value(string("component-value-quantity"))
                .build())
            .build();

        // Value -- CodeableConcept.class, Boolean.class, Quantity.class, Range.class, Reference.class)
        Collection<Extension> extensions = new ArrayList<>();
        extensions.add(Extension.builder()
            .url("http://www.ibm.com/search/code")
            .value(string("_has:Observation:patient:combo-value-quantity"))
            .build());
        extensions.add(Extension.builder()
            .url("http://www.ibm.com/search/value")
            .value(string("gt123.0||mmHg"))
            .build());

        CodeableConcept value = CodeableConcept.builder().coding(Coding.builder().code(Code.of("55284-4"))
            .system(Uri.of("http://loinc.org")).build())
            .text(string("Blood pressure systolic & diastolic"))
            .extension(extensions)
            .build();

        com.ibm.fhir.model.type.Boolean exclude = com.ibm.fhir.model.type.Boolean.of(false);
        Characteristic characteristic = Characteristic.builder()
            .code(code)
            .value(value)
            .exclude(exclude)
            .build();
        return characteristic;
    }

    private Characteristic generateSecondCharacteristic() {
        CodeableConcept code = CodeableConcept.builder().coding(Coding.builder().code(Code.of("29553-5"))
            .system(Uri.of("http://loinc.org")).build())
            .text(string("Age calculated"))
            .extension(Extension.builder()
                .url("http://www.ibm.com/search/code")
                .value(string("age"))
                .build())
            .build();

        Collection<Extension> extensions = new ArrayList<>();
        extensions.add(Extension.builder()
            .url("http://www.ibm.com/search/code")
            .value(string("age"))
            .build());
        extensions.add(Extension.builder()
            .url("http://www.ibm.com/search/value")
            .value(string("20,35"))
            .build());

        CodeableConcept value = CodeableConcept.builder().coding(Coding.builder().code(Code.of("55284-4"))
            .system(Uri.of("http://loinc.org")).build())
            .text(string("Blood pressure systolic & diastolic"))
            .extension(extensions)
            .build();

        com.ibm.fhir.model.type.Boolean exclude = com.ibm.fhir.model.type.Boolean.of(false);
        Characteristic characteristic = Characteristic.builder()
            .code(code)
            .value(value)
            .exclude(exclude)
            .build();
        return characteristic;
    }

    @Override
    public Bundle sampleData() {
        // TODO Auto-generated method stub
        return null;
    }
}
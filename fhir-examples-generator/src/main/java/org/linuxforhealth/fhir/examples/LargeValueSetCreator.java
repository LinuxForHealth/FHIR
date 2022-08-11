/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.examples;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.resource.ValueSet;
import org.linuxforhealth.fhir.model.resource.ValueSet.Expansion.Contains;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class LargeValueSetCreator {
    private static final Uri TEST_URI = Uri.of("http://example.com/fhir/valueset/test");

    private final PodamFactory podam;

    public LargeValueSetCreator() throws IOException {
        super();
        podam = new PodamFactoryImpl();
    }

    public ValueSet createValueSet() throws Exception {
        final ValueSet.Builder vsBuilder = ValueSet.builder().status(PublicationStatus.DRAFT);
        final ValueSet.Expansion.Builder expansionBuilder = ValueSet.Expansion.builder().timestamp(DateTime.now());
        final ValueSet.Expansion.Contains.Builder template = ValueSet.Expansion.Contains.builder()
                .system(TEST_URI)
                .version(string("1"));

        for (int i=0; i < 100_000; i++) {
            expansionBuilder.contains(template
                    .code(Code.of(podam.manufacturePojo(String.class)))
                    .display(string(podam.manufacturePojo(String.class)))
                    .build());
        }

        return vsBuilder.expansion(expansionBuilder.build()).build();
    }

    public Set<String> convertToHashSet(ValueSet vs) throws Exception {
        final Set<String> set = new HashSet<>();
        vs.getExpansion().getContains().stream()
                .forEach(concept -> set.add(concept.getSystem().getValue() + "|" + concept.getCode().getValue()));
//        .forEach(concept -> set.add(Coding.builder()
//            .system(concept.getSystem())
//            .version(concept.getVersion())
//            .code(concept.getCode())
//            .build())
//            );

        return set;
    }

    public static void main(String[] args) throws Exception {
        LargeValueSetCreator creator = new LargeValueSetCreator();
        ValueSet vs = creator.createValueSet();

        Path json = Paths.get("ValueSet-large.json");
        try (BufferedWriter writer = Files.newBufferedWriter(json, StandardCharsets.UTF_8)) {
            FHIRGenerator.generator(Format.JSON).generate(vs, writer);
        }

        Path xml = Paths.get("ValueSet-large.xml");
        try (BufferedWriter writer = Files.newBufferedWriter(xml, StandardCharsets.UTF_8)) {
            FHIRGenerator.generator(Format.XML).generate(vs, writer);
        }

        Path txt = Paths.get("ValueSet-large.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(txt, StandardCharsets.UTF_8)) {
            for (Contains concept : vs.getExpansion().getContains()) {
                writer.write(concept.getSystem().getValue() + "|" + concept.getCode().getValue() + "\n");
            }
        }

        Set<String> set = creator.convertToHashSet(vs);
        Path serializedHashSet = Paths.get("ValueSet-large-HashSet.ser");
        try (FileOutputStream os = new FileOutputStream(serializedHashSet.toFile())) {
            ObjectOutputStream out = new ObjectOutputStream(os);
            out.writeObject(set);
        }
    }
}

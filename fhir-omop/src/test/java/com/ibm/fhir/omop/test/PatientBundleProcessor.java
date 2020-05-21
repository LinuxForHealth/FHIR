/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.omop.test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.util.ReferenceMappingVisitor;

public class PatientBundleProcessor {
    public static void main(String[] args) throws Exception {
        File dir = new File("src/test/resources/fhir/");
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                System.out.println(file.getName());
                Bundle bundle = null;
                try (FileReader reader = new FileReader(file)) {
                    Map<String, String> localRefMap = new HashMap<>();
                    bundle = FHIRParser.parser(Format.JSON).parse(reader);
                    for (Bundle.Entry entry : bundle.getEntry()) {
                        if (entry.getFullUrl() == null) {
                            continue;
                        }
                        String localIdentifier = entry.getFullUrl().getValue();
                        Resource resource = entry.getResource();
                        String externalIdentifier = resource.getClass().getSimpleName() + "/" + resource.getId();
                        localRefMap.put(localIdentifier, externalIdentifier);
                    }
                    ReferenceMappingVisitor<Bundle> visitor = new ReferenceMappingVisitor<>(localRefMap);
                    bundle.accept(visitor);
                    bundle = visitor.getResult();
                }
                try (FileWriter writer = new FileWriter(file)) {
                    bundle = Bundle.builder()
                        .type(BundleType.COLLECTION)
                        .entry(bundle.getEntry().stream()
                            .map(entry -> Entry.builder()
                                .resource(entry.getResource())
                                .build())
                            .collect(Collectors.toList()))
                        .build();
                    FHIRGenerator.generator(Format.JSON, true).generate(bundle, writer);
                }
            }
        }
    }
}

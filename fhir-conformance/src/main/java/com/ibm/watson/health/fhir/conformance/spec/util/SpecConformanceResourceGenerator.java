/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.conformance.spec.util;

import static com.ibm.watson.health.fhir.conformance.util.ConformanceUtil.getUrl;
import static com.ibm.watson.health.fhir.conformance.util.ConformanceUtil.isConformanceResource;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.resource.Bundle;
import com.ibm.watson.health.fhir.model.resource.Resource;

public class SpecConformanceResourceGenerator {
    private static final List<String> DEFINITIONS = Arrays.asList(
        "definitions/conceptmaps.json", 
        "definitions/dataelements.json", 
        "definitions/extension-definitions.json", 
        "definitions/profiles-others.json", 
        "definitions/profiles-resources.json", 
        "definitions/profiles-types.json", 
        "definitions/v2-tables.json", 
        "definitions/v3-codesystems.json", 
        "definitions/valuesets.json");
    
    public static void main(String[] args) throws Exception {
        List<String> index = new ArrayList<>();
        for (String definition : DEFINITIONS) {
            try (FileReader reader = new FileReader(definition)) {
                Bundle bundle = FHIRParser.parser(Format.JSON).parse(reader);
                for (Bundle.Entry entry : bundle.getEntry()) {
                    Resource resource = entry.getResource();
                    if (!isConformanceResource(resource)) {
                        continue;
                    }
                    String fileName = resource.getClass().getSimpleName() + "-" + resource.getId().getValue() + ".json";
                    File file = new File("src/main/resources/conformance/" + fileName);
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                    }
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(resource.toString());
                    }
                    index.add(String.format("%s,%s", getUrl(resource), "conformance/" + fileName));
                }
            }
        }
        Collections.sort(index);
        Files.write(Paths.get("src/main/resources/index"), index);
    }
}
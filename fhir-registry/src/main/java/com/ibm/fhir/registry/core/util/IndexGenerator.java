/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.core.util;

import static com.ibm.fhir.registry.util.FHIRRegistryUtil.*;
import static com.ibm.fhir.registry.util.FHIRRegistryUtil.getVersion;
import static com.ibm.fhir.registry.util.FHIRRegistryUtil.isDefinitionalResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.registry.util.Index;
import com.ibm.fhir.registry.util.Index.Entry;

public class IndexGenerator {
    private static final List<String> DEFINITIONS = Arrays.asList(
        "definitions/conceptmaps.json", 
        "definitions/dataelements.json", 
        "definitions/extension-definitions.json", 
        "definitions/profiles-others.json", 
        "definitions/profiles-resources.json", 
        "definitions/profiles-types.json", 
        "definitions/search-parameters.json", 
        "definitions/v2-tables.json", 
        "definitions/v3-codesystems.json", 
        "definitions/valuesets.json");
    
    public static void main(String[] args) throws Exception {
        Index index = new Index(1);
        for (String definition : DEFINITIONS) {
            try (FileReader reader = new FileReader(definition)) {
                Bundle bundle = FHIRParser.parser(Format.JSON).parse(reader);
                for (Bundle.Entry entry : bundle.getEntry()) {
                    Resource resource = entry.getResource();
                    
                    if (!isDefinitionalResource(resource)) {
                        continue;
                    }
                    
                    String id = resource.getId();
                    if (id == null) {
                        continue;
                    }

                    String url = getUrl(resource);
                    String version = getVersion(resource);
                    if (url == null || version == null) {
                        continue;
                    }
                    
                    String fileName = resource.getClass().getSimpleName() + "-" + id + ".json";
                    File file = new File("src/main/resources/hl7/fhir/core/package/" + fileName);
                    
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                    }
                    
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(resource.toString());
                    }
                    
                    index.add(Entry.entry(resource));
                }
            }
        }
        try (OutputStream out = new FileOutputStream("src/main/resources/hl7/fhir/core/package/.index.json")) {
            index.store(out);
        }
    }
}
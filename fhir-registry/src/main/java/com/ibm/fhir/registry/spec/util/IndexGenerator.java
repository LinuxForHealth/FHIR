/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.spec.util;

import java.io.FileOutputStream;
import java.io.FileReader;
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
                    index.add(Entry.entry(resource, Format.JSON));
                }
            }
        }
        try (OutputStream out = new FileOutputStream("src/main/resources/fhir-registry.index.json")) {
            index.store(out);
        }
    }
}
/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.spec.util;

import static com.ibm.fhir.registry.util.FHIRRegistryUtil.getUrl;
import static com.ibm.fhir.registry.util.FHIRRegistryUtil.getVersion;
import static com.ibm.fhir.registry.util.FHIRRegistryUtil.isDefinitionalResource;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.StructureDefinition;

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
        List<String> index = new ArrayList<>();
        for (String definition : DEFINITIONS) {
            try (FileReader reader = new FileReader(definition)) {
                Bundle bundle = FHIRParser.parser(Format.JSON).parse(reader);
                for (Bundle.Entry entry : bundle.getEntry()) {
                    Resource resource = entry.getResource();
                    if (!isDefinitionalResource(resource)) {
                        continue;
                    }
                    String url = getUrl(resource);
                    String version = getVersion(resource);
                    int idx = url.indexOf("|");
                    if (idx != -1) {
                        version = url.substring(idx + 1);
                        url = url.substring(0, idx);
                    }
                    if (url != null && version != null) {
                        String resourceTypeName = resource.getClass().getSimpleName();
                        String id = (resource.getId() != null) ? resource.getId() : "";
                        String kind = "";
                        String type = "";
                        String derivation = "";
                        if (resource instanceof StructureDefinition) {
                            StructureDefinition structureDefinition = (StructureDefinition) resource;
                            kind = structureDefinition.getKind().getValue();
                            type = structureDefinition.getType().getValue();
                            derivation = (structureDefinition.getDerivation() != null) ? structureDefinition.getDerivation().getValue() : "";
                        } else if (resource instanceof SearchParameter) {
                            SearchParameter searchParameter = (SearchParameter) resource;
                            type = searchParameter.getType().getValue();
                        }
                        String fileName = resourceTypeName + "-" + resource.getId() + ".json";
                        File file = new File("src/main/resources/definitions/" + fileName);
                        if (!file.exists()) {
                            file.getParentFile().mkdirs();
                        }
                        try (FileWriter writer = new FileWriter(file)) {
                            writer.write(resource.toString());
                        }
                        index.add(String.format("%s,%s,%s,%s,%s,%s,%s,%s", resourceTypeName, id, url, version, kind, type, derivation, "definitions/" + fileName));
                    }
                }
            }
        }
        Collections.sort(index);
        Files.write(Paths.get("src/main/resources/spec.index"), index);
    }
}
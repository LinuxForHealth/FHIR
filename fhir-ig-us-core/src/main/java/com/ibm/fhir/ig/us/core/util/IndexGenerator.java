/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core.util;

import static com.ibm.fhir.registry.util.FHIRRegistryUtil.*;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;

public class IndexGenerator {
    public static void main(String[] args) throws Exception {
        List<String> index = new ArrayList<>();
        File dir = new File("src/main/resources/resources/");
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                try (FileReader reader = new FileReader(file)) {
                    try {
                        Resource resource = FHIRParser.parser(Format.JSON).parse(reader);
                        if (!isDefinitionalResource(resource)) {
                            continue;
                        }
                        String url = getUrl(resource);
                        String version = getVersion(resource);
                        if (url != null && version != null) {
                            index.add(String.format("%s,%s,%s", url, version, "resources/" + file.getName()));
                        }
                    } catch (FHIRParserException e) {
                        System.err.println("Unable to add: " + file.getName() + " to the index due to exception: " + e.getMessage());
                    }
                }
            }
        }
        Collections.sort(index);
        Files.write(Paths.get("src/main/resources/us-core.index"), index);
    }
}
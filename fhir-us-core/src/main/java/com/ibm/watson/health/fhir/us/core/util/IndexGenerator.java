package com.ibm.watson.health.fhir.us.core.util;

/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import static com.ibm.watson.health.fhir.conformance.util.ConformanceUtil.getUrl;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.parser.exception.FHIRParserException;
import com.ibm.watson.health.fhir.model.resource.Resource;

public class IndexGenerator {
    public static void main(String[] args) throws Exception {
        List<String> index = new ArrayList<>();
        File dir = new File("src/main/resources/conformance/");
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                try (FileReader reader = new FileReader(file)) {
                    try {
                        Resource resource = FHIRParser.parser(Format.JSON).parse(reader);
                        index.add(String.format("%s,%s", getUrl(resource), "conformance/" + file.getName()));
                    } catch (FHIRParserException e) {
                        System.err.println("Unable to add: " + file.getName() + " to the index due to exception: " + e.getMessage());
                    }
                }
            }
        }
        Collections.sort(index);
        Files.write(Paths.get("src/main/resources/index"), index);
    }
}
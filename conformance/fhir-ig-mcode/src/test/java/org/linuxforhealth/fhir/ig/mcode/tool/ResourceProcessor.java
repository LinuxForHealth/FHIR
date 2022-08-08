/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.ig.mcode.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.stream.Collectors;

/**
 * This class is used to process resources in the mCODE IG definitions package and fix issues related to:
 * <a href="https://jira.hl7.org/projects/FHIR/issues/FHIR-26611">https://jira.hl7.org/projects/FHIR/issues/FHIR-26611</a>
 */
public class ResourceProcessor {
    public static void main(String[] args) throws Exception {
        File dir = new File("src/main/resources/hl7/fhir/us/mcode/package/");
        for (File file : dir.listFiles()) {
            String fileName = file.getName();
            if (!fileName.endsWith(".json") || file.isDirectory()) {
                continue;
            }
            String json = null;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String prefix = fileName.replace(".json", "");
                json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                json = json.replace("{{[type]}}-{{[id]}}", prefix);
            }
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(json);
            }
        }
    }
}

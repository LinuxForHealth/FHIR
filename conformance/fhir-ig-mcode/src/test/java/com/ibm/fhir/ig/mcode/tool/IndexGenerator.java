/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.mcode.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.registry.util.Index;
import com.ibm.fhir.registry.util.Index.Entry;

public class IndexGenerator {
    public static void main(String[] args) throws Exception {
        Index index = new Index(1);
        File dir = new File("src/main/resources/hl7/fhir/us/mcode/package/");
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                try (FileReader reader = new FileReader(file)) {
                    try {
                        Resource resource = FHIRParser.parser(Format.JSON).parse(reader);
                        index.add(Entry.entry(resource));
                    } catch (FHIRParserException e) {
                        System.err.println("Unable to add: " + file.getName() + " to the index due to exception: " + e.getMessage());
                    }
                }
            }
        }
        try (OutputStream out = new FileOutputStream("src/main/resources/hl7/fhir/us/mcode/package/.index.json")) {
            index.store(out);
        }
    }
}

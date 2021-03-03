/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.tool;

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

/**
 * Generates the Index file for the IG
 */
public class IndexGenerator {
    public static void main(String[] args) throws Exception {
        final String DIR_PATH = "src/main/resources/hl7/fhir/uv/bulkdata/package/";
        final String INDEX_FILE_PATH = DIR_PATH + ".index.json";

        Index index = new Index(1);
        File dir = new File(DIR_PATH);
        for (File file : dir.listFiles()) {
            if (!file.isDirectory() && !file.getPath().equals(INDEX_FILE_PATH)) {
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
        try (OutputStream out = new FileOutputStream(INDEX_FILE_PATH)) {
            index.store(out);
        }
    }
}

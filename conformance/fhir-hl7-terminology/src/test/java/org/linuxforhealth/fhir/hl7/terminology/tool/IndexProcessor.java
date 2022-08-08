/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.hl7.terminology.tool;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

import org.linuxforhealth.fhir.registry.util.Index;

/**
 * Strip out any index entries that are missing required fields for our registry
 */
public class IndexProcessor {

    public static void main(String[] args) throws Exception {
        Index index = new Index(1);

        File indexFile = new File("src/main/resources/hl7/terminology/310/package/.index.json");
        try (Reader reader = new FileReader(indexFile)) {
            index.load(reader);
        }

        try (Writer writer = new FileWriter(indexFile)) {
            index.store(writer);
        }

    }
}
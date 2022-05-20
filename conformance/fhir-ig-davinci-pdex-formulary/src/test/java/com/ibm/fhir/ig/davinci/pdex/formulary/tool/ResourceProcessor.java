/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.ig.davinci.pdex.formulary.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;

/**
 * This class fixes the following issues with the packaged DaVinci PDEX US Drug Formulary artifacts:
 * <ol>
 * <li>The provided CapabilityStatement contains invalid XHTML</li>
 * </ol>
 */
public class ResourceProcessor {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("Please specify one or more version qualifiers (e.g. '1.0.1 1.1.0')");
        }

        for (String version : args) {
            String packageVersion = version.replace(".", "");
            updateInPlace(packageVersion);
        }
    }

    public static void updateInPlace(String packageVersion) throws Exception {
        Map<String, Object> writerConfig = Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        JsonReaderFactory jsonReaderFactory = Json.createReaderFactory(null);
        JsonWriterFactory jsonWriterFactory = Json.createWriterFactory(writerConfig);
        JsonBuilderFactory jsonBuilderFactory = Json.createBuilderFactory(null);

        File mainDir = new File("src/main/resources/hl7/fhir/us/davinci-pdex-formulary/" + packageVersion + "/package/");
        File examplesDir = new File("src/test/resources/examples/" + packageVersion);
        for (File dir : List.of(mainDir, examplesDir)) {
            for (File file : dir.listFiles()) {
                String fileName = file.getName();
                if (!fileName.endsWith(".json") || file.isDirectory()
                        || fileName.startsWith(".index.json")
                        || fileName.startsWith("package.json")
                        ) {
                    continue;
                }
                JsonObject jsonObject = null;
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    JsonReader jsonReader = jsonReaderFactory.createReader(reader);
                    jsonObject = jsonReader.readObject();

                    JsonObjectBuilder jsonObjectBuilder = jsonBuilderFactory.createObjectBuilder(jsonObject);

                    JsonObject text = jsonObject.getJsonObject("text");
                    if (text != null) {
                        // Replace the generated text with some [much smaller] generic placeholder
                        JsonObjectBuilder textBuilder = jsonBuilderFactory.createObjectBuilder();
                        textBuilder.add("status", "empty");
                        textBuilder.add("div", "<div xmlns=\"http://www.w3.org/1999/xhtml\">Redacted for size</div>");
                        jsonObjectBuilder.add("text", textBuilder);
                    }

                    jsonObject = jsonObjectBuilder.build();
                }
                try (FileWriter writer = new FileWriter(file)) {
                    JsonWriter jsonWriter = jsonWriterFactory.createWriter(writer);
                    jsonWriter.write(jsonObject);
                }
            }
        }
    }
}
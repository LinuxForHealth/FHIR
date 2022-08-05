/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.core.r4b.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
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
 * This class fixes two issues with the packaged US Core artifacts:
 * <ol>
 * <li>The provided structure definitions contain invalid XHTML</li>
 * <li>The provided structure definitions do not contain a version element</li>
 * </ol>
 */
public class ResourceProcessor {
    public static void main(String[] args) throws Exception {
        Map<String, Object> writerConfig = Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        JsonReaderFactory jsonReaderFactory = Json.createReaderFactory(null);
        JsonWriterFactory jsonWriterFactory = Json.createWriterFactory(writerConfig);
        JsonBuilderFactory jsonBuilderFactory = Json.createBuilderFactory(null);

        File dir = new File("src/main/resources/hl7/fhir/core/410/package/");
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

                if (!jsonObject.containsKey("version")) {
                    System.out.println("file: " + file + " does not have a version");
                    jsonObjectBuilder.add("version", "4.3.0");
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
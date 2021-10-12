/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core.tool.v400;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;

/**
 * This class fixes two issues with the packaged US Core artifacts:
 * <ol>
 * <li>The provided structure definitions contain invalid XHTML</li>
 * <li>The provided structure definitions do not contain a version element</li>
 * </ol>
 */
public class ResourceProcessor {
    public static void main(String[] args) throws Exception {
        JsonReaderFactory jsonReaderFactory = Json.createReaderFactory(null);
        JsonWriterFactory jsonWriterFactory = Json.createWriterFactory(null);
        JsonBuilderFactory jsonBuilderFactory = Json.createBuilderFactory(null);

        File dir = new File("src/main/resources/hl7/fhir/us/core/400/package/");
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
                    JsonObjectBuilder textBuilder = jsonBuilderFactory.createObjectBuilder(text);
                    String div = text.getString("div");
                    div = div.replace("<p><pre>", "<pre>").replace("</pre></p>", "</pre>");
                    textBuilder.add("div", div);
                    jsonObjectBuilder.add("text", textBuilder);
                }

                if (!jsonObject.containsKey("version")) {
                    System.out.println("file: " + file + " does not have a version");
                    jsonObjectBuilder.add("version", "0.1.0");
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
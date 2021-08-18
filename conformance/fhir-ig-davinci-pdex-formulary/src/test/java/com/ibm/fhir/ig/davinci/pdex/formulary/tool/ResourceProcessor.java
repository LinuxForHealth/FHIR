/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.davinci.pdex.formulary.tool;

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
 * This class fixes the following issues with the packaged DaVinci PDEX Formulary artifacts:
 * <ol>
 * <li>The provided CapabilityStatement contains invalid XHTML</li>
 * </ol>
 */
public class ResourceProcessor {
    public static void main(String[] args) throws Exception {
        JsonReaderFactory jsonReaderFactory = Json.createReaderFactory(null);
        JsonWriterFactory jsonWriterFactory = Json.createWriterFactory(null);
        JsonBuilderFactory jsonBuilderFactory = Json.createBuilderFactory(null);

        File dir = new File("src/main/resources/hl7/fhir/us/davinci-pdex-formulary/package/");
        for (File file : dir.listFiles()) {
            String fileName = file.getName();
            if (!fileName.endsWith(".json") || file.isDirectory()) {
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
                    div = div.replace("</li><br/>", "</li>");
                    textBuilder.add("div", div);
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
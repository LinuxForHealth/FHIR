/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.tool;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;

/**
 * Walks through the FHIR expansions.json download and removes the following :
 * 1. expansions that have the valueset-toocostly extension.
 * 2. expansions that have the valueset-unclosed extension.
 * 3. 
 */
public class ValueSetExpansionCleaner {
    static final Path path = Paths.get("definitions/R4B/expansions.json");

    public static void main(String[] args) throws IOException {
        Map<String, Object> writerConfig = Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory jsonWriterFactory = Json.createWriterFactory(writerConfig);
        JsonReaderFactory jsonReaderFactory = Json.createReaderFactory(null);
        JsonBuilderFactory jsonBuilderFactory = Json.createBuilderFactory(null);

        JsonObject expansions;

        try (Reader reader = Files.newBufferedReader(path)) {
            JsonReader parser = jsonReaderFactory.createReader(reader);
            JsonObject parsedExpansions = parser.readObject();

            JsonObjectBuilder filteredExpansionsBuilder = jsonBuilderFactory.createObjectBuilder(parsedExpansions);
            JsonArrayBuilder newEntryList = Json.createArrayBuilder();

            JsonArray jsonArray = parsedExpansions.getJsonArray("entry");
            for (JsonValue jsonValue : jsonArray) {
                System.out.println("iterating..");
                JsonObject entry = jsonValue.asJsonObject();
                JsonObjectBuilder entryCopy = jsonBuilderFactory.createObjectBuilder(entry);

                JsonObject resource = jsonValue.asJsonObject().getJsonObject("resource");
                if (resource == null) {
                    System.out.println("null resource");
                }
                JsonObjectBuilder resourceCopy = jsonBuilderFactory.createObjectBuilder(resource);

                JsonObject expansion = resource.getJsonObject("expansion");
                if (expansion == null) {
                    System.out.println("null expansion");
                }
                if (expansion != null) {
                    JsonArray extensions = expansion.getJsonArray("extension");
                    JsonArray parameters = expansion.getJsonArray("parameter");
                    if (extensions != null) {
                        for (JsonValue extension : extensions) {
                            if ("http://hl7.org/fhir/StructureDefinition/valueset-toocostly".equals(extension.asJsonObject().getString("url"))) {
                                resourceCopy.remove("expansion");
                            }
                            if ("http://hl7.org/fhir/StructureDefinition/valueset-unclosed".equals(extension.asJsonObject().getString("url"))) {
                                resourceCopy.remove("expansion");
                            }
                        }
                    }
                    if (parameters != null) {
                        for (JsonValue parameter : parameters) {
                            if ("limitedExpansion".equals(parameter.asJsonObject().getString("name"))) {
                                resourceCopy.remove("expansion");
                            }
                        }
                    }
                }
                entryCopy.add("resource", resourceCopy);
                newEntryList.add(entryCopy); 
                
                
            }
            filteredExpansionsBuilder.add("entry", newEntryList);
            expansions = filteredExpansionsBuilder.build();
        }

        try (Writer writer = Files.newBufferedWriter(path)) {
            JsonWriter generator = jsonWriterFactory.createWriter(writer);
            generator.writeObject(expansions);
        }
    }
}

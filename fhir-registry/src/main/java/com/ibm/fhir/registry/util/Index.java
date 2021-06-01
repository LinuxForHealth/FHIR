/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.util;

import static com.ibm.fhir.registry.util.FHIRRegistryUtil.isDefinitionalResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParser.Event;
import jakarta.json.stream.JsonParserFactory;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.StructureDefinition;

public class Index {
    private static final Logger log = Logger.getLogger(Index.class.getName());

    private static final JsonProvider PROVIDER = JsonProvider.provider();
    private static final JsonParserFactory PARSER_FACTORY = PROVIDER.createParserFactory(null);
    private static final JsonGeneratorFactory GENERATOR_FACTORY = PROVIDER.createGeneratorFactory(Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true));

    private int version = -1;
    private final List<Entry> entries = new ArrayList<>();

    public Index() { }

    public Index(int version) {
        if (version < 1) {
            throw new IllegalArgumentException("index version must be a positive integer");
        }
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public List<Entry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public void load(InputStream in) {
        load(new BufferedReader(new InputStreamReader(in,  StandardCharsets.UTF_8)));
    }

    public void load(Reader reader) {
        JsonParser parser = PARSER_FACTORY.createParser(reader);

        String keyName = null;
        while (parser.hasNext()) {
            Event event = parser.next();
            switch (event) {
            case KEY_NAME:
                keyName = parser.getString();
                break;
            case VALUE_NUMBER:
                if ("index-version".equals(keyName)) {
                    version = parser.getInt();
                }
            case START_ARRAY:
                if ("files".equals(keyName)) {
                    parseFiles(parser);
                }
                break;
            default:
                break;
            }
        }

        parser.close();

        if (version < 1) {
            throw new IllegalStateException("index version was not set");
        }
    }

    private void parseFiles(JsonParser parser) {
        int i = 0;
        while (parser.hasNext()) {
            Event event = parser.next();
            switch (event) {
            case START_OBJECT:
                try {
                    parseFile(parser);
                } catch (NullPointerException e) {
                    log.log(Level.WARNING, "Skipping index entry " + i + " due to " +
                        "one or more missing required fields, beginning with: " + e.getMessage());
                }
                i++;
                break;
            case END_ARRAY:
                return;
            default:
                break;
            }
        }
    }

    private void parseFile(JsonParser parser) {
        String fileName = null;
        String resourceType = null;
        String id = null;
        String url = null;
        String version = null;
        String kind = null;
        String type = null;

        String keyName = null;
        while (parser.hasNext()) {
            Event event = parser.next();
            switch (event) {
            case KEY_NAME:
                keyName = parser.getString();
                break;
            case VALUE_STRING:
                switch (keyName) {
                case "filename":
                    fileName = parser.getString();
                    break;
                case "resourceType":
                    resourceType = parser.getString();
                    break;
                case "id":
                    id = parser.getString();
                    break;
                case "url":
                    url = parser.getString();
                    break;
                case "version":
                    version = parser.getString();
                    break;
                case "kind":
                    kind = parser.getString();
                    break;
                case "type":
                    type = parser.getString();
                    break;
                }
                break;
            case END_OBJECT:
                entries.add(new Entry(fileName, resourceType, id, url, version, kind, type));
                return;
            default:
                break;
            }
        }
    }

    public void store(OutputStream out) {
        store(new OutputStreamWriter(out, StandardCharsets.UTF_8));
    }

    public void store(Writer writer) {
        if (version < 1) {
            throw new IllegalStateException("index version was not set");
        }
        if (entries.isEmpty()) {
            throw new IllegalStateException("index contains no entries");
        }
        Collections.sort(entries);
        JsonGenerator generator = GENERATOR_FACTORY.createGenerator(writer);
        generator.writeStartObject();
        generator.write("index-version", version);
        generator.writeStartArray("files");
        for (Entry entry : entries) {
            write(generator, entry);
        }
        generator.writeEnd();
        generator.writeEnd();
        generator.flush();
        generator.close();
    }

    private void write(JsonGenerator generator, Entry entry) {
        generator.writeStartObject();
        generator.write("filename", entry.getFileName());
        generator.write("resourceType", entry.getResourceType());
        generator.write("id", entry.getId());
        generator.write("url", entry.getUrl());
        generator.write("version", entry.getVersion());
        if (entry.getKind() != null) {
            generator.write("kind", entry.getKind());
        }
        if (entry.getType() != null) {
            generator.write("type", entry.getType());
        }
        generator.writeEnd();
    }

    public boolean add(Entry entry) {
        if (entry == null) {
            return false;
        }
        return entries.add(entry);
    }

    public static class Entry implements Comparable<Entry> {
        private final String fileName;
        private final String resourceType;
        private final String id;
        private final String url;
        private final String version;
        private final String kind;
        private final String type;

        private Entry(
                String fileName,
                String resourceType,
                String id,
                String url,
                String version,
                String kind,
                String type) {
            this.fileName = Objects.requireNonNull(fileName, "fileName");
            this.resourceType = Objects.requireNonNull(resourceType, "resourceType");
            this.id = Objects.requireNonNull(id, "id");
            this.url = Objects.requireNonNull(url, "url");
            this.version = version;
            this.kind = kind;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public String getResourceType() {
            return resourceType;
        }

        public String getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }

        public String getVersion() {
            return version;
        }

        public String getKind() {
            return kind;
        }

        public String getType() {
            return type;
        }

        public static Entry entry(Resource resource) {
            Objects.requireNonNull(resource);

            if (!isDefinitionalResource(resource)) {
                return null;
            }

            String id = resource.getId();
            if (id == null) {
                return null;
            }

            String url = FHIRRegistryUtil.getUrl(resource);
            String version = FHIRRegistryUtil.getVersion(resource);
            if (url == null) {
                return null;
            }

            int idx = url.indexOf("|");
            if (idx != -1) {
                version = url.substring(idx + 1);
                url = url.substring(0, idx);
            }

            String resourceType = resource.getClass().getSimpleName();
            String fileName = resourceType + "-" + id + ".json";
            String kind = null;
            String type = null;

            if (resource instanceof StructureDefinition) {
                StructureDefinition structureDefinition = (StructureDefinition) resource;
                kind = structureDefinition.getKind().getValue();
                type = structureDefinition.getType().getValue();
            } else if (resource instanceof SearchParameter) {
                SearchParameter searchParameter = (SearchParameter) resource;
                type = searchParameter.getType().getValue();
            }

            return new Entry(fileName, resourceType, id, url, version, kind, type);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Entry other = (Entry) obj;
            return Objects.equals(fileName, other.fileName) &&
                    Objects.equals(resourceType, other.resourceType) &&
                    Objects.equals(id, other.id) &&
                    Objects.equals(url, other.url) &&
                    Objects.equals(version, other.version) &&
                    Objects.equals(kind, other.kind) &&
                    Objects.equals(type, other.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                fileName,
                resourceType,
                id,
                url,
                version,
                kind,
                type);
        }

        @Override
        public int compareTo(Entry other) {
            return this.fileName.compareTo(other.fileName);
        }
    }
}

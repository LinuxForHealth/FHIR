/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core.tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Resource;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatch;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

/**
 *
 */
public class VersionComparatorTool {
    private static final List<String> SKIP =
            Arrays.asList("/version", "/publisher", "/copyright", "/contact/0/name", "/contact/0/telecom/0/system", "/contact/0/telecom/0/value", "/date", "/copyright", "/contact",
                "/text/status", "/text/div", "/contact/0/telecom/1/system", "/contact/0/telecom/1/value", "/contact/0/telecom/0", "/jurisdiction/0/coding/0/display", "/description",
                "/identifier", "/name", "/title", "/contact/1/telecom/0/system", "/contact/1/telecom/0/value", "/contact/1/name", "/contact/0", "/xpath", "/extension", "/_multipleOr/extension/0/valueCode");

    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    // This is an internal model and does not need to honor _pretty printing as it is only communicating with the java
    // batch framework.
    private static final Map<java.lang.String, Object> properties =
            Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
    private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY =
            Json.createGeneratorFactory(properties);

    /**
     * IG's specify an index, and we need to difference the files in the index.
     * This facilitates the serialization of the index, and putting into a form which is usable.
     */
    public static class Index {

        private int indexVersion = 0;
        private List<Entry> files = new ArrayList<>();

        public int getIndexVersion() {
            return indexVersion;
        }

        public void setIndexVersion(int indexVersion) {
            this.indexVersion = indexVersion;
        }

        public List<Entry> getFiles() {
            return files;
        }

        public void addEntry(Entry entry) {
            this.files.add(entry);
        }

        public void addEntries(List<Entry> entries) {
            this.files.addAll(entries);
        }

        /**
         * Index.Entry
         */
        public static class Entry {

            private String filename;
            private String resourceType;
            private String id;
            private String url;
            private String version;
            private String kind;
            private String type;

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getResourceType() {
                return resourceType;
            }

            public void setResourceType(String resourceType) {
                this.resourceType = resourceType;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getKind() {
                return kind;
            }

            public void setKind(String kind) {
                this.kind = kind;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            /**
             * Simple builder for Index.Entry
             */
            public static class Builder {

                private String filename;
                private String resourceType;
                private String id;
                private String url;
                private String version;
                private String kind;
                private String type;

                public Builder filename(String filename) {
                    this.filename = filename;
                    return this;
                }

                public Builder resourceType(String resourceType) {
                    this.resourceType = resourceType;
                    return this;
                }

                public Builder id(String id) {
                    this.id = id;
                    return this;
                }

                public Builder url(String url) {
                    this.url = url;
                    return this;
                }

                public Builder version(String version) {
                    this.version = version;
                    return this;
                }

                public Builder kind(String kind) {
                    this.kind = kind;
                    return this;
                }

                public Builder type(String type) {
                    this.type = type;
                    return this;
                }

                public Entry build() {
                    Entry entry = new Entry();
                    entry.setFilename(filename);
                    entry.setId(id);
                    entry.setKind(kind);
                    entry.setResourceType(resourceType);
                    entry.setType(type);
                    entry.setUrl(url);
                    entry.setVersion(version);
                    return entry;
                }
            }

            /**
             * Parses an Entry in the Index into an Index.Entry object.
             */
            public static class Parser {

                private Parser() {
                    // No Imp
                }

                public static Entry parse(JsonObject jsonObject) throws FHIRException {
                    Entry.Builder builder = Index.builder();
                    if (jsonObject.containsKey("filename")) {
                        String val = jsonObject.getString("filename");
                        builder.filename(val);
                    }
                    if (jsonObject.containsKey("resourceType")) {
                        String val = jsonObject.getString("resourceType");
                        builder.resourceType(val);
                    }
                    if (jsonObject.containsKey("id")) {
                        String val = jsonObject.getString("id");
                        builder.id(val);
                    }
                    if (jsonObject.containsKey("url")) {
                        String val = jsonObject.getString("url");
                        builder.url(val);
                    }
                    if (jsonObject.containsKey("version")) {
                        String val = jsonObject.getString("version");
                        builder.version(val);
                    }
                    if (jsonObject.containsKey("type")) {
                        String val = jsonObject.getString("type");
                        builder.type(val);
                    }
                    if (jsonObject.containsKey("kind")) {
                        String val = jsonObject.getString("kind");
                        builder.kind(val);
                    }
                    return builder.build();
                }
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof Entry) {
                    Entry entry = (Entry) obj;
                    return entry.filename == this.getFilename();
                }
                return super.equals(obj);
            }
        }

        /**
         * Creates an Entry Builder
         *
         * @return
         */
        public static Entry.Builder builder() {
            return new Entry.Builder();
        }

        /**
         * Parses the Index JSON into a Java object which can be acted upon for analysis.
         */
        public static class Parser {

            private Parser() {
                // No Imp
            }

            public static Index parse(InputStream in) throws FHIRException {
                try (JsonReader jsonReader = JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                    JsonObject jsonObject = jsonReader.readObject();
                    Index.Builder builder = VersionComparatorTool.builder();

                    if (jsonObject.containsKey("index-version")) {
                        int indexVersion = jsonObject.getInt("index-version");
                        builder.version(indexVersion);
                    }

                    if (jsonObject.containsKey("files")) {
                        JsonArray array = jsonObject.getJsonArray("files");
                        Iterator<JsonValue> iter = array.iterator();
                        while (iter.hasNext()) {
                            JsonObject object = iter.next().asJsonObject();
                            Entry entry = Entry.Parser.parse(object);
                            builder.entry(entry);
                        }
                    }
                    return builder.build();
                } catch (Exception e) {
                    throw new FHIRException("Unexpected issues processing the index.json", e);
                }
            }
        }

        /**
         * Builder for Index Java class.
         */
        public static class Builder {

            private int indexVersion;
            private List<Entry> entries = new ArrayList<>();

            public Builder version(int version) {
                this.indexVersion = version;
                return this;
            }

            public Builder entry(Entry entry) {
                this.entries.add(entry);
                return this;
            }

            public Index build() {
                Index index = new Index();
                index.setIndexVersion(indexVersion);
                index.addEntries(entries);
                return index;
            }
        }
    }

    /**
     * Creates an Index.Builder.
     *
     * @return
     */
    public static Index.Builder builder() {
        return new Index.Builder();
    }

    public static Resource loadResource(String version, String resource) throws Exception {
        String filename = "src/main/resources/hl7/fhir/us/core/" + version + "/package/" + resource;
        final Resource r;
        try (InputStream in = Files.newInputStream(Paths.get(filename), StandardOpenOption.READ)) {
            r = FHIRParser.parser(Format.JSON).parse(in);
        }
        return r;
    }

    public static JsonObject loadResourceJson(String version, String resource) throws Exception {
        String filename = "src/main/resources/hl7/fhir/us/core/" + version + "/package/" + resource;
        final JsonObject jsonObject;
        try (InputStream in = Files.newInputStream(Paths.get(filename), StandardOpenOption.READ);
                JsonReader jsonReader = JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
            jsonObject = jsonReader.readObject();
        }
        return jsonObject;
    }

    public static String generate(JsonPatch diff) throws IOException {
        String o = "{}";
        try (StringWriter writer = new StringWriter();) {
            try (JsonGenerator generator = PRETTY_PRINTING_GENERATOR_FACTORY.createGenerator(writer);) {

            }
            o = writer.toString();
        }
        return o;
    }

    public static void main(String[] args) throws Exception {
        String filename = "src/main/resources/hl7/fhir/us/core/stu3/package/.index.json";
        final Index stu3;
        try (InputStream in = Files.newInputStream(Paths.get(filename), StandardOpenOption.READ)) {
            stu3 = Index.Parser.parse(in);
        }

        filename = "src/main/resources/hl7/fhir/us/core/stu4/package/.index.json";
        final Index stu4;
        try (InputStream in = Files.newInputStream(Paths.get(filename), StandardOpenOption.READ)) {
            stu4 = Index.Parser.parse(in);
        }

        List<String> stu3Files = stu3.getFiles().stream().map(m -> m.filename).sorted().collect(Collectors.toList());
        List<String> stu4Files = stu4.getFiles().stream().map(m -> m.filename).sorted().collect(Collectors.toList());

        // Compare Files:
        System.out.println("Files in STU3 but not STU4");
        stu3Files.stream().filter(file -> !stu4Files.contains(file)).forEach(System.out::println);
        System.out.println();

        System.out.println("Files in STU4 but not STU3");
        stu4Files.stream().filter(file -> !stu3Files.contains(file)).forEach(System.out::println);
        System.out.println();

        stu4Files.stream().filter(file -> stu3Files.contains(file)).forEach(resource -> {
            try {
                JsonObject stu3json = loadResourceJson("stu3", resource);
                JsonObject stu4json = loadResourceJson("stu4", resource);
                JsonPatch diff = Json.createDiff(stu3json, stu4json);
                JsonArray arr = diff.toJsonArray();
                System.out.println("--- DIFFERENCES --- " + resource);
                Iterator<JsonValue> iter = arr.iterator();
                while (iter.hasNext()) {
                    JsonObject obj = iter.next().asJsonObject();

                    String path = obj.getString("path");
                    if (!SKIP.contains(path) && !path.endsWith("/documentation") && !path.startsWith("/differential")) {
                        System.out.println(obj);
                    }
                }
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error Parsing Resource " + resource);
            }
        });

        System.out.println();
        System.out.println("Constraint Analysis");
//        stu4Files.stream().filter(file -> stu3Files.contains(file)).forEach(resource -> {
//            System.out.println("------------------------------------------");
//            System.out.println(resource);
//            try {
//                Resource stu3R = loadResource("stu3", resource);
//                Resource stu4R = loadResource("stu4", resource);
//
//                if (stu3R instanceof StructureDefinition) {
//                    Map<String,Constraint> oldConstraints = new LinkedHashMap<>();
//                    if (stu3R instanceof StructureDefinition) {
//                        List<Constraint> constraints = null;
//                        FHIRRegistryResourceProvider provider = new USCoreSTU3ResourceProvider();
//                        for (FHIRRegistryResource registryResource : provider.getRegistryResources()) {
//                            if (StructureDefinition.class.equals(registryResource.getResourceType())) {
//                                String url = registryResource.getUrl();
//                                System.out.println(url);
//                                Class<?> type =
//                                        ModelSupport.isResourceType(registryResource.getType()) ? ModelSupport.getResourceType(registryResource.getType()) : Extension.class;
//                                for (Constraint constraint : ProfileSupport.getConstraints(url, type)) {
//                                    System.out.println("    " + constraint);
//                                    if (!Constraint.LOCATION_BASE.equals(constraint.location())) {
//                                        compile(constraint.location());
//                                    }
//                                    compile(constraint.expression());
//                                }
//                        }
////                        for (Constraint constraint : constraints) {
////                            String key = constraint.id();
////                            oldConstraints.put(key, constraint);
////                        }
//                    }
//
//                    Map<String,Constraint> newConstraints = new LinkedHashMap<>();
//                    if (stu4R instanceof StructureDefinition) {
//                        List<Constraint> constraints = null;
//                        for (Constraint constraint : constraints) {
//                            String key = constraint.id();
//                            oldConstraints.put(key, constraint);
//                        }
//                    }
//
//                    System.out.println();
//                    System.out.println("[REMOVED CONSTRAINTS]");
//                    oldConstraints.entrySet().stream().forEach( kv -> {
//                        if (!newConstraints.containsKey(kv.getKey())) {
//                            System.out.println("[R] " + kv.getKey());
//                        }
//                    });
//
//                    System.out.println();
//                    System.out.println("[CHANGED CONSTRAINTS]");
//                    oldConstraints.entrySet().stream().forEach( kv -> {
//                        if (newConstraints.containsKey(kv.getKey())) {
//                            Constraint c = newConstraints.get(kv.getKey());
//                            String expr = c.expression();
//                            if (expr.equals(kv.getValue().expression())) {
//                                System.out.println("[C] " + kv.getKey());
//                            }
//                        }
//                    });
//
//                    System.out.println();
//                    System.out.println("[NEW CONSTRAINTS]");
//                    newConstraints.entrySet().stream().forEach( kv -> {
//                        if (!oldConstraints.containsKey(kv.getKey())) {
//                            System.out.println("[A] " + kv.getKey());
//                        }
//                    });
//
//                    System.out.println();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("Error Parsing Resource " + resource);
//            }
//        });
    }

}
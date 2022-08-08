/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.audit.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import org.linuxforhealth.fhir.exception.FHIRException;

/**
 * This class defines the Data section of the FHIR server AuditLogEntry.
 */
public class Data {

    private String resourceType;

    private String id;

    private String versionId;

    public Data() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * Generates JSON from this object.
     */
    public static class Writer {
        private static final Map<java.lang.String, Object> properties =
                Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY =
                Json.createGeneratorFactory(properties);

        private Writer() {
            // No Operation
        }

        /**
         * @param obj
         * @return
         * @throws IOException
         */
        public static String generate(Data obj)
                throws IOException {
            String o = "{}";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator =
                        PRETTY_PRINTING_GENERATOR_FACTORY.createGenerator(writer);) {
                    generator.writeStartObject();
                    generate(obj, generator);
                    generator.writeEnd();
                }
                o = writer.toString();
            }
            return o;
        }

        public static void generate(Data obj, JsonGenerator generator)
                throws IOException {
            if (obj.getResourceType() != null) {
                generator.write("resource_type", obj.getResourceType());
            }

            if (obj.getId() != null) {
                generator.write("id", obj.getId());
            }

            if (obj.getVersionId() != null) {
                generator.write("version_id", obj.getVersionId());
            }
        }
    }

    /**
     * Parser
     */
    public static class Parser {
        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        private Parser() {
            // No Impl
        }

        public static Data parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the Data", e);
            }
        }

        public static Data parse(JsonObject jsonObject) {
            Data.Builder builder =
                    Data.builder();

            JsonValue t = jsonObject.get("resource_type");
            if (t != null) {
                String resourceType = jsonObject.getString("resource_type");
                builder.resourceType(resourceType);
            }

            t = jsonObject.get("id");
            if (t != null) {
                String id = jsonObject.getString("id");
                builder.id(id);
            }

            t = jsonObject.get("version_id");
            if (t != null) {
                String versionId = jsonObject.getString("version_id");
                builder.versionId(versionId);
            }

            return builder.build();
        }
    }

    /**
     * Builder is a convenience pattern to assemble to Java Object
     */
    public static class Builder {
        private Data data = new Data();

        private Builder() {
            // Intentionally hiding from external callers.
        }

        public Builder id(String id) {
            data.setId(id);
            return this;
        }

        public Builder versionId(String versionId) {
            data.setVersionId(versionId);
            return this;
        }

        public Builder resourceType(String resourceType) {
            data.setResourceType(resourceType);
            return this;
        }

        public Data build() throws IllegalStateException {
            return data;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
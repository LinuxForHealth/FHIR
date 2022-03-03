/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import com.ibm.fhir.exception.FHIRException;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

/**
 * This class defines the Batch parameters section of the IBM FHIR server
 * AuditLogEntry.
 */
public class Batch {

    private String status;

    private Long resourcesRead = 0L;

    private Long resourcesCreated = 0L;

    private Long resourcesUpdated = 0L;

    private Long resourcesDeleted = 0L;

    private Long resourcesExecuted = 0L;

    public Batch() {
        super();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getResourcesRead() {
        return resourcesRead;
    }

    public void setResourcesRead(Long recsRead) {
        this.resourcesRead = recsRead;
    }

    public Long getResourcesCreated() {
        return resourcesCreated;
    }

    public void setResourcesCreated(Long recsCreated) {
        this.resourcesCreated = recsCreated;
    }

    public Long getResourcesUpdated() {
        return resourcesUpdated;
    }

    public void setResourcesUpdated(Long resourcesUpdated) {
        this.resourcesUpdated = resourcesUpdated;
    }

    public Long getResourcesDeleted() {
        return resourcesDeleted;
    }

    public void setResourcesDeleted(Long resourcesDeleted) {
        this.resourcesDeleted = resourcesDeleted;
    }

    public Long getResourcesExecuted() {
        return resourcesExecuted;
    }

    public void setResourcesExecuted(Long resourcesExecuted) {
        this.resourcesExecuted = resourcesExecuted;
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
        public static String generate(Batch obj)
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

        public static void generate(Batch obj, JsonGenerator generator)
                throws IOException {
            if (obj.getResourcesRead() != null) {
                generator.write("resources_read", obj.getResourcesRead());
            }

            if (obj.getResourcesCreated() != null) {
                generator.write("resources_created", obj.getResourcesCreated());
            }

            if (obj.getResourcesUpdated() != null) {
                generator.write("resources_updated", obj.getResourcesUpdated());
            }

            if (obj.getResourcesDeleted() != null) {
                generator.write("resources_deleted", obj.getResourcesDeleted());
            }

            if (obj.getResourcesExecuted() != null) {
                generator.write("resources_executed", obj.getResourcesExecuted());
            }

            if (obj.getStatus() != null) {
                generator.write("status", obj.getStatus());
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

        public static Batch parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the Batch", e);
            }
        }

        public static Batch parse(JsonObject jsonObject) {
            Batch.Builder builder =
                    Batch.builder();

            JsonValue t = jsonObject.get("resources_read");
            if (t != null) {
                long resourcesRead = jsonObject.getInt("resources_read");
                builder.resourcesRead(resourcesRead);
            }

            t = jsonObject.get("resources_created");
            if (t != null) {
                long resourcesCreated = jsonObject.getInt("resources_created");
                builder.resourcesCreated(resourcesCreated);
            }

            t = jsonObject.get("resources_updated");
            if (t != null) {
                long resourcesUpdated = jsonObject.getInt("resources_updated");
                builder.resourcesUpdated(resourcesUpdated);
            }

            t = jsonObject.get("resources_deleted");
            if (t != null) {
                long resourcesDeleted = jsonObject.getInt("resources_deleted");
                builder.resourcesDeleted(resourcesDeleted);
            }

            t = jsonObject.get("resources_executed");
            if (t != null) {
                long resourcesExecuted = jsonObject.getInt("resources_executed");
                builder.resourcesExecuted(resourcesExecuted);
            }

            t = jsonObject.get("status");
            if (t != null) {
                String status = jsonObject.getString("status");
                builder.status(status);
            }

            return builder.build();
        }
    }

    /**
     * Builder is a convenience pattern to assemble to Java Object
     */
    public static class Builder {
        private Batch batch = new Batch();

        private Builder() {
            // Intentionally hiding from external callers.
        }

        public Builder status(String status) {
            batch.setStatus(status);
            return this;
        }

        public Builder resourcesRead(long resourcesRead) {
            batch.setResourcesRead(resourcesRead);
            return this;
        }

        public Builder resourcesCreated(long resourcesCreated) {
            batch.setResourcesCreated(resourcesCreated);
            return this;
        }

        public Builder resourcesUpdated(long resourcesUpdated) {
            batch.setResourcesUpdated(resourcesUpdated);
            return this;
        }

        public Builder resourcesDeleted(long resourcesDeleted) {
            batch.setResourcesDeleted(resourcesDeleted);
            return this;
        }

        public Builder resourcesExecuted(long resourcesExecuted) {
            batch.setResourcesExecuted(resourcesExecuted);
            return this;
        }

        public Batch build() {
            return batch;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
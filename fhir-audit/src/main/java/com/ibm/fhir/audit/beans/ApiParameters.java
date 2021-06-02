/*
 * (C) Copyright IBM Corp. 2016, 2021
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

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import com.ibm.fhir.exception.FHIRException;

/**
 * This class defines the ApiParameters section of the FHIR server
 * AuditLogEntry.
 */
public class ApiParameters {

    private String request;

    private Integer status;

    public ApiParameters() {
        super();
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        public static String generate(ApiParameters obj)
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

        public static void generate(ApiParameters obj, JsonGenerator generator)
                throws IOException {
            if (obj.getRequest() != null) {
                generator.write("request", obj.getRequest());
            }

            if (obj.getStatus() != null) {
                generator.write("request_status", obj.getStatus());
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

        public static ApiParameters parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the ApiParameters", e);
            }
        }

        public static ApiParameters parse(JsonObject jsonObject) {
            ApiParameters.Builder builder =
                    ApiParameters.builder();

            JsonValue t = jsonObject.get("request");
            if (t != null) {
                String request = jsonObject.getString("request");
                builder.request(request);
            }

            t = jsonObject.get("request_status");
            if (t != null) {
                int status = jsonObject.getInt("request_status", -100000000);
                if (status != -100000000) {
                    builder.status(status);
                }
            }
            return builder.build();
        }
    }

    /**
     * Builder is a convenience pattern to assemble to Java Object
     */
    public static class Builder {
        private ApiParameters parameters = new ApiParameters();

        private Builder() {
            // Intentionally hiding from external callers.
        }

        public Builder status(int status) {
            parameters.setStatus(status);
            return this;
        }

        public Builder request(String request) {
            parameters.setRequest(request);
            return this;
        }

        public ApiParameters build() {
            return parameters;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
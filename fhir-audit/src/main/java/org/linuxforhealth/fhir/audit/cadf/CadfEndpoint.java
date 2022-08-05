/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.audit.cadf;

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
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import org.linuxforhealth.fhir.exception.FHIRException;

/**
 * Representation of the CADF Endpoint object. The Endpoint type is used to
 * provide information about a resource's location on a network
 */
public final class CadfEndpoint {
    private final String url;
    private final String name;
    private final String port;

    /**
     * Construct a CADF Endpoint object
     *
     * @param url  -- String. Required. The network address of the endpoint; for
     *             IP-based addresses. Note: The IP address value may include the
     *             port number as part of the syntax as an alternative to separating
     *             it out into the optional port attribute
     * @param name - String. Optional. Provides a logical name for the object.
     * @param port - String. Optional. An optional property to provide the port
     *             value separate from the address property, intended to facilitate
     *             a consistent means to query resource information on a specific
     *             port.
     */
    public CadfEndpoint(String url, String name, String port) {
        this.url  = url;
        this.name = name;
        this.port = port;
    }

    public String getUrl() {
        return this.url;
    }

    public String getName() {
        return this.name;
    }

    public String getPort() {
        return this.port;
    }

    /**
     * Generates JSON from this object.
     */
    public static class Writer {

        private Writer() {
            // No Operation
        }

        private static final Map<java.lang.String, Object> properties =
                Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY =
                Json.createGeneratorFactory(properties);

        /**
         * @param obj
         * @return
         * @throws IOException
         */
        public static String generate(CadfEndpoint obj)
                throws IOException {
            String o = "{}";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator =
                        PRETTY_PRINTING_GENERATOR_FACTORY.createGenerator(writer);) {

                    generate(obj, generator);

                }
                o = writer.toString();
            }
            return o;
        }

        public static void generate(CadfEndpoint obj, JsonGenerator generator) throws IOException {
            generator.writeStartObject();

            if (obj.getUrl() != null) {
                generator.write("url", obj.getUrl());
            }

            if (obj.getName() != null) {
                generator.write("name", obj.getName());
            }

            if (obj.getPort() != null) {
                generator.write("port", obj.getPort());
            }

            generator.writeEnd();
        }
    }

    /**
     * Parser
     */
    public static class Parser {

        private Parser() {
            // No Impl
        }

        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        public static CadfEndpoint parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the CadfEndpoint", e);
            }
        }

        public static CadfEndpoint parse(JsonObject jsonObject)
                throws FHIRException {

            CadfEndpoint.Builder builder =
                    CadfEndpoint.builder();

            String url = jsonObject.getString("url");
            if (url != null) {
                builder.url(url);
            }

            String name = jsonObject.getString("name");
            if (name != null) {
                builder.name(name);
            }

            String port = jsonObject.getString("port");
            if (port != null) {
                builder.port(port);
            }

            return builder.build();

        }

    }

    /**
     * Builder is a convenience pattern to assemble to Java Object
     */
    public static class Builder {
        private String url = null;
        private String name = null;
        private String port = null;

        private Builder() {
            // Intentionally hiding from external callers.
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder port(String port) {
            this.port = port;
            return this;
        }

        public CadfEndpoint build() {
            return new CadfEndpoint(url, name, port);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
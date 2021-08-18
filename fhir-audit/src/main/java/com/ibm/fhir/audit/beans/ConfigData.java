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
 * This class defines the Configuration Data section of the FHIR server
 * AuditLogEntry.
 */
public class ConfigData {

    private String serverStartupParms;

    public String getServerStartupParms() {
        return serverStartupParms;
    }

    public void setServerStartupParms(String serverStartupParms) {
        this.serverStartupParms = serverStartupParms;
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
        public static String generate(ConfigData obj)
                throws IOException {
            String o = "{}";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator =
                        PRETTY_PRINTING_GENERATOR_FACTORY.createGenerator(writer);) {
                    generator.writeStartObject();

                    if (obj.getServerStartupParms() != null) {
                        generator.write("server_startup_parameters", obj.getServerStartupParms());
                    }

                    generator.writeEnd();
                }
                o = writer.toString();
            }
            return o;
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

        public static ConfigData parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                ConfigData.Builder builder =
                        ConfigData.builder();

                JsonValue v = jsonObject.get("server_startup_parameters");
                if (v != null) {
                    String serverStartupParameters = jsonObject.getString("server_startup_parameters");
                    builder.serverStartupParameters(serverStartupParameters);
                }

                return builder.build();
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the ConfigData", e);
            }
        }
    }

    /**
     * Builder is a convenience pattern to assemble to Java Object
     */
    public static class Builder {
        private ConfigData configData = new ConfigData();

        private Builder() {
            // Intentionally hiding from external callers.
        }

        public Builder serverStartupParameters(String serverStartupParameters) {
            configData.setServerStartupParms(serverStartupParameters);
            return this;
        }

        public ConfigData build() {
            return configData;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

import com.ibm.fhir.exception.FHIRException;

/**
 * Representation of the CADF Map Item object.
 */
public final class CadfMapItem {
    private final String key;
    private final Object value;

    public CadfMapItem(String key, Object value) {
        this.key   = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
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
        public static String generate(CadfMapItem obj)
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

        public static void generate(CadfMapItem obj, JsonGenerator generator)
                throws IOException {
            generator.writeStartObject();

            if (obj.getKey() != null) {
                generator.write("key", obj.getKey());
            }

            if (obj.getValue() != null) {
                Object tmpObj = obj.getValue();
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream objStream = new ObjectOutputStream(baos);) {
                    objStream.writeObject(tmpObj);
                    generator.write("value",
                            java.util.Base64.getEncoder().encodeToString(baos.toByteArray()));
                }
            }

            generator.writeEnd();
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

        public static CadfMapItem parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                return parse(jsonReader.readObject());
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the CadfMapItem", e);
            }
        }

        public static CadfMapItem parse(JsonObject jsonObject) throws FHIRException {
            CadfMapItem.Builder builder =
                    CadfMapItem.builder();

            String key = jsonObject.getString("key");
            if (key != null) {
                builder.key(key);
            }

            // Get the value and switch over to Object
            String value = jsonObject.getString("value");
            if (value != null) {
                try {
                    byte[] values = java.util.Base64.getDecoder().decode(value);
                    try (ByteArrayInputStream bis = new ByteArrayInputStream(values);
                            ObjectInput input = new ObjectInputStream(bis);) {
                        Object tmpValue = input.readObject();
                        builder.value(tmpValue);
                    }
                } catch (Exception e) {
                    throw new FHIRException("Class not found", e);
                }
            }

            return builder.build();
        }
    }

    /**
     * Builder is a convenience pattern to assemble to Java Object
     */
    public static class Builder {
        private String key = null;
        private Object value = null;

        private Builder() {
            // Intentionally hiding from external callers.
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder value(Object value) {
            this.value = value;
            return this;
        }

        public CadfMapItem build() {
            return new CadfMapItem(key, value);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
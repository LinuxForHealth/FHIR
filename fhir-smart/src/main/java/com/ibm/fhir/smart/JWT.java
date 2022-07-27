/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.smart;

import java.io.StringReader;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.JsonValue.ValueType;

/**
 * A minimal alternative to the com.auth0:java-jwt library.
 * This flavor mimics the java-jwt API but implements only the [very small] subset of functionality that is
 * actually needed by the fhir-smart policy enforcement.
 */
public class JWT {
    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);
    private static final Decoder decoder = Base64.getDecoder();

    public static DecodedJWT decode(String jwt) {
        String[] parts = jwt.split("\\.");
        if (parts.length < 2 || parts.length > 3 || (parts.length == 2 && !jwt.endsWith("."))) {
            throw new IllegalArgumentException("Invalid JWT: expected 3 parts but found " + parts.length);
        }

        return new DecodedJWT(
            new String(decoder.decode(parts[0])),
            new String(decoder.decode(parts[1])),
            parts.length == 2 ? null : parts[2]);
    }

    public static class DecodedJWT {
        final JsonObject header;
        final JsonObject data;
        final String signature;

        private DecodedJWT(String header, String data, String signature) {
            this.header = JSON_READER_FACTORY.createReader(new StringReader(header)).readObject();
            this.data = JSON_READER_FACTORY.createReader(new StringReader(data)).readObject();
            this.signature = signature;
        }

        public Claim getClaim(String name) {
            if (data.containsKey(name) && data.get(name) == null) {
                return new Claim(JsonValue.NULL);
            }
            return new Claim(data.get(name));
        }
    }

    public static class Claim {
        final JsonValue value;

        private Claim(JsonValue value) {
            this.value = value;
        }

        /**
         * Get this Claim as a String. If the value isn't of type String or it can't be converted to a String, null will be returned.
         */
        public String asString() {
            if (value == null) return null;

            switch (value.getValueType()) {
            case STRING:
                return ((JsonString) value).getString();
            case TRUE:
            case FALSE:
            case NUMBER:
                return value.toString();
            case NULL:
            case OBJECT:
            case ARRAY:
            default:
                return null;
            }
        }

        /**
         * Get this Claim as a List of Strings. If the value isn't an Array, null will be returned.
         */
        public List<String> asList() {
            if (value == null || !(value instanceof JsonArray)) {
                return null;
            }
            List<String> list = value.asJsonArray().getValuesAs(JsonString.class).stream()
                    .map(s -> s.getString())
                    .collect(Collectors.toList());
            return Collections.unmodifiableList(list);
        }

        /**
         * @return true if the claim exists and has a value of null; otherwise false
         */
        public boolean isNull() {
            return value != null && value.getValueType() == ValueType.NULL;
        }

        /**
         * @return true if the claim is missing entirely
         */
        public boolean isMissing() {
            return value == null;
        }
    }
}

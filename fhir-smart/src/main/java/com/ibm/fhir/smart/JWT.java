/*
 * (C) Copyright IBM Corp. 2021
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
import jakarta.json.JsonObject;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.JsonValue.ValueType;

/**
 * A minimal alternative to the com.auth0:java-jwt library.
 * This flavor mimics the java-jwt API but implement only the [very small] subset of functionality that is
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
            parts.length == 2 ? null : new String(decoder.decode(parts[2]).toString())
            );
    }

    public static class DecodedJWT {
        final JsonObject header;
        final JsonObject data;
        final JsonObject signature;

        private DecodedJWT(String header, String data, String signature) {
            this.header = JSON_READER_FACTORY.createReader(new StringReader(header)).readObject();
            this.data = JSON_READER_FACTORY.createReader(new StringReader(data)).readObject();
            this.signature = signature == null ? null :
                    JSON_READER_FACTORY.createReader(new StringReader(signature)).readObject();
        }

        public Claim getClaim(String name) {
            return new Claim(data.get(name));
        }
    }

    public static class Claim {
        final JsonValue value;

        private Claim(JsonValue value) {
            this.value = value;
        }

        public String asString() {
            if (value == null) return null;

            switch (value.getValueType()) {
            case STRING:
                return ((JsonString) value).getString();
            case TRUE:
            case FALSE:
            case NUMBER:
            case NULL:
            case OBJECT:
            case ARRAY:
            default:
                return value.toString();
            }
        }

        public List<String> asList() {
            List<String> list = value.asJsonArray().getValuesAs(JsonString.class).stream()
                    .map(s -> s.getString())
                    .collect(Collectors.toList());
            return Collections.unmodifiableList(list);
        }

        public boolean isNull() {
            if (value == null || value.getValueType() == ValueType.NULL) {
                return true;
            }
            return false;
        }
    }
}

/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import com.ibm.fhir.exception.FHIRException;

/**
 * Representation of the CADF Credential object
 */
public class CadfCredential {
    private final String type;
    private final String token;
    private final String authority;
    private final ArrayList<CadfMapItem> assertions;

    private CadfCredential(CadfCredential.Builder builder) {
        type       = builder.type;
        token      = builder.token;
        authority  = builder.authority;
        assertions = builder.assertions;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the authority
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * @return the assertions
     */
    public ArrayList<CadfMapItem> getAssertions() {
        return assertions;
    }

    public String getToken() {
        return token;
    }

    /**
     * Validate contents of the CadfCredential object.
     * The logic is determined by the CADF specification.
     *
     * @throws IllegalStateException when the event does not meet the specification.
     */
    private void validate() throws IllegalStateException {
        // The only required property is token
        if (this.token == null || this.token.length() == 0) {
            throw new IllegalStateException("token is required");
        }
        // if we are here, everything seems to be ok
    }

    public static class Builder {
        private String type;
        private String token;
        private String authority;
        private ArrayList<CadfMapItem> assertions;

        private Builder() {
            // No Operation
        }

        /**
         * Constructs a Builder instance based on the Credential token
         *
         * @param token -- String. The primary opaque or non-opaque identity or security
         *              token (e.g., an opaque or obfuscated user ID, opaque security
         *              token string, or security certificate).
         */
        public Builder(String token) {
            this.token = token;
        }

        /**
         * Set the optional credential type property.
         *
         * @param type -- String. Type of credential. (e.g., auth. token, identity
         *             token, etc.)
         * @return Builder instance
         */
        public Builder type(String type) {
            this.type = type;
            return this;
        }

        /**
         * Set the optional authority property.
         *
         * @param authority - String. The trusted authority (a service) that understands
         *                  and can verify the credential.
         * @return Builder instance
         */
        public Builder authority(String authority) {
            this.authority = authority;
            return this;
        }

        /**
         * Set the optional assertions property. This property contains a list of
         * additional opaque or non-opaque assertions or attributes that belong to the
         * credential. One example might be the certificate trust chain if the
         * credential is a certificate.
         *
         * @param assertions -- Array of CadfMapItem. Optional list of additional opaque
         *                   or non-opaque assertions or attributes that belong to the
         *                   credential.
         * @return Builder instance
         */
        public Builder assertions(CadfMapItem[] assertions) {
            this.assertions = new ArrayList<>(Arrays.asList(assertions));
            return this;
        }

        /**
         * Set the optional assertions property. This property contains a list of
         * additional opaque or non-opaque assertions or attributes that belong to the
         * credential. One example might be the certificate trust chain if the
         * credential is a certificate.
         *
         * @param assertions -- Array of CadfMapItem. Optional list of additional opaque
         *                   or non-opaque assertions or attributes that belong to the
         *                   credential.
         * @return Builder instance
         */
        public Builder assertions(ArrayList<CadfMapItem> assertions) {
            this.assertions = assertions;
            return this;
        }

        /**
         * Add an assertion to the assertion list, one at a time.
         *
         * @see #assertion(CadfMapItem)
         * @param assertion -- A single CadfMapItem.
         * @return Builder instance
         */
        public Builder assertion(CadfMapItem assertion) {
            if (this.assertions == null) {
                this.assertions = new ArrayList<>();
            }
            this.assertions.add(assertion);
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * Builds the CadfCredential object
         *
         * @return {@link CadfCredential}
         * @throws IllegalStateException when the event does not meet the specification.
         */
        public CadfCredential build() throws IllegalStateException {
            CadfCredential cred = new CadfCredential(this);
            cred.validate();
            return cred;
        }

    }

    public static Builder builder() {
        return new Builder();
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
        public static String generate(CadfCredential obj)
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

        public static void generate(CadfCredential obj, JsonGenerator generator)
                throws IOException {
            if (obj.getAuthority() != null) {
                generator.write("authority", obj.getAuthority());
            }

            if (obj.getToken() != null) {
                generator.write("token", obj.getToken());
            }

            if (obj.getType() != null) {
                generator.write("type", obj.getType());
            }

            //Annotations
            if (obj.getAssertions() != null) {
                generator.writeStartArray("assertions");
                for (CadfMapItem item : obj.getAssertions()) {
                    CadfMapItem.Writer.generate(item, generator);
                }
                generator.writeEnd();
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

        public static CadfCredential parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the CadfCredential", e);
            }
        }

        public static CadfCredential parse(JsonObject jsonObject)
                throws FHIRException, IOException {

            CadfCredential.Builder builder =
                    CadfCredential.builder();

            if (jsonObject.get("type") != null) {
                String type = jsonObject.getString("type");
                builder.type(type);
            }

            if (jsonObject.get("token") != null) {
                String token = jsonObject.getString("token");
                builder.token(token);
            }

            if (jsonObject.get("authority") != null) {
                String authority = jsonObject.getString("authority");
                builder.authority(authority);
            }

            if (jsonObject.get("assertions") != null) {
                JsonArray annotations = jsonObject.getJsonArray("assertions");
                for (int i = 0; i < annotations.size(); i++) {
                    JsonObject obj = (JsonObject) annotations.get(0);
                    CadfMapItem mapItem = CadfMapItem.Parser.parse(obj);
                    builder.assertion(mapItem);
                }
            }
            return builder.build();
        }
    }
}
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
 * Outcome reason. Provides additional information to describe the event outcome
 */
public final class CadfReason {
    private final String reasonType;
    private final String reasonCode;
    private final String policyType;
    private final String policyId;

    /**
     * Create a CADF Reason object
     *
     * @param reasonType - String. The reason code domain URI. Must be present if
     *                   reasonCode is present.
     * @param reasonCode - String. Detailed result code as described by the domain
     *                   identifier (reason type). Must be specified if policyId is
     *                   not specified.
     * @param policyType - String. The policy domain URI. Must be present if
     *                   policyId is present.
     * @param policyId   - String. An optional identifier that indicates which
     *                   policy or algorithm was applied in order to achieve the
     *                   described OUTCOME. Must be specified if reasonCode is not
     *                   specified.
     */
    public CadfReason(String reasonType, String reasonCode, String policyType, String policyId) {
        this.reasonType = reasonType;
        this.reasonCode = reasonCode;
        this.policyId   = policyId;
        this.policyType = policyType;
    }

    public String getReasonType() {
        return reasonType;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getPolicyType() {
        return policyType;
    }

    public String getPolicyId() {
        return policyId;
    }

    /**
     * Validate contents of the Reason type.
     * The logic is determined by the CADF specification.
     *
     * @return This object for chaining.
     * @throws IllegalStateException when the properties do not meet the
     *                               specification.
     */
    public CadfReason validate() throws IllegalStateException {
        if (policyId == null && reasonCode == null) {
            throw new IllegalStateException("at least one of: policyId, reasonCode must be specified");
        }
        if (reasonCode != null) {
            // it must not be empty, and reasonType must be specified and non-empty
            if (reasonCode.length() == 0) {
                throw new IllegalStateException("invalid reasonCode");
            }
            if (reasonType == null || reasonType.length() == 0) {
                throw new IllegalStateException("invalid reasonType");
            }
        }
        if (policyId != null) {
            // it must not be empty, and policyType must be specified and non-empty
            if (policyId.length() == 0) {
                throw new IllegalStateException("invalid policyId");
            }
            if (policyType == null || policyType.length() == 0) {
                throw new IllegalStateException("invalid policyType");
            }
        }
        return this;
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
        public static String generate(CadfReason obj)
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

        public static void generate(CadfReason obj, JsonGenerator generator)
                throws IOException {
            // If the CADF reason is null, then simply skip it.
            if (obj == null) {
                return;
            }
            if (obj.getReasonType() != null) {
                generator.write("reasonType", obj.getReasonType());
            }

            if (obj.getReasonCode() != null) {
                generator.write("reasonCode", obj.getReasonCode());
            }

            if (obj.getPolicyType() != null) {
                generator.write("policyType", obj.getPolicyType());
            }

            if (obj.getPolicyId() != null) {
                generator.write("policyId", obj.getPolicyId());
            }
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

        public static CadfReason parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the CadfReason", e);
            }
        }

        public static CadfReason parse(JsonObject jsonObject)
                throws FHIRException {
            CadfReason.Builder builder =
                    CadfReason.builder();

            if (jsonObject.get("reasonType") != null) {
                String reasonType = jsonObject.getString("reasonType");
                builder.reasonType(reasonType);
            }

            if (jsonObject.get("reasonCode") != null) {
                String reasonCode = jsonObject.getString("reasonCode");
                builder.reasonCode(reasonCode);
            }

            if (jsonObject.get("policyType") != null) {
                String policyType = jsonObject.getString("policyType");
                builder.policyType(policyType);
            }

            if (jsonObject.get("policyId") != null) {
                String policyId = jsonObject.getString("policyId");
                builder.policyId(policyId);
            }

            return builder.build();

        }
    }

    /**
     * Builder is a convenience pattern to assemble to Java Object
     */
    public static class Builder {
        private String policyId = null;
        private String policyType = null;
        private String reasonCode = null;
        private String reasonType = null;

        private Builder() {
            // Intentionally hiding from external callers.
        }

        public Builder policyId(String policyId) {
            this.policyId = policyId;
            return this;
        }

        public Builder policyType(String policyType) {
            this.policyType = policyType;
            return this;
        }

        public Builder reasonCode(String reasonCode) {
            this.reasonCode = reasonCode;
            return this;
        }

        public Builder reasonType(String reasonType) {
            this.reasonType = reasonType;
            return this;
        }

        public CadfReason build() {
            return new CadfReason(reasonType, reasonCode, policyType, policyId);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

}

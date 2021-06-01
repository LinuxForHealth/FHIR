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
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import com.ibm.fhir.audit.cadf.enums.ResourceType;
import com.ibm.fhir.exception.FHIRException;

/**
 * Implementation of the CADF Resource type.
 * Resource represents an actor in a CADF event: OBSERVER that reports the
 * event, INITIATOR that performs the action that generates the event, or TARGET
 * upon which the action is performed.
 */
public final class CadfResource {
    private final String id;
    private final ResourceType typeURI;
    private final String name;
    private final String domain;
    private final CadfCredential credential;
    private final ArrayList<CadfEndpoint> addresses;
    private final String host;
    private final String geolocationId;
    private final CadfGeolocation geolocation;
    private final ArrayList<CadfAttachment> attachments;

    private CadfResource(Builder builder) {
        this.id            = builder.id;
        this.typeURI       = builder.typeURI;
        this.name          = builder.name;
        this.domain        = builder.domain;
        this.credential    = builder.credential;
        this.addresses     = builder.addresses;
        this.host          = builder.host;
        this.geolocationId = builder.geolocationId;
        this.geolocation   = builder.geolocation;
        this.attachments   = builder.attachments;
    }

    /**
     * Return the resource ID. It can be used to reference the resource from other
     * events in the same log.
     *
     * @return Resource ID string
     */
    public String getId() {
        return this.id;
    }

    public CadfGeolocation getGeolocation() {
        return geolocation;
    }

    public ResourceType getTypeURI() {
        return typeURI;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @return the credential
     */
    public CadfCredential getCredential() {
        return credential;
    }

    /**
     * @return the addresses
     */
    public ArrayList<CadfEndpoint> getAddresses() {
        return addresses;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the attachments
     */
    public ArrayList<CadfAttachment> getAttachments() {
        return attachments;
    }

    public String getGeolocationId() {
        return geolocationId;
    }

    /**
     * Validate contents of the resource.
     * The logic is determined by the CADF specification. In short, ID, type, and
     * one of the geolocation properties are required
     *
     * @throws IllegalStateException when the properties do not meet the
     *                               specification.
     */
    private void validate() throws IllegalStateException {
        if (this.typeURI == null || this.id == null || this.id.isEmpty()) {
            throw new IllegalStateException("missing required properties");
        }
        if (this.geolocation == null && (this.geolocationId == null || this.geolocationId.isEmpty())) {
            throw new IllegalStateException("missing geolocation data");
        }
    }

    /**
     * Builder for immutable CadfResource objects
     */
    public static class Builder {
        private String id;
        private ResourceType typeURI;
        private String name;
        private String domain;
        private CadfCredential credential;
        private ArrayList<CadfEndpoint> addresses;
        private String host;
        private String geolocationId;
        private CadfGeolocation geolocation;
        private ArrayList<CadfAttachment> attachments;

        private Builder() {
            // No Operation
        }

        /**
         * Creates an instance of the CadfResource builder.
         *
         * @param id      - String. Resource identifier.
         * @param typeURI - CadfEvent.ResourceType. Resource classification in the CADF
         *                taxonomy.
         * @see ResourceType
         */
        public Builder(String id, ResourceType typeURI) {
            this.id      = id;
            this.typeURI = typeURI;
        }

        /**
         * Set the optional local name for the resource (not necessarily unique)
         *
         * @param name
         * @return Builder
         */
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * Set the optional local name for the resource (not necessarily unique)
         *
         * @param name
         * @return Builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Set the optional information about the (network) host of the resource
         *
         * @param host
         * @return Builder
         */
        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder typeURI(ResourceType typeURI) {
            this.typeURI = typeURI;
            return this;
        }

        /**
         * Set the optional name of the domain that qualifies the name of the resource
         * (e.g., a path name, a container name, etc.).
         *
         * @param domain
         * @return Builder
         */
        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }

        /**
         * Set the optional optional security credentials associated with the resource’s
         * identity.
         *
         * @see {@link CadfCredential}
         * @param cred
         * @return Builder
         */
        public Builder credential(CadfCredential cred) {
            this.credential = cred;
            return this;
        }

        /**
         * This optional property identifies a CADF Geolocation by reference and whose
         * definition exists outside the event record itself (e.g., within the same CADF
         * Log or Report level). Note: This property can be used instead of the
         * "geolocation" property to reference a valid CADF Geolocation definition,
         * which is already defined outside the resource itself, by its identifier
         * (e.g., a CADF Geolocation already defined at the CADF Log or Report level
         * that also contains the CADF Resource definition). This property is required
         * if the geolocation property is not used.
         *
         * @param geolocId
         * @return Builder
         */
        public Builder geolocationId(String geolocId) {
            this.geolocationId = geolocId;
            return this;
        }

        /**
         * Set the property describing the geographic location of the resource using a
         * CADF Geolocation data type. This property is required if the geolocationId
         * property is not used.
         *
         * @see {@link CadfGeolocation}
         * @param geoloc
         * @return Builder
         */
        public Builder geolocation(CadfGeolocation geoloc) {
            this.geolocation = geoloc;
            return this;
        }

        /**
         * An optional array of additional data containing information about the
         * reporter or any action it performed that affected the CADF Event Record
         * contents.
         */
        public Builder attachments(CadfAttachment[] attachments) {
            this.attachments = new ArrayList<>(Arrays.asList(attachments));
            return this;
        }

        /**
         * An optional array of additional data containing information about the
         * reporter or any action it performed that affected the CADF Event Record
         * contents.
         */
        public Builder attachments(ArrayList<CadfAttachment> attachments) {
            this.attachments = attachments;
            return this;
        }

        /**
         * A convenience method to add one attachment at a time.
         *
         * @see #attachments(CadfAttachment[])
         */
        public Builder attachment(CadfAttachment attachment) {
            if (this.attachments == null) {
                attachments = new ArrayList<>();
            }
            this.attachments.add(attachment);
            return this;
        }

        /**
         * An optional array of descriptive addresses (including URLs) of the resource
         */
        public Builder addresses(CadfEndpoint[] addresses) {
            this.addresses = new ArrayList<>(Arrays.asList(addresses));
            return this;
        }

        /**
         * An optional ArrayList of descriptive addresses (including URLs) of the
         * resource
         */
        public Builder addresses(ArrayList<CadfEndpoint> addresses) {
            this.addresses = addresses;
            return this;
        }

        /**
         * A convenience method to add one address at a time.
         *
         * @see #addresses(CadfEndpoint[])
         */
        public Builder address(CadfEndpoint address) {
            if (this.addresses == null) {
                addresses = new ArrayList<>();
            }
            this.addresses.add(address);
            return this;
        }

        /**
         * Build an immutable ReporterStep instance.
         *
         * @return ReporterStep
         * @throws IllegalStateException when the properties do not meet the
         *                               specification.
         */
        public CadfResource build() {
            CadfResource res = new CadfResource(this);
            res.validate();
            return res;
        }
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
        public static String generate(CadfResource obj)
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

        public static void generate(CadfResource obj, JsonGenerator generator) throws IOException {
            // If the CADF resource, e.g, observer, is null, then simply skip it.
            if (obj == null) {
                return;
            }
            if (obj.getId() != null) {
                generator.write("id", obj.getId());
            }

            if (obj.getTypeURI() != null) {
                generator.write("typeURI", obj.getTypeURI().toString());
            }

            if (obj.getName() != null) {
                generator.write("name", obj.getName());
            }

            if (obj.getDomain() != null) {
                generator.write("domain", obj.getDomain());
            }

            if (obj.getHost() != null) {
                generator.write("host", obj.getHost());
            }

            if (obj.getGeolocationId() != null) {
                generator.write("geolocationId", obj.getGeolocationId());
            }

            if (obj.getCredential() != null) {
                generator.writeStartObject("credential");
                CadfCredential.Writer.generate(obj.getCredential(), generator);
                generator.writeEnd();
            }

            if (obj.getAddresses() != null) {
                generator.writeStartArray("addresses");
                for (CadfEndpoint endpoint : obj.getAddresses()) {
                    CadfEndpoint.Writer.generate(endpoint, generator);
                }
                generator.writeEnd();
            }

            if (obj.getGeolocation() != null) {
                generator.writeStartObject("geolocation");
                CadfGeolocation.Writer.generate(obj.getGeolocation(), generator);
                generator.writeEnd();
            }

            if (obj.getAttachments() != null) {
                generator.writeStartArray("attachments");
                for (CadfAttachment attachment : obj.getAttachments()) {
                    CadfAttachment.Writer.generate(attachment, generator);
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

        public static CadfResource parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the CadfResource", e);
            }
        }

        public static CadfResource parse(JsonObject jsonObject)
                throws IOException, FHIRException {
            CadfResource.Builder builder = CadfResource.builder();

            if (jsonObject.get("id") != null) {
                String id = jsonObject.getString("id");
                builder.id(id);
            }

            if (jsonObject.get("name") != null) {
                String name = jsonObject.getString("name");
                builder.name(name);
            }

            if (jsonObject.get("domain") != null) {
                String domain = jsonObject.getString("domain");
                builder.domain(domain);
            }

            if (jsonObject.get("host") != null) {
                String host = jsonObject.getString("host");
                builder.host(host);
            }

            if (jsonObject.get("geolocationId") != null) {
                String geolocationId = jsonObject.getString("geolocationId");
                builder.geolocationId(geolocationId);
            }

            if (jsonObject.get("typeURI") != null) {
                String typeURI = jsonObject.getString("typeURI");
                ResourceType t = ResourceType.of(typeURI);
                builder.typeURI(t);
            }

            if (jsonObject.get("credential") != null) {
                JsonObject credential = jsonObject.getJsonObject("credential");
                CadfCredential c = CadfCredential.Parser.parse(credential);
                builder.credential(c);
            }

            if (jsonObject.get("geolocation") != null) {
                JsonObject geolocation = jsonObject.getJsonObject("geolocation");
                CadfGeolocation c = CadfGeolocation.Parser.parse(geolocation);
                builder.geolocation(c);
            }

            JsonValue t = jsonObject.get("addresses");
            if (t != null) {
                JsonArray addresses = jsonObject.getJsonArray("addresses");
                for (JsonValue v : addresses) {
                    CadfEndpoint c = CadfEndpoint.Parser.parse(v.asJsonObject());
                    builder.address(c);
                }
            }

            if (jsonObject.get("attachments") != null) {
                JsonArray attachments = jsonObject.getJsonArray("attachments");
                for (JsonValue v : attachments) {
                    CadfAttachment c = CadfAttachment.Parser.parse(v.asJsonObject());
                    builder.attachment(c);
                }
            }

            return builder.build();
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Implementation of the CADF Resource type.
 * 
 * Resource represents an actor in a CADF event: OBSERVER that reports the
 * event, INITIATOR that performs the action that generates the event, or TARGET
 * upon which the action is performed.
 */
public final class CadfResource {
    private final String id;
    private final CadfEvent.ResourceType typeURI;
    private final String name;
    private final String domain;
    private final CadfCredential credential;
    private final ArrayList<CadfEndpoint> addresses;
    private final String host;
    private final String geolocationId;
    private final CadfGeolocation geolocation;
    private final ArrayList<CadfAttachment> attachments;

    private CadfResource(Builder builder) {
        this.id = builder.id;
        this.typeURI = builder.typeURI;
        this.name = builder.name;
        this.domain = builder.domain;
        this.credential = builder.credential;
        this.addresses = builder.addresses;
        this.host = builder.host;
        this.geolocationId = builder.geolocationId;
        this.geolocation = builder.geolocation;
        this.attachments = builder.attachments;
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

    /**
     * Validate contents of the resource.
     * 
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

    /**
     * Builder for immutable CadfResource objects
     */
    public static class Builder {
        private String id;
        private CadfEvent.ResourceType typeURI;
        private String name;
        private String domain;
        private CadfCredential credential;
        private ArrayList<CadfEndpoint> addresses;
        private String host;
        private String geolocationId;
        private CadfGeolocation geolocation;
        private ArrayList<CadfAttachment> attachments;

        /**
         * Creates an instance of the CadfResource builder.
         * 
         * @param id      - String. Resource identifier.
         * @param typeURI - CadfEvent.ResourceType. Resource classification in the CADF
         *                taxonomy.
         * @see CadfEvent.ResourceType
         */
        public Builder(String id, CadfEvent.ResourceType typeURI) {
            this.id = id;
            this.typeURI = typeURI;
        }

        /**
         * Set the optional local name for the resource (not necessarily unique)
         * 
         * @param name
         * @return Builder
         */
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Set the optional information about the (network) host of the resource
         * 
         * @param host
         * @return Builder
         */
        public Builder withHost(String host) {
            this.host = host;
            return this;
        }

        /**
         * Set the optional name of the domain that qualifies the name of the resource
         * (e.g., a path name, a container name, etc.).
         * 
         * @param domain
         * @return Builder
         */
        public Builder withDomain(String domain) {
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
        public Builder withCredential(CadfCredential cred) {
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
        public Builder withGeolocationId(String geolocId) {
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
        public Builder withGeolocation(CadfGeolocation geoloc) {
            this.geolocation = geoloc;
            return this;
        }

        /**
         * An optional array of additional data containing information about the
         * reporter or any action it performed that affected the CADF Event Record
         * contents.
         */
        public Builder withAttachments(CadfAttachment[] attachments) {
            this.attachments = new ArrayList<CadfAttachment>(Arrays.asList(attachments));
            return this;
        }

        /**
         * An optional array of additional data containing information about the
         * reporter or any action it performed that affected the CADF Event Record
         * contents.
         */
        public Builder withAttachments(ArrayList<CadfAttachment> attachments) {
            this.attachments = attachments;
            return this;
        }

        /**
         * A convenience method to add one attachment at a time.
         * 
         * @see #withAttachments(CadfAttachment[])
         */
        public Builder withAttachment(CadfAttachment attachment) {
            if (this.attachments == null) {
                attachments = new ArrayList<CadfAttachment>();
            }
            this.attachments.add(attachment);
            return this;
        }

        /**
         * An optional array of descriptive addresses (including URLs) of the resource
         */
        public Builder withAddresses(CadfEndpoint[] addresses) {
            this.addresses = new ArrayList<CadfEndpoint>(Arrays.asList(addresses));
            return this;
        }

        /**
         * An optional ArrayList of descriptive addresses (including URLs) of the
         * resource
         */
        public Builder withAddresses(ArrayList<CadfEndpoint> addresses) {
            this.addresses = addresses;
            return this;
        }

        /**
         * A convenience method to add one address at a time.
         * 
         * @see #withAddresses(CadfEndpoint[])
         */
        public Builder withAddress(CadfEndpoint address) {
            if (this.addresses == null) {
                addresses = new ArrayList<CadfEndpoint>();
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

    public CadfGeolocation getGeolocation() {
        return geolocation;
    }

    public CadfEvent.ResourceType getTypeURI() {
        return typeURI;
    }
}

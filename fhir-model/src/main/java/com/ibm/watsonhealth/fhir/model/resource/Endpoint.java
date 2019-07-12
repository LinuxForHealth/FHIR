/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.EndpointStatus;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Url;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The technical details of an endpoint that can be used for electronic services, such as for web services providing XDS.
 * b or a REST endpoint for another FHIR server. This may include any security context information.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Endpoint extends DomainResource {
    private final List<Identifier> identifier;
    private final EndpointStatus status;
    private final Coding connectionType;
    private final String name;
    private final Reference managingOrganization;
    private final List<ContactPoint> contact;
    private final Period period;
    private final List<CodeableConcept> payloadType;
    private final List<Code> payloadMimeType;
    private final Url address;
    private final List<String> header;

    private volatile int hashCode;

    private Endpoint(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        connectionType = ValidationSupport.requireNonNull(builder.connectionType, "connectionType");
        name = builder.name;
        managingOrganization = builder.managingOrganization;
        contact = Collections.unmodifiableList(builder.contact);
        period = builder.period;
        payloadType = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.payloadType, "payloadType"));
        payloadMimeType = Collections.unmodifiableList(builder.payloadMimeType);
        address = ValidationSupport.requireNonNull(builder.address, "address");
        header = Collections.unmodifiableList(builder.header);
    }

    /**
     * <p>
     * Identifier for the organization that is used to identify the endpoint across multiple disparate systems.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * active | suspended | error | off | test.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link EndpointStatus}.
     */
    public EndpointStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * A coded value that represents the technical details of the usage of this endpoint, such as what WSDLs should be used 
     * in what way. (e.g. XDS.b/DICOM/cds-hook).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Coding}.
     */
    public Coding getConnectionType() {
        return connectionType;
    }

    /**
     * <p>
     * A friendly name that this endpoint can be referred to with.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * The organization that manages this endpoint (even if technically another organization is hosting this in the cloud, it 
     * is the organization associated with the data).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getManagingOrganization() {
        return managingOrganization;
    }

    /**
     * <p>
     * Contact details for a human to contact about the subscription. The primary use of this for system administrator 
     * troubleshooting.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactPoint}.
     */
    public List<ContactPoint> getContact() {
        return contact;
    }

    /**
     * <p>
     * The interval during which the endpoint is expected to be operational.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * <p>
     * The payload type describes the acceptable content that can be communicated on the endpoint.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getPayloadType() {
        return payloadType;
    }

    /**
     * <p>
     * The mime type to send the payload in - e.g. application/fhir+xml, application/fhir+json. If the mime type is not 
     * specified, then the sender could send any content (including no content depending on the connectionType).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Code}.
     */
    public List<Code> getPayloadMimeType() {
        return payloadMimeType;
    }

    /**
     * <p>
     * The uri that describes the actual end-point to connect to.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Url}.
     */
    public Url getAddress() {
        return address;
    }

    /**
     * <p>
     * Additional headers / information to send as part of the notification.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String}.
     */
    public List<String> getHeader() {
        return header;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(meta, "meta", visitor);
                accept(implicitRules, "implicitRules", visitor);
                accept(language, "language", visitor);
                accept(text, "text", visitor);
                accept(contained, "contained", visitor, Resource.class);
                accept(extension, "extension", visitor, Extension.class);
                accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(status, "status", visitor);
                accept(connectionType, "connectionType", visitor);
                accept(name, "name", visitor);
                accept(managingOrganization, "managingOrganization", visitor);
                accept(contact, "contact", visitor, ContactPoint.class);
                accept(period, "period", visitor);
                accept(payloadType, "payloadType", visitor, CodeableConcept.class);
                accept(payloadMimeType, "payloadMimeType", visitor, Code.class);
                accept(address, "address", visitor);
                accept(header, "header", visitor, String.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Endpoint other = (Endpoint) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(status, other.status) && 
            Objects.equals(connectionType, other.connectionType) && 
            Objects.equals(name, other.name) && 
            Objects.equals(managingOrganization, other.managingOrganization) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(period, other.period) && 
            Objects.equals(payloadType, other.payloadType) && 
            Objects.equals(payloadMimeType, other.payloadMimeType) && 
            Objects.equals(address, other.address) && 
            Objects.equals(header, other.header);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                meta, 
                implicitRules, 
                language, 
                text, 
                contained, 
                extension, 
                modifierExtension, 
                identifier, 
                status, 
                connectionType, 
                name, 
                managingOrganization, 
                contact, 
                period, 
                payloadType, 
                payloadMimeType, 
                address, 
                header);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(status, connectionType, payloadType, address).from(this);
    }

    public Builder toBuilder(EndpointStatus status, Coding connectionType, List<CodeableConcept> payloadType, Url address) {
        return new Builder(status, connectionType, payloadType, address).from(this);
    }

    public static Builder builder(EndpointStatus status, Coding connectionType, List<CodeableConcept> payloadType, Url address) {
        return new Builder(status, connectionType, payloadType, address);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final EndpointStatus status;
        private final Coding connectionType;
        private final List<CodeableConcept> payloadType;
        private final Url address;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private String name;
        private Reference managingOrganization;
        private List<ContactPoint> contact = new ArrayList<>();
        private Period period;
        private List<Code> payloadMimeType = new ArrayList<>();
        private List<String> header = new ArrayList<>();

        private Builder(EndpointStatus status, Coding connectionType, List<CodeableConcept> payloadType, Url address) {
            super();
            this.status = status;
            this.connectionType = connectionType;
            this.payloadType = new ArrayList<>(payloadType);
            this.address = address;
        }

        /**
         * <p>
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * </p>
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(Id id) {
            return (Builder) super.id(id);
        }

        /**
         * <p>
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
         * </p>
         * 
         * @param meta
         *     Metadata about the resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder meta(Meta meta) {
            return (Builder) super.meta(meta);
        }

        /**
         * <p>
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
         * </p>
         * 
         * @param implicitRules
         *     A set of rules under which this content was created
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder implicitRules(Uri implicitRules) {
            return (Builder) super.implicitRules(implicitRules);
        }

        /**
         * <p>
         * The base language in which the resource is written.
         * </p>
         * 
         * @param language
         *     Language of the resource content
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder language(Code language) {
            return (Builder) super.language(language);
        }

        /**
         * <p>
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
         * </p>
         * 
         * @param text
         *     Text summary of the resource, for human interpretation
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder text(Narrative text) {
            return (Builder) super.text(text);
        }

        /**
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder contained(Resource... contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * </p>
         * <p>
         * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Extension... modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * </p>
         * <p>
         * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * Identifier for the organization that is used to identify the endpoint across multiple disparate systems.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     Identifies this endpoint across multiple systems
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifier for the organization that is used to identify the endpoint across multiple disparate systems.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Identifies this endpoint across multiple systems
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * <p>
         * A friendly name that this endpoint can be referred to with.
         * </p>
         * 
         * @param name
         *     A name that this endpoint can be identified by
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * <p>
         * The organization that manages this endpoint (even if technically another organization is hosting this in the cloud, it 
         * is the organization associated with the data).
         * </p>
         * 
         * @param managingOrganization
         *     Organization that manages this endpoint (might not be the organization that exposes the endpoint)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder managingOrganization(Reference managingOrganization) {
            this.managingOrganization = managingOrganization;
            return this;
        }

        /**
         * <p>
         * Contact details for a human to contact about the subscription. The primary use of this for system administrator 
         * troubleshooting.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param contact
         *     Contact details for source (e.g. troubleshooting)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(ContactPoint... contact) {
            for (ContactPoint value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Contact details for a human to contact about the subscription. The primary use of this for system administrator 
         * troubleshooting.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contact
         *     Contact details for source (e.g. troubleshooting)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(Collection<ContactPoint> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * <p>
         * The interval during which the endpoint is expected to be operational.
         * </p>
         * 
         * @param period
         *     Interval the endpoint is expected to be operational
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * <p>
         * The mime type to send the payload in - e.g. application/fhir+xml, application/fhir+json. If the mime type is not 
         * specified, then the sender could send any content (including no content depending on the connectionType).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param payloadMimeType
         *     Mimetype to send. If not specified, the content could be anything (including no payload, if the connectionType defined 
         *     this)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder payloadMimeType(Code... payloadMimeType) {
            for (Code value : payloadMimeType) {
                this.payloadMimeType.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The mime type to send the payload in - e.g. application/fhir+xml, application/fhir+json. If the mime type is not 
         * specified, then the sender could send any content (including no content depending on the connectionType).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param payloadMimeType
         *     Mimetype to send. If not specified, the content could be anything (including no payload, if the connectionType defined 
         *     this)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder payloadMimeType(Collection<Code> payloadMimeType) {
            this.payloadMimeType = new ArrayList<>(payloadMimeType);
            return this;
        }

        /**
         * <p>
         * Additional headers / information to send as part of the notification.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param header
         *     Usage depends on the channel type
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder header(String... header) {
            for (String value : header) {
                this.header.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Additional headers / information to send as part of the notification.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param header
         *     Usage depends on the channel type
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder header(Collection<String> header) {
            this.header = new ArrayList<>(header);
            return this;
        }

        @Override
        public Endpoint build() {
            return new Endpoint(this);
        }

        private Builder from(Endpoint endpoint) {
            id = endpoint.id;
            meta = endpoint.meta;
            implicitRules = endpoint.implicitRules;
            language = endpoint.language;
            text = endpoint.text;
            contained.addAll(endpoint.contained);
            extension.addAll(endpoint.extension);
            modifierExtension.addAll(endpoint.modifierExtension);
            identifier.addAll(endpoint.identifier);
            name = endpoint.name;
            managingOrganization = endpoint.managingOrganization;
            contact.addAll(endpoint.contact);
            period = endpoint.period;
            payloadMimeType.addAll(endpoint.payloadMimeType);
            header.addAll(endpoint.header);
            return this;
        }
    }
}

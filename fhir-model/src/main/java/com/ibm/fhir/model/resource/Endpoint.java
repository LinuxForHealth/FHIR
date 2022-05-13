/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.EndpointStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The technical details of an endpoint that can be used for electronic services, such as for web services providing XDS.
 * b or a REST endpoint for another FHIR server. This may include any security context information.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "endpoint-0",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/endpoint-connection-type",
    expression = "connectionType.exists() and connectionType.memberOf('http://hl7.org/fhir/ValueSet/endpoint-connection-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Endpoint",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Endpoint extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "EndpointStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The status of the endpoint.",
        valueSet = "http://hl7.org/fhir/ValueSet/endpoint-status|4.3.0-cibuild"
    )
    @Required
    private final EndpointStatus status;
    @Summary
    @Binding(
        bindingName = "endpoint-contype",
        strength = BindingStrength.Value.EXTENSIBLE,
        valueSet = "http://hl7.org/fhir/ValueSet/endpoint-connection-type"
    )
    @Required
    private final Coding connectionType;
    @Summary
    private final String name;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference managingOrganization;
    private final List<ContactPoint> contact;
    @Summary
    private final Period period;
    @Summary
    @Binding(
        bindingName = "PayloadType",
        strength = BindingStrength.Value.EXAMPLE,
        valueSet = "http://hl7.org/fhir/ValueSet/endpoint-payload-type"
    )
    @Required
    private final List<CodeableConcept> payloadType;
    @Summary
    @Binding(
        bindingName = "MimeType",
        strength = BindingStrength.Value.REQUIRED,
        description = "BCP 13 (RFCs 2045, 2046, 2047, 4288, 4289 and 2049)",
        valueSet = "http://hl7.org/fhir/ValueSet/mimetypes|4.3.0-cibuild"
    )
    private final List<Code> payloadMimeType;
    @Summary
    @Required
    private final Url address;
    private final List<String> header;

    private Endpoint(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        connectionType = builder.connectionType;
        name = builder.name;
        managingOrganization = builder.managingOrganization;
        contact = Collections.unmodifiableList(builder.contact);
        period = builder.period;
        payloadType = Collections.unmodifiableList(builder.payloadType);
        payloadMimeType = Collections.unmodifiableList(builder.payloadMimeType);
        address = builder.address;
        header = Collections.unmodifiableList(builder.header);
    }

    /**
     * Identifier for the organization that is used to identify the endpoint across multiple disparate systems.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * active | suspended | error | off | test.
     * 
     * @return
     *     An immutable object of type {@link EndpointStatus} that is non-null.
     */
    public EndpointStatus getStatus() {
        return status;
    }

    /**
     * A coded value that represents the technical details of the usage of this endpoint, such as what WSDLs should be used 
     * in what way. (e.g. XDS.b/DICOM/cds-hook).
     * 
     * @return
     *     An immutable object of type {@link Coding} that is non-null.
     */
    public Coding getConnectionType() {
        return connectionType;
    }

    /**
     * A friendly name that this endpoint can be referred to with.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * The organization that manages this endpoint (even if technically another organization is hosting this in the cloud, it 
     * is the organization associated with the data).
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getManagingOrganization() {
        return managingOrganization;
    }

    /**
     * Contact details for a human to contact about the subscription. The primary use of this for system administrator 
     * troubleshooting.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactPoint} that may be empty.
     */
    public List<ContactPoint> getContact() {
        return contact;
    }

    /**
     * The interval during which the endpoint is expected to be operational.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * The payload type describes the acceptable content that can be communicated on the endpoint.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that is non-empty.
     */
    public List<CodeableConcept> getPayloadType() {
        return payloadType;
    }

    /**
     * The mime type to send the payload in - e.g. application/fhir+xml, application/fhir+json. If the mime type is not 
     * specified, then the sender could send any content (including no content depending on the connectionType).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Code} that may be empty.
     */
    public List<Code> getPayloadMimeType() {
        return payloadMimeType;
    }

    /**
     * The uri that describes the actual end-point to connect to.
     * 
     * @return
     *     An immutable object of type {@link Url} that is non-null.
     */
    public Url getAddress() {
        return address;
    }

    /**
     * Additional headers / information to send as part of the notification.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getHeader() {
        return header;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (connectionType != null) || 
            (name != null) || 
            (managingOrganization != null) || 
            !contact.isEmpty() || 
            (period != null) || 
            !payloadType.isEmpty() || 
            !payloadMimeType.isEmpty() || 
            (address != null) || 
            !header.isEmpty();
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
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
            visitor.visitEnd(elementName, elementIndex, this);
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
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private EndpointStatus status;
        private Coding connectionType;
        private String name;
        private Reference managingOrganization;
        private List<ContactPoint> contact = new ArrayList<>();
        private Period period;
        private List<CodeableConcept> payloadType = new ArrayList<>();
        private List<Code> payloadMimeType = new ArrayList<>();
        private Url address;
        private List<String> header = new ArrayList<>();

        private Builder() {
            super();
        }

        /**
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
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
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
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
         * The base language in which the resource is written.
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
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
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
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * Identifier for the organization that is used to identify the endpoint across multiple disparate systems.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Identifier for the organization that is used to identify the endpoint across multiple disparate systems.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Identifies this endpoint across multiple systems
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * active | suspended | error | off | test.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     active | suspended | error | off | entered-in-error | test
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(EndpointStatus status) {
            this.status = status;
            return this;
        }

        /**
         * A coded value that represents the technical details of the usage of this endpoint, such as what WSDLs should be used 
         * in what way. (e.g. XDS.b/DICOM/cds-hook).
         * 
         * <p>This element is required.
         * 
         * @param connectionType
         *     Protocol/Profile/Standard to be used with this endpoint connection
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder connectionType(Coding connectionType) {
            this.connectionType = connectionType;
            return this;
        }

        /**
         * Convenience method for setting {@code name}.
         * 
         * @param name
         *     A name that this endpoint can be identified by
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #name(com.ibm.fhir.model.type.String)
         */
        public Builder name(java.lang.String name) {
            this.name = (name == null) ? null : String.of(name);
            return this;
        }

        /**
         * A friendly name that this endpoint can be referred to with.
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
         * The organization that manages this endpoint (even if technically another organization is hosting this in the cloud, it 
         * is the organization associated with the data).
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
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
         * Contact details for a human to contact about the subscription. The primary use of this for system administrator 
         * troubleshooting.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Contact details for a human to contact about the subscription. The primary use of this for system administrator 
         * troubleshooting.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact details for source (e.g. troubleshooting)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contact(Collection<ContactPoint> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * The interval during which the endpoint is expected to be operational.
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
         * The payload type describes the acceptable content that can be communicated on the endpoint.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param payloadType
         *     The type of content that may be used at this endpoint (e.g. XDS Discharge summaries)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder payloadType(CodeableConcept... payloadType) {
            for (CodeableConcept value : payloadType) {
                this.payloadType.add(value);
            }
            return this;
        }

        /**
         * The payload type describes the acceptable content that can be communicated on the endpoint.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param payloadType
         *     The type of content that may be used at this endpoint (e.g. XDS Discharge summaries)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder payloadType(Collection<CodeableConcept> payloadType) {
            this.payloadType = new ArrayList<>(payloadType);
            return this;
        }

        /**
         * The mime type to send the payload in - e.g. application/fhir+xml, application/fhir+json. If the mime type is not 
         * specified, then the sender could send any content (including no content depending on the connectionType).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The mime type to send the payload in - e.g. application/fhir+xml, application/fhir+json. If the mime type is not 
         * specified, then the sender could send any content (including no content depending on the connectionType).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param payloadMimeType
         *     Mimetype to send. If not specified, the content could be anything (including no payload, if the connectionType defined 
         *     this)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder payloadMimeType(Collection<Code> payloadMimeType) {
            this.payloadMimeType = new ArrayList<>(payloadMimeType);
            return this;
        }

        /**
         * The uri that describes the actual end-point to connect to.
         * 
         * <p>This element is required.
         * 
         * @param address
         *     The technical base address for connecting to this endpoint
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder address(Url address) {
            this.address = address;
            return this;
        }

        /**
         * Convenience method for setting {@code header}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param header
         *     Usage depends on the channel type
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #header(com.ibm.fhir.model.type.String)
         */
        public Builder header(java.lang.String... header) {
            for (java.lang.String value : header) {
                this.header.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * Additional headers / information to send as part of the notification.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Additional headers / information to send as part of the notification.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param header
         *     Usage depends on the channel type
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder header(Collection<String> header) {
            this.header = new ArrayList<>(header);
            return this;
        }

        /**
         * Build the {@link Endpoint}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>connectionType</li>
         * <li>payloadType</li>
         * <li>address</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Endpoint}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Endpoint per the base specification
         */
        @Override
        public Endpoint build() {
            Endpoint endpoint = new Endpoint(this);
            if (validating) {
                validate(endpoint);
            }
            return endpoint;
        }

        protected void validate(Endpoint endpoint) {
            super.validate(endpoint);
            ValidationSupport.checkList(endpoint.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(endpoint.status, "status");
            ValidationSupport.requireNonNull(endpoint.connectionType, "connectionType");
            ValidationSupport.checkList(endpoint.contact, "contact", ContactPoint.class);
            ValidationSupport.checkNonEmptyList(endpoint.payloadType, "payloadType", CodeableConcept.class);
            ValidationSupport.checkList(endpoint.payloadMimeType, "payloadMimeType", Code.class);
            ValidationSupport.requireNonNull(endpoint.address, "address");
            ValidationSupport.checkList(endpoint.header, "header", String.class);
            ValidationSupport.checkReferenceType(endpoint.managingOrganization, "managingOrganization", "Organization");
        }

        protected Builder from(Endpoint endpoint) {
            super.from(endpoint);
            identifier.addAll(endpoint.identifier);
            status = endpoint.status;
            connectionType = endpoint.connectionType;
            name = endpoint.name;
            managingOrganization = endpoint.managingOrganization;
            contact.addAll(endpoint.contact);
            period = endpoint.period;
            payloadType.addAll(endpoint.payloadType);
            payloadMimeType.addAll(endpoint.payloadMimeType);
            address = endpoint.address;
            header.addAll(endpoint.header);
            return this;
        }
    }
}

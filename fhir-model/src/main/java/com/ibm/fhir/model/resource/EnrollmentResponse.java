/*
 * (C) Copyright IBM Corp. 2019, 2021
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
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.EnrollmentResponseStatus;
import com.ibm.fhir.model.type.code.RemittanceOutcome;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * This resource provides enrollment and plan details from the processing of an EnrollmentRequest resource.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class EnrollmentResponse extends DomainResource {
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "EnrollmentResponseStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "A code specifying the state of the resource instance.",
        valueSet = "http://hl7.org/fhir/ValueSet/fm-status|4.3.0-CIBUILD"
    )
    private final EnrollmentResponseStatus status;
    @ReferenceTarget({ "EnrollmentRequest" })
    private final Reference request;
    @Binding(
        bindingName = "RemittanceOutcome",
        strength = BindingStrength.Value.REQUIRED,
        description = "The outcome of the processing.",
        valueSet = "http://hl7.org/fhir/ValueSet/remittance-outcome|4.3.0-CIBUILD"
    )
    private final RemittanceOutcome outcome;
    private final String disposition;
    private final DateTime created;
    @ReferenceTarget({ "Organization" })
    private final Reference organization;
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
    private final Reference requestProvider;

    private EnrollmentResponse(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        request = builder.request;
        outcome = builder.outcome;
        disposition = builder.disposition;
        created = builder.created;
        organization = builder.organization;
        requestProvider = builder.requestProvider;
    }

    /**
     * The Response business identifier.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The status of the resource instance.
     * 
     * @return
     *     An immutable object of type {@link EnrollmentResponseStatus} that may be null.
     */
    public EnrollmentResponseStatus getStatus() {
        return status;
    }

    /**
     * Original request resource reference.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRequest() {
        return request;
    }

    /**
     * Processing status: error, complete.
     * 
     * @return
     *     An immutable object of type {@link RemittanceOutcome} that may be null.
     */
    public RemittanceOutcome getOutcome() {
        return outcome;
    }

    /**
     * A description of the status of the adjudication.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDisposition() {
        return disposition;
    }

    /**
     * The date when the enclosed suite of services were performed or completed.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * The Insurer who produced this adjudicated response.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getOrganization() {
        return organization;
    }

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRequestProvider() {
        return requestProvider;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (request != null) || 
            (outcome != null) || 
            (disposition != null) || 
            (created != null) || 
            (organization != null) || 
            (requestProvider != null);
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
                accept(request, "request", visitor);
                accept(outcome, "outcome", visitor);
                accept(disposition, "disposition", visitor);
                accept(created, "created", visitor);
                accept(organization, "organization", visitor);
                accept(requestProvider, "requestProvider", visitor);
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
        EnrollmentResponse other = (EnrollmentResponse) obj;
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
            Objects.equals(request, other.request) && 
            Objects.equals(outcome, other.outcome) && 
            Objects.equals(disposition, other.disposition) && 
            Objects.equals(created, other.created) && 
            Objects.equals(organization, other.organization) && 
            Objects.equals(requestProvider, other.requestProvider);
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
                request, 
                outcome, 
                disposition, 
                created, 
                organization, 
                requestProvider);
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
        private EnrollmentResponseStatus status;
        private Reference request;
        private RemittanceOutcome outcome;
        private String disposition;
        private DateTime created;
        private Reference organization;
        private Reference requestProvider;

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
         * The Response business identifier.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier
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
         * The Response business identifier.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier
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
         * The status of the resource instance.
         * 
         * @param status
         *     active | cancelled | draft | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(EnrollmentResponseStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Original request resource reference.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link EnrollmentRequest}</li>
         * </ul>
         * 
         * @param request
         *     Claim reference
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder request(Reference request) {
            this.request = request;
            return this;
        }

        /**
         * Processing status: error, complete.
         * 
         * @param outcome
         *     queued | complete | error | partial
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder outcome(RemittanceOutcome outcome) {
            this.outcome = outcome;
            return this;
        }

        /**
         * Convenience method for setting {@code disposition}.
         * 
         * @param disposition
         *     Disposition Message
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #disposition(com.ibm.fhir.model.type.String)
         */
        public Builder disposition(java.lang.String disposition) {
            this.disposition = (disposition == null) ? null : String.of(disposition);
            return this;
        }

        /**
         * A description of the status of the adjudication.
         * 
         * @param disposition
         *     Disposition Message
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder disposition(String disposition) {
            this.disposition = disposition;
            return this;
        }

        /**
         * The date when the enclosed suite of services were performed or completed.
         * 
         * @param created
         *     Creation date
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder created(DateTime created) {
            this.created = created;
            return this;
        }

        /**
         * The Insurer who produced this adjudicated response.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param organization
         *     Insurer
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder organization(Reference organization) {
            this.organization = organization;
            return this;
        }

        /**
         * The practitioner who is responsible for the services rendered to the patient.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param requestProvider
         *     Responsible practitioner
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder requestProvider(Reference requestProvider) {
            this.requestProvider = requestProvider;
            return this;
        }

        /**
         * Build the {@link EnrollmentResponse}
         * 
         * @return
         *     An immutable object of type {@link EnrollmentResponse}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid EnrollmentResponse per the base specification
         */
        @Override
        public EnrollmentResponse build() {
            EnrollmentResponse enrollmentResponse = new EnrollmentResponse(this);
            if (validating) {
                validate(enrollmentResponse);
            }
            return enrollmentResponse;
        }

        protected void validate(EnrollmentResponse enrollmentResponse) {
            super.validate(enrollmentResponse);
            ValidationSupport.checkList(enrollmentResponse.identifier, "identifier", Identifier.class);
            ValidationSupport.checkReferenceType(enrollmentResponse.request, "request", "EnrollmentRequest");
            ValidationSupport.checkReferenceType(enrollmentResponse.organization, "organization", "Organization");
            ValidationSupport.checkReferenceType(enrollmentResponse.requestProvider, "requestProvider", "Practitioner", "PractitionerRole", "Organization");
        }

        protected Builder from(EnrollmentResponse enrollmentResponse) {
            super.from(enrollmentResponse);
            identifier.addAll(enrollmentResponse.identifier);
            status = enrollmentResponse.status;
            request = enrollmentResponse.request;
            outcome = enrollmentResponse.outcome;
            disposition = enrollmentResponse.disposition;
            created = enrollmentResponse.created;
            organization = enrollmentResponse.organization;
            requestProvider = enrollmentResponse.requestProvider;
            return this;
        }
    }
}

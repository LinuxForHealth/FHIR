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
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.EligibilityResponsePurpose;
import com.ibm.fhir.model.type.code.EligibilityResponseStatus;
import com.ibm.fhir.model.type.code.RemittanceOutcome;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * This resource provides eligibility and plan details from the processing of an CoverageEligibilityRequest resource.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "ces-1",
    level = "Rule",
    location = "CoverageEligibilityResponse.insurance.item",
    description = "SHALL contain a category or a billcode but not both.",
    expression = "category.exists() xor productOrService.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/CoverageEligibilityResponse"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CoverageEligibilityResponse extends DomainResource {
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "EligibilityResponseStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "A code specifying the state of the resource instance.",
        valueSet = "http://hl7.org/fhir/ValueSet/fm-status|4.3.0"
    )
    @Required
    private final EligibilityResponseStatus status;
    @Summary
    @Binding(
        bindingName = "EligibilityResponsePurpose",
        strength = BindingStrength.Value.REQUIRED,
        description = "A code specifying the types of information being requested.",
        valueSet = "http://hl7.org/fhir/ValueSet/eligibilityresponse-purpose|4.3.0"
    )
    @Required
    private final List<EligibilityResponsePurpose> purpose;
    @Summary
    @ReferenceTarget({ "Patient" })
    @Required
    private final Reference patient;
    @Choice({ Date.class, Period.class })
    private final Element serviced;
    @Summary
    @Required
    private final DateTime created;
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
    private final Reference requestor;
    @Summary
    @ReferenceTarget({ "CoverageEligibilityRequest" })
    @Required
    private final Reference request;
    @Summary
    @Binding(
        bindingName = "RemittanceOutcome",
        strength = BindingStrength.Value.REQUIRED,
        description = "The outcome of the processing.",
        valueSet = "http://hl7.org/fhir/ValueSet/remittance-outcome|4.3.0"
    )
    @Required
    private final RemittanceOutcome outcome;
    private final String disposition;
    @Summary
    @ReferenceTarget({ "Organization" })
    @Required
    private final Reference insurer;
    private final List<Insurance> insurance;
    private final String preAuthRef;
    @Binding(
        bindingName = "Forms",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The forms codes.",
        valueSet = "http://hl7.org/fhir/ValueSet/forms"
    )
    private final CodeableConcept form;
    private final List<Error> error;

    private CoverageEligibilityResponse(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        purpose = Collections.unmodifiableList(builder.purpose);
        patient = builder.patient;
        serviced = builder.serviced;
        created = builder.created;
        requestor = builder.requestor;
        request = builder.request;
        outcome = builder.outcome;
        disposition = builder.disposition;
        insurer = builder.insurer;
        insurance = Collections.unmodifiableList(builder.insurance);
        preAuthRef = builder.preAuthRef;
        form = builder.form;
        error = Collections.unmodifiableList(builder.error);
    }

    /**
     * A unique identifier assigned to this coverage eligiblity request.
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
     *     An immutable object of type {@link EligibilityResponseStatus} that is non-null.
     */
    public EligibilityResponseStatus getStatus() {
        return status;
    }

    /**
     * Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; 
     * benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation 
     * that the specified coverage is in-force at the date/period specified or 'now' if not specified.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link EligibilityResponsePurpose} that is non-empty.
     */
    public List<EligibilityResponsePurpose> getPurpose() {
        return purpose;
    }

    /**
     * The party who is the beneficiary of the supplied coverage and for whom eligibility is sought.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * The date or dates when the enclosed suite of services were performed or completed.
     * 
     * @return
     *     An immutable object of type {@link Date} or {@link Period} that may be null.
     */
    public Element getServiced() {
        return serviced;
    }

    /**
     * The date this resource was created.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that is non-null.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * The provider which is responsible for the request.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRequestor() {
        return requestor;
    }

    /**
     * Reference to the original request resource.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getRequest() {
        return request;
    }

    /**
     * The outcome of the request processing.
     * 
     * @return
     *     An immutable object of type {@link RemittanceOutcome} that is non-null.
     */
    public RemittanceOutcome getOutcome() {
        return outcome;
    }

    /**
     * A human readable description of the status of the adjudication.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDisposition() {
        return disposition;
    }

    /**
     * The Insurer who issued the coverage in question and is the author of the response.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getInsurer() {
        return insurer;
    }

    /**
     * Financial instruments for reimbursement for the health care products and services.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Insurance} that may be empty.
     */
    public List<Insurance> getInsurance() {
        return insurance;
    }

    /**
     * A reference from the Insurer to which these services pertain to be used on further communication and as proof that the 
     * request occurred.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getPreAuthRef() {
        return preAuthRef;
    }

    /**
     * A code for the form to be used for printing the content.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getForm() {
        return form;
    }

    /**
     * Errors encountered during the processing of the request.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Error} that may be empty.
     */
    public List<Error> getError() {
        return error;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            !purpose.isEmpty() || 
            (patient != null) || 
            (serviced != null) || 
            (created != null) || 
            (requestor != null) || 
            (request != null) || 
            (outcome != null) || 
            (disposition != null) || 
            (insurer != null) || 
            !insurance.isEmpty() || 
            (preAuthRef != null) || 
            (form != null) || 
            !error.isEmpty();
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
                accept(purpose, "purpose", visitor, EligibilityResponsePurpose.class);
                accept(patient, "patient", visitor);
                accept(serviced, "serviced", visitor);
                accept(created, "created", visitor);
                accept(requestor, "requestor", visitor);
                accept(request, "request", visitor);
                accept(outcome, "outcome", visitor);
                accept(disposition, "disposition", visitor);
                accept(insurer, "insurer", visitor);
                accept(insurance, "insurance", visitor, Insurance.class);
                accept(preAuthRef, "preAuthRef", visitor);
                accept(form, "form", visitor);
                accept(error, "error", visitor, Error.class);
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
        CoverageEligibilityResponse other = (CoverageEligibilityResponse) obj;
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
            Objects.equals(purpose, other.purpose) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(serviced, other.serviced) && 
            Objects.equals(created, other.created) && 
            Objects.equals(requestor, other.requestor) && 
            Objects.equals(request, other.request) && 
            Objects.equals(outcome, other.outcome) && 
            Objects.equals(disposition, other.disposition) && 
            Objects.equals(insurer, other.insurer) && 
            Objects.equals(insurance, other.insurance) && 
            Objects.equals(preAuthRef, other.preAuthRef) && 
            Objects.equals(form, other.form) && 
            Objects.equals(error, other.error);
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
                purpose, 
                patient, 
                serviced, 
                created, 
                requestor, 
                request, 
                outcome, 
                disposition, 
                insurer, 
                insurance, 
                preAuthRef, 
                form, 
                error);
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
        private EligibilityResponseStatus status;
        private List<EligibilityResponsePurpose> purpose = new ArrayList<>();
        private Reference patient;
        private Element serviced;
        private DateTime created;
        private Reference requestor;
        private Reference request;
        private RemittanceOutcome outcome;
        private String disposition;
        private Reference insurer;
        private List<Insurance> insurance = new ArrayList<>();
        private String preAuthRef;
        private CodeableConcept form;
        private List<Error> error = new ArrayList<>();

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
         * A unique identifier assigned to this coverage eligiblity request.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for coverage eligiblity request
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
         * A unique identifier assigned to this coverage eligiblity request.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for coverage eligiblity request
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
         * <p>This element is required.
         * 
         * @param status
         *     active | cancelled | draft | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(EligibilityResponseStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; 
         * benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation 
         * that the specified coverage is in-force at the date/period specified or 'now' if not specified.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param purpose
         *     auth-requirements | benefits | discovery | validation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(EligibilityResponsePurpose... purpose) {
            for (EligibilityResponsePurpose value : purpose) {
                this.purpose.add(value);
            }
            return this;
        }

        /**
         * Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; 
         * benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation 
         * that the specified coverage is in-force at the date/period specified or 'now' if not specified.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param purpose
         *     auth-requirements | benefits | discovery | validation
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder purpose(Collection<EligibilityResponsePurpose> purpose) {
            this.purpose = new ArrayList<>(purpose);
            return this;
        }

        /**
         * The party who is the beneficiary of the supplied coverage and for whom eligibility is sought.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
         * 
         * @param patient
         *     Intended recipient of products and services
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        /**
         * Convenience method for setting {@code serviced} with choice type Date.
         * 
         * @param serviced
         *     Estimated date or dates of service
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #serviced(Element)
         */
        public Builder serviced(java.time.LocalDate serviced) {
            this.serviced = (serviced == null) ? null : Date.of(serviced);
            return this;
        }

        /**
         * The date or dates when the enclosed suite of services were performed or completed.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Date}</li>
         * <li>{@link Period}</li>
         * </ul>
         * 
         * @param serviced
         *     Estimated date or dates of service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder serviced(Element serviced) {
            this.serviced = serviced;
            return this;
        }

        /**
         * The date this resource was created.
         * 
         * <p>This element is required.
         * 
         * @param created
         *     Response creation date
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder created(DateTime created) {
            this.created = created;
            return this;
        }

        /**
         * The provider which is responsible for the request.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param requestor
         *     Party responsible for the request
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder requestor(Reference requestor) {
            this.requestor = requestor;
            return this;
        }

        /**
         * Reference to the original request resource.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link CoverageEligibilityRequest}</li>
         * </ul>
         * 
         * @param request
         *     Eligibility request reference
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder request(Reference request) {
            this.request = request;
            return this;
        }

        /**
         * The outcome of the request processing.
         * 
         * <p>This element is required.
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
         * A human readable description of the status of the adjudication.
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
         * The Insurer who issued the coverage in question and is the author of the response.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param insurer
         *     Coverage issuer
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder insurer(Reference insurer) {
            this.insurer = insurer;
            return this;
        }

        /**
         * Financial instruments for reimbursement for the health care products and services.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param insurance
         *     Patient insurance information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder insurance(Insurance... insurance) {
            for (Insurance value : insurance) {
                this.insurance.add(value);
            }
            return this;
        }

        /**
         * Financial instruments for reimbursement for the health care products and services.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param insurance
         *     Patient insurance information
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder insurance(Collection<Insurance> insurance) {
            this.insurance = new ArrayList<>(insurance);
            return this;
        }

        /**
         * Convenience method for setting {@code preAuthRef}.
         * 
         * @param preAuthRef
         *     Preauthorization reference
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #preAuthRef(com.ibm.fhir.model.type.String)
         */
        public Builder preAuthRef(java.lang.String preAuthRef) {
            this.preAuthRef = (preAuthRef == null) ? null : String.of(preAuthRef);
            return this;
        }

        /**
         * A reference from the Insurer to which these services pertain to be used on further communication and as proof that the 
         * request occurred.
         * 
         * @param preAuthRef
         *     Preauthorization reference
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder preAuthRef(String preAuthRef) {
            this.preAuthRef = preAuthRef;
            return this;
        }

        /**
         * A code for the form to be used for printing the content.
         * 
         * @param form
         *     Printed form identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder form(CodeableConcept form) {
            this.form = form;
            return this;
        }

        /**
         * Errors encountered during the processing of the request.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param error
         *     Processing errors
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder error(Error... error) {
            for (Error value : error) {
                this.error.add(value);
            }
            return this;
        }

        /**
         * Errors encountered during the processing of the request.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param error
         *     Processing errors
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder error(Collection<Error> error) {
            this.error = new ArrayList<>(error);
            return this;
        }

        /**
         * Build the {@link CoverageEligibilityResponse}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>purpose</li>
         * <li>patient</li>
         * <li>created</li>
         * <li>request</li>
         * <li>outcome</li>
         * <li>insurer</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link CoverageEligibilityResponse}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid CoverageEligibilityResponse per the base specification
         */
        @Override
        public CoverageEligibilityResponse build() {
            CoverageEligibilityResponse coverageEligibilityResponse = new CoverageEligibilityResponse(this);
            if (validating) {
                validate(coverageEligibilityResponse);
            }
            return coverageEligibilityResponse;
        }

        protected void validate(CoverageEligibilityResponse coverageEligibilityResponse) {
            super.validate(coverageEligibilityResponse);
            ValidationSupport.checkList(coverageEligibilityResponse.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(coverageEligibilityResponse.status, "status");
            ValidationSupport.checkNonEmptyList(coverageEligibilityResponse.purpose, "purpose", EligibilityResponsePurpose.class);
            ValidationSupport.requireNonNull(coverageEligibilityResponse.patient, "patient");
            ValidationSupport.choiceElement(coverageEligibilityResponse.serviced, "serviced", Date.class, Period.class);
            ValidationSupport.requireNonNull(coverageEligibilityResponse.created, "created");
            ValidationSupport.requireNonNull(coverageEligibilityResponse.request, "request");
            ValidationSupport.requireNonNull(coverageEligibilityResponse.outcome, "outcome");
            ValidationSupport.requireNonNull(coverageEligibilityResponse.insurer, "insurer");
            ValidationSupport.checkList(coverageEligibilityResponse.insurance, "insurance", Insurance.class);
            ValidationSupport.checkList(coverageEligibilityResponse.error, "error", Error.class);
            ValidationSupport.checkReferenceType(coverageEligibilityResponse.patient, "patient", "Patient");
            ValidationSupport.checkReferenceType(coverageEligibilityResponse.requestor, "requestor", "Practitioner", "PractitionerRole", "Organization");
            ValidationSupport.checkReferenceType(coverageEligibilityResponse.request, "request", "CoverageEligibilityRequest");
            ValidationSupport.checkReferenceType(coverageEligibilityResponse.insurer, "insurer", "Organization");
        }

        protected Builder from(CoverageEligibilityResponse coverageEligibilityResponse) {
            super.from(coverageEligibilityResponse);
            identifier.addAll(coverageEligibilityResponse.identifier);
            status = coverageEligibilityResponse.status;
            purpose.addAll(coverageEligibilityResponse.purpose);
            patient = coverageEligibilityResponse.patient;
            serviced = coverageEligibilityResponse.serviced;
            created = coverageEligibilityResponse.created;
            requestor = coverageEligibilityResponse.requestor;
            request = coverageEligibilityResponse.request;
            outcome = coverageEligibilityResponse.outcome;
            disposition = coverageEligibilityResponse.disposition;
            insurer = coverageEligibilityResponse.insurer;
            insurance.addAll(coverageEligibilityResponse.insurance);
            preAuthRef = coverageEligibilityResponse.preAuthRef;
            form = coverageEligibilityResponse.form;
            error.addAll(coverageEligibilityResponse.error);
            return this;
        }
    }

    /**
     * Financial instruments for reimbursement for the health care products and services.
     */
    public static class Insurance extends BackboneElement {
        @Summary
        @ReferenceTarget({ "Coverage" })
        @Required
        private final Reference coverage;
        private final Boolean inforce;
        private final Period benefitPeriod;
        private final List<Item> item;

        private Insurance(Builder builder) {
            super(builder);
            coverage = builder.coverage;
            inforce = builder.inforce;
            benefitPeriod = builder.benefitPeriod;
            item = Collections.unmodifiableList(builder.item);
        }

        /**
         * Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer 
         * will use these details to locate the patient's actual coverage within the insurer's information system.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getCoverage() {
            return coverage;
        }

        /**
         * Flag indicating if the coverage provided is inforce currently if no service date(s) specified or for the whole 
         * duration of the service dates.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getInforce() {
            return inforce;
        }

        /**
         * The term of the benefits documented in this response.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getBenefitPeriod() {
            return benefitPeriod;
        }

        /**
         * Benefits and optionally current balances, and authorization details by category or service.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Item} that may be empty.
         */
        public List<Item> getItem() {
            return item;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (coverage != null) || 
                (inforce != null) || 
                (benefitPeriod != null) || 
                !item.isEmpty();
        }

        @Override
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(coverage, "coverage", visitor);
                    accept(inforce, "inforce", visitor);
                    accept(benefitPeriod, "benefitPeriod", visitor);
                    accept(item, "item", visitor, Item.class);
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
            Insurance other = (Insurance) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(coverage, other.coverage) && 
                Objects.equals(inforce, other.inforce) && 
                Objects.equals(benefitPeriod, other.benefitPeriod) && 
                Objects.equals(item, other.item);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    coverage, 
                    inforce, 
                    benefitPeriod, 
                    item);
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

        public static class Builder extends BackboneElement.Builder {
            private Reference coverage;
            private Boolean inforce;
            private Period benefitPeriod;
            private List<Item> item = new ArrayList<>();

            private Builder() {
                super();
            }

            /**
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * 
             * @param id
             *     Unique id for inter-element referencing
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder id(java.lang.String id) {
                return (Builder) super.id(id);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
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
             * Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer 
             * will use these details to locate the patient's actual coverage within the insurer's information system.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Coverage}</li>
             * </ul>
             * 
             * @param coverage
             *     Insurance information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder coverage(Reference coverage) {
                this.coverage = coverage;
                return this;
            }

            /**
             * Convenience method for setting {@code inforce}.
             * 
             * @param inforce
             *     Coverage inforce indicator
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #inforce(com.ibm.fhir.model.type.Boolean)
             */
            public Builder inforce(java.lang.Boolean inforce) {
                this.inforce = (inforce == null) ? null : Boolean.of(inforce);
                return this;
            }

            /**
             * Flag indicating if the coverage provided is inforce currently if no service date(s) specified or for the whole 
             * duration of the service dates.
             * 
             * @param inforce
             *     Coverage inforce indicator
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder inforce(Boolean inforce) {
                this.inforce = inforce;
                return this;
            }

            /**
             * The term of the benefits documented in this response.
             * 
             * @param benefitPeriod
             *     When the benefits are applicable
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder benefitPeriod(Period benefitPeriod) {
                this.benefitPeriod = benefitPeriod;
                return this;
            }

            /**
             * Benefits and optionally current balances, and authorization details by category or service.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param item
             *     Benefits and authorization details
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder item(Item... item) {
                for (Item value : item) {
                    this.item.add(value);
                }
                return this;
            }

            /**
             * Benefits and optionally current balances, and authorization details by category or service.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param item
             *     Benefits and authorization details
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder item(Collection<Item> item) {
                this.item = new ArrayList<>(item);
                return this;
            }

            /**
             * Build the {@link Insurance}
             * 
             * <p>Required elements:
             * <ul>
             * <li>coverage</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Insurance}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Insurance per the base specification
             */
            @Override
            public Insurance build() {
                Insurance insurance = new Insurance(this);
                if (validating) {
                    validate(insurance);
                }
                return insurance;
            }

            protected void validate(Insurance insurance) {
                super.validate(insurance);
                ValidationSupport.requireNonNull(insurance.coverage, "coverage");
                ValidationSupport.checkList(insurance.item, "item", Item.class);
                ValidationSupport.checkReferenceType(insurance.coverage, "coverage", "Coverage");
                ValidationSupport.requireValueOrChildren(insurance);
            }

            protected Builder from(Insurance insurance) {
                super.from(insurance);
                coverage = insurance.coverage;
                inforce = insurance.inforce;
                benefitPeriod = insurance.benefitPeriod;
                item.addAll(insurance.item);
                return this;
            }
        }

        /**
         * Benefits and optionally current balances, and authorization details by category or service.
         */
        public static class Item extends BackboneElement {
            @Binding(
                bindingName = "BenefitCategory",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Benefit categories such as: oral, medical, vision etc.",
                valueSet = "http://hl7.org/fhir/ValueSet/ex-benefitcategory"
            )
            private final CodeableConcept category;
            @Binding(
                bindingName = "ServiceProduct",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Allowable service and product codes.",
                valueSet = "http://hl7.org/fhir/ValueSet/service-uscls"
            )
            private final CodeableConcept productOrService;
            @Binding(
                bindingName = "Modifiers",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Item type or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.",
                valueSet = "http://hl7.org/fhir/ValueSet/claim-modifiers"
            )
            private final List<CodeableConcept> modifier;
            @ReferenceTarget({ "Practitioner", "PractitionerRole" })
            private final Reference provider;
            private final Boolean excluded;
            private final String name;
            private final String description;
            @Binding(
                bindingName = "BenefitNetwork",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Code to classify in or out of network services.",
                valueSet = "http://hl7.org/fhir/ValueSet/benefit-network"
            )
            private final CodeableConcept network;
            @Binding(
                bindingName = "BenefitUnit",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Unit covered/serviced - individual or family.",
                valueSet = "http://hl7.org/fhir/ValueSet/benefit-unit"
            )
            private final CodeableConcept unit;
            @Binding(
                bindingName = "BenefitTerm",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Coverage unit - annual, lifetime.",
                valueSet = "http://hl7.org/fhir/ValueSet/benefit-term"
            )
            private final CodeableConcept term;
            private final List<Benefit> benefit;
            private final Boolean authorizationRequired;
            @Binding(
                bindingName = "AuthSupporting",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Type of supporting information to provide with a preauthorization.",
                valueSet = "http://hl7.org/fhir/ValueSet/coverageeligibilityresponse-ex-auth-support"
            )
            private final List<CodeableConcept> authorizationSupporting;
            private final Uri authorizationUrl;

            private Item(Builder builder) {
                super(builder);
                category = builder.category;
                productOrService = builder.productOrService;
                modifier = Collections.unmodifiableList(builder.modifier);
                provider = builder.provider;
                excluded = builder.excluded;
                name = builder.name;
                description = builder.description;
                network = builder.network;
                unit = builder.unit;
                term = builder.term;
                benefit = Collections.unmodifiableList(builder.benefit);
                authorizationRequired = builder.authorizationRequired;
                authorizationSupporting = Collections.unmodifiableList(builder.authorizationSupporting);
                authorizationUrl = builder.authorizationUrl;
            }

            /**
             * Code to identify the general type of benefits under which products and services are provided.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getCategory() {
                return category;
            }

            /**
             * This contains the product, service, drug or other billing code for the item.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getProductOrService() {
                return productOrService;
            }

            /**
             * Item typification or modifiers codes to convey additional context for the product or service.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getModifier() {
                return modifier;
            }

            /**
             * The practitioner who is eligible for the provision of the product or service.
             * 
             * @return
             *     An immutable object of type {@link Reference} that may be null.
             */
            public Reference getProvider() {
                return provider;
            }

            /**
             * True if the indicated class of service is excluded from the plan, missing or False indicates the product or service is 
             * included in the coverage.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getExcluded() {
                return excluded;
            }

            /**
             * A short name or tag for the benefit.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getName() {
                return name;
            }

            /**
             * A richer description of the benefit or services covered.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getDescription() {
                return description;
            }

            /**
             * Is a flag to indicate whether the benefits refer to in-network providers or out-of-network providers.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getNetwork() {
                return network;
            }

            /**
             * Indicates if the benefits apply to an individual or to the family.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getUnit() {
                return unit;
            }

            /**
             * The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual visits'.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getTerm() {
                return term;
            }

            /**
             * Benefits used to date.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Benefit} that may be empty.
             */
            public List<Benefit> getBenefit() {
                return benefit;
            }

            /**
             * A boolean flag indicating whether a preauthorization is required prior to actual service delivery.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getAuthorizationRequired() {
                return authorizationRequired;
            }

            /**
             * Codes or comments regarding information or actions associated with the preauthorization.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getAuthorizationSupporting() {
                return authorizationSupporting;
            }

            /**
             * A web location for obtaining requirements or descriptive information regarding the preauthorization.
             * 
             * @return
             *     An immutable object of type {@link Uri} that may be null.
             */
            public Uri getAuthorizationUrl() {
                return authorizationUrl;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (category != null) || 
                    (productOrService != null) || 
                    !modifier.isEmpty() || 
                    (provider != null) || 
                    (excluded != null) || 
                    (name != null) || 
                    (description != null) || 
                    (network != null) || 
                    (unit != null) || 
                    (term != null) || 
                    !benefit.isEmpty() || 
                    (authorizationRequired != null) || 
                    !authorizationSupporting.isEmpty() || 
                    (authorizationUrl != null);
            }

            @Override
            public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, elementIndex, this);
                    if (visitor.visit(elementName, elementIndex, this)) {
                        // visit children
                        accept(id, "id", visitor);
                        accept(extension, "extension", visitor, Extension.class);
                        accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                        accept(category, "category", visitor);
                        accept(productOrService, "productOrService", visitor);
                        accept(modifier, "modifier", visitor, CodeableConcept.class);
                        accept(provider, "provider", visitor);
                        accept(excluded, "excluded", visitor);
                        accept(name, "name", visitor);
                        accept(description, "description", visitor);
                        accept(network, "network", visitor);
                        accept(unit, "unit", visitor);
                        accept(term, "term", visitor);
                        accept(benefit, "benefit", visitor, Benefit.class);
                        accept(authorizationRequired, "authorizationRequired", visitor);
                        accept(authorizationSupporting, "authorizationSupporting", visitor, CodeableConcept.class);
                        accept(authorizationUrl, "authorizationUrl", visitor);
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
                Item other = (Item) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(category, other.category) && 
                    Objects.equals(productOrService, other.productOrService) && 
                    Objects.equals(modifier, other.modifier) && 
                    Objects.equals(provider, other.provider) && 
                    Objects.equals(excluded, other.excluded) && 
                    Objects.equals(name, other.name) && 
                    Objects.equals(description, other.description) && 
                    Objects.equals(network, other.network) && 
                    Objects.equals(unit, other.unit) && 
                    Objects.equals(term, other.term) && 
                    Objects.equals(benefit, other.benefit) && 
                    Objects.equals(authorizationRequired, other.authorizationRequired) && 
                    Objects.equals(authorizationSupporting, other.authorizationSupporting) && 
                    Objects.equals(authorizationUrl, other.authorizationUrl);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        category, 
                        productOrService, 
                        modifier, 
                        provider, 
                        excluded, 
                        name, 
                        description, 
                        network, 
                        unit, 
                        term, 
                        benefit, 
                        authorizationRequired, 
                        authorizationSupporting, 
                        authorizationUrl);
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

            public static class Builder extends BackboneElement.Builder {
                private CodeableConcept category;
                private CodeableConcept productOrService;
                private List<CodeableConcept> modifier = new ArrayList<>();
                private Reference provider;
                private Boolean excluded;
                private String name;
                private String description;
                private CodeableConcept network;
                private CodeableConcept unit;
                private CodeableConcept term;
                private List<Benefit> benefit = new ArrayList<>();
                private Boolean authorizationRequired;
                private List<CodeableConcept> authorizationSupporting = new ArrayList<>();
                private Uri authorizationUrl;

                private Builder() {
                    super();
                }

                /**
                 * Unique id for the element within a resource (for internal references). This may be any string value that does not 
                 * contain spaces.
                 * 
                 * @param id
                 *     Unique id for inter-element referencing
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                @Override
                public Builder id(java.lang.String id) {
                    return (Builder) super.id(id);
                }

                /**
                 * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
                 * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
                 * May be used to represent additional information that is not part of the basic definition of the element and that 
                 * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                 * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                 * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                 * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                 * extension. Applications processing a resource are required to check for modifier extensions.
                 * 
                 * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                 * change the meaning of modifierExtension itself).
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                @Override
                public Builder modifierExtension(Extension... modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * May be used to represent additional information that is not part of the basic definition of the element and that 
                 * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                 * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                 * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                 * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                 * extension. Applications processing a resource are required to check for modifier extensions.
                 * 
                 * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                 * change the meaning of modifierExtension itself).
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
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
                 * Code to identify the general type of benefits under which products and services are provided.
                 * 
                 * @param category
                 *     Benefit classification
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder category(CodeableConcept category) {
                    this.category = category;
                    return this;
                }

                /**
                 * This contains the product, service, drug or other billing code for the item.
                 * 
                 * @param productOrService
                 *     Billing, service, product, or drug code
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder productOrService(CodeableConcept productOrService) {
                    this.productOrService = productOrService;
                    return this;
                }

                /**
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param modifier
                 *     Product or service billing modifiers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder modifier(CodeableConcept... modifier) {
                    for (CodeableConcept value : modifier) {
                        this.modifier.add(value);
                    }
                    return this;
                }

                /**
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param modifier
                 *     Product or service billing modifiers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder modifier(Collection<CodeableConcept> modifier) {
                    this.modifier = new ArrayList<>(modifier);
                    return this;
                }

                /**
                 * The practitioner who is eligible for the provision of the product or service.
                 * 
                 * <p>Allowed resource types for this reference:
                 * <ul>
                 * <li>{@link Practitioner}</li>
                 * <li>{@link PractitionerRole}</li>
                 * </ul>
                 * 
                 * @param provider
                 *     Performing practitioner
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder provider(Reference provider) {
                    this.provider = provider;
                    return this;
                }

                /**
                 * Convenience method for setting {@code excluded}.
                 * 
                 * @param excluded
                 *     Excluded from the plan
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #excluded(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder excluded(java.lang.Boolean excluded) {
                    this.excluded = (excluded == null) ? null : Boolean.of(excluded);
                    return this;
                }

                /**
                 * True if the indicated class of service is excluded from the plan, missing or False indicates the product or service is 
                 * included in the coverage.
                 * 
                 * @param excluded
                 *     Excluded from the plan
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder excluded(Boolean excluded) {
                    this.excluded = excluded;
                    return this;
                }

                /**
                 * Convenience method for setting {@code name}.
                 * 
                 * @param name
                 *     Short name for the benefit
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
                 * A short name or tag for the benefit.
                 * 
                 * @param name
                 *     Short name for the benefit
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder name(String name) {
                    this.name = name;
                    return this;
                }

                /**
                 * Convenience method for setting {@code description}.
                 * 
                 * @param description
                 *     Description of the benefit or services covered
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #description(com.ibm.fhir.model.type.String)
                 */
                public Builder description(java.lang.String description) {
                    this.description = (description == null) ? null : String.of(description);
                    return this;
                }

                /**
                 * A richer description of the benefit or services covered.
                 * 
                 * @param description
                 *     Description of the benefit or services covered
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder description(String description) {
                    this.description = description;
                    return this;
                }

                /**
                 * Is a flag to indicate whether the benefits refer to in-network providers or out-of-network providers.
                 * 
                 * @param network
                 *     In or out of network
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder network(CodeableConcept network) {
                    this.network = network;
                    return this;
                }

                /**
                 * Indicates if the benefits apply to an individual or to the family.
                 * 
                 * @param unit
                 *     Individual or family
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder unit(CodeableConcept unit) {
                    this.unit = unit;
                    return this;
                }

                /**
                 * The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual visits'.
                 * 
                 * @param term
                 *     Annual or lifetime
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder term(CodeableConcept term) {
                    this.term = term;
                    return this;
                }

                /**
                 * Benefits used to date.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param benefit
                 *     Benefit Summary
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder benefit(Benefit... benefit) {
                    for (Benefit value : benefit) {
                        this.benefit.add(value);
                    }
                    return this;
                }

                /**
                 * Benefits used to date.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param benefit
                 *     Benefit Summary
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder benefit(Collection<Benefit> benefit) {
                    this.benefit = new ArrayList<>(benefit);
                    return this;
                }

                /**
                 * Convenience method for setting {@code authorizationRequired}.
                 * 
                 * @param authorizationRequired
                 *     Authorization required flag
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #authorizationRequired(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder authorizationRequired(java.lang.Boolean authorizationRequired) {
                    this.authorizationRequired = (authorizationRequired == null) ? null : Boolean.of(authorizationRequired);
                    return this;
                }

                /**
                 * A boolean flag indicating whether a preauthorization is required prior to actual service delivery.
                 * 
                 * @param authorizationRequired
                 *     Authorization required flag
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder authorizationRequired(Boolean authorizationRequired) {
                    this.authorizationRequired = authorizationRequired;
                    return this;
                }

                /**
                 * Codes or comments regarding information or actions associated with the preauthorization.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param authorizationSupporting
                 *     Type of required supporting materials
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder authorizationSupporting(CodeableConcept... authorizationSupporting) {
                    for (CodeableConcept value : authorizationSupporting) {
                        this.authorizationSupporting.add(value);
                    }
                    return this;
                }

                /**
                 * Codes or comments regarding information or actions associated with the preauthorization.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param authorizationSupporting
                 *     Type of required supporting materials
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder authorizationSupporting(Collection<CodeableConcept> authorizationSupporting) {
                    this.authorizationSupporting = new ArrayList<>(authorizationSupporting);
                    return this;
                }

                /**
                 * A web location for obtaining requirements or descriptive information regarding the preauthorization.
                 * 
                 * @param authorizationUrl
                 *     Preauthorization requirements endpoint
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder authorizationUrl(Uri authorizationUrl) {
                    this.authorizationUrl = authorizationUrl;
                    return this;
                }

                /**
                 * Build the {@link Item}
                 * 
                 * @return
                 *     An immutable object of type {@link Item}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Item per the base specification
                 */
                @Override
                public Item build() {
                    Item item = new Item(this);
                    if (validating) {
                        validate(item);
                    }
                    return item;
                }

                protected void validate(Item item) {
                    super.validate(item);
                    ValidationSupport.checkList(item.modifier, "modifier", CodeableConcept.class);
                    ValidationSupport.checkList(item.benefit, "benefit", Benefit.class);
                    ValidationSupport.checkList(item.authorizationSupporting, "authorizationSupporting", CodeableConcept.class);
                    ValidationSupport.checkReferenceType(item.provider, "provider", "Practitioner", "PractitionerRole");
                    ValidationSupport.requireValueOrChildren(item);
                }

                protected Builder from(Item item) {
                    super.from(item);
                    category = item.category;
                    productOrService = item.productOrService;
                    modifier.addAll(item.modifier);
                    provider = item.provider;
                    excluded = item.excluded;
                    name = item.name;
                    description = item.description;
                    network = item.network;
                    unit = item.unit;
                    term = item.term;
                    benefit.addAll(item.benefit);
                    authorizationRequired = item.authorizationRequired;
                    authorizationSupporting.addAll(item.authorizationSupporting);
                    authorizationUrl = item.authorizationUrl;
                    return this;
                }
            }

            /**
             * Benefits used to date.
             */
            public static class Benefit extends BackboneElement {
                @Binding(
                    bindingName = "BenefitType",
                    strength = BindingStrength.Value.EXAMPLE,
                    description = "Deductable, visits, co-pay, etc.",
                    valueSet = "http://hl7.org/fhir/ValueSet/benefit-type"
                )
                @Required
                private final CodeableConcept type;
                @Choice({ UnsignedInt.class, String.class, Money.class })
                private final Element allowed;
                @Choice({ UnsignedInt.class, String.class, Money.class })
                private final Element used;

                private Benefit(Builder builder) {
                    super(builder);
                    type = builder.type;
                    allowed = builder.allowed;
                    used = builder.used;
                }

                /**
                 * Classification of benefit being provided.
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept} that is non-null.
                 */
                public CodeableConcept getType() {
                    return type;
                }

                /**
                 * The quantity of the benefit which is permitted under the coverage.
                 * 
                 * @return
                 *     An immutable object of type {@link UnsignedInt}, {@link String} or {@link Money} that may be null.
                 */
                public Element getAllowed() {
                    return allowed;
                }

                /**
                 * The quantity of the benefit which have been consumed to date.
                 * 
                 * @return
                 *     An immutable object of type {@link UnsignedInt}, {@link String} or {@link Money} that may be null.
                 */
                public Element getUsed() {
                    return used;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (type != null) || 
                        (allowed != null) || 
                        (used != null);
                }

                @Override
                public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                    if (visitor.preVisit(this)) {
                        visitor.visitStart(elementName, elementIndex, this);
                        if (visitor.visit(elementName, elementIndex, this)) {
                            // visit children
                            accept(id, "id", visitor);
                            accept(extension, "extension", visitor, Extension.class);
                            accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                            accept(type, "type", visitor);
                            accept(allowed, "allowed", visitor);
                            accept(used, "used", visitor);
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
                    Benefit other = (Benefit) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(type, other.type) && 
                        Objects.equals(allowed, other.allowed) && 
                        Objects.equals(used, other.used);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            type, 
                            allowed, 
                            used);
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

                public static class Builder extends BackboneElement.Builder {
                    private CodeableConcept type;
                    private Element allowed;
                    private Element used;

                    private Builder() {
                        super();
                    }

                    /**
                     * Unique id for the element within a resource (for internal references). This may be any string value that does not 
                     * contain spaces.
                     * 
                     * @param id
                     *     Unique id for inter-element referencing
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    @Override
                    public Builder id(java.lang.String id) {
                        return (Builder) super.id(id);
                    }

                    /**
                     * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
                     * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
                     * May be used to represent additional information that is not part of the basic definition of the element and that 
                     * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                     * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                     * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                     * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                     * extension. Applications processing a resource are required to check for modifier extensions.
                     * 
                     * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                     * change the meaning of modifierExtension itself).
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param modifierExtension
                     *     Extensions that cannot be ignored even if unrecognized
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    @Override
                    public Builder modifierExtension(Extension... modifierExtension) {
                        return (Builder) super.modifierExtension(modifierExtension);
                    }

                    /**
                     * May be used to represent additional information that is not part of the basic definition of the element and that 
                     * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                     * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                     * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                     * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                     * extension. Applications processing a resource are required to check for modifier extensions.
                     * 
                     * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                     * change the meaning of modifierExtension itself).
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param modifierExtension
                     *     Extensions that cannot be ignored even if unrecognized
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
                     * Classification of benefit being provided.
                     * 
                     * <p>This element is required.
                     * 
                     * @param type
                     *     Benefit classification
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder type(CodeableConcept type) {
                        this.type = type;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code allowed} with choice type String.
                     * 
                     * @param allowed
                     *     Benefits allowed
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #allowed(Element)
                     */
                    public Builder allowed(java.lang.String allowed) {
                        this.allowed = (allowed == null) ? null : String.of(allowed);
                        return this;
                    }

                    /**
                     * The quantity of the benefit which is permitted under the coverage.
                     * 
                     * <p>This is a choice element with the following allowed types:
                     * <ul>
                     * <li>{@link UnsignedInt}</li>
                     * <li>{@link String}</li>
                     * <li>{@link Money}</li>
                     * </ul>
                     * 
                     * @param allowed
                     *     Benefits allowed
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder allowed(Element allowed) {
                        this.allowed = allowed;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code used} with choice type String.
                     * 
                     * @param used
                     *     Benefits used
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #used(Element)
                     */
                    public Builder used(java.lang.String used) {
                        this.used = (used == null) ? null : String.of(used);
                        return this;
                    }

                    /**
                     * The quantity of the benefit which have been consumed to date.
                     * 
                     * <p>This is a choice element with the following allowed types:
                     * <ul>
                     * <li>{@link UnsignedInt}</li>
                     * <li>{@link String}</li>
                     * <li>{@link Money}</li>
                     * </ul>
                     * 
                     * @param used
                     *     Benefits used
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder used(Element used) {
                        this.used = used;
                        return this;
                    }

                    /**
                     * Build the {@link Benefit}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>type</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Benefit}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Benefit per the base specification
                     */
                    @Override
                    public Benefit build() {
                        Benefit benefit = new Benefit(this);
                        if (validating) {
                            validate(benefit);
                        }
                        return benefit;
                    }

                    protected void validate(Benefit benefit) {
                        super.validate(benefit);
                        ValidationSupport.requireNonNull(benefit.type, "type");
                        ValidationSupport.choiceElement(benefit.allowed, "allowed", UnsignedInt.class, String.class, Money.class);
                        ValidationSupport.choiceElement(benefit.used, "used", UnsignedInt.class, String.class, Money.class);
                        ValidationSupport.requireValueOrChildren(benefit);
                    }

                    protected Builder from(Benefit benefit) {
                        super.from(benefit);
                        type = benefit.type;
                        allowed = benefit.allowed;
                        used = benefit.used;
                        return this;
                    }
                }
            }
        }
    }

    /**
     * Errors encountered during the processing of the request.
     */
    public static class Error extends BackboneElement {
        @Binding(
            bindingName = "AdjudicationError",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The error codes for adjudication processing.",
            valueSet = "http://hl7.org/fhir/ValueSet/adjudication-error"
        )
        @Required
        private final CodeableConcept code;

        private Error(Builder builder) {
            super(builder);
            code = builder.code;
        }

        /**
         * An error code,from a specified code system, which details why the eligibility check could not be performed.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null);
        }

        @Override
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(code, "code", visitor);
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
            Error other = (Error) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code);
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

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept code;

            private Builder() {
                super();
            }

            /**
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * 
             * @param id
             *     Unique id for inter-element referencing
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder id(java.lang.String id) {
                return (Builder) super.id(id);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
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
             * An error code,from a specified code system, which details why the eligibility check could not be performed.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     Error code detailing processing issues
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * Build the {@link Error}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Error}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Error per the base specification
             */
            @Override
            public Error build() {
                Error error = new Error(this);
                if (validating) {
                    validate(error);
                }
                return error;
            }

            protected void validate(Error error) {
                super.validate(error);
                ValidationSupport.requireNonNull(error.code, "code");
                ValidationSupport.requireValueOrChildren(error);
            }

            protected Builder from(Error error) {
                super.from(error);
                code = error.code;
                return this;
            }
        }
    }
}

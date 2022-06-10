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
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.NoteType;
import com.ibm.fhir.model.type.code.PaymentReconciliationStatus;
import com.ibm.fhir.model.type.code.RemittanceOutcome;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * This resource provides the details including amount of a payment and allocates the payment items being paid.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class PaymentReconciliation extends DomainResource {
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "PaymentReconciliationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "A code specifying the state of the resource instance.",
        valueSet = "http://hl7.org/fhir/ValueSet/fm-status|4.3.0"
    )
    @Required
    private final PaymentReconciliationStatus status;
    @Summary
    private final Period period;
    @Summary
    @Required
    private final DateTime created;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference paymentIssuer;
    @ReferenceTarget({ "Task" })
    private final Reference request;
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
    private final Reference requestor;
    @Binding(
        bindingName = "RemittanceOutcome",
        strength = BindingStrength.Value.REQUIRED,
        description = "The outcome of the processing.",
        valueSet = "http://hl7.org/fhir/ValueSet/remittance-outcome|4.3.0"
    )
    private final RemittanceOutcome outcome;
    private final String disposition;
    @Summary
    @Required
    private final Date paymentDate;
    @Summary
    @Required
    private final Money paymentAmount;
    private final Identifier paymentIdentifier;
    private final List<Detail> detail;
    @Binding(
        bindingName = "Forms",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The forms codes.",
        valueSet = "http://hl7.org/fhir/ValueSet/forms"
    )
    private final CodeableConcept formCode;
    private final List<ProcessNote> processNote;

    private PaymentReconciliation(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        period = builder.period;
        created = builder.created;
        paymentIssuer = builder.paymentIssuer;
        request = builder.request;
        requestor = builder.requestor;
        outcome = builder.outcome;
        disposition = builder.disposition;
        paymentDate = builder.paymentDate;
        paymentAmount = builder.paymentAmount;
        paymentIdentifier = builder.paymentIdentifier;
        detail = Collections.unmodifiableList(builder.detail);
        formCode = builder.formCode;
        processNote = Collections.unmodifiableList(builder.processNote);
    }

    /**
     * A unique identifier assigned to this payment reconciliation.
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
     *     An immutable object of type {@link PaymentReconciliationStatus} that is non-null.
     */
    public PaymentReconciliationStatus getStatus() {
        return status;
    }

    /**
     * The period of time for which payments have been gathered into this bulk payment for settlement.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * The date when the resource was created.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that is non-null.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * The party who generated the payment.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPaymentIssuer() {
        return paymentIssuer;
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
     * The practitioner who is responsible for the services rendered to the patient.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRequestor() {
        return requestor;
    }

    /**
     * The outcome of a request for a reconciliation.
     * 
     * @return
     *     An immutable object of type {@link RemittanceOutcome} that may be null.
     */
    public RemittanceOutcome getOutcome() {
        return outcome;
    }

    /**
     * A human readable description of the status of the request for the reconciliation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDisposition() {
        return disposition;
    }

    /**
     * The date of payment as indicated on the financial instrument.
     * 
     * @return
     *     An immutable object of type {@link Date} that is non-null.
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * Total payment amount as indicated on the financial instrument.
     * 
     * @return
     *     An immutable object of type {@link Money} that is non-null.
     */
    public Money getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Issuer's unique identifier for the payment instrument.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getPaymentIdentifier() {
        return paymentIdentifier;
    }

    /**
     * Distribution of the payment amount for a previously acknowledged payable.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Detail} that may be empty.
     */
    public List<Detail> getDetail() {
        return detail;
    }

    /**
     * A code for the form to be used for printing the content.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getFormCode() {
        return formCode;
    }

    /**
     * A note that describes or explains the processing in a human readable form.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ProcessNote} that may be empty.
     */
    public List<ProcessNote> getProcessNote() {
        return processNote;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (period != null) || 
            (created != null) || 
            (paymentIssuer != null) || 
            (request != null) || 
            (requestor != null) || 
            (outcome != null) || 
            (disposition != null) || 
            (paymentDate != null) || 
            (paymentAmount != null) || 
            (paymentIdentifier != null) || 
            !detail.isEmpty() || 
            (formCode != null) || 
            !processNote.isEmpty();
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
                accept(period, "period", visitor);
                accept(created, "created", visitor);
                accept(paymentIssuer, "paymentIssuer", visitor);
                accept(request, "request", visitor);
                accept(requestor, "requestor", visitor);
                accept(outcome, "outcome", visitor);
                accept(disposition, "disposition", visitor);
                accept(paymentDate, "paymentDate", visitor);
                accept(paymentAmount, "paymentAmount", visitor);
                accept(paymentIdentifier, "paymentIdentifier", visitor);
                accept(detail, "detail", visitor, Detail.class);
                accept(formCode, "formCode", visitor);
                accept(processNote, "processNote", visitor, ProcessNote.class);
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
        PaymentReconciliation other = (PaymentReconciliation) obj;
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
            Objects.equals(period, other.period) && 
            Objects.equals(created, other.created) && 
            Objects.equals(paymentIssuer, other.paymentIssuer) && 
            Objects.equals(request, other.request) && 
            Objects.equals(requestor, other.requestor) && 
            Objects.equals(outcome, other.outcome) && 
            Objects.equals(disposition, other.disposition) && 
            Objects.equals(paymentDate, other.paymentDate) && 
            Objects.equals(paymentAmount, other.paymentAmount) && 
            Objects.equals(paymentIdentifier, other.paymentIdentifier) && 
            Objects.equals(detail, other.detail) && 
            Objects.equals(formCode, other.formCode) && 
            Objects.equals(processNote, other.processNote);
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
                period, 
                created, 
                paymentIssuer, 
                request, 
                requestor, 
                outcome, 
                disposition, 
                paymentDate, 
                paymentAmount, 
                paymentIdentifier, 
                detail, 
                formCode, 
                processNote);
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
        private PaymentReconciliationStatus status;
        private Period period;
        private DateTime created;
        private Reference paymentIssuer;
        private Reference request;
        private Reference requestor;
        private RemittanceOutcome outcome;
        private String disposition;
        private Date paymentDate;
        private Money paymentAmount;
        private Identifier paymentIdentifier;
        private List<Detail> detail = new ArrayList<>();
        private CodeableConcept formCode;
        private List<ProcessNote> processNote = new ArrayList<>();

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
         * A unique identifier assigned to this payment reconciliation.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for a payment reconciliation
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
         * A unique identifier assigned to this payment reconciliation.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for a payment reconciliation
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
        public Builder status(PaymentReconciliationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The period of time for which payments have been gathered into this bulk payment for settlement.
         * 
         * @param period
         *     Period covered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * The date when the resource was created.
         * 
         * <p>This element is required.
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
         * The party who generated the payment.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param paymentIssuer
         *     Party generating payment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder paymentIssuer(Reference paymentIssuer) {
            this.paymentIssuer = paymentIssuer;
            return this;
        }

        /**
         * Original request resource reference.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Task}</li>
         * </ul>
         * 
         * @param request
         *     Reference to requesting resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder request(Reference request) {
            this.request = request;
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
         * @param requestor
         *     Responsible practitioner
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder requestor(Reference requestor) {
            this.requestor = requestor;
            return this;
        }

        /**
         * The outcome of a request for a reconciliation.
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
         *     Disposition message
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
         * A human readable description of the status of the request for the reconciliation.
         * 
         * @param disposition
         *     Disposition message
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder disposition(String disposition) {
            this.disposition = disposition;
            return this;
        }

        /**
         * Convenience method for setting {@code paymentDate}.
         * 
         * <p>This element is required.
         * 
         * @param paymentDate
         *     When payment issued
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #paymentDate(com.ibm.fhir.model.type.Date)
         */
        public Builder paymentDate(java.time.LocalDate paymentDate) {
            this.paymentDate = (paymentDate == null) ? null : Date.of(paymentDate);
            return this;
        }

        /**
         * The date of payment as indicated on the financial instrument.
         * 
         * <p>This element is required.
         * 
         * @param paymentDate
         *     When payment issued
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder paymentDate(Date paymentDate) {
            this.paymentDate = paymentDate;
            return this;
        }

        /**
         * Total payment amount as indicated on the financial instrument.
         * 
         * <p>This element is required.
         * 
         * @param paymentAmount
         *     Total amount of Payment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder paymentAmount(Money paymentAmount) {
            this.paymentAmount = paymentAmount;
            return this;
        }

        /**
         * Issuer's unique identifier for the payment instrument.
         * 
         * @param paymentIdentifier
         *     Business identifier for the payment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder paymentIdentifier(Identifier paymentIdentifier) {
            this.paymentIdentifier = paymentIdentifier;
            return this;
        }

        /**
         * Distribution of the payment amount for a previously acknowledged payable.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param detail
         *     Settlement particulars
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder detail(Detail... detail) {
            for (Detail value : detail) {
                this.detail.add(value);
            }
            return this;
        }

        /**
         * Distribution of the payment amount for a previously acknowledged payable.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param detail
         *     Settlement particulars
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder detail(Collection<Detail> detail) {
            this.detail = new ArrayList<>(detail);
            return this;
        }

        /**
         * A code for the form to be used for printing the content.
         * 
         * @param formCode
         *     Printed form identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder formCode(CodeableConcept formCode) {
            this.formCode = formCode;
            return this;
        }

        /**
         * A note that describes or explains the processing in a human readable form.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param processNote
         *     Note concerning processing
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder processNote(ProcessNote... processNote) {
            for (ProcessNote value : processNote) {
                this.processNote.add(value);
            }
            return this;
        }

        /**
         * A note that describes or explains the processing in a human readable form.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param processNote
         *     Note concerning processing
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder processNote(Collection<ProcessNote> processNote) {
            this.processNote = new ArrayList<>(processNote);
            return this;
        }

        /**
         * Build the {@link PaymentReconciliation}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>created</li>
         * <li>paymentDate</li>
         * <li>paymentAmount</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link PaymentReconciliation}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid PaymentReconciliation per the base specification
         */
        @Override
        public PaymentReconciliation build() {
            PaymentReconciliation paymentReconciliation = new PaymentReconciliation(this);
            if (validating) {
                validate(paymentReconciliation);
            }
            return paymentReconciliation;
        }

        protected void validate(PaymentReconciliation paymentReconciliation) {
            super.validate(paymentReconciliation);
            ValidationSupport.checkList(paymentReconciliation.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(paymentReconciliation.status, "status");
            ValidationSupport.requireNonNull(paymentReconciliation.created, "created");
            ValidationSupport.requireNonNull(paymentReconciliation.paymentDate, "paymentDate");
            ValidationSupport.requireNonNull(paymentReconciliation.paymentAmount, "paymentAmount");
            ValidationSupport.checkList(paymentReconciliation.detail, "detail", Detail.class);
            ValidationSupport.checkList(paymentReconciliation.processNote, "processNote", ProcessNote.class);
            ValidationSupport.checkReferenceType(paymentReconciliation.paymentIssuer, "paymentIssuer", "Organization");
            ValidationSupport.checkReferenceType(paymentReconciliation.request, "request", "Task");
            ValidationSupport.checkReferenceType(paymentReconciliation.requestor, "requestor", "Practitioner", "PractitionerRole", "Organization");
        }

        protected Builder from(PaymentReconciliation paymentReconciliation) {
            super.from(paymentReconciliation);
            identifier.addAll(paymentReconciliation.identifier);
            status = paymentReconciliation.status;
            period = paymentReconciliation.period;
            created = paymentReconciliation.created;
            paymentIssuer = paymentReconciliation.paymentIssuer;
            request = paymentReconciliation.request;
            requestor = paymentReconciliation.requestor;
            outcome = paymentReconciliation.outcome;
            disposition = paymentReconciliation.disposition;
            paymentDate = paymentReconciliation.paymentDate;
            paymentAmount = paymentReconciliation.paymentAmount;
            paymentIdentifier = paymentReconciliation.paymentIdentifier;
            detail.addAll(paymentReconciliation.detail);
            formCode = paymentReconciliation.formCode;
            processNote.addAll(paymentReconciliation.processNote);
            return this;
        }
    }

    /**
     * Distribution of the payment amount for a previously acknowledged payable.
     */
    public static class Detail extends BackboneElement {
        private final Identifier identifier;
        private final Identifier predecessor;
        @Binding(
            bindingName = "PaymentType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The reason for the amount: payment, adjustment, advance.",
            valueSet = "http://hl7.org/fhir/ValueSet/payment-type"
        )
        @Required
        private final CodeableConcept type;
        private final Reference request;
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
        private final Reference submitter;
        private final Reference response;
        private final Date date;
        @ReferenceTarget({ "PractitionerRole" })
        private final Reference responsible;
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
        private final Reference payee;
        private final Money amount;

        private Detail(Builder builder) {
            super(builder);
            identifier = builder.identifier;
            predecessor = builder.predecessor;
            type = builder.type;
            request = builder.request;
            submitter = builder.submitter;
            response = builder.response;
            date = builder.date;
            responsible = builder.responsible;
            payee = builder.payee;
            amount = builder.amount;
        }

        /**
         * Unique identifier for the current payment item for the referenced payable.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        /**
         * Unique identifier for the prior payment item for the referenced payable.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getPredecessor() {
            return predecessor;
        }

        /**
         * Code to indicate the nature of the payment.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * A resource, such as a Claim, the evaluation of which could lead to payment.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getRequest() {
            return request;
        }

        /**
         * The party which submitted the claim or financial transaction.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getSubmitter() {
            return submitter;
        }

        /**
         * A resource, such as a ClaimResponse, which contains a commitment to payment.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getResponse() {
            return response;
        }

        /**
         * The date from the response resource containing a commitment to pay.
         * 
         * @return
         *     An immutable object of type {@link Date} that may be null.
         */
        public Date getDate() {
            return date;
        }

        /**
         * A reference to the individual who is responsible for inquiries regarding the response and its payment.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getResponsible() {
            return responsible;
        }

        /**
         * The party which is receiving the payment.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getPayee() {
            return payee;
        }

        /**
         * The monetary amount allocated from the total payment to the payable.
         * 
         * @return
         *     An immutable object of type {@link Money} that may be null.
         */
        public Money getAmount() {
            return amount;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (identifier != null) || 
                (predecessor != null) || 
                (type != null) || 
                (request != null) || 
                (submitter != null) || 
                (response != null) || 
                (date != null) || 
                (responsible != null) || 
                (payee != null) || 
                (amount != null);
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
                    accept(identifier, "identifier", visitor);
                    accept(predecessor, "predecessor", visitor);
                    accept(type, "type", visitor);
                    accept(request, "request", visitor);
                    accept(submitter, "submitter", visitor);
                    accept(response, "response", visitor);
                    accept(date, "date", visitor);
                    accept(responsible, "responsible", visitor);
                    accept(payee, "payee", visitor);
                    accept(amount, "amount", visitor);
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
            Detail other = (Detail) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(predecessor, other.predecessor) && 
                Objects.equals(type, other.type) && 
                Objects.equals(request, other.request) && 
                Objects.equals(submitter, other.submitter) && 
                Objects.equals(response, other.response) && 
                Objects.equals(date, other.date) && 
                Objects.equals(responsible, other.responsible) && 
                Objects.equals(payee, other.payee) && 
                Objects.equals(amount, other.amount);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    identifier, 
                    predecessor, 
                    type, 
                    request, 
                    submitter, 
                    response, 
                    date, 
                    responsible, 
                    payee, 
                    amount);
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
            private Identifier identifier;
            private Identifier predecessor;
            private CodeableConcept type;
            private Reference request;
            private Reference submitter;
            private Reference response;
            private Date date;
            private Reference responsible;
            private Reference payee;
            private Money amount;

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
             * Unique identifier for the current payment item for the referenced payable.
             * 
             * @param identifier
             *     Business identifier of the payment detail
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identifier(Identifier identifier) {
                this.identifier = identifier;
                return this;
            }

            /**
             * Unique identifier for the prior payment item for the referenced payable.
             * 
             * @param predecessor
             *     Business identifier of the prior payment detail
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder predecessor(Identifier predecessor) {
                this.predecessor = predecessor;
                return this;
            }

            /**
             * Code to indicate the nature of the payment.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     Category of payment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * A resource, such as a Claim, the evaluation of which could lead to payment.
             * 
             * @param request
             *     Request giving rise to the payment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder request(Reference request) {
                this.request = request;
                return this;
            }

            /**
             * The party which submitted the claim or financial transaction.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param submitter
             *     Submitter of the request
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder submitter(Reference submitter) {
                this.submitter = submitter;
                return this;
            }

            /**
             * A resource, such as a ClaimResponse, which contains a commitment to payment.
             * 
             * @param response
             *     Response committing to a payment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder response(Reference response) {
                this.response = response;
                return this;
            }

            /**
             * Convenience method for setting {@code date}.
             * 
             * @param date
             *     Date of commitment to pay
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #date(com.ibm.fhir.model.type.Date)
             */
            public Builder date(java.time.LocalDate date) {
                this.date = (date == null) ? null : Date.of(date);
                return this;
            }

            /**
             * The date from the response resource containing a commitment to pay.
             * 
             * @param date
             *     Date of commitment to pay
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder date(Date date) {
                this.date = date;
                return this;
            }

            /**
             * A reference to the individual who is responsible for inquiries regarding the response and its payment.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link PractitionerRole}</li>
             * </ul>
             * 
             * @param responsible
             *     Contact for the response
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder responsible(Reference responsible) {
                this.responsible = responsible;
                return this;
            }

            /**
             * The party which is receiving the payment.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param payee
             *     Recipient of the payment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder payee(Reference payee) {
                this.payee = payee;
                return this;
            }

            /**
             * The monetary amount allocated from the total payment to the payable.
             * 
             * @param amount
             *     Amount allocated to this payable
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amount(Money amount) {
                this.amount = amount;
                return this;
            }

            /**
             * Build the {@link Detail}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Detail}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Detail per the base specification
             */
            @Override
            public Detail build() {
                Detail detail = new Detail(this);
                if (validating) {
                    validate(detail);
                }
                return detail;
            }

            protected void validate(Detail detail) {
                super.validate(detail);
                ValidationSupport.requireNonNull(detail.type, "type");
                ValidationSupport.checkReferenceType(detail.submitter, "submitter", "Practitioner", "PractitionerRole", "Organization");
                ValidationSupport.checkReferenceType(detail.responsible, "responsible", "PractitionerRole");
                ValidationSupport.checkReferenceType(detail.payee, "payee", "Practitioner", "PractitionerRole", "Organization");
                ValidationSupport.requireValueOrChildren(detail);
            }

            protected Builder from(Detail detail) {
                super.from(detail);
                identifier = detail.identifier;
                predecessor = detail.predecessor;
                type = detail.type;
                request = detail.request;
                submitter = detail.submitter;
                response = detail.response;
                date = detail.date;
                responsible = detail.responsible;
                payee = detail.payee;
                amount = detail.amount;
                return this;
            }
        }
    }

    /**
     * A note that describes or explains the processing in a human readable form.
     */
    public static class ProcessNote extends BackboneElement {
        @Binding(
            bindingName = "NoteType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The presentation types of notes.",
            valueSet = "http://hl7.org/fhir/ValueSet/note-type|4.3.0"
        )
        private final NoteType type;
        private final String text;

        private ProcessNote(Builder builder) {
            super(builder);
            type = builder.type;
            text = builder.text;
        }

        /**
         * The business purpose of the note text.
         * 
         * @return
         *     An immutable object of type {@link NoteType} that may be null.
         */
        public NoteType getType() {
            return type;
        }

        /**
         * The explanation or description associated with the processing.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getText() {
            return text;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (text != null);
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
                    accept(text, "text", visitor);
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
            ProcessNote other = (ProcessNote) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(text, other.text);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    text);
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
            private NoteType type;
            private String text;

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
             * The business purpose of the note text.
             * 
             * @param type
             *     display | print | printoper
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(NoteType type) {
                this.type = type;
                return this;
            }

            /**
             * Convenience method for setting {@code text}.
             * 
             * @param text
             *     Note explanatory text
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #text(com.ibm.fhir.model.type.String)
             */
            public Builder text(java.lang.String text) {
                this.text = (text == null) ? null : String.of(text);
                return this;
            }

            /**
             * The explanation or description associated with the processing.
             * 
             * @param text
             *     Note explanatory text
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder text(String text) {
                this.text = text;
                return this;
            }

            /**
             * Build the {@link ProcessNote}
             * 
             * @return
             *     An immutable object of type {@link ProcessNote}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid ProcessNote per the base specification
             */
            @Override
            public ProcessNote build() {
                ProcessNote processNote = new ProcessNote(this);
                if (validating) {
                    validate(processNote);
                }
                return processNote;
            }

            protected void validate(ProcessNote processNote) {
                super.validate(processNote);
                ValidationSupport.requireValueOrChildren(processNote);
            }

            protected Builder from(ProcessNote processNote) {
                super.from(processNote);
                type = processNote.type;
                text = processNote.text;
                return this;
            }
        }
    }
}

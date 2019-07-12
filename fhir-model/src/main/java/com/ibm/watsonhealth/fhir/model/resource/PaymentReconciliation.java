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

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Money;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.NoteType;
import com.ibm.watsonhealth.fhir.model.type.PaymentReconciliationStatus;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.RemittanceOutcome;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * This resource provides the details including amount of a payment and allocates the payment items being paid.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class PaymentReconciliation extends DomainResource {
    private final List<Identifier> identifier;
    private final PaymentReconciliationStatus status;
    private final Period period;
    private final DateTime created;
    private final Reference paymentIssuer;
    private final Reference request;
    private final Reference requestor;
    private final RemittanceOutcome outcome;
    private final String disposition;
    private final Date paymentDate;
    private final Money paymentAmount;
    private final Identifier paymentIdentifier;
    private final List<Detail> detail;
    private final CodeableConcept formCode;
    private final List<ProcessNote> processNote;

    private volatile int hashCode;

    private PaymentReconciliation(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        period = builder.period;
        created = ValidationSupport.requireNonNull(builder.created, "created");
        paymentIssuer = builder.paymentIssuer;
        request = builder.request;
        requestor = builder.requestor;
        outcome = builder.outcome;
        disposition = builder.disposition;
        paymentDate = ValidationSupport.requireNonNull(builder.paymentDate, "paymentDate");
        paymentAmount = ValidationSupport.requireNonNull(builder.paymentAmount, "paymentAmount");
        paymentIdentifier = builder.paymentIdentifier;
        detail = Collections.unmodifiableList(builder.detail);
        formCode = builder.formCode;
        processNote = Collections.unmodifiableList(builder.processNote);
    }

    /**
     * <p>
     * A unique identifier assigned to this payment reconciliation.
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
     * The status of the resource instance.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PaymentReconciliationStatus}.
     */
    public PaymentReconciliationStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The period of time for which payments have been gathered into this bulk payment for settlement.
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
     * The date when the resource was created.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * <p>
     * The party who generated the payment.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPaymentIssuer() {
        return paymentIssuer;
    }

    /**
     * <p>
     * Original request resource reference.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getRequest() {
        return request;
    }

    /**
     * <p>
     * The practitioner who is responsible for the services rendered to the patient.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getRequestor() {
        return requestor;
    }

    /**
     * <p>
     * The outcome of a request for a reconciliation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link RemittanceOutcome}.
     */
    public RemittanceOutcome getOutcome() {
        return outcome;
    }

    /**
     * <p>
     * A human readable description of the status of the request for the reconciliation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getDisposition() {
        return disposition;
    }

    /**
     * <p>
     * The date of payment as indicated on the financial instrument.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Date}.
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * <p>
     * Total payment amount as indicated on the financial instrument.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Money}.
     */
    public Money getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * <p>
     * Issuer's unique identifier for the payment instrument.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Identifier}.
     */
    public Identifier getPaymentIdentifier() {
        return paymentIdentifier;
    }

    /**
     * <p>
     * Distribution of the payment amount for a previously acknowledged payable.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Detail}.
     */
    public List<Detail> getDetail() {
        return detail;
    }

    /**
     * <p>
     * A code for the form to be used for printing the content.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getFormCode() {
        return formCode;
    }

    /**
     * <p>
     * A note that describes or explains the processing in a human readable form.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ProcessNote}.
     */
    public List<ProcessNote> getProcessNote() {
        return processNote;
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
        return new Builder(status, created, paymentDate, paymentAmount).from(this);
    }

    public Builder toBuilder(PaymentReconciliationStatus status, DateTime created, Date paymentDate, Money paymentAmount) {
        return new Builder(status, created, paymentDate, paymentAmount).from(this);
    }

    public static Builder builder(PaymentReconciliationStatus status, DateTime created, Date paymentDate, Money paymentAmount) {
        return new Builder(status, created, paymentDate, paymentAmount);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final PaymentReconciliationStatus status;
        private final DateTime created;
        private final Date paymentDate;
        private final Money paymentAmount;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private Period period;
        private Reference paymentIssuer;
        private Reference request;
        private Reference requestor;
        private RemittanceOutcome outcome;
        private String disposition;
        private Identifier paymentIdentifier;
        private List<Detail> detail = new ArrayList<>();
        private CodeableConcept formCode;
        private List<ProcessNote> processNote = new ArrayList<>();

        private Builder(PaymentReconciliationStatus status, DateTime created, Date paymentDate, Money paymentAmount) {
            super();
            this.status = status;
            this.created = created;
            this.paymentDate = paymentDate;
            this.paymentAmount = paymentAmount;
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * A unique identifier assigned to this payment reconciliation.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A unique identifier assigned to this payment reconciliation.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Business Identifier for a payment reconciliation
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
         * The period of time for which payments have been gathered into this bulk payment for settlement.
         * </p>
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
         * <p>
         * The party who generated the payment.
         * </p>
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
         * <p>
         * Original request resource reference.
         * </p>
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
         * <p>
         * The practitioner who is responsible for the services rendered to the patient.
         * </p>
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
         * <p>
         * The outcome of a request for a reconciliation.
         * </p>
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
         * <p>
         * A human readable description of the status of the request for the reconciliation.
         * </p>
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
         * <p>
         * Issuer's unique identifier for the payment instrument.
         * </p>
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
         * <p>
         * Distribution of the payment amount for a previously acknowledged payable.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Distribution of the payment amount for a previously acknowledged payable.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param detail
         *     Settlement particulars
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder detail(Collection<Detail> detail) {
            this.detail = new ArrayList<>(detail);
            return this;
        }

        /**
         * <p>
         * A code for the form to be used for printing the content.
         * </p>
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
         * <p>
         * A note that describes or explains the processing in a human readable form.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A note that describes or explains the processing in a human readable form.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param processNote
         *     Note concerning processing
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder processNote(Collection<ProcessNote> processNote) {
            this.processNote = new ArrayList<>(processNote);
            return this;
        }

        @Override
        public PaymentReconciliation build() {
            return new PaymentReconciliation(this);
        }

        private Builder from(PaymentReconciliation paymentReconciliation) {
            id = paymentReconciliation.id;
            meta = paymentReconciliation.meta;
            implicitRules = paymentReconciliation.implicitRules;
            language = paymentReconciliation.language;
            text = paymentReconciliation.text;
            contained.addAll(paymentReconciliation.contained);
            extension.addAll(paymentReconciliation.extension);
            modifierExtension.addAll(paymentReconciliation.modifierExtension);
            identifier.addAll(paymentReconciliation.identifier);
            period = paymentReconciliation.period;
            paymentIssuer = paymentReconciliation.paymentIssuer;
            request = paymentReconciliation.request;
            requestor = paymentReconciliation.requestor;
            outcome = paymentReconciliation.outcome;
            disposition = paymentReconciliation.disposition;
            paymentIdentifier = paymentReconciliation.paymentIdentifier;
            detail.addAll(paymentReconciliation.detail);
            formCode = paymentReconciliation.formCode;
            processNote.addAll(paymentReconciliation.processNote);
            return this;
        }
    }

    /**
     * <p>
     * Distribution of the payment amount for a previously acknowledged payable.
     * </p>
     */
    public static class Detail extends BackboneElement {
        private final Identifier identifier;
        private final Identifier predecessor;
        private final CodeableConcept type;
        private final Reference request;
        private final Reference submitter;
        private final Reference response;
        private final Date date;
        private final Reference responsible;
        private final Reference payee;
        private final Money amount;

        private volatile int hashCode;

        private Detail(Builder builder) {
            super(builder);
            identifier = builder.identifier;
            predecessor = builder.predecessor;
            type = ValidationSupport.requireNonNull(builder.type, "type");
            request = builder.request;
            submitter = builder.submitter;
            response = builder.response;
            date = builder.date;
            responsible = builder.responsible;
            payee = builder.payee;
            amount = builder.amount;
        }

        /**
         * <p>
         * Unique identifier for the current payment item for the referenced payable.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Identifier}.
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        /**
         * <p>
         * Unique identifier for the prior payment item for the referenced payable.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Identifier}.
         */
        public Identifier getPredecessor() {
            return predecessor;
        }

        /**
         * <p>
         * Code to indicate the nature of the payment.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * <p>
         * A resource, such as a Claim, the evaluation of which could lead to payment.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getRequest() {
            return request;
        }

        /**
         * <p>
         * The party which submitted the claim or financial transaction.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getSubmitter() {
            return submitter;
        }

        /**
         * <p>
         * A resource, such as a ClaimResponse, which contains a commitment to payment.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getResponse() {
            return response;
        }

        /**
         * <p>
         * The date from the response resource containing a commitment to pay.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Date}.
         */
        public Date getDate() {
            return date;
        }

        /**
         * <p>
         * A reference to the individual who is responsible for inquiries regarding the response and its payment.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getResponsible() {
            return responsible;
        }

        /**
         * <p>
         * The party which is receiving the payment.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getPayee() {
            return payee;
        }

        /**
         * <p>
         * The monetary amount allocated from the total payment to the payable.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Money}.
         */
        public Money getAmount() {
            return amount;
        }

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
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
            return new Builder(type).from(this);
        }

        public Builder toBuilder(CodeableConcept type) {
            return new Builder(type).from(this);
        }

        public static Builder builder(CodeableConcept type) {
            return new Builder(type);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept type;

            // optional
            private Identifier identifier;
            private Identifier predecessor;
            private Reference request;
            private Reference submitter;
            private Reference response;
            private Date date;
            private Reference responsible;
            private Reference payee;
            private Money amount;

            private Builder(CodeableConcept type) {
                super();
                this.type = type;
            }

            /**
             * <p>
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * </p>
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
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
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
             *     Extensions that cannot be ignored even if unrecognized
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
             * Unique identifier for the current payment item for the referenced payable.
             * </p>
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
             * <p>
             * Unique identifier for the prior payment item for the referenced payable.
             * </p>
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
             * <p>
             * A resource, such as a Claim, the evaluation of which could lead to payment.
             * </p>
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
             * <p>
             * The party which submitted the claim or financial transaction.
             * </p>
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
             * <p>
             * A resource, such as a ClaimResponse, which contains a commitment to payment.
             * </p>
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
             * <p>
             * The date from the response resource containing a commitment to pay.
             * </p>
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
             * <p>
             * A reference to the individual who is responsible for inquiries regarding the response and its payment.
             * </p>
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
             * <p>
             * The party which is receiving the payment.
             * </p>
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
             * <p>
             * The monetary amount allocated from the total payment to the payable.
             * </p>
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

            @Override
            public Detail build() {
                return new Detail(this);
            }

            private Builder from(Detail detail) {
                id = detail.id;
                extension.addAll(detail.extension);
                modifierExtension.addAll(detail.modifierExtension);
                identifier = detail.identifier;
                predecessor = detail.predecessor;
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
     * <p>
     * A note that describes or explains the processing in a human readable form.
     * </p>
     */
    public static class ProcessNote extends BackboneElement {
        private final NoteType type;
        private final String text;

        private volatile int hashCode;

        private ProcessNote(Builder builder) {
            super(builder);
            type = builder.type;
            text = builder.text;
        }

        /**
         * <p>
         * The business purpose of the note text.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link NoteType}.
         */
        public NoteType getType() {
            return type;
        }

        /**
         * <p>
         * The explanation or description associated with the processing.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getText() {
            return text;
        }

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(type, "type", visitor);
                    accept(text, "text", visitor);
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
            // optional
            private NoteType type;
            private String text;

            private Builder() {
                super();
            }

            /**
             * <p>
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * </p>
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
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
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
             *     Extensions that cannot be ignored even if unrecognized
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
             * The business purpose of the note text.
             * </p>
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
             * <p>
             * The explanation or description associated with the processing.
             * </p>
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

            @Override
            public ProcessNote build() {
                return new ProcessNote(this);
            }

            private Builder from(ProcessNote processNote) {
                id = processNote.id;
                extension.addAll(processNote.extension);
                modifierExtension.addAll(processNote.modifierExtension);
                type = processNote.type;
                text = processNote.text;
                return this;
            }
        }
    }
}

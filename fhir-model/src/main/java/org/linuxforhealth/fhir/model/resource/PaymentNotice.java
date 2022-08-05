/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import org.linuxforhealth.fhir.model.annotation.Binding;
import org.linuxforhealth.fhir.model.annotation.Maturity;
import org.linuxforhealth.fhir.model.annotation.ReferenceTarget;
import org.linuxforhealth.fhir.model.annotation.Required;
import org.linuxforhealth.fhir.model.annotation.Summary;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Date;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Identifier;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Money;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.BindingStrength;
import org.linuxforhealth.fhir.model.type.code.PaymentNoticeStatus;
import org.linuxforhealth.fhir.model.type.code.StandardsStatus;
import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.model.visitor.Visitor;

/**
 * This resource provides the status of the payment for goods and services rendered, and the request and response 
 * resource references.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class PaymentNotice extends DomainResource {
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "PaymentNoticeStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "A code specifying the state of the resource instance.",
        valueSet = "http://hl7.org/fhir/ValueSet/fm-status|4.3.0"
    )
    @Required
    private final PaymentNoticeStatus status;
    private final Reference request;
    private final Reference response;
    @Summary
    @Required
    private final DateTime created;
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
    private final Reference provider;
    @Summary
    @ReferenceTarget({ "PaymentReconciliation" })
    @Required
    private final Reference payment;
    private final Date paymentDate;
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
    private final Reference payee;
    @Summary
    @ReferenceTarget({ "Organization" })
    @Required
    private final Reference recipient;
    @Summary
    @Required
    private final Money amount;
    @Binding(
        bindingName = "PaymentStatus",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The payment conveyance status codes.",
        valueSet = "http://hl7.org/fhir/ValueSet/payment-status"
    )
    private final CodeableConcept paymentStatus;

    private PaymentNotice(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        request = builder.request;
        response = builder.response;
        created = builder.created;
        provider = builder.provider;
        payment = builder.payment;
        paymentDate = builder.paymentDate;
        payee = builder.payee;
        recipient = builder.recipient;
        amount = builder.amount;
        paymentStatus = builder.paymentStatus;
    }

    /**
     * A unique identifier assigned to this payment notice.
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
     *     An immutable object of type {@link PaymentNoticeStatus} that is non-null.
     */
    public PaymentNoticeStatus getStatus() {
        return status;
    }

    /**
     * Reference of resource for which payment is being made.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRequest() {
        return request;
    }

    /**
     * Reference of response to resource for which payment is being made.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getResponse() {
        return response;
    }

    /**
     * The date when this resource was created.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that is non-null.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getProvider() {
        return provider;
    }

    /**
     * A reference to the payment which is the subject of this notice.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getPayment() {
        return payment;
    }

    /**
     * The date when the above payment action occurred.
     * 
     * @return
     *     An immutable object of type {@link Date} that may be null.
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * The party who will receive or has received payment that is the subject of this notification.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPayee() {
        return payee;
    }

    /**
     * The party who is notified of the payment status.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getRecipient() {
        return recipient;
    }

    /**
     * The amount sent to the payee.
     * 
     * @return
     *     An immutable object of type {@link Money} that is non-null.
     */
    public Money getAmount() {
        return amount;
    }

    /**
     * A code indicating whether payment has been sent or cleared.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getPaymentStatus() {
        return paymentStatus;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (request != null) || 
            (response != null) || 
            (created != null) || 
            (provider != null) || 
            (payment != null) || 
            (paymentDate != null) || 
            (payee != null) || 
            (recipient != null) || 
            (amount != null) || 
            (paymentStatus != null);
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
                accept(response, "response", visitor);
                accept(created, "created", visitor);
                accept(provider, "provider", visitor);
                accept(payment, "payment", visitor);
                accept(paymentDate, "paymentDate", visitor);
                accept(payee, "payee", visitor);
                accept(recipient, "recipient", visitor);
                accept(amount, "amount", visitor);
                accept(paymentStatus, "paymentStatus", visitor);
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
        PaymentNotice other = (PaymentNotice) obj;
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
            Objects.equals(response, other.response) && 
            Objects.equals(created, other.created) && 
            Objects.equals(provider, other.provider) && 
            Objects.equals(payment, other.payment) && 
            Objects.equals(paymentDate, other.paymentDate) && 
            Objects.equals(payee, other.payee) && 
            Objects.equals(recipient, other.recipient) && 
            Objects.equals(amount, other.amount) && 
            Objects.equals(paymentStatus, other.paymentStatus);
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
                response, 
                created, 
                provider, 
                payment, 
                paymentDate, 
                payee, 
                recipient, 
                amount, 
                paymentStatus);
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
        private PaymentNoticeStatus status;
        private Reference request;
        private Reference response;
        private DateTime created;
        private Reference provider;
        private Reference payment;
        private Date paymentDate;
        private Reference payee;
        private Reference recipient;
        private Money amount;
        private CodeableConcept paymentStatus;

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
         * A unique identifier assigned to this payment notice.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for the payment noctice
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
         * A unique identifier assigned to this payment notice.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for the payment noctice
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
        public Builder status(PaymentNoticeStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Reference of resource for which payment is being made.
         * 
         * @param request
         *     Request reference
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder request(Reference request) {
            this.request = request;
            return this;
        }

        /**
         * Reference of response to resource for which payment is being made.
         * 
         * @param response
         *     Response reference
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder response(Reference response) {
            this.response = response;
            return this;
        }

        /**
         * The date when this resource was created.
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
         * The practitioner who is responsible for the services rendered to the patient.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param provider
         *     Responsible practitioner
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder provider(Reference provider) {
            this.provider = provider;
            return this;
        }

        /**
         * A reference to the payment which is the subject of this notice.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link PaymentReconciliation}</li>
         * </ul>
         * 
         * @param payment
         *     Payment reference
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder payment(Reference payment) {
            this.payment = payment;
            return this;
        }

        /**
         * Convenience method for setting {@code paymentDate}.
         * 
         * @param paymentDate
         *     Payment or clearing date
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #paymentDate(org.linuxforhealth.fhir.model.type.Date)
         */
        public Builder paymentDate(java.time.LocalDate paymentDate) {
            this.paymentDate = (paymentDate == null) ? null : Date.of(paymentDate);
            return this;
        }

        /**
         * The date when the above payment action occurred.
         * 
         * @param paymentDate
         *     Payment or clearing date
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder paymentDate(Date paymentDate) {
            this.paymentDate = paymentDate;
            return this;
        }

        /**
         * The party who will receive or has received payment that is the subject of this notification.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param payee
         *     Party being paid
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder payee(Reference payee) {
            this.payee = payee;
            return this;
        }

        /**
         * The party who is notified of the payment status.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param recipient
         *     Party being notified
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recipient(Reference recipient) {
            this.recipient = recipient;
            return this;
        }

        /**
         * The amount sent to the payee.
         * 
         * <p>This element is required.
         * 
         * @param amount
         *     Monetary amount of the payment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder amount(Money amount) {
            this.amount = amount;
            return this;
        }

        /**
         * A code indicating whether payment has been sent or cleared.
         * 
         * @param paymentStatus
         *     Issued or cleared Status of the payment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder paymentStatus(CodeableConcept paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        /**
         * Build the {@link PaymentNotice}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>created</li>
         * <li>payment</li>
         * <li>recipient</li>
         * <li>amount</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link PaymentNotice}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid PaymentNotice per the base specification
         */
        @Override
        public PaymentNotice build() {
            PaymentNotice paymentNotice = new PaymentNotice(this);
            if (validating) {
                validate(paymentNotice);
            }
            return paymentNotice;
        }

        protected void validate(PaymentNotice paymentNotice) {
            super.validate(paymentNotice);
            ValidationSupport.checkList(paymentNotice.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(paymentNotice.status, "status");
            ValidationSupport.requireNonNull(paymentNotice.created, "created");
            ValidationSupport.requireNonNull(paymentNotice.payment, "payment");
            ValidationSupport.requireNonNull(paymentNotice.recipient, "recipient");
            ValidationSupport.requireNonNull(paymentNotice.amount, "amount");
            ValidationSupport.checkReferenceType(paymentNotice.provider, "provider", "Practitioner", "PractitionerRole", "Organization");
            ValidationSupport.checkReferenceType(paymentNotice.payment, "payment", "PaymentReconciliation");
            ValidationSupport.checkReferenceType(paymentNotice.payee, "payee", "Practitioner", "PractitionerRole", "Organization");
            ValidationSupport.checkReferenceType(paymentNotice.recipient, "recipient", "Organization");
        }

        protected Builder from(PaymentNotice paymentNotice) {
            super.from(paymentNotice);
            identifier.addAll(paymentNotice.identifier);
            status = paymentNotice.status;
            request = paymentNotice.request;
            response = paymentNotice.response;
            created = paymentNotice.created;
            provider = paymentNotice.provider;
            payment = paymentNotice.payment;
            paymentDate = paymentNotice.paymentDate;
            payee = paymentNotice.payee;
            recipient = paymentNotice.recipient;
            amount = paymentNotice.amount;
            paymentStatus = paymentNotice.paymentStatus;
            return this;
        }
    }
}

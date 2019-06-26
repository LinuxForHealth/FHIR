/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Generated;

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
import com.ibm.watsonhealth.fhir.model.type.PaymentNoticeStatus;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * This resource provides the status of the payment for goods and services rendered, and the request and response 
 * resource references.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class PaymentNotice extends DomainResource {
    private final List<Identifier> identifier;
    private final PaymentNoticeStatus status;
    private final Reference request;
    private final Reference response;
    private final DateTime created;
    private final Reference provider;
    private final Reference payment;
    private final Date paymentDate;
    private final Reference payee;
    private final Reference recipient;
    private final Money amount;
    private final CodeableConcept paymentStatus;

    private PaymentNotice(Builder builder) {
        super(builder);
        this.identifier = builder.identifier;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.request = builder.request;
        this.response = builder.response;
        this.created = ValidationSupport.requireNonNull(builder.created, "created");
        this.provider = builder.provider;
        this.payment = ValidationSupport.requireNonNull(builder.payment, "payment");
        this.paymentDate = builder.paymentDate;
        this.payee = builder.payee;
        this.recipient = ValidationSupport.requireNonNull(builder.recipient, "recipient");
        this.amount = ValidationSupport.requireNonNull(builder.amount, "amount");
        this.paymentStatus = builder.paymentStatus;
    }

    /**
     * <p>
     * A unique identifier assigned to this payment notice.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Identifier}.
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
     *     An immutable object of type {@link PaymentNoticeStatus}.
     */
    public PaymentNoticeStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Reference of resource for which payment is being made.
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
     * Reference of response to resource for which payment is being made.
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
     * The date when this resource was created.
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
     * The practitioner who is responsible for the services rendered to the patient.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getProvider() {
        return provider;
    }

    /**
     * <p>
     * A reference to the payment which is the subject of this notice.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPayment() {
        return payment;
    }

    /**
     * <p>
     * The date when the above payment action occurred.
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
     * The party who will receive or has received payment that is the subject of this notification.
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
     * The party who is notified of the payment status.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getRecipient() {
        return recipient;
    }

    /**
     * <p>
     * The amount sent to the payee.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Money}.
     */
    public Money getAmount() {
        return amount;
    }

    /**
     * <p>
     * A code indicating whether payment has been sent or cleared.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getPaymentStatus() {
        return paymentStatus;
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, created, payment, recipient, amount);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.request = request;
        builder.response = response;
        builder.provider = provider;
        builder.paymentDate = paymentDate;
        builder.payee = payee;
        builder.paymentStatus = paymentStatus;
        return builder;
    }

    public static Builder builder(PaymentNoticeStatus status, DateTime created, Reference payment, Reference recipient, Money amount) {
        return new Builder(status, created, payment, recipient, amount);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final PaymentNoticeStatus status;
        private final DateTime created;
        private final Reference payment;
        private final Reference recipient;
        private final Money amount;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private Reference request;
        private Reference response;
        private Reference provider;
        private Date paymentDate;
        private Reference payee;
        private CodeableConcept paymentStatus;

        private Builder(PaymentNoticeStatus status, DateTime created, Reference payment, Reference recipient, Money amount) {
            super();
            this.status = status;
            this.created = created;
            this.payment = payment;
            this.recipient = recipient;
            this.amount = amount;
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * A unique identifier assigned to this payment notice.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for the payment noctice
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A unique identifier assigned to this payment notice.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for the payment noctice
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier.addAll(identifier);
            return this;
        }

        /**
         * <p>
         * Reference of resource for which payment is being made.
         * </p>
         * 
         * @param request
         *     Request reference
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder request(Reference request) {
            this.request = request;
            return this;
        }

        /**
         * <p>
         * Reference of response to resource for which payment is being made.
         * </p>
         * 
         * @param response
         *     Response reference
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder response(Reference response) {
            this.response = response;
            return this;
        }

        /**
         * <p>
         * The practitioner who is responsible for the services rendered to the patient.
         * </p>
         * 
         * @param provider
         *     Responsible practitioner
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder provider(Reference provider) {
            this.provider = provider;
            return this;
        }

        /**
         * <p>
         * The date when the above payment action occurred.
         * </p>
         * 
         * @param paymentDate
         *     Payment or clearing date
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder paymentDate(Date paymentDate) {
            this.paymentDate = paymentDate;
            return this;
        }

        /**
         * <p>
         * The party who will receive or has received payment that is the subject of this notification.
         * </p>
         * 
         * @param payee
         *     Party being paid
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder payee(Reference payee) {
            this.payee = payee;
            return this;
        }

        /**
         * <p>
         * A code indicating whether payment has been sent or cleared.
         * </p>
         * 
         * @param paymentStatus
         *     Issued or cleared Status of the payment
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder paymentStatus(CodeableConcept paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        @Override
        public PaymentNotice build() {
            return new PaymentNotice(this);
        }
    }
}

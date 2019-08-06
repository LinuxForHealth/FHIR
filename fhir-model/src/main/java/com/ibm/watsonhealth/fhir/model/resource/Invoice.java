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

import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.InvoicePriceComponentType;
import com.ibm.watsonhealth.fhir.model.type.InvoiceStatus;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Money;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing 
 * purpose.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Invoice extends DomainResource {
    private final List<Identifier> identifier;
    private final InvoiceStatus status;
    private final String cancelledReason;
    private final CodeableConcept type;
    private final Reference subject;
    private final Reference recipient;
    private final DateTime date;
    private final List<Participant> participant;
    private final Reference issuer;
    private final Reference account;
    private final List<LineItem> lineItem;
    private final List<Invoice.LineItem.PriceComponent> totalPriceComponent;
    private final Money totalNet;
    private final Money totalGross;
    private final Markdown paymentTerms;
    private final List<Annotation> note;

    private volatile int hashCode;

    private Invoice(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        status = ValidationSupport.requireNonNull(builder.status, "status");
        cancelledReason = builder.cancelledReason;
        type = builder.type;
        subject = builder.subject;
        recipient = builder.recipient;
        date = builder.date;
        participant = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.participant, "participant"));
        issuer = builder.issuer;
        account = builder.account;
        lineItem = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.lineItem, "lineItem"));
        totalPriceComponent = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.totalPriceComponent, "totalPriceComponent"));
        totalNet = builder.totalNet;
        totalGross = builder.totalGross;
        paymentTerms = builder.paymentTerms;
        note = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.note, "note"));
    }

    /**
     * <p>
     * Identifier of this Invoice, often used for reference in correspondence about this invoice or for tracking of payments.
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
     * The current state of the Invoice.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link InvoiceStatus}.
     */
    public InvoiceStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * In case of Invoice cancellation a reason must be given (entered in error, superseded by corrected invoice etc.).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getCancelledReason() {
        return cancelledReason;
    }

    /**
     * <p>
     * Type of Invoice depending on domain, realm an usage (e.g. internal/external, dental, preliminary).
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
     * The individual or set of individuals receiving the goods and services billed in this invoice.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * <p>
     * The individual or Organization responsible for balancing of this invoice.
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
     * Date/time(s) of when this Invoice was posted.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * <p>
     * Indicates who or what performed or participated in the charged service.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Participant}.
     */
    public List<Participant> getParticipant() {
        return participant;
    }

    /**
     * <p>
     * The organizationissuing the Invoice.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getIssuer() {
        return issuer;
    }

    /**
     * <p>
     * Account which is supposed to be balanced with this Invoice.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getAccount() {
        return account;
    }

    /**
     * <p>
     * Each line item represents one charge for goods and services rendered. Details such as date, code and amount are found 
     * in the referenced ChargeItem resource.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link LineItem}.
     */
    public List<LineItem> getLineItem() {
        return lineItem;
    }

    /**
     * <p>
     * The total amount for the Invoice may be calculated as the sum of the line items with surcharges/deductions that apply 
     * in certain conditions. The priceComponent element can be used to offer transparency to the recipient of the Invoice of 
     * how the total price was calculated.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link PriceComponent}.
     */
    public List<Invoice.LineItem.PriceComponent> getTotalPriceComponent() {
        return totalPriceComponent;
    }

    /**
     * <p>
     * Invoice total , taxes excluded.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Money}.
     */
    public Money getTotalNet() {
        return totalNet;
    }

    /**
     * <p>
     * Invoice total, tax included.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Money}.
     */
    public Money getTotalGross() {
        return totalGross;
    }

    /**
     * <p>
     * Payment details such as banking details, period of payment, deductibles, methods of payment.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getPaymentTerms() {
        return paymentTerms;
    }

    /**
     * <p>
     * Comments made about the invoice by the issuer, subject, or other participants.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
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
                accept(cancelledReason, "cancelledReason", visitor);
                accept(type, "type", visitor);
                accept(subject, "subject", visitor);
                accept(recipient, "recipient", visitor);
                accept(date, "date", visitor);
                accept(participant, "participant", visitor, Participant.class);
                accept(issuer, "issuer", visitor);
                accept(account, "account", visitor);
                accept(lineItem, "lineItem", visitor, LineItem.class);
                accept(totalPriceComponent, "totalPriceComponent", visitor, Invoice.LineItem.PriceComponent.class);
                accept(totalNet, "totalNet", visitor);
                accept(totalGross, "totalGross", visitor);
                accept(paymentTerms, "paymentTerms", visitor);
                accept(note, "note", visitor, Annotation.class);
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
        Invoice other = (Invoice) obj;
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
            Objects.equals(cancelledReason, other.cancelledReason) && 
            Objects.equals(type, other.type) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(recipient, other.recipient) && 
            Objects.equals(date, other.date) && 
            Objects.equals(participant, other.participant) && 
            Objects.equals(issuer, other.issuer) && 
            Objects.equals(account, other.account) && 
            Objects.equals(lineItem, other.lineItem) && 
            Objects.equals(totalPriceComponent, other.totalPriceComponent) && 
            Objects.equals(totalNet, other.totalNet) && 
            Objects.equals(totalGross, other.totalGross) && 
            Objects.equals(paymentTerms, other.paymentTerms) && 
            Objects.equals(note, other.note);
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
                cancelledReason, 
                type, 
                subject, 
                recipient, 
                date, 
                participant, 
                issuer, 
                account, 
                lineItem, 
                totalPriceComponent, 
                totalNet, 
                totalGross, 
                paymentTerms, 
                note);
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
        private InvoiceStatus status;
        private String cancelledReason;
        private CodeableConcept type;
        private Reference subject;
        private Reference recipient;
        private DateTime date;
        private List<Participant> participant = new ArrayList<>();
        private Reference issuer;
        private Reference account;
        private List<LineItem> lineItem = new ArrayList<>();
        private List<Invoice.LineItem.PriceComponent> totalPriceComponent = new ArrayList<>();
        private Money totalNet;
        private Money totalGross;
        private Markdown paymentTerms;
        private List<Annotation> note = new ArrayList<>();

        private Builder() {
            super();
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
         * Identifier of this Invoice, often used for reference in correspondence about this invoice or for tracking of payments.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     Business Identifier for item
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
         * Identifier of this Invoice, often used for reference in correspondence about this invoice or for tracking of payments.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Business Identifier for item
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
         * The current state of the Invoice.
         * </p>
         * 
         * @param status
         *     draft | issued | balanced | cancelled | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(InvoiceStatus status) {
            this.status = status;
            return this;
        }

        /**
         * <p>
         * In case of Invoice cancellation a reason must be given (entered in error, superseded by corrected invoice etc.).
         * </p>
         * 
         * @param cancelledReason
         *     Reason for cancellation of this Invoice
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder cancelledReason(String cancelledReason) {
            this.cancelledReason = cancelledReason;
            return this;
        }

        /**
         * <p>
         * Type of Invoice depending on domain, realm an usage (e.g. internal/external, dental, preliminary).
         * </p>
         * 
         * @param type
         *     Type of Invoice
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * <p>
         * The individual or set of individuals receiving the goods and services billed in this invoice.
         * </p>
         * 
         * @param subject
         *     Recipient(s) of goods and services
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * <p>
         * The individual or Organization responsible for balancing of this invoice.
         * </p>
         * 
         * @param recipient
         *     Recipient of this invoice
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recipient(Reference recipient) {
            this.recipient = recipient;
            return this;
        }

        /**
         * <p>
         * Date/time(s) of when this Invoice was posted.
         * </p>
         * 
         * @param date
         *     Invoice date / posting date
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * <p>
         * Indicates who or what performed or participated in the charged service.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param participant
         *     Participant in creation of this Invoice
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder participant(Participant... participant) {
            for (Participant value : participant) {
                this.participant.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates who or what performed or participated in the charged service.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param participant
         *     Participant in creation of this Invoice
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder participant(Collection<Participant> participant) {
            this.participant = new ArrayList<>(participant);
            return this;
        }

        /**
         * <p>
         * The organizationissuing the Invoice.
         * </p>
         * 
         * @param issuer
         *     Issuing Organization of Invoice
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder issuer(Reference issuer) {
            this.issuer = issuer;
            return this;
        }

        /**
         * <p>
         * Account which is supposed to be balanced with this Invoice.
         * </p>
         * 
         * @param account
         *     Account that is being balanced
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder account(Reference account) {
            this.account = account;
            return this;
        }

        /**
         * <p>
         * Each line item represents one charge for goods and services rendered. Details such as date, code and amount are found 
         * in the referenced ChargeItem resource.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param lineItem
         *     Line items of this Invoice
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lineItem(LineItem... lineItem) {
            for (LineItem value : lineItem) {
                this.lineItem.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Each line item represents one charge for goods and services rendered. Details such as date, code and amount are found 
         * in the referenced ChargeItem resource.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param lineItem
         *     Line items of this Invoice
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lineItem(Collection<LineItem> lineItem) {
            this.lineItem = new ArrayList<>(lineItem);
            return this;
        }

        /**
         * <p>
         * The total amount for the Invoice may be calculated as the sum of the line items with surcharges/deductions that apply 
         * in certain conditions. The priceComponent element can be used to offer transparency to the recipient of the Invoice of 
         * how the total price was calculated.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param totalPriceComponent
         *     Components of Invoice total
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder totalPriceComponent(Invoice.LineItem.PriceComponent... totalPriceComponent) {
            for (Invoice.LineItem.PriceComponent value : totalPriceComponent) {
                this.totalPriceComponent.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The total amount for the Invoice may be calculated as the sum of the line items with surcharges/deductions that apply 
         * in certain conditions. The priceComponent element can be used to offer transparency to the recipient of the Invoice of 
         * how the total price was calculated.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param totalPriceComponent
         *     Components of Invoice total
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder totalPriceComponent(Collection<Invoice.LineItem.PriceComponent> totalPriceComponent) {
            this.totalPriceComponent = new ArrayList<>(totalPriceComponent);
            return this;
        }

        /**
         * <p>
         * Invoice total , taxes excluded.
         * </p>
         * 
         * @param totalNet
         *     Net total of this Invoice
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder totalNet(Money totalNet) {
            this.totalNet = totalNet;
            return this;
        }

        /**
         * <p>
         * Invoice total, tax included.
         * </p>
         * 
         * @param totalGross
         *     Gross total of this Invoice
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder totalGross(Money totalGross) {
            this.totalGross = totalGross;
            return this;
        }

        /**
         * <p>
         * Payment details such as banking details, period of payment, deductibles, methods of payment.
         * </p>
         * 
         * @param paymentTerms
         *     Payment details
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder paymentTerms(Markdown paymentTerms) {
            this.paymentTerms = paymentTerms;
            return this;
        }

        /**
         * <p>
         * Comments made about the invoice by the issuer, subject, or other participants.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param note
         *     Comments made about the invoice
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Annotation... note) {
            for (Annotation value : note) {
                this.note.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Comments made about the invoice by the issuer, subject, or other participants.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param note
         *     Comments made about the invoice
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        @Override
        public Invoice build() {
            return new Invoice(this);
        }

        protected Builder from(Invoice invoice) {
            super.from(invoice);
            identifier.addAll(invoice.identifier);
            status = invoice.status;
            cancelledReason = invoice.cancelledReason;
            type = invoice.type;
            subject = invoice.subject;
            recipient = invoice.recipient;
            date = invoice.date;
            participant.addAll(invoice.participant);
            issuer = invoice.issuer;
            account = invoice.account;
            lineItem.addAll(invoice.lineItem);
            totalPriceComponent.addAll(invoice.totalPriceComponent);
            totalNet = invoice.totalNet;
            totalGross = invoice.totalGross;
            paymentTerms = invoice.paymentTerms;
            note.addAll(invoice.note);
            return this;
        }
    }

    /**
     * <p>
     * Indicates who or what performed or participated in the charged service.
     * </p>
     */
    public static class Participant extends BackboneElement {
        private final CodeableConcept role;
        private final Reference actor;

        private volatile int hashCode;

        private Participant(Builder builder) {
            super(builder);
            role = builder.role;
            actor = ValidationSupport.requireNonNull(builder.actor, "actor");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Describes the type of involvement (e.g. transcriptionist, creator etc.). If the invoice has been created 
         * automatically, the Participant may be a billing engine or another kind of device.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getRole() {
            return role;
        }

        /**
         * <p>
         * The device, practitioner, etc. who performed or participated in the service.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getActor() {
            return actor;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (role != null) || 
                (actor != null);
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
                    accept(role, "role", visitor);
                    accept(actor, "actor", visitor);
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
            Participant other = (Participant) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(role, other.role) && 
                Objects.equals(actor, other.actor);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    role, 
                    actor);
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
            private CodeableConcept role;
            private Reference actor;

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
             * Adds new element(s) to existing list
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
             * Describes the type of involvement (e.g. transcriptionist, creator etc.). If the invoice has been created 
             * automatically, the Participant may be a billing engine or another kind of device.
             * </p>
             * 
             * @param role
             *     Type of involvement in creation of this Invoice
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder role(CodeableConcept role) {
                this.role = role;
                return this;
            }

            /**
             * <p>
             * The device, practitioner, etc. who performed or participated in the service.
             * </p>
             * 
             * @param actor
             *     Individual who was involved
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder actor(Reference actor) {
                this.actor = actor;
                return this;
            }

            @Override
            public Participant build() {
                return new Participant(this);
            }

            protected Builder from(Participant participant) {
                super.from(participant);
                role = participant.role;
                actor = participant.actor;
                return this;
            }
        }
    }

    /**
     * <p>
     * Each line item represents one charge for goods and services rendered. Details such as date, code and amount are found 
     * in the referenced ChargeItem resource.
     * </p>
     */
    public static class LineItem extends BackboneElement {
        private final PositiveInt sequence;
        private final Element chargeItem;
        private final List<PriceComponent> priceComponent;

        private volatile int hashCode;

        private LineItem(Builder builder) {
            super(builder);
            sequence = builder.sequence;
            chargeItem = ValidationSupport.requireChoiceElement(builder.chargeItem, "chargeItem", Reference.class, CodeableConcept.class);
            priceComponent = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.priceComponent, "priceComponent"));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Sequence in which the items appear on the invoice.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * <p>
         * The ChargeItem contains information such as the billing code, date, amount etc. If no further details are required for 
         * the lineItem, inline billing codes can be added using the CodeableConcept data type instead of the Reference.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getChargeItem() {
            return chargeItem;
        }

        /**
         * <p>
         * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
         * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
         * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
         * the Invoice as to how the prices have been calculated.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PriceComponent}.
         */
        public List<PriceComponent> getPriceComponent() {
            return priceComponent;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (sequence != null) || 
                (chargeItem != null) || 
                !priceComponent.isEmpty();
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
                    accept(sequence, "sequence", visitor);
                    accept(chargeItem, "chargeItem", visitor);
                    accept(priceComponent, "priceComponent", visitor, PriceComponent.class);
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
            LineItem other = (LineItem) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(sequence, other.sequence) && 
                Objects.equals(chargeItem, other.chargeItem) && 
                Objects.equals(priceComponent, other.priceComponent);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    sequence, 
                    chargeItem, 
                    priceComponent);
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
            private PositiveInt sequence;
            private Element chargeItem;
            private List<PriceComponent> priceComponent = new ArrayList<>();

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
             * Adds new element(s) to existing list
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
             * Sequence in which the items appear on the invoice.
             * </p>
             * 
             * @param sequence
             *     Sequence number of line item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sequence(PositiveInt sequence) {
                this.sequence = sequence;
                return this;
            }

            /**
             * <p>
             * The ChargeItem contains information such as the billing code, date, amount etc. If no further details are required for 
             * the lineItem, inline billing codes can be added using the CodeableConcept data type instead of the Reference.
             * </p>
             * 
             * @param chargeItem
             *     Reference to ChargeItem containing details of this line item or an inline billing code
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder chargeItem(Element chargeItem) {
                this.chargeItem = chargeItem;
                return this;
            }

            /**
             * <p>
             * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
             * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
             * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
             * the Invoice as to how the prices have been calculated.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param priceComponent
             *     Components of total line item price
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder priceComponent(PriceComponent... priceComponent) {
                for (PriceComponent value : priceComponent) {
                    this.priceComponent.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
             * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
             * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
             * the Invoice as to how the prices have been calculated.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param priceComponent
             *     Components of total line item price
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder priceComponent(Collection<PriceComponent> priceComponent) {
                this.priceComponent = new ArrayList<>(priceComponent);
                return this;
            }

            @Override
            public LineItem build() {
                return new LineItem(this);
            }

            protected Builder from(LineItem lineItem) {
                super.from(lineItem);
                sequence = lineItem.sequence;
                chargeItem = lineItem.chargeItem;
                priceComponent.addAll(lineItem.priceComponent);
                return this;
            }
        }

        /**
         * <p>
         * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
         * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
         * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
         * the Invoice as to how the prices have been calculated.
         * </p>
         */
        public static class PriceComponent extends BackboneElement {
            private final InvoicePriceComponentType type;
            private final CodeableConcept code;
            private final Decimal factor;
            private final Money amount;

            private volatile int hashCode;

            private PriceComponent(Builder builder) {
                super(builder);
                type = ValidationSupport.requireNonNull(builder.type, "type");
                code = builder.code;
                factor = builder.factor;
                amount = builder.amount;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * This code identifies the type of the component.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link InvoicePriceComponentType}.
             */
            public InvoicePriceComponentType getType() {
                return type;
            }

            /**
             * <p>
             * A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts 
             * etc.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getCode() {
                return code;
            }

            /**
             * <p>
             * The factor that has been applied on the base price for calculating this component.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Decimal}.
             */
            public Decimal getFactor() {
                return factor;
            }

            /**
             * <p>
             * The amount calculated for this component.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Money}.
             */
            public Money getAmount() {
                return amount;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    (code != null) || 
                    (factor != null) || 
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
                        accept(type, "type", visitor);
                        accept(code, "code", visitor);
                        accept(factor, "factor", visitor);
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
                PriceComponent other = (PriceComponent) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(code, other.code) && 
                    Objects.equals(factor, other.factor) && 
                    Objects.equals(amount, other.amount);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        code, 
                        factor, 
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
                private InvoicePriceComponentType type;
                private CodeableConcept code;
                private Decimal factor;
                private Money amount;

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
                 * Adds new element(s) to existing list
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
                 * This code identifies the type of the component.
                 * </p>
                 * 
                 * @param type
                 *     base | surcharge | deduction | discount | tax | informational
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(InvoicePriceComponentType type) {
                    this.type = type;
                    return this;
                }

                /**
                 * <p>
                 * A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts 
                 * etc.
                 * </p>
                 * 
                 * @param code
                 *     Code identifying the specific component
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder code(CodeableConcept code) {
                    this.code = code;
                    return this;
                }

                /**
                 * <p>
                 * The factor that has been applied on the base price for calculating this component.
                 * </p>
                 * 
                 * @param factor
                 *     Factor used for calculating this component
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder factor(Decimal factor) {
                    this.factor = factor;
                    return this;
                }

                /**
                 * <p>
                 * The amount calculated for this component.
                 * </p>
                 * 
                 * @param amount
                 *     Monetary amount associated with this component
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder amount(Money amount) {
                    this.amount = amount;
                    return this;
                }

                @Override
                public PriceComponent build() {
                    return new PriceComponent(this);
                }

                protected Builder from(PriceComponent priceComponent) {
                    super.from(priceComponent);
                    type = priceComponent.type;
                    code = priceComponent.code;
                    factor = priceComponent.factor;
                    amount = priceComponent.amount;
                    return this;
                }
            }
        }
    }
}

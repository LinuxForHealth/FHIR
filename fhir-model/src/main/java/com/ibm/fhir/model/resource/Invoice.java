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
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.InvoicePriceComponentType;
import com.ibm.fhir.model.type.code.InvoiceStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing 
 * purpose.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Invoice extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "InvoiceStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Codes identifying the lifecycle stage of an Invoice.",
        valueSet = "http://hl7.org/fhir/ValueSet/invoice-status|4.3.0"
    )
    @Required
    private final InvoiceStatus status;
    private final String cancelledReason;
    @Summary
    private final CodeableConcept type;
    @Summary
    @ReferenceTarget({ "Patient", "Group" })
    private final Reference subject;
    @Summary
    @ReferenceTarget({ "Organization", "Patient", "RelatedPerson" })
    private final Reference recipient;
    @Summary
    private final DateTime date;
    private final List<Participant> participant;
    @ReferenceTarget({ "Organization" })
    private final Reference issuer;
    @ReferenceTarget({ "Account" })
    private final Reference account;
    private final List<LineItem> lineItem;
    private final List<Invoice.LineItem.PriceComponent> totalPriceComponent;
    @Summary
    private final Money totalNet;
    @Summary
    private final Money totalGross;
    private final Markdown paymentTerms;
    private final List<Annotation> note;

    private Invoice(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        cancelledReason = builder.cancelledReason;
        type = builder.type;
        subject = builder.subject;
        recipient = builder.recipient;
        date = builder.date;
        participant = Collections.unmodifiableList(builder.participant);
        issuer = builder.issuer;
        account = builder.account;
        lineItem = Collections.unmodifiableList(builder.lineItem);
        totalPriceComponent = Collections.unmodifiableList(builder.totalPriceComponent);
        totalNet = builder.totalNet;
        totalGross = builder.totalGross;
        paymentTerms = builder.paymentTerms;
        note = Collections.unmodifiableList(builder.note);
    }

    /**
     * Identifier of this Invoice, often used for reference in correspondence about this invoice or for tracking of payments.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The current state of the Invoice.
     * 
     * @return
     *     An immutable object of type {@link InvoiceStatus} that is non-null.
     */
    public InvoiceStatus getStatus() {
        return status;
    }

    /**
     * In case of Invoice cancellation a reason must be given (entered in error, superseded by corrected invoice etc.).
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getCancelledReason() {
        return cancelledReason;
    }

    /**
     * Type of Invoice depending on domain, realm an usage (e.g. internal/external, dental, preliminary).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * The individual or set of individuals receiving the goods and services billed in this invoice.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The individual or Organization responsible for balancing of this invoice.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRecipient() {
        return recipient;
    }

    /**
     * Date/time(s) of when this Invoice was posted.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * Indicates who or what performed or participated in the charged service.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Participant} that may be empty.
     */
    public List<Participant> getParticipant() {
        return participant;
    }

    /**
     * The organizationissuing the Invoice.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getIssuer() {
        return issuer;
    }

    /**
     * Account which is supposed to be balanced with this Invoice.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getAccount() {
        return account;
    }

    /**
     * Each line item represents one charge for goods and services rendered. Details such as date, code and amount are found 
     * in the referenced ChargeItem resource.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link LineItem} that may be empty.
     */
    public List<LineItem> getLineItem() {
        return lineItem;
    }

    /**
     * The total amount for the Invoice may be calculated as the sum of the line items with surcharges/deductions that apply 
     * in certain conditions. The priceComponent element can be used to offer transparency to the recipient of the Invoice of 
     * how the total price was calculated.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link PriceComponent} that may be empty.
     */
    public List<Invoice.LineItem.PriceComponent> getTotalPriceComponent() {
        return totalPriceComponent;
    }

    /**
     * Invoice total , taxes excluded.
     * 
     * @return
     *     An immutable object of type {@link Money} that may be null.
     */
    public Money getTotalNet() {
        return totalNet;
    }

    /**
     * Invoice total, tax included.
     * 
     * @return
     *     An immutable object of type {@link Money} that may be null.
     */
    public Money getTotalGross() {
        return totalGross;
    }

    /**
     * Payment details such as banking details, period of payment, deductibles, methods of payment.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPaymentTerms() {
        return paymentTerms;
    }

    /**
     * Comments made about the invoice by the issuer, subject, or other participants.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (cancelledReason != null) || 
            (type != null) || 
            (subject != null) || 
            (recipient != null) || 
            (date != null) || 
            !participant.isEmpty() || 
            (issuer != null) || 
            (account != null) || 
            !lineItem.isEmpty() || 
            !totalPriceComponent.isEmpty() || 
            (totalNet != null) || 
            (totalGross != null) || 
            (paymentTerms != null) || 
            !note.isEmpty();
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
         * Identifier of this Invoice, often used for reference in correspondence about this invoice or for tracking of payments.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Identifier of this Invoice, often used for reference in correspondence about this invoice or for tracking of payments.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for item
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
         * The current state of the Invoice.
         * 
         * <p>This element is required.
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
         * Convenience method for setting {@code cancelledReason}.
         * 
         * @param cancelledReason
         *     Reason for cancellation of this Invoice
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #cancelledReason(com.ibm.fhir.model.type.String)
         */
        public Builder cancelledReason(java.lang.String cancelledReason) {
            this.cancelledReason = (cancelledReason == null) ? null : String.of(cancelledReason);
            return this;
        }

        /**
         * In case of Invoice cancellation a reason must be given (entered in error, superseded by corrected invoice etc.).
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
         * Type of Invoice depending on domain, realm an usage (e.g. internal/external, dental, preliminary).
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
         * The individual or set of individuals receiving the goods and services billed in this invoice.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Group}</li>
         * </ul>
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
         * The individual or Organization responsible for balancing of this invoice.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * <li>{@link Patient}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
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
         * Date/time(s) of when this Invoice was posted.
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
         * Indicates who or what performed or participated in the charged service.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Indicates who or what performed or participated in the charged service.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param participant
         *     Participant in creation of this Invoice
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder participant(Collection<Participant> participant) {
            this.participant = new ArrayList<>(participant);
            return this;
        }

        /**
         * The organizationissuing the Invoice.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
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
         * Account which is supposed to be balanced with this Invoice.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Account}</li>
         * </ul>
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
         * Each line item represents one charge for goods and services rendered. Details such as date, code and amount are found 
         * in the referenced ChargeItem resource.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Each line item represents one charge for goods and services rendered. Details such as date, code and amount are found 
         * in the referenced ChargeItem resource.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param lineItem
         *     Line items of this Invoice
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder lineItem(Collection<LineItem> lineItem) {
            this.lineItem = new ArrayList<>(lineItem);
            return this;
        }

        /**
         * The total amount for the Invoice may be calculated as the sum of the line items with surcharges/deductions that apply 
         * in certain conditions. The priceComponent element can be used to offer transparency to the recipient of the Invoice of 
         * how the total price was calculated.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The total amount for the Invoice may be calculated as the sum of the line items with surcharges/deductions that apply 
         * in certain conditions. The priceComponent element can be used to offer transparency to the recipient of the Invoice of 
         * how the total price was calculated.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param totalPriceComponent
         *     Components of Invoice total
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder totalPriceComponent(Collection<Invoice.LineItem.PriceComponent> totalPriceComponent) {
            this.totalPriceComponent = new ArrayList<>(totalPriceComponent);
            return this;
        }

        /**
         * Invoice total , taxes excluded.
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
         * Invoice total, tax included.
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
         * Payment details such as banking details, period of payment, deductibles, methods of payment.
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
         * Comments made about the invoice by the issuer, subject, or other participants.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Comments made about the invoice by the issuer, subject, or other participants.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments made about the invoice
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        /**
         * Build the {@link Invoice}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Invoice}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Invoice per the base specification
         */
        @Override
        public Invoice build() {
            Invoice invoice = new Invoice(this);
            if (validating) {
                validate(invoice);
            }
            return invoice;
        }

        protected void validate(Invoice invoice) {
            super.validate(invoice);
            ValidationSupport.checkList(invoice.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(invoice.status, "status");
            ValidationSupport.checkList(invoice.participant, "participant", Participant.class);
            ValidationSupport.checkList(invoice.lineItem, "lineItem", LineItem.class);
            ValidationSupport.checkList(invoice.totalPriceComponent, "totalPriceComponent", Invoice.LineItem.PriceComponent.class);
            ValidationSupport.checkList(invoice.note, "note", Annotation.class);
            ValidationSupport.checkReferenceType(invoice.subject, "subject", "Patient", "Group");
            ValidationSupport.checkReferenceType(invoice.recipient, "recipient", "Organization", "Patient", "RelatedPerson");
            ValidationSupport.checkReferenceType(invoice.issuer, "issuer", "Organization");
            ValidationSupport.checkReferenceType(invoice.account, "account", "Account");
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
     * Indicates who or what performed or participated in the charged service.
     */
    public static class Participant extends BackboneElement {
        private final CodeableConcept role;
        @ReferenceTarget({ "Practitioner", "Organization", "Patient", "PractitionerRole", "Device", "RelatedPerson" })
        @Required
        private final Reference actor;

        private Participant(Builder builder) {
            super(builder);
            role = builder.role;
            actor = builder.actor;
        }

        /**
         * Describes the type of involvement (e.g. transcriptionist, creator etc.). If the invoice has been created 
         * automatically, the Participant may be a billing engine or another kind of device.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getRole() {
            return role;
        }

        /**
         * The device, practitioner, etc. who performed or participated in the service.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
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
             * Describes the type of involvement (e.g. transcriptionist, creator etc.). If the invoice has been created 
             * automatically, the Participant may be a billing engine or another kind of device.
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
             * The device, practitioner, etc. who performed or participated in the service.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link Organization}</li>
             * <li>{@link Patient}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Device}</li>
             * <li>{@link RelatedPerson}</li>
             * </ul>
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

            /**
             * Build the {@link Participant}
             * 
             * <p>Required elements:
             * <ul>
             * <li>actor</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Participant}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Participant per the base specification
             */
            @Override
            public Participant build() {
                Participant participant = new Participant(this);
                if (validating) {
                    validate(participant);
                }
                return participant;
            }

            protected void validate(Participant participant) {
                super.validate(participant);
                ValidationSupport.requireNonNull(participant.actor, "actor");
                ValidationSupport.checkReferenceType(participant.actor, "actor", "Practitioner", "Organization", "Patient", "PractitionerRole", "Device", "RelatedPerson");
                ValidationSupport.requireValueOrChildren(participant);
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
     * Each line item represents one charge for goods and services rendered. Details such as date, code and amount are found 
     * in the referenced ChargeItem resource.
     */
    public static class LineItem extends BackboneElement {
        private final PositiveInt sequence;
        @ReferenceTarget({ "ChargeItem" })
        @Choice({ Reference.class, CodeableConcept.class })
        @Required
        private final Element chargeItem;
        private final List<PriceComponent> priceComponent;

        private LineItem(Builder builder) {
            super(builder);
            sequence = builder.sequence;
            chargeItem = builder.chargeItem;
            priceComponent = Collections.unmodifiableList(builder.priceComponent);
        }

        /**
         * Sequence in which the items appear on the invoice.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that may be null.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * The ChargeItem contains information such as the billing code, date, amount etc. If no further details are required for 
         * the lineItem, inline billing codes can be added using the CodeableConcept data type instead of the Reference.
         * 
         * @return
         *     An immutable object of type {@link Reference} or {@link CodeableConcept} that is non-null.
         */
        public Element getChargeItem() {
            return chargeItem;
        }

        /**
         * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
         * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
         * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
         * the Invoice as to how the prices have been calculated.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PriceComponent} that may be empty.
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
             * Sequence in which the items appear on the invoice.
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
             * The ChargeItem contains information such as the billing code, date, amount etc. If no further details are required for 
             * the lineItem, inline billing codes can be added using the CodeableConcept data type instead of the Reference.
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Reference}</li>
             * <li>{@link CodeableConcept}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link ChargeItem}</li>
             * </ul>
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
             * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
             * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
             * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
             * the Invoice as to how the prices have been calculated.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
             * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
             * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
             * the Invoice as to how the prices have been calculated.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param priceComponent
             *     Components of total line item price
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder priceComponent(Collection<PriceComponent> priceComponent) {
                this.priceComponent = new ArrayList<>(priceComponent);
                return this;
            }

            /**
             * Build the {@link LineItem}
             * 
             * <p>Required elements:
             * <ul>
             * <li>chargeItem</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link LineItem}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid LineItem per the base specification
             */
            @Override
            public LineItem build() {
                LineItem lineItem = new LineItem(this);
                if (validating) {
                    validate(lineItem);
                }
                return lineItem;
            }

            protected void validate(LineItem lineItem) {
                super.validate(lineItem);
                ValidationSupport.requireChoiceElement(lineItem.chargeItem, "chargeItem", Reference.class, CodeableConcept.class);
                ValidationSupport.checkList(lineItem.priceComponent, "priceComponent", PriceComponent.class);
                ValidationSupport.checkReferenceType(lineItem.chargeItem, "chargeItem", "ChargeItem");
                ValidationSupport.requireValueOrChildren(lineItem);
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
         * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
         * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
         * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
         * the Invoice as to how the prices have been calculated.
         */
        public static class PriceComponent extends BackboneElement {
            @Binding(
                bindingName = "InvoicePriceComponentType",
                strength = BindingStrength.Value.REQUIRED,
                description = "Codes indicating the kind of the price component.",
                valueSet = "http://hl7.org/fhir/ValueSet/invoice-priceComponentType|4.3.0"
            )
            @Required
            private final InvoicePriceComponentType type;
            private final CodeableConcept code;
            private final Decimal factor;
            private final Money amount;

            private PriceComponent(Builder builder) {
                super(builder);
                type = builder.type;
                code = builder.code;
                factor = builder.factor;
                amount = builder.amount;
            }

            /**
             * This code identifies the type of the component.
             * 
             * @return
             *     An immutable object of type {@link InvoicePriceComponentType} that is non-null.
             */
            public InvoicePriceComponentType getType() {
                return type;
            }

            /**
             * A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts 
             * etc.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getCode() {
                return code;
            }

            /**
             * The factor that has been applied on the base price for calculating this component.
             * 
             * @return
             *     An immutable object of type {@link Decimal} that may be null.
             */
            public Decimal getFactor() {
                return factor;
            }

            /**
             * The amount calculated for this component.
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
                 * This code identifies the type of the component.
                 * 
                 * <p>This element is required.
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
                 * A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts 
                 * etc.
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
                 * The factor that has been applied on the base price for calculating this component.
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
                 * The amount calculated for this component.
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

                /**
                 * Build the {@link PriceComponent}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>type</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link PriceComponent}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid PriceComponent per the base specification
                 */
                @Override
                public PriceComponent build() {
                    PriceComponent priceComponent = new PriceComponent(this);
                    if (validating) {
                        validate(priceComponent);
                    }
                    return priceComponent;
                }

                protected void validate(PriceComponent priceComponent) {
                    super.validate(priceComponent);
                    ValidationSupport.requireNonNull(priceComponent.type, "type");
                    ValidationSupport.requireValueOrChildren(priceComponent);
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

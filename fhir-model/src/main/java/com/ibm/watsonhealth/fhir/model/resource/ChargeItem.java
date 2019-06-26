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

import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.ChargeItemStatus;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Money;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore 
 * referring not only to the product, but containing in addition details of the provision, like date, time, amounts and 
 * participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal 
 * cost allocation.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ChargeItem extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Uri> definitionUri;
    private final List<Canonical> definitionCanonical;
    private final ChargeItemStatus status;
    private final List<Reference> partOf;
    private final CodeableConcept code;
    private final Reference subject;
    private final Reference context;
    private final Element occurrence;
    private final List<Performer> performer;
    private final Reference performingOrganization;
    private final Reference requestingOrganization;
    private final Reference costCenter;
    private final Quantity quantity;
    private final List<CodeableConcept> bodysite;
    private final Decimal factorOverride;
    private final Money priceOverride;
    private final String overrideReason;
    private final Reference enterer;
    private final DateTime enteredDate;
    private final List<CodeableConcept> reason;
    private final List<Reference> service;
    private final Element product;
    private final List<Reference> account;
    private final List<Annotation> note;
    private final List<Reference> supportingInformation;

    private ChargeItem(Builder builder) {
        super(builder);
        this.identifier = builder.identifier;
        this.definitionUri = builder.definitionUri;
        this.definitionCanonical = builder.definitionCanonical;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.partOf = builder.partOf;
        this.code = ValidationSupport.requireNonNull(builder.code, "code");
        this.subject = ValidationSupport.requireNonNull(builder.subject, "subject");
        this.context = builder.context;
        this.occurrence = ValidationSupport.choiceElement(builder.occurrence, "occurrence", DateTime.class, Period.class, Timing.class);
        this.performer = builder.performer;
        this.performingOrganization = builder.performingOrganization;
        this.requestingOrganization = builder.requestingOrganization;
        this.costCenter = builder.costCenter;
        this.quantity = builder.quantity;
        this.bodysite = builder.bodysite;
        this.factorOverride = builder.factorOverride;
        this.priceOverride = builder.priceOverride;
        this.overrideReason = builder.overrideReason;
        this.enterer = builder.enterer;
        this.enteredDate = builder.enteredDate;
        this.reason = builder.reason;
        this.service = builder.service;
        this.product = ValidationSupport.choiceElement(builder.product, "product", Reference.class, CodeableConcept.class);
        this.account = builder.account;
        this.note = builder.note;
        this.supportingInformation = builder.supportingInformation;
    }

    /**
     * <p>
     * Identifiers assigned to this event performer or other systems.
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
     * References the (external) source of pricing information, rules of application for the code this ChargeItem uses.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Uri}.
     */
    public List<Uri> getDefinitionUri() {
        return definitionUri;
    }

    /**
     * <p>
     * References the source of pricing information, rules of application for the code this ChargeItem uses.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getDefinitionCanonical() {
        return definitionCanonical;
    }

    /**
     * <p>
     * The current state of the ChargeItem.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ChargeItemStatus}.
     */
    public ChargeItemStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * ChargeItems can be grouped to larger ChargeItems covering the whole set.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * <p>
     * A code that identifies the charge, like a billing code.
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
     * The individual or set of individuals the action is being or was performed on.
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
     * The encounter or episode of care that establishes the context for this event.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getContext() {
        return context;
    }

    /**
     * <p>
     * Date/time(s) or duration when the charged service was applied.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getOccurrence() {
        return occurrence;
    }

    /**
     * <p>
     * Indicates who or what performed or participated in the charged service.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Performer}.
     */
    public List<Performer> getPerformer() {
        return performer;
    }

    /**
     * <p>
     * The organization requesting the service.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPerformingOrganization() {
        return performingOrganization;
    }

    /**
     * <p>
     * The organization performing the service.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getRequestingOrganization() {
        return requestingOrganization;
    }

    /**
     * <p>
     * The financial cost center permits the tracking of charge attribution.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getCostCenter() {
        return costCenter;
    }

    /**
     * <p>
     * Quantity of which the charge item has been serviced.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Quantity}.
     */
    public Quantity getQuantity() {
        return quantity;
    }

    /**
     * <p>
     * The anatomical location where the related service has been applied.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getBodysite() {
        return bodysite;
    }

    /**
     * <p>
     * Factor overriding the factor determined by the rules associated with the code.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Decimal}.
     */
    public Decimal getFactorOverride() {
        return factorOverride;
    }

    /**
     * <p>
     * Total price of the charge overriding the list price associated with the code.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Money}.
     */
    public Money getPriceOverride() {
        return priceOverride;
    }

    /**
     * <p>
     * If the list price or the rule-based factor associated with the code is overridden, this attribute can capture a text 
     * to indicate the reason for this action.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getOverrideReason() {
        return overrideReason;
    }

    /**
     * <p>
     * The device, practitioner, etc. who entered the charge item.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getEnterer() {
        return enterer;
    }

    /**
     * <p>
     * Date the charge item was entered.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getEnteredDate() {
        return enteredDate;
    }

    /**
     * <p>
     * Describes why the event occurred in coded or textual form.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getReason() {
        return reason;
    }

    /**
     * <p>
     * Indicated the rendered service that caused this charge.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getService() {
        return service;
    }

    /**
     * <p>
     * Identifies the device, food, drug or other product being charged either by type code or reference to an instance.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getProduct() {
        return product;
    }

    /**
     * <p>
     * Account into which this ChargeItems belongs.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getAccount() {
        return account;
    }

    /**
     * <p>
     * Comments made about the event by the performer, subject or other participants.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * <p>
     * Further information supporting this charge.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSupportingInformation() {
        return supportingInformation;
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
                accept(definitionUri, "definitionUri", visitor, Uri.class);
                accept(definitionCanonical, "definitionCanonical", visitor, Canonical.class);
                accept(status, "status", visitor);
                accept(partOf, "partOf", visitor, Reference.class);
                accept(code, "code", visitor);
                accept(subject, "subject", visitor);
                accept(context, "context", visitor);
                accept(occurrence, "occurrence", visitor, true);
                accept(performer, "performer", visitor, Performer.class);
                accept(performingOrganization, "performingOrganization", visitor);
                accept(requestingOrganization, "requestingOrganization", visitor);
                accept(costCenter, "costCenter", visitor);
                accept(quantity, "quantity", visitor);
                accept(bodysite, "bodysite", visitor, CodeableConcept.class);
                accept(factorOverride, "factorOverride", visitor);
                accept(priceOverride, "priceOverride", visitor);
                accept(overrideReason, "overrideReason", visitor);
                accept(enterer, "enterer", visitor);
                accept(enteredDate, "enteredDate", visitor);
                accept(reason, "reason", visitor, CodeableConcept.class);
                accept(service, "service", visitor, Reference.class);
                accept(product, "product", visitor, true);
                accept(account, "account", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
                accept(supportingInformation, "supportingInformation", visitor, Reference.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, code, subject);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.definitionUri.addAll(definitionUri);
        builder.definitionCanonical.addAll(definitionCanonical);
        builder.partOf.addAll(partOf);
        builder.context = context;
        builder.occurrence = occurrence;
        builder.performer.addAll(performer);
        builder.performingOrganization = performingOrganization;
        builder.requestingOrganization = requestingOrganization;
        builder.costCenter = costCenter;
        builder.quantity = quantity;
        builder.bodysite.addAll(bodysite);
        builder.factorOverride = factorOverride;
        builder.priceOverride = priceOverride;
        builder.overrideReason = overrideReason;
        builder.enterer = enterer;
        builder.enteredDate = enteredDate;
        builder.reason.addAll(reason);
        builder.service.addAll(service);
        builder.product = product;
        builder.account.addAll(account);
        builder.note.addAll(note);
        builder.supportingInformation.addAll(supportingInformation);
        return builder;
    }

    public static Builder builder(ChargeItemStatus status, CodeableConcept code, Reference subject) {
        return new Builder(status, code, subject);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final ChargeItemStatus status;
        private final CodeableConcept code;
        private final Reference subject;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Uri> definitionUri = new ArrayList<>();
        private List<Canonical> definitionCanonical = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private Reference context;
        private Element occurrence;
        private List<Performer> performer = new ArrayList<>();
        private Reference performingOrganization;
        private Reference requestingOrganization;
        private Reference costCenter;
        private Quantity quantity;
        private List<CodeableConcept> bodysite = new ArrayList<>();
        private Decimal factorOverride;
        private Money priceOverride;
        private String overrideReason;
        private Reference enterer;
        private DateTime enteredDate;
        private List<CodeableConcept> reason = new ArrayList<>();
        private List<Reference> service = new ArrayList<>();
        private Element product;
        private List<Reference> account = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<Reference> supportingInformation = new ArrayList<>();

        private Builder(ChargeItemStatus status, CodeableConcept code, Reference subject) {
            super();
            this.status = status;
            this.code = code;
            this.subject = subject;
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
         * Identifiers assigned to this event performer or other systems.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for item
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
         * Identifiers assigned to this event performer or other systems.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for item
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
         * References the (external) source of pricing information, rules of application for the code this ChargeItem uses.
         * </p>
         * 
         * @param definitionUri
         *     Defining information about the code of this charge item
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder definitionUri(Uri... definitionUri) {
            for (Uri value : definitionUri) {
                this.definitionUri.add(value);
            }
            return this;
        }

        /**
         * <p>
         * References the (external) source of pricing information, rules of application for the code this ChargeItem uses.
         * </p>
         * 
         * @param definitionUri
         *     Defining information about the code of this charge item
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder definitionUri(Collection<Uri> definitionUri) {
            this.definitionUri.addAll(definitionUri);
            return this;
        }

        /**
         * <p>
         * References the source of pricing information, rules of application for the code this ChargeItem uses.
         * </p>
         * 
         * @param definitionCanonical
         *     Resource defining the code of this ChargeItem
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder definitionCanonical(Canonical... definitionCanonical) {
            for (Canonical value : definitionCanonical) {
                this.definitionCanonical.add(value);
            }
            return this;
        }

        /**
         * <p>
         * References the source of pricing information, rules of application for the code this ChargeItem uses.
         * </p>
         * 
         * @param definitionCanonical
         *     Resource defining the code of this ChargeItem
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder definitionCanonical(Collection<Canonical> definitionCanonical) {
            this.definitionCanonical.addAll(definitionCanonical);
            return this;
        }

        /**
         * <p>
         * ChargeItems can be grouped to larger ChargeItems covering the whole set.
         * </p>
         * 
         * @param partOf
         *     Part of referenced ChargeItem
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partOf(Reference... partOf) {
            for (Reference value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * <p>
         * ChargeItems can be grouped to larger ChargeItems covering the whole set.
         * </p>
         * 
         * @param partOf
         *     Part of referenced ChargeItem
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf.addAll(partOf);
            return this;
        }

        /**
         * <p>
         * The encounter or episode of care that establishes the context for this event.
         * </p>
         * 
         * @param context
         *     Encounter / Episode associated with event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder context(Reference context) {
            this.context = context;
            return this;
        }

        /**
         * <p>
         * Date/time(s) or duration when the charged service was applied.
         * </p>
         * 
         * @param occurrence
         *     When the charged service was applied
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder occurrence(Element occurrence) {
            this.occurrence = occurrence;
            return this;
        }

        /**
         * <p>
         * Indicates who or what performed or participated in the charged service.
         * </p>
         * 
         * @param performer
         *     Who performed charged service
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder performer(Performer... performer) {
            for (Performer value : performer) {
                this.performer.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates who or what performed or participated in the charged service.
         * </p>
         * 
         * @param performer
         *     Who performed charged service
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder performer(Collection<Performer> performer) {
            this.performer.addAll(performer);
            return this;
        }

        /**
         * <p>
         * The organization requesting the service.
         * </p>
         * 
         * @param performingOrganization
         *     Organization providing the charged service
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder performingOrganization(Reference performingOrganization) {
            this.performingOrganization = performingOrganization;
            return this;
        }

        /**
         * <p>
         * The organization performing the service.
         * </p>
         * 
         * @param requestingOrganization
         *     Organization requesting the charged service
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder requestingOrganization(Reference requestingOrganization) {
            this.requestingOrganization = requestingOrganization;
            return this;
        }

        /**
         * <p>
         * The financial cost center permits the tracking of charge attribution.
         * </p>
         * 
         * @param costCenter
         *     Organization that has ownership of the (potential, future) revenue
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder costCenter(Reference costCenter) {
            this.costCenter = costCenter;
            return this;
        }

        /**
         * <p>
         * Quantity of which the charge item has been serviced.
         * </p>
         * 
         * @param quantity
         *     Quantity of which the charge item has been serviced
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder quantity(Quantity quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * <p>
         * The anatomical location where the related service has been applied.
         * </p>
         * 
         * @param bodysite
         *     Anatomical location, if relevant
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder bodysite(CodeableConcept... bodysite) {
            for (CodeableConcept value : bodysite) {
                this.bodysite.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The anatomical location where the related service has been applied.
         * </p>
         * 
         * @param bodysite
         *     Anatomical location, if relevant
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder bodysite(Collection<CodeableConcept> bodysite) {
            this.bodysite.addAll(bodysite);
            return this;
        }

        /**
         * <p>
         * Factor overriding the factor determined by the rules associated with the code.
         * </p>
         * 
         * @param factorOverride
         *     Factor overriding the associated rules
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder factorOverride(Decimal factorOverride) {
            this.factorOverride = factorOverride;
            return this;
        }

        /**
         * <p>
         * Total price of the charge overriding the list price associated with the code.
         * </p>
         * 
         * @param priceOverride
         *     Price overriding the associated rules
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder priceOverride(Money priceOverride) {
            this.priceOverride = priceOverride;
            return this;
        }

        /**
         * <p>
         * If the list price or the rule-based factor associated with the code is overridden, this attribute can capture a text 
         * to indicate the reason for this action.
         * </p>
         * 
         * @param overrideReason
         *     Reason for overriding the list price/factor
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder overrideReason(String overrideReason) {
            this.overrideReason = overrideReason;
            return this;
        }

        /**
         * <p>
         * The device, practitioner, etc. who entered the charge item.
         * </p>
         * 
         * @param enterer
         *     Individual who was entering
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder enterer(Reference enterer) {
            this.enterer = enterer;
            return this;
        }

        /**
         * <p>
         * Date the charge item was entered.
         * </p>
         * 
         * @param enteredDate
         *     Date the charge item was entered
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder enteredDate(DateTime enteredDate) {
            this.enteredDate = enteredDate;
            return this;
        }

        /**
         * <p>
         * Describes why the event occurred in coded or textual form.
         * </p>
         * 
         * @param reason
         *     Why was the charged service rendered?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reason(CodeableConcept... reason) {
            for (CodeableConcept value : reason) {
                this.reason.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Describes why the event occurred in coded or textual form.
         * </p>
         * 
         * @param reason
         *     Why was the charged service rendered?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reason(Collection<CodeableConcept> reason) {
            this.reason.addAll(reason);
            return this;
        }

        /**
         * <p>
         * Indicated the rendered service that caused this charge.
         * </p>
         * 
         * @param service
         *     Which rendered service is being charged?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder service(Reference... service) {
            for (Reference value : service) {
                this.service.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicated the rendered service that caused this charge.
         * </p>
         * 
         * @param service
         *     Which rendered service is being charged?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder service(Collection<Reference> service) {
            this.service.addAll(service);
            return this;
        }

        /**
         * <p>
         * Identifies the device, food, drug or other product being charged either by type code or reference to an instance.
         * </p>
         * 
         * @param product
         *     Product charged
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder product(Element product) {
            this.product = product;
            return this;
        }

        /**
         * <p>
         * Account into which this ChargeItems belongs.
         * </p>
         * 
         * @param account
         *     Account to place this charge
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder account(Reference... account) {
            for (Reference value : account) {
                this.account.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Account into which this ChargeItems belongs.
         * </p>
         * 
         * @param account
         *     Account to place this charge
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder account(Collection<Reference> account) {
            this.account.addAll(account);
            return this;
        }

        /**
         * <p>
         * Comments made about the event by the performer, subject or other participants.
         * </p>
         * 
         * @param note
         *     Comments made about the ChargeItem
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Annotation... note) {
            for (Annotation value : note) {
                this.note.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Comments made about the event by the performer, subject or other participants.
         * </p>
         * 
         * @param note
         *     Comments made about the ChargeItem
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Collection<Annotation> note) {
            this.note.addAll(note);
            return this;
        }

        /**
         * <p>
         * Further information supporting this charge.
         * </p>
         * 
         * @param supportingInformation
         *     Further information supporting this charge
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder supportingInformation(Reference... supportingInformation) {
            for (Reference value : supportingInformation) {
                this.supportingInformation.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Further information supporting this charge.
         * </p>
         * 
         * @param supportingInformation
         *     Further information supporting this charge
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder supportingInformation(Collection<Reference> supportingInformation) {
            this.supportingInformation.addAll(supportingInformation);
            return this;
        }

        @Override
        public ChargeItem build() {
            return new ChargeItem(this);
        }
    }

    /**
     * <p>
     * Indicates who or what performed or participated in the charged service.
     * </p>
     */
    public static class Performer extends BackboneElement {
        private final CodeableConcept function;
        private final Reference actor;

        private Performer(Builder builder) {
            super(builder);
            this.function = builder.function;
            this.actor = ValidationSupport.requireNonNull(builder.actor, "actor");
        }

        /**
         * <p>
         * Describes the type of performance or participation(e.g. primary surgeon, anesthesiologiest, etc.).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getFunction() {
            return function;
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(function, "function", visitor);
                    accept(actor, "actor", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Reference actor) {
            return new Builder(actor);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Reference actor;

            // optional
            private CodeableConcept function;

            private Builder(Reference actor) {
                super();
                this.actor = actor;
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
             *     A reference to this Builder instance.
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
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
             * Describes the type of performance or participation(e.g. primary surgeon, anesthesiologiest, etc.).
             * </p>
             * 
             * @param function
             *     What type of performance was done
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder function(CodeableConcept function) {
                this.function = function;
                return this;
            }

            @Override
            public Performer build() {
                return new Performer(this);
            }

            private static Builder from(Performer performer) {
                Builder builder = new Builder(performer.actor);
                builder.id = performer.id;
                builder.extension.addAll(performer.extension);
                builder.modifierExtension.addAll(performer.modifierExtension);
                builder.function = performer.function;
                return builder;
            }
        }
    }
}

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
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ChargeItemStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore 
 * referring not only to the product, but containing in addition details of the provision, like date, time, amounts and 
 * participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal 
 * cost allocation.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ChargeItem extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    private final List<Uri> definitionUri;
    private final List<Canonical> definitionCanonical;
    @Summary
    @Binding(
        bindingName = "ChargeItemStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Codes identifying the lifecycle stage of a ChargeItem.",
        valueSet = "http://hl7.org/fhir/ValueSet/chargeitem-status|4.3.0-CIBUILD"
    )
    @Required
    private final ChargeItemStatus status;
    @ReferenceTarget({ "ChargeItem" })
    private final List<Reference> partOf;
    @Summary
    @Binding(
        bindingName = "ChargeItemCode",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Example codes for billing purposes",
        valueSet = "http://hl7.org/fhir/ValueSet/chargeitem-billingcodes"
    )
    @Required
    private final CodeableConcept code;
    @Summary
    @ReferenceTarget({ "Patient", "Group" })
    @Required
    private final Reference subject;
    @Summary
    @ReferenceTarget({ "Encounter", "EpisodeOfCare" })
    private final Reference context;
    @Summary
    @Choice({ DateTime.class, Period.class, Timing.class })
    private final Element occurrence;
    private final List<Performer> performer;
    @ReferenceTarget({ "Organization" })
    private final Reference performingOrganization;
    @ReferenceTarget({ "Organization" })
    private final Reference requestingOrganization;
    @ReferenceTarget({ "Organization" })
    private final Reference costCenter;
    @Summary
    private final Quantity quantity;
    @Summary
    @Binding(
        bindingName = "BodySite",
        strength = BindingStrength.Value.EXAMPLE,
        description = "SNOMED CT Body site concepts",
        valueSet = "http://hl7.org/fhir/ValueSet/body-site"
    )
    private final List<CodeableConcept> bodysite;
    private final Decimal factorOverride;
    private final Money priceOverride;
    private final String overrideReason;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization", "Patient", "Device", "RelatedPerson" })
    private final Reference enterer;
    @Summary
    private final DateTime enteredDate;
    @Binding(
        bindingName = "ChargeItemReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "ICD 10 diagnosis codes",
        valueSet = "http://hl7.org/fhir/ValueSet/icd-10"
    )
    private final List<CodeableConcept> reason;
    @ReferenceTarget({ "DiagnosticReport", "ImagingStudy", "Immunization", "MedicationAdministration", "MedicationDispense", "Observation", "Procedure", "SupplyDelivery" })
    private final List<Reference> service;
    @ReferenceTarget({ "Device", "Medication", "Substance" })
    @Choice({ Reference.class, CodeableConcept.class })
    @Binding(
        bindingName = "ChargeItemProduct",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Example binding for product type.",
        valueSet = "http://hl7.org/fhir/ValueSet/device-kind"
    )
    private final Element product;
    @Summary
    @ReferenceTarget({ "Account" })
    private final List<Reference> account;
    private final List<Annotation> note;
    private final List<Reference> supportingInformation;

    private ChargeItem(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        definitionUri = Collections.unmodifiableList(builder.definitionUri);
        definitionCanonical = Collections.unmodifiableList(builder.definitionCanonical);
        status = builder.status;
        partOf = Collections.unmodifiableList(builder.partOf);
        code = builder.code;
        subject = builder.subject;
        context = builder.context;
        occurrence = builder.occurrence;
        performer = Collections.unmodifiableList(builder.performer);
        performingOrganization = builder.performingOrganization;
        requestingOrganization = builder.requestingOrganization;
        costCenter = builder.costCenter;
        quantity = builder.quantity;
        bodysite = Collections.unmodifiableList(builder.bodysite);
        factorOverride = builder.factorOverride;
        priceOverride = builder.priceOverride;
        overrideReason = builder.overrideReason;
        enterer = builder.enterer;
        enteredDate = builder.enteredDate;
        reason = Collections.unmodifiableList(builder.reason);
        service = Collections.unmodifiableList(builder.service);
        product = builder.product;
        account = Collections.unmodifiableList(builder.account);
        note = Collections.unmodifiableList(builder.note);
        supportingInformation = Collections.unmodifiableList(builder.supportingInformation);
    }

    /**
     * Identifiers assigned to this event performer or other systems.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * References the (external) source of pricing information, rules of application for the code this ChargeItem uses.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri} that may be empty.
     */
    public List<Uri> getDefinitionUri() {
        return definitionUri;
    }

    /**
     * References the source of pricing information, rules of application for the code this ChargeItem uses.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getDefinitionCanonical() {
        return definitionCanonical;
    }

    /**
     * The current state of the ChargeItem.
     * 
     * @return
     *     An immutable object of type {@link ChargeItemStatus} that is non-null.
     */
    public ChargeItemStatus getStatus() {
        return status;
    }

    /**
     * ChargeItems can be grouped to larger ChargeItems covering the whole set.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * A code that identifies the charge, like a billing code.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * The individual or set of individuals the action is being or was performed on.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The encounter or episode of care that establishes the context for this event.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getContext() {
        return context;
    }

    /**
     * Date/time(s) or duration when the charged service was applied.
     * 
     * @return
     *     An immutable object of type {@link DateTime}, {@link Period} or {@link Timing} that may be null.
     */
    public Element getOccurrence() {
        return occurrence;
    }

    /**
     * Indicates who or what performed or participated in the charged service.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Performer} that may be empty.
     */
    public List<Performer> getPerformer() {
        return performer;
    }

    /**
     * The organization requesting the service.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPerformingOrganization() {
        return performingOrganization;
    }

    /**
     * The organization performing the service.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRequestingOrganization() {
        return requestingOrganization;
    }

    /**
     * The financial cost center permits the tracking of charge attribution.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getCostCenter() {
        return costCenter;
    }

    /**
     * Quantity of which the charge item has been serviced.
     * 
     * @return
     *     An immutable object of type {@link Quantity} that may be null.
     */
    public Quantity getQuantity() {
        return quantity;
    }

    /**
     * The anatomical location where the related service has been applied.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getBodysite() {
        return bodysite;
    }

    /**
     * Factor overriding the factor determined by the rules associated with the code.
     * 
     * @return
     *     An immutable object of type {@link Decimal} that may be null.
     */
    public Decimal getFactorOverride() {
        return factorOverride;
    }

    /**
     * Total price of the charge overriding the list price associated with the code.
     * 
     * @return
     *     An immutable object of type {@link Money} that may be null.
     */
    public Money getPriceOverride() {
        return priceOverride;
    }

    /**
     * If the list price or the rule-based factor associated with the code is overridden, this attribute can capture a text 
     * to indicate the reason for this action.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getOverrideReason() {
        return overrideReason;
    }

    /**
     * The device, practitioner, etc. who entered the charge item.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEnterer() {
        return enterer;
    }

    /**
     * Date the charge item was entered.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getEnteredDate() {
        return enteredDate;
    }

    /**
     * Describes why the event occurred in coded or textual form.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReason() {
        return reason;
    }

    /**
     * Indicated the rendered service that caused this charge.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getService() {
        return service;
    }

    /**
     * Identifies the device, food, drug or other product being charged either by type code or reference to an instance.
     * 
     * @return
     *     An immutable object of type {@link Reference} or {@link CodeableConcept} that may be null.
     */
    public Element getProduct() {
        return product;
    }

    /**
     * Account into which this ChargeItems belongs.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getAccount() {
        return account;
    }

    /**
     * Comments made about the event by the performer, subject or other participants.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * Further information supporting this charge.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSupportingInformation() {
        return supportingInformation;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !definitionUri.isEmpty() || 
            !definitionCanonical.isEmpty() || 
            (status != null) || 
            !partOf.isEmpty() || 
            (code != null) || 
            (subject != null) || 
            (context != null) || 
            (occurrence != null) || 
            !performer.isEmpty() || 
            (performingOrganization != null) || 
            (requestingOrganization != null) || 
            (costCenter != null) || 
            (quantity != null) || 
            !bodysite.isEmpty() || 
            (factorOverride != null) || 
            (priceOverride != null) || 
            (overrideReason != null) || 
            (enterer != null) || 
            (enteredDate != null) || 
            !reason.isEmpty() || 
            !service.isEmpty() || 
            (product != null) || 
            !account.isEmpty() || 
            !note.isEmpty() || 
            !supportingInformation.isEmpty();
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
                accept(definitionUri, "definitionUri", visitor, Uri.class);
                accept(definitionCanonical, "definitionCanonical", visitor, Canonical.class);
                accept(status, "status", visitor);
                accept(partOf, "partOf", visitor, Reference.class);
                accept(code, "code", visitor);
                accept(subject, "subject", visitor);
                accept(context, "context", visitor);
                accept(occurrence, "occurrence", visitor);
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
                accept(product, "product", visitor);
                accept(account, "account", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
                accept(supportingInformation, "supportingInformation", visitor, Reference.class);
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
        ChargeItem other = (ChargeItem) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(definitionUri, other.definitionUri) && 
            Objects.equals(definitionCanonical, other.definitionCanonical) && 
            Objects.equals(status, other.status) && 
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(code, other.code) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(context, other.context) && 
            Objects.equals(occurrence, other.occurrence) && 
            Objects.equals(performer, other.performer) && 
            Objects.equals(performingOrganization, other.performingOrganization) && 
            Objects.equals(requestingOrganization, other.requestingOrganization) && 
            Objects.equals(costCenter, other.costCenter) && 
            Objects.equals(quantity, other.quantity) && 
            Objects.equals(bodysite, other.bodysite) && 
            Objects.equals(factorOverride, other.factorOverride) && 
            Objects.equals(priceOverride, other.priceOverride) && 
            Objects.equals(overrideReason, other.overrideReason) && 
            Objects.equals(enterer, other.enterer) && 
            Objects.equals(enteredDate, other.enteredDate) && 
            Objects.equals(reason, other.reason) && 
            Objects.equals(service, other.service) && 
            Objects.equals(product, other.product) && 
            Objects.equals(account, other.account) && 
            Objects.equals(note, other.note) && 
            Objects.equals(supportingInformation, other.supportingInformation);
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
                definitionUri, 
                definitionCanonical, 
                status, 
                partOf, 
                code, 
                subject, 
                context, 
                occurrence, 
                performer, 
                performingOrganization, 
                requestingOrganization, 
                costCenter, 
                quantity, 
                bodysite, 
                factorOverride, 
                priceOverride, 
                overrideReason, 
                enterer, 
                enteredDate, 
                reason, 
                service, 
                product, 
                account, 
                note, 
                supportingInformation);
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
        private List<Uri> definitionUri = new ArrayList<>();
        private List<Canonical> definitionCanonical = new ArrayList<>();
        private ChargeItemStatus status;
        private List<Reference> partOf = new ArrayList<>();
        private CodeableConcept code;
        private Reference subject;
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
         * Identifiers assigned to this event performer or other systems.
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
         * Identifiers assigned to this event performer or other systems.
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
         * References the (external) source of pricing information, rules of application for the code this ChargeItem uses.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param definitionUri
         *     Defining information about the code of this charge item
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder definitionUri(Uri... definitionUri) {
            for (Uri value : definitionUri) {
                this.definitionUri.add(value);
            }
            return this;
        }

        /**
         * References the (external) source of pricing information, rules of application for the code this ChargeItem uses.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param definitionUri
         *     Defining information about the code of this charge item
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder definitionUri(Collection<Uri> definitionUri) {
            this.definitionUri = new ArrayList<>(definitionUri);
            return this;
        }

        /**
         * References the source of pricing information, rules of application for the code this ChargeItem uses.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param definitionCanonical
         *     Resource defining the code of this ChargeItem
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder definitionCanonical(Canonical... definitionCanonical) {
            for (Canonical value : definitionCanonical) {
                this.definitionCanonical.add(value);
            }
            return this;
        }

        /**
         * References the source of pricing information, rules of application for the code this ChargeItem uses.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param definitionCanonical
         *     Resource defining the code of this ChargeItem
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder definitionCanonical(Collection<Canonical> definitionCanonical) {
            this.definitionCanonical = new ArrayList<>(definitionCanonical);
            return this;
        }

        /**
         * The current state of the ChargeItem.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     planned | billable | not-billable | aborted | billed | entered-in-error | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(ChargeItemStatus status) {
            this.status = status;
            return this;
        }

        /**
         * ChargeItems can be grouped to larger ChargeItems covering the whole set.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ChargeItem}</li>
         * </ul>
         * 
         * @param partOf
         *     Part of referenced ChargeItem
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Reference... partOf) {
            for (Reference value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * ChargeItems can be grouped to larger ChargeItems covering the whole set.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ChargeItem}</li>
         * </ul>
         * 
         * @param partOf
         *     Part of referenced ChargeItem
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf = new ArrayList<>(partOf);
            return this;
        }

        /**
         * A code that identifies the charge, like a billing code.
         * 
         * <p>This element is required.
         * 
         * @param code
         *     A code that identifies the charge, like a billing code
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * The individual or set of individuals the action is being or was performed on.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Group}</li>
         * </ul>
         * 
         * @param subject
         *     Individual service was done for/to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The encounter or episode of care that establishes the context for this event.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * <li>{@link EpisodeOfCare}</li>
         * </ul>
         * 
         * @param context
         *     Encounter / Episode associated with event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder context(Reference context) {
            this.context = context;
            return this;
        }

        /**
         * Date/time(s) or duration when the charged service was applied.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link DateTime}</li>
         * <li>{@link Period}</li>
         * <li>{@link Timing}</li>
         * </ul>
         * 
         * @param occurrence
         *     When the charged service was applied
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder occurrence(Element occurrence) {
            this.occurrence = occurrence;
            return this;
        }

        /**
         * Indicates who or what performed or participated in the charged service.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param performer
         *     Who performed charged service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performer(Performer... performer) {
            for (Performer value : performer) {
                this.performer.add(value);
            }
            return this;
        }

        /**
         * Indicates who or what performed or participated in the charged service.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param performer
         *     Who performed charged service
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder performer(Collection<Performer> performer) {
            this.performer = new ArrayList<>(performer);
            return this;
        }

        /**
         * The organization requesting the service.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param performingOrganization
         *     Organization providing the charged service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performingOrganization(Reference performingOrganization) {
            this.performingOrganization = performingOrganization;
            return this;
        }

        /**
         * The organization performing the service.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param requestingOrganization
         *     Organization requesting the charged service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder requestingOrganization(Reference requestingOrganization) {
            this.requestingOrganization = requestingOrganization;
            return this;
        }

        /**
         * The financial cost center permits the tracking of charge attribution.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param costCenter
         *     Organization that has ownership of the (potential, future) revenue
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder costCenter(Reference costCenter) {
            this.costCenter = costCenter;
            return this;
        }

        /**
         * Quantity of which the charge item has been serviced.
         * 
         * @param quantity
         *     Quantity of which the charge item has been serviced
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder quantity(Quantity quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * The anatomical location where the related service has been applied.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param bodysite
         *     Anatomical location, if relevant
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder bodysite(CodeableConcept... bodysite) {
            for (CodeableConcept value : bodysite) {
                this.bodysite.add(value);
            }
            return this;
        }

        /**
         * The anatomical location where the related service has been applied.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param bodysite
         *     Anatomical location, if relevant
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder bodysite(Collection<CodeableConcept> bodysite) {
            this.bodysite = new ArrayList<>(bodysite);
            return this;
        }

        /**
         * Factor overriding the factor determined by the rules associated with the code.
         * 
         * @param factorOverride
         *     Factor overriding the associated rules
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder factorOverride(Decimal factorOverride) {
            this.factorOverride = factorOverride;
            return this;
        }

        /**
         * Total price of the charge overriding the list price associated with the code.
         * 
         * @param priceOverride
         *     Price overriding the associated rules
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder priceOverride(Money priceOverride) {
            this.priceOverride = priceOverride;
            return this;
        }

        /**
         * Convenience method for setting {@code overrideReason}.
         * 
         * @param overrideReason
         *     Reason for overriding the list price/factor
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #overrideReason(com.ibm.fhir.model.type.String)
         */
        public Builder overrideReason(java.lang.String overrideReason) {
            this.overrideReason = (overrideReason == null) ? null : String.of(overrideReason);
            return this;
        }

        /**
         * If the list price or the rule-based factor associated with the code is overridden, this attribute can capture a text 
         * to indicate the reason for this action.
         * 
         * @param overrideReason
         *     Reason for overriding the list price/factor
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder overrideReason(String overrideReason) {
            this.overrideReason = overrideReason;
            return this;
        }

        /**
         * The device, practitioner, etc. who entered the charge item.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * <li>{@link Patient}</li>
         * <li>{@link Device}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
         * 
         * @param enterer
         *     Individual who was entering
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder enterer(Reference enterer) {
            this.enterer = enterer;
            return this;
        }

        /**
         * Date the charge item was entered.
         * 
         * @param enteredDate
         *     Date the charge item was entered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder enteredDate(DateTime enteredDate) {
            this.enteredDate = enteredDate;
            return this;
        }

        /**
         * Describes why the event occurred in coded or textual form.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reason
         *     Why was the charged service rendered?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reason(CodeableConcept... reason) {
            for (CodeableConcept value : reason) {
                this.reason.add(value);
            }
            return this;
        }

        /**
         * Describes why the event occurred in coded or textual form.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reason
         *     Why was the charged service rendered?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reason(Collection<CodeableConcept> reason) {
            this.reason = new ArrayList<>(reason);
            return this;
        }

        /**
         * Indicated the rendered service that caused this charge.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DiagnosticReport}</li>
         * <li>{@link ImagingStudy}</li>
         * <li>{@link Immunization}</li>
         * <li>{@link MedicationAdministration}</li>
         * <li>{@link MedicationDispense}</li>
         * <li>{@link Observation}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link SupplyDelivery}</li>
         * </ul>
         * 
         * @param service
         *     Which rendered service is being charged?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder service(Reference... service) {
            for (Reference value : service) {
                this.service.add(value);
            }
            return this;
        }

        /**
         * Indicated the rendered service that caused this charge.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DiagnosticReport}</li>
         * <li>{@link ImagingStudy}</li>
         * <li>{@link Immunization}</li>
         * <li>{@link MedicationAdministration}</li>
         * <li>{@link MedicationDispense}</li>
         * <li>{@link Observation}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link SupplyDelivery}</li>
         * </ul>
         * 
         * @param service
         *     Which rendered service is being charged?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder service(Collection<Reference> service) {
            this.service = new ArrayList<>(service);
            return this;
        }

        /**
         * Identifies the device, food, drug or other product being charged either by type code or reference to an instance.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Reference}</li>
         * <li>{@link CodeableConcept}</li>
         * </ul>
         * 
         * When of type {@link Reference}, the allowed resource types for this reference are:
         * <ul>
         * <li>{@link Device}</li>
         * <li>{@link Medication}</li>
         * <li>{@link Substance}</li>
         * </ul>
         * 
         * @param product
         *     Product charged
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder product(Element product) {
            this.product = product;
            return this;
        }

        /**
         * Account into which this ChargeItems belongs.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Account}</li>
         * </ul>
         * 
         * @param account
         *     Account to place this charge
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder account(Reference... account) {
            for (Reference value : account) {
                this.account.add(value);
            }
            return this;
        }

        /**
         * Account into which this ChargeItems belongs.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Account}</li>
         * </ul>
         * 
         * @param account
         *     Account to place this charge
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder account(Collection<Reference> account) {
            this.account = new ArrayList<>(account);
            return this;
        }

        /**
         * Comments made about the event by the performer, subject or other participants.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments made about the ChargeItem
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
         * Comments made about the event by the performer, subject or other participants.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments made about the ChargeItem
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
         * Further information supporting this charge.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param supportingInformation
         *     Further information supporting this charge
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supportingInformation(Reference... supportingInformation) {
            for (Reference value : supportingInformation) {
                this.supportingInformation.add(value);
            }
            return this;
        }

        /**
         * Further information supporting this charge.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param supportingInformation
         *     Further information supporting this charge
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder supportingInformation(Collection<Reference> supportingInformation) {
            this.supportingInformation = new ArrayList<>(supportingInformation);
            return this;
        }

        /**
         * Build the {@link ChargeItem}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>code</li>
         * <li>subject</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ChargeItem}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ChargeItem per the base specification
         */
        @Override
        public ChargeItem build() {
            ChargeItem chargeItem = new ChargeItem(this);
            if (validating) {
                validate(chargeItem);
            }
            return chargeItem;
        }

        protected void validate(ChargeItem chargeItem) {
            super.validate(chargeItem);
            ValidationSupport.checkList(chargeItem.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(chargeItem.definitionUri, "definitionUri", Uri.class);
            ValidationSupport.checkList(chargeItem.definitionCanonical, "definitionCanonical", Canonical.class);
            ValidationSupport.requireNonNull(chargeItem.status, "status");
            ValidationSupport.checkList(chargeItem.partOf, "partOf", Reference.class);
            ValidationSupport.requireNonNull(chargeItem.code, "code");
            ValidationSupport.requireNonNull(chargeItem.subject, "subject");
            ValidationSupport.choiceElement(chargeItem.occurrence, "occurrence", DateTime.class, Period.class, Timing.class);
            ValidationSupport.checkList(chargeItem.performer, "performer", Performer.class);
            ValidationSupport.checkList(chargeItem.bodysite, "bodysite", CodeableConcept.class);
            ValidationSupport.checkList(chargeItem.reason, "reason", CodeableConcept.class);
            ValidationSupport.checkList(chargeItem.service, "service", Reference.class);
            ValidationSupport.choiceElement(chargeItem.product, "product", Reference.class, CodeableConcept.class);
            ValidationSupport.checkList(chargeItem.account, "account", Reference.class);
            ValidationSupport.checkList(chargeItem.note, "note", Annotation.class);
            ValidationSupport.checkList(chargeItem.supportingInformation, "supportingInformation", Reference.class);
            ValidationSupport.checkReferenceType(chargeItem.partOf, "partOf", "ChargeItem");
            ValidationSupport.checkReferenceType(chargeItem.subject, "subject", "Patient", "Group");
            ValidationSupport.checkReferenceType(chargeItem.context, "context", "Encounter", "EpisodeOfCare");
            ValidationSupport.checkReferenceType(chargeItem.performingOrganization, "performingOrganization", "Organization");
            ValidationSupport.checkReferenceType(chargeItem.requestingOrganization, "requestingOrganization", "Organization");
            ValidationSupport.checkReferenceType(chargeItem.costCenter, "costCenter", "Organization");
            ValidationSupport.checkReferenceType(chargeItem.enterer, "enterer", "Practitioner", "PractitionerRole", "Organization", "Patient", "Device", "RelatedPerson");
            ValidationSupport.checkReferenceType(chargeItem.service, "service", "DiagnosticReport", "ImagingStudy", "Immunization", "MedicationAdministration", "MedicationDispense", "Observation", "Procedure", "SupplyDelivery");
            ValidationSupport.checkReferenceType(chargeItem.product, "product", "Device", "Medication", "Substance");
            ValidationSupport.checkReferenceType(chargeItem.account, "account", "Account");
        }

        protected Builder from(ChargeItem chargeItem) {
            super.from(chargeItem);
            identifier.addAll(chargeItem.identifier);
            definitionUri.addAll(chargeItem.definitionUri);
            definitionCanonical.addAll(chargeItem.definitionCanonical);
            status = chargeItem.status;
            partOf.addAll(chargeItem.partOf);
            code = chargeItem.code;
            subject = chargeItem.subject;
            context = chargeItem.context;
            occurrence = chargeItem.occurrence;
            performer.addAll(chargeItem.performer);
            performingOrganization = chargeItem.performingOrganization;
            requestingOrganization = chargeItem.requestingOrganization;
            costCenter = chargeItem.costCenter;
            quantity = chargeItem.quantity;
            bodysite.addAll(chargeItem.bodysite);
            factorOverride = chargeItem.factorOverride;
            priceOverride = chargeItem.priceOverride;
            overrideReason = chargeItem.overrideReason;
            enterer = chargeItem.enterer;
            enteredDate = chargeItem.enteredDate;
            reason.addAll(chargeItem.reason);
            service.addAll(chargeItem.service);
            product = chargeItem.product;
            account.addAll(chargeItem.account);
            note.addAll(chargeItem.note);
            supportingInformation.addAll(chargeItem.supportingInformation);
            return this;
        }
    }

    /**
     * Indicates who or what performed or participated in the charged service.
     */
    public static class Performer extends BackboneElement {
        @Binding(
            bindingName = "ChargeItemPerformerFunction",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes describing the types of functional roles performers can take on when performing events.",
            valueSet = "http://hl7.org/fhir/ValueSet/performer-role"
        )
        private final CodeableConcept function;
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization", "CareTeam", "Patient", "Device", "RelatedPerson" })
        @Required
        private final Reference actor;

        private Performer(Builder builder) {
            super(builder);
            function = builder.function;
            actor = builder.actor;
        }

        /**
         * Describes the type of performance or participation(e.g. primary surgeon, anesthesiologiest, etc.).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getFunction() {
            return function;
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
                (function != null) || 
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
                    accept(function, "function", visitor);
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
            Performer other = (Performer) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(function, other.function) && 
                Objects.equals(actor, other.actor);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    function, 
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
            private CodeableConcept function;
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
             * Describes the type of performance or participation(e.g. primary surgeon, anesthesiologiest, etc.).
             * 
             * @param function
             *     What type of performance was done
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder function(CodeableConcept function) {
                this.function = function;
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
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Organization}</li>
             * <li>{@link CareTeam}</li>
             * <li>{@link Patient}</li>
             * <li>{@link Device}</li>
             * <li>{@link RelatedPerson}</li>
             * </ul>
             * 
             * @param actor
             *     Individual who was performing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder actor(Reference actor) {
                this.actor = actor;
                return this;
            }

            /**
             * Build the {@link Performer}
             * 
             * <p>Required elements:
             * <ul>
             * <li>actor</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Performer}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Performer per the base specification
             */
            @Override
            public Performer build() {
                Performer performer = new Performer(this);
                if (validating) {
                    validate(performer);
                }
                return performer;
            }

            protected void validate(Performer performer) {
                super.validate(performer);
                ValidationSupport.requireNonNull(performer.actor, "actor");
                ValidationSupport.checkReferenceType(performer.actor, "actor", "Practitioner", "PractitionerRole", "Organization", "CareTeam", "Patient", "Device", "RelatedPerson");
                ValidationSupport.requireValueOrChildren(performer);
            }

            protected Builder from(Performer performer) {
                super.from(performer);
                function = performer.function;
                actor = performer.actor;
                return this;
            }
        }
    }
}

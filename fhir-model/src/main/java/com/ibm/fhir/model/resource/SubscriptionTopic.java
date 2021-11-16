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
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.CriteriaNotExistsBehavior;
import com.ibm.fhir.model.type.code.MethodCode;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.type.code.SubscriptionTopicFilterBySearchModifier;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Describes a stream of resource state changes or events and annotated with labels useful to filter projections from 
 * this topic.
 * 
 * <p>Maturity level: FMM0 (draft)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.DRAFT
)
@Constraint(
    id = "subscriptionTopic-0",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/SubscriptionTopic",
    generated = true
)
@Constraint(
    id = "subscriptionTopic-1",
    level = "Warning",
    location = "resourceTrigger.resource",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/defined-types",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/defined-types', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/SubscriptionTopic",
    generated = true
)
@Constraint(
    id = "subscriptionTopic-2",
    level = "Warning",
    location = "eventTrigger.resource",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/defined-types",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/defined-types', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/SubscriptionTopic",
    generated = true
)
@Constraint(
    id = "subscriptionTopic-3",
    level = "Warning",
    location = "canFilterBy.resource",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/defined-types",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/defined-types', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/SubscriptionTopic",
    generated = true
)
@Constraint(
    id = "subscriptionTopic-4",
    level = "Warning",
    location = "notificationShape.resource",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/defined-types",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/defined-types', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/SubscriptionTopic",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SubscriptionTopic extends DomainResource {
    @Summary
    @Required
    private final Uri url;
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final String version;
    @Summary
    private final String title;
    @Summary
    private final List<Canonical> derivedFrom;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status|4.0.1"
    )
    @Required
    private final PublicationStatus status;
    @Summary
    private final Boolean experimental;
    @Summary
    private final DateTime date;
    @Summary
    private final String publisher;
    @Summary
    private final List<ContactDetail> contact;
    private final Markdown description;
    @Summary
    private final List<UsageContext> useContext;
    @Summary
    @Binding(
        bindingName = "Jurisdiction",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Countries and regions within which this artifact is targeted for use.",
        valueSet = "http://hl7.org/fhir/ValueSet/jurisdiction"
    )
    private final List<CodeableConcept> jurisdiction;
    private final Markdown purpose;
    private final Markdown copyright;
    private final Date approvalDate;
    private final Date lastReviewDate;
    @Summary
    private final Period effectivePeriod;
    @Summary
    private final List<ResourceTrigger> resourceTrigger;
    @Summary
    private final List<EventTrigger> eventTrigger;
    @Summary
    private final List<CanFilterBy> canFilterBy;
    @Summary
    private final List<NotificationShape> notificationShape;

    private SubscriptionTopic(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        title = builder.title;
        derivedFrom = Collections.unmodifiableList(builder.derivedFrom);
        status = builder.status;
        experimental = builder.experimental;
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        description = builder.description;
        useContext = Collections.unmodifiableList(builder.useContext);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
        purpose = builder.purpose;
        copyright = builder.copyright;
        approvalDate = builder.approvalDate;
        lastReviewDate = builder.lastReviewDate;
        effectivePeriod = builder.effectivePeriod;
        resourceTrigger = Collections.unmodifiableList(builder.resourceTrigger);
        eventTrigger = Collections.unmodifiableList(builder.eventTrigger);
        canFilterBy = Collections.unmodifiableList(builder.canFilterBy);
        notificationShape = Collections.unmodifiableList(builder.notificationShape);
    }

    /**
     * An absolute URL that is used to identify this SubscriptionTopic when it is referenced in a specification, model, 
     * design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this Topic is 
     * (or will be) published. The URL SHOULD include the major version of the Topic. For more information see [Technical and 
     * Business Versions](resource.html#versions).
     * 
     * @return
     *     An immutable object of type {@link Uri} that is non-null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * Business identifiers assigned to this SubscriptionTopic by the performer and/or other systems. These identifiers 
     * remain constant as the resource is updated and propagates from server to server.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the SubscriptionTopic when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the Topic author and is not expected 
     * to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. 
     * There is also no expectation that versions are orderable.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A short, descriptive, user-friendly title for the SubscriptionTopic, for example, "admission".
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The canonical URL pointing to another FHIR-defined SubscriptionTopic that is adhered to in whole or in part by this 
     * SubscriptionTopic.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getDerivedFrom() {
        return derivedFrom;
    }

    /**
     * The current state of the SubscriptionTopic.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A flag to indicate that this TopSubscriptionTopicic is authored for testing purposes (or 
     * education/evaluation/marketing), and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * For draft definitions, indicates the date of initial creation. For active definitions, represents the date of 
     * activation. For withdrawn definitions, indicates the date of withdrawal.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * Helps establish the "authority/credibility" of the SubscriptionTopic. May also allow for contact.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Contact details to assist a user in finding and communicating with the publisher.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getContact() {
        return contact;
    }

    /**
     * A free text natural language description of the Topic from the consumer's perspective.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used 
     * to assist with indexing and searching of code system definitions.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A jurisdiction in which the Topic is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * Explains why this Topic is needed and why it has been designed as it has.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * A copyright statement relating to the SubscriptionTopic and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the SubscriptionTopic.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * The date on which the asset content was approved by the publisher. Approval happens once when the content is 
     * officially approved for usage.
     * 
     * @return
     *     An immutable object of type {@link Date} that may be null.
     */
    public Date getApprovalDate() {
        return approvalDate;
    }

    /**
     * The date on which the asset content was last reviewed. Review happens periodically after that, but doesn't change the 
     * original approval date.
     * 
     * @return
     *     An immutable object of type {@link Date} that may be null.
     */
    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    /**
     * The period during which the SubscriptionTopic content was or is planned to be effective.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getEffectivePeriod() {
        return effectivePeriod;
    }

    /**
     * A definition of a resource-based event that triggers a notification based on the SubscriptionTopic. The criteria may 
     * be just a human readable description and/or a full FHIR search string or FHIRPath expression. Multiple triggers are 
     * considered OR joined (e.g., a resource update matching ANY of the definitions will trigger a notification).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ResourceTrigger} that may be empty.
     */
    public List<ResourceTrigger> getResourceTrigger() {
        return resourceTrigger;
    }

    /**
     * Event definition which can be used to trigger the SubscriptionTopic.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link EventTrigger} that may be empty.
     */
    public List<EventTrigger> getEventTrigger() {
        return eventTrigger;
    }

    /**
     * List of properties by which Subscriptions on the SubscriptionTopic can be filtered. May be defined Search Parameters 
     * (e.g., Encounter.patient) or parameters defined within this SubscriptionTopic context (e.g., hub.event).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CanFilterBy} that may be empty.
     */
    public List<CanFilterBy> getCanFilterBy() {
        return canFilterBy;
    }

    /**
     * List of properties to describe the shape (e.g., resources) included in notifications from this Subscription Topic.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link NotificationShape} that may be empty.
     */
    public List<NotificationShape> getNotificationShape() {
        return notificationShape;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
            !identifier.isEmpty() || 
            (version != null) || 
            (title != null) || 
            !derivedFrom.isEmpty() || 
            (status != null) || 
            (experimental != null) || 
            (date != null) || 
            (publisher != null) || 
            !contact.isEmpty() || 
            (description != null) || 
            !useContext.isEmpty() || 
            !jurisdiction.isEmpty() || 
            (purpose != null) || 
            (copyright != null) || 
            (approvalDate != null) || 
            (lastReviewDate != null) || 
            (effectivePeriod != null) || 
            !resourceTrigger.isEmpty() || 
            !eventTrigger.isEmpty() || 
            !canFilterBy.isEmpty() || 
            !notificationShape.isEmpty();
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
                accept(url, "url", visitor);
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(version, "version", visitor);
                accept(title, "title", visitor);
                accept(derivedFrom, "derivedFrom", visitor, Canonical.class);
                accept(status, "status", visitor);
                accept(experimental, "experimental", visitor);
                accept(date, "date", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(description, "description", visitor);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                accept(purpose, "purpose", visitor);
                accept(copyright, "copyright", visitor);
                accept(approvalDate, "approvalDate", visitor);
                accept(lastReviewDate, "lastReviewDate", visitor);
                accept(effectivePeriod, "effectivePeriod", visitor);
                accept(resourceTrigger, "resourceTrigger", visitor, ResourceTrigger.class);
                accept(eventTrigger, "eventTrigger", visitor, EventTrigger.class);
                accept(canFilterBy, "canFilterBy", visitor, CanFilterBy.class);
                accept(notificationShape, "notificationShape", visitor, NotificationShape.class);
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
        SubscriptionTopic other = (SubscriptionTopic) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(url, other.url) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(version, other.version) && 
            Objects.equals(title, other.title) && 
            Objects.equals(derivedFrom, other.derivedFrom) && 
            Objects.equals(status, other.status) && 
            Objects.equals(experimental, other.experimental) && 
            Objects.equals(date, other.date) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(description, other.description) && 
            Objects.equals(useContext, other.useContext) && 
            Objects.equals(jurisdiction, other.jurisdiction) && 
            Objects.equals(purpose, other.purpose) && 
            Objects.equals(copyright, other.copyright) && 
            Objects.equals(approvalDate, other.approvalDate) && 
            Objects.equals(lastReviewDate, other.lastReviewDate) && 
            Objects.equals(effectivePeriod, other.effectivePeriod) && 
            Objects.equals(resourceTrigger, other.resourceTrigger) && 
            Objects.equals(eventTrigger, other.eventTrigger) && 
            Objects.equals(canFilterBy, other.canFilterBy) && 
            Objects.equals(notificationShape, other.notificationShape);
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
                url, 
                identifier, 
                version, 
                title, 
                derivedFrom, 
                status, 
                experimental, 
                date, 
                publisher, 
                contact, 
                description, 
                useContext, 
                jurisdiction, 
                purpose, 
                copyright, 
                approvalDate, 
                lastReviewDate, 
                effectivePeriod, 
                resourceTrigger, 
                eventTrigger, 
                canFilterBy, 
                notificationShape);
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
        private Uri url;
        private List<Identifier> identifier = new ArrayList<>();
        private String version;
        private String title;
        private List<Canonical> derivedFrom = new ArrayList<>();
        private PublicationStatus status;
        private Boolean experimental;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown purpose;
        private Markdown copyright;
        private Date approvalDate;
        private Date lastReviewDate;
        private Period effectivePeriod;
        private List<ResourceTrigger> resourceTrigger = new ArrayList<>();
        private List<EventTrigger> eventTrigger = new ArrayList<>();
        private List<CanFilterBy> canFilterBy = new ArrayList<>();
        private List<NotificationShape> notificationShape = new ArrayList<>();

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
         * An absolute URL that is used to identify this SubscriptionTopic when it is referenced in a specification, model, 
         * design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this Topic is 
         * (or will be) published. The URL SHOULD include the major version of the Topic. For more information see [Technical and 
         * Business Versions](resource.html#versions).
         * 
         * <p>This element is required.
         * 
         * @param url
         *     Logical canonical URL to reference this SubscriptionTopic (globally unique)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * Business identifiers assigned to this SubscriptionTopic by the performer and/or other systems. These identifiers 
         * remain constant as the resource is updated and propagates from server to server.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for SubscriptionTopic
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
         * Business identifiers assigned to this SubscriptionTopic by the performer and/or other systems. These identifiers 
         * remain constant as the resource is updated and propagates from server to server.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for SubscriptionTopic
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
         * Convenience method for setting {@code version}.
         * 
         * @param version
         *     Business version of the SubscriptionTopic
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #version(com.ibm.fhir.model.type.String)
         */
        public Builder version(java.lang.String version) {
            this.version = (version == null) ? null : String.of(version);
            return this;
        }

        /**
         * The identifier that is used to identify this version of the SubscriptionTopic when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the Topic author and is not expected 
         * to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. 
         * There is also no expectation that versions are orderable.
         * 
         * @param version
         *     Business version of the SubscriptionTopic
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * Convenience method for setting {@code title}.
         * 
         * @param title
         *     Name for this SubscriptionTopic (Human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #title(com.ibm.fhir.model.type.String)
         */
        public Builder title(java.lang.String title) {
            this.title = (title == null) ? null : String.of(title);
            return this;
        }

        /**
         * A short, descriptive, user-friendly title for the SubscriptionTopic, for example, "admission".
         * 
         * @param title
         *     Name for this SubscriptionTopic (Human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The canonical URL pointing to another FHIR-defined SubscriptionTopic that is adhered to in whole or in part by this 
         * SubscriptionTopic.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param derivedFrom
         *     Based on FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder derivedFrom(Canonical... derivedFrom) {
            for (Canonical value : derivedFrom) {
                this.derivedFrom.add(value);
            }
            return this;
        }

        /**
         * The canonical URL pointing to another FHIR-defined SubscriptionTopic that is adhered to in whole or in part by this 
         * SubscriptionTopic.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param derivedFrom
         *     Based on FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder derivedFrom(Collection<Canonical> derivedFrom) {
            this.derivedFrom = new ArrayList<>(derivedFrom);
            return this;
        }

        /**
         * The current state of the SubscriptionTopic.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     draft | active | retired | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(PublicationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Convenience method for setting {@code experimental}.
         * 
         * @param experimental
         *     If for testing purposes, not real usage
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #experimental(com.ibm.fhir.model.type.Boolean)
         */
        public Builder experimental(java.lang.Boolean experimental) {
            this.experimental = (experimental == null) ? null : Boolean.of(experimental);
            return this;
        }

        /**
         * A flag to indicate that this TopSubscriptionTopicic is authored for testing purposes (or 
         * education/evaluation/marketing), and is not intended to be used for genuine usage.
         * 
         * @param experimental
         *     If for testing purposes, not real usage
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder experimental(Boolean experimental) {
            this.experimental = experimental;
            return this;
        }

        /**
         * For draft definitions, indicates the date of initial creation. For active definitions, represents the date of 
         * activation. For withdrawn definitions, indicates the date of withdrawal.
         * 
         * @param date
         *     Date status first applied
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * Convenience method for setting {@code publisher}.
         * 
         * @param publisher
         *     The name of the individual or organization that published the SubscriptionTopic
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #publisher(com.ibm.fhir.model.type.String)
         */
        public Builder publisher(java.lang.String publisher) {
            this.publisher = (publisher == null) ? null : String.of(publisher);
            return this;
        }

        /**
         * Helps establish the "authority/credibility" of the SubscriptionTopic. May also allow for contact.
         * 
         * @param publisher
         *     The name of the individual or organization that published the SubscriptionTopic
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        /**
         * Contact details to assist a user in finding and communicating with the publisher.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(ContactDetail... contact) {
            for (ContactDetail value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * Contact details to assist a user in finding and communicating with the publisher.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * A free text natural language description of the Topic from the consumer's perspective.
         * 
         * @param description
         *     Natural language description of the SubscriptionTopic
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used 
         * to assist with indexing and searching of code system definitions.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param useContext
         *     Content intends to support these contexts
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder useContext(UsageContext... useContext) {
            for (UsageContext value : useContext) {
                this.useContext.add(value);
            }
            return this;
        }

        /**
         * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used 
         * to assist with indexing and searching of code system definitions.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param useContext
         *     Content intends to support these contexts
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder useContext(Collection<UsageContext> useContext) {
            this.useContext = new ArrayList<>(useContext);
            return this;
        }

        /**
         * A jurisdiction in which the Topic is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction of the SubscriptionTopic (if applicable)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder jurisdiction(CodeableConcept... jurisdiction) {
            for (CodeableConcept value : jurisdiction) {
                this.jurisdiction.add(value);
            }
            return this;
        }

        /**
         * A jurisdiction in which the Topic is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction of the SubscriptionTopic (if applicable)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
            this.jurisdiction = new ArrayList<>(jurisdiction);
            return this;
        }

        /**
         * Explains why this Topic is needed and why it has been designed as it has.
         * 
         * @param purpose
         *     Why this SubscriptionTopic is defined
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * A copyright statement relating to the SubscriptionTopic and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the SubscriptionTopic.
         * 
         * @param copyright
         *     Use and/or publishing restrictions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder copyright(Markdown copyright) {
            this.copyright = copyright;
            return this;
        }

        /**
         * Convenience method for setting {@code approvalDate}.
         * 
         * @param approvalDate
         *     When SubscriptionTopic is/was approved by publisher
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #approvalDate(com.ibm.fhir.model.type.Date)
         */
        public Builder approvalDate(java.time.LocalDate approvalDate) {
            this.approvalDate = (approvalDate == null) ? null : Date.of(approvalDate);
            return this;
        }

        /**
         * The date on which the asset content was approved by the publisher. Approval happens once when the content is 
         * officially approved for usage.
         * 
         * @param approvalDate
         *     When SubscriptionTopic is/was approved by publisher
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder approvalDate(Date approvalDate) {
            this.approvalDate = approvalDate;
            return this;
        }

        /**
         * Convenience method for setting {@code lastReviewDate}.
         * 
         * @param lastReviewDate
         *     Date the Subscription Topic was last reviewed by the publisher
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #lastReviewDate(com.ibm.fhir.model.type.Date)
         */
        public Builder lastReviewDate(java.time.LocalDate lastReviewDate) {
            this.lastReviewDate = (lastReviewDate == null) ? null : Date.of(lastReviewDate);
            return this;
        }

        /**
         * The date on which the asset content was last reviewed. Review happens periodically after that, but doesn't change the 
         * original approval date.
         * 
         * @param lastReviewDate
         *     Date the Subscription Topic was last reviewed by the publisher
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lastReviewDate(Date lastReviewDate) {
            this.lastReviewDate = lastReviewDate;
            return this;
        }

        /**
         * The period during which the SubscriptionTopic content was or is planned to be effective.
         * 
         * @param effectivePeriod
         *     The effective date range for the SubscriptionTopic
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder effectivePeriod(Period effectivePeriod) {
            this.effectivePeriod = effectivePeriod;
            return this;
        }

        /**
         * A definition of a resource-based event that triggers a notification based on the SubscriptionTopic. The criteria may 
         * be just a human readable description and/or a full FHIR search string or FHIRPath expression. Multiple triggers are 
         * considered OR joined (e.g., a resource update matching ANY of the definitions will trigger a notification).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param resourceTrigger
         *     Definition of a resource-based trigger for the subscription topic
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder resourceTrigger(ResourceTrigger... resourceTrigger) {
            for (ResourceTrigger value : resourceTrigger) {
                this.resourceTrigger.add(value);
            }
            return this;
        }

        /**
         * A definition of a resource-based event that triggers a notification based on the SubscriptionTopic. The criteria may 
         * be just a human readable description and/or a full FHIR search string or FHIRPath expression. Multiple triggers are 
         * considered OR joined (e.g., a resource update matching ANY of the definitions will trigger a notification).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param resourceTrigger
         *     Definition of a resource-based trigger for the subscription topic
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder resourceTrigger(Collection<ResourceTrigger> resourceTrigger) {
            this.resourceTrigger = new ArrayList<>(resourceTrigger);
            return this;
        }

        /**
         * Event definition which can be used to trigger the SubscriptionTopic.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param eventTrigger
         *     Event definitions the SubscriptionTopic
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder eventTrigger(EventTrigger... eventTrigger) {
            for (EventTrigger value : eventTrigger) {
                this.eventTrigger.add(value);
            }
            return this;
        }

        /**
         * Event definition which can be used to trigger the SubscriptionTopic.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param eventTrigger
         *     Event definitions the SubscriptionTopic
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder eventTrigger(Collection<EventTrigger> eventTrigger) {
            this.eventTrigger = new ArrayList<>(eventTrigger);
            return this;
        }

        /**
         * List of properties by which Subscriptions on the SubscriptionTopic can be filtered. May be defined Search Parameters 
         * (e.g., Encounter.patient) or parameters defined within this SubscriptionTopic context (e.g., hub.event).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param canFilterBy
         *     Properties by which a Subscription can filter notifications from the SubscriptionTopic
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder canFilterBy(CanFilterBy... canFilterBy) {
            for (CanFilterBy value : canFilterBy) {
                this.canFilterBy.add(value);
            }
            return this;
        }

        /**
         * List of properties by which Subscriptions on the SubscriptionTopic can be filtered. May be defined Search Parameters 
         * (e.g., Encounter.patient) or parameters defined within this SubscriptionTopic context (e.g., hub.event).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param canFilterBy
         *     Properties by which a Subscription can filter notifications from the SubscriptionTopic
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder canFilterBy(Collection<CanFilterBy> canFilterBy) {
            this.canFilterBy = new ArrayList<>(canFilterBy);
            return this;
        }

        /**
         * List of properties to describe the shape (e.g., resources) included in notifications from this Subscription Topic.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param notificationShape
         *     Properties for describing the shape of notifications generated by this topic
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder notificationShape(NotificationShape... notificationShape) {
            for (NotificationShape value : notificationShape) {
                this.notificationShape.add(value);
            }
            return this;
        }

        /**
         * List of properties to describe the shape (e.g., resources) included in notifications from this Subscription Topic.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param notificationShape
         *     Properties for describing the shape of notifications generated by this topic
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder notificationShape(Collection<NotificationShape> notificationShape) {
            this.notificationShape = new ArrayList<>(notificationShape);
            return this;
        }

        /**
         * Build the {@link SubscriptionTopic}
         * 
         * <p>Required elements:
         * <ul>
         * <li>url</li>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link SubscriptionTopic}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid SubscriptionTopic per the base specification
         */
        @Override
        public SubscriptionTopic build() {
            SubscriptionTopic subscriptionTopic = new SubscriptionTopic(this);
            if (validating) {
                validate(subscriptionTopic);
            }
            return subscriptionTopic;
        }

        protected void validate(SubscriptionTopic subscriptionTopic) {
            super.validate(subscriptionTopic);
            ValidationSupport.requireNonNull(subscriptionTopic.url, "url");
            ValidationSupport.checkList(subscriptionTopic.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(subscriptionTopic.derivedFrom, "derivedFrom", Canonical.class);
            ValidationSupport.requireNonNull(subscriptionTopic.status, "status");
            ValidationSupport.checkList(subscriptionTopic.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(subscriptionTopic.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(subscriptionTopic.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.checkList(subscriptionTopic.resourceTrigger, "resourceTrigger", ResourceTrigger.class);
            ValidationSupport.checkList(subscriptionTopic.eventTrigger, "eventTrigger", EventTrigger.class);
            ValidationSupport.checkList(subscriptionTopic.canFilterBy, "canFilterBy", CanFilterBy.class);
            ValidationSupport.checkList(subscriptionTopic.notificationShape, "notificationShape", NotificationShape.class);
        }

        protected Builder from(SubscriptionTopic subscriptionTopic) {
            super.from(subscriptionTopic);
            url = subscriptionTopic.url;
            identifier.addAll(subscriptionTopic.identifier);
            version = subscriptionTopic.version;
            title = subscriptionTopic.title;
            derivedFrom.addAll(subscriptionTopic.derivedFrom);
            status = subscriptionTopic.status;
            experimental = subscriptionTopic.experimental;
            date = subscriptionTopic.date;
            publisher = subscriptionTopic.publisher;
            contact.addAll(subscriptionTopic.contact);
            description = subscriptionTopic.description;
            useContext.addAll(subscriptionTopic.useContext);
            jurisdiction.addAll(subscriptionTopic.jurisdiction);
            purpose = subscriptionTopic.purpose;
            copyright = subscriptionTopic.copyright;
            approvalDate = subscriptionTopic.approvalDate;
            lastReviewDate = subscriptionTopic.lastReviewDate;
            effectivePeriod = subscriptionTopic.effectivePeriod;
            resourceTrigger.addAll(subscriptionTopic.resourceTrigger);
            eventTrigger.addAll(subscriptionTopic.eventTrigger);
            canFilterBy.addAll(subscriptionTopic.canFilterBy);
            notificationShape.addAll(subscriptionTopic.notificationShape);
            return this;
        }
    }

    /**
     * A definition of a resource-based event that triggers a notification based on the SubscriptionTopic. The criteria may 
     * be just a human readable description and/or a full FHIR search string or FHIRPath expression. Multiple triggers are 
     * considered OR joined (e.g., a resource update matching ANY of the definitions will trigger a notification).
     */
    public static class ResourceTrigger extends BackboneElement {
        @Summary
        private final Markdown description;
        @Summary
        @Binding(
            bindingName = "FHIRDefinedTypeExt",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Either a resource or a data type, including logical model types.",
            valueSet = "http://hl7.org/fhir/ValueSet/defined-types"
        )
        @Required
        private final Uri resource;
        @Summary
        @Binding(
            bindingName = "MethodCode",
            strength = BindingStrength.Value.REQUIRED,
            description = "FHIR RESTful interaction used to filter a resource-based SubscriptionTopic trigger.",
            valueSet = "http://hl7.org/fhir/ValueSet/interaction-trigger|4.0.1"
        )
        private final List<MethodCode> supportedInteraction;
        @Summary
        private final QueryCriteria queryCriteria;
        @Summary
        private final String fhirPathCriteria;

        private ResourceTrigger(Builder builder) {
            super(builder);
            description = builder.description;
            resource = builder.resource;
            supportedInteraction = Collections.unmodifiableList(builder.supportedInteraction);
            queryCriteria = builder.queryCriteria;
            fhirPathCriteria = builder.fhirPathCriteria;
        }

        /**
         * The human readable description of this resource trigger for the SubscriptionTopic - for example, "An Encounter enters 
         * the 'in-progress' state".
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getDescription() {
            return description;
        }

        /**
         * URL of the Resource that is the type used in this resource trigger. Relative URLs are relative to the 
         * StructureDefinition root of the implemented FHIR version (e.g., http://hl7.org/fhir/StructureDefinition). For example, 
         * "Patient" maps to http://hl7.org/fhir/StructureDefinition/Patient. For more information, see &lt;a href="
         * elementdefinition-definitions.html#ElementDefinition.type.code"&gt;ElementDefinition.type.code&lt;/a&gt;.
         * 
         * @return
         *     An immutable object of type {@link Uri} that is non-null.
         */
        public Uri getResource() {
            return resource;
        }

        /**
         * The FHIR RESTful interaction which can be used to trigger a notification for the SubscriptionTopic. Multiple values 
         * are considered OR joined (e.g., CREATE or UPDATE).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link MethodCode} that may be empty.
         */
        public List<MethodCode> getSupportedInteraction() {
            return supportedInteraction;
        }

        /**
         * The FHIR query based rules that the server should use to determine when to trigger a notification for this 
         * subscription topic.
         * 
         * @return
         *     An immutable object of type {@link QueryCriteria} that may be null.
         */
        public QueryCriteria getQueryCriteria() {
            return queryCriteria;
        }

        /**
         * The FHIRPath based rules that the server should use to determine when to trigger a notification for this topic.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getFhirPathCriteria() {
            return fhirPathCriteria;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (resource != null) || 
                !supportedInteraction.isEmpty() || 
                (queryCriteria != null) || 
                (fhirPathCriteria != null);
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
                    accept(description, "description", visitor);
                    accept(resource, "resource", visitor);
                    accept(supportedInteraction, "supportedInteraction", visitor, MethodCode.class);
                    accept(queryCriteria, "queryCriteria", visitor);
                    accept(fhirPathCriteria, "fhirPathCriteria", visitor);
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
            ResourceTrigger other = (ResourceTrigger) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(resource, other.resource) && 
                Objects.equals(supportedInteraction, other.supportedInteraction) && 
                Objects.equals(queryCriteria, other.queryCriteria) && 
                Objects.equals(fhirPathCriteria, other.fhirPathCriteria);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    resource, 
                    supportedInteraction, 
                    queryCriteria, 
                    fhirPathCriteria);
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
            private Markdown description;
            private Uri resource;
            private List<MethodCode> supportedInteraction = new ArrayList<>();
            private QueryCriteria queryCriteria;
            private String fhirPathCriteria;

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
             * The human readable description of this resource trigger for the SubscriptionTopic - for example, "An Encounter enters 
             * the 'in-progress' state".
             * 
             * @param description
             *     Text representation of the resource trigger
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(Markdown description) {
                this.description = description;
                return this;
            }

            /**
             * URL of the Resource that is the type used in this resource trigger. Relative URLs are relative to the 
             * StructureDefinition root of the implemented FHIR version (e.g., http://hl7.org/fhir/StructureDefinition). For example, 
             * "Patient" maps to http://hl7.org/fhir/StructureDefinition/Patient. For more information, see &lt;a href="
             * elementdefinition-definitions.html#ElementDefinition.type.code"&gt;ElementDefinition.type.code&lt;/a&gt;.
             * 
             * <p>This element is required.
             * 
             * @param resource
             *     Data Type or Resource (reference to definition) for this trigger definition
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder resource(Uri resource) {
                this.resource = resource;
                return this;
            }

            /**
             * The FHIR RESTful interaction which can be used to trigger a notification for the SubscriptionTopic. Multiple values 
             * are considered OR joined (e.g., CREATE or UPDATE).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param supportedInteraction
             *     create | update | delete
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder supportedInteraction(MethodCode... supportedInteraction) {
                for (MethodCode value : supportedInteraction) {
                    this.supportedInteraction.add(value);
                }
                return this;
            }

            /**
             * The FHIR RESTful interaction which can be used to trigger a notification for the SubscriptionTopic. Multiple values 
             * are considered OR joined (e.g., CREATE or UPDATE).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param supportedInteraction
             *     create | update | delete
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder supportedInteraction(Collection<MethodCode> supportedInteraction) {
                this.supportedInteraction = new ArrayList<>(supportedInteraction);
                return this;
            }

            /**
             * The FHIR query based rules that the server should use to determine when to trigger a notification for this 
             * subscription topic.
             * 
             * @param queryCriteria
             *     Query based trigger rule
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder queryCriteria(QueryCriteria queryCriteria) {
                this.queryCriteria = queryCriteria;
                return this;
            }

            /**
             * Convenience method for setting {@code fhirPathCriteria}.
             * 
             * @param fhirPathCriteria
             *     FHIRPath based trigger rule
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #fhirPathCriteria(com.ibm.fhir.model.type.String)
             */
            public Builder fhirPathCriteria(java.lang.String fhirPathCriteria) {
                this.fhirPathCriteria = (fhirPathCriteria == null) ? null : String.of(fhirPathCriteria);
                return this;
            }

            /**
             * The FHIRPath based rules that the server should use to determine when to trigger a notification for this topic.
             * 
             * @param fhirPathCriteria
             *     FHIRPath based trigger rule
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder fhirPathCriteria(String fhirPathCriteria) {
                this.fhirPathCriteria = fhirPathCriteria;
                return this;
            }

            /**
             * Build the {@link ResourceTrigger}
             * 
             * <p>Required elements:
             * <ul>
             * <li>resource</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link ResourceTrigger}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid ResourceTrigger per the base specification
             */
            @Override
            public ResourceTrigger build() {
                ResourceTrigger resourceTrigger = new ResourceTrigger(this);
                if (validating) {
                    validate(resourceTrigger);
                }
                return resourceTrigger;
            }

            protected void validate(ResourceTrigger resourceTrigger) {
                super.validate(resourceTrigger);
                ValidationSupport.requireNonNull(resourceTrigger.resource, "resource");
                ValidationSupport.checkList(resourceTrigger.supportedInteraction, "supportedInteraction", MethodCode.class);
                ValidationSupport.requireValueOrChildren(resourceTrigger);
            }

            protected Builder from(ResourceTrigger resourceTrigger) {
                super.from(resourceTrigger);
                description = resourceTrigger.description;
                resource = resourceTrigger.resource;
                supportedInteraction.addAll(resourceTrigger.supportedInteraction);
                queryCriteria = resourceTrigger.queryCriteria;
                fhirPathCriteria = resourceTrigger.fhirPathCriteria;
                return this;
            }
        }

        /**
         * The FHIR query based rules that the server should use to determine when to trigger a notification for this 
         * subscription topic.
         */
        public static class QueryCriteria extends BackboneElement {
            @Summary
            private final String previous;
            @Summary
            @Binding(
                bindingName = "CriteriaNotExistsBehavior",
                strength = BindingStrength.Value.REQUIRED,
                description = "Behavior a server can exhibit when a criteria state does not exist (e.g., state prior to a create or after a delete).",
                valueSet = "http://hl7.org/fhir/ValueSet/subscriptiontopic-cr-behavior|4.0.1"
            )
            private final CriteriaNotExistsBehavior resultForCreate;
            @Summary
            private final String current;
            @Summary
            @Binding(
                bindingName = "CriteriaNotExistsBehavior",
                strength = BindingStrength.Value.REQUIRED,
                description = "Behavior a server can exhibit when a criteria state does not exist (e.g., state prior to a create or after a delete).",
                valueSet = "http://hl7.org/fhir/ValueSet/subscriptiontopic-cr-behavior|4.0.1"
            )
            private final CriteriaNotExistsBehavior resultForDelete;
            @Summary
            private final Boolean requireBoth;

            private QueryCriteria(Builder builder) {
                super(builder);
                previous = builder.previous;
                resultForCreate = builder.resultForCreate;
                current = builder.current;
                resultForDelete = builder.resultForDelete;
                requireBoth = builder.requireBoth;
            }

            /**
             * The FHIR query based rules are applied to the previous resource state (e.g., state before an update).
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getPrevious() {
                return previous;
            }

            /**
             * What behavior a server will exhibit if the previous state of a resource does NOT exist (e.g., prior to a create).
             * 
             * @return
             *     An immutable object of type {@link CriteriaNotExistsBehavior} that may be null.
             */
            public CriteriaNotExistsBehavior getResultForCreate() {
                return resultForCreate;
            }

            /**
             * The FHIR query based rules are applied to the current resource state (e.g., state after an update).
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getCurrent() {
                return current;
            }

            /**
             * What behavior a server will exhibit if the current state of a resource does NOT exist (e.g., after a DELETE).
             * 
             * @return
             *     An immutable object of type {@link CriteriaNotExistsBehavior} that may be null.
             */
            public CriteriaNotExistsBehavior getResultForDelete() {
                return resultForDelete;
            }

            /**
             * If set to true, both current and previous criteria must evaluate true to trigger a notification for this topic. 
             * Otherwise a notification for this topic will be triggered if either one evaluates to true.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getRequireBoth() {
                return requireBoth;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (previous != null) || 
                    (resultForCreate != null) || 
                    (current != null) || 
                    (resultForDelete != null) || 
                    (requireBoth != null);
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
                        accept(previous, "previous", visitor);
                        accept(resultForCreate, "resultForCreate", visitor);
                        accept(current, "current", visitor);
                        accept(resultForDelete, "resultForDelete", visitor);
                        accept(requireBoth, "requireBoth", visitor);
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
                QueryCriteria other = (QueryCriteria) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(previous, other.previous) && 
                    Objects.equals(resultForCreate, other.resultForCreate) && 
                    Objects.equals(current, other.current) && 
                    Objects.equals(resultForDelete, other.resultForDelete) && 
                    Objects.equals(requireBoth, other.requireBoth);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        previous, 
                        resultForCreate, 
                        current, 
                        resultForDelete, 
                        requireBoth);
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
                private String previous;
                private CriteriaNotExistsBehavior resultForCreate;
                private String current;
                private CriteriaNotExistsBehavior resultForDelete;
                private Boolean requireBoth;

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
                 * Convenience method for setting {@code previous}.
                 * 
                 * @param previous
                 *     Rule applied to previous resource state
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #previous(com.ibm.fhir.model.type.String)
                 */
                public Builder previous(java.lang.String previous) {
                    this.previous = (previous == null) ? null : String.of(previous);
                    return this;
                }

                /**
                 * The FHIR query based rules are applied to the previous resource state (e.g., state before an update).
                 * 
                 * @param previous
                 *     Rule applied to previous resource state
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder previous(String previous) {
                    this.previous = previous;
                    return this;
                }

                /**
                 * What behavior a server will exhibit if the previous state of a resource does NOT exist (e.g., prior to a create).
                 * 
                 * @param resultForCreate
                 *     test-passes | test-fails
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder resultForCreate(CriteriaNotExistsBehavior resultForCreate) {
                    this.resultForCreate = resultForCreate;
                    return this;
                }

                /**
                 * Convenience method for setting {@code current}.
                 * 
                 * @param current
                 *     Rule applied to current resource state
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #current(com.ibm.fhir.model.type.String)
                 */
                public Builder current(java.lang.String current) {
                    this.current = (current == null) ? null : String.of(current);
                    return this;
                }

                /**
                 * The FHIR query based rules are applied to the current resource state (e.g., state after an update).
                 * 
                 * @param current
                 *     Rule applied to current resource state
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder current(String current) {
                    this.current = current;
                    return this;
                }

                /**
                 * What behavior a server will exhibit if the current state of a resource does NOT exist (e.g., after a DELETE).
                 * 
                 * @param resultForDelete
                 *     test-passes | test-fails
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder resultForDelete(CriteriaNotExistsBehavior resultForDelete) {
                    this.resultForDelete = resultForDelete;
                    return this;
                }

                /**
                 * Convenience method for setting {@code requireBoth}.
                 * 
                 * @param requireBoth
                 *     Both must be true flag
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #requireBoth(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder requireBoth(java.lang.Boolean requireBoth) {
                    this.requireBoth = (requireBoth == null) ? null : Boolean.of(requireBoth);
                    return this;
                }

                /**
                 * If set to true, both current and previous criteria must evaluate true to trigger a notification for this topic. 
                 * Otherwise a notification for this topic will be triggered if either one evaluates to true.
                 * 
                 * @param requireBoth
                 *     Both must be true flag
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder requireBoth(Boolean requireBoth) {
                    this.requireBoth = requireBoth;
                    return this;
                }

                /**
                 * Build the {@link QueryCriteria}
                 * 
                 * @return
                 *     An immutable object of type {@link QueryCriteria}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid QueryCriteria per the base specification
                 */
                @Override
                public QueryCriteria build() {
                    QueryCriteria queryCriteria = new QueryCriteria(this);
                    if (validating) {
                        validate(queryCriteria);
                    }
                    return queryCriteria;
                }

                protected void validate(QueryCriteria queryCriteria) {
                    super.validate(queryCriteria);
                    ValidationSupport.requireValueOrChildren(queryCriteria);
                }

                protected Builder from(QueryCriteria queryCriteria) {
                    super.from(queryCriteria);
                    previous = queryCriteria.previous;
                    resultForCreate = queryCriteria.resultForCreate;
                    current = queryCriteria.current;
                    resultForDelete = queryCriteria.resultForDelete;
                    requireBoth = queryCriteria.requireBoth;
                    return this;
                }
            }
        }
    }

    /**
     * Event definition which can be used to trigger the SubscriptionTopic.
     */
    public static class EventTrigger extends BackboneElement {
        @Summary
        private final Markdown description;
        @Summary
        @Binding(
            bindingName = "SubscriptionTopicEventTrigger",
            strength = BindingStrength.Value.EXAMPLE,
            description = "FHIR Value set/code system definition for HL7 v2 table 0003 (EVENT TYPE CODE).",
            valueSet = "http://terminology.hl7.org/ValueSet/v2-0003"
        )
        @Required
        private final CodeableConcept event;
        @Summary
        @Binding(
            bindingName = "FHIRDefinedTypeExt",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Either a resource or a data type, including logical model types.",
            valueSet = "http://hl7.org/fhir/ValueSet/defined-types"
        )
        @Required
        private final Uri resource;

        private EventTrigger(Builder builder) {
            super(builder);
            description = builder.description;
            event = builder.event;
            resource = builder.resource;
        }

        /**
         * The human readable description of an event to trigger a notification for the SubscriptionTopic - for example, "Patient 
         * Admission, as defined in HL7v2 via message ADT^A01". Multiple values are considered OR joined (e.g., matching any 
         * single event listed).
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getDescription() {
            return description;
        }

        /**
         * A well-defined event which can be used to trigger notifications from the SubscriptionTopic.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getEvent() {
            return event;
        }

        /**
         * URL of the Resource that is the focus type used in this event trigger. Relative URLs are relative to the 
         * StructureDefinition root of the implemented FHIR version (e.g., http://hl7.org/fhir/StructureDefinition). For example, 
         * "Patient" maps to http://hl7.org/fhir/StructureDefinition/Patient. For more information, see &lt;a href="
         * elementdefinition-definitions.html#ElementDefinition.type.code"&gt;ElementDefinition.type.code&lt;/a&gt;.
         * 
         * @return
         *     An immutable object of type {@link Uri} that is non-null.
         */
        public Uri getResource() {
            return resource;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (event != null) || 
                (resource != null);
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
                    accept(description, "description", visitor);
                    accept(event, "event", visitor);
                    accept(resource, "resource", visitor);
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
            EventTrigger other = (EventTrigger) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(event, other.event) && 
                Objects.equals(resource, other.resource);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    event, 
                    resource);
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
            private Markdown description;
            private CodeableConcept event;
            private Uri resource;

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
             * The human readable description of an event to trigger a notification for the SubscriptionTopic - for example, "Patient 
             * Admission, as defined in HL7v2 via message ADT^A01". Multiple values are considered OR joined (e.g., matching any 
             * single event listed).
             * 
             * @param description
             *     Text representation of the event trigger
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(Markdown description) {
                this.description = description;
                return this;
            }

            /**
             * A well-defined event which can be used to trigger notifications from the SubscriptionTopic.
             * 
             * <p>This element is required.
             * 
             * @param event
             *     Event which can trigger a notification from the SubscriptionTopic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder event(CodeableConcept event) {
                this.event = event;
                return this;
            }

            /**
             * URL of the Resource that is the focus type used in this event trigger. Relative URLs are relative to the 
             * StructureDefinition root of the implemented FHIR version (e.g., http://hl7.org/fhir/StructureDefinition). For example, 
             * "Patient" maps to http://hl7.org/fhir/StructureDefinition/Patient. For more information, see &lt;a href="
             * elementdefinition-definitions.html#ElementDefinition.type.code"&gt;ElementDefinition.type.code&lt;/a&gt;.
             * 
             * <p>This element is required.
             * 
             * @param resource
             *     Data Type or Resource (reference to definition) for this trigger definition
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder resource(Uri resource) {
                this.resource = resource;
                return this;
            }

            /**
             * Build the {@link EventTrigger}
             * 
             * <p>Required elements:
             * <ul>
             * <li>event</li>
             * <li>resource</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link EventTrigger}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid EventTrigger per the base specification
             */
            @Override
            public EventTrigger build() {
                EventTrigger eventTrigger = new EventTrigger(this);
                if (validating) {
                    validate(eventTrigger);
                }
                return eventTrigger;
            }

            protected void validate(EventTrigger eventTrigger) {
                super.validate(eventTrigger);
                ValidationSupport.requireNonNull(eventTrigger.event, "event");
                ValidationSupport.requireNonNull(eventTrigger.resource, "resource");
                ValidationSupport.requireValueOrChildren(eventTrigger);
            }

            protected Builder from(EventTrigger eventTrigger) {
                super.from(eventTrigger);
                description = eventTrigger.description;
                event = eventTrigger.event;
                resource = eventTrigger.resource;
                return this;
            }
        }
    }

    /**
     * List of properties by which Subscriptions on the SubscriptionTopic can be filtered. May be defined Search Parameters 
     * (e.g., Encounter.patient) or parameters defined within this SubscriptionTopic context (e.g., hub.event).
     */
    public static class CanFilterBy extends BackboneElement {
        @Summary
        private final Markdown description;
        @Summary
        @Binding(
            bindingName = "FHIRDefinedTypeExt",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Either a resource or a data type, including logical model types.",
            valueSet = "http://hl7.org/fhir/ValueSet/defined-types"
        )
        private final Uri resource;
        @Summary
        private final String filterParameter;
        @Summary
        @Binding(
            bindingName = "SubscriptionTopicFilterBySearchModifier",
            strength = BindingStrength.Value.REQUIRED,
            description = "Operator to apply to filter label.",
            valueSet = "http://hl7.org/fhir/ValueSet/subscription-search-modifier|4.0.1"
        )
        private final List<SubscriptionTopicFilterBySearchModifier> modifier;

        private CanFilterBy(Builder builder) {
            super(builder);
            description = builder.description;
            resource = builder.resource;
            filterParameter = builder.filterParameter;
            modifier = Collections.unmodifiableList(builder.modifier);
        }

        /**
         * Description of how this filtering parameter is intended to be used.
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getDescription() {
            return description;
        }

        /**
         * URL of the Resource that is the type used in this filter. This is the "focus" of the topic (or one of them if there 
         * are more than one). It will be the same, a generality, or a specificity of SubscriptionTopic.resourceTrigger.resource 
         * or SubscriptionTopic.eventTrigger.resource when they are present.
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getResource() {
            return resource;
        }

        /**
         * Either the canonical URL to a search parameter (like "http://hl7.org/fhir/SearchParameter/encounter-patient") or topic-
         * defined parameter (like "hub.event") which is a label for the filter.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getFilterParameter() {
            return filterParameter;
        }

        /**
         * Allowable operators to apply when determining matches (Search Modifiers).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link SubscriptionTopicFilterBySearchModifier} that may be 
         *     empty.
         */
        public List<SubscriptionTopicFilterBySearchModifier> getModifier() {
            return modifier;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (resource != null) || 
                (filterParameter != null) || 
                !modifier.isEmpty();
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
                    accept(description, "description", visitor);
                    accept(resource, "resource", visitor);
                    accept(filterParameter, "filterParameter", visitor);
                    accept(modifier, "modifier", visitor, SubscriptionTopicFilterBySearchModifier.class);
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
            CanFilterBy other = (CanFilterBy) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(resource, other.resource) && 
                Objects.equals(filterParameter, other.filterParameter) && 
                Objects.equals(modifier, other.modifier);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    resource, 
                    filterParameter, 
                    modifier);
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
            private Markdown description;
            private Uri resource;
            private String filterParameter;
            private List<SubscriptionTopicFilterBySearchModifier> modifier = new ArrayList<>();

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
             * Description of how this filtering parameter is intended to be used.
             * 
             * @param description
             *     Description of this filter parameter
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(Markdown description) {
                this.description = description;
                return this;
            }

            /**
             * URL of the Resource that is the type used in this filter. This is the "focus" of the topic (or one of them if there 
             * are more than one). It will be the same, a generality, or a specificity of SubscriptionTopic.resourceTrigger.resource 
             * or SubscriptionTopic.eventTrigger.resource when they are present.
             * 
             * @param resource
             *     URL of the triggering Resource that this filter applies to
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder resource(Uri resource) {
                this.resource = resource;
                return this;
            }

            /**
             * Convenience method for setting {@code filterParameter}.
             * 
             * @param filterParameter
             *     Resource Search Parameter or filter parameter defined in this topic that serves as filter key
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #filterParameter(com.ibm.fhir.model.type.String)
             */
            public Builder filterParameter(java.lang.String filterParameter) {
                this.filterParameter = (filterParameter == null) ? null : String.of(filterParameter);
                return this;
            }

            /**
             * Either the canonical URL to a search parameter (like "http://hl7.org/fhir/SearchParameter/encounter-patient") or topic-
             * defined parameter (like "hub.event") which is a label for the filter.
             * 
             * @param filterParameter
             *     Resource Search Parameter or filter parameter defined in this topic that serves as filter key
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder filterParameter(String filterParameter) {
                this.filterParameter = filterParameter;
                return this;
            }

            /**
             * Allowable operators to apply when determining matches (Search Modifiers).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifier
             *     = | eq | ne | gt | lt | ge | le | sa | eb | ap | above | below | in | not-in | of-type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder modifier(SubscriptionTopicFilterBySearchModifier... modifier) {
                for (SubscriptionTopicFilterBySearchModifier value : modifier) {
                    this.modifier.add(value);
                }
                return this;
            }

            /**
             * Allowable operators to apply when determining matches (Search Modifiers).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifier
             *     = | eq | ne | gt | lt | ge | le | sa | eb | ap | above | below | in | not-in | of-type
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder modifier(Collection<SubscriptionTopicFilterBySearchModifier> modifier) {
                this.modifier = new ArrayList<>(modifier);
                return this;
            }

            /**
             * Build the {@link CanFilterBy}
             * 
             * @return
             *     An immutable object of type {@link CanFilterBy}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid CanFilterBy per the base specification
             */
            @Override
            public CanFilterBy build() {
                CanFilterBy canFilterBy = new CanFilterBy(this);
                if (validating) {
                    validate(canFilterBy);
                }
                return canFilterBy;
            }

            protected void validate(CanFilterBy canFilterBy) {
                super.validate(canFilterBy);
                ValidationSupport.checkList(canFilterBy.modifier, "modifier", SubscriptionTopicFilterBySearchModifier.class);
                ValidationSupport.requireValueOrChildren(canFilterBy);
            }

            protected Builder from(CanFilterBy canFilterBy) {
                super.from(canFilterBy);
                description = canFilterBy.description;
                resource = canFilterBy.resource;
                filterParameter = canFilterBy.filterParameter;
                modifier.addAll(canFilterBy.modifier);
                return this;
            }
        }
    }

    /**
     * List of properties to describe the shape (e.g., resources) included in notifications from this Subscription Topic.
     */
    public static class NotificationShape extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "FHIRDefinedTypeExt",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Either a resource or a data type, including logical model types.",
            valueSet = "http://hl7.org/fhir/ValueSet/defined-types"
        )
        @Required
        private final Uri resource;
        @Summary
        private final List<String> include;
        @Summary
        private final List<String> revInclude;

        private NotificationShape(Builder builder) {
            super(builder);
            resource = builder.resource;
            include = Collections.unmodifiableList(builder.include);
            revInclude = Collections.unmodifiableList(builder.revInclude);
        }

        /**
         * URL of the Resource that is the type used in this shape. This is the "focus" of the topic (or one of them if there are 
         * more than one) and the root resource for this shape definition. It will be the same, a generality, or a specificity of 
         * SubscriptionTopic.resourceTrigger.resource or SubscriptionTopic.eventTrigger.resource when they are present.
         * 
         * @return
         *     An immutable object of type {@link Uri} that is non-null.
         */
        public Uri getResource() {
            return resource;
        }

        /**
         * Search-style _include directives, rooted in the resource for this shape. Servers SHOULD include resources listed here, 
         * if they exist and the user is authorized to receive them. Clients SHOULD be prepared to receive these additional 
         * resources, but SHALL function properly without them.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
         */
        public List<String> getInclude() {
            return include;
        }

        /**
         * Search-style _revinclude directives, rooted in the resource for this shape. Servers SHOULD include resources listed 
         * here, if they exist and the user is authorized to receive them. Clients SHOULD be prepared to receive these additional 
         * resources, but SHALL function properly without them.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
         */
        public List<String> getRevInclude() {
            return revInclude;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (resource != null) || 
                !include.isEmpty() || 
                !revInclude.isEmpty();
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
                    accept(resource, "resource", visitor);
                    accept(include, "include", visitor, String.class);
                    accept(revInclude, "revInclude", visitor, String.class);
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
            NotificationShape other = (NotificationShape) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(resource, other.resource) && 
                Objects.equals(include, other.include) && 
                Objects.equals(revInclude, other.revInclude);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    resource, 
                    include, 
                    revInclude);
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
            private Uri resource;
            private List<String> include = new ArrayList<>();
            private List<String> revInclude = new ArrayList<>();

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
             * URL of the Resource that is the type used in this shape. This is the "focus" of the topic (or one of them if there are 
             * more than one) and the root resource for this shape definition. It will be the same, a generality, or a specificity of 
             * SubscriptionTopic.resourceTrigger.resource or SubscriptionTopic.eventTrigger.resource when they are present.
             * 
             * <p>This element is required.
             * 
             * @param resource
             *     URL of the Resource that is the focus (main) resource in a notification shape
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder resource(Uri resource) {
                this.resource = resource;
                return this;
            }

            /**
             * Convenience method for setting {@code include}.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param include
             *     Include directives, rooted in the resource for this shape
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #include(com.ibm.fhir.model.type.String)
             */
            public Builder include(java.lang.String... include) {
                for (java.lang.String value : include) {
                    this.include.add((value == null) ? null : String.of(value));
                }
                return this;
            }

            /**
             * Search-style _include directives, rooted in the resource for this shape. Servers SHOULD include resources listed here, 
             * if they exist and the user is authorized to receive them. Clients SHOULD be prepared to receive these additional 
             * resources, but SHALL function properly without them.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param include
             *     Include directives, rooted in the resource for this shape
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder include(String... include) {
                for (String value : include) {
                    this.include.add(value);
                }
                return this;
            }

            /**
             * Search-style _include directives, rooted in the resource for this shape. Servers SHOULD include resources listed here, 
             * if they exist and the user is authorized to receive them. Clients SHOULD be prepared to receive these additional 
             * resources, but SHALL function properly without them.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param include
             *     Include directives, rooted in the resource for this shape
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder include(Collection<String> include) {
                this.include = new ArrayList<>(include);
                return this;
            }

            /**
             * Convenience method for setting {@code revInclude}.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param revInclude
             *     Reverse include directives, rooted in the resource for this shape
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #revInclude(com.ibm.fhir.model.type.String)
             */
            public Builder revInclude(java.lang.String... revInclude) {
                for (java.lang.String value : revInclude) {
                    this.revInclude.add((value == null) ? null : String.of(value));
                }
                return this;
            }

            /**
             * Search-style _revinclude directives, rooted in the resource for this shape. Servers SHOULD include resources listed 
             * here, if they exist and the user is authorized to receive them. Clients SHOULD be prepared to receive these additional 
             * resources, but SHALL function properly without them.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param revInclude
             *     Reverse include directives, rooted in the resource for this shape
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder revInclude(String... revInclude) {
                for (String value : revInclude) {
                    this.revInclude.add(value);
                }
                return this;
            }

            /**
             * Search-style _revinclude directives, rooted in the resource for this shape. Servers SHOULD include resources listed 
             * here, if they exist and the user is authorized to receive them. Clients SHOULD be prepared to receive these additional 
             * resources, but SHALL function properly without them.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param revInclude
             *     Reverse include directives, rooted in the resource for this shape
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder revInclude(Collection<String> revInclude) {
                this.revInclude = new ArrayList<>(revInclude);
                return this;
            }

            /**
             * Build the {@link NotificationShape}
             * 
             * <p>Required elements:
             * <ul>
             * <li>resource</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link NotificationShape}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid NotificationShape per the base specification
             */
            @Override
            public NotificationShape build() {
                NotificationShape notificationShape = new NotificationShape(this);
                if (validating) {
                    validate(notificationShape);
                }
                return notificationShape;
            }

            protected void validate(NotificationShape notificationShape) {
                super.validate(notificationShape);
                ValidationSupport.requireNonNull(notificationShape.resource, "resource");
                ValidationSupport.checkList(notificationShape.include, "include", String.class);
                ValidationSupport.checkList(notificationShape.revInclude, "revInclude", String.class);
                ValidationSupport.requireValueOrChildren(notificationShape);
            }

            protected Builder from(NotificationShape notificationShape) {
                super.from(notificationShape);
                resource = notificationShape.resource;
                include.addAll(notificationShape.include);
                revInclude.addAll(notificationShape.revInclude);
                return this;
            }
        }
    }
}

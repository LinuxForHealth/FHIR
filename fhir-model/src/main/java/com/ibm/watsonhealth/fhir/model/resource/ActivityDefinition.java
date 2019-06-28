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

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.ActivityDefinitionKind;
import com.ibm.watsonhealth.fhir.model.type.ActivityParticipantType;
import com.ibm.watsonhealth.fhir.model.type.Age;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Dosage;
import com.ibm.watsonhealth.fhir.model.type.Duration;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Expression;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.RelatedArtifact;
import com.ibm.watsonhealth.fhir.model.type.RequestIntent;
import com.ibm.watsonhealth.fhir.model.type.RequestPriority;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * This resource allows for the definition of some activity to be performed, independent of a particular patient, 
 * practitioner, or other performance context.
 * </p>
 */
@Constraint(
    id = "adf-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ActivityDefinition extends DomainResource {
    private final Uri url;
    private final List<Identifier> identifier;
    private final String version;
    private final String name;
    private final String title;
    private final String subtitle;
    private final PublicationStatus status;
    private final Boolean experimental;
    private final Element subject;
    private final DateTime date;
    private final String publisher;
    private final List<ContactDetail> contact;
    private final Markdown description;
    private final List<UsageContext> useContext;
    private final List<CodeableConcept> jurisdiction;
    private final Markdown purpose;
    private final String usage;
    private final Markdown copyright;
    private final Date approvalDate;
    private final Date lastReviewDate;
    private final Period effectivePeriod;
    private final List<CodeableConcept> topic;
    private final List<ContactDetail> author;
    private final List<ContactDetail> editor;
    private final List<ContactDetail> reviewer;
    private final List<ContactDetail> endorser;
    private final List<RelatedArtifact> relatedArtifact;
    private final List<Canonical> library;
    private final ActivityDefinitionKind kind;
    private final Canonical profile;
    private final CodeableConcept code;
    private final RequestIntent intent;
    private final RequestPriority priority;
    private final Boolean doNotPerform;
    private final Element timing;
    private final Reference location;
    private final List<Participant> participant;
    private final Element product;
    private final Quantity quantity;
    private final List<Dosage> dosage;
    private final List<CodeableConcept> bodySite;
    private final List<Reference> specimenRequirement;
    private final List<Reference> observationRequirement;
    private final List<Reference> observationResultRequirement;
    private final Canonical transform;
    private final List<DynamicValue> dynamicValue;

    private ActivityDefinition(Builder builder) {
        super(builder);
        this.url = builder.url;
        this.identifier = builder.identifier;
        this.version = builder.version;
        this.name = builder.name;
        this.title = builder.title;
        this.subtitle = builder.subtitle;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.experimental = builder.experimental;
        this.subject = ValidationSupport.choiceElement(builder.subject, "subject", CodeableConcept.class, Reference.class);
        this.date = builder.date;
        this.publisher = builder.publisher;
        this.contact = builder.contact;
        this.description = builder.description;
        this.useContext = builder.useContext;
        this.jurisdiction = builder.jurisdiction;
        this.purpose = builder.purpose;
        this.usage = builder.usage;
        this.copyright = builder.copyright;
        this.approvalDate = builder.approvalDate;
        this.lastReviewDate = builder.lastReviewDate;
        this.effectivePeriod = builder.effectivePeriod;
        this.topic = builder.topic;
        this.author = builder.author;
        this.editor = builder.editor;
        this.reviewer = builder.reviewer;
        this.endorser = builder.endorser;
        this.relatedArtifact = builder.relatedArtifact;
        this.library = builder.library;
        this.kind = builder.kind;
        this.profile = builder.profile;
        this.code = builder.code;
        this.intent = builder.intent;
        this.priority = builder.priority;
        this.doNotPerform = builder.doNotPerform;
        this.timing = ValidationSupport.choiceElement(builder.timing, "timing", Timing.class, DateTime.class, Age.class, Period.class, Range.class, Duration.class);
        this.location = builder.location;
        this.participant = builder.participant;
        this.product = ValidationSupport.choiceElement(builder.product, "product", Reference.class, CodeableConcept.class);
        this.quantity = builder.quantity;
        this.dosage = builder.dosage;
        this.bodySite = builder.bodySite;
        this.specimenRequirement = builder.specimenRequirement;
        this.observationRequirement = builder.observationRequirement;
        this.observationResultRequirement = builder.observationResultRequirement;
        this.transform = builder.transform;
        this.dynamicValue = builder.dynamicValue;
    }

    /**
     * <p>
     * An absolute URI that is used to identify this activity definition when it is referenced in a specification, model, 
     * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
     * address at which at which an authoritative instance of this activity definition is (or will be) published. This URL 
     * can be the target of a canonical reference. It SHALL remain the same when the activity definition is stored on 
     * different servers.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * <p>
     * A formal identifier that is used to identify this activity definition when it is represented in other formats, or 
     * referenced in a specification, model, design or an instance.
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
     * The identifier that is used to identify this version of the activity definition when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the activity definition author and is 
     * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
     * available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a 
     * version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). 
     * For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a 
     * version is required for non-experimental active assets.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getVersion() {
        return version;
    }

    /**
     * <p>
     * A natural language name identifying the activity definition. This name should be usable as an identifier for the 
     * module by machine processing applications such as code generation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * A short, descriptive, user-friendly title for the activity definition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>
     * An explanatory or alternate title for the activity definition giving additional information about its content.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * <p>
     * The status of this activity definition. Enables tracking the life-cycle of the content.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus}.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * A Boolean value to indicate that this activity definition is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * <p>
     * A code or group definition that describes the intended subject of the activity being defined.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getSubject() {
        return subject;
    }

    /**
     * <p>
     * The date (and optionally time) when the activity definition was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the activity definition changes.
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
     * The name of the organization or individual that published the activity definition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * <p>
     * Contact details to assist a user in finding and communicating with the publisher.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getContact() {
        return contact;
    }

    /**
     * <p>
     * A free text natural language description of the activity definition from a consumer's perspective.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * <p>
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate activity definition instances.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link UsageContext}.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * <p>
     * A legal or geographic region in which the activity definition is intended to be used.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * <p>
     * Explanation of why this activity definition is needed and why it has been designed as it has.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * <p>
     * A detailed description of how the activity definition is used from a clinical perspective.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * <p>
     * A copyright statement relating to the activity definition and/or its contents. Copyright statements are generally 
     * legal restrictions on the use and publishing of the activity definition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * <p>
     * The date on which the resource content was approved by the publisher. Approval happens once when the content is 
     * officially approved for usage.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Date}.
     */
    public Date getApprovalDate() {
        return approvalDate;
    }

    /**
     * <p>
     * The date on which the resource content was last reviewed. Review happens periodically after approval but does not 
     * change the original approval date.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Date}.
     */
    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    /**
     * <p>
     * The period during which the activity definition content was or is planned to be in active use.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getEffectivePeriod() {
        return effectivePeriod;
    }

    /**
     * <p>
     * Descriptive topics related to the content of the activity. Topics provide a high-level categorization of the activity 
     * that can be useful for filtering and searching.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getTopic() {
        return topic;
    }

    /**
     * <p>
     * An individiual or organization primarily involved in the creation and maintenance of the content.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getAuthor() {
        return author;
    }

    /**
     * <p>
     * An individual or organization primarily responsible for internal coherence of the content.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getEditor() {
        return editor;
    }

    /**
     * <p>
     * An individual or organization primarily responsible for review of some aspect of the content.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getReviewer() {
        return reviewer;
    }

    /**
     * <p>
     * An individual or organization responsible for officially endorsing the content for use in some setting.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getEndorser() {
        return endorser;
    }

    /**
     * <p>
     * Related artifacts such as additional documentation, justification, or bibliographic references.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link RelatedArtifact}.
     */
    public List<RelatedArtifact> getRelatedArtifact() {
        return relatedArtifact;
    }

    /**
     * <p>
     * A reference to a Library resource containing any formal logic used by the activity definition.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getLibrary() {
        return library;
    }

    /**
     * <p>
     * A description of the kind of resource the activity definition is representing. For example, a MedicationRequest, a 
     * ServiceRequest, or a CommunicationRequest. Typically, but not always, this is a Request resource.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ActivityDefinitionKind}.
     */
    public ActivityDefinitionKind getKind() {
        return kind;
    }

    /**
     * <p>
     * A profile to which the target of the activity definition is expected to conform.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Canonical}.
     */
    public Canonical getProfile() {
        return profile;
    }

    /**
     * <p>
     * Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter.
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
     * Indicates the level of authority/intentionality associated with the activity and where the request should fit into the 
     * workflow chain.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link RequestIntent}.
     */
    public RequestIntent getIntent() {
        return intent;
    }

    /**
     * <p>
     * Indicates how quickly the activity should be addressed with respect to other requests.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link RequestPriority}.
     */
    public RequestPriority getPriority() {
        return priority;
    }

    /**
     * <p>
     * Set this to true if the definition is to indicate that a particular activity should NOT be performed. If true, this 
     * element should be interpreted to reinforce a negative coding. For example NPO as a code with a doNotPerform of true 
     * would still indicate to NOT perform the action.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getDoNotPerform() {
        return doNotPerform;
    }

    /**
     * <p>
     * The period, timing or frequency upon which the described activity is to occur.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getTiming() {
        return timing;
    }

    /**
     * <p>
     * Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getLocation() {
        return location;
    }

    /**
     * <p>
     * Indicates who should participate in performing the action described.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Participant}.
     */
    public List<Participant> getParticipant() {
        return participant;
    }

    /**
     * <p>
     * Identifies the food, drug or other product being consumed or supplied in the activity.
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
     * Identifies the quantity expected to be consumed at once (per dose, per meal, etc.).
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
     * Provides detailed dosage instructions in the same way that they are described for MedicationRequest resources.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Dosage}.
     */
    public List<Dosage> getDosage() {
        return dosage;
    }

    /**
     * <p>
     * Indicates the sites on the subject's body where the procedure should be performed (I.e. the target sites).
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getBodySite() {
        return bodySite;
    }

    /**
     * <p>
     * Defines specimen requirements for the action to be performed, such as required specimens for a lab test.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSpecimenRequirement() {
        return specimenRequirement;
    }

    /**
     * <p>
     * Defines observation requirements for the action to be performed, such as body weight or surface area.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getObservationRequirement() {
        return observationRequirement;
    }

    /**
     * <p>
     * Defines the observations that are expected to be produced by the action.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getObservationResultRequirement() {
        return observationResultRequirement;
    }

    /**
     * <p>
     * A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource 
     * using the ActivityDefinition instance as the input.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Canonical}.
     */
    public Canonical getTransform() {
        return transform;
    }

    /**
     * <p>
     * Dynamic values that will be evaluated to produce values for elements of the resulting resource. For example, if the 
     * dosage of a medication must be computed based on the patient's weight, a dynamic value would be used to specify an 
     * expression that calculated the weight, and the path on the request resource that would contain the result.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link DynamicValue}.
     */
    public List<DynamicValue> getDynamicValue() {
        return dynamicValue;
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
                accept(url, "url", visitor);
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(version, "version", visitor);
                accept(name, "name", visitor);
                accept(title, "title", visitor);
                accept(subtitle, "subtitle", visitor);
                accept(status, "status", visitor);
                accept(experimental, "experimental", visitor);
                accept(subject, "subject", visitor, true);
                accept(date, "date", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(description, "description", visitor);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                accept(purpose, "purpose", visitor);
                accept(usage, "usage", visitor);
                accept(copyright, "copyright", visitor);
                accept(approvalDate, "approvalDate", visitor);
                accept(lastReviewDate, "lastReviewDate", visitor);
                accept(effectivePeriod, "effectivePeriod", visitor);
                accept(topic, "topic", visitor, CodeableConcept.class);
                accept(author, "author", visitor, ContactDetail.class);
                accept(editor, "editor", visitor, ContactDetail.class);
                accept(reviewer, "reviewer", visitor, ContactDetail.class);
                accept(endorser, "endorser", visitor, ContactDetail.class);
                accept(relatedArtifact, "relatedArtifact", visitor, RelatedArtifact.class);
                accept(library, "library", visitor, Canonical.class);
                accept(kind, "kind", visitor);
                accept(profile, "profile", visitor);
                accept(code, "code", visitor);
                accept(intent, "intent", visitor);
                accept(priority, "priority", visitor);
                accept(doNotPerform, "doNotPerform", visitor);
                accept(timing, "timing", visitor, true);
                accept(location, "location", visitor);
                accept(participant, "participant", visitor, Participant.class);
                accept(product, "product", visitor, true);
                accept(quantity, "quantity", visitor);
                accept(dosage, "dosage", visitor, Dosage.class);
                accept(bodySite, "bodySite", visitor, CodeableConcept.class);
                accept(specimenRequirement, "specimenRequirement", visitor, Reference.class);
                accept(observationRequirement, "observationRequirement", visitor, Reference.class);
                accept(observationResultRequirement, "observationResultRequirement", visitor, Reference.class);
                accept(transform, "transform", visitor);
                accept(dynamicValue, "dynamicValue", visitor, DynamicValue.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.url = url;
        builder.identifier.addAll(identifier);
        builder.version = version;
        builder.name = name;
        builder.title = title;
        builder.subtitle = subtitle;
        builder.experimental = experimental;
        builder.subject = subject;
        builder.date = date;
        builder.publisher = publisher;
        builder.contact.addAll(contact);
        builder.description = description;
        builder.useContext.addAll(useContext);
        builder.jurisdiction.addAll(jurisdiction);
        builder.purpose = purpose;
        builder.usage = usage;
        builder.copyright = copyright;
        builder.approvalDate = approvalDate;
        builder.lastReviewDate = lastReviewDate;
        builder.effectivePeriod = effectivePeriod;
        builder.topic.addAll(topic);
        builder.author.addAll(author);
        builder.editor.addAll(editor);
        builder.reviewer.addAll(reviewer);
        builder.endorser.addAll(endorser);
        builder.relatedArtifact.addAll(relatedArtifact);
        builder.library.addAll(library);
        builder.kind = kind;
        builder.profile = profile;
        builder.code = code;
        builder.intent = intent;
        builder.priority = priority;
        builder.doNotPerform = doNotPerform;
        builder.timing = timing;
        builder.location = location;
        builder.participant.addAll(participant);
        builder.product = product;
        builder.quantity = quantity;
        builder.dosage.addAll(dosage);
        builder.bodySite.addAll(bodySite);
        builder.specimenRequirement.addAll(specimenRequirement);
        builder.observationRequirement.addAll(observationRequirement);
        builder.observationResultRequirement.addAll(observationResultRequirement);
        builder.transform = transform;
        builder.dynamicValue.addAll(dynamicValue);
        return builder;
    }

    public static Builder builder(PublicationStatus status) {
        return new Builder(status);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final PublicationStatus status;

        // optional
        private Uri url;
        private List<Identifier> identifier = new ArrayList<>();
        private String version;
        private String name;
        private String title;
        private String subtitle;
        private Boolean experimental;
        private Element subject;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown purpose;
        private String usage;
        private Markdown copyright;
        private Date approvalDate;
        private Date lastReviewDate;
        private Period effectivePeriod;
        private List<CodeableConcept> topic = new ArrayList<>();
        private List<ContactDetail> author = new ArrayList<>();
        private List<ContactDetail> editor = new ArrayList<>();
        private List<ContactDetail> reviewer = new ArrayList<>();
        private List<ContactDetail> endorser = new ArrayList<>();
        private List<RelatedArtifact> relatedArtifact = new ArrayList<>();
        private List<Canonical> library = new ArrayList<>();
        private ActivityDefinitionKind kind;
        private Canonical profile;
        private CodeableConcept code;
        private RequestIntent intent;
        private RequestPriority priority;
        private Boolean doNotPerform;
        private Element timing;
        private Reference location;
        private List<Participant> participant = new ArrayList<>();
        private Element product;
        private Quantity quantity;
        private List<Dosage> dosage = new ArrayList<>();
        private List<CodeableConcept> bodySite = new ArrayList<>();
        private List<Reference> specimenRequirement = new ArrayList<>();
        private List<Reference> observationRequirement = new ArrayList<>();
        private List<Reference> observationResultRequirement = new ArrayList<>();
        private Canonical transform;
        private List<DynamicValue> dynamicValue = new ArrayList<>();

        private Builder(PublicationStatus status) {
            super();
            this.status = status;
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
         * An absolute URI that is used to identify this activity definition when it is referenced in a specification, model, 
         * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
         * address at which at which an authoritative instance of this activity definition is (or will be) published. This URL 
         * can be the target of a canonical reference. It SHALL remain the same when the activity definition is stored on 
         * different servers.
         * </p>
         * 
         * @param url
         *     Canonical identifier for this activity definition, represented as a URI (globally unique)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * <p>
         * A formal identifier that is used to identify this activity definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the activity definition
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
         * A formal identifier that is used to identify this activity definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the activity definition
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
         * The identifier that is used to identify this version of the activity definition when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the activity definition author and is 
         * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
         * available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a 
         * version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). 
         * For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a 
         * version is required for non-experimental active assets.
         * </p>
         * 
         * @param version
         *     Business version of the activity definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * <p>
         * A natural language name identifying the activity definition. This name should be usable as an identifier for the 
         * module by machine processing applications such as code generation.
         * </p>
         * 
         * @param name
         *     Name for this activity definition (computer friendly)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * <p>
         * A short, descriptive, user-friendly title for the activity definition.
         * </p>
         * 
         * @param title
         *     Name for this activity definition (human friendly)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * <p>
         * An explanatory or alternate title for the activity definition giving additional information about its content.
         * </p>
         * 
         * @param subtitle
         *     Subordinate title of the activity definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        /**
         * <p>
         * A Boolean value to indicate that this activity definition is authored for testing purposes (or 
         * education/evaluation/marketing) and is not intended to be used for genuine usage.
         * </p>
         * 
         * @param experimental
         *     For testing purposes, not real usage
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder experimental(Boolean experimental) {
            this.experimental = experimental;
            return this;
        }

        /**
         * <p>
         * A code or group definition that describes the intended subject of the activity being defined.
         * </p>
         * 
         * @param subject
         *     Type of individual the activity definition is intended for
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subject(Element subject) {
            this.subject = subject;
            return this;
        }

        /**
         * <p>
         * The date (and optionally time) when the activity definition was published. The date must change when the business 
         * version changes and it must change if the status code changes. In addition, it should change when the substantive 
         * content of the activity definition changes.
         * </p>
         * 
         * @param date
         *     Date last changed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * <p>
         * The name of the organization or individual that published the activity definition.
         * </p>
         * 
         * @param publisher
         *     Name of the publisher (organization or individual)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        /**
         * <p>
         * Contact details to assist a user in finding and communicating with the publisher.
         * </p>
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contact(ContactDetail... contact) {
            for (ContactDetail value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Contact details to assist a user in finding and communicating with the publisher.
         * </p>
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact.addAll(contact);
            return this;
        }

        /**
         * <p>
         * A free text natural language description of the activity definition from a consumer's perspective.
         * </p>
         * 
         * @param description
         *     Natural language description of the activity definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * <p>
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate activity definition instances.
         * </p>
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder useContext(UsageContext... useContext) {
            for (UsageContext value : useContext) {
                this.useContext.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate activity definition instances.
         * </p>
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder useContext(Collection<UsageContext> useContext) {
            this.useContext.addAll(useContext);
            return this;
        }

        /**
         * <p>
         * A legal or geographic region in which the activity definition is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for activity definition (if applicable)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder jurisdiction(CodeableConcept... jurisdiction) {
            for (CodeableConcept value : jurisdiction) {
                this.jurisdiction.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A legal or geographic region in which the activity definition is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for activity definition (if applicable)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
            this.jurisdiction.addAll(jurisdiction);
            return this;
        }

        /**
         * <p>
         * Explanation of why this activity definition is needed and why it has been designed as it has.
         * </p>
         * 
         * @param purpose
         *     Why this activity definition is defined
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * <p>
         * A detailed description of how the activity definition is used from a clinical perspective.
         * </p>
         * 
         * @param usage
         *     Describes the clinical usage of the activity definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder usage(String usage) {
            this.usage = usage;
            return this;
        }

        /**
         * <p>
         * A copyright statement relating to the activity definition and/or its contents. Copyright statements are generally 
         * legal restrictions on the use and publishing of the activity definition.
         * </p>
         * 
         * @param copyright
         *     Use and/or publishing restrictions
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder copyright(Markdown copyright) {
            this.copyright = copyright;
            return this;
        }

        /**
         * <p>
         * The date on which the resource content was approved by the publisher. Approval happens once when the content is 
         * officially approved for usage.
         * </p>
         * 
         * @param approvalDate
         *     When the activity definition was approved by publisher
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder approvalDate(Date approvalDate) {
            this.approvalDate = approvalDate;
            return this;
        }

        /**
         * <p>
         * The date on which the resource content was last reviewed. Review happens periodically after approval but does not 
         * change the original approval date.
         * </p>
         * 
         * @param lastReviewDate
         *     When the activity definition was last reviewed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder lastReviewDate(Date lastReviewDate) {
            this.lastReviewDate = lastReviewDate;
            return this;
        }

        /**
         * <p>
         * The period during which the activity definition content was or is planned to be in active use.
         * </p>
         * 
         * @param effectivePeriod
         *     When the activity definition is expected to be used
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder effectivePeriod(Period effectivePeriod) {
            this.effectivePeriod = effectivePeriod;
            return this;
        }

        /**
         * <p>
         * Descriptive topics related to the content of the activity. Topics provide a high-level categorization of the activity 
         * that can be useful for filtering and searching.
         * </p>
         * 
         * @param topic
         *     E.g. Education, Treatment, Assessment, etc.
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder topic(CodeableConcept... topic) {
            for (CodeableConcept value : topic) {
                this.topic.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Descriptive topics related to the content of the activity. Topics provide a high-level categorization of the activity 
         * that can be useful for filtering and searching.
         * </p>
         * 
         * @param topic
         *     E.g. Education, Treatment, Assessment, etc.
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder topic(Collection<CodeableConcept> topic) {
            this.topic.addAll(topic);
            return this;
        }

        /**
         * <p>
         * An individiual or organization primarily involved in the creation and maintenance of the content.
         * </p>
         * 
         * @param author
         *     Who authored the content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder author(ContactDetail... author) {
            for (ContactDetail value : author) {
                this.author.add(value);
            }
            return this;
        }

        /**
         * <p>
         * An individiual or organization primarily involved in the creation and maintenance of the content.
         * </p>
         * 
         * @param author
         *     Who authored the content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder author(Collection<ContactDetail> author) {
            this.author.addAll(author);
            return this;
        }

        /**
         * <p>
         * An individual or organization primarily responsible for internal coherence of the content.
         * </p>
         * 
         * @param editor
         *     Who edited the content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder editor(ContactDetail... editor) {
            for (ContactDetail value : editor) {
                this.editor.add(value);
            }
            return this;
        }

        /**
         * <p>
         * An individual or organization primarily responsible for internal coherence of the content.
         * </p>
         * 
         * @param editor
         *     Who edited the content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder editor(Collection<ContactDetail> editor) {
            this.editor.addAll(editor);
            return this;
        }

        /**
         * <p>
         * An individual or organization primarily responsible for review of some aspect of the content.
         * </p>
         * 
         * @param reviewer
         *     Who reviewed the content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reviewer(ContactDetail... reviewer) {
            for (ContactDetail value : reviewer) {
                this.reviewer.add(value);
            }
            return this;
        }

        /**
         * <p>
         * An individual or organization primarily responsible for review of some aspect of the content.
         * </p>
         * 
         * @param reviewer
         *     Who reviewed the content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reviewer(Collection<ContactDetail> reviewer) {
            this.reviewer.addAll(reviewer);
            return this;
        }

        /**
         * <p>
         * An individual or organization responsible for officially endorsing the content for use in some setting.
         * </p>
         * 
         * @param endorser
         *     Who endorsed the content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder endorser(ContactDetail... endorser) {
            for (ContactDetail value : endorser) {
                this.endorser.add(value);
            }
            return this;
        }

        /**
         * <p>
         * An individual or organization responsible for officially endorsing the content for use in some setting.
         * </p>
         * 
         * @param endorser
         *     Who endorsed the content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder endorser(Collection<ContactDetail> endorser) {
            this.endorser.addAll(endorser);
            return this;
        }

        /**
         * <p>
         * Related artifacts such as additional documentation, justification, or bibliographic references.
         * </p>
         * 
         * @param relatedArtifact
         *     Additional documentation, citations, etc.
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relatedArtifact(RelatedArtifact... relatedArtifact) {
            for (RelatedArtifact value : relatedArtifact) {
                this.relatedArtifact.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Related artifacts such as additional documentation, justification, or bibliographic references.
         * </p>
         * 
         * @param relatedArtifact
         *     Additional documentation, citations, etc.
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relatedArtifact(Collection<RelatedArtifact> relatedArtifact) {
            this.relatedArtifact.addAll(relatedArtifact);
            return this;
        }

        /**
         * <p>
         * A reference to a Library resource containing any formal logic used by the activity definition.
         * </p>
         * 
         * @param library
         *     Logic used by the activity definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder library(Canonical... library) {
            for (Canonical value : library) {
                this.library.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A reference to a Library resource containing any formal logic used by the activity definition.
         * </p>
         * 
         * @param library
         *     Logic used by the activity definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder library(Collection<Canonical> library) {
            this.library.addAll(library);
            return this;
        }

        /**
         * <p>
         * A description of the kind of resource the activity definition is representing. For example, a MedicationRequest, a 
         * ServiceRequest, or a CommunicationRequest. Typically, but not always, this is a Request resource.
         * </p>
         * 
         * @param kind
         *     Kind of resource
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder kind(ActivityDefinitionKind kind) {
            this.kind = kind;
            return this;
        }

        /**
         * <p>
         * A profile to which the target of the activity definition is expected to conform.
         * </p>
         * 
         * @param profile
         *     What profile the resource needs to conform to
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder profile(Canonical profile) {
            this.profile = profile;
            return this;
        }

        /**
         * <p>
         * Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter.
         * </p>
         * 
         * @param code
         *     Detail type of activity
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * <p>
         * Indicates the level of authority/intentionality associated with the activity and where the request should fit into the 
         * workflow chain.
         * </p>
         * 
         * @param intent
         *     proposal | plan | order
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder intent(RequestIntent intent) {
            this.intent = intent;
            return this;
        }

        /**
         * <p>
         * Indicates how quickly the activity should be addressed with respect to other requests.
         * </p>
         * 
         * @param priority
         *     routine | urgent | asap | stat
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder priority(RequestPriority priority) {
            this.priority = priority;
            return this;
        }

        /**
         * <p>
         * Set this to true if the definition is to indicate that a particular activity should NOT be performed. If true, this 
         * element should be interpreted to reinforce a negative coding. For example NPO as a code with a doNotPerform of true 
         * would still indicate to NOT perform the action.
         * </p>
         * 
         * @param doNotPerform
         *     True if the activity should not be performed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder doNotPerform(Boolean doNotPerform) {
            this.doNotPerform = doNotPerform;
            return this;
        }

        /**
         * <p>
         * The period, timing or frequency upon which the described activity is to occur.
         * </p>
         * 
         * @param timing
         *     When activity is to occur
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder timing(Element timing) {
            this.timing = timing;
            return this;
        }

        /**
         * <p>
         * Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.
         * </p>
         * 
         * @param location
         *     Where it should happen
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder location(Reference location) {
            this.location = location;
            return this;
        }

        /**
         * <p>
         * Indicates who should participate in performing the action described.
         * </p>
         * 
         * @param participant
         *     Who should participate in the action
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder participant(Participant... participant) {
            for (Participant value : participant) {
                this.participant.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates who should participate in performing the action described.
         * </p>
         * 
         * @param participant
         *     Who should participate in the action
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder participant(Collection<Participant> participant) {
            this.participant.addAll(participant);
            return this;
        }

        /**
         * <p>
         * Identifies the food, drug or other product being consumed or supplied in the activity.
         * </p>
         * 
         * @param product
         *     What's administered/supplied
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
         * Identifies the quantity expected to be consumed at once (per dose, per meal, etc.).
         * </p>
         * 
         * @param quantity
         *     How much is administered/consumed/supplied
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
         * Provides detailed dosage instructions in the same way that they are described for MedicationRequest resources.
         * </p>
         * 
         * @param dosage
         *     Detailed dosage instructions
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dosage(Dosage... dosage) {
            for (Dosage value : dosage) {
                this.dosage.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Provides detailed dosage instructions in the same way that they are described for MedicationRequest resources.
         * </p>
         * 
         * @param dosage
         *     Detailed dosage instructions
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dosage(Collection<Dosage> dosage) {
            this.dosage.addAll(dosage);
            return this;
        }

        /**
         * <p>
         * Indicates the sites on the subject's body where the procedure should be performed (I.e. the target sites).
         * </p>
         * 
         * @param bodySite
         *     What part of body to perform on
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder bodySite(CodeableConcept... bodySite) {
            for (CodeableConcept value : bodySite) {
                this.bodySite.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates the sites on the subject's body where the procedure should be performed (I.e. the target sites).
         * </p>
         * 
         * @param bodySite
         *     What part of body to perform on
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder bodySite(Collection<CodeableConcept> bodySite) {
            this.bodySite.addAll(bodySite);
            return this;
        }

        /**
         * <p>
         * Defines specimen requirements for the action to be performed, such as required specimens for a lab test.
         * </p>
         * 
         * @param specimenRequirement
         *     What specimens are required to perform this action
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder specimenRequirement(Reference... specimenRequirement) {
            for (Reference value : specimenRequirement) {
                this.specimenRequirement.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Defines specimen requirements for the action to be performed, such as required specimens for a lab test.
         * </p>
         * 
         * @param specimenRequirement
         *     What specimens are required to perform this action
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder specimenRequirement(Collection<Reference> specimenRequirement) {
            this.specimenRequirement.addAll(specimenRequirement);
            return this;
        }

        /**
         * <p>
         * Defines observation requirements for the action to be performed, such as body weight or surface area.
         * </p>
         * 
         * @param observationRequirement
         *     What observations are required to perform this action
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder observationRequirement(Reference... observationRequirement) {
            for (Reference value : observationRequirement) {
                this.observationRequirement.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Defines observation requirements for the action to be performed, such as body weight or surface area.
         * </p>
         * 
         * @param observationRequirement
         *     What observations are required to perform this action
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder observationRequirement(Collection<Reference> observationRequirement) {
            this.observationRequirement.addAll(observationRequirement);
            return this;
        }

        /**
         * <p>
         * Defines the observations that are expected to be produced by the action.
         * </p>
         * 
         * @param observationResultRequirement
         *     What observations must be produced by this action
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder observationResultRequirement(Reference... observationResultRequirement) {
            for (Reference value : observationResultRequirement) {
                this.observationResultRequirement.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Defines the observations that are expected to be produced by the action.
         * </p>
         * 
         * @param observationResultRequirement
         *     What observations must be produced by this action
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder observationResultRequirement(Collection<Reference> observationResultRequirement) {
            this.observationResultRequirement.addAll(observationResultRequirement);
            return this;
        }

        /**
         * <p>
         * A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource 
         * using the ActivityDefinition instance as the input.
         * </p>
         * 
         * @param transform
         *     Transform to apply the template
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder transform(Canonical transform) {
            this.transform = transform;
            return this;
        }

        /**
         * <p>
         * Dynamic values that will be evaluated to produce values for elements of the resulting resource. For example, if the 
         * dosage of a medication must be computed based on the patient's weight, a dynamic value would be used to specify an 
         * expression that calculated the weight, and the path on the request resource that would contain the result.
         * </p>
         * 
         * @param dynamicValue
         *     Dynamic aspects of the definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dynamicValue(DynamicValue... dynamicValue) {
            for (DynamicValue value : dynamicValue) {
                this.dynamicValue.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Dynamic values that will be evaluated to produce values for elements of the resulting resource. For example, if the 
         * dosage of a medication must be computed based on the patient's weight, a dynamic value would be used to specify an 
         * expression that calculated the weight, and the path on the request resource that would contain the result.
         * </p>
         * 
         * @param dynamicValue
         *     Dynamic aspects of the definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dynamicValue(Collection<DynamicValue> dynamicValue) {
            this.dynamicValue.addAll(dynamicValue);
            return this;
        }

        @Override
        public ActivityDefinition build() {
            return new ActivityDefinition(this);
        }
    }

    /**
     * <p>
     * Indicates who should participate in performing the action described.
     * </p>
     */
    public static class Participant extends BackboneElement {
        private final ActivityParticipantType type;
        private final CodeableConcept role;

        private Participant(Builder builder) {
            super(builder);
            this.type = ValidationSupport.requireNonNull(builder.type, "type");
            this.role = builder.role;
        }

        /**
         * <p>
         * The type of participant in the action.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ActivityParticipantType}.
         */
        public ActivityParticipantType getType() {
            return type;
        }

        /**
         * <p>
         * The role the participant should play in performing the described action.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getRole() {
            return role;
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
                    accept(role, "role", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(ActivityParticipantType type) {
            return new Builder(type);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final ActivityParticipantType type;

            // optional
            private CodeableConcept role;

            private Builder(ActivityParticipantType type) {
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
             * The role the participant should play in performing the described action.
             * </p>
             * 
             * @param role
             *     E.g. Nurse, Surgeon, Parent, etc.
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder role(CodeableConcept role) {
                this.role = role;
                return this;
            }

            @Override
            public Participant build() {
                return new Participant(this);
            }

            private static Builder from(Participant participant) {
                Builder builder = new Builder(participant.type);
                builder.id = participant.id;
                builder.extension.addAll(participant.extension);
                builder.modifierExtension.addAll(participant.modifierExtension);
                builder.role = participant.role;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Dynamic values that will be evaluated to produce values for elements of the resulting resource. For example, if the 
     * dosage of a medication must be computed based on the patient's weight, a dynamic value would be used to specify an 
     * expression that calculated the weight, and the path on the request resource that would contain the result.
     * </p>
     */
    public static class DynamicValue extends BackboneElement {
        private final String path;
        private final Expression expression;

        private DynamicValue(Builder builder) {
            super(builder);
            this.path = ValidationSupport.requireNonNull(builder.path, "path");
            this.expression = ValidationSupport.requireNonNull(builder.expression, "expression");
        }

        /**
         * <p>
         * The path to the element to be customized. This is the path on the resource that will hold the result of the 
         * calculation defined by the expression. The specified path SHALL be a FHIRPath resolveable on the specified target type 
         * of the ActivityDefinition, and SHALL consist only of identifiers, constant indexers, and a restricted subset of 
         * functions. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to 
         * traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getPath() {
            return path;
        }

        /**
         * <p>
         * An expression specifying the value of the customized element.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Expression}.
         */
        public Expression getExpression() {
            return expression;
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
                    accept(path, "path", visitor);
                    accept(expression, "expression", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(String path, Expression expression) {
            return new Builder(path, expression);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String path;
            private final Expression expression;

            private Builder(String path, Expression expression) {
                super();
                this.path = path;
                this.expression = expression;
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

            @Override
            public DynamicValue build() {
                return new DynamicValue(this);
            }

            private static Builder from(DynamicValue dynamicValue) {
                Builder builder = new Builder(dynamicValue.path, dynamicValue.expression);
                builder.id = dynamicValue.id;
                builder.extension.addAll(dynamicValue.extension);
                builder.modifierExtension.addAll(dynamicValue.modifierExtension);
                return builder;
            }
        }
    }
}

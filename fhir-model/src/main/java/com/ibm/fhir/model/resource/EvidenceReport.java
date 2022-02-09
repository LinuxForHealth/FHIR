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
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ReportRelationshipType;
import com.ibm.fhir.model.type.code.SectionMode;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The EvidenceReport Resource is a specialized container for a collection of resources and codable concepts, adapted to 
 * support compositions of Evidence, EvidenceVariable, and Citation resources and related concepts.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "cnl-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/EvidenceReport"
)
@Constraint(
    id = "evidenceReport-1",
    level = "Warning",
    location = "subject.characteristic.code",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/focus-characteristic-code",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/focus-characteristic-code', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/EvidenceReport",
    generated = true
)
@Constraint(
    id = "evidenceReport-2",
    level = "Warning",
    location = "section.focus",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/evidence-report-section",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/evidence-report-section', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/EvidenceReport",
    generated = true
)
@Constraint(
    id = "evidenceReport-3",
    level = "Warning",
    location = "section.orderedBy",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/list-order",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/list-order', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/EvidenceReport",
    generated = true
)
@Constraint(
    id = "evidenceReport-4",
    level = "Warning",
    location = "section.entryClassifier",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/evidence-classifier-code",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/evidence-classifier-code', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/EvidenceReport",
    generated = true
)
@Constraint(
    id = "evidenceReport-5",
    level = "Warning",
    location = "section.emptyReason",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/list-empty-reason",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/list-empty-reason', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/EvidenceReport",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class EvidenceReport extends DomainResource {
    @Summary
    private final Uri url;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status|4.3.0-CIBUILD"
    )
    @Required
    private final PublicationStatus status;
    @Summary
    private final List<UsageContext> useContext;
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final List<Identifier> relatedIdentifier;
    @ReferenceTarget({ "Citation" })
    @Choice({ Reference.class, Markdown.class })
    private final Element citeAs;
    @Binding(
        bindingName = "EvidenceReportType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The kind of report, such as grouping of classifiers, search results, or human-compiled expression.",
        valueSet = "http://hl7.org/fhir/ValueSet/evidence-report-type"
    )
    private final CodeableConcept type;
    private final List<Annotation> note;
    private final List<RelatedArtifact> relatedArtifact;
    @Summary
    @Required
    private final Subject subject;
    @Summary
    private final String publisher;
    @Summary
    private final List<ContactDetail> contact;
    @Summary
    private final List<ContactDetail> author;
    private final List<ContactDetail> editor;
    private final List<ContactDetail> reviewer;
    @Summary
    private final List<ContactDetail> endorser;
    private final List<RelatesTo> relatesTo;
    private final List<Section> section;

    private EvidenceReport(Builder builder) {
        super(builder);
        url = builder.url;
        status = builder.status;
        useContext = Collections.unmodifiableList(builder.useContext);
        identifier = Collections.unmodifiableList(builder.identifier);
        relatedIdentifier = Collections.unmodifiableList(builder.relatedIdentifier);
        citeAs = builder.citeAs;
        type = builder.type;
        note = Collections.unmodifiableList(builder.note);
        relatedArtifact = Collections.unmodifiableList(builder.relatedArtifact);
        subject = builder.subject;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        author = Collections.unmodifiableList(builder.author);
        editor = Collections.unmodifiableList(builder.editor);
        reviewer = Collections.unmodifiableList(builder.reviewer);
        endorser = Collections.unmodifiableList(builder.endorser);
        relatesTo = Collections.unmodifiableList(builder.relatesTo);
        section = Collections.unmodifiableList(builder.section);
    }

    /**
     * An absolute URI that is used to identify this EvidenceReport when it is referenced in a specification, model, design 
     * or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address 
     * at which at which an authoritative instance of this summary is (or will be) published. This URL can be the target of a 
     * canonical reference. It SHALL remain the same when the summary is stored on different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * The status of this summary. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate evidence report instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A formal identifier that is used to identify this EvidenceReport when it is represented in other formats, or 
     * referenced in a specification, model, design or an instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * A formal identifier that is used to identify things closely related to this EvidenceReport.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getRelatedIdentifier() {
        return relatedIdentifier;
    }

    /**
     * Citation Resource or display of suggested citation for this report.
     * 
     * @return
     *     An immutable object of type {@link Reference} or {@link Markdown} that may be null.
     */
    public Element getCiteAs() {
        return citeAs;
    }

    /**
     * Specifies the kind of report, such as grouping of classifiers, search results, or human-compiled expression.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * Used for footnotes and annotations.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * Link, description or reference to artifact associated with the report.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RelatedArtifact} that may be empty.
     */
    public List<RelatedArtifact> getRelatedArtifact() {
        return relatedArtifact;
    }

    /**
     * Specifies the subject or focus of the report. Answers "What is this report about?".
     * 
     * @return
     *     An immutable object of type {@link Subject} that is non-null.
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * The name of the organization or individual that published the evidence report.
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
     * An individiual, organization, or device primarily involved in the creation and maintenance of the content.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getAuthor() {
        return author;
    }

    /**
     * An individiual, organization, or device primarily responsible for internal coherence of the content.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getEditor() {
        return editor;
    }

    /**
     * An individiual, organization, or device primarily responsible for review of some aspect of the content.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getReviewer() {
        return reviewer;
    }

    /**
     * An individiual, organization, or device responsible for officially endorsing the content for use in some setting.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getEndorser() {
        return endorser;
    }

    /**
     * Relationships that this composition has with other compositions or documents that already exist.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RelatesTo} that may be empty.
     */
    public List<RelatesTo> getRelatesTo() {
        return relatesTo;
    }

    /**
     * The root of the sections that make up the composition.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Section} that may be empty.
     */
    public List<Section> getSection() {
        return section;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
            (status != null) || 
            !useContext.isEmpty() || 
            !identifier.isEmpty() || 
            !relatedIdentifier.isEmpty() || 
            (citeAs != null) || 
            (type != null) || 
            !note.isEmpty() || 
            !relatedArtifact.isEmpty() || 
            (subject != null) || 
            (publisher != null) || 
            !contact.isEmpty() || 
            !author.isEmpty() || 
            !editor.isEmpty() || 
            !reviewer.isEmpty() || 
            !endorser.isEmpty() || 
            !relatesTo.isEmpty() || 
            !section.isEmpty();
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
                accept(status, "status", visitor);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(relatedIdentifier, "relatedIdentifier", visitor, Identifier.class);
                accept(citeAs, "citeAs", visitor);
                accept(type, "type", visitor);
                accept(note, "note", visitor, Annotation.class);
                accept(relatedArtifact, "relatedArtifact", visitor, RelatedArtifact.class);
                accept(subject, "subject", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(author, "author", visitor, ContactDetail.class);
                accept(editor, "editor", visitor, ContactDetail.class);
                accept(reviewer, "reviewer", visitor, ContactDetail.class);
                accept(endorser, "endorser", visitor, ContactDetail.class);
                accept(relatesTo, "relatesTo", visitor, RelatesTo.class);
                accept(section, "section", visitor, Section.class);
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
        EvidenceReport other = (EvidenceReport) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(url, other.url) && 
            Objects.equals(status, other.status) && 
            Objects.equals(useContext, other.useContext) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(relatedIdentifier, other.relatedIdentifier) && 
            Objects.equals(citeAs, other.citeAs) && 
            Objects.equals(type, other.type) && 
            Objects.equals(note, other.note) && 
            Objects.equals(relatedArtifact, other.relatedArtifact) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(author, other.author) && 
            Objects.equals(editor, other.editor) && 
            Objects.equals(reviewer, other.reviewer) && 
            Objects.equals(endorser, other.endorser) && 
            Objects.equals(relatesTo, other.relatesTo) && 
            Objects.equals(section, other.section);
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
                status, 
                useContext, 
                identifier, 
                relatedIdentifier, 
                citeAs, 
                type, 
                note, 
                relatedArtifact, 
                subject, 
                publisher, 
                contact, 
                author, 
                editor, 
                reviewer, 
                endorser, 
                relatesTo, 
                section);
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
        private PublicationStatus status;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<Identifier> identifier = new ArrayList<>();
        private List<Identifier> relatedIdentifier = new ArrayList<>();
        private Element citeAs;
        private CodeableConcept type;
        private List<Annotation> note = new ArrayList<>();
        private List<RelatedArtifact> relatedArtifact = new ArrayList<>();
        private Subject subject;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private List<ContactDetail> author = new ArrayList<>();
        private List<ContactDetail> editor = new ArrayList<>();
        private List<ContactDetail> reviewer = new ArrayList<>();
        private List<ContactDetail> endorser = new ArrayList<>();
        private List<RelatesTo> relatesTo = new ArrayList<>();
        private List<Section> section = new ArrayList<>();

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
         * An absolute URI that is used to identify this EvidenceReport when it is referenced in a specification, model, design 
         * or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address 
         * at which at which an authoritative instance of this summary is (or will be) published. This URL can be the target of a 
         * canonical reference. It SHALL remain the same when the summary is stored on different servers.
         * 
         * @param url
         *     Canonical identifier for this EvidenceReport, represented as a globally unique URI
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * The status of this summary. Enables tracking the life-cycle of the content.
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
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate evidence report instances.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param useContext
         *     The context that the content is intended to support
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
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate evidence report instances.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param useContext
         *     The context that the content is intended to support
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
         * A formal identifier that is used to identify this EvidenceReport when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Unique identifier for the evidence report
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
         * A formal identifier that is used to identify this EvidenceReport when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Unique identifier for the evidence report
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
         * A formal identifier that is used to identify things closely related to this EvidenceReport.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedIdentifier
         *     Identifiers for articles that may relate to more than one evidence report
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relatedIdentifier(Identifier... relatedIdentifier) {
            for (Identifier value : relatedIdentifier) {
                this.relatedIdentifier.add(value);
            }
            return this;
        }

        /**
         * A formal identifier that is used to identify things closely related to this EvidenceReport.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedIdentifier
         *     Identifiers for articles that may relate to more than one evidence report
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder relatedIdentifier(Collection<Identifier> relatedIdentifier) {
            this.relatedIdentifier = new ArrayList<>(relatedIdentifier);
            return this;
        }

        /**
         * Citation Resource or display of suggested citation for this report.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Reference}</li>
         * <li>{@link Markdown}</li>
         * </ul>
         * 
         * When of type {@link Reference}, the allowed resource types for this reference are:
         * <ul>
         * <li>{@link Citation}</li>
         * </ul>
         * 
         * @param citeAs
         *     Citation for this report
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder citeAs(Element citeAs) {
            this.citeAs = citeAs;
            return this;
        }

        /**
         * Specifies the kind of report, such as grouping of classifiers, search results, or human-compiled expression.
         * 
         * @param type
         *     Kind of report
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * Used for footnotes and annotations.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Used for footnotes and annotations
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
         * Used for footnotes and annotations.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Used for footnotes and annotations
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
         * Link, description or reference to artifact associated with the report.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedArtifact
         *     Link, description or reference to artifact associated with the report
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relatedArtifact(RelatedArtifact... relatedArtifact) {
            for (RelatedArtifact value : relatedArtifact) {
                this.relatedArtifact.add(value);
            }
            return this;
        }

        /**
         * Link, description or reference to artifact associated with the report.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedArtifact
         *     Link, description or reference to artifact associated with the report
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder relatedArtifact(Collection<RelatedArtifact> relatedArtifact) {
            this.relatedArtifact = new ArrayList<>(relatedArtifact);
            return this;
        }

        /**
         * Specifies the subject or focus of the report. Answers "What is this report about?".
         * 
         * <p>This element is required.
         * 
         * @param subject
         *     Focus of the report
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Subject subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Convenience method for setting {@code publisher}.
         * 
         * @param publisher
         *     Name of the publisher (organization or individual)
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
         * The name of the organization or individual that published the evidence report.
         * 
         * @param publisher
         *     Name of the publisher (organization or individual)
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
         * An individiual, organization, or device primarily involved in the creation and maintenance of the content.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param author
         *     Who authored the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(ContactDetail... author) {
            for (ContactDetail value : author) {
                this.author.add(value);
            }
            return this;
        }

        /**
         * An individiual, organization, or device primarily involved in the creation and maintenance of the content.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param author
         *     Who authored the content
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder author(Collection<ContactDetail> author) {
            this.author = new ArrayList<>(author);
            return this;
        }

        /**
         * An individiual, organization, or device primarily responsible for internal coherence of the content.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param editor
         *     Who edited the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder editor(ContactDetail... editor) {
            for (ContactDetail value : editor) {
                this.editor.add(value);
            }
            return this;
        }

        /**
         * An individiual, organization, or device primarily responsible for internal coherence of the content.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param editor
         *     Who edited the content
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder editor(Collection<ContactDetail> editor) {
            this.editor = new ArrayList<>(editor);
            return this;
        }

        /**
         * An individiual, organization, or device primarily responsible for review of some aspect of the content.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reviewer
         *     Who reviewed the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reviewer(ContactDetail... reviewer) {
            for (ContactDetail value : reviewer) {
                this.reviewer.add(value);
            }
            return this;
        }

        /**
         * An individiual, organization, or device primarily responsible for review of some aspect of the content.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reviewer
         *     Who reviewed the content
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reviewer(Collection<ContactDetail> reviewer) {
            this.reviewer = new ArrayList<>(reviewer);
            return this;
        }

        /**
         * An individiual, organization, or device responsible for officially endorsing the content for use in some setting.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param endorser
         *     Who endorsed the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder endorser(ContactDetail... endorser) {
            for (ContactDetail value : endorser) {
                this.endorser.add(value);
            }
            return this;
        }

        /**
         * An individiual, organization, or device responsible for officially endorsing the content for use in some setting.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param endorser
         *     Who endorsed the content
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder endorser(Collection<ContactDetail> endorser) {
            this.endorser = new ArrayList<>(endorser);
            return this;
        }

        /**
         * Relationships that this composition has with other compositions or documents that already exist.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatesTo
         *     Relationships to other compositions/documents
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relatesTo(RelatesTo... relatesTo) {
            for (RelatesTo value : relatesTo) {
                this.relatesTo.add(value);
            }
            return this;
        }

        /**
         * Relationships that this composition has with other compositions or documents that already exist.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatesTo
         *     Relationships to other compositions/documents
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder relatesTo(Collection<RelatesTo> relatesTo) {
            this.relatesTo = new ArrayList<>(relatesTo);
            return this;
        }

        /**
         * The root of the sections that make up the composition.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param section
         *     Composition is broken into sections
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder section(Section... section) {
            for (Section value : section) {
                this.section.add(value);
            }
            return this;
        }

        /**
         * The root of the sections that make up the composition.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param section
         *     Composition is broken into sections
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder section(Collection<Section> section) {
            this.section = new ArrayList<>(section);
            return this;
        }

        /**
         * Build the {@link EvidenceReport}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>subject</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link EvidenceReport}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid EvidenceReport per the base specification
         */
        @Override
        public EvidenceReport build() {
            EvidenceReport evidenceReport = new EvidenceReport(this);
            if (validating) {
                validate(evidenceReport);
            }
            return evidenceReport;
        }

        protected void validate(EvidenceReport evidenceReport) {
            super.validate(evidenceReport);
            ValidationSupport.requireNonNull(evidenceReport.status, "status");
            ValidationSupport.checkList(evidenceReport.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(evidenceReport.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(evidenceReport.relatedIdentifier, "relatedIdentifier", Identifier.class);
            ValidationSupport.choiceElement(evidenceReport.citeAs, "citeAs", Reference.class, Markdown.class);
            ValidationSupport.checkList(evidenceReport.note, "note", Annotation.class);
            ValidationSupport.checkList(evidenceReport.relatedArtifact, "relatedArtifact", RelatedArtifact.class);
            ValidationSupport.requireNonNull(evidenceReport.subject, "subject");
            ValidationSupport.checkList(evidenceReport.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(evidenceReport.author, "author", ContactDetail.class);
            ValidationSupport.checkList(evidenceReport.editor, "editor", ContactDetail.class);
            ValidationSupport.checkList(evidenceReport.reviewer, "reviewer", ContactDetail.class);
            ValidationSupport.checkList(evidenceReport.endorser, "endorser", ContactDetail.class);
            ValidationSupport.checkList(evidenceReport.relatesTo, "relatesTo", RelatesTo.class);
            ValidationSupport.checkList(evidenceReport.section, "section", Section.class);
            ValidationSupport.checkReferenceType(evidenceReport.citeAs, "citeAs", "Citation");
        }

        protected Builder from(EvidenceReport evidenceReport) {
            super.from(evidenceReport);
            url = evidenceReport.url;
            status = evidenceReport.status;
            useContext.addAll(evidenceReport.useContext);
            identifier.addAll(evidenceReport.identifier);
            relatedIdentifier.addAll(evidenceReport.relatedIdentifier);
            citeAs = evidenceReport.citeAs;
            type = evidenceReport.type;
            note.addAll(evidenceReport.note);
            relatedArtifact.addAll(evidenceReport.relatedArtifact);
            subject = evidenceReport.subject;
            publisher = evidenceReport.publisher;
            contact.addAll(evidenceReport.contact);
            author.addAll(evidenceReport.author);
            editor.addAll(evidenceReport.editor);
            reviewer.addAll(evidenceReport.reviewer);
            endorser.addAll(evidenceReport.endorser);
            relatesTo.addAll(evidenceReport.relatesTo);
            section.addAll(evidenceReport.section);
            return this;
        }
    }

    /**
     * Specifies the subject or focus of the report. Answers "What is this report about?".
     */
    public static class Subject extends BackboneElement {
        private final List<Characteristic> characteristic;
        private final List<Annotation> note;

        private Subject(Builder builder) {
            super(builder);
            characteristic = Collections.unmodifiableList(builder.characteristic);
            note = Collections.unmodifiableList(builder.note);
        }

        /**
         * Characteristic.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Characteristic} that may be empty.
         */
        public List<Characteristic> getCharacteristic() {
            return characteristic;
        }

        /**
         * Used for general notes and annotations not coded elsewhere.
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
                !characteristic.isEmpty() || 
                !note.isEmpty();
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
                    accept(characteristic, "characteristic", visitor, Characteristic.class);
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
            Subject other = (Subject) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(characteristic, other.characteristic) && 
                Objects.equals(note, other.note);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    characteristic, 
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

        public static class Builder extends BackboneElement.Builder {
            private List<Characteristic> characteristic = new ArrayList<>();
            private List<Annotation> note = new ArrayList<>();

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
             * Characteristic.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param characteristic
             *     Characteristic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder characteristic(Characteristic... characteristic) {
                for (Characteristic value : characteristic) {
                    this.characteristic.add(value);
                }
                return this;
            }

            /**
             * Characteristic.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param characteristic
             *     Characteristic
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder characteristic(Collection<Characteristic> characteristic) {
                this.characteristic = new ArrayList<>(characteristic);
                return this;
            }

            /**
             * Used for general notes and annotations not coded elsewhere.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Footnotes and/or explanatory notes
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
             * Used for general notes and annotations not coded elsewhere.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Footnotes and/or explanatory notes
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
             * Build the {@link Subject}
             * 
             * @return
             *     An immutable object of type {@link Subject}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Subject per the base specification
             */
            @Override
            public Subject build() {
                Subject subject = new Subject(this);
                if (validating) {
                    validate(subject);
                }
                return subject;
            }

            protected void validate(Subject subject) {
                super.validate(subject);
                ValidationSupport.checkList(subject.characteristic, "characteristic", Characteristic.class);
                ValidationSupport.checkList(subject.note, "note", Annotation.class);
                ValidationSupport.requireValueOrChildren(subject);
            }

            protected Builder from(Subject subject) {
                super.from(subject);
                characteristic.addAll(subject.characteristic);
                note.addAll(subject.note);
                return this;
            }
        }

        /**
         * Characteristic.
         */
        public static class Characteristic extends BackboneElement {
            @Binding(
                bindingName = "FocusCharacteristicCode",
                strength = BindingStrength.Value.EXTENSIBLE,
                description = "Evidence focus characteristic code.",
                valueSet = "http://hl7.org/fhir/ValueSet/focus-characteristic-code"
            )
            @Required
            private final CodeableConcept code;
            @Choice({ Reference.class, CodeableConcept.class, Boolean.class, Quantity.class, Range.class })
            @Required
            private final Element value;
            private final Boolean exclude;
            private final Period period;

            private Characteristic(Builder builder) {
                super(builder);
                code = builder.code;
                value = builder.value;
                exclude = builder.exclude;
                period = builder.period;
            }

            /**
             * Characteristic code.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getCode() {
                return code;
            }

            /**
             * Characteristic value.
             * 
             * @return
             *     An immutable object of type {@link Reference}, {@link CodeableConcept}, {@link Boolean}, {@link Quantity} or {@link 
             *     Range} that is non-null.
             */
            public Element getValue() {
                return value;
            }

            /**
             * Is used to express not the characteristic.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getExclude() {
                return exclude;
            }

            /**
             * Timeframe for the characteristic.
             * 
             * @return
             *     An immutable object of type {@link Period} that may be null.
             */
            public Period getPeriod() {
                return period;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (code != null) || 
                    (value != null) || 
                    (exclude != null) || 
                    (period != null);
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
                        accept(code, "code", visitor);
                        accept(value, "value", visitor);
                        accept(exclude, "exclude", visitor);
                        accept(period, "period", visitor);
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
                Characteristic other = (Characteristic) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(code, other.code) && 
                    Objects.equals(value, other.value) && 
                    Objects.equals(exclude, other.exclude) && 
                    Objects.equals(period, other.period);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        code, 
                        value, 
                        exclude, 
                        period);
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
                private CodeableConcept code;
                private Element value;
                private Boolean exclude;
                private Period period;

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
                 * Characteristic code.
                 * 
                 * <p>This element is required.
                 * 
                 * @param code
                 *     Characteristic code
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder code(CodeableConcept code) {
                    this.code = code;
                    return this;
                }

                /**
                 * Convenience method for setting {@code value} with choice type Boolean.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Characteristic value
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #value(Element)
                 */
                public Builder value(java.lang.Boolean value) {
                    this.value = (value == null) ? null : Boolean.of(value);
                    return this;
                }

                /**
                 * Characteristic value.
                 * 
                 * <p>This element is required.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Reference}</li>
                 * <li>{@link CodeableConcept}</li>
                 * <li>{@link Boolean}</li>
                 * <li>{@link Quantity}</li>
                 * <li>{@link Range}</li>
                 * </ul>
                 * 
                 * @param value
                 *     Characteristic value
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(Element value) {
                    this.value = value;
                    return this;
                }

                /**
                 * Convenience method for setting {@code exclude}.
                 * 
                 * @param exclude
                 *     Is used to express not the characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #exclude(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder exclude(java.lang.Boolean exclude) {
                    this.exclude = (exclude == null) ? null : Boolean.of(exclude);
                    return this;
                }

                /**
                 * Is used to express not the characteristic.
                 * 
                 * @param exclude
                 *     Is used to express not the characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder exclude(Boolean exclude) {
                    this.exclude = exclude;
                    return this;
                }

                /**
                 * Timeframe for the characteristic.
                 * 
                 * @param period
                 *     Timeframe for the characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder period(Period period) {
                    this.period = period;
                    return this;
                }

                /**
                 * Build the {@link Characteristic}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>code</li>
                 * <li>value</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Characteristic}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Characteristic per the base specification
                 */
                @Override
                public Characteristic build() {
                    Characteristic characteristic = new Characteristic(this);
                    if (validating) {
                        validate(characteristic);
                    }
                    return characteristic;
                }

                protected void validate(Characteristic characteristic) {
                    super.validate(characteristic);
                    ValidationSupport.requireNonNull(characteristic.code, "code");
                    ValidationSupport.requireChoiceElement(characteristic.value, "value", Reference.class, CodeableConcept.class, Boolean.class, Quantity.class, Range.class);
                    ValidationSupport.requireValueOrChildren(characteristic);
                }

                protected Builder from(Characteristic characteristic) {
                    super.from(characteristic);
                    code = characteristic.code;
                    value = characteristic.value;
                    exclude = characteristic.exclude;
                    period = characteristic.period;
                    return this;
                }
            }
        }
    }

    /**
     * Relationships that this composition has with other compositions or documents that already exist.
     */
    public static class RelatesTo extends BackboneElement {
        @Binding(
            bindingName = "ReportRelationshipType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The type of relationship between reports.",
            valueSet = "http://hl7.org/fhir/ValueSet/report-relation-type|4.3.0-CIBUILD"
        )
        @Required
        private final ReportRelationshipType code;
        @ReferenceTarget({ "EvidenceReport" })
        @Choice({ Identifier.class, Reference.class })
        @Required
        private final Element target;

        private RelatesTo(Builder builder) {
            super(builder);
            code = builder.code;
            target = builder.target;
        }

        /**
         * The type of relationship that this composition has with anther composition or document.
         * 
         * @return
         *     An immutable object of type {@link ReportRelationshipType} that is non-null.
         */
        public ReportRelationshipType getCode() {
            return code;
        }

        /**
         * The target composition/document of this relationship.
         * 
         * @return
         *     An immutable object of type {@link Identifier} or {@link Reference} that is non-null.
         */
        public Element getTarget() {
            return target;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                (target != null);
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
                    accept(code, "code", visitor);
                    accept(target, "target", visitor);
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
            RelatesTo other = (RelatesTo) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(target, other.target);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    target);
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
            private ReportRelationshipType code;
            private Element target;

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
             * The type of relationship that this composition has with anther composition or document.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     replaces | amends | appends | transforms | replacedWith | amendedWith | appendedWith | transformedWith
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(ReportRelationshipType code) {
                this.code = code;
                return this;
            }

            /**
             * The target composition/document of this relationship.
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Identifier}</li>
             * <li>{@link Reference}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link EvidenceReport}</li>
             * </ul>
             * 
             * @param target
             *     Target of the relationship
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder target(Element target) {
                this.target = target;
                return this;
            }

            /**
             * Build the {@link RelatesTo}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * <li>target</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link RelatesTo}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid RelatesTo per the base specification
             */
            @Override
            public RelatesTo build() {
                RelatesTo relatesTo = new RelatesTo(this);
                if (validating) {
                    validate(relatesTo);
                }
                return relatesTo;
            }

            protected void validate(RelatesTo relatesTo) {
                super.validate(relatesTo);
                ValidationSupport.requireNonNull(relatesTo.code, "code");
                ValidationSupport.requireChoiceElement(relatesTo.target, "target", Identifier.class, Reference.class);
                ValidationSupport.checkReferenceType(relatesTo.target, "target", "EvidenceReport");
                ValidationSupport.requireValueOrChildren(relatesTo);
            }

            protected Builder from(RelatesTo relatesTo) {
                super.from(relatesTo);
                code = relatesTo.code;
                target = relatesTo.target;
                return this;
            }
        }
    }

    /**
     * The root of the sections that make up the composition.
     */
    public static class Section extends BackboneElement {
        private final String title;
        @Binding(
            bindingName = "ReportSectionType",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Evidence Report Section Type.",
            valueSet = "http://hl7.org/fhir/ValueSet/evidence-report-section"
        )
        private final CodeableConcept focus;
        private final Reference focusReference;
        @ReferenceTarget({ "Person", "Device", "Group", "Organization" })
        private final List<Reference> author;
        private final Narrative text;
        @Binding(
            bindingName = "SectionMode",
            strength = BindingStrength.Value.REQUIRED,
            description = "The processing mode that applies to this section.",
            valueSet = "http://hl7.org/fhir/ValueSet/list-mode|4.3.0-CIBUILD"
        )
        private final SectionMode mode;
        @Binding(
            bindingName = "SectionEntryOrder",
            strength = BindingStrength.Value.PREFERRED,
            description = "What order applies to the items in the entry.",
            valueSet = "http://hl7.org/fhir/ValueSet/list-order"
        )
        private final CodeableConcept orderedBy;
        @Binding(
            bindingName = "EvidenceClassifier",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Commonly used classifiers for evidence sets.",
            valueSet = "http://hl7.org/fhir/ValueSet/evidence-classifier-code"
        )
        private final List<CodeableConcept> entryClassifier;
        private final List<Reference> entryReference;
        private final List<Quantity> entryQuantity;
        @Binding(
            bindingName = "SectionEmptyReason",
            strength = BindingStrength.Value.PREFERRED,
            description = "If a section is empty, why it is empty.",
            valueSet = "http://hl7.org/fhir/ValueSet/list-empty-reason"
        )
        private final CodeableConcept emptyReason;
        private final List<EvidenceReport.Section> section;

        private Section(Builder builder) {
            super(builder);
            title = builder.title;
            focus = builder.focus;
            focusReference = builder.focusReference;
            author = Collections.unmodifiableList(builder.author);
            text = builder.text;
            mode = builder.mode;
            orderedBy = builder.orderedBy;
            entryClassifier = Collections.unmodifiableList(builder.entryClassifier);
            entryReference = Collections.unmodifiableList(builder.entryReference);
            entryQuantity = Collections.unmodifiableList(builder.entryQuantity);
            emptyReason = builder.emptyReason;
            section = Collections.unmodifiableList(builder.section);
        }

        /**
         * The label for this particular section. This will be part of the rendered content for the document, and is often used 
         * to build a table of contents.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getTitle() {
            return title;
        }

        /**
         * A code identifying the kind of content contained within the section. This should be consistent with the section title.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getFocus() {
            return focus;
        }

        /**
         * A definitional Resource identifying the kind of content contained within the section. This should be consistent with 
         * the section title.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getFocusReference() {
            return focusReference;
        }

        /**
         * Identifies who is responsible for the information in this section, not necessarily who typed it in.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getAuthor() {
            return author;
        }

        /**
         * A human-readable narrative that contains the attested content of the section, used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is peferred to contain sufficient 
         * detail to make it acceptable for a human to just read the narrative.
         * 
         * @return
         *     An immutable object of type {@link Narrative} that may be null.
         */
        public Narrative getText() {
            return text;
        }

        /**
         * How the entry list was prepared - whether it is a working list that is suitable for being maintained on an ongoing 
         * basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where 
         * items may be marked as added, modified or deleted.
         * 
         * @return
         *     An immutable object of type {@link SectionMode} that may be null.
         */
        public SectionMode getMode() {
            return mode;
        }

        /**
         * Specifies the order applied to the items in the section entries.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getOrderedBy() {
            return orderedBy;
        }

        /**
         * Specifies any type of classification of the evidence report.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getEntryClassifier() {
            return entryClassifier;
        }

        /**
         * A reference to the actual resource from which the narrative in the section is derived.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getEntryReference() {
            return entryReference;
        }

        /**
         * Quantity as content.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Quantity} that may be empty.
         */
        public List<Quantity> getEntryQuantity() {
            return entryQuantity;
        }

        /**
         * If the section is empty, why the list is empty. An empty section typically has some text explaining the empty reason.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getEmptyReason() {
            return emptyReason;
        }

        /**
         * A nested sub-section within this section.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Section} that may be empty.
         */
        public List<EvidenceReport.Section> getSection() {
            return section;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (title != null) || 
                (focus != null) || 
                (focusReference != null) || 
                !author.isEmpty() || 
                (text != null) || 
                (mode != null) || 
                (orderedBy != null) || 
                !entryClassifier.isEmpty() || 
                !entryReference.isEmpty() || 
                !entryQuantity.isEmpty() || 
                (emptyReason != null) || 
                !section.isEmpty();
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
                    accept(title, "title", visitor);
                    accept(focus, "focus", visitor);
                    accept(focusReference, "focusReference", visitor);
                    accept(author, "author", visitor, Reference.class);
                    accept(text, "text", visitor);
                    accept(mode, "mode", visitor);
                    accept(orderedBy, "orderedBy", visitor);
                    accept(entryClassifier, "entryClassifier", visitor, CodeableConcept.class);
                    accept(entryReference, "entryReference", visitor, Reference.class);
                    accept(entryQuantity, "entryQuantity", visitor, Quantity.class);
                    accept(emptyReason, "emptyReason", visitor);
                    accept(section, "section", visitor, EvidenceReport.Section.class);
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
            Section other = (Section) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(title, other.title) && 
                Objects.equals(focus, other.focus) && 
                Objects.equals(focusReference, other.focusReference) && 
                Objects.equals(author, other.author) && 
                Objects.equals(text, other.text) && 
                Objects.equals(mode, other.mode) && 
                Objects.equals(orderedBy, other.orderedBy) && 
                Objects.equals(entryClassifier, other.entryClassifier) && 
                Objects.equals(entryReference, other.entryReference) && 
                Objects.equals(entryQuantity, other.entryQuantity) && 
                Objects.equals(emptyReason, other.emptyReason) && 
                Objects.equals(section, other.section);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    title, 
                    focus, 
                    focusReference, 
                    author, 
                    text, 
                    mode, 
                    orderedBy, 
                    entryClassifier, 
                    entryReference, 
                    entryQuantity, 
                    emptyReason, 
                    section);
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
            private String title;
            private CodeableConcept focus;
            private Reference focusReference;
            private List<Reference> author = new ArrayList<>();
            private Narrative text;
            private SectionMode mode;
            private CodeableConcept orderedBy;
            private List<CodeableConcept> entryClassifier = new ArrayList<>();
            private List<Reference> entryReference = new ArrayList<>();
            private List<Quantity> entryQuantity = new ArrayList<>();
            private CodeableConcept emptyReason;
            private List<EvidenceReport.Section> section = new ArrayList<>();

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
             * Convenience method for setting {@code title}.
             * 
             * @param title
             *     Label for section (e.g. for ToC)
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
             * The label for this particular section. This will be part of the rendered content for the document, and is often used 
             * to build a table of contents.
             * 
             * @param title
             *     Label for section (e.g. for ToC)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder title(String title) {
                this.title = title;
                return this;
            }

            /**
             * A code identifying the kind of content contained within the section. This should be consistent with the section title.
             * 
             * @param focus
             *     Classification of section (recommended)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder focus(CodeableConcept focus) {
                this.focus = focus;
                return this;
            }

            /**
             * A definitional Resource identifying the kind of content contained within the section. This should be consistent with 
             * the section title.
             * 
             * @param focusReference
             *     Classification of section by Resource
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder focusReference(Reference focusReference) {
                this.focusReference = focusReference;
                return this;
            }

            /**
             * Identifies who is responsible for the information in this section, not necessarily who typed it in.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Person}</li>
             * <li>{@link Device}</li>
             * <li>{@link Group}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param author
             *     Who and/or what authored the section
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder author(Reference... author) {
                for (Reference value : author) {
                    this.author.add(value);
                }
                return this;
            }

            /**
             * Identifies who is responsible for the information in this section, not necessarily who typed it in.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Person}</li>
             * <li>{@link Device}</li>
             * <li>{@link Group}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param author
             *     Who and/or what authored the section
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder author(Collection<Reference> author) {
                this.author = new ArrayList<>(author);
                return this;
            }

            /**
             * A human-readable narrative that contains the attested content of the section, used to represent the content of the 
             * resource to a human. The narrative need not encode all the structured data, but is peferred to contain sufficient 
             * detail to make it acceptable for a human to just read the narrative.
             * 
             * @param text
             *     Text summary of the section, for human interpretation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder text(Narrative text) {
                this.text = text;
                return this;
            }

            /**
             * How the entry list was prepared - whether it is a working list that is suitable for being maintained on an ongoing 
             * basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where 
             * items may be marked as added, modified or deleted.
             * 
             * @param mode
             *     working | snapshot | changes
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder mode(SectionMode mode) {
                this.mode = mode;
                return this;
            }

            /**
             * Specifies the order applied to the items in the section entries.
             * 
             * @param orderedBy
             *     Order of section entries
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder orderedBy(CodeableConcept orderedBy) {
                this.orderedBy = orderedBy;
                return this;
            }

            /**
             * Specifies any type of classification of the evidence report.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param entryClassifier
             *     Extensible classifiers as content
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder entryClassifier(CodeableConcept... entryClassifier) {
                for (CodeableConcept value : entryClassifier) {
                    this.entryClassifier.add(value);
                }
                return this;
            }

            /**
             * Specifies any type of classification of the evidence report.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param entryClassifier
             *     Extensible classifiers as content
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder entryClassifier(Collection<CodeableConcept> entryClassifier) {
                this.entryClassifier = new ArrayList<>(entryClassifier);
                return this;
            }

            /**
             * A reference to the actual resource from which the narrative in the section is derived.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param entryReference
             *     Reference to resources as content
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder entryReference(Reference... entryReference) {
                for (Reference value : entryReference) {
                    this.entryReference.add(value);
                }
                return this;
            }

            /**
             * A reference to the actual resource from which the narrative in the section is derived.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param entryReference
             *     Reference to resources as content
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder entryReference(Collection<Reference> entryReference) {
                this.entryReference = new ArrayList<>(entryReference);
                return this;
            }

            /**
             * Quantity as content.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param entryQuantity
             *     Quantity as content
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder entryQuantity(Quantity... entryQuantity) {
                for (Quantity value : entryQuantity) {
                    this.entryQuantity.add(value);
                }
                return this;
            }

            /**
             * Quantity as content.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param entryQuantity
             *     Quantity as content
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder entryQuantity(Collection<Quantity> entryQuantity) {
                this.entryQuantity = new ArrayList<>(entryQuantity);
                return this;
            }

            /**
             * If the section is empty, why the list is empty. An empty section typically has some text explaining the empty reason.
             * 
             * @param emptyReason
             *     Why the section is empty
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder emptyReason(CodeableConcept emptyReason) {
                this.emptyReason = emptyReason;
                return this;
            }

            /**
             * A nested sub-section within this section.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param section
             *     Nested Section
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder section(EvidenceReport.Section... section) {
                for (EvidenceReport.Section value : section) {
                    this.section.add(value);
                }
                return this;
            }

            /**
             * A nested sub-section within this section.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param section
             *     Nested Section
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder section(Collection<EvidenceReport.Section> section) {
                this.section = new ArrayList<>(section);
                return this;
            }

            /**
             * Build the {@link Section}
             * 
             * @return
             *     An immutable object of type {@link Section}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Section per the base specification
             */
            @Override
            public Section build() {
                Section section = new Section(this);
                if (validating) {
                    validate(section);
                }
                return section;
            }

            protected void validate(Section section) {
                super.validate(section);
                ValidationSupport.checkList(section.author, "author", Reference.class);
                ValidationSupport.checkList(section.entryClassifier, "entryClassifier", CodeableConcept.class);
                ValidationSupport.checkList(section.entryReference, "entryReference", Reference.class);
                ValidationSupport.checkList(section.entryQuantity, "entryQuantity", Quantity.class);
                ValidationSupport.checkList(section.section, "section", EvidenceReport.Section.class);
                ValidationSupport.checkReferenceType(section.author, "author", "Person", "Device", "Group", "Organization");
                ValidationSupport.requireValueOrChildren(section);
            }

            protected Builder from(Section section) {
                super.from(section);
                title = section.title;
                focus = section.focus;
                focusReference = section.focusReference;
                author.addAll(section.author);
                text = section.text;
                mode = section.mode;
                orderedBy = section.orderedBy;
                entryClassifier.addAll(section.entryClassifier);
                entryReference.addAll(section.entryReference);
                entryQuantity.addAll(section.entryQuantity);
                emptyReason = section.emptyReason;
                this.section.addAll(section.section);
                return this;
            }
        }
    }
}

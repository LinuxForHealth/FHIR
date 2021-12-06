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
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.DocumentReferenceStatus;
import com.ibm.fhir.model.type.code.DocumentRelationshipType;
import com.ibm.fhir.model.type.code.ReferredDocumentStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A reference to a document of any kind for any purpose. Provides metadata about the document so that the document can 
 * be discovered and managed. The scope of a document is any seralized object with a mime-type, so includes formal 
 * patient centric documents (CDA), cliical notes, scanned paper, and non-patient specific documents like policy text.
 * 
 * <p>Maturity level: FMM3 (Trial Use)
 */
@Maturity(
    level = 3,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "documentReference-0",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/doc-typecodes",
    expression = "type.exists() implies (type.memberOf('http://hl7.org/fhir/ValueSet/doc-typecodes', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/DocumentReference",
    generated = true
)
@Constraint(
    id = "documentReference-1",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/security-labels",
    expression = "securityLabel.exists() implies (securityLabel.all(memberOf('http://hl7.org/fhir/ValueSet/security-labels', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/DocumentReference",
    generated = true
)
@Constraint(
    id = "documentReference-2",
    level = "Warning",
    location = "content.format",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/formatcodes",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/formatcodes', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/DocumentReference",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DocumentReference extends DomainResource {
    @Summary
    private final Identifier masterIdentifier;
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "DocumentReferenceStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The status of the document reference.",
        valueSet = "http://hl7.org/fhir/ValueSet/document-reference-status|4.1.0"
    )
    @Required
    private final DocumentReferenceStatus status;
    @Summary
    @Binding(
        bindingName = "ReferredDocumentStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Status of the underlying document.",
        valueSet = "http://hl7.org/fhir/ValueSet/composition-status|4.1.0"
    )
    private final ReferredDocumentStatus docStatus;
    @Summary
    @Binding(
        bindingName = "DocumentC80Type",
        strength = BindingStrength.Value.PREFERRED,
        description = "Precise type of clinical document.",
        valueSet = "http://hl7.org/fhir/ValueSet/doc-typecodes"
    )
    private final CodeableConcept type;
    @Summary
    @Binding(
        bindingName = "DocumentC80Class",
        strength = BindingStrength.Value.EXAMPLE,
        description = "High-level kind of a clinical document at a macro level.",
        valueSet = "http://hl7.org/fhir/ValueSet/doc-classcodes"
    )
    private final List<CodeableConcept> category;
    @Summary
    @ReferenceTarget({ "Patient", "Practitioner", "Group", "Device" })
    private final Reference subject;
    @Summary
    private final Instant date;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization", "Device", "Patient", "RelatedPerson" })
    private final List<Reference> author;
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
    private final Reference authenticator;
    @ReferenceTarget({ "Organization" })
    private final Reference custodian;
    @Summary
    private final List<RelatesTo> relatesTo;
    @Summary
    private final String description;
    @Summary
    @Binding(
        bindingName = "SecurityLabels",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Security Labels from the Healthcare Privacy and Security Classification System.",
        valueSet = "http://hl7.org/fhir/ValueSet/security-labels"
    )
    private final List<CodeableConcept> securityLabel;
    @Summary
    @Required
    private final List<Content> content;
    @Summary
    private final Context context;

    private DocumentReference(Builder builder) {
        super(builder);
        masterIdentifier = builder.masterIdentifier;
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        docStatus = builder.docStatus;
        type = builder.type;
        category = Collections.unmodifiableList(builder.category);
        subject = builder.subject;
        date = builder.date;
        author = Collections.unmodifiableList(builder.author);
        authenticator = builder.authenticator;
        custodian = builder.custodian;
        relatesTo = Collections.unmodifiableList(builder.relatesTo);
        description = builder.description;
        securityLabel = Collections.unmodifiableList(builder.securityLabel);
        content = Collections.unmodifiableList(builder.content);
        context = builder.context;
    }

    /**
     * Document identifier as assigned by the source of the document. This identifier is specific to this version of the 
     * document. This unique identifier may be used elsewhere to identify this version of the document.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getMasterIdentifier() {
        return masterIdentifier;
    }

    /**
     * Other identifiers associated with the document, including version independent identifiers.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The status of this document reference.
     * 
     * @return
     *     An immutable object of type {@link DocumentReferenceStatus} that is non-null.
     */
    public DocumentReferenceStatus getStatus() {
        return status;
    }

    /**
     * The status of the underlying document.
     * 
     * @return
     *     An immutable object of type {@link ReferredDocumentStatus} that may be null.
     */
    public ReferredDocumentStatus getDocStatus() {
        return docStatus;
    }

    /**
     * Specifies the particular kind of document referenced (e.g. History and Physical, Discharge Summary, Progress Note). 
     * This usually equates to the purpose of making the document referenced.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * A categorization for the type of document referenced - helps for indexing and searching. This may be implied by or 
     * derived from the code specified in the DocumentReference.type.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device 
     * (e.g. a machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients 
     * that share a common exposure).
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * When the document reference was created.
     * 
     * @return
     *     An immutable object of type {@link Instant} that may be null.
     */
    public Instant getDate() {
        return date;
    }

    /**
     * Identifies who is responsible for adding the information to the document.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getAuthor() {
        return author;
    }

    /**
     * Which person or organization authenticates that this document is valid.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getAuthenticator() {
        return authenticator;
    }

    /**
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the document.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getCustodian() {
        return custodian;
    }

    /**
     * Relationships that this document has with other document references that already exist.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RelatesTo} that may be empty.
     */
    public List<RelatesTo> getRelatesTo() {
        return relatesTo;
    }

    /**
     * Human-readable description of the source document.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDescription() {
        return description;
    }

    /**
     * A set of Security-Tag codes specifying the level of privacy/security of the Document. Note that DocumentReference.meta.
     * security contains the security labels of the "reference" to the document, while DocumentReference.securityLabel 
     * contains a snapshot of the security labels on the document the reference refers to.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getSecurityLabel() {
        return securityLabel;
    }

    /**
     * The document and format referenced. There may be multiple content element repetitions, each with a different format.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Content} that is non-empty.
     */
    public List<Content> getContent() {
        return content;
    }

    /**
     * The clinical context in which the document was prepared.
     * 
     * @return
     *     An immutable object of type {@link Context} that may be null.
     */
    public Context getContext() {
        return context;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (masterIdentifier != null) || 
            !identifier.isEmpty() || 
            (status != null) || 
            (docStatus != null) || 
            (type != null) || 
            !category.isEmpty() || 
            (subject != null) || 
            (date != null) || 
            !author.isEmpty() || 
            (authenticator != null) || 
            (custodian != null) || 
            !relatesTo.isEmpty() || 
            (description != null) || 
            !securityLabel.isEmpty() || 
            !content.isEmpty() || 
            (context != null);
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
                accept(masterIdentifier, "masterIdentifier", visitor);
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(status, "status", visitor);
                accept(docStatus, "docStatus", visitor);
                accept(type, "type", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(subject, "subject", visitor);
                accept(date, "date", visitor);
                accept(author, "author", visitor, Reference.class);
                accept(authenticator, "authenticator", visitor);
                accept(custodian, "custodian", visitor);
                accept(relatesTo, "relatesTo", visitor, RelatesTo.class);
                accept(description, "description", visitor);
                accept(securityLabel, "securityLabel", visitor, CodeableConcept.class);
                accept(content, "content", visitor, Content.class);
                accept(context, "context", visitor);
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
        DocumentReference other = (DocumentReference) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(masterIdentifier, other.masterIdentifier) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(status, other.status) && 
            Objects.equals(docStatus, other.docStatus) && 
            Objects.equals(type, other.type) && 
            Objects.equals(category, other.category) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(date, other.date) && 
            Objects.equals(author, other.author) && 
            Objects.equals(authenticator, other.authenticator) && 
            Objects.equals(custodian, other.custodian) && 
            Objects.equals(relatesTo, other.relatesTo) && 
            Objects.equals(description, other.description) && 
            Objects.equals(securityLabel, other.securityLabel) && 
            Objects.equals(content, other.content) && 
            Objects.equals(context, other.context);
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
                masterIdentifier, 
                identifier, 
                status, 
                docStatus, 
                type, 
                category, 
                subject, 
                date, 
                author, 
                authenticator, 
                custodian, 
                relatesTo, 
                description, 
                securityLabel, 
                content, 
                context);
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
        private Identifier masterIdentifier;
        private List<Identifier> identifier = new ArrayList<>();
        private DocumentReferenceStatus status;
        private ReferredDocumentStatus docStatus;
        private CodeableConcept type;
        private List<CodeableConcept> category = new ArrayList<>();
        private Reference subject;
        private Instant date;
        private List<Reference> author = new ArrayList<>();
        private Reference authenticator;
        private Reference custodian;
        private List<RelatesTo> relatesTo = new ArrayList<>();
        private String description;
        private List<CodeableConcept> securityLabel = new ArrayList<>();
        private List<Content> content = new ArrayList<>();
        private Context context;

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
         * Document identifier as assigned by the source of the document. This identifier is specific to this version of the 
         * document. This unique identifier may be used elsewhere to identify this version of the document.
         * 
         * @param masterIdentifier
         *     Master Version Specific Identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder masterIdentifier(Identifier masterIdentifier) {
            this.masterIdentifier = masterIdentifier;
            return this;
        }

        /**
         * Other identifiers associated with the document, including version independent identifiers.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Other identifiers for the document
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
         * Other identifiers associated with the document, including version independent identifiers.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Other identifiers for the document
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
         * The status of this document reference.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     current | superseded | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(DocumentReferenceStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The status of the underlying document.
         * 
         * @param docStatus
         *     preliminary | final | amended | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder docStatus(ReferredDocumentStatus docStatus) {
            this.docStatus = docStatus;
            return this;
        }

        /**
         * Specifies the particular kind of document referenced (e.g. History and Physical, Discharge Summary, Progress Note). 
         * This usually equates to the purpose of making the document referenced.
         * 
         * @param type
         *     Kind of document (LOINC if possible)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * A categorization for the type of document referenced - helps for indexing and searching. This may be implied by or 
         * derived from the code specified in the DocumentReference.type.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Categorization of document
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * A categorization for the type of document referenced - helps for indexing and searching. This may be implied by or 
         * derived from the code specified in the DocumentReference.type.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Categorization of document
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device 
         * (e.g. a machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients 
         * that share a common exposure).
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link Group}</li>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param subject
         *     Who/what is the subject of the document
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Convenience method for setting {@code date}.
         * 
         * @param date
         *     When this document reference was created
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #date(com.ibm.fhir.model.type.Instant)
         */
        public Builder date(java.time.ZonedDateTime date) {
            this.date = (date == null) ? null : Instant.of(date);
            return this;
        }

        /**
         * When the document reference was created.
         * 
         * @param date
         *     When this document reference was created
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder date(Instant date) {
            this.date = date;
            return this;
        }

        /**
         * Identifies who is responsible for adding the information to the document.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * <li>{@link Device}</li>
         * <li>{@link Patient}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
         * 
         * @param author
         *     Who and/or what authored the document
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
         * Identifies who is responsible for adding the information to the document.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * <li>{@link Device}</li>
         * <li>{@link Patient}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
         * 
         * @param author
         *     Who and/or what authored the document
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
         * Which person or organization authenticates that this document is valid.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param authenticator
         *     Who/what authenticated the document
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder authenticator(Reference authenticator) {
            this.authenticator = authenticator;
            return this;
        }

        /**
         * Identifies the organization or group who is responsible for ongoing maintenance of and access to the document.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param custodian
         *     Organization which maintains the document
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder custodian(Reference custodian) {
            this.custodian = custodian;
            return this;
        }

        /**
         * Relationships that this document has with other document references that already exist.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatesTo
         *     Relationships to other documents
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
         * Relationships that this document has with other document references that already exist.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatesTo
         *     Relationships to other documents
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
         * Convenience method for setting {@code description}.
         * 
         * @param description
         *     Human-readable description
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #description(com.ibm.fhir.model.type.String)
         */
        public Builder description(java.lang.String description) {
            this.description = (description == null) ? null : String.of(description);
            return this;
        }

        /**
         * Human-readable description of the source document.
         * 
         * @param description
         *     Human-readable description
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * A set of Security-Tag codes specifying the level of privacy/security of the Document. Note that DocumentReference.meta.
         * security contains the security labels of the "reference" to the document, while DocumentReference.securityLabel 
         * contains a snapshot of the security labels on the document the reference refers to.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param securityLabel
         *     Document security-tags
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder securityLabel(CodeableConcept... securityLabel) {
            for (CodeableConcept value : securityLabel) {
                this.securityLabel.add(value);
            }
            return this;
        }

        /**
         * A set of Security-Tag codes specifying the level of privacy/security of the Document. Note that DocumentReference.meta.
         * security contains the security labels of the "reference" to the document, while DocumentReference.securityLabel 
         * contains a snapshot of the security labels on the document the reference refers to.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param securityLabel
         *     Document security-tags
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder securityLabel(Collection<CodeableConcept> securityLabel) {
            this.securityLabel = new ArrayList<>(securityLabel);
            return this;
        }

        /**
         * The document and format referenced. There may be multiple content element repetitions, each with a different format.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param content
         *     Document referenced
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder content(Content... content) {
            for (Content value : content) {
                this.content.add(value);
            }
            return this;
        }

        /**
         * The document and format referenced. There may be multiple content element repetitions, each with a different format.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param content
         *     Document referenced
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder content(Collection<Content> content) {
            this.content = new ArrayList<>(content);
            return this;
        }

        /**
         * The clinical context in which the document was prepared.
         * 
         * @param context
         *     Clinical context of document
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        /**
         * Build the {@link DocumentReference}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>content</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link DocumentReference}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid DocumentReference per the base specification
         */
        @Override
        public DocumentReference build() {
            DocumentReference documentReference = new DocumentReference(this);
            if (validating) {
                validate(documentReference);
            }
            return documentReference;
        }

        protected void validate(DocumentReference documentReference) {
            super.validate(documentReference);
            ValidationSupport.checkList(documentReference.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(documentReference.status, "status");
            ValidationSupport.checkList(documentReference.category, "category", CodeableConcept.class);
            ValidationSupport.checkList(documentReference.author, "author", Reference.class);
            ValidationSupport.checkList(documentReference.relatesTo, "relatesTo", RelatesTo.class);
            ValidationSupport.checkList(documentReference.securityLabel, "securityLabel", CodeableConcept.class);
            ValidationSupport.checkNonEmptyList(documentReference.content, "content", Content.class);
            ValidationSupport.checkReferenceType(documentReference.subject, "subject", "Patient", "Practitioner", "Group", "Device");
            ValidationSupport.checkReferenceType(documentReference.author, "author", "Practitioner", "PractitionerRole", "Organization", "Device", "Patient", "RelatedPerson");
            ValidationSupport.checkReferenceType(documentReference.authenticator, "authenticator", "Practitioner", "PractitionerRole", "Organization");
            ValidationSupport.checkReferenceType(documentReference.custodian, "custodian", "Organization");
        }

        protected Builder from(DocumentReference documentReference) {
            super.from(documentReference);
            masterIdentifier = documentReference.masterIdentifier;
            identifier.addAll(documentReference.identifier);
            status = documentReference.status;
            docStatus = documentReference.docStatus;
            type = documentReference.type;
            category.addAll(documentReference.category);
            subject = documentReference.subject;
            date = documentReference.date;
            author.addAll(documentReference.author);
            authenticator = documentReference.authenticator;
            custodian = documentReference.custodian;
            relatesTo.addAll(documentReference.relatesTo);
            description = documentReference.description;
            securityLabel.addAll(documentReference.securityLabel);
            content.addAll(documentReference.content);
            context = documentReference.context;
            return this;
        }
    }

    /**
     * Relationships that this document has with other document references that already exist.
     */
    public static class RelatesTo extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "DocumentRelationshipType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The type of relationship between documents.",
            valueSet = "http://hl7.org/fhir/ValueSet/document-relationship-type|4.1.0"
        )
        @Required
        private final DocumentRelationshipType code;
        @Summary
        @ReferenceTarget({ "DocumentReference" })
        @Required
        private final Reference target;

        private RelatesTo(Builder builder) {
            super(builder);
            code = builder.code;
            target = builder.target;
        }

        /**
         * The type of relationship that this document has with anther document.
         * 
         * @return
         *     An immutable object of type {@link DocumentRelationshipType} that is non-null.
         */
        public DocumentRelationshipType getCode() {
            return code;
        }

        /**
         * The target document of this relationship.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getTarget() {
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
            private DocumentRelationshipType code;
            private Reference target;

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
             * The type of relationship that this document has with anther document.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     replaces | transforms | signs | appends
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(DocumentRelationshipType code) {
                this.code = code;
                return this;
            }

            /**
             * The target document of this relationship.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param target
             *     Target of the relationship
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder target(Reference target) {
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
                ValidationSupport.requireNonNull(relatesTo.target, "target");
                ValidationSupport.checkReferenceType(relatesTo.target, "target", "DocumentReference");
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
     * The document and format referenced. There may be multiple content element repetitions, each with a different format.
     */
    public static class Content extends BackboneElement {
        @Summary
        @Required
        private final Attachment attachment;
        @Summary
        @Binding(
            bindingName = "DocumentFormat",
            strength = BindingStrength.Value.PREFERRED,
            description = "Document Format Codes.",
            valueSet = "http://hl7.org/fhir/ValueSet/formatcodes"
        )
        private final Coding format;

        private Content(Builder builder) {
            super(builder);
            attachment = builder.attachment;
            format = builder.format;
        }

        /**
         * The document or URL of the document along with critical metadata to prove content has integrity.
         * 
         * @return
         *     An immutable object of type {@link Attachment} that is non-null.
         */
        public Attachment getAttachment() {
            return attachment;
        }

        /**
         * An identifier of the document encoding, structure, and template that the document conforms to beyond the base format 
         * indicated in the mimeType.
         * 
         * @return
         *     An immutable object of type {@link Coding} that may be null.
         */
        public Coding getFormat() {
            return format;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (attachment != null) || 
                (format != null);
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
                    accept(attachment, "attachment", visitor);
                    accept(format, "format", visitor);
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
            Content other = (Content) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(attachment, other.attachment) && 
                Objects.equals(format, other.format);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    attachment, 
                    format);
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
            private Attachment attachment;
            private Coding format;

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
             * The document or URL of the document along with critical metadata to prove content has integrity.
             * 
             * <p>This element is required.
             * 
             * @param attachment
             *     Where to access the document
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder attachment(Attachment attachment) {
                this.attachment = attachment;
                return this;
            }

            /**
             * An identifier of the document encoding, structure, and template that the document conforms to beyond the base format 
             * indicated in the mimeType.
             * 
             * @param format
             *     Format/content rules for the document
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder format(Coding format) {
                this.format = format;
                return this;
            }

            /**
             * Build the {@link Content}
             * 
             * <p>Required elements:
             * <ul>
             * <li>attachment</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Content}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Content per the base specification
             */
            @Override
            public Content build() {
                Content content = new Content(this);
                if (validating) {
                    validate(content);
                }
                return content;
            }

            protected void validate(Content content) {
                super.validate(content);
                ValidationSupport.requireNonNull(content.attachment, "attachment");
                ValidationSupport.requireValueOrChildren(content);
            }

            protected Builder from(Content content) {
                super.from(content);
                attachment = content.attachment;
                format = content.format;
                return this;
            }
        }
    }

    /**
     * The clinical context in which the document was prepared.
     */
    public static class Context extends BackboneElement {
        @ReferenceTarget({ "Encounter", "EpisodeOfCare" })
        private final List<Reference> encounter;
        @Binding(
            bindingName = "DocumentEventType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "This list of codes represents the main clinical acts being documented.",
            valueSet = "http://terminology.hl7.org/ValueSet/v3-ActCode"
        )
        private final List<CodeableConcept> event;
        @Summary
        private final Period period;
        @Binding(
            bindingName = "DocumentC80FacilityType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "XDS Facility Type.",
            valueSet = "http://hl7.org/fhir/ValueSet/c80-facilitycodes"
        )
        private final CodeableConcept facilityType;
        @Binding(
            bindingName = "DocumentC80PracticeSetting",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Additional details about where the content was created (e.g. clinical specialty).",
            valueSet = "http://hl7.org/fhir/ValueSet/c80-practice-codes"
        )
        private final CodeableConcept practiceSetting;
        @ReferenceTarget({ "Patient" })
        private final Reference sourcePatientInfo;
        private final List<Reference> related;

        private Context(Builder builder) {
            super(builder);
            encounter = Collections.unmodifiableList(builder.encounter);
            event = Collections.unmodifiableList(builder.event);
            period = builder.period;
            facilityType = builder.facilityType;
            practiceSetting = builder.practiceSetting;
            sourcePatientInfo = builder.sourcePatientInfo;
            related = Collections.unmodifiableList(builder.related);
        }

        /**
         * Describes the clinical encounter or type of care that the document content is associated with.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getEncounter() {
            return encounter;
        }

        /**
         * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In 
         * some cases, the event is inherent in the type Code, such as a "History and Physical Report" in which the procedure 
         * being documented is necessarily a "History and Physical" act.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getEvent() {
            return event;
        }

        /**
         * The time period over which the service that is described by the document was provided.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getPeriod() {
            return period;
        }

        /**
         * The kind of facility where the patient was seen.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getFacilityType() {
            return facilityType;
        }

        /**
         * This property may convey specifics about the practice setting where the content was created, often reflecting the 
         * clinical specialty.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getPracticeSetting() {
            return practiceSetting;
        }

        /**
         * The Patient Information as known when the document was published. May be a reference to a version specific, or 
         * contained.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getSourcePatientInfo() {
            return sourcePatientInfo;
        }

        /**
         * Related identifiers or resources associated with the DocumentReference.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getRelated() {
            return related;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !encounter.isEmpty() || 
                !event.isEmpty() || 
                (period != null) || 
                (facilityType != null) || 
                (practiceSetting != null) || 
                (sourcePatientInfo != null) || 
                !related.isEmpty();
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
                    accept(encounter, "encounter", visitor, Reference.class);
                    accept(event, "event", visitor, CodeableConcept.class);
                    accept(period, "period", visitor);
                    accept(facilityType, "facilityType", visitor);
                    accept(practiceSetting, "practiceSetting", visitor);
                    accept(sourcePatientInfo, "sourcePatientInfo", visitor);
                    accept(related, "related", visitor, Reference.class);
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
            Context other = (Context) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(encounter, other.encounter) && 
                Objects.equals(event, other.event) && 
                Objects.equals(period, other.period) && 
                Objects.equals(facilityType, other.facilityType) && 
                Objects.equals(practiceSetting, other.practiceSetting) && 
                Objects.equals(sourcePatientInfo, other.sourcePatientInfo) && 
                Objects.equals(related, other.related);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    encounter, 
                    event, 
                    period, 
                    facilityType, 
                    practiceSetting, 
                    sourcePatientInfo, 
                    related);
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
            private List<Reference> encounter = new ArrayList<>();
            private List<CodeableConcept> event = new ArrayList<>();
            private Period period;
            private CodeableConcept facilityType;
            private CodeableConcept practiceSetting;
            private Reference sourcePatientInfo;
            private List<Reference> related = new ArrayList<>();

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
             * Describes the clinical encounter or type of care that the document content is associated with.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Encounter}</li>
             * <li>{@link EpisodeOfCare}</li>
             * </ul>
             * 
             * @param encounter
             *     Context of the document content
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder encounter(Reference... encounter) {
                for (Reference value : encounter) {
                    this.encounter.add(value);
                }
                return this;
            }

            /**
             * Describes the clinical encounter or type of care that the document content is associated with.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Encounter}</li>
             * <li>{@link EpisodeOfCare}</li>
             * </ul>
             * 
             * @param encounter
             *     Context of the document content
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder encounter(Collection<Reference> encounter) {
                this.encounter = new ArrayList<>(encounter);
                return this;
            }

            /**
             * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In 
             * some cases, the event is inherent in the type Code, such as a "History and Physical Report" in which the procedure 
             * being documented is necessarily a "History and Physical" act.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param event
             *     Main clinical acts documented
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder event(CodeableConcept... event) {
                for (CodeableConcept value : event) {
                    this.event.add(value);
                }
                return this;
            }

            /**
             * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In 
             * some cases, the event is inherent in the type Code, such as a "History and Physical Report" in which the procedure 
             * being documented is necessarily a "History and Physical" act.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param event
             *     Main clinical acts documented
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder event(Collection<CodeableConcept> event) {
                this.event = new ArrayList<>(event);
                return this;
            }

            /**
             * The time period over which the service that is described by the document was provided.
             * 
             * @param period
             *     Time of service that is being documented
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * The kind of facility where the patient was seen.
             * 
             * @param facilityType
             *     Kind of facility where patient was seen
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder facilityType(CodeableConcept facilityType) {
                this.facilityType = facilityType;
                return this;
            }

            /**
             * This property may convey specifics about the practice setting where the content was created, often reflecting the 
             * clinical specialty.
             * 
             * @param practiceSetting
             *     Additional details about where the content was created (e.g. clinical specialty)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder practiceSetting(CodeableConcept practiceSetting) {
                this.practiceSetting = practiceSetting;
                return this;
            }

            /**
             * The Patient Information as known when the document was published. May be a reference to a version specific, or 
             * contained.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Patient}</li>
             * </ul>
             * 
             * @param sourcePatientInfo
             *     Patient demographics from source
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sourcePatientInfo(Reference sourcePatientInfo) {
                this.sourcePatientInfo = sourcePatientInfo;
                return this;
            }

            /**
             * Related identifiers or resources associated with the DocumentReference.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param related
             *     Related identifiers or resources
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder related(Reference... related) {
                for (Reference value : related) {
                    this.related.add(value);
                }
                return this;
            }

            /**
             * Related identifiers or resources associated with the DocumentReference.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param related
             *     Related identifiers or resources
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder related(Collection<Reference> related) {
                this.related = new ArrayList<>(related);
                return this;
            }

            /**
             * Build the {@link Context}
             * 
             * @return
             *     An immutable object of type {@link Context}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Context per the base specification
             */
            @Override
            public Context build() {
                Context context = new Context(this);
                if (validating) {
                    validate(context);
                }
                return context;
            }

            protected void validate(Context context) {
                super.validate(context);
                ValidationSupport.checkList(context.encounter, "encounter", Reference.class);
                ValidationSupport.checkList(context.event, "event", CodeableConcept.class);
                ValidationSupport.checkList(context.related, "related", Reference.class);
                ValidationSupport.checkReferenceType(context.encounter, "encounter", "Encounter", "EpisodeOfCare");
                ValidationSupport.checkReferenceType(context.sourcePatientInfo, "sourcePatientInfo", "Patient");
                ValidationSupport.requireValueOrChildren(context);
            }

            protected Builder from(Context context) {
                super.from(context);
                encounter.addAll(context.encounter);
                event.addAll(context.event);
                period = context.period;
                facilityType = context.facilityType;
                practiceSetting = context.practiceSetting;
                sourcePatientInfo = context.sourcePatientInfo;
                related.addAll(context.related);
                return this;
            }
        }
    }
}

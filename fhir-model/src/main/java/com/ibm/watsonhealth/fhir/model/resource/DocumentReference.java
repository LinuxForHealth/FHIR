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

import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.DocumentReferenceStatus;
import com.ibm.watsonhealth.fhir.model.type.DocumentRelationshipType;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.ReferredDocumentStatus;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A reference to a document of any kind for any purpose. Provides metadata about the document so that the document can 
 * be discovered and managed. The scope of a document is any seralized object with a mime-type, so includes formal 
 * patient centric documents (CDA), cliical notes, scanned paper, and non-patient specific documents like policy text.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class DocumentReference extends DomainResource {
    private final Identifier masterIdentifier;
    private final List<Identifier> identifier;
    private final DocumentReferenceStatus status;
    private final ReferredDocumentStatus docStatus;
    private final CodeableConcept type;
    private final List<CodeableConcept> category;
    private final Reference subject;
    private final Instant date;
    private final List<Reference> author;
    private final Reference authenticator;
    private final Reference custodian;
    private final List<RelatesTo> relatesTo;
    private final String description;
    private final List<CodeableConcept> securityLabel;
    private final List<Content> content;
    private final Context context;

    private volatile int hashCode;

    private DocumentReference(Builder builder) {
        super(builder);
        masterIdentifier = builder.masterIdentifier;
        identifier = Collections.unmodifiableList(builder.identifier);
        status = ValidationSupport.requireNonNull(builder.status, "status");
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
        content = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.content, "content"));
        context = builder.context;
    }

    /**
     * <p>
     * Document identifier as assigned by the source of the document. This identifier is specific to this version of the 
     * document. This unique identifier may be used elsewhere to identify this version of the document.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Identifier}.
     */
    public Identifier getMasterIdentifier() {
        return masterIdentifier;
    }

    /**
     * <p>
     * Other identifiers associated with the document, including version independent identifiers.
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
     * The status of this document reference.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DocumentReferenceStatus}.
     */
    public DocumentReferenceStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The status of the underlying document.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ReferredDocumentStatus}.
     */
    public ReferredDocumentStatus getDocStatus() {
        return docStatus;
    }

    /**
     * <p>
     * Specifies the particular kind of document referenced (e.g. History and Physical, Discharge Summary, Progress Note). 
     * This usually equates to the purpose of making the document referenced.
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
     * A categorization for the type of document referenced - helps for indexing and searching. This may be implied by or 
     * derived from the code specified in the DocumentReference.type.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * <p>
     * Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device 
     * (e.g. a machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients 
     * that share a common exposure).
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
     * When the document reference was created.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Instant}.
     */
    public Instant getDate() {
        return date;
    }

    /**
     * <p>
     * Identifies who is responsible for adding the information to the document.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getAuthor() {
        return author;
    }

    /**
     * <p>
     * Which person or organization authenticates that this document is valid.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getAuthenticator() {
        return authenticator;
    }

    /**
     * <p>
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the document.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getCustodian() {
        return custodian;
    }

    /**
     * <p>
     * Relationships that this document has with other document references that already exist.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RelatesTo}.
     */
    public List<RelatesTo> getRelatesTo() {
        return relatesTo;
    }

    /**
     * <p>
     * Human-readable description of the source document.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>
     * A set of Security-Tag codes specifying the level of privacy/security of the Document. Note that DocumentReference.meta.
     * security contains the security labels of the "reference" to the document, while DocumentReference.securityLabel 
     * contains a snapshot of the security labels on the document the reference refers to.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getSecurityLabel() {
        return securityLabel;
    }

    /**
     * <p>
     * The document and format referenced. There may be multiple content element repetitions, each with a different format.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Content}.
     */
    public List<Content> getContent() {
        return content;
    }

    /**
     * <p>
     * The clinical context in which the document was prepared.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Context}.
     */
    public Context getContext() {
        return context;
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
            visitor.visitEnd(elementName, this);
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
        return new Builder(status, content).from(this);
    }

    public Builder toBuilder(DocumentReferenceStatus status, List<Content> content) {
        return new Builder(status, content).from(this);
    }

    public static Builder builder(DocumentReferenceStatus status, List<Content> content) {
        return new Builder(status, content);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final DocumentReferenceStatus status;
        private final List<Content> content;

        // optional
        private Identifier masterIdentifier;
        private List<Identifier> identifier = new ArrayList<>();
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
        private Context context;

        private Builder(DocumentReferenceStatus status, List<Content> content) {
            super();
            this.status = status;
            this.content = content;
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Document identifier as assigned by the source of the document. This identifier is specific to this version of the 
         * document. This unique identifier may be used elsewhere to identify this version of the document.
         * </p>
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
         * <p>
         * Other identifiers associated with the document, including version independent identifiers.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Other identifiers associated with the document, including version independent identifiers.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Other identifiers for the document
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
         * The status of the underlying document.
         * </p>
         * 
         * @param docStatus
         *     preliminary | final | appended | amended | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder docStatus(ReferredDocumentStatus docStatus) {
            this.docStatus = docStatus;
            return this;
        }

        /**
         * <p>
         * Specifies the particular kind of document referenced (e.g. History and Physical, Discharge Summary, Progress Note). 
         * This usually equates to the purpose of making the document referenced.
         * </p>
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
         * <p>
         * A categorization for the type of document referenced - helps for indexing and searching. This may be implied by or 
         * derived from the code specified in the DocumentReference.type.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A categorization for the type of document referenced - helps for indexing and searching. This may be implied by or 
         * derived from the code specified in the DocumentReference.type.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param category
         *     Categorization of document
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * <p>
         * Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device 
         * (e.g. a machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients 
         * that share a common exposure).
         * </p>
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
         * <p>
         * When the document reference was created.
         * </p>
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
         * <p>
         * Identifies who is responsible for adding the information to the document.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Identifies who is responsible for adding the information to the document.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param author
         *     Who and/or what authored the document
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(Collection<Reference> author) {
            this.author = new ArrayList<>(author);
            return this;
        }

        /**
         * <p>
         * Which person or organization authenticates that this document is valid.
         * </p>
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
         * <p>
         * Identifies the organization or group who is responsible for ongoing maintenance of and access to the document.
         * </p>
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
         * <p>
         * Relationships that this document has with other document references that already exist.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Relationships that this document has with other document references that already exist.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param relatesTo
         *     Relationships to other documents
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relatesTo(Collection<RelatesTo> relatesTo) {
            this.relatesTo = new ArrayList<>(relatesTo);
            return this;
        }

        /**
         * <p>
         * Human-readable description of the source document.
         * </p>
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
         * <p>
         * A set of Security-Tag codes specifying the level of privacy/security of the Document. Note that DocumentReference.meta.
         * security contains the security labels of the "reference" to the document, while DocumentReference.securityLabel 
         * contains a snapshot of the security labels on the document the reference refers to.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A set of Security-Tag codes specifying the level of privacy/security of the Document. Note that DocumentReference.meta.
         * security contains the security labels of the "reference" to the document, while DocumentReference.securityLabel 
         * contains a snapshot of the security labels on the document the reference refers to.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param securityLabel
         *     Document security-tags
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder securityLabel(Collection<CodeableConcept> securityLabel) {
            this.securityLabel = new ArrayList<>(securityLabel);
            return this;
        }

        /**
         * <p>
         * The clinical context in which the document was prepared.
         * </p>
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

        @Override
        public DocumentReference build() {
            return new DocumentReference(this);
        }

        private Builder from(DocumentReference documentReference) {
            id = documentReference.id;
            meta = documentReference.meta;
            implicitRules = documentReference.implicitRules;
            language = documentReference.language;
            text = documentReference.text;
            contained.addAll(documentReference.contained);
            extension.addAll(documentReference.extension);
            modifierExtension.addAll(documentReference.modifierExtension);
            masterIdentifier = documentReference.masterIdentifier;
            identifier.addAll(documentReference.identifier);
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
            context = documentReference.context;
            return this;
        }
    }

    /**
     * <p>
     * Relationships that this document has with other document references that already exist.
     * </p>
     */
    public static class RelatesTo extends BackboneElement {
        private final DocumentRelationshipType code;
        private final Reference target;

        private volatile int hashCode;

        private RelatesTo(Builder builder) {
            super(builder);
            code = ValidationSupport.requireNonNull(builder.code, "code");
            target = ValidationSupport.requireNonNull(builder.target, "target");
        }

        /**
         * <p>
         * The type of relationship that this document has with anther document.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DocumentRelationshipType}.
         */
        public DocumentRelationshipType getCode() {
            return code;
        }

        /**
         * <p>
         * The target document of this relationship.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getTarget() {
            return target;
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
                    accept(code, "code", visitor);
                    accept(target, "target", visitor);
                }
                visitor.visitEnd(elementName, this);
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
            return new Builder(code, target).from(this);
        }

        public Builder toBuilder(DocumentRelationshipType code, Reference target) {
            return new Builder(code, target).from(this);
        }

        public static Builder builder(DocumentRelationshipType code, Reference target) {
            return new Builder(code, target);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final DocumentRelationshipType code;
            private final Reference target;

            private Builder(DocumentRelationshipType code, Reference target) {
                super();
                this.code = code;
                this.target = target;
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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

            @Override
            public RelatesTo build() {
                return new RelatesTo(this);
            }

            private Builder from(RelatesTo relatesTo) {
                id = relatesTo.id;
                extension.addAll(relatesTo.extension);
                modifierExtension.addAll(relatesTo.modifierExtension);
                return this;
            }
        }
    }

    /**
     * <p>
     * The document and format referenced. There may be multiple content element repetitions, each with a different format.
     * </p>
     */
    public static class Content extends BackboneElement {
        private final Attachment attachment;
        private final Coding format;

        private volatile int hashCode;

        private Content(Builder builder) {
            super(builder);
            attachment = ValidationSupport.requireNonNull(builder.attachment, "attachment");
            format = builder.format;
        }

        /**
         * <p>
         * The document or URL of the document along with critical metadata to prove content has integrity.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Attachment}.
         */
        public Attachment getAttachment() {
            return attachment;
        }

        /**
         * <p>
         * An identifier of the document encoding, structure, and template that the document conforms to beyond the base format 
         * indicated in the mimeType.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Coding}.
         */
        public Coding getFormat() {
            return format;
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
                    accept(attachment, "attachment", visitor);
                    accept(format, "format", visitor);
                }
                visitor.visitEnd(elementName, this);
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
            return new Builder(attachment).from(this);
        }

        public Builder toBuilder(Attachment attachment) {
            return new Builder(attachment).from(this);
        }

        public static Builder builder(Attachment attachment) {
            return new Builder(attachment);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Attachment attachment;

            // optional
            private Coding format;

            private Builder(Attachment attachment) {
                super();
                this.attachment = attachment;
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * An identifier of the document encoding, structure, and template that the document conforms to beyond the base format 
             * indicated in the mimeType.
             * </p>
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

            @Override
            public Content build() {
                return new Content(this);
            }

            private Builder from(Content content) {
                id = content.id;
                extension.addAll(content.extension);
                modifierExtension.addAll(content.modifierExtension);
                format = content.format;
                return this;
            }
        }
    }

    /**
     * <p>
     * The clinical context in which the document was prepared.
     * </p>
     */
    public static class Context extends BackboneElement {
        private final List<Reference> encounter;
        private final List<CodeableConcept> event;
        private final Period period;
        private final CodeableConcept facilityType;
        private final CodeableConcept practiceSetting;
        private final Reference sourcePatientInfo;
        private final List<Reference> related;

        private volatile int hashCode;

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
         * <p>
         * Describes the clinical encounter or type of care that the document content is associated with.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getEncounter() {
            return encounter;
        }

        /**
         * <p>
         * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In 
         * some cases, the event is inherent in the type Code, such as a "History and Physical Report" in which the procedure 
         * being documented is necessarily a "History and Physical" act.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getEvent() {
            return event;
        }

        /**
         * <p>
         * The time period over which the service that is described by the document was provided.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getPeriod() {
            return period;
        }

        /**
         * <p>
         * The kind of facility where the patient was seen.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getFacilityType() {
            return facilityType;
        }

        /**
         * <p>
         * This property may convey specifics about the practice setting where the content was created, often reflecting the 
         * clinical specialty.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getPracticeSetting() {
            return practiceSetting;
        }

        /**
         * <p>
         * The Patient Information as known when the document was published. May be a reference to a version specific, or 
         * contained.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getSourcePatientInfo() {
            return sourcePatientInfo;
        }

        /**
         * <p>
         * Related identifiers or resources associated with the DocumentReference.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getRelated() {
            return related;
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
                    accept(encounter, "encounter", visitor, Reference.class);
                    accept(event, "event", visitor, CodeableConcept.class);
                    accept(period, "period", visitor);
                    accept(facilityType, "facilityType", visitor);
                    accept(practiceSetting, "practiceSetting", visitor);
                    accept(sourcePatientInfo, "sourcePatientInfo", visitor);
                    accept(related, "related", visitor, Reference.class);
                }
                visitor.visitEnd(elementName, this);
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
            // optional
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * Describes the clinical encounter or type of care that the document content is associated with.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Describes the clinical encounter or type of care that the document content is associated with.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param encounter
             *     Context of the document content
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder encounter(Collection<Reference> encounter) {
                this.encounter = new ArrayList<>(encounter);
                return this;
            }

            /**
             * <p>
             * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In 
             * some cases, the event is inherent in the type Code, such as a "History and Physical Report" in which the procedure 
             * being documented is necessarily a "History and Physical" act.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In 
             * some cases, the event is inherent in the type Code, such as a "History and Physical Report" in which the procedure 
             * being documented is necessarily a "History and Physical" act.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param event
             *     Main clinical acts documented
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder event(Collection<CodeableConcept> event) {
                this.event = new ArrayList<>(event);
                return this;
            }

            /**
             * <p>
             * The time period over which the service that is described by the document was provided.
             * </p>
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
             * <p>
             * The kind of facility where the patient was seen.
             * </p>
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
             * <p>
             * This property may convey specifics about the practice setting where the content was created, often reflecting the 
             * clinical specialty.
             * </p>
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
             * <p>
             * The Patient Information as known when the document was published. May be a reference to a version specific, or 
             * contained.
             * </p>
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
             * <p>
             * Related identifiers or resources associated with the DocumentReference.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Related identifiers or resources associated with the DocumentReference.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param related
             *     Related identifiers or resources
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder related(Collection<Reference> related) {
                this.related = new ArrayList<>(related);
                return this;
            }

            @Override
            public Context build() {
                return new Context(this);
            }

            private Builder from(Context context) {
                id = context.id;
                extension.addAll(context.extension);
                modifierExtension.addAll(context.modifierExtension);
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

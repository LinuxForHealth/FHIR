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

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.DocumentReferenceStatus;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A collection of documents compiled for a purpose together with metadata that applies to the collection.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class DocumentManifest extends DomainResource {
    private final Identifier masterIdentifier;
    private final List<Identifier> identifier;
    private final DocumentReferenceStatus status;
    private final CodeableConcept type;
    private final Reference subject;
    private final DateTime created;
    private final List<Reference> author;
    private final List<Reference> recipient;
    private final Uri source;
    private final String description;
    private final List<Reference> content;
    private final List<Related> related;

    private volatile int hashCode;

    private DocumentManifest(Builder builder) {
        super(builder);
        masterIdentifier = builder.masterIdentifier;
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        status = ValidationSupport.requireNonNull(builder.status, "status");
        type = builder.type;
        subject = builder.subject;
        created = builder.created;
        author = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.author, "author"));
        recipient = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.recipient, "recipient"));
        source = builder.source;
        description = builder.description;
        content = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.content, "content"));
        related = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.related, "related"));
    }

    /**
     * <p>
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR 
     * contexts.
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
     * Other identifiers associated with the document manifest, including version independent identifiers.
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
     * The status of this document manifest.
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
     * The code specifying the type of clinical activity that resulted in placing the associated content into the 
     * DocumentManifest.
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
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), 
     * a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of 
     * patients that share a common exposure). If the documents cross more than one subject, then more than one subject is 
     * allowed here (unusual use case).
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
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual 
     * resource last modified time, since it may be modified, replicated, etc.).
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
     * Identifies who is the author of the manifest. Manifest author is not necessarly the author of the references included.
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
     * A patient, practitioner, or organization for which this set of documents is intended.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getRecipient() {
        return recipient;
    }

    /**
     * <p>
     * Identifies the source system, application, or software that produced the document manifest.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getSource() {
        return source;
    }

    /**
     * <p>
     * Human-readable description of the source document. This is sometimes known as the "title".
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
     * The list of Resources that consist of the parts of this manifest.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getContent() {
        return content;
    }

    /**
     * <p>
     * Related identifiers or resources associated with the DocumentManifest.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Related}.
     */
    public List<Related> getRelated() {
        return related;
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
                accept(type, "type", visitor);
                accept(subject, "subject", visitor);
                accept(created, "created", visitor);
                accept(author, "author", visitor, Reference.class);
                accept(recipient, "recipient", visitor, Reference.class);
                accept(source, "source", visitor);
                accept(description, "description", visitor);
                accept(content, "content", visitor, Reference.class);
                accept(related, "related", visitor, Related.class);
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
        DocumentManifest other = (DocumentManifest) obj;
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
            Objects.equals(type, other.type) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(created, other.created) && 
            Objects.equals(author, other.author) && 
            Objects.equals(recipient, other.recipient) && 
            Objects.equals(source, other.source) && 
            Objects.equals(description, other.description) && 
            Objects.equals(content, other.content) && 
            Objects.equals(related, other.related);
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
                type, 
                subject, 
                created, 
                author, 
                recipient, 
                source, 
                description, 
                content, 
                related);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder(DocumentReferenceStatus status, Collection<Reference> content) {
        Builder builder = new Builder();
        builder.status(status);
        builder.content(content);
        return builder;
    }

    public static class Builder extends DomainResource.Builder {
        private Identifier masterIdentifier;
        private List<Identifier> identifier = new ArrayList<>();
        private DocumentReferenceStatus status;
        private CodeableConcept type;
        private Reference subject;
        private DateTime created;
        private List<Reference> author = new ArrayList<>();
        private List<Reference> recipient = new ArrayList<>();
        private Uri source;
        private String description;
        private List<Reference> content = new ArrayList<>();
        private List<Related> related = new ArrayList<>();

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
         * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR 
         * contexts.
         * </p>
         * 
         * @param masterIdentifier
         *     Unique Identifier for the set of documents
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
         * Other identifiers associated with the document manifest, including version independent identifiers.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     Other identifiers for the manifest
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
         * Other identifiers associated with the document manifest, including version independent identifiers.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Other identifiers for the manifest
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
         * The status of this document manifest.
         * </p>
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
         * <p>
         * The code specifying the type of clinical activity that resulted in placing the associated content into the 
         * DocumentManifest.
         * </p>
         * 
         * @param type
         *     Kind of document set
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
         * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), 
         * a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of 
         * patients that share a common exposure). If the documents cross more than one subject, then more than one subject is 
         * allowed here (unusual use case).
         * </p>
         * 
         * @param subject
         *     The subject of the set of documents
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
         * When the document manifest was created for submission to the server (not necessarily the same thing as the actual 
         * resource last modified time, since it may be modified, replicated, etc.).
         * </p>
         * 
         * @param created
         *     When this document manifest created
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder created(DateTime created) {
            this.created = created;
            return this;
        }

        /**
         * <p>
         * Identifies who is the author of the manifest. Manifest author is not necessarly the author of the references included.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param author
         *     Who and/or what authored the DocumentManifest
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
         * Identifies who is the author of the manifest. Manifest author is not necessarly the author of the references included.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param author
         *     Who and/or what authored the DocumentManifest
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
         * A patient, practitioner, or organization for which this set of documents is intended.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param recipient
         *     Intended to get notified about this set of documents
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recipient(Reference... recipient) {
            for (Reference value : recipient) {
                this.recipient.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A patient, practitioner, or organization for which this set of documents is intended.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param recipient
         *     Intended to get notified about this set of documents
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recipient(Collection<Reference> recipient) {
            this.recipient = new ArrayList<>(recipient);
            return this;
        }

        /**
         * <p>
         * Identifies the source system, application, or software that produced the document manifest.
         * </p>
         * 
         * @param source
         *     The source system/application/software
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder source(Uri source) {
            this.source = source;
            return this;
        }

        /**
         * <p>
         * Human-readable description of the source document. This is sometimes known as the "title".
         * </p>
         * 
         * @param description
         *     Human-readable description (title)
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
         * The list of Resources that consist of the parts of this manifest.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param content
         *     Items in manifest
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder content(Reference... content) {
            for (Reference value : content) {
                this.content.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The list of Resources that consist of the parts of this manifest.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param content
         *     Items in manifest
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder content(Collection<Reference> content) {
            this.content = new ArrayList<>(content);
            return this;
        }

        /**
         * <p>
         * Related identifiers or resources associated with the DocumentManifest.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param related
         *     Related things
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder related(Related... related) {
            for (Related value : related) {
                this.related.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Related identifiers or resources associated with the DocumentManifest.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param related
         *     Related things
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder related(Collection<Related> related) {
            this.related = new ArrayList<>(related);
            return this;
        }

        @Override
        public DocumentManifest build() {
            return new DocumentManifest(this);
        }

        protected Builder from(DocumentManifest documentManifest) {
            super.from(documentManifest);
            masterIdentifier = documentManifest.masterIdentifier;
            identifier.addAll(documentManifest.identifier);
            status = documentManifest.status;
            type = documentManifest.type;
            subject = documentManifest.subject;
            created = documentManifest.created;
            author.addAll(documentManifest.author);
            recipient.addAll(documentManifest.recipient);
            source = documentManifest.source;
            description = documentManifest.description;
            content.addAll(documentManifest.content);
            related.addAll(documentManifest.related);
            return this;
        }
    }

    /**
     * <p>
     * Related identifiers or resources associated with the DocumentManifest.
     * </p>
     */
    public static class Related extends BackboneElement {
        private final Identifier identifier;
        private final Reference ref;

        private volatile int hashCode;

        private Related(Builder builder) {
            super(builder);
            identifier = builder.identifier;
            ref = builder.ref;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Related identifier to this DocumentManifest. For example, Order numbers, accession numbers, XDW workflow numbers.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Identifier}.
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        /**
         * <p>
         * Related Resource to this DocumentManifest. For example, Order, ServiceRequest, Procedure, EligibilityRequest, etc.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getRef() {
            return ref;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (identifier != null) || 
                (ref != null);
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
                    accept(identifier, "identifier", visitor);
                    accept(ref, "ref", visitor);
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
            Related other = (Related) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(ref, other.ref);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    identifier, 
                    ref);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            Builder builder = new Builder();
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private Identifier identifier;
            private Reference ref;

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
             * Related identifier to this DocumentManifest. For example, Order numbers, accession numbers, XDW workflow numbers.
             * </p>
             * 
             * @param identifier
             *     Identifiers of things that are related
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identifier(Identifier identifier) {
                this.identifier = identifier;
                return this;
            }

            /**
             * <p>
             * Related Resource to this DocumentManifest. For example, Order, ServiceRequest, Procedure, EligibilityRequest, etc.
             * </p>
             * 
             * @param ref
             *     Related Resource
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder ref(Reference ref) {
                this.ref = ref;
                return this;
            }

            @Override
            public Related build() {
                return new Related(this);
            }

            protected Builder from(Related related) {
                super.from(related);
                identifier = related.identifier;
                ref = related.ref;
                return this;
            }
        }
    }
}

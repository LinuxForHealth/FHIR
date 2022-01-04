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
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.DocumentReferenceStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A collection of documents compiled for a purpose together with metadata that applies to the collection.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DocumentManifest extends DomainResource {
    @Summary
    private final Identifier masterIdentifier;
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "DocumentReferenceStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The status of the document reference.",
        valueSet = "http://hl7.org/fhir/ValueSet/document-reference-status|4.3.0-CIBUILD"
    )
    @Required
    private final DocumentReferenceStatus status;
    @Summary
    @Binding(
        bindingName = "v3Act",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The activity that caused the DocumentManifest to be created.",
        valueSet = "http://terminology.hl7.org/ValueSet/v3-ActCode"
    )
    private final CodeableConcept type;
    @Summary
    @ReferenceTarget({ "Patient", "Practitioner", "Group", "Device" })
    private final Reference subject;
    private final DateTime created;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization", "Device", "Patient", "RelatedPerson" })
    private final List<Reference> author;
    @ReferenceTarget({ "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "Organization" })
    private final List<Reference> recipient;
    private final Uri source;
    @Summary
    private final String description;
    @Summary
    @Required
    private final List<Reference> content;
    private final List<Related> related;

    private DocumentManifest(Builder builder) {
        super(builder);
        masterIdentifier = builder.masterIdentifier;
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        type = builder.type;
        subject = builder.subject;
        created = builder.created;
        author = Collections.unmodifiableList(builder.author);
        recipient = Collections.unmodifiableList(builder.recipient);
        source = builder.source;
        description = builder.description;
        content = Collections.unmodifiableList(builder.content);
        related = Collections.unmodifiableList(builder.related);
    }

    /**
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR 
     * contexts.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getMasterIdentifier() {
        return masterIdentifier;
    }

    /**
     * Other identifiers associated with the document manifest, including version independent identifiers.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The status of this document manifest.
     * 
     * @return
     *     An immutable object of type {@link DocumentReferenceStatus} that is non-null.
     */
    public DocumentReferenceStatus getStatus() {
        return status;
    }

    /**
     * The code specifying the type of clinical activity that resulted in placing the associated content into the 
     * DocumentManifest.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), 
     * a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of 
     * patients that share a common exposure). If the documents cross more than one subject, then more than one subject is 
     * allowed here (unusual use case).
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual 
     * resource last modified time, since it may be modified, replicated, etc.).
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * Identifies who is the author of the manifest. Manifest author is not necessarly the author of the references included.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getAuthor() {
        return author;
    }

    /**
     * A patient, practitioner, or organization for which this set of documents is intended.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getRecipient() {
        return recipient;
    }

    /**
     * Identifies the source system, application, or software that produced the document manifest.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getSource() {
        return source;
    }

    /**
     * Human-readable description of the source document. This is sometimes known as the "title".
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDescription() {
        return description;
    }

    /**
     * The list of Resources that consist of the parts of this manifest.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that is non-empty.
     */
    public List<Reference> getContent() {
        return content;
    }

    /**
     * Related identifiers or resources associated with the DocumentManifest.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Related} that may be empty.
     */
    public List<Related> getRelated() {
        return related;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (masterIdentifier != null) || 
            !identifier.isEmpty() || 
            (status != null) || 
            (type != null) || 
            (subject != null) || 
            (created != null) || 
            !author.isEmpty() || 
            !recipient.isEmpty() || 
            (source != null) || 
            (description != null) || 
            !content.isEmpty() || 
            !related.isEmpty();
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

    public static Builder builder() {
        return new Builder();
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
         * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR 
         * contexts.
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
         * Other identifiers associated with the document manifest, including version independent identifiers.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Other identifiers associated with the document manifest, including version independent identifiers.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Other identifiers for the manifest
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
         * The status of this document manifest.
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
         * The code specifying the type of clinical activity that resulted in placing the associated content into the 
         * DocumentManifest.
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
         * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), 
         * a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of 
         * patients that share a common exposure). If the documents cross more than one subject, then more than one subject is 
         * allowed here (unusual use case).
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
         * When the document manifest was created for submission to the server (not necessarily the same thing as the actual 
         * resource last modified time, since it may be modified, replicated, etc.).
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
         * Identifies who is the author of the manifest. Manifest author is not necessarly the author of the references included.
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
         * Identifies who is the author of the manifest. Manifest author is not necessarly the author of the references included.
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
         *     Who and/or what authored the DocumentManifest
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
         * A patient, practitioner, or organization for which this set of documents is intended.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Organization}</li>
         * </ul>
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
         * A patient, practitioner, or organization for which this set of documents is intended.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param recipient
         *     Intended to get notified about this set of documents
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder recipient(Collection<Reference> recipient) {
            this.recipient = new ArrayList<>(recipient);
            return this;
        }

        /**
         * Identifies the source system, application, or software that produced the document manifest.
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
         * Convenience method for setting {@code description}.
         * 
         * @param description
         *     Human-readable description (title)
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
         * Human-readable description of the source document. This is sometimes known as the "title".
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
         * The list of Resources that consist of the parts of this manifest.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
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
         * The list of Resources that consist of the parts of this manifest.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param content
         *     Items in manifest
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder content(Collection<Reference> content) {
            this.content = new ArrayList<>(content);
            return this;
        }

        /**
         * Related identifiers or resources associated with the DocumentManifest.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Related identifiers or resources associated with the DocumentManifest.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param related
         *     Related things
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder related(Collection<Related> related) {
            this.related = new ArrayList<>(related);
            return this;
        }

        /**
         * Build the {@link DocumentManifest}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>content</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link DocumentManifest}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid DocumentManifest per the base specification
         */
        @Override
        public DocumentManifest build() {
            DocumentManifest documentManifest = new DocumentManifest(this);
            if (validating) {
                validate(documentManifest);
            }
            return documentManifest;
        }

        protected void validate(DocumentManifest documentManifest) {
            super.validate(documentManifest);
            ValidationSupport.checkList(documentManifest.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(documentManifest.status, "status");
            ValidationSupport.checkList(documentManifest.author, "author", Reference.class);
            ValidationSupport.checkList(documentManifest.recipient, "recipient", Reference.class);
            ValidationSupport.checkNonEmptyList(documentManifest.content, "content", Reference.class);
            ValidationSupport.checkList(documentManifest.related, "related", Related.class);
            ValidationSupport.checkReferenceType(documentManifest.subject, "subject", "Patient", "Practitioner", "Group", "Device");
            ValidationSupport.checkReferenceType(documentManifest.author, "author", "Practitioner", "PractitionerRole", "Organization", "Device", "Patient", "RelatedPerson");
            ValidationSupport.checkReferenceType(documentManifest.recipient, "recipient", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "Organization");
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
     * Related identifiers or resources associated with the DocumentManifest.
     */
    public static class Related extends BackboneElement {
        private final Identifier identifier;
        private final Reference ref;

        private Related(Builder builder) {
            super(builder);
            identifier = builder.identifier;
            ref = builder.ref;
        }

        /**
         * Related identifier to this DocumentManifest. For example, Order numbers, accession numbers, XDW workflow numbers.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        /**
         * Related Resource to this DocumentManifest. For example, Order, ServiceRequest, Procedure, EligibilityRequest, etc.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
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
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private Identifier identifier;
            private Reference ref;

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
             * Related identifier to this DocumentManifest. For example, Order numbers, accession numbers, XDW workflow numbers.
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
             * Related Resource to this DocumentManifest. For example, Order, ServiceRequest, Procedure, EligibilityRequest, etc.
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

            /**
             * Build the {@link Related}
             * 
             * @return
             *     An immutable object of type {@link Related}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Related per the base specification
             */
            @Override
            public Related build() {
                Related related = new Related(this);
                if (validating) {
                    validate(related);
                }
                return related;
            }

            protected void validate(Related related) {
                super.validate(related);
                ValidationSupport.requireValueOrChildren(related);
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

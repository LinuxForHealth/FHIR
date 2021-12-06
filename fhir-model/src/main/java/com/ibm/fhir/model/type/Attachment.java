/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * For referring to data content defined in other formats.
 */
@Constraint(
    id = "att-1",
    level = "Rule",
    location = "(base)",
    description = "If the Attachment has data, it SHALL have a contentType",
    expression = "data.empty() or contentType.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Attachment"
)
@Constraint(
    id = "attachment-2",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/languages",
    expression = "language.exists() implies (language.memberOf('http://hl7.org/fhir/ValueSet/languages', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/Attachment",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Attachment extends Element {
    @Summary
    @Binding(
        bindingName = "MimeType",
        strength = BindingStrength.Value.REQUIRED,
        description = "The mime type of an attachment. Any valid mime type is allowed.",
        valueSet = "http://hl7.org/fhir/ValueSet/mimetypes|4.1.0"
    )
    private final Code contentType;
    @Summary
    @Binding(
        bindingName = "Language",
        strength = BindingStrength.Value.PREFERRED,
        description = "A human language.",
        valueSet = "http://hl7.org/fhir/ValueSet/languages",
        maxValueSet = "http://hl7.org/fhir/ValueSet/all-languages"
    )
    private final Code language;
    private final Base64Binary data;
    @Summary
    private final Url url;
    @Summary
    private final UnsignedInt size;
    @Summary
    private final Base64Binary hash;
    @Summary
    private final String title;
    @Summary
    private final DateTime creation;

    private Attachment(Builder builder) {
        super(builder);
        contentType = builder.contentType;
        language = builder.language;
        data = builder.data;
        url = builder.url;
        size = builder.size;
        hash = builder.hash;
        title = builder.title;
        creation = builder.creation;
    }

    /**
     * Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. 
     * Includes mime type parameters such as charset where appropriate.
     * 
     * @return
     *     An immutable object of type {@link Code} that may be null.
     */
    public Code getContentType() {
        return contentType;
    }

    /**
     * The human language of the content. The value can be any valid value according to BCP 47.
     * 
     * @return
     *     An immutable object of type {@link Code} that may be null.
     */
    public Code getLanguage() {
        return language;
    }

    /**
     * The actual data of the attachment - a sequence of bytes, base64 encoded.
     * 
     * @return
     *     An immutable object of type {@link Base64Binary} that may be null.
     */
    public Base64Binary getData() {
        return data;
    }

    /**
     * A location where the data can be accessed.
     * 
     * @return
     *     An immutable object of type {@link Url} that may be null.
     */
    public Url getUrl() {
        return url;
    }

    /**
     * The number of bytes of data that make up this attachment (before base64 encoding, if that is done).
     * 
     * @return
     *     An immutable object of type {@link UnsignedInt} that may be null.
     */
    public UnsignedInt getSize() {
        return size;
    }

    /**
     * The calculated hash of the data using SHA-1. Represented using base64.
     * 
     * @return
     *     An immutable object of type {@link Base64Binary} that may be null.
     */
    public Base64Binary getHash() {
        return hash;
    }

    /**
     * A label or set of text to display in place of the data.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The date that the attachment was first created.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getCreation() {
        return creation;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (contentType != null) || 
            (language != null) || 
            (data != null) || 
            (url != null) || 
            (size != null) || 
            (hash != null) || 
            (title != null) || 
            (creation != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(contentType, "contentType", visitor);
                accept(language, "language", visitor);
                accept(data, "data", visitor);
                accept(url, "url", visitor);
                accept(size, "size", visitor);
                accept(hash, "hash", visitor);
                accept(title, "title", visitor);
                accept(creation, "creation", visitor);
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
        Attachment other = (Attachment) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(contentType, other.contentType) && 
            Objects.equals(language, other.language) && 
            Objects.equals(data, other.data) && 
            Objects.equals(url, other.url) && 
            Objects.equals(size, other.size) && 
            Objects.equals(hash, other.hash) && 
            Objects.equals(title, other.title) && 
            Objects.equals(creation, other.creation);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                contentType, 
                language, 
                data, 
                url, 
                size, 
                hash, 
                title, 
                creation);
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

    public static class Builder extends Element.Builder {
        private Code contentType;
        private Code language;
        private Base64Binary data;
        private Url url;
        private UnsignedInt size;
        private Base64Binary hash;
        private String title;
        private DateTime creation;

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
         * Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. 
         * Includes mime type parameters such as charset where appropriate.
         * 
         * @param contentType
         *     Mime type of the content, with charset etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contentType(Code contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * The human language of the content. The value can be any valid value according to BCP 47.
         * 
         * @param language
         *     Human language of the content (BCP-47)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder language(Code language) {
            this.language = language;
            return this;
        }

        /**
         * The actual data of the attachment - a sequence of bytes, base64 encoded.
         * 
         * @param data
         *     Data inline, base64ed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder data(Base64Binary data) {
            this.data = data;
            return this;
        }

        /**
         * A location where the data can be accessed.
         * 
         * @param url
         *     Uri where the data can be found
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Url url) {
            this.url = url;
            return this;
        }

        /**
         * The number of bytes of data that make up this attachment (before base64 encoding, if that is done).
         * 
         * @param size
         *     Number of bytes of content (if url provided)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder size(UnsignedInt size) {
            this.size = size;
            return this;
        }

        /**
         * The calculated hash of the data using SHA-1. Represented using base64.
         * 
         * @param hash
         *     Hash of the data (sha-1, base64ed)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder hash(Base64Binary hash) {
            this.hash = hash;
            return this;
        }

        /**
         * Convenience method for setting {@code title}.
         * 
         * @param title
         *     Label to display in place of the data
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
         * A label or set of text to display in place of the data.
         * 
         * @param title
         *     Label to display in place of the data
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The date that the attachment was first created.
         * 
         * @param creation
         *     Date attachment was first created
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder creation(DateTime creation) {
            this.creation = creation;
            return this;
        }

        /**
         * Build the {@link Attachment}
         * 
         * @return
         *     An immutable object of type {@link Attachment}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Attachment per the base specification
         */
        @Override
        public Attachment build() {
            Attachment attachment = new Attachment(this);
            if (validating) {
                validate(attachment);
            }
            return attachment;
        }

        protected void validate(Attachment attachment) {
            super.validate(attachment);
            ValidationSupport.checkValueSetBinding(attachment.language, "language", "http://hl7.org/fhir/ValueSet/all-languages", "urn:ietf:bcp:47");
            ValidationSupport.requireValueOrChildren(attachment);
        }

        protected Builder from(Attachment attachment) {
            super.from(attachment);
            contentType = attachment.contentType;
            language = attachment.language;
            data = attachment.data;
            url = attachment.url;
            size = attachment.size;
            hash = attachment.hash;
            title = attachment.title;
            creation = attachment.creation;
            return this;
        }
    }
}

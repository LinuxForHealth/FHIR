/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * For referring to data content defined in other formats.
 * </p>
 */
@Constraint(
    id = "att-1",
    level = "Rule",
    location = "(base)",
    description = "If the Attachment has data, it SHALL have a contentType",
    expression = "data.empty() or contentType.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Attachment extends Element {
    private final Code contentType;
    private final Code language;
    private final Base64Binary data;
    private final Url url;
    private final UnsignedInt size;
    private final Base64Binary hash;
    private final String title;
    private final DateTime creation;

    private Attachment(Builder builder) {
        super(builder);
        this.contentType = builder.contentType;
        this.language = builder.language;
        this.data = builder.data;
        this.url = builder.url;
        this.size = builder.size;
        this.hash = builder.hash;
        this.title = builder.title;
        this.creation = builder.creation;
    }

    /**
     * <p>
     * Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. 
     * Includes mime type parameters such as charset where appropriate.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Code}.
     */
    public Code getContentType() {
        return contentType;
    }

    /**
     * <p>
     * The human language of the content. The value can be any valid value according to BCP 47.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Code}.
     */
    public Code getLanguage() {
        return language;
    }

    /**
     * <p>
     * The actual data of the attachment - a sequence of bytes, base64 encoded.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Base64Binary}.
     */
    public Base64Binary getData() {
        return data;
    }

    /**
     * <p>
     * A location where the data can be accessed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Url}.
     */
    public Url getUrl() {
        return url;
    }

    /**
     * <p>
     * The number of bytes of data that make up this attachment (before base64 encoding, if that is done).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link UnsignedInt}.
     */
    public UnsignedInt getSize() {
        return size;
    }

    /**
     * <p>
     * The calculated hash of the data using SHA-1. Represented using base64.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Base64Binary}.
     */
    public Base64Binary getHash() {
        return hash;
    }

    /**
     * <p>
     * A label or set of text to display in place of the data.
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
     * The date that the attachment was first created.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getCreation() {
        return creation;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.contentType = contentType;
        builder.language = language;
        builder.data = data;
        builder.url = url;
        builder.size = size;
        builder.hash = hash;
        builder.title = title;
        builder.creation = creation;
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Element.Builder {
        // optional
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
         * Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. 
         * Includes mime type parameters such as charset where appropriate.
         * </p>
         * 
         * @param contentType
         *     Mime type of the content, with charset etc.
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contentType(Code contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * <p>
         * The human language of the content. The value can be any valid value according to BCP 47.
         * </p>
         * 
         * @param language
         *     Human language of the content (BCP-47)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder language(Code language) {
            this.language = language;
            return this;
        }

        /**
         * <p>
         * The actual data of the attachment - a sequence of bytes, base64 encoded.
         * </p>
         * 
         * @param data
         *     Data inline, base64ed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder data(Base64Binary data) {
            this.data = data;
            return this;
        }

        /**
         * <p>
         * A location where the data can be accessed.
         * </p>
         * 
         * @param url
         *     Uri where the data can be found
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder url(Url url) {
            this.url = url;
            return this;
        }

        /**
         * <p>
         * The number of bytes of data that make up this attachment (before base64 encoding, if that is done).
         * </p>
         * 
         * @param size
         *     Number of bytes of content (if url provided)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder size(UnsignedInt size) {
            this.size = size;
            return this;
        }

        /**
         * <p>
         * The calculated hash of the data using SHA-1. Represented using base64.
         * </p>
         * 
         * @param hash
         *     Hash of the data (sha-1, base64ed)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder hash(Base64Binary hash) {
            this.hash = hash;
            return this;
        }

        /**
         * <p>
         * A label or set of text to display in place of the data.
         * </p>
         * 
         * @param title
         *     Label to display in place of the data
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
         * The date that the attachment was first created.
         * </p>
         * 
         * @param creation
         *     Date attachment was first created
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder creation(DateTime creation) {
            this.creation = creation;
            return this;
        }

        @Override
        public Attachment build() {
            return new Attachment(this);
        }
    }
}

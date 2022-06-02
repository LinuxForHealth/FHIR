/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.resource;

import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A resource that represents the data of a single raw artifact as digital content accessible in its native format. A 
 * Binary resource can contain any content, whether text, image, pdf, zip archive, etc.
 * 
 * <p>Maturity level: FMM5 (Normative)
 */
@Maturity(
    level = 5,
    status = StandardsStatus.Value.NORMATIVE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Binary extends Resource {
    @Summary
    @Binding(
        bindingName = "MimeType",
        strength = BindingStrength.Value.REQUIRED,
        description = "BCP 13 (RFCs 2045, 2046, 2047, 4288, 4289 and 2049)",
        valueSet = "http://hl7.org/fhir/ValueSet/mimetypes|4.3.0-cibuild"
    )
    @Required
    private final Code contentType;
    @Summary
    private final Reference securityContext;
    private final Base64Binary data;

    private Binary(Builder builder) {
        super(builder);
        contentType = builder.contentType;
        securityContext = builder.securityContext;
        data = builder.data;
    }

    /**
     * MimeType of the binary content represented as a standard MimeType (BCP 13).
     * 
     * @return
     *     An immutable object of type {@link Code} that is non-null.
     */
    public Code getContentType() {
        return contentType;
    }

    /**
     * This element identifies another resource that can be used as a proxy of the security sensitivity to use when deciding 
     * and enforcing access control rules for the Binary resource. Given that the Binary resource contains very few elements 
     * that can be used to determine the sensitivity of the data and relationships to individuals, the referenced resource 
     * stands in as a proxy equivalent for this purpose. This referenced resource may be related to the Binary (e.g. Media, 
     * DocumentReference), or may be some non-related Resource purely as a security proxy. E.g. to identify that the binary 
     * resource relates to a patient, and access should only be granted to applications that have access to the patient.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSecurityContext() {
        return securityContext;
    }

    /**
     * The actual content, base64 encoded.
     * 
     * @return
     *     An immutable object of type {@link Base64Binary} that may be null.
     */
    public Base64Binary getData() {
        return data;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (contentType != null) || 
            (securityContext != null) || 
            (data != null);
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
                accept(contentType, "contentType", visitor);
                accept(securityContext, "securityContext", visitor);
                accept(data, "data", visitor);
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
        Binary other = (Binary) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(contentType, other.contentType) && 
            Objects.equals(securityContext, other.securityContext) && 
            Objects.equals(data, other.data);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                meta, 
                implicitRules, 
                language, 
                contentType, 
                securityContext, 
                data);
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

    public static class Builder extends Resource.Builder {
        private Code contentType;
        private Reference securityContext;
        private Base64Binary data;

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
         * MimeType of the binary content represented as a standard MimeType (BCP 13).
         * 
         * <p>This element is required.
         * 
         * @param contentType
         *     MimeType of the binary content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contentType(Code contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * This element identifies another resource that can be used as a proxy of the security sensitivity to use when deciding 
         * and enforcing access control rules for the Binary resource. Given that the Binary resource contains very few elements 
         * that can be used to determine the sensitivity of the data and relationships to individuals, the referenced resource 
         * stands in as a proxy equivalent for this purpose. This referenced resource may be related to the Binary (e.g. Media, 
         * DocumentReference), or may be some non-related Resource purely as a security proxy. E.g. to identify that the binary 
         * resource relates to a patient, and access should only be granted to applications that have access to the patient.
         * 
         * @param securityContext
         *     Identifies another resource to use as proxy when enforcing access control
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder securityContext(Reference securityContext) {
            this.securityContext = securityContext;
            return this;
        }

        /**
         * The actual content, base64 encoded.
         * 
         * @param data
         *     The actual content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder data(Base64Binary data) {
            this.data = data;
            return this;
        }

        /**
         * Build the {@link Binary}
         * 
         * <p>Required elements:
         * <ul>
         * <li>contentType</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Binary}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Binary per the base specification
         */
        @Override
        public Binary build() {
            Binary binary = new Binary(this);
            if (validating) {
                validate(binary);
            }
            return binary;
        }

        protected void validate(Binary binary) {
            super.validate(binary);
            ValidationSupport.requireNonNull(binary.contentType, "contentType");
        }

        protected Builder from(Binary binary) {
            super.from(binary);
            contentType = binary.contentType;
            securityContext = binary.securityContext;
            data = binary.data;
            return this;
        }
    }
}

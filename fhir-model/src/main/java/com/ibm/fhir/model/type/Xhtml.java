/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;
import org.owasp.encoder.Encode;

/**
 * XHTML
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Xhtml extends Element {
    public static final java.lang.String DIV_OPEN = "<div xmlns=\"http://www.w3.org/1999/xhtml\">";

    public static final java.lang.String DIV_CLOSE = "</div>";

    @Required
    private final java.lang.String value;

    private Xhtml(Builder builder) {
        super(builder);
        value = builder.value;
    }

    /**
     * Actual xhtml
     *
     * @return
     *     An immutable object of type {@link java.lang.String} that is non-null.
     */
    public java.lang.String getValue() {
        return value;
    }

    @Override
    public boolean hasValue() {
        return (value != null);
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren();
    }

    /**
     * Factory method for creating Xhtml objects from an XHTML java.lang.String
     *
     * @param value
     *     A java.lang.String with valid XHTML content, not null
     */
    public static Xhtml of(java.lang.String value) {
        Objects.requireNonNull(value, "value");
        return Xhtml.builder().value(value).build();
    }

    /**
     * Factory method for creating Xhtml objects from an XHTML java.lang.String
     *
     * @param value
     *     A java.lang.String with valid XHTML content, not null
     */
    public static Xhtml xhtml(java.lang.String value) {
        Objects.requireNonNull(value, "value");
        return Xhtml.builder().value(value).build();
    }

    /**
     * Factory method for creating Xhtml objects from a plain text string
     *
     * <p>This method will automatically encode the passed string for use within XHTML,
     * then wrap it in an XHTML {@code <div>} element with a namespace of {@code http://www.w3.org/1999/xhtml}
     *
     * @param plainText
     *     The text to encode and wrap for use within a Narrative, not null
     */
    public static Xhtml from(java.lang.String plainText) {
        Objects.requireNonNull(plainText, "plainText");
        return Xhtml.builder().value(DIV_OPEN + Encode.forHtmlContent(plainText) + DIV_CLOSE).build();
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(value, "value", visitor);
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
        Xhtml other = (Xhtml) obj;
        return Objects.equals(id, other.id) &&
            Objects.equals(extension, other.extension) &&
            Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id,
                extension,
                value);
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
        private java.lang.String value;

        private Builder() {
            super();
        }

        /**
         * unique id for the element within a resource (for internal references)
         *
         * @param id
         *     xml:id (or equivalent in JSON)
         *
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
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
         * <p>This element is prohibited.
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
         * <p>This element is prohibited.
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
         * Actual xhtml
         *
         * <p>This element is required.
         *
         * @param value
         *     Actual xhtml
         *
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(java.lang.String value) {
            this.value = value;
            return this;
        }

        /**
         * Build the {@link Xhtml}
         *
         * <p>Required elements:
         * <ul>
         * <li>value</li>
         * </ul>
         *
         * @return
         *     An immutable object of type {@link Xhtml}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Xhtml per the base specification
         */
        @Override
        public Xhtml build() {
            Xhtml xhtml = new Xhtml(this);
            if (validating) {
                validate(xhtml);
            }
            return xhtml;
        }

        protected void validate(Xhtml xhtml) {
            super.validate(xhtml);
            ValidationSupport.requireNonNull(xhtml.value, "value");
            ValidationSupport.prohibited(xhtml.extension, "extension");
            ValidationSupport.checkXHTMLContent(xhtml.value);
        }

        protected Builder from(Xhtml xhtml) {
            super.from(xhtml);
            value = xhtml.value;
            return this;
        }
    }
}

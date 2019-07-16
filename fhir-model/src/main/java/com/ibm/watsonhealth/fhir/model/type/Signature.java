/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A signature along with supporting context. The signature may be a digital signature that is cryptographic in nature, 
 * or some other signature acceptable to the domain. This other signature may be as simple as a graphical image 
 * representing a hand-written signature, or a signature ceremony Different signature approaches have different utilities.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Signature extends Element {
    private final List<Coding> type;
    private final Instant when;
    private final Reference who;
    private final Reference onBehalfOf;
    private final Code targetFormat;
    private final Code sigFormat;
    private final Base64Binary data;

    private volatile int hashCode;

    private Signature(Builder builder) {
        super(builder);
        type = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.type, "type"));
        when = ValidationSupport.requireNonNull(builder.when, "when");
        who = ValidationSupport.requireNonNull(builder.who, "who");
        onBehalfOf = builder.onBehalfOf;
        targetFormat = builder.targetFormat;
        sigFormat = builder.sigFormat;
        data = builder.data;
        ValidationSupport.requireValueOrChildren(this);
    }

    /**
     * <p>
     * An indication of the reason that the entity signed this document. This may be explicitly included as part of the 
     * signature information and can be used when determining accountability for various actions concerning the document.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Coding}.
     */
    public List<Coding> getType() {
        return type;
    }

    /**
     * <p>
     * When the digital signature was signed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Instant}.
     */
    public Instant getWhen() {
        return when;
    }

    /**
     * <p>
     * A reference to an application-usable description of the identity that signed (e.g. the signature used their private 
     * key).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getWho() {
        return who;
    }

    /**
     * <p>
     * A reference to an application-usable description of the identity that is represented by the signature.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getOnBehalfOf() {
        return onBehalfOf;
    }

    /**
     * <p>
     * A mime type that indicates the technical format of the target resources signed by the signature.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Code}.
     */
    public Code getTargetFormat() {
        return targetFormat;
    }

    /**
     * <p>
     * A mime type that indicates the technical format of the signature. Important mime types are application/signature+xml 
     * for X ML DigSig, application/jose for JWS, and image/* for a graphical image of a signature, etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Code}.
     */
    public Code getSigFormat() {
        return sigFormat;
    }

    /**
     * <p>
     * The base64 encoding of the Signature content. When signature is not recorded electronically this element would be 
     * empty.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Base64Binary}.
     */
    public Base64Binary getData() {
        return data;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !type.isEmpty() || 
            (when != null) || 
            (who != null) || 
            (onBehalfOf != null) || 
            (targetFormat != null) || 
            (sigFormat != null) || 
            (data != null);
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(type, "type", visitor, Coding.class);
                accept(when, "when", visitor);
                accept(who, "who", visitor);
                accept(onBehalfOf, "onBehalfOf", visitor);
                accept(targetFormat, "targetFormat", visitor);
                accept(sigFormat, "sigFormat", visitor);
                accept(data, "data", visitor);
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
        Signature other = (Signature) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(type, other.type) && 
            Objects.equals(when, other.when) && 
            Objects.equals(who, other.who) && 
            Objects.equals(onBehalfOf, other.onBehalfOf) && 
            Objects.equals(targetFormat, other.targetFormat) && 
            Objects.equals(sigFormat, other.sigFormat) && 
            Objects.equals(data, other.data);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                type, 
                when, 
                who, 
                onBehalfOf, 
                targetFormat, 
                sigFormat, 
                data);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, when, who).from(this);
    }

    public Builder toBuilder(Collection<Coding> type, Instant when, Reference who) {
        return new Builder(type, when, who).from(this);
    }

    public static Builder builder(Collection<Coding> type, Instant when, Reference who) {
        return new Builder(type, when, who);
    }

    public static class Builder extends Element.Builder {
        // required
        private final List<Coding> type;
        private final Instant when;
        private final Reference who;

        // optional
        private Reference onBehalfOf;
        private Code targetFormat;
        private Code sigFormat;
        private Base64Binary data;

        private Builder(Collection<Coding> type, Instant when, Reference who) {
            super();
            this.type = new ArrayList<>(type);
            this.when = when;
            this.who = who;
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
         * A reference to an application-usable description of the identity that is represented by the signature.
         * </p>
         * 
         * @param onBehalfOf
         *     The party represented
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder onBehalfOf(Reference onBehalfOf) {
            this.onBehalfOf = onBehalfOf;
            return this;
        }

        /**
         * <p>
         * A mime type that indicates the technical format of the target resources signed by the signature.
         * </p>
         * 
         * @param targetFormat
         *     The technical format of the signed resources
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder targetFormat(Code targetFormat) {
            this.targetFormat = targetFormat;
            return this;
        }

        /**
         * <p>
         * A mime type that indicates the technical format of the signature. Important mime types are application/signature+xml 
         * for X ML DigSig, application/jose for JWS, and image/* for a graphical image of a signature, etc.
         * </p>
         * 
         * @param sigFormat
         *     The technical format of the signature
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sigFormat(Code sigFormat) {
            this.sigFormat = sigFormat;
            return this;
        }

        /**
         * <p>
         * The base64 encoding of the Signature content. When signature is not recorded electronically this element would be 
         * empty.
         * </p>
         * 
         * @param data
         *     The actual signature content (XML DigSig. JWS, picture, etc.)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder data(Base64Binary data) {
            this.data = data;
            return this;
        }

        @Override
        public Signature build() {
            return new Signature(this);
        }

        private Builder from(Signature signature) {
            id = signature.id;
            extension.addAll(signature.extension);
            onBehalfOf = signature.onBehalfOf;
            targetFormat = signature.targetFormat;
            sigFormat = signature.sigFormat;
            data = signature.data;
            return this;
        }
    }
}

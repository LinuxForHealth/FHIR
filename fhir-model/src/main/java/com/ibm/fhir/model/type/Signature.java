/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A signature along with supporting context. The signature may be a digital signature that is cryptographic in nature, 
 * or some other signature acceptable to the domain. This other signature may be as simple as a graphical image 
 * representing a hand-written signature, or a signature ceremony Different signature approaches have different utilities.
 */
@Constraint(
    id = "signature-0",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/signature-type",
    expression = "type.exists() and type.all(memberOf('http://hl7.org/fhir/ValueSet/signature-type', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/Signature",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Signature extends Element {
    @Summary
    @Binding(
        bindingName = "SignatureType",
        strength = BindingStrength.Value.PREFERRED,
        valueSet = "http://hl7.org/fhir/ValueSet/signature-type"
    )
    @Required
    private final List<Coding> type;
    @Summary
    @Required
    private final Instant when;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "RelatedPerson", "Patient", "Device", "Organization" })
    @Required
    private final Reference who;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "RelatedPerson", "Patient", "Device", "Organization" })
    private final Reference onBehalfOf;
    @Binding(
        bindingName = "MimeType",
        strength = BindingStrength.Value.REQUIRED,
        description = "BCP 13 (RFCs 2045, 2046, 2047, 4288, 4289 and 2049)",
        valueSet = "http://hl7.org/fhir/ValueSet/mimetypes|4.3.0-cibuild"
    )
    private final Code targetFormat;
    @Binding(
        bindingName = "MimeType",
        strength = BindingStrength.Value.REQUIRED,
        description = "BCP 13 (RFCs 2045, 2046, 2047, 4288, 4289 and 2049)",
        valueSet = "http://hl7.org/fhir/ValueSet/mimetypes|4.3.0-cibuild"
    )
    private final Code sigFormat;
    private final Base64Binary data;

    private Signature(Builder builder) {
        super(builder);
        type = Collections.unmodifiableList(builder.type);
        when = builder.when;
        who = builder.who;
        onBehalfOf = builder.onBehalfOf;
        targetFormat = builder.targetFormat;
        sigFormat = builder.sigFormat;
        data = builder.data;
    }

    /**
     * An indication of the reason that the entity signed this document. This may be explicitly included as part of the 
     * signature information and can be used when determining accountability for various actions concerning the document.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Coding} that is non-empty.
     */
    public List<Coding> getType() {
        return type;
    }

    /**
     * When the digital signature was signed.
     * 
     * @return
     *     An immutable object of type {@link Instant} that is non-null.
     */
    public Instant getWhen() {
        return when;
    }

    /**
     * A reference to an application-usable description of the identity that signed (e.g. the signature used their private 
     * key).
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getWho() {
        return who;
    }

    /**
     * A reference to an application-usable description of the identity that is represented by the signature.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getOnBehalfOf() {
        return onBehalfOf;
    }

    /**
     * A mime type that indicates the technical format of the target resources signed by the signature.
     * 
     * @return
     *     An immutable object of type {@link Code} that may be null.
     */
    public Code getTargetFormat() {
        return targetFormat;
    }

    /**
     * A mime type that indicates the technical format of the signature. Important mime types are application/signature+xml 
     * for X ML DigSig, application/jose for JWS, and image/* for a graphical image of a signature, etc.
     * 
     * @return
     *     An immutable object of type {@link Code} that may be null.
     */
    public Code getSigFormat() {
        return sigFormat;
    }

    /**
     * The base64 encoding of the Signature content. When signature is not recorded electronically this element would be 
     * empty.
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
            !type.isEmpty() || 
            (when != null) || 
            (who != null) || 
            (onBehalfOf != null) || 
            (targetFormat != null) || 
            (sigFormat != null) || 
            (data != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
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
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Element.Builder {
        private List<Coding> type = new ArrayList<>();
        private Instant when;
        private Reference who;
        private Reference onBehalfOf;
        private Code targetFormat;
        private Code sigFormat;
        private Base64Binary data;

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
         * An indication of the reason that the entity signed this document. This may be explicitly included as part of the 
         * signature information and can be used when determining accountability for various actions concerning the document.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     Indication of the reason the entity signed the object(s)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(Coding... type) {
            for (Coding value : type) {
                this.type.add(value);
            }
            return this;
        }

        /**
         * An indication of the reason that the entity signed this document. This may be explicitly included as part of the 
         * signature information and can be used when determining accountability for various actions concerning the document.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     Indication of the reason the entity signed the object(s)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder type(Collection<Coding> type) {
            this.type = new ArrayList<>(type);
            return this;
        }

        /**
         * Convenience method for setting {@code when}.
         * 
         * <p>This element is required.
         * 
         * @param when
         *     When the signature was created
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #when(com.ibm.fhir.model.type.Instant)
         */
        public Builder when(java.time.ZonedDateTime when) {
            this.when = (when == null) ? null : Instant.of(when);
            return this;
        }

        /**
         * When the digital signature was signed.
         * 
         * <p>This element is required.
         * 
         * @param when
         *     When the signature was created
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder when(Instant when) {
            this.when = when;
            return this;
        }

        /**
         * A reference to an application-usable description of the identity that signed (e.g. the signature used their private 
         * key).
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Patient}</li>
         * <li>{@link Device}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param who
         *     Who signed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder who(Reference who) {
            this.who = who;
            return this;
        }

        /**
         * A reference to an application-usable description of the identity that is represented by the signature.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Patient}</li>
         * <li>{@link Device}</li>
         * <li>{@link Organization}</li>
         * </ul>
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
         * A mime type that indicates the technical format of the target resources signed by the signature.
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
         * A mime type that indicates the technical format of the signature. Important mime types are application/signature+xml 
         * for X ML DigSig, application/jose for JWS, and image/* for a graphical image of a signature, etc.
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
         * The base64 encoding of the Signature content. When signature is not recorded electronically this element would be 
         * empty.
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

        /**
         * Build the {@link Signature}
         * 
         * <p>Required elements:
         * <ul>
         * <li>type</li>
         * <li>when</li>
         * <li>who</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Signature}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Signature per the base specification
         */
        @Override
        public Signature build() {
            Signature signature = new Signature(this);
            if (validating) {
                validate(signature);
            }
            return signature;
        }

        protected void validate(Signature signature) {
            super.validate(signature);
            ValidationSupport.checkNonEmptyList(signature.type, "type", Coding.class);
            ValidationSupport.requireNonNull(signature.when, "when");
            ValidationSupport.requireNonNull(signature.who, "who");
            ValidationSupport.checkReferenceType(signature.who, "who", "Practitioner", "PractitionerRole", "RelatedPerson", "Patient", "Device", "Organization");
            ValidationSupport.checkReferenceType(signature.onBehalfOf, "onBehalfOf", "Practitioner", "PractitionerRole", "RelatedPerson", "Patient", "Device", "Organization");
            ValidationSupport.requireValueOrChildren(signature);
        }

        protected Builder from(Signature signature) {
            super.from(signature);
            type.addAll(signature.type);
            when = signature.when;
            who = signature.who;
            onBehalfOf = signature.onBehalfOf;
            targetFormat = signature.targetFormat;
            sigFormat = signature.sigFormat;
            data = signature.data;
            return this;
        }
    }
}

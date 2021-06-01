/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Optional Extension Element - found in all resources.
 */
@Constraint(
    id = "ext-1",
    level = "Rule",
    location = "(base)",
    description = "Must have either extensions or value[x], not both",
    expression = "extension.exists() != value.exists()"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Extension extends Element {
    @Required
    private final java.lang.String url;
    @Choice({ Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class })
    private final Element value;

    private Extension(Builder builder) {
        super(builder);
        url = builder.url;
        value = builder.value;
    }

    /**
     * Source of the definition for the extension code - a logical name or a URL.
     * 
     * @return
     *     An immutable object of type {@link java.lang.String} that is non-null.
     */
    public java.lang.String getUrl() {
        return url;
    }

    /**
     * Value of extension - must be one of a constrained set of the data types (see [Extensibility](extensibility.html) for a 
     * list).
     * 
     * @return
     *     An immutable object of type {@link Element} that may be null.
     */
    public Element getValue() {
        return value;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
            (value != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(url, "url", visitor);
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
        Extension other = (Extension) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(url, other.url) && 
            Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                url, 
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
        private java.lang.String url;
        private Element value;

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
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * Source of the definition for the extension code - a logical name or a URL.
         * 
         * <p>This element is required.
         * 
         * @param url
         *     identifies the meaning of the extension
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(java.lang.String url) {
            this.url = url;
            return this;
        }

        /**
         * Value of extension - must be one of a constrained set of the data types (see [Extensibility](extensibility.html) for a 
         * list).
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Base64Binary}</li>
         * <li>{@link Boolean}</li>
         * <li>{@link Canonical}</li>
         * <li>{@link Code}</li>
         * <li>{@link Date}</li>
         * <li>{@link DateTime}</li>
         * <li>{@link Decimal}</li>
         * <li>{@link Id}</li>
         * <li>{@link Instant}</li>
         * <li>{@link Integer}</li>
         * <li>{@link Markdown}</li>
         * <li>{@link Oid}</li>
         * <li>{@link PositiveInt}</li>
         * <li>{@link String}</li>
         * <li>{@link Time}</li>
         * <li>{@link UnsignedInt}</li>
         * <li>{@link Uri}</li>
         * <li>{@link Url}</li>
         * <li>{@link Uuid}</li>
         * <li>{@link Address}</li>
         * <li>{@link Age}</li>
         * <li>{@link Annotation}</li>
         * <li>{@link Attachment}</li>
         * <li>{@link CodeableConcept}</li>
         * <li>{@link Coding}</li>
         * <li>{@link ContactPoint}</li>
         * <li>{@link Count}</li>
         * <li>{@link Distance}</li>
         * <li>{@link Duration}</li>
         * <li>{@link HumanName}</li>
         * <li>{@link Identifier}</li>
         * <li>{@link Money}</li>
         * <li>{@link Period}</li>
         * <li>{@link Quantity}</li>
         * <li>{@link Range}</li>
         * <li>{@link Ratio}</li>
         * <li>{@link Reference}</li>
         * <li>{@link SampledData}</li>
         * <li>{@link Signature}</li>
         * <li>{@link Timing}</li>
         * <li>{@link ContactDetail}</li>
         * <li>{@link Contributor}</li>
         * <li>{@link DataRequirement}</li>
         * <li>{@link Expression}</li>
         * <li>{@link ParameterDefinition}</li>
         * <li>{@link RelatedArtifact}</li>
         * <li>{@link TriggerDefinition}</li>
         * <li>{@link UsageContext}</li>
         * <li>{@link Dosage}</li>
         * <li>{@link Meta}</li>
         * </ul>
         * 
         * @param value
         *     Value of extension
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Element value) {
            this.value = value;
            return this;
        }

        /**
         * Build the {@link Extension}
         * 
         * <p>Required elements:
         * <ul>
         * <li>url</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Extension}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Extension per the base specification
         */
        @Override
        public Extension build() {
            Extension extension = new Extension(this);
            if (validating) {
                validate(extension);
            }
            return extension;
        }

        protected void validate(Extension extension) {
            super.validate(extension);
            ValidationSupport.requireNonNull(extension.url, "url");
            ValidationSupport.choiceElement(extension.value, "value", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class);
            ValidationSupport.checkUri(extension.url);
            ValidationSupport.requireValueOrChildren(extension);
        }

        protected Builder from(Extension extension) {
            super.from(extension);
            url = extension.url;
            value = extension.value;
            return this;
        }
    }
}

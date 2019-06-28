/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Optional Extension Element - found in all resources.
 * </p>
 */
@Constraint(
    id = "ext-1",
    level = "Rule",
    location = "(base)",
    description = "Must have either extensions or value[x], not both",
    expression = "extension.exists() != value.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Extension extends Element {
    private final java.lang.String url;
    private final Element value;

    private Extension(Builder builder) {
        super(builder);
        url = ValidationSupport.requireNonNull(builder.url, "url");
        value = ValidationSupport.choiceElement(builder.value, "value", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class);
    }

    /**
     * <p>
     * Source of the definition for the extension code - a logical name or a URL.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link java.lang.String}.
     */
    public java.lang.String getUrl() {
        return url;
    }

    /**
     * <p>
     * Value of extension - must be one of a constrained set of the data types (see [Extensibility](extensibility.html) for a 
     * list).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getValue() {
        return value;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(url, "url", visitor);
                accept(value, "value", visitor, true);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(url);
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
    }

    public static Builder builder(java.lang.String url) {
        return new Builder(url);
    }

    public static class Builder extends Element.Builder {
        // required
        private final java.lang.String url;

        // optional
        private Element value;

        private Builder(java.lang.String url) {
            super();
            this.url = url;
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
         * Value of extension - must be one of a constrained set of the data types (see [Extensibility](extensibility.html) for a 
         * list).
         * </p>
         * 
         * @param value
         *     Value of extension
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder value(Element value) {
            this.value = value;
            return this;
        }

        @Override
        public Extension build() {
            return new Extension(this);
        }
    }
}

/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Specifies clinical/business/etc. metadata that can be used to retrieve, index and/or categorize an artifact. This 
 * metadata can either be specific to the applicable population (e.g., age category, DRG) or the specific context of care 
 * (e.g., venue, care setting, provider of care).
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class UsageContext extends Element {
    private final Coding code;
    private final Element value;

    private volatile int hashCode;

    private UsageContext(Builder builder) {
        super(builder);
        code = ValidationSupport.requireNonNull(builder.code, "code");
        value = ValidationSupport.requireChoiceElement(builder.value, "value", CodeableConcept.class, Quantity.class, Range.class, Reference.class);
        ValidationSupport.requireValueOrChildren(this);
    }

    /**
     * <p>
     * A code that identifies the type of context being specified by this usage context.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Coding}.
     */
    public Coding getCode() {
        return code;
    }

    /**
     * <p>
     * A value that defines the context specified in this context of use. The interpretation of the value is defined by the 
     * code.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getValue() {
        return value;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (code != null) || 
            (value != null);
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(code, "code", visitor);
                accept(value, "value", visitor);
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
        UsageContext other = (UsageContext) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(code, other.code) && 
            Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                code, 
                value);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(code, value).from(this);
    }

    public Builder toBuilder(Coding code, Element value) {
        return new Builder(code, value).from(this);
    }

    public static Builder builder(Coding code, Element value) {
        return new Builder(code, value);
    }

    public static class Builder extends Element.Builder {
        // required
        private final Coding code;
        private final Element value;

        private Builder(Coding code, Element value) {
            super();
            this.code = code;
            this.value = value;
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
         * Adds new element(s) to the existing list
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

        @Override
        public UsageContext build() {
            return new UsageContext(this);
        }

        private Builder from(UsageContext usageContext) {
            id = usageContext.id;
            extension.addAll(usageContext.extension);
            return this;
        }
    }
}

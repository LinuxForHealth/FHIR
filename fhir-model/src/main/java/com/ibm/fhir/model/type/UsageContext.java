/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Specifies clinical/business/etc. metadata that can be used to retrieve, index and/or categorize an artifact. This 
 * metadata can either be specific to the applicable population (e.g., age category, DRG) or the specific context of care 
 * (e.g., venue, care setting, provider of care).
 */
@Constraint(
    id = "usageContext-0",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/usage-context-type",
    expression = "code.exists() and code.memberOf('http://hl7.org/fhir/ValueSet/usage-context-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/UsageContext",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class UsageContext extends Element {
    @Summary
    @Binding(
        bindingName = "UsageContextType",
        strength = BindingStrength.Value.EXTENSIBLE,
        valueSet = "http://hl7.org/fhir/ValueSet/usage-context-type"
    )
    @Required
    private final Coding code;
    @Summary
    @ReferenceTarget({ "PlanDefinition", "ResearchStudy", "InsurancePlan", "HealthcareService", "Group", "Location", "Organization" })
    @Choice({ CodeableConcept.class, Quantity.class, Range.class, Reference.class })
    @Binding(
        bindingName = "UsageContextValue",
        strength = BindingStrength.Value.EXAMPLE,
        valueSet = "http://hl7.org/fhir/ValueSet/use-context"
    )
    @Required
    private final Element value;

    private UsageContext(Builder builder) {
        super(builder);
        code = builder.code;
        value = builder.value;
    }

    /**
     * A code that identifies the type of context being specified by this usage context.
     * 
     * @return
     *     An immutable object of type {@link Coding} that is non-null.
     */
    public Coding getCode() {
        return code;
    }

    /**
     * A value that defines the context specified in this context of use. The interpretation of the value is defined by the 
     * code.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}, {@link Quantity}, {@link Range} or {@link Reference} that is non-
     *     null.
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
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(code, "code", visitor);
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
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Element.Builder {
        private Coding code;
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
         * A code that identifies the type of context being specified by this usage context.
         * 
         * <p>This element is required.
         * 
         * @param code
         *     Type of context being specified
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(Coding code) {
            this.code = code;
            return this;
        }

        /**
         * A value that defines the context specified in this context of use. The interpretation of the value is defined by the 
         * code.
         * 
         * <p>This element is required.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link CodeableConcept}</li>
         * <li>{@link Quantity}</li>
         * <li>{@link Range}</li>
         * <li>{@link Reference}</li>
         * </ul>
         * 
         * When of type {@link Reference}, the allowed resource types for this reference are:
         * <ul>
         * <li>{@link PlanDefinition}</li>
         * <li>{@link ResearchStudy}</li>
         * <li>{@link InsurancePlan}</li>
         * <li>{@link HealthcareService}</li>
         * <li>{@link Group}</li>
         * <li>{@link Location}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param value
         *     Value that defines the context
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Element value) {
            this.value = value;
            return this;
        }

        /**
         * Build the {@link UsageContext}
         * 
         * <p>Required elements:
         * <ul>
         * <li>code</li>
         * <li>value</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link UsageContext}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid UsageContext per the base specification
         */
        @Override
        public UsageContext build() {
            UsageContext usageContext = new UsageContext(this);
            if (validating) {
                validate(usageContext);
            }
            return usageContext;
        }

        protected void validate(UsageContext usageContext) {
            super.validate(usageContext);
            ValidationSupport.requireNonNull(usageContext.code, "code");
            ValidationSupport.requireChoiceElement(usageContext.value, "value", CodeableConcept.class, Quantity.class, Range.class, Reference.class);
            ValidationSupport.checkReferenceType(usageContext.value, "value", "PlanDefinition", "ResearchStudy", "InsurancePlan", "HealthcareService", "Group", "Location", "Organization");
            ValidationSupport.requireValueOrChildren(usageContext);
        }

        protected Builder from(UsageContext usageContext) {
            super.from(usageContext);
            code = usageContext.code;
            value = usageContext.value;
            return this;
        }
    }
}

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
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.FHIRAllTypes;
import com.ibm.fhir.model.type.code.ParameterUse;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The parameters to the module. This collection specifies both the input and output parameters. Input parameters are 
 * provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ParameterDefinition extends Element {
    @Summary
    private final Code name;
    @Summary
    @Binding(
        bindingName = "ParameterUse",
        strength = BindingStrength.Value.REQUIRED,
        valueSet = "http://hl7.org/fhir/ValueSet/operation-parameter-use|4.3.0-CIBUILD"
    )
    @Required
    private final ParameterUse use;
    @Summary
    private final Integer min;
    @Summary
    private final String max;
    @Summary
    private final String documentation;
    @Summary
    @Binding(
        bindingName = "FHIRAllTypes",
        strength = BindingStrength.Value.REQUIRED,
        valueSet = "http://hl7.org/fhir/ValueSet/all-types|4.3.0-CIBUILD"
    )
    @Required
    private final FHIRAllTypes type;
    @Summary
    private final Canonical profile;

    private ParameterDefinition(Builder builder) {
        super(builder);
        name = builder.name;
        use = builder.use;
        min = builder.min;
        max = builder.max;
        documentation = builder.documentation;
        type = builder.type;
        profile = builder.profile;
    }

    /**
     * The name of the parameter used to allow access to the value of the parameter in evaluation contexts.
     * 
     * @return
     *     An immutable object of type {@link Code} that may be null.
     */
    public Code getName() {
        return name;
    }

    /**
     * Whether the parameter is input or output for the module.
     * 
     * @return
     *     An immutable object of type {@link ParameterUse} that is non-null.
     */
    public ParameterUse getUse() {
        return use;
    }

    /**
     * The minimum number of times this parameter SHALL appear in the request or response.
     * 
     * @return
     *     An immutable object of type {@link Integer} that may be null.
     */
    public Integer getMin() {
        return min;
    }

    /**
     * The maximum number of times this element is permitted to appear in the request or response.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getMax() {
        return max;
    }

    /**
     * A brief discussion of what the parameter is for and how it is used by the module.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDocumentation() {
        return documentation;
    }

    /**
     * The type of the parameter.
     * 
     * @return
     *     An immutable object of type {@link FHIRAllTypes} that is non-null.
     */
    public FHIRAllTypes getType() {
        return type;
    }

    /**
     * If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.
     * 
     * @return
     *     An immutable object of type {@link Canonical} that may be null.
     */
    public Canonical getProfile() {
        return profile;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (name != null) || 
            (use != null) || 
            (min != null) || 
            (max != null) || 
            (documentation != null) || 
            (type != null) || 
            (profile != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(name, "name", visitor);
                accept(use, "use", visitor);
                accept(min, "min", visitor);
                accept(max, "max", visitor);
                accept(documentation, "documentation", visitor);
                accept(type, "type", visitor);
                accept(profile, "profile", visitor);
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
        ParameterDefinition other = (ParameterDefinition) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(name, other.name) && 
            Objects.equals(use, other.use) && 
            Objects.equals(min, other.min) && 
            Objects.equals(max, other.max) && 
            Objects.equals(documentation, other.documentation) && 
            Objects.equals(type, other.type) && 
            Objects.equals(profile, other.profile);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                name, 
                use, 
                min, 
                max, 
                documentation, 
                type, 
                profile);
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
        private Code name;
        private ParameterUse use;
        private Integer min;
        private String max;
        private String documentation;
        private FHIRAllTypes type;
        private Canonical profile;

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
         * The name of the parameter used to allow access to the value of the parameter in evaluation contexts.
         * 
         * @param name
         *     Name used to access the parameter value
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(Code name) {
            this.name = name;
            return this;
        }

        /**
         * Whether the parameter is input or output for the module.
         * 
         * <p>This element is required.
         * 
         * @param use
         *     in | out
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder use(ParameterUse use) {
            this.use = use;
            return this;
        }

        /**
         * Convenience method for setting {@code min}.
         * 
         * @param min
         *     Minimum cardinality
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #min(com.ibm.fhir.model.type.Integer)
         */
        public Builder min(java.lang.Integer min) {
            this.min = (min == null) ? null : Integer.of(min);
            return this;
        }

        /**
         * The minimum number of times this parameter SHALL appear in the request or response.
         * 
         * @param min
         *     Minimum cardinality
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder min(Integer min) {
            this.min = min;
            return this;
        }

        /**
         * Convenience method for setting {@code max}.
         * 
         * @param max
         *     Maximum cardinality (a number of *)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #max(com.ibm.fhir.model.type.String)
         */
        public Builder max(java.lang.String max) {
            this.max = (max == null) ? null : String.of(max);
            return this;
        }

        /**
         * The maximum number of times this element is permitted to appear in the request or response.
         * 
         * @param max
         *     Maximum cardinality (a number of *)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder max(String max) {
            this.max = max;
            return this;
        }

        /**
         * Convenience method for setting {@code documentation}.
         * 
         * @param documentation
         *     A brief description of the parameter
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #documentation(com.ibm.fhir.model.type.String)
         */
        public Builder documentation(java.lang.String documentation) {
            this.documentation = (documentation == null) ? null : String.of(documentation);
            return this;
        }

        /**
         * A brief discussion of what the parameter is for and how it is used by the module.
         * 
         * @param documentation
         *     A brief description of the parameter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder documentation(String documentation) {
            this.documentation = documentation;
            return this;
        }

        /**
         * The type of the parameter.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     What type of value
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(FHIRAllTypes type) {
            this.type = type;
            return this;
        }

        /**
         * If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.
         * 
         * @param profile
         *     What profile the value is expected to be
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder profile(Canonical profile) {
            this.profile = profile;
            return this;
        }

        /**
         * Build the {@link ParameterDefinition}
         * 
         * <p>Required elements:
         * <ul>
         * <li>use</li>
         * <li>type</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ParameterDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ParameterDefinition per the base specification
         */
        @Override
        public ParameterDefinition build() {
            ParameterDefinition parameterDefinition = new ParameterDefinition(this);
            if (validating) {
                validate(parameterDefinition);
            }
            return parameterDefinition;
        }

        protected void validate(ParameterDefinition parameterDefinition) {
            super.validate(parameterDefinition);
            ValidationSupport.requireNonNull(parameterDefinition.use, "use");
            ValidationSupport.requireNonNull(parameterDefinition.type, "type");
            ValidationSupport.requireValueOrChildren(parameterDefinition);
        }

        protected Builder from(ParameterDefinition parameterDefinition) {
            super.from(parameterDefinition);
            name = parameterDefinition.name;
            use = parameterDefinition.use;
            min = parameterDefinition.min;
            max = parameterDefinition.max;
            documentation = parameterDefinition.documentation;
            type = parameterDefinition.type;
            profile = parameterDefinition.profile;
            return this;
        }
    }
}

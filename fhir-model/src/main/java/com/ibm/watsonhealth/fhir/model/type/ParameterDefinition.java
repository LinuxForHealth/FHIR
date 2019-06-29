/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.FHIRAllTypes;
import com.ibm.watsonhealth.fhir.model.type.ParameterUse;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The parameters to the module. This collection specifies both the input and output parameters. Input parameters are 
 * provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ParameterDefinition extends Element {
    private final Code name;
    private final ParameterUse use;
    private final Integer min;
    private final String max;
    private final String documentation;
    private final FHIRAllTypes type;
    private final Canonical profile;

    private ParameterDefinition(Builder builder) {
        super(builder);
        name = builder.name;
        use = ValidationSupport.requireNonNull(builder.use, "use");
        min = builder.min;
        max = builder.max;
        documentation = builder.documentation;
        type = ValidationSupport.requireNonNull(builder.type, "type");
        profile = builder.profile;
    }

    /**
     * <p>
     * The name of the parameter used to allow access to the value of the parameter in evaluation contexts.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Code}.
     */
    public Code getName() {
        return name;
    }

    /**
     * <p>
     * Whether the parameter is input or output for the module.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ParameterUse}.
     */
    public ParameterUse getUse() {
        return use;
    }

    /**
     * <p>
     * The minimum number of times this parameter SHALL appear in the request or response.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Integer}.
     */
    public Integer getMin() {
        return min;
    }

    /**
     * <p>
     * The maximum number of times this element is permitted to appear in the request or response.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getMax() {
        return max;
    }

    /**
     * <p>
     * A brief discussion of what the parameter is for and how it is used by the module.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getDocumentation() {
        return documentation;
    }

    /**
     * <p>
     * The type of the parameter.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link FHIRAllTypes}.
     */
    public FHIRAllTypes getType() {
        return type;
    }

    /**
     * <p>
     * If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Canonical}.
     */
    public Canonical getProfile() {
        return profile;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        return new Builder(use, type).from(this);
    }

    public Builder toBuilder(ParameterUse use, FHIRAllTypes type) {
        return new Builder(use, type).from(this);
    }

    public static Builder builder(ParameterUse use, FHIRAllTypes type) {
        return new Builder(use, type);
    }

    public static class Builder extends Element.Builder {
        // required
        private final ParameterUse use;
        private final FHIRAllTypes type;

        // optional
        private Code name;
        private Integer min;
        private String max;
        private String documentation;
        private Canonical profile;

        private Builder(ParameterUse use, FHIRAllTypes type) {
            super();
            this.use = use;
            this.type = type;
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
         * The name of the parameter used to allow access to the value of the parameter in evaluation contexts.
         * </p>
         * 
         * @param name
         *     Name used to access the parameter value
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder name(Code name) {
            this.name = name;
            return this;
        }

        /**
         * <p>
         * The minimum number of times this parameter SHALL appear in the request or response.
         * </p>
         * 
         * @param min
         *     Minimum cardinality
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder min(Integer min) {
            this.min = min;
            return this;
        }

        /**
         * <p>
         * The maximum number of times this element is permitted to appear in the request or response.
         * </p>
         * 
         * @param max
         *     Maximum cardinality (a number of *)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder max(String max) {
            this.max = max;
            return this;
        }

        /**
         * <p>
         * A brief discussion of what the parameter is for and how it is used by the module.
         * </p>
         * 
         * @param documentation
         *     A brief description of the parameter
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder documentation(String documentation) {
            this.documentation = documentation;
            return this;
        }

        /**
         * <p>
         * If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.
         * </p>
         * 
         * @param profile
         *     What profile the value is expected to be
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder profile(Canonical profile) {
            this.profile = profile;
            return this;
        }

        @Override
        public ParameterDefinition build() {
            return new ParameterDefinition(this);
        }

        private Builder from(ParameterDefinition parameterDefinition) {
            id = parameterDefinition.id;
            extension.addAll(parameterDefinition.extension);
            name = parameterDefinition.name;
            min = parameterDefinition.min;
            max = parameterDefinition.max;
            documentation = parameterDefinition.documentation;
            profile = parameterDefinition.profile;
            return this;
        }
    }
}

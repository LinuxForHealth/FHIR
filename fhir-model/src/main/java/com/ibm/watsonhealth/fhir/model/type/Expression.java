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
 * A expression that is evaluated in a specified context and returns a value. The context of use of the expression must 
 * specify the context in which the expression is evaluated, and how the result of the expression is used.
 * </p>
 */
@Constraint(
    id = "exp-1",
    level = "Rule",
    location = "(base)",
    description = "An expression or a reference must be provided",
    expression = "expression.exists() or reference.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Expression extends Element {
    private final String description;
    private final Id name;
    private final Code language;
    private final String expression;
    private final Uri reference;

    private Expression(Builder builder) {
        super(builder);
        description = builder.description;
        name = builder.name;
        language = ValidationSupport.requireNonNull(builder.language, "language");
        expression = builder.expression;
        reference = builder.reference;
    }

    /**
     * <p>
     * A brief, natural language description of the condition that effectively communicates the intended semantics.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>
     * A short name assigned to the expression to allow for multiple reuse of the expression in the context where it is 
     * defined.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Id}.
     */
    public Id getName() {
        return name;
    }

    /**
     * <p>
     * The media type of the language for the expression.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Code}.
     */
    public Code getLanguage() {
        return language;
    }

    /**
     * <p>
     * An expression in the specified language that returns a value.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getExpression() {
        return expression;
    }

    /**
     * <p>
     * A URI that defines where the expression is found.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getReference() {
        return reference;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(description, "description", visitor);
                accept(name, "name", visitor);
                accept(language, "language", visitor);
                accept(expression, "expression", visitor);
                accept(reference, "reference", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        return new Builder(language).from(this);
    }

    public Builder toBuilder(Code language) {
        return new Builder(language).from(this);
    }

    public static Builder builder(Code language) {
        return new Builder(language);
    }

    public static class Builder extends Element.Builder {
        // required
        private final Code language;

        // optional
        private String description;
        private Id name;
        private String expression;
        private Uri reference;

        private Builder(Code language) {
            super();
            this.language = language;
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
         * A brief, natural language description of the condition that effectively communicates the intended semantics.
         * </p>
         * 
         * @param description
         *     Natural language description of the condition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * <p>
         * A short name assigned to the expression to allow for multiple reuse of the expression in the context where it is 
         * defined.
         * </p>
         * 
         * @param name
         *     Short name assigned to expression for reuse
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder name(Id name) {
            this.name = name;
            return this;
        }

        /**
         * <p>
         * An expression in the specified language that returns a value.
         * </p>
         * 
         * @param expression
         *     Expression in specified language
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder expression(String expression) {
            this.expression = expression;
            return this;
        }

        /**
         * <p>
         * A URI that defines where the expression is found.
         * </p>
         * 
         * @param reference
         *     Where the expression is found
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reference(Uri reference) {
            this.reference = reference;
            return this;
        }

        @Override
        public Expression build() {
            return new Expression(this);
        }

        private Builder from(Expression expression) {
            id = expression.id;
            extension.addAll(expression.extension);
            description = expression.description;
            name = expression.name;
            this.expression = expression.expression;
            reference = expression.reference;
            return this;
        }
    }
}

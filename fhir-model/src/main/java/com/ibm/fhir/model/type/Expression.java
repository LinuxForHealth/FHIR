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
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A expression that is evaluated in a specified context and returns a value. The context of use of the expression must 
 * specify the context in which the expression is evaluated, and how the result of the expression is used.
 */
@Constraint(
    id = "exp-1",
    level = "Rule",
    location = "(base)",
    description = "An expression or a reference must be provided",
    expression = "expression.exists() or reference.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Expression"
)
@Constraint(
    id = "expression-2",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/expression-language",
    expression = "language.exists() and language.memberOf('http://hl7.org/fhir/ValueSet/expression-language', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Expression",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Expression extends Element {
    @Summary
    private final String description;
    @Summary
    private final Id name;
    @Summary
    @Binding(
        bindingName = "ExpressionLanguage",
        strength = BindingStrength.Value.EXTENSIBLE,
        valueSet = "http://hl7.org/fhir/ValueSet/expression-language",
        maxValueSet = "http://hl7.org/fhir/ValueSet/mimetypes"
    )
    @Required
    private final Code language;
    @Summary
    private final String expression;
    @Summary
    private final Uri reference;

    private Expression(Builder builder) {
        super(builder);
        description = builder.description;
        name = builder.name;
        language = builder.language;
        expression = builder.expression;
        reference = builder.reference;
    }

    /**
     * A brief, natural language description of the condition that effectively communicates the intended semantics.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDescription() {
        return description;
    }

    /**
     * A short name assigned to the expression to allow for multiple reuse of the expression in the context where it is 
     * defined.
     * 
     * @return
     *     An immutable object of type {@link Id} that may be null.
     */
    public Id getName() {
        return name;
    }

    /**
     * The media type of the language for the expression.
     * 
     * @return
     *     An immutable object of type {@link Code} that is non-null.
     */
    public Code getLanguage() {
        return language;
    }

    /**
     * An expression in the specified language that returns a value.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getExpression() {
        return expression;
    }

    /**
     * A URI that defines where the expression is found.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getReference() {
        return reference;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (description != null) || 
            (name != null) || 
            (language != null) || 
            (expression != null) || 
            (reference != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(description, "description", visitor);
                accept(name, "name", visitor);
                accept(language, "language", visitor);
                accept(expression, "expression", visitor);
                accept(reference, "reference", visitor);
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
        Expression other = (Expression) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(description, other.description) && 
            Objects.equals(name, other.name) && 
            Objects.equals(language, other.language) && 
            Objects.equals(expression, other.expression) && 
            Objects.equals(reference, other.reference);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                description, 
                name, 
                language, 
                expression, 
                reference);
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
        private String description;
        private Id name;
        private Code language;
        private String expression;
        private Uri reference;

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
         * Convenience method for setting {@code description}.
         * 
         * @param description
         *     Natural language description of the condition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #description(com.ibm.fhir.model.type.String)
         */
        public Builder description(java.lang.String description) {
            this.description = (description == null) ? null : String.of(description);
            return this;
        }

        /**
         * A brief, natural language description of the condition that effectively communicates the intended semantics.
         * 
         * @param description
         *     Natural language description of the condition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * A short name assigned to the expression to allow for multiple reuse of the expression in the context where it is 
         * defined.
         * 
         * @param name
         *     Short name assigned to expression for reuse
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(Id name) {
            this.name = name;
            return this;
        }

        /**
         * The media type of the language for the expression.
         * 
         * <p>This element is required.
         * 
         * @param language
         *     text/cql | text/fhirpath | application/x-fhir-query | text/cql-identifier | text/cql-expression | etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder language(Code language) {
            this.language = language;
            return this;
        }

        /**
         * Convenience method for setting {@code expression}.
         * 
         * @param expression
         *     Expression in specified language
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #expression(com.ibm.fhir.model.type.String)
         */
        public Builder expression(java.lang.String expression) {
            this.expression = (expression == null) ? null : String.of(expression);
            return this;
        }

        /**
         * An expression in the specified language that returns a value.
         * 
         * @param expression
         *     Expression in specified language
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder expression(String expression) {
            this.expression = expression;
            return this;
        }

        /**
         * A URI that defines where the expression is found.
         * 
         * @param reference
         *     Where the expression is found
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reference(Uri reference) {
            this.reference = reference;
            return this;
        }

        /**
         * Build the {@link Expression}
         * 
         * <p>Required elements:
         * <ul>
         * <li>language</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Expression}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Expression per the base specification
         */
        @Override
        public Expression build() {
            Expression expression = new Expression(this);
            if (validating) {
                validate(expression);
            }
            return expression;
        }

        protected void validate(Expression expression) {
            super.validate(expression);
            ValidationSupport.requireNonNull(expression.language, "language");
            ValidationSupport.requireValueOrChildren(expression);
        }

        protected Builder from(Expression expression) {
            super.from(expression);
            description = expression.description;
            name = expression.name;
            language = expression.language;
            this.expression = expression.expression;
            reference = expression.reference;
            return this;
        }
    }
}

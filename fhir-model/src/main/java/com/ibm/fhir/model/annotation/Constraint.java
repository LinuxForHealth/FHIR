/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

import com.ibm.fhir.model.constraint.spi.ConstraintValidator;
import com.ibm.fhir.model.type.ElementDefinition;
import com.ibm.fhir.model.visitor.Visitable;

/**
 * An annotation interface that represents a formal constraint (invariant)
 *
 * <p>Constraints can be computationally evaluated using a FHIRPath engine or an instance of {@link ConstraintValidator}.
 */
@Repeatable(Constraints.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Constraint {
    static final String LEVEL_RULE = "Rule";
    static final String LEVEL_WARNING = "Warning";
    static final String LOCATION_BASE = "(base)";
    static final String SOURCE_UNKNOWN = "(unknown)";

    /**
     * Identifies the constraint uniquely amongst all the constraints in the context
     *
     * @return
     *     the id
     * @see ElementDefinition.Constraint#getKey()
     */
    String id();

    /**
     * The severity of the constraint
     *
     * @return
     *     the level
     * @see ElementDefinition.Constraint#getSeverity()
     */
    String level();

    /**
     * The location (path) of the validation target {@link Element} or {@link Resource}
     *
     * @return
     *     the location
     * @see ElementDefinition#getPath()
     */
    String location();

    /**
     * A description used in messages identifying that the constraint has been violated
     *
     * @return
     *     the description
     * @see ElementDefinition.Constraint#getHuman()
     */
    String description();

    /**
     * A FHIRPath expression that must evaluate to true when run on the validation target {@link Element} or {@link Resource}
     *
     * @return
     *     the expression
     * @see ElementDefinition.Constraint#getExpression()
     */
    String expression();

    /**
     * The original source of the constraint
     *
     * @return
     *     the source
     * @see ElementDefinition.Constraint#getSource()
     */
    String source();

    /**
     * Indicates whether the constraint is checked in the model
     *
     * @return
     *     true if the constraint is checked in the model, false otherwise
     */
    boolean modelChecked() default false;

    /**
     * Indicates whether the constraint was generated
     *
     * @return
     *     true if the constraint was generated, false otherwise
     */
    boolean generated() default false;

    /**
     * The {@link ConstraintValidator} class used to validate this constraint
     *
     * @implNote
     *     The default value is {@link FHIRPathConstraintValidator} which is non-instantiable. Constraint providers may set this
     *     to a concrete implementation of the {@link ConstraintValidator} interface using {@link Constraint.Factory}.
     * @return
     *     the {@link ConstraintValidator} class used to validate this constraint
     */
    Class<? extends ConstraintValidator<?>> validatorClass() default FHIRPathConstraintValidator.class;

    /**
     * A marker interface used to indicate that a constraint should be evaluated using a FHIRPath engine
     */
    interface FHIRPathConstraintValidator extends ConstraintValidator<Visitable> { }

    /**
     * A factory class for programmatically creating Constraint instances using an anonymous inner class
     */
    final class Factory {
        private Factory() { }

        public static Constraint createConstraint(
                String id,
                String level,
                String location,
                String description,
                String expression,
                String source,
                boolean modelChecked,
                boolean generated) {
            return createConstraint(
                id,
                level,
                location,
                description,
                expression,
                source,
                modelChecked,
                generated,
                FHIRPathConstraintValidator.class);
        }

        public static Constraint createConstraint(
                String id,
                String level,
                String location,
                String description,
                String expression,
                String source,
                boolean modelChecked,
                boolean generated,
                Class<? extends ConstraintValidator<?>> validatorClass) {
            return new Constraint() {
                @Override
                public String id() {
                    return id;
                }

                @Override
                public String level() {
                    return level;
                }

                @Override
                public String location() {
                    return location;
                }

                @Override
                public String description() {
                    return description;
                }

                @Override
                public String expression() {
                    return expression;
                }

                @Override
                public String source() {
                    return source;
                }

                @Override
                public boolean modelChecked() {
                    return modelChecked;
                }

                @Override
                public boolean generated() {
                    return generated;
                }

                @Override
                public Class<? extends ConstraintValidator<?>> validatorClass() {
                    return validatorClass;
                }

                @Override
                public Class<? extends Annotation> annotationType() {
                    return Constraint.class;
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
                    Constraint other = (Constraint) obj;
                    return Objects.equals(id(), other.id()) &&
                        Objects.equals(level, other.level()) &&
                        Objects.equals(location, other.location()) &&
                        Objects.equals(description, other.description()) &&
                        Objects.equals(expression, other.expression()) &&
                        Objects.equals(source, other.source()) &&
                        Objects.equals(modelChecked, other.modelChecked()) &&
                        Objects.equals(generated, other.generated()) &&
                        Objects.equals(validatorClass, other.validatorClass());
                }

                @Override
                public int hashCode() {
                    return Objects.hash(
                        id,
                        level,
                        location,
                        description,
                        expression,
                        source,
                        modelChecked,
                        generated,
                        validatorClass);
                }

                @Override
                public String toString() {
                    return new StringBuilder()
                        .append("@com.ibm.fhir.model.annotation.Constraint(")
                        .append("id=\"").append(id).append("\", ")
                        .append("level=\"").append(level).append("\", ")
                        .append("location=\"").append(location).append("\", ")
                        .append("description=\"").append(description).append("\", ")
                        .append("expression=\"").append(expression).append("\", ")
                        .append("source=\"").append(source).append("\", ")
                        .append("modelChecked=").append(modelChecked).append(", ")
                        .append("generated=").append(generated).append(", ")
                        .append("validatorClass=").append(validatorClass)
                        .append(")")
                        .toString();
                }
            };
        }
    }
}

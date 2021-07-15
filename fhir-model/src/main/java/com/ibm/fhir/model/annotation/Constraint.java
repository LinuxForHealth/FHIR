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

@Repeatable(Constraints.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Constraint {
    static final String LEVEL_RULE = "Rule";
    static final String LEVEL_WARNING = "Warning";
    static final String LOCATION_BASE = "(base)";
    static final String SOURCE_UNKNOWN = "(unknown)";

    String id();
    String level();
    String location();
    String description();
    String expression();
    String source();
    boolean modelChecked() default false;
    boolean generated() default false;

    /**
     * A factory class for programmatically creating Constraint instances using an anonymous inner class
     */
    static class Factory {
        public static Constraint createConstraint(
                String id,
                String level,
                String location,
                String description,
                String expression,
                String source,
                boolean modelChecked,
                boolean generated) {
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
                        Objects.equals(level(), other.level()) &&
                        Objects.equals(location(), other.location()) &&
                        Objects.equals(description(), other.description()) &&
                        Objects.equals(expression(), other.expression()) &&
                        Objects.equals(source(), other.source()) &&
                        Objects.equals(modelChecked(), other.modelChecked()) &&
                        Objects.equals(generated(), other.generated()) &&
                        Objects.equals(annotationType(), other.annotationType());
                }

                @Override
                public int hashCode() {
                    return Objects.hash(
                        id(),
                        level(),
                        location(),
                        description(),
                        expression(),
                        source(),
                        modelChecked(),
                        generated(),
                        annotationType());
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
                        .append("source=\"").append(source).append("\"")
                        .append("modelChecked=").append(modelChecked).append(", ")
                        .append("generated=").append(generated)
                        .append(")")
                        .toString();
                }
            };
        }
    }
}

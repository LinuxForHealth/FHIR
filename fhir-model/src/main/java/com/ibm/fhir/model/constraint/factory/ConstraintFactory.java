/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.constraint.factory;

import java.lang.annotation.Annotation;
import java.util.Objects;

import com.ibm.fhir.model.annotation.Constraint;

/**
 * A factory class for programmatically creating Constraint instances using an anonymous inner class
 */
public final class ConstraintFactory {
    private ConstraintFactory() { }

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
                    Objects.equals(generated(), other.generated());
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
                    generated());
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
                    .append("generated=").append(generated)
                    .append(")")
                    .toString();
            }
        };
    }
}

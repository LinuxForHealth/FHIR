/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.constraint.spi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Predicate;

import com.ibm.fhir.model.annotation.Constraint;

public interface ConstraintProvider {
    List<Constraint> getConstraints();
    List<Predicate<Constraint>> getRemovalPredicates();
    List<Replacement> getReplacements();

    public static class Replacement {
        private final Predicate<Constraint> predicate;
        private final Constraint constraint;

        private Replacement(Predicate<Constraint> predicate, Constraint constraint) {
            this.predicate = Objects.requireNonNull(predicate, "predicate");
            this.constraint = Objects.requireNonNull(constraint, "constraint");
        }

        public Predicate<Constraint> getPredicate() {
            return predicate;
        }

        public Constraint getConstraint() {
            return constraint;
        }

        public static Replacement replacement(Predicate<Constraint> predicate, Constraint constraint) {
            return new Replacement(predicate, constraint);
        }
    }

    static <T extends ConstraintProvider> List<T> providers(Class<T> providerClass) {
        List<T> providers = new ArrayList<>();
        for (T provider : ServiceLoader.load(providerClass)) {
            providers.add(provider);
        }
        return Collections.unmodifiableList(providers);
    }
}

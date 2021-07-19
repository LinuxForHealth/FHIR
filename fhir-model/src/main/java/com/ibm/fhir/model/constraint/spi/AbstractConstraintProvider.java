/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.constraint.spi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import com.ibm.fhir.model.annotation.Constraint;

public abstract class AbstractConstraintProvider implements ConstraintProvider {
    private final List<Constraint> constraints;
    private final List<Predicate<Constraint>> removalPredicates;
    private final List<Replacement> replacements;

    public AbstractConstraintProvider() {
        constraints = buildConstraints();
        removalPredicates = buildRemovalPredicates();
        replacements = buildReplacements();
    }

    @Override
    public final List<Constraint> getConstraints() {
        return constraints;
    }

    @Override
    public final List<Predicate<Constraint>> getRemovalPredicates() {
        return removalPredicates;
    }

    @Override
    public final List<Replacement> getReplacements() {
        return replacements;
    }

    protected final List<Constraint> buildConstraints() {
        List<Constraint> constraints = new ArrayList<>();
        addConstraints(constraints);
        return Collections.unmodifiableList(constraints);
    }

    protected abstract void addConstraints(List<Constraint> constraints);

    protected final List<Predicate<Constraint>> buildRemovalPredicates() {
        List<Predicate<Constraint>> removalPredicates = new ArrayList<>();
        addRemovalPredicates(removalPredicates);
        return Collections.unmodifiableList(removalPredicates);
    }

    protected abstract void addRemovalPredicates(List<Predicate<Constraint>> removalPredicates);

    protected final List<Replacement> buildReplacements() {
        List<Replacement> replacements = new ArrayList<>();
        addReplacements(replacements);
        return Collections.unmodifiableList(replacements);
    }

    protected abstract void addReplacements(List<Replacement> replacements);

    protected Predicate<Constraint> idEquals(String id) {
        return constraint -> constraint.id().equals(id);
    }

    protected Predicate<Constraint> locationEquals(String location) {
        return constraint -> constraint.location().equals(location);
    }

    protected Predicate<Constraint> sourceEquals(String source) {
        return constraint -> constraint.source().equals(source);
    }
}
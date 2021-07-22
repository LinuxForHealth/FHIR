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

/**
 * An abstract base class that implements the {@link ConstraintProider} interface
 */
public abstract class AbstractConstraintProvider implements ConstraintProvider {
    private final List<Predicate<Constraint>> removalPredicates;
    private final List<Constraint> constraints;

    public AbstractConstraintProvider() {
        removalPredicates = buildRemovalPredicates();
        constraints = buildConstraints();
    }

    @Override
    public final List<Predicate<Constraint>> getRemovalPredicates() {
        return removalPredicates;
    }

    @Override
    public final List<Constraint> getConstraints() {
        return constraints;
    }

    /**
     * Add zero or more removal predicates to the given list of removal predicates.
     *
     * @param removalPredicates
     *     the list of removal predicates
     */
    protected abstract void addRemovalPredicates(List<Predicate<Constraint>> removalPredicates);

    /**
     * Add zero or more constraints to the given list of constraints.
     *
     * @param constraints
     *     the list of constraints
     */
    protected abstract void addConstraints(List<Constraint> constraints);

    /**
     * Create a predicate that matches on the given constraint id.
     *
     * @param id
     *     the id
     * @return
     *     a predicate that matches on the given constraint id
     * @see Constraint#id()
     */
    protected Predicate<Constraint> idEquals(String id) {
        return constraint -> constraint.id().equals(id);
    }

    /**
     * Create a predicate that matches on the given constraint location.
     *
     * @param location
     *     the location
     * @return
     *     a predicate that matches on the given constraint location
     * @see Constraint#location()
     */
    protected Predicate<Constraint> locationEquals(String location) {
        return constraint -> constraint.location().equals(location);
    }

    /**
     * Create a predicate that matches on the given constraint source.
     *
     * @param source
     *     the source
     * @return
     *     a predicate that matches on the given constraint source
     */
    protected Predicate<Constraint> sourceEquals(String source) {
        return constraint -> constraint.source().equals(source);
    }

    /**
     * Create a {@link Constraint} instance.
     *
     * @param id
     *     the id
     * @param level
     *     the level
     * @param location
     *     the location
     * @param description
     *     the description
     * @param validationClass
     *     the validation class
     * @return
     *     a {@link Constraint} instance
     */
    protected Constraint constraint(String id, String level, String location, String description, Class<? extends ConstraintValidator<?>> validationClass) {
        return Constraint.Factory.createConstraint(id, level, location, description, "", "", false, false, validationClass);
    }

    /**
     * Create a {@link Constraint} instance.
     *
     * @param id
     *     the id
     * @param level
     *     the level
     * @param location
     *     the location
     * @param description
     *     the description
     * @param expression
     *     the expression
     * @param source
     *     the source
     * @return
     *     a {@link Constraint} instance
     */
    protected Constraint constraint(String id, String level, String location, String description, String expression, String source) {
        return Constraint.Factory.createConstraint(id, level, location, description, expression, source, false, false);
    }

    /**
     * Create a {@link Constraint} instance.
     *
     * @param id
     *     the id
     * @param level
     *     the level
     * @param location
     *     the location
     * @param description
     *     the description
     * @param expression
     *     the expression
     * @param source
     *     the source
     * @param validatorClass
     *     the validator class
     * @return
     *     a {@link Constraint} instance
     */
    protected Constraint constraint(String id, String level, String location, String description, String expression, String source, Class<? extends ConstraintValidator<?>> validatorClass) {
        return Constraint.Factory.createConstraint(id, level, location, description, expression, source, false, false, validatorClass);
    }

    private List<Predicate<Constraint>> buildRemovalPredicates() {
        List<Predicate<Constraint>> removalPredicates = new ArrayList<>();
        addRemovalPredicates(removalPredicates);
        return Collections.unmodifiableList(removalPredicates);
    }

    private List<Constraint> buildConstraints() {
        List<Constraint> constraints = new ArrayList<>();
        addConstraints(constraints);
        return Collections.unmodifiableList(constraints);
    }
}

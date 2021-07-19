/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.constraint.spi;

import java.util.List;
import java.util.function.Predicate;

import com.ibm.fhir.model.annotation.Constraint;

public abstract class AbstractModelConstraintProvider extends AbstractConstraintProvider implements ModelConstraintProvider {
    @Override
    public abstract boolean appliesTo(Class<?> modelClass);

    @Override
    protected void addConstraints(List<Constraint> constraints) {
        // do nothing
    }

    @Override
    protected void addRemovalPredicates(List<Predicate<Constraint>> removalPredicates) {
        // do nothing
    }

    @Override
    protected void addReplacements(List<Replacement> replacements) {
        // do nothing
    }
}

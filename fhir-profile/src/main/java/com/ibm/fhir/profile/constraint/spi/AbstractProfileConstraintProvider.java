/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.profile.constraint.spi;

import java.util.List;
import java.util.function.Predicate;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.constraint.spi.AbstractConstraintProvider;

public abstract class AbstractProfileConstraintProvider extends AbstractConstraintProvider implements ProfileConstraintProvider {
    @Override
    public abstract boolean appliesTo(String url, String version);

    @Override
    protected void addRemovalPredicates(List<Predicate<Constraint>> removalPredicates) {
        // do nothing
    }

    @Override
    protected void addConstraints(List<Constraint> constraints) {
        // do nothing
    }
}

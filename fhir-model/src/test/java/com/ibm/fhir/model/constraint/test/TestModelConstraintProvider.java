/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.constraint.test;

import static com.ibm.fhir.model.constraint.spi.ConstraintProvider.Replacement.replacement;

import java.util.List;
import java.util.function.Predicate;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.constraint.spi.AbstractModelConstraintProvider;
import com.ibm.fhir.model.resource.Patient;

public class TestModelConstraintProvider extends AbstractModelConstraintProvider {
    @Override
    public boolean appliesTo(Class<?> modelClass) {
        return Patient.class.equals(modelClass);
    }

    @Override
    protected void addConstraints(List<Constraint> constraints) {
        constraints.add(Constraint.Factory.createConstraint(
            "added-pat-1",
            Constraint.LEVEL_RULE,
            Constraint.LOCATION_BASE,
            "description",
            "expression",
            "source",
            false,
            false));
    }

    @Override
    protected void addRemovalPredicates(List<Predicate<Constraint>> removalPredicates) {
        removalPredicates.add(idEquals("pat-1"));
    }

    @Override
    protected void addReplacements(List<Replacement> replacements) {
        replacements.add(replacement(idEquals("patient-4"), Constraint.Factory.createConstraint(
            "replaced-patient-4",
            Constraint.LEVEL_WARNING,
            Constraint.LOCATION_BASE,
            "description",
            "expession",
            "source",
            false,
            false)));
    }
}

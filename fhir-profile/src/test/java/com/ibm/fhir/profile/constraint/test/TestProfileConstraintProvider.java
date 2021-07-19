/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.profile.constraint.test;

import static com.ibm.fhir.model.constraint.spi.ConstraintProvider.Replacement.replacement;

import java.util.List;
import java.util.function.Predicate;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.profile.constraint.spi.AbstractProfileConstraintProvider;

public class TestProfileConstraintProvider extends AbstractProfileConstraintProvider {
    @Override
    public boolean appliesTo(String url, String version) {
        return "http://hl7.org/fhir/StructureDefinition/bp".equals(url);
    }

    @Override
    protected void addConstraints(List<Constraint> constraints) {
        constraints.add(Constraint.Factory.createConstraint(
            "added-vs-4",
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
        removalPredicates.add(idEquals("vs-1"));
    }

    @Override
    protected void addReplacements(List<Replacement> replacements) {
        replacements.add(replacement(idEquals("vs-2"), Constraint.Factory.createConstraint(
            "replaced-vs-2",
            Constraint.LEVEL_RULE,
            Constraint.LOCATION_BASE,
            "description",
            "expression",
            "source",
            false,
            false)));
    }
}

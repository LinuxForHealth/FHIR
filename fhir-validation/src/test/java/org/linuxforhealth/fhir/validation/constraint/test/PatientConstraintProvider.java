/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.validation.constraint.test;

import java.util.List;

import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.constraint.spi.AbstractModelConstraintProvider;
import org.linuxforhealth.fhir.model.resource.Patient;

public class PatientConstraintProvider extends AbstractModelConstraintProvider {
    @Override
    public boolean appliesTo(Class<?> modelClass) {
        return Patient.class.equals(modelClass);
    }

    @Override
    protected void addConstraints(List<Constraint> constraints) {
        constraints.add(constraint(
            "patient-name-1",
            Constraint.LEVEL_WARNING,
            Constraint.LOCATION_BASE,
            "If Patient.name exists, then Patient.name.family SHOULD exist",
            PatientConstraintValidator.class));
    }
}

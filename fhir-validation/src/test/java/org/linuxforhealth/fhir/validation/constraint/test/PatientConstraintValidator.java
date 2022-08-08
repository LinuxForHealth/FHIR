/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.validation.constraint.test;

import java.util.logging.Logger;

import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.constraint.spi.ConstraintValidator;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.type.HumanName;

public class PatientConstraintValidator implements ConstraintValidator<Patient> {
    private static final Logger log = Logger.getLogger(PatientConstraintValidator.class.getName());

    public PatientConstraintValidator() {
        super();
    }

    public boolean appliesTo(Class<?> modelClass) {
        return Patient.class.equals(modelClass);
    }

    @Override
    public boolean isValid(Patient patient, Constraint constraint) {
        log.fine("PatientConstraintValidator.isValid(Patient, Constraint) method");
        for (HumanName name : patient.getName()) {
            if (name.getFamily() == null) {
                return false;
            }
        }
        return true;
    }
}

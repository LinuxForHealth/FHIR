/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.constraint.test;

import java.util.logging.Logger;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.constraint.spi.ConstraintValidator;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.HumanName;

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

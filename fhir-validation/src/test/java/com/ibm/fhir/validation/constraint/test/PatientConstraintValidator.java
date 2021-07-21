/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.constraint.test;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.constraint.spi.ConstraintValidator;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.HumanName;

public class PatientConstraintValidator implements ConstraintValidator {
    public boolean appliesTo(Class<?> modelClass) {
        return Patient.class.equals(modelClass);
    }

    @Override
    public boolean isValid(Resource resource, Constraint constraint) {
        Patient patient = (Patient) resource;
        for (HumanName name : patient.getName()) {
            if (name.getFamily() == null) {
                return false;
            }
        }
        return true;
    }
}

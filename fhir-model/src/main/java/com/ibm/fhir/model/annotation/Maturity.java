/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.model.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.ibm.fhir.model.type.code.StandardsStatus;

/**
 * @see <a href="https://confluence.hl7.org/display/FHIR/FHIR+Maturity+Model">https://confluence.hl7.org/display/FHIR/FHIR+Maturity+Model</a>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Maturity {
    int level();
    StandardsStatus.Value status();
}

/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.model.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @see <a href="https://confluence.hl7.org/display/FHIR/FHIR+Maturity+Model">https://confluence.hl7.org/display/FHIR/FHIR+Maturity+Model</a>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MaturityLevel {
    int value();
}

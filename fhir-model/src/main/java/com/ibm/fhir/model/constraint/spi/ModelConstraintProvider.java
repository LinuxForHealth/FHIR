/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.constraint.spi;

public interface ModelConstraintProvider extends ConstraintProvider {
    boolean appliesTo(Class<?> modelClass);
}

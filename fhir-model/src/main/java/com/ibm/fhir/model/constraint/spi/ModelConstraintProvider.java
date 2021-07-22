/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.constraint.spi;

/**
 * An interface that extends {@link ConstraintProvider} with a method that determines whether this constraint provider applies to a specific model class
 */
public interface ModelConstraintProvider extends ConstraintProvider {
    /**
     * Indicates whether this constraint provider applies to the given model class
     *
     * @param modelClass
     *     the model class
     * @return
     *     true if this constraint provider applies to the given model class, false otherwise
     */
    boolean appliesTo(Class<?> modelClass);
}

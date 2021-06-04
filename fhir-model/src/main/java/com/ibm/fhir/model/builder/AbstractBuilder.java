/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.builder;

public abstract class AbstractBuilder<T> implements Builder<T> {
    protected boolean validating = true;

    /**
     * Set the validating builder indicator for this builder
     *
     * <p>A validating builder may perform basic validation during object construction (e.g. cardinality checking, type checking, etc.)
     *
     * @param validating
     *     the validating builder indicator
     */
    public void setValidating(boolean validating) {
        this.validating = validating;
    }

    /**
     * Indicates whether this builder is a validating builder
     *
     * @return
     *     true if this builder is a validating builder, false otherwise
     */
    public boolean isValidating() {
        return validating;
    }

    @Override
    public abstract T build();
}

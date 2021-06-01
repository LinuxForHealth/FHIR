/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.builder;

public abstract class AbstractBuilder<T> implements Builder<T> {
    protected boolean validating = true;

    public void setValidating(boolean validating) {
        this.validating = validating;
    }

    public boolean isValidating() {
        return validating;
    }

    @Override
    public abstract T build();
}
